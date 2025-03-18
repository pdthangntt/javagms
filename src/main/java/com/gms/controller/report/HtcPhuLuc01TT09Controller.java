package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.CustomerTypeEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.tt09.PhuLuc01Excel;
import com.gms.entity.form.tt09.Phuluc01TT09Form;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
@Controller(value = "htc_export_tt09")
public class HtcPhuLuc01TT09Controller extends BaseController {

    @Value("${app.report.tt09.phuluc01.sort}")
    private String tt09Phuluc01Sort;

    private static String TEMPLATE_REPORT = "report/tt09/phuluc01.html";

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
     * Lấy dữ liệu báo cáo
     *
     * @param month
     * @param year
     * @param serviceIDs
     * @return
     */
    private Phuluc01TT09Form getData(int month, int year, String serviceIDs, String code, String customerType) {
        LoggedUser currentUser = getCurrentUser();
        Date firstDay = TextUtils.getFirstDayOfMonth(month, year);
        Date lastDay = TextUtils.getLastDayOfMonth(month, year);

        Phuluc01TT09Form form = new Phuluc01TT09Form();
        if (code != null && !"".equals(code)) {
            form.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        form.setCustomerType(customerType);
        form.setMonth(month);
        form.setYear(year);
        form.setServices(getServiceOption(serviceIDs.split(",", -1)));
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setTitle("BÁO CÁO KẾT QUẢ XÉT NGHIỆM HIV TRONG THÁNG");
        form.setFileName("PhuLuc01_TT092012TTBYT_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        ArrayList<String> services = new ArrayList<>(form.getServices().keySet());
        List<Long> siteID = new ArrayList<>();
        siteID.add(currentUser.getSite().getID());
        form.setTable(htcVisitRepository.getTablePhuluc01TT09(
                siteID,
                firstDay,
                lastDay,
                services,
                form.getCode(),
                form.getCustomerType()
        ));

        return form;
    }

    @GetMapping(value = {"/htc-tt09/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        //default param search
        year = year == -1 ? Calendar.getInstance().get(Calendar.YEAR) : year;
        month = month == -1 ? Calendar.getInstance().get(Calendar.MONTH) + 1 : month;

        Phuluc01TT09Form form = getData(month, year, service, code, customerType);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("title", "Báo cáo kết quả xét nghiệm HIV trong tháng");
        model.addAttribute("title_small", "TT09/2012/TT-BYT");
        model.addAttribute("serviceOptions", getServiceOption(null));
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        return "report/htc/phuluc01tt09";
    }

    @GetMapping(value = {"/htc-tt09/email.html"})
    public String actionEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcPhuluc02());
        }
        Phuluc01TT09Form form = getData(month, year, service, code, customerType);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo kết quả xét nghiệm HIV trong tháng %s năm %s", form.getMonth(), form.getYear()),
                EmailoutboxEntity.TEMPLATE_REPORT_09_PHULUC01,
                context,
                new PhuLuc01Excel(form, EXTENSION_EXCEL));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcTT09(service, month, year));
    }

    @GetMapping(value = {"/htc-tt09/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        return exportExcel(new PhuLuc01Excel(getData(month, year, service, code, customerType), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc-tt09/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        Phuluc01TT09Form form = getData(month, year, service, code, customerType);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }

    @GetMapping(value = {"/htc-tt09/print.html"})
    public String actionPrint(Model model,
            @RequestParam(name = "service", defaultValue = "") String service,
            @RequestParam(name = "month", defaultValue = "-1") int month,
            @RequestParam(name = "year", defaultValue = "-1") int year,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "customer_type", defaultValue = "") String customerType) throws Exception {
        Phuluc01TT09Form form = getData(month, year, service, code, customerType);
        model.addAttribute("form", form);
        return print(TEMPLATE_REPORT, model);
    }
}
