package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.InsuranceExcel;
import com.gms.entity.form.opc_arv.InsuranceForm;
import com.gms.entity.form.opc_arv.InsuranceTable;
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
 * BC bảo hiểm y tế
 *
 * @author vvthành
 */
@Controller(value = "opc_insurance_report")
public class OpcInsuranceController extends OpcController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;

    private InsuranceForm getData(String fromTime, String toTime) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> siteOpcs = options.get("siteOpcFrom");

        LoggedUser currentUser = getCurrentUser();
        Date date = new Date();

        fromTime = fromTime == null || fromTime.equals("") ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE) : toTime;

        InsuranceForm form = new InsuranceForm();
        form.setFileName("BC_BHYT_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo bảo hiểm y tế");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());

        Set<Long> siteID = new HashSet<>();
        if (form.isOpcManager()) {
            siteID.addAll(getSiteManager());
        } else {
            siteID.add(currentUser.getSite().getID());
        }

        HashMap<String, String> defaultSite = new HashMap<>();
        defaultSite.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite().getShortName());
        if (form.isOpcManager()) {
            defaultSite = siteOpcs;
        }

        form.setTable(arvRepositoryImpl.getInsuranceTable(siteID,
                TextUtils.convertDate(fromTime, FORMATDATE),
                TextUtils.convertDate(toTime, FORMATDATE)));

        //config default
        Map<Long, InsuranceTable> table = new HashMap<>();
        for (Map.Entry<String, String> site : defaultSite.entrySet()) {
            if (site.getKey().equals("") || site.getKey().equals("-1")) {
                continue;
            }
            Long itemID = Long.valueOf(site.getKey());
            if (form.getTable() != null) {
                table.put(itemID, form.getTable().getOrDefault(itemID, new InsuranceTable()));
            }
        }
        form.setTable(table);
        return form;
    }

    @GetMapping(value = {"/opc-insurance/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) {
        InsuranceForm form = getData(fromTime, toTime);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Báo cáo bảo hiểm y tế");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("isOpc", form.isOpc());
        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("title_small", form.isOpcManager() ? "Khoa điều trị" : "Cơ sở điều trị");
        return "report/opc/insurance-index";
    }

    @GetMapping(value = {"/opc-insurance/email.html"})
    public String actionSendEmail(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcReportInsurance("index"));
        }
        InsuranceForm form = getData(fromTime, toTime);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s (%s - %s)", form.getTitle(), form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_REPORT_03,
                context,
                new InsuranceExcel(getData(fromTime, toTime), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcReportInsurance("index"));
    }

    @GetMapping(value = {"/opc-insurance/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        return exportExcel(new InsuranceExcel(getData(fromTime, toTime), getOptions(), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-insurance/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            RedirectAttributes redirectAttributes) throws Exception {
        InsuranceForm form = getData(fromTime, toTime);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", form.isOpc());
        context.put("isOpcManager", form.isOpcManager());
        context.put("currentUser", getCurrentUser());
        return exportPdf(form.getFileName(), "report/opc/insurance-pdf.html", context);
    }
}
