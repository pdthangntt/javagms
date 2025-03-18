package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.htc.PositiveExcel;
import com.gms.entity.form.htc.PositiveForm;
import com.gms.entity.form.htc.PositiveTableForm;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
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
 * Controller xuất danh sách ca dương tính
 *
 * @author TrangBN
 */
@Controller(value = "htc_list_positive")
public class HtcPositiveController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/htc/positive-pdf.html";
    protected static final String PDF_FILE_NAME = "DS Duong Tinh Phat Hien_";

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private SiteService siteService;

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
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
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
     * Search list of the positive
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return
     */
    private PositiveForm getData(String startDate, String endDate, String services, String objects, String code) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        //Bắt thêm dịch vụ
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            services = currentUser.getUser().getServiceVisitID();
        }

        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);

        PositiveForm form = new PositiveForm();
        form.setOptions(getOptions());
        if (code != null && !"".equals(code)) {
            form.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        form.getOptions().remove(ParameterEntity.GENDER);
        form.getOptions().remove(ParameterEntity.RACE);
        form.getOptions().remove(ParameterEntity.GENDER);

        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));

        if (StringUtils.isEmpty(services) || services == null) {
            form.setServices(getServiceOption(null));
        } else {
            form.setServices(getServiceOption(services.split(",", -1)));
        }
        form.setObjects(getObjectOption(objects.split(",", -1)));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Danh sách trường hợp nhiễm HIV mới phát hiện ");
        form.setFileName("DanhSachDuongTinhPhatHien_TT092012TTBYT_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));

        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));

        List<HtcVisitEntity> models = htcVisitService.findHtcPositive(
                startDateConvert,
                endDateConvert,
                new ArrayList<>(form.getServices().keySet()),
                form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()),
                form.getSiteID(),
                form.getCode()
        );

        if (models != null && !models.isEmpty()) {
            form.setTable(convertTablePositiveForm(models));
        }
        return form;
    }

    /**
     * Convert to form display table
     *
     * @param findHtcPositive
     * @return
     */
    private List<PositiveTableForm> convertTablePositiveForm(List<HtcVisitEntity> entitys) {

        HashMap<String, HashMap<String, String>> options = getOptions();
        List<PositiveTableForm> data = new ArrayList<>();
        PositiveTableForm form = null;
        Set<Long> siteIDs = new HashSet<>();
        for (HtcVisitEntity model : entitys) {
            siteIDs.add(model.getSiteID());
            if (StringUtils.isNotBlank(model.getSiteConfirmTest())) {
                siteIDs.add(Long.parseLong(model.getSiteConfirmTest()));
            }
        }
        HashMap<Long, String> siteOptions = getSiteOption(siteIDs);

        StringBuilder sb = null;
        for (HtcVisitEntity model : entitys) {
            form = new PositiveTableForm();
            form.setPatientName(model.getPatientName());
            form.setRace(model.getRaceID() == null || model.getRaceID().equals("") ? null : options.get(ParameterEntity.RACE).getOrDefault(model.getRaceID(), null));
            form.setGender(options.get(ParameterEntity.GENDER).getOrDefault(model.getGenderID(), null));
//            form.setJob(StringUtils.isEmpty(model.getJobID()) ? StringUtils.EMPTY : model.getJobID() + ": " + options.get(ParameterEntity.JOB).getOrDefault(model.getJobID(), null));
            form.setJob(StringUtils.isEmpty(model.getJobID()) ? StringUtils.EMPTY : options.get(ParameterEntity.JOB).getOrDefault(model.getJobID(), null));
            form.setYearOfBirth(model.getYearOfBirth());
            form.setPatientID(model.getPatientID());
            form.setCurrentAddress(model.getCurrentAddressFull());
            form.setPermanentAddress(model.getPermanentAddressFull());
//            form.setModeOfTransmission(StringUtils.isEmpty(model.getModeOfTransmission()) ? StringUtils.EMPTY : model.getModeOfTransmission() + ": " + options.get(ParameterEntity.MODE_OF_TRANSMISSION).getOrDefault(model.getModeOfTransmission(), null));
            form.setModeOfTransmission(StringUtils.isEmpty(model.getModeOfTransmission()) ? StringUtils.EMPTY : options.get(ParameterEntity.MODE_OF_TRANSMISSION).getOrDefault(model.getModeOfTransmission(), null));
            form.setSiteName(getCurrentUser().getSite().getName());
            form.setConfirmTime(model.getConfirmTime() == null ? "" : TextUtils.formatDate(model.getConfirmTime(), FORMATDATE));
            if (StringUtils.isNotBlank(model.getSiteConfirmTest())) {
                form.setSiteConfirmTest(siteOptions.getOrDefault(Long.parseLong(model.getSiteConfirmTest()), null));
            }
            form.setBloodPlace(siteOptions.getOrDefault(model.getSiteID(), null));
            form.setObjectGroupID(options.get(ParameterEntity.TEST_OBJECT_GROUP).getOrDefault(model.getObjectGroupID(), ""));

//            if (StringUtils.isNotEmpty(model.getObjectGroupID())) {
//                sb = new StringBuilder();
//                sb.append(options.get(ParameterEntity.TEST_OBJECT_GROUP).getOrDefault(model.getObjectGroupID(), null));
//                if ("5.1".equals(model.getObjectGroupID()) || "5.2".equals(model.getObjectGroupID())) {
//                    sb.replace(0, 4, "");
//                }
//            }
//            form.setObjectGroupID(StringUtils.isEmpty(model.getObjectGroupID()) ? StringUtils.EMPTY : model.getObjectGroupID() + ": " + sb.toString());
            sb = new StringBuilder();
            if (model.getRiskBehaviorID() != null && !model.getRiskBehaviorID().isEmpty()) {
                for (int i = 0; i < model.getRiskBehaviorID().size(); i++) {
//                    sb.append(model.getRiskBehaviorID().get(i)).append(": ");
                    sb.append(options.get(ParameterEntity.RISK_BEHAVIOR).getOrDefault(model.getRiskBehaviorID().get(i), ""));
                    if (i < model.getRiskBehaviorID().size() - 1) {
                        sb.append("; ");
                    }

                }
            }
            form.setRiskBehaviorID(sb.toString());
            data.add(form);
        }
        return data;
    }

    @GetMapping(value = {"/htc-positive/index.html"})
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
        PositiveForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, String> serviceOption = getServiceOption(null);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Danh sách dương tính phát hiện");
        model.addAttribute("title_small", "Danh sách dương tính phát hiện");
        model.addAttribute("serviceOptions", serviceOption);
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        model.addAttribute("search_object", form.getObjects().keySet().size() == objectTestActive.size() ? "" : form.getObjects().keySet().toArray(new String[form.getObjects().size()]));
        return "report/htc/positive";

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

    @GetMapping(value = {"/htc-positive/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPositivePdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {

        PositiveForm form = getData(startDate, endDate, services, objects, code);
        form.setSiteAgency(form.getSiteAgency());

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);

    }

    @GetMapping(value = {"/htc-positive/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        return exportExcel(new PositiveExcel(getData(startDate, endDate, services, objects, code), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc-positive/email.html"})
    public String actionPositiveEmail(
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
        PositiveForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s thời gian từ ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_POSITIVE,
                context,
                new PositiveExcel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcPositiveView(startDate, endDate, services, objects));
    }

    /**
     * Get site by id
     *
     * @param entitys
     * @return
     */
    private HashMap<Long, String> getSiteOption(Set<Long> siteIs) {
        HashMap<Long, String> options = new HashMap<>();
        Set siteIDs = new HashSet();
        for (SiteEntity siteEntity : siteService.findByIDs(siteIs)) {
            siteIDs.add(siteEntity.getID());
        }
        List<SiteEntity> models = siteService.findByIDs(siteIDs);
        for (SiteEntity model : models) {
            options.put(model.getID(), model.getName());
        }
        return options;
    }

    @GetMapping(value = {"/htc-positive/print.html"})
    public String actionPrint(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        model.addAttribute("form", getData(startDate, endDate, services, objects, code));
        return print(TEMPLATE_REPORT, model);
    }
}
