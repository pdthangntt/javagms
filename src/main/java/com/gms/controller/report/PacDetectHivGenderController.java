package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DetectHivGenderExcel;
import com.gms.entity.form.pac.DetectHivGenderForm;
import com.gms.entity.form.pac.TableDetectHivGenderForm;
import com.gms.repository.impl.DetectHivRepositoryImpl;
import com.gms.service.LocationsService;
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
 * Báo cáo người nhiễm phát hiện: Giới tính
 *
 * @author TrangBN
 */
@Controller(value = "pac_detecthiv_gender")
public class PacDetectHivGenderController extends PacDetectHivController {

    private static final String TEMPLATE_DETECT_GENDER_HIV_REPORT = "report/pac/detecthiv-gender-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private DetectHivRepositoryImpl detectHivRepositoryImpl;

    private DetectHivGenderForm getData(String updateTimeFrom, String updateTimeTo, String permanentProvinceID, String permanentDistrictID, String permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String manageStatus, String levelDisplay, String placeTest, String facility, String hasHealthInsurance) throws Exception {

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

        DetectHivGenderForm form = new DetectHivGenderForm();
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
        form.setFileName("BCNguoiNhiemPhatHienQuanLyTheoGioiTinh_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo giới tính");

        if (!form.isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo người nhiễm phát hiện theo giới tính");
                    break;
                case "1":
                    form.setTitle("Báo cáo người nhiễm phát hiện chưa rà soát theo giới tính");
                    break;
                case "2":
                    form.setTitle("Báo cáo người nhiễm phát hiện cần rà soát theo giới tính");
                    break;
                case "3":
                    form.setTitle("Báo cáo người nhiễm phát hiện đã rà soát theo giới tính");
                    break;
                case "4":
                    form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo giới tính");
                    break;
                default:
                    form.setTitle("");
            }
        }

        //Điều kiện tìm kiếm
        form.setAddressFilter(addressFilter);
        form.setJob(job);
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
        form.setHasHealthInsurance(hasHealthInsurance);
        
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
        form.setPlaceTest(placeTest);
        form.setFacility(facility);

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
        List<TableDetectHivGenderForm> result = detectHivRepositoryImpl.getTableDetectHivGender(
                form.isVAAC(), levelDisplay, manageStatus,
                form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(),
                pIDs, dIDs, wIDs, addressFilter,
                patientStatus,
                isThisDateValid(manageTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom) : null,
                isThisDateValid(manageTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo) : null,
                isThisDateValid(inputTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom) : null,
                isThisDateValid(inputTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) : null,
                isThisDateValid(confirmTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom) : null,
                isThisDateValid(confirmTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo) : null,
                isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null,
                isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null,
                isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null,
                isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                job, race, testObject, statusResident, statusTreatment, transmission, isTTYT(), isTYT(), placeTest, facility, hasHealthInsurance);

