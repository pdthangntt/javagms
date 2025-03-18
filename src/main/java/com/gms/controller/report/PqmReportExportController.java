package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.pac.PqmExportExcel;
import com.gms.entity.form.opc_arv.PqmExportForm;
import com.gms.service.PqmDataService;
import com.gms.service.PqmDrugEstimateDataService;
import com.gms.service.PqmDrugNewDataService;
import com.gms.service.PqmDrugPlanDataService;
import com.gms.service.PqmDrugeLMISEDataService;
import com.gms.service.PqmShiDataService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
@Controller(value = "pqm_reportddd_controllers")
public class PqmReportExportController extends BaseController {

    @Autowired
    private PqmDataService pqmService;
    @Autowired
    private PqmDrugPlanDataService dataService;
    @Autowired
    private PqmDrugEstimateDataService pqmDrugDataService;
    @Autowired
    private PqmDrugNewDataService drugNewDataService;
    @Autowired
    private PqmDrugeLMISEDataService drugeLMISEDataService;
    @Autowired
    private PqmShiDataService pqmShiDataService;

    protected static final String FORMATDATE_SQL = "yyyy-MM-dd";

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

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, List<SiteEntity>> entry : getSites().entrySet()) {
            for (SiteEntity siteEntity : entry.getValue()) {
                ids.add(siteEntity.getID());
            }
        }
        return ids;
    }

    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")) + 10;
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        options.put("months", new LinkedHashMap<String, String>());
        for (int i = 12; i > 0; i--) {
            options.get("months").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        options.put("quarters", new LinkedHashMap<String, String>());
        for (int i = 4; i > 0; i--) {
            options.get("quarters").put(String.valueOf(i), String.format("Quý %s", i));
        }

        return options;
    }

    @GetMapping(value = {"/pqm/export.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("title", "Xuất số liệu báo cáo");
        model.addAttribute("options", getOptions());

        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        int month = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int quarter = TextUtils.getQuarter(new Date()) + 1;

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("quarter", quarter);

        return "/report/pqm/export";
    }

    @GetMapping(value = {"/pqm/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "t", required = false, defaultValue = "") String t,
            @RequestParam(name = "a", required = false, defaultValue = "") String a,
            @RequestParam(name = "b", required = false, defaultValue = "") String b,
            @RequestParam(name = "c", required = false, defaultValue = "") String c,
            @RequestParam(name = "d", required = false, defaultValue = "") String d
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();

        PqmExportForm form = getData(t, a, b, c, d);

        return exportExcel(new PqmExportExcel(form, EXTENSION_EXCEL_X));
    }

    private PqmExportForm getData(String t, String a, String b, String c, String d) {
        String from = "";
        String to = "";
        int a1 = Integer.valueOf(a);
        int b1 = Integer.valueOf(b);
        int c1 = Integer.valueOf(c);
        int d1 = Integer.valueOf(d);

        List<String> list = new LinkedList<>();
        List<Integer> years = new LinkedList<>();
        Set<Long> siteIDHub = new HashSet<>();
        siteIDHub.addAll(getSiteIds());

        for (int i = b1; i <= d1; i++) {
            years.add(i);
        }
        int fromYear = years.get(0);
        int toYear = years.get(years.size() - 1);
        if ("1".endsWith(t) || "2".endsWith(t) || "6".endsWith(t)) {
            for (int i = fromYear; i <= toYear; i++) {
                if (fromYear == toYear) {
                    for (int j = a1; j <= c1; j++) {
                        list.add(j + "-" + i);
                    }
                } else {
                    if (i == fromYear) {
                        for (int j = a1; j < 13; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                    //Giua
                    if (i > fromYear && i < toYear) {
                        for (int j = 1; j < 13; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                    if (i == toYear) {
                        for (int j = 1; j <= c1; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                }

            }
        } else {
            for (int i = fromYear; i <= toYear; i++) {
                if (fromYear == toYear) {
                    for (int j = a1; j <= c1; j++) {
                        list.add(j + "-" + i);
                    }
                } else {
                    if (i == fromYear) {
                        for (int j = a1; j < 5; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                    //Giua
                    if (i > fromYear && i < toYear) {
                        for (int j = 1; j < 5; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                    if (i == toYear) {
                        for (int j = 1; j <= c1; j++) {
                            list.add(j + "-" + i);
                        }
                    }
                }

            }
        }

        for (String string : list) {
            System.out.println("mon " + string.substring(0, string.length() - 5) + " year: " + string.substring(string.length() - 4));
        }

        System.out.println("from " + from);
        System.out.println("to " + to);

        PqmExportForm form = new PqmExportForm();

        LinkedList<String> headers;
        LinkedList<LinkedList<String>> datas;
        LinkedList<String> item;
        switch (t) {
            case "1":
                form.setFileName("Chi so XN-DT-PrEP");
                form.setSheetName("ChiSoXN-DT-PREP");
                List<PqmDataEntity> dataALL = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));
                    List<PqmDataEntity> listDataHub = pqmService.findBySiteIDsOrderBySiteID(m, y, siteIDHub);
                    if (listDataHub != null) {
                        for (PqmDataEntity e : listDataHub) {
                            if (e.getQuantity().equals(Long.valueOf("0")) || (e.getStatus() != 2 && e.getStatus() != 3)) {
                                continue;
                            }
                            if (isPAC() && e.getSiteID().equals(getCurrentUser().getSite().getID())) {
                                continue;
                            }
                            dataALL.add(e);
                        }
                    }
                }

                headers = new LinkedList<>();
                headers.add("id");
                headers.add("age_group");
                headers.add("created_at");
                headers.add("district_id");
                headers.add("indicator_code");
                headers.add("month");
                headers.add("multi_month");
                headers.add("object_group_id");
                headers.add("province_id");
                headers.add("quantity");
                headers.add("send_date");
                headers.add("sex_id");
                headers.add("site_id");
                headers.add("status");
                headers.add("updated_at");
                headers.add("year");
                headers.add("key_early");
                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmDataEntity p : dataALL) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(p.getAgeGroup());
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getDistrictID());
                    item.add(p.getIndicatorCode());
                    item.add(p.getMonth() + "");
                    item.add(p.getMultiMonth() + "");
                    item.add(StringUtils.isBlank(p.getObjectGroupID()) ? "null" : p.getObjectGroupID());
                    item.add(p.getProvinceID());
                    item.add(p.getQuantity() + "");
                    item.add(p.getSendDate() + "");
                    item.add(p.getSexID());
                    item.add(p.getSiteID() + "");
                    item.add(p.getStatus() + "");
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getYear() + "");
                    item.add(p.getKeyEarly());
                    datas.add(item);
                }

                form.setDatas(datas);

                break;
            case "2":
                form.setFileName("Chi so thuoc arv du tru an toan");
                form.setSheetName("ChiSoThuocArvDuTruAnToanHMED");
                List<PqmDrugPlanDataEntity> listsALL = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));
                    List<PqmDrugPlanDataEntity> lists = dataService.findByEx(m, y, getCurrentUser().getSite().getProvinceID());
                    if (lists != null && !lists.isEmpty()) {
                        for (PqmDrugPlanDataEntity list1 : lists) {
                            listsALL.add(list1);
                        }
                    }
                }
                headers = new LinkedList<>();
                headers.add("id");
                headers.add("created_at");
                headers.add("drug_name");
                headers.add("month");
                headers.add("province_id");
                headers.add("site_code");
                headers.add("site_name");
                headers.add("total_drug_backlog");
                headers.add("total_drug_plan_2month");
                headers.add("unit");
                headers.add("updated_at");
                headers.add("year");
                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmDrugPlanDataEntity p : listsALL) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getDrugName());
                    item.add(p.getMonth() + "");
                    item.add(p.getProvinceID());
                    item.add(p.getSiteCode());
                    item.add(p.getSiteName());
                    item.add(p.getTotalDrugBacklog() + "");
                    item.add(p.getTotalDrugPlan2Month() + "");
                    item.add(p.getUnit());
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getYear() + "");

                    datas.add(item);
                }

                form.setDatas(datas);
                break;
            case "3":
                form.setSheetName("ChiSoThuocArvDaCapHMED");
                form.setFileName("Chi so thuoc ARV da cap");
                List<PqmDrugEstimateDataEntity> listsALL3 = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));

                    List<PqmDrugEstimateDataEntity> data = pqmDrugDataService.findData("drugs_plan", m, y, getCurrentUser().getSite().getProvinceID());
                    if (data != null && !data.isEmpty()) {
                        for (PqmDrugEstimateDataEntity list1 : data) {
                            listsALL3.add(list1);
                        }
                    }
                }
                headers = new LinkedList<>();
                headers.add("id");
                headers.add("created_at");
                headers.add("data_code");
                headers.add("dispensed_quantity");
                headers.add("drug_name");
                headers.add("drug_name_lowercase");
                headers.add("drug_uom");
                headers.add("drug_uom_lowercase");
                headers.add("facilyty_code");
                headers.add("facilyty_name");
                headers.add("planned_quantity");
                headers.add("province_code");
                headers.add("quarter");
                headers.add("updated_at");
                headers.add("version");
                headers.add("year");
                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmDrugEstimateDataEntity p : listsALL3) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getDataCode());
                    item.add(p.getDispensedQuantity() + "");
                    item.add(p.getDrugName());
                    item.add(p.getDrugNameLowercase());
                    item.add(p.getDrugUom());
                    item.add(p.getDrugUomLowercase());
                    item.add(p.getFacilityCode());
                    item.add(p.getFacilityName());
                    item.add(p.getPlannedQuantity() + "");
                    item.add(p.getProvinceCode());
                    item.add(p.getQuarter() + "");
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getVersion());
                    item.add(p.getYear() + "");

                    datas.add(item);
                }

                form.setDatas(datas);
                break;
            case "4":
                form.setSheetName("ChiSoThuocArvHMED");
                form.setFileName("Chi so thuoc ARV HMED");
                List<PqmDrugNewDataEntity> listsALL4 = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));

                    List<PqmDrugNewDataEntity> data = drugNewDataService.findByExport(y, m, getCurrentUser().getSite().getProvinceID());
                    if (data != null && !data.isEmpty()) {
                        for (PqmDrugNewDataEntity list1 : data) {
                            listsALL4.add(list1);
                        }
                    }
                }
                headers = new LinkedList<>();
                headers.add("id");
                headers.add("created_at");
                headers.add("drug_name");
                headers.add("hh");
                headers.add("ndk");
                headers.add("nk");
                headers.add("province_id");
                headers.add("quarter");
                headers.add("site_id");
                headers.add("source");
                headers.add("tck");
                headers.add("tdk");
                headers.add("tlcb");
                headers.add("tldg");
                headers.add("updated_at");
                headers.add("xcbntk");
                headers.add("xdctk");
                headers.add("year");
                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmDrugNewDataEntity p : listsALL4) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getDrugName());
                    item.add(p.getHh() + "");
                    item.add(p.getNdk() + "");
                    item.add(p.getNk() + "");
                    item.add(p.getProvinceID());
                    item.add(p.getQuarter() + "");
                    item.add(p.getSiteID() + "");
                    item.add(p.getSource());
                    item.add(p.getTck() + "");
                    item.add(p.getTdk() + "");
                    item.add(p.getTlcb() + "");
                    item.add(p.getTldg() + "");
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getXcbntk() + "");
                    item.add(p.getXdctk() + "");
                    item.add(p.getYear() + "");
                    datas.add(item);
                }

                form.setDatas(datas);
                break;
            case "5":
                form.setSheetName("ChiSoThuocArveLMIS");
                form.setFileName("Chi so thuoc ARV eLMIS");
                List<PqmDrugeLMISEDataEntity> listsALL5 = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));

                    List<PqmDrugeLMISEDataEntity> data = drugeLMISEDataService.findData(m, y, getCurrentUser().getSite().getProvinceID());
                    if (data != null && !data.isEmpty()) {
                        for (PqmDrugeLMISEDataEntity list1 : data) {
                            listsALL5.add(list1);
                        }
                    }
                }
                headers = new LinkedList<>();
                headers.add("id");
                headers.add("created_at");
                headers.add("drug_name");
                headers.add("province_id");
                headers.add("quarter");
                headers.add("site_id");
                headers.add("su_dung");
                headers.add("thang");
                headers.add("ton");
                headers.add("trong_ky");
                headers.add("updated_at");
                headers.add("year");
                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmDrugeLMISEDataEntity p : listsALL5) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getDrugName());
                    item.add(p.getProvinceID());
                    item.add(p.getQuarter() + "");
                    item.add(p.getSiteID() + "");
                    item.add(p.getSuDung() + "");
                    item.add(p.getThang() + "");
                    item.add(p.getTon() + "");
                    item.add(p.getTrongKy() + "");
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getYear() + "");
                    datas.add(item);
                }

                form.setDatas(datas);
                break;
            case "6":
                form.setSheetName("ChiSoBHYTeLMIS");
                form.setFileName("Chi so BHYT eLMIS");
                List<PqmShiDataEntity> listsALL6 = new LinkedList<>();
                for (String string : list) {
                    int m = Integer.valueOf(string.substring(0, string.length() - 5));
                    int y = Integer.valueOf(string.substring(string.length() - 4));

                    List<PqmShiDataEntity> data = pqmShiDataService.findData(m, y, getCurrentUser().getSite().getProvinceID());
                    if (data != null && !data.isEmpty()) {
                        for (PqmShiDataEntity list1 : data) {
                            listsALL6.add(list1);
                        }
                    }
                }
                headers = new LinkedList<>();
                headers.add("id");
                headers.add("age_group");
                headers.add("created_at");
                headers.add("data_code");
                headers.add("facility_code");
                headers.add("facility_name");
                headers.add("month");
                headers.add("province_id");
                headers.add("province_code");
                headers.add("sex");
                headers.add("shi_art");
                headers.add("shi_mmd");
                headers.add("site_id");
                headers.add("updated_at");
                headers.add("version");
                headers.add("year");

                form.setHead(headers);

                datas = new LinkedList<>();
                for (PqmShiDataEntity p : listsALL6) {
                    item = new LinkedList<>();
                    item.add(p.getID() + "");
                    item.add(p.getAge_group());
                    item.add(TextUtils.formatDate(p.getCreateAT(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getData_code());
                    item.add(p.getFacility_code());
                    item.add(p.getFacility_name());
                    item.add(p.getMonth() + "");
                    item.add(p.getProvinceID());
                    item.add(p.getProvince_code());
                    item.add(p.getSex());
                    item.add(p.getShi_art() + "");
                    item.add(p.getShi_mmd() + "");
                    item.add(p.getSiteID() + "");
                    item.add(TextUtils.formatDate(p.getUpdateAt(), "dd/MM/yyyy HH:mm"));
                    item.add(p.getVersion());
                    item.add(p.getYear() + "");

                    datas.add(item);
                }

                form.setDatas(datas);
                break;
            default:
                throw new AssertionError();
        }

        return form;
    }

}
