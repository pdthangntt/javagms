package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.excel.pac.OutProvinceAgeExcel;
import com.gms.entity.form.pac.PacOutProvinceAgeForm;
import com.gms.entity.form.pac.PacOutProvinceAgeTable;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
 * BC ngoai tinh
 *
 * @author pdThang
 */
@Controller(value = "pacOutProvinceAge")
public class PacOutProvinceAgeController extends BaseController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientRepositoryImpl pacPatientRepositoryImpl;

    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    private PacOutProvinceAgeForm getData(String dateFilter,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        List<String> provinceIDs = new LinkedList<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinceIDs.add(entity.getID());
        }

        PacOutProvinceAgeForm form = new PacOutProvinceAgeForm();
        form.setFileName("BaoCaoNgoaiTinhTheoNhomTuoi_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo người nhiễm ngoại tỉnh theo nhóm tuổi");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setOptions(getOptions());

        //Điều kiện tìm kiếm
        form.setJob(job);
        form.setObject(testObject);
        form.setResident(statusResident);
        form.setTreatment(statusTreatment);
        form.setTransmision(transmission);
        form.setRace(race);
        form.setPlaceTest(placeTest);
        form.setFacility(facility);

        // Chuẩn hóa ngày
        confirmTimeFrom = isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null;
        confirmTimeTo = isThisDateValid(confirmTimeTo) ? confirmTimeTo : null;
        aidsFrom = isThisDateValid(aidsFrom) ? aidsFrom : null;
        aidsTo = isThisDateValid(aidsTo) ? aidsTo : null;
        inputTimeFrom = isThisDateValid(inputTimeFrom) ? inputTimeFrom : null;
        inputTimeTo = isThisDateValid(inputTimeTo) ? inputTimeTo : null;
        manageTimeFrom = isThisDateValid(manageTimeFrom) ? manageTimeFrom : null;
        manageTimeTo = isThisDateValid(manageTimeTo) ? manageTimeTo : null;
        updateTimeFrom = isThisDateValid(updateTimeFrom) ? updateTimeFrom : null;
        updateTimeTo = isThisDateValid(updateTimeTo) ? updateTimeTo : null;

        form.setUpdateTimeFrom(updateTimeFrom);
        form.setUpdateTimeTo(updateTimeTo);
        form.setConfirmTimeFrom(confirmTimeFrom);
        form.setConfirmTimeTo(confirmTimeTo);
        form.setAidsFrom(aidsFrom);
        form.setAidsTo(aidsTo);
        form.setInputTimeFrom(inputTimeFrom);
        form.setInputTimeTo(inputTimeTo);
        form.setManageTimeFrom(manageTimeFrom);
        form.setManageTimeTo(manageTimeTo);
        form.setPatientStatus(patientStatus);
        form.setGender(gender);
        form.setDateFilter(dateFilter);

        form.setItems(pacPatientRepositoryImpl.getDataOutprovinceAge(dateFilter, provinceIDs,
                isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null,
                isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                patientStatus,
                isThisDateValid(manageTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom) : null,
                isThisDateValid(manageTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo) : null,
                isThisDateValid(inputTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom) : null,
                isThisDateValid(inputTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) : null,
                isThisDateValid(confirmTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom) : null,
                isThisDateValid(confirmTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo) : null,
                isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null,
                isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null,
                gender, job, race,
                testObject, statusResident, statusTreatment,
                transmission, placeTest, facility));

        PacOutProvinceAgeTable table = new PacOutProvinceAgeTable();
        for (Map.Entry<String, HashMap<String, PacOutProvinceAgeTable>> entry : form.getItems().entrySet()) {
            HashMap<String, PacOutProvinceAgeTable> value = entry.getValue();

            for (Map.Entry<String, PacOutProvinceAgeTable> entry1 : value.entrySet()) {
                PacOutProvinceAgeTable value1 = entry1.getValue();

                table.setUnder15(table.getUnder15() + value1.getUnder15());
                table.setFrom15to24(table.getFrom15to24() + value1.getFrom15to24());
                table.setFrom25to49(table.getFrom25to49() + value1.getFrom25to49());
                table.setOver49(table.getOver49() + value1.getOver49());
            }
        }
        form.setTable(table);

        return form;
    }

    @GetMapping(value = {"/pac-out-province-age/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus, @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            RedirectAttributes redirectAttributes) throws Exception {

        if (!isVAAC()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.backendHome());
        }

        PacOutProvinceAgeForm form = getData(dateFilter, updateTimeFrom, updateTimeTo,
                patientStatus,
                manageTimeFrom, manageTimeTo,
                inputTimeFrom, inputTimeTo,
                confirmTimeFrom, confirmTimeTo,
                aidsFrom, aidsTo,
                gender, job, race,
                testObject, statusResident, statusTreatment,
                transmision, placeTest, facility);
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("title", "Báo cáo người nhiễm ngoại tỉnh");

        return "report/pac/out-province-age";
    }

    @GetMapping(value = {"/pac-out-province-age/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus, @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility // Nơi điều trị
    ) throws Exception {
        PacOutProvinceAgeForm form = getData(dateFilter, updateTimeFrom, updateTimeTo,
                patientStatus,
                manageTimeFrom, manageTimeTo,
                inputTimeFrom, inputTimeTo,
                confirmTimeFrom, confirmTimeTo,
                aidsFrom, aidsTo,
                gender, job, race,
                testObject, statusResident, statusTreatment,
                transmision, placeTest, facility);

        return exportExcel(new OutProvinceAgeExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-out-province-age/mail.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus, @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (currentUser.getUser().getEmail() == null || "".equals(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacOutProvinceAge("index"));
        }
        PacOutProvinceAgeForm form = getData(dateFilter, updateTimeFrom, updateTimeTo,
                patientStatus,
                manageTimeFrom, manageTimeTo,
                inputTimeFrom, inputTimeTo,
                confirmTimeFrom, confirmTimeTo,
                aidsFrom, aidsTo,
                gender, job, race,
                testObject, statusResident, statusTreatment,
                transmision, placeTest, facility);

        HashMap<String, Object> context = new HashMap<>();
        context.put("options", getOptions());
        context.put("form", form);

        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo người nhiễm ngoại tỉnh"),
                EmailoutboxEntity.TEMPLATE_OUT_PROVINCE,
                context,
                new OutProvinceAgeExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacOutProvinceAge("index"));
    }

    @GetMapping(value = {"/pac-out-province-age/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus, @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility // Nơi điều trị
    ) throws Exception {
        PacOutProvinceAgeForm form = getData(dateFilter, updateTimeFrom, updateTimeTo,
                patientStatus,
                manageTimeFrom, manageTimeTo,
                inputTimeFrom, inputTimeTo,
                confirmTimeFrom, confirmTimeTo,
                aidsFrom, aidsTo,
                gender, job, race,
                testObject, statusResident, statusTreatment,
                transmision, placeTest, facility);

        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        context.put("options", getOptions());

        return exportPdf(form.getFileName(), "report/pac/out-province-age-pdf.html", context);
    }

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

        String patientStatus = "patientStatus";
        options.put(patientStatus, new HashMap<String, String>());
        options.get(patientStatus).put("", "Tất cả");
        options.get(patientStatus).put("alive", "Còn sống");
        options.get(patientStatus).put("die", "Tử vong");

        String manageStatus = "manageStatus";
        options.put(manageStatus, new LinkedHashMap<String, String>());
        options.get(manageStatus).put("-1", "Tất cả");
        options.get(manageStatus).put(ManageStatusEnum.NN_PHAT_HIEN.getKey(), ManageStatusEnum.NN_PHAT_HIEN.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_CAN_RA_SOAT.getKey(), ManageStatusEnum.NN_CAN_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_DA_RA_SOAT.getKey(), ManageStatusEnum.NN_DA_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_QUAN_LY.getKey(), ManageStatusEnum.NN_QUAN_LY.getLabel());

        HashMap<String, String> provinces = new HashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        return options;
    }

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