        form.setTable(new ArrayList<TableDetectHivGenderForm>());
        TableDetectHivGenderForm item;
        TableDetectHivGenderForm dItem;
        TableDetectHivGenderForm wItem;
        TableDetectHivGenderForm oItem;
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
            item = new TableDetectHivGenderForm();
            item.setStt(index);
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<TableDetectHivGenderForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new TableDetectHivGenderForm();
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<TableDetectHivGenderForm>());
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

                            wItem = new TableDetectHivGenderForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
                        }

                        // Thêm vào danh sách xã mặc định cua huyện khi login
                        if (form.isTTYT() && form.isDefaultSearchPlace() && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            List<TableDetectHivGenderForm> childs = new ArrayList<>(dItem.getChilds());
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getWardID")));
                            for (WardEntity wardEntity : wardsDefault) {
                                if (!childsResult.contains(wardEntity.getID())) {
                                    wItem = new TableDetectHivGenderForm();
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
                                wItem = new TableDetectHivGenderForm();
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
        for (TableDetectHivGenderForm row : form.getTable()) {
            for (TableDetectHivGenderForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    row.setMale(row.getMale() + line.getMale());
                    row.setFemale(row.getFemale() + line.getFemale());
                    row.setOther(row.getOther() + line.getOther());
                    form.setSum(form.getSum() + line.getSum());
                }

                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (TableDetectHivGenderForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setMale(pChild.getMale() + line.getMale());
                            pChild.setFemale(pChild.getFemale() + line.getFemale());
                            pChild.setOther(pChild.getOther() + line.getOther());
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (TableDetectHivGenderForm dChild : pChild.getChilds()) {
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
            Map<String, TableDetectHivGenderForm> mOutProvince = new HashMap<>();
            for (TableDetectHivGenderForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new TableDetectHivGenderForm());
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
                TableDetectHivGenderForm outProvince = new TableDetectHivGenderForm();
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(form.getTable().size() + 1);
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (TableDetectHivGenderForm gender : mOutProvince.values()) {
                    outProvince.setMale(outProvince.getMale() + gender.getMale());
                    outProvince.setFemale(outProvince.getFemale() + gender.getFemale());
                    outProvince.setOther(outProvince.getOther() + gender.getOther());

                    form.setSum(form.getSum() + gender.getSum());
                }
                form.getTable().add(outProvince);
            }
        }
        return form;
    }

    @GetMapping(value = {"/pac-detecthiv-gender/index.html"})
    public String actionDetectHivGenderIndex(Model model,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
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
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {
        DetectHivGenderForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom,
                confirmTimeTo, aidsFrom, aidsTo, job, race, testObject, statusResident, statusTreatment,
                transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("options", form.getOptions());
        model.addAttribute("isVAAC", form.isVAAC());
        model.addAttribute("isPAC", form.isPAC());
        model.addAttribute("isTTYT", form.isTTYT());
        model.addAttribute("isTYT", form.isTYT());
        model.addAttribute("title", "Báo cáo người nhiễm phát hiện");
        return "report/pac/detecthiv-gender";
    }

    /**
     * Xuất danh sách excel
     *
     * @param levelDisplay
     * @param addressFilter
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
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param manageStt
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-detecthiv-gender/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
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
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {
        return exportExcel(new DetectHivGenderExcel(getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo, aidsFrom, aidsTo, job, race, testObject, statusResident,
                statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance), EXTENSION_EXCEL_X));
    }

    /**
     * Xuất file PDF danh sách mới phát hiện theo nhóm giới tính
     *
     * @param levelDisplay
     * @param addressFilter
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
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param manageStt
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-detecthiv-gender/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
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
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {

        DetectHivGenderForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom,
                confirmTimeTo, aidsFrom, aidsTo, job, race, testObject, statusResident, statusTreatment,
                transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isTTYT", isTTYT());
        context.put("isPAC", isPAC());
        context.put("isTYT", isTYT());
        context.put("isVAAC", isVAAC());
        return exportPdf(form.getFileName(), TEMPLATE_DETECT_GENDER_HIV_REPORT, context);
    }

    /**
     * Gửi mail có đính kèm excel
     *
     * @param redirectAttributes
     * @param levelDisplay
     * @param addressFilter
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
     * @param updateTimeFrom
     * @param updateTimeTo
     * @param manageStt
     * @param placeTest
     * @param facility
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-detecthiv-gender/email.html"})
    public String actionEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
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
            @RequestParam(name = "update_time_from", required = false, defaultValue = "") String updateTimeFrom,
            @RequestParam(name = "update_time_to", required = false, defaultValue = "") String updateTimeTo,
            @RequestParam(name = "manage_status", required = false, defaultValue = "4") String manageStt,
            @RequestParam(name = "place_test", required = false) String placeTest, // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacDetectHivGenderReport());
        }

        DetectHivGenderForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom,
                confirmTimeTo, aidsFrom, aidsTo, job, race, testObject, statusResident, statusTreatment,
                transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(currentUser.getUser().getEmail(), form.getTitle(), EmailoutboxEntity.TEMPLATE_PAC_GENDER_HIV, context,
                new DetectHivGenderExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacDetectHivGenderReport());
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
