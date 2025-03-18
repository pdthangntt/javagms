package com.gms.controller.report;

import com.gms.components.HubUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.PqmDrugNewDataExcel;
import com.gms.entity.excel.opc.PqmDrugNewExcel;
import com.gms.entity.form.opc_arv.PqmDrugNewForm;
import com.gms.entity.input.PqmDrugNewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PqmDrugNewDataService;
import com.gms.service.PqmDrugNewService;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
@Controller(value = "pqm_data_drug_new")
public class PqmDrugNewController extends OpcController {

    @Autowired
    private PqmDrugNewDataService drugNewDataService;
    @Autowired
    private PqmDrugNewService drugNewService;
    @Autowired
    private LocationsService locationsService;
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

    @GetMapping(value = {"/drug-new/index.html"})
    public String actionDrugNew(Model model,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size, PqmDrugNewSearch search
    ) throws Exception {

        int m = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(9999);
        search.setYear(y);
        search.setQuarter(m);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        Map<Long, String> siteMaps = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findAll()) {
            siteMaps.put(siteEntity.getID(), siteEntity.getName());
        }

        DataPage<PqmDrugNewEntity> dataPages = drugNewService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        if (dataPages.getData() != null && !dataPages.getData().isEmpty()) {
            for (PqmDrugNewEntity e : dataPages.getData()) {
                siteMap.put(e.getSiteID().toString(), siteMaps.getOrDefault(e.getSiteID(), null));
            }
        }

        search.setSiteID(!isPAC() ? getCurrentUser().getSite().getID() : (!StringUtils.isEmpty(sites) ? Long.valueOf(sites) : null));
        search.setYear(y);
        search.setQuarter(m);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<PqmDrugNewEntity> dataPage = drugNewService.find(search);
        Map<String, String> years = new LinkedHashMap<>();
        Map<String, String> quarters = new LinkedHashMap<>();

        for (int i = 1900; i < 2200; i++) {
            years.put(String.valueOf(i), "Năm " + i);
        }
        for (int i = 1; i < 5; i++) {
            quarters.put(String.valueOf(i), "Quý " + i);
        }

        model.addAttribute("title", "BC tình hình sử dụng và tồn kho thuốc ARV (HMED)");
        model.addAttribute("parent_title", "Điều trị ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("sites", sites);
        model.addAttribute("years", years);
        model.addAttribute("quarters", quarters);
        model.addAttribute("quarter", m + "");
        model.addAttribute("year", y + "");
        model.addAttribute("options", getOptions());
        model.addAttribute("isPAC", isPAC());

        return "report/pqm_new/drug-new";
    }

    @GetMapping(value = {"/drug-new/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "1000000") int size, PqmDrugNewSearch search
    ) throws Exception {

        int m = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        LoggedUser currentUser = getCurrentUser();

        search.setSiteID(!isPAC() ? getCurrentUser().getSite().getID() : (!StringUtils.isEmpty(sites) ? Long.valueOf(sites) : null));
        search.setYear(y);
        search.setQuarter(m);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<PqmDrugNewEntity> dataPage = drugNewService.find(search);
        Map<Long, String> siteMaps = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findAll()) {
            siteMaps.put(siteEntity.getID(), siteEntity.getName());
        }

        PqmDrugNewForm form = new PqmDrugNewForm();

        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("DanhSachBaoCaoTinhHinhSuDungThuocArvHMED" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Danh sách báo cáo tình hình sử dụng và tồn kho thuốc ARV (HMED)");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setMonth(m);
        form.setYear(y);
        form.setSiteMaps(siteMaps);

        form.setItems(dataPage.getData());

        try {
            form.setSiteSearch(siteMaps.getOrDefault(Long.valueOf(sites), null));
        } catch (Exception e) {
            form.setSiteSearch(null);
        }

        return exportExcel(new PqmDrugNewExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/drug-new-data/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportDataExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "1000000") int size, PqmDrugNewSearch search
    ) throws Exception {

        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmDrugNewForm form = getData(q, y);

        Map<Long, String> siteMaps = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findAll()) {
            siteMaps.put(siteEntity.getID(), siteEntity.getName());
        }
        form.setSiteMaps(siteMaps);

        search = new PqmDrugNewSearch();

        search.setYear(y);
        search.setQuarter(q);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        search.setPageIndex(1);
        search.setPageSize(99999);
        search.setSiteID(!isPAC() ? getCurrentUser().getSite().getID() : (!StringUtils.isEmpty(sites) ? Long.valueOf(sites) : null));

        DataPage<PqmDrugNewDataEntity> dataPage = drugNewDataService.find(search);

        form.setItemAPI(dataPage.getData());

        return exportExcel(new PqmDrugNewDataExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/drug-new-data/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            //            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        PqmDrugNewForm form = getData(q, y);

        model.addAttribute("title", "Kết quả chỉ số thuốc ARV (HMED)");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Chọn cơ sở");

        Map<Long, String> siteMaps = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findAll()) {
            siteMaps.put(null, "Tất cả");
            siteMaps.put(siteEntity.getID(), siteEntity.getName());
        }

        PqmDrugNewSearch search = new PqmDrugNewSearch();

        search.setYear(y);
        search.setQuarter(q);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        search.setPageIndex(1);
        search.setPageSize(99999);
        search.setSiteID(isPAC() ? null : getCurrentUser().getSite().getID());
//        search.setSiteID(!isPAC() ? getCurrentUser().getSite().getID() : (!StringUtils.isEmpty(sites) ? Long.valueOf(sites) : null));

        DataPage<PqmDrugNewDataEntity> dataPage = drugNewDataService.find(search);

        form.setKeyMonth(new HashMap<String, String>());
        for (int i = 1; i <= 4; i++) {
            PqmDrugNewForm formQuater = getData(i, y);
            if (formQuater.getItems() != null && !formQuater.getItems().isEmpty()) {
                form.getKeyMonth().put(String.valueOf(i), "2");
            }

        }

        Map<String, String> monthIsReport = new HashMap<String, String>();
        for (int i = 1; i <= 4; i++) {
            List<PqmDrugNewDataEntity> listData = drugNewDataService.findByIndex(
                    isPAC() ? null : getCurrentUser().getSite().getID().toString(),
                    y,
                    i,
                    getCurrentUser().getSite().getProvinceID());
            if (listData != null && !listData.isEmpty()) {
                monthIsReport.put(String.valueOf(i), "2");
            }
        }

        model.addAttribute("isPAC", isPAC());
        model.addAttribute("quarterKey", form.getKeyMonth());
        model.addAttribute("quarter", quarter);
        model.addAttribute("monthKey", monthIsReport);
        model.addAttribute("items", dataPage.getData());
        model.addAttribute("siteMaps", siteMaps);
        model.addAttribute("siteID", search.getSiteID());

        return "report/pqm_new/drug-data";
    }

