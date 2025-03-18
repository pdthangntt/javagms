package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.LocalExcel;
import com.gms.entity.form.pac.PacLocalForm;
import com.gms.entity.form.pac.PacLocalTableForm;
import com.gms.repository.impl.PacLocalRepositoryImpl;
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
 * Giám sát phát hiện -> Báo cáo huyện xã
 *
 * @author TrangBN
 */
@Controller(value = "report_pac_local")
public class PacLocalController extends BaseController {

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;

    @Autowired
    private PacLocalRepositoryImpl localImpl;

    private static final String TEMPLATE_PAC_LOCAL = "report/pac/pac-local-pdf.html";
    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    /**
     *
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param fromTime
     * @param toTime
     * @param levelDisplay
     * @return
     * @throws Exception
     */
    public PacLocalForm getData(String permanentProvinceID, String permanentDistrictID, String permanentWardID, String fromTime, String toTime, String levelDisplay) throws Exception {

        //Khởi tạo biến
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> rowProvince = new LinkedHashMap();
        Map<String, List<WardEntity>> rowWard = new LinkedHashMap<>();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        Set<String> pIDsDisplay = new HashSet<>();
        Set<String> dIDsDisplay = new HashSet<>();
        Set<String> wIDsDisplay = new HashSet<>();

        PacPatientInfoEntity entity = isVAAC() ? pacPatientService.getFirst() : pacPatientService.getFirst(getCurrentUser().getSite().getProvinceID());
        if (entity == null || entity.getConfirmTime() == null) {
            entity = new PacPatientInfoEntity();
            entity.setConfirmTime(TextUtils.convertDate("01/01/1990", FORMATDATE));
        }

        // Mặc đinh hiển thị tỉnh huyện xã hiện tại title bảng
        boolean defaultLoggin = StringUtils.isEmpty(permanentProvinceID) && StringUtils.isEmpty(permanentDistrictID) && StringUtils.isEmpty(permanentWardID);
        if (defaultLoggin) {
            if (isTYT()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
                dIDsDisplay.add(currentUser.getSiteDistrict().getID());
                wIDsDisplay.add(currentUser.getSiteWard().getID());
                pIDs.add(currentUser.getSiteProvince().getID());
                dIDs.add(currentUser.getSiteDistrict().getID());
                wIDs.add(currentUser.getSiteWard().getID());
                permanentProvinceID = currentUser.getSiteProvince().getID();
                permanentDistrictID = currentUser.getSiteDistrict().getID();
                permanentWardID = currentUser.getSiteWard().getID();
            }
            if (isTTYT()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
                dIDsDisplay.add(currentUser.getSiteDistrict().getID());
                pIDs.add(currentUser.getSiteProvince().getID());
                dIDs.add(currentUser.getSiteDistrict().getID());
                permanentProvinceID = currentUser.getSiteProvince().getID();
                permanentDistrictID = currentUser.getSiteDistrict().getID();
            }
            if (isPAC()) {
                pIDsDisplay.add(currentUser.getSiteProvince().getID());
                pIDs.add(currentUser.getSiteProvince().getID());
                permanentProvinceID = currentUser.getSiteProvince().getID();
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

        PacLocalForm form = new PacLocalForm();
        form.setVAAC(isVAAC());
        form.setPAC(isPAC());
        form.setTTYT(isTTYT());
        form.setTYT(isTYT());
        form.setDefaultSearchPlace(defaultLoggin);
        form.setLevelDisplay(levelDisplay);
        form.setFileName("BaoCaoTheoHuyenXa_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteProvince(currentUser.getSiteProvince().getName().contains("Tỉnh") || currentUser.getSiteProvince().getName().contains("Thành phố") ? currentUser.getSiteProvince().getName().replace("Tỉnh", "").replace("Thành phố", "")
                : currentUser.getSiteProvince().getName().contains("Huyện") ? currentUser.getSiteProvince().getName().replace("Huyện", "")
                : currentUser.getSiteProvince().getName());
        form.setProvinceName(currentUser.getSiteProvince().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitleLocation(super.getTitleAddress(pIDs, dIDs, wIDs));
        form.setTitleLocationDisplay(super.getTitleAddress(new ArrayList<>(pIDsDisplay), new ArrayList<>(dIDsDisplay), new ArrayList<>(wIDsDisplay)));

        // Chuẩn hóa ngày
        fromTime = isThisDateValid(fromTime) ? fromTime : null;
        toTime = isThisDateValid(toTime) ? toTime : null;

        fromTime = StringUtils.isNotEmpty(fromTime) ? fromTime : TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE);
        toTime = StringUtils.isNotEmpty(toTime) ? toTime : TextUtils.formatDate(new Date(), FORMATDATE);

        // Set điều kiện tìm kiếm
        form.setProvince(permanentProvinceID);
        form.setDistrict(permanentDistrictID);
        form.setWard(permanentWardID);
        form.setFromTime(fromTime);
        form.setToTime(toTime);

        // Lấy ds tỉnh huyện xã
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
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "ward" : levelDisplay;
            form.setSiteProvinceID(currentUser.getSite().getProvinceID());
            //Chỉ hiển thị 1 tỉnh duy nhất
            rowProvince.put(form.getSiteProvinceID(), currentUser.getSiteProvince().getName());
        } else {
            //VAAC
            levelDisplay = levelDisplay == null || "".equals(levelDisplay) ? "district" : levelDisplay;
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

        // Set tiêu đề
        if (form.isVAAC()) {
            switch (levelDisplay) {
                case "province":
                    form.setTitle("Báo cáo số tỉnh/thành phố có người nhiễm HIV");
                    break;
                case "district":
                    form.setTitle("Báo cáo số quận/huyện có người nhiễm HIV");
                    break;
                case "ward":
                    form.setTitle("Báo cáo số phường/xã có người nhiễm HIV");
                    break;
                default:
                    form.setTitle("");
            }
        } else {
            form.setTitle("Báo cáo số phường/xã có người nhiễm HIV");
        }

        //Truy vẫn dữ liệu
        List<PacLocalTableForm> result = localImpl.getData(pIDs, dIDs, wIDs, form.getSiteProvinceID(), form.getSiteDistrictID(), form.getSiteWardID(),
                levelDisplay,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, form.getFromTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE_SQL, form.getToTime()),
                form.isVAAC());
        form.setTable(new ArrayList<PacLocalTableForm>());
        PacLocalTableForm item;
        PacLocalTableForm dItem;
        PacLocalTableForm wItem;
        int index = 1;

        for (Map.Entry<String, String> entry : rowProvince.entrySet()) {
            String pID = entry.getKey();
            String pName = entry.getValue();

            //Tìm kiếm tỉnh
            if (!pIDs.isEmpty() && !pIDs.contains(pID)) {
                continue;
            }
            item = new PacLocalTableForm();
            item.setStt(index);
            item.setProvinceID(pID);
            item.setDisplayName(pName);

            //Lặp huyện
            if (levelDisplay.equals("district") || levelDisplay.equals("ward")) {
                int dIndex = 1;
                item.setChilds(new ArrayList<PacLocalTableForm>());
                for (Map.Entry<String, DistrictEntity> mDistrict : districts.entrySet()) {
                    DistrictEntity district = mDistrict.getValue();
                    //Tìm kiếm huyện: huyện thuộc tỉnh, khi tìm kiếm thì phải nằm trong ds, khi có province quản lý thì phải nằm trong
                    if (!item.getProvinceID().equals(district.getProvinceID())
                            || (!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                            || (form.getSiteDistrictID() != null && !(form.getSiteDistrictID().equals(district.getID())))) {
                        continue;
                    }
                    dItem = new PacLocalTableForm();
                    dItem.setStt(dItem.getStt() + dIndex);
                    dItem.setProvinceID(district.getProvinceID());
                    dItem.setDistrictID(district.getID());
                    dItem.setDisplayName(district.getName());
                    dIndex++;
                    //Lặp xã
                    if (levelDisplay.equals("ward")) {
                        dItem.setChilds(new ArrayList<PacLocalTableForm>());
                        //Trong trương hợp VAAC => xã được lấy hết ra ở phía trên
                        List<WardEntity> wards = rowWard.getOrDefault(dItem.getDistrictID(), null);
                        if (wards == null) {
                            //Trường hợp pac, ttyt và tyt thì xã load từng phần
                            wards = locationsService.findWardByDistrictID(dItem.getDistrictID());
                        }
                        for (WardEntity ward : wards) {
                            //Tìm kiếm xã: khi tìm kiếm hoặc khi vào tuyến xã
                            if ((!wIDs.isEmpty() && !wIDs.contains(ward.getID()))
                                    || (form.getSiteWardID() != null && !(form.getSiteWardID().equals(ward.getID())))) {
                                continue;
                            }

                            wItem = new PacLocalTableForm();
                            wItem.setProvinceID(dItem.getProvinceID());
                            wItem.setDistrictID(wItem.getDistrictID());
                            wItem.setWardID(ward.getID());
                            wItem.setDisplayName(ward.getName());
                            //Thêm xã vào huyện
                            dItem.getChilds().add(wItem);
                        }

                        // Thêm vào danh sách xã mặc định cua huyện khi login
                        if (form.isTTYT() && form.isDefaultSearchPlace() && dItem.getDistrictID().equals(form.getSiteDistrictID())) {
                            List<PacLocalTableForm> childs = new ArrayList<>(dItem.getChilds());
                            Set<String> childsResult = new HashSet<>(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getWardID")));
                            for (WardEntity wardEntity : wardsDefault) {
                                if (!childsResult.contains(wardEntity.getID())) {
                                    wItem = new PacLocalTableForm();
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
                                wItem = new PacLocalTableForm();
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
        List<String> provinceIDs = new ArrayList<>();
        List<String> districtIDs = new ArrayList<>();
        List<String> wardIDs = new ArrayList<>();

        for (PacLocalTableForm row : form.getTable()) {
            for (PacLocalTableForm line : result) {
                //Cộng dữ liệu theo tỉnh
                if (row.getProvinceID().equals(line.getProvinceID())) {
                    form.setOutProvinceTotal(form.getOutProvinceTotal() + line.getOutprovince());

                    if (form.isVAAC() && form.getLevelDisplay().equals("province")) {
                        row.setNumAlive(row.getNumAlive() + line.getNumAlive());
                        row.setNumDead(row.getNumDead() + line.getNumDead());

                        form.setAliveTotal(form.getAliveTotal() + line.getNumAlive());
                        form.setDeadTotal(form.getDeadTotal() + line.getNumDead());
                        form.setSum(form.getSum() + line.getCumulative());
                    }

                    // Đếm các tỉnh có dữ liệu
                    if (form.getLevelDisplay().equals("province") && form.isVAAC() && line.getCumulative() != 0 && !provinceIDs.contains(line.getProvinceID())) {
                        form.setProvinceTotal(form.getProvinceTotal() + 1);
                        provinceIDs.add(line.getProvinceID());
                    }
                }

                //Cộng dữ liệu huyện nếu có 
                if (row.getChilds() != null && !row.getChilds().isEmpty()) {
                    for (PacLocalTableForm pChild : row.getChilds()) {
                        if (pChild.getDistrictID().equals(line.getDistrictID()) && line.getProvinceID().equals(row.getProvinceID())) {
                            pChild.setOutprovince(pChild.getOutprovince() + line.getOutprovince());

                            if (form.isVAAC() && form.getLevelDisplay().equals("district")) {
                                pChild.setNumAlive(pChild.getNumAlive() + line.getNumAlive());
                                pChild.setNumDead(pChild.getNumDead() + line.getNumDead());

                                row.setNumAlive(row.getNumAlive() + line.getNumAlive());
                                row.setNumDead(row.getNumDead() + line.getNumDead());
                            }

                            form.setNotReviewTotal(form.getNotReviewTotal() + line.getNotReviewYet());
                            form.setNeedReviewTotal(form.getNeedReviewTotal() + line.getNeedReview());
                            form.setReviewedTotal(form.getReviewedTotal() + line.getReviewed());

                            // Đếm các huyện có dữ liệu
                            if (form.getLevelDisplay().equals("district") && form.isVAAC() && line.getCumulative() != 0 && !districtIDs.contains(line.getDistrictID())) {
                                form.setDistrictTotal(form.getDistrictTotal() + 1);
                                districtIDs.add(line.getDistrictID());
                            }

                            if ((form.isVAAC() && form.getLevelDisplay().equals("district"))) {
                                form.setAliveTotal(form.getAliveTotal() + line.getNumAlive());
                                form.setDeadTotal(form.getDeadTotal() + line.getNumDead());
                                form.setSum(form.getSum() + line.getCumulative());
                            }
                        }
                        //Cộng dữ liệu xã nếu có
                        if (pChild.getChilds() != null && !pChild.getChilds().isEmpty()) {
                            for (PacLocalTableForm dChild : pChild.getChilds()) {
                                if (dChild.getWardID().equals(line.getWardID()) && line.getDistrictID().equals(pChild.getDistrictID()) && line.getProvinceID().equals(pChild.getProvinceID())) {
                                    dChild.setOutprovince(dChild.getOutprovince() + line.getOutprovince());
                                    if (form.getLevelDisplay().equals("ward")) {
                                        dChild.setNumAlive(dChild.getNumAlive() + line.getNumAlive());
                                        dChild.setNumDead(dChild.getNumDead() + line.getNumDead());
                                        pChild.setNumAlive(pChild.getNumAlive() + line.getNumAlive());
                                        pChild.setNumDead(pChild.getNumDead() + line.getNumDead());

                                        row.setNumAlive(row.getNumAlive() + line.getNumAlive());
                                        row.setNumDead(row.getNumDead() + line.getNumDead());
                                    }

                                    dChild.setNotReviewYet(dChild.getNotReviewYet() + line.getNotReviewYet());
                                    dChild.setNeedReview(dChild.getNeedReview() + line.getNeedReview());
                                    dChild.setReviewed(dChild.getReviewed() + line.getReviewed());

                                    pChild.setNotReviewYet(pChild.getNotReviewYet() + line.getNotReviewYet());
                                    pChild.setNeedReview(pChild.getNeedReview() + line.getNeedReview());
                                    pChild.setReviewed(pChild.getReviewed() + line.getReviewed());

                                    // Đếm các huyện có dữ liệu TH VAAC
                                    if (form.getLevelDisplay().equals("ward") && form.isVAAC() && line.getCumulative() != 0 && !wardIDs.contains(line.getWardID())) {
                                        form.setWardTotal(form.getWardTotal() + 1);
                                        wardIDs.add(line.getWardID());
                                    }

                                    // Đếm các huyện có dữ liệu TH ko phải VAAC
                                    if (form.getLevelDisplay().equals("ward") && !form.isVAAC() && !wardIDs.contains(line.getWardID()) && (line.getCumulative() != 0 || line.getNotReviewYet() != 0 || line.getNeedReview() != 0 || line.getReviewed() != 0 || line.getOutprovince() != 0)) {
                                        form.setWardTotal(form.getWardTotal() + 1);
                                        wardIDs.add(line.getWardID());
                                    }

                                    // Tính tổng cộng lũy tích
                                    if (!form.isVAAC() || (form.isVAAC() && form.getLevelDisplay().equals("ward"))) {
                                        form.setAliveTotal(form.getAliveTotal() + line.getNumAlive());
                                        form.setDeadTotal(form.getDeadTotal() + line.getNumDead());
                                        form.setSum(form.getSum() + line.getCumulative());
                                    }
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
        return form;
    }

    /**
     * Ds BC huyện xã
     *
     * @param model
     * @param startDate
     * @param endDate
     * @param levelDisplay
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-local/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID) throws Exception {

        PacLocalForm form = getData(permanentProvinceID, permanentDistrictID, permanentWardID, startDate, endDate, levelDisplay);
        form.setPrintable(false);
        model.addAttribute("form", form);
        model.addAttribute("title", "Báo cáo theo huyện/xã");
        model.addAttribute("titleBreacum", "Báo cáo theo huyện xã");
        return "report/pac/pac-local.html";
    }

    /**
     * In BC huyện xã
     *
     * @param startDate
     * @param endDate
     * @param levelDisplay
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-local/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID) throws Exception {

        HashMap<String, Object> context = new HashMap<>();
        PacLocalForm form = getData(permanentProvinceID, permanentDistrictID, permanentWardID, startDate, endDate, levelDisplay);
        form.setPrintable(true);
        context.put("form", form);
        context.put("isTYT", isTYT());
        return exportPdf(form.getFileName(), TEMPLATE_PAC_LOCAL, context);
    }

    /**
     * Xuất excel
     *
     * @param startDate
     * @param endDate
     * @param levelDisplay
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-local/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID) throws Exception {
        return exportExcel(new LocalExcel(getData(permanentProvinceID, permanentDistrictID, permanentWardID, startDate, endDate, levelDisplay), EXTENSION_EXCEL_X));
    }

    /**
     * Gửi email
     *
     * @param redirectAttributes
     * @param startDate
     * @param endDate
     * @param levelDisplay
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-local/email.html"})
    public String actionEmail(RedirectAttributes redirectAttributes,
            @RequestParam(name = "from_time", defaultValue = "") String startDate,
            @RequestParam(name = "to_time", defaultValue = "") String endDate,
            @RequestParam(name = "level_display", required = false, defaultValue = "") String levelDisplay,
            @RequestParam(name = "province_id", required = false, defaultValue = "") String permanentProvinceID,
            @RequestParam(name = "district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "ward_id", required = false, defaultValue = "") String permanentWardID) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacLocalIndex());
        }
        PacLocalForm form = getData(permanentProvinceID, permanentDistrictID, permanentWardID, startDate, endDate, levelDisplay);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo theo huyện xã ( %s)", TextUtils.formatDate(new Date(), "dd/MM/yyyy")),
                EmailoutboxEntity.TEMPLATE_PAC_LOCAL,
                context,
                new LocalExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacLocalIndex());
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
}
