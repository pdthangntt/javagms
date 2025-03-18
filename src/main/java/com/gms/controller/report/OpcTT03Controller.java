package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.TT03Excel;
import com.gms.entity.form.opc_arv.TT03Form;
import com.gms.entity.form.opc_arv.TT03Table02;
import com.gms.entity.form.opc_arv.TT03Table03;
import com.gms.entity.form.opc_arv.TT03Table04;
import com.gms.entity.form.opc_arv.TT03Table05;
import com.gms.entity.form.opc_arv.TT03Table06;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
 * BC TT03/2015/TT-BYT
 *
 * @author vvthành
 */
@Controller(value = "opc_tt03_report")
public class OpcTT03Controller extends OpcController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;

    private TT03Form getData(String fromTime, String toTime, String tab) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> siteOpcs = options.get("siteOpcFrom");

        boolean opcManager = isOpcManager();
        LoggedUser currentUser = getCurrentUser();
        Date date = new Date();

        if (tab.equals("year")) {
            fromTime = fromTime == null || fromTime.equals("") || !isThisDateValid(fromTime) ? TextUtils.formatDate(TextUtils.getFirstDayOfYear(date), FORMATDATE) : fromTime;
            toTime = toTime == null || toTime.equals("") || !isThisDateValid(toTime) ? TextUtils.formatDate(TextUtils.getLastDayOfYear(date), FORMATDATE) : toTime;
        } else {
            fromTime = fromTime == null || fromTime.equals("") || !isThisDateValid(fromTime) ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(date), FORMATDATE) : fromTime;
            toTime = toTime == null || toTime.equals("") || !isThisDateValid(toTime) ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(date), FORMATDATE) : toTime;
        }
        TT03Form form = new TT03Form();
        form.setFileName("TT032015TTBYT_ARV_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo điều trị ngoại trú HIV " + (isOpcManager() ? "tuyến tỉnh" : "tuyến huyện"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setTab(tab);
        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());
        form.setStaffName(currentUser.getUser().getName());

        Set<Long> siteID = new HashSet<>();
        if (opcManager) {
            siteID.addAll(getSiteManager());
        } else {
            siteID.add(currentUser.getSite().getID());
        }

        if (tab.equals("year")) {
            //Bảng điều trị arv 
            form.setTable02(arvRepositoryImpl.getTT03Table02(siteID,
                    TextUtils.convertDate(fromTime, FORMATDATE),
                    TextUtils.convertDate(toTime, FORMATDATE)));

            //Bảng theo dõi xét nghiệm tải lượng hiv
            form.setTable03(arvRepositoryImpl.getTT03Table03(siteID,
                    TextUtils.convertDate(fromTime, FORMATDATE),
                    TextUtils.convertDate(toTime, FORMATDATE)));
        } else {
            //Quản ký trước điều trị arv và dự phòng lao
            form.setTable04(arvRepositoryImpl.getTT03Table04(siteID,
                    TextUtils.convertDate(fromTime, FORMATDATE),
                    TextUtils.convertDate(toTime, FORMATDATE)));

            //Quản lý điều trị ARV
            form.setTable05(arvRepositoryImpl.getTT03Table05(siteID,
                    TextUtils.convertDate(fromTime, FORMATDATE),
                    TextUtils.convertDate(toTime, FORMATDATE)));
            
            form.setTable06(arvRepositoryImpl.getTT03Table06(siteID,
                    TextUtils.convertDate(fromTime, FORMATDATE),
                    TextUtils.convertDate(toTime, FORMATDATE)));
        }

        HashMap<String, String> defaultSite = new HashMap<>();
        defaultSite.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite().getShortName());
        if (opcManager) {
            defaultSite = siteOpcs;
        }

        Map<Long, TT03Table02> table02 = new HashMap<>();
        Map<Long, TT03Table03> table03 = new HashMap<>();
        Map<Long, TT03Table04> table04 = new HashMap<>();
        Map<Long, Map<String, TT03Table05>> table05 = new HashMap<>();
        Map<Long, TT03Table06> table06 = new HashMap<>();
        
        if (form.getTable05() != null && form.getTable05().size() > 0) {
            table05 = form.getTable05();
        }
        
        for (Map.Entry<String, String> site : defaultSite.entrySet()) {
            if (site.getKey().equals("") || site.getKey().equals("-1")) {
                continue;
            }
            Long itemID = Long.valueOf(site.getKey());
            if (form.getTable02() != null) {
                table02.put(itemID, form.getTable02().getOrDefault(itemID, new TT03Table02()));
            }
            if (form.getTable03() != null) {
                table03.put(itemID, form.getTable03().getOrDefault(itemID, new TT03Table03()));
            }
            if (form.getTable04() != null) {
                table04.put(itemID, form.getTable04().getOrDefault(itemID, new TT03Table04()));
            }
            
            if (form.getTable06() != null) {
                table06.put(itemID, form.getTable06().getOrDefault(itemID, new TT03Table06()));
            }
            
            if (!table05.containsKey(itemID)) {
                table05.put(itemID, new HashMap<String, TT03Table05>());
                table05.get(itemID).put("", new TT03Table05());
            }
        }
        form.setTable02(table02);
        form.setTable03(table03);
        form.setTable04(table04);
        form.setTable05(table05);
        form.setTable06(table06);

        return form;
    }

    @GetMapping(value = {"/opc-tt03/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "tab", defaultValue = "quarter") String tab) {
        TT03Form form = getData(fromTime, toTime, tab);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Báo cáo TT03/2015/TT-BYT");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("tab", form.getTab());
        model.addAttribute("isOpc", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("title_small", isOpcManager() ? "tuyến tỉnh" : "tuyến huyện");
        return "report/opc/tt03-index";
    }

    @GetMapping(value = {"/opc-tt03/email.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "tab", defaultValue = "quarter") String tab) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcTT03("index"));
        }
        TT03Form form = getData(fromTime, toTime, tab);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("tab", form.getTab());
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo điều trị ngoại trú HIV (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_REPORT_03,
                context,
                new TT03Excel(getData(fromTime, toTime, tab), getOptions(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcTT03("index"));
    }

    @GetMapping(value = {"/opc-tt03/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "tab", defaultValue = "quarter") String tab) throws Exception {
        return exportExcel(new TT03Excel(getData(fromTime, toTime, tab), getOptions(), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-tt03/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "tab", defaultValue = "quarter") String tab,
            RedirectAttributes redirectAttributes) throws Exception {
        TT03Form form = getData(fromTime, toTime, tab);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);
        context.put("tab", form.getTab());
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());
        return exportPdf(form.getFileName(), "report/opc/tt03-pdf.html", context);
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
