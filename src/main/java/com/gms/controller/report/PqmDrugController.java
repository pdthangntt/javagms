package com.gms.controller.report;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.PqmProportionEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmDrugPlanDataExcel;
import com.gms.entity.excel.opc.PqmPlanDataExcel;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmPlanForm;
import com.gms.entity.form.report.PqmDrugPlanForm;
import com.gms.repository.impl.PqmDrugPlanImpl;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanDataService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmProportionService;
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
 * BC PQM HTC
 *
 * @author pdThang
 */
@Controller(value = "pqm_data_plan")
public class PqmDrugController extends OpcController {

    @Autowired
    private PqmProportionService pqmProportionService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private PqmDrugPlanDataService pqmDrugDataService;
    @Autowired
    private PqmDrugPlanImpl pqmDrugPlanImpl;
    @Autowired
    private PqmDrugPlanService drugPlanService;
    @Autowired
    private PqmDrugEstimateService drugEstimateService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private HubUtils hubUtils;

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
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    @GetMapping(value = {"/pqm-drug-plan/index.html"})
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
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Chi tiết thuốc dự trù an toàn");
        } else {
            model.addAttribute("title", "Tổng hợp thuốc dự trù an toàn");
        }

        Integer yearS = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        Integer monthS = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        if (!StringUtils.isEmpty(month)) {
            monthS = Integer.parseInt(month);
        }
        if (!StringUtils.isEmpty(year)) {
            yearS = Integer.parseInt(year);
        }
        List<PqmDrugPlanDataEntity> lists = pqmDrugPlanImpl.getDataIndex(yearS, monthS, getCurrentUser().getSite().getProvinceID(), sites);
//        Map<String, String> site = pqmDrugPlanImpl.getAllSites(yearS, monthS, getCurrentUser().getSite().getProvinceID());

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Chọn cơ sở");
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

        Map<String, String> monthIsReport = new HashMap<String, String>();
        for (int i = 1; i <= 12; i++) {
            List<PqmDrugPlanDataEntity> listData = pqmDrugPlanImpl.getDataIndex(yearS, i, getCurrentUser().getSite().getProvinceID(), sites);
            if (listData != null && !listData.isEmpty()) {
                monthIsReport.put(String.valueOf(i), "2");
            }
        }
        String siteName = "";
        if (tab.equals("detail") && !sites.isEmpty()) {
            siteName = siteMap.getOrDefault(sites, "");
        }
//        site.put("", "Chọn cơ sở");
        model.addAttribute("items", lists);
        model.addAttribute("sites", siteMap);
        model.addAttribute("siteCode", sites);
        model.addAttribute("siteName", siteName);
        model.addAttribute("options", getOptions());
        model.addAttribute("year", yearS);
        model.addAttribute("month", monthS);
        model.addAttribute("monthKey", monthIsReport);
        model.addAttribute("tab", tab);
        model.addAttribute("isPAC", isPAC());
        return "report/pqm/pqm_drug_index";
    }

    @GetMapping(value = {"/pqm-drug-plan/index-view.html"})
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
        if (tab != null && tab.equals("detail")) {
            model.addAttribute("title", "Chi tiết thuốc dự trù an toàn");
        } else {
            model.addAttribute("title", "Tổng hợp thuốc dự trù an toàn");
        }

        Integer yearS = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        Integer monthS = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        if (!StringUtils.isEmpty(month)) {
            monthS = Integer.parseInt(month);
        }
        if (!StringUtils.isEmpty(year)) {
            yearS = Integer.parseInt(year);
        }
        List<PqmDrugPlanDataEntity> lists = pqmDrugPlanImpl.getDataIndex(yearS, monthS, getCurrentUser().getSite().getProvinceID(), sites);
//        Map<String, String> site = pqmDrugPlanImpl.getAllSites(yearS, monthS, getCurrentUser().getSite().getProvinceID());

        List<PqmDrugEstimateEntity> estimates = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
        List<PqmDrugPlanEntity> plans = drugPlanService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());

        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Chọn cơ sở");
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

        Map<String, String> monthIsReport = new HashMap<String, String>();
        for (int i = 1; i <= 12; i++) {
            List<PqmDrugPlanDataEntity> listData = pqmDrugPlanImpl.getDataIndex(yearS, i, getCurrentUser().getSite().getProvinceID(), sites);
            if (listData != null && !listData.isEmpty()) {
                monthIsReport.put(String.valueOf(i), "2");
            }
        }
        String siteName = "";
        if (tab.equals("detail") && !sites.isEmpty()) {
            siteName = siteMap.getOrDefault(sites, "");
        }
