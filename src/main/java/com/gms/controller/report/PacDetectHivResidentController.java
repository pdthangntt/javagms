package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.StatusOfResidentEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DetectHivResidentExcel;
import com.gms.entity.form.pac.PacDetectHivResidentFrom;
import com.gms.entity.form.pac.PacDetectHivResidentTableForm;
import com.gms.entity.form.pac.PacDetectSearchForm;
import com.gms.repository.impl.PacDetecthivResidentImpl;
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
 * Báo cáo người nhiễm phát hiện: hiện trạng cư trú
 *
 * @author vvthanh
 */
@Controller(value = "pac_detecthiv_resident")
public class PacDetectHivResidentController extends PacDetectHivController {

    private static final String FORMATDATE_SQL = "yyyy-MM-dd";
    @Autowired
    private PacDetecthivResidentImpl detecthivResidentImpl;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;

    public PacDetectHivResidentFrom getData(String updateTimeFrom, String updateTimeTo ,String permanentProvinceID, String permanentDistrictID, String permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,String aidsFrom, String aidsTo, String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String manageStatus, String levelDisplay, String placeTest, String facility, String hasHealthInsurance) throws Exception {

        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> rowProvince = new LinkedHashMap<>();
        Map<String, List<WardEntity>> rowWard = new LinkedHashMap<>();
        //tìm kiếm
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        //tên cơ sở đăng nhập
        List<String> sitepID = new ArrayList<>();
        List<String> sitedID = new ArrayList<>();
        List<String> sitewID = new ArrayList<>();

        boolean defaultLoggin = StringUtils.isEmpty(permanentProvinceID) && StringUtils.isEmpty(permanentDistrictID) && StringUtils.isEmpty(permanentWardID);

        //Lấy thông tin tìm kiếm tỉnh huyện xã
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

        PacDetectHivResidentFrom form = new PacDetectHivResidentFrom();
        //Thông tin xuất dữ liệu
        form.setTitle("Báo cáo người nhiễm phát hiện theo hiện trạng cư trú");
        if (!isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo người nhiễm phát hiện theo hiện trạng cư trú");
                    break;
                case "1":
                    form.setTitle("Báo cáo người nhiễm phát hiện chưa rà soát theo hiện trạng cư trú");
                    break;
                case "2":
                    form.setTitle("Báo cáo người nhiễm phát hiện cần rà soát theo hiện trạng cư trú");
                    break;
                case "3":
                    form.setTitle("Báo cáo người nhiễm phát hiện đã rà soát theo hiện trạng cư trú");
                    break;
                case "4":
                    form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo hiện trạng cư trú");
                    break;
                default:
                    form.setTitle("");
            }
        }
        if (isVAAC()) {
            form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo hiện trạng cư trú");
        }
        form.setFileName("BaoCaoPhatHienTheoHienTrangCuTru_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getOptions());
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setDistrictID(currentUser.getSite().getDistrictID());
        //Điều kiện lọc danh sách
        if (form.isTYT()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "ward" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            form.setSiteDistrictID(currentUser.getSite().getDistrictID());
            form.setSiteWardID(currentUser.getSite().getWardID());
            sitepID.add(currentUser.getSite().getProvinceID());
            sitedID.add(currentUser.getSite().getDistrictID());
            sitewID.add(currentUser.getSite().getWardID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
        } else if (form.isTTYT()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "ward" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            form.setSiteDistrictID(currentUser.getSite().getDistrictID());
            sitepID.add(currentUser.getSite().getProvinceID());
            sitedID.add(currentUser.getSite().getDistrictID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
            //tìm xã
            form.setWards(new ArrayList<String>());
            for (WardEntity wardEntity : locationsService.findWardByDistrictID(currentUser.getSite().getDistrictID())) {
                form.getWards().add(wardEntity.getID());
            }
        } else if (form.isPAC()) {
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "district" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
            sitepID.add(currentUser.getSite().getProvinceID());
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
        //Điều kiện tìm kiếm
        form.setAddressFilter(addressFilter);
        form.setLevelDisplay(levelDisplay);
        form.setJob(job);
        form.setObject(testObject);
        form.setResident(statusResident);
        form.setTreatment(statusTreatment);
        form.setManageStatus(manageStatus);
        form.setTransmision(transmission);
        form.setRace(race);
        form.setGender(gender);
        form.setPlaceTest(placeTest);
        form.setFacility(facility);

        // Chuẩn hóa ngày
        confirmTimeFrom = isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null;
        confirmTimeTo = isThisDateValid(confirmTimeTo) ? confirmTimeTo : null;
       form.setAidsFrom(isThisDateValid(aidsFrom) ? aidsFrom : null);
        form.setAidsTo(isThisDateValid(aidsTo) ? aidsTo : null);
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
        form.setInputTimeFrom(inputTimeFrom);
        form.setInputTimeTo(inputTimeTo);
        form.setManageTimeFrom(manageTimeFrom);
        form.setManageTimeTo(manageTimeTo);

        form.setPatientStatus(patientStatus);
        form.setProvince(permanentProvinceID);
        form.setDistrict(permanentDistrictID);
        form.setWard(permanentWardID);
        // Default patient status
        patientStatus = patientStatus.contains(",") ? null : patientStatus;

        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, wIDs));
        form.setProvinceName(isVAAC() ? currentUser.getSiteProvince().getName() : super.getTitleAddress(sitepID, sitedID, sitewID));
        //Truy vẫn dữ liệu
        List<PacDetectHivResidentTableForm> result = detecthivResidentImpl.getTableDetectHivResident(
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
                isThisDateValid(updateTimeFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeFrom) : null,
                isThisDateValid(updateTimeTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, updateTimeTo) : null,
                isThisDateValid(aidsFrom) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom) : null,
                isThisDateValid(aidsTo) ? TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo) : null, gender,
                job, race, testObject, statusResident, statusTreatment, transmission, isPAC(), placeTest, facility, hasHealthInsurance);

