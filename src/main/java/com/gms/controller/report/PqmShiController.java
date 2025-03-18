package com.gms.controller.report;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmShiDataExcel;
import com.gms.entity.excel.opc.PqmShiExcel;
import com.gms.entity.form.form_error.MainForm;
import com.gms.entity.form.form_error.error_rows;
import com.gms.entity.form.opc_arv.PqmShiForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import com.gms.service.LocationsService;
import com.gms.service.PqmShiArtService;
import com.gms.service.PqmShiDataService;
import com.gms.service.PqmShiMmdService;
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
@Controller(value = "pqm_data_shi")
public class PqmShiController extends OpcController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmShiDataService dataService;
    @Autowired
    private PqmShiArtService artService;
    @Autowired
    private PqmShiMmdService mmdService;
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

    @GetMapping(value = {"/pqm-shi/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) throws Exception {

        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int q = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmShiForm form = getData(q, y);
        model.addAttribute("title", "Kết quả chỉ số Bảo hiểm y tế");

        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 12; i++) {
            PqmShiForm formQuater = getData(i, y);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }

        }

        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_shi";
    }

    @GetMapping(value = {"/pqm-shi/index-view.html"})
    public String actionIndex2(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) throws Exception {

        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int q = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmShiForm form = getData(q, y);
        model.addAttribute("title", "Kết quả chỉ số Bảo hiểm y tế");

        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 12; i++) {
            PqmShiForm formQuater = getData(i, y);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }

        }

        model.addAttribute("monthKey", form.getKeyMonth());
        model.addAttribute("month", month);

        return "report/pqm/pqm_shi_view";
    }

    @GetMapping(value = {"/pqm-shi/synthetic.html"})
    public String actionSynthetic(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) throws Exception {

        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int m = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        int currentTime = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyyMM"));
        int paramTime = Integer.valueOf(year + (Integer.valueOf(month) < 10 ? "0" + month : month));

        if (currentTime == paramTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo!"));
            return redirect("/report/pqm-shi/index.html" + "?year=" + y + "&month=" + m);
        }
        if (currentTime < paramTime) { 
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của tháng sau tháng hiện tại. Vui lòng kiểm tra lại!"));
            return redirect("/report/pqm-shi/index.html" + "?year=" + y + "&month=" + m);
        }

        try {
            LoggedUser currentUser = getCurrentUser();
            dataService.resetDataProvince(m, y, currentUser.getSite().getProvinceID());

            PqmShiForm form = getData(m, y);

            PqmShiDataEntity e;
            if (form.getRowData() != null) {
                for (PqmShiTable item : form.getRowData()) {
                    e = new PqmShiDataEntity();
                    e.setSiteID(Long.valueOf(item.getSite()));
                    e.setProvinceID(getCurrentUser().getSite().getProvinceID());
                    e.setMonth(m);
                    e.setYear(y);

                    e.setShi_art(item.getArv());
                    e.setShi_mmd(item.getDrug());

                    dataService.save(e);
                }
            }

            PqmShiForm form2 = getData(m, y);

            Map<String, Object> result = hubUtils.sendShiToPqm(form2);
            Gson g = new Gson();
            MainForm p = g.fromJson(g.toJson(result), MainForm.class);
            boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
            if (succeed) {
                redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
                return redirect("/report/pqm-shi/index.html" + "?year=" + y + "&month=" + m);
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
                return redirect("/report/pqm-shi/index.html" + "?year=" + y + "&month=" + m);
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp và gửi số liệu báo cáo lên PQM không thành công"));
            return redirect("/report/pqm-shi/index.html" + "?year=" + y + "&month=" + m);
        }

    }

    private PqmShiForm getData(int month, int year) {
        LoggedUser currentUser = getCurrentUser();

        PqmShiForm form = new PqmShiForm();

        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("pqm_shi" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Kết quả chỉ số Bảo hiểm y tế");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setMonth(month);
        form.setYear(year);

        Map<Long, String> siteMap = new HashMap<>();
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.OPC.getKey(), currentUser.getSite().getProvinceID())) {
            siteMap.put(siteEntity.getID(), siteEntity.getName());
        }

        List<PqmShiArtEntity> arts = artService.findByProvinceIdAndMonthAndYear(currentUser.getSite().getProvinceID(), month, year);
        List<PqmShiMmdEntity> mmds = mmdService.findDataReport(currentUser.getSite().getProvinceID(), month, year);

        PqmShiTable row;

        Map<Long, PqmShiTable> mapData = new HashMap<>();
        if (arts != null) {
            for (PqmShiArtEntity item : arts) {
                row = new PqmShiTable();
                row.setSite(item.getSiteID().toString());
                row.setArv(item.getBnnt());
                mapData.put(item.getSiteID(), row);
            }
        }
        if (mmds != null) {
            for (PqmShiMmdEntity item : mmds) {
                if (mapData.getOrDefault(item.getSiteID(), null) == null) {
                    row = new PqmShiTable();
                    row.setSite(item.getSiteID().toString());
                    row.setDrug(item.getTotalPatient3MonthTk());
                    mapData.put(item.getSiteID(), row);

                } else {
                    mapData.get(item.getSiteID()).setDrug(item.getTotalPatient3MonthTk());
                }
            }
        }

        List<PqmShiTable> items = new ArrayList<>();
        List<PqmShiTable> datas = new ArrayList<>();

        for (Map.Entry<Long, PqmShiTable> entry : mapData.entrySet()) {
            PqmShiTable item = entry.getValue();
            row = new PqmShiTable();
            row.setSite(item.getSite());
            row.setArv(item.getArv());
            row.setDrug(item.getDrug());
            items.add(row);
        }
        form.setRowData(items);//item thông sau khi tổng hợp

        List<PqmShiDataEntity> data = dataService.findData(month, year, currentUser.getSite().getProvinceID());
        if (data != null) {
            for (PqmShiDataEntity item : data) {
                row = new PqmShiTable();
                row.setSite(siteMap.getOrDefault(item.getSiteID(), "Cơ sở đã bị xóa hoặc không tồn tại"));
                row.setArv(item.getShi_art());
                row.setDrug(item.getShi_mmd());
                datas.add(row);

                form.setTotalArv(form.getTotalArv() + item.getShi_art());
                form.setTotalDrug(form.getTotalDrug() + item.getShi_mmd());

            }
        } else {
           data = new ArrayList<>();
        }
        form.setApiDatas(data);
        form.setItems(datas);

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
        Map<Long, String> mapSite = new HashMap<>();
        for (SiteEntity site : siteService.findAll()) {
            if (StringUtils.isNotEmpty(site.getHubSiteCode())) {
                mapSite.put(site.getID(), site.getHubSiteCode());
            }
        }

        //Các trường sẵn có
        form.setProvincePQMcode(provincePQM.getOrDefault(currentUser.getSite().getProvinceID(), ""));
        form.setDistrictPQMcode(districtPQM.getOrDefault(currentUser.getSite().getDistrictID(), ""));
        form.setSitePQMcodes(mapSite);

        return form;
    }

    @GetMapping(value = {"/pqm-shi/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year
    ) throws Exception {
        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int m = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmShiForm form = getData(m, y);

        return exportExcel(new PqmShiExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-shi/excel-data.html"})
    public ResponseEntity<InputStreamResource> actionExportDataExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year
    ) throws Exception {
        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int m = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmShiForm form = getData(m, y);
        form.setFileName("BaoHiemYte_" + "Thang" + month + "_Nam" + year + "_" + TextUtils.removeDiacritical(getCurrentUser().getSiteProvince().getName()));

        return exportExcel(new PqmShiDataExcel(form, EXTENSION_EXCEL_X));
    }
}
