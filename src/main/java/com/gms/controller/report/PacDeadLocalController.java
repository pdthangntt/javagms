package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DeadLocalExcel;
import com.gms.entity.form.pac.PacDeadLocalForm;
import com.gms.entity.form.pac.TablePacDeadForm;
import com.gms.repository.impl.PacPatientRepositoryImpl;
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
 * @author DSNAnh
 */
@Controller(value = "pac_dead_local")
public class PacDeadLocalController extends BaseController {

    private static String TEMPLATE_DEAD_REPORT = "report/pac/pac-dead-local-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";
    @Autowired
    PacPatientService pacPatientService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PacPatientRepositoryImpl patientRepositoryImpl;

    @GetMapping(value = {"/pac-dead-local/index.html"})
    public String actionReportDeadIndex(Model model,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "age_from", required = false, defaultValue = "") String ageFrom,
            @RequestParam(name = "age_to", required = false, defaultValue = "") String ageTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt) throws Exception {
        
        PacDeadLocalForm form = getData(requestDeathTimeFrom, requestDeathTimeTo, updateTimeFrom, updateTimeTo ,provinceID, districtID, wardID, addressFilter, manageStt, levelDisplay, deathTimeFrom, deathTimeTo, ageFrom, ageTo,aidsFrom,aidsTo, job, gender, testObject, statusResident, statusTreatment);
        form.setPrintable(false);
        model.addAttribute("parent_title", "Quản lý người nhiễm");
        model.addAttribute("title", "Báo cáo tử vong");
        model.addAttribute("form", form);
        model.addAttribute("options", form.getOptions());
        model.addAttribute("isVAAC", form.isVAAC());
        model.addAttribute("isPAC", form.isPAC());
        model.addAttribute("isTTYT", form.isTTYT());
        model.addAttribute("isTYT", form.isTYT());

        return "report/pac/report_dead";
    }
    
    @GetMapping(value = {"/pac-dead-local/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "age_from", required = false, defaultValue = "") String ageFrom,
            @RequestParam(name = "age_to", required = false, defaultValue = "") String ageTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt) throws Exception {
        
        PacDeadLocalForm form = getData(requestDeathTimeFrom, requestDeathTimeTo,updateTimeFrom, updateTimeTo ,provinceID, districtID, wardID, addressFilter, manageStt, levelDisplay, deathTimeFrom, deathTimeTo, ageFrom, ageTo,aidsFrom,aidsTo, job, gender, testObject, statusResident, statusTreatment);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isTTYT", isTTYT());
        context.put("isPAC", isPAC());
        context.put("isTYT", isTYT());
        context.put("isVAAC", isVAAC());
        return exportPdf(form.getFileName(), TEMPLATE_DEAD_REPORT, context);
    }

    @GetMapping(value = {"/pac-dead-local/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "age_from", required = false, defaultValue = "") String ageFrom,
            @RequestParam(name = "age_to", required = false, defaultValue = "") String ageTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt) throws Exception {

        return exportExcel(new DeadLocalExcel(getData(requestDeathTimeFrom, requestDeathTimeTo,updateTimeFrom, updateTimeTo ,provinceID, districtID, wardID, addressFilter, manageStt, levelDisplay, deathTimeFrom, deathTimeTo, ageFrom, ageTo,aidsFrom,aidsTo, job, gender, testObject, statusResident, statusTreatment), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-dead-local/email.html"})
    public String actionPositiveEmail(RedirectAttributes redirectAttributes,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "death_time_from", required = false, defaultValue = "") String deathTimeFrom,
            @RequestParam(name = "death_time_to", required = false, defaultValue = "") String deathTimeTo,
            @RequestParam(name = "age_from", required = false, defaultValue = "") String ageFrom,
            @RequestParam(name = "age_to", required = false, defaultValue = "") String ageTo,
            @RequestParam(name = "aids_from", required = false, defaultValue = "") String aidsFrom,
            @RequestParam(name = "aids_to", required = false, defaultValue = "") String aidsTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String provinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String districtID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String wardID,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "gender", required = false, defaultValue = "") String gender,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.deadReport());
        }
        PacDeadLocalForm form = getData(requestDeathTimeFrom, requestDeathTimeTo,updateTimeFrom, updateTimeTo , provinceID, districtID, wardID, addressFilter, manageStt, levelDisplay, deathTimeFrom, deathTimeTo, ageFrom, ageTo,aidsFrom,aidsTo, job, gender, testObject, statusResident, statusTreatment);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s (Từ %s đến ngày %s)", form.getTitle(), form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.TEMPLATE_PAC_DEAD_LOCAL,
                context,
                new DeadLocalExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.deadReport());
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
    
    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        String manageStatus = "manageStatus";
        options.put(manageStatus, new LinkedHashMap<String, String>());
        options.get(manageStatus).put("-1", "Tất cả");
        options.get(manageStatus).put(ManageStatusEnum.NN_PHAT_HIEN.getKey(), ManageStatusEnum.NN_PHAT_HIEN.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_CAN_RA_SOAT.getKey(), ManageStatusEnum.NN_CAN_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_DA_RA_SOAT.getKey(), ManageStatusEnum.NN_DA_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_QUAN_LY.getKey(), ManageStatusEnum.NN_QUAN_LY.getLabel());
        
        return options;
    }

    
    private PacDeadLocalForm getData(String requestDeathTimeFrom, String requestDeathTimeTo,String updateTimeFrom, String updateTimeTo ,String permanentProvinceID, String permanentDistrictID, String permanentWardID,
            String addressFilter, String manageStatus, String levelDisplay, 
            String deathTimeFrom, String deathTimeTo, String ageFrom, String ageTo,String aidsFrom, String aidsTo, 
            String job, String gender, String testObject, String statusResident, String statusTreatment) throws Exception {
        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        PacDeadLocalForm form = new PacDeadLocalForm();
        
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

        form.setOptions(getOptions());
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setDefaultSearchPlace(defaultLoggin);
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setProvinceName(currentUser.getSiteProvince().getName());
        form.setDistrictName(districts.get(currentUser.getSite().getDistrictID()).getName());
        form.setWardName(locationsService.findWard(currentUser.getSite().getWardID()).getName());
        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, wIDs));
        form.setTitleLocationDisplay(super.getTitleAddress(pIDsDisplay, dIDsDisplay, wIDsDisplay));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setFileName("BCTuVong_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo tử vong được quản lý");

        if (!form.isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo tử vong");
                    break;
                case "1":
                    form.setTitle("Báo cáo tử vong chưa rà soát");
                    break;
                case "2":
                    form.setTitle("Báo cáo tử vong cần rà soát");
                    break;
                case "3":
                    form.setTitle("Báo cáo tử vong đã rà soát");
                    break;
                case "4":
                    form.setTitle("Báo cáo tử vong được quản lý");
                    break;
                default:
                    form.setTitle("");
            }
        }

        //Điều kiện tìm kiếm
        form.setAddressFilter(addressFilter);
        form.setJob(job);
        form.setGender(gender);
        form.setObject(testObject);
        form.setResident(statusResident);
        form.setTreatment(statusTreatment);
        form.setManageStatus(manageStatus);
        form.setProvince(permanentProvinceID);
        form.setDistrict(permanentDistrictID);
        form.setWard(permanentWardID);
        form.setStaffName(currentUser.getUser().getName());

        // Chuẩn hóa ngày
        deathTimeFrom = isThisDateValid(deathTimeFrom) ? deathTimeFrom : null;
        deathTimeTo = isThisDateValid(deathTimeTo) ? deathTimeTo : null;
        requestDeathTimeFrom = isThisDateValid(requestDeathTimeFrom) ? requestDeathTimeFrom : null;
        requestDeathTimeTo = isThisDateValid(requestDeathTimeTo) ? requestDeathTimeTo : null;
        aidsFrom = isThisDateValid(aidsFrom) ? aidsFrom : null;
        aidsTo = isThisDateValid(aidsTo) ? aidsTo : null;
        updateTimeFrom = isThisDateValid(updateTimeFrom) ? updateTimeFrom : null;
        updateTimeTo = isThisDateValid(updateTimeTo) ? updateTimeTo : null;
        
        try {
            ageFrom = StringUtils.isEmpty(ageFrom) || Integer.valueOf(ageFrom) < 0 ? "0" : ageFrom;
            ageTo = StringUtils.isNotEmpty(ageTo) && Integer.valueOf(ageTo) <= 0 ? "-1" : ageTo;
        } catch (NumberFormatException e) {
            ageFrom = "-1";
            ageTo = "-1";
        }
        
        form.setAgeFrom(ageFrom);
        form.setAgeTo(ageTo);
        form.setDeathTimeFrom(deathTimeFrom);
        form.setDeathTimeTo(deathTimeTo);
        form.setAidsFrom(aidsFrom);
        form.setAidsTo(aidsTo);
        form.setRequestDeathTimeFrom(requestDeathTimeFrom);
        form.setRequestDeathTimeTo(requestDeathTimeTo);
        form.setUpdateTimeFrom(updateTimeFrom);
        form.setUpdateTimeTo(updateTimeTo);

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
        //Truy vẫn dữ liệu
        List<TablePacDeadForm> result = patientRepositoryImpl.getTableDead(
                                                    form.isVAAC(), form.isTTYT(), form.isTYT(), levelDisplay, manageStatus, addressFilter,
                                                    form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), pIDs, dIDs, wIDs, 
                                                    isThisDateValid(deathTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, deathTimeFrom) : null,
                                                    isThisDateValid(deathTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, deathTimeTo) : null,
                                                    isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null,
                                                    isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null,
                                                    isThisDateValid(requestDeathTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, requestDeathTimeFrom) : null,
                                                    isThisDateValid(requestDeathTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, requestDeathTimeTo) : null,
                                                    isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null,
                                                    isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                                                    ageFrom, ageTo ,job, gender, testObject, statusResident, statusTreatment);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        PacPatientInfoEntity firstEntity = pacPatientService.getFirst(currentUser.getSite().getProvinceID());
        PacPatientInfoEntity entity = pacPatientService.getFirstDead(form.isTTYT(), form.isTYT(), manageStatus, addressFilter,
                                                    form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(), pIDs, dIDs, wIDs, 
                                                    isThisDateValid(deathTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, deathTimeFrom) : null,
                                                    isThisDateValid(deathTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, deathTimeTo) : null,
                                                    isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null,
                                                    isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null,
                                                    isThisDateValid(requestDeathTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, requestDeathTimeFrom) : null,
                                                    isThisDateValid(requestDeathTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, requestDeathTimeTo) : null,
                                                    isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null,
                                                    isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                                                    ageFrom, ageTo ,job, gender, testObject, statusResident, statusTreatment);
        Date startDate = StringUtils.isEmpty(deathTimeFrom) || !isThisDateValid(deathTimeFrom) ? (entity == null ? (firstEntity == null ? new Date() : firstEntity.getConfirmTime()) : entity.getDeathTime()) : sdf.parse(deathTimeFrom);
        form.setStartDate(TextUtils.formatDate(startDate, FORMATDATE));

        Date endDate = StringUtils.isEmpty(deathTimeTo) || !isThisDateValid(deathTimeTo) ? new Date() : sdf.parse(deathTimeTo);
        form.setEndDate(TextUtils.formatDate(endDate, FORMATDATE));
        
        form.setTable(new ArrayList<TablePacDeadForm>());
        TablePacDeadForm item;
        TablePacDeadForm dItem;
        TablePacDeadForm wItem;
        TablePacDeadForm oItem;
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
            item = new TablePacDeadForm();
            item.setStt(index);
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<TablePacDeadForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new TablePacDeadForm();
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<TablePacDeadForm>());
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
                            
                            wItem = new TablePacDeadForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
                        }
                        
                        // Thêm vào danh sách xã mặc định cua huyện khi login
                        if (form.isTTYT() && form.isDefaultSearchPlace() && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            List<TablePacDeadForm> childs = new ArrayList<>(dItem.getChilds());
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getWardID")));
                            for (WardEntity wardEntity : wardsDefault) {
                                if (!childsResult.contains(wardEntity.getID())) {
                                    wItem = new TablePacDeadForm();
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
                                wItem = new TablePacDeadForm();
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

        //set dữ liệu vào table - Mapping data Tỉnh
        for (TablePacDeadForm row : form.getTable()) {
            for (TablePacDeadForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    row.setMale(row.getMale() + line.getMale());
                    row.setFemale(row.getFemale() + line.getFemale());
                    row.setOther(row.getOther() + line.getOther());
                    form.setSum(form.getSum() + line.getSum());
                }

                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (TablePacDeadForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setMale(pChild.getMale() + line.getMale());
                            pChild.setFemale(pChild.getFemale() + line.getFemale());
                            pChild.setOther(pChild.getOther() + line.getOther());
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (TablePacDeadForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID())) {
                                    dChild.setMale(dChild.getMale() + line.getMale());
                                    dChild.setFemale(dChild.getFemale() + line.getFemale());
                                    dChild.setOther(dChild.getOther() + line.getOther());
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
            Map<String, TablePacDeadForm> mOutProvince = new HashMap<>();
            for (TablePacDeadForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new TablePacDeadForm());
                        mOutProvince.get(line.getProvinceID()).setDisplayName(provinces.get(line.getProvinceID()));
                    }
                    oItem = mOutProvince.get(line.getProvinceID());
                    oItem.setMale(oItem.getMale() + line.getMale());
                    oItem.setFemale(oItem.getFemale() + line.getFemale());
                    oItem.setOther(oItem.getOther() + line.getOther());
                    mOutProvince.put(line.getProvinceID(), oItem);
                }
            }
            if (!mOutProvince.isEmpty()) {
                TablePacDeadForm outProvince = new TablePacDeadForm();
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(form.getTable().size() + 1);
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (TablePacDeadForm model : mOutProvince.values()) {
                    outProvince.setMale(outProvince.getMale() + model.getMale());
                    outProvince.setFemale(outProvince.getFemale() + model.getFemale());
                    outProvince.setOther(outProvince.getOther() + model.getOther());

                    form.setSum(form.getSum() + model.getSum());
                }
                form.getTable().add(outProvince);
            }
        }
        return form;
    }
}
