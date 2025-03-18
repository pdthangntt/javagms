package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcVisitService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vvThành
 */
@RestController
public class OpcVisitController extends OpcController {

    @Autowired
    private OpcVisitService visitService;
    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcStageService stageService;

    @RequestMapping(value = "/opc-visit/logs.json", method = RequestMethod.GET)
    public Response<?> actionLogs(@RequestParam(name = "oid") Long ID) {
        OpcVisitEntity visit = visitService.findOne(ID);
        if (visit == null) {
            return new Response<>(false, "Không tìm thấy thông tin lượt khám");
        }

        // Lấy thông tin lịch sử
        List<OpcArvLogEntity> logs = visitService.getLogs(visit.getArvID(), visit.getID());
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

    @RequestMapping(value = "/opc-visit/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody OpcArvLogEntity log, BindingResult bindingResult) {
        OpcVisitEntity visit = visitService.findOne(log.getTestID());
        if (visit == null) {
            return new Response<>(false, "Không tìm thấy thông tin");
        }
        OpcArvLogEntity logStage = visitService.logVisit(visit.getArvID(), visit.getPatientID(), visit.getID(), log.getContent());
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
    @RequestMapping(value = "/opc-visit/log.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<OpcArvLogEntity> logs = arvService.getVisitLogs(oID);
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
    @RequestMapping(value = "/opc-visit/log-creates.json", method = RequestMethod.POST)
    public Response<?> actionvViralLogCreate(@RequestBody OpcArvLogEntity form, BindingResult bindingResult) {
        OpcArvEntity arv = arvService.findOne(form.getArvID());
        if (arv == null) {
            return new Response<>(false, "Không tìm thấy bệnh án");
        }
        arvService.logVisit(arv.getID(), arv.getPatientID(), arv.getID(), form.getContent());
        return new Response<>(true, "Ghi chú thành công");
    }
    
    @RequestMapping(value = "/opc-visit/get-date.json", method = RequestMethod.POST)
    public Response<?> actionGetDate(@RequestParam(name = "oid") Long oID) {
        OpcStageEntity stage = stageService.findOne(oID);
        Map<String, Object> data = new HashMap<>();
        data.put("statusOfTreatmentID", stage.getStatusOfTreatmentID());
        data.put("treatmentRegimenStage", stage.getTreatmentRegimenStage());
        data.put("treatmentRegimenID", stage.getTreatmentRegimenID());
        data.put("registrationTime", TextUtils.formatDate(stage.getRegistrationTime(), FORMATDATE));
        data.put("treatmentTime", TextUtils.formatDate(stage.getTreatmentTime(), FORMATDATE));
        data.put("endTime", TextUtils.formatDate(stage.getEndTime(), FORMATDATE));
        return new Response<>(true, data);
    }
}
