package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.opc_arv.AppointmentViralForm;
import com.gms.entity.form.opc_arv.OpcViralLoadForm;
import com.gms.entity.validate.OpcAppointmentViralValidate;
import com.gms.entity.validate.OpcViralValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TrangBN
 */
@RestController
public class OpcViralController extends OpcController {

    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcViralLoadService viralLoadService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OpcViralValidate viralValidate;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcAppointmentViralValidate opcAppointmentViralValidate;

    @RequestMapping(value = "/opc-viralload/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "arvid") Long arvID, @RequestParam(name = "id") Long ID) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = arvService.findOne(arvID);
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

        OpcViralLoadEntity model = viralLoadService.findOne(ID);
        if (model == null || (!isOpcManager() && !model.getPatientID().equals(arvEntity.getPatientID()))) {
            return new Response<>(false, "Không tìm thấy lần khám");
        }

        OpcViralLoadForm form = new OpcViralLoadForm();
        form.setForm(model);
        form.setSiteID(arvEntity.getSiteID().toString());

        return new Response<>(true, form);
    }

    /**
     * Thêm mới TLVR
     *
     * @param arvID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/opc-viralload/new.json", method = RequestMethod.POST)
    public Response<?> actionNew(@RequestParam(name = "arvid") Long arvID,
            @RequestBody OpcViralLoadForm form,
            BindingResult bindingResult) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = arvService.findOne(arvID);
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
        form.setSiteID(String.valueOf(arvEntity.getSiteID()));
        form.setFirstViralLoadTime(arvEntity.getFirstViralLoadTime() == null ? null : TextUtils.formatDate(arvEntity.getFirstViralLoadTime(), "dd/MM/yyyy"));

        //Validate
        viralValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }
        OpcViralLoadEntity model = form.toForm(new OpcViralLoadEntity());
        model.setArvID(arvEntity.getID());
        model.setPatientID(arvEntity.getPatientID());
        if (isOpcManager()) {
            model.setSiteID(arvEntity.getSiteID());
        } else {
            model.setSiteID(currentUser.getSite().getID());
        }
        try {
            viralLoadService.save(model);
            viralLoadService.logViralLoad(model.getArvID(), model.getPatientID(), model.getID(), "Thêm mới tải lượng virus");
            return new Response<>(true, "Lượt xét nghiệm tải lượng virus được thêm mới thành công", model);
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
    }

    /**
     *
     *
     * @param arvID
     * @param ID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/opc-viralload/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdate(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            @RequestBody OpcViralLoadForm form,
            BindingResult bindingResult) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arvEntity = arvService.findOne(arvID);
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
            OpcViralLoadEntity model = viralLoadService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arvEntity.getPatientID())) {
                return new Response<>(false, "Không tìm thấy thông tin tải lượng virus");
            }
            //Validate
            form.setConfirmTime(TextUtils.formatDate(arvEntity.getPatient().getConfirmTime(), FORMATDATE));
            form.setFirstViralLoadTime(arvEntity.getFirstViralLoadTime() == null ? null : TextUtils.formatDate(arvEntity.getFirstViralLoadTime(), "dd/MM/yyyy"));
            viralValidate.validate(form, bindingResult);
            //Trả lỗi
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (ObjectError error : bindingResult.getAllErrors()) {
                    errors.put(error.getCode(), error.getDefaultMessage());
                }
                return new Response<>(false, "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin", errors);
            }
            //Sửa không được phép sửa cơ sở, mã bệnh án, mã bệnh nhân
            form.toForm(model);
            if (isOpcManager()) {
                model.setSiteID(arvEntity.getSiteID());
            } else {
                model.setSiteID(currentUser.getSite().getID());
            }
            viralLoadService.save(model);
            viralLoadService.logViralLoad(model.getArvID(), model.getPatientID(), model.getID(), "Cập nhật tải lượng virus");
            return new Response<>(true, "Lượt xét nghiệm tải lượng virus được cập nhật thành công", model);
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
    }

    /**
     * Lấy thông tin lịch sử
     *
     * @param ID
     * @return
     */
    @RequestMapping(value = "/opc-viralload/logs.json", method = RequestMethod.GET)
    public Response<?> actionLogs(@RequestParam(name = "oid") Long ID) {
        OpcViralLoadEntity stage = viralLoadService.findOne(ID);
        if (stage == null) {
            return new Response<>(false, "Không tìm thấy thông tin tải lượng virus");
        }

        // Lấy thông tin lịch sử
        List<OpcArvLogEntity> logs = viralLoadService.getLogs(stage.getArvID(), stage.getID());
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

    /**
     * Thêm ghi chú
     *
     * @param log
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/opc-viralload/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody OpcArvLogEntity log, BindingResult bindingResult) {
        OpcViralLoadEntity viral = viralLoadService.findOne(log.getViralLoadID());
        if (viral == null) {
            return new Response<>(false, "Không tìm thấy thông tin");
        }
        OpcArvLogEntity logStage = viralLoadService.logViralLoad(viral.getArvID(), viral.getPatientID(), viral.getID(), log.getContent());
        if (logStage == null) {
            return new Response<>(false, "Ghi chú thất bại");
        }
        return new Response<>(true, "Ghi chú thành công");
    }

    /**
     * Danh sách lịch sử bệnh án
     *
     * @param oID
     * @return
     */
    @RequestMapping(value = "/opc-viralload/log.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<OpcArvLogEntity> logs = opcArvService.getViralLogs(oID);
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
     * Tạo log bệnh án
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/opc-viralload/log-creates.json", method = RequestMethod.POST)
    public Response<?> actionvViralLogCreate(@RequestBody OpcArvLogEntity form, BindingResult bindingResult) {
        OpcArvEntity arv = opcArvService.findOne(form.getArvID());
        if (arv == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }
        opcArvService.logViralLoad(arv.getID(), arv.getPatientID(), arv.getID(), form.getContent());
        return new Response<>(true, "Ghi chú thành công");
    }

    @RequestMapping(value = "/opc-arv-virals/get.json", method = RequestMethod.GET)
    public Response<?> actionGets(@RequestParam(name = "id") String oid) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap();

        OpcArvEntity entity = opcArvService.findOne(Long.parseLong(oid));
        if (entity == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }

        AppointmentViralForm form = new AppointmentViralForm();
        form.setViralLoadCauses(entity.getViralLoadCauses());
        form.setViralLoadRetryTime(TextUtils.formatDate(entity.getViralLoadRetryTime(), "ddMMyyyy"));
        form.setNote("");

        data.put("entity", entity);
        data.put("form", form);

        return new Response<>(true, data);
    }

    @RequestMapping(value = "/opc-arv-virals/save.json", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestParam(name = "id") Long oid,
            @RequestBody AppointmentViralForm form,
            BindingResult bindingResult) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(oid);
        if (arv == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }
        //Validate
        opcAppointmentViralValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Gửi lịch hẹn xét nghiệm TLVR không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }
        try {
            //Trường hợp bản ghi không có lượt XN tlvr
            if (viralLoadService.countByArvID(oid) < 1) {
                arv.setViralLoadCauses(form.getViralLoadCauses());
                arv.setViralLoadRetryTime(TextUtils.convertDate(form.getViralLoadRetryTime(), "ddMMyyyy"));
                opcArvService.update(arv);
            } else {
                List<OpcViralLoadEntity> list = viralLoadService.findByArvIDandSiteID(oid, false, currentUser.getSite().getID());
                OpcViralLoadEntity model = list.get(0);
                model.setCauses(form.getViralLoadCauses());
                model.setRetryTime(TextUtils.convertDate(form.getViralLoadRetryTime(), "ddMMyyyy"));
                model.setNote(form.getNote());
                viralLoadService.save(model);
            }

            opcArvService.logViralLoad(arv.getID(), arv.getPatientID(), arv.getID(), "Gửi lịch hẹn xét nghiệm TLVR");
            return new Response<>(true, "Gửi lịch hẹn bệnh nhân xét nghiệm TLVR thành công");
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
    }

}
