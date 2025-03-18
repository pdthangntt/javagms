package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.DetectHivObjectExcel;
import com.gms.entity.form.pac.DetectHivObjectForm;
import com.gms.entity.form.pac.TableDetectHivObjectForm;
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
import java.util.LinkedList;
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
 * Báo cáo người nhiễm phát hiện: nhóm đối tượng
 *
 * @author pdThang
 */
@Controller(value = "pac_detecthiv_object")
public class PacDetectHivObjectController extends PacDetectHivController {

    private static String TEMPLATE_PDF = "report/pac/detecthiv-object-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    DetectHivRepositoryImpl detectHivRepositoryImpl;

    private DetectHivObjectForm getData(String updateTimeFrom, String updateTimeTo, String permanentProvinceID, String permanentDistrictID, String permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String gender, String job, String race,
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

        DetectHivObjectForm form = new DetectHivObjectForm();
        //Thông tin xuất dữ liệu
        form.setTitle("Báo cáo người nhiễm phát hiện theo đối tượng");
        if (!isVAAC()) {
            switch (manageStatus) {
                case "-1":
                    form.setTitle("Báo cáo người nhiễm phát hiện theo đối tượng");
                    break;
                case "1":
                    form.setTitle("Báo cáo người nhiễm phát hiện chưa rà soát theo đối tượng");
                    break;
                case "2":
                    form.setTitle("Báo cáo người nhiễm phát hiện cần rà soát theo đối tượng");
                    break;
                case "3":
                    form.setTitle("Báo cáo người nhiễm phát hiện đã rà soát theo đối tượng");
                    break;
                case "4":
                    form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo đối tượng");
                    break;
                default:
                    form.setTitle("");
            }
        }
        if (isVAAC()) {
            form.setTitle("Báo cáo người nhiễm phát hiện được quản lý theo đối tượng");
        }
        form.setFileName("BaoCaoPhatHienTheoDoiTuong_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getOptions());
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setWardID(currentUser.getSite().getWardID());
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
        form.setProvince(permanentProvinceID);
        form.setDistrict(permanentDistrictID);
        form.setWard(permanentWardID);
        form.setGender(gender);
        // Default patient status
        patientStatus = patientStatus.contains(",") ? null : patientStatus;

        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, wIDs));
        form.setProvinceName(isVAAC() ? currentUser.getSiteProvince().getName() : super.getTitleAddress(sitepID, sitedID, sitewID));
        //Truy vẫn dữ liệu
        List<TableDetectHivObjectForm> result = detectHivRepositoryImpl.getTableDetectHivObject(
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
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsFrom), TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, aidsTo), gender,
                job, race, testObject, statusResident, statusTreatment, transmission, isPAC(), placeTest, facility, hasHealthInsurance);

        form.setTable(new ArrayList<TableDetectHivObjectForm>());
        TableDetectHivObjectForm item;
        TableDetectHivObjectForm dItem;
        TableDetectHivObjectForm wItem;
        TableDetectHivObjectForm oItem;
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
            item = new TableDetectHivObjectForm();
            item.setStt(String.valueOf(index));
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<TableDetectHivObjectForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID()) || resultDIDs.contains(district.getID())))) {
                        continue;
                    }
                    dItem = new TableDetectHivObjectForm();
