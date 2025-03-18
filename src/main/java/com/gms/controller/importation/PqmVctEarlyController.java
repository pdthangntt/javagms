package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmVctEarlyForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmHtcElogImportForm;
import com.gms.entity.form.PqmShiArtImportForm;
import com.gms.entity.form.PqmVctRecencyForm;
import com.gms.service.LocationsService;
import com.gms.service.PqmLogService;
import com.gms.service.PqmVctRecencyService;
import com.gms.service.PqmVctService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_pqm_vct_early")
public class PqmVctEarlyController extends BaseController<PqmVctEarlyForm> {

    private static int firstRow = 4;

    private static HashMap<String, HashMap<String, String>> options;

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmVctService pqmVctService;
    @Autowired
    private PqmVctRecencyService pqmVctRecencyService;
    @Autowired
    private PqmLogService logService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {

        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.GENDER);
        options = parameterService.getOptionsByTypes(parameterTypes, null);
//        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        List<SiteEntity> siteHtc = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtc", new HashMap<String, String>());
        options.get("siteHtc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteHtc) {
            options.get("siteHtc").put(String.valueOf(site.getID()), site.getName());
        }
        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-vct-ealry/index.html");
        form.setSmallUrl("/backend/pqm-vct-recency/index.html");
        form.setReadUrl("/pqm-vct-ealry/index.html");
        form.setTitle("Tải file excel thêm dữ liệu nhiễm mới");
        form.setSmallTitle("Khách hàng nhiễm mới");
        form.setTemplate(fileTemplate("DHIS2_template.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "stt");
        cols.put("1", "name");
        cols.put("2", "code");
        cols.put("3", "gender");
        cols.put("4", "yearOfBirth");
        cols.put("5", "address");
        cols.put("6", "province");
        cols.put("7", "district");
        cols.put("8", "ward");
        cols.put("9", "identityCard");
        cols.put("10", "objectGroupID");
        cols.put("11", "siteTesting");
        cols.put("12", "");
        cols.put("13", "testTime");
        cols.put("14", "");
        cols.put("15", "");
        cols.put("16", "");
        cols.put("17", "");
        cols.put("18", "");
        cols.put("19", "confirmTestNo");
        cols.put("20", "earlyDiagnose");
        cols.put("21", "");
        cols.put("22", "siteConfirmCode");
        return cols;
    }

    @GetMapping(value = {"/pqm-vct-ealry/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("sites", getOptions().get("siteHtc"));
        model.addAttribute("isPac", isPAC());
        return "importation/pqm/upload";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-vct-ealry/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        try {
            if (!isPAC()) {
                sites = getCurrentUser().getSite().getID().toString();
            }

            if (StringUtils.isEmpty(sites)) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
                return redirect(form.getUploadUrl());
            }
            List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
            Set<Long> siteIDs = new HashSet<>();
            for (SiteEntity siteEntity : siteHtc) {
                siteIDs.add(siteEntity.getID());
            }

            //tìm bản ghi sẵn có
            Map<String, PqmVctEntity> mapCurrentDatas = new HashMap<>();
            List<PqmVctEntity> list = pqmVctService.findBySiteIDs(siteIDs);
            if (list != null) {
                for (PqmVctEntity pqmVctEntity : list) {
                    mapCurrentDatas.put(pqmVctEntity.getCode(), pqmVctEntity);
                }
            }

            //xoas du lieu
            pqmVctRecencyService.resetDataBySite(Long.valueOf(sites));
            
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());

            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmVctEarlyForm> earlyForms = new ArrayList<>();
            List<PqmVctRecencyForm> recencyForms = new ArrayList<>();
            List<PqmHtcElogImportForm> items = new ArrayList<>();
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 1) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(2));
                    System.out.println("333 " + row.getValue().get(3));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("HỌ TÊN"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("MÃ SỐ NƠI GỬI"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Giới "))) {
                        ok = true;
                    }
                }
                if (row.getKey() <= (firstRow - 2)) {
                    continue;
                }
                if (row.getValue().size() == 0 || (row != null && (row.getValue().get(0) == null || row.getValue().get(0).equals("")))) {
                    continue;
                }
                PqmVctEarlyForm item = mapping(cols(), row.getValue());
                PqmVctRecencyForm itemRecency = mappingVctRecency(cols(), row.getValue());
                earlyForms.add(item);
                recencyForms.add(itemRecency);
            }

            model.addAttribute("site", getOptions().get("siteHtc").get(sites));
            model.addAttribute("form", form);

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = firstRow - 1;
            int success = 0;

            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();

            Set<PqmVctRecencyEntity> datas = new HashSet<>();
            for (PqmVctRecencyForm item1 : recencyForms) {
                System.out.println("xxx " + i + ' ' + item1.getProvince() + ' ' + item1.getDistrict() + ' ' + item1.getWard());

                i += 1;
                //Lấy dòng excel
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateHtcVisit(item1, row, style, cols);
                if (object.get("errors").isEmpty()) {
                    PqmVctRecencyEntity entity = getVctRecency(item1, sites, getCurrentUser().getSite().getProvinceID());

                    try {
                        datas.add(entity);
//                        pqmVctRecencyService.save(entity);
                        success += 1;
                    } catch (Exception e) {
                        object.put("errors", new ArrayList<String>());
                        object.get("errors").add("Đã xảy ra lỗi với bản ghi, vui lòng kiểm tra lại!");
                    }
                    continue;
                }
                errors.put(String.valueOf(i), object.get("errors"));

            }

            //Lưu tạm file vào thư mục tạm
            model.addAttribute("filePath", saveFile(workbook, file));

            model.addAttribute("errorsw", errors);
            model.addAttribute("total", recencyForms.size());
            model.addAttribute("successx", success);
            model.addAttribute("site", getOptions().get("siteHtc").get(sites));
            model.addAttribute("form", form);

            int x = 1;
            if (errors.isEmpty()) {
                for (PqmVctRecencyEntity data : datas) {
                    System.out.println("data " + x++ + data.getAddress().length());
                    try {
                        pqmVctRecencyService.save(data);
                    } catch (Exception e) {
                        model.addAttribute("error", "Có lỗi xảy ra khi import. Vui lòng kiểm tra lại.");
                        model.addAttribute("ok", false);
                    }

                }
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                logService.log("Tải file excel thêm dữ liệu nhiễm mới", recencyForms.size(), success, recencyForms.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", Long.valueOf(sites), "Xét nghiệm khẳng định");
                model.addAttribute("ok", true);
                return "importation/htc/early_pqm";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/htc/early_pqm";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private HashMap<String, List<String>> validateHtcVisit(PqmVctRecencyForm item1, Row row, CellStyle style, Map<String, String> cols) {
        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();

        //validate nhập vào
        if (StringUtils.isEmpty(item1.getName())) {
            errors.add("Họ tên không được để trống");
            addExcelError("name", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getTestTime())) {
            errors.add("Ngày xét nghiệm không được để trống");
            addExcelError("testTime", row, cols, style);
        }
        if (!isThisDateValid(item1.getTestTime())) {
            errors.add("Ngày xét nghiệm không đúng định dạng");
            addExcelError("testTime", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getEarlyDiagnose())) {
            errors.add("Kết quả nhiễm mới không được để trống");
            addExcelError("earlyDiagnose", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getCode())) {
            errors.add("Mã số nơi gửi không được để trống");
            addExcelError("code", row, cols, style);
        }
//                if (StringUtils.isEmpty(item1.getGender())) {
//                    
//                    errors.add("Giới tính không được để trống");
//                }
//                if (StringUtils.isEmpty(item1.getYearOfBirth())) {
//                    
//                    errors.add("Năm sinh không được để trống");
//                }
        if (StringUtils.isNotEmpty(item1.getYearOfBirth())) {
            try {
                if (Integer.parseInt(item1.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR)) {

                    errors.add("Năm sinh không được sau năm hiện tại");
                    addExcelError("yearOfBirth", row, cols, style);

                }
            } catch (Exception e) {

                errors.add("Năm sinh không hợp lệ");
                addExcelError("yearOfBirth", row, cols, style);

            }

        }
        if (StringUtils.isNotEmpty(item1.getTestTime()) && isThisDateValid(item1.getTestTime())) {
            Date today = new Date();
            Date exchangeTime = TextUtils.convertDate(item1.getTestTime(), FORMATDATE);
            if (exchangeTime.compareTo(today) > 0) {

                errors.add("Ngày xét nghiệm không sau ngày hiện tại");
                addExcelError("testTime", row, cols, style);
            }
        }
        if (StringUtils.isEmpty(item1.getConfirmTestNo())) {

            errors.add("Mã số BN của PXN không được để trống");
            addExcelError("confirmTestNo", row, cols, style);

        }
        object.put("errors", errors);
        return object;
    }

    private PqmVctRecencyEntity getVctRecency(PqmVctRecencyForm item, String siteID, String provinceID) {
        Date earlyHivDate = TextUtils.convertDate(item.getTestTime(), FORMATDATE);
        PqmVctRecencyEntity model = pqmVctRecencyService.findByCodeAndSiteIDAndEarlyHivDate(item.getConfirmTestNo(), Long.parseLong(siteID), earlyHivDate);
        if (model == null) {
            model = new PqmVctRecencyEntity();
        }
        model.setProvinceID(Long.parseLong(provinceID));
        model.setCode(item.getCode());
        model.setPatientName(TextUtils.toFullname(item.getName()));
        model.setGenderID(item.getGender());
        model.setYearOfBirth(item.getYearOfBirth());
        model.setIdentityCard(item.getIdentityCard());
        model.setObjectGroupID(getObjectGroupId(!StringUtils.isEmpty(item.getObjectGroupID()) ? item.getObjectGroupID().trim() : ""));
        model.setAddress(getAddress(item.getAddress(), item.getWard(), item.getDistrict(), item.getProvince()));
        model.setSiteTesting(item.getSiteTesting());
        model.setConfirmTestNo(item.getConfirmTestNo());
        model.setEarlyHivDate(TextUtils.convertDate(item.getTestTime(), FORMATDATE));
        model.setEarlyDiagnose(item.getEarlyDiagnose().contains("lâu") ? "2" : "1");
        model.setSiteID(Long.parseLong(siteID));
//        model.setPqmVctID(vctID);
        //site
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        Set<Long> siteIDs = new HashSet<>();
        for (SiteEntity site : siteHtc) {
            siteIDs.add(site.getID());
        }
        List<PqmVctEntity> vcts = pqmVctService.findByCodeAndSiteIDs(model.getCode(), siteIDs);
        if (vcts != null && !vcts.isEmpty() && vcts.size() == 1) {
            PqmVctEntity vct = vcts.get(0);
            vct.setEarlyHivDate(model.getEarlyHivDate());
            vct.setEarlyDiagnose(model.getEarlyDiagnose());
            pqmVctService.save(vct);
            model.setPqmVctID(vct.getID());
        }

        return model;
    }

    @Override
    public PqmVctEarlyForm mapping(Map<String, String> cols, List<String> cells) {
        PqmVctEarlyForm item = new PqmVctEarlyForm();
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
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }

    private String getAddress(String address, String ward, String district, String province) {
        String add = "";
        if (!StringUtils.isEmpty(address)) {
            add += address + ", ";
        }
        if (!StringUtils.isEmpty(ward)) {
            add += ward + ", ";
        }
        if (!StringUtils.isEmpty(district)) {
            add += district + ", ";
        }
        if (!StringUtils.isEmpty(province)) {
            add += province;
        }
        return add;
    }

    public PqmVctRecencyForm mappingVctRecency(Map<String, String> cols, List<String> cells) {
        PqmVctRecencyForm item = new PqmVctRecencyForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    case "gender":
                        HashMap<String, String> option = options.get(ParameterEntity.GENDER);
                        Set<String> optionKeys = option.keySet();
                        for (String key : optionKeys) {
                            if (TextUtils.removeDiacritical(option.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setGender(key);
                                break;
                            }
                        }
                        break;
                    case "objectGroupID":
                        item.setObjectGroupID(cells.get(i).trim());
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
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

    private String getObjectGroupId(String strDoiTuong) {
        String maDT = "";
        if (!StringUtils.isEmpty(strDoiTuong)) {
            switch (strDoiTuong) {
                case "TCMT":
                    maDT = "1";
                    break;
                case "MSM":
                    maDT = "3";
                    break;
                case "Người bán dâm":
                    maDT = "2";
                    break;
                case "TG":
                    maDT = "13";
                    break;
                case "Vợ/chồng/bạn tình của người NCMT":
                    maDT = "33";
                    break;
                case "Vợ/bạn tình nữ của nam có QHTD đồng giới":
                    maDT = "16";
                    break;
                case "Vợ/bạn tình của người nhiễm":
                    maDT = "4";
                    break;
                case "Người mua dâm":
                    maDT = "17";
                    break;
                case "Nhiều bạn tình":
                    maDT = "18";
                    break;
                case "Bệnh nhân nghi AIDS":
                    maDT = "10";
                    break;
                case "TB":
                    maDT = "6";
                    break;
                case "STD":
                    maDT = "11";
                    break;
                case "Phạm nhân":
                    maDT = "19";
                    break;
                case "PNMT":
                    maDT = "5.1";
                    break;
                case "Phụ nữ chuyển dạ đẻ":
                    maDT = "5.2";
                    break;
                case "Người bán máu":
                    maDT = "34";
                    break;
                case "Người hiến máu tình nguyện":
                    maDT = "35";
                    break;
                case "Người nhà cho máu":
                    maDT = "36";
                    break;
                case "NVQS":
                    maDT = "12";
                    break;
                default:
                    maDT = "7";
                    break;
            }
        }
        return maDT;
    }

}
