package com.gms.controller.backend;

import com.gms.components.PasswordUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmHubEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.db.AuthAssignmentEntity;
import com.gms.entity.db.AuthRoleEntity;
import com.gms.entity.db.AuthRoleServiceEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffActivityEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.SiteForm;
import com.gms.entity.form.StaffForm;
import com.gms.entity.input.StaffSearch;
import com.gms.entity.validate.SiteValidate;
import com.gms.entity.validate.StaffValidate;
import com.gms.service.AuthService;
import com.gms.service.StaffActivityService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_site")
public class SiteController extends BaseController {

    @Autowired
    private AuthService authService;
    @Autowired
    private StaffActivityService activityService;

    @Autowired
    private SiteValidate siteValidate;
    @Autowired
    private StaffValidate staffValidate;

    private HashMap<Boolean, String> getActiveOption() {
        HashMap<Boolean, String> list = new HashMap<>();
        list.put(false, "Tạm khoá");
        list.put(true, "Hoạt động");
        return list;
    }

    private HashMap<String, String> getServiceOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.SERVICE)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    private HashMap<String, String> getServiceTestOption() {
        HashMap<String, String> list = new HashMap<>();
        list.put("", "Chưa xác định");
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.SERVICE_TEST)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * @auth vvThành
     * @return
     */
    private HashMap<String, String> getProjectOption() {
        Set<String> params = new HashSet<>();
        params.add(ParameterEntity.SITE_PROJECT);
        return parameterService.getOptionsByTypes(params, getFinalParameter()).get(ParameterEntity.SITE_PROJECT);
    }

    private HashMap<String, String> getHubOptions() {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("", "Chọn cấu hình");
        map.put(PqmHubEnum.NHAP_DU_LIEU_THU_CAP.getKey(), PqmHubEnum.NHAP_DU_LIEU_THU_CAP.getLabel());
        map.put(PqmHubEnum.IMPORT_DU_LIEU.getKey(), PqmHubEnum.IMPORT_DU_LIEU.getLabel());
        map.put(PqmHubEnum.FROM_IMS.getKey(), PqmHubEnum.FROM_IMS.getLabel());
        return map;
    }

    private HashMap<Long, String> getRoles(SiteEntity site) {
        List<String> serviceIDs = site.getServiceIDs();
        HashMap<Long, String> list = new HashMap<>();
        List<AuthRoleEntity> roles = authService.findAllRole();
        if (roles != null && !roles.isEmpty()) {
            for (AuthRoleEntity role : roles) {
                Set<AuthRoleServiceEntity> roleServices = role.getRoleServices();
                if (roleServices == null || serviceIDs == null) {
                    continue;
                }
                boolean isAdd = false;
                for (AuthRoleServiceEntity roleService : roleServices) {
                    if (serviceIDs.contains(roleService.getServiceID())) {
                        isAdd = true;
                        break;
                    }
                }
                if (isAdd) {
                    list.put(role.getID(), role.getName());
                }
            }
        }
        return list;
    }

    private void buildTree(List<SiteEntity> data, Long ID, int level, List<SiteEntity> sites) {
        level++;
        for (SiteEntity item : sites) {
            try {
                SiteEntity site = item.clone();
                if (site.getParentID().equals(ID)) {
                    String name = "";
                    for (int i = 0; i < level; i++) {
                        name += "&nbsp;&nbsp;";
                    }
                    site.setName(String.format("%s %s. %s", name, site.getCode().toUpperCase(), site.getName()));
                    data.add(site);
                    buildTree(data, site.getID(), level, sites);
                }

            } catch (CloneNotSupportedException e) {
                getLogger().error(e.getMessage());
            }
        }
    }

    private List<SiteEntity> getLeaf() {
        LoggedUser currentUser = getCurrentUser();
        List<SiteEntity> sites = siteService.findAll();
        List<SiteEntity> data = new ArrayList<>();
        if (sites != null && !sites.isEmpty()) {
            buildTree(data, currentUser.getSite().getID(), 0, sites);
        }
        return data;
    }

    /**
     * Danh sách các cấp nhỏ hơn, nếu là loại VAAC, có quyền tạo cùng cấp
     *
     * @auth vvThành
     * @return
     */
    private LinkedHashMap<Long, String> getLeafOptions() {
        LoggedUser currentUser = getCurrentUser();
        LinkedHashMap<Long, String> options = new LinkedHashMap<>();
        if (currentUser.getSite().getType() == SiteEntity.TYPE_VAAC) {
            options.put(Long.parseLong("0"), "Cấp cao nhất");
        }
        options.put(currentUser.getSite().getID(), String.format("%s. %s", currentUser.getSite().getCode(), currentUser.getSite().getName()));
        List<SiteEntity> leaf = getLeaf();
        for (SiteEntity siteEntity : getLeaf()) {
            options.put(siteEntity.getID(), siteEntity.getName());
        }
        return options;
    }

    /**
     * Kiểm tra quyền truy cập của tài khoản
     *
     * @auth vvThành
     * @param site
     * @return
     */
    private boolean verifyRole(SiteEntity site) {
        boolean flag = true;
        SiteEntity currentSite = getCurrentUser().getSite();
        if (!site.getPathID().contains(currentSite.getID())) {
            return false;
        }
        return flag;
    }

    @RequestMapping(value = {"/site/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", defaultValue = "all") String tab,
            @RequestParam(name = "oid", defaultValue = "0") Long siteID) {
        SiteEntity site = siteService.findOne(siteID.equals(Long.parseLong("0")) ? getCurrentUser().getSite().getID() : siteID, true);
        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }
        List<SiteEntity> sites;
        if (tab.equals("tree")) {
            sites = getLeaf();
        } else {
            sites = siteService.getChild(site.getID());
        }

        model.addAttribute("title", "Cơ sở trực thuộc");
        model.addAttribute("tab", tab);
        model.addAttribute("site", site);
        model.addAttribute("projects", getProjectOption());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("sites", siteService.findPath(sites));

        return "backend/site/index";
    }

    @RequestMapping(value = {"/site/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model, SiteForm form) {
        model.addAttribute("title", "Đăng ký mới");
        model.addAttribute("parent_title", "Cơ sở trực thuộc");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("form", form);
        model.addAttribute("sites", getLeafOptions());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());
        model.addAttribute("hubOptions", getHubOptions());
        return "backend/site/new";
    }

    @RequestMapping(value = {"/site/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid SiteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("title", "Đăng ký mới");
        model.addAttribute("parent_title", "Cơ sở trực thuộc");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("form", form);
        model.addAttribute("sites", getLeafOptions());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());
        model.addAttribute("hubOptions", getHubOptions());

        siteValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/site/new";
        }

        SiteEntity siteEntity = new SiteEntity();
        try {
            List<String> ignoreAttributes = new ArrayList<>();
            ignoreAttributes.add("service");
            siteEntity.set(form, ignoreAttributes);
            siteEntity.setCode(form.getCode());
            siteEntity.setHub(form.getHub());

            if (form.getService() != null && !form.getService().isEmpty()) {
                siteEntity.setServices(parameterService.findByCodes(ParameterEntity.SERVICE, new HashSet<>(form.getService())));
            }

            siteEntity = siteService.save(siteEntity);
            if (siteEntity == null) {
                throw new Exception("Đăng ký cơ sở thất bại!");
            }
            redirectAttributes.addFlashAttribute("success", "Cơ sở được đăng ký thành công.");
            redirectAttributes.addFlashAttribute("success_id", siteEntity.getID());
            return redirect(UrlUtils.siteIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/site/new";
        }
    }

    @RequestMapping(value = {"/site/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid") Long siteID,
            SiteForm form,
            RedirectAttributes redirectAttributes) {

        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }
        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        form.setID(siteID);
        form.setCode(site.getCode());
        form.setName(site.getName());
        form.setShortName(site.getShortName());
        form.setDescription(site.getDescription());
        form.setProvinceID(site.getProvinceID());
        form.setDistrictID(site.getDistrictID());
        form.setWardID(site.getWardID());
        form.setAddress(site.getAddress());
        form.setEmail(site.getEmail());
        form.setContactName(site.getContactName());
        form.setContactPosition(site.getContactPosition());
        form.setPhone(site.getPhone());
        form.setParentID(site.getParentID());
        form.setType(site.getType());
        form.setIsActive(site.isIsActive());
        form.setParentAgency(site.getParentAgency());
        form.setProjectID(site.getProjectID());
        form.setHub(site.getHub());
        form.setService(site.getServiceIDs() == null ? new ArrayList<String>() : site.getServiceIDs());

        model.addAttribute("title", "Cập nhật thông tin");
        model.addAttribute("parent_title", "Cơ sở trực thuộc");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("form", form);
        model.addAttribute("site", site);
        model.addAttribute("sites", getLeafOptions());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());
        model.addAttribute("hubOptions", getHubOptions());
        return "backend/site/update";
    }

    @RequestMapping(value = {"/site/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "oid") Long siteID,
            @ModelAttribute("form") @Valid SiteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }
        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        model.addAttribute("title", "Cập nhật thông tin");
        model.addAttribute("parent_title", "Cơ sở trực thuộc");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("form", form);
        model.addAttribute("site", site);
        model.addAttribute("sites", getLeafOptions());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());
        model.addAttribute("hubOptions", getHubOptions());

        siteValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thất bại! dữ liệu không chính xác.");
            return "backend/site/update";
        }

        try {
            List<String> ignoreAttributes = new ArrayList<>();
            ignoreAttributes.add("service");
            ignoreAttributes.add("ID");
            site.set(form, ignoreAttributes);
            site.setCode(form.getCode());
            site.setHub(form.getHub());

            if (form.getService() != null && !form.getService().isEmpty()) {
                site.setServices(parameterService.findByCodes(ParameterEntity.SERVICE, new HashSet<>(form.getService())));
            }

            site = siteService.save(site);
            if (site == null) {
                throw new Exception("Cập nhật thông tin cơ sở thất bại!");
            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cơ sở thành công. Quay lại click <a href='" + UrlUtils.siteIndex() + "'>vào đây</a>");
            redirectAttributes.addFlashAttribute("success_id", site.getID());
            return redirect(UrlUtils.siteUpdate(site.getID()));
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage() + ". " + ex.toString());
            return "backend/site/update";
        }
    }

    @RequestMapping(value = {"/site/remove.html"}, method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long siteID,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }
        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        List<Long> childIDs = siteService.getLeafID(getCurrentUser().getSite().getID());
        if (childIDs == null || childIDs.isEmpty() || !childIDs.contains(siteID)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở %s không thuộc quản lý của bạn.", site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        site.setIsActive(!site.isIsActive());
        siteService.save(site);

        redirectAttributes.addFlashAttribute("success", String.format("Cơ sở %s đã được được xoá khỏi hệ thống.", site.getName()));
        return redirect(UrlUtils.siteIndex());
    }

    @RequestMapping(value = {"/site/staff.html"}, method = RequestMethod.GET)
    public String actionStaff(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "tab", defaultValue = "all", required = false) String tab,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            RedirectAttributes redirectAttributes) {

        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffSearch search = new StaffSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSort(sort);
        search.setIsActive(true);
        search.setSiteID(site.getID());
        if (tab.equals("remove")) {
            search.setIsActive(false);
        }
        DataPage<StaffEntity> dataPage = staffService.find(search);

        model.addAttribute("title", "Quản trị viên");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("serviceTests", getServiceTestOption());
        model.addAttribute("dataPage", dataPage);

        return "backend/site/staff_index";
    }

    @GetMapping(value = {"/site/staff-new.html"})
    public String actionStaffNew(Model model,
            StaffForm form,
            @RequestParam(name = "sid") Long siteID,
            @RequestHeader(name = "X-Forwarded-Host", required = false) String host,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        model.addAttribute("title", "Thêm mới quản trị viên");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("form", form);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("serviceTests", getServiceTestOption());
        model.addAttribute("roles", getRoles(site));

        return "backend/site/staff_new";
    }

    @PostMapping(value = {"/site/staff-new.html"})
    public String doActionStaffNew(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestHeader(name = "X-Forwarded-Host", required = false) String host,
            @ModelAttribute("form") @Valid StaffForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        model.addAttribute("title", "Thêm mới quản trị viên");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("form", form);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("serviceTests", getServiceTestOption());
        model.addAttribute("roles", getRoles(site));

        staffValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/site/staff_new";
        }

        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setSiteID(site.getID());
        staffEntity.setEmail(form.getEmail());
        staffEntity.setPhone(form.getPhone());
        staffEntity.setName(form.getName());
        staffEntity.setUsername(form.getUsername());
        staffEntity.setPwd(PasswordUtils.encrytePassword(form.getPwd()));
        staffEntity.setIsActive(form.isIsActive());
        staffEntity.setChangePwd(true);
        staffEntity.setServiceVisitID(form.getServiceVisitID());

        staffEntity = staffService.save(staffEntity, form.getRoleID());
        if (staffEntity == null) {
            model.addAttribute("error", "Thêm nhân viên thất bại!");
            return "backend/site/staff_new";
        }
        //Gửi email
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "THÔNG BÁO CẤP TÀI KHOẢN THÀNH CÔNG");
        variables.put("fullname", form.getName());
        variables.put("username", form.getUsername());
        variables.put("password", form.getPwd());
        variables.put("createBy", currentUser.getUser().getName());
        variables.put("urlemail", host.contains("hub") ? "https://hub.dulieuhiv.vn/" : "https://dulieuhiv.vn/");
        //Gửi emmail cho thằng được tạo tài khoản
        if (form.getEmail() != null && !form.getEmail().isEmpty()) {
            sendEmail(form.getEmail(), "[Thông báo] Tài khoản đã được tạo thành công", EmailoutboxEntity.TEMPLATE_CREATE_ACCOUNT, variables);
        }
        //gửi email cho thằng cơ sở
        String email = parameterService.getSiteConfig(site.getID(), SiteConfigEnum.ALERT_SITE_EMAIL.getKey());
        if (email != null && !email.isEmpty()) {
            sendEmail(email, "[Thông báo] Tài khoản quản trị viên đã được tạo thành công", EmailoutboxEntity.TEMPLATE_SENT_SITE_ACCOUNT, variables);
        }

        redirectAttributes.addFlashAttribute("success", "Thêm quản trị thành công");
        redirectAttributes.addFlashAttribute("success_id", staffEntity.getID());
        return redirect(UrlUtils.siteStaff(site.getID()));
    }

    @GetMapping(value = {"/site/staff-update.html"})
    public String actionStaffUpdate(Model model,
            StaffForm form,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "oid") Long staffID,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffEntity staff = staffService.findOne(staffID);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", staffID));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        if (!staff.getSiteID().equals(site.getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Nhân viên %s không thuộc quản lý cơ sở %s", staff.getName(), site.getName()));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        form.setID(staff.getID());
        form.setEmail(staff.getEmail());
        form.setIsActive(staff.isIsActive());
        form.setName(staff.getName());
        form.setPhone(staff.getPhone());
        form.setUsername(staff.getUsername());
        form.setServiceVisitID(staff.getServiceVisitID());
        form.setPwd("123123");
        form.setConfirmPwd("123123");
        List<AuthAssignmentEntity> roleStaff = authService.findOneAssignmentByUserID(staff.getID());
        if (roleStaff != null) {
            form.setRoleID(new ArrayList<Long>());
            for (AuthAssignmentEntity authAssignmentEntity : roleStaff) {
                form.getRoleID().add(authAssignmentEntity.getRole().getID());
            }
        }
        model.addAttribute("title", "Cập nhật quản trị viên");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("form", form);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("serviceTests", getServiceTestOption());
        model.addAttribute("roles", getRoles(site));

        return "backend/site/staff_update";
    }

    @PostMapping(value = {"/site/staff-update.html"})
    public String doActionStaffUpdate(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "oid") Long staffID,
            @ModelAttribute("form") @Valid StaffForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffEntity staff = staffService.findOne(staffID);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", staffID));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        if (!staff.getSiteID().equals(site.getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Nhân viên %s không thuộc quản lý cơ sở %s", staff.getName(), site.getName()));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        model.addAttribute("title", "Cập nhật quản trị viên");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("form", form);
        model.addAttribute("activeLabel", getActiveOption());
        model.addAttribute("serviceTests", getServiceTestOption());
        model.addAttribute("roles", getRoles(site));

        staffValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/site/staff_update";
        }

        staff.setEmail(form.getEmail());
        staff.setPhone(form.getPhone());
        staff.setName(form.getName());
        staff.setIsActive(form.isIsActive());
        staff.setServiceVisitID(form.getServiceVisitID());

        staff = staffService.save(staff, form.getRoleID());
        if (staff == null) {
            model.addAttribute("error", "Cập nhật quản trị viên thất bại!");
            return "backend/site/staff_new";
        }

        redirectAttributes.addFlashAttribute("success", "Cập nhật quản trị viên thành công");
        redirectAttributes.addFlashAttribute("success_id", staff.getID());
        return redirect(UrlUtils.siteStaff(site.getID()));
    }

    @GetMapping(value = {"/site/staff-change-password.html"})
    public String actionStaffChangePwd(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "oid") Long staffID,
            StaffForm form,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffEntity staff = staffService.findOne(staffID);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", staffID));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        if (!staff.getSiteID().equals(site.getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Nhân viên %s không thuộc quản lý cơ sở %s", staff.getName(), site.getName()));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        form.setID(staff.getID());
        form.setEmail(staff.getEmail());
        form.setIsActive(staff.isIsActive());
        form.setName(staff.getName());
        form.setPhone(staff.getPhone());
        form.setUsername(staff.getUsername());

        model.addAttribute("title", "Đổi mật khẩu");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("form", form);

        return "backend/site/staff_password";
    }

    @PostMapping(value = {"/site/staff-change-password.html"})
    public String doActionStaffChangePwd(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "oid") Long staffID,
            @ModelAttribute("form") @Valid StaffForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffEntity staff = staffService.findOne(staffID);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", staffID));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        if (!staff.getSiteID().equals(site.getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Nhân viên %s không thuộc quản lý cơ sở %s", staff.getName(), site.getName()));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        model.addAttribute("title", "Đổi mật khẩu");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("form", form);

        staffValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/site/staff_password";
        }

        staff.setPwd(PasswordUtils.encrytePassword(form.getPwd()));
        staff = staffService.save(staff);
        if (staff == null) {
            model.addAttribute("error", "Đổi mật khẩu thất bại!");
            return "backend/site/staff_password";
        }

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        redirectAttributes.addFlashAttribute("success_id", staff.getID());
        return redirect(UrlUtils.siteStaff(site.getID()));
    }

    @GetMapping(value = {"/site/staff-history.html"})
    public String actionStaffHistory(Model model,
            @RequestParam(name = "sid") Long siteID,
            @RequestParam(name = "oid") Long staffID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            RedirectAttributes redirectAttributes) {
        SiteEntity site = siteService.findOne(siteID, true);
        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteID));
            return redirect(UrlUtils.siteIndex());
        }

        if (!verifyRole(site)) {
            redirectAttributes.addFlashAttribute("error", String.format("Cơ sở #%s. %s không thuộc quản lý", site.getID(), site.getName()));
            return redirect(UrlUtils.siteIndex());
        }

        StaffEntity staff = staffService.findOne(staffID);
        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", staffID));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        if (!staff.getSiteID().equals(site.getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Nhân viên %s không thuộc quản lý cơ sở %s", staff.getName(), site.getName()));
            return redirect(UrlUtils.siteStaff(site.getID()));
        }

        DataPage<StaffActivityEntity> dataPage = activityService.find(staff.getID(), page, size);

        List<AuthActionEntity> actionEntitys = authService.findAllAction();
        HashMap<String, String> actions = new HashMap<>();
        for (AuthActionEntity action : actionEntitys) {
            actions.put(action.getName(), action.getTitle());
        }

        model.addAttribute("title", "Lịch sử hoạt động");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("actions", actions);

        return "backend/site/staff_history";
    }

    @GetMapping(value = {"/site-config/index.html"})
    public String actionSiteConfig(Model model) {
        SiteEntity site = siteService.findOne(getCurrentUser().getSite().getID(), true, true);
        model.addAttribute("title", "Cấu hình tại cơ sở");
        model.addAttribute("parent_title", String.format("Cơ sở %s", site.viewName()));
        model.addAttribute("site", site);
        model.addAttribute("items", parameterService.getSiteConfig(site.getID()));
        model.addAttribute("options", getOptions());

        model.addAttribute("isHtc", site.getServiceIDs().contains(ServiceEnum.HTC.getKey()));
        model.addAttribute("isConfirm", site.getServiceIDs().contains(ServiceEnum.HTC_CONFIRM.getKey()));
        model.addAttribute("isLaytest", site.getServiceIDs().contains(ServiceEnum.LAYTEST.getKey()));
        model.addAttribute("isPac", site.getServiceIDs().contains(ServiceEnum.PAC.getKey()));
        model.addAttribute("isOpc", site.getServiceIDs().contains(ServiceEnum.OPC.getKey()));
        model.addAttribute("isOpcManager", site.getServiceIDs().contains(ServiceEnum.OPC_MANAGEMENT.getKey()));
        return "backend/site/config";
    }

    /**
     * @auth vvThành
     * @return
     */
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }

}
