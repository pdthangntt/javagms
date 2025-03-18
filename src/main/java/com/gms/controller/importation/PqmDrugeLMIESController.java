package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmElmiseForm;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmDrugeLMISEService;
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
@Controller(value = "importation_pqm_drug_elmise")
public class PqmDrugeLMIESController extends BaseController<PqmElmiseForm> {

    private static int firstRow = 2;
    private static String textTitle = "";

    @Autowired
    private PqmDrugeLMISEService drugeLMISEService;
    @Autowired
    private PqmLogService logService;

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
        form.setUploadUrl("/pqm-drug-elmise/import.html");
        form.setSmallUrl("/backend/pqm-drug-elmise/index.html");
        form.setReadUrl("/pqm-drug-elmise/import.html");
        form.setTitle("Nhập dữ liệu cung ứng, sử dụng thuốc");
        form.setSmallTitle("Cung ứng, sử dụng thuốc ARV");
        form.setTemplate(fileTemplate("template_A06.xlsx"));
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
        cols.put("1", "");
        cols.put("2", "siteCode");
        cols.put("3", "");
        cols.put("4", "drugCode");
        cols.put("5", "drugName");
        cols.put("6", "soDangKy");
        cols.put("7", "ttThau");
        cols.put("8", "");
        cols.put("9", "duTru");
        cols.put("10", "");
        cols.put("11", "");
        cols.put("12", "");
        cols.put("13", "");
        cols.put("14", "");
        cols.put("15", "");
        cols.put("16", "");
        cols.put("17", "");
        cols.put("18", "tongSuDung");
        cols.put("19", "tonKho");

