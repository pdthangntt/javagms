package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.htc_confirm.PositiveExcel;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveForm;
import com.gms.entity.form.htc_confirm.HtcConfirmPositiveTableForm;
import com.gms.service.HtcConfirmService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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
 * @author NamAnh_HaUI
 */
@Controller(value = "htc_confirm_positive")
public class HtcConfirmPositiveController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/htc_confirm/positive-pdf.html";
    protected static final String EXTENSION_EXCEL = ".xls";

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private HtcConfirmService htcConfirmService;

    @Autowired
    SiteService siteService;

    /**
     * Lấy danh sách các nhóm đối tượng
     *
     * @return list
     */
    private HashMap<String, String> getObjectGroupOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.TEST_OBJECT_GROUP)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách kết quả XN sàng lọc
     *
     * @return list
     */
    private HashMap<String, String> getTestResultOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.TEST_RESULTS)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách cơ sở
     * @param entities
     * @return list
     */
    private HashMap<Long, String> getSite(List<HtcConfirmEntity> entities) {
        HashMap<Long, String> list = new HashMap<>();
        Set<Long> siteIDs = new HashSet<>();
        siteIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getSourceSiteID")));
        List<SiteEntity> sites = siteService.findByIDs(siteIDs);
        for (SiteEntity site : sites) {
            list.put(site.getID(), site.getName());
        }
        return list;
    }

    /**
     * Set giá trị cho form
     * @param startDate
     * @param endDate
     * @return form
     */
    private HtcConfirmPositiveForm getData(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmPositiveForm form = new HtcConfirmPositiveForm();
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Danh sách khách hàng dương tính phát hiện");
        form.setFileName("DSKHDuongTinhPhatHien_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));
        List<HtcConfirmEntity> models = htcConfirmService.findPositive(
                startDateConvert,
                endDateConvert,
                form.getSiteID());
        if (models != null && !models.isEmpty()) {
            // Lấy full địa chỉ
            models = htcConfirmService.getAddress(models);
            form.setTable(convertHtcConfirmPositiveTableForm(models));
        }
        return form;
    }
    
    
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }

    /**
     * Set dữ liệu vào bảng trong form
     *
     * @param entitys
     * @return form
     */
    private List<HtcConfirmPositiveTableForm> convertHtcConfirmPositiveTableForm(List<HtcConfirmEntity> entitys) {
        List<HtcConfirmPositiveTableForm> data = new ArrayList<>();
        HtcConfirmPositiveTableForm form;
        StringBuilder sb = null;
        for (HtcConfirmEntity model : entitys) {
            form = new HtcConfirmPositiveTableForm();
            form.setFullName(model.getFullname());
            form.setYear(model.getYear());
            form.setGenderID(model.getGenderID());
            form.setIdentityCard(model.getPatientID());
            form.setAddress(model.getAddressFull());
            if (StringUtils.isNotEmpty(model.getObjectGroupID())) {
                sb = new StringBuilder();
                sb.append(getObjectGroupOption().getOrDefault(model.getObjectGroupID(), null));
                if ("5.1".equals(model.getObjectGroupID()) || "5.2".equals(model.getObjectGroupID())) {
                    sb.replace(0, 4, "");
                }
            }
            form.setObjectGroupID(StringUtils.isEmpty(model.getObjectGroupID()) ? StringUtils.EMPTY : model.getObjectGroupID() + ": " + sb.toString());
            form.setSourceReceiveSampleTime(model.getSourceReceiveSampleTime() == null ? "" : TextUtils.formatDate(model.getSourceReceiveSampleTime(), FORMATDATE));
            form.setTestResultsID(getTestResultOption().get(model.getTestResultsID()));
            form.setVisitTestCode(model.getSourceID());
            form.setSourceSiteID(getSite(entitys).get(model.getSourceSiteID()));
            form.setSampleReceiveTime(model.getSampleReceiveTime() == null ? "" : TextUtils.formatDate(model.getSampleReceiveTime(), FORMATDATE));
            form.setConfirmTime(model.getConfirmTime() == null ? "" : TextUtils.formatDate(model.getConfirmTime(), FORMATDATE));
            form.setCode(model.getCode());
            
            form.setSourceID(model.getSourceID());
            form.setEarlyBioName(StringUtils.isEmpty(model.getEarlyBioName()) ? "" : getOptions().get(ParameterEntity.BIOLOGY_PRODUCT_TEST).get(model.getEarlyBioName()));
            form.setEarlyHivDate(model.getEarlyHivDate() == null ? "" : TextUtils.formatDate(model.getEarlyHivDate(), FORMATDATE));
            form.setEarlyHiv(StringUtils.isEmpty(model.getEarlyHiv()) ? "" : getOptions().get(ParameterEntity.EARLY_HIV).get(model.getEarlyHiv()));
            form.setVirusLoadDate(model.getVirusLoadDate() == null ? "" : TextUtils.formatDate(model.getVirusLoadDate(), FORMATDATE));
            form.setVirusLoadNumber(model.getVirusLoadNumber());
            form.setVirusLoad(StringUtils.isEmpty(model.getVirusLoad()) ? "" : getOptions().get(ParameterEntity.VIRUS_LOAD).get(model.getVirusLoad()));
            form.setEarlyDiagnose(StringUtils.isEmpty(model.getEarlyDiagnose()) ? "" : getOptions().get(ParameterEntity.EARLY_DIAGNOSE).get(model.getEarlyDiagnose()));
            
            data.add(form);
        }
        return data;
    }

    @GetMapping(value = {"/htc-confirm/positive.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        HtcConfirmPositiveForm form = getData(startDate, endDate);
        model.addAttribute("bread_crumb", "Khách hàng khẳng định dương tính");
        model.addAttribute("title", "Danh sách khách hàng dương tính phát hiện");
        model.addAttribute("form", form);
        return "report/htc_confirm/index";
    }
    

    @GetMapping(value = {"/htc-confirm/positive/pdf.html"})
    public ResponseEntity<InputStreamResource> actionHtcConfirmPositivePdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        HtcConfirmPositiveForm form = getData(startDate, endDate);
        form.setSiteAgency(form.getSiteAgency());
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(form.getFileName(), TEMPLATE_REPORT, context);
    }

    @GetMapping(value = {"/htc-confirm/positive/excel.html"})
    public ResponseEntity<InputStreamResource> actionHtcConfirmPositiveExcel(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        return exportExcel(new PositiveExcel(getData(startDate, endDate), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc-confirm/positive/email.html"})
    public String actionHtcConfirmPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.htcConfirmPositiveView());
        }
        HtcConfirmPositiveForm form = getData(startDate, endDate);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_HTC_CONFIRM_POSITIVE,
                context,
                new PositiveExcel(form, EXTENSION_EXCEL));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcConfirmPositiveView(startDate, endDate));
    }
}
