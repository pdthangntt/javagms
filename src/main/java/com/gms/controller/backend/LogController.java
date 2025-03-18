package com.gms.controller.backend;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcConfirmLogEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcLaytestLogEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.HtcVisitLogEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvLogEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.PacPatientLogEntity;
import com.gms.entity.db.PqmLogApiEntity;
import com.gms.entity.db.StaffActivityEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.input.ApiArvLogSearch;
import com.gms.entity.input.LogSearch;
import com.gms.service.AuthService;
import com.gms.service.CommonService;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcLaytestService;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcBaseService;
import com.gms.service.PacPatientService;
import com.gms.service.PqmLogApiService;
import com.gms.service.SiteService;
import com.gms.service.StaffActivityService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_log")
public class LogController extends BaseController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private HtcLaytestService htcLaytestService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private AuthService authService;
    @Autowired
    private StaffActivityService activityService;
    @Autowired
    private PqmLogApiService apiService;

    /**
     * pdthang Lịch sử hoạt động
     *
     * @param model
     * @param tab
     * @param ID
     * @param from
     * @param to
     * @param requestMethod
     * @param staffID
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = {"/log/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "request_method", required = false) String requestMethod,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);
        search.setRequestMethod(requestMethod);

        DataPage<StaffActivityEntity> dataPage = activityService.findAll(search);

        List<AuthActionEntity> actionEntitys = authService.findAllAction();
        HashMap<String, String> actions = new HashMap<>();
        for (AuthActionEntity action : actionEntitys) {
            actions.put(action.getName(), action.getTitle());
        }
        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }
        
        HashMap<String,String> requestMethodOption = new HashMap<>();
        requestMethodOption.put("", "Tất cả");
        requestMethodOption.put("1", "GET");
        requestMethodOption.put("2", "POST");
        
        model.addAttribute("title", "Quản lý lịch sử hành động");
        model.addAttribute("actions", actions);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("requestMethodOption", requestMethodOption);
        return "backend/log/index";
    }

    @RequestMapping(value = {"/log/visit.html"}, method = RequestMethod.GET)
    public String actionVisit(Model model,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);

        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }

        DataPage<HtcVisitLogEntity> dataPage = htcVisitService.findAllLog(search);
        Set<Long> patientNameIDs = new HashSet<>();
        Map<String, String> patientNames = new HashMap<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            patientNameIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getHtcVisitID")));// get ID
        }
        if (!patientNameIDs.isEmpty()) {
            for (HtcVisitEntity item : htcVisitService.findAllByIDs(patientNameIDs)) {
                patientNames.put(String.valueOf(item.getID()), item.getPatientName());
            }
        }

        model.addAttribute("title", "Quản lý log hệ thống");
        model.addAttribute("patientNames", patientNames);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/visit";
    }

    @RequestMapping(value = {"/log/confirm.html"}, method = RequestMethod.GET)
    public String actionConfirm(Model model,
           @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);

        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }

        DataPage<HtcConfirmLogEntity> dataPage = confirmService.findAllLog(search);
        List<Long> patientNameIDs = new ArrayList<>();
        Map<String, String> patientNames = new HashMap<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            patientNameIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getHtcConfirmID")));// get ID
        }
        if (!patientNameIDs.isEmpty()) {
            for (HtcConfirmEntity item : confirmService.findAllByIds(patientNameIDs)) {
                patientNames.put(String.valueOf(item.getID()), item.getFullname());
            }
        }

        model.addAttribute("title", "Quản lý log hệ thống");
        model.addAttribute("patientNames", patientNames);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/confirm";
    }

    @RequestMapping(value = {"/log/laytest.html"}, method = RequestMethod.GET)
    public String actionLaytest(Model model,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);

        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }

        DataPage<HtcLaytestLogEntity> dataPage = htcLaytestService.findAllLog(search);
        Set<Long> patientNameIDs = new HashSet<>();
        Map<String, String> patientNames = new HashMap<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            patientNameIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getHtcLaytestID")));// get ID
        }
        if (!patientNameIDs.isEmpty()) {
            for (HtcLaytestEntity item : htcLaytestService.findAllByIDs(patientNameIDs)) {
                patientNames.put(String.valueOf(item.getID()), item.getPatientName());
            }
        }

        model.addAttribute("title", "Quản lý log hệ thống");
        model.addAttribute("patientNames", patientNames);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/laytest";
    }

    /**
     * pdThang
     * @param model
     * @param ID
     * @param from
     * @param to
     * @param staffID
     * @param page
     * @param size
     * @return 
     */
    @RequestMapping(value = {"/log/pac.html"}, method = RequestMethod.GET)
    public String actionPac(Model model,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);

        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }

        DataPage<PacPatientLogEntity> dataPage = pacPatientService.findAllLog(search);
        List<Long> patientNameIDs = new ArrayList<>();
        Map<String, String> patientNames = new HashMap<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            patientNameIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getPatientID")));// get ID
        }
        if (!patientNameIDs.isEmpty()) {
            for (PacPatientInfoEntity item : pacPatientService.findAllByIds(patientNameIDs)) {
                patientNames.put(String.valueOf(item.getID()), item.getFullname());
            }
        }

        model.addAttribute("title", "Quản lý log hệ thống");
        model.addAttribute("patientNames", patientNames);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/pac";
    }
    
    
    /**
     * pdThang
     * @param model
     * @param ID
     * @param from
     * @param to
     * @param staffID
     * @param page
     * @param size
     * @return 
     */
    @RequestMapping(value = {"/log/opc.html"}, method = RequestMethod.GET)
    public String actionOpc(Model model,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "staff_id", required = false) String staffID,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LogSearch search = new LogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        try {
            search.setID(ID == null || "".equals(ID) ? Long.valueOf(0) : Long.valueOf(ID));
        } catch (Exception e) {
            search.setID(Long.valueOf(-1));
        }
        try {
            search.setStaffID(staffID == null || "".equals(staffID) ? Long.valueOf(0) : Long.valueOf(staffID));
        } catch (Exception e) {
            search.setStaffID(Long.valueOf(-1));
        }

        search.setFrom(from);
        search.setTo(to);
        search.setCategory(category);

        Map<String, String> staffNames = new HashMap<>();
        for (StaffEntity staffEntity : staffService.findAll()) {
            staffNames.put(String.valueOf(staffEntity.getID()), staffEntity.getName());
        }

        DataPage<OpcArvLogEntity> dataPage = opcArvService.findAllLog(search);
        Set<Long> patientNameIDs = new HashSet<>();
        List<Long> arvIDs = new ArrayList<>();
        Map<String, String> patientNames = new HashMap<>();
        Map<String, String> arvCodes = new HashMap<>();
        Map<String, String>  categorys = new HashMap<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            patientNameIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getPatientID")));// get ID
            arvIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getArvID")));// get ID
            
        }
        if (!patientNameIDs.isEmpty()) {
            for (OpcPatientEntity item : opcArvService.getPatientsByIds(patientNameIDs)) {
                patientNames.put(String.valueOf(item.getID()), item.getFullName());
            }
        }
        if (!arvIDs.isEmpty()) {
            for (OpcArvEntity item : opcArvService.findAllByIds(arvIDs)) {
                arvCodes.put(String.valueOf(item.getID()), item.getCode());
            }
        }
        
         categorys.put("", "Tất cả");
         categorys.put("1", "Bệnh án");
         categorys.put("2", "Giai đoạn điều trị");
         categorys.put("3", "Lượt khám");
         categorys.put("4", "Xét nghiệm & dự phòng");
         categorys.put("5", "Tải lượng virus");

        model.addAttribute("title", "Quản lý log hệ thống");
        model.addAttribute("patientNames", patientNames);
        model.addAttribute("arvCodes", arvCodes);
        model.addAttribute("categorys",  categorys);
        model.addAttribute("staffNames", staffNames);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/opc";
    }
    
    @RequestMapping(value = {"/log/pqm-api.html"}, method = RequestMethod.GET)
    public String actionPqmApi(Model model,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        ApiArvLogSearch search = new ApiArvLogSearch();
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setFrom(from);
        search.setTo(to);
        search.setStatus(status);

        Map<String, String> statusOptions = new HashMap<>();
        statusOptions.put(null, "Tất cả");
        statusOptions.put("1", "Thành công");
        statusOptions.put("2", "Không thành công");

        DataPage<PqmLogApiEntity> dataPage = apiService.find(search);

        model.addAttribute("title", "Quản lý log PQM API");
        model.addAttribute("statusOptions", statusOptions);
        model.addAttribute("dataPage", dataPage);
        return "backend/log/pqm_api";
    }
}
