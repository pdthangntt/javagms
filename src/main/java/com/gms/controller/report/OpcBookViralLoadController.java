package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.excel.opc.ViralloadExcel;
import com.gms.entity.form.opc_arv.OpcBookViralLoadForm;
import com.gms.entity.form.opc_arv.OpcBookViralLoadTable2Form;
import com.gms.entity.form.opc_arv.OpcBookViralLoadTableForm;
import com.gms.repository.impl.OpcBookViralRepositoryImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * Số TLVR
 *
 * @author vvthành
 */
@Controller(value = "opc_book_viralload_report")
public class OpcBookViralLoadController extends OpcController {

    @Autowired
    private OpcBookViralRepositoryImpl bookViralRepositoryImpl;
    
    private static final String TEMPLATE_REPORT = "report/opc/opc-book-viralload-pdf";
    
    private OpcBookViralLoadForm getData(String keywords, String fromTime, String toTime, int page, int size) throws ParseException {
        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();

        OpcBookViralLoadForm form = new OpcBookViralLoadForm();
        
        form.setTitle("Sổ theo dõi xét nghiệm tải lượng virus HIV");
        form.setFileName("SoTlvrHIV" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getOptions());
        // Chuẩn hóa ngày
        Date date = new Date();
        fromTime = isThisDateValid(fromTime) ? fromTime : null;
        toTime = isThisDateValid(toTime) ? toTime : null;

        // Xử lý điều kiện tìm kiếm
        fromTime = fromTime == null || fromTime.equals("") || !isThisDateValid(fromTime) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") || !isThisDateValid(toTime) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE) : toTime;
        form.setStartDate(fromTime);
        form.setEndDate(toTime);
        form.setKeywords(keywords);
        
        // Tìm trong tháng
        SimpleDateFormat sdfrmt1 = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDateConvert = sdfrmt1.parse(fromTime);
        Date toDateConvert = sdfrmt1.parse(toTime);
        
        Date firstFromDate = TextUtils.getFirstDayOfMonth(fromDateConvert);
        Date firstToDate = TextUtils.getFirstDayOfMonth(toDateConvert);
        Date lastFromDate = TextUtils.getLastDayOfMonth(fromDateConvert);
        Date lastToDate = TextUtils.getLastDayOfMonth(toDateConvert);
        
        if (firstFromDate != null && firstToDate != null && lastFromDate != null && lastToDate != null
                && TextUtils.formatDate(firstFromDate, FORMATDATE).equals(TextUtils.formatDate(firstToDate, FORMATDATE))
                && TextUtils.formatDate(lastFromDate, FORMATDATE).equals(TextUtils.formatDate(lastFromDate, FORMATDATE))) {
                form.setSearchInMonth(true);
        } else {
            form.setSearchInMonth(false);
        }

        //Truy vấn dữ liệu
        Long id = currentUser.getSite().getID();
        DataPage<OpcBookViralLoadTableForm> bookViralLoadTable = bookViralRepositoryImpl.getBookViralLoadTable(id, keywords, fromTime, toTime, page, size);
        form.setTable(bookViralLoadTable);
        
        OpcBookViralLoadTable2Form table2 = bookViralRepositoryImpl.getBookViralLoadTable02(id, keywords, fromTime, toTime);
        form.setTable02(table2);
        
        if (bookViralLoadTable.getTotalPages() > 1 || size == 99999) {
            DataPage<OpcBookViralLoadTableForm> bookViralLoadTable2 = bookViralRepositoryImpl.getBookViralLoadTable2(id, keywords, fromTime, toTime);
            form.setTable2All(bookViralLoadTable2);
        }
        
        return form;
    }

    /**
     * Sổ tải lượng virus
     * 
     * @param model
     * @param startDate
     * @param endDate
     * @param keywords
     * @param page
     * @param size
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/opc-book-viralload/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "keywords", defaultValue = "") String keywords,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {
        
        OpcBookViralLoadForm form = getData(keywords, startDate, endDate, page, size);
        
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Sổ theo dõi xét nghiệm tải lượng virus HIV");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        
        return "report/opc/opc-book-viralload";
    }

    @GetMapping(value = {"/opc-book-viralload/mail.html"})
    public String actionSendEmail(@RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "keywords", defaultValue = "") String keywords,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size,
            RedirectAttributes redirectAttributes) throws ParseException, Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcBookViralLoad("index"));
        }
        OpcBookViralLoadForm form = getData(keywords, startDate, endDate, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s%s", form.getTitle(), "(Thời gian từ  " + form.getStartDate() + " đến " + form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_OPC_VIRALLOAD_BOOK,
                context,
                new ViralloadExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcBookViralLoad("index"));
    }

    @GetMapping(value = {"/opc-book-viralload/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "keywords", defaultValue = "") String keywords,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size) throws Exception {
        
        OpcBookViralLoadForm data = getData(keywords, startDate, endDate, page, size);
        return exportExcel(new ViralloadExcel(data, EXTENSION_EXCEL_X));
    }

    /**
     * Check valid date
     *
     * @param dateToValidate
     * @return
     */
    private boolean isThisDateValid(String dateToValidate) {

        if (StringUtils.isEmpty(dateToValidate)) {
            return false;
        }
        dateToValidate = dateToValidate.toLowerCase();
        if (dateToValidate.contains("d") || dateToValidate.contains("m") || dateToValidate.contains("y")) {
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
    
    @GetMapping(value = {"/opc-book-viralload/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "keywords", defaultValue = "") String keywords,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size) throws Exception {
        
        OpcBookViralLoadForm form = getData(keywords, startDate, endDate, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isOpcManager", isOpcManager());
        return exportPdf("SoTLVR_" + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);
    }
}
