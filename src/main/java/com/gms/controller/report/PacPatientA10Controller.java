/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.pac.PatientExcelA10;
import com.gms.entity.form.pac.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
 * @author Admin
 */
@Controller(value = "report_pac_patient_a10")
public class PacPatientA10Controller extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;

    private static final String TEMPLATE_REPORT = "report/pac/pac-patient-a10-pdf.html";
    protected static final String PDF_FILE_NAME = "So A10/YTCS_";

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        return options;
    }

    /**
     * Lấy thông tin người nhiễm
     *
     * @author DSNAnh
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param genderID
     * @param objectGroupID
     * @param identityCard
     * @param healthInsuranceNo
     * @param page
     * @param size
     * @return PacPatientForm
     */
    private PacPatientForm getData(String fullname, Integer yearOfBirth, String confirmTimeFrom, String confirmTimeTo, String genderID, String objectGroupID, String identityCard, String healthInsuranceNo, int page, int size) {
        PacPatientForm form = new PacPatientForm();
        Date currentDate = new Date();
        LoggedUser currentUser = getCurrentUser();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        PacPatientInfoEntity entity = pacPatientService.getFirst(getCurrentUser().getSite().getProvinceID(), getCurrentUser().getSite().getDistrictID(), getCurrentUser().getSite().getWardID());
        try {
            Date startDate = StringUtils.isEmpty(confirmTimeFrom) ? (entity == null ? currentDate : entity.getConfirmTime()) : sdf.parse(confirmTimeFrom);
            form.setStartDate(TextUtils.formatDate(startDate, FORMATDATE));
        } catch (ParseException e) {
            form.setStartDate(TextUtils.formatDate(entity == null ? currentDate : entity.getConfirmTime(), FORMATDATE));
        }

        try {
            Date endDate = StringUtils.isEmpty(confirmTimeTo) ? currentDate : sdf.parse(confirmTimeTo);
            form.setEndDate(TextUtils.formatDate(endDate, FORMATDATE));
        } catch (ParseException e) {
            form.setEndDate(TextUtils.formatDate(currentDate, FORMATDATE));
        }
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setRemove(false);
        search.setProvinceID(currentUser.getSite().getProvinceID());
        search.setDistrictID(currentUser.getSite().getDistrictID());
        search.setWardID(currentUser.getSite().getWardID());
        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (healthInsuranceNo != null && !"".equals(healthInsuranceNo)) {
            search.setHealthInsuranceNo(StringUtils.normalizeSpace(healthInsuranceNo.trim()));
        }
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setConfirmTimeFrom(form.getStartDate());
        search.setConfirmTimeTo(form.getEndDate());
        search.setObjectGroupID(objectGroupID);

        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findPatientA10(search);

        List<String> pIDs = Arrays.asList(currentUser.getSite().getProvinceID().split(","));
        List<String> dIDs = Arrays.asList(currentUser.getSite().getDistrictID().split(","));
        List<String> wIDs = Arrays.asList(currentUser.getSite().getWardID().split(","));
        form.setTitleLocation(getTitleAddress(pIDs, dIDs, wIDs));

        form.setSearch(search);
        form.setOptions(getOptions());
        form.setDataPage(dataPage);
        form.setTitle("Sổ theo dõi, quản lý bệnh nhân HIV tại cộng đồng");
        form.setFileName("SoA10/YTCS_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        List<Long> confirmTime = new ArrayList<>();
        if (form.getItems() != null) {
            for (PacPatientInfoEntity model : form.getItems()) {
                if (model.getConfirmTime() != null && !confirmTime.contains(model.getConfirmTime().getTime())) {
                    confirmTime.add(model.getConfirmTime().getTime());
                }
            }
            Collections.sort(confirmTime);
            if (confirmTime.size() >= 1) {
                String start = TextUtils.formatDate(new Date(confirmTime.get(0)), FORMATDATE);
                String end = TextUtils.formatDate(new Date(confirmTime.get(confirmTime.size() - 1)), FORMATDATE);
                form.setConfirmTime(start.equals(end) ? String.format("Ngày XN khẳng định %s", start) : String.format("Ngày XN khẳng định từ %s đến %s", start, end));
            }
        }

        return form;
    }

    /**
     * Trang xem sổ A10/YTCS
     *
     * @author DSNAnh
     * @param model
     * @param redirectAttributes
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param genderID
     * @param objectGroupID
     * @param identityCard
     * @param healthInsuranceNo
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-patient-a10/index.html"})
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "gender", required = false) String genderID,
            @RequestParam(name = "test_object", required = false) String objectGroupID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {

        if (!isTYT()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng.");
            return redirect(UrlUtils.home());
        }

        PacPatientForm form = getData(fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, genderID, objectGroupID, identityCard, healthInsuranceNo, page, size);
        model.addAttribute("parent_title", "Quản lý người nhiễm");
        model.addAttribute("title", "Sổ theo dõi, quản lý bệnh nhân HIV tại cộng đồng");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        return "report/pac/pac-patient-a10";
    }

    /**
     * Gửi mail sổ A10/YTCS
     *
     * @author DSNAnh
     * @param model
     * @param redirectAttributes
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param genderID
     * @param objectGroupID
     * @param identityCard
     * @param healthInsuranceNo
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-patient-a10/email.html"})
    public String actionSendEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "gender", required = false) String genderID,
            @RequestParam(name = "test_object", required = false) String objectGroupID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacPatientA10());
        }
        PacPatientForm form = getData(fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, genderID, objectGroupID, identityCard, healthInsuranceNo, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                "Sổ A10/YTCS",
                EmailoutboxEntity.TEMPLATE_PAC_PATIENT_A10,
                context,
                new PatientExcelA10(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacPatientA10());
    }

    /**
     * Xuất excel sổ A10/YTCS
     *
     * @author DSNAnh
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param genderID
     * @param objectGroupID
     * @param identityCard
     * @param healthInsuranceNo
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-patient-a10/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "gender", required = false) String genderID,
            @RequestParam(name = "test_object", required = false) String objectGroupID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        PacPatientForm form = getData(fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, genderID, objectGroupID, identityCard, healthInsuranceNo, page, size);
        return exportExcel(new PatientExcelA10(form, EXTENSION_EXCEL_X));
    }

    /**
     * In sổ A10/YTCS
     *
     * @author DSNAnh
     * @param model
     * @param redirectAttributes
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param genderID
     * @param objectGroupID
     * @param identityCard
     * @param healthInsuranceNo
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-patient-a10/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "gender", required = false) String genderID,
            @RequestParam(name = "test_object", required = false) String objectGroupID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        PacPatientForm form = getData(fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, genderID, objectGroupID, identityCard, healthInsuranceNo, page, size);
        List<Integer> order = new LinkedList<>();
        List<PacPatientInfoEntity> list = new LinkedList<>();
        for (PacPatientInfoEntity item : form.getDataPage().getData()) {
            list.add(item);
            for (int i = 0; i < 4; i++) {
                list.add(new PacPatientInfoEntity());
            }
        }
        int count = 0;
        for (PacPatientInfoEntity item : list) {
            if (item.getID() != null) {
                count++;
                order.add(count);
            } else {
                order.add(count);
            }
        }
        form.getDataPage().setData(list);
        HashMap<String, Object> context = new HashMap<>();
        context.put("order", order);
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);

    }
}
