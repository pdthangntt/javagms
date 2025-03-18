package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.opc.MerQuarterlyExcel;
import com.gms.entity.form.opc_arv.OpcMerForm;
import com.gms.repository.impl.OpcMerRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * BC MER
 *
 * @author vvthành
 */
@Controller(value = "opc_mer_report")
public class OpcMERController extends OpcController {

    @Autowired
    private OpcMerRepositoryImpl merRepositoryImpl;
    
    private OpcMerForm getData(String quarter, String year, String siteID) {
        OpcMerForm form =  new OpcMerForm();

        LoggedUser currentUser = getCurrentUser();
        form.setFileName("BC_PEPFAR_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo dự án PEPFAR");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) : Integer.valueOf(quarter);
        
        String fromTime =  TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(q, y), FORMATDATE) ;
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(q, y), FORMATDATE);
        
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setYear(String.valueOf(y));
        form.setQuarter(String.valueOf(q));
        
        List<Long> siteIDs = new ArrayList<>();
        List<SiteEntity> sites = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        if(isOpcManager()){
            try {
                if(StringUtils.isNotEmpty(siteID) ){
                    siteIDs.add(Long.parseLong(siteID));
                    form.setTreatmentSiteName(siteService.findOne(Long.parseLong(siteID)).getName());
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
        
        form.setTable01Quarterly(merRepositoryImpl.getMerTable01Quarterly(siteIDs, TextUtils.convertDate(fromTime, FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        form.setTable0102Quarterly(merRepositoryImpl.getMerTable012Quarterly(siteIDs, TextUtils.convertDate(fromTime, FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        form.setTable02Quarterly(merRepositoryImpl.getMerTable02Quarterly(siteIDs, TextUtils.convertDate(fromTime, FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        form.setTable03Quarterly(merRepositoryImpl.getMerTable03Quarterly(siteIDs, TextUtils.convertDate(fromTime, FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        form.setTable04Quarterly(merRepositoryImpl.getMerTable04Quarterly(siteIDs, TextUtils.convertDate(fromTime, FORMATDATE), TextUtils.convertDate(toTime, FORMATDATE)));
        
        return form;
    }

    @GetMapping(value = {"/opc-mer/email.html"})
    public String actionSendEmail(Model model, 
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcMerReport());
        }
        OpcMerForm form = getData(quarter, year, siteID);
        form.setOpcManager(isOpcManager() && !isOPC());
        form.setOpc(!isOpcManager() && isOPC());
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo dự án PEPFAR theo quý"),
                EmailoutboxEntity.TEMPLATE_REPORT_MER_QUARTERLY,
                context,
                new MerQuarterlyExcel(form, getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcMerReport());
    }

    @GetMapping(value = {"/opc-mer/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID) throws Exception {
        OpcMerForm form = getData(quarter, year, siteID);
        form.setOpcManager(isOpcManager() && !isOPC());
        form.setOpc(!isOpcManager() && isOPC());
        return exportExcel(new MerQuarterlyExcel(form, getOptions(), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-mer/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes) throws Exception {
        return exportPdf(null, "report/pac/pac-dead-pdf.html", null);
    }
    
    @GetMapping(value = {"/opc-mer/index.html"})
    public String actionIndexQuaterly(
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "site_id", required = false, defaultValue = "") String siteID) {
        
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        
        OpcMerForm form = getData(quarter, year, siteID);
        form.setOpcManager(isOpcManager() && !isOPC());
        form.setOpc(!isOpcManager() && isOPC());
        model.addAttribute("shortName", getCurrentUser().getSite().getShortName());
        model.addAttribute("title", "Báo cáo dự án PEPFAR");
        model.addAttribute("options", options);
        model.addAttribute("tab", tab);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("isOPC", isOPC());
        model.addAttribute("form", form);
        return "report/opc/mer-quaterly";
    }
}
