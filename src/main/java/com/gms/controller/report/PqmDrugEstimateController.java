package com.gms.controller.report;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmDrugEstimateExcel;
import com.gms.entity.excel.opc.PqmEstimateDataExcel;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmEstimateForm;
import com.gms.entity.form.opc_arv.PqmEstimateTable;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.PqmDataService;
import com.gms.service.PqmDrugEstimateDataService;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmService;
import com.google.gson.Gson;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * BC PQM
 *
 * @author pdThang
 */
@Controller(value = "pqm_data_drug_estimate")
public class PqmDrugEstimateController extends OpcController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmDrugEstimateDataService pqmDrugDataService;
    @Autowired
    private PqmDrugPlanService drugPlanService;
    @Autowired
    private PqmDrugEstimateService drugEstimateService;
    @Autowired
    private HubUtils hubUtils;

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));

        options.put("years", new LinkedHashMap<String, String>());
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }

        return options;
    }

    @GetMapping(value = {"/pqm-drug-estimate/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        int q = StringUtils.isEmpty(month) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmEstimateForm form = getData(q, y, sites, tab);
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Chi tiết thuốc đã cấp");
        } else {
            model.addAttribute("title", "Chỉ số thuốc đã cấp");
        }

        model.addAttribute("parent_title", "Chỉ số thuốc ARV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Chọn cơ sở");

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        if (estimates != null) {
            for (PqmDrugEstimateEntity e : estimates) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        if (plans != null) {
            for (PqmDrugPlanEntity e : plans) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        model.addAttribute("sites", siteMap);
        model.addAttribute("tab", tab);
        model.addAttribute("siteID", sites);
        model.addAttribute("siteName", StringUtils.isEmpty(sites) ? "" : siteMap.getOrDefault(sites, ""));

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 4; i++) {
            PqmEstimateForm formQuater = getData(i, y, sites, tab);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }

        }

        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_estimate_index";
    }

    @GetMapping(value = {"/pqm-drug-estimate/index-view.html"})
    public String actionIndex2(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        int q = StringUtils.isEmpty(month) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmEstimateForm form = getData(q, y, sites, tab);
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Chi tiết thuốc đã cấp");
        } else {
            model.addAttribute("title", "Chỉ số thuốc đã cấp");
        }

        model.addAttribute("parent_title", "Chỉ số thuốc ARV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Chọn cơ sở");

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        if (estimates != null) {
            for (PqmDrugEstimateEntity e : estimates) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        if (plans != null) {
            for (PqmDrugPlanEntity e : plans) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        model.addAttribute("sites", siteMap);
        model.addAttribute("tab", tab);
        model.addAttribute("siteID", sites);
        model.addAttribute("siteName", StringUtils.isEmpty(sites) ? "" : siteMap.getOrDefault(sites, ""));

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 4; i++) {
            PqmEstimateForm formQuater = getData(i, y, sites, tab);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }
        }
        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_estimate_index_view";
    }

    @GetMapping(value = {"/pqm-drug-estimate/synthetic.html"})
    public String actionSynthetic(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        int q = StringUtils.isEmpty(month) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        int currentTime = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy") + "" + (TextUtils.getQuarter(new Date()) + 1));
        int paramTime = Integer.valueOf(year + month);

        if (currentTime == paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo!"));
            return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
        }
        if (currentTime < paramTime) { 
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của quý sau quý hiện tại. Vui lòng kiểm tra lại!"));
            return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
        }

        try {

            pqmDrugDataService.getDrugEstimate(q, y, getCurrentUser().getSite().getProvinceID());

            PqmEstimateForm form = getData(q, y, sites, tab);

            if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {
                Map<String, Object> result = hubUtils.sendDrugPlanToPqm(form);
                Gson g = new Gson();
                MainForm p = g.fromJson(g.toJson(result), MainForm.class);
                boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
                if (succeed) {
                    redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                    return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
                } else {
                    List<String> errors = new ArrayList<>();
                    if (p.getError() != null && StringUtils.isNotEmpty(p.getError().getErrorMessage())) {
                        errors.add(p.getError().getErrorMessage());
                    } else if (p.getData() != null && p.getData().getError_rows() != null && !p.getData().getError_rows().isEmpty()) {
                        for (error_rows error_row : p.getData().getError_rows()) {
                            errors.add(error_row.getError());
                        }
                    }
                    Set<String> errorx = new HashSet<>();
                    for (String error : errors) {
                        if (error.equals("Year is not defined")) {
                            errorx.add("Dữ liệu không có năm");
                        } else if (error.equals("Month is not defined")) {
                            errorx.add("Dữ liệu không có tháng");
                        } else if (error.contains("Site")) {
                            errorx.add("Không tìm thấy cơ sở #" + error.replaceAll("\\D+", "") + " trên Provincial Dashboards");
                        } else {
                            errorx.add(error);
                        }
                    }

                    redirectAttributes.addFlashAttribute("false_key", errorx);
                    redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp và gửi số liệu báo cáo lên PQM không thành công"));
                    return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
                }

            }
            redirectAttributes.addFlashAttribute("success", "Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công.");
            return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi khi tổng hợp dữ liệu.");
            return redirect("/report/pqm-drug-estimate/index.html" + "?year=" + y + "&month=" + q);
        }

    }

    private PqmEstimateForm getData(int quarter, int year, String sites, String tab) {
        LoggedUser currentUser = getCurrentUser();

        PqmEstimateForm form = new PqmEstimateForm();

        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("ChiSoThuocArv" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Chỉ số thuốc ARV");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setMonth(quarter);
        form.setYear(year);
        form.setSite(sites);
        form.setTab(tab);

        List<PqmDrugEstimateDataEntity> data = pqmDrugDataService.findData("drugs_plan", quarter, year, getCurrentUser().getSite().getProvinceID());
        Map<String, List<PqmDrugEstimateDataEntity>> map = new HashMap<>();
        if (data != null) {
            for (PqmDrugEstimateDataEntity e : data) {
                if (tab != null && tab.equals("detail")) {
                    if (!(e.getFacilityCode() != null && e.getFacilityCode().equals(sites))) {
                        continue;
                    }
                }
                String key = e.getDrugNameLowercase() + e.getDrugUomLowercase();
                if (map.getOrDefault(key, null) == null) {
                    map.put(key, new ArrayList<PqmDrugEstimateDataEntity>());
                }
                map.get(key).add(e);
            }

        }
        List<PqmEstimateTable> items = new ArrayList<>();
        PqmEstimateTable row;
        for (Map.Entry<String, List<PqmDrugEstimateDataEntity>> entry : map.entrySet()) {
            row = new PqmEstimateTable();
            row.setDispensedQuantity(0);
            row.setPlannedQuantity(0);
            List<PqmDrugEstimateDataEntity> value = entry.getValue();
            for (PqmDrugEstimateDataEntity e : value) {
                row.setDrugNameLowercase(e.getDrugName());
                row.setDrugUomLowercase(e.getDrugUom());
                row.setDispensedQuantity(row.getDispensedQuantity() + Integer.valueOf(e.getDispensedQuantity() + ""));
                row.setPlannedQuantity(row.getPlannedQuantity() + Integer.valueOf(e.getPlannedQuantity() + ""));
            }
            items.add(row);
        }
        form.setItems(items);

        //Xử lý để xuất API
        //xử lý tỉnh huyện PQM - CODE - API
        HashMap<String, String> provincePQM = new HashMap<>();
        HashMap<String, String> districtPQM = new HashMap<>();
        HashMap<String, String> provinceNamePQM = new HashMap<>();
        HashMap<String, String> districtNamePQM = new HashMap<>();
        for (ProvinceEntity p : locationsService.findAllProvince()) {
            provincePQM.put(p.getID(), p.getPqmCode());
            provinceNamePQM.put(p.getID(), p.getName());
        }
        for (DistrictEntity d : locationsService.findAllDistrict()) {
            districtPQM.put(d.getID(), d.getPqmCode());
            districtNamePQM.put(d.getID(), d.getName());
        }
        form.setDistrictPQM(districtPQM);
        form.setProvincePQM(provincePQM);
        form.setDistrictNamePQM(districtNamePQM);
        form.setProvinceNamePQM(provinceNamePQM);

        form.setCurrentProvinceID(getCurrentUser().getSite().getProvinceID());

        //site pqm code
        Map<String, String> mapSite = new HashMap<>();
        for (SiteEntity site : siteService.findAll()) {
            if (StringUtils.isNotEmpty(site.getHubSiteCode())) {
                mapSite.put(site.getPqmSiteCode(), site.getHubSiteCode());
            }
        }

        //Các trường sẵn có
        form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
        form.setDistrictPQMcode(districtPQM.getOrDefault(currentUser.getSite().getDistrictID(), ""));
        form.setSitePQMcodes(mapSite);

        form.setItemAPI(data);

        return form;
    }

    @GetMapping(value = {"/pqm-drug-estimate/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {

        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        int q = StringUtils.isEmpty(month) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        System.out.println(" param " + tab + " param " + q + " param " + y + " param " + sites);

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        Map<String, String> siteMap = new LinkedHashMap<>();
        if (estimates != null) {
            for (PqmDrugEstimateEntity e : estimates) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        if (plans != null) {
            for (PqmDrugPlanEntity e : plans) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        PqmEstimateForm form = getData(q, y, sites, tab);
        form.setSiteSearch(siteMap.getOrDefault(sites, ""));

        return exportExcel(new PqmDrugEstimateExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-drug-estimate/excel-data.html"})
    public ResponseEntity<InputStreamResource> actionExportExcelData(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {

        if (StringUtils.isEmpty(tab)) {
            tab = "x";
        }
        int q = StringUtils.isEmpty(month) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        System.out.println(" param " + tab + " param " + q + " param " + y + " param " + sites);

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        Map<String, String> siteMap = new LinkedHashMap<>();
        if (estimates != null) {
            for (PqmDrugEstimateEntity e : estimates) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        if (plans != null) {
            for (PqmDrugPlanEntity e : plans) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        PqmEstimateForm form = getData(q, y, sites, tab);
        form.setSiteSearch(siteMap.getOrDefault(sites, ""));
        form.setFileName("ThuocArvDaCap_" + "Quy" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(getCurrentUser().getSiteProvince().getName()));

        return exportExcel(new PqmEstimateDataExcel(form, EXTENSION_EXCEL_X));
    }

}
