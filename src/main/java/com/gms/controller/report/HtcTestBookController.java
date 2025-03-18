/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.htc.TestBookExcel;
import com.gms.entity.form.htc.TestBookForm;
import com.gms.entity.form.htc.TestBookTableForm;
import com.gms.service.HtcVisitService;
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
 *
 * @author DSNAnh
 */
@Controller(value = "htc_test_book")
public class HtcTestBookController extends BaseController {
    
    private static final String TEMPLATE_REPORT = "report/htc/test-book-pdf.html";
    private static final String EXCEL_EXTENSION = ".xlsx";
    
    @Autowired
    private ParameterService parameterService;
    
    @Autowired
    private HtcVisitService htcVisitService;

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }

    /**
     * Danh sách dịch vụ thuộc cơ sở xét nghiệm theo ids
     *
     * @param serviceIDs
     * @return
     */
    private HashMap<String, String> getServiceOption(String[] serviceIDs) {
        List<String> lServiceIDs = null;
        if (serviceIDs != null && serviceIDs.length >= 1 && !serviceIDs[0].isEmpty()) {
            lServiceIDs = Arrays.asList(serviceIDs);
        }
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        List<ParameterEntity> serviceTest = parameterService.getServiceTest();
        for (ParameterEntity entity : serviceTest) {
            if (entity.getStatus() == 0 || (lServiceIDs != null && !lServiceIDs.contains(entity.getCode()))) {
                continue;
            }
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Danh sách đối tượng thuộc cơ sở xét nghiệm theo ids
     *
     * @param objectIDs
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
    
    private TestBookTableForm convertTableBookForm(HtcVisitEntity entity) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        
        TestBookTableForm form = new TestBookTableForm();
        form.setPatientName(entity.getPatientName());
        form.setCode(entity.getCode());
        form.setGender(entity.getGenderID());
        form.setYearOfBirth(entity.getYearOfBirth());
        form.setPermanentAddress(entity.getPermanentAddressFull());
        form.setObjectGroupID(options.get(ParameterEntity.TEST_OBJECT_GROUP).getOrDefault(entity.getObjectGroupID(), null));
        form.setPreTestTime(TextUtils.formatDate(entity.getPreTestTime(), FORMATDATE));
        form.setBioName(StringUtils.isEmpty(entity.getBioName()) ? "" : options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST).getOrDefault(entity.getBioName(), null));
        form.setBioNameResult("2".equals(entity.getTestResultsID()) || "4".equals(entity.getTestResultsID()) ? "Dương tính" : "1".equals(entity.getTestResultsID()) ? "Âm tính" : "3".equals(entity.getTestResultsID()) ? "Nghi ngờ" : "");
        form.setTestResultsID(entity.getTestResultsID() != null && entity.getTestResultsID().equals("") ? "" : options.get(ParameterEntity.TEST_RESULTS).getOrDefault(entity.getTestResultsID(), null));
        if (StringUtils.isNotEmpty(entity.getConfirmResultsID())) {
            form.setConfirmResultID(options.get(ParameterEntity.TEST_RESULTS_CONFIRM).getOrDefault(entity.getConfirmResultsID(), null));
        }
        form.setNote("");
        form.setStaffBeforeTestID(entity.getStaffBeforeTestID());
        return form;
    }
    
    private TestBookForm getData(String startDate, String endDate, String services, String objects, String code) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        //Bắt thêm dịch vụ
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            services = currentUser.getUser().getServiceVisitID();
        }
        
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);
        
        TestBookForm form = new TestBookForm();
        form.setOptions(getOptions());
        
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setServices(getServiceOption(services.split(",", -1)));
        form.setObjects(getObjectOption(objects.split(",", -1)));
        if (code != null && !"".equals(code)) {
            form.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Sổ xét nghiệm sàng lọc HIV");
        form.setFileName("SoXN_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        
        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));
        
        List<HtcVisitEntity> models = htcVisitService.findHtcTestBook(
                startDateConvert,
                endDateConvert,
                new ArrayList<>(form.getServices().keySet()),
                form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()),
                form.getSiteID(),
                form.getCode()
        );
        if (models != null && !models.isEmpty()) {
            List<TestBookTableForm> data = new ArrayList<>();
            for (HtcVisitEntity model : models) {
                data.add(convertTableBookForm(model));
            }
            form.setTable(data);
        }
        return form;
    }
    
    @GetMapping(value = {"/htc-test-book/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        List<ParameterEntity> objectTestActive = new ArrayList<>();
        for (ParameterEntity entity : parameterService.getTestObjectGroup()) {
            if (entity.getStatus() == 1) {
                objectTestActive.add(entity);
            }
        }
        TestBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, String> serviceOption = getServiceOption(null);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Sổ xét nghiệm sàng lọc HIV");
        model.addAttribute("title_small", "Sổ xét nghiệm sàng lọc HIV");
        model.addAttribute("serviceOptions", serviceOption);
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        model.addAttribute("search_object", form.getObjects().keySet().size() == objectTestActive.size() ? "" : form.getObjects().keySet().toArray(new String[form.getObjects().size()]));
        
        return "report/htc/test-book";
    }
    
    @GetMapping(value = {"/htc-test-book/excel.html"})
    public ResponseEntity<InputStreamResource> actionTestBookExcel(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        
        return exportExcel(new TestBookExcel(getData(startDate, endDate, services, objects, code), EXCEL_EXTENSION));
    }
    
    @GetMapping(value = {"/htc-test-book/email.html"})
    public String actionBookEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcTestBook());
        }
        TestBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_POSITIVE,
                context,
                new TestBookExcel(form, EXTENSION_EXCEL));
        
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcTestBook(startDate, endDate, services, objects, code));
    }
    
    @GetMapping(value = {"/htc-test-book/pdf.html"})
    public ResponseEntity<InputStreamResource> actionBookPdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        TestBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        
        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }
}