//        site.put("", "Chọn cơ sở");
        model.addAttribute("items", lists);
        model.addAttribute("sites", siteMap);
        model.addAttribute("siteCode", sites);
        model.addAttribute("siteName", siteName);
        model.addAttribute("options", getOptions());
        model.addAttribute("year", yearS);
        model.addAttribute("month", monthS);
        model.addAttribute("monthKey", monthIsReport);
        model.addAttribute("tab", tab);
        model.addAttribute("isPAC", isPAC());
        return "report/pqm/pqm_drug_index_view";
    }

    @GetMapping(value = {"/pqm-drug-plan/synthetic.html"})
    public String actionSynthetic(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) throws Exception {

        int currentTime = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyyMM"));
        int paramTime = Integer.valueOf(year + (Integer.valueOf(month) < 10 ? "0" + month : month));

        if (currentTime == paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo!"));
            return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
        }
        if (currentTime < paramTime) { 
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của tháng sau tháng hiện tại. Vui lòng kiểm tra lại!"));
            return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
        }

        LoggedUser currentUser = getCurrentUser();
        Set<String> provinces = new HashSet<>();
        provinces.add(currentUser.getSite().getProvinceID());
        List<PqmProportionEntity> listTT = pqmProportionService.findByProvince(provinces);
        if (listTT == null || listTT.size() <= 0) {
            redirectAttributes.addFlashAttribute("error", String.format("Phải cấu hình tỷ trọng trước khi thực hiện tổng hợp."));
            return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
        }
        Map<String, String> mapPro = new HashMap<>();
        for (PqmProportionEntity pqmProportionEntity : listTT) {
            mapPro.put(pqmProportionEntity.getCode(), pqmProportionEntity.getValue());
        }

        pqmDrugDataService.resetDataProvince(
                Integer.parseInt(month),
                Integer.parseInt(year),
                currentUser.getSite().getProvinceID());

        List<PqmDrugPlanForm> list = pqmDrugPlanImpl.getDataDrugPlanData(Integer.parseInt(year), Integer.parseInt(month), currentUser.getSite().getProvinceID());
        for (PqmDrugPlanForm pqmDrugPlanForm : list) {
            PqmDrugPlanDataEntity entity = pqmDrugDataService.findByUniqueConstraints(Integer.parseInt(month), Integer.parseInt(year), currentUser.getSite().getProvinceID(), pqmDrugPlanForm.getSiteCode(), pqmDrugPlanForm.getDrugName(), pqmDrugPlanForm.getUnit());
            if (entity == null) {
                entity = new PqmDrugPlanDataEntity();
            }
            entity.setDrugName(pqmDrugPlanForm.getDrugName());
            entity.setUnit(pqmDrugPlanForm.getUnit());
            entity.setYear(Integer.parseInt(year));
            entity.setMonth(Integer.parseInt(month));
            entity.setProvinceID(currentUser.getSite().getProvinceID());
            entity.setSiteCode(pqmDrugPlanForm.getSiteCode());
            entity.setSiteName(pqmDrugPlanForm.getSiteName());
            entity.setTotalDrugBacklog(pqmDrugPlanForm.getTotalEndPeriod());
            entity.setTotalDrugPlan2Month(getTotalDrugPlan2Month(pqmDrugPlanForm.getD1(), pqmDrugPlanForm.getD2(), pqmDrugPlanForm.getD3(), mapPro.get("T1"), mapPro.get("T2"), mapPro.get("T3")).longValue());
            pqmDrugDataService.save(entity);
        }

        List<PqmDrugPlanDataEntity> lists = pqmDrugPlanImpl.getDataIndex(Integer.parseInt(year), Integer.parseInt(month), getCurrentUser().getSite().getProvinceID(), "");

        if (!lists.isEmpty()) {
            PqmPlanForm form = new PqmPlanForm();

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
            form.setMonth(Integer.parseInt(month));
            form.setYear(Integer.parseInt(year));

            form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
            form.setDistrictPQMcode(districtPQM.getOrDefault(currentUser.getSite().getDistrictID(), ""));
            form.setSitePQMcodes(mapSite);

            form.setItems(lists);

            Map<String, Object> result = hubUtils.sendDrugPlan2ToPqm(form);
            Gson g = new Gson();
            MainForm p = g.fromJson(g.toJson(result), MainForm.class);
            boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
            if (succeed) {
                redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
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
                return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
            }

        }

        redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công."));
        return redirect(UrlUtils.pqmReportDrugPlanIndex() + String.format("?year=%s&month=%s", year, month));
    }

    private Integer getTotalDrugPlan2Month(Long d1, Long d2, Long d3, String pro1, String pro2, String pro3) {
        return Math.round((((d1 / Float.valueOf(pro1) + d2 / Float.valueOf(pro2) + d3 / Float.valueOf(pro3)) / 3) * 2));
    }

    @GetMapping(value = {"/pqm-drug-plan/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        Integer yearS = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        Integer monthS = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        if (!StringUtils.isEmpty(month)) {
            monthS = Integer.parseInt(month);
        }
        if (!StringUtils.isEmpty(year)) {
            yearS = Integer.parseInt(year);
        }
        Map<String, String> siteMap = new LinkedHashMap<>();
        List<PqmDrugPlanDataEntity> lists = pqmDrugPlanImpl.getDataIndex(yearS, monthS, currentUser.getSite().getProvinceID(), sites);
        if (lists != null) {
            for (PqmDrugPlanDataEntity e : lists) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        String siteNameSearch = siteMap.getOrDefault(sites, "");
        return exportExcel(new PqmDrugPlanDataExcel(lists, yearS, monthS, currentUser.getSite().getName(), EXTENSION_EXCEL_X, tab, siteNameSearch));
    }

    @GetMapping(value = {"/pqm-drug-plan/excel-data.html"})
    public ResponseEntity<InputStreamResource> actionExportExcelData(
            @RequestParam(name = "tab", required = false, defaultValue = "x") String tab,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        Integer yearS = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        Integer monthS = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        if (!StringUtils.isEmpty(month)) {
            monthS = Integer.parseInt(month);
        }
        if (!StringUtils.isEmpty(year)) {
            yearS = Integer.parseInt(year);
        }
        Map<String, String> siteMap = new LinkedHashMap<>();
        List<PqmDrugPlanDataEntity> lists = pqmDrugDataService.getDataDetail(monthS, yearS, getCurrentUser().getSite().getProvinceID());

        if (lists != null) {
            for (PqmDrugPlanDataEntity e : lists) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }
        String siteNameSearch = siteMap.getOrDefault(sites, "");
        return exportExcel(new PqmPlanDataExcel(lists, yearS, monthS, currentUser.getSite().getName(), EXTENSION_EXCEL_X, tab, siteNameSearch, getCurrentUser().getSiteProvince().getName()));
    }
}
