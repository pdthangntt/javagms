package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PqmVctRecencySearch;
import com.gms.entity.validate.PqmVctValidate;
import com.gms.service.PqmVctRecencyService;
import com.gms.service.PqmVctService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Controller(value = "pqm_vct_recency")
public class PqmVctRecencyController extends PqmController {
    
    @Autowired
    private PqmVctRecencyService vctRecencyService;
    @Autowired
    private PqmVctService pqmVctService;
    @Autowired
    private PqmVctValidate pqmVctValidate;
    
    @GetMapping(value = {"/pqm-vct-recency/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false) String tab,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "earlyDiagnose", required = false) String earlyDiagnose,
            @RequestParam(name = "earlyHivDateFrom", required = false) String earlyHivDateFrom,
            @RequestParam(name = "earlyHivDateTo", required = false) String earlyHivDateTo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmVctRecencySearch search) {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);
        
        Set<Long> siteIDs = new HashSet<>();
        if (!isPAC()) {
            siteIDs.add(getCurrentUser().getSite().getID());
        } else {
            search.setProvinceID(Long.valueOf(getCurrentUser().getSiteProvince().getID()));
        }
        System.out.println("HOAAAAAAAa:" + getOptions().get("test-object-group").get("11"));
//        }
        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);
        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setEarlyDiagnose(StringUtils.isEmpty(earlyDiagnose) ? null : earlyDiagnose);
        search.setEarlyHivDateFrom(isThisDateValid(earlyHivDateFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateFrom) : null);
        search.setEarlyHivDateTo(isThisDateValid(earlyHivDateTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateTo) : null);
        
        DataPage<PqmVctRecencyEntity> dataPage = vctRecencyService.find(search);
        model.addAttribute("title", "Khách hàng nhiễm mới");
        model.addAttribute("parent_title", "Khách hàng nhiễm mới");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tab", tab);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("options", getOptions());
        return "backend/pqm/vct_recency";
    }
    
    @GetMapping(value = {"/pqm-vct-recency/update.html"})
    public String actionUpdateFromVct(Model model, RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        Set<Long> siteIDs = new HashSet<>();
        for (SiteEntity site : siteHtc) {
            siteIDs.add(site.getID());
        }
        try {
            List<PqmVctRecencyEntity> items = vctRecencyService.findByProvinceIDAndVctID(Long.valueOf(getCurrentUser().getSite().getProvinceID()));
            if (items != null) {
                for (PqmVctRecencyEntity item : items) {
                    List<PqmVctEntity> vcts = pqmVctService.findByCodeAndSiteIDs(item.getCode(), siteIDs);
                    if (vcts != null && !vcts.isEmpty() && vcts.size() == 1) {
                        PqmVctEntity vct = vcts.get(0).clone();
                        vct.setEarlyHivDate(item.getEarlyHivDate());
                        vct.setEarlyDiagnose(item.getEarlyDiagnose());
                        pqmVctService.save(vct);
                        item.setPqmVctID(vct.getID());
                        vctRecencyService.save(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật thành công thông tin nhiễm mới cho Khách hàng xét nghiệm");
        return redirect("/backend/pqm-vct-recency/index.html");
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
}
