package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.htc.TransferSuccessExcel;
import com.gms.entity.form.htc.TransferSuccessForm;
import com.gms.entity.form.htc.TransferSuccessTableForm;
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
 * @author NamAnh_HaUI
 */
@Controller(value = "htc_transfer_success")
public class HtcTransferSuccessController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/htc/transfer-success-pdf.html";
    protected static final String EXTENSION_EXCEL = ".xlsx";
    protected static final String PDF_FILE_NAME = "DSKH CGDT Thanh Cong_";

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
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }

    /**
     * Lấy danh sách các loại giới tính
     *
     * @return list
     */
    private HashMap<String, String> getGenderOption() {
        HashMap<String, String> list = new HashMap<>();
        for (ParameterEntity entity : parameterService.findByType(ParameterEntity.GENDER)) {
            list.put(entity.getCode(), entity.getValue());
        }
        return list;
    }

    /**
     * Lấy danh sách các loại hình dịch vụ
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return list
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
     * Set dữ liệu vào form
     *
     * @param startDate
     * @param endDate
     * @param services
     * @return form
     */
    private TransferSuccessForm getData(String startDate, String endDate, String services, String objects, String code) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date startDateConvert = StringUtils.isEmpty(startDate) ? TextUtils.getFirstDayOfMonth(new Date()) : sdf.parse(startDate);
        Date endDateConvert = StringUtils.isEmpty(endDate) ? TextUtils.getLastDayOfMonth(new Date()) : sdf.parse(endDate);
        LoggedUser currentUser = getCurrentUser();
        //Bắt thêm dịch vụ
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            services = currentUser.getUser().getServiceVisitID();
        }
        
        TransferSuccessForm form = new TransferSuccessForm();
        if (code != null && !"".equals(code)) {
            form.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        form.setSiteID(currentUser.getSite().getID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setServices(getServiceOption(services.split(",", -1)));
        form.setObjects(getObjectOption(objects.split(",", -1)));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(getCurrentUser().getUser().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("Danh sách khách hàng được chuyển gửi điều trị thành công");
        form.setFileName("DanhSachKhachHangChuyenGuiThanhCong_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStart(TextUtils.formatDate(startDateConvert, FORMATDATE));
        form.setEnd(TextUtils.formatDate(endDateConvert, FORMATDATE));

        List<HtcVisitEntity> models = htcVisitService.findTransferSuccess(
                startDateConvert,
                endDateConvert,
                new ArrayList<>(form.getServices().keySet()),
                form.getObjects().keySet().size() == parameterService.getTestObjectGroup().size() ? null : new ArrayList<>(form.getObjects().keySet()),
                form.getSiteID(),
                form.getCode()
        );
        if (models != null && !models.isEmpty()) {
            form.setTable(convertTransferSuccessTableForm(models));
        }
        return form;
    }

    /**
     * Set dữ liệu vào bảng trong form
     *
     * @param entitys
     * @return form
     */
    private List<TransferSuccessTableForm> convertTransferSuccessTableForm(List<HtcVisitEntity> entitys) {
        List<TransferSuccessTableForm> data = new ArrayList<>();
        TransferSuccessTableForm form;
        for (HtcVisitEntity model : entitys) {
            form = new TransferSuccessTableForm();
            form.setPatientID(model.getCode());
            form.setPatientName(model.getPatientName());
            form.setYearOfBirth(model.getYearOfBirth());
            form.setGenderID(getGenderOption().get(model.getGenderID()));
            form.setCurrentAddress(model.getCurrentAddressFull());
            form.setPermanentAddress(model.getPermanentAddressFull());
            form.setPhone(model.getPatientPhone());
            form.setArrivalSite(model.getArrivalSite());
            form.setTherapyNo(model.getTherapyNo());
            form.setRegisteredTherapySite(model.getRegisteredTherapySite());
            form.setExchangeTime(model.getExchangeTime() == null ? "" : TextUtils.formatDate(model.getExchangeTime(), FORMATDATE));
            form.setRegisterTherapyTime(model.getRegisterTherapyTime() == null ? "" : TextUtils.formatDate(model.getRegisterTherapyTime(), FORMATDATE));
            data.add(form);
        }
        return data;
    }

    @GetMapping(value = {"/htc/transfer-success.html"})
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
        TransferSuccessForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, String> serviceOption = getServiceOption(null);
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("small_title", "Danh sách dương tính phát hiện");
        model.addAttribute("title", "Danh sách chuyển gửi điều trị thành công");
        model.addAttribute("serviceOptions", serviceOption);
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("search_service", form.getServices().keySet().toArray(new String[form.getServices().size()]));
        model.addAttribute("search_object", form.getObjects().keySet().size() == objectTestActive.size() ? "" : form.getObjects().keySet().toArray(new String[form.getObjects().size()]));
        return "report/htc/transfer-success";
    }

    @GetMapping(value = {"/htc/transfer-success/pdf.html"})
    public ResponseEntity<InputStreamResource> actionTransferSuccessPdf(Model model,
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        TransferSuccessForm form = getData(startDate, endDate, services, objects, code);
        form.setSiteAgency(form.getSiteAgency());
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);
    }

    @GetMapping(value = {"/htc/transfer-success/excel.html"})
    public ResponseEntity<InputStreamResource> actionTransferSuccessExcel(
            @RequestParam(name = "start", defaultValue = "") String startDate,
            @RequestParam(name = "end", defaultValue = "") String endDate,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "code", defaultValue = "") String code,
            @RequestParam(name = "objects", defaultValue = "") String objects) throws Exception {
        return exportExcel(new TransferSuccessExcel(getData(startDate, endDate, services, objects, code), EXTENSION_EXCEL));
    }

    @GetMapping(value = {"/htc/transfer-success/email.html"})
    public String actionTransferSuccessEmail(
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
        TransferSuccessForm form = getData(startDate, endDate, services, objects, code);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s ( %s - %s )", form.getTitle(), form.getStart(), form.getEnd()),
                EmailoutboxEntity.TEMPLATE_REPORT_TRANSFER_SUCCESS,
                context,
                new TransferSuccessExcel(form, EXTENSION_EXCEL));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.htcTransferSuccessView(startDate, endDate, services, objects));
    }
}
