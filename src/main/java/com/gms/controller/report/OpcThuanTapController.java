package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.ThuanTapExcel;
import com.gms.entity.form.opc_arv.OpcThuantapAgesForm;
import com.gms.entity.form.opc_arv.ThuanTapForm;
import com.gms.repository.impl.OpcThuanTapRepositoryImpl;
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
 * BC thuần tập
 *
 * @author vvthành
 */
@Controller(value = "opc_thuantap_report")
public class OpcThuanTapController extends OpcController {

    @Autowired
    private OpcThuanTapRepositoryImpl thuantapRevision;
    
    /**
     * Lấy thông tin các cơ sở thuộc khoa
     * 
     * @return 
     */
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
    
    /**
     * Lấy thông tin dữ liệu
     * 
     * @auth TrangBN
     * @param year
     * @param sites
     * @return 
     */
    private ThuanTapForm getData(String year, String siteID) {
        
        LoggedUser currentUser = getCurrentUser();
        List<Long> siteIDs = new ArrayList<>();
        ThuanTapForm form = new ThuanTapForm();
        form.setFileName("BC_THUANTAP_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo thuần tập");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());
        int yy = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        
        List<SiteEntity> sites = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        if(isOpcManager()){
            try {
                if(StringUtils.isNotEmpty(siteID) ){
                    siteIDs.add(Long.parseLong(siteID));
                    form.setSite(siteService.findOne(Long.parseLong(siteID)) != null ? siteService.findOne(Long.parseLong(siteID)).getID().toString() : "");
                } else {
                    for(SiteEntity site : sites){
                        siteIDs.add(site.getID());
                    }
                }
            } catch (NumberFormatException ex){
                for(SiteEntity site : sites){
                    siteIDs.add(site.getID());
                }
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }

        if (isOPC()) {
            form.setSiteLabel(currentUser.getSite().getName());
        }
        
        if (isOpcManager()) {
            form.setSiteLabel(getSites().get(siteID) == null ? "" : getSites().get(siteID).getName());
        }
        
        int yearNumber = Calendar.getInstance().get(Calendar.YEAR) - 1;
        HashMap<String, HashMap<String, OpcThuantapAgesForm>> data = new HashMap<>();
        HashMap<String, HashMap<String, OpcThuantapAgesForm>> results = new HashMap<>();
        if (StringUtils.isNotEmpty(year)) {
            yearNumber = Integer.parseInt(year) - 1;
        }
        
        // Lặp theo từng quý
        for (int i = 0; i < 4; i++) {
            Date startDate = TextUtils.getFirstDayOfQuarter(i, yearNumber);
            Date endDate = TextUtils.getLastDayOfQuarter(i, yearNumber);
            data = thuantapRevision.getTable(siteIDs, startDate, endDate, yearNumber, i);
            
            for (Map.Entry<String, HashMap<String, OpcThuantapAgesForm>> entry : data.entrySet()) {
                if (results.containsKey(entry.getKey())) {
                    for (Map.Entry<String, OpcThuantapAgesForm> e : entry.getValue().entrySet()) {
                        results.get(entry.getKey()).put(e.getKey(), e.getValue());
                    }
                    continue;
                }
                
                results.put(entry.getKey(), new HashMap<String, OpcThuantapAgesForm>());
                for (Map.Entry<String, OpcThuantapAgesForm> e : entry.getValue().entrySet()) {
                    results.get(entry.getKey()).put(e.getKey(), e.getValue());
                }
            }
        }
        
        // Tính tổng năm
        List<Integer> years = new ArrayList<>();
        years.add(yearNumber);
        years.add(yearNumber + 1);
        List<String> listKey = new ArrayList<>();
        HashMap<String, HashMap<String, OpcThuantapAgesForm>> cloneResult = new HashMap<>();
        for (Integer y : years) {
            listKey.add(String.format("%s-%s", String.valueOf(y), GenderEnum.MALE.getKey()));
            listKey.add(String.format("%s-%s", String.valueOf(y), GenderEnum.FEMALE.getKey()));
        }
        
        OpcThuantapAgesForm ttForm = new OpcThuantapAgesForm();
        String femaleLastYear = String.format("%s-%s", String.valueOf(yearNumber), GenderEnum.FEMALE.getKey());
        String maleLastYear = String.format("%s-%s", String.valueOf(yearNumber), GenderEnum.MALE.getKey());
        String maleThisYear = String.format("%s-%s", String.valueOf(yearNumber + 1), GenderEnum.MALE.getKey());
        String femaleThisYear = String.format("%s-%s", String.valueOf(yearNumber + 1), GenderEnum.FEMALE.getKey());
        
        for (Map.Entry<String, HashMap<String, OpcThuantapAgesForm>> entry : results.entrySet()) {
            cloneResult.put(entry.getKey(), new HashMap<String, OpcThuantapAgesForm>());
            for (Map.Entry<String, OpcThuantapAgesForm> e : entry.getValue().entrySet()) {
                if (e.getKey().contains(femaleLastYear)) {
                    ttForm = new OpcThuantapAgesForm();
                    if (cloneResult.get(entry.getKey()).containsKey("total-" + femaleLastYear)) {
                        cloneResult.get(entry.getKey()).get("total-" + femaleLastYear).setUnderFifteen(  cloneResult.get(entry.getKey()).get("total-" + femaleLastYear).getUnderFifteen() + e.getValue().getUnderFifteen());
                        cloneResult.get(entry.getKey()).get("total-" + femaleLastYear).setOverFifteen(  cloneResult.get(entry.getKey()).get("total-" + femaleLastYear).getOverFifteen() + e.getValue().getOverFifteen());
                    } else {
                        ttForm.setUnderFifteen(e.getValue().getUnderFifteen());
                        ttForm.setOverFifteen(e.getValue().getOverFifteen());
                        cloneResult.get(entry.getKey()).put("total-" + femaleLastYear, ttForm);
                    }
                }
                
                if (e.getKey().contains(maleLastYear)) {
                    ttForm = new OpcThuantapAgesForm();
                    if (cloneResult.get(entry.getKey()).containsKey("total-" + maleLastYear)) {
                        cloneResult.get(entry.getKey()).get("total-" + maleLastYear).setUnderFifteen(  cloneResult.get(entry.getKey()).get("total-" + maleLastYear).getUnderFifteen() + e.getValue().getUnderFifteen());
                        cloneResult.get(entry.getKey()).get("total-" + maleLastYear).setOverFifteen(  cloneResult.get(entry.getKey()).get("total-" + maleLastYear).getOverFifteen() + e.getValue().getOverFifteen());
                    } else {
                        ttForm.setUnderFifteen(e.getValue().getUnderFifteen());
                        ttForm.setOverFifteen(e.getValue().getOverFifteen());
                        cloneResult.get(entry.getKey()).put("total-" + maleLastYear, ttForm);
                    }
                }
                
                if (e.getKey().contains(femaleThisYear)) {
                    ttForm = new OpcThuantapAgesForm();
                    if (cloneResult.get(entry.getKey()).containsKey("total-" + femaleThisYear)) {
                        cloneResult.get(entry.getKey()).get("total-" + femaleThisYear).setUnderFifteen(  cloneResult.get(entry.getKey()).get("total-" + femaleThisYear).getUnderFifteen() + e.getValue().getUnderFifteen());
                        cloneResult.get(entry.getKey()).get("total-" + femaleThisYear).setOverFifteen(  cloneResult.get(entry.getKey()).get("total-" + femaleThisYear).getOverFifteen() + e.getValue().getOverFifteen());
                    } else {
                        ttForm.setUnderFifteen(e.getValue().getUnderFifteen());
                        ttForm.setOverFifteen(e.getValue().getOverFifteen());
                        cloneResult.get(entry.getKey()).put("total-" + femaleThisYear, ttForm);
                    }
                }
                
                if (e.getKey().contains(maleThisYear)) {
                    ttForm = new OpcThuantapAgesForm();
                    if (cloneResult.get(entry.getKey()).containsKey("total-" + maleThisYear)) {
                        cloneResult.get(entry.getKey()).get("total-" + maleThisYear).setUnderFifteen(  cloneResult.get(entry.getKey()).get("total-" + maleThisYear).getUnderFifteen() + e.getValue().getUnderFifteen());
                        cloneResult.get(entry.getKey()).get("total-" + maleThisYear).setOverFifteen(  cloneResult.get(entry.getKey()).get("total-" + maleThisYear).getOverFifteen() + e.getValue().getOverFifteen());
                    } else {
                        ttForm.setUnderFifteen(e.getValue().getUnderFifteen());
                        ttForm.setOverFifteen(e.getValue().getOverFifteen());
                        cloneResult.get(entry.getKey()).put("total-" + maleThisYear, ttForm);
                    }
                }
            }
        }
        
        for (Map.Entry<String, HashMap<String, OpcThuantapAgesForm>> entry : cloneResult.entrySet()) {
            if (results.containsKey(entry.getKey())) {
                for (Map.Entry<String, OpcThuantapAgesForm> e : entry.getValue().entrySet()) {
                    results.get(entry.getKey()).put(e.getKey(), e.getValue());
                }
            }
        }
        
        form.setYear(yy);
        form.setDataResults(results);
        return form;
    }

