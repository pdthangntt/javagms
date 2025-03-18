package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.ReferralSourceEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.HtcElogImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.json.Locations;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_elog_grid")
public class HtcElogController extends BaseController<HtcElogImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 9;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private HtcVisitService htcVisitService;

    /**
     * Lay danh sach co so dieu tri cua htc elog
     *
     * @return
     */
    @Cacheable(value = "getTreatmentFacilityElog")
    private HashMap<String, String> getTreatmentFacilityElog() {
        HashMap<String, String> options = new HashMap<>();
        try {
            InputStream file = TextUtils.getFile("data/elog/co_so_dieu_tri.xlsx");
            Workbook workbook = ExcelUtils.getWorkbook(file, "co_so_dieu_tri.xlsx");
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() <= 1) {
                    continue;
                }
                options.put(String.format("%s", new Double(String.valueOf(row.getCell(0))).intValue()), String.valueOf(row.getCell(1)).trim());
            }
        } catch (Exception e) {
        }
        return options;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        if (options != null) {
            return options;
        }
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<String, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getHivInfoCode() == null || entity.getHivInfoCode().equals("")) {
                continue;
            }
            options.get(entity.getType()).put(entity.getElogCode(), entity.getCode());
        }

        Locations data = locationsService.findAll();
        //Tỉnh thành
        options.put("provinces", new HashMap<String, String>());
        for (ProvinceEntity provinceEntity : data.getProvince()) {
            options.get("provinces").put(provinceEntity.getElogCode(), provinceEntity.getID());
        }
        //Quận huyện
        options.put("districts", new HashMap<String, String>());
        for (DistrictEntity districtEntity : data.getDistrict()) {
            options.get("districts").put(districtEntity.getElogCode(), districtEntity.getID());
        }

        options.put(ParameterEntity.TREATMENT_FACILITY, getTreatmentFacilityElog());
        return options;
    }

    public HashMap<String, String> getWard(String districtID) {
        HashMap<String, String> wards = new HashMap<>();
        List<WardEntity> list = locationsService.findWardByDistrictID(districtID);
        for (WardEntity wardEntity : list) {
            wards.put(wardEntity.getElogCode(), wardEntity.getID());
        }
        return wards;
    }

    public HashMap<String, String> getWardName(String districtID, String nameWard) {
        HashMap<String, String> wards = new HashMap<>();
        List<WardEntity> list = locationsService.findWardByDistrictID(districtID);
        for (WardEntity wardEntity : list) {
            if (wardEntity.getName().toLowerCase().contains(nameWard)) {
                wards.put(nameWard, wardEntity.getID());
            }
        }
        return wards;
    }

    public String getSiteByName(String siteName) {
        String siteId = null;
        List<SiteEntity> list = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        for (SiteEntity siteEntity : list) {
            if (siteEntity.getName().trim().toLowerCase().equals(siteName)) {
                siteId = siteEntity.getID().toString();
            }
        }
        return siteId;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-elog-grid/index.html");
        form.setSmallUrl("/import-elog-grid/index.html");
        form.setReadUrl("/import-elog-grid/index.html");
        form.setTitle("Tải file excel của HTC Elog");
        form.setSmallTitle("Tư vấn & Xét nghiệm");
        form.setTemplate(fileTemplate("elog_grid.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "no");
        cols.put("1", "siteCode");
        cols.put("2", "code");
        cols.put("3", "service");
        cols.put("4", "targer");//biến tạm
        cols.put("5", "laytestStaff");
        cols.put("6", "patientName");
        cols.put("7", "patientPhone");
        cols.put("8", "patientID");
        cols.put("9", "raceID");
        cols.put("10", "genderID");
        cols.put("11", "yearOfBirth");
        cols.put("12", "permanentAddress");
        cols.put("13", "permanentWardID");
        cols.put("14", "permanentDistrictID");
        cols.put("15", "permanentProvinceID");
        cols.put("16", "currentAddress");
        cols.put("17", "currentWardID");
        cols.put("18", "currentDistrictID");
        cols.put("19", "currentProvinceID");
        cols.put("20", "jobID");
        cols.put("21", "objectGroupID_ncmt");
        cols.put("22", "objectGroupID_pnbd");
        cols.put("23", "objectGroupID_pnmangthai");
        cols.put("24", "objectGroupID_nguoihienmau");
        cols.put("25", "objectGroupID_lao");
        cols.put("26", "objectGroupID_laytruyenquatd");
        cols.put("27", "objectGroupID_nghiavuquansu");
        cols.put("28", "objectGroupID_msm");
        cols.put("29", "objectGroupID_chuyengioi");
        cols.put("30", "objectGroupID_vcbantinhnguoinhiem");
        cols.put("31", "objectGroupID_vcbantinhnguoincc");
        cols.put("32", "objectGroupID_benhnhannghingoaids");
        cols.put("33", "objectGroupID_other");
        cols.put("34", "riskBehaviorID_tiemtrich");
        cols.put("45", "riskBehaviorID_quanhetinhducnguoimuabandam");
        cols.put("36", "riskBehaviorID_msm");
        cols.put("37", "riskBehaviorID_quanhenhieunguoi");
        cols.put("38", "riskBehaviorID_other");
        cols.put("39", "referralSource_tiepcancongdong");
        cols.put("40", "referralSource_xetnghiemtheodau");
        cols.put("41", "referralSource_canboyte");
        cols.put("42", "referralSource_mangxahoi");
        cols.put("43", "referralSource_other");
        cols.put("44", "causer");//biến tạm
        cols.put("45", "approacherNo");
        cols.put("46", "youInjectCode");
        cols.put("47", "preTestTime");
        cols.put("48", "isAgreePreTest");
        cols.put("49", "testResultsID");
        cols.put("50", "result");//biến tạm
        cols.put("51", "resultsTime");
        cols.put("52", "isAgreeTest");
        cols.put("53", "siteVisit");
        cols.put("54", "siteConfirmTest");
        cols.put("55", "confirmTestNo");
        cols.put("56", "c2");//biến tạm
        cols.put("57", "confirmResultsID");
        cols.put("58", "earlyHiv");
        cols.put("59", "modeOfTransmission");
        cols.put("60", "c4a");//biến tạm
        cols.put("61", "c4b");//biến tạm
        cols.put("62", "confirmTime");
        cols.put("63", "resultsSiteTime");
        cols.put("64", "virusLoad");
        cols.put("65", "c8b");//biến tạm
        cols.put("66", "arv_hiv");
        cols.put("67", "arv_lnqdtd");
        cols.put("68", "arv_lao");
        cols.put("69", "arv_pmtct");
        cols.put("70", "arv_khhgd");
        cols.put("71", "arv_chamsocytekhac");
        cols.put("72", "arv_tiepcancongdong");
        cols.put("73", "arv_nhomhotro");
        cols.put("74", "arv_cainghiencongdong");
        cols.put("75", "arv_tuvangiamnguyco");
        cols.put("76", "arv_laymauxnkhangdinhhiv");
        cols.put("77", "arv_other");
        cols.put("78", "khongchuyengui");
        cols.put("79", "note");
        cols.put("80", "exchangeConsultTime");
        cols.put("81", "arvExchangeResult");
        cols.put("82", "exchangeTime");
        cols.put("83", "partnerProvideResult");
        cols.put("84", "arrivalSite");
        cols.put("85", "d2");//biến tạm
        cols.put("86", "exchangeProvinceID");
        cols.put("87", "exchangeDistrictID");
        cols.put("88", "registerTherapyTime");
        cols.put("89", "registeredTherapySite");
        cols.put("90", "d4");//biến tạm
        cols.put("91", "therapyRegistProvinceID");
        cols.put("92", "therapyRegistDistrictID");
        cols.put("93", "therapyNo");
        cols.put("94", "staffBeforeTestID");
        cols.put("95", "staffAfterID");
        cols.put("96", "BookingID");
        cols.put("97", "GUID");
        cols.put("98", "updateTime");
        return cols;
    }

    @GetMapping(value = {"/import-elog-grid/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-elog-grid/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<HtcVisitEntity> items = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() <= (firstRow - 2) || (row.getValue() != null && !row.getValue().isEmpty() && row.getValue().get(1) == null)) {
                    continue;
                }
                if (row.getValue().get(2) == null || row.getValue().get(2).equals("")) {
                    continue;
                }
                HtcVisitEntity item = getVisit(mapping(cols(), row.getValue()), getOptions());
                items.add(item);
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            List<String> doubleCode = new ArrayList<>();
            int i = firstRow - 1;
            int success = 0;
            for (HtcVisitEntity item1 : items) {
                i += 1;
                HashMap<String, List< String>> object = validateHtcVisit(item1);
                if (object.get("errors").isEmpty()) {
                    item1 = htcVisitService.save(item1);
                    htcVisitService.log(item1.getID(), "Thêm mới khách hàng từ import excel HTC Elog");
                    success += 1;
                    continue;
                }
                doubleCode.addAll(object.get("doubleCode"));
                errors.put(String.valueOf(i), object.get("errors"));
            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("doubleCode", StringUtils.join(doubleCode, ", "));
            model.addAttribute("form", form);

            return "importation/htc/elog-grid";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @Override
    public HtcElogImportForm mapping(Map<String, String> cols, List<String> cells) {
        HtcElogImportForm item = new HtcElogImportForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
                getLogger().warn(ex.getMessage());
            }
        }
        if (item.getService() != null && !"".equals(item.getService())) {
            item.setService(TextUtils.removeDiacritical(item.getService()));
        }

        return item;
    }

    private HtcVisitEntity getVisit(HtcElogImportForm item, HashMap<String, HashMap<String, String>> options) {
        LoggedUser currentUser = getCurrentUser();
        HtcVisitEntity model = new HtcVisitEntity();

        model.setIsRemove(false);
        model.setUpdateAt(new Date());
        model.setCreateAT(new Date());
        model.setHasHealthInsurance("0");

        model.setSiteID(currentUser.getSite().getID());
        model.setCode(String.format("%s-%s", item.getSiteCode(), item.getCode()).toLowerCase());
        model.setCdService(item.getTarger());
        model.setServiceID(item.getService());
        model.setPatientName(WordUtils.capitalizeFully(item.getPatientName()));
        model.setPatientPhone(item.getPatientPhone());
        model.setPatientID(item.getPatientID());
        model.setRaceID(options.get(ParameterEntity.RACE).getOrDefault(item.getRaceID(), RaceEnum.KINH.getKey()));
        model.setGenderID(item.getGenderID() == null || "".equals(item.getGenderID()) || "3".equals(item.getGenderID()) ? "" : item.getGenderID());
        model.setYearOfBirth(item.getYearOfBirth());
        model.setPermanentAddress(WordUtils.capitalizeFully(item.getPermanentAddress()));
        model.setPermanentDistrictID(options.get("districts").getOrDefault(item.getPermanentDistrictID(), ""));
        model.setPermanentProvinceID(options.get("provinces").getOrDefault(item.getPermanentProvinceID(), ""));
        model.setPermanentWardID(StringUtils.isEmpty(item.getPermanentWardID()) ? "" : getWardName(model.getPermanentDistrictID(), item.getPermanentWardID() == null ? "" : item.getPermanentWardID().toLowerCase()).getOrDefault(item.getPermanentWardID() == null ? "" : item.getPermanentWardID().toLowerCase(), ""));

        model.setCurrentAddress(item.getCurrentAddress());
        model.setCurrentDistrictID(options.get("districts").getOrDefault(item.getCurrentDistrictID(), ""));
        model.setCurrentProvinceID(options.get("provinces").getOrDefault(item.getCurrentProvinceID(), ""));
//        model.setCurrentWardID(getWard(model.getCurrentDistrictID()).getOrDefault(item.getCurrentWardID(), ""));
        model.setCurrentWardID(StringUtils.isEmpty(item.getCurrentWardID()) ? "" : getWardName(model.getCurrentDistrictID(), item.getCurrentWardID() == null ? "" : item.getCurrentWardID().toLowerCase()).getOrDefault(item.getCurrentWardID() == null ? "" : item.getCurrentWardID().toLowerCase(), ""));

        model.setJobID(options.get(ParameterEntity.JOB).getOrDefault(item.getJobID(), ""));

        model.setObjectGroupID(getObjectGroupID(item));

        model.setApproacherNo(item.getApproacherNo());
        model.setYouInjectCode(item.getYouInjectCode());

        List<String> refferal = new ArrayList<>(Arrays.asList(getReferralSource(item)));
        model.setReferralSource(refferal);

        model.setPreTestTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "dd/MM/yyyy"));
        model.setIsAgreePreTest(item.getIsAgreePreTest() == null || "".equals(item.getIsAgreePreTest()) || "0".equals(item.getIsAgreePreTest()) ? "0" : "1");
        model.setTestResultsID("2".equals(item.getTestResultsID()) ? "2" : "1".equals(item.getTestResultsID()) ? "1" : "3");
        if (item.getTestResultsID() != null && !item.getTestResultsID().equals("")) {
            if (item.getTestResultsID().equals("2")) {
                model.setResultsPatienTime(item.getResultsTime() == null || "".equals(item.getResultsTime()) ? null : TextUtils.convertDate(item.getResultsTime(), "dd/MM/yyyy"));
            } else if (item.getTestResultsID().equals("1")) {
                model.setResultsTime(item.getResultsTime() == null || "".equals(item.getResultsTime()) ? null : TextUtils.convertDate(item.getResultsTime(), "dd/MM/yyyy"));
            }
        } else {
            model.setResultsTime(item.getResultsTime() == null || "".equals(item.getResultsTime()) ? null : TextUtils.convertDate(item.getResultsTime(), "dd/MM/yyyy"));
        }
        model.setIsAgreeTest(false);
        if (!"".equals(item.getIsAgreeTest()) && item.getIsAgreeTest() != null && "1".equals(item.getIsAgreeTest())) {
            model.setIsAgreeTest(true);
        }
        model.setConfirmTestNo(item.getConfirmTestNo());
        model.setConfirmResultsID("".equals(item.getConfirmResultsID()) || item.getConfirmResultsID() == null ? "3" : item.getConfirmResultsID());
        model.setEarlyHiv("".equals(item.getEarlyHiv()) || item.getEarlyHiv() == null ? "" : (item.getEarlyHiv().equals("1") ? "8" : "9"));
        model.setModeOfTransmission(item.getModeOfTransmission());
        model.setConfirmTime(item.getConfirmTime() == null || "".equals(item.getConfirmTime()) ? null : TextUtils.convertDate(item.getConfirmTime(), "dd/MM/yyyy"));
        model.setResultsSiteTime(item.getResultsSiteTime() == null || "".equals(item.getResultsSiteTime()) ? null : TextUtils.convertDate(item.getResultsSiteTime(), "dd/MM/yyyy"));
        model.setVirusLoad(item.getVirusLoad());
        if (item.getVirusLoad() != null && !item.getVirusLoad().equals("")) {
            model.setVirusLoadDate(TextUtils.convertDate(item.getConfirmTime(), "dd/MM/yyyy"));
        }
        model.setVirusLoadNumber(item.getC8b());
        if (item.getEarlyHiv() != null && !item.getEarlyHiv().equals("")) {
            model.setEarlyHivDate(TextUtils.convertDate(item.getConfirmTime(), "dd/MM/yyyy"));
        }
        model.setRegisteredTherapySite(item.getRegisteredTherapySite());
        model.setSiteConfirmTest(getSiteByName(item.getSiteConfirmTest().trim().toLowerCase()));
        if (getRiskBehaviorID(item).size() > 0) {
            model.setRiskBehaviorID(getRiskBehaviorID(item));
        }
        model.setExchangeConsultTime(null);
        if (item.getExchangeConsultTime() != null && !item.getExchangeConsultTime().equals("")) {
            model.setExchangeConsultTime(TextUtils.convertDate(item.getExchangeConsultTime(), "dd/MM/yyyy"));
            model.setExchangeService("1");
        }

//        model.setExchangeConsultTime(item.getExchangeConsultTime() == null || "".equals(item.getExchangeConsultTime()) ? null : TextUtils.convertDate(item.getExchangeConsultTime(), "dd/MM/yyyy"));
        model.setArvExchangeResult(item.getArvExchangeResult());
        model.setExchangeTime(item.getExchangeTime() == null || "".equals(item.getExchangeTime()) ? null : TextUtils.convertDate(item.getExchangeTime(), "dd/MM/yyyy"));
        model.setPartnerProvideResult(item.getPartnerProvideResult());
        model.setArrivalSite(options.get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(item.getArrivalSite(), ""));
        if (item.getTherapyRegistProvinceID() != null && !item.getTherapyRegistProvinceID().equals("")) {
            model.setExchangeProvinceID(options.get("provinces").getOrDefault(item.getTherapyRegistProvinceID(), ""));
        } else {
            model.setExchangeProvinceID(options.get("provinces").getOrDefault(item.getExchangeProvinceID(), ""));
        }
        model.setExchangeDistrictID(options.get("districts").getOrDefault(item.getExchangeDistrictID(), ""));
        model.setRegisterTherapyTime(item.getRegisterTherapyTime() == null || "".equals(item.getRegisterTherapyTime()) ? null : TextUtils.convertDate(item.getRegisterTherapyTime(), "dd/MM/yyyy"));
        if (item.getArrivalSite() != null && !item.getArrivalSite().toString().equals("")) {
            model.setArrivalSiteID(new Long(-1));
            model.setArrivalSite(item.getArrivalSite());
            model.setTherapyExchangeStatus("1");
        }
        model.setTherapyRegistProvinceID(options.get("provinces").getOrDefault(item.getTherapyRegistProvinceID(), ""));
        model.setTherapyRegistDistrictID(options.get("districts").getOrDefault(item.getTherapyRegistDistrictID(), ""));
        model.setAdvisoryeTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "dd/MM/yyyy"));
        model.setTherapyNo(item.getTherapyNo());
        model.setStaffBeforeTestID(item.getStaffBeforeTestID());
        model.setStaffAfterID(item.getStaffAfterID());

        return model;
    }

    private String getObjectGroupID(HtcElogImportForm item) {
        List<String> testObject = new ArrayList<>();
        if (item.getObjectGroupID_ncmt() != null && !"".equals(item.getObjectGroupID_ncmt())) {
            testObject.add(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
        }
        if (item.getObjectGroupID_benhnhannghingoaids() != null && !"".equals(item.getObjectGroupID_benhnhannghingoaids())) {
            testObject.add(TestObjectGroupEnum.NGUOI_MUA_DAM.getKey());
        }
        if (item.getObjectGroupID_chuyengioi() != null && !"".equals(item.getObjectGroupID_chuyengioi())) {
            testObject.add(TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey());
        }
        if (item.getObjectGroupID_lao() != null && !"".equals(item.getObjectGroupID_lao())) {
            testObject.add(TestObjectGroupEnum.LAO.getKey());
        }
        if (item.getObjectGroupID_laytruyenquatd() != null && !"".equals(item.getObjectGroupID_laytruyenquatd())) {
            testObject.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        }
        if (item.getObjectGroupID_msm() != null && !"".equals(item.getObjectGroupID_msm())) {
            testObject.add(TestObjectGroupEnum.MSM.getKey());
        }
        if (item.getObjectGroupID_nghiavuquansu() != null && !"".equals(item.getObjectGroupID_nghiavuquansu())) {
            testObject.add(TestObjectGroupEnum.THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU.getKey());
        }
        if (item.getObjectGroupID_nguoihienmau() != null && !"".equals(item.getObjectGroupID_nguoihienmau())) {
            testObject.add(TestObjectGroupEnum.NGUOI_BAN_MAU_HIENMAU_CHOMAU.getKey());
        }
        if (item.getObjectGroupID_other() != null && !"".equals(item.getObjectGroupID_other())) {
            testObject.add(TestObjectGroupEnum.KHAC.getKey());
        }
        if (item.getObjectGroupID_pnbd() != null && !"".equals(item.getObjectGroupID_pnbd())) {
            testObject.add(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
        }
        if (item.getObjectGroupID_pnmangthai() != null && !"".equals(item.getObjectGroupID_pnmangthai())) {
            testObject.add(TestObjectGroupEnum.PREGNANT_PERIOD.getKey());
        }
        if (item.getObjectGroupID_vcbantinhnguoincc() != null && !"".equals(item.getObjectGroupID_vcbantinhnguoincc())) {
            testObject.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getKey());
        }
        if (item.getObjectGroupID_vcbantinhnguoinhiem() != null && !"".equals(item.getObjectGroupID_vcbantinhnguoinhiem())) {
            testObject.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        }
        return testObject.size() > 0 ? testObject.get(0) : null;
    }

    private String getReferralSource(HtcElogImportForm item) {
        List<String> referralSource = new ArrayList<>();
        if (item.getReferralSource_tiepcancongdong() != null && !"".equals(item.getReferralSource_tiepcancongdong())) {
            referralSource.add(ReferralSourceEnum.TC_CONG_DONG.getKey());
        }
        if (item.getReferralSource_xetnghiemtheodau() != null && !"".equals(item.getReferralSource_xetnghiemtheodau())) {
            referralSource.add(ReferralSourceEnum.KENH_BTBC.getKey());
        }
        if (item.getReferralSource_canboyte() != null && !"".equals(item.getReferralSource_canboyte())) {
            referralSource.add(ReferralSourceEnum.CAN_BO_YTE.getKey());
        }
        if (item.getReferralSource_mangxahoi() != null && !"".equals(item.getReferralSource_mangxahoi())) {
            referralSource.add(ReferralSourceEnum.INTERNET.getKey());
        }
        if (item.getReferralSource_other() != null && !"".equals(item.getReferralSource_other())) {
            referralSource.add(ReferralSourceEnum.KHAC.getKey());
        }
        return referralSource.size() > 0 ? referralSource.get(0) : null;
    }

    private List<String> getRiskBehaviorID(HtcElogImportForm item) {
        List<String> riskBehavior = new ArrayList<>();
        if (item.getRiskBehaviorID_msm() != null && !"".equals(item.getRiskBehaviorID_msm())) {
            riskBehavior.add("3");
        }
        if (item.getRiskBehaviorID_other() != null && !"".equals(item.getRiskBehaviorID_other())) {
            riskBehavior.add("100");
        }
        if (item.getRiskBehaviorID_quanhenhieunguoi() != null && !"".equals(item.getRiskBehaviorID_quanhenhieunguoi())) {
            riskBehavior.add("4");
        }
        if (item.getRiskBehaviorID_quanhetinhducnguoimuabandam() != null && !"".equals(item.getRiskBehaviorID_quanhetinhducnguoimuabandam())) {
            riskBehavior.add("2");
        }
        if (item.getRiskBehaviorID_tiemtrich() != null && !"".equals(item.getRiskBehaviorID_tiemtrich())) {
            riskBehavior.add("1");
        }

        return riskBehavior;
    }

    private HashMap<String, List<String>> validateHtcVisit(HtcVisitEntity form) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> doubleCode = new ArrayList<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        // Validate bắt buộc nếu kết quả xét nghiệm có phản ứng
        if ("2".equals(form.getTestResultsID())) {

            // Validate Họ tên
            if (form.getPatientName() == null || "".equals(form.getPatientName())) {
                errors.add(errorMsg("A5. Tên khách hàng", "không được để trống"));
            }

            // Validate year of birth
            if (form.getYearOfBirth() == null || "".equals(form.getYearOfBirth())) {
                errors.add(errorMsg("A10. Năm sinh", "không được để trống"));
            }

//            if (form.getPermanentAddress() == null || "".equals(form.getPermanentAddress())) {
//                errors.add(errorMsg("A11.4  Địa chỉ thường trú", "không được để trống"));
//            }
            if (form.getPermanentProvinceID() == null || "".equals(form.getPermanentProvinceID())) {
                errors.add(errorMsg("A11.1 Tỉnh/Thành phố", "không được để trống"));
            }
            if (form.getPermanentDistrictID() == null || "".equals(form.getPermanentDistrictID())) {
                errors.add(errorMsg("A11.2 Quận /Huyện", "không được để trống"));
            }

        }

        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE)) {
            errors.add(errorMsg("A4. Số thứ tự khách hàng", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu -"));
        }

        // Check not null cho loại dịch vụ 
        validateNull(form.getServiceID(), "Loại hình dịch vụ ", errors);

        // Check null cho nhóm đối tượng
        validateNull(form.getObjectGroupID(), "Nhóm đối tượng ", errors);

        // Check null cho câu trả lời đồng ý XN sàng lọc
        validateNull(form.getIsAgreePreTest(), "Câu trả lời ", errors);

        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.add("<span>A5. Tên khách hàng</span> <i class=\"text-danger\">Họ và tên chỉ chứa các kí tự chữ cái</i>");
        }
        // Validate gender
        validateNull(form.getGenderID(), "Giới tính ", errors);

        // Validate existing code
        if (StringUtils.isNotBlank(form.getCode()) && htcVisitService.existCodeAndSiteID(form.getCode(), form.getSiteID())) {
            errors.add(errorMsg("A4. Số thứ tự khách hàng", " mã đã tồn tại"));
            doubleCode.add(form.getCode());
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.add(errorMsg("A10. Năm sinh", " năm sinh gồm bốn chữ số"));
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.add(errorMsg("A10. Năm sinh", " năm sinh hợp lệ từ 1900 - năm hiện tại"));
        }

        // Validate giá trị liên quan trường đồng ý xét nghiệm sàng lọc
        if (form.getPreTestTime() == null || StringUtils.isEmpty(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE))) {
            errors.add(errorMsg("A1. Ngày lấy máu xét nghiệm sàng lọc ", "không được bỏ trống"));
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(form.getTestResultsID())) {
            errors.add(errorMsg("B1.Kết quả xét nghiệm sàng lọc ", "phải có giá trị khi đồng ý xét nghiệm sàng lọc"));
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE))) {
            errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "phải có giá trị khi đồng ý xét nghiệm sàng lọc"));
        }
        if ((StringUtils.isEmpty(form.getIsAgreePreTest()) || (StringUtils.isNotEmpty(form.getIsAgreePreTest()) && form.getIsAgreePreTest().equals("không"))) && StringUtils.isNotEmpty(TextUtils.formatDate(form.getResultsTime(), FORMATDATE))) {
            errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "phải có giá trị khi đồng ý xét nghiệm sàng lọc"));
        }

        try {
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getResultsTime(), FORMATDATE)) && StringUtils.isNotEmpty(TextUtils.formatDate(form.getAdvisoryeTime(), FORMATDATE))) {
                Date today = new Date();
                Date resultsTime = sdfrmt.parse(TextUtils.formatDate(form.getResultsTime(), FORMATDATE));
                Date advisoryeTime = sdfrmt.parse(TextUtils.formatDate(form.getAdvisoryeTime(), FORMATDATE));
                if (resultsTime.compareTo(today) > 0) {
                    errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "không được sau ngày hiện tại"));
                }
                if (resultsTime.compareTo(advisoryeTime) < 0) {
                    errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "phải sau ngày tư vấn trước XN"));
                }
            }
        } catch (Exception e) {
        }
        if (form.getResultsTime() != null && StringUtils.isNotBlank(TextUtils.formatDate(form.getResultsTime(), FORMATDATE))) {
            if (TextUtils.formatDate("dd/MM/yyyy", "yyyy", TextUtils.formatDate(form.getResultsTime(), FORMATDATE)).charAt(0) == '0') {
                errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "không đúng định dạng"));
            }
        }

        if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID())
                && !TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID())) {
            errors.add(errorMsg("C3. Kết quả xét nghiệm khẳng định ", "chỉ có khi kết quả XNSL là có phản ứng"));
        }

        if (form.getPreTestTime() != null && StringUtils.isNotEmpty(form.getTestResultsID())) {
            validateNull(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE), "A1. Ngày lấy máu xét nghiệm sàng lọc ", errors);
        }

        if (form.getConfirmTime() != null && StringUtils.isNotEmpty(form.getConfirmResultsID())) {
            validateNull(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE), "C5.  Ngày xét nghiệm khẳng định", errors);
        }

        if (form.getPreTestTime() != null && StringUtils.isNotEmpty(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE))) {
            if (TextUtils.formatDate("dd/MM/yyyy", "yyyy", TextUtils.formatDate(form.getPreTestTime(), FORMATDATE)) == null || TextUtils.formatDate("dd/MM/yyyy", "yyyy", TextUtils.formatDate(form.getPreTestTime(), FORMATDATE)).charAt(0) == '0') {
                errors.add(errorMsg("A1. Ngày lấy máu xét nghiệm sàng lọc ", "không đúng định dạng"));
            }
        }

        if (form.getConfirmTime() != null && StringUtils.isNotEmpty(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE))) {
            if (TextUtils.formatDate("dd/MM/yyyy", "yyyy", TextUtils.formatDate(form.getConfirmTime(), FORMATDATE)) == null || TextUtils.formatDate("dd/MM/yyyy", "yyyy", TextUtils.formatDate(form.getConfirmTime(), FORMATDATE)).charAt(0) == '0') {
                errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không đúng định dạng"));
            }
        }

        try {
            if (form.getPreTestTime() != null && form.getConfirmTime() != null && StringUtils.isNotEmpty(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE)) && StringUtils.isNotEmpty(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE))) {
                Date c5 = sdfrmt.parse(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE));
                Date c6 = sdfrmt.parse(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE));
                if (c5.compareTo(c6) > 0) {
                    errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không được nhỏ hơn ngày xét nghiệm sàng lọc"));
                }
            }

        } catch (ParseException e) {
        }
        // Ngày XN sàng lọc
        try {
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE))) {
                Date today = new Date();
                Date preTestTime = sdfrmt.parse(TextUtils.formatDate(form.getPreTestTime(), FORMATDATE));
                if (preTestTime.compareTo(today) > 0) {
                    errors.add(errorMsg("A1. Ngày lấy máu xét nghiệm sàng lọc ", "không được sau ngày hiện tại"));
                }
            }
            // Ngày xét nghiệm khẳng định
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE))) {
                Date today = new Date();
                Date confirmDate = sdfrmt.parse(TextUtils.formatDate(form.getConfirmTime(), FORMATDATE));
                if (confirmDate.compareTo(today) > 0) {
                    errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không được sau ngày hiện tại"));
                }
            }

            // Ngày cơ sở nhận kết quả KĐ
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getResultsSiteTime(), FORMATDATE))) {
                Date today = new Date();
                Date resultsSiteTime = sdfrmt.parse(TextUtils.formatDate(form.getResultsSiteTime(), FORMATDATE));
                if (resultsSiteTime.compareTo(today) > 0) {
                    errors.add(errorMsg("C6. Ngày cơ sở nhận kết quả xét nghiệm khẳng định ", "không được sau ngày hiện tại"));
                }
            }

            // Ngày tư vấn chuyển gửi điều trị ARV
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getExchangeConsultTime(), FORMATDATE))) {
                Date today = new Date();
                Date exchangeConsultTime = sdfrmt.parse(TextUtils.formatDate(form.getExchangeConsultTime(), FORMATDATE));
                if (exchangeConsultTime.compareTo(today) > 0) {
                    errors.add(errorMsg("D1. Ngày thực hiện tư vấn chuyển gửi điều trị ARV: ", "không được sau ngày hiện tại"));
                }
            }

            // Ngày chuyển gửi
            if (StringUtils.isNotEmpty(TextUtils.formatDate(form.getExchangeTime(), FORMATDATE))) {
                Date today = new Date();
                Date exchangeTime = sdfrmt.parse(TextUtils.formatDate(form.getExchangeTime(), FORMATDATE));
                if (exchangeTime.compareTo(today) > 0) {
                    errors.add(errorMsg("D1.1 Ngày chuyển gửi điều trị ARV ", "không được sau ngày hiện tại"));
                }
            }

        } catch (Exception e) {
        }
        object.put("errors", errors);
        object.put("doubleCode", doubleCode);

        return object;
    }

    private void validateNull(String text, String message, List<String> errors) {
        if (text == null || "".equals(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
        }

    }

    private String errorMsg(String attribute, String msg) {
        return String.format("<span> %s </span> <i class=\"text-danger\"> %s </i>", attribute, msg);
    }
}
