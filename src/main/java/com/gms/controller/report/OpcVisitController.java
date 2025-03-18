package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.Cd4SymtomEnum;
import com.gms.entity.constant.HBVSymtomEnum;
import com.gms.entity.constant.HCVSymtomEnum;
import com.gms.entity.constant.InsurancePayEnum;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.LaoDiagnoseEnum;
import com.gms.entity.constant.LaoEndCaseEnum;
import com.gms.entity.constant.LaoSymtomEnum;
import com.gms.entity.constant.MedicationAdherenceEnum;
import com.gms.entity.constant.NTCHEndCaseEnum;
import com.gms.entity.constant.NTCHSymtomEnum;
import com.gms.entity.constant.OpcTestResultEnum;
import com.gms.entity.constant.QuickTreatmentEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.SuspiciousSymptomsEnum;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.pac.OpcVisitExcel;
import com.gms.entity.form.opc_arv.OpcPrescriptionForm;
import com.gms.entity.form.pac.OpcVisitForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcVisitService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthành
 */
@Controller(value = "opc_visit_report")
public class OpcVisitController extends OpcController {
    
    protected HashMap<String, HashMap<String, String>> getOptionss(boolean config, OpcArvEntity arv) {
        return getOptionss(config, arv, null, null);
    }
    
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcVisitService visitService;
    @Autowired
    HtcVisitService htcService;
    @Autowired
    private SiteService siteService;

