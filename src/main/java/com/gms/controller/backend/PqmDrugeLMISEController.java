package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PqmDrugeLMISESearch;
import com.gms.service.PqmDrugeLMISEService;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_drug_elmise")
public class PqmDrugeLMISEController extends PqmController {

    @Autowired
    private PqmDrugeLMISEService drugeLMISEService;

    @GetMapping(value = {"/pqm-drug-elmise/index.html"})
    public String actionIndexEstimate(Model model,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size, PqmDrugeLMISESearch search) {

        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(99999);
        search.setYear(y);
        search.setQuarter(q);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugeLMISEEntity> dataPages = drugeLMISEService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : siteOpc) {
            siteMap.put(String.valueOf(site.getID()), site.getName());

        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);

        DataPage<PqmDrugeLMISEEntity> dataPage = drugeLMISEService.find(search);
        Map<String, String> years = new LinkedHashMap<>();
        Map<String, String> quarters = new LinkedHashMap<>();

        for (int i = 1900; i < 2200; i++) {
            years.put(String.valueOf(i), "Năm " + i);
        }
        quarters.put("1", "Quý " + 1);
        quarters.put("2", "Quý " + 2);
        quarters.put("3", "Quý " + 3);
        quarters.put("4", "Quý " + 4);

        model.addAttribute("title", "A06. Tình hình cung ứng, sử dụng thuốc ARV");
        model.addAttribute("parent_title", "Điều trị ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("sites", sites);
        model.addAttribute("years", years);
        model.addAttribute("quarters", quarters);
        model.addAttribute("year", y + "");
        model.addAttribute("quarter", q + "");
        model.addAttribute("options", getOptions());
        return "backend/pqm/drug_elmies";
    }

}
