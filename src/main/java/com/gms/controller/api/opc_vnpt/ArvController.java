package com.gms.controller.api.opc_vnpt;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.InsuranceEnum;
import com.gms.entity.constant.JobEnum;
import com.gms.entity.constant.MedicationAdherenceEnum;
import com.gms.entity.constant.OpcDataTypeEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.constant.VirusLoadResultEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteVnptFkEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.validate.OpcArvVNPTValidate;
import com.gms.entity.validate.OpcStageVNPTValidate;
import com.gms.entity.validate.OpcVisitVNPTValidate;
import com.gms.entity.xml.vpnt_arv.BenhAn;
import com.gms.entity.xml.vpnt_arv.BenhNhan;
import com.gms.entity.xml.vpnt_arv.DSBenhAn;
import com.gms.entity.xml.vpnt_arv.DSBenhNhan;
import com.gms.entity.xml.vpnt_arv.DSKetQuaCls;
import com.gms.entity.xml.vpnt_arv.DSLanKham;
import com.gms.entity.xml.vpnt_arv.DotDieuTri;
import com.gms.entity.xml.vpnt_arv.KetQuaCls;
import com.gms.entity.xml.vpnt_arv.KhamHIV;
import com.gms.entity.xml.vpnt_arv.LanKham;
import com.gms.entity.xml.vpnt_arv.Main;
import com.gms.entity.xml.vpnt_arv.XuTri;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.OpcVisitService;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXB;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArvController extends BaseController {

    private HashMap<String, HashMap<String, String>> options;
    private SiteVnptFkEntity siteFk;
    private Main xmlMain;
    private HashMap<String, HashMap<String, String>> errors;
    public static final String FORMATDATE = "dd/MM/yyyy";
    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-.\\/]+[^-]$)";

    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcVisitService visitService;
    @Autowired
    private OpcViralLoadService viralLoadService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private OpcStageService stageService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private OpcArvVNPTValidate arvVNPTValidate;
    @Autowired
    private OpcStageVNPTValidate opcStageVNPTValidate;
    @Autowired
    private OpcVisitVNPTValidate opcVisitVNPTValidate;

    @RequestMapping(value = "/v1/vnpt-arv.api", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum,
            @RequestBody String xmlContent) throws CloneNotSupportedException {
        String apiType = "/v1/vnpt-arv.api";
        getOptions();
        xmlMain = JAXB.unmarshal(new StringReader(xmlContent), Main.class);
        errors = new HashMap<>();
        int isStages = 0;
        int isVisits = 0;
        try {
            validate();

            //Fake data test
//            Random rand = new Random();
//            xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0).setSo_hsba("VVTHANH" + (rand.nextInt((99999 - 1000) + 1) + 1000));
//            Kiểm tra checksum bảo mật
            String arvCode = xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0).getSo_hsba();
            if (!checksum(checksum, xmlMain.getMa_cskcb(), arvCode)) {
                Response<String> response = new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat}/{ma_cskcb}/{ma_benh_an})");
                arvService.logAPIError(apiType, checksum, response.getMessage(), response.getData(), xmlMain.getMa_cskcb());
                return response;
            }

            //Lấy thông tin cơ sở
            siteFk = siteService.findByVnptSiteID(xmlMain.getMa_cskcb());
            if (siteFk == null) {
                Response<String> response = new Response<>(false, "SITE_MAP", String.format("CSĐT %s chưa khai báo trên HIS-IMS", xmlMain.getMa_cskcb()));
                arvService.logAPIError(apiType, checksum, response.getMessage(), response.getData(), xmlMain.getMa_cskcb());
                return response;
            }
            if (!siteFk.isActive()) {
                Response<String> response = new Response<>(false, "SITE_LOCKED", String.format("CSĐT %s tạm khoá đồng bộ trên HIS-IMS", xmlMain.getMa_cskcb()));
                arvService.logAPIError(apiType, checksum, response.getMessage(), response.getData(), xmlMain.getMa_cskcb());
                return response;
            }
            OpcArvEntity model = arvService.findBySiteIDAndCode(siteFk.getID(), arvCode);

            //Xử lý bệnh án 
            OpcArvEntity arv = getArv(model);
            boolean isPrep = false;
            arv.setPatient(getPatient());
            if (arv.getFirstTreatmentTime() != null && (arv.getPatient().getConfirmTime() == null || arv.getPatient().getConfirmTime().compareTo(arv.getFirstTreatmentTime()) > 0)) {
                arv.getPatient().setConfirmTime(arv.getFirstTreatmentTime());
            }

            if (arv.getCode().substring(arv.getCode().length() - 1).equals("P") || arv.getCode().substring(arv.getCode().length() - 1).equals("p")) {
                isPrep = true;
                arv.setRemove(true);
            }
            System.out.println("Bản ghi PREP " + isPrep);
            isPrep = true;

            if (!arvVNPTValidate.validate(arv, isPrep).isEmpty()) {
                errors.put("arv-error", new HashMap<String, String>());
                errors.get("arv-error").putAll(arvVNPTValidate.validate(arv, isPrep));
            }
            //Lưu thông tin
            errors.put("status", new HashMap<String, String>());
            if (errors.get("arv-error") == null || errors.get("arv-error").isEmpty()) {
                if (model != null) {
                    arv.setPatientID(model.getPatientID());
                    arvService.updateVNPT(arv);
                    arvService.logAPI(apiType, checksum, true, "OK_UPDATE", "Cập nhật thành công", arv.getID(), arvCode, siteFk.getID(), xmlMain.getMa_cskcb());
                } else {
                    arvService.createVNPT(arv);
                    arvService.logAPI(apiType, checksum, true, "OK_INSERT", "Thêm mới thành công", arv.getID(), arvCode, siteFk.getID(), xmlMain.getMa_cskcb());
                }

                errors.get("status").put("Bệnh án", model != null ? "Cập nhật thành công" : "Thêm mới thành công");

                OpcArvEntity arvNew = arvService.findBySiteIDAndCode(siteFk.getID(), arvCode);

                List<OpcStageEntity> stages = getStage();
                int indexStage = -1;
                int indexVisit = -1;
                for (OpcStageEntity stage : stages) {
                    indexStage++;
                    //Xử lý giai đoạn điều trị
                    OpcStageEntity stageFind = stageService.findByArvIDAndDataIDIDAndSite(arvNew.getID(), stage.getDataID(), siteFk.getID());
                    stage.setArvID(arvNew.getID());
                    stage.setPatientID(arvNew.getPatientID());
                    stage.setSiteID(arvNew.getSiteID());

                    if (stageFind != null) {
                        stage.setID(stageFind.getID());
                    }
                    if (!stageService.validateStage(stage) && !isPrep) {
                        errors.get("status").put("Đợt điều trị mã: " + stage.getDataID(), "Thất bại, thời gian nhập của giai đoạn điều trị không hợp lệ hoặc ngày kết thúc của đợt điều trị nhập trong quá khứ đang trống");
                    } else {
                        if (!opcStageVNPTValidate.validate(stage, arvNew.getPatient().getConfirmTime(), isPrep).isEmpty()) {
                            errors.put("stage-error-code: " + stage.getDataID(), new HashMap<String, String>());
                            errors.get("stage-error-code: " + stage.getDataID()).putAll(opcStageVNPTValidate.validate(stage, arvNew.getPatient().getConfirmTime(), isPrep));
                        }
                        if (errors.get("stage-error-code: " + stage.getDataID()) == null || errors.get("stage-error-code: " + stage.getDataID()).isEmpty()) {
                            stageService.saveVNPT(stage);
                            errors.get("status").put("Đợt điều trị mã:" + stage.getDataID(), stageFind != null ? "Cập nhật thành công" : "Thêm mới thành công");

                            //code phản hồi htc
                            try {
                                HtcSearch search = new HtcSearch();
                                search.setRemove(false);
                                search.setReceiveStatus(null);
                                search.setPageIndex(1);
                                search.setPageSize(999999);

                                DataPage<HtcVisitEntity> dataPage = htcVisitService.findReceiveHtcConnect(search);

                                OpcArvEntity arvhtc = arvNew.clone();
                                for (HtcVisitEntity e : dataPage.getData()) {
                                    if (StringUtils.isEmpty(arvhtc.getPatient().getIdentityCard())) {
                                        break;
                                    }
                                    if (StringUtils.isEmpty(e.getPatientID())) {
                                        continue;
                                    }
                                    if (e.getPatientID().equals(arvhtc.getPatient().getIdentityCard())) {
                                        e.setArrivalTime(arvhtc.getTranferToTime() == null ? new Date() : arvhtc.getTranferToTime());//ngày tiếp nhận
                                        e.setFeedbackReceiveTime(arvhtc.getFeedbackResultsReceivedTime() == null ? new Date() : arvhtc.getFeedbackResultsReceivedTime());//ngày phản hồi
                                        e.setRegisterTherapyTime(arvhtc.getRegistrationTime());
                                        //cơ sở điều trị
                                        e.setArrivalSite(getCurrentUser().getSite().getName());
                                        //
                                        e.setTherapyNo(arvhtc.getCode());
                                        e.setTherapyExchangeStatus("2");

                                        htcVisitService.save(e);

                                        arvhtc.setTranferToTime(arvhtc.getTranferToTime() == null ? new Date() : arvhtc.getTranferToTime());
                                        arvhtc.setFeedbackResultsReceivedTime(arvhtc.getFeedbackResultsReceivedTime() == null ? new Date() : arvhtc.getFeedbackResultsReceivedTime());//ngày phản hồi

                                        arvService.save(arvhtc);
                                    }

                                }

                            } catch (Exception e) {
                            }

                            String madotdieu = stage.getDataID();

                            //Xử lý lượt khám
                            OpcStageEntity stageOld = stageService.findByArvIDAndDataIDIDAndSite(arvNew.getID(), madotdieu, siteFk.getID());

                            List<OpcVisitEntity> visits = getVisits();
                            if (!(visits == null || visits.isEmpty())) {
                                for (OpcVisitEntity visit : visits) {
                                    if (!visit.getDataStageID().equals(madotdieu)) {
                                        continue;
                                    }
                                    indexVisit++;
                                    OpcVisitEntity currentVisit = visitService.findByArvIDAndStageIDAndDataID(arvNew.getID(), stageOld.getID(), visit.getDataID());
                                    visit.setArvID(arvNew.getID());
                                    visit.setPatientID(arvNew.getPatientID());
                                    visit.setSiteID(arvNew.getSiteID());
                                    visit.setStageID(stageOld.getID());
                                    visit.setID(currentVisit != null ? currentVisit.getID() : null);
                                    if (visit.getInsuranceNo() == null || StringUtils.isEmpty(visit.getInsuranceNo())) {
                                        visit.setInsurance("0");
                                    } else {
                                        visit.setInsurance("1");
                                    }
                                    if (currentVisit != null && currentVisit.getTreatmentRegimenStage() != null && StringUtils.isEmpty(visit.getTreatmentRegimenStage())) {
                                        visit.setTreatmentRegimenStage(currentVisit.getTreatmentRegimenStage());
                                    }

                                    if (!opcVisitVNPTValidate.validate(visit, stageOld.getTreatmentTime(), stageOld.getRegistrationTime(), stageOld.getEndTime(), stageOld.getStatusOfTreatmentID(), isPrep).isEmpty()) {
                                        errors.put("visit-error-code " + visit.getDataID(), new HashMap<String, String>());
                                        errors.get("visit-error-code " + visit.getDataID()).putAll(opcVisitVNPTValidate.validate(visit, stageOld.getTreatmentTime(), stageOld.getRegistrationTime(), stageOld.getEndTime(), stageOld.getStatusOfTreatmentID(), isPrep));
                                    }
                                    if (errors.get("visit-error-code " + visit.getDataID()) == null || errors.get("visit-error-code " + visit.getDataID()).isEmpty()) {
                                        visitService.saveVNPT(visit);
                                        errors.get("status").put("Lần khám mã: " + visit.getDataID(), currentVisit != null ? "Cập nhật thành công" : "Thêm mới thành công");

                                        List<OpcViralLoadEntity> virals = getViralLoad();
                                        if (virals != null && !virals.isEmpty()) {

                                            for (OpcViralLoadEntity viral : virals) {
                                                if (!viral.getDataVisitID().equals(visit.getDataID())) {
                                                    continue;
                                                }
                                                OpcViralLoadEntity currentViral = viralLoadService.findByArvIDAndStageIDAndDataID(arvNew.getID(), stage.getID(), viral.getDataID());
                                                viral.setArvID(arvNew.getID());
                                                viral.setPatientID(arvNew.getPatientID());
                                                viral.setSiteID(arvNew.getSiteID());
                                                viral.setTestSiteID(arvNew.getSiteID());
                                                viral.setStageID(stage.getID());
                                                viral.setID(currentViral != null ? currentViral.getID() : null);

                                                if (viral.getResultNumber() != null && viral.getResultNumber().length() > 45) {
                                                    errors.put("viral-error-code " + viral.getDataID(), new HashMap<String, String>());
                                                    errors.get("viral-error-code " + viral.getDataID()).put("viral.resultNumber", "Kết quả TLVR nhập không quá 45 ký tự");
                                                }

                                                if (errors.get("viral-error " + viral.getDataID()) == null || errors.get("viral-error " + viral.getDataID()).isEmpty()) {
                                                    viralLoadService.saveVNPT(viral);
                                                    errors.get("status").put("Lượt XN TLVR mã: " + viral.getDataID(), currentViral != null ? "Cập nhật thành công" : "Thêm mới thành công");

                                                } else {
                                                    errors.get("status").put("Lượt XN TLVR mã: " + viral.getDataID(), currentViral != null ? "Cập nhật không thành công" : "Thêm mới không thành công");
                                                }
                                            }

                                        } else {
                                            errors.get("status").put("Lượt XN TLVR", "Không tìm thấy lượt xét nghiệm");
                                        }

                                    } else {
                                        errors.get("status").put("Lần khám " + visit.getDataID(), currentVisit != null ? "Cập nhật không thành công" : "Thêm mới không thành công");
                                        isVisits = isVisits + 1;
                                    }
                                }

                            } else {
                                errors.get("status").put("Lần khám " + indexVisit, "Không tìm thấy lần khám");
                            }

                            //Xử lý lượt xn TLVR
                        } else {
                            errors.get("status").put("Đợt điều trị mã: " + stage.getDataID(), stageFind != null ? "Cập nhật không thành công" : "Thêm mới không thành công");
                            isStages = isStages + 1;
                        }
                    }

                }

            } else {
                errors.get("status").put("Bệnh án", model != null ? "Cập nhật không thành công" : "Thêm mới không thành công");
                arvService.logAPI(apiType, checksum, false, "EXCEPTION", model != null ? "Cập nhật bệnh án thất bại" : "Thêm bệnh án thất bại", arv.getID(), arvCode, siteFk.getID(), xmlMain.getMa_cskcb());
            }

            if ((errors.get("arv-error") == null || errors.get("arv-error").isEmpty()) && (errors.get("viral-error") == null || errors.get("viral-error").isEmpty()) && isStages < 1 && isVisits < 1) {
                Response<HashMap<String, HashMap<String, String>>> response = new Response<>(true, model != null ? "OK_UPDATE" : "OK_INSERT", errors);
                return response;
            }
            Response<HashMap<String, HashMap<String, String>>> response = new Response<>(false, "EXCEPTION_VALIDATE", errors);
            arvService.logAPI(apiType, checksum, false, "EXCEPTION_VALIDATE", response.getData(), arv.getID(), arvCode, siteFk.getID(), xmlMain.getMa_cskcb());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            OpcArvEntity model = arvService.findBySiteIDAndCode(siteFk.getID(), xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0).getSo_hsba());
            OpcArvEntity arv = getArv(model);

            if (model != null) {
                arv.setID(model.getID());
            }
            if (errors != null) {
                Response<HashMap<String, HashMap<String, String>>> response = new Response<>(false, "EXCEPTION_VALIDATE", errors);
                arvService.logAPI(apiType, checksum, false, "EXCEPTION_VALIDATE", response.getData(), arv.getID(), xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0).getSo_hsba(), siteFk.getID(), xmlMain.getMa_cskcb());
                return response;
            } else {
                Response<String> response = new Response<>(false, "EXCEPTION", e.getMessage());
                arvService.logAPIError(apiType, checksum, response.getMessage(), response.getData(), xmlMain.getMa_cskcb());
                arvService.logAPI(apiType, checksum, false, "EXCEPTION_VALIDATE", response.getData(), arv.getID(), xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0).getSo_hsba(), siteFk.getID(), xmlMain.getMa_cskcb());
                getLogger().error(response.getMessage());
                return response;
            }

        }
    }

    private List<OpcStageEntity> getStage() {

        List<OpcStageEntity> stage = new ArrayList<>();
        Date currentDate = new Date();
        List<DotDieuTri> dsDotDieuTri;
        BenhAn benhAn;
        try {
            dsDotDieuTri = xmlMain.getDu_lieu_cap_nhat().getDs_dot_dieu_tri().getDot_dieu_tri();
            benhAn = xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0);
        } catch (Exception e) {
            return stage;
        }

        DSLanKham ds_lan_kham = xmlMain.getDu_lieu_cap_nhat().getDs_lan_kham();
        LanKham lan_kham = ds_lan_kham == null ? new LanKham() : ds_lan_kham.getLan_kham().get(0);
        XuTri xu_tri = new XuTri();
        try {
            xu_tri = xmlMain.getDu_lieu_cap_nhat().getDs_xu_tri().getXu_tri().get(0);
        } catch (Exception e) {
        }
        OpcStageEntity stage1;
        for (DotDieuTri dotDieuTri1 : dsDotDieuTri) {
            stage1 = new OpcStageEntity();
            //Thông tin chung stage
            stage1.setCreateAT(currentDate);
            stage1.setUpdateAt(currentDate);
            stage1.setCreatedBY(Long.valueOf("0"));
            stage1.setUpdatedBY(Long.valueOf("0"));
            stage1.setDataID(dotDieuTri1.getMa_dot_dieu_tri());
            stage1.setRegistrationType(getLyDoDangKy(dotDieuTri1.getLy_do_dang_ky())); // Loại đăng ký
            stage1.setEndTime(getDateTime(dotDieuTri1.getNgay_ket_thuc()));
            stage1.setEndCase(getLyDoKetThuc(dotDieuTri1.getLy_do_ket_thuc()));
            stage1.setTransferSiteID(StringUtils.isNotEmpty(dotDieuTri1.getMa_noi_chuyen_di()) ? Long.valueOf("-1") : Long.valueOf("0"));
            stage1.setTransferSiteName(StringUtils.isNotEmpty(dotDieuTri1.getMa_noi_chuyen_di()) ? dotDieuTri1.getTen_noi_chuyen_di() : "");
            stage1.setStatusOfTreatmentID(getTrangThaiDieuTri(stage1.getEndCase(), stage1.getEndTime()));

            stage1.setTranferFromTime(StringUtils.isNotEmpty(dotDieuTri1.getMa_noi_chuyen_di()) ? getDateTime(dotDieuTri1.getNgay_ket_thuc()) : null);
            stage1.setClinicalStage(lan_kham.getKham_hiv() == null ? null : lan_kham.getKham_hiv().getGd_lam_sang_hiv());

            //Phác đồ điều trị đầu tiên
            //Bậc phác đồ điều trị - sử dụng enum
            stage1.setTreatmentRegimenStage(xu_tri.getBac_phac_do());
            stage1.setTreatmentRegimenID(getPhacDoDieuTri(xu_tri.getTen_phac_do()));

            if (stage1.getEndTime() != null && !StringUtils.isEmpty(stage1.getEndCase())) {
                stage1.setTreatmentStageTime(stage1.getEndTime());
                if (stage1.getEndCase().equals("3")) {
                    stage1.setTreatmentStageID("3");
                } else if (stage1.getEndCase().equals("4")) {
                    stage1.setTreatmentStageID("5");
                } else if (stage1.getEndCase().equals("1")) {
                    stage1.setTreatmentStageID("6");
                } else if (stage1.getEndCase().equals("2")) {
                    stage1.setTreatmentStageID("7");
                } else if (stage1.getEndCase().equals("5")) {
                    stage1.setTreatmentStageID("8");
                }

            } else {
                stage1.setTreatmentStageTime(stage1.getRegistrationTime());
                if (stage1.getRegistrationType().equals("4") || stage1.getRegistrationType().equals("5")) {
                    stage1.setTreatmentStageID("1");
                } else if (stage1.getRegistrationType().equals("1")) {
                    stage1.setTreatmentStageID("1");
                } else if (stage1.getRegistrationType().equals("2")) {
                    stage1.setTreatmentStageID("3");
                } else if (stage1.getRegistrationType().equals("3")) {
                    stage1.setTreatmentStageID("4");
                } else {
                    stage1.setTreatmentStageID("1");
                }

            }
            if (stage1.getRegistrationType().equals(RegistrationTypeEnum.TRANSFER.getKey())) {
                stage1.setSourceSiteID(Long.valueOf("-1"));
                stage1.setSourceSiteName(dotDieuTri1.getTen_noi_chuyen_den());
            } else {
                stage1.setSourceSiteID(Long.valueOf("0"));
                stage1.setSourceSiteName("");
            }

            stage1.setRegistrationTime(getDateTime(dotDieuTri1.getNgay_dang_ky()) == null ? new Date() : getDateTime(dotDieuTri1.getNgay_dang_ky()));
            stage1.setRegistrationType(getLyDoDangKy(dotDieuTri1.getLy_do_dang_ky())); // Loại đăng ký
            stage1.setTreatmentTime(stage1.getRegistrationTime());

            if (stage1.getEndTime() != null && stage1.getEndCase() != null && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(stage1.getEndCase())) {
                stage1.setTranferFromTime(stage1.getEndTime());
                stage1.setTransferSiteID(Long.valueOf("-1"));
                stage1.setTransferSiteName(dotDieuTri1.getTen_noi_chuyen_di() == null ? "" : dotDieuTri1.getTen_noi_chuyen_di());
            }

            stage.add(stage1);
        }

        return stage;
    }

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);

        this.options = new HashMap<>();
        for (ParameterEntity item : parameterService.findByTypes(parameterTypes)) {
            if (options.get(item.getType()) == null) {
                options.put(item.getType(), new LinkedHashMap<String, String>());
            }

            if (item.getType().equals(ParameterEntity.RACE)) {
                //Dân tộc lấy theo tên
                options.get(item.getType()).put(TextUtils.removeDiacritical(item.getValue()).toLowerCase(), item.getCode());
            } else if (item.getType().equals(ParameterEntity.TREATMENT_REGIMEN)) {
                //Phác đồ điều trị
                options.get(ParameterEntity.TREATMENT_REGIMEN).put(item.getCode(), item.getValue());
            } else {
                options.get(item.getType()).put(item.getCode(), item.getValue());
            }
        }

        String key = "province";
        options.put(key, new LinkedHashMap<String, String>());
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            options.get(key).put(TextUtils.removeDiacritical(item.getName()).toLowerCase(), item.getID());
        }

        key = "district";
        options.put(key, new LinkedHashMap<String, String>());
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            options.get(key).put(TextUtils.removeDiacritical(item.getName()).toLowerCase(), item.getID());
        }

        return options;
    }

    /**
     * Validate dữ liệu xml đầu vào
     *
     * @param xmlMain
     * @throws Exception
     */
    private void validate() throws Exception {
        DSBenhAn dsBenhAn = xmlMain.getDu_lieu_cap_nhat().getDs_benh_an();
        if (dsBenhAn == null || dsBenhAn.getBenh_an() == null || dsBenhAn.getBenh_an().isEmpty()) {
            throw new Exception("Không tìm thấy bệnh án");
        }

        BenhAn benhan = dsBenhAn.getBenh_an().get(0);
        if (benhan == null || benhan.getSo_hsba() == null || "".equals(benhan.getSo_hsba())) {
            throw new Exception("Không tìm thấy mã bệnh án");
        }

        DSBenhNhan dsBenhNhan = xmlMain.getDu_lieu_cap_nhat().getDs_benh_nhan();
        if (dsBenhNhan == null || dsBenhNhan.getBenh_nhan() == null || dsBenhNhan.getBenh_nhan().isEmpty()) {
            throw new Exception("Không tìm thấy thông tin bệnh nhân");
        }
    }

    //Convert xml to thông tin cơ bản bệnh nhân
    private OpcPatientEntity getPatient() {
        BenhAn benhAn;
        BenhNhan benhNhan;
        try {
            benhAn = xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0);
        } catch (Exception e) {
            benhAn = new BenhAn();
        }
        try {
            benhNhan = xmlMain.getDu_lieu_cap_nhat().getDs_benh_nhan().getBenh_nhan().get(0);
        } catch (Exception e) {
            benhNhan = new BenhNhan();
        }

        //Thông tin cơ bản bệnh nhân
        OpcPatientEntity patient = new OpcPatientEntity();
        patient.setSiteID(siteFk.getID());
        patient.setFullName(benhNhan.getHo_ten());
        patient.setGenderID(getGender(benhNhan.getGioi_tinh()));
        patient.setIdentityCard(benhNhan.getCmnd());
        patient.setDob(getDate(benhNhan.getNgay_sinh()));
        patient.setRaceID(getRace(benhNhan.getTen_dan_toc(), options.get(ParameterEntity.RACE)));
        //thông tin XN KĐ
        patient.setConfirmCode(benhAn.getMa_xn_kd_hiv());
        patient.setConfirmTime(getDateTime(benhAn.getNgay_khang_dinh_hiv()));
        patient.setConfirmSiteID(Long.valueOf("-1"));
        patient.setConfirmSiteName(benhAn.getTen_noi_khang_dinh_hiv());
        return patient;
    }

    //Convert xml to Arv
    private OpcArvEntity getArv(OpcArvEntity model) throws CloneNotSupportedException {
        BenhAn benhAn = xmlMain.getDu_lieu_cap_nhat().getDs_benh_an().getBenh_an().get(0);
        BenhNhan benhNhan = xmlMain.getDu_lieu_cap_nhat().getDs_benh_nhan().getBenh_nhan().get(0);
        DotDieuTri dieutri;
        try {
            dieutri = xmlMain.getDu_lieu_cap_nhat().getDs_dot_dieu_tri().getDot_dieu_tri().get(0);
        } catch (Exception e) {
            dieutri = new DotDieuTri();
        }
        List<DotDieuTri> dsDotDieuTri;
        DotDieuTri dotDieuTri;
        try {
            dsDotDieuTri = xmlMain.getDu_lieu_cap_nhat().getDs_dot_dieu_tri().getDot_dieu_tri();
            dotDieuTri = dsDotDieuTri.get(dsDotDieuTri.size() - 1);//đợt điều trị mới nhất
        } catch (Exception e) {
            dotDieuTri = new DotDieuTri();
        }

        //Lần khám
        DSLanKham ds_lan_kham = xmlMain.getDu_lieu_cap_nhat().getDs_lan_kham();
        List<LanKham> dsLanKham = ds_lan_kham == null ? null : ds_lan_kham.getLan_kham();
        LanKham lankham = dsLanKham == null || dsLanKham.isEmpty() ? null : dsLanKham.get(dsLanKham.size() - 1);

        OpcArvEntity arv;
        if (model != null) {
            arv = model.clone();
        } else {
            arv = new OpcArvEntity();
        }

        arv.setDataType(OpcDataTypeEnum.HIS_VNPT.getKey());
        arv.setDataID(benhAn.getMa_ba_arv());
        arv.setDataPatientID(benhAn.getMa_bn());

        if (lankham != null) {
            arv.setInsurance(StringUtils.isEmpty(lankham.getMa_the()) ? "0" : "1");
            arv.setInsuranceNo(lankham.getMa_the());
            arv.setInsuranceExpiry(getDate(lankham.getGt_the_den()));
            arv.setLastExaminationTime(getDateTime(lankham.getNgay_vao()));

            if (lankham.getKham_hiv() != null) {
                arv.setMedicationAdherence(getTuanThuDieuTri(lankham.getKham_hiv().getTuan_thu_dieu_tri()));
                try {
                    arv.setDaysReceived(Integer.valueOf(lankham.getKham_hiv().getSo_ngay_uong_thuoc()));
                } catch (Exception e) {
                    arv.setDaysReceived(0);
                }

                arv.setAppointmentTime(getDateTime(lankham.getKham_hiv().getNgay_hen_tai_kham()));
                //Các vấn đề trong lần khám gần nhất
            }

            //chỉ số lượt khám
            if (lankham.getChi_so_hieu_sinh() != null && lankham.getChi_so_hieu_sinh().getChieu_cao() != null && !"".equals(lankham.getChi_so_hieu_sinh().getChieu_cao())) {
                arv.setPatientHeight(Float.valueOf(lankham.getChi_so_hieu_sinh().getChieu_cao()));
            }
            if (lankham.getChi_so_hieu_sinh() != null && lankham.getChi_so_hieu_sinh().getCan_nang() != null && !"".equals(lankham.getChi_so_hieu_sinh().getCan_nang())) {
                arv.setPatientWeight(Float.valueOf(lankham.getChi_so_hieu_sinh().getCan_nang()));
            }
        }

        arv.setCode(benhAn.getSo_hsba() == null ? "" : benhAn.getSo_hsba().toUpperCase());
        arv.setSiteID(siteFk.getID());
        arv.setJobID(getJob(benhNhan.getMa_nghe_nghiep(), benhNhan.getTen_nghe_nghiep()));

        //Loại thanh toán
        //Nơi đăng ký khám chữa bệnh ban đầu
        //Tỉ lệ thanh toán
        arv.setPatientPhone(benhNhan.getDien_thoai());
        arv.setPermanentAddress(benhNhan.getDia_chi());
        arv.setPermanentProvinceID(getProvince(benhNhan.getMa_tinh_thanh(), benhNhan.getTen_tinh_thanh()));
        arv.setPermanentDistrictID(getDistrict(benhNhan.getMa_quan_huyen(), benhNhan.getTen_quan_huyen()));
        arv.setPermanentWardID(getWard(benhNhan.getMa_phuong_xa(), benhNhan.getTen_phuong_xa(), arv.getPermanentDistrictID()));
        arv.setCurrentAddress(benhNhan.getDia_chi_tam_tru());
        // * không có tạm trú nên set tạm bằng cư trú
        arv.setCurrentProvinceID(arv.getPermanentProvinceID());
        arv.setCurrentDistrictID(arv.getPermanentDistrictID());
        arv.setCurrentWardID(arv.getPermanentWardID());
        //Người hỗ trợ
        //Quan với hệ bệnh nhân
        //Số điện thoại người hỗ trợ

        //Giai đoạn lâm sàng
        //CD4 hoặc cd4%
        arv.setLao(false); //Không tìm thấy lao
        //Ngày xét nghiệm lao
        //triệu chứng khác
        arv.setInh(false); //Điều trị dự phòng INH
        // inh - Lao từ ngày
        //Lao đến ngày
        arv.setNtch(false); //Nhiễm trùng cơ hội - ntch
        //triệu chứng khác ntch
        //cotrimoxazole từ ngày
        //cotrimoxazole đến ngày
        arv.setEndTime(getDateTime(dieutri.getNgay_ket_thuc()));
        arv.setEndCase(getLyDoKetThuc(dieutri.getLy_do_ket_thuc()));
        arv.setStatusOfTreatmentID(getTrangThaiDieuTri(arv.getEndCase(), arv.getEndTime())); //Chưa lấy được thông tin
        arv.setFirstTreatmentTime(getDateTime(benhAn.getNgay_khoi_lieu_arv()));
        //Phác đồ điều trị đầu tiên
        arv.setTreatmentTime(getDateTime(benhAn.getNgay_khoi_lieu_arv()));
        //Bậc phác đồ điều trị - sử dụng enum
//        arv.setTreatmentRegimenStage(xmlMain.getDu_lieu_cap_nhat().);
//        arv.setTreatmentRegimenID(getPhacDoDieuTri(benhAn.getTen_phac_do_ban_dau()));

        //Ngày xét nghiệm cd4 đầu tiên
        //Kết quả xét nghiệm cd4
        //Ngày xét nghiệm cd4
        //Kết quả xét nghiệm cd4 gần nhất
        //Kết quả xét nghiệm TLVR
        //TLVR - Ngày xét nghiệm đầu tiên
        //Kết quả xét nghiệm TLVR
        //TLVR - Ngày xét nghiệm
        arv.setHbv(false); //Không tìm thấy thông tin hbv
        arv.setHcv(false); //Không tìm thấy thông tin hcv

        //Cơ sở chuyển đi - ma_noi_chuyen_di/ten_noi_chuyen_di chưa có thông tin
        //Lý do chuyển đi
        arv.setPcrOneTime(getDateTime(benhAn.getNgay_lay_mau_pcr_lan1()));
        arv.setPcrOneResult(benhAn.getChi_so_pcr_lan1());
        arv.setPcrTwoTime(getDateTime(benhAn.getNgay_lay_mau_pcr_lan2()));
        arv.setPcrTwoResult(benhAn.getChi_so_pcr_lan2());
        arv.setTranferFromTime(getDateTime(dotDieuTri.getNgay_ket_thuc()));
        arv.setRegistrationTime(getDateTime(dieutri.getNgay_dang_ky()) == null ? new Date() : getDateTime(dieutri.getNgay_dang_ky()));
        arv.setRegistrationType(getLyDoDangKy(dotDieuTri.getLy_do_dang_ky())); // Loại đăng ký
        if (arv.getRegistrationTime() != null && arv.getTreatmentTime() != null && arv.getRegistrationTime().compareTo(arv.getTreatmentTime()) > 0) {
            arv.setRegistrationTime(arv.getTreatmentTime());
        } else {
            arv.setRegistrationTime(new Date());
        }

        if (arv.getCode().substring(arv.getCode().length() - 1).equals("P") || arv.getCode().substring(arv.getCode().length() - 1).equals("p")) {
            arv.setRemove(true);
        }
        arv.setInsurance("0");

        return arv;
    }

    //Phac đồ điều trị
    private String getPhacDoDieuTri(String ten_phac_do) {
        ten_phac_do = replatePhacDo(ten_phac_do);
        ten_phac_do = softString(ten_phac_do);

        String maPhacDo = "";
        HashMap<String, String> map = options.get(ParameterEntity.TREATMENT_REGIMEN);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            value = replatePhacDo(value);
            value = softString(value);

            if (ten_phac_do.equals(value)) {
                maPhacDo = key;
                break;
            }

        }

        return maPhacDo;
    }

    private String replatePhacDo(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String s = text;
        s = s.replaceAll("Bậc 1", "");
        s = s.replaceAll("Bậc 1", "");
        s = s.replaceAll("Bậc 2", "");
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\/", "");
        s = s.replaceAll("\\+", "");
        s = s.replaceAll("\\(", "");
        s = s.replaceAll("\\)", "");
        s = s.replaceAll("\\=", "");
        s = s.replaceAll("\\-", "");

        return s;
    }

    private String softString(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String s = text;
        char[] c = s.toCharArray();        // convert to array of chars 
        Arrays.sort(c);          // sort
        String newString = new String(c);  // convert back to String

        return newString;
    }

    //Giới tính; Mã hóa (1: Nam; 2: Nữ; 3: Chưa xác định)
    private String getGender(String gioi_tinh) {
        switch (gioi_tinh) {
            case "1":
                return GenderEnum.MALE.getKey();
            case "2":
                return GenderEnum.FEMALE.getKey();
            default:
                return GenderEnum.NONE.getKey();
        }
    }

    //yyyymmdd
    private Date getDate(String stringDate) {
        try {
            if (stringDate == null || "".equals(stringDate)) {
                return null;
            }
            return new SimpleDateFormat("yyyyMMdd").parse(stringDate);
        } catch (ParseException ex) {
            getLogger().error(ex.getMessage());
            return null;
        }
    }

    //yyyymmddhhMM
    private Date getDateTime(String stringDate) {
        try {
            if (stringDate == null || "".equals(stringDate)) {
                return null;
            }
            Date date = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH).parse(stringDate);
            return TextUtils.convertDate(TextUtils.formatDate(date, FORMATDATE), FORMATDATE);
        } catch (ParseException ex) {
            getLogger().error(ex.getMessage());
            return null;
        }
    }

    //Dân tộc theo tên
    private String getRace(String ten_dan_toc, HashMap<String, String> options) {
        String key = TextUtils.removeDiacritical(ten_dan_toc).toLowerCase();
        return options.getOrDefault(key, RaceEnum.KINH.getKey());
    }

    //Nghề nghiệp
    private String getJob(String ma_nghe_nghiep, String ten_nghe_nghiep) {
        for (Map.Entry<String, String> item : options.get(ParameterEntity.JOB).entrySet()) {
            if (TextUtils.removeDiacritical(item.getValue()).equals(ten_nghe_nghiep)) {
                return item.getKey();
            }
        }
        return JobEnum.KHAC.getKey();
    }

    //Tỉnh thành
    private String getProvince(String ID, String name) {
        return options.get("province").getOrDefault(TextUtils.removeDiacritical(name).toLowerCase(), null);
    }

    //Quận huyện
    private String getDistrict(String ID, String name) {
        return options.get("district").getOrDefault(TextUtils.removeDiacritical(name).toLowerCase(), null);
    }

    //Phường xã
    private String getWard(String ID, String name, String districtID) {
        if (districtID != null) {
            name = TextUtils.removeDiacritical(name).toLowerCase();
            List<WardEntity> items = locationsService.findWardByDistrictID(districtID);
            if (items != null) {
                for (WardEntity item : items) {
                    if (name.equals(TextUtils.removeDiacritical(item.getName()).toLowerCase())) {
                        return item.getID();
                    }
                }
            }
        }
        return null;
    }

    //Mã hóa lý do đăng ký đợt điều trị (0: BN HIV mới đăng ký lần đầu; 1: BN HIV nơi khác chuyển tới; 2: BN ARV bỏ trị tái điều trị; 4: Trẻ dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV; 5: Điều trị phơi nhiễm; 6: BN chưa ARV nơi khác chuyển tới)
    private String getLyDoDangKy(String ly_do_dang_ky) {
        if (StringUtils.isEmpty(ly_do_dang_ky)) {
            return RegistrationTypeEnum.NEW.getKey();
        }
        switch (ly_do_dang_ky) {
            case "1":
                return RegistrationTypeEnum.TRANSFER.getKey();
            case "2":
                return RegistrationTypeEnum.REPLAY.getKey();
            case "4":
                return RegistrationTypeEnum.CHILDREN_UNDER_18.getKey();
            case "5":
                return RegistrationTypeEnum.EXPOSURE.getKey();
            case "6": //BN chưa ARV nơi khác chuyển tới - không có trên hệ thống
                return RegistrationTypeEnum.TRANSFER.getKey();
            case "0":
            default:
                return RegistrationTypeEnum.NEW.getKey();
        }
    }

    //Bận phác đồ điều trị
    private String getBacPhacDo(String ten_phac_do_ban_dau) {
        if (StringUtils.isEmpty(ten_phac_do_ban_dau)) {
            return "";
        }
        if (ten_phac_do_ban_dau.contains("2")) {
            return TreatmentRegimenStageEnum.TWO.getKey();
        } else if (ten_phac_do_ban_dau.contains("3")) {
            return TreatmentRegimenStageEnum.EXPOSURE.getKey();
        } else {
            return TreatmentRegimenStageEnum.ONE.getKey();
        }
    }

    //Tuân thủ điều trị
    private String getTuanThuDieuTri(String tuan_thu_dieu_tri) {
        if (StringUtils.isEmpty(tuan_thu_dieu_tri)) {
            return "";
        }
        if (tuan_thu_dieu_tri.contains("1")) {
            return MedicationAdherenceEnum.GOOD.getKey();
        } else if (tuan_thu_dieu_tri.contains("2")) { //Bình thường - hệ thống k có
            return MedicationAdherenceEnum.GOOD.getKey();
        } else if (tuan_thu_dieu_tri.contains("3")) {
            return MedicationAdherenceEnum.NOTGOOD.getKey();
        } else {
            return MedicationAdherenceEnum.NONE.getKey();
        }
    }

    //Lý do kết thúc
    private String getLyDoKetThuc(String ly_do_ket_thuc) {
        if (ly_do_ket_thuc == null || ly_do_ket_thuc.equals("")) {
            return null;
        }
        if (ly_do_ket_thuc.endsWith("1")) {
            return ArvEndCaseEnum.MOVED_AWAY.getKey();
        } else if (ly_do_ket_thuc.endsWith("2")) {
            return ArvEndCaseEnum.CANCELLED.getKey(); //Bỏ trị
        } else if (ly_do_ket_thuc.endsWith("3")) {
            return ArvEndCaseEnum.LOSE_TRACK.getKey(); //Mất dấu
        } else if (ly_do_ket_thuc.endsWith("4")) {
            return ArvEndCaseEnum.DEAD.getKey(); //Tử vong
        } else { //Khác để là END - tạm thời
            return ArvEndCaseEnum.END.getKey();
        }
    }

    //Trạng thái điều trị
    private String getTrangThaiDieuTri(String endCase, Date endTime) {
        String status = StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey();
        if (StringUtils.isNotEmpty(endCase) && endTime != null) {
            if (endCase.equals(ArvEndCaseEnum.CANCELLED.getKey())) {
                status = StatusOfTreatmentEnum.BO_TRI.getKey();
            } else if (endCase.equals(ArvEndCaseEnum.LOSE_TRACK.getKey())) {
                status = StatusOfTreatmentEnum.MAT_DAU.getKey();
            } else if (endCase.equals(ArvEndCaseEnum.DEAD.getKey())) {
                status = StatusOfTreatmentEnum.TU_VONG.getKey();
            } else {
                status = StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey();
            }
        }
        return status;
    }

    //Lấy thông tin lượt khám
    private List<OpcVisitEntity> getVisits() {
        List<OpcVisitEntity> items = new ArrayList<>();
        DSLanKham ds_lan_kham = xmlMain.getDu_lieu_cap_nhat().getDs_lan_kham();
        List<LanKham> dslankham = ds_lan_kham == null ? null : ds_lan_kham.getLan_kham();
        if (dslankham == null || dslankham.isEmpty()) {
            return items;
        }
        XuTri xu_tri = new XuTri();
        try {
            xu_tri = xmlMain.getDu_lieu_cap_nhat().getDs_xu_tri().getXu_tri().get(0);
        } catch (Exception e) {
        }

        OpcVisitEntity item;
        for (LanKham lanKham : dslankham) {
            lanKham.setKham_hiv(lanKham.getKham_hiv() == null ? new KhamHIV() : lanKham.getKham_hiv());
            item = new OpcVisitEntity();
            item.setDataStageID(lanKham.getMa_dot_dieu_tri());
            item.setDataType(OpcDataTypeEnum.HIS_VNPT.getKey());
            item.setDataID(lanKham.getMa_lk());
            item.setInsurance(lanKham.getMa_the() != null && !"".equals(lanKham.getMa_the()) ? InsuranceEnum.TRUE.getKey() : "0");
            item.setInsuranceNo(lanKham.getMa_the());
            //Loại thanh toán thẻ BHYT - Sử dụng enum
            //Nơi đăng ký kcb ban đầu
            item.setInsuranceExpiry(getDate(lanKham.getGt_the_den()));
            //Tỉ lệ thanh toán thẻ BHYT - Sử dụng enum
            item.setExaminationTime(getDateTime(lanKham.getNgay_vao()));
            //Bậc phác đồ điều trị - sử dụng enum: Set vào khi save từ giai đoạn điều trị
            item.setTreatmentRegimenStage(getBacPhacDo(xu_tri.getBac_phac_do()));
            item.setTreatmentRegimenID(getPhacDoDieuTri(xu_tri.getTen_phac_do()));

            //Phác đồ điều trị tại cơ sở OPC -  Set vào khi save từ giai đoạn điều trị
            item.setCircuit(lanKham.getChi_so_hieu_sinh() != null ? lanKham.getChi_so_hieu_sinh().getMach() : "");
            item.setBloodPressure(lanKham.getChi_so_hieu_sinh() != null ? lanKham.getChi_so_hieu_sinh().getHuyet_ap() : "");
            item.setBodyTemperature(lanKham.getChi_so_hieu_sinh() != null ? lanKham.getChi_so_hieu_sinh().getNhiet_do() : "");
            item.setDiagnostic(lanKham.getTen_benh()); //Chuẩn đoán
            item.setMedicationAdherence(getTuanThuDieuTri(lanKham.getKham_hiv().getTuan_thu_dieu_tri()));
            try {
                item.setDaysReceived(Integer.valueOf(lanKham.getKham_hiv().getSo_ngay_uong_thuoc()));
            } catch (Exception e) {
                item.setDaysReceived(0);
            }

            item.setAppointmentTime(getDateTime(lanKham.getKham_hiv().getNgay_hen_tai_kham()));
            item.setClinical(lanKham.getKham_hiv().getGd_lam_sang_hiv());
            items.add(item);
        }
        return items;
    }

    private List<OpcViralLoadEntity> getViralLoad() {
        List<OpcViralLoadEntity> virals = new ArrayList<>();

        DSLanKham ds_lan_kham = xmlMain.getDu_lieu_cap_nhat().getDs_lan_kham();
        List<LanKham> dslankham = ds_lan_kham == null ? null : ds_lan_kham.getLan_kham();
        if (dslankham == null || dslankham.isEmpty()) {
            return virals;
        }
        List<KetQuaCls> ket_qua_cls = new ArrayList<>();
        try {
            ket_qua_cls = xmlMain.getDu_lieu_cap_nhat().getDs_lan_kham().getLan_kham().get(0).getDs_ket_qua_cls().getKet_qua_cls();
        } catch (Exception e) {
            return virals;
        }
        OpcViralLoadEntity viral;
        for (KetQuaCls ket_qua_cl : ket_qua_cls) {
            if (StringUtils.normalizeSpace(ket_qua_cl.getTen_chi_so().trim()).equals(StringUtils.normalizeSpace("HIV đo tải lượng hệ thống tự động".trim()))) {
                viral = new OpcViralLoadEntity();

                viral.setCauses(new ArrayList<String>());
                viral.getCauses().add("7");
                viral.setDataVisitID(ket_qua_cl.getMa_lk());
                viral.setDataID(ket_qua_cl.getId());
                viral.setTestTime(ket_qua_cl.getNgay_kq() == null ? new Date() : getDateTime(ket_qua_cl.getNgay_kq()));
                viral.setResultNumber(ket_qua_cl.getGia_tri() == null ? "" : ket_qua_cl.getGia_tri());
                viral.setResult(getKetQuaTLVR(ket_qua_cl.getGia_tri()));

                virals.add(viral);
                break;
            }

        }

        return virals;
    }

    private String getKetQuaTLVR(String kq) {
        String kqtlvr = "";
        if (StringUtils.isEmpty(kq)) {
            return kqtlvr;

        }
        kq = StringUtils.normalizeSpace(kq.trim());
        kq = TextUtils.removeDiacritical(kq);

        if (kq.contains("<") || kq.contains(">")) {
            String kq3 = TextUtils.removeDiacritical(kq);
            kq = kq.replaceAll("\\D+", "");
            try {
                int kq2 = Integer.valueOf(kq);
                kqtlvr = kq3.contains("<") && kq2 == 200 ? VirusLoadResultEnum.LESS_THAN_200.getKey()
                        : kq3.contains(">") && kq2 == 200 ? VirusLoadResultEnum.FROM_200_1000.getKey()
                        : kq3.contains("<") && kq2 == 1000 ? VirusLoadResultEnum.FROM_200_1000.getKey()
                        : kq3.contains(">") && kq2 == 1000 ? VirusLoadResultEnum.OVER_1000.getKey()
                        : kq2 >= 0 && kq2 < 200 ? VirusLoadResultEnum.LESS_THAN_200.getKey() : kq2 >= 200 && kq2 < 1000 ? VirusLoadResultEnum.FROM_200_1000.getKey() : kq2 >= 1000 ? VirusLoadResultEnum.OVER_1000.getKey() : "";
            } catch (Exception e) {
                return "";
            }
        } else if (kq.contains("khong")) {
            kqtlvr = VirusLoadResultEnum.NOT_DETECT.getKey();
        } else {
            kq = kq.replaceAll("\\D+", "");
            try {
                int kq2 = Integer.valueOf(kq);
                kqtlvr = kq2 >= 0 && kq2 < 200 ? VirusLoadResultEnum.LESS_THAN_200.getKey() : kq2 >= 200 && kq2 < 1000 ? VirusLoadResultEnum.FROM_200_1000.getKey() : kq2 >= 1000 ? VirusLoadResultEnum.OVER_1000.getKey() : "";
            } catch (Exception e) {
                return "";
            }
        }
        return kqtlvr;

    }
}
