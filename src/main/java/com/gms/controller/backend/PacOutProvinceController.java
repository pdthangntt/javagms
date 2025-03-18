package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacPatientStatusEnum;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.PacPatientLogEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.form.pac.OutProvinceFrom;
import com.gms.entity.input.PacOutProvinceFilter;
import com.gms.entity.input.PacOutProvinceSearch;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import java.util.Arrays;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Giám sát phát hiện -> Danh sách ngoại tỉnh
 *
 * @author Văn Thành
 */
@Controller(value = "backend_pac_out_province")
public class PacOutProvinceController extends PacController {
    
    @Autowired
    private PacPatientService patientService;
    @Autowired
    private PacPatientHistoryService patientHistoryService;
    @Autowired
    private PacPatientHistoryService historyService;
    
    private void addCommon(Model model, PacPatientInfoEntity entity) {
        List<PacPatientLogEntity> logs = patientService.getLogs(entity.getID());
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }
        model.addAttribute("options", getOptions());
        model.addAttribute("model", entity);
        model.addAttribute("logs", logs);
        model.addAttribute("staffs", staffs);
    }
    
    @RequestMapping(value = {"/pac-out-province/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "permanent_province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "current_province_id", required = false, defaultValue = "") String currentProvinceID,
            @RequestParam(name = "detect_province_id", required = false, defaultValue = "") String detectProvinceID,
            @RequestParam(name = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "year_of_birth", required = false, defaultValue = "") String yearOfBirth,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "identity_card", required = false, defaultValue = "") String identityCard,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBaseID,
            @RequestParam(name = "service", required = false, defaultValue = "") String sourceServiceID,
            @RequestParam(name = "site_confirm", required = false, defaultValue = "") String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false, defaultValue = "") String siteTreatmentFacilityID,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "status_patient", required = false, defaultValue = "") String statusPatient,
            @RequestParam(name = "status_manage", required = false, defaultValue = "") String statusManage,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "hiv_id", required = false) String hivID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> total = new HashMap<>();
        Set<String> tabs = new HashSet<>();
        tabs.add("");
        tabs.add("remove");
        tabs.add("vaac");
        tabs.add("pac");
        tabs.add("review");
        tabs.add("manage");
        
        OutProvinceFrom form = new OutProvinceFrom();

        //Thêm điều kiện search
        PacOutProvinceSearch search = new PacOutProvinceSearch();
        
        
        try {
            search.setHivID(StringUtils.isBlank(hivID) ? Long.valueOf("0") : (patientService.findHivInfoByCode(hivID).getID() == null ? Long.valueOf("0") : patientService.findHivInfoByCode(hivID).getID()));
        } catch (Exception e) {
            search.setHivID(Long.valueOf("-99"));
        }
        
        
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);
        search.setProvinceID(provinceID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setCurrentProvinceID(currentProvinceID);
        search.setDetectProvinceID(detectProvinceID);
        search.setFullname(fullname);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setIdentityCard(identityCard);
        search.setConfirmTimeFrom(TextUtils.validDate(confirmTimeFrom) ? confirmTimeFrom : null);
        search.setConfirmTimeTo(TextUtils.validDate(confirmTimeTo) ? confirmTimeTo : null);
        search.setBloodBaseID(bloodBaseID);
        search.setSourceServiceID(sourceServiceID);
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        search.setIndex(true);
        search.setRemove("remove".equals(search.getTab()));
        search.setStatus(status);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setStatusPatient(statusPatient);
        search.setStatusManage(statusManage);
        
        search.setTestObject(testObject);
        search.setTransmision(transmision);
        search.setStatusTreatment(statusTreatment);
        search.setStatusResident(statusResident);
        
        form.setSearch(search);
        form.setDataPage(patientService.findOutProvince(search));
        
        Set<Long> ids = new HashSet<>();
        Map<String, String> hivCode = new HashMap<>();
        ids.addAll(CollectionUtils.collect(form.getDataPage().getData(), TransformerUtils.invokerTransformer("getID")));

        List<PacHivInfoEntity> hivInfos = patientService.findHivInfoByIDs(ids);
        if (hivInfos != null && !hivInfos.isEmpty()) {
            for (PacHivInfoEntity hivInfo : hivInfos) {
                hivCode.put(hivInfo.getID().toString(), hivInfo.getCode());
            }
        }
        
        
        
        for (String tab1 : tabs) {
            total.put(tab1, patientService.countPacOutProvince(tab1).toString());
        }
        
        model.addAttribute("title", "Người nhiễm ngoại tỉnh");
        model.addAttribute("options", options);
        model.addAttribute("dataPage", form.getDataPage());
        model.addAttribute("search", form.getSearch());
        model.addAttribute("total", total);
        model.addAttribute("hivCode", hivCode);
        return "backend/pac/outprovince_index";
    }
    
    @RequestMapping(value = {"/pac-out-province/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacOutProvinceIndex());
        }

        //DS CON
        List<PacPatientHistoryEntity> childs = patientHistoryService.findByParentID(pacID);
        model.addAttribute("childs", childs);

        // Set dữ liệu hiển thị trang view
        form.setFormPac(entity);
        model.addAttribute("title", "Xem thông tin người nhiễm");
        model.addAttribute("options", getOptions());
        model.addAttribute("back", UrlUtils.pacOutProvinceIndex()); //nút quay lại
        model.addAttribute("breadcrumbUrl", UrlUtils.pacOutProvinceIndex()); // breadcrumbUrl level 2
        model.addAttribute("breadcrumbTitle", "Người nhiễm ngoại tỉnh"); // tên breadcrumbUrl level 2
        model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacOutProvinceView(pacID)); // tên breadcrumbUrl level 3
        form.setID(pacID);
        
        model.addAttribute("form", form);
        return "backend/pac/patient_view";
        
    }
    
    @RequestMapping(value = {"/pac-out-province/manage.html"}, method = RequestMethod.GET)
    public String actionManage(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "id") Long pacID) {
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null || entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        if (entity.getRefID() != null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm có mã %s đã được chuyển quản lý", pacID));
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        
        PacPatientForm form = new PacPatientForm();
        form.setID(entity.getID());
        form.setProvinceID(entity.getProvinceID());
        form.setDistrictID(entity.getDistrictID());
        form.setWardID(entity.getWardID());
        
        addCommon(model, entity);
        model.addAttribute("title", "Chuyển quản lý");
        model.addAttribute("form", form);
        return "backend/pac/outprovince_manage";
    }
    
    @RequestMapping(value = {"/pac-out-province/manage.html"}, method = RequestMethod.POST)
    public String doActionManage(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "id") Long pacID, @ModelAttribute("form") PacPatientForm form, BindingResult bindingResult) {
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null || entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        if (entity.getRefID() != null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm có mã %s đã được chuyển quản lý", pacID));
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        if (form.getProvinceID() == null || form.getProvinceID().equals("")) {
            redirectAttributes.addFlashAttribute("error", String.format("Tỉnh chuyển quản lý không được để trống!", pacID));
            return redirect(UrlUtils.pacOutProvinceManager(pacID));
        }
        
        HashMap<String, HashMap<String, String>> options = getOptions();
        addCommon(model, entity);
        model.addAttribute("title", "Chuyển quản lý");
        model.addAttribute("form", form);
        
        try {
            entity.setProvinceID(form.getProvinceID());
            entity.setDistrictID(form.getDistrictID());
            entity.setWardID(form.getWardID());
            
            PacPatientInfoEntity cpModel = (PacPatientInfoEntity) entity.clone();
            cpModel.setID(null);
            cpModel.setUpdatedPacTime(null);
            cpModel.setReviewVaacTime(null);
            cpModel.setSourceServiceID(ServiceEnum.VAAC.getKey());
            cpModel.setSourceID(entity.getID());
            cpModel.setSourceSiteID(getCurrentUser().getSite().getID());
            cpModel.setRequestVaacTime(new Date());
            patientService.save(cpModel);
            patientService.log(cpModel.getID(), "VAAC chuyển quản lý");
            
            entity.setRefID(cpModel.getID());
            patientService.save(entity);
            patientService.log(entity.getID(), "Chuyển quản lý về " + options.get("provinces").getOrDefault(entity.getProvinceID(), "#" + entity.getProvinceID()));

            //Thông báo
            SiteEntity pacManager = siteService.findByPAC(cpModel.getProvinceID());
            if (pacManager != null) {
                sendNotifyToSite(pacManager.getID(), ServiceEnum.PAC, "Đơn vị nhận được ca nhiễm #" + cpModel.getID() + " ngoại tỉnh cần rà soát", null, UrlUtils.pacNew(PacTabEnum.NEW_OTHER_PROVINCE.getKey()) + "&fullname=" + cpModel.getFullname());
            }
            
            SiteEntity pacDetect = siteService.findByPAC(entity.getDetectProvinceID());
            if (pacDetect != null) {
                sendNotifyToSite(pacDetect.getID(), ServiceEnum.PAC, "Chuyển quản lý ca nhiễm #" + cpModel.getID() + " về " + options.get("provinces").getOrDefault(entity.getProvinceID(), "#" + entity.getProvinceID()), null, UrlUtils.pacNew(PacTabEnum.NEW_OTHER_PROVINCE.getKey()) + "&fullname=" + entity.getFullname());
            }
            
            redirectAttributes.addFlashAttribute("success", "Thông tin quản lý được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());
            return redirect(UrlUtils.pacOutProvinceIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/outprovince_manage";
        }
    }
    
    @RequestMapping(value = {"/pac-out-province/review.html"}, method = RequestMethod.GET)
    public String actionReview(Model model, RedirectAttributes redirectAttributes, @RequestParam(name = "id") Long pacID) {
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null || entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        
        PacPatientForm form = new PacPatientForm();
        form.setID(entity.getID());
        form.setProvinceID(entity.getProvinceID());
        form.setNote(entity.getNote());
        
        addCommon(model, entity);
        model.addAttribute("title", "Yêu cầu rà soát");
        model.addAttribute("form", form);
        return "backend/pac/outprovince_review";
    }
    
    @RequestMapping(value = {"/pac-out-province/review.html"}, method = RequestMethod.POST)
    public String doActionReview(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "id") Long pacID, @ModelAttribute("form") PacPatientForm form, BindingResult bindingResult) {
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null || entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        if (form.getProvinceID() == null || form.getProvinceID().equals("")) {
            redirectAttributes.addFlashAttribute("error", String.format("Tỉnh yêu cầu rà soát không được để trống!", pacID));
            return redirect(UrlUtils.pacOutProvinceReview(pacID));
        }
        
        HashMap<String, HashMap<String, String>> options = getOptions();
        addCommon(model, entity);
        model.addAttribute("title", "Yêu cầu rà soát");
        model.addAttribute("form", form);
        
        if (StringUtils.isNotEmpty(form.getNote()) && form.getNote().length() > 500 ) {
            model.addAttribute("error", "Ghi chú không quá 500 ký tự");
            return "backend/pac/outprovince_review";
        }
        
        try {
            entity.setReviewVaacTime(new Date());
            entity.setNote(form.getNote());
            entity.setProvinceID(form.getProvinceID());
            entity.setDistrictID(null);
            entity.setWardID(null);
            patientService.save(entity);
            patientService.log(entity.getID(), "Yêu cầu rà soát về " + options.get("provinces").getOrDefault(entity.getProvinceID(), "#" + entity.getProvinceID()) + (form.getNote() == null || form.getNote().equals("") ? "" : ". Ghi chú: " + form.getNote()));
            redirectAttributes.addFlashAttribute("success", "Yêu cầu rà soát về " + options.get("provinces").getOrDefault(entity.getProvinceID(), "#" + entity.getProvinceID()) + " thành công");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());

            //Thông báo
            SiteEntity pacManager = siteService.findByPAC(entity.getProvinceID());
            if (pacManager != null) {
                sendNotifyToSite(pacManager.getID(), ServiceEnum.PAC, "Trung ương yêu cầu rà soát ca nhiễm #" + entity.getID(), null, null);
            }
            return redirect(UrlUtils.pacOutProvinceIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/outprovince_review";
        }
    }

    /**
     * Xóa khách hàng
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-out-province/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) {
        
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacNew());
            
        }
        
        patientService.delete(entity.getID());
        patientService.log(entity.getID(), "Xóa thông tin người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Xóa người nhiễm thành công");
        return redirect(UrlUtils.pacOutProvinceIndex());
    }

    /**
     * Khôi phục khách hàng
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/pac-out-province/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {
        
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacOutProvinceIndex());
            
        }
        
        entity.setRemove(false);
        patientService.save(entity);
        patientService.log(entity.getID(), "Khôi phục người nhiễm đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục người nhiễm đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", entity.getID());
        return redirect(UrlUtils.pacOutProvinceIndex());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-out-province/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) {
        
        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }
        
        PacPatientInfoEntity entity = patientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacOutProvinceIndex("remove"));
            
        }
        
        patientService.remove(entity.getID());
        patientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacOutProvinceIndex("remove"));
    }

    /**
     * Kiểm tra trùng lắp VAAC
     *
     * @param model
     * @param redirectAttributes
     * @param ID
     * @param page
     * @param size
     * @param input
     * @return
     */
    @RequestMapping(value = {"/pac-out-province/filter.html"}, method = RequestMethod.GET)
    public String actionFilter(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "id", required = true) Long ID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "filter", required = false, defaultValue = "fullname,gender,identity,insurance,address") String input) {
        PacPatientInfoEntity patient = patientService.findOne(ID);
        List<PacPatientHistoryEntity> history = patientHistoryService.findByDetectRefID(ID);
        PacPatientInfoEntity parents = new PacPatientInfoEntity();
        if (history != null && !history.isEmpty()) {
            parents = patientService.findOne(history.get(0).getParentID());
        }
        
        if (patient == null) {
            redirectAttributes.addFlashAttribute("error", "Ca nhiễm không tồn tại");
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        
        List<String> params = Arrays.asList(input.split(","));
        PacOutProvinceFilter filter = new PacOutProvinceFilter();
        filter.setPageIndex(page);
        filter.setPageSize(size);
        filter.setFullname(params.contains("fullname"));
        filter.setGender(params.contains("gender"));
        filter.setAddress(params.contains("address"));
        filter.setIdentity(params.contains("identity"));
        filter.setInsurance(params.contains("insurance"));
        filter.setPhone(params.contains("phone"));
        
        DataPage<PacPatientInfoEntity> dataPage = patientService.duplicateFilter(patient, filter);
        
        model.addAttribute("title", "Kiểm tra trùng lắp");
        model.addAttribute("options", getOptions());
        model.addAttribute("patient", patient);
        model.addAttribute("parents", parents);
        model.addAttribute("history", history);
        model.addAttribute("filter", filter);
        model.addAttribute("dataPage", dataPage);
        return "backend/pac/outprovince_filter";
    }

    /**
     * Đánh dấu là ca mới
     *
     * @auth vvThành
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-out-province/filter-new.html", method = RequestMethod.GET)
    public String actionFilterNew(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            PacPatientInfoEntity patient = patientService.findOne(ID);
            if (patient == null) {
                throw new Exception("Ca nhiễm không tồn tại");
            }
            if (patient.getStatus() != null && patient.getStatus().equals(PacPatientStatusEnum.OLD.getKey())) {
                throw new Exception("Ca nhiễm đã được xác định là ca cũ");
            }
            if (patient.getStatus() == null || "".equals(patient.getStatus())) {
                patient.setStatus(PacPatientStatusEnum.NEW.getKey());
            } else {
                patient.setStatus(null);
            }
            patientService.save(patient);
            patientService.log(patient.getID(), patient.getStatus() == null ? "Huỷ xác nhận ca nhiễm mới" : "Xác nhận ca nhiễm mới");
            redirectAttributes.addFlashAttribute("success", patient.getStatus() == null ? "Huỷ xác nhận ca nhiễm mới thành công" : "Xác nhận ca nhiễm mới thành công");
            return redirect(UrlUtils.pacOutProvinceFilter(ID));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
    }

    /**
     * Thêm lịch sử
     *
     * @param model
     * @param redirectAttributes
     * @param oID
     * @param cID
     * @return
     */
    @RequestMapping(value = {"/pac-out-province/filter-history.html"}, method = RequestMethod.GET)
    public String actionFilterHistory(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "oid", required = true) Long oID,
            @RequestParam(name = "cid", required = true) Long cID) {
        PacPatientInfoEntity sourcePatient = patientService.findOne(oID);
        PacPatientInfoEntity targetPatient = patientService.findOne(cID);
        
        if (sourcePatient == null || targetPatient == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin ca nhiễm");
            return redirect(UrlUtils.pacOutProvinceFilter(oID));
        }
        
        boolean sourceTreatment = StringUtils.isNotEmpty(sourcePatient.getSiteTreatmentFacilityID()) && StringUtils.isNotEmpty(sourcePatient.getStatusOfTreatmentID()) && !sourcePatient.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey()) && !sourcePatient.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        boolean targetTreatment = StringUtils.isNotEmpty(targetPatient.getSiteTreatmentFacilityID()) && StringUtils.isNotEmpty(targetPatient.getStatusOfTreatmentID()) && !targetPatient.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey()) && !targetPatient.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        
        try {
            // Cập nhật thông tin bản ghi cũ mới
            sourcePatient.setStatus(PacPatientStatusEnum.OLD.getKey());
            sourcePatient.setRefParentID(targetPatient.getID());

            // Cập nhật thông tin lịch sử
            PacPatientHistoryEntity history = new PacPatientHistoryEntity();
            history.set(sourcePatient);
            history.setID(null);
            history.setCreateAT(null);
            history.setCreatedBY(null);
            history.setDetectRefID(sourcePatient.getID());
            history.setParentID(targetPatient.getID());
            historyService.save(history);
            
            String logSource = "Trung ương xác nhận ca cũ qua việc kiểm tra trùng lắp - thêm lịch sử #" + targetPatient.getID();
            String logTarget = "Trung ương xác nhận ca nhiễm trùng lắp, thêm lịch sử.";
            if (sourceTreatment && !targetTreatment) {
                //Sét ref kết nối điều trị
                sourcePatient.setRefOpcID(targetPatient.getID());

                // Kết nối thông tin 
                targetPatient.setStatusOfTreatmentID(sourcePatient.getStatusOfTreatmentID());
                targetPatient.setStartTreatmentTime(sourcePatient.getStartTreatmentTime());
                targetPatient.setSiteTreatmentFacilityID(sourcePatient.getSiteTreatmentFacilityID());
                targetPatient.setTreatmentRegimenID(sourcePatient.getTreatmentRegimenID());
                targetPatient.setCdFourResult(sourcePatient.getCdFourResult());
                targetPatient.setCdFourResultDate(sourcePatient.getCdFourResultDate());
                targetPatient.setCdFourResultCurrent(sourcePatient.getCdFourResultCurrent());
                targetPatient.setCdFourResultLastestDate(sourcePatient.getCdFourResultLastestDate());
                targetPatient.setVirusLoadArv(sourcePatient.getVirusLoadArv());
                targetPatient.setVirusLoadArvDate(sourcePatient.getVirusLoadArvDate());
                targetPatient.setVirusLoadArvCurrent(sourcePatient.getVirusLoadArvCurrent());
                targetPatient.setVirusLoadArvLastestDate(sourcePatient.getVirusLoadArvLastestDate());
                targetPatient.setClinicalStage(sourcePatient.getClinicalStage());
                targetPatient.setSymptomID(sourcePatient.getSymptomID());
                targetPatient.setClinicalStageDate(sourcePatient.getClinicalStageDate());
                targetPatient.setAidsStatus(sourcePatient.getAidsStatus());
                targetPatient.setStatusOfChangeTreatmentID(sourcePatient.getStatusOfChangeTreatmentID());
                targetPatient.setNote(sourcePatient.getNote());
                targetPatient.setAidsStatusDate(sourcePatient.getAidsStatusDate());
                targetPatient.setChangeTreatmentDate(sourcePatient.getChangeTreatmentDate());
                targetPatient.setParentID(sourcePatient.getID()); //Set trạng thái kết nối
                logTarget += " Tự động kết nối điều trị #" + sourcePatient.getID();
                logSource += ". Kết nối điều trị";
            }
            patientService.save(sourcePatient);
            patientService.log(sourcePatient.getID(), logSource);
            
            patientService.save(targetPatient);
            patientService.log(targetPatient.getID(), logTarget);
            SiteEntity pacManager = siteService.findByPAC(targetPatient.getProvinceID());
            if (pacManager != null) {
                sendNotifyToSite(pacManager.getID(), ServiceEnum.PAC, "Trung ương xác định ca cũ trùng lắp với ca nhiễm có mã #" + targetPatient.getID(), null, getUrl(targetPatient));
            }
            
            SiteEntity pacDetect = siteService.findByPAC(sourcePatient.getDetectProvinceID());
            if (pacDetect != null) {
                sendNotifyToSite(pacDetect.getID(), ServiceEnum.PAC, "Trung ương xác nhận ca nhiễm có mã #" + sourcePatient.getID() + " có thông tin trùng lắp", null, getUrl(sourcePatient));
            }
            
            redirectAttributes.addFlashAttribute("success", "Thêm mới lịch sử thành công");
            return redirect(UrlUtils.pacOutProvinceFilter(oID));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm mới lịch sử không thành công. " + e.getMessage());
            return redirect(UrlUtils.pacOutProvinceFilter(oID));
        }
    }

    /**
     * Lấy URL theo hiển thị trang view
     *
     * @param patient
     * @return
     */
    private String getUrl(PacPatientInfoEntity patient) {
        String url = "";
        if (patient.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) {
            url = UrlUtils.pacOpcView("", patient.getID());
        } else if (patient.getAcceptTime() != null && patient.getReviewWardTime() == null && patient.getReviewProvinceTime() == null) {
            url = UrlUtils.pacReviewView(patient.getID());
        } else if (patient.getAcceptTime() != null && patient.getReviewWardTime() != null && patient.getReviewProvinceTime() == null) {
            url = UrlUtils.pacAcceptView(patient.getID());
        } else if (patient.getAcceptTime() != null && patient.getReviewWardTime() != null && patient.getReviewProvinceTime() != null) {
            url = UrlUtils.pacPatientView(patient.getID());
        } else if (patient.getAcceptTime() == null && patient.getReviewWardTime() == null && patient.getReviewProvinceTime() == null && !patient.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) {
            url = UrlUtils.pacPatientView("new_in_province", patient.getID());
        }
        return url;
    }
}
