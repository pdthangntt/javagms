package com.gms.controller.backend;

import com.gms.components.ClassUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.input.StaffSearch;
import com.gms.service.CommonService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@Controller(value = "backend_staff")
public class StaffController extends BaseController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = {"/staff/index.html"}, method = RequestMethod.GET)
    public String actionStaff(Model model,
            @RequestParam(name = "oid", required = false, defaultValue = "0") String oid,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "phone", required = false, defaultValue = "") String phone,
            @RequestParam(name = "sid", required = false, defaultValue = "0") String siteID,
            @RequestParam(name = "tab", required = false, defaultValue = "all") String tab,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();

        StaffSearch search = new StaffSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSort(sort);
        search.setIsActive(true);

        Map<String, String> siteSearchOptions = new LinkedHashMap<>();
        siteSearchOptions.put("0", "Tất cả");
        if (isPAC() || isTTYT()) {
            List<Long> siteIDs = siteService.getProgenyID(currentUser.getSite().getID());

            if (!siteIDs.isEmpty()) {
                // Creating a hash set using constructor 
                Set<Long> hSet = new HashSet<Long>(siteIDs);
                search.setSiteIDs(hSet);

                for (SiteEntity item : siteService.findByIDs(search.getSiteIDs())) {
                    siteSearchOptions.put(String.valueOf(item.getID()), item.getName());
                }
            }
        }
        if (!"0".equals(siteID)) {
            if (search.getSiteIDs() != null) {
                search.getSiteIDs().clear();
            }
            search.setSiteIDs(new HashSet<Long>());
            try {
                search.getSiteIDs().add(Long.valueOf(siteID));
            } catch (Exception e) {
                search.getSiteIDs().add(Long.valueOf(-2));
            }

        }

        try {
            search.setID(Long.valueOf(oid));
        } catch (Exception e) {
            search.setID(Long.valueOf(-2));
        }

        if (username != null && !"".equals(username)) {
            search.setUserName(StringUtils.normalizeSpace(username.trim()));
        }
        if (name != null && !"".equals(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        if (email != null && !"".equals(email)) {
            search.setEmail(StringUtils.normalizeSpace(email.trim()));
        }
        if (phone != null && !"".equals(phone)) {
            search.setPhone(StringUtils.normalizeSpace(phone.trim()));
        }

        if (tab.equals("delete")) {
            search.setIsActive(false);
        }

        DataPage<StaffEntity> dataPage = staffService.findAllAdmin(search);
        Set<Long> sID = new HashSet<>();
        sID.addAll(ClassUtils.getAttribute(dataPage.getData(), "getSiteID"));
        Map<Long, String> siteOptions = new HashMap<>();
        if (!sID.isEmpty()) {
            for (SiteEntity item : siteService.findByIDs(sID)) {
                siteOptions.put(item.getID(), item.getShortName());
            }
        }

        model.addAttribute("title", "Quản trị viên");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("vaac", isPAC() || isTTYT());
        model.addAttribute("siteOptions", siteOptions);
        model.addAttribute("siteSearchOptions", siteSearchOptions);
        return "backend/staff/index";
    }

    @RequestMapping(value = {"/staff/switch.html"}, method = RequestMethod.GET)
    public String actionSwitch(Model model,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            RedirectAttributes redirectAttributes) {
        try {
            UserDetails staff = commonService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(staff, staff.getPassword(), staff.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            redirectAttributes.addFlashAttribute("successs", "Chuyển đổi tài khoản <b>" + username + "</b> thành công");
            return redirect(UrlUtils.backendHome());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return redirect(UrlUtils.adminStaffIndex(""));
        }
    }
}
