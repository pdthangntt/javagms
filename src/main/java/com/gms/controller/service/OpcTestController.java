package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.opc_arv.OpcTestForm;
import com.gms.entity.validate.OpcTestValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcTestService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
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
 * @author pdThang
 */
@RestController
public class OpcTestController extends OpcController {

    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcTestService testService;
    @Autowired
    private OpcTestValidate validate;

    @RequestMapping(value = "/opc-test/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "arvid") Long arvID, @RequestParam(name = "id") Long ID) {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }
        OpcTestEntity model = testService.findOne(ID);
        if (model == null ) {
            return new Response<>(false, "Không tìm thấy lần khám");
        }
        OpcTestForm form = new OpcTestForm();
        return new Response<>(true, form.setFrom(model));
    }

    @RequestMapping(value = "/opc-test/new.json", method = RequestMethod.POST)
    public Response<?> actionNew(@RequestParam(name = "arvid") Long arvID,
            @RequestBody @Valid OpcTestForm form,
            BindingResult bindingResult) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            if (arv == null) {
                return new Response<>(false, "Không tìm thấy bệnh án");
            }
            form.setSiteID(String.valueOf(arv.getSiteID()));
            form.setFirstCd4Time(TextUtils.formatDate(arv.getFirstCd4Time(), FORMATDATE));
            if (isOpcManager() && form.getSiteID() == null) {
                return new Response<>(false, "Vui lòng chọn cơ sở điều trị");
            }
            //Validate
            validate.validate(form, bindingResult);
            //Trả lỗi
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (ObjectError error : bindingResult.getAllErrors()) {
                    errors.put(error.getCode(), error.getDefaultMessage());
                }
                return new Response<>(false, "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin", errors);
            }
            OpcTestEntity model = form.toForm(new OpcTestEntity());
            model.setArvID(arv.getID());
            model.setPatientID(arv.getPatientID());
            if (isOpcManager()) {
                model.setSiteID(arv.getSiteID());
            } else {
               model.setSiteID(currentUser.getSite().getID());
            }
            testService.save(model);
            testService.logTest(model.getArvID(), model.getPatientID(), model.getID(), "Thêm mới lượt xét nghiệm");
            return new Response<>(true, "Thêm mới thành công lượt xét nghiệm", model);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Response<>(false, ex.getMessage());
        }
    }

    @RequestMapping(value = "/opc-test/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdate(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            @RequestBody @Valid OpcTestForm form,
            BindingResult bindingResult) {
        try {

            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            if (arv == null) {
                return new Response<>(false, "Không tìm thấy bệnh án");
            }
            OpcTestEntity model = testService.findOne(ID);
            if (model == null) {
                return new Response<>(false, "Không tìm thấy lần khám");
            }
            //Validate
            form.setFirstCd4Time(TextUtils.formatDate(arv.getFirstCd4Time(), FORMATDATE));
            validate.validate(form, bindingResult);
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
            testService.save(model);
            testService.logTest(model.getArvID(), model.getPatientID(), model.getID(), "Cập nhật lượt xét nghiệm");
            return new Response<>(true, "Cập nhật thành công lượt xét nghiệm", model);
        } catch (Exception ex) {
            return new Response<>(false, ex.getMessage());
        }
    }

    @RequestMapping(value = "/opc-test/logs.json", method = RequestMethod.GET)
    public Response<?> actionLogs(@RequestParam(name = "oid") Long ID) {
        OpcTestEntity stage = testService.findOne(ID);
        if (stage == null) {
            return new Response<>(false, "Không tìm thấy thông tin đợt điều trị");
        }

        // Lấy thông tin lịch sử
        List<OpcArvLogEntity> logs = testService.getLogs(stage.getArvID(), stage.getID());
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

    @RequestMapping(value = "/opc-test/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody OpcArvLogEntity log, BindingResult bindingResult) {
        OpcTestEntity test = testService.findOne(log.getTestID());
        if (test == null) {
            return new Response<>(false, "Không tìm thấy thông tin");
        }
        OpcArvLogEntity logStage = testService.logTest(test.getArvID(), test.getPatientID(), test.getID(), log.getContent());
        if (logStage == null) {
            return new Response<>(false, "Ghi chú thất bại");
        }
        return new Response<>(true, "Ghi chú thành công");
    }

}
