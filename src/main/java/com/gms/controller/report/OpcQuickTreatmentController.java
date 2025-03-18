package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.QuickTreatmentExcel;
import com.gms.entity.form.opc_arv.OpcQuickTreatmentForm;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * BC điều trị nhanh (BÁO CÁO ĐIỀU TRỊ  ARV VÀ CẤP THUỐC ARV TỐI ĐA 90 NGÀY)
 *
 * @author TrangBN
 */
@Controller(value = "opc_quick_report")
public class OpcQuickTreatmentController extends OpcController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;

    private OpcQuickTreatmentForm getData(String fromTime, String toTime) {
        boolean opcManager = isOpcManager();
        LoggedUser currentUser = getCurrentUser();
        Date date = new Date();
        
        fromTime = fromTime == null || fromTime.equals("") || !isThisDateValid(fromTime) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") || !isThisDateValid(toTime) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE) : toTime;

        OpcQuickTreatmentForm form = new OpcQuickTreatmentForm();
        form.setFileName("BC_ARV_NHANTHUOC90" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo điều trị nhanh ");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());
        form.setStaffName(currentUser.getUser().getName());
        form.setOptions(getOptions().get("siteOpcFrom"));

        Set<Long> siteID = new HashSet<>();
        if (opcManager) {
            siteID.addAll(getSiteManager());
        } else {
            siteID.add(currentUser.getSite().getID());
        }

        // Quản ký trước điều trị arv và dự phòng lao
        form.setTable(arvRepositoryImpl.getQuickTreatmentTable(siteID,
                TextUtils.convertDate(fromTime, FORMATDATE),
                TextUtils.convertDate(toTime, FORMATDATE)));

        return form;
    }

    /**
     * Báo cáo diều trị nhanh ARV và cấp thuốc 90 ngày
     * 
     * @param model
     * @param fromTime
     * @param toTime
     * @param tab
     * @return 
     */
    @GetMapping(value = {"/opc-quick-treatment/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) {
        OpcQuickTreatmentForm form = getData(fromTime, toTime);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Báo cáo điều trị nhanh");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("isOpc", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("title_small", "");
        return "report/opc/quick-treatment";
    }

    @GetMapping(value = {"/opc-quick-treatment/email.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcQuickTreatment("index"));
        }
        OpcQuickTreatmentForm form = getData(fromTime, toTime);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo điều trị  arv và cấp thuốc arv tối đa 90 ngày (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.QUICK_TREATMENT,
                context,
                new QuickTreatmentExcel(getData(fromTime, toTime), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcQuickTreatment("index"));
    }

    @GetMapping(value = {"/opc-quick-treatment/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        return exportExcel(new QuickTreatmentExcel(getData(fromTime, toTime), getOptions(), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-quick-treatment/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            RedirectAttributes redirectAttributes) throws Exception {
        OpcQuickTreatmentForm form = getData(fromTime, toTime);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());
        return exportPdf(form.getFileName(), "report/opc/quick-treatment-pdf.html", context);
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
