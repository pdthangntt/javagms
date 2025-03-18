package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PqmLogEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PqmLogSearch;
import com.gms.service.PqmLogService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_log")
public class PqmLogController extends PqmController {

    @Autowired
    private PqmLogService logService;

    @GetMapping(value = {"/pqm-log/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "service", required = false) String service,
            @RequestParam(name = "site", required = false) String site,
            @RequestParam(name = "object", required = false) String object,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmLogSearch search) {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setProvinceID(currentUser.getSite().getProvinceID());
        search.setFrom(isThisDateValid(from) ? from : null);
        search.setTo(isThisDateValid(to) ? to : null);
        search.setService(StringUtils.isEmpty(service) ? null : service);
        Long siteID = StringUtils.isEmpty(site) ? Long.valueOf("99999") : Long.valueOf(site);
        search.setSite(new HashSet<Long>());
        search.getSite().add(isPAC() ? siteID : getCurrentUser().getSite().getID());
        search.setObject(StringUtils.isEmpty(object) ? null : object);
        if (!isPAC()) {
            search.setObject("Cơ sở");
        }
        search.setFrom(isThisDateValid(from) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, from) : null);
        search.setTo(isThisDateValid(to) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, to) : null);
        DataPage<PqmLogEntity> dataPage = logService.find(search);
        //site
        String provinceID = getCurrentUser().getSite().getProvinceID();
        Map<String, String> sites = new HashMap<>();
        Map<String, String> services = new HashMap<>();
        Map<String, String> objects = new HashMap<>();
        sites.put("", "Tất cả");
        objects.put("", "Tất cả");
        objects.put("Tuyến tỉnh", "Tuyến tỉnh");
        objects.put("Cơ sở", "Cơ sở");
        services.put("", "Tất cả");
        services.put("Tư vấn & xét nghiệm", "Tư vấn & xét nghiệm");
        services.put("Xét nghiệm khẳng định", "Xét nghiệm khẳng định");
        services.put("Điều trị ARV", "Điều trị ARV");
        services.put("Dịch vụ PrEP", "Dịch vụ PrEP");
        services.put("Bảo hiểm y tế", "Bảo hiểm y tế");
        services.put("Thuốc ARV", "Thuốc ARV");

        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC.getKey())) {
            sites.put(siteEntity.getID().toString(), siteEntity.getName());

        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.HTC_CONFIRM.getKey())) {
            sites.put(siteEntity.getID().toString(), siteEntity.getName());

        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.OPC.getKey())) {
            sites.put(siteEntity.getID().toString(), siteEntity.getName());
        }
        for (SiteEntity siteEntity : getSites().get(ServiceEnum.PREP.getKey())) {
            sites.put(siteEntity.getID().toString(), siteEntity.getName());
        }

        model.addAttribute("title", "Lịch sử nhập liệu");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("services", services);
        model.addAttribute("objects", objects);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("options", getOptions());
        return "backend/pqm/log";
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private HashMap<String, List<SiteEntity>> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, List<SiteEntity>> option = new LinkedHashMap<>();

        option.put(ServiceEnum.HTC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.OPC.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.PREP.getKey(), new ArrayList<SiteEntity>());
        option.put(ServiceEnum.HTC_CONFIRM.getKey(), new ArrayList<SiteEntity>());

        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.HTC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.OPC.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.PREP.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.PREP.getKey()).add(siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), currentUser.getSite().getProvinceID())) {
            option.get(ServiceEnum.HTC_CONFIRM.getKey()).add(siteEntity);
        }

        return option;
    }

}
