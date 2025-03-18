package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.excel.pac.OutProvinceExcel;
import com.gms.entity.form.pac.OutProvinceFrom;
import com.gms.entity.input.PacOutProvinceSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * @author pdThang
 */
@Controller(value = "pac_out_province_action")
public class PacOutProvinceController extends BaseController {

    @Autowired
    private PacPatientService patientService;
    @Autowired
    private PacPatientHistoryService patientHistoryService;
    @Autowired
    private LocationsService locationsService;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.TYPE_OF_PATIENT);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.SYSMPTOM);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE);
        parameterTypes.add(ParameterEntity.AIDS_STATUS);
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        HashMap<String, String> provinces = new LinkedHashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        HashMap<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("1", "Chưa rà soát");
        status.put("2", "Đã rà soát");
        options.put("status-review", status);

        return options;
    }

    private OutProvinceFrom getDataOutProvince(String tab, String provinceID, String permanentProvinceID, String currentProvinceID, String detectProvinceID, String fullname, String yearOfBirth, String genderID, String identityCard, String confirmTimeFrom, String confirmTimeTo, String bloodBaseID, String sourceServiceID, String siteConfirmID, String siteTreatmentFacilityID, String status) {

        LoggedUser currentUser = getCurrentUser();
        OutProvinceFrom form = new OutProvinceFrom();
        form.setFileName("NguoiNhiemNgoaiTinh" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Người nhiễm ngoại tỉnh");
        form.setSiteAgency(currentUser.getSite().getName());
        form.setSiteProvince(currentUser.getSiteProvince().getName());
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setOptions(getOptions());

        //Thêm điều kiện search
        PacOutProvinceSearch search = new PacOutProvinceSearch();
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);
        search.setProvinceID(provinceID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setCurrentProvinceID(currentProvinceID);
        search.setDetectProvinceID(detectProvinceID);
        search.setFullname(fullname);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setIdentityCard(identityCard);
        search.setConfirmTimeFrom(TextUtils.validDate(confirmTimeFrom) ? confirmTimeFrom : null);
        search.setConfirmTimeTo(TextUtils.validDate(confirmTimeTo) ? confirmTimeTo : null);
        search.setBloodBaseID(bloodBaseID);
        search.setSourceServiceID(sourceServiceID);
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        search.setIndex(true);
        search.setPageIndex(1);
        search.setPageSize(99999999);
        search.setRemove("remove".equals(search.getTab()));
        search.setStatus(status);

        form.setSearch(search);

        //Load dữ liệu
        form.setDataPage(patientService.findOutProvince(search));

        // Set ngày XNKĐ
        PacPatientInfoEntity entity = new PacPatientInfoEntity();
        if (StringUtils.isEmpty(confirmTimeFrom)) {
            entity = patientService.getFirst();
        }
        if (entity == null || entity.getConfirmTime() == null) {
            entity = new PacPatientInfoEntity();
            entity.setConfirmTime(TextUtils.convertDate("01/01/1990", FORMATDATE));
        }
        String start = StringUtils.isNotEmpty(confirmTimeFrom) ? confirmTimeFrom : TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE);
        String end = StringUtils.isNotEmpty(confirmTimeTo) ? confirmTimeTo : TextUtils.formatDate(new Date(), FORMATDATE);
        form.setConfirmTime(start.equals(end) ? String.format("Ngày XN khẳng định %s", start) : String.format("Ngày XN khẳng định từ %s đến %s", start, end));

        return form;
    }

    @GetMapping(value = {"/pac-out-province/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "permanent_province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "current_province_id", required = false, defaultValue = "") String currentProvinceID,
            @RequestParam(name = "detect_province_id", required = false, defaultValue = "") String detectProvinceID,
            @RequestParam(name = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "year_of_birth", required = false, defaultValue = "") String yearOfBirth,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "identity_card", required = false, defaultValue = "") String identityCard,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBaseID,
            @RequestParam(name = "service", required = false, defaultValue = "") String sourceServiceID,
            @RequestParam(name = "site_confirm", required = false, defaultValue = "") String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false, defaultValue = "") String siteTreatmentFacilityID,
            @RequestParam(name = "status", required = false, defaultValue = "") String status
    ) throws Exception {

        OutProvinceFrom form = getDataOutProvince(tab, provinceID, permanentProvinceID, currentProvinceID, detectProvinceID, fullname,
                yearOfBirth, genderID, identityCard, confirmTimeFrom, confirmTimeTo, bloodBaseID, sourceServiceID, siteConfirmID, siteTreatmentFacilityID, status);

        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        context.put("dataPage", form.getDataPage());
        context.put("options", getOptions());
        context.put("tab", tab);

        return exportPdf("DS Nguoi Nhiem Ngoai Tinh", "report/pac/out-province-pdf.html", context);

    }

    @GetMapping(value = {"/pac-out-province/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "permanent_province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "current_province_id", required = false, defaultValue = "") String currentProvinceID,
            @RequestParam(name = "detect_province_id", required = false, defaultValue = "") String detectProvinceID,
            @RequestParam(name = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "year_of_birth", required = false, defaultValue = "") String yearOfBirth,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "identity_card", required = false, defaultValue = "") String identityCard,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBaseID,
            @RequestParam(name = "service", required = false, defaultValue = "") String sourceServiceID,
            @RequestParam(name = "site_confirm", required = false, defaultValue = "") String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false, defaultValue = "") String siteTreatmentFacilityID,
            @RequestParam(name = "status", required = false, defaultValue = "") String status
    ) throws Exception {
        OutProvinceFrom form = getDataOutProvince(tab, provinceID, permanentProvinceID, currentProvinceID, detectProvinceID, fullname,
                yearOfBirth, genderID, identityCard, confirmTimeFrom, confirmTimeTo, bloodBaseID, sourceServiceID, siteConfirmID, siteTreatmentFacilityID, status);

        return exportExcel(new OutProvinceExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-out-province/mail.html"})
    public String actionPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "permanent_province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "current_province_id", required = false, defaultValue = "") String currentProvinceID,
            @RequestParam(name = "detect_province_id", required = false, defaultValue = "") String detectProvinceID,
            @RequestParam(name = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "year_of_birth", required = false, defaultValue = "") String yearOfBirth,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "identity_card", required = false, defaultValue = "") String identityCard,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBaseID,
            @RequestParam(name = "service", required = false, defaultValue = "") String sourceServiceID,
            @RequestParam(name = "site_confirm", required = false, defaultValue = "") String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false, defaultValue = "") String siteTreatmentFacilityID,
            @RequestParam(name = "status", required = false, defaultValue = "") String status
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacOutProvinceIndex());
        }
        OutProvinceFrom form = getDataOutProvince(tab, provinceID, permanentProvinceID, currentProvinceID, detectProvinceID, fullname,
                yearOfBirth, genderID, identityCard, confirmTimeFrom, confirmTimeTo, bloodBaseID, sourceServiceID, siteConfirmID, siteTreatmentFacilityID, status);

        HashMap<String, Object> context = new HashMap<>();
        form.setTitle("Danh sách người nhiễm ngoại tỉnh");
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Danh sách người nhiễm ngoại tỉnh ( %s)", form.getConfirmTime()),
                EmailoutboxEntity.TEMPLATE_PAC_OUT_PROVINCE,
                context,
                new OutProvinceExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacOutProvinceIndex());
    }

}
