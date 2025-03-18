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
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.PqmHtcElogImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.json.Locations;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.PqmLogService;
import com.gms.service.PqmVctService;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
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

@Controller(value = "importation_pqm_elog_grid")
public class PqmVctController extends BaseController<PqmHtcElogImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 9;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmVctService pqmVctService;
    @Autowired
    private PqmLogService logService;

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
        options.put("provinceNames", new HashMap<String, String>());
        for (ProvinceEntity provinceEntity : data.getProvince()) {
            if (StringUtils.isEmpty(provinceEntity.getElogCode())) {
                continue;
            }
            options.get("provinces").put(provinceEntity.getElogCode(), provinceEntity.getID());
            options.get("provinceNames").put(provinceEntity.getID(), provinceEntity.getName());
        }
        //Quận huyện
        options.put("districts", new HashMap<String, String>());
        options.put("districtNames", new HashMap<String, String>());
        for (DistrictEntity districtEntity : data.getDistrict()) {
            if (StringUtils.isEmpty(districtEntity.getElogCode())) {
                continue;
            }
            options.get("districts").put(districtEntity.getElogCode(), districtEntity.getID());
            options.get("districtNames").put(districtEntity.getID(), districtEntity.getName());
        }

        options.put(ParameterEntity.TREATMENT_FACILITY, getTreatmentFacilityElog());

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        options.get("siteOpc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> sitePrEP = siteService.getSitePrEP(getCurrentUser().getSite().getProvinceID());
        options.put("sitePrEP", new HashMap<String, String>());
        options.get("sitePrEP").put("", "Chọn cơ sở");
        for (SiteEntity site : sitePrEP) {
            options.get("sitePrEP").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtc", new HashMap<String, String>());
        options.get("siteHtc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteHtc) {
            if (StringUtils.isNotEmpty(site.getHub()) && site.getHub().equals("1")) {
                options.get("siteHtc").put(String.valueOf(site.getID()), site.getName());
            }
        }

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

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-vct/import.html");
        form.setSmallUrl("/backend/pqm-vct/index.html");
        form.setReadUrl("/pqm-vct/import.html");
        form.setTitle("Tải file excel của HTC Elog");
        form.setSmallTitle("Khách hàng xét nghiệm");
        form.setTemplate(fileTemplate("htc_template.xlsx"));
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

    @GetMapping(value = {"/pqm-vct/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("sites", getOptions().get("siteHtc"));
        return "importation/pqm/upload_vct";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-vct/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();

        if (!isPAC()) {
            sites = getCurrentUser().getSite().getID().toString();
        }

        System.out.println("xxx " + sites);
        if (StringUtils.isEmpty(sites)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
            return redirect(form.getUploadUrl());
        }
        try {
            //xoas du lieu
            pqmVctService.resetDataBySite(Long.valueOf(sites));
            
            
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());

            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmHtcElogImportForm> items = new ArrayList<>();
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 6) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("A3. Mã cơ sở "))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("A4. Số thứ tự khách hàng"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("A2. Loại dịch vụ"))) {
                        ok = true;
                    }
                }

                if (row.getKey() <= (firstRow - 2) || (row.getValue() != null && !row.getValue().isEmpty() && row.getValue().get(0) == null)) {
                    continue;
                }
                if (row.getValue().get(0) == null || row.getValue().get(0).equals("")) {
                    continue;
                }
                PqmHtcElogImportForm item = mapping(cols(), row.getValue());
                items.add(item);
            }
            model.addAttribute("form", form);
            model.addAttribute("site", options.get("siteHtc").get(sites));

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = firstRow - 1;

            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();

            int success = 0;
            int ignore = 0;

            Set<PqmVctEntity> datas = new HashSet<>();

            PqmVctEntity item2;
            for (PqmHtcElogImportForm item1 : items) {
                i += 1;
                if (StringUtils.isEmpty(item1.getTestResultsID()) || StringUtils.isEmpty(item1.getConfirmResultsID())
                        || !TestResultEnum.CO_PHAN_UNG.getKey().equals(item1.getTestResultsID()) || !item1.getConfirmResultsID().equals("2")) {
                    ignore++;
                    continue;
                }
                //Lấy dòng excel
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateHtcVisit(item1, row, style, cols);
                if (object.get("errors").isEmpty()) {
                    PqmVctEntity item = getVisit(item1, options, Long.valueOf(sites));
                    if (item.getBlocked() != null && item.getBlocked() == 1) {
                        errors.put(String.valueOf(i), new ArrayList<String>());
                        errors.get(String.valueOf(i)).add("Bản ghi này đang có trạng thái là Không được ghi đè, nên không thể import. Thực hiện xóa bản ghi này trên file excel và import lại");
                        continue;

                    }
                    item.setSiteID(Long.valueOf(sites));
                    item.setSource("ELOG");
//                    item = pqmVctService.save(item);
                    datas.add(item);
                    success += 1;
                    continue;
                }
                errors.put(String.valueOf(i), object.get("errors"));
            }

            //Lưu tạm file vào thư mục tạm
            model.addAttribute("filePath", saveFile(workbook, file));

            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("ignore", ignore);

            if (errors.isEmpty()) {
                for (PqmVctEntity data : datas) {
                    pqmVctService.save(data);
                }
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                logService.log("Tải file excel của HTC Elog", items.size(), success, items.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", Long.valueOf(sites), "Tư vấn & xét nghiệm");
                model.addAttribute("ok", true);
                return "importation/htc/elog-grid-pqm";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/htc/elog-grid-pqm";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    @Override
    public PqmHtcElogImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmHtcElogImportForm item = new PqmHtcElogImportForm();
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

    private PqmVctEntity getVisit(PqmHtcElogImportForm item, HashMap<String, HashMap<String, String>> options, Long siteID) {

        String code = String.format("%s-%s", item.getSiteCode(), item.getCode()).toLowerCase();

        PqmVctEntity model = pqmVctService.findBySiteAndCode(siteID, code.toUpperCase());
        if (model == null) {
            model = new PqmVctEntity();
        }

        model.setUpdateAt(new Date());
        model.setCreateAT(new Date());

        model.setPatientPhone(item.getPatientPhone());

        model.setCode(String.format("%s-%s", item.getSiteCode(), item.getCode()).toLowerCase());
        model.setCode(model.getCode().toUpperCase());

        model.setPatientName(WordUtils.capitalizeFully(item.getPatientName()));
        model.setPatientID(item.getPatientID());
        model.setIdentityCard(item.getPatientID());
        model.setGenderID(item.getGenderID() == null || "".equals(item.getGenderID()) ? "" : item.getGenderID());
        model.setYearOfBirth(item.getYearOfBirth());

        model.setPermanentAddress(WordUtils.capitalizeFully(item.getPermanentAddress()));

        String pp = options.get("provinces").getOrDefault(item.getPermanentProvinceID(), "");
        String pd = options.get("districts").getOrDefault(item.getPermanentDistrictID(), "");
        String cp = options.get("provinces").getOrDefault(item.getCurrentProvinceID(), "");
        String cd = options.get("districts").getOrDefault(item.getCurrentDistrictID(), "");

        model.setPermanentAddress(WordUtils.capitalizeFully(buildAddress(item.getPermanentAddress(), options.get("provinceNames").getOrDefault(pp, ""), options.get("districtNames").getOrDefault(pd, ""), item.getPermanentWardID())));

        model.setCurrentAddress(WordUtils.capitalizeFully(buildAddress(item.getCurrentAddress(), options.get("provinceNames").getOrDefault(pp, ""), options.get("districtNames").getOrDefault(pd, ""), item.getCurrentWardID())));

        model.setObjectGroupID(getObjectGroupID(item));

        model.setPreTestTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "dd/MM/yyyy"));
        model.setTestResultsID("2".equals(item.getTestResultsID()) ? "2" : "1".equals(item.getTestResultsID()) ? "1" : "3");
        model.setResultsTime(item.getResultsTime() == null || "".equals(item.getResultsTime()) ? null : TextUtils.convertDate(item.getResultsTime(), "dd/MM/yyyy"));
        model.setConfirmTestNo(item.getConfirmTestNo());
        model.setConfirmResultsID("".equals(item.getConfirmResultsID()) || item.getConfirmResultsID() == null ? "" : item.getConfirmResultsID());
        model.setConfirmTime(item.getConfirmTime() == null || "".equals(item.getConfirmTime()) ? null : TextUtils.convertDate(item.getConfirmTime(), "dd/MM/yyyy"));
        model.setResultsSiteTime(item.getResultsSiteTime() == null || "".equals(item.getResultsSiteTime()) ? null : TextUtils.convertDate(item.getResultsSiteTime(), "dd/MM/yyyy"));

        model.setExchangeTime(item.getExchangeTime() == null || "".equals(item.getExchangeTime()) ? null : TextUtils.convertDate(item.getExchangeTime(), "dd/MM/yyyy"));
        model.setRegisterTherapyTime(item.getRegisterTherapyTime() == null || "".equals(item.getRegisterTherapyTime()) ? null : TextUtils.convertDate(item.getRegisterTherapyTime(), "dd/MM/yyyy"));
        model.setRegisteredTherapySite(options.get("treatment-facility").get(model.getRegisteredTherapySite()));

        model.setAdvisoryeTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "dd/MM/yyyy"));
        model.setTherapyNo(item.getTherapyNo());
        model.setEarlyDiagnose(item.getEarlyHiv() == null ? "" : item.getEarlyHiv());
        model.setRegisteredTherapySite(item.getRegisteredTherapySite());

        return model;
    }

    protected String buildAddress(String address, String province, String district, String ward) {
        address = address == null ? "" : address;
        if (StringUtils.isNotEmpty(ward)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + ward;
        }
        if (StringUtils.isNotEmpty(district) && !district.equals("Huyện Hoàng Sa")) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + district;
        }
        if (StringUtils.isNotEmpty(province)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + province;
        }

        return address;
    }

    private String getObjectGroupID(PqmHtcElogImportForm item) {
        List<String> testObject = new ArrayList<>();
        if (item.getObjectGroupID_ncmt() != null && !"".equals(item.getObjectGroupID_ncmt())) {
            testObject.add(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
        }
        if (item.getObjectGroupID_benhnhannghingoaids() != null && !"".equals(item.getObjectGroupID_benhnhannghingoaids())) {
            testObject.add(TestObjectGroupEnum.NGHI_NGO_AIDS.getKey());
        }
        if (item.getObjectGroupID_chuyengioi() != null && !"".equals(item.getObjectGroupID_chuyengioi())) {
            testObject.add(TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey());
        }
        if (item.getObjectGroupID_lao() != null && !"".equals(item.getObjectGroupID_lao())) {
            testObject.add(TestObjectGroupEnum.LAO.getKey());
        }
        if (item.getObjectGroupID_laytruyenquatd() != null && !"".equals(item.getObjectGroupID_laytruyenquatd())) {
            testObject.add(TestObjectGroupEnum.BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD.getKey());
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
            testObject.add(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
        }
        if (item.getObjectGroupID_vcbantinhnguoincc() != null && !"".equals(item.getObjectGroupID_vcbantinhnguoincc())) {
            testObject.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO.getKey());
        }
        if (item.getObjectGroupID_vcbantinhnguoinhiem() != null && !"".equals(item.getObjectGroupID_vcbantinhnguoinhiem())) {
            testObject.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        }
        return testObject.size() > 0 ? testObject.get(0) : null;
    }

    private String getReferralSource(PqmHtcElogImportForm item) {
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

    private List<String> getRiskBehaviorID(PqmHtcElogImportForm item) {
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

    private HashMap<String, List<String>> validateHtcVisit(PqmHtcElogImportForm form, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        if (StringUtils.isEmpty(form.getConfirmResultsID()) || !form.getConfirmResultsID().equals("2")) {
            errors.add(errorMsg("Kết quả XNKĐ", "không được để trống hoặc phải là dương tính"));
            addExcelError("confirmResultsID", row, cols, style);
        }

        // Validate bắt buộc nếu kết quả xét nghiệm có phản ứng
        if ("2".equals(form.getTestResultsID())) {
            // Validate Họ tên
            if (form.getPatientName() == null || "".equals(form.getPatientName())) {
                errors.add(errorMsg("A5. Tên khách hàng", "không được để trống"));
                addExcelError("patientName", row, cols, style);
            }
        }

        if (StringUtils.isEmpty(form.getSiteCode())) {
            errors.add(errorMsg("A3. Mã cơ sở", " không được để trống"));
            addExcelError("siteCode", row, cols, style);
        }
        if (StringUtils.isEmpty(form.getCode())) {
            errors.add(errorMsg("A4. Số thứ tự khách hàng", " không được để trống"));
            addExcelError("code", row, cols, style);
        }
//        if (StringUtils.isNotEmpty(form.getCode()) && !TextUtils.removeDiacritical(form.getCode().trim()).matches(RegexpEnum.CODE)) {
//            errors.add(errorMsg("A4. Số thứ tự khách hàng", "Mã không đúng định dạng (Không chứa tự đặc biệt, chỉ chứa số, chữ và dấu -"));
//            addExcelError("code", row, cols, style);
//        }

        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.add("<span>A5. Tên khách hàng</span> <i class=\"text-danger\">Họ và tên chỉ chứa các kí tự chữ cái</i>");
            addExcelError("patientName", row, cols, style);
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.add(errorMsg("A10. Năm sinh", " năm sinh gồm bốn chữ số"));
            addExcelError("yearOfBirth", row, cols, style);
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.add(errorMsg("A10. Năm sinh", " năm sinh hợp lệ từ 1900 - năm hiện tại"));
            addExcelError("yearOfBirth", row, cols, style);
        }

        try {
            if (StringUtils.isNotEmpty(form.getResultsTime()) && StringUtils.isNotEmpty(form.getPreTestTime())
                    && isThisDateValid(form.getResultsTime()) && isThisDateValid(form.getPreTestTime())) {
                Date today = new Date();
                Date resultsTime = sdfrmt.parse(form.getResultsTime());
                Date advisoryeTime = sdfrmt.parse(form.getPreTestTime());
                if (resultsTime.compareTo(advisoryeTime) < 0) {
                    errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "phải sau ngày tư vấn trước XN"));
                    addExcelError("resultsTime", row, cols, style);
                }
            }
            if (StringUtils.isNotEmpty(form.getResultsTime()) && isThisDateValid(form.getResultsTime())) {
                Date today = new Date();
                Date confirmDate = sdfrmt.parse(form.getResultsTime());
                if (confirmDate.compareTo(today) > 0) {
                    errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "không được sau ngày hiện tại"));
                    addExcelError("resultsTime", row, cols, style);
                }
            }
        } catch (Exception e) {
        }
        if (StringUtils.isNotBlank(form.getResultsTime()) && !isThisDateValid(form.getResultsTime())) {
            errors.add(errorMsg("(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm  ", "không đúng định dạng"));
            addExcelError("resultsTime", row, cols, style);
        }

        if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID())
                && !TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID())) {
            errors.add(errorMsg("C3. Kết quả xét nghiệm khẳng định ", "chỉ có khi kết quả XNSL là có phản ứng"));
        }

        if (form.getPreTestTime() != null && StringUtils.isNotEmpty(form.getTestResultsID())) {
            validateNull(form.getPreTestTime(), "A1. Ngày lấy máu xét nghiệm sàng lọc ", errors);
            if (form.getPreTestTime() == null) {
                addExcelError("preTestTime", row, cols, style);
            }
        }

        validateNull(form.getConfirmTime(), "Ngày XN khẳng định", errors);
        if (StringUtils.isEmpty(form.getConfirmTime())) {
            addExcelError("confirmTime", row, cols, style);
        }

        if (StringUtils.isNotBlank(form.getPreTestTime()) && !isThisDateValid(form.getPreTestTime())) {
            errors.add(errorMsg("A1. Ngày lấy máu xét nghiệm sàng lọc ", "không đúng định dạng"));
            addExcelError("preTestTime", row, cols, style);
        }

        if (StringUtils.isNotBlank(form.getConfirmTime()) && !isThisDateValid(form.getConfirmTime())) {
            errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không đúng định dạng"));
            addExcelError("confirmTime", row, cols, style);
        }

        try {
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getPreTestTime())
                    && isThisDateValid(form.getConfirmTime()) && isThisDateValid(form.getPreTestTime())) {
                Date c5 = sdfrmt.parse(form.getPreTestTime());
                Date c6 = sdfrmt.parse(form.getConfirmTime());
                if (c5.compareTo(c6) > 0) {
                    errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không được nhỏ hơn ngày xét nghiệm sàng lọc"));
                    addExcelError("confirmTime", row, cols, style);
                }
            }

        } catch (ParseException e) {
        }
        // Ngày XN sàng lọc
        try {
            // Ngày xét nghiệm khẳng định
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && isThisDateValid(form.getConfirmTime())) {
                Date today = new Date();
                Date confirmDate = sdfrmt.parse(form.getConfirmTime());
                if (confirmDate.compareTo(today) > 0) {
                    errors.add(errorMsg("C5.  Ngày xét nghiệm khẳng định ", "không được sau ngày hiện tại"));
                    addExcelError("confirmTime", row, cols, style);
                }
            }

            if (StringUtils.isNotBlank(form.getExchangeTime()) && !isThisDateValid(form.getExchangeTime())) {
                errors.add(errorMsg("D1.1 Ngày chuyển gửi điều trị ARV ", "không đúng định dạng"));
                addExcelError("exchangeTime", row, cols, style);
            }
            // Ngày chuyển gửi
            if (StringUtils.isNotEmpty(form.getExchangeTime()) && isThisDateValid(form.getExchangeTime())) {
                Date today = new Date();
                Date exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (exchangeTime.compareTo(today) > 0) {
                    errors.add(errorMsg("D1.1 Ngày chuyển gửi điều trị ARV ", "không được sau ngày hiện tại"));
                    addExcelError("exchangeTime", row, cols, style);
                }
            }

        } catch (Exception e) {
        }
        object.put("errors", errors);

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

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null || dateToValidate.length() < 10) {
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
