package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacPatientStatusEnum;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.NewExcel;
import com.gms.entity.excel.pac.NewVaacExcel;
import com.gms.entity.excel.pac.PatientExportAIDSExcel;
import com.gms.entity.form.pac.Appendix02Form;
import com.gms.entity.form.pac.PacNewForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Giám sát phát hiện -> Phát hiện ca bệnh
 *
 * @author Văn Thành
 */
@Controller(value = "report_pac_new")
public class PacNewController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/pac/pac-new.html";
    protected static final String PDF_FILE_NAME = "DS Nguoi nhiem phat hien_";

    private static final String TEMPLATE_REPORT2 = "report/pac/pac-new-vaac-pdf.html";
    protected static final String PDF_FILE_NAME2 = "DS Trung uong yeu cau ra soat_";

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    LocationsService locationsService;

    protected HashMap<String, HashMap<String, String>> getOptions() {
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
        if (options == null || options.isEmpty()) {
            return null;
        }

        // Lọc lại danh sách Nguồn thông tin người nhiễm
        HashMap<String, String> service = options.get(ParameterEntity.SERVICE);
        if (service.isEmpty()) {
            return options;
        }

        HashMap<String, String> provinces = new HashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        HashMap<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("1", "Chưa rà soát");
        status.put("2", "Đã rà soát");
        options.put("status-review", status);

        options.put(ParameterEntity.SERVICE, new HashMap<String, String>());
        options.get(ParameterEntity.SERVICE).put("", "Chọn dịch vụ");
        options.get(ParameterEntity.SERVICE).put("100", service.get("100"));
        options.get(ParameterEntity.SERVICE).put("500", service.get("500"));
        options.get(ParameterEntity.SERVICE).put("103", service.get("103"));
        options.get(ParameterEntity.SERVICE).put("ttyt", service.get("ttyt"));
        options.get(ParameterEntity.SERVICE).put("tyt", service.get("tyt"));
        options.get(ParameterEntity.SERVICE).put("vaac", service.get("vaac"));

        addEnumOption(options, "status", PacPatientStatusEnum.values(), "Chọn trạng thái ngoại tỉnh");
        return options;
    }

    /**
     * DSNAnh Set dữ liệu vào form
     *
     * @param tab
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvinceID
     * @param genderID
     * @param identityCard
     * @param status
     * @return form
     */
    private PacNewForm getData(String ID, String siteConfirmID, String siteTreatmentFacilityID, String tab, String fullname, Integer yearOfBirth, String confirmTimeFrom, String confirmTimeTo, String searchProvinceID, String searchDetectProvinceID, String permanentDistrictID, String permanentWardID, String permanentProvinceID, String genderID, String identityCard, String status, String service, String bloodBase) {
        LoggedUser currentUser = getCurrentUser();
        PacNewForm form = new PacNewForm();
        if (tab.equals(PacTabEnum.NEW_IN_PROVINCE.getKey())) {
            form.setTitle("DANH SÁCH NGƯỜI NHIỄM PHÁT HIỆN TRONG TỈNH CHƯA RÀ SOÁT");
        } else if (tab.equals(PacTabEnum.NEW_OTHER_PROVINCE.getKey())) {
            form.setTitle("DANH SÁCH NGƯỜI NHIỄM TỈNH KHÁC PHÁT HIỆN CHƯA RÀ SOÁT");
        } else {
            form.setTitle("DANH SÁCH NGƯỜI NHIỄM PHÁT HIỆN NGOẠI TỈNH CHƯA RÀ SOÁT");
        }

        if (isVAAC()) {
            form.setTitle("DANH SÁCH NGƯỜI NHIỄM CHƯA RÀ SOÁT");
        }
        form.setIsVAAC(isVAAC());
        form.setFileName("DanhSachNguoiNhiemChuaRaSoat_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        //Thoong tin nguoi nhiem
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(1);
        search.setPageSize(999999999);

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setRemove(false);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setCurrentPermanentProvinceID(currentUser.getSite().getProvinceID());
        search.setDetectProvinceID(currentUser.getSite().getProvinceID());

        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setService(service);
        search.setBloodBase(bloodBase);
        if (tab.equals(PacTabEnum.NEW_IN_PROVINCE.getKey())) {
            //phát hiện trong tỉnh: 
            search.setPermanentProvince(1);
            search.setDetectProvince(1);
            search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        } else if (tab.equals(PacTabEnum.NEW_OTHER_PROVINCE.getKey())) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
//            search.setSearchDetectProvinceID(searchDetectProvinceID);
            search.setPermanentProvince(0);//1-Cờ này dùng để kiểm tra Tỉnh thường trú của bệnh nhân = Tỉnh của cơ sở đang đăng nhập
            search.setDetectProvince(0);//2-Cờ này dùng để kiểm tra Mã tỉnh/thành phố phát hiệN KHÁC Tỉnh của cơ sở đang đăng nhập
            search.setAcceptPermanentTime("1"); //Cở đánh dấu tỉnh khác phát hiện
        } else if (tab.equals(PacTabEnum.NEW_OUT_PROVINCE.getKey())) {
            //phát hiện ngoại tỉnh
            search.setSearchProvinceID(searchProvinceID);
//            search.setAcceptPermanentTime(status);//Trạng thái
            search.setPermanentProvince(2);//Cờ này dùng để kiểm tra Tỉnh thường trú của bệnh nhân KHÁC Tỉnh của cơ sở đang đăng nhập. 
            search.setDetectProvince(1); ////Cờ này dùng để kiểm tra Mã tỉnh/thành phố phát hiệN = Tỉnh của cơ sở đang đăng nhập
        }
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));

        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        if (isVAAC()) {
            dataPage = pacPatientService.findNewVAAC(search);
        } else {
            dataPage = pacPatientService.findNew(search, tab.equals("") ? PacTabEnum.NEW_IN_PROVINCE.getKey() : tab);
        }

        HashMap<String, String> allProvinces = new LinkedHashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setAllProvincesName(allProvinces);
        form.setItems(dataPage.getData());
        form.setSearch(search);
        form.setOptions(options);

        if (form.getSearch().getSearchDetectProvinceID() != null && !form.getSearch().getSearchDetectProvinceID().equals("")) {
            ProvinceEntity province = locationsService.findProvince(form.getSearch().getSearchDetectProvinceID());
            form.setDetectProvinceName(province == null ? "" : province.getName());
        }

        if (form.getSearch().getPermanentProvinceID() != null && !form.getSearch().getPermanentProvinceID().equals("")) {
            ProvinceEntity province = locationsService.findProvince(form.getSearch().getPermanentProvinceID());
            form.setProvinceName(province == null ? "" : province.getName());
        }

        if (form.getSearch().getPermanentDistrictID() != null && !form.getSearch().getPermanentDistrictID().equals("")) {
            DistrictEntity district = locationsService.findDistrict(form.getSearch().getPermanentDistrictID());
            form.setDistrictName(district == null ? "" : district.getName());
        }

        if (form.getSearch().getPermanentWardID() != null && !form.getSearch().getPermanentWardID().equals("")) {
            WardEntity ward = locationsService.findWard(form.getSearch().getPermanentWardID());
            form.setWardName(ward == null ? "" : ward.getName());
        }

        List<Long> confirmTime = new ArrayList<>();
        if (dataPage.getData() != null) {
            for (PacPatientInfoEntity model : dataPage.getData()) {
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

    private PacNewForm getDataVaacNew(String fullname,
            Integer yearOfBirth,
            String confirmTimeFrom,
            String confirmTimeTo,
            String permanentDistrictID,
            String permanentWardID,
            String permanentProvinceID,
            String genderID,
            String identityCard,
            String status,
            String service,
            String bloodBase,
            String siteConfirmID,
            String siteTreatmentFacilityID) {

        LoggedUser currentUser = getCurrentUser();
        PacNewForm form = new PacNewForm();
        form.setTitle("DANH SÁCH TRUNG ƯƠNG YÊU CẦU RÀ SOÁT");
        form.setIsVAAC(isVAAC());
        form.setFileName("DSTrungUongYeuCauRaSoat_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        //Thoong tin nguoi nhiem
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setProvinceID(currentUser.getSite().getProvinceID());
        search.setPageIndex(1);
        search.setPageSize(9999999);

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setConfirmTimeFrom(isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null);
        search.setConfirmTimeTo(isThisDateValid(confirmTimeTo) ? confirmTimeTo : null);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setCurrentPermanentProvinceID(currentUser.getSite().getProvinceID());
        search.setDetectProvinceID(currentUser.getSite().getProvinceID());
        search.setService(service);
        search.setBloodBase(bloodBase);

        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);

        form.setSearch(search);
        form.setOptions(options);
        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findVAACNew(search);
        form.setItems(dataPage.getData());

        // Set ngày XNKĐ
        PacPatientInfoEntity entity = new PacPatientInfoEntity();
        if (StringUtils.isEmpty(confirmTimeFrom)) {
            entity = pacPatientService.getFirst(currentUser.getSite().getProvinceID());
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

    /**
     * DSNAnh Gửi email DS người nhiễm
     *
     * @param model
     * @param redirectAttributes
     * @param tab
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvinceID
     * @param genderID
     * @param identityCard
     * @param status
     * @param service
     * @param bloodBase
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-new/email.html"})
    public String actionSendEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "new_in_province") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "search_province_id", required = false) String searchProvinceID,
            @RequestParam(name = "detect_province_id", required = false) String searchDetectProvinceID,//dùng để tìm kiếm tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacNew());
        }
        PacNewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, searchProvinceID, searchDetectProvinceID, permanentDistrictID, permanentWardID, permanentProvinceID, genderID, identityCard, status, service, bloodBase);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                "Danh sách người nhiễm chưa rà soát",
                EmailoutboxEntity.TEMPLATE_PAC_NEW,
                context,
                new NewExcel(form, EXTENSION_EXCEL_X, tab));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacNew());
    }

    @GetMapping(value = {"/pac-new/vaac-mail.html"})
    public String actionSendVaacEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacNew());
        }
        PacNewForm form = getDataVaacNew(fullname,
                yearOfBirth,
                confirmTimeFrom,
                confirmTimeTo,
                permanentDistrictID,
                permanentWardID,
                permanentProvinceID,
                genderID,
                identityCard,
                status,
                service,
                bloodBase,
                siteConfirmID,
                siteTreatmentFacilityID);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                "Trung ương yêu cầu rà soát",
                EmailoutboxEntity.TEMPLATE_PAC_NEW,
                context,
                new NewVaacExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacNew());
    }

    /**
     * DSNAnh Xuất Excel danh sách người nhiễm phát hiện
     *
     * @param tab
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvinceID
     * @param genderID
     * @param identityCard
     * @param status
     * @param service
     * @param bloodBase
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-new/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "new_in_province") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "search_province_id", required = false) String searchProvinceID,
            @RequestParam(name = "detect_province_id", required = false) String searchDetectProvinceID,//dùng để tìm kiếm tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase) throws Exception {
        PacNewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, searchProvinceID, searchDetectProvinceID, permanentDistrictID, permanentWardID, permanentProvinceID, genderID, identityCard, status, service, bloodBase);
        return exportExcel(new NewExcel(form, EXTENSION_EXCEL_X, tab));
    }

    @GetMapping(value = {"/pac-new/excel-aids.html"})
    public ResponseEntity<InputStreamResource> actionExportAIDSExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "new_in_province") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "search_province_id", required = false) String searchProvinceID,
            @RequestParam(name = "detect_province_id", required = false) String searchDetectProvinceID,//dùng để tìm kiếm tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase) throws Exception {
        PacNewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, searchProvinceID, searchDetectProvinceID, permanentDistrictID, permanentWardID, permanentProvinceID, genderID, identityCard, status, service, bloodBase);

        //Mã HIV Info
        HashMap<String, String> hivCode = new HashMap<>();
        HashMap<String, String> wardOptions = new HashMap<>();
        HashMap<String, String> wardHivInfoCodes = new HashMap<>();
        Set<Long> pacIDs = new HashSet<>();
        Set<String> wardIDs = new HashSet<>();
        if (form.getItems() != null && form.getItems().size() > 0) {
            pacIDs.addAll(CollectionUtils.collect(form.getItems(), TransformerUtils.invokerTransformer("getID")));
            wardIDs.addAll(CollectionUtils.collect(form.getItems(), TransformerUtils.invokerTransformer("getPermanentWardID")));
            wardIDs.addAll(CollectionUtils.collect(form.getItems(), TransformerUtils.invokerTransformer("getCurrentWardID")));
            for (PacHivInfoEntity hivEntity : pacPatientService.findHivInfoByIDs(pacIDs)) {
                hivCode.put(String.valueOf(hivEntity.getID()), hivEntity.getCode());
            }
            for (WardEntity wardEntity : locationsService.findWardByIDs(wardIDs)) {
                wardOptions.put(wardEntity.getID(), wardEntity.getName());
                wardHivInfoCodes.put(wardEntity.getID(), wardEntity.getHivInfoCode());
            }
        }

        form.setHivCodeOptions(hivCode);
        form.setOptions(getOptions());
        form.setsOptions(getOptionsHivinfoCode());

        form.setDistricts(new HashMap<String, String>());
        for (DistrictEntity districs : locationsService.findAllDistrict()) {
            form.getDistricts().put(districs.getID(), districs.getName());
        }
        form.setProvinces(new HashMap<String, String>());
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            form.getProvinces().put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setWards(wardOptions);
        form.setWardHivInfoCodes(wardHivInfoCodes);

        return exportExcel(new PatientExportAIDSExcel(form, EXTENSION_EXCEL_X));
    }

    private static HashMap<String, HashMap<String, String>> sOptions;

    private HashMap<String, HashMap<String, String>> getOptionsHivinfoCode() {
        if (sOptions != null) {
            return sOptions;
        }
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);

        sOptions = new HashMap<>();
        for (String item : parameterTypes) {
            if (sOptions.get(item) == null) {
                sOptions.put(item, new LinkedHashMap<String, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getHivInfoCode() == null || entity.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(entity.getType()).put(entity.getCode(), entity.getHivInfoCode());
        }

        String key = "province";
        sOptions.put(key, new HashMap<String, String>());
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getID(), item.getHivInfoCode());
        }

        key = "district";
        sOptions.put(key, new HashMap<String, String>());
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getID(), item.getHivInfoCode());
        }

        return sOptions;
    }

    @GetMapping(value = {"/pac-new/vaac-excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID) throws Exception {

        PacNewForm form = getDataVaacNew(fullname,
                yearOfBirth,
                confirmTimeFrom,
                confirmTimeTo,
                permanentDistrictID,
                permanentWardID,
                permanentProvinceID,
                genderID,
                identityCard,
                status,
                service,
                bloodBase,
                siteConfirmID,
                siteTreatmentFacilityID);

        return exportExcel(new NewVaacExcel(form, EXTENSION_EXCEL_X));
    }

    /**
     * DSNAnh In danh sách người nhiễm phát hiện
     *
     * @param model
     * @param redirectAttributes
     * @param tab
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvinceID
     * @param genderID
     * @param identityCard
     * @param status
     * @param service
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param bloodBase
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-new/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "new_in_province") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "search_province_id", required = false) String searchProvinceID,
            @RequestParam(name = "detect_province_id", required = false) String searchDetectProvinceID,//dùng để tìm kiếm tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase) throws Exception {
        PacNewForm form = getData(ID, siteConfirmID, siteTreatmentFacilityID, tab, fullname, yearOfBirth, confirmTimeFrom, confirmTimeTo, searchProvinceID, searchDetectProvinceID, permanentDistrictID, permanentWardID, permanentProvinceID, genderID, identityCard, status, service, bloodBase);
        HashMap<String, Object> context = new HashMap<>();
        context.put("tab", tab);
        form.setConfirmTime((StringUtils.isEmpty(confirmTimeFrom) || StringUtils.isEmpty(confirmTimeTo)) ? form.getConfirmTime() : confirmTimeFrom.equals(confirmTimeTo) ? String.format("Ngày XN khẳng định %s", confirmTimeTo) : String.format("Ngày XN khẳng định từ %s đến %s", confirmTimeFrom, confirmTimeTo));
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);

    }

    @GetMapping(value = {"/pac-new/vaac-pdf.html"})
    public ResponseEntity<InputStreamResource> actionVaacPdf(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID) throws Exception {

        PacNewForm form = getDataVaacNew(fullname,
                yearOfBirth,
                confirmTimeFrom,
                confirmTimeTo,
                permanentDistrictID,
                permanentWardID,
                permanentProvinceID,
                genderID,
                identityCard,
                status,
                service,
                bloodBase,
                siteConfirmID,
                siteTreatmentFacilityID);
        HashMap<String, Object> context = new HashMap<>();
        form.setConfirmTime((StringUtils.isEmpty(confirmTimeFrom) || StringUtils.isEmpty(confirmTimeTo)) ? form.getConfirmTime() : confirmTimeFrom.equals(confirmTimeTo) ? String.format("Ngày XN khẳng định %s", confirmTimeTo) : String.format("Ngày XN khẳng định từ %s đến %s", confirmTimeFrom, confirmTimeTo));
        context.put("form", form);
        context.put("items", form.getItems());
        context.put("options", getOptions());
        return exportPdf(PDF_FILE_NAME2 + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT2, context);

    }

    /**
     * In phụ lục 02 TT09
     *
     * @param oids
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-review/appendix02.html", "/pac-accept/appendix02.html", "/pac-patient/appendix02.html"})
    public ResponseEntity<InputStreamResource> actionAppendix02Sent(@RequestParam(name = "oid") String oids) throws Exception {
        Appendix02Form form = getDataAppendix(oids);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf(form.getFileName(), "report/pac/appendix-02-sent-pdf.html", context);
    }

    /**
     * Lấy thông tin in phụ lục 02
     *
     * @author TrangBN
     * @param ids
     * @return
     */
    private Appendix02Form getDataAppendix(String ids) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        LoggedUser currentUser = getCurrentUser();

        Appendix02Form form = new Appendix02Form();
        form.setTitle("BÁO CÁO GIÁM SÁT CA BỆNH ");
        form.setFileName("BCGiamSatCaBenh_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));

        if (ids == null || ids.isEmpty() || ids.equals("null")) {
            return form;
        }

        List<Long> pIDs = new ArrayList<>();
        String[] split = ids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pIDs.add(Long.parseLong(string));
        }
        List<PacPatientInfoEntity> items = pacPatientService.findAllByIds(pIDs);
        if (!items.isEmpty()) {
            List<String> cause;
            for (PacPatientInfoEntity item : items) {
                if (item.getCauseOfDeath() == null || item.getCauseOfDeath().isEmpty()) {
                    continue;
                }
                cause = new ArrayList<>();
                for (String c : item.getCauseOfDeath()) {
                    cause.add(options.get(ParameterEntity.CAUSE_OF_DEATH).getOrDefault(c, "/"));
                }
                item.setCausesOfDead(String.join(", ", cause));
            }
        }
        form.setListPatient(items);
        return form;
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
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