    @GetMapping(value = {"/drug-new-data/synthetic.html"})
    public String actionSynthetic(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        int currentQuater = TextUtils.getQuarter(new Date()) + 1;

        int currentTime = Integer.valueOf(currentYear + "" + currentQuater);
        int reportTime = Integer.valueOf(y + "" + q);
        if (reportTime == currentTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Chưa đủ dữ liệu để tổng hợp báo cáo. Vui lòng kiểm tra lại!"));
            return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&month=" + q);
        }
        if (reportTime > currentTime) {
            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của quý sau quý hiện tại. Vui lòng kiểm tra lại!"));
            return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&month=" + q);
        }

//        int currentTime = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy") + "" + (TextUtils.getQuarter(new Date()) + 1 + 1));
//        int paramTime = Integer.valueOf(year + quarter);
//
//        if (currentTime < paramTime) {
//            redirectAttributes.addFlashAttribute("error", String.format("Không thể tổng hợp dữ liệu của quý sau quý hiện tại. Vui lòng kiểm tra lại!"));
//            return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&month=" + q);
//        }
        try {
            //Lay quy truoc
            Date firstDayOfQuarter = TextUtils.getFirstDayOfQuarter(q - 1, y);
            String midMonth = "15/" + TextUtils.formatDate(firstDayOfQuarter, "MM/yyyy");

            Date referenceDate = TextUtils.convertDate(midMonth, FORMATDATE);
            Calendar c = Calendar.getInstance();
            c.setTime(referenceDate);
            c.add(Calendar.MONTH, -3);
            Date time = c.getTime();
            int beforeQuarter = TextUtils.getQuarter(time) + 1;
            int beforeYear = Integer.valueOf(TextUtils.formatDate(time, "yyyy"));

            //xoa du lieu cu
            drugNewDataService.deleteByProvinceIDAndMonthAndYear(getCurrentUser().getSite().getProvinceID(), q, y);

            PqmDrugNewSearch search = new PqmDrugNewSearch();

            search.setYear(y);
            search.setQuarter(q);
            search.setProvinceID(getCurrentUser().getSite().getProvinceID());
            search.setPageIndex(1);
            search.setPageSize(99999);

            DataPage<PqmDrugNewEntity> dataPage = drugNewService.find(search);
            if (!dataPage.getData().isEmpty()) {
                //Cong thuoc theo co so, ten, nguon
                Map<String, PqmDrugNewEntity> maps = new HashMap<>();
                for (PqmDrugNewEntity pqmDrugNewEntity : dataPage.getData()) {
                    String key = pqmDrugNewEntity.getDrugName() + "c" + pqmDrugNewEntity.getSource() + "X" + pqmDrugNewEntity.getSiteID();
                    if (maps.getOrDefault(key, null) == null) {
                        maps.put(key, pqmDrugNewEntity);
                    } else {
                        maps.get(key).setTdk(maps.get(key).getTdk() + pqmDrugNewEntity.getTdk());
                        maps.get(key).setNdk(maps.get(key).getNdk() + pqmDrugNewEntity.getNdk());
                        maps.get(key).setNk(maps.get(key).getNk() + pqmDrugNewEntity.getNk());
                        maps.get(key).setXcbntk(maps.get(key).getXcbntk() + pqmDrugNewEntity.getXcbntk());
                        maps.get(key).setXdctk(maps.get(key).getXdctk() + pqmDrugNewEntity.getXdctk());
                        maps.get(key).setHh(maps.get(key).getHh() + pqmDrugNewEntity.getHh());
                        maps.get(key).setTck(maps.get(key).getTck() + pqmDrugNewEntity.getTck());
                    }

                }
                for (Map.Entry<String, PqmDrugNewEntity> entry : maps.entrySet()) {
                    PqmDrugNewEntity pqmDrugNewEntity = entry.getValue();

                    PqmDrugNewDataEntity item1 = new PqmDrugNewDataEntity();
                    item1.setProvinceID(pqmDrugNewEntity.getProvinceID());
                    item1.setSiteID(pqmDrugNewEntity.getSiteID());
                    item1.setYear(pqmDrugNewEntity.getYear());
                    item1.setQuarter(pqmDrugNewEntity.getQuarter());

                    item1.setDrugName(pqmDrugNewEntity.getDrugName().trim());
                    item1.setSource(pqmDrugNewEntity.getSource());

                    item1.setTdk(pqmDrugNewEntity.getTdk());
                    item1.setNdk(pqmDrugNewEntity.getNdk());
                    item1.setNk(pqmDrugNewEntity.getNk());
                    item1.setXcbntk(pqmDrugNewEntity.getXcbntk());
                    item1.setXdctk(pqmDrugNewEntity.getXdctk());
                    item1.setHh(pqmDrugNewEntity.getHh());
                    item1.setTck(pqmDrugNewEntity.getTck());
                    //Tinh ty le Tỉ lệ cảnh báo số lượng thuốc đủ cấp phát trong quý tiếp theo => Chỉ số PQM13
                    //Tồn cuối kỳ dự kiến (quý báo cáo):   Tính bằng Cột  "Tồn cuối kỳ"  của Quý báo cáo								
                    //Nhập trong kỳ dự kiến (quý kế tiếp):  Tính bằng Tổng "Nhập định kỳ" VÀ  "Nhập khác"   của Quý báo cáo 								
                    //Xuất dự kiến (quý kế tiếp):  Tính bằng  Tổng  "Xuất cho bệnh nhân trong kỳ"   của Quý báo cáo								
                    //Tính bằng:  ( [ Tồn cuối kỳ dự kiến (quý báo cáo)  +  Nhập trong kỳ dự kiến (quý kế tiếp) ] )   /  [Xuất dự kiến (quý kế tiếp)]   * 100   =>  làm tròn 2 chữ số sau phần thập phân: VD: 34.12%								
                    try {
                        Long tckdy = item1.getTck();
                        Long ntkdk = item1.getNdk() + item1.getNk();
                        Long xdk = item1.getXcbntk();
                        if (xdk == Long.valueOf(0)) {
                            item1.setTlcb(9999);
                        } else {
                            item1.setTlcb(((Double.valueOf(tckdy) + Double.valueOf(ntkdk)) / Double.valueOf(xdk)) * 100);
                            item1.setTlcb(Math.round(item1.getTlcb() * 100.0) / 100.0);
                        }

                    } catch (Exception e) {
                        item1.setTlcb(9999);
                    }
//                    Tỉ lệ đánh giá hiệu quả dự trù và quản lý kho => Chỉ số PQM14	
//                    Xuất kho thực tế (quý trước): Tính bằng  Tổng cột  "Xuất cho bệnh nhân trong kỳ"   VÀ  cột  "Xuất điều chuyển trong kỳ"    của  Quý trước quý báo cáo								
//                    Tồn cuối kỳ (quý báo cáo): Tính bằng  cột   "Tồn cuối kỳ"    của Quý báo cáo								
//                    Nhập trong kỳ (quý trước):   Tính bằng   Tổng cột   "Nhập định kỳ"   VÀ   cột  "Nhập khác"   của Quý trước  quý báo cáo								
//                    Tính bằng:   [ Xuất kho thực tế (quý trước) ]  /   ( [ Tồn cuối kỳ (quý báo cáo) ]  +  [ Nhập trong kỳ (quý trước) ] )   * 100  => làm tròn 2 chữ số sau phần thập phân								
                    try {
                        //quy truoc
                        PqmDrugNewEntity itemBeforeQuarter = drugNewService.findByUniqueConstraints(
                                item1.getSiteID().toString(),
                                beforeYear,
                                beforeQuarter,
                                item1.getProvinceID(),
                                item1.getDrugName(),
                                item1.getSource());

                        if (itemBeforeQuarter != null) {
                            Long xktt = itemBeforeQuarter.getXcbntk() + itemBeforeQuarter.getXdctk();
                            Long tck = item1.getTck();
                            Long ntk = itemBeforeQuarter.getNdk() + itemBeforeQuarter.getNk();
                            Long mauso = tck + ntk;

                            if (xktt == Long.valueOf(0)) {
                                item1.setTldg(0);
                            } else {
                                if (mauso == Long.valueOf(0)) {
                                    item1.setTldg(1);
                                } else {
                                    item1.setTldg((Double.valueOf(xktt) / Double.valueOf(mauso)) * 100);
                                    item1.setTldg(Math.round(item1.getTldg() * 100.0) / 100.0);
                                }
                            }
                        } else {
                            item1.setTldg(0);
                        }

                    } catch (Exception e) {
                        item1.setTldg(1);
                    }

                    drugNewDataService.save(item1);

                }

            }
            
            PqmDrugNewForm form = getData(search.getQuarter(), search.getYear());
            
            

//            if (form.getItemAPI() != null && !form.getItemAPI().isEmpty()) {
//                Map<String, Object> result = hubUtils.sendDrugPlanToPqm(form);
//            }
//                Map<String, Object> result = hubUtils.sendDrugPlanToPqm(form);
//                Gson g = new Gson();
//                MainForm p = g.fromJson(g.toJson(result), MainForm.class);
//                boolean succeed = p.getError() == null && p.getData() != null && p.getData().getError_rows() != null && p.getData().getError_rows().isEmpty();
//                if (succeed) {
//                    redirectAttributes.addFlashAttribute("success", String.format("Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công"));
//                    return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&quarter=" + q);
//                } else {
//                    List<String> errors = new ArrayList<>();
//                    if (p.getError() != null && StringUtils.isNotEmpty(p.getError().getErrorMessage())) {
//                        errors.add(p.getError().getErrorMessage());
//                    } else if (p.getData() != null && p.getData().getError_rows() != null && !p.getData().getError_rows().isEmpty()) {
//                        for (error_rows error_row : p.getData().getError_rows()) {
//                            errors.add(error_row.getError());
//                        }
//                    }
//                    Set<String> errorx = new HashSet<>();
//                    for (String error : errors) {
//                        if (error.equals("Year is not defined")) {
//                            errorx.add("Dữ liệu không có năm");
//                        } else if (error.equals("Month is not defined")) {
//                            errorx.add("Dữ liệu không có tháng");
//                        } else if (error.contains("Site")) {
//                            errorx.add("Không tìm thấy cơ sở #" + error.replaceAll("\\D+", "") + " trên Provincial Dashboards");
//                        } else {
//                            errorx.add(error);
//                        }
//                    }
//
//                    redirectAttributes.addFlashAttribute("false_key", errorx);
//                    redirectAttributes.addFlashAttribute("error", String.format("Tổng hợp và gửi số liệu báo cáo lên PQM không thành công"));
//                    return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&quarter=" + q);
//                }
//
//            }
//            if (StringUtils.isNotEmpty(getCurrentUser().getUser().getEmail())) {
//                sendEmailNotify(getCurrentUser().getUser().getEmail(), "Kết quả chỉ số thuốc ARV (HMED)", "Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công quý " + q + " năm " + y);
//            }

            redirectAttributes.addFlashAttribute("success", "Đã tổng hợp và gửi số liệu báo cáo lên PQM thành công.");
            return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&month=" + q);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi khi tổng hợp dữ liệu.");
            return redirect("/report/drug-new-data/index.html" + "?year=" + y + "&month=" + q);
        }

    }

    private PqmDrugNewForm getData(int quarter, int year) {
        LoggedUser currentUser = getCurrentUser();

        PqmDrugNewForm form = new PqmDrugNewForm();

        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("ChiSoThuocArvHMED" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Chỉ số thuốc ARV");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setMonth(quarter);
        form.setYear(year);

        List<PqmDrugNewDataEntity> data = drugNewDataService.findByIndex(
                    isPAC() ? null : getCurrentUser().getSite().getID().toString(),
                    year,
                    quarter,
                    getCurrentUser().getSite().getProvinceID());
       
        form.setItemAPI(data);
        
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

//        form.setItemAPI(data);
        return form;
    }
}
