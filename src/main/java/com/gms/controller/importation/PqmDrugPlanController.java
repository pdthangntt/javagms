package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.PqmDrugPlanImportForm;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmLogService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

/**
 *
 * @author pdThang
 */
@Controller(value = "importation_pqm_drug_plan")
public class PqmDrugPlanController extends BaseController<PqmDrugPlanImportForm> {

    private static int firstRow = 11;
    private static String textTitle = "";

    @Autowired
    private PqmDrugPlanService drugPlanService;
    @Autowired
    private PqmLogService logService;
    @Autowired
    private PqmDrugEstimateService drugEstimateService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());

        }

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-drug-plan/import.html");
        form.setSmallUrl("/backend/pqm-drug-estimate/index.html");
        form.setReadUrl("/pqm-drug-plan/import.html");
        form.setTitle("Nhập dữ liệu tình hình sử dụng thuốc ARV");
        form.setSmallTitle("Quản lý thuốc ARV");
        form.setTemplate(fileTemplate("TongHopSuDungThuoc.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "provinceID");
        cols.put("2", "siteCode");
        cols.put("3", "siteName");
        cols.put("4", "source");
        cols.put("5", "drugName");
        cols.put("6", "packing");
        cols.put("7", "unit");
        cols.put("8", "lotNumber");
        cols.put("9", "expiryDate");
        cols.put("10", "");
        cols.put("11", "beginning");
        cols.put("12", "inThePeriod");
        cols.put("13", "patient");
        cols.put("14", "transfer");
        cols.put("15", "loss");
        cols.put("16", "ending");

        return cols;
    }

    @GetMapping(value = {"/pqm-drug-plan/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/pqm-drug-plan/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        int index = 0;
        try {
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            try {
                form.setData(readFileFormattedCell(workbook, 1, model, -1));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            List<PqmDrugPlanImportForm> items = new LinkedList<>();
            String year = "";
            String month = "";
            String text = "";
            boolean flagCheckDate = false;
            boolean isTemplateTrue = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (index == 8) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("STT"))
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Tỉnh"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Mã KCB"))) {
                        isTemplateTrue = true;
                    }
                }
                if (index == 6) {
                    try {
                        String time = row.getValue().get(0);
                        text = time == null ? null : time.substring(time.length() - 7);
                        month = text == null ? null : text.substring(0, 2);
                        year = text == null ? null : text.substring(text.length() - 4);
                        Integer.valueOf(year);
                        Integer.valueOf(month);
                    } catch (Exception e) {
                        flagCheckDate = true;
                    }
                }
                if (index > 10 && (row.getValue() == null || (StringUtils.isBlank(row.getValue().get(1)) && StringUtils.isBlank(row.getValue().get(2)) && StringUtils.isBlank(row.getValue().get(3))))) {
                    break;
                }
                if (index > 10) {
                    PqmDrugPlanImportForm item = mapping(cols(), row.getValue());
                    item.setMonth(month);
                    item.setYear(year);
                    items.add(item);
                }
            }
            if (!isTemplateTrue) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }
            if (month == null || year == null || !isThisDateValid("01/" + text)) {
                redirectAttributes.addFlashAttribute("error", "Không lấy được tháng, năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }
            if (flagCheckDate) {
                redirectAttributes.addFlashAttribute("error", "Không đọc được tháng, năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = firstRow - 1;
            int success = 0;
            //tạm
            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(1);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();
            Set<PqmDrugPlanEntity> datas = new HashSet<>();
            for (PqmDrugPlanImportForm item : items) {
                i++;
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateCustom(item, row, style, cols);
                if (object.get("errors").isEmpty()) {
                    PqmDrugPlanEntity entity = toEntity(item);
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
            model.addAttribute("filePath", saveFile(workbook, file));
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            model.addAttribute("form", form);
            if (errors.isEmpty()) {
                for (PqmDrugPlanEntity data1 : datas) {
                    PqmDrugPlanEntity  data = drugPlanService.save(data1.clone());
                    List<PqmDrugEstimateEntity> estimates = drugEstimateService.findUpdateUnit(getCurrentUser().getSite().getProvinceID(), data.getDrugName(), data.getSiteCode(), Integer.valueOf(year));
                    if (estimates != null && !estimates.isEmpty()) {
                        for (PqmDrugEstimateEntity estimate : estimates) {
                            if (StringUtils.isEmpty(estimate.getUnit()) || estimate.getUnit().equals("Không rõ")) {
                                estimate.setUnit(data.getUnit());
                                drugEstimateService.save(estimate);
                            }
                        }

                    }
                }
                logService.log("Tải file excel nhập DL tình hình sử dụng thuốc", items.size(), success, items.size() - success, "Tuyến tỉnh", getCurrentUser().getSite().getID(), "Tình hình sử dụng thuốc ARV");
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                model.addAttribute("ok", true);
                return "importation/pqm/drug";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/drug";
            }
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private PqmDrugPlanEntity toEntity(PqmDrugPlanImportForm item) {

        PqmDrugPlanEntity model = drugPlanService.findByUniqueConstraints(Integer.valueOf(item.getMonth()), Integer.valueOf(item.getYear()), item.getProvinceID(), item.getDrugName(), item.getSiteCode(), item.getSource(), item.getLotNumber(), item.getUnit());
        if (model == null) {
            model = new PqmDrugPlanEntity();
        }
        model.setMonth(Integer.valueOf(item.getMonth()));
        model.setYear(Integer.valueOf(item.getYear()));
        model.setProvinceID(item.getProvinceID());
        model.setSiteCode(item.getSiteCode());
        model.setSiteName(item.getSiteName());
        model.setSource(item.getSource());
        model.setDrugName(item.getDrugName());
        model.setPacking(StringUtils.isEmpty(item.getPacking()) ? "0" : item.getPacking());
        model.setUnit(item.getUnit());
        model.setLotNumber(item.getLotNumber());
        model.setExpiryDate(StringUtils.isEmpty(item.getExpiryDate()) || item.getExpiryDate().equals("null") ? "" : item.getExpiryDate());
        model.setBeginning(StringUtils.isEmpty(item.getBeginning()) ? Long.valueOf(0) : Long.valueOf(item.getBeginning()));
        model.setInThePeriod(StringUtils.isEmpty(item.getInThePeriod()) ? Long.valueOf(0) : Long.valueOf(item.getInThePeriod()));
        model.setPatient(StringUtils.isEmpty(item.getPatient()) ? Long.valueOf(0) : Long.valueOf(item.getPatient()));
        model.setTransfer(StringUtils.isEmpty(item.getTransfer()) ? Long.valueOf(0) : Long.valueOf(item.getTransfer()));
        model.setLoss(StringUtils.isEmpty(item.getLoss()) ? Long.valueOf(0) : Long.valueOf(item.getLoss()));
        model.setEnding(StringUtils.isEmpty(item.getEnding()) ? Long.valueOf(0) : Long.valueOf(item.getEnding()));
        model.setCurrentProvinceID(getCurrentUser().getSite().getProvinceID());
        return model;
    }

    private HashMap<String, List<String>> validateCustom(PqmDrugPlanImportForm item, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        validateNull(item.getProvinceID(), "Tỉnh", errors, "provinceID", row, style, cols);
        validateNull(item.getSiteCode(), "Mã KCB", errors, "siteCode", row, style, cols);
        validateNull(item.getSiteName(), "Tên cơ sở", errors, "siteName", row, style, cols);
        validateNull(item.getSource(), "Nguồn thuốc", errors, "source", row, style, cols);
        validateNull(item.getDrugName(), "Tên thuốc", errors, "drugName", row, style, cols);
        validateNull(item.getLotNumber(), "Số lô", errors, "lotNumber", row, style, cols);
        validateNull(item.getUnit(), "Đơn vị tính", errors, "unit", row, style, cols);

        if (!checkNumber(item.getPacking())) {
            errors.add("Quy cách đóng gói chỉ được nhập số nguyên dương");
            addExcelError("packing", row, cols, style);
        }
        if (!checkNumber(item.getBeginning())) {
            errors.add("Số lượng tồn đầu kỳ chỉ được nhập số nguyên dương");
            addExcelError("beginning", row, cols, style);
        }
        if (!checkNumber(item.getInThePeriod())) {
            errors.add("Số lượng nhập trong kỳ chỉ được nhập số nguyên dương");
            addExcelError("inThePeriod", row, cols, style);
        }
        if (!checkNumber(item.getPatient())) {
            errors.add("Số lượng xuất cho bệnh nhân trong kỳ chỉ được nhập số nguyên dương");
            addExcelError("patient", row, cols, style);
        }
        if (!checkNumber(item.getTransfer())) {
            errors.add("Số lượng xuất điều chuyển trong kỳ chỉ được nhập số nguyên dương");
            addExcelError("transfer", row, cols, style);
        }
        if (!checkNumber(item.getEnding())) {
            errors.add("Số lượng tồn cuối kỳ chỉ được nhập số nguyên dương");
            addExcelError("ending", row, cols, style);
        }
        if (!checkNumber(item.getLoss())) {
            errors.add("Số lượng hư hao chỉ được nhập số nguyên dương");
            addExcelError("loss", row, cols, style);
        }

        if (StringUtils.isNotBlank(item.getExpiryDate()) && !item.getExpiryDate().equals("null") && !isThisDateValid(item.getExpiryDate())) {
            errors.add("Hạn sử dụng có ngày không hợp lệ");
            addExcelError("expiryDate", row, cols, style);
        }
        object.put("errors", errors);

        return object;
    }

    private boolean checkNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return true;
        }
        try {
            Integer.valueOf(number);
            if (Integer.valueOf(number) < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private void validateNull(String text, String message, List<String> errors, String colName, Row row, CellStyle style, Map<String, String> cols) {
        if (StringUtils.isBlank(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
            addExcelError(colName, row, cols, style);
        }

    }

    @Override
    public PqmDrugPlanImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmDrugPlanImportForm item = new PqmDrugPlanImportForm();
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
//                getLogger().warn(ex.getMessage());
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

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

}
