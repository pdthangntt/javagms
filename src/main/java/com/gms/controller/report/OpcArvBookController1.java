package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.ArvBookExcel;
import com.gms.entity.form.opc_arv.ArvBookForm1;
import com.gms.entity.form.opc_arv.SoKhangTheArvForm;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import java.text.ParseException;
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
 * Sổ kháng thể ARV
 *
 * @author TrangBN
 */
@Controller(value = "opc_arv_book_report")
public class OpcArvBookController1 extends OpcController {

    protected static final String PDF_FILE_NAME = "So Arv_";

    @Autowired
    private OpcArvRepositoryImpl opcArvRepositoryImpl;

    private ArvBookForm1 getData(String word, String treatmentTimeFrom, String treatmentTimeTo, int page, int size) throws ParseException {
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
        treatmentTimeFrom = StringUtils.isEmpty(treatmentTimeFrom) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(new Date()), FORMATDATE) : treatmentTimeFrom;
        treatmentTimeTo = StringUtils.isEmpty(treatmentTimeTo) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(new Date()), FORMATDATE) : treatmentTimeTo;

        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        ArvBookForm1 form = new ArvBookForm1();
        form.setTitle("SỔ ĐIỀU TRỊ BẰNG THUỐC KHÁNG HIV");
        form.setFileName("SoARV_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(options);
        form.setFrom(treatmentTimeFrom);
        form.setTo(treatmentTimeTo);
        form.setWord(StringUtils.isEmpty(word) || word.equals("undefined") ? "" : word);

        Long siteID = currentUser.getSite().getID();
        DataPage<SoKhangTheArvForm> data = opcArvRepositoryImpl.getSoKhangTheArv(siteID, word, TextUtils.convertDate(treatmentTimeFrom, FORMATDATE), TextUtils.convertDate(treatmentTimeTo, FORMATDATE), page, size);
        form.setDataPage(data);
        
        return form;
    }

    /**
     *
     * @param model
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param word
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = {"/opc-book/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "word", required = false) String word,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
        ArvBookForm1 form = getData(word, treatmentTimeFrom, treatmentTimeTo, page, size);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Sổ điều trị bằng thuốc kháng HIV");
        model.addAttribute("small_title", "Sổ điều trị ARV");
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        return "report/opc/arv-book_1";
    }

    @GetMapping(value = {"/opc-book/email.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "word", required = false) String word,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.TREATMENT_REGIMEN).remove("");
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcBook());
        }
        ArvBookForm1 form = getData(word, treatmentTimeFrom, treatmentTimeTo, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("options", options);
        context.put("form", form);
        context.put("isOpc", isOPC());
        context.put("isOpcManager", isOpcManager());
        context.put("currentUser", getCurrentUser());

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Sổ điều trị bằng thuốc kháng HIV"),
                EmailoutboxEntity.TEMPLATE_ARV_BOOK,
                context,
                new ArvBookExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcBook());
    }

    @GetMapping(value = {"/opc-book/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "word", required = false) String word,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "9999999") int size) throws Exception {
        return exportExcel(new ArvBookExcel(getData(word, treatmentTimeFrom, treatmentTimeTo, page, size), EXTENSION_EXCEL_X));
    }
}
