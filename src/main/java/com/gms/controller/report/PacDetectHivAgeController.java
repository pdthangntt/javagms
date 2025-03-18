package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DetectHivAgeExcel;
import com.gms.entity.form.pac.DetectHivAgeForm;
import com.gms.entity.form.pac.TableDetectHivAgeForm;
import com.gms.repository.impl.DetectHivRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * Báo cáo người nhiễm phát hiện: nhóm tuổi
 *
 * @author TrangBN
 */
@Controller(value = "pac_detect_age")
public class PacDetectHivAgeController extends PacDetectHivController {

    private static final String TEMPLATE_DETECT_AGE_HIV_REPORT = "report/pac/detecthiv-age-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private DetectHivRepositoryImpl detectHivRepositoryImpl;

    @GetMapping(value = {"/pac-detecthiv-age/index.html"})
    public String actionDetectHivAgeIndex(Model model,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "age1", required = false, defaultValue = "") String age1,
            @RequestParam(name = "age2", required = false, defaultValue = "") String age2,
            @RequestParam(name = "age3", required = false, defaultValue = "") String age3,
            @RequestParam(name = "age4", required = false, defaultValue = "") String age4,
            @RequestParam(name = "age5", required = false, defaultValue = "") String age5,
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {
        DetectHivAgeForm form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus, dateFilter, levelDisplay, manageStt, 
                                        manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, testObject, 
                                        statusResident, race, transmision, statusTreatment, age1, age2, age3, age4, age5, placeTest, facility, hasHealthInsurance);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("isPAC", form.isPAC());
        model.addAttribute("isTYT", form.isTYT());
        model.addAttribute("isTTYT", form.isTTYT());
        model.addAttribute("isVAAC", form.isVAAC());
        model.addAttribute("title", "Báo cáo người nhiễm phát hiện");

