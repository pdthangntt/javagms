package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.OpcApiLogEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.SiteVnptFkEntity;
import com.gms.entity.form.VNPTConfigForm;
import com.gms.entity.input.VNPTConfigSearch;
import com.gms.entity.validate.VNPTConfigValidate;
import com.gms.repository.impl.VNPTLogRepositoryImpl;
import com.gms.service.OpcApiLogService;
import com.gms.service.SiteService;
import com.gms.service.VNPTConfigService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
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
 *
 * @author bingo
 */
@Controller(value = "his_hiv")
public class VNPTConfigController extends BaseController {

    @Autowired
    private VNPTConfigService configService;
    @Autowired
    private OpcApiLogService apiLogService;
    @Autowired
    private VNPTLogRepositoryImpl logRepositoryImpl;
    @Autowired
    private VNPTConfigValidate configValidate;

    @Autowired
    private SiteService siteService;

    @RequestMapping(value = {"/vnpt-config/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "site", required = false, defaultValue = "") String site,
            @RequestParam(name = "status", required = false, defaultValue = "") String active,
            @RequestParam(name = "id", required = false, defaultValue = "") String id,
            @RequestParam(name = "vnpt_id", required = false, defaultValue = "") String vnptID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        //Khởi tạo
        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        Map<String, String> sites = new LinkedHashMap<>();
        sites.put("", "Tất cả");
        Map<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        //Lọc cơ sở theo tỉnh khi khác vaac
        if (!getSite().isEmpty()) {
            for (SiteEntity siteEntity : getSite()) {
                sites.put(siteEntity.getID().toString(), siteEntity.getName());
                siteIDs.add(siteEntity.getID());
            }
        }
        if (siteIDs.isEmpty()) {
            siteIDs.add(Long.valueOf(-999));
        }

        //Điều kiện lọc
        VNPTConfigSearch search = new VNPTConfigSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setActive(StringUtils.isEmpty(active) ? "2" : active);
        search.setSiteID(isVAAC() ? null : siteIDs);
        try {
            search.setID(StringUtils.isEmpty(id) ? Long.valueOf("0") : Long.valueOf(id));
            search.setSite(StringUtils.isEmpty(site) ? Long.valueOf("0") : Long.valueOf(site));
        } catch (Exception e) {
            search.setID(Long.valueOf("-1"));
        }
        search.setVnptSiteID(vnptID);

        //Lấy dữ liệu
        DataPage<SiteVnptFkEntity> dataPage = configService.findAll(search);

        model.addAttribute("status", status);
        model.addAttribute("sites", sites);
        model.addAttribute("title", "Cấu hình VNPT");
        model.addAttribute("dataPage", dataPage);
        return "backend/vnpt_config/index";
    }

    @RequestMapping(value = {"/vnpt-config/deactive.html"}, method = RequestMethod.GET)
    public String actionSwitch(@RequestParam(name = "id", required = false, defaultValue = "") String ID,
            RedirectAttributes redirectAttributes) {
        try {
            SiteVnptFkEntity model = configService.findOne(Long.valueOf(ID));
            if (model == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy cấu hình");
                return redirect(UrlUtils.hisHIVIndex());
            }

            model.setActive(!model.isActive());
            configService.save(model);

            return redirect(UrlUtils.hisHIVIndex());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return redirect(UrlUtils.hisHIVIndex());
        }
    }

    @RequestMapping(value = {"/vnpt-config/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            VNPTConfigForm form,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        Map<String, String> sites = new LinkedHashMap<>();
        sites.put("", "Chọn cơ sở");
        Map<String, String> status = new HashMap<>();
        status.put("", "Chọn trạng thái");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        //Lọc cơ sở theo tỉnh khi khác vaac
        if (!getSite().isEmpty()) {
            for (SiteEntity siteEntity : getSite()) {
                sites.put(siteEntity.getID().toString(), siteEntity.getName());
            }

        }

        model.addAttribute("title", "Thêm mới cấu hình");
        model.addAttribute("breadcrumbTitle", "Cấu hình VNPT");
        model.addAttribute("breadcrumbUrl", UrlUtils.hisHIVIndex());
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("sites", sites);

        return "backend/vnpt_config/form";
    }

    @RequestMapping(value = {"/vnpt-config/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid VNPTConfigForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        Map<String, String> sites = new LinkedHashMap<>();
        sites.put("", "Chọn cơ sở");
        Map<String, String> status = new HashMap<>();
        status.put("", "Chọn trạng thái");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        //Lọc cơ sở theo tỉnh khi khác vaac
        if (!getSite().isEmpty()) {
            for (SiteEntity siteEntity : getSite()) {
                sites.put(siteEntity.getID().toString(), siteEntity.getName());
            }

        }
        model.addAttribute("title", "Thêm mới cấu hình");
        model.addAttribute("breadcrumbTitle", "Cấu hình VNPT");
        model.addAttribute("breadcrumbUrl", UrlUtils.hisHIVIndex());
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("sites", sites);

        //validate form
        configValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", " Thêm thông cấu hình không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/vnpt_config/form";
        }

        try {
            SiteVnptFkEntity siteVnptFkEntity = new SiteVnptFkEntity();
            siteVnptFkEntity.setID(Long.valueOf(form.getID()));
            siteVnptFkEntity.setVpntSiteID(form.getVpntSiteID());
            siteVnptFkEntity.setActive(form.getActive().equals("0"));

            configService.save(siteVnptFkEntity);
            redirectAttributes.addFlashAttribute("success", "Cấu hình được thêm mới thành công");
            redirectAttributes.addFlashAttribute("success_id", siteVnptFkEntity.getID());
            return redirect(UrlUtils.hisHIVIndex());

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "backend/vnpt_config/form";
    }

    @RequestMapping(value = {"/vnpt-config/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long id,
            VNPTConfigForm form,
            RedirectAttributes redirectAttributes) {

        SiteVnptFkEntity siteVnptFkEntity = configService.findOne(id);

        if (id == null || siteVnptFkEntity == null) {
            redirectAttributes.addFlashAttribute("warning", "Không tìm thấy cấu hình");
            return redirect(UrlUtils.hisHIVIndex());
        }
        //khởi tạo
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> sites = new LinkedHashMap<>();
        sites.put("", "Chọn cơ sở");
        Map<String, String> status = new HashMap<>();
        status.put("", "Chọn trạng thái");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        //Lọc cơ sở theo tỉnh khi khác vaac
        if (!getSite().isEmpty()) {
            for (SiteEntity siteEntity : getSite()) {
                sites.put(siteEntity.getID().toString(), siteEntity.getName());
            }

        }

        form.setID(String.valueOf(siteVnptFkEntity.getID()));
        form.setVpntSiteID(siteVnptFkEntity.getVpntSiteID());
        form.setActive(siteVnptFkEntity.isActive() ? "0" : "1");
        form.setCreateAt(siteVnptFkEntity.getCreateAT());

        model.addAttribute("title", "Cập nhật cấu hình");
        model.addAttribute("breadcrumbTitle", "Cấu hình VNPT");
        model.addAttribute("breadcrumbUrl", UrlUtils.hisHIVIndex());
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("sites", sites);

        return "backend/vnpt_config/form";
    }

    @RequestMapping(value = {"/vnpt-config/update.html"}, method = RequestMethod.POST)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long id,
            @ModelAttribute("form") @Valid VNPTConfigForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        SiteVnptFkEntity siteVnptFkEntity = configService.findOne(id);

        if (id == null || siteVnptFkEntity == null) {
            redirectAttributes.addFlashAttribute("warning", "Không tìm thấy cấu hình");
            return redirect(UrlUtils.hisHIVIndex());
        }
        //khởi tạo
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> sites = new LinkedHashMap<>();
        sites.put("", "Chọn cơ sở");
        Map<String, String> status = new HashMap<>();
        status.put("", "Chọn trạng thái");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        //Lọc cơ sở theo tỉnh khi khác vaac
        if (!getSite().isEmpty()) {
            for (SiteEntity siteEntity : getSite()) {
                sites.put(siteEntity.getID().toString(), siteEntity.getName());
            }

        }

        model.addAttribute("title", "Cập nhật cấu hình");
        model.addAttribute("breadcrumbTitle", "Cấu hình VNPT");
        model.addAttribute("breadcrumbUrl", UrlUtils.hisHIVIndex());
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("sites", sites);

        //validate form
        form.setCreateAt(siteVnptFkEntity.getCreateAT());
        form.setIDcheck(siteVnptFkEntity.getVpntSiteID());
        configValidate.validate(form, bindingResult);

        form.setID(siteVnptFkEntity.getID().toString());
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật cấu hình không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/vnpt_config/form";
        }

        try {
            siteVnptFkEntity.setVpntSiteID(form.getVpntSiteID());
            siteVnptFkEntity.setActive(form.getActive().equals("0"));

            configService.save(siteVnptFkEntity);
            redirectAttributes.addFlashAttribute("success", "Cấu hình được cập nhật thành công");
            redirectAttributes.addFlashAttribute("success_id", siteVnptFkEntity.getID());
            return redirect(UrlUtils.hisHIVIndex());

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "backend/vnpt_config/form";
    }

    @RequestMapping(value = {"/vnpt-config/log.html"}, method = RequestMethod.GET)
    public String actionLog(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "id", required = false, defaultValue = "") String vnptID,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        //Khởi tạo
        Map<String, String> sites = new LinkedHashMap<>();
        //Lấy dữ liệu
        DataPage<OpcApiLogEntity> dataPage = apiLogService.findAll(vnptID, status, from, to, page, size);
        for (SiteEntity siteEntity : siteService.findByService(ServiceEnum.OPC.getKey())) {
            sites.put(String.valueOf(siteEntity.getID()), siteEntity.getName());
        }
        int s = Integer.valueOf(logRepositoryImpl.getDasboard(vnptID, status, from, to).get("success"));
        int e = Integer.valueOf(logRepositoryImpl.getDasboard(vnptID, status, from, to).get("error"));

        int success = apiLogService.countAllLogs(vnptID, "1", from, to);
        int error = apiLogService.countAllLogs(vnptID, "2", from, to);

        model.addAttribute("title", "Lịch sử cấu hình API");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("ss", s);
        model.addAttribute("ex", e);
        model.addAttribute("vnptID", vnptID);
        model.addAttribute("tong", s + e);
        model.addAttribute("tongLog", success + error);
        model.addAttribute("tyle", String.format("Thành công: %s, Thất bại: %s", success, error));
        return "backend/vnpt_config/log";
    }

    private List<SiteEntity> getSite() {
        //Lọc cơ sở theo tỉnh khi khác vaac
        List<SiteEntity> sites = new ArrayList<>();
        LoggedUser currentUser = getCurrentUser();
        if (!isVAAC()) {
            for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), currentUser.getSite().getProvinceID())) {
                sites.add(siteEntity);
            }
        } else {
            for (SiteEntity siteEntity : siteService.findByService(ServiceEnum.OPC.getKey())) {
                sites.add(siteEntity);
            }
        }

        return sites;

    }

}
