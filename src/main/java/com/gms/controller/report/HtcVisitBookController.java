package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.ExchangeServiceEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.htc.VisitBookExcel;
import com.gms.entity.form.htc.VisitBookForm;
import com.gms.entity.form.htc.VisitBookTableForm;
import com.gms.service.HtcVisitService;
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
import org.springframework.beans.factory.annotation.Value;
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
 * @author TrangBN
 */
@Controller(value = "htc_visit_book")
public class HtcVisitBookController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/htc/book-pdf.html";
    private static final String EXCEL_EXTENSION = ".xlsx";

    @Value("${app.report.htc.book.object_group}")
    private String objectGroup;

    @Autowired
    private HtcVisitService htcVisitService;

    /**
     * Convert to form for display table
     *
     * @param findHtcPositive
     * @return
     */
    private VisitBookTableForm convertTableBookForm(HtcVisitEntity entity) {
        // Get list of object
        String[] split = StringUtils.split(objectGroup, ',');
        List<String> listCodeObject = Arrays.asList(split);

        HashMap<String, HashMap<String, String>> options = getOptions();

        VisitBookTableForm form = new VisitBookTableForm();
        form.setAdvisoryeTime(TextUtils.formatDate(entity.getAdvisoryeTime(), FORMATDATE));
        form.setCode(entity.getCode());
        form.setPatientName(entity.getPatientName());
        form.setYearOfBirth(entity.getYearOfBirth());
        form.setGender(entity.getGenderID());
        form.setPermanentAddress(entity.getPermanentAddressFull());
        form.setIsAgreePreTest("1".equals(entity.getIsAgreePreTest()));
        form.setIsAgreeTest(entity.getIsAgreeTest());
        form.setTestResultsID(entity.getTestResultsID());
        form.setConfirmResult(options.get(ParameterEntity.TEST_RESULTS_CONFIRM).getOrDefault(entity.getConfirmResultsID(), null));
        form.setConfirmResultID(entity.getConfirmResultsID());
        form.setObjectGroupID(StringUtils.isNotEmpty(entity.getObjectGroupID()) && listCodeObject.contains(entity.getObjectGroupID()) ? entity.getObjectGroupID() : "7");
        if(entity.getTestResultsID() != null && TestResultEnum.KHONG_PHAN_UNG.getKey().equals(entity.getTestResultsID()) || TestResultEnum.KHONG_RO.getKey().equals(entity.getTestResultsID())){
            form.setResultsPatienTime(TextUtils.formatDate(entity.getResultsTime(), FORMATDATE));
        }
        if (entity.getConfirmResultsID() != null && entity.getConfirmResultsID().equals(ConfirmTestResultEnum.DUONG_TINH)) {
            form.setResultsPatienTime(TextUtils.formatDate(entity.getResultsPatienTime(), FORMATDATE));
        }
        form.setPatientID(entity.getPatientID());
        if (!StringUtils.isEmpty(entity.getArrivalSite())) {
            form.setServiceAfterConsult("Chuyển gửi tới OPC " + entity.getArrivalSite());
        } else {
            form.setServiceAfterConsult("");
        }

        form.setStaffAfter(entity.getStaffAfterID());
        form.setStaffBeforeTest(entity.getStaffBeforeTestID());
        form.setExchangeService(ExchangeServiceEnum.KHAC.getKey().equals(entity.getExchangeService()) ? entity.getExchangeServiceName()
                : (entity.getExchangeService() != null && entity.getExchangeService().equals("") ? "" : options.get(ParameterEntity.EXCHANGE_SERVICE).getOrDefault(entity.getExchangeService(), null)));
        return form;
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

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.EXCHANGE_SERVICE);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        addEnumOption(options, ParameterEntity.EXCHANGE_SERVICE, ExchangeServiceEnum.values(), "Chọn dịch vụ tư vấn chuyển gửi");
        return options;
    }

    /**
     * Search list of the positive
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return
     */
    private VisitBookForm getData(String startDate, String endDate, String services, String objects, String code) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        //Bắt thêm dịch vụ
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            services = currentUser.getUser().getServiceVisitID();
        }
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);

        VisitBookForm form = new VisitBookForm();
        form.setOptions(getOptions());
        form.getOptions().remove(ParameterEntity.GENDER);
        form.getOptions().remove(ParameterEntity.RACE);
        form.getOptions().remove(ParameterEntity.GENDER);

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
        form.setTitle("Sổ tư vấn xét nghiệm HIV");
        form.setFileName("SoTVXN_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));

        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));

        List<HtcVisitEntity> models = htcVisitService.findHtcVisitBook(
                startDateConvert,
                endDateConvert,
                new ArrayList<>(form.getServices().keySet()),
                form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()),
                form.getSiteID(),
                form.getCode()
        );
        if (models != null && !models.isEmpty()) {
            List<VisitBookTableForm> data = new ArrayList<>();
            for (HtcVisitEntity model : models) {
                data.add(convertTableBookForm(model));
            }
            form.setTable(data);
        }
        return form;
    }

    //*************************
    //        ACTION
    //*************************
    /**
     * Home page xuất sổ tư vấn xét nghiệm
     *
     * @param model
     * @param startDate
     * @param endDate
     * @param services
     * @param code
     * @param objects
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-book/index.html"})
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
        VisitBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, String> serviceOption = getServiceOption(null);

        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Sổ tư vấn xét nghiệm");
        model.addAttribute("title_small", "Sổ tư vấn xét nghiệm");
        model.addAttribute("serviceOptions", serviceOption);
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        model.addAttribute("search_object", form.getObjects().keySet().size() == objectTestActive.size() ? "" : form.getObjects().keySet().toArray(new String[form.getObjects().size()]));
        return "report/htc/visitbook";

    }

    @GetMapping(value = {"/htc-book/excel.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookExcel(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {

        return exportExcel(new VisitBookExcel(getData(startDate, endDate, services, objects, code), EXCEL_EXTENSION));
    }

    /**
     * Action gửi mail
     *
     * @param redirectAttributes
     * @param startDate
     * @param endDate
     * @param services
     * @param code
     * @param objects
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/htc-book/email.html"})
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
            return redirect(UrlUtils.htcPositiveView());
        }
        VisitBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_POSITIVE,
                context,
                new VisitBookExcel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcVisitBook(startDate, endDate, services, code));
    }

    @GetMapping(value = {"/htc-book/pdf.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookPdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        VisitBookForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }

}
