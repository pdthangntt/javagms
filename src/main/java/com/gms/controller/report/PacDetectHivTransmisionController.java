package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DetectHivTransmisionExcel;
import com.gms.entity.form.pac.DetectHivTransmissionForm;
import com.gms.entity.form.pac.TableDetectHivTransmissionForm;
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
 * Báo cáo người nhiễm phát hiện: Đường lây nhiễm
 *
 * @author vvthanh
 */
@Controller(value = "pac_detecthiv_transmision")
public class PacDetectHivTransmisionController extends PacDetectHivController {
    private static String TEMPLATE_PDF = "report/pac/pac-detecthiv-transmission-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private PacPatientService pacPatientService;

    @Autowired
    DetectHivRepositoryImpl detectHivRepositoryImpl;

    //check valid date
    private boolean isThisDateValid(String dateToValidate){
        if(dateToValidate == null){
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
    
    private DetectHivTransmissionForm getData(String updateTimeFrom, String updateTimeTo ,String permanentProvinceID, String permanentDistrictID, String permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String manageStatus, String levelDisplay, String placeTest, String facility, String hasHealthInsurance) throws Exception {
        //default
        patientStatus = patientStatus.contains(",") ? null : patientStatus;

        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> rowProvince = new LinkedHashMap();
        Map<String, List<WardEntity>> rowWard = new LinkedHashMap<>();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();

        //Lấy thông tin tìm kiếm tỉnh huyện xã
        boolean defaultLoggin = StringUtils.isEmpty(permanentProvinceID) && StringUtils.isEmpty(permanentDistrictID) && StringUtils.isEmpty(permanentWardID);
        if (StringUtils.isNotEmpty(permanentProvinceID) && !permanentProvinceID.equals("null")) {
            pIDs.addAll(Arrays.asList(permanentProvinceID.split(",")));
        }
        if (StringUtils.isNotEmpty(permanentDistrictID) && !permanentDistrictID.equals("null")) {
            dIDs.addAll(Arrays.asList(permanentDistrictID.split(",")));
        }
        if (StringUtils.isNotEmpty(permanentWardID) && !permanentWardID.equals("null")) {
            wIDs.addAll(Arrays.asList(permanentWardID.split(",")));
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

        DetectHivTransmissionForm form = new DetectHivTransmissionForm();
        form.setProvinceName(provinces.get(currentUser.getSite().getProvinceID()));
        form.setDistrictName(districts.get(currentUser.getSite().getDistrictID()).getName());
        form.setWardName(locationsService.findWard(currentUser.getSite().getWardID()).getName());
        form.setTitleLocation(getTitleAddress(pIDs, dIDs, wIDs));
        form.setFileName("BaoCaoPhatHienTheoDuongLayNhiem_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getOptions());
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setDefaultSearchPlace(defaultLoggin);
        form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo đường lây nhiễm");
        if (!form.isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo người nhiễm phát hiện theo đường lây nhiễm");
                    break;
                case "1":
                    form.setTitle("Báo cáo người nhiễm phát hiện chưa rà soát theo đường lây nhiễm");
                    break;
                case "2":
                    form.setTitle("Báo cáo người nhiễm phát hiện cần rà soát theo đường lây nhiễm");
                    break;
                case "3":
                    form.setTitle("Báo cáo người nhiễm phát hiện đã rà soát theo đường lây nhiễm");
                    break;
                case "4":
                    form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo đường lây nhiễm");
                    break;
                default:
                    form.setTitle("");
            }
        }

        //Điều kiện tìm kiếm
        form.setUpdateTimeFrom(isThisDateValid(updateTimeFrom) ? updateTimeFrom : null);
        form.setUpdateTimeTo(isThisDateValid(updateTimeTo) ? updateTimeTo : null);
        form.setConfirmTimeFrom(isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null);
        form.setConfirmTimeTo(isThisDateValid(confirmTimeTo) ? confirmTimeTo : null);
        form.setAidsFrom(isThisDateValid(aidsFrom) ? aidsFrom : null);
        form.setAidsTo(isThisDateValid(aidsTo) ? aidsTo : null);
        form.setGender(gender);
        form.setManageTimeFrom(isThisDateValid(manageTimeFrom) ? manageTimeFrom : null);
        form.setManageTimeTo(isThisDateValid(manageTimeTo) ? manageTimeTo : null);
        form.setCreateAtFrom(isThisDateValid(inputTimeFrom) ? inputTimeFrom : null);
        form.setCreateAtTo(isThisDateValid(inputTimeTo) ? inputTimeTo : null);
        form.setProvince(isPAC() || isTTYT() || isTYT() ? getCurrentUser().getSite().getProvinceID() : permanentProvinceID);
        form.setPatientStatus(patientStatus);
        form.setAddressFilter(addressFilter);
        form.setJob(job);
        form.setRace(race);
        form.setObject(testObject);
        form.setTransmision(transmission);
        form.setTreatment(statusTreatment);
        form.setResident(statusResident);
        form.setManageStatus(manageStatus);
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
        List<TableDetectHivTransmissionForm> result = detectHivRepositoryImpl.getTableDetectHivTransmission(
                form.isVAAC(), levelDisplay, manageStatus,
                form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(),
                pIDs, dIDs, wIDs, addressFilter,patientStatus, 
                isThisDateValid(manageTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeFrom) : null, 
                isThisDateValid(manageTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, manageTimeTo) : null,
                isThisDateValid(inputTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeFrom) : null, 
                isThisDateValid(inputTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, inputTimeTo) : null,
                isThisDateValid(confirmTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeFrom) : null, 
                isThisDateValid(confirmTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, confirmTimeTo) : null,
                isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null, 
                isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null, 
                isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null,gender,
                job, race, testObject, statusResident, statusTreatment, transmission, form.isTTYT(), form.isTYT(), placeTest, facility, hasHealthInsurance);
        
        form.setTable(new ArrayList<TableDetectHivTransmissionForm>());
        TableDetectHivTransmissionForm item;
        TableDetectHivTransmissionForm dItem;
        TableDetectHivTransmissionForm wItem;
        TableDetectHivTransmissionForm oItem;
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
            item = new TableDetectHivTransmissionForm();
            item.setStt(String.valueOf(index));
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<TableDetectHivTransmissionForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new TableDetectHivTransmissionForm();
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<TableDetectHivTransmissionForm>());
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
                            
                            wItem = new TableDetectHivTransmissionForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
                        }
                        
                        // Thêm vào danh sách xã mặc định cua huyện khi login
                        if (form.isTTYT() && form.isDefaultSearchPlace() && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            List<TableDetectHivTransmissionForm> childs = new ArrayList<>(dItem.getChilds());
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getWardID")));
                            for (WardEntity wardEntity : wardsDefault) {
                                if (!childsResult.contains(wardEntity.getID())) {
                                    wItem = new TableDetectHivTransmissionForm();
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
                                wItem = new TableDetectHivTransmissionForm();
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

        //Row tổng
        TableDetectHivTransmissionForm rowTong = new TableDetectHivTransmissionForm();
        rowTong.setProvinceID("all");

        //set dữ liệu vào table - Mapping data Tỉnh
        for (TableDetectHivTransmissionForm row : form.getTable()) {
            for (TableDetectHivTransmissionForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    rowTong.setBlood(rowTong.getBlood() + line.getBlood());
                    rowTong.setMomtochild(rowTong.getMomtochild() + line.getMomtochild());
                    rowTong.setNoinformation(rowTong.getNoinformation() + line.getNoinformation());
                    rowTong.setSexuality(rowTong.getSexuality() + line.getSexuality());
                    rowTong.setUnclear(rowTong.getUnclear() + line.getUnclear());

                    row.setBlood(row.getBlood() + line.getBlood());
                    row.setMomtochild(row.getMomtochild() + line.getMomtochild());
                    row.setNoinformation(row.getNoinformation() + line.getNoinformation());
                    row.setSexuality(row.getSexuality() + line.getSexuality());
                    row.setUnclear(row.getUnclear() + line.getUnclear());
                }
                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (TableDetectHivTransmissionForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setBlood(pChild.getBlood() + line.getBlood());
                            pChild.setMomtochild(pChild.getMomtochild() + line.getMomtochild());
                            pChild.setNoinformation(pChild.getNoinformation() + line.getNoinformation());
                            pChild.setSexuality(pChild.getSexuality() + line.getSexuality());
                            pChild.setUnclear(pChild.getUnclear() + line.getUnclear());
                            
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (TableDetectHivTransmissionForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID())) {
                                    dChild.setBlood(dChild.getBlood() + line.getBlood());
                                    dChild.setMomtochild(dChild.getMomtochild() + line.getMomtochild());
                                    dChild.setNoinformation(dChild.getNoinformation() + line.getNoinformation());
                                    dChild.setSexuality(dChild.getSexuality() + line.getSexuality());
                                    dChild.setUnclear(dChild.getUnclear() + line.getUnclear());
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

        //Thêm ngoại tỉnh
        if (!form.isVAAC()) {
            Map<String, TableDetectHivTransmissionForm> mOutProvince = new HashMap<>();
            for (TableDetectHivTransmissionForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new TableDetectHivTransmissionForm());
                        mOutProvince.get(line.getProvinceID()).setProvinceID(line.getProvinceID());
                        mOutProvince.get(line.getProvinceID()).setDisplayName(provinces.get(line.getProvinceID()));
                    }
                    oItem = mOutProvince.get(line.getProvinceID());
                    oItem.setBlood(oItem.getBlood() + line.getBlood());
                    oItem.setMomtochild(oItem.getMomtochild() + line.getMomtochild());
                    oItem.setNoinformation(oItem.getNoinformation() + line.getNoinformation());
                    oItem.setSexuality(oItem.getSexuality() + line.getSexuality());
                    oItem.setUnclear(oItem.getUnclear() + line.getUnclear());
                    mOutProvince.put(line.getProvinceID(), oItem);
                }
            }
            if (!mOutProvince.isEmpty()) {
                TableDetectHivTransmissionForm outProvince = new TableDetectHivTransmissionForm();
                outProvince.setProvinceID("");
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(String.valueOf(form.getTable().size() + 1));
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (TableDetectHivTransmissionForm line : mOutProvince.values()) {
                    outProvince.setBlood(outProvince.getBlood() + line.getBlood());
                    outProvince.setMomtochild(outProvince.getMomtochild() + line.getMomtochild());
                    outProvince.setNoinformation(outProvince.getNoinformation() + line.getNoinformation());
                    outProvince.setSexuality(outProvince.getSexuality() + line.getSexuality());
                    outProvince.setUnclear(outProvince.getUnclear() + line.getUnclear());
                    
                    rowTong.setBlood(rowTong.getBlood() + line.getBlood());
                    rowTong.setMomtochild(rowTong.getMomtochild() + line.getMomtochild());
                    rowTong.setNoinformation(rowTong.getNoinformation() + line.getNoinformation());
                    rowTong.setSexuality(rowTong.getSexuality() + line.getSexuality());
                    rowTong.setUnclear(rowTong.getUnclear() + line.getUnclear());
                }
                form.getTable().add(outProvince);
                form.getTable().add(rowTong);
            }
        }

        //Set giá trị cho dòng tổng cộng
        if (isVAAC()) {
            form.getTable().add(rowTong);
        }
        return form;
    }

    @GetMapping(value = {"/pac-detecthiv-transmission/index.html"})
    public String actionDetectHivTransmissionIndex(Model model,
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
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,     // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {

        DetectHivTransmissionForm form = getData(updateTimeFrom,  updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        form.setPatientStatus("".equals(patientStatus) ? null : patientStatus);
        model.addAttribute("form", form);
        model.addAttribute("item", new TableDetectHivTransmissionForm());
        model.addAttribute("title", "Báo cáo người nhiễm phát hiện");
        model.addAttribute("options", getOptions());
        model.addAttribute("isVAAC", isVAAC());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isTYT", isTYT());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("provinceParam", provinceID);

        return "report/pac/detecthiv-transmission";
    }

    @GetMapping(value = {"/pac-detecthiv-transmission/excel.html"})
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
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,     // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {

        DetectHivTransmissionForm form = getData(updateTimeFrom,  updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        
        return exportExcel(new DetectHivTransmisionExcel(form, EXTENSION_EXCEL_X, provinceID));
    }


    @GetMapping(value = {"/pac-detecthiv-transmission/pdf.html"})
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
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,     // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {

        DetectHivTransmissionForm form = getData(updateTimeFrom,  updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("item", new TableDetectHivTransmissionForm());
        context.put("options", getOptions());
        context.put("isVAAC", isVAAC());
        context.put("isTYT", isTYT());
        context.put("isPAC", isPAC());
        context.put("isTTYT", isTTYT());
        context.put("provinceParam", provinceID);

        return exportPdf(form.getFileName(), TEMPLATE_PDF, context);
    }

    @GetMapping(value = {"/pac-detecthiv-transmission/email.html"})
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
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,     // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {

        DetectHivTransmissionForm form = getData(updateTimeFrom,  updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacDetectHivTransmissionReport());
        }
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                form.getTitle(),
                EmailoutboxEntity.TEMPLATE_PAC_TRANSMISION_HIV,
                context,
                new DetectHivTransmisionExcel(form, EXTENSION_EXCEL_X, provinceID));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacDetectHivTransmissionReport());
    }
}
