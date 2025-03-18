package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.HtcConfirmPqmIndicatorExcel;
import com.gms.entity.form.htc_confirm.PQMForm;
import com.gms.entity.form.opc_arv.PqmHtcConfirmReportForm;
import com.gms.entity.form.opc_arv.PqmHtcConfirmReportTable;
import com.gms.repository.impl.HtcConfirmRepositoryImpl;
import com.gms.service.HtcVisitService;
import com.gms.service.PqmService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BC htc confirm PQM
 *
 * @author pdThang
 */
@Controller(value = "htc_confirm_pqm_htc-confirm_report")
public class HtcConfirmPQMController extends OpcController {

    @Autowired
    private PqmService pqmService;
    @Autowired
    private HtcConfirmRepositoryImpl confirmRepositoryImpl;
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

    private LinkedHashMap<String, String> getNameIndicator() {
        LinkedHashMap<String, String> maps = new LinkedHashMap<>();
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
        maps.put("1", "Số ca dương tính");
        maps.put("2", "Số ca có kết quả ban đầu nhiễm mới");
        maps.put("3", "Số ca có tải lượng virus > 1000 bản sao/ml");
        maps.put("4", "Số ca có kết luận nhiễm mới");

        return maps;
    }

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int to = Calendar.getInstance().get(Calendar.YEAR);

        HtcVisitEntity entity = htcVisitService.getFirst(siteIds);
        if (entity != null) {
            c.setTime(entity.getAdvisoryeTime());
        }
        options.put("tos", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= to; i++) {
            options.get("tos").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    private PqmHtcConfirmReportForm getData(String from, String to, String sites) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> siteOptions = getSites();
        Date date = new Date();

        from = from == null || from.equals("") || !isThisDateValid(from) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE) : from;
        to = to == null || to.equals("") || !isThisDateValid(to) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE) : to;

        PqmHtcConfirmReportForm form = new PqmHtcConfirmReportForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("BC_NHIEMMOI_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo nhiễm mới");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(from);
        form.setEndDate(to);
        form.setSites(sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1))));
        List<Long> sourceSites = null;
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            sourceSites = new ArrayList<>();
            for (String site : form.getSites()) {
                sourceSites.add(Long.valueOf(site));
            }
        }

        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());

        HashMap<String, String> siteOption = new LinkedHashMap<>();
        siteOption.put("", "Tất cả");
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            siteOption.put(siteEntity.getID().toString(), siteEntity.getName());
        }
        form.setSiteOptions(siteOption);

        List<PQMForm> datas = confirmRepositoryImpl.getTablePQM(sourceSites, currentUser.getSite().getID().toString(), TextUtils.convertDate(from, FORMATDATE), TextUtils.convertDate(to, FORMATDATE));
        List<PqmHtcConfirmReportTable> items = new LinkedList<>();
        PqmHtcConfirmReportTable row;
        int i = 1;
        for (Map.Entry<String, String> entry : getNameIndicatorNames().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            row = new PqmHtcConfirmReportTable();
            row.setStt(i++);
            row.setKey(key);
            row.setIndicators(value);

            for (PQMForm data : datas) {
                if (data.getIndicator().equals(key)) {
                    if (data.getAgeGroup().equals("<15")) {
                        row.setUnder15(row.getUnder15() + data.getQuantity());
                    }
                    if (data.getAgeGroup().equals("15-25")) {
                        row.setM15to25(row.getM15to25() + data.getQuantity());
                    }
                    if (data.getAgeGroup().equals("25-49")) {
                        row.setM25to49(row.getM25to49() + data.getQuantity());
                    }
                    if (data.getAgeGroup().equals(">49")) {
                        row.setOver49(row.getOver49() + data.getQuantity());
                    }
                    if (data.getGender().equals("1")) {
                        row.setMale(row.getMale() + data.getQuantity());
                    }
                    if (data.getGender().equals("2")) {
                        row.setFemale(row.getFemale() + data.getQuantity());
                    }
                    //Đối Tượng
                    if (data.getObjectGroup().equals("1")) {
                        row.setNtmt(row.getNtmt() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("2")) {
                        row.setMd(row.getMd() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("5") || data.getObjectGroup().equals("5.1") || data.getObjectGroup().equals("5.2")) {
                        row.setPnmt(row.getPnmt() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("9")) {
                        row.setNhm(row.getNhm() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("10")) {
                        row.setAids(row.getAids() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("6")) {
                        row.setLao(row.getLao() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("25") || data.getObjectGroup().equals("11")) {
                        row.setLtqdtd(row.getLtqdtd() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("12")) {
                        row.setNvqs(row.getNvqs() + data.getQuantity());
                    } else if (data.getObjectGroup().equals("3")) {
                        row.setMsm(row.getMsm() + data.getQuantity());
                    } else {
                        row.setOther(row.getOther() + data.getQuantity());
                    }

                }

            }
            items.add(row);
        }

        form.setItems(items);
        return form;
    }

    @GetMapping(value = {"/pqm/htc-confirm-index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        PqmHtcConfirmReportForm form = getData(from, to, sites);
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("title", "Báo cáo nhiễm mới");
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
        return "report/pqm/htc-confirm-index";
    }

    @GetMapping(value = {"/pqm/htc-confirm-excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        return exportExcel(new HtcConfirmPqmIndicatorExcel(getData(from, to, sites), EXTENSION_EXCEL));
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
