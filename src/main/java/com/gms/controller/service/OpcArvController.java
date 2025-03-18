package com.gms.controller.service;

import com.gms.components.QLNNUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.ExchangeServiceEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.opc_arv.TransferTreatmentForm;
import com.gms.service.CommonService;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpcArvController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private HtcVisitService htcService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private StaffService staffServices;
    @Autowired
    private SiteService siteServices;
    @Autowired
    private QLNNUtils qlnnUtils;

    /**
     * Tạo log bệnh án
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/opc-arv/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody OpcArvLogEntity form, BindingResult bindingResult) {
        OpcArvEntity arv = opcArvService.findOne(form.getArvID());
        if (arv == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }
        opcArvService.logArv(arv.getID(), arv.getPatientID(), form.getContent());
        return new Response<>(true, "Ghi chú thành công");
    }

    /**
     * Danh sách lịch sử bệnh án
     *
     * @param oID
     * @return
     */
    @RequestMapping(value = "/opc-arv/log.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<OpcArvLogEntity> logs = opcArvService.getLogs(oID);
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", logs);
        data.put("staffs", staffs);
        return new Response<>(true, data);
    }

    /**
     * Chi tiết bệnh án
     *
     * @param oid
     * @return
     */
    @RequestMapping(value = "/opc-arv/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oid) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcArvEntity entity = opcArvService.findOne(Long.parseLong(oid));
        if (entity == null || entity.isRemove()
                || (!entity.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (!options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        List<OpcStageEntity> stages = opcStageService.findByPatientID(entity.getPatientID(), false);
        if (stages != null && stages.size() > 0) {
            OpcStageEntity result = stages.get(0);
            data.put("stageID", result.getID());
        }
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("");
        data.put("currentUserName", currentUser.getUser().getName());
        data.put("options", options);
        data.put("model", entity);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/opc-arv/treatment-transfer.json", method = RequestMethod.POST)
    public Response<?> actionTransfer(@RequestParam(name = "oid") String oid,
            @RequestParam(name = "stage_id") String stageID,
            @RequestBody TransferTreatmentForm form) {
        Map<String, String> errors = new HashMap();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        LoggedUser currentUser = getCurrentUser();
        if ("null".equals(form.getTransferSiteID())) {
            form.setTransferSiteID("");
        }
        sdfrmt.setLenient(false);
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcArvEntity entity = opcArvService.findOne(Long.parseLong(oid));
        OpcStageEntity stage;
        String tranferFromTime = form.getTranferFromTime();
        if (StringUtils.isNotEmpty(tranferFromTime) && !tranferFromTime.contains("/")) {
            tranferFromTime = tranferFromTime.substring(0, 2) + "/" + tranferFromTime.substring(2, 4) + "/" + tranferFromTime.substring(4, tranferFromTime.length());
        }
        String endTime = form.getEndTime();
        if (StringUtils.isNotEmpty(endTime) && !endTime.contains("/")) {
            endTime = endTime.substring(0, 2) + "/" + endTime.substring(2, 4) + "/" + endTime.substring(4, endTime.length());
        }
        if (StringUtils.isEmpty(stageID)) {
            stage = new OpcStageEntity();
        } else {
            stage = opcStageService.findOne(Long.parseLong(stageID));
        }
        if (StringUtils.isEmpty(form.getTranferFromTime())) {
            errors.put("tranferFromTime", "Ngày chuyển gửi không được để trống");
        }

        if (StringUtils.isEmpty(endTime)) {
            errors.put("endTime", "Ngày kết thúc không được để trống");
        }

        if (StringUtils.isNotEmpty(tranferFromTime)) {
            try {
                Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
                if (now.compareTo(sdfrmt.parse(tranferFromTime)) < 0) {
                    errors.put("tranferFromTime", "Ngày chuyển gửi không được lớn hơn ngày hiện tại");
                }
            } catch (ParseException ex) {
                errors.put("tranferFromTime", "Ngày chuyển gửi không đúng định dạng");
            }
        }

        if (StringUtils.isNotEmpty(endTime)) {
            try {
                Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
                if (now.compareTo(sdfrmt.parse(endTime)) < 0) {
                    errors.put("endTime", "Ngày kết thúc không được lớn hơn ngày hiện tại");
                }
            } catch (ParseException ex) {
                errors.put("endTime", "Ngày kết thúc không đúng định dạng");
            }
        }

        if (StringUtils.isNotEmpty(tranferFromTime) && StringUtils.isNotEmpty(endTime)) {
            try {
                Date tranferFromTimeValidate = sdfrmt.parse(tranferFromTime);
                Date endTimeValidate = sdfrmt.parse(endTime);
                if (tranferFromTimeValidate.compareTo(endTimeValidate) != 0) {
                    errors.put("tranferFromTime", "Ngày chuyển đi phải bằng ngày kết thúc");
                }
            } catch (ParseException ex) {
            }
        }

        if (entity.getTreatmentTime() != null && StringUtils.isNotEmpty(endTime)) {
            try {
                Date treatmentTimeValidate = sdfrmt.parse(TextUtils.formatDate(entity.getTreatmentTime(), FORMATDATE));
                Date endTimeValidate = sdfrmt.parse(endTime);
                if (treatmentTimeValidate.compareTo(endTimeValidate) > 0) {
                    errors.put("endTime", "Ngày kết thúc không được nhỏ hơn Ngày điều trị ARV");
                }
            } catch (ParseException ex) {
            }
        }

        if (entity.getRegistrationTime() != null && StringUtils.isNotEmpty(endTime)) {
            try {
                Date registrationTimeValidate = sdfrmt.parse(TextUtils.formatDate(entity.getRegistrationTime(), FORMATDATE));
                Date endTimeValidate = sdfrmt.parse(endTime);
                if (registrationTimeValidate.compareTo(endTimeValidate) > 0) {
                    errors.put("endTime", "Ngày kết thúc không được nhỏ hơn ngày đăng ký");
                }
            } catch (ParseException ex) {
            }
        }
        String transferSiteID = form.getTransferSiteID();
        String transferSiteName = form.getTransferSiteName();
        String transferCase = form.getTransferCase();
        if (StringUtils.isEmpty(transferSiteID) || "null".equals(transferSiteID)) {
            errors.put("transferSiteID", "Cơ sở chuyển đi không được để trống");
        }
        if ("-1".equals(transferSiteID) && StringUtils.isEmpty(transferSiteName)) {
            errors.put("transferSiteName", "Cơ sở chuyển đi không được để trống");
        }

        if ("-1".equals(transferSiteID) && StringUtils.isNotEmpty(transferSiteName) && !"null".equals(transferSiteName) && transferSiteName.length() > 255) {
            errors.put("transferSiteName", "Cơ sở chuyển đi không được quá 255 ký tự");
        }

        if (StringUtils.isNotEmpty(transferCase) && transferCase.length() > 200) {
            errors.put("transferCase", "Lý do chuyển không được quá 200 ký tự");
        }

        if (StringUtils.isNotEmpty(transferSiteID) && Long.parseLong(transferSiteID) == currentUser.getSite().getID()) {
            return new Response<>(false, "Không thể chuyển gửi cho cơ sở hiện tại");
        }

        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }

        entity.setTranferFromTime(TextUtils.convertDate(tranferFromTime, FORMATDATE));
        entity.setTransferSiteID(Long.parseLong(transferSiteID));
        entity.setTransferSiteName(transferSiteName);
        entity.setTransferCase(transferCase);
        entity.setEndCase("3");
        entity.setEndTime(StringUtils.isEmpty(endTime) ? new Date() : TextUtils.convertDate(endTime, FORMATDATE));
        entity.setTreatmentStageID("3");
        entity.setTreatmentStageTime(new Date());
        entity.setTranferBY(getCurrentUser().getUser().getID());
        entity.setSourceArvCode(entity.getCode());

        stage.setRegistrationTime(entity.getRegistrationTime());
        stage.setArvID(entity.getID());
        stage.setPatientID(entity.getPatientID());
        stage.setEndCase("3");
        stage.setEndTime(StringUtils.isEmpty(endTime) ? new Date() : TextUtils.convertDate(endTime, FORMATDATE));
        stage.setTransferSiteID(Long.parseLong(transferSiteID));
        stage.setTransferSiteName(transferSiteName);
        stage.setTransferCase(transferCase);
        stage.setTreatmentStageID("3");
        stage.setTreatmentStageTime(new Date());
        stage.setTranferFromTime(TextUtils.convertDate(tranferFromTime, FORMATDATE));
        stage.setTranferBY(getCurrentUser().getUser().getID());
        stage.setCreateAT(new Date());
        stage.setCreatedBY(getCurrentUser().getUser().getID());
        stage.setUpdateAt(new Date());
        stage.setUpdatedBY(getCurrentUser().getUser().getID());
        try {

            opcArvService.update(entity);
            String siteName = "-1".equals(entity.getTransferSiteID().toString()) ? entity.getTransferSiteName() : options.get(ParameterEntity.TREATMENT_FACILITY).get(entity.getTransferSiteID().toString());
            opcArvService.logArv(entity.getID(), entity.getPatientID(), "Bệnh nhân được chuyển tới cơ sở " + siteName);
            opcStageService.save(stage);
            if (StringUtils.isEmpty(stageID)) {
                opcStageService.logStage(entity.getID(), entity.getPatientID(), stage.getID(), "Thêm mới giai đoạn điều trị");
            } else {
                opcStageService.logStage(entity.getID(), entity.getPatientID(), stage.getID(), "Cập nhật giai đoạn điều trị");
            }

            //Gửi email thông báo
            Map<String, Object> variables = new HashMap<>();
            SiteEntity transferSite = siteService.findOne(Long.parseLong(transferSiteID));
            variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + entity.getCode());
            variables.put("transferSiteName", transferSite == null ? transferSiteName : transferSite.getName());
            variables.put("currentSiteName", getCurrentUser().getSite().getName());
            variables.put("gender", options.get(ParameterEntity.GENDER).get(entity.getPatient().getGenderID()));
            variables.put("fullName", entity.getPatient().getFullName());
            variables.put("dob", TextUtils.formatDate(entity.getPatient().getDob(), FORMATDATE));

            variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));

            sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển tiếp điều trị bệnh nhân mã %s", entity.getCode()), EmailoutboxEntity.ARV_TRANSFER_MAIL, variables);
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
        return new Response<>(true, "Bệnh nhân được chuyển gửi tới cơ sở khác thành công.", entity);
    }

    @RequestMapping(value = "/opc-arv/get-feedback.json", method = RequestMethod.GET)
    public Response<?> actionGetFeedback(@RequestParam(name = "oid", defaultValue = "") Long ID,
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "isPaper", defaultValue = "") String isPaper) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        HtcVisitEntity oldEntityHtc = null;
        OpcArvEntity oldEntityOpc = null;
        OpcArvEntity newEntity = null;
        if (!"".equals(type)) {
            newEntity = opcArvService.findBySourceID(ID, type);
            if (newEntity == null) {
                newEntity = opcArvService.findOne(ID);
            }
            if ("100".equals(type)) {
                if ("0".equals(isPaper)) {
                    oldEntityHtc = htcService.findOne(ID);
                }

            } else {
                oldEntityOpc = opcArvService.findOne(ID);
            }
        }
        if (newEntity == null || newEntity.isRemove()
                || (!newEntity.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (!options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(newEntity.getSiteID().toString()) && isOpcManager())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu", data);
        }
        data.put("options", options);
        data.put("typeSite", type);
        data.put("oldModel", "100".equals(type) ? oldEntityHtc : oldEntityOpc);
        data.put("newModel", newEntity);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/opc-arv/feedback-receive.json", method = RequestMethod.POST)
    public Response<?> actionFeedback(@RequestParam(name = "oid", defaultValue = "") Long ID,
            @RequestParam(name = "type", defaultValue = "") String type,
            @RequestParam(name = "isPaper", defaultValue = "") String isPaper) {
        HtcVisitEntity oldEntityHtc;
        OpcArvEntity oldEntityOpc;
        OpcArvEntity newEntity;
        Set<Long> patients = new HashSet<>();
        LoggedUser currentUser = getCurrentUser();
        if ("100".equals(type)) {
            try {
                oldEntityHtc = htcService.findOne(ID);
                if (oldEntityHtc != null && "false".equals(isPaper)) {
                    newEntity = opcArvService.findBySourceID(ID, type);
                    newEntity.setFeedbackResultsReceivedTime(new Date());
                    newEntity.setUpdateAt(new Date());
                    newEntity.setUpdatedBY(currentUser.getUser().getID());
                    opcArvService.update(newEntity);
                    opcArvService.logArv(newEntity.getID(), newEntity.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + siteService.findOne(oldEntityHtc.getSiteID()).getName());
                    patients.add(oldEntityHtc.getUpdatedBY());
                    patients.add(oldEntityHtc.getCreatedBY());

                    if (StringUtils.isNotEmpty(oldEntityHtc.getArvExchangeResult()) && oldEntityHtc.getArvExchangeResult().equals(ArvExchangeEnum.DONG_Y.getKey())) {
                        oldEntityHtc.setRegisterTherapyTime(newEntity.getRegistrationTime());
                        oldEntityHtc.setRegisteredTherapySite(currentUser.getSite().getName());
                        oldEntityHtc.setTherapyRegistProvinceID(currentUser.getSite().getProvinceID());
                        oldEntityHtc.setTherapyRegistDistrictID(currentUser.getSite().getDistrictID());
                        oldEntityHtc.setTherapyNo(newEntity.getCode());
                        oldEntityHtc.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
                        oldEntityHtc.setFeedbackReceiveTime(new Date());
                    }
                    oldEntityHtc.setUpdateAt(new Date());
                    oldEntityHtc.setUpdatedBY(currentUser.getUser().getID());
                    htcService.save(oldEntityHtc);
                    //Gửi email thông báo
                    Map<String, Object> variables = new HashMap<>();
                    SiteEntity transferSite = siteService.findOne(newEntity.getSourceSiteID());
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", transferSite == null ? "" : transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", newEntity.getPatient().getFullName());
                    variables.put("confirmCode", newEntity.getPatient().getConfirmCode());
                    variables.put("tranferToTime", TextUtils.formatDate(newEntity.getTranferToTime(), FORMATDATE));
                    sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.HTC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.ARV_RECEIVE_SUCCESS_MAIL, variables);
                    staffService.sendMessage(patients, "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Khách hàng: %s", newEntity.getPatient().getConfirmCode()), String.format("%s&code=%s", UrlUtils.htcIndex("opc"), oldEntityHtc.getCode()));
                    return new Response<>(true, "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ " + siteService.findOne(oldEntityHtc.getSiteID()).getName());
                } else {
                    newEntity = opcArvService.findOne(ID);
                    String sourceSiteName = newEntity.getSourceSiteID() == -1 ? newEntity.getSourceArvSiteName() : siteService.findOne(newEntity.getSourceSiteID()).getName();
                    try {
                        newEntity.setFeedbackResultsReceivedTime(new Date());
                        opcArvService.update(newEntity);
                        opcArvService.logArv(newEntity.getID(), newEntity.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + sourceSiteName);
                    } catch (Exception ex) {
                        return new Response<>(false, "Gửi phản hồi thất bại");
                    }
                    return new Response<>(true, "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ " + sourceSiteName);
                }
            } catch (Exception ex) {
                return new Response<>(false, "Gửi phản hồi thất bại");
            }
        } else if ("103".equals(type)) {
            try {
                newEntity = opcArvService.findBySourceID(ID, type);
                oldEntityOpc = opcArvService.findOne(ID);
                if (newEntity != null) {
                    // Đồng ý phản hổi tiếp nhận
                    oldEntityOpc.setFeedbackResultsReceivedTimeOpc(new Date());
                    opcArvService.update(oldEntityOpc);//cập nhật bản ghi cũ
                    opcArvService.logArv(oldEntityOpc.getID(), oldEntityOpc.getPatientID(), "Cập nhật thông tin bệnh án");

                    newEntity.setFeedbackResultsReceivedTime(new Date());
                    opcArvService.update(newEntity);
                    opcArvService.logArv(newEntity.getID(), newEntity.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + getOptions().get("siteOpcTo").get(oldEntityOpc.getSiteID().toString()));
                    //Gửi email thông báo
                    Map<String, Object> variables = new HashMap<>();
                    SiteEntity sourceSite = siteService.findOne(newEntity.getSourceSiteID());
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", sourceSite == null ? "" : sourceSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", newEntity.getPatient().getFullName());
                    variables.put("code", newEntity.getCode());
                    variables.put("tranferToTime", TextUtils.formatDate(newEntity.getTranferToTime(), FORMATDATE));
                    sendEmail(sourceSite == null ? "" : getSiteEmail(sourceSite.getID(), ServiceEnum.OPC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
                    staffService.sendMessage(newEntity.getUpdatedBY(), "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Bệnh nhân: %s", newEntity.getCode()), String.format("%s&code=%s", UrlUtils.opcArvIndex("stage"), newEntity.getCode()));
                    return new Response<>(true, "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + sourceSite.getName());
                } else {
                    newEntity = opcArvService.findOne(ID);
                    String sourceSiteName = newEntity.getSourceSiteID() == -1 ? newEntity.getSourceArvSiteName() : siteService.findOne(newEntity.getSourceSiteID()).getName();
                    try {
                        newEntity.setFeedbackResultsReceivedTime(new Date());
                        opcArvService.update(newEntity);
                        opcArvService.logArv(newEntity.getID(), newEntity.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + sourceSiteName);
                    } catch (Exception ex) {
                        return new Response<>(false, "Gửi phản hồi thất bại");
                    }
                    return new Response<>(true, "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + sourceSiteName);
                }
            } catch (Exception ex) {
                return new Response<>(false, "Gửi phản hồi thất bại");
            }
        } else {
            newEntity = opcArvService.findOne(ID);
            String sourceSiteName = newEntity.getSourceSiteID() == -1 ? newEntity.getSourceArvSiteName() : siteService.findOne(newEntity.getSourceSiteID()).getName();
            try {

                newEntity.setFeedbackResultsReceivedTime(new Date());
                opcArvService.update(newEntity);
                opcArvService.logArv(newEntity.getID(), newEntity.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + sourceSiteName);
            } catch (Exception ex) {
                return new Response<>(false, "Gửi phản hồi thất bại");
            }
            return new Response<>(true, "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ " + sourceSiteName);
        }
    }

    @RequestMapping(value = "/opc-arv/transfer.json", method = RequestMethod.GET)
    public Response<?> actionTransfer(@RequestParam(name = "oid") String oids) {
        List<Long> confirmIds = new ArrayList<>();
        LoggedUser currentUser = getCurrentUser();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        Map<String, Object> data = new HashMap();
        List<OpcArvEntity> datas = opcArvService.findAllByIds(confirmIds);
        int i = 0;
        for (OpcArvEntity item1 : datas) {
            if (item1.getTransferTimeGSPH() == null && !item1.getStatusOfTreatmentID().equals("0")) {
                i += 1;
            }
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        HashMap<String, HashMap<String, String>> options = getOptions();
        String siteSent = "siteSent";
        options.put(siteSent, new HashMap<String, String>());
        for (SiteEntity site : sites) {
            options.get(siteSent).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("i", i);
        data.put("options", options);
        data.put("entities", datas);
        return new Response<>(true, "", data);
    }

    @RequestMapping(value = "/opc-arv/transfer-opc.json", method = RequestMethod.GET)
    public Response<?> actionTransferOPC(@RequestParam(name = "oid") String oids) throws Exception {
        List<Long> confirmIds = new ArrayList<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        List<OpcArvEntity> data = opcArvService.findAllByIds(confirmIds);
        if (data == null || data.isEmpty()) {
            return new Response<>(false, "Không tìm thấy khách hàng", data);
        }
        for (OpcArvEntity arv : data) {
            if (arv.getTransferTimeGSPH() == null && !arv.getStatusOfTreatmentID().equals("0")) {
                PacPatientInfoEntity patient = qlnnUtils.arv(arv);

                arv.setTransferTimeGSPH(new Date());
                arv.setUpdateTimeGSPH(new Date());
                arv.setGsphID(patient.getID());
                opcArvService.update(arv);
                opcArvService.logArv(arv.getID(), arv.getPatientID(), "Chuyển gửi GSPH");

                // Gửi email cho GSPH nhận
                List<SiteEntity> pacSite = siteService.findByServiceAndProvince(ServiceEnum.PAC.getKey(), getCurrentUser().getSite().getProvinceID());

                Map<String, Object> variables = new HashMap<>();
                variables.put("code", arv.getGsphID().toString());
                variables.put("fullName", arv.getPatient().getFullName());
                variables.put("sentMessage", "Chuyển gửi thông tin bệnh nhân qua quản lý người nhiễm");
                variables.put("pacSentDate", TextUtils.formatDate(arv.getTransferTimeGSPH(), "dd/MM/yyyy"));
                variables.put("sourceSentSite", getCurrentUser().getSite().getName());

                for (SiteEntity siteEntity : pacSite) {
                    String email = getSiteEmail(siteEntity.getID(), ServiceEnum.PAC);
                    if (StringUtils.isNotEmpty(email)) {
                        sendEmail(email, "[Thông báo] Chuyển gửi GSPH bệnh nhân(" + arv.getCode() + ")", EmailoutboxEntity.TEMPLATE_OPC_ARV_SENT_PAC, variables);
                    }
                }

            }
        }
        return new Response<>(true, "Bệnh nhân đã được chuyển sang báo cáo giám sát phát hiện thành công", null);
    }

//    /**
//     * Chuyển gửi sang GSPH từ OPC
//     *
//     * @param arvID
//     * @return
//     * @throws java.lang.Exception
//     */
//    @RequestMapping(value = "/opc-arv/transfer-gsph.json", method = RequestMethod.GET)
//    public Response<?> actionTransferGSPH(@RequestParam(name = "oid") Long arvID) throws Exception {
//
//        Map<String, Map<Long, String>> options = getSiteOptions();
//        OpcArvEntity arv = opcArvService.findOne(arvID);
//
//        if (arv == null) {
//            return new Response<>(false, "Thông tin bệnh án không tồn tại");
//        }
//
//        SiteEntity site = siteService.findOne(arv.getSiteID());
//        Long confirmSiteID = arv.getPatient().getConfirmSiteID();
//        SiteEntity confirmSite = siteService.findOne(confirmSiteID);
//
//        PacPatientInfoEntity patient = new PacPatientInfoEntity();
////        String bloodBase = options.get(ParameterEntity.BLOOD_BASE).getOrDefault(arv.getSiteID(), null);
//        String confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
//        String treatmentSite = options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(arv.getSiteID(), null);
//
//        // Kiểm tra mapping thông tin tham số bên quản lý người nhiễm
//        StringBuilder errorMsg = new StringBuilder();
//        String errorFlg = "";
//        errorMsg.append(String.format("Chuyển gửi không thành công. Cơ sở [%s] chưa cấu hình các thông tin quản lý người nhiễm dưới đây: ", siteService.findOne(arv.getSiteID()).getName()));
//
////        if (StringUtils.isEmpty(bloodBase)) {
////            if (arv.getSiteID() != null) {
////                ParameterEntity configOk = addConfig(ParameterEntity.BLOOD_BASE, site);
////                if (configOk == null) {
////                    errorFlg = "a";
////                    errorMsg.append("<br> - Nơi lấy mẫu xét nghiêm.");
////                }
////            } else {
////                errorFlg = "a";
////                errorMsg.append("<br> - Nơi lấy mẫu xét nghiêm.");
////            }
////        }
//        if (StringUtils.isEmpty(confirmTestSite) && !confirmSiteID.equals(-1L)) {
//            if (arv.getSiteID() != null && confirmSite != null) {
//                ParameterEntity configOk = commonService.addConfig(ParameterEntity.PLACE_TEST, confirmSite);
//                options = getSiteOptions();
//                confirmTestSite = options.get(ParameterEntity.PLACE_TEST).getOrDefault(confirmSiteID, null);
//                if (configOk == null) {
//                    errorFlg = "a";
//                    errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
//                }
//            } else {
//                errorFlg = "a";
//                errorMsg.append("<br> - Cơ sở xét nghiệm khẳng định.");
//            }
//        }
//
//        if (StringUtils.isEmpty(treatmentSite)) {
//            if (arv.getSiteID() != null) {
//                ParameterEntity configOk = commonService.addConfig(ParameterEntity.TREATMENT_FACILITY, site);
//                if (configOk == null) {
//                    errorFlg = "a";
//                    errorMsg.append("<br> - Cơ sở điều trị");
//                }
//            } else {
//                errorFlg = "a";
//                errorMsg.append("<br> - Cơ sở điều trị");
//            }
//
//        }
//
//        if (StringUtils.isNotEmpty(errorFlg)) {
//            opcArvService.logArv(arv.getID(), arv.getPatientID(), errorMsg.toString().replace("<br>", "\n"));
//            return new Response<>(false, errorMsg.toString());
//        }
//
//        OpcPatientEntity patientSource = arv.getPatient();
//
//        patient.setOpcCode(arv.getCode());
//        patient.setRaceID(patientSource.getRaceID());
//        patient.setJobID(arv.getJobID());
//        patient.setHealthInsuranceNo(arv.getInsuranceNo());
//
////        patient.setBloodBaseID(bloodBase);
//        patient.setSiteConfirmID(patientSource.getConfirmSiteID() == -1 ? patientSource.getConfirmSiteName() : confirmTestSite);
//        patient.setYearOfBirth(patientSource.getDob() != null ? Integer.parseInt(TextUtils.formatDate(patientSource.getDob(), "YYYY")) : null);
//        patient.setStatusOfTreatmentID(arv.getStatusOfTreatmentID());
//        patient.setTreatmentRegimenID(arv.getTreatmentRegimenID());
//        patient.setCdFourResultLastestDate(arv.getCd4Time());
//        patient.setCdFourResultDate(arv.getFirstCd4Time());
//        patient.setStartTreatmentTime(arv.getTreatmentTime());
//        patient.setSiteTreatmentFacilityID(treatmentSite);
//        patient.setVirusLoadArv("6".equals(arv.getFirstViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getFirstViralLoadResult());
//        patient.setVirusLoadArvDate(arv.getFirstViralLoadTime());
//        patient.setVirusLoadArvCurrent("6".equals(arv.getViralLoadResult()) ? VirusLoadResultEnum.LESS_THAN_200.getKey() : arv.getViralLoadResult());
//        patient.setVirusLoadArvLastestDate(arv.getViralLoadTime());
//        patient.setClinicalStage(arv.getClinicalStage());
//        patient.setStatusOfChangeTreatmentID(arv.getTreatmentStageID());
//
//        patient.setFullname(patientSource.getFullName());
//        patient.setGenderID(patientSource.getGenderID());
//        patient.setIdentityCard(patientSource.getIdentityCard());
//        patient.setObjectGroupID(arv.getObjectGroupID());
//
//        patient.setPermanentAddressNo(arv.getPermanentAddress());
//        patient.setPermanentAddressStreet(arv.getPermanentAddressStreet());
//        patient.setPermanentAddressGroup(arv.getPermanentAddressGroup());
//        patient.setPermanentProvinceID(arv.getPermanentProvinceID());
//        patient.setPermanentDistrictID(arv.getPermanentDistrictID());
//        patient.setPermanentWardID(arv.getPermanentWardID());
//
//        patient.setCurrentAddressNo(arv.getCurrentAddress());
//        patient.setCurrentAddressStreet(arv.getCurrentAddressStreet());
//        patient.setCurrentAddressGroup(arv.getCurrentAddressGroup());
//        patient.setCurrentProvinceID(arv.getCurrentProvinceID());
//        patient.setCurrentDistrictID(arv.getCurrentDistrictID());
//        patient.setCurrentWardID(arv.getCurrentWardID());
//
//        patient.setSourceServiceID(ServiceEnum.OPC.getKey());
//        patient.setSourceSiteID(patientSource.getSiteID());
//        patient.setSourceID(arv.getID());
//        patient.setDetectProvinceID(getCurrentUser().getSite().getProvinceID());
//        patient.setDetectDistrictID(getCurrentUser().getSite().getDistrictID());
//        patient.setProvinceID(getCurrentUser().getSite().getProvinceID());
//        patient.setDistrictID(getCurrentUser().getSite().getDistrictID());
//        patient.setConfirmTime(patientSource.getConfirmTime() == null ? null : patientSource.getConfirmTime());
//        patient.setConfirmCode(patientSource.getConfirmCode());
//
//        patient.setPatientPhone(arv.getPatientPhone());
//        patient.setDeathTime(arv.getDeathTime());
//
//        try {
//            patient.setCdFourResult(Long.parseLong(arv.getFirstCd4Result()));
//        } catch (NumberFormatException e) {
//        }
//        try {
//            patient.setCdFourResultCurrent(Long.parseLong(arv.getCd4Result()));
//        } catch (NumberFormatException e) {
//        }
//
//        commonService.sendToPacPatientOpc(arv, patient);
//
//        // Gửi email cho GSPH nhận
//        List<SiteEntity> pacSite = siteService.findByServiceAndProvince(ServiceEnum.PAC.getKey(), getCurrentUser().getSite().getProvinceID());
//
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("code", arv.getGsphID().toString());
//        variables.put("fullName", arv.getPatient().getFullName());
//        variables.put("sentMessage", "Chuyển gửi thông tin bệnh nhân qua quản lý người nhiễm");
//        variables.put("pacSentDate", TextUtils.formatDate(arv.getTransferTimeGSPH(), "dd/MM/yyyy"));
//        variables.put("sourceSentSite", getCurrentUser().getSite().getName());
//
//        for (SiteEntity siteEntity : pacSite) {
//            String email = getSiteEmail(siteEntity.getID(), ServiceEnum.PAC);
//            if (StringUtils.isNotEmpty(email)) {
//                sendEmail(email, "[Thông báo] Chuyển gửi GSPH bệnh nhân(" + arv.getCode() + ")", EmailoutboxEntity.TEMPLATE_OPC_ARV_SENT_PAC, variables);
//            }
//        }
//
//        return new Response<>(true, "Bệnh nhân đã được chuyển sang báo cáo giám sát phát hiện thành công", patient);
//    }
    /**
     * Lấy DS cơ sở chuyển đến theo loại đăng ký
     *
     * @author DSNAnh
     * @param type
     * @return
     */
    @RequestMapping(value = "/opc-arv/get-source-site.json", method = RequestMethod.GET)
    public Response<?> actionGetSourceSite(@RequestParam(name = "type") String type) {
        HashMap<String, String> sites = new HashMap<>();
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : siteHtc) {
            sites.put(String.valueOf(site.getID()), site.getShortName());
        }
        if ("3".equals(type)) {
            for (SiteEntity site : siteOpc) {
                sites.put(String.valueOf(site.getID()), site.getShortName());
            }
        }
        sites.put("-1", "Cơ sở khác");
        return new Response<>(true, "", sites);
    }

    /**
     * Lấy thông tin mapping của cơ sở hiện tại với thông tin quản lý người
     * nhiẽm
     *
     * @auth TrangBN
     * @return
     */
    private Map<String, Map<Long, String>> getSiteOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        Map<String, Map<Long, String>> options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<Long, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getSiteID() == null || entity.getSiteID().equals(Long.valueOf("0"))) {
                continue;
            }
            options.get(entity.getType()).put(entity.getSiteID(), entity.getCode());
        }

        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(site.getID(), site.getName());
        }
        return options;
    }

    /**
     * Đồng ý phản hồi
     *
     * @param oldID
     * @param newID
     * @return
     */
    @RequestMapping(value = "/opc-from-arv/feedback-opc.json", method = RequestMethod.GET)
    public Response<?> actionFeedbackOpc(@RequestParam(name = "oldID", required = false) String oldID, @RequestParam(name = "newID", required = false) String newID) {

        HashMap<String, HashMap<String, String>> options = getOptions();
        try {
            if (StringUtils.isEmpty(newID)) {
                return new Response<>(false, "Phản hồi thông tin thất bại");
            }

            OpcArvEntity arv = opcArvService.findOne(Long.parseLong(newID));

            if (StringUtils.isEmpty(oldID)) {
                arv.setFeedbackResultsReceivedTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());
            }

            if (StringUtils.isNotEmpty(oldID)) {
                OpcArvEntity cloneEntity = opcArvService.findOne(Long.parseLong(oldID));
                if (cloneEntity == null || !cloneEntity.getID().equals(arv.getSourceID())) {
                    return new Response<>(false, "Phản hồi thông tin thất bại");
                }

                // Đồng ý phản hổi tiếp nhận
                arv.setFeedbackResultsReceivedTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());
                cloneEntity.setFeedbackResultsReceivedTimeOpc(arv.getFeedbackResultsReceivedTime() != null ? arv.getFeedbackResultsReceivedTime() : new Date());
                opcArvService.update(cloneEntity);//cập nhật bản ghi cũ

                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity sourceSite = siteServices.findOne(arv.getSourceArvSiteID());
                if (sourceSite != null) {
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", sourceSite == null ? "" : sourceSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", arv.getPatient().getFullName());
                    variables.put("code", arv.getCode());
                    variables.put("tranferToTime", TextUtils.formatDate(arv.getTranferToTime(), FORMATDATE));
                    sendEmail(getSiteEmail(sourceSite.getID(), ServiceEnum.OPC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
                }
                staffServices.sendMessage(cloneEntity.getUpdatedBY(), "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Bệnh nhân: %s", cloneEntity.getCode()), String.format("%s?code=%s", UrlUtils.opcIndex(), cloneEntity.getCode()));
            }

            opcArvService.update(arv);//cập nhật bản ghi cũ
            String sourceSiteName = arv.getSourceSiteID() == null || arv.getSourceSiteID() == 0L ? "" : arv.getSourceSiteID() == -1 ? arv.getSourceArvSiteName() : options.get("siteOpcTo").get(arv.getSourceSiteID().toString());
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ " + sourceSiteName);
            return new Response<>(true, "Bệnh nhân chuyển gửi điều trị được tiếp nhận thành công");

        } catch (Exception e) {
            return new Response<>(false, "Phản hồi thông tin thất bại");
        }
    }

    /**
     * Đồng ý phản hồi HTC
     *
     * @param newID
     * @return
     */
    @RequestMapping(value = "/opc-from-htc/feedback-htc.json", method = RequestMethod.GET)
    public Response<?> actionFeedbackHtc(@RequestParam(name = "newID", required = false) String newID) {

        LoggedUser currentUser = getCurrentUser();
        try {
            if (StringUtils.isEmpty(newID)) {
                return new Response<>(false, "Phản hồi thông tin thất bại");
            }

            OpcArvEntity arv = opcArvService.findOne(Long.parseLong(newID));
            SiteEntity arvSite = siteService.findOne(arv.getSiteID());

            if (arv.getSourceSiteID() == null) {
                return new Response<>(false, "Phản hồi thông tin thất bại");
            }
            HtcVisitEntity htcEntity = new HtcVisitEntity();

            if (arv.getSourceID() == null || htcService.findOne(arv.getSourceID()) == null) {
                arv.setFeedbackResultsReceivedTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());
            } else {
                htcEntity = htcService.findOne(arv.getSourceID());
            }

            if (htcEntity.getID() != null) {
                arv.setFeedbackResultsReceivedTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());
                arv.setDateOfArrival(htcEntity.getExchangeTime());
                if (StringUtils.isNotEmpty(htcEntity.getExchangeService()) && StringUtils.isNotEmpty(htcEntity.getArvExchangeResult()) && htcEntity.getExchangeService().equals(ExchangeServiceEnum.ARV.getKey()) && htcEntity.getArvExchangeResult().equals(ArvExchangeEnum.DONG_Y.getKey())) {
                    htcEntity.setArrivalTime(arv.getTranferToTime());
                    htcEntity.setFeedbackReceiveTime(arv.getFeedbackResultsReceivedTime());
                    htcEntity.setExchangeConsultTime(arv.getTranferToTime());
                    htcEntity.setArvExchangeResult(ArvExchangeEnum.DONG_Y.getKey());
                    htcEntity.setArrivalSiteID(currentUser.getSite().getID());
                    htcEntity.setArrivalSite(currentUser.getSite().getName());
                    htcEntity.setExchangeProvinceID(arvSite.getProvinceID());
                    htcEntity.setExchangeDistrictID(arvSite.getDistrictID());
                    if (htcEntity.getResultsPatienTime() == null) {
                        htcEntity.setResultsPatienTime(new Date());
                    }
                    htcEntity.setExchangeTime(arv.getTranferToTime());
                    htcEntity.setRegisterTherapyTime(arv.getRegistrationTime());
                    htcEntity.setRegisteredTherapySite(arvSite.getName());
                    htcEntity.setTherapyNo(arv.getCode());
                    htcEntity.setTherapyRegistProvinceID(arvSite.getProvinceID());
                    htcEntity.setTherapyRegistDistrictID(arvSite.getDistrictID());
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
                }

                htcEntity.setUpdateAt(new Date());
                htcEntity.setUpdatedBY(currentUser.getUser().getID());
                htcService.save(htcEntity);
                staffService.sendMessage(htcEntity.getUpdatedBY(), "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Khách hàng: %s", arv.getPatient().getConfirmCode()), String.format("%s&code=%s", UrlUtils.htcIndex("opc"), htcEntity.getCode()));

                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(arv.getSourceSiteID());
                variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                variables.put("transferSiteName", transferSite == null ? "" : transferSite.getName());
                variables.put("currentSiteName", getCurrentUser().getSite().getName());
                variables.put("fullName", arv.getPatient().getFullName());
                variables.put("confirmCode", arv.getPatient().getConfirmCode());
                variables.put("code", arv.getCode());
                variables.put("tranferToTime", TextUtils.formatDate(arv.getTranferToTime(), FORMATDATE));
                sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.HTC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", "1".equals(arv.getRegistrationType()) ? EmailoutboxEntity.ARV_RECEIVE_SUCCESS_MAIL : EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
            }
            opcArvService.update(arv);
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + (arv.getSourceSiteID().equals(-1L) ? arv.getSourceArvSiteName() : siteService.findOne(arv.getSourceSiteID()).getName()));
            return new Response<>(true, "Gửi phản hồi tiếp nhận khách hàng chuyển gửi điều trị thành công", arv);
        } catch (Exception e) {
            return new Response<>(false, "Phản hồi thông tin thất bại");
        }
    }

    @RequestMapping(value = "/opc-arv/last-opc.json", method = RequestMethod.GET)
    public Response<?> actionGetLastVisit() {
        LoggedUser currentUser = getCurrentUser();
        String code = currentUser.getSite().getCode();
        return new Response<>(true, opcArvService.findLastBySiteID(currentUser.getSite().getID(), code));
    }

    private Map<String, Map<Long, String>> getSiteOptionsConfig(SiteEntity currentSite) {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        Map<String, Map<Long, String>> options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<Long, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getSiteID() == null || entity.getSiteID().equals(Long.valueOf("0"))) {
                continue;
            }
            options.get(entity.getType()).put(entity.getSiteID(), entity.getCode());
        }
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<Long, String>());
        if (currentSite != null) {
            List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentSite.getProvinceID());

            for (SiteEntity site : siteConfirm) {
                options.get(key).put(site.getID(), site.getName());
            }
        }

        return options;
    }
}
