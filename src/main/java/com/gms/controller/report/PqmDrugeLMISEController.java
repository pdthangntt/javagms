package com.gms.controller.report;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmDrugEstimateExcel;
import com.gms.entity.excel.opc.PqmDrugeLMISEExcel;
import com.gms.entity.excel.opc.PqmElmisDataExcel;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmeLMISEForm;
import com.gms.entity.form.opc_arv.PqmeLMISETable;
import com.gms.repository.impl.PqmDrugPlanImpl;
import com.gms.service.LocationsService;
import com.gms.service.PqmDrugeLMISEDataService;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import com.google.gson.Gson;
import java.util.ArrayList;
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
@Controller(value = "pqm_data_drug_elmise")
public class PqmDrugeLMISEController extends OpcController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmDrugeLMISEDataService pqmDrugDataService;
    @Autowired
    private PqmDrugPlanImpl pqmDrugPlanImpl;
    @Autowired
    private HubUtils hubUtils;

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = 1990; i < 2200; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }

        return options;
    }

    @GetMapping(value = {"/pqm-drug-elmise/index.html"})
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

        PqmeLMISEForm form = getData(q, y, sites, tab);
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Kết quả chỉ số thuốc ARV (eLMIS)");
        } else {
            model.addAttribute("title", "Kết quả chỉ số thuốc ARV (eLMIS)");
        }

        model.addAttribute("parent_title", "Chỉ số thuốc ARV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        List<SiteEntity> siteOpcs = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        Map<String, String> siteMaps = new HashMap<>();
        siteMaps.put("", "Tất cả");
        for (SiteEntity site : siteOpcs) {
            siteMaps.put(site.getID().toString(), site.getName());
        }

        model.addAttribute("sites", siteMaps);
        model.addAttribute("tab", tab);
        model.addAttribute("siteID", sites);
        model.addAttribute("siteName", StringUtils.isEmpty(sites) ? "" : siteMaps.getOrDefault(sites, ""));

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 4; i++) {
            PqmeLMISEForm formQuater = getData(i, y, sites, tab);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }

        }

        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_elmise_index";
    }

    @GetMapping(value = {"/pqm-drug-elmise/index-view.html"})
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

        PqmeLMISEForm form = getData(q, y, sites, tab);
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Kết quả chỉ số thuốc ARV (eLMIS)");
        } else {
            model.addAttribute("title", "Kết quả chỉ số thuốc ARV (eLMIS)");
        }

        model.addAttribute("parent_title", "Chỉ số thuốc ARV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        List<SiteEntity> siteOpcs = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        Map<String, String> siteMaps = new HashMap<>();
        siteMaps.put("", "Tất cả");
        for (SiteEntity site : siteOpcs) {
            siteMaps.put(site.getID().toString(), site.getName());
        }

        model.addAttribute("sites", siteMaps);
        model.addAttribute("tab", tab);
        model.addAttribute("siteID", sites);
        model.addAttribute("siteName", StringUtils.isEmpty(sites) ? "" : siteMaps.getOrDefault(sites, ""));

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 4; i++) {
            PqmeLMISEForm formQuater = getData(i, y, sites, tab);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }
        }
        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_elmise_index_view";
    }

    @GetMapping(value = {"/pqm-drug-elmise/synthetic.html"})
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
        if (currentTime < paramTime) {
            
        }
        if (currentTime == paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo!"));
            return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
        }
        if (currentTime < paramTime) { 
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của quý sau quý hiện tại. Vui lòng kiểm tra lại!"));
            return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
        }

        try {
            pqmDrugDataService.resetDataProvince(q, y, getCurrentUser().getSite().getProvinceID());
            List<PqmeLMISETable> data = pqmDrugPlanImpl.getARV_2MoS(q, y, getCurrentUser().getSite().getProvinceID());

            List<PqmDrugeLMISEDataEntity> itemAPI = new ArrayList<>();

            PqmDrugeLMISEDataEntity entity;
            for (PqmeLMISETable p : data) {
                entity = new PqmDrugeLMISEDataEntity();
                entity.setSiteID(p.getSiteID());
                entity.setYear(y);
                entity.setQuarter(q);
                entity.setProvinceID(getCurrentUser().getSite().getProvinceID());
                entity.setDrugName(p.getDrugName());
                entity.setTon(p.getTon());
                entity.setThang(p.getThang());
                entity.setSuDung(p.getSuDung());
                entity.setTrongKy(p.getTrongKy());

                entity = pqmDrugDataService.save(entity);
                itemAPI.add(entity);
            }

            PqmeLMISEForm form = getData(q, y, sites, tab);
            form.setItemAPI(itemAPI);

            if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {

                Map<String, Object> result = hubUtils.sendDrugPlanToPqmELMIS(form);
                Map<String, Object> result2 = hubUtils.sendDrugPlanToPqmELMIS2(form);

                result.putAll(result2);

                Gson g = new Gson();
                MainForm p = g.fromJson(g.toJson(result), MainForm.class);
                boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
                if (succeed) {
                    redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                    return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
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
                    return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
                }

            }
            redirectAttributes.addFlashAttribute("success", "Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công.");
            return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi khi tổng hợp dữ liệu.");
            return redirect("/report/pqm-drug-elmise/index.html" + "?year=" + y + "&month=" + q);
        }

    }

    private PqmeLMISEForm getData(int quarter, int year, String site, String tab) {

        Long sites = StringUtils.isEmpty(site) ? null : Long.valueOf(site);

        LoggedUser currentUser = getCurrentUser();

        PqmeLMISEForm form = new PqmeLMISEForm();

        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("ChiSoThuocArv" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Kết quả chỉ số thuốc ARV (eLMIS)");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setMonth(quarter);
        form.setYear(year);
        form.setSite(sites);
        form.setTab(tab);

        if (StringUtils.isNotBlank(site)) {
            Set<Long> siteSearch = new HashSet<>();
            siteSearch.add(sites);
            form.setSiteSearch(siteService.findByIDs(siteSearch).get(0).getName());
        }

        List<PqmDrugeLMISEDataEntity> data = pqmDrugDataService.findData(quarter, year, getCurrentUser().getSite().getProvinceID());
        Map<String, List<PqmDrugeLMISEDataEntity>> map = new HashMap<>();
        if (data != null) {
            for (PqmDrugeLMISEDataEntity e : data) {
                if (tab != null && tab.equals("detail")) {
                    if (!(e.getSiteID() != null && e.getSiteID().equals(sites))) {
                        continue;
                    }
                }
                String key = e.getDrugName();
                if (map.getOrDefault(key, null) == null) {
                    map.put(key, new ArrayList<PqmDrugeLMISEDataEntity>());
                }
                map.get(key).add(e);
            }

        }
        List<PqmeLMISETable> items = new ArrayList<>();

        PqmeLMISETable row;
        Long zezo = Long.valueOf("0");

        for (Map.Entry<String, List<PqmDrugeLMISEDataEntity>> entry : map.entrySet()) {
            row = new PqmeLMISETable();
            row.setDrugName(entry.getKey());
            row.setTon(zezo);
            row.setThang(Double.valueOf("0"));
            row.setSuDung(zezo);
            row.setTrongKy(zezo);
            List<PqmDrugeLMISEDataEntity> value = entry.getValue();
            for (PqmDrugeLMISEDataEntity e : value) {
                row.setTon(row.getTon() + e.getTon());
                row.setThang(row.getThang() + e.getThang());
                row.setSuDung(row.getSuDung() + e.getSuDung());
                row.setTrongKy(row.getTrongKy() + e.getTrongKy());
            }
            if (!(row.getTon().equals(Long.valueOf("0"))
                    && row.getThang().equals(Double.valueOf("0"))
                    && row.getSuDung().equals(Long.valueOf("0"))
                    && row.getTrongKy().equals(Long.valueOf("0")))) {
                items.add(row);
            }

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
        for (SiteEntity sitet : siteService.findAll()) {
            if (StringUtils.isNotEmpty(sitet.getHubSiteCode())) {
                mapSite.put(sitet.getID().toString(), sitet.getHubSiteCode());
            }
        }

        //Các trường sẵn có
        form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
        form.setDistrictPQMcode(districtPQM.getOrDefault(currentUser.getSite().getDistrictID(), ""));
        form.setSitePQMcodes(mapSite);

        form.setItemAPI(data);

        return form;
    }

    @GetMapping(value = {"/pqm-drug-elmise/excel.html"})
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

        PqmeLMISEForm form = getData(q, y, sites, tab);

        return exportExcel(new PqmDrugeLMISEExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-drug-elmise/excel-data.html"})
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

        PqmeLMISEForm form = getData(q, y, sites, tab);
        form.setFileName("ThuocArvElmis_" + "Quy" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(getCurrentUser().getSiteProvince().getName()));

        return exportExcel(new PqmElmisDataExcel(form, EXTENSION_EXCEL_X));
    }
}
