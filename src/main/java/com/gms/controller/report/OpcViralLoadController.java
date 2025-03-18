package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.ViralSiteExcel;
import com.gms.entity.excel.pac.DeadExcel;
import com.gms.entity.form.opc_arv.ViralloadForm;
import com.gms.entity.form.opc_arv.ViralLoadReportForm;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.util.Date;
import java.util.HashMap;
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
 * BC tải lượng virus Cơ sở điều trị
 *
 * @author vvthành
 */
@Controller(value = "opc_report_viralload_report")
public class OpcViralLoadController extends OpcController {
    
    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";
    
    private ViralloadForm getData(String month, String year) {
        month = StringUtils.isEmpty(month) ? TextUtils.formatDate(new Date(), "M") : month;
        year = StringUtils.isEmpty(year) ? TextUtils.formatDate(new Date(), "yyyy") : year;
        
        LoggedUser currentUser = getCurrentUser();
        
        ViralloadForm form = new ViralloadForm();
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
        form.setTable(arvRepositoryImpl.getViralloadTable(currentUser.getSite().getID(),
                TextUtils.formatDate(TextUtils.getFirstDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL),
                TextUtils.formatDate(TextUtils.getLastDayOfMonth(Integer.valueOf(month), Integer.valueOf(year)), FORMATDATE_SQL)));
        
        return form;
    }
    
    @GetMapping(value = {"/opc-report-viralload/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            RedirectAttributes redirectAttributes) throws Exception {
        
        if (!isOPC()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.backendHome());
        }
        
        ViralloadForm form = getData(month, year);
        model.addAttribute("form", form);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", getOptions());
        model.addAttribute("title", "Báo cáo tải lượng virus HIV");
        model.addAttribute("titleBreacum", "BC tải lượng virus HIV");
        
        return "report/opc/viralload";
    }
    
    @GetMapping(value = {"/opc-report-viralload/mail.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.viralloadIndex());
        }
        ViralloadForm form = getData(month, year);
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
                new ViralSiteExcel(getData(month, year), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.viralloadIndex());
    }
    
    @GetMapping(value = {"/opc-report-viralload/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year) throws Exception {
        return exportExcel(new ViralSiteExcel(getData(month, year), getOptions(), EXTENSION_EXCEL_X));
    }
    
    @GetMapping(value = {"/opc-report-viralload/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year
    ) throws Exception {
        ViralloadForm form = getData(month, year);
        
        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        
        return exportPdf(form.getFileName(), "report/opc/viralload-pdf.html", context);
    }
}
