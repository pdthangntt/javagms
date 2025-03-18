package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.opc_arv.OpcStageForm;
import com.gms.entity.validate.OpcStageEndValidate;
import com.gms.entity.validate.OpcStageValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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
public class OpcStageController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcStageValidate opcStageValidate;
    @Autowired
    private OpcStageEndValidate opcStageEndValidate;

    /**
     * Lấy thông tin đợt điều trị
     *
     * @param arvID
     * @param stageID
     * @return
     */
    @RequestMapping(value = "/opc-stage/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        Map<String, Object> data = new HashMap();
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                return new Response<>(false, "Thông tin bệnh án không tồn tại");
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Thông tin bệnh án không tồn tại");
        }

        OpcStageEntity model = opcStageService.findOne(stageID);
        if (model == null || !model.getPatientID().equals(arvEntity.getPatientID())) {
            return new Response<>(true, "Không tìm thấy thông tin điều trị");
        }

        // Tự động fill các trường từ thông tin bệnh án
        OpcStageForm form = new OpcStageForm();

        // Thông tin chung
        OpcPatientEntity patient = arvEntity.getPatient();
        form = form.setFromStage(model);
        form.setDob(TextUtils.formatDate(patient.getDob(), FORMATDATE));
        form.setGenderID(patient.getGenderID());
        form.setName(patient.getFullName());
        form.setIdentityCard(patient.getIdentityCard());

        form.setFromStage(model);
        form.setOPC(isOPC());
        form.setOpcManager(isOpcManager());
        form.setArvCode(arvEntity.getCode());
        data.put("form", form);

        return new Response<>(true, data);
    }

    /**
     * Cập nhật trạng thông tin điều trị
     *
     * @param arvID
     * @param stageID
     * @param form
     * @param bindingResult
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = "/opc-stage/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdateStage(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            @RequestBody OpcStageForm form,
            BindingResult bindingResult) throws ParseException {

        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                return new Response<>(false, "Thông tin bệnh án không tồn tại");
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Thông tin bệnh án không tồn tại");
        }

        OpcStageEntity oldStage = opcStageService.findOne(stageID);
        OpcStageEntity stage = opcStageService.findOne(stageID);
        if (stage == null || !stage.getPatientID().equals(arvEntity.getPatientID())) {
            return new Response<>(false, "Không tìm thấy thông tin điều trị");
        }

        // validate các trường nhập
        form.setConfirmTime(TextUtils.formatDate(arvEntity.getPatient().getConfirmTime(), FORMATDATE));
        form.setCurrentSiteID(currentUser.getSite().getID());
        form.setFirstTreatmentTime(TextUtils.formatDate(arvEntity.getFirstTreatmentTime(), FORMATDATE));

        opcStageEndValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }

        if (isOpcManager() && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
            return new Response<>(false, "Khoa điều trị không được thực hiện chuyển gửi bệnh nhân");
        }

        stage = form.convertStage(stage);
        stage.setPatientID(arvEntity.getPatientID());
        stage.setArvID(arvID);

        // Validate khoảng thời gian điều trị
        if (!opcStageService.validateStage(stage)) {
            return new Response<>(false, "Thời gian nhập của giai đoạn điều trị không hợp lệ hoặc ngày kết thúc của đợt điều trị nhập trong quá khứ đang trống");
        }

        // Lý do kết thúc và trạng thái điều trị
        if (stage.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey()) && stage.getStatusOfTreatmentID().equals (StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
            return new Response<>(false, "Không thể chuyển gửi bệnh nhân có Trạng thái điều trị là Chưa có thông tin");
        }
        if (stage.getEndCase().equals(ArvEndCaseEnum.END.getKey()) && stage.getStatusOfTreatmentID().equals (StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())) {
            return new Response<>(false, "Không thể kết thúc giai đoạn có Trạng thái điều trị là Chưa có thông tin");
        }

        try {
            // Thông tin thỏa mãn chuyển đi set lại
            if (StringUtils.isNotEmpty(stage.getEndCase()) && stage.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                stage.setTranferFromTime(stage.getEndTime());
            }

            stage = opcStageService.save(stage);
            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), stage.getID(), "Đợt điều trị được cập nhật thành công");

            // Gửi mail thông báo nếu có thay đổi cơ sở điều trị khi đã chuyển gửi
            if (stage.getTransferSiteID() != null) {
                //Gửi email thông báo cơ sở mới thay đổi
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(stage.getTransferSiteID());
                if (stage.getTransferSiteID() != null && !stage.getTransferSiteID().equals(-1l)) {
                    variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arvEntity.getCode());
                    variables.put("transferSiteName", transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("gender", options.get(ParameterEntity.GENDER).get(arvEntity.getPatient().getGenderID()));
                    variables.put("fullName", arvEntity.getPatient().getFullName());
                    variables.put("dob", TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
                    variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                    sendEmail(getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển tiếp điều trị bệnh nhân mã %s", arvEntity.getCode()), EmailoutboxEntity.ARV_TRANSFER_MAIL, variables);
                }

                //Gửi email thông báo cơ sở cũ bị thay đổi
                if (oldStage.getTransferSiteID() != null && !oldStage.getTransferSiteID().equals(-1l)
                        && stage.getTransferSiteID() != null && !stage.getTransferSiteID().equals(-1l)) {
                    variables = new HashMap<>();
                    SiteEntity oldTransferSite = siteService.findOne(oldStage.getTransferSiteID());
                    if (oldTransferSite != null) {
                        variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arvEntity.getCode());
                        variables.put("transferSiteName", oldTransferSite.getName());
                        variables.put("changedTransferSiteName", transferSite.getName());
                        variables.put("currentSiteName", getCurrentUser().getSite().getName());
                        variables.put("gender", options.get(ParameterEntity.GENDER).get(arvEntity.getPatient().getGenderID()));
                        variables.put("fullName", arvEntity.getPatient().getFullName());
                        variables.put("dob", TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
                        variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                        sendEmail(getSiteEmail(oldTransferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Bệnh nhân #%s - %s đã thay đổi cơ sở điều trị sang cơ sở %s", arvEntity.getCode(), arvEntity.getPatient().getFullName(), transferSite.getName()), EmailoutboxEntity.ARV_TRANSFER_MAIL_CHANGED, variables);
                    }
                }
            }

            return new Response<>(true, "Đợt điều trị được cập nhật thành công", stage);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(false, "Lưu thông tin thất bại. " + ex.getMessage());
        }
    }

    /**
     * Thêm mới trạng thông tin điều trị
     *
     * @param arvID
     * @param form
     * @param bindingResult
     * @return
     * @throws java.text.ParseException
     */
