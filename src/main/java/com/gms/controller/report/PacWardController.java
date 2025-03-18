package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ManageStatusEnum;
import com.gms.entity.constant.StatusOfResidentEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.PacWardExcel;
import com.gms.entity.form.pac.PacWardForm;
import com.gms.entity.form.pac.PacWardTableForm;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Giám sát phát hiện -> Báo cáo huyện xã
 *
 * @author TrangBN
 */
@Controller(value = "report_pac_ward")
public class PacWardController extends BaseController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;

    @Autowired
    private PacPatientRepositoryImpl patientRepositoryImpl;

    private static final String TEMPLATE_PAC_LOCAL = "report/pac/pac-local-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    public PacWardForm getData(String statusDeath, String manageStatus, String levelDisplay, String toTime, String gender, String testObject, String month, String year) throws Exception {

        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        List<WardEntity> wards = new ArrayList<>();
        List<DistrictEntity> districts = new ArrayList<>();
        Map<String, List<WardEntity>> wardMaps = new HashMap<>();
        List<PacWardTableForm> datas = new LinkedList<>();
        //list trong tỉnh
        List<PacWardTableForm> listDistricts = new ArrayList<>();

        wards.add(locationsService.findWard(currentUser.getSite().getWardID()));
        if (isPAC()) {
            districts.addAll(locationsService.findDistrictByProvinceID(currentUser.getSiteProvince().getID()));
        } else {
            districts.add(locationsService.findDistrict(currentUser.getSite().getDistrictID()));
        }

        for (DistrictEntity district : districts) {
            wardMaps.put(district.getID(), locationsService.findWardByDistrictID(district.getID()));
        }
        String years = "0";
        if (pacPatientService.getFirst(currentUser.getSite().getProvinceID()) != null) {
            years = TextUtils.formatDate(pacPatientService.getFirst(currentUser.getSite().getProvinceID()).getConfirmTime(), "yyyy");
        }

        PacWardForm form = new PacWardForm();
        form.setYears(Integer.valueOf(years));
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setMonth(StringUtils.isEmpty(month) ? TextUtils.formatDate(new Date(), "M") : month);
        form.setYear(StringUtils.isEmpty(year) ? TextUtils.formatDate(new Date(), "yyyy") : year);
        form.setPrintable(false);
        form.setStaffName(currentUser.getUser().getName());

        String endTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(Integer.valueOf(form.getMonth()), Integer.valueOf(form.getYear())), FORMATDATE_SQL);

        if (isPAC()) {
            form.setLevelDisplay(StringUtils.isEmpty(levelDisplay) ? "district" : levelDisplay);
        } else {
            form.setLevelDisplay("ward");
        }
        form.setManageStatus(StringUtils.isEmpty(manageStatus) ? "4" : manageStatus);

        form.setFileName("BaoCaoXa_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setProvinceName(currentUser.getSiteProvince().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, null));
        form.setGenderID(gender);
        form.setObjectID(testObject);
        form.setOptions(getOptions());
        form.setPrint(false);

        // Set tiêu đề
        form.setTitle("Báo cáo số liệu HIV được quản lý");
        switch (form.getManageStatus()) {
            case "-1":
                form.setTitle("Báo cáo số liệu HIV");
                break;
            case "1":
                form.setTitle("Báo cáo số liệu HIV chưa rà soát");
                break;
            case "2":
                form.setTitle("Báo cáo số liệu HIV cần rà soát");
                break;
            case "3":
                form.setTitle("Báo cáo số liệu HIV đã rà soát");
                break;
            case "4":
                form.setTitle("Báo cáo số liệu HIV được quản lý");
                break;
            default:
                form.setTitle("Báo cáo số liệu HIV");
        }

        //Truy vẫn dữ liệu
        List<PacWardTableForm> result = patientRepositoryImpl.getWardTable(statusDeath, form.getManageStatus(), isPAC(), currentUser.getSiteProvince().getID(), isTTYT() || isTYT() ? currentUser.getSite().getDistrictID() : null, isTYT() ? currentUser.getSite().getWardID() : null, toTime, gender, testObject, endTime);

        PacWardTableForm dRow;
        PacWardTableForm dRowUnclear;
        PacWardTableForm wRow;
        PacWardTableForm wRowUnclear;
        PacWardTableForm rowTotal = new PacWardTableForm();
        List<PacWardTableForm> listWards;

        //row total
        for (PacWardTableForm item : result) {
            //dl quản lý
            rowTotal.setHiv(rowTotal.getHiv() + item.getHiv());
            rowTotal.setAids(rowTotal.getAids() + item.getAids());
            rowTotal.setTv(rowTotal.getTv() + item.getTv());
            rowTotal.setUnder15(rowTotal.getUnder15() + item.getUnder15());
            rowTotal.setOver15(rowTotal.getOver15() + item.getOver15());
            //dl hiện trạng
            if (StatusOfResidentEnum.MAT_DAU.getKey().equals(item.getResident())) {
                rowTotal.setMatDau(rowTotal.getMatDau() + item.getCount());
            } else if (StatusOfResidentEnum.KHONG_CO_THUC_TE.getKey().equals(item.getResident())) {
                rowTotal.setKhongCoThucTe(rowTotal.getKhongCoThucTe() + item.getCount());
            } else if (StatusOfResidentEnum.CHUYEN_DI_NOI_KHAC.getKey().equals(item.getResident())) {
                rowTotal.setChuyenDiNoiKhac(rowTotal.getChuyenDiNoiKhac() + item.getCount());
            } else if (StatusOfResidentEnum.CHUA_XAC_DINH.getKey().equals(item.getResident())) {
                rowTotal.setChuaXacDinhDuoc(rowTotal.getChuaXacDinhDuoc() + item.getCount());
            } else if (StatusOfResidentEnum.CHUYEN_TRONG_TINH.getKey().equals(item.getResident())) {
                rowTotal.setChuyenTrongTinh(rowTotal.getChuyenTrongTinh() + item.getCount());
            } else if (StatusOfResidentEnum.DI_LAM_AN_XA.getKey().equals(item.getResident())) {
                rowTotal.setDiLamAnXa(rowTotal.getDiLamAnXa() + item.getCount());
            } else if (StatusOfResidentEnum.DI_TRAI.getKey().equals(item.getResident())) {
                rowTotal.setDiTrai(rowTotal.getDiTrai() + item.getCount());
            } else if (StatusOfResidentEnum.DANG_O_DIA_PHUONG.getKey().equals(item.getResident())) {
                rowTotal.setDangODiaPhuong(rowTotal.getDangODiaPhuong() + item.getCount());
            } else {
                rowTotal.setKhongCoThongTin(rowTotal.getKhongCoThongTin() + item.getCount());
            }
        }

        int index = 1;
        int totalWard = 0;

        //Map giá trị
        for (DistrictEntity district : districts) {
            listWards = new ArrayList<>();
            //Các huyện trong tỉnh
            dRow = new PacWardTableForm();
            dRow.setStt(index++);
            dRow.setCountWard(0);
            for (WardEntity wardEntity : wardMaps.get(district.getID())) {
                if (wardEntity.isActive()) {
                    dRow.setCountWard(dRow.getCountWard() + 1);
                }
            }
            totalWard += dRow.getCountWard();
            dRow.setDistrictID(district.getID());
            dRow.setDistrictName(district.getName());
            dRow.setActive(true);

            //map dữ liệu
            for (PacWardTableForm item : result) {
                //du huyện
                if (district.getID().equals(item.getDistrictID())) {
                    //dl quản lý
                    dRow.setHiv(dRow.getHiv() + item.getHiv());
                    dRow.setAids(dRow.getAids() + item.getAids());
                    dRow.setTv(dRow.getTv() + item.getTv());
                    dRow.setUnder15(dRow.getUnder15() + item.getUnder15());
                    dRow.setOver15(dRow.getOver15() + item.getOver15());
                    //dl hiện trạng
                    if (StatusOfResidentEnum.MAT_DAU.getKey().equals(item.getResident())) {
                        dRow.setMatDau(dRow.getMatDau() + item.getCount());
                    } else if (StatusOfResidentEnum.KHONG_CO_THUC_TE.getKey().equals(item.getResident())) {
                        dRow.setKhongCoThucTe(dRow.getKhongCoThucTe() + item.getCount());
                    } else if (StatusOfResidentEnum.CHUYEN_DI_NOI_KHAC.getKey().equals(item.getResident())) {
                        dRow.setChuyenDiNoiKhac(dRow.getChuyenDiNoiKhac() + item.getCount());
                    } else if (StatusOfResidentEnum.CHUA_XAC_DINH.getKey().equals(item.getResident())) {
                        dRow.setChuaXacDinhDuoc(dRow.getChuaXacDinhDuoc() + item.getCount());
                    } else if (StatusOfResidentEnum.CHUYEN_TRONG_TINH.getKey().equals(item.getResident())) {
                        dRow.setChuyenTrongTinh(dRow.getChuyenTrongTinh() + item.getCount());
                    } else if (StatusOfResidentEnum.DI_LAM_AN_XA.getKey().equals(item.getResident())) {
                        dRow.setDiLamAnXa(dRow.getDiLamAnXa() + item.getCount());
                    } else if (StatusOfResidentEnum.DI_TRAI.getKey().equals(item.getResident())) {
                        dRow.setDiTrai(dRow.getDiTrai() + item.getCount());
                    } else if (StatusOfResidentEnum.DANG_O_DIA_PHUONG.getKey().equals(item.getResident())) {
                        dRow.setDangODiaPhuong(dRow.getDangODiaPhuong() + item.getCount());
                    } else {
                        dRow.setKhongCoThongTin(dRow.getKhongCoThongTin() + item.getCount());
                    }

                }
            }
            listDistricts.add(dRow);
            if (isPAC()) {
                datas.add(dRow);
            }

            int indexWard = 1;
            for (WardEntity wardEntity : isTYT() ? wards : wardMaps.get(district.getID())) {
                //Các xã trong tỉnh
                wRow = new PacWardTableForm();
                wRow.setWardID(wardEntity.getID());
                if (wardEntity.isActive()) {
                    wRow.setStt(indexWard++);
                }
                wRow.setWardName(wardEntity.getName());
                wRow.setActive(wardEntity.isActive());

                //map dữ liệu
                for (PacWardTableForm item : result) {
                    //du huyện
                    if (wardEntity.getID().equals(item.getWardID())) {
                        //dl quản lý
                        wRow.setHiv(wRow.getHiv() + item.getHiv());
                        wRow.setAids(wRow.getAids() + item.getAids());
                        wRow.setTv(wRow.getTv() + item.getTv());
                        wRow.setUnder15(wRow.getUnder15() + item.getUnder15());
                        wRow.setOver15(wRow.getOver15() + item.getOver15());
                        //dl hiện trạng
                        if (StatusOfResidentEnum.MAT_DAU.getKey().equals(item.getResident())) {
                            wRow.setMatDau(wRow.getMatDau() + item.getCount());
                        } else if (StatusOfResidentEnum.KHONG_CO_THUC_TE.getKey().equals(item.getResident())) {
                            wRow.setKhongCoThucTe(wRow.getKhongCoThucTe() + item.getCount());
                        } else if (StatusOfResidentEnum.CHUYEN_DI_NOI_KHAC.getKey().equals(item.getResident())) {
                            wRow.setChuyenDiNoiKhac(wRow.getChuyenDiNoiKhac() + item.getCount());
                        } else if (StatusOfResidentEnum.CHUA_XAC_DINH.getKey().equals(item.getResident())) {
                            wRow.setChuaXacDinhDuoc(wRow.getChuaXacDinhDuoc() + item.getCount());
                        } else if (StatusOfResidentEnum.CHUYEN_TRONG_TINH.getKey().equals(item.getResident())) {
                            wRow.setChuyenTrongTinh(wRow.getChuyenTrongTinh() + item.getCount());
                        } else if (StatusOfResidentEnum.DI_LAM_AN_XA.getKey().equals(item.getResident())) {
                            wRow.setDiLamAnXa(wRow.getDiLamAnXa() + item.getCount());
                        } else if (StatusOfResidentEnum.DI_TRAI.getKey().equals(item.getResident())) {
                            wRow.setDiTrai(wRow.getDiTrai() + item.getCount());
                        } else if (StatusOfResidentEnum.DANG_O_DIA_PHUONG.getKey().equals(item.getResident())) {
                            wRow.setDangODiaPhuong(wRow.getDangODiaPhuong() + item.getCount());
                        } else {
                            wRow.setKhongCoThongTin(wRow.getKhongCoThongTin() + item.getCount());
                        }

                    }
                }
                //thêm xã thường
                listWards.add(wRow);
                if (form.getLevelDisplay().equals("ward")) {
                    datas.add(wRow);
                }

            }
            //thêm xã không rõ
            wRowUnclear = new PacWardTableForm();
            wRowUnclear.setWardID("-1");
            wRowUnclear.setStt(indexWard);
            wRowUnclear.setWardName("Không rõ");
            wRowUnclear.setActive(true);

            //dl quản lý
            wRowUnclear.setHiv(dRow.getHiv() - sumList(listWards, "getHiv"));
            wRowUnclear.setAids(dRow.getAids() - sumList(listWards, "getAids"));
            wRowUnclear.setTv(dRow.getTv() - sumList(listWards, "getTv"));
            wRowUnclear.setUnder15(dRow.getUnder15() - sumList(listWards, "getUnder15"));
            wRowUnclear.setOver15(dRow.getOver15() - sumList(listWards, "getOver15"));

            wRowUnclear.setMatDau(dRow.getMatDau() - sumList(listWards, "getMatDau"));
            wRowUnclear.setKhongCoThucTe(dRow.getKhongCoThucTe() - sumList(listWards, "getKhongCoThucTe"));
            wRowUnclear.setChuyenDiNoiKhac(dRow.getChuyenDiNoiKhac() - sumList(listWards, "getChuyenDiNoiKhac"));
            wRowUnclear.setChuaXacDinhDuoc(dRow.getChuaXacDinhDuoc() - sumList(listWards, "getChuaXacDinhDuoc"));
            wRowUnclear.setChuyenTrongTinh(dRow.getChuyenTrongTinh() - sumList(listWards, "getChuyenTrongTinh"));
            wRowUnclear.setDiLamAnXa(dRow.getDiLamAnXa() - sumList(listWards, "getDiLamAnXa"));
            wRowUnclear.setDiTrai(dRow.getDiTrai() - sumList(listWards, "getDiTrai"));
            wRowUnclear.setDangODiaPhuong(dRow.getDangODiaPhuong() - sumList(listWards, "getDangODiaPhuong"));
            wRowUnclear.setKhongCoThongTin(dRow.getKhongCoThongTin() - sumList(listWards, "getKhongCoThongTin"));
            if (form.getLevelDisplay().equals("ward") && wRowUnclear.getHiv() > 0) {
                datas.add(wRowUnclear);
            }

        }
        //thêm huyện không rõ
        dRowUnclear = new PacWardTableForm();
        dRowUnclear.setDistrictID("-1");
        dRowUnclear.setDistrictName("Không rõ");
        dRowUnclear.setStt(index++);
        dRowUnclear.setActive(true);
        //dl quản lý
        dRowUnclear.setHiv(rowTotal.getHiv() - sumList(listDistricts, "getHiv"));
        dRowUnclear.setAids(rowTotal.getAids() - sumList(listDistricts, "getAids"));
        dRowUnclear.setTv(rowTotal.getTv() - sumList(listDistricts, "getTv"));
        dRowUnclear.setUnder15(rowTotal.getUnder15() - sumList(listDistricts, "getUnder15"));
        dRowUnclear.setOver15(rowTotal.getOver15() - sumList(listDistricts, "getOver15"));

        dRowUnclear.setMatDau(rowTotal.getMatDau() - sumList(listDistricts, "getMatDau"));
        dRowUnclear.setKhongCoThucTe(rowTotal.getKhongCoThucTe() - sumList(listDistricts, "getKhongCoThucTe"));
        dRowUnclear.setChuyenDiNoiKhac(rowTotal.getChuyenDiNoiKhac() - sumList(listDistricts, "getChuyenDiNoiKhac"));
        dRowUnclear.setChuaXacDinhDuoc(rowTotal.getChuaXacDinhDuoc() - sumList(listDistricts, "getChuaXacDinhDuoc"));
        dRowUnclear.setChuyenTrongTinh(rowTotal.getChuyenTrongTinh() - sumList(listDistricts, "getChuyenTrongTinh"));
        dRowUnclear.setDiLamAnXa(rowTotal.getDiLamAnXa() - sumList(listDistricts, "getDiLamAnXa"));
        dRowUnclear.setDiTrai(rowTotal.getDiTrai() - sumList(listDistricts, "getDiTrai"));
        dRowUnclear.setDangODiaPhuong(rowTotal.getDangODiaPhuong() - sumList(listDistricts, "getDangODiaPhuong"));
        dRowUnclear.setKhongCoThongTin(rowTotal.getKhongCoThongTin() - sumList(listDistricts, "getKhongCoThongTin"));

        if (isPAC() && dRowUnclear.getHiv() > 0) {
            datas.add(dRowUnclear);
        }
        rowTotal.setCountWard(totalWard);
        form.setTableTotal(rowTotal);
        form.setTable(datas);
        return form;
    }

    /**
     *
     * @param model
     * @param endDate
     * @param gender
     * @param testObject
     * @param levelDisplay
     * @param manageStatus
     * @param month
     * @param year
     * @param permanentWardID
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-ward/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "gender", defaultValue = "") String gender,
            @RequestParam(name = "test_object", defaultValue = "") String testObject,
            @RequestParam(name = "level_display", defaultValue = "") String levelDisplay,
            @RequestParam(name = "manage_status", defaultValue = "") String manageStatus,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "statusDeath", required = false, defaultValue = "") String statusDeath,
            RedirectAttributes redirectAttributes) throws Exception {

        PacWardForm form = getData(statusDeath, manageStatus, levelDisplay, endDate, gender, testObject, month, year);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("isVAAC", isVAAC());

        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.backendHome());
        }

        HashMap<String, String> statusDeaths = new LinkedHashMap<>();
        statusDeaths.put("", "Tất cả");
        statusDeaths.put("1", "Ngày tử vong");
        statusDeaths.put("2", "Ngày báo tử vong");

        model.addAttribute("statusDeaths", statusDeaths);
        model.addAttribute("title", "Báo cáo số liệu HIV");
        model.addAttribute("titleBreacum", "Báo cáo số liệu HIV");
        return "report/pac/pac-ward";
    }

    @GetMapping(value = {"/pac-ward/excel.html"})
    public ResponseEntity<InputStreamResource> actionExport(Model model,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "gender", defaultValue = "") String gender,
            @RequestParam(name = "test_object", defaultValue = "") String testObject,
            @RequestParam(name = "level_display", defaultValue = "") String levelDisplay,
            @RequestParam(name = "manage_status", defaultValue = "") String manageStatus,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "statusDeath", required = false, defaultValue = "") String statusDeath) throws Exception {

        PacWardForm form = getData(statusDeath, manageStatus, levelDisplay, endDate, gender, testObject, month, year);
        return exportExcel(new PacWardExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-ward/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "gender", defaultValue = "") String gender,
            @RequestParam(name = "test_object", defaultValue = "") String testObject,
            @RequestParam(name = "level_display", defaultValue = "") String levelDisplay,
            @RequestParam(name = "manage_status", defaultValue = "") String manageStatus,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "statusDeath", required = false, defaultValue = "") String statusDeath
    ) throws Exception {
        PacWardForm form = getData(statusDeath, manageStatus, levelDisplay, endDate, gender, testObject, month, year);

        HashMap<String, Object> context = new HashMap<>();
        form.setPrint(true);
        form.setPrintable(true);
        context.put("form", form);

        return exportPdf(form.getFileName(), "report/pac/pac-ward-pdf.html", context);
    }

    @GetMapping(value = {"/pac-ward/email.html"})
    public String actionEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "gender", defaultValue = "") String gender,
            @RequestParam(name = "test_object", defaultValue = "") String testObject,
            @RequestParam(name = "level_display", defaultValue = "") String levelDisplay,
            @RequestParam(name = "manage_status", defaultValue = "") String manageStatus,
            @RequestParam(name = "month", defaultValue = "") String month,
            @RequestParam(name = "year", defaultValue = "") String year,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "statusDeath", required = false, defaultValue = "") String statusDeath
    ) throws Exception {
        PacWardForm form = getData(statusDeath, manageStatus, levelDisplay, endDate, gender, testObject, month, year);

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacWardIndex());
        }
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                form.getTitle(),
                EmailoutboxEntity.TEMPLATE_PAC_WARD,
                context,
                new PacWardExcel(form, EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacWardIndex());
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

        dateToValidate = dateToValidate.toLowerCase();
        if (dateToValidate.contains("d") || dateToValidate.contains("m") || dateToValidate.contains("y")) {
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
        String manageStatus = "manageStatus";
        options.put(manageStatus, new LinkedHashMap<String, String>());
        options.get(manageStatus).put("-1", "Tất cả");
        options.get(manageStatus).put(ManageStatusEnum.NN_PHAT_HIEN.getKey(), ManageStatusEnum.NN_PHAT_HIEN.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_CAN_RA_SOAT.getKey(), ManageStatusEnum.NN_CAN_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_DA_RA_SOAT.getKey(), ManageStatusEnum.NN_DA_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_QUAN_LY.getKey(), ManageStatusEnum.NN_QUAN_LY.getLabel());

        return options;
    }

    private int sumList(List<PacWardTableForm> list, String value) {
        List<Integer> values = new ArrayList<>();
        if (list.isEmpty()) {
            return 0;
        }
        values.addAll(CollectionUtils.collect(list, TransformerUtils.invokerTransformer(value)));// get site ID
        int sum = 0;
        for (Integer i : values) {
            sum += i;
        }
        return sum;
    }
}
