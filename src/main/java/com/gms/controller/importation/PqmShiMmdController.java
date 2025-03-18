package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmShiMmdImportForm;
import com.gms.service.ParameterService;
import com.gms.service.PqmLogService;
import com.gms.service.PqmShiMmdService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

@Controller(value = "importation_pqm_shi_mmd")
public class PqmShiMmdController extends BaseController<PqmShiMmdImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 8;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PqmShiMmdService shiMmdService;
    @Autowired
    private PqmLogService logService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-shi-mmd/import.html");
        form.setSmallUrl("/backend/pqm-shi-mmd/index.html");
        form.setReadUrl("/pqm-shi-mmd/import.html");
        form.setTitle("Nhập dữ liệu kê đơn thuốc ARV");
        form.setSmallTitle("Điều trị ARV theo BHYT");
        form.setTemplate(fileTemplate("template_shi_mmd.xlsx"));
        return form;
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

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        options.get("siteOpc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            if (StringUtils.isNotEmpty(site.getHub()) && site.getHub().equals("1")) {
                options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
            }
        }
        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        options.put("years", new LinkedHashMap<String, String>());
        options.get("years").put("0", "Chọn năm");
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
        options.put("months", new LinkedHashMap<String, String>());
        options.get("months").put("0", "Chọn tháng");
        for (int i = 1; i <= 12; i++) {
            options.get("months").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        return options;
    }

    @GetMapping(value = {"/pqm-shi-mmd/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        model.addAttribute("months", getOptions().get("months"));
        model.addAttribute("years", getOptions().get("years"));
        return "importation/pqm-shi/upload_mmd";
    }

    @PostMapping(value = "/pqm-shi-mmd/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        try {

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<SiteEntity> siteOpcs = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
            Map<String, String> siteMaps = new HashMap<>();
            for (SiteEntity site : siteOpcs) {
                siteMaps.put(site.getPqmSiteCode(), String.valueOf(site.getID()));
            }

            List<PqmShiMmdImportForm> items = new ArrayList<>();
            boolean ok = false;
            int year = 0;
            int month = 0;
            int monthTo = 0;
            List<String> siteCodes = new ArrayList<>();
            Set<String> siteCodeSet = new HashSet<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 2) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && StringUtils.isNotEmpty(row.getValue().get(3))) {
                        try {
                            year = Integer.valueOf(row.getValue().get(1));
                            month = Integer.valueOf(row.getValue().get(2));
                            monthTo = Integer.valueOf(row.getValue().get(3));
                        } catch (Exception e) {
                            year = 0;
                            month = 0;
                            monthTo = 0;
                        }
                    }
                }
                if (row.getKey() == 4) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("Mã CSKCB"))
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Tên CSKCB"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Tổng"))) {
                        ok = true;
                    }
                }
                if (row.getKey() > 6) {
                    if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                        continue;
                    }
                    if (!checkRowIsValid(row)) {
                        continue;
                    }
                    boolean key = false;
                    if (StringUtils.isNotBlank(row.getValue().get(0)) && row.getValue().get(0).length() > 9) {
                        try {
                            System.out.println("444 " + row.getValue().get(0));
                            Integer.valueOf(row.getValue().get(0));
                        } catch (Exception e) {
                            key = true;
                        }
                    }
                    PqmShiMmdImportForm item = mapping(cols(key), row.getValue());
                    item.setRow(row.getKey() + 1);
                    item.setMonth(String.valueOf(month));
                    item.setYear(String.valueOf(year));
                    items.add(item);
                    siteCodes.add(item.getSiteCode());
                }
            }
            if (!siteCodes.isEmpty()) {
                for (String string : siteCodes) {
                    siteCodeSet.add(string);
                }
            }
            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }
            if (year < 0 || year == 0 || month == 0 || month < 0 || month > 12 || year > 2100 || year < 1900) {
                redirectAttributes.addFlashAttribute("error", "Tháng, năm không đúng hoặc bị bỏ trống. Vui lòng kiểm tra lại file excel.");
                return redirect(form.getUploadUrl());
            }
            if (siteCodes.isEmpty() || siteCodeSet.size() != siteCodes.size()) {
                redirectAttributes.addFlashAttribute("error", "Mã cơ sở KCB không được để trống hoặc phải là duy nhất trong file dữ liệu. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }
            if (month != monthTo) {
                redirectAttributes.addFlashAttribute("error", "Dữ liệu import phải trong cùng 1 tháng. Kiểm tra lại thời gian từ tháng, đến tháng trong file excel");
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
            Map<String, String> cols = cols(false);
            Set<PqmShiMmdEntity> datas = new HashSet<>();
            String provinceID = getCurrentUser().getSite().getProvinceID();
            for (PqmShiMmdImportForm item : items) {
                item.setMonth(String.valueOf(month));
                item.setYear(String.valueOf(year));
                item.setProvinceID(provinceID);
                i += 1;
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateImport(item, row, style, cols, siteMaps);
                if (object.get("errors").isEmpty()) {
                    PqmShiMmdEntity entity = getShiMmd(item, siteMaps);
                    try {
                        datas.add(entity);
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
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);
            if (errors.isEmpty()) {
                for (PqmShiMmdEntity data : datas) {
                    shiMmdService.save(data);
                }
                logService.log("Tải file excel nhập DL kê đơn thuốc ARV", items.size(), success, items.size() - success, "Tuyến tỉnh", getCurrentUser().getSite().getID(), "Tình hình kê đơn thuốc ARV");
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                model.addAttribute("ok", true);
                return "importation/pqm/shi_mmd";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/shi_mmd";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private Boolean checkRowIsValid(Map.Entry<Integer, List<String>> row) {
        Boolean flag = false;
        if (row.getValue() != null && !row.getValue().isEmpty()) {
            for (int i = 0; i <= 3; i++) {
                if (row.getValue().get(i) != null && !row.getValue().get(i).equals("")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private HashMap<String, List<String>> validateImport(PqmShiMmdImportForm form, Row row, CellStyle style, Map<String, String> cols, Map<String, String> siteMaps) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();

//        if (StringUtils.isEmpty(form.getSiteCode())) {
//            errors.add("Mã CSKCB không được để trống");
//            addExcelError("siteCode", row, cols, style);
//        }
        if (StringUtils.isEmpty(form.getSiteCode()) || StringUtils.isEmpty(siteMaps.getOrDefault(form.getSiteCode(), null))) {
            errors.add("Mã CSKCB không được để trống hoặc chưa được kết nối với cơ sở ARV, vui lòng nhập mã cơ sở tại tỉnh trong chức năng Cơ sở dịch vụ");
            addExcelError("siteCode", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurnTK())) {
            errors.add("Tổng số lượt trong kỳ phải là số nguyên dương");
            addExcelError("totalTurnTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurnLK())) {
            errors.add("Tổng số lượt lũy kế phải là số nguyên dương");
            addExcelError("totalTurnLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatientTK())) {
            errors.add("Tổng số BN trong kỳ phải là số nguyên dương");
            addExcelError("totalPatientTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatientLK())) {
            errors.add("Tổng số BN lũy kế phải là số nguyên dương");
            addExcelError("totalPatientLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatientLess1MonthTK())) {
            errors.add("Số BN trong kỳ dưới 1 tháng phải là số nguyên dương ");
            addExcelError("totalPatientLess1MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatientLess1MonthLK())) {
            errors.add("Số BN lũy kế dưới 1 tháng phải là số nguyên dương ");
            addExcelError("totalPatientLess1MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurnLess1MonthTK())) {
            errors.add("Số lượt trong kỳ dưới 1 tháng phải là số nguyên dương ");
            addExcelError("totalTurnLess1MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurnLess1MonthLK())) {
            errors.add("Số lượt lũy kế dưới 1 tháng phải là số nguyên dương");
            addExcelError("totalTurnLess1MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient1To2MonthTK())) {
            errors.add("Số BN trong kỳ từ 1-2 tháng phải là số nguyên dương ");
            addExcelError("totalPatient1To2MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient1To2MonthLK())) {
            errors.add("Số BN lũy kế từ 1-2 tháng phải là số nguyên dương");
            addExcelError("totalPatient1To2MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn1To2MonthTK())) {
            errors.add("Số lượt trong kỳ từ 1-2 tháng phải là số nguyên dương ");
            addExcelError("totalTurn1To2MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn1To2MonthLK())) {
            errors.add("Số lượt lũy kế từ 1-2 tháng phải là số nguyên dương");
            addExcelError("totalTurn1To2MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient2To3MonthTK())) {
            errors.add("Số BN trong kỳ từ 2-3 tháng phải là số nguyên dương ");
            addExcelError("totalPatient2To3MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient2To3MonthLK())) {
            errors.add("Số BN lũy kế từ 2-3 tháng phải là số nguyên dương ");
            addExcelError("totalPatient2To3MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn2To3MonthTK())) {
            errors.add("Số lượt trong kỳ từ 2-3 tháng phải là số nguyên dương ");
            addExcelError("totalTurn2To3MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn2To3MonthLK())) {
            errors.add("Số lượt lũy kế từ 2-3 tháng phải là số nguyên dương ");
            addExcelError("totalTurn2To3MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient3MonthTK())) {
            errors.add("Số BN trong 3 tháng phải là số nguyên dương ");
            addExcelError("totalPatient3MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalPatient3MonthLK())) {
            errors.add("Số BN lũy kế trong 3 tháng phải là số nguyên dương ");
            addExcelError("totalPatient3MonthLK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn3MonthTK())) {
            errors.add("Số lượt trong 3 tháng phải là số nguyên dương");
            addExcelError("totalTurn3MonthTK", row, cols, style);
        }
        if (!checkInteger(form.getTotalTurn3MonthLK())) {
            errors.add("Số lượt lũy kế trong 3 tháng phải là số nguyên dương ");
            addExcelError("totalTurn3MonthLK", row, cols, style);
        }

        if (!checkFloat(form.getRatioPatient3MonthTK())) {
            errors.add("Tỷ lệ BN cấp thuốc trong 3 tháng phải là số dương ");
            addExcelError("ratioPatient3MonthTK", row, cols, style);
        }
        if (!checkFloat(form.getRatioPatient3MonthLK())) {
            errors.add("Tỷ lệ BN cấp thuốc lũy kế trong 3 tháng phải là số dương");
            addExcelError("ratioPatient3MonthLK", row, cols, style);
        }
        object.put("errors", errors);
        return object;
    }
//

    private PqmShiMmdEntity getShiMmd(PqmShiMmdImportForm item, Map<String, String> siteMaps) {
        PqmShiMmdEntity model = shiMmdService.findByProvinceIdAndMonthAndYear(Long.valueOf(siteMaps.get(item.getSiteCode())), item.getProvinceID(), Integer.parseInt(item.getMonth()), Integer.parseInt(item.getYear()));
        if (model == null) {
            model = new PqmShiMmdEntity();
        }
        model.setSiteID(Long.valueOf(siteMaps.get(item.getSiteCode())));
        model.setSiteCode(item.getSiteCode());
        model.setSiteName(item.getSiteName());
        model.setZipCode(item.getZipCode() != null ? item.getZipCode() : "0");
        model.setProvinceID(item.getProvinceID());
        model.setMonth(Integer.valueOf(item.getMonth()));
        model.setYear(Integer.valueOf(item.getYear()));
        
        model.setTotalTurnTk(item.getTotalTurnTK() != null ? Integer.parseInt(item.getTotalTurnTK()) : 0);
        model.setTotalTurnLk(item.getTotalTurnLK() != null ? Integer.parseInt(item.getTotalTurnLK()) : 0);
        model.setTotalPatientTk(item.getTotalPatientTK() != null ? Integer.parseInt(item.getTotalPatientTK()) : 0);
        model.setTotalPatientLk(item.getTotalPatientLK() != null ? Integer.parseInt(item.getTotalPatientLK()) : 0);
        model.setTotalTurnLess1MonthTk(item.getTotalTurnLess1MonthTK() != null ? Integer.parseInt(item.getTotalTurnLess1MonthTK()) : 0);
        model.setTotalTurnLess1MonthLk(item.getTotalTurnLess1MonthLK() != null ? Integer.parseInt(item.getTotalTurnLess1MonthLK()) : 0);
        model.setTotalPatientLess1MonthTk(item.getTotalPatientLess1MonthTK() != null ? Integer.parseInt(item.getTotalPatientLess1MonthTK()) : 0);
        model.setTotalPatientLess1MonthLk(item.getTotalPatientLess1MonthLK() != null ? Integer.parseInt(item.getTotalPatientLess1MonthLK()) : 0);
        model.setTotalPatient1To2MonthTk(item.getTotalPatient1To2MonthTK() != null ? Integer.parseInt(item.getTotalPatient1To2MonthTK()) : 0);
        model.setTotalPatient1To2MonthLk(item.getTotalPatient1To2MonthLK() != null ? Integer.parseInt(item.getTotalPatient1To2MonthLK()) : 0);
        model.setTotalTurn1To2MonthTk(item.getTotalTurn1To2MonthTK() != null ? Integer.parseInt(item.getTotalTurn1To2MonthTK()) : 0);
        model.setTotalTurn1To2MonthLk(item.getTotalTurn1To2MonthLK() != null ? Integer.parseInt(item.getTotalTurn1To2MonthLK()) : 0);
        model.setTotalPatient2To3MonthTk(item.getTotalPatient2To3MonthTK() != null ? Integer.parseInt(item.getTotalPatient2To3MonthTK()) : 0);
        model.setTotalPatient2To3MonthLk(item.getTotalPatient2To3MonthLK() != null ? Integer.parseInt(item.getTotalPatient2To3MonthLK()) : 0);
        model.setTotalTurn2To3MonthTk(item.getTotalTurn2To3MonthTK() != null ? Integer.parseInt(item.getTotalTurn2To3MonthTK()) : 0);
        model.setTotalTurn2To3MonthLk(item.getTotalTurn2To3MonthLK() != null ? Integer.parseInt(item.getTotalTurn2To3MonthLK()) : 0);
        model.setTotalPatient3MonthTk(item.getTotalPatient3MonthTK() != null ? Integer.parseInt(item.getTotalPatient3MonthTK()) : 0);
        model.setTotalPatient3MonthLk(item.getTotalPatient3MonthLK() != null ? Integer.parseInt(item.getTotalPatient3MonthLK()) : 0);
        model.setTotalTurn3MonthTk(item.getTotalTurn3MonthTK() != null ? Integer.parseInt(item.getTotalTurn3MonthTK()) : 0);
        model.setTotalTurn3MonthLk(item.getTotalTurn3MonthLK() != null ? Integer.parseInt(item.getTotalTurn3MonthLK()) : 0);
        model.setRatio3MonthTk(item.getRatioPatient3MonthTK() != null ? Float.parseFloat(item.getRatioPatient3MonthTK()) : 0);
        model.setRatio3MonthLk(item.getRatioPatient3MonthLK() != null ? Float.parseFloat(item.getRatioPatient3MonthLK()) : 0);
        return model;
    }

    public Map<String, String> cols(boolean key) {
        Map<String, String> cols = new HashMap<>();
        if (key) {
            cols.put("0", "siteName");
            cols.put("1", "zipCode");
            cols.put("2", "totalTurnTK");
            cols.put("3", "totalTurnLK");
            cols.put("4", "totalPatientTK");
            cols.put("5", "totalPatientLK");
            cols.put("6", "totalTurnLess1MonthTK");
            cols.put("7", "totalTurnLess1MonthLK");
            cols.put("8", "totalPatientLess1MonthTK");
            cols.put("9", "totalPatientLess1MonthLK");
            cols.put("10", "totalTurn1To2MonthTK");
            cols.put("11", "totalTurn1To2MonthLK");
            cols.put("12", "totalPatient1To2MonthTK");
            cols.put("13", "totalPatient1To2MonthLK");
            cols.put("14", "totalTurn2To3MonthTK");
            cols.put("15", "totalTurn2To3MonthLK");
            cols.put("16", "totalPatient2To3MonthTK");
            cols.put("17", "totalPatient2To3MonthLK");
            cols.put("18", "totalTurn3MonthTK");
            cols.put("19", "totalTurn3MonthLK");
            cols.put("20", "totalPatient3MonthTK");
            cols.put("21", "totalPatient3MonthLK");
            cols.put("22", "ratioPatient3MonthTK");
            cols.put("23", "ratioPatient3MonthLK");
        } else {
            cols.put("0", "siteCode");
            cols.put("1", "siteName");
            cols.put("2", "zipCode");
            cols.put("3", "totalTurnTK");
            cols.put("4", "totalTurnLK");
            cols.put("5", "totalPatientTK");
            cols.put("6", "totalPatientLK");
            cols.put("7", "totalTurnLess1MonthTK");
            cols.put("8", "totalTurnLess1MonthLK");
            cols.put("9", "totalPatientLess1MonthTK");
            cols.put("10", "totalPatientLess1MonthLK");
            cols.put("11", "totalTurn1To2MonthTK");
            cols.put("12", "totalTurn1To2MonthLK");
            cols.put("13", "totalPatient1To2MonthTK");
            cols.put("14", "totalPatient1To2MonthLK");
            cols.put("15", "totalTurn2To3MonthTK");
            cols.put("16", "totalTurn2To3MonthLK");
            cols.put("17", "totalPatient2To3MonthTK");
            cols.put("18", "totalPatient2To3MonthLK");
            cols.put("19", "totalTurn3MonthTK");
            cols.put("20", "totalTurn3MonthLK");
            cols.put("21", "totalPatient3MonthTK");
            cols.put("22", "totalPatient3MonthLK");
            cols.put("23", "ratioPatient3MonthTK");
            cols.put("24", "ratioPatient3MonthLK");
        }

        return cols;
    }
//

    @Override
    public PqmShiMmdImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmShiMmdImportForm item = new PqmShiMmdImportForm();
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

    private void validateNull(String text, String message, List<String> errors) {
        if (text == null || "".equals(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
        }
    }

    private boolean isValidTime(String timeStr) {
        DateFormat sdf = new SimpleDateFormat("yyyyMM");
        sdf.setLenient(false);
        try {
            sdf.parse(timeStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean checkInteger(String value) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        try {
            int v = Integer.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean checkFloat(String value) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        try {
            float v = Float.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void validInteger(String text, String message, List<String> errors) {
        Boolean isError = false;
        if (text != null && !"".equals(text)) {
            try {
                Integer.parseInt(text);
            } catch (Exception e) {
                isError = true;
            }
            if (isError || Integer.parseInt(text) < 0) {
                errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> phải là số nguyên dương</i>", message));
            }
        }
    }

    private void validFloat(String text, String message, List<String> errors) {
        Boolean isError = false;
        if (text != null && !"".equals(text)) {
            try {
                Float.parseFloat(text);
            } catch (Exception e) {
                isError = true;
            }
            if (isError) {
                errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> phải là số</i>", message));
            }
        }
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    public static void main(String[] args) {
        String num = "-1";
        System.out.println("HAA:" + Integer.valueOf(num));
    }

//    @Override
//    public PqmShiMmdImportForm mapping(Map<String, String> cols, List<String> cells) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