//    @RequestMapping(value = "/opc-stage/new.json", method = RequestMethod.POST)
//    public Response<?> actionNewStage(@RequestParam(name = "arvid") Long arvID,
//            @RequestBody OpcStageForm form,
//            BindingResult bindingResult) throws ParseException {
//
//        LoggedUser currentUser = getCurrentUser();
//        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
//        if (isOpcManager()) {
//            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
//            List<Long> siteIDs = new ArrayList<>();
//            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));
//
//            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
//                return new Response<>(false, "Thông tin bệnh án không tồn tại");
//            }
//        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
//            return new Response<>(false, "Thông tin bệnh án không tồn tại");
//        }
//
//        // validate các trường nhập
//        form.setFromPatientEntity(arvEntity.getPatient());
//        form.setCurrentSiteID(currentUser.getSite().getID());
//        form.setFirstTreatmentTime(TextUtils.formatDate(arvEntity.getFirstTreatmentTime(), FORMATDATE));
//
//        opcStageValidate.validate(form, bindingResult);
//        if (bindingResult.hasErrors()) {
//            Map<String, String> errors = new HashMap<>();
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                errors.put(error.getCode(), error.getDefaultMessage());
//            }
//            return new Response<>(false, "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin", errors);
//        }
//
//        if (isOpcManager() && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
//            return new Response<>(false, "Khoa điều trị không được thực hiện chuyển gửi bệnh nhân");
//        }
//
//        OpcStageEntity opcStageEntity = new OpcStageEntity();
//        OpcStageEntity stageEntity = form.convertStageForm(opcStageEntity);
//        stageEntity.setPatientID(arvEntity.getPatientID());
//
//        // Validate khoảng thời gian điều trị
//        if (!opcStageService.validateStage(stageEntity)) {
//            return new Response<>(false, "Thời gian nhập của giai đoạn điều trị không hợp lệ hoặc ngày kết thúc của đợt điều trị nhập trong quá khứ đang trống");
//        }
//
//        stageEntity.setArvID(arvID);
//
//        if (isOpcManager()) {
//            stageEntity.setSiteID(arvEntity.getSiteID());
//        } else {
//            stageEntity.setSiteID(currentUser.getSite().getID());
//        }
//
//        try {
//            // Set thông tin chuyển gửi nếu đủ điều kiện
//            if (StringUtils.isNotEmpty(stageEntity.getEndCase()) && stageEntity.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
//                stageEntity.setTranferFromTime(new Date());
//            }
//
//            stageEntity = opcStageService.save(stageEntity);
//            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), stageEntity.getID(), stageEntity.getID() == null ? "Đợt điều trị được thêm mới thành công" : "Đợt điều trị được cập nhật thành công");
//        } catch (Exception ex) {
//            return new Response<>(false, "Lưu thông tin thất bại");
//        }
//        return new Response<>(true, "Đợt điều trị được thêm mới thành công", stageEntity);
//    }

    /**
     * Xem lịch sử thông tin điều trị
     *
     * @param arvID
     * @param stageID
     * @return
     */
    @RequestMapping(value = "/opc-stage/log.json", method = RequestMethod.GET)
    public Response<?> actionStageLog(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                return new Response<>(false, "Thông tin bệnh án không tồn tại");
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Thông tin bệnh án không tồn tại");
        }

        OpcStageEntity opcStageEntity = opcStageService.findOne(stageID);

        if (opcStageEntity == null || !opcStageEntity.getPatientID().equals(arvEntity.getPatientID())) {
            return new Response<>(false, "Không tìm thấy thông tin đợt điều trị: " + stageID);
        }

        // Lấy thông tin lịch sử
        List<OpcArvLogEntity> logs = opcStageService.getLogs(arvEntity.getID(), opcStageEntity.getID());
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }

        Map<String, Object> data = new HashMap();
        data.put("logs", logs);
        data.put("staffs", staffs);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/opc-stage/log-create.json", method = RequestMethod.POST)
    public Response<?> actionStageLogCreate(@RequestBody OpcArvLogEntity arvLogEntity, BindingResult bindingResult) {
        OpcArvLogEntity logStage = opcStageService.logStage(arvLogEntity.getArvID(), arvLogEntity.getPatientID(), arvLogEntity.getStageID(), arvLogEntity.getContent());
        if (logStage == null) {
            return new Response<>(false, "Cập nhật thông tin thất bại");
        }
        return new Response<>(true, "Cập nhật thông tin thành công");
    }

    /**
     * Validate trường hợp bệnh nhân điều trị lần đầu tiên tại cơ sở Có 1 bản
     * ghi tại giai đoạn điều trị chưa có ngày điều trị và kết thúc
     *
     * @param arvID
     * @return
     */
    @RequestMapping(value = "/opc-stage/new-validate.json", method = RequestMethod.GET)
    public Response<?> actionNewStageValidate(@RequestParam(name = "arvid") Long arvID) {

        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        List<OpcStageEntity> stages = opcStageService.findByPatientID(arvEntity.getPatientID(), false);
        if (stages == null || stages.isEmpty()) {
            return new Response<>(true, "ok");
        }

        List<OpcStageEntity> stageChecked = new ArrayList<>();
        for (OpcStageEntity elm : stages) {
            if (!elm.getArvID().equals(arvID)) {
                continue;
            }
            stageChecked.add(elm);
        }

        if (!stageChecked.isEmpty()) {
            if (stageChecked.size() > 1) {
                return new Response<>(true, "ok");
            } else {
                return new Response<>(!("0".equals(stageChecked.get(0).getStatusOfTreatmentID()) || stageChecked.get(0).getEndTime() == null) ,("0".equals(stageChecked.get(0).getStatusOfTreatmentID()) || stageChecked.get(0).getEndTime() == null) ? "Không thể thêm đợt điều trị mới trong khi đợt điều trị hiện tại chưa được cập nhật thông tin điều trị hoặc kết thúc" : "ok");
            }
        } else {
            return new Response<>(true, "ok");
        }
    }
}
