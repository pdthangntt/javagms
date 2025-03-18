package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.PqmDataForm;
import com.gms.service.HtcVisitService;
import com.gms.service.PqmDataService;
import com.gms.service.PqmService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BC PQM HTC
 *
 * @author pdThang
 */
@Controller(value = "pqm_data_reports")
public class PqmDataPlanController extends OpcController {

    @Autowired
    private PqmDataService pqmDataService;
    @Autowired
    private PqmService pqmService;
    @Autowired
    private HtcVisitService htcVisitService;

    private HashMap<String, SiteEntity> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            option.put(siteEntity.getID().toString(), siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.PREP.getKey(), currentUser.getSite().getProvinceID())) {
            option.put(siteEntity.getID().toString(), siteEntity);
        }
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), currentUser.getSite().getProvinceID())) {
            option.put(siteEntity.getID().toString(), siteEntity);
        }

        return option;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        HtcVisitEntity entity = htcVisitService.getFirst(siteIds);
        if (entity != null) {
            c.setTime(entity.getAdvisoryeTime());
        }
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    private PqmDataForm getData(String month, String year, String sites, int page, int size) {
        LoggedUser currentUser = getCurrentUser();

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        Date firstDay = TextUtils.getFirstDayOfMonth(m, y);
        Date lastDay = TextUtils.getLastDayOfMonth(m, y);

        PqmDataForm form = new PqmDataForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("DULIEU_PQM_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Dữ liệu PQM");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setMonth(m);
        form.setYear(y);
        form.setSites(sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1))));

        HashMap<String, String> siteOption = new LinkedHashMap<>();
        siteOption.put("", "Tất cả");
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            String key = entry.getKey();
            SiteEntity siteEntity = entry.getValue();
            siteOption.put(siteEntity.getID().toString(), siteEntity.getName());
        }
        form.setSiteOptions(siteOption);

        Set<Long> siteIDs = new HashSet<>();
        if (form.getSites() != null) {
            for (String site : form.getSites()) {
                siteIDs.add(Long.valueOf(site));
            }
        } else {
            siteIDs.addAll(getSiteIds());
        }
        DataPage<PqmDataEntity> dataPage = pqmDataService.findData(m, y, siteIDs, page, size);
        form.setDataPage(dataPage);
        return form;
    }

    @GetMapping(value = {"/pqm-data/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size) throws Exception {
        PqmDataForm form = getData(month, year, sites, page, size);
        model.addAttribute("parent_title", "PQM");
        model.addAttribute("title", "Dữ liệu PQM");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("dataPage", form.getDataPage());
        model.addAttribute("isPAC", isPAC());
        return "report/pqm/pqm_data";
    }

}