    /**
     * Báo cáo thuần tập lấy số liệu báo cáo
     * 
     * @param model
     * @param year
     * @param siteID
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/opc-thuantap/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String siteID) throws Exception {
        
        ThuanTapForm form = getData(year, siteID);
        
        if (isOPC()) {
            form.setSiteLabel(siteID);
        }
        
        form.setStaffName(getCurrentUser().getUser().getName());
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Báo cáo thuần tập");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("sites", getSites());
        model.addAttribute("isOpc", form.isOpc());
        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        return "report/opc/thuantap";
    }

    /**
     * Gửi mail
     * 
     * @param model
     * @param year
     * @param sites
     * @param redirectAttributes
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/opc-thuantap/email.html"})
    public String actionSendEmail(Model model,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcThuanTap("index"));
        }
        ThuanTapForm form = getData(year, sites);
        HashMap<String, Object> context = new HashMap<>();
        form.setStaffName(getCurrentUser().getUser().getName());
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo thuần tập  (năm %s)", form.getYear()),
                EmailoutboxEntity.TEMPLATE_THUAN_TAP,
                context,
                new ThuanTapExcel(getData(year, sites), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcThuanTap("index"));
    }

    /**
     * Xuất excel
     * 
     * @param year
     * @param sites
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/opc-thuantap/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        ThuanTapForm form = getData(year, sites);
        return exportExcel(new ThuanTapExcel(form, EXTENSION_EXCEL_X));
    }

    /**
     * In BC thuần tập
     * 
     * @param year
     * @param sites
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/opc-thuantap/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites) throws Exception {
        ThuanTapForm form = getData(year, sites);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());
        return exportPdf(form.getFileName(), "report/opc/thuantap-pdf", context);
    }
}