        form.setTable(new ArrayList<PacDetectHivResidentTableForm>());
        PacDetectHivResidentTableForm item;
        PacDetectHivResidentTableForm dItem;
        PacDetectHivResidentTableForm wItem;
        PacDetectHivResidentTableForm oItem;
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
            item = new PacDetectHivResidentTableForm();
            item.setStt(String.valueOf(index));
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<PacDetectHivResidentTableForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new PacDetectHivResidentTableForm();
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<PacDetectHivResidentTableForm>());
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
                                    || (form.getSiteDistrictID() != null && !resultWIDs.contains(ward.getID())) && !defaultLoggin) {
                                continue;
                            }
                            wItem = new PacDetectHivResidentTableForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
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
        for (PacDetectHivResidentTableForm row : form.getTable()) {
            for (PacDetectHivResidentTableForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    row.setDangODiaPhuong(row.getDangODiaPhuong() + line.getDangODiaPhuong());
                    row.setDiLamAnXa(row.getDiLamAnXa() + line.getDiLamAnXa());
                    row.setDiTrai(row.getDiTrai() + line.getDiTrai());
                    row.setChuyenDiNoiKhac(row.getChuyenDiNoiKhac() + line.getChuyenDiNoiKhac());
                    row.setChuyenTrongTinh(row.getChuyenTrongTinh() + line.getChuyenTrongTinh());
                    row.setMatDau(row.getMatDau() + line.getMatDau());
                    row.setChuaXacDinhDuoc(row.getChuaXacDinhDuoc() + line.getChuaXacDinhDuoc());
                    row.setKhongCoThucTe(row.getKhongCoThucTe() + line.getKhongCoThucTe());
                    row.setUnclear(row.getUnclear() + line.getUnclear());

                }
                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (PacDetectHivResidentTableForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setDangODiaPhuong(pChild.getDangODiaPhuong() + line.getDangODiaPhuong());
                            pChild.setDiLamAnXa(pChild.getDiLamAnXa() + line.getDiLamAnXa());
                            pChild.setDiTrai(pChild.getDiTrai() + line.getDiTrai());
                            pChild.setChuyenDiNoiKhac(pChild.getChuyenDiNoiKhac() + line.getChuyenDiNoiKhac());
                            pChild.setChuyenTrongTinh(pChild.getChuyenTrongTinh() + line.getChuyenTrongTinh());
                            pChild.setMatDau(pChild.getMatDau() + line.getMatDau());
                            pChild.setChuaXacDinhDuoc(pChild.getChuaXacDinhDuoc() + line.getChuaXacDinhDuoc());
                            pChild.setKhongCoThucTe(pChild.getKhongCoThucTe() + line.getKhongCoThucTe());
                            pChild.setUnclear(pChild.getUnclear() + line.getUnclear());
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (PacDetectHivResidentTableForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID())) {
                                    dChild.setDangODiaPhuong(dChild.getDangODiaPhuong() + line.getDangODiaPhuong());
                                    dChild.setDiLamAnXa(dChild.getDiLamAnXa() + line.getDiLamAnXa());
                                    dChild.setDiTrai(dChild.getDiTrai() + line.getDiTrai());
                                    dChild.setChuyenDiNoiKhac(dChild.getChuyenDiNoiKhac() + line.getChuyenDiNoiKhac());
                                    dChild.setChuyenTrongTinh(dChild.getChuyenTrongTinh() + line.getChuyenTrongTinh());
                                    dChild.setMatDau(dChild.getMatDau() + line.getMatDau());
                                    dChild.setChuaXacDinhDuoc(dChild.getChuaXacDinhDuoc() + line.getChuaXacDinhDuoc());
                                    dChild.setKhongCoThucTe(dChild.getKhongCoThucTe() + line.getKhongCoThucTe());
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
        //.biến tính tổng
        int matDau = 0;
        int dangODiaPhuong = 0;
        int khongCoThucTe = 0;
        int diTrai = 0;
        int chuyenDiNoiKhac = 0;
        int chuaXacDinhDuoc = 0;
        int diLamAnXa = 0;
        int chuyenTrongTinh = 0;
        int unclear = 0;
        int total = 0;

        //Thêm item ngoại tỉnh khi không phải vaac
        if (!form.isVAAC()) {
            Map<String, PacDetectHivResidentTableForm> mOutProvince = new HashMap<>();
            for (PacDetectHivResidentTableForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new PacDetectHivResidentTableForm());
                        mOutProvince.get(line.getProvinceID()).setDisplayName(provinces.get(line.getProvinceID()));
                    }
                    oItem = mOutProvince.get(line.getProvinceID());
                    oItem.setDangODiaPhuong(oItem.getDangODiaPhuong() + line.getDangODiaPhuong());
                    oItem.setDiLamAnXa(oItem.getDiLamAnXa() + line.getDiLamAnXa());
                    oItem.setDiTrai(oItem.getDiTrai() + line.getDiTrai());
                    oItem.setChuyenDiNoiKhac(oItem.getChuyenDiNoiKhac() + line.getChuyenDiNoiKhac());
                    oItem.setChuyenTrongTinh(oItem.getChuyenTrongTinh() + line.getChuyenTrongTinh());
                    oItem.setMatDau(oItem.getMatDau() + line.getMatDau());
                    oItem.setChuaXacDinhDuoc(oItem.getChuaXacDinhDuoc() + line.getChuaXacDinhDuoc());
                    oItem.setKhongCoThucTe(oItem.getKhongCoThucTe() + line.getKhongCoThucTe());
                    oItem.setUnclear(oItem.getUnclear() + line.getUnclear());
                    mOutProvince.put(line.getProvinceID(), oItem);
                }
            }
            if (!mOutProvince.isEmpty()) {
                PacDetectHivResidentTableForm outProvince = new PacDetectHivResidentTableForm();
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(String.valueOf(form.getTable().size() + 1));
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (PacDetectHivResidentTableForm line : mOutProvince.values()) {
                    outProvince.setDangODiaPhuong(outProvince.getDangODiaPhuong() + line.getDangODiaPhuong());
                    outProvince.setDiLamAnXa(outProvince.getDiLamAnXa() + line.getDiLamAnXa());
                    outProvince.setDiTrai(outProvince.getDiTrai() + line.getDiTrai());
                    outProvince.setChuyenDiNoiKhac(outProvince.getChuyenDiNoiKhac() + line.getChuyenDiNoiKhac());
                    outProvince.setChuyenTrongTinh(outProvince.getChuyenTrongTinh() + line.getChuyenTrongTinh());
                    outProvince.setMatDau(outProvince.getMatDau() + line.getMatDau());
                    outProvince.setChuaXacDinhDuoc(outProvince.getChuaXacDinhDuoc() + line.getChuaXacDinhDuoc());
                    outProvince.setKhongCoThucTe(outProvince.getKhongCoThucTe() + line.getKhongCoThucTe());
                    outProvince.setUnclear(outProvince.getUnclear() + line.getUnclear());
                }
                form.getTable().add(outProvince);
            }

        }

        for (PacDetectHivResidentTableForm model : form.getTable()) {
            matDau = matDau + model.getMatDau();
            dangODiaPhuong = dangODiaPhuong + model.getDangODiaPhuong();
            khongCoThucTe = khongCoThucTe + model.getKhongCoThucTe();
            diTrai = diTrai + model.getDiTrai();
            chuyenDiNoiKhac = chuyenDiNoiKhac + model.getChuyenDiNoiKhac();
            chuaXacDinhDuoc = chuaXacDinhDuoc + model.getChuaXacDinhDuoc();
            diLamAnXa = diLamAnXa + model.getDiLamAnXa();
            chuyenTrongTinh = chuyenTrongTinh + model.getChuyenTrongTinh();
            unclear = unclear + model.getUnclear();
            total = total + model.getSum();
        }

        //set form total
        form.setDangODiaPhuong(dangODiaPhuong);
        form.setDiLamAnXa(diLamAnXa);
        form.setDiTrai(diTrai);
        form.setChuyenDiNoiKhac(chuyenDiNoiKhac);
        form.setChuyenTrongTinh(chuyenTrongTinh);
        form.setMatDau(matDau);
        form.setChuaXacDinhDuoc(chuaXacDinhDuoc);
        form.setKhongCoThucTe(khongCoThucTe);
        form.setUnclear(unclear);
        form.setTotal(total);
        return form;
    }

    @GetMapping(value = {"/pac-detecthiv-resident/index.html"})
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
            @RequestParam(name = "place_test", required = false) String placeTest,  // Nơi xét nghiệm khẳng định
            @RequestParam(name = "facility", required = false) String facility,     // Nơi điều trị
            @RequestParam(name = "has_health_insurance", required = false) String hasHealthInsurance
    ) throws Exception {
        PacDetectHivResidentFrom form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom,aidsTo,gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("options", form.getOptions());
        model.addAttribute("isVAAC", form.isVAAC());
        model.addAttribute("isPAC", form.isPAC());
        model.addAttribute("isTTYT", form.isTTYT());
        model.addAttribute("isTYT", form.isTYT());
        model.addAttribute("title", "Báo cáo người nhiễm phát hiện");

        return "report/pac/detecthiv-resident";
    }

    private static String TEMPLATE_PDF = "report/pac/pac-detecthiv-resident-pdf.html";

    @GetMapping(value = {"/pac-detecthiv-resident/pdf.html"})
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
        PacDetectHivResidentFrom form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom,aidsTo,gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("isPAC", isPAC());
        context.put("isTYT", isTYT());
        context.put("isTTYT", isTTYT());
        context.put("isVAAC", isVAAC());
        return exportPdf(form.getFileName(), TEMPLATE_PDF, context);
    }

    @GetMapping(value = {"/pac-detecthiv-resident/excel.html"})
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
        PacDetectHivResidentFrom form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom,aidsTo,gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);

        return exportExcel(new DetectHivResidentExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-detecthiv-resident/email.html"})
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
        PacDetectHivResidentFrom form = getData(updateTimeFrom, updateTimeTo,provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo,aidsFrom,aidsTo,gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacDetectHivResidentReport());
        }
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                form.getTitle(),
                EmailoutboxEntity.TEMPLATE_PAC_REGISIDENT_HIV,
                context,
                new DetectHivResidentExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacDetectHivResidentReport());
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
