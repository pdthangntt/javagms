package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PqmReportEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.HtcPqmIndicatorExcel;
import com.gms.entity.form.opc_arv.HtcPqmReportForm;
import com.gms.entity.form.opc_arv.HtcReportTable;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.HtcVisitService;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static org.xhtmlrenderer.util.Uu.p;

/**
 * BC PQM HTC
 *
 * @author pdThang
 */
@Controller(value = "htc_pqm_indicators_report")
public class HtcPQMController extends OpcController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
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

        return option;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }

    private List<String> getKeyIndicator() {
        List<String> keys = new ArrayList<>();
        keys.add(PqmIndicatorEnum.HTS_TST_POS.getKey());
        keys.add(PqmIndicatorEnum.HTS_TST_RECENCY.getKey());
        keys.add(PqmIndicatorEnum.IMS_POS_ART.getKey());
        keys.add(PqmIndicatorEnum.IMS_TST_POS.getKey());
        keys.add(PqmIndicatorEnum.IMS_TST_RECENCY.getKey());
        keys.add(PqmIndicatorEnum.POS_TO_ART.getKey());

        return keys;
    }

    private Map<String, String> getNameIndicator() {
        Map<String, String> maps = new HashMap<>();
        maps.put(PqmIndicatorEnum.HTS_TST_POS.getKey(), PqmIndicatorEnum.HTS_TST_POS.getLabel());
        maps.put(PqmIndicatorEnum.HTS_TST_RECENCY.getKey(), PqmIndicatorEnum.HTS_TST_RECENCY.getLabel());
        maps.put(PqmIndicatorEnum.IMS_POS_ART.getKey(), PqmIndicatorEnum.IMS_POS_ART.getLabel());
        maps.put(PqmIndicatorEnum.IMS_TST_POS.getKey(), PqmIndicatorEnum.IMS_TST_POS.getLabel());
        maps.put(PqmIndicatorEnum.IMS_TST_RECENCY.getKey(), PqmIndicatorEnum.IMS_TST_RECENCY.getLabel());
        maps.put(PqmIndicatorEnum.POS_TO_ART.getKey(), PqmIndicatorEnum.POS_TO_ART.getLabel());

        return maps;
    }

    private Map<String, String> getNameIndicatorNames() {
        Map<String, String> maps = new HashMap<>();
        maps.put(PqmIndicatorEnum.HTS_TST_POS.getKey(), "Số ca dương tính");
        maps.put(PqmIndicatorEnum.HTS_TST_RECENCY.getKey(), "Số ca có kết luận nhiễm mới");
        maps.put(PqmIndicatorEnum.IMS_POS_ART.getKey(), "Số ca dương tính chuyển gửi điều trị");
        maps.put(PqmIndicatorEnum.IMS_TST_POS.getKey(), "Số ca dương tính nhận kết quả");
        maps.put(PqmIndicatorEnum.IMS_TST_RECENCY.getKey(), "Số ca có kết quả ban đầu nhiễm mới");
        maps.put(PqmIndicatorEnum.POS_TO_ART.getKey(), "Số ca chuyển gửi điều trị thành công");

        return maps;
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

    private HtcPqmReportForm getData(String month, String year, String sites) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> siteOptions = getSites();

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        Date firstDay = TextUtils.getFirstDayOfMonth(m, y);
        Date lastDay = TextUtils.getLastDayOfMonth(m, y);

        HtcPqmReportForm form = new HtcPqmReportForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("BC_CHISO_PQM_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo theo chỉ số");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setMonth(m);
        form.setYear(y);
        form.setSites(sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1))));
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());

        HashMap<String, String> siteOption = new LinkedHashMap<>();
        siteOption.put("", "Tất cả");
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            siteOption.put(siteEntity.getID().toString(), siteEntity.getName());
        }
        form.setSiteOptions(siteOption);
        List<PqmReportEntity> datas = pqmService.findAll();
        List<HtcReportTable> items = new ArrayList<>();
        HtcReportTable row;
        int currentYear = StringUtils.isEmpty(year) ? Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")) : Integer.valueOf(year);
        int currentMonth = StringUtils.isEmpty(month) ? Integer.valueOf(TextUtils.formatDate(new Date(), "MM")) : Integer.valueOf(month);
        System.out.println(currentYear + "    " + currentMonth);
        for (String key : getKeyIndicator()) {
            row = new HtcReportTable();
            row.setIndicators(getNameIndicatorNames().get(key));
            for (PqmReportEntity item : datas) {
                if (form.getSites() != null && isPAC()) {
                    if (!form.getSites().contains(item.getSiteID().toString())) {
                        continue;
                    }
                }
                if (!isPAC() && !currentUser.getSite().getID().toString().equals(item.getSiteID().toString())) {
                    continue;
                }

                if (currentYear != item.getYear() || currentMonth != item.getMonth()) {
                    continue;
                }
                if (!getSiteIds().contains(item.getSiteID())) {
                    continue;
                }
                if (item.getIndicatorCode() != null && item.getIndicatorCode().equals(key)) {
                    if (item.getAgeGroup().equals("10-14")) {
                        row.setI10x14(row.getI10x14() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("15-19")) {
                        row.setI15x19(row.getI15x19() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("20-24")) {
                        row.setI20x24(row.getI20x24() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("25-29")) {
                        row.setI25x29(row.getI25x29() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("30-34")) {
                        row.setI30x34(row.getI30x34() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("35-39")) {
                        row.setI35x39(row.getI35x39() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("40-44")) {
                        row.setI40x44(row.getI40x44() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("45-49")) {
                        row.setI45x49(row.getI45x49() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getAgeGroup().equals("50+")) {
                        row.setI50x(row.getI50x() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getSexID().equals("1")) {
                        row.setMale(row.getMale() + Integer.valueOf(item.getQuantity().toString()));
                    }
                    if (item.getSexID().equals("2")) {
                        row.setFemale(row.getFemale() + Integer.valueOf(item.getQuantity().toString()));
                    }

                    if (item.getKeyPopulationID().equals("1")) {
                        row.setNtmt(row.getNtmt() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("2")) {
                        row.setMd(row.getMd() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("5") || item.getKeyPopulationID().equals("5.1") || item.getKeyPopulationID().equals("5.1")) {
                        row.setPnmt(row.getPnmt() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("9")) {
                        row.setNhm(row.getNhm() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("10")) {
                        row.setAids(row.getAids() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("6")) {
                        row.setLao(row.getLao() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("25") || item.getKeyPopulationID().equals("11")) {
                        row.setLtqdtd(row.getLtqdtd() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("12")) {
                        row.setNvqs(row.getNvqs() + Integer.valueOf(item.getQuantity().toString()));
                    } else if (item.getKeyPopulationID().equals("3")) {
                        row.setMsm(row.getMsm() + Integer.valueOf(item.getQuantity().toString()));
                    } else {
                        row.setOther(row.getOther() + Integer.valueOf(item.getQuantity().toString()));
                    }

                }
            }
            items.add(row);
        }

        form.setItems(items);
        return form;
    }

    @GetMapping(value = {"/pqm/indicators-index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        HtcPqmReportForm form = getData(month, year, sites);
        model.addAttribute("parent_title", "Báo cáo quốc gia");
        model.addAttribute("title", "Báo cáo theo chỉ số TVXN");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        HashMap<String, SiteEntity> listSites = new HashMap<>();
        for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
            listSites.put(item.getID().toString(), item);
        }

        model.addAttribute("sites", listSites);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentSite", getCurrentUser().getSite().getName());
//        model.addAttribute("title_small", isOpcManager() ? "Khoa điều trị" : "Cơ sở điều trị");
        return "report/pqm/indicators-index";
    }

    @GetMapping(value = {"/pqm/indicators-excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        return exportExcel(new HtcPqmIndicatorExcel(getData(month, year, sites), EXTENSION_EXCEL));
    }

}
