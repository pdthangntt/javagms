package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.QrCodeEntity;
import com.gms.entity.form.opc_arv.OpcVisitForm;
import com.gms.entity.validate.OpcVisitQrValidate;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcPatientService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcVisitService;
import com.gms.service.QrCodeService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpcQRController extends OpcController {

    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private OpcPatientService patientService;
    @Autowired
    private OpcArvService arvService;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private OpcStageService stageService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcVisitQrValidate validate;

    /**
     * scan mã
     *
     * @param code
     * @param arvCode
     * @return
     */
    @RequestMapping(value = "/opc-qr/scan.json", method = RequestMethod.GET)
    public Response<?> actionScan(@RequestParam(name = "code", defaultValue = "", required = false) String code,
            @RequestParam(name = "arv", defaultValue = "", required = false) String arvCode) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();

        Map<String, Object> data = new HashMap<>();
        QrCodeEntity qr = null;
        OpcPatientEntity patient = null;
        OpcArvEntity currentArv = null;
        OpcArvEntity receiveArv = null;
        HtcVisitEntity visit = null;
        OpcVisitEntity opcVisit = null;
        OpcVisitEntity opcVisitForm = new OpcVisitEntity();
        OpcStageEntity stage = null;
        List<OpcArvEntity> arvs = null;

        if (arvCode == null || arvCode.equals("")) {
            qr = qrCodeService.findOne(code);
            if (qr == null || !qr.isActive()) {
                return new Response<>(false, "Mã QR không tìm thấy hoặc tạm khoá");
            }
            //Lấy thông tin bệnh nhân
            patient = patientService.findByQR(qr.getID());
        } else {
            currentArv = arvService.findByCode(currentUser.getSite().getID(), arvCode);
            if (currentArv == null) {
                return new Response<>(false, "Không tìm thấy bệnh án có mã " + arvCode);
            }
            patient = currentArv.getPatient();

        }

        if (patient != null) {
            arvs = arvService.findByPatient(patient.getID());
        }

        if (arvs != null && !arvs.isEmpty()) {
            for (OpcArvEntity arv : arvs) {
                //Bệnh án tại cơ sở hiện tại
                if (arv.getSiteID().equals(currentUser.getSite().getID())) {
                    currentArv = arv;
                    options.put("treatment-stage", getTreatmentStage(currentArv.getPatientID(), currentArv.getSiteID(), false));
                }
                //Bệnh án tiếp nhận
                if (!arv.getSiteID().equals(currentUser.getSite().getID()) && arv.getTranferFromTime() != null
                        && (arv.getTransferSiteID().equals(currentUser.getSite().getID()) || (arv.getTransferSiteName() != null && !arv.getTransferSiteName().equals("")))
                        && arv.getTranferToTime() == null) {
                    receiveArv = arv;
                    options.put("treatment-stage", getTreatmentStage(receiveArv.getPatientID(), receiveArv.getSiteID(), false));
                }
            }
            if (receiveArv == null && currentArv == null) {
                return new Response<>(false, "Không tìm thấy bệnh nhân phù hợp!");
            }
            if (currentArv != null) {
                List<OpcVisitEntity> visits = opcVisitService.findByArvID(currentArv.getID(), false);
                opcVisit = visits == null || visits.isEmpty() ? null : visits.get(0);
                if (opcVisit != null && !TextUtils.formatDate(opcVisit.getExaminationTime(), FORMATDATE).equals(TextUtils.formatDate(new Date(), FORMATDATE))) {
                    opcVisit = null;
                }
            }
        } else {
            if (qr == null || !qr.isActive()) {
                return new Response<>(false, "Mã QR không tìm thấy hoặc tạm khoá");
            }
            //Tiếp nhận từ HTC
            visit = visitService.findByQR(qr.getID());

            if (visit == null) {
                HtcConfirmEntity confirmQR = confirmService.findByQR(qr.getID());
                visit = visitService.findByCodeAndSite(confirmQR.getSourceID(), confirmQR.getSourceSiteID(), true);
            }

//            if (!(visit != null && visit.getArrivalSiteID() != null && (visit.getArrivalSiteID().equals(currentUser.getSite().getID()) || (visit.getArrivalSite() != null && !visit.getArrivalSite().equals(""))))) {
            if (visit == null) {
                return new Response<>(false, "Không thể tiếp nhận bệnh nhân, do thông tin chuyển gửi không chính xác!");
            } else if (visit.getArrivalSiteID() == null || StringUtils.isEmpty(visit.getArrivalSite())) {
                data.put("error", "Cơ sở xét nghiệm sàng lọc chưa thực hiện nhập thông tin chuyển gửi");
            } else if (visit.getArrivalSiteID() != null && (visit.getArrivalSiteID().equals(currentUser.getSite().getID()) || (visit.getArrivalSite() != null && !visit.getArrivalSite().equals("")))) {
                data.put("error", "Cơ sở xét nghiệm sàng lọc chuyển gửi cơ sở khác");
            }
        }

        String siteConfirmTestName = "";
        String sourceSiteArvName = "";
        if (visit != null && options.get("siteConfirm").get(visit.getSiteConfirmTest()) == null) {
            siteConfirmTestName = siteService.findOne(Long.valueOf(visit.getSiteConfirmTest() == null ? "-1" : visit.getSiteConfirmTest())).getName();
        } else if (visit != null && options.get("siteConfirm") != null) {
            siteConfirmTestName = options.get("siteConfirm").get(visit.getSiteConfirmTest());
        }

        if (options.get("siteHtc") != null && visit != null && visit.getSiteID() != null && options.get("siteHtc").get(visit.getSiteID().toString()) == null) {
            sourceSiteArvName = siteService.findOne(visit.getSiteID()).getName();
        } else if (options.get("siteHtc") != null && visit != null && visit.getSiteID() != null) {
            sourceSiteArvName = options.get("siteHtc").get(visit.getSiteID().toString());
        }

        data.put("patient", patient);
        data.put("currentArv", currentArv);
        data.put("receiveArv", receiveArv);
        data.put("visit", visit);
        data.put("siteConfirmName", siteConfirmTestName);
        data.put("siteHtcName", sourceSiteArvName);
        data.put("opcVisit", opcVisit);
        data.put("options", options);
        data.put("opcVisitForm", new OpcVisitForm(opcVisitForm));

        //Xử lý visit
        if (currentArv != null) {
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, 1);
            List<OpcVisitEntity> visits = opcVisitService.findByArvID(currentArv.getID(),
                    TextUtils.formatDate(currentDate, "yyyy-MM-dd"),
                    TextUtils.formatDate(c.getTime(), "yyyy-MM-dd"));
            OpcVisitForm form;
            if (visits != null && visits.size() > 0) {
                form = new OpcVisitForm(visits.get(0));
                stage = stageService.findOne(visits.get(0).getStageID());
                form.setStageID(stage.getID().toString());
                form.setBaseTreatmentRegimenID(stage.getTreatmentRegimenID());
                form.setBaseTreatmentRegimenStage(stage.getTreatmentRegimenStage());
                form.setEndTime(TextUtils.formatDate(stage.getEndTime(), "dd/MM/yyyy"));
                form.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
                form.setRegistrationTime(TextUtils.formatDate(stage.getRegistrationTime(), "dd/MM/yyyy"));
                form.setTreatmentTime(TextUtils.formatDate(stage.getTreatmentTime(), "dd/MM/yyyy"));
            } else {
                form = new OpcVisitForm(currentArv);
                form.setLastExaminationTime(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
                List<OpcStageEntity> stageList = stageService.findByPatientID(currentArv.getPatientID(), false, currentUser.getSite().getID());
                if (stageList != null && stageList.size() > 0) {
                    OpcStageEntity lastStage = stageList.get(isOpcManager() ? 0 : stageList.size() - 1);
                    form.setStageID(lastStage.getID().toString());
                    form.setBaseTreatmentRegimenID(lastStage.getTreatmentRegimenID());
                    form.setBaseTreatmentRegimenStage(lastStage.getTreatmentRegimenStage());
                    form.setEndTime(TextUtils.formatDate(lastStage.getEndTime(), "dd/MM/yyyy"));
                    form.setStatusOfTreatmentID(lastStage.getStatusOfTreatmentID());
                    form.setRegistrationTime(TextUtils.formatDate(lastStage.getRegistrationTime(), "dd/MM/yyyy"));
                    form.setTreatmentTime(TextUtils.formatDate(lastStage.getTreatmentTime(), "dd/MM/yyyy"));
                }
            }
            data.put("opcVisitForm", form);
        }
        return new Response<>(true, data);
    }

    /**
     * Tự động tạo lượt khám
     *
     * @param arvID
     * @param form
     * @param bindingResult
     * @auth vvThành
     * @return
     */
    @RequestMapping(value = "/opc-qr/visit.json", method = RequestMethod.POST)
    public Response<?> actionVisit(@RequestParam(name = "arv_id") Long arvID,
            @RequestBody OpcVisitForm form,
            BindingResult bindingResult) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            if (arv == null || arv.isRemove()) {
                throw new Exception("Không tìm thấy bệnh án phù hợp!");
            }
            if (!arv.getSiteID().equals(currentUser.getSite().getID())) {
                throw new Exception("Bệnh án không thuộc quán lý tại cơ sở!");
            }

            List<OpcStageEntity> stages = stageService.findByArvIDAndSite(arv.getID(), currentUser.getSite().getID());
            if (stages == null || stages.isEmpty()) {
                throw new Exception("Bệnh án chưa có giai đoạn điều trị!");
            }
            OpcStageEntity stage = stages.get(stages.size() - 1);
            if (!stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()) && stage.getEndCase() == null) {
                throw new Exception("Bệnh án không tìm thấy giai đoạn đang điều trị!");
            }

            OpcVisitEntity visit = new OpcVisitEntity();
            form.setInsuranceExpiry(getValidDate(form.getInsuranceExpiry()));
            form.setLastExaminationTime(getValidDate(form.getLastExaminationTime()));
            form.setRegimenStageDate(getValidDate(form.getRegimenStageDate()));
            form.setRegimenDate(getValidDate(form.getRegimenDate()));
            form.setAppointmentTime(getValidDate(form.getAppointmentTime()));
            form.setSupplyMedicinceDate(getValidDate(form.getSupplyMedicinceDate()));
            form.setReceivedWardDate(getValidDate(form.getReceivedWardDate()));
            form.setPregnantStartDate(getValidDate(form.getPregnantStartDate()));
            form.setPregnantEndDate(getValidDate(form.getPregnantEndDate()));
            validate.validate(form, bindingResult);
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (ObjectError error : bindingResult.getAllErrors()) {
                    errors.put(error.getCode(), error.getDefaultMessage());
                }
                return new Response<>(false, "Vui lòng kiểm tra lại thông tin", errors);
            }
            if (StringUtils.isNotEmpty(form.getID())) {
                visit.setID(Long.parseLong(form.getID()));
            }
            if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
                form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
            }
            if (form.getTreatmentRegimenID() != null && form.getTreatmentRegimenID().contains("string:")) {
                form.setTreatmentRegimenID(form.getTreatmentRegimenID().replace("string:", ""));
            }
            if (form.getOldTreatmentRegimenID() != null && form.getOldTreatmentRegimenID().contains("string:")) {
                form.setOldTreatmentRegimenID(form.getOldTreatmentRegimenID().replace("string:", ""));
            }
            if (form.getOldTreatmentRegimenStage() != null && form.getOldTreatmentRegimenStage().contains("string:")) {
                form.setOldTreatmentRegimenStage(form.getOldTreatmentRegimenStage().replace("string:", ""));
            }
            OpcVisitEntity result = form.setToEntity(visit);
            result.setArvID(arv.getID());
            result.setSiteID(arv.getSiteID());
            result.setPatientID(arv.getPatientID());
            if (isOpcManager()) {
                result.setSiteID(arv.getSiteID());
            } else {
                result.setSiteID(currentUser.getSite().getID());
            }
            opcVisitService.save(result);
            if (StringUtils.isNotEmpty(form.getID())) {
                opcVisitService.logVisit(result.getArvID(), result.getPatientID(), result.getID(), "Tự động cập nhật lượt khám khi quét mã QR");
            } else {
                opcVisitService.logVisit(result.getArvID(), result.getPatientID(), result.getID(), "Tự động tạo lượt khám khi quét mã QR");
            }
            return new Response<>(true, result);
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
    }

    //Lấy danh sách các giai đoạn điều trị của bệnh án
    public HashMap<String, String> getTreatmentStage(Long patientID, Long siteID, boolean isModified) {
        HashMap<String, String> treatmentStage = new HashMap<>();
        treatmentStage.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> stageList;
        if (siteID != null) {
            stageList = stageService.findByPatientID(patientID, false, siteID);
        } else {
            stageList = stageService.findByPatientID(patientID, false);
        }
        if (stageList != null) {
            for (OpcStageEntity entity : stageList) {
                String treatmentRegimenStage = StringUtils.isEmpty(entity.getTreatmentRegimenStage()) ? "0" : entity.getTreatmentRegimenStage();
                String treatmentRegimenID = StringUtils.isEmpty(entity.getTreatmentRegimenID()) ? "0" : entity.getTreatmentRegimenID();
                String statusOfTreatmentID = StringUtils.isEmpty(entity.getStatusOfTreatmentID()) ? "99" : entity.getStatusOfTreatmentID();
                String startTime = entity.getTreatmentTime() == null ? TextUtils.formatDate(entity.getRegistrationTime(), FORMATDATE) : TextUtils.formatDate(entity.getTreatmentTime(), FORMATDATE);
                String endTime = entity.getEndTime() == null ? "hiện tại" : TextUtils.formatDate(entity.getEndTime(), FORMATDATE);
                String value = "Giai đoạn: " + startTime + " - " + endTime;
                String modified = isModified ? String.format("-%s-%s-%s", treatmentRegimenStage, treatmentRegimenID, statusOfTreatmentID) : "";
                treatmentStage.put(entity.getID().toString() + modified, value);
            }
        }
        return treatmentStage;
    }

    public String getValidDate(String date) {
        if (StringUtils.isNotEmpty(date) && date.length() == 8 && !date.contains("/")) {
            date = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);
        }
        return date == null ? "" : date;
    }
}
