package com.gms.controller.backend;

import com.gms.components.PasswordUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.controller.WebController;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.PasswordForm;
import com.gms.entity.form.ProfileForm;
import com.gms.entity.form.ProfileSiteForm;
import com.gms.entity.validate.PasswordValidate;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author TrangBN
 */
@Controller(value = "backend_profile")
public class ProfileController extends WebController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PasswordValidate passwordValidate;
    @Autowired
    private ParameterService parameterService;

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

    private HashMap<String, String> getServiceOption() {
        HashMap<String, String> list = new HashMap<>();
//        list.put("0", "Chọn dịch vụ");
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.SERVICE)) {
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

    @RequestMapping(value = {"/profile.html"}, method = RequestMethod.GET)
    public String actionProfile(Model model, ProfileForm form) {

        // Lấy thông tin user hiện tại
        StaffEntity staff = staffService.findOne(getCurrentUser().getUser().getID());
        if (staff == null) {
            return redirect(UrlUtils.error403());
        }

        // Setting hiển thị thông tin user
        form.setID(staff.getID());
        form.setName(staff.getName());
        form.setPhone(staff.getPhone());
        form.setEmail(staff.getEmail());

        model.addAttribute("title", "Thông tin cá nhân");
        model.addAttribute("form", form);
        model.addAttribute("staff", staff);

        return "backend/profile/profile";
    }

    @RequestMapping(value = {"/profile.html"}, method = RequestMethod.POST)
    public String doActionProfile(Model model,
            @ModelAttribute("form") @Valid ProfileForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Lấy thông tin user hiện tại
        Long profileId = getCurrentUser().getUser().getID();

        // Lấy thông tin mới nhất của nhân viên
        StaffEntity staff = staffService.findOne(profileId);

        model.addAttribute("title", "Thông tin cá nhân");
        model.addAttribute("form", form);
        model.addAttribute("staff", staff);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thông tin cá nhân thất bại! Dữ liệu không chính xác.");
            return "backend/profile/profile";
        }

        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", profileId));
            return redirect(UrlUtils.siteIndex());
        }

        try {
            staff.setName(form.getName());
            staff.setEmail(form.getEmail());
            staff.setPhone(form.getPhone());
            staff = staffService.save(staff);

            if (staff == null) {
                throw new Exception("Cập nhật thông tin nhân viên thất bại thất bại!");
            }

            //gửi email thông báo cập nhật thông tin cá nhân
            sendEmailNotify(staff.getEmail(),
                    "Thông tin cá nhân vừa được thay đổi",
                    String.format(
                            "Xin chào %s <br/>Thông tin cá nhân tài khoản %s vừa được thay đổi lúc %s",
                            staff.getName(), staff.getUsername(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                    ));

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin nhân viên thành công.");
            redirectAttributes.addFlashAttribute("success_id", staff.getID());

            //Reset user login
            LoggedUser currentUser = getCurrentUser();
            currentUser.setUser(staff);
            Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, getCurrentUser().getPassword(), getCurrentUser().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return redirect(UrlUtils.profile());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/profile/profile";
        }
    }

    @RequestMapping(value = {"/profile/password.html"}, method = RequestMethod.GET)
    public String actionProfilePassword(Model model, PasswordForm form) {

        // Lấy thông tin user hiện tại
        StaffEntity staff = staffService.findOne(getCurrentUser().getUser().getID());
        if (staff == null) {
            return redirect(UrlUtils.error403());
        }

        model.addAttribute("title", "Đổi mật khẩu");
        model.addAttribute("staff", staff);
        model.addAttribute("form", form);

        return "backend/profile/password";
    }

    @RequestMapping(value = {"/profile/password.html"}, method = RequestMethod.POST)
    public String doActionChangePass(Model model,
            @ModelAttribute("form") @Valid PasswordForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Lấy thông tin id user hiện tại
        Long profileId = getCurrentUser().getUser().getID();

        // Lấy thông tin mới nhất của nhân viên
        StaffEntity staff = staffService.findOne(profileId);

        model.addAttribute("title", "Đổi mật khẩu");
        model.addAttribute("form", form);
        model.addAttribute("staff", staff);

        passwordValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Đổi mật khẩu thất bại! Dữ liệu không chính xác.");
            return "backend/profile/password";
        }

        if (staff == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy nhân viên có mã %s", profileId));
            return redirect(UrlUtils.siteIndex());
        }

        if (!PasswordUtils.validatePassword(form.getOldPassword(), staff.getPwd())) {
            model.addAttribute("error", "Mật khẩu cũ không chính xác.");
            return "backend/profile/password";
        }

        try {
            staff.setPwd(PasswordUtils.encrytePassword(form.getPassword()));
            staff.setChangePwd(false);
            staff = staffService.save(staff);

            //gửi email thông báo cập nhật thông tin cá nhân
            sendEmailNotify(staff.getEmail(),
                    "Mật khẩu vừa được thay đổi",
                    String.format(
                            "Xin chào %s <br/>Mật khẩu tài khoản %s vừa được thay đổi lúc %s",
                            staff.getName(), staff.getUsername(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                    ));

            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công.");
            redirectAttributes.addFlashAttribute("success_id", staff.getID());
            return redirect(UrlUtils.profileChangePwd());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/profile/password";
        }
    }

    @RequestMapping(value = {"/profile/staff.html"}, method = RequestMethod.GET)
    public String actionStaff(Model model) {
        return redirect(UrlUtils.siteStaff(getCurrentUser().getSite().getID()));
    }

    @RequestMapping(value = {"/profile/site.html"}, method = RequestMethod.GET)
    public String actionSite(Model model,
            ProfileSiteForm form,
            RedirectAttributes redirectAttributes) {

        // Lấy thông tin site hiện tại
        Long siteId = getCurrentUser().getSite().getID();
        SiteEntity site = siteService.findOne(siteId, false, true);

        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteId));
            return redirect(UrlUtils.siteIndex());
        }

        form.setCode(site.getCode());
        form.setName(site.getName());
        form.setShortName(site.getShortName());
        form.setDescription(site.getDescription());
        form.setProvinceID(site.getProvinceID());
        form.setDistrictID(site.getDistrictID());
        form.setWardID(site.getWardID());
        form.setAddress(site.getAddress());
        form.setEmail(site.getEmail());
        form.setPhone(site.getPhone());
        form.setType(site.getType());
        form.setContactName(site.getContactName());
        form.setContactPosition(site.getContactPosition());
        form.setParentAgency(site.getParentAgency());
        form.setProjectID(site.getProjectID());
        form.setService(site.getServiceIDs() == null ? new ArrayList<String>() : site.getServiceIDs());

        model.addAttribute("title", "Thông tin cơ sở");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("form", form);
        model.addAttribute("site", site);
        model.addAttribute("sites", getLeafOptions());
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());
        return "backend/profile/site";
    }

    @RequestMapping(value = {"/profile/site.html"}, method = RequestMethod.POST)
    public String doActionSite(Model model,
            @ModelAttribute("form") @Valid ProfileSiteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Lấy thông tin site hiện tại
        Long siteId = getCurrentUser().getSite().getID();
        SiteEntity site = siteService.findOne(siteId, false, true);

        if (site == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy cơ sở có mã %s", siteId));
            return redirect(UrlUtils.currentSiteUpdate());
        }
        model.addAttribute("title", "Cập nhật thông tin cơ sở");
        model.addAttribute("typeLabel", SiteEntity.typeLabel);
        model.addAttribute("form", form);
        model.addAttribute("site", site);
        model.addAttribute("services", getServiceOption());
        model.addAttribute("projects", getProjectOption());

        if (form.getCode() != null) {
            SiteEntity siteCode = siteService.findByCode(form.getCode());
            if (siteCode != null && !siteCode.getID().equals(site.getID())) {
                bindingResult.rejectValue("code", "code.error.message", "Mã cơ sở đã được sử dụng");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thất bại! Dữ liệu không chính xác.");
            model.addAttribute("sites", getLeafOptions());
            return "backend/profile/site";
        }

        try {
            //            Set thông tin cập nhật của site hiện tại
            site.setCode(form.getCode());
            site.setName(form.getName());
            site.setShortName(form.getShortName());
            site.setDescription(form.getDescription());
            if (form.getType() == SiteEntity.TYPE_VAAC) {
                site.setProvinceID(form.getProvinceID());
                site.setDistrictID(form.getDistrictID());
                site.setWardID(form.getWardID());
            }
            if (form.getType() == SiteEntity.TYPE_PROVINCE) {
                site.setDistrictID(form.getDistrictID());
                site.setWardID(form.getWardID());
            }
            if (form.getType() == SiteEntity.TYPE_DISTRICT) {
                site.setWardID(form.getWardID());
            }
            site.setAddress(form.getAddress());
            site.setEmail(form.getEmail());
            site.setPhone(form.getPhone());
            site.setContactName(form.getContactName());
            site.setContactPosition(form.getContactPosition());
//            site.setParentAgency(form.getParentAgency());
            site.setProjectID(form.getProjectID());
//            if (form.getService() != null && !form.getService().isEmpty()) {
//                site.setServices(parameterService.findByCodes(ParameterEntity.SERVICE, new HashSet<>(form.getService())));
//            }

            // Cập nhật thông tin site
            site = siteService.save(site);

            if (site == null) {
                throw new Exception("Cập nhật thông tin cơ sở thất bại!");
            }
            StaffEntity staff = staffService.findOne(getCurrentUser().getUser().getID());
            sendEmailNotify(site.getEmail(),
                    "Thông tin cơ sở vừa được thay đổi",
                    String.format(
                            "Thông báo <br/>Thông tin cơ sở %s vừa được thay đổi lúc %s bởi người dùng %s",
                            site.getName(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy"), staff.getName()
                    ));
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cơ sở thành công.");
            redirectAttributes.addFlashAttribute("success_id", site.getID());

            //Reset user login
            LoggedUser currentUser = getCurrentUser();
            currentUser.setSite(siteService.findOne(site.getID(), true, true));
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(currentUser, getCurrentUser().getPassword(), getCurrentUser().getAuthorities()));
            return redirect(UrlUtils.currentSiteUpdate());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/profile/site";
        }
    }

    @GetMapping(value = {"/profile/config.html"})
    public String actionConfig(Model model) {
        SiteEntity site = siteService.findOne(getCurrentUser().getSite().getID(), true, true);
        StaffEntity staff = staffService.findOne(getCurrentUser().getUser().getID());

        model.addAttribute("title", "Cấu hình chung");
        model.addAttribute("parent_title", String.format("Cán bộ %s", staff.getName()));
        model.addAttribute("site", site);
        model.addAttribute("staff", staff);
        model.addAttribute("items", parameterService.getStaffConfig(staff.getID()));
        model.addAttribute("options", getOptions());

        model.addAttribute("isLaytest", site.getServiceIDs().contains(ServiceEnum.LAYTEST.getKey()));
        model.addAttribute("isHtc", site.getServiceIDs().contains(ServiceEnum.HTC.getKey()));
        return "backend/profile/config";
    }

    /**
     * @auth vvThành
     * @return
     */
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        List<SiteEntity> sites = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        String key = "site_htc";
        options.put(key, new HashMap<String, String>());
        options.get(key).put("", "Chọn cơ sở");
        for (SiteEntity site : sites) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }
        return options;
    }
}
