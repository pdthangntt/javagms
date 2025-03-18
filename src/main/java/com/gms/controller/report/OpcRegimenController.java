package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.RegimenExcel;
import com.gms.entity.form.opc_arv.RegimenForm;
import com.gms.entity.form.opc_arv.RegimenTable;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.OpcArvService;
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
 * BC phác đồ điều trị
 *
 * @author vvthành
 */
@Controller(value = "opc_regimen_report")
public class OpcRegimenController extends OpcController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    @Autowired
    private OpcArvService arvService;

    private HashMap<String, SiteEntity> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        option.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite());
        List<Long> ids = siteService.getProgenyID(currentUser.getSite().getID());
        Set<String> services = new HashSet<>();
        services.add(ServiceEnum.OPC.getKey());
        for (SiteEntity site : siteService.findByServiceAndSiteID(services, new HashSet<>(ids))) {
            option.put(String.valueOf(site.getID()), site);
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

        OpcArvEntity arv = arvService.getFirst(siteIds);
        if (arv != null) {
            c.setTime(arv.getRegistrationTime());
        }
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        return options;
    }

    private RegimenForm getData(String month, String year, String sites) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> siteOptions = getSites();

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        Date firstDay = TextUtils.getFirstDayOfMonth(m, y);
        Date lastDay = TextUtils.getLastDayOfMonth(m, y);

        RegimenForm form = new RegimenForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("BC_PHACDODIEUTRI_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo bệnh nhân điều trị ARV");
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

        List<Long> mSites = new ArrayList<>();
        List<SiteEntity> siteOpcs = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        if(isOpcManager()){
            try {
                if(StringUtils.isNotEmpty(sites) ){
                    mSites.add(Long.parseLong(sites));
                    form.setSite(siteService.findOne(Long.parseLong(sites)).getName());
                } else {
                    for(SiteEntity site : siteOpcs){
                        mSites.add(site.getID());
                    }
                }
            } catch (NumberFormatException ex){
                for(SiteEntity site : siteOpcs){
                    mSites.add(site.getID());
                }
            }
            
        } else {
            mSites.add(currentUser.getSite().getID());
        }

        //Label site
        form.setSiteLabel("Tất cả");
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            String str = "";
            for (Long siteID : mSites) {
                SiteEntity site = siteOptions.getOrDefault(String.valueOf(siteID), null);
                if (site == null) {
                    continue;
                }
                str = str.concat(site.getShortName()).concat(";");
            }
            form.setSiteLabel(str);
        }

        HashMap<String, RegimenTable> results1 = arvRepositoryImpl.getRegimenTable(mSites, firstDay, lastDay, "1");
        HashMap<String, RegimenTable> results2 = arvRepositoryImpl.getRegimenTable(mSites, firstDay, lastDay, "2");

        //Default
        form.setChildren(new LinkedHashMap<String, HashMap<String, RegimenTable>>());
        form.setAdults(new LinkedHashMap<String, HashMap<String, RegimenTable>>());
        HashMap<String, RegimenTable> map;
        RegimenTable item1;
        RegimenTable item2;
        RegimenTable row;
        RegimenTable row1;
        for (ParameterEntity parameter : parameterService.getTreatmentRegimen(true, true)) {
            if (parameter.getAttribute01().equals("1")) {
                map = new LinkedHashMap<>();
                item1 = new RegimenTable();
                item1.setRegimentName(parameter.getValue());
                item1.setRegimentID(parameter.getCode());
                item1.setStt(String.valueOf(form.getChildren().size() + 1));
                row = results1.getOrDefault(String.format("%s_%s", item1.getRegimentID(), "te"), null);
                if (row != null) {
                    item1.setBatdaunhan(row.getBatdaunhan());
                    item1.setDangnhan(row.getDangnhan());
                    item1.setBonhan_botri(row.getBonhan_botri());
                    item1.setBonhan_chuyendi(row.getBonhan_chuyendi());
                    item1.setBonhan_chuyenphacdo(row.getBonhan_chuyenphacdo());
                    item1.setBonhan_matdau(row.getBonhan_matdau());
                    item1.setBonhan_tuvong(row.getBonhan_tuvong());
                    item1.setNhanThuoc(row.getNhanThuoc());
                }
                map.put("1", item1);
                item2 = new RegimenTable();
                item2.setRegimentName(parameter.getValue());
                item2.setRegimentID(parameter.getCode());
                item2.setStt(String.valueOf(form.getChildren().size() + 1));
                row1 = results2.getOrDefault(String.format("%s_%s", item2.getRegimentID(), "te"), null);
                if (row1 != null) {
                    item2.setBatdaunhan(row1.getBatdaunhan());
                    item2.setDangnhan(row1.getDangnhan());
                    item2.setBonhan_botri(row1.getBonhan_botri());
                    item2.setBonhan_chuyendi(row1.getBonhan_chuyendi());
                    item2.setBonhan_chuyenphacdo(row1.getBonhan_chuyenphacdo());
                    item2.setBonhan_matdau(row1.getBonhan_matdau());
                    item2.setBonhan_tuvong(row1.getBonhan_tuvong());
                    item2.setNhanThuoc(row1.getNhanThuoc());
                }
                map.put("2", item2);
                
                form.getChildren().put(parameter.getValue(), map);
            }
            if (parameter.getAttribute02().equals("1")) {
                map = new LinkedHashMap<>();
                item1 = new RegimenTable();
                item1.setRegimentName(parameter.getValue());
                item1.setRegimentID(parameter.getCode());
                item1.setStt(String.valueOf(form.getChildren().size() + 1));
                row = results1.getOrDefault(String.format("%s_%s", item1.getRegimentID(), "nl"), null);
                if (row != null) {
                    item1.setBatdaunhan(row.getBatdaunhan());
                    item1.setDangnhan(row.getDangnhan());
                    item1.setBonhan_botri(row.getBonhan_botri());
                    item1.setBonhan_chuyendi(row.getBonhan_chuyendi());
                    item1.setBonhan_chuyenphacdo(row.getBonhan_chuyenphacdo());
                    item1.setBonhan_matdau(row.getBonhan_matdau());
                    item1.setBonhan_tuvong(row.getBonhan_tuvong());
                    item1.setNhanThuoc(row.getNhanThuoc());
                }
                map.put("1", item1);
                item2 = new RegimenTable();
                item2.setRegimentName(parameter.getValue());
                item2.setRegimentID(parameter.getCode());
                item2.setStt(String.valueOf(form.getChildren().size() + 1));
                row1 = results2.getOrDefault(String.format("%s_%s", item2.getRegimentID(), "nl"), null);
                if (row1 != null) {
                    item2.setBatdaunhan(row1.getBatdaunhan());
                    item2.setDangnhan(row1.getDangnhan());
                    item2.setBonhan_botri(row1.getBonhan_botri());
                    item2.setBonhan_chuyendi(row1.getBonhan_chuyendi());
                    item2.setBonhan_chuyenphacdo(row1.getBonhan_chuyenphacdo());
                    item2.setBonhan_matdau(row1.getBonhan_matdau());
                    item2.setBonhan_tuvong(row1.getBonhan_tuvong());
                    item2.setNhanThuoc(row1.getNhanThuoc());
                }
                map.put("2", item2);
                
                form.getAdults().put(parameter.getValue(), map);
            }
        }

        return form;
    }

    @GetMapping(value = {"/opc-regimen/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        RegimenForm form = getData(month, year, sites);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Báo cáo phác đồ điều trị");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        
        HashMap<String, SiteEntity> listSites = new HashMap<>();
        for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
            listSites.put(item.getID().toString(), item);
        }
        
        model.addAttribute("sites", listSites);
        model.addAttribute("isOpc", form.isOpc());
        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("title_small", isOpcManager() ? "Khoa điều trị" : "Cơ sở điều trị");
        return "report/opc/regimen-index";
    }

    @GetMapping(value = {"/opc-regimen/email.html"})
    public String actionSendEmail(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcRegimenReport("index"));
        }
        RegimenForm form = getData(month, year, sites);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo phác đồ điều trị (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_REPORT_03,
                context,
                new RegimenExcel(getData(month, year, sites), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcRegimenReport("index"));
    }

    @GetMapping(value = {"/opc-regimen/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        return exportExcel(new RegimenExcel(getData(month, year, sites), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/opc-regimen/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        RegimenForm form = getData(month, year, sites);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());
        return exportPdf(form.getFileName(), "report/opc/regimen-pdf", context);
    }

}