//                    dItem.setStt(item.getStt() + "." + dIndex);
                    dItem.setStt(item.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<TableDetectHivObjectForm>());
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
                            wItem = new TableDetectHivObjectForm();
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
        for (TableDetectHivObjectForm row : form.getTable()) {
            for (TableDetectHivObjectForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    row.setNtmt(row.getNtmt() + line.getNtmt());
                    row.setMd(row.getMd() + line.getMd());
                    row.setMsm(row.getMsm() + line.getMsm());
                    row.setVcbtbc(row.getVcbtbc() + line.getVcbtbc());
                    row.setPnmt(row.getPnmt() + line.getPnmt());
                    row.setLao(row.getLao() + line.getLao());
                    row.setKhongthongtin(row.getKhongthongtin() + line.getKhongthongtin());
                    row.setHoalieu(row.getHoalieu() + line.getHoalieu());
                    row.setPhamnhan(row.getPhamnhan() + line.getPhamnhan());
                    row.setMecon(row.getMecon() + line.getMecon());
                    row.setNghiavu(row.getNghiavu() + line.getNghiavu());
                    row.setKhac(row.getKhac() + line.getKhac());

                }

                //Cộng dữ liệu huyện nếu có
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (TableDetectHivObjectForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID())) {
                            pChild.setNtmt(pChild.getNtmt() + line.getNtmt());
                            pChild.setMd(pChild.getMd() + line.getMd());
                            pChild.setMsm(pChild.getMsm() + line.getMsm());
                            pChild.setVcbtbc(pChild.getVcbtbc() + line.getVcbtbc());
                            pChild.setPnmt(pChild.getPnmt() + line.getPnmt());
                            pChild.setLao(pChild.getLao() + line.getLao());
                            pChild.setKhongthongtin(pChild.getKhongthongtin() + line.getKhongthongtin());
                            pChild.setHoalieu(pChild.getHoalieu() + line.getHoalieu());
                            pChild.setPhamnhan(pChild.getPhamnhan() + line.getPhamnhan());
                            pChild.setMecon(pChild.getMecon() + line.getMecon());
                            pChild.setNghiavu(pChild.getNghiavu() + line.getNghiavu());
                            pChild.setKhac(pChild.getKhac() + line.getKhac());
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (TableDetectHivObjectForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID())) {
                                    dChild.setNtmt(dChild.getNtmt() + line.getNtmt());
                                    dChild.setMd(dChild.getMd() + line.getMd());
                                    dChild.setMsm(dChild.getMsm() + line.getMsm());
                                    dChild.setVcbtbc(dChild.getVcbtbc() + line.getVcbtbc());
                                    dChild.setPnmt(dChild.getPnmt() + line.getPnmt());
                                    dChild.setLao(dChild.getLao() + line.getLao());
                                    dChild.setKhongthongtin(dChild.getKhongthongtin() + line.getKhongthongtin());
                                    dChild.setHoalieu(dChild.getHoalieu() + line.getHoalieu());
                                    dChild.setPhamnhan(dChild.getPhamnhan() + line.getPhamnhan());
                                    dChild.setMecon(dChild.getMecon() + line.getMecon());
                                    dChild.setNghiavu(dChild.getNghiavu() + line.getNghiavu());
                                    dChild.setKhac(dChild.getKhac() + line.getKhac());
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
        int ntmt = 0;
        int md = 0;
        int msm = 0;
        int vcbtbc = 0;
        int pnmt = 0;
        int lao = 0;
        int khac = 0;
        int khongthongtin = 0;
        int hoalieu = 0;
        int phamnhan = 0;
        int mecon = 0;
        int nghiavu = 0;
        int total = 0;
        //Thêm item ngoại tỉnh khi không phải vaac
        if (!form.isVAAC()) {
            Map<String, TableDetectHivObjectForm> mOutProvince = new HashMap<>();
            for (TableDetectHivObjectForm line : result) {
                //Cộng dữ liệu ngoại tỉnh
                if (!form.getSiteProvinceID().equals(line.getProvinceID())) {
                    if (mOutProvince.getOrDefault(line.getProvinceID(), null) == null) {
                        mOutProvince.put(line.getProvinceID(), new TableDetectHivObjectForm());
                        mOutProvince.get(line.getProvinceID()).setDisplayName(provinces.get(line.getProvinceID()));
                    }
                    oItem = mOutProvince.get(line.getProvinceID());
                    oItem.setNtmt(oItem.getNtmt() + line.getNtmt());
                    oItem.setMd(oItem.getMd() + line.getMd());
                    oItem.setMsm(oItem.getMsm() + line.getMsm());
                    oItem.setVcbtbc(oItem.getVcbtbc() + line.getVcbtbc());
                    oItem.setPnmt(oItem.getPnmt() + line.getPnmt());
                    oItem.setLao(oItem.getLao() + line.getLao());
                    oItem.setKhongthongtin(oItem.getKhongthongtin() + line.getKhongthongtin());
                    oItem.setHoalieu(oItem.getHoalieu() + line.getHoalieu());
                    oItem.setPhamnhan(oItem.getPhamnhan() + line.getPhamnhan());
                    oItem.setMecon(oItem.getMecon() + line.getMecon());
                    oItem.setNghiavu(oItem.getNghiavu() + line.getNghiavu());
                    oItem.setKhac(oItem.getKhac() + line.getKhac());
                    mOutProvince.put(line.getProvinceID(), oItem);
                }
            }
            if (!mOutProvince.isEmpty()) {
                TableDetectHivObjectForm outProvince = new TableDetectHivObjectForm();
                outProvince.setDisplayName("Tỉnh khác");
                outProvince.setWardID("other");
                outProvince.setStt(String.valueOf(form.getTable().size() + 1));
                outProvince.setChilds(new ArrayList<>(mOutProvince.values()));
                for (TableDetectHivObjectForm line : mOutProvince.values()) {
                    outProvince.setNtmt(outProvince.getNtmt() + line.getNtmt());
                    outProvince.setMd(outProvince.getMd() + line.getMd());
                    outProvince.setMsm(outProvince.getMsm() + line.getMsm());
                    outProvince.setVcbtbc(outProvince.getVcbtbc() + line.getVcbtbc());
                    outProvince.setPnmt(outProvince.getPnmt() + line.getPnmt());
                    outProvince.setLao(outProvince.getLao() + line.getLao());
                    outProvince.setKhongthongtin(outProvince.getKhongthongtin() + line.getKhongthongtin());
                    outProvince.setHoalieu(outProvince.getHoalieu() + line.getHoalieu());
                    outProvince.setPhamnhan(outProvince.getPhamnhan() + line.getPhamnhan());
                    outProvince.setMecon(outProvince.getMecon() + line.getMecon());
                    outProvince.setNghiavu(outProvince.getNghiavu() + line.getNghiavu());
                    outProvince.setKhac(outProvince.getKhac() + line.getKhac());
//                    outProvince.setSum(outProvince.getSum() + line.getSum());
                }
                form.getTable().add(outProvince);
            }

        }

        //Tổng 
        for (TableDetectHivObjectForm model : form.getTable()) {
            ntmt = ntmt + model.getNtmt();
            md = md + model.getMd();
            msm = msm + model.getMsm();
            vcbtbc = vcbtbc + model.getVcbtbc();
            pnmt = pnmt + model.getPnmt();
            lao = lao + model.getLao();
            khac = khac + model.getKhac();
            khongthongtin = khongthongtin + model.getKhongthongtin();
            hoalieu = hoalieu + model.getHoalieu();
            phamnhan = phamnhan + model.getPhamnhan();
            mecon = mecon + model.getMecon();
            nghiavu = nghiavu + model.getNghiavu();
            total = total + model.getSum();
        }

        //set form total
        form.setTotalntmt(ntmt);
        form.setTotalmd(md);
        form.setTotalmsm(msm);
        form.setTotalvcbtbc(vcbtbc);
        form.setTotalpnmt(pnmt);
        form.setTotallao(lao);
        form.setTotalkhac(khac);
        form.setTotalkhongthongtin(khongthongtin);
        form.setTotalhoalieu(hoalieu);
        form.setTotalphamnhan(phamnhan);
        form.setTotalmecon(mecon);
        form.setTotalnghiavu(nghiavu);
        form.setTotal(total);
        return form;
    }

    @GetMapping(value = {"/pac-detecthiv-object/index.html"})
    public String actionDetectHivObjectIndex(Model model,
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
        DetectHivObjectForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo, aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("options", form.getOptions());
        model.addAttribute("isVAAC", form.isVAAC());
        model.addAttribute("isPAC", form.isPAC());
        model.addAttribute("isTTYT", form.isTTYT());
        model.addAttribute("isTYT", form.isTYT());
        model.addAttribute("title", "Báo cáo người nhiễm phát hiện");

        return "report/pac/detecthiv-object";
    }

    @GetMapping(value = {"/pac-detecthiv-object/pdf.html"})
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
        DetectHivObjectForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo, aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);
        HashMap<String, Object> context = new HashMap<>();
        form.setPrintable(true);
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isTYT", isTYT());
        context.put("isPAC", isPAC());
        context.put("isTYT", isTYT());
        context.put("isVAAC", isVAAC());
        return exportPdf(form.getFileName(), TEMPLATE_PDF, context);
    }

    @GetMapping(value = {"/pac-detecthiv-object/excel.html"})
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
        DetectHivObjectForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo, aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);

        return exportExcel(new DetectHivObjectExcel(form, EXTENSION_EXCEL_X));

    }

    @GetMapping(value = {"/pac-detecthiv-object/email.html"})
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
            return redirect(UrlUtils.pacDetectHivObjectReport());
        }
        DetectHivObjectForm form = getData(updateTimeFrom, updateTimeTo, provinceID, districtID, wardID, addressFilter, patientStatus,
                manageTimeFrom, manageTimeTo, inputTimeFrom, inputTimeTo, confirmTimeFrom, confirmTimeTo, aidsFrom, aidsTo, gender, job, race, testObject, statusResident, statusTreatment, transmision, manageStt, levelDisplay, placeTest, facility, hasHealthInsurance);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                form.getTitle(),
                EmailoutboxEntity.TEMPLATE_PAC_DETECT_HIV_OBJECT,
                context,
                new DetectHivObjectExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacDetectHivObjectReport());
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