        return cols;
    }

    @GetMapping(value = {"/pqm-drug-elmise/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());

        Map<String, String> years = new LinkedHashMap<>();
        Map<String, String> quarters = new LinkedHashMap<>();
        years.put("", "Chọn Năm");
        for (int i = 1990; i < 2200; i++) {
            years.put(String.valueOf(i), "Năm " + i);
        }

        quarters.put("", "Chọn Quý");
        quarters.put("1", "Quý " + 1);
        quarters.put("2", "Quý " + 2);
        quarters.put("3", "Quý " + 3);
        quarters.put("4", "Quý " + 4);

        model.addAttribute("years", years);
        model.addAttribute("quarters", quarters);
        return "importation/pqm/upload_elmies";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-drug-elmise/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            RedirectAttributes redirectAttributes) {
        ImportForm form = initForm();
        try {
            if (StringUtils.isEmpty(quarter) || StringUtils.isEmpty(year)) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn Quý, Năm để nhập dữ liệu");
                return redirect(form.getUploadUrl());
            }
            int q = Integer.valueOf(quarter);
            int y = Integer.valueOf(year);

            System.out.println(q + " c " + y);

            List<SiteEntity> siteOpcs = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
            Map<String, String> siteMaps = new HashMap<>();
            Map<String, String> siteNames = new HashMap<>();
            for (SiteEntity site : siteOpcs) {
                siteMaps.put(site.getPqmSiteCode(), String.valueOf(site.getID()));
                siteNames.put(site.getID().toString(), site.getName());
            }

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmElmiseForm> items = new ArrayList<>();

            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 0) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("MA_TINH"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("MA_CSKCB"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("TEN_CSKCB"))) {
                        ok = true;
                    }
                }
                if (row.getKey() > 0) {
                    if (row.getValue().size() == 0 || (row != null && (row.getValue().get(1) == null || row.getValue().get(1).equals("")) && (row.getValue().get(10) == null || row.getValue().get(10).equals(""))
                            )) {
                        continue;
                    }
                    PqmElmiseForm item = mapping(cols(), row.getValue());
                    item.setRow(row.getKey() + 1);
                    items.add(item);
                }
            }
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

            Set<PqmDrugeLMISEEntity> datas = new HashSet<>();
            for (PqmElmiseForm item : items) {
                item.setQ(q);
                item.setY(y);

                i += 1;
                //Lấy dòng excel
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateCustom(item, row, style, cols, siteMaps);
                if (object.get("errors").isEmpty()) {
                    PqmDrugeLMISEEntity entity = getEntity(item, siteMaps, siteNames);
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
                for (PqmDrugeLMISEEntity data : datas) {
                    drugeLMISEService.save(data);
                }
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                logService.log("Tải file excel nhập DL tình hình cung ứng, sử dụng thuốc ARV (eLMIS)", items.size(), success, items.size() - success, "Tuyến tỉnh", getCurrentUser().getSite().getID(), "Tình hình cung ứng, sử dụng thuốc thuốc ARV (eLMIS)");
                model.addAttribute("ok", true);
                return "importation/pqm/drud_elmise";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/drud_elmise";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private PqmDrugeLMISEEntity getEntity(PqmElmiseForm item,
            Map<String, String> siteMaps,
            Map<String, String> siteNames
    ) {
        String provinceID = getCurrentUser().getSite().getProvinceID();

        PqmDrugeLMISEEntity model = drugeLMISEService.findByUniqueConstraints(
                item.getY(),
                item.getQ(),
                provinceID,
                item.getSiteCode(),
                item.getDrugName(),
                item.getDrugCode(),
                item.getTtThau()
        );

        if (model == null) { 
            model = new PqmDrugeLMISEEntity();
        }
        model.setYear(item.getY());
        model.setQuarter(item.getQ());
        model.setProvinceID(provinceID);
        model.setSiteID(Long.valueOf(siteMaps.get(item.getSiteCode())));
        model.setSiteCode(item.getSiteCode());
        model.setSiteName(siteNames.get(model.getSiteID().toString()));
        model.setDrugCode(item.getDrugCode());
        model.setDrugName(item.getDrugName());
        model.setSoDangKy(item.getSoDangKy());
        model.setTtThau(item.getTtThau());
        model.setDuTru(StringUtils.isBlank(item.getDuTru()) ? Long.valueOf("0") : Long.valueOf(item.getDuTru()));
        model.setTongSuDung(StringUtils.isBlank(item.getTongSuDung()) ? Long.valueOf("0") : Long.valueOf(item.getTongSuDung()));
        model.setTonKho(StringUtils.isBlank(item.getTonKho()) ? Long.valueOf("0") : Long.valueOf(item.getTonKho()));
        return model;
    }

    private HashMap<String, List<String>> validateCustom(PqmElmiseForm item1, Row row, CellStyle style, Map<String, String> cols, Map<String, String> siteMaps) {
        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(item1.getSiteCode()) || StringUtils.isEmpty(siteMaps.getOrDefault(item1.getSiteCode(), null))) {
            errors.add("Mã CSKCB không được để trống hoặc chưa được kết nối với cơ sở ARV, vui lòng nhập mã cơ sở tại tỉnh trong chức năng Cơ sở dịch vụ");
            addExcelError("siteCode", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getDrugCode())) {
            errors.add("Mã thuốc không được để trống");
            addExcelError("drugCode", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getDrugName())) {
            errors.add("Tên thuốc không được để trống");
            addExcelError("drugName", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getSoDangKy())) {
            errors.add("Số đăng ký không được để trống");
            addExcelError("soDangKy", row, cols, style);
        }
        if (StringUtils.isEmpty(item1.getTtThau())) {
            errors.add("Quyết định thầu không được để trống");
            addExcelError("ttThau", row, cols, style);
        }

        if (!checkInteger(item1.getDuTru())) {
            errors.add("Số lượng dự trù phải là số nguyên dương");
            addExcelError("duTru", row, cols, style);
        }
        if (!checkInteger(item1.getTongSuDung())) {
            errors.add("Số lượng sử dụng phải là số nguyên dương");
            addExcelError("tongSuDung", row, cols, style);
        }

        if (!checkLong(item1.getTonKho())) {
            errors.add("Số lượng tồn kho phải là số nguyên");
            addExcelError("tonKho", row, cols, style);
        }

        object.put("errors", errors);
        return object;
    }

    private boolean checkInteger(String value) {
        if (StringUtils.isEmpty(value)) {
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

    private boolean checkLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        try {
            double v = Long.valueOf(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public PqmElmiseForm mapping(Map<String, String> cols, List<String> cells) {
        PqmElmiseForm item = new PqmElmiseForm();
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

}
