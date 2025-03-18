package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.ReviewExcel;
import com.gms.entity.form.pac.PacReviewForm;
import com.gms.entity.input.PacPatientReviewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Controller(value = "report_pac_review")
public class PacReviewController extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private ParameterService parameterService;

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
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }

    @GetMapping(value = {"/pac-review/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "identity_card", required = false) String identityCard) throws Exception {

        PacReviewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, addressFilter, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, identityCard);
        HashMap<String, Object> context = new HashMap<>();
        form.setConfirmTime((StringUtils.isEmpty(confirmTimeFrom) || StringUtils.isEmpty(confirmTimeTo)) ? form.getConfirmTime() : confirmTimeFrom.equals(confirmTimeTo) ? String.format("Ngày XN khẳng định %s", confirmTimeTo) : String.format("Ngày XN khẳng định từ %s đến %s", confirmTimeFrom, confirmTimeTo));
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf("DS Nguoi Nhiem Can Ra Soat", "report/pac/pac-review.html", context);

    }

    /**
     * Lấy thông tin khách hảng gửi mẫu XN
     *
     * @author TrangBN
     * @param ids
     * @return
     */
    private PacReviewForm getData(String ID, String siteConfirmID, String siteTreatmentFacilityID, String addressFilter, String tab, String fullname, Integer yearOfBirth, String confirmTimeFrom, String confirmTimeTo, String permanentProvinceID, String permanentDistrictID, String permanentWardID, String genderID, String identityCard) {

        LoggedUser currentUser = getCurrentUser();
        PacPatientReviewSearch search = new PacPatientReviewSearch();
        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setPageIndex(1);
        search.setPageSize(999999999);
        search.setRemove(false);
        search.setFullname(fullname != null && !"".equals(fullname) ? StringUtils.normalizeSpace(fullname.trim()) : "");
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        search.setAddressFilter(addressFilter);
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        if (isPAC()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            if (tab.equals(PacTabEnum.DELETE.getKey())) {
                search.setRemove(true);
            }
        } else if (isTTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());

        } else if (isTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
            search.setWardID(currentUser.getSite().getWardID());
        }
        HashMap<String, String> provinceNames = new HashMap<>();
        for (ProvinceEntity object : locationsService.findAllProvince()) {
            provinceNames.put(object.getID(), object.getName());
        }

        PacReviewForm form = new PacReviewForm();
        form.setTitle("Danh sách người nhiễm cần rà soát tại địa phương");
        form.setFileName("DanhSachNguoiNhiemCanRaSoat_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSearch(search);
        form.setProvinceNames(provinceNames);
        form.setIsVAAC(isVAAC());
        form.setOptions(getOptions());

        if (form.getSearch().getPermanentDistrictID() != null && !form.getSearch().getPermanentDistrictID().equals("")) {
            DistrictEntity district = locationsService.findDistrict(form.getSearch().getPermanentDistrictID());
            form.setDistrictName(district == null ? "" : district.getName());
        }

        if (form.getSearch().getPermanentWardID() != null && !form.getSearch().getPermanentWardID().equals("")) {
            WardEntity ward = locationsService.findWard(form.getSearch().getPermanentWardID());
            form.setWardName(ward == null ? "" : ward.getName());
        }

        form.setItems(pacPatientService.findReview(form.getSearch()).getData());

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

    @GetMapping(value = {"/pac-review/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "identity_card", required = false) String identityCard) throws Exception {

        PacReviewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, addressFilter, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, identityCard);
        return exportExcel(new ReviewExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-review/email.html"})
    public String actionPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "identity_card", required = false) String identityCard) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacReviewIndex());
        }
        PacReviewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, addressFilter, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, permanentProvinceID, permanentDistrictID, permanentWardID, genderID, identityCard);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Danh sách người nhiễm cần rà soát tại địa phương ( %s)", form.getConfirmTime()),
                EmailoutboxEntity.TEMPLATE_PAC_REVIEW,
                context,
                new ReviewExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacReviewIndex());
    }
}
