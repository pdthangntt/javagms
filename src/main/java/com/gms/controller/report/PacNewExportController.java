package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.excel.pac.NewExcelExport;
import com.gms.entity.form.pac.PacNewExportForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
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
 * Giám sát phát hiện -> Xuất danh sách trường hợp phát hiện
 *
 * @author Văn Thành
 */
@Controller(value = "report_pac_new_export")
public class PacNewExportController extends BaseController {

    private static final String TEMPLATE_REPORT = "report/pac/pac-new-export-pdf.html";
    protected static final String PDF_FILE_NAME = "DS Nguoi nhiem phat hien_";

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

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        return options;
    }

    private PacNewExportForm getData(String aidsFrom, String aidsTo, String code, String name, String blood, String updateTimeFrom, String updateTimeTo, String ageFilter, String patientStatus, String resident, String treament, String addressFilter, String confirmTimeFrom, String confirmTimeTo,
            String inputTimeFrom, String inputTimeTo, String deathTimeFrom, String deathTimeTo, String provinceID, String districtID, String wardID,
            String testObject, String race, String gender, String transmision, String ageStart, String ageEnd, String managestatus, String placeTest,
            String facility, String sortName, String sortType, int page, int size) throws ParseException {

        String ageFrom = "";
        String ageTo = "";

        if (StringUtils.isNotBlank(ageStart)) {
            ageFrom = ageStart.replaceAll("\\s+", "");
        }
        if (StringUtils.isNotBlank(ageEnd)) {
            ageTo = ageEnd.replaceAll("\\s+", "");
        }

        LoggedUser currentUser = getCurrentUser();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        List<String> genderIDs = new ArrayList<>();
        List<String> raceIDs = new ArrayList<>();
        List<String> testObjectIDs = new ArrayList<>();
        List<String> transmisionIDs = new ArrayList<>();
        List<String> aliveIDs = new ArrayList<>();
        List<String> residentIDs = new ArrayList<>();
        List<String> treamentIDs = new ArrayList<>();
        if (provinceID != null && !provinceID.isEmpty() && !provinceID.equals("null")) {
            pIDs.addAll(Arrays.asList(provinceID.split(",")));
        }
        if (districtID != null && !districtID.isEmpty() && !districtID.equals("null")) {
            dIDs.addAll(Arrays.asList(districtID.split(",")));
        }
        if (wardID != null && !wardID.isEmpty() && !wardID.equals("null")) {
            wIDs.addAll(Arrays.asList(wardID.split(",")));
        }
        if (gender != null && !gender.isEmpty() && !gender.equals("null")) {
            genderIDs.addAll(Arrays.asList(gender.split(",")));
        }
        if (race != null && !race.isEmpty() && !race.equals("null")) {
            raceIDs.addAll(Arrays.asList(race.split(",")));
        }
        if (testObject != null && !testObject.isEmpty() && !testObject.equals("null")) {
            testObjectIDs.addAll(Arrays.asList(testObject.split(",")));
        }
        if (transmision != null && !transmision.isEmpty() && !transmision.equals("null")) {
            transmisionIDs.addAll(Arrays.asList(transmision.split(",")));
        }
        if (treament != null && !treament.isEmpty() && !treament.equals("null")) {
            treamentIDs.addAll(Arrays.asList(treament.split(",")));
        }
        if (patientStatus != null && !patientStatus.isEmpty() && !patientStatus.equals("null")) {
            aliveIDs.addAll(Arrays.asList(patientStatus.split(",")));
        }
        if (resident != null && !resident.isEmpty() && !resident.equals("null")) {
            residentIDs.addAll(Arrays.asList(resident.split(",")));
        }

        PacNewExportForm form = new PacNewExportForm();
        form.setTitle("DANH SÁCH NGƯỜI NHIỄM PHÁT HIỆN");
        if (!isVAAC()) {
            switch (managestatus) {
                case "-1":
                    form.setTitle("Danh sách người nhiễm phát hiện ");
                    break;
                case "1":
                    form.setTitle("Danh sách người nhiễm phát hiện chưa rà soát ");
                    break;
                case "2":
                    form.setTitle("Danh sách người nhiễm phát hiện cần rà soát ");
                    break;
                case "3":
                    form.setTitle("Danh sách người nhiễm phát hiện đã rà soát ");
                    break;
                case "4":
                    form.setTitle("Danh sách người nhiễm phát hiện được quản lý ");
                    break;
                default:
                    form.setTitle("");
            }
        }
        form.setFileName("DanhSachNguoiNhiemPhatHien_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        form.setIsVAAC(isVAAC());

        // Chuẩn hóa ngày
        confirmTimeFrom = TextUtils.validDate(confirmTimeFrom) ? confirmTimeFrom : null;
        confirmTimeTo = TextUtils.validDate(confirmTimeTo) ? confirmTimeTo : null;
        aidsFrom = TextUtils.validDate(aidsFrom) ? aidsFrom : null;
        aidsTo = TextUtils.validDate(aidsTo) ? aidsTo : null;
        inputTimeFrom = TextUtils.validDate(inputTimeFrom) ? inputTimeFrom : null;
        inputTimeTo = TextUtils.validDate(inputTimeTo) ? inputTimeTo : null;
        deathTimeFrom = TextUtils.validDate(deathTimeFrom) ? deathTimeFrom : null;
        deathTimeTo = TextUtils.validDate(deathTimeTo) ? deathTimeTo : null;
        updateTimeFrom = TextUtils.validDate(updateTimeFrom) ? updateTimeFrom : null;
        updateTimeTo = TextUtils.validDate(updateTimeTo) ? updateTimeTo : null;

        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setSortName(sortName);
        search.setSortType(sortType);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setpIDs(pIDs);
        search.setdIDs(dIDs);
        search.setwIDs(wIDs);
        search.setGenderIDs(genderIDs);
        search.setRaceIDs(raceIDs);
        search.setTransmisionIDs(transmisionIDs);
        search.setTestObjectIDs(testObjectIDs);
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setAidsFrom(aidsFrom);
        search.setAidsTo(aidsTo);
        search.setInputTimeFrom(inputTimeFrom);
        search.setInputTimeTo(inputTimeTo);
        search.setUpdateTimeFrom(updateTimeFrom);
        search.setUpdateTimeTo(updateTimeTo);
        search.setDeathTimeFrom(deathTimeFrom);
        search.setDeathTimeTo(deathTimeTo);
        search.setAddressFilter(addressFilter);
        search.setIsVAAC(isVAAC());
        search.setIsPAC(isPAC());
        search.setManageStatus(managestatus);
        search.setPlaceTest(StringUtils.isNotEmpty(placeTest) ? placeTest.trim() : placeTest);
        search.setFacility(StringUtils.isNotEmpty(facility) ? facility.trim() : facility);
        search.setAgeFromParam(ageFrom);
        search.setAgeToParam(ageTo);

        //xóa dấu cách 2 đầu và giữa
        if (code != null && !"".equals(code)) {
            search.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        if (name != null && !"".equals(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        search.setBlood(blood);

        if (aliveIDs.isEmpty() || aliveIDs.size() == 2) {
            search.setPatientStatus(null);
        } else {
            for (String aliveID : aliveIDs) {
                search.setPatientStatus(aliveID);
            }
        }
        if (treamentIDs.contains("0")) {
            search.setStatusTreatmentRule("1");
        } else {
            search.setStatusTreatmentRule("0");
        }
        search.setTreamentIDs(treamentIDs);
        search.setResidentIDs(residentIDs);

        form.setManageStatus(managestatus);
        form.setSearchProvince(provinceID);
        form.setSearchDistrict(districtID);
        form.setSearchWard(wardID);

        try {
            ageFrom = StringUtils.isEmpty(ageFrom) || Integer.valueOf(ageFrom) < 0 ? "0" : ageFrom;
            ageTo = StringUtils.isNotEmpty(ageTo) && Integer.valueOf(ageTo) <= 0 ? "-1" : ageTo;
        } catch (Exception e) {
            ageFrom = "-1";
            ageTo = "-1";
        }

        search.setProvinceID(currentUser.getSite().getProvinceID());
        search.setDistrictID(currentUser.getSite().getDistrictID());
        search.setWardID(currentUser.getSite().getWardID());

        if (isVAAC()) {
            search.setProvinceID(null);
            search.setDistrictID(null);
            search.setWardID(null);
        }

        if (isPAC()) {
            search.setDistrictID(null);
            search.setWardID(null);
        }

        if (isTTYT()) {
            search.setWardID(null);
        }
        if ("xn".equals(ageFilter)) {
            search.setAgeFrom(ageFrom);
            search.setAgeTo(ageTo);
            search.setAgeSearchFrom(ageFrom);
            search.setAgeSearchTo(ageTo);

        } else {
            search.setAgeFrom("0");
            search.setAgeTo("");
            search.setCurrentAgeFrom(ageFrom);
            search.setCurrentAgeTo(ageTo);
            search.setAgeSearchFrom(ageFrom);
            search.setAgeSearchTo(ageTo);
        }
        if ("0".equals(search.getAgeSearchFrom()) && StringUtils.isEmpty(search.getAgeSearchTo())) {
            search.setAgeSearchFrom(null);
            search.setAgeSearchTo(null);
        }

        form.setSearch(search);
        form.setOptions(options);
        form.setTitleLocation(getTitleAddress(pIDs, dIDs, wIDs));
        List<String> sitepID = new ArrayList<>();
        List<String> sitedID = new ArrayList<>();
        List<String> sitewID = new ArrayList<>();

        if (isPAC()) {
            sitepID.add(currentUser.getSite().getProvinceID());
        } else if (isTTYT()) {
            sitepID.add(currentUser.getSite().getProvinceID());
            sitedID.add(currentUser.getSite().getDistrictID());
        } else if (isTYT()) {
            sitepID.add(currentUser.getSite().getProvinceID());
            sitedID.add(currentUser.getSite().getDistrictID());
            sitewID.add(currentUser.getSite().getWardID());
        }

        form.setProvinceName(StringUtils.isEmpty(getTitleAddress(pIDs, dIDs, wIDs)) && isVAAC() ? "Quốc gia" : super.getTitleAddress(sitepID, sitedID, sitewID));
        return form;
    }

    @GetMapping(value = {"/pac-new-export/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "age_type", required = false, defaultValue = "xn") String ageFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "age_from", required = false) String ageFrom,
            @RequestParam(name = "age_to", required = false) String ageTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "blood", required = false) String blood,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "sort_name", required = false, defaultValue = "") String sortName,
            @RequestParam(name = "sort_type", required = false, defaultValue = "") String sortType,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {

        PacNewExportForm form = getData(aidsFrom, aidsTo, code, name, blood, updateTimeFrom, updateTimeTo, ageFilter,
                patientStatus, statusResident, statusTreatment, addressFilter, confirmTimeFrom, confirmTimeTo,
                inputTimeFrom, inputTimeTo, deathTimeFrom, deathTimeTo, provinceID, districtID, wardID, testObject,
                race, gender, transmision, ageFrom, ageTo, manageStt, placeTest, facility, sortName, sortType, page, size);

        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findNewExports(form.getSearch());
        HashMap<String, String> allProvinces = new LinkedHashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setAllProvincesName(allProvinces);
        form.setAddressFilter(addressFilter);
        form.setTestObject(testObject);
        form.setRace(race);
        form.setTransmision(transmision);
        form.setGender(gender);
        form.setAlive(patientStatus);
        form.setResident(statusResident);
        form.setTreatment(statusTreatment);
        form.setAgeFilter(ageFilter);

        HashMap<String, String> manageStatus = new LinkedHashMap<>();
        manageStatus.put("-1", "Tất cả");
        manageStatus.put(ManageStatusEnum.NN_PHAT_HIEN.getKey(), ManageStatusEnum.NN_PHAT_HIEN.getLabel());
        manageStatus.put(ManageStatusEnum.NN_CAN_RA_SOAT.getKey(), ManageStatusEnum.NN_CAN_RA_SOAT.getLabel());
        manageStatus.put(ManageStatusEnum.NN_DA_RA_SOAT.getKey(), ManageStatusEnum.NN_DA_RA_SOAT.getLabel());
        manageStatus.put(ManageStatusEnum.NN_QUAN_LY.getKey(), ManageStatusEnum.NN_QUAN_LY.getLabel());

        HashMap<String, String> patientStatusOptions = new LinkedHashMap<>();
        patientStatusOptions.put("1", "Còn sống");
        patientStatusOptions.put("2", "Tử vong");

        model.addAttribute("parent_title", "Quản lý người nhiễm");
        model.addAttribute("title", "Danh sách người nhiễm phát hiện");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isTYT", isTYT());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("isVAAC", isVAAC());
        model.addAttribute("manageStatus", manageStatus);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("patientStatusOptions", patientStatusOptions);

        return "report/pac/pac-new-export";
    }

    @GetMapping(value = {"/pac-new-export/email.html"})
    public String actionSendEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "age_type", required = false, defaultValue = "xn") String ageFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "age_from", required = false) String ageFrom,
            @RequestParam(name = "age_to", required = false) String ageTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "blood", required = false) String blood,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "sort_name", required = false, defaultValue = "") String sortName,
            @RequestParam(name = "sort_type", required = false, defaultValue = "") String sortType,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacNewExport());
        }
        PacNewExportForm form = getData(aidsFrom, aidsTo, code, name, blood, updateTimeFrom, updateTimeTo, ageFilter, patientStatus,
                statusResident, statusTreatment, addressFilter, confirmTimeFrom, confirmTimeTo, inputTimeFrom,
                inputTimeTo, deathTimeFrom, deathTimeTo, provinceID, districtID, wardID, testObject, race, gender,
                transmision, ageFrom, ageTo, manageStt, placeTest, facility, sortName, sortType, page, size);
        HashMap<String, Object> context = new HashMap<>();
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findNewExports(form.getSearch());
        form.setItems(dataPage.getData());
        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setAllProvincesName(allProvinces);

        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                "Danh sách người nhiễm phát hiện",
                EmailoutboxEntity.TEMPLATE_PAC_NEW_EXPORT,
                context,
                new NewExcelExport(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacNewExport());
    }

    @GetMapping(value = {"/pac-new-export/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "age_type", required = false, defaultValue = "xn") String ageFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "age_from", required = false) String ageFrom,
            @RequestParam(name = "age_to", required = false) String ageTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "blood", required = false) String blood,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "sort_name", required = false, defaultValue = "") String sortName,
            @RequestParam(name = "sort_type", required = false, defaultValue = "") String sortType,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        PacNewExportForm form = getData(aidsFrom, aidsTo, code, name, blood, updateTimeFrom, updateTimeTo, ageFilter, patientStatus, statusResident,
                statusTreatment, addressFilter, confirmTimeFrom, confirmTimeTo, inputTimeFrom, inputTimeTo, deathTimeFrom,
                deathTimeTo, provinceID, districtID, wardID, testObject, race, gender, transmision, ageFrom, ageTo, manageStt,
                placeTest, facility, sortName, sortType, page, size);
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findNewExports(form.getSearch());
        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setAllProvincesName(allProvinces);
        form.setItems(dataPage.getData());
        return exportExcel(new NewExcelExport(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-new-export/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "age_type", required = false, defaultValue = "xn") String ageFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "age_from", required = false) String ageFrom,
            @RequestParam(name = "age_to", required = false) String ageTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "blood", required = false) String blood,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "sort_name", required = false, defaultValue = "") String sortName,
            @RequestParam(name = "sort_type", required = false, defaultValue = "") String sortType,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        PacNewExportForm form = getData(aidsFrom, aidsTo, code, name, blood, updateTimeFrom, updateTimeTo, ageFilter, patientStatus, statusResident,
                statusTreatment, addressFilter, confirmTimeFrom, confirmTimeTo, inputTimeFrom, inputTimeTo, deathTimeFrom,
                deathTimeTo, provinceID, districtID, wardID, testObject, race, gender, transmision, ageFrom, ageTo, manageStt,
                placeTest, facility, sortName, sortType, page, size);
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findNewExports(form.getSearch());
        HashMap<String, Object> context = new HashMap<>();
        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        form.setAllProvincesName(allProvinces);
        context.put("form", form);
        context.put("isVAAC", isVAAC());
        context.put("dataPage", dataPage);
        context.put("options", getOptions());
        context.put("config", parameterService.getSiteConfig(getCurrentUser().getSite().getID()));
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);

    }
}
