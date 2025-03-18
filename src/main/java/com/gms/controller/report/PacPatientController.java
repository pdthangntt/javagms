package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.PatientExcel;
import com.gms.entity.form.pac.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.service.LocationsService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
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
@Controller(value = "report_pac_patient")
public class PacPatientController extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;

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
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        HashMap<String, String> provinces = new LinkedHashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);
        HashMap<String, String> districts = new LinkedHashMap<>();
        for (DistrictEntity entity : locationsService.findAllDistrict()) {
            districts.put(entity.getID(), entity.getName());
        }
        options.put("districts", districts);
        HashMap<String, String> wards = new LinkedHashMap<>();
        for (WardEntity entity : locationsService.findAllWard()) {
            wards.put(entity.getID(), entity.getName());
        }
        options.put("wards", wards);

        return options;
    }

    @GetMapping(value = {"/pac-patient/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "hiv_id", required = false) String hivID,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "status_of_resident_id", required = false) String statusOfResidentID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo) throws Exception {

        PacPatientForm form = getData(hivID, siteConfirmID, siteTreatmentFacilityID, requestDeathTimeFrom, requestDeathTimeTo, ID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, statusOfResidentID, statusOfTreatmentID, identityCard, healthInsuranceNo, addressFilter);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("tab", tab);
        context.put("options", getOptions());
        return exportPdf("DS Nguoi Nhiem Quan Ly", "report/pac/pac-patient.html", context);

    }

    /**
     * Lấy thông tin người nhiễm
     *
     * @author pdThang
     * @param ids
     * @return
     */
    private PacPatientForm getData(String hivID, String siteConfirmID, String siteTreatmentFacilityID, String requestDeathTimeFrom, String requestDeathTimeTo, String ID, String tab, String fullname, Integer yearOfBirth, String confirmTimeFrom, String confirmTimeTo, String permanentProvinceID, String permanentDistrictID, String permanentWardID, String genderID, String statusOfResidentID, String statusOfTreatmentID, String identityCard, String healthInsuranceNo, String addressFilter) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(1);
        search.setPageSize(999999999);
        search.setRemove(false);
        if (isPAC()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
        }
        if (isTTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
        }
        if (isTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
            search.setWardID(currentUser.getSite().getWardID());
        }
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

        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setAddressFilter(addressFilter);
        search.setIsVAAC(isVAAC());
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        confirmTimeFrom = isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null;
        confirmTimeTo = isThisDateValid(confirmTimeTo) ? confirmTimeTo : null;
        requestDeathTimeFrom = isThisDateValid(requestDeathTimeFrom) ? requestDeathTimeFrom : null;
        requestDeathTimeTo = isThisDateValid(requestDeathTimeTo) ? requestDeathTimeTo : null;
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setRequestDeathTimeFrom(requestDeathTimeFrom);
        search.setRequestDeathTimeTo(requestDeathTimeTo);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        List<String> lsStatusOfRD = new ArrayList<String>();
        if (StringUtils.isNotEmpty(statusOfResidentID) && !statusOfResidentID.equals("null")) {
            lsStatusOfRD.addAll(Arrays.asList(statusOfResidentID.split(",")));
            search.setStatusOfResidentIDs(lsStatusOfRD);
        }
//        search.setStatusOfResidentID(statusOfResidentID);
        search.setStatusOfTreatmentID(statusOfTreatmentID);

        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        try {
            search.setHivID(StringUtils.isBlank(hivID) ? Long.valueOf("0") : (pacPatientService.findHivInfoByCode(hivID).getID() == null ? Long.valueOf("0") : pacPatientService.findHivInfoByCode(hivID).getID()));
        } catch (Exception e) {
            search.setHivID(Long.valueOf("-99"));
        }

        if ("new".equals(tab)) {
            search.setEarlyHiv("8");
        }
        if ("update".equals(tab)) {
            search.setHasRequest(1);
        }
        if ("alive".equals(tab)) {
            search.setDead(1);
        }
        if ("dead".equals(tab)) {
            search.setDead(2);
        }
        if ("review".equals(tab)) {
            search.setHasReview(1);
        }
        if ("other".equals(tab)) {
            search.setOther(1);
        }
        if (isPAC()) {
            if (tab.equals(PacTabEnum.DELETE.getKey())) {
                search.setRemove(true);
            }
        }
//        else if (isTTYT()) {
//            search.setPermanentDistrictID(currentUser.getSite().getDistrictID());
//        } else if (isTYT()) {
//            search.setPermanentDistrictID(currentUser.getSite().getDistrictID());
//            search.setPermanentWardID(currentUser.getSite().getWardID());
//        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findPatient(search);
        //Mã HIV Info
        HashMap<String, String> hivCode = new HashMap<>();
        Set<Long> pacIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            pacIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getID")));
            List<PacHivInfoEntity> pacHivInfos = pacPatientService.findHivInfoByIDs(pacIDs);
            for (PacHivInfoEntity hivEntity : pacHivInfos) {
                hivCode.put(String.valueOf(hivEntity.getID()), hivEntity.getCode());
            }
        }

        PacPatientForm form = new PacPatientForm();
        form.setTab(tab);
        form.setHivCodeOptions(hivCode);
        form.setSearch(search);
        form.setOptions(getOptions());
        form.setItems(pacPatientService.findPatient(form.getSearch()).getData());

        form.setTitle("Danh sách quản lý người nhiễm");
        if ("new".equals(tab)) {
            form.setTitle("Danh sách người nhiễm nhiễm mới");
        }
        if ("update".equals(tab)) {
            form.setTitle("Danh sách người nhiễm yêu cầu cập nhật thông tin");
        }
        if ("alive".equals(tab)) {
            form.setTitle("Danh sách người nhiễm còn sống");
        }
        if ("dead".equals(tab)) {
            form.setTitle("Danh sách người nhiễm tử vong");
        }
        if ("review".equals(tab)) {
            form.setTitle("Danh sách người nhiễm rà soát lại");
        }
        if ("other".equals(tab)) {
            form.setTitle("Danh sách người nhiễm tỉnh khác");
        }
        form.setFileName("DanhSachQuanLyNguoiNhiem_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setConfirmTime(StringUtils.isEmpty(confirmTimeFrom) && StringUtils.isEmpty(confirmTimeTo) ? "" : String.format("Ngày XN khẳng định %s  %s", (StringUtils.isNotEmpty(confirmTimeFrom) ? ("từ " + confirmTimeFrom) : ""), StringUtils.isNotEmpty(confirmTimeTo) ? ("đến " + confirmTimeTo) : ""));
        if (StringUtils.isNotEmpty(form.getSearch().getPermanentProvinceID())) {
            ProvinceEntity province = locationsService.findProvince(form.getSearch().getPermanentProvinceID());
            form.setPermanentProvinceName(province == null ? "" : province.getName());
        }
        if (form.getSearch().getPermanentDistrictID() != null && !form.getSearch().getPermanentDistrictID().equals("")) {
            DistrictEntity district = locationsService.findDistrict(form.getSearch().getPermanentDistrictID());
            form.setPermanentDistrictName(district == null ? "" : district.getName());
        }

        if (form.getSearch().getPermanentWardID() != null && !form.getSearch().getPermanentWardID().equals("")) {
            WardEntity ward = locationsService.findWard(form.getSearch().getPermanentWardID());
            form.setPermanentWardName(ward == null ? "" : ward.getName());
        }

        // Set ngày XNKĐ
        PacPatientInfoEntity entity = new PacPatientInfoEntity();
        if (StringUtils.isEmpty(confirmTimeFrom)) {
            entity = pacPatientService.getFirst(getCurrentUser().getSite().getProvinceID());
            if (entity == null || entity.getConfirmTime() == null) {
                entity = new PacPatientInfoEntity();
                entity.setConfirmTime(TextUtils.convertDate("01/01/1990", FORMATDATE));
            }
        }
        String start = StringUtils.isNotEmpty(confirmTimeFrom) ? confirmTimeFrom : TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE);
        String end = StringUtils.isNotEmpty(confirmTimeTo) ? confirmTimeTo : TextUtils.formatDate(new Date(), FORMATDATE);
        form.setConfirmTime(start.equals(end) ? String.format("Ngày XN khẳng định %s", start) : String.format("Ngày XN khẳng định từ %s đến %s", start, end));

        return form;
    }

    @GetMapping(value = {"/pac-patient/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "hiv_id", required = false) String hivID,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "status_of_resident_id", required = false) String statusOfResidentID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo) throws Exception {

        PacPatientForm form = getData(hivID, siteConfirmID, siteTreatmentFacilityID, requestDeathTimeFrom, requestDeathTimeTo, ID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, statusOfResidentID, statusOfTreatmentID, identityCard, healthInsuranceNo, addressFilter);
        return exportExcel(new PatientExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-patient/email.html"})
    public String actionPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "hiv_id", required = false) String hivID,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "status_of_resident_id", required = false) String statusOfResidentID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacAcceptIndex());
        }
        PacPatientForm form = getData(hivID, siteConfirmID, siteTreatmentFacilityID, requestDeathTimeFrom, requestDeathTimeTo, ID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, statusOfResidentID, statusOfTreatmentID, identityCard, healthInsuranceNo, addressFilter);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Danh sách người nhiễm quản lý ( %s)", form.getConfirmTime()),
                EmailoutboxEntity.TEMPLATE_PAC_PATIENT,
                context,
                new PatientExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacPatientIndex());
    }

    // Validate date
    private boolean isThisDateValid(String dateToValidate) {

        if (StringUtils.isEmpty(dateToValidate)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