    /**
     * In đơn thuốc
     *
     * @author DSNAnh
     * @param model
     * @param arvID
     * @param visitID
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/opc-visit/print.html"})
    public ResponseEntity<InputStreamResource> actionPrintVisit(Model model,
            @RequestParam(name = "arvid") String arvID,
            @RequestParam(name = "oid") String visitID,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(Long.parseLong(arvID));
        OpcVisitEntity visitEntity = visitService.findOne(Long.parseLong(visitID));
        OpcPrescriptionForm form = new OpcPrescriptionForm();
        
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setArvCode(arvEntity.getCode());
        form.setFullName(arvEntity.getPatient().getFullName());
        form.setDob(TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
        form.setGenderID(arvEntity.getPatient().getGenderID());
        form.setCurrentAddressFull(arvEntity.getCurrentAddressFull());
        form.setInsuranceNo(visitEntity.getInsuranceNo());
        form.setCircuit(visitEntity.getCircuit());
        form.setBloodPressure(visitEntity.getBloodPressure());
        form.setBodyTemperature(visitEntity.getBodyTemperature());
        form.setDiagnostic(visitEntity.getDiagnostic());
        form.setClinical(visitEntity.getClinical());
        form.setNote(visitEntity.getNote());
        form.setMsg(visitEntity.getMsg());
        form.setDaysReceived("" + visitEntity.getDaysReceived());
        form.setTreatmentRegimenID(StringUtils.isEmpty(visitEntity.getTreatmentRegimenID()) ? "" : getOptions().get(ParameterEntity.TREATMENT_REGIMEN).get(visitEntity.getTreatmentRegimenID()));
        form.setAppointmentTime(TextUtils.formatDate(visitEntity.getAppointmentTime(), FORMATDATE));
        
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf("DonThuoc", "report/opc/sample-prescription.html", context);
    }
    
    @GetMapping(value = {"/opc-visit/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "insurance", required = false) String insurance,
            @RequestParam(name = "insurance_type", required = false) String insuranceType,
            @RequestParam(name = "insurance_no", required = false) String insuranceNo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "viral_load_time_from", required = false) String viralLoadTimeFrom,//ngày hẹn khám
            @RequestParam(name = "viral_load_time_to", required = false) String viralLoadTimeTo,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "last_examination_time_from", required = false) String lastExaminationTimeFrom,
            @RequestParam(name = "last_examination_time_to", required = false) String lastExaminationTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "therapy_site_id", required = false) String therapySiteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999999") int size) throws Exception {
        
        LoggedUser currentUser = getCurrentUser();
        OpcArvSearch search = new OpcArvSearch();
        HashMap<String, HashMap<String, String>> options = getOptionss(false, null);
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        Set<Long> siteIDs = new HashSet<>();
        if (isOpcManager()) {
            if (StringUtils.isEmpty(therapySiteID)) {
                HashMap<String, String> siteList = options.get(ParameterEntity.TREATMENT_FACILITY);
                if (siteList != null && siteList.size() > 0) {
                    for (Map.Entry<String, String> entry : siteList.entrySet()) {
                        String key = entry.getKey();
                        if (StringUtils.isEmpty(key)) {
                            continue;
                        }
                        siteIDs.add(Long.parseLong(key));
                    }
                }
            } else {
                siteIDs.add(Long.parseLong(therapySiteID));
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }
        
        search.setRemove(false);
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);
        search.setInsurance(insurance);
        search.setInsuranceType(insuranceType);
        search.setLastExaminationTimeFrom(isThisDateValid(lastExaminationTimeFrom) ? lastExaminationTimeFrom : null);
        search.setLastExaminationTimeTo(isThisDateValid(lastExaminationTimeTo) ? lastExaminationTimeTo : null);
        search.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        search.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        search.setTreatmentStageID(treatmentStageID);
        if (StringUtils.isNotEmpty(code)) {
            search.setCode(code.trim());
        }
        if (StringUtils.isNotEmpty(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        if (StringUtils.isNotEmpty(identityCard)) {
            search.setIdentityCard(identityCard.trim());
        }
        if (StringUtils.isNotEmpty(insuranceNo)) {
            search.setInsuranceNo(insuranceNo.trim());
        }
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setResultID(resultID);
        
        if (!StringUtils.isEmpty(viralLoadTimeFrom)) {
            search.setTreatmentTimeFrom(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);//ngày hẹn khám từ
            search.setTreatmentTimeTo(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);//ngày hẹn khám từ
        } else {
            search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);//ngày hẹn khám từ
            search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);//ngày hẹn khám từ
        }
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPageIndex(page);
        search.setPageSize(size);
        
        

        //tab - date 
        LocalDate localDate = LocalDate.now().minusDays(91);
        LocalDate localDate90 = LocalDate.now().minusDays(90);
        LocalDate localDate31 = LocalDate.now().minusDays(31);
        
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date90 = Date.from(localDate90.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date31 = Date.from(localDate31.atStartOfDay(ZoneId.systemDefault()).toInstant());
        search.setTime84(TextUtils.formatDate(date, FORMATDATE));
        search.setTime90(TextUtils.formatDate(date90, FORMATDATE));
        search.setTime30(TextUtils.formatDate(date31, FORMATDATE));
        
        search.setTab("all".equals(tab) ? "0" : "late".equals(tab) ? "2" : "late84".equals(tab) ? "3" : "late30".equals(tab) ? "4" : "1");
        
        DataPage<OpcArvEntity> dataPage = opcArvService.findVisit(search);
        search.setIsOpcManage(isOpcManager());
        
        OpcVisitForm form = new OpcVisitForm();
        form.setSearch(search);
        form.setOptions(options);
        form.setItems(dataPage.getData());
        form.setIsVAAC(isVAAC());
        form.setTitle("Danh sách bệnh nhân tái khám");
        form.setFileName("DanhSachBenhNhanTaiKham_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        
        return exportExcel(new OpcVisitExcel(form, EXTENSION_EXCEL_X));
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    protected HashMap<String, HashMap<String, String>> getOptionss(boolean config, OpcArvEntity arv, OpcStageEntity stage, OpcVisitEntity visit) {
        HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
        
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE); //Câu hỏi thẻ BHYT
        parameterTypes.add(ParameterEntity.SUPPORTER_RELATION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_PCR); //Kết quả XN PCR
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT); //Biến động điều trị
        parameterTypes.add(ParameterEntity.REGISTRATION_TYPE); //Loại đăng ký
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE); //Giai đoạn lâm sàng
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT); //Trạng thái điều trị
        parameterTypes.add(ParameterEntity.PLACE_TEST); //Phác đồ điều trị
        parameterTypes.add(ParameterEntity.STOP_REGISTRATION_REASON);
        parameterTypes.add(ParameterEntity.ROUTE);
        parameterTypes.add(ParameterEntity.ARV_REGISTRATION_STATUS);
        
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        //Lấy phác đồ điều trị phù hợp
        List<ParameterEntity> treatmentRegimen = parameterService.getTreatmentRegimen(true, true);
        options.put(ParameterEntity.TREATMENT_REGIMEN, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_REGIMEN).put("", "Chọn phác đồ điều trị");
        for (ParameterEntity param : treatmentRegimen) {
            options.get(ParameterEntity.TREATMENT_REGIMEN).put(param.getCode(), param.getValue());
        }
        String regimenConfig = siteConfig.getOrDefault(SiteConfigEnum.OPC_REGIMEN.getKey(), null);
        if (config && regimenConfig != null && !regimenConfig.equals("")) {
            List<String> arrlist = new ArrayList<>(Arrays.asList(regimenConfig.split(",")));
            if (arv != null && arv.getTreatmentRegimenID() != null) {
                arrlist.add(arv.getTreatmentRegimenID());
            }
            if (arv != null && arv.getFirstTreatmentRegimenID() != null) {
                arrlist.add(arv.getFirstTreatmentRegimenID());
            }
            if (arv != null && stage != null && stage.getOldTreatmentRegimenID() != null) {
                arrlist.add(stage.getOldTreatmentRegimenID());
            }
            if (arv != null && stage != null && stage.getTreatmentRegimenID() != null) {
                arrlist.add(stage.getTreatmentRegimenID());
            }
            if (arv != null && visit != null && visit.getOldTreatmentRegimenID() != null) {
                arrlist.add(visit.getOldTreatmentRegimenID());
            }
            if (arv != null && visit != null && visit.getTreatmentRegimenID() != null) {
                arrlist.add(visit.getTreatmentRegimenID());
            }
            options.put(ParameterEntity.TREATMENT_REGIMEN, findOptions(options.get(ParameterEntity.TREATMENT_REGIMEN), arrlist.toArray(new String[arrlist.size()])));
        }
        
        addEnumOption(options, ParameterEntity.ANSWER, BooleanEnum.values(), null); //Sử dụng cho câu hỏi có/không
        addEnumOption(options, "has", BooleanEnum.values(), "Chọn câu trả lời"); //Sử dụng enum boolean
        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");
        addEnumOption(options, ParameterEntity.INSURANCE_PAY, InsurancePayEnum.values(), "Chọn tỷ lệ thanh toán BHYT"); // Chọn tỉ lệ thanh toán BHYT
        addEnumOption(options, ParameterEntity.LAO_SYMTOM, LaoSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.LAO_END_CASE, LaoEndCaseEnum.values(), null); //Lý do kết thúc điều trị Lao
        addEnumOption(options, ParameterEntity.NTCH_SYMTOM, NTCHSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.NTCH_END_CASE, NTCHEndCaseEnum.values(), null); //Lý do kết thúc điều trị NTCH
        addEnumOption(options, ParameterEntity.TREATMENT_REGINMEN_STAGE, TreatmentRegimenStageEnum.values(), "Chọn bậc phác đồ điều trị");
        addEnumOption(options, ParameterEntity.MEDICATION_ADHERENCE, MedicationAdherenceEnum.values(), "Chọn mức độ tuân thủ");
        addEnumOption(options, ParameterEntity.CD4_SYMTOM, Cd4SymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.VIRAL_LOAD_SYMTOM, ViralLoadSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.ARV_END_CASE, ArvEndCaseEnum.values(), "Chọn lý do"); //Lý do kết thúc đợt điều trị
        addEnumOption(options, ParameterEntity.TEST_RESULTS, OpcTestResultEnum.values(), "Chọn kết quả"); //Kết quả xét nghiệm
        addEnumOption(options, ParameterEntity.HBV_SYMTOM, HBVSymtomEnum.values(), "Chọn lý Xét nghiệm HBV");
        addEnumOption(options, ParameterEntity.HCV_SYMTOM, HCVSymtomEnum.values(), "Chọn lý Xét nghiệm HCV");
        addEnumOption(options, ParameterEntity.REGISTRATION_TYPE, RegistrationTypeEnum.values(), "Chọn loại đăng ký");
        addEnumOption(options, ParameterEntity.QUICK_TREATMENT, QuickTreatmentEnum.values(), "Chọn thời gian điều trị nhanh");
        addEnumOption(options, ParameterEntity.SUSPICIOUS_SYMPTOMS, SuspiciousSymptomsEnum.values(), "Chọn triệu chứng");
        addEnumOption(options, ParameterEntity.LAO_DIAGNOSE, LaoDiagnoseEnum.values(), "Chọn loại hình Lao");

        //Loại bỏ Chưa được điều trị
        options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        //Kết quả XN đầu tiên - Bỏ Không xét nghiệm
//        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        //Sửa theo tltk 21/05/2020
        options.put(ParameterEntity.VIRUS_LOAD, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.VIRUS_LOAD).put("", "Chọn kết quả xn tải lượng virus");
        options.get(ParameterEntity.VIRUS_LOAD).put("1", "Không phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("6", "Dưới ngưỡng phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("2", "Từ ngưỡng phát hiện đến < 200 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("3", "Từ 200 - < 1000 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("4", ">= 1000 bản sao/ml");

        //Lấy thêm htc
        List<SiteEntity> siteHtc = siteService.getSiteHtc(currentUser.getSite().getProvinceID());
        //Nơi xét nghiệm khẳng định
        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentUser.getSite().getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Chọn nơi xét nghiệm");
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(String.valueOf(site.getID()), site.getShortName());
        }
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getShortName());
        }
        options.get(key).put("-1", "Cơ sở khác");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        options.put(ParameterEntity.TREATMENT_FACILITY, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_FACILITY).put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get(ParameterEntity.TREATMENT_FACILITY).put(String.valueOf(site.getID()), site.getShortName());
        }
        options.get(ParameterEntity.TREATMENT_FACILITY).put("-1", "Cơ sở khác");

        //Cơ sở chuyển đi
        key = "siteOpcFrom";
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            map.put(String.valueOf(site.getID()), site.getShortName());
        }
        map.put("-1", "Cơ sở khác");
        options.put(key, map);

        //Cơ sở chuyển đến
        key = "siteOpcTo";
        HashMap<String, String> mSiteOpcTo = new LinkedHashMap<>();
        mSiteOpcTo.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getShortName());
        }
        for (SiteEntity site : siteHtc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getShortName());
        }
        mSiteOpcTo.put("-1", "Cơ sở khác");
        options.put(key, mSiteOpcTo);

        //Cơ sở xét nghiệm CD4
        options.put("siteCd4", new LinkedHashMap<String, String>());
        options.get("siteCd4").put("", "Chọn nơi xét nghiệm");
        
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key1 = entry.getKey();
            String value = entry.getValue();
            if (!StringUtils.isEmpty(key1) && !key1.equals("-1")) {
                options.get("siteCd4").put(key1, value);
            }
            
        }
        
        for (SiteEntity site : siteHtc) {
            options.get("siteCd4").put(String.valueOf(site.getID()), site.getName());
        }
        options.get("siteCd4").put("-1", "Cơ sở khác");

        //Nơi xét nghiệm sàng lọc
        key = "siteHtc";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }
        
        key = "transferGSPH";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        options.get(key).put("0", "Chưa chuyển");
        options.get(key).put("1", "Đã chuyển");
        
        return options;
    }
    
}
