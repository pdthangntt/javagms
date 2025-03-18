package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.CustomerTypeEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.tt03.TT03Form;
import com.gms.entity.excel.tt03.PhuLuc02Excel;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import com.gms.service.ParameterService;
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
 *
 * @author vvthanh
 */
//@RestController
@Controller(value = "htc_export_tt03")
public class HtcPhuLuc02TT03Controller extends BaseController {

    private static String TEMPLATE_PHULUC02 = "report/tt03/phuluc02.html";
    @Value("${app.report.tt03.table02.sort}")
    private String tt03Table02Sort;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private HtcVisitRepositoryImpl htcVisitRepository;

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options;
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        addEnumOption(options, ParameterEntity.CUSTOMER_TYPE, CustomerTypeEnum.values(), "Chọn loại bệnh nhân");
        return options;
    }

    /**
     * Get Services
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
     * Data Form report
     *
     * @param quarter
     * @param year
     * @param serviceIDs
     * @return
     */
    private TT03Form getData(String fromTime, String toTime, String serviceIDs, String code, String customerType) {
        Date date = new Date();
        fromTime = fromTime == null || fromTime.equals("") ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(date), FORMATDATE) : fromTime;
        toTime = toTime == null || toTime.equals("") ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(date), FORMATDATE) : toTime;

        LoggedUser currentUser = getCurrentUser();
        TT03Form form = new TT03Form();
        form.setServices(getServiceOption(serviceIDs.split(",", -1)));
        form.setStartDate(fromTime);
        form.setEndDate(toTime);

        if (code != null && !"".equals(code)) {
            form.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        form.setCustomerType(customerType);
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setTitle("Báo cáo tư vấn xét nghiệm HIV");
        form.setFileName("PhuLuc02_TT032015TTBYT_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        ArrayList<String> services = new ArrayList<>(form.getServices().keySet());
        List<Long> siteIds = new ArrayList<>();
        siteIds.add(currentUser.getSite().getID());
        form.setTable02(htcVisitRepository.getTable02TT03(siteIds,
                TextUtils.convertDate(fromTime, FORMATDATE),
                TextUtils.convertDate(toTime, FORMATDATE), services,
                form.getCode(), form.getCustomerType()
        ));
        return form;
    }

    @GetMapping(value = {"/htc-tt03/index.html"})
    public String actionPhuluc02Index(Model model,
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        TT03Form form = getData(fromTime, toTime, service, code, customerType);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Báo cáo tư vấn xét nghiệm HIV");
        model.addAttribute("title_small", "TT03/2015/TT-BYT");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("serviceOptions", getServiceOption(null));
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        return "report/htc/phuluc02tt03";
    }

    @GetMapping(value = {"/htc-tt03/email.html"})
    public String actionPhuluc02email(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcPhuluc02());
        }
        TT03Form form = getData(fromTime, toTime, service, code, customerType);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo tư vấn xét nghiệm HIV (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_REPORT_03,
                context,
                new PhuLuc02Excel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcPhuluc02(fromTime, toTime, service));
    }

    @GetMapping(value = {"/htc-tt03/excel.html"})
    public ResponseEntity<InputStreamResource> actionPhuluc02Excel(
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        return exportExcel(new PhuLuc02Excel(getData(fromTime, toTime, service, code, customerType), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc-tt03/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPhuluc02Pdf(
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        TT03Form form = getData(fromTime, toTime, service, code, customerType);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), TEMPLATE_PHULUC02, context);
    }
}