        return "report/pac/detecthiv-age";
    }

    /**
     * Xuất danh sách excel
     * 
     * @param levelDisplay
     * @param addressFilter
     * @param dateFilter
     * @param patientStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param job
     * @param testObject
     * @param race
     * @param gender
     * @param transmision
     * @param statusTreatment
     * @param statusResident
     * @param manageStt
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param age1
     * @param age2
     * @param age3
     * @param age4
     * @param age5
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/pac-detecthiv-age/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "age1", required = false, defaultValue = "") String age1,
            @RequestParam(name = "age2", required = false, defaultValue = "") String age2,
            @RequestParam(name = "age3", required = false, defaultValue = "") String age3,
            @RequestParam(name = "age4", required = false, defaultValue = "") String age4,
            @RequestParam(name = "age5", required = false, defaultValue = "") String age5,
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance     // Nơi điều trị
    ) throws Exception {
        return exportExcel(new DetectHivAgeExcel(getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus, dateFilter, levelDisplay, manageStt, 
                                        manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, testObject, 
                                        statusResident, race, transmision, statusTreatment, age1, age2, age3, age4, age5, placeTest, facility, hasHealthInsurance), EXTENSION_EXCEL_X));
    }

    /**
     * Xuất file PDF danh sách mới phát hiện theo nhóm giới tính
     * 
     * @param levelDisplay
     * @param addressFilter
     * @param dateFilter
     * @param patientStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param job
     * @param testObject
     * @param race
     * @param gender
     * @param transmision
     * @param statusTreatment
     * @param statusResident
     * @param manageStt
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param age1
     * @param age2
     * @param age3
     * @param age4
     * @param age5
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/pac-detecthiv-age/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "age1", required = false, defaultValue = "") String age1,
            @RequestParam(name = "age2", required = false, defaultValue = "") String age2,
            @RequestParam(name = "age3", required = false, defaultValue = "") String age3,
            @RequestParam(name = "age4", required = false, defaultValue = "") String age4,
            @RequestParam(name = "age5", required = false, defaultValue = "") String age5,
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance     // Nơi điều trị
    ) throws Exception {
        
        DetectHivAgeForm form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus, dateFilter, levelDisplay, manageStt, 
                                        manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, testObject, 
                                        statusResident, race, transmision, statusTreatment, age1, age2, age3, age4, age5, placeTest, facility, hasHealthInsurance);
        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isTYT", form.isTYT());
        context.put("isPAC", form.isPAC());
        context.put("isTTYT", form.isTTYT());
        context.put("isVAAC", form.isVAAC());
        return exportPdf(form.getFileName(), TEMPLATE_DETECT_AGE_HIV_REPORT, context);
    }

    /**
     * Gửi mail đính kèm excel file
     * 
     * @param redirectAttributes
     * @param levelDisplay
     * @param addressFilter
     * @param dateFilter
     * @param patientStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param job
     * @param testObject
     * @param race
     * @param gender
     * @param transmision
     * @param statusTreatment
     * @param statusResident
     * @param manageStt
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param age1
     * @param age2
     * @param age3
     * @param age4
     * @param age5
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception 
     */
    @GetMapping(value = {"/pac-detecthiv-age/email.html"})
    public String actionEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "date_filter", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "status_alive", required = false, defaultValue = "") String patientStatus,
            @RequestParam(name = "manage_time_from", required = false, defaultValue = "") String manageTimeFrom,
            @RequestParam(name = "manage_time_to", required = false, defaultValue = "") String manageTimeTo,
            @RequestParam(name = "input_time_from", required = false, defaultValue = "") String inputTimeFrom,
            @RequestParam(name = "input_time_to", required = false, defaultValue = "") String inputTimeTo,
            @RequestParam(name = "confirm_time_from", required = false, defaultValue = "") String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false, defaultValue = "") String confirmTimeTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "race", required = false, defaultValue = "") String race,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "transmision", required = false, defaultValue = "") String transmision,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "age1", required = false, defaultValue = "") String age1,
            @RequestParam(name = "age2", required = false, defaultValue = "") String age2,
            @RequestParam(name = "age3", required = false, defaultValue = "") String age3,
            @RequestParam(name = "age4", required = false, defaultValue = "") String age4,
            @RequestParam(name = "age5", required = false, defaultValue = "") String age5,
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance     // Nơi điều trị
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacDetectHivAgeReport());
        }
        
        DetectHivAgeForm form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus, dateFilter, levelDisplay, manageStt, 
                                        manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, testObject, 
                                        statusResident, race, transmision, statusTreatment, age1, age2, age3, age4, age5, placeTest, facility, hasHealthInsurance);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s ", form.getTitle()),
                EmailoutboxEntity.TEMPLATE_PAC_AGE_HIV,
                context,
                new DetectHivAgeExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacDetectHivAgeReport());
    }

    /**
     * Data Form report
     *
     * @param districtID
     * @param wardID
     * @param addressFilter
     * @param patientStatus
     * @param dateFilter
     * @param fromTime
     * @param toTime
     * @param job
     * @param testObject
     * @param statusResident
     * @param statusTreatment
     * @param age1
     * @param age2
     * @param age3
     * @param age4
     * @param age5
     * @return
     * @throws Exception
     */
    private DetectHivAgeForm getData(String updateTimeFrom, String updateTimeTo ,String permanentProvinceID, String permanentDistrictID, String permanentWardID, String addressFilter, 
                                    String patientStatus, String dateFilter, String levelDisplay, String manageStatus,
                                    String manageTimeFrom, String manageTimeTo, String inputTimeFrom, String inputTimeTo, String confirmTimeFrom, 
                                    String confirmTimeTo,String aidsFrom, String aidsTo,String gender, String job, String testObject, String statusResident, String race, String transmission,
                                    String statusTreatment, String age1, String age2, String age3, String age4, String age5, String placeTest, String facility, String hasHealthInsurance) throws Exception {
        
        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> rowProvince = new LinkedHashMap();
        Map<String, List<WardEntity>> rowWard = new LinkedHashMap<>();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        List<String> pIDsDisplay = new ArrayList<>();
        List<String> dIDsDisplay = new ArrayList<>();
        List<String> wIDsDisplay = new ArrayList<>();
        
        // Mặc đinh hiển thị tỉnh huyện xã hiện tại title bảng
        boolean defaultLoggin = StringUtils.isEmpty(permanentProvinceID) && StringUtils.isEmpty(permanentDistrictID) && StringUtils.isEmpty(permanentWardID);
        if (defaultLoggin) {
            if (isTYT()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
                dIDsDisplay.add(currentUser.getSiteDistrict().getID());
                wIDsDisplay.add(currentUser.getSiteWard().getID());
            }
            if (isTTYT()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
                dIDsDisplay.add(currentUser.getSiteDistrict().getID());
            }
            if (isPAC()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
            }
        }
        
        //Lấy thông tin tìm kiếm tỉnh huyện xã
        if (StringUtils.isNotEmpty(permanentProvinceID) && !permanentProvinceID.equals("null")) {
            pIDs.addAll(Arrays.asList(permanentProvinceID.split(",")));
            pIDsDisplay.addAll(Arrays.asList(permanentProvinceID.split(",")));
        }
        if (StringUtils.isNotEmpty(permanentDistrictID) && !permanentDistrictID.equals("null")) {
            dIDs.addAll(Arrays.asList(permanentDistrictID.split(",")));
            dIDsDisplay.addAll(Arrays.asList(permanentDistrictID.split(",")));
        }
        if (StringUtils.isNotEmpty(permanentWardID) && !permanentWardID.equals("null")) {
            wIDs.addAll(Arrays.asList(permanentWardID.split(",")));
            wIDsDisplay.addAll(Arrays.asList(permanentWardID.split(",")));
        }
        
        //Lấy thông tin tỉnh/ huyện
        Map<String, String> provinces = new LinkedHashMap<>();
        List<ProvinceEntity> allProvinces = locationsService.findAllProvince();
        for (ProvinceEntity item : allProvinces) {
            provinces.put(item.getID(), item.getName());
        }

        Map<String, DistrictEntity> districts = new LinkedHashMap<>();
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        
        DetectHivAgeForm form = new DetectHivAgeForm();
        form.setOptions(getOptions());
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setDefaultSearchPlace(defaultLoggin);
        form.setSiteProvince(currentUser.getSiteProvince().getName().contains("Tỉnh") || currentUser.getSiteProvince().getName().contains("Thành phố") ? currentUser.getSiteProvince().getName().replace("Tỉnh", "").replace("Thành phố", "")
                : currentUser.getSiteProvince().getName().contains("Huyện") ? currentUser.getSiteProvince().getName().replace("Huyện", "")
                : currentUser.getSiteProvince().getName());
        form.setProvinceName(currentUser.getSiteProvince().getName());
        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, wIDs));
        form.setTitleLocationDisplay(super.getTitleAddress(pIDsDisplay, dIDsDisplay, wIDsDisplay));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setFileName("BaoCaoNguoiNhiemPhatHienTheoNhomTuoi_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo nhóm tuổi");
        form.setHasHealthInsurance(hasHealthInsurance);
        
        if (!form.isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo người nhiễm phát hiện theo nhóm tuổi");
                    break;
                case "1":
                    form.setTitle("Báo cáo người nhiễm phát hiện chưa rà soát theo nhóm tuổi");
                    break;
                case "2":
                    form.setTitle("Báo cáo người nhiễm phát hiện cần rà soát theo nhóm tuổi");
                    break;
                case "3":
                    form.setTitle("Báo cáo người nhiễm phát hiện đã rà soát theo nhóm tuổi");
                    break;
                case "4":
                    form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo nhóm tuổi");
                    break;
                default:
                    form.setTitle("");
            }
        }
        
        //Điều kiện tìm kiếm
        form.setAddressFilter(addressFilter);
        form.setJob(job);
        form.setGender(gender);
        form.setRace(race);
        form.setTransmision(transmission);
        form.setObject(testObject);
        form.setResident(statusResident);
        form.setTreatment(statusTreatment);
        form.setManageStatus(manageStatus);
        form.setProvince(permanentProvinceID);
        form.setDistrict(permanentDistrictID);
        form.setWard(permanentWardID);
        form.setPatientStatus(patientStatus);
        form.setStaffName(currentUser.getUser().getName());
        form.setDateFilter(dateFilter);
        form.setPlaceTest(StringUtils.isNotEmpty(placeTest) ? placeTest.trim() : placeTest);
        form.setFacility(StringUtils.isNotEmpty(facility) ? facility.trim() : facility);
        
        // Default patient status
        patientStatus = patientStatus.contains(",") ? null : patientStatus;

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
        
        age1 = validateAge(age1);
        age2 = validateAge(age2);
        age3 = validateAge(age3);
        age4 = validateAge(age4);
        age5 = validateAge(age5);

        if (StringUtils.isEmpty(age1) && StringUtils.isEmpty(age2)
                && StringUtils.isEmpty(age3)
                && StringUtils.isEmpty(age4)
                && StringUtils.isEmpty(age5)) {
            age1 = "<15";
            age2 = "15-24";
            age3 = "25-49";
            age4 = ">49";
            age5 = "";
        } else {
            form.setSearchAge1(age1);
            form.setSearchAge2(age2);
            form.setSearchAge3(age3);
            form.setSearchAge4(age4);
            form.setSearchAge5(age5);
        }

        form.setAge1(age1);
        form.setAge2(age2);
        form.setAge3(age3);
        form.setAge4(age4);
        form.setAge5(age5);

        List<WardEntity> wardsDefault = new ArrayList<>();
        WardEntity wardDefault = new WardEntity();
        //Điều kiện lọc danh sách
        if (form.isTYT()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "ward" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            form.setSiteDistrictID(currentUser.getSite().getDistrictID());
            form.setSiteWardID(currentUser.getSite().getWardID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
            // Default hiển thị 
            if (form.isDefaultSearchPlace()) {
                wardDefault = locationsService.findWard(form.getSiteWardID());
            }
            
        } else if (form.isTTYT()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "ward" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            form.setSiteDistrictID(currentUser.getSite().getDistrictID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
            // Mặc định lấy tất cả các xã trong huyện
            if (form.isDefaultSearchPlace()) {
                wardsDefault = locationsService.findWardByDistrictID(form.getSiteDistrictID());
            }
            
        } else if (form.isPAC()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "district" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
        } else {
            //VAAC
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "province" : levelDisplay;
            //Hiển thị toàn bộ tỉnh khi là cục vaac
            rowProvince.putAll(provinces);
            //Lấy toàn bộ xã khi là cục vaac khi không search và lựa chọn hiển thị xã
            if (pIDs.isEmpty() && levelDisplay.equals("ward")) {
                for (WardEntity item : locationsService.findAllWard()) {
                    if (rowWard.getOrDefault(item.getDistrictID(), null) == null) {
                        rowWard.put(item.getDistrictID(), new ArrayList<WardEntity>());
                    }
                    rowWard.get(item.getDistrictID()).add(item);
                }
            }
        }
        form.setLevelDisplay(levelDisplay);

        // Tìm kiếm theo từng nhóm tuổi
        List<TableDetectHivAgeForm> result = new ArrayList<>();

        if (StringUtils.isNotEmpty(age1)) {
            List<TableDetectHivAgeForm> tableDetectHivAge = detectHivRepositoryImpl.getTableDetectHivAge(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), 
                                                                                                        addressFilter, levelDisplay, patientStatus, dateFilter, manageStatus, 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) , 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo),
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo),
                                                                                                        gender,
                                                                                                        job, race, testObject, statusResident, statusTreatment,
                                                                                                        age1, transmission, "1",isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);

            for (TableDetectHivAgeForm tableDetectHivAgeForm : tableDetectHivAge) {
                result.add(tableDetectHivAgeForm);
            }
        }
        if (StringUtils.isNotEmpty(age2)) {
            List<TableDetectHivAgeForm> tableDetectHivAge = detectHivRepositoryImpl.getTableDetectHivAge(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), 
                                                                                                        addressFilter, levelDisplay, patientStatus, dateFilter, manageStatus, 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) , 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo),
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo),
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo),gender,
                                                                                                        job, race, testObject, statusResident, statusTreatment,
                                                                                                        age2, transmission, "2", isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);
            for (TableDetectHivAgeForm tableDetectHivAgeForm : tableDetectHivAge) {
                result.add(tableDetectHivAgeForm);
            }
        }
        if (StringUtils.isNotEmpty(age3)) {
            List<TableDetectHivAgeForm> tableDetectHivAge = detectHivRepositoryImpl.getTableDetectHivAge(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), 
                                                                                                        addressFilter, levelDisplay, patientStatus, dateFilter, manageStatus, 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) , 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo),
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo),gender,
                                                                                                        job, race, testObject, statusResident, statusTreatment,
                                                                                                        age3, transmission, "3", isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);
            for (TableDetectHivAgeForm tableDetectHivAgeForm : tableDetectHivAge) {
                result.add(tableDetectHivAgeForm);
            }
        }
        if (StringUtils.isNotEmpty(age4)) {
            List<TableDetectHivAgeForm> tableDetectHivAge = detectHivRepositoryImpl.getTableDetectHivAge(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), 
                                                                                                        addressFilter, levelDisplay, patientStatus, dateFilter, manageStatus, 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) , 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo),
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo),gender,
                                                                                                        job, race, testObject, statusResident, statusTreatment,
                                                                                                        age4, transmission, "4", isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);
            for (TableDetectHivAgeForm tableDetectHivAgeForm : tableDetectHivAge) {
                result.add(tableDetectHivAgeForm);
            }
        }
        if (StringUtils.isNotEmpty(age5)) {
            List<TableDetectHivAgeForm> tableDetectHivAge = detectHivRepositoryImpl.getTableDetectHivAge(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), 
                                                                                                        addressFilter, levelDisplay, patientStatus , dateFilter, manageStatus, 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) , 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo), 
                                                                                                        TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo),gender,
                                                                                                        job, race, testObject, statusResident, statusTreatment,
                                                                                                        age5, transmission, "5", isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);
            for (TableDetectHivAgeForm tableDetectHivAgeForm : tableDetectHivAge) {
                result.add(tableDetectHivAgeForm);
            }
        }
        
        form.setTable(new ArrayList<TableDetectHivAgeForm>());
        TableDetectHivAgeForm item;
        TableDetectHivAgeForm dItem;
        TableDetectHivAgeForm wItem;
        TableDetectHivAgeForm oItem;
        int index = 1;

        //Khởi tạo - Lấy thông tin huyện, xã khác trong trường hợp ttyt và tyt
        Set<String> resultDIDs = new HashSet<>(CollectionUtils.collect(result, TransformerUtils.invokerTransformer("getDistrictID")));
        Set<String> resultWIDs = new HashSet<>(CollectionUtils.collect(result, TransformerUtils.invokerTransformer("getWardID")));

        //Set default list - Lặp tỉnh
        for (Map.Entry<String, String> entry : rowProvince.entrySet()) {
            String pID = entry.getKey();
            String pName = entry.getValue();

            //Tìm kiếm tỉnh
            if (!pIDs.isEmpty() && !pIDs.contains(pID)) {
                continue;
            }
            item = new TableDetectHivAgeForm();
            item.setStt(index);
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<TableDetectHivAgeForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new TableDetectHivAgeForm();
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<TableDetectHivAgeForm>());
                        //Trong trương hợp VAAC => xã được lấy hết ra ở phía trên
                        List<WardEntity> wards = rowWard.getOrDefault(dItem.getDistrictID(), null);
                        if (wards == null) {
                            //Trường hợp pac, ttyt và tyt thì xã load từng phần
                            wards = locationsService.findWardByDistrictID(dItem.getDistrictID());
                        }
                        for (WardEntity ward : wards) {
                            //Tìm kiếm xã: khi tìm kiếm hoặc khi vào tuyến xã
                            if ((!wIDs.isEmpty() && !wIDs.contains(ward.getID()))
                                    || (form.getSiteWardID() != null && !(form.getSiteWardID().equals(ward.getID()) || resultWIDs.contains(ward.getID())))
                                    || (form.getSiteDistrictID() != null && !resultWIDs.contains(ward.getID()))) {
                                continue;
                            }
                            
                            wItem = new TableDetectHivAgeForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
                        }
                        
                        // Thêm vào danh sách xã mặc định cua huyện khi login
                        if (form.isTTYT() && form.isDefaultSearchPlace() && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            List<TableDetectHivAgeForm> childs = new ArrayList<>(dItem.getChilds());
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getWardID")));
                            for (WardEntity wardEntity : wardsDefault) {
                                if (!childsResult.contains(wardEntity.getID())) {
                                    wItem = new TableDetectHivAgeForm();
                                    wItem.setProvinceID(dItem.getProvinceID());
                                    wItem.setDistrictID(wardEntity.getDistrictID());
                                    wItem.setWardID(wardEntity.getID());
                                    wItem.setDisplayName(wardEntity.getName());
                                    dItem.getChilds().add(wItem);
                                }
                            }
                        }
                        
                        if (form.isTYT() && form.isDefaultSearchPlace() && wardDefault != null && form.getSiteWardID().equals(wardDefault.getID()) && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(dItem.getChilds(), TransformerUtils.invokerTransformer("getWardID")));
                            if (childsResult.isEmpty() || !childsResult.contains(wardDefault.getID())) {
                                wItem = new TableDetectHivAgeForm();
                                wItem.setProvinceID(dItem.getProvinceID());
                                wItem.setDistrictID(wardDefault.getDistrictID());
                                wItem.setWardID(wardDefault.getID());
                                wItem.setDisplayName(wardDefault.getName());
                                dItem.getChilds().add(wItem);
                            }
                        }
                    }
                    //Thêm huyện vào tỉnh
                    item.getChilds().add(dItem);
                }
            }
            //Thêm tỉnh vào form
            form.getTable().add(item);
            index++;
        }
        
        List<String> listProvince = new ArrayList<>();
        //set dữ liệu vào table - Mapping data Tỉnh
        for (TableDetectHivAgeForm row : form.getTable()) {
            for (TableDetectHivAgeForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    row.setAge1(row.getAge1() + line.getAge1());
                    row.setAge2(row.getAge2() + line.getAge2());
                    row.setAge3(row.getAge3() + line.getAge3());
                    row.setAge4(row.getAge4() + line.getAge4());
                    row.setAge5(row.getAge5() + line.getAge5());
                    listProvince.add(row.getProvinceID());
                    form.setSum(form.getSum() + line.getSum());
                }

                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (TableDetectHivAgeForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setAge1(pChild.getAge1() + line.getAge1());
                            pChild.setAge2(pChild.getAge2() + line.getAge2());
                            pChild.setAge3(pChild.getAge3() + line.getAge3());
                            pChild.setAge4(pChild.getAge4() + line.getAge4());
                            pChild.setAge5(pChild.getAge5() + line.getAge5());
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (TableDetectHivAgeForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID())) {
                                    dChild.setAge1(dChild.getAge1() + line.getAge1());
                                    dChild.setAge2(dChild.getAge2() + line.getAge2());
                                    dChild.setAge3(dChild.getAge3() + line.getAge3());
                                    dChild.setAge4(dChild.getAge4() + line.getAge4());
                                    dChild.setAge5(dChild.getAge5() + line.getAge5());
                                }
                            }
                        }
                        //#end cộng xã
                    }
                    //#end cộng huyện
                }
                //#end cộng tỉnh
            }
        }
        
        //Thêm item ngoại tỉnh khi không phải vaac
        if (!form.isVAAC()) {
            Map<String, TableDetectHivAgeForm> mOutProvince = new HashMap<>();
            for (TableDetectHivAgeForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new TableDetectHivAgeForm());
                        mOutProvince.get(line.getProvinceID()).setDisplayName(provinces.get(line.getProvinceID()));
                    }
                    oItem = mOutProvince.get(line.getProvinceID());
                    oItem.setProvinceID(line.getProvinceID());
                    oItem.setAge1(oItem.getAge1() + line.getAge1());
                    oItem.setAge2(oItem.getAge2() + line.getAge2());
                    oItem.setAge3(oItem.getAge3() + line.getAge3());
                    oItem.setAge4(oItem.getAge4() + line.getAge4());
                    oItem.setAge5(oItem.getAge5() + line.getAge5());
                    mOutProvince.put(line.getProvinceID(), oItem);
                }
            }
            if (!mOutProvince.isEmpty()) {
                TableDetectHivAgeForm outProvince = new TableDetectHivAgeForm();
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(form.getTable().size() + 1);
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (TableDetectHivAgeForm age : mOutProvince.values()) {
                    outProvince.setAge1(outProvince.getAge1() + age.getAge1());
                    outProvince.setAge2(outProvince.getAge2() + age.getAge2());
                    outProvince.setAge3(outProvince.getAge3() + age.getAge3());
                    outProvince.setAge4(outProvince.getAge4() + age.getAge4());
                    outProvince.setAge5(outProvince.getAge5() + age.getAge5());
                    listProvince.add(age.getProvinceID());
                    
                    form.setSum(form.getSum() + age.getSum());
                }
                form.getTable().add(outProvince);
            }
        }
        return form;
    }

    /**
     * Chuẩn hóa chuỗi tìm tuổi
     * 
     * @param age
     * @return 
     */
    private String validateAge(String age) {
        try {
            String[] split = age.split("-", -1);
            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);
        } catch (NumberFormatException nfe) {
            return "";
        }
        return age;
    }
    
    /**
     * Check valid date
     *
     * @param dateToValidate
     * @return
     */
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
