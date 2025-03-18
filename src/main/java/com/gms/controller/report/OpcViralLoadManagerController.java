package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.ViralManagerExcel;
import com.gms.entity.excel.pac.DeadExcel;
import com.gms.entity.form.opc_arv.ViralLoadReportForm;
import com.gms.entity.form.opc_arv.ViralloadManagerForm;
import com.gms.entity.form.opc_arv.ViralloadManagerTable;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
 * BC tải lượng virus Khoa điều trị
 *
 * @author vvthành
 */
@Controller(value = "opc_report_viralload_manager_report")
public class OpcViralLoadManagerController extends OpcController {
    
    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";
    
    private ViralloadManagerForm getData(String month, String year) {
        
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> siteOpcs = options.get("siteOpcFrom");
        
        boolean opcManager = isOpcManager();
        LoggedUser currentUser = getCurrentUser();
        
        month = StringUtils.isEmpty(month) ? TextUtils.formatDate(new Date(), "M") : month;
        year = StringUtils.isEmpty(year) ? TextUtils.formatDate(new Date(), "yyyy") : year;
        
        ViralloadManagerForm form = new ViralloadManagerForm();
        form.setFileName("Bao cao tai luong virus -CoSo_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo tải lượng virus HIV");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setMonth(month);
        form.setYear(year);
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());
        form.setOptions(options);
        
        Set<Long> siteID = new HashSet<>();
        siteID.addAll(getSiteManager());
        
        form.setTable(arvRepositoryImpl.getViralloadManagerTable(siteID,
                TextUtils.formatDate(TextUtils.getFirstDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL),
                TextUtils.formatDate(TextUtils.getLastDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL)));
        
        form.setTable02(arvRepositoryImpl.getViralloadManagerTable02(siteID,
                TextUtils.formatDate(TextUtils.getFirstDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL),
                TextUtils.formatDate(TextUtils.getLastDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL)));
        
        HashMap<String, String> defaultSite = new HashMap<>();
        defaultSite.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite().getShortName());
        if (opcManager) {
            defaultSite = siteOpcs;
        }
        
        Map<Long, ViralloadManagerTable> table = new HashMap<>();
        
        for (Map.Entry<String, String> site : defaultSite.entrySet()) {
            if (site.getKey().equals("") || site.getKey().equals("-1")) {
                continue;
            }
            Long itemID = Long.valueOf(site.getKey());
            if (form.getTable() != null) {
                table.put(itemID, form.getTable().getOrDefault(itemID, new ViralloadManagerTable()));
            }
        }
        form.setTable(table);
        
        return form;
    }

    /**
     * Báo cáo tải lương virus Khoa điều trị
     *
     * @param model
     * @param month
     * @param redirectAttributes
     * @param year
     * @return
     * @throws java.lang.Exception
     */
    @GetMapping(value = {"/opc-report-viralload-manager/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            RedirectAttributes redirectAttributes) throws Exception {
        
        if (!isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.backendHome());
        }
        
        ViralloadManagerForm form = getData(month, year);
        model.addAttribute("form", form);
        model.addAttribute("isOPC", isOPC());
        model.addAttribute("options", getOptions());
        model.addAttribute("title", "Báo cáo tải lượng virus HIV");
        model.addAttribute("titleBreacum", "BC tải lượng virus HIV");
        
        return "report/opc/viralload-manager";
        
    }
    
    @GetMapping(value = {"/opc-report-viralload-manager/mail.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.viralloadManagerIndex());
        }
        ViralloadManagerForm form = getData(month, year);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("tab", form.getTab());
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());
        
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo tải lượng virus HIV"),
                EmailoutboxEntity.TEMPLATE_REPORT_VIRRALOAD,
                context,
                new ViralManagerExcel(getData(month, year), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.viralloadManagerIndex());
    }
    
    @GetMapping(value = {"/opc-report-viralload-manager/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year) throws Exception {
        return exportExcel(new ViralManagerExcel(getData(month, year), getOptions(), EXTENSION_EXCEL_X));
    }
    
    @GetMapping(value = {"/opc-report-viralload-manager/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year
    ) throws Exception {
        ViralloadManagerForm form = getData(month, year);
        
        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        context.put("options", getOptions());
        
        return exportPdf(form.getFileName(), "report/opc/viralload-manager-pdf.html", context);
    }
}
