package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.laytest.BookExcel;
import com.gms.entity.form.laytest.LaytestBookForm;
import com.gms.service.HtcLaytestService;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
 * Controller xuất sổ tư vấn xét nghiệm
 *
 * @author pdThang
 */
@Controller(value = "htc_laytest_book")
public class LaytestBookController extends BaseController {

    @Autowired
    private HtcLaytestService laytestService;
    @Autowired
    private ParameterService parameterService;

    private static final String TEMPLATE_REPORT = "report/laytest/book-pdf.html";
    private static final String EXCEL_EXTENSION = ".xlsx";

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        return options;
    }

    @GetMapping(value = {"/laytest-book/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        List<ParameterEntity> objectTestActive = new ArrayList<>();
        for (ParameterEntity entity : parameterService.getTestObjectGroup()) {
            if(entity.getStatus() == 1){
                objectTestActive.add(entity);
            }
        }
        LaytestBookForm form = getData(startDate, endDate, objects);

        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Sổ tư vấn xét nghiệm");
        model.addAttribute("title_small", "Sổ tư vấn xét nghiệm");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("search_object", form.getObjects().keySet().size() == objectTestActive.size() ? "" : form.getObjects().keySet().toArray(new String[form.getObjects().size()]));
        return "report/laytest/book";

    }

    /**
     * Search list of the positive
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return
     */
    private LaytestBookForm getData(String startDate, String endDate, String objects) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);

        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);

        LoggedUser currentUser = getCurrentUser();
        LaytestBookForm form = new LaytestBookForm();
        form.setOptions(getOptions());
        form.setObjects(getObjectOption(objects.split(",", -1)));

        form.setStaffID(currentUser.getUser().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getUser().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("SỔ QUẢN LÝ TƯ VẤN XÉT NGHIỆM");
        form.setFileName("SoTVXN_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));

        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));
        Set<Long> siteids = new HashSet<>();
        siteids.add(currentUser.getSite().getID());

        List<HtcLaytestEntity> models = laytestService.findLaytestBook(
                startDateConvert,
                endDateConvert,
                siteids,
                form.getStaffID(),
                form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()));
        if (models != null && !models.isEmpty()) {
            form.setTable(models);
        }
        return form;
    }

    @GetMapping(value = {"/laytest-book/pdf.html"})
    public ResponseEntity<InputStreamResource> actionLaytestPdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {

        LaytestBookForm form = getData(startDate, endDate, objects);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());

        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }

    @GetMapping(value = {"/laytest-book/excel.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookExcel(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {

        return exportExcel(new BookExcel(getData(startDate, endDate, objects), EXCEL_EXTENSION));
    }

    /**
     * Action gửi mail
     *
     * @param redirectAttributes
     * @param startDate
     * @param endDate
     * @param objects
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/laytest-book/email.html"})
    public String actionBookEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.laytestBook());
        }
        LaytestBookForm form = getData(startDate, endDate, objects);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_LAYTEST_BOOK,
                context,
                new BookExcel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.laytestBook(startDate, endDate));
    }

    /**
     * Danh sách dịch vụ thuộc cơ sở xét nghiệm theo ids
     *
     * @param serviceIDs
     * @return
     */
    private HashMap<String, String> getObjectOption(String[] objectIDs) {
        List<String> lobjectIDs = null;
        if (objectIDs != null && objectIDs.length >= 1 && !objectIDs[0].isEmpty()) {
            lobjectIDs = Arrays.asList(objectIDs);
        }
        HashMap<String, String> list = new HashMap<>();
        List<ParameterEntity> objectTest = parameterService.getTestObjectGroup();
        for (ParameterEntity entity : objectTest) {
            if (entity.getStatus() == 0 || (lobjectIDs != null && !lobjectIDs.contains(entity.getCode()))) {
                continue;
            }
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }
}
