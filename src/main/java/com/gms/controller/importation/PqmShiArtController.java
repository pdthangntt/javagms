package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmShiArtForm;
import com.gms.service.LocationsService;
import com.gms.service.PqmLogService;
import com.gms.service.PqmShiArtService;
import java.io.IOException;
import java.util.ArrayList;
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

@Controller(value = "importation_pqm_art")
public class PqmShiArtController extends BaseController<PqmShiArtForm> {

    private static int firstRow = 7;

    private static HashMap<String, HashMap<String, String>> options;

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmShiArtService pqmService;
    @Autowired
    private PqmLogService logService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {

        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        options = parameterService.getOptionsByTypes(parameterTypes, null);

        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-shi-art/import.html");
        form.setSmallUrl("/backend/pqm-shi-art/index.html");
        form.setReadUrl("/pqm-shi-art/import.html");
        form.setTitle("Nhập dữ liệu bệnh nhân nhận thuốc ARV");
        form.setSmallTitle("Bệnh nhân nhận thuốc ARV");
        form.setTemplate(fileTemplate("template_shi_art.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols(boolean key) {
        Map<String, String> cols = new HashMap<>();
        if (key) {
            cols.put("0", "siteName");
            cols.put("1", "zipCode");
            cols.put("2", "bnnt");
            cols.put("3", "bnm");
            cols.put("4", "bnqldt");
            cols.put("5", "bndt12t");
            cols.put("6", "bnnhtk");
            cols.put("7", "tlbnnhtk");
            cols.put("8", "bnbttk");
            cols.put("9", "bnbtlk");
        } else {
            cols.put("0", "siteCode");
            cols.put("1", "siteName");
            cols.put("2", "zipCode");
            cols.put("3", "bnnt");
            cols.put("4", "bnm");
            cols.put("5", "bnqldt");
            cols.put("6", "bndt12t");
            cols.put("7", "bnnhtk");
            cols.put("8", "tlbnnhtk");
            cols.put("9", "bnbttk");
            cols.put("10", "bnbtlk");
        }

        return cols;
    }

    @GetMapping(value = {"/pqm-shi-art/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        return "importation/pqm/upload_shi_art";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-shi-art/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        try {

            List<SiteEntity> siteOpcs = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
            Map<String, String> siteMaps = new HashMap<>();
            for (SiteEntity site : siteOpcs) {
                siteMaps.put(site.getPqmSiteCode(), String.valueOf(site.getID()));
            }

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmShiArtForm> items = new ArrayList<>();

            List<String> siteCodes = new ArrayList<>();
            Set<String> siteCodeSet = new HashSet<>();

            boolean ok = false;
            int year = 0;
            int month = 0;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 2) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1))
                            && StringUtils.isNotEmpty(row.getValue().get(2))) {
                        try {
                            year = Integer.valueOf(row.getValue().get(1));
                            month = Integer.valueOf(row.getValue().get(2));
                            if (year < 0 || month < 1) {
                                year = 0;
                                month = 0;
                            }

                        } catch (Exception e) {
                            year = 0;
                            month = 0;
                        }

                    }
                }
                if (row.getKey() == 4) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(3));
                    System.out.println("333 " + row.getValue().get(0));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("Mã CSKCB"))
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Tên CSKCB"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Số BN"))) {
                        ok = true;
                    }
                }
                if (row.getKey() > 5) {
                    if (row.getValue().size() == 0 || (row != null && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
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

                    PqmShiArtForm item = mapping(cols(key), row.getValue());
                    item.setRow(row.getKey() + 1);
                    item.setKey(key);
                    items.add(item);
                    siteCodes.add(item.getSiteCode());
                }

            }

            model.addAttribute("form", form);

            if (!siteCodes.isEmpty()) {
                for (String string : siteCodes) {
                    siteCodeSet.add(string);
                }
            }

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }
            if (year == 0 || month == 0 || month > 12) {
                redirectAttributes.addFlashAttribute("error", "Tháng, năm không đúng định dạng hoặc bị bỏ trống. Vui lòng kiểm tra lại file excel.");
                return redirect(form.getUploadUrl());
            }

            if (siteCodes.isEmpty() || siteCodeSet.size() != siteCodes.size()) {
                redirectAttributes.addFlashAttribute("error", "Mã cơ sở KCB không được để trống hoặc phải là duy nhất trong file dữ liệu. Vui lòng kiểm tra lại!");
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

            Set<PqmShiArtEntity> datas = new HashSet<>();
            String provinceID = getCurrentUser().getSite().getProvinceID();
            for (PqmShiArtForm item : items) {

                item.setMonth(String.valueOf(month));
                item.setYear(String.valueOf(year));
                item.setProvinceID(provinceID);
                i += 1;
                //Lấy dòng excel
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateCustom(item, row, style, cols, siteMaps);
                if (object.get("errors").isEmpty()) {
                    PqmShiArtEntity entity = getEntity(item, siteMaps);

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
                for (PqmShiArtEntity data : datas) {
                    pqmService.save(data);
                }
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                logService.log("Tải file excel nhập DL BN nhận thuốc ARV", items.size(), success, items.size() - success, "Tuyến tỉnh", getCurrentUser().getSite().getID(), "Bệnh nhân nhận thuốc ARV");
                model.addAttribute("ok", true);
                return "importation/pqm/shi_art";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/shi_art";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private HashMap<String, List<String>> validateCustom(PqmShiArtForm item1, Row row, CellStyle style, Map<String, String> cols, Map<String, String> siteMaps) {
        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();

        //validate nhập vào
//        if (StringUtils.isEmpty(item1.getSiteCode())) {
//            errors.add("Mã CSKCB không được để trống");
//            addExcelError("siteCode", row, cols, style);
//        }
        if (StringUtils.isEmpty(item1.getSiteCode()) || StringUtils.isEmpty(siteMaps.getOrDefault(item1.getSiteCode(), null))) {
            errors.add("Mã CSKCB không được để trống hoặc chưa được kết nối với cơ sở ARV, vui lòng nhập mã cơ sở tại tỉnh trong chức năng Cơ sở dịch vụ");
            addExcelError("siteCode", row, cols, style);
        }

        if (!checkInteger(item1.getBnnt())) {
            errors.add("Số BN hiện nhận thuốc phải là số nguyên dương");
            addExcelError("bnnt", row, cols, style);
        }
        if (!checkInteger(item1.getBnm())) {
            errors.add("Số BN mới phải là số nguyên dương");
            addExcelError("bnm", row, cols, style);
        }
        if (!checkInteger(item1.getBnqldt())) {
            errors.add("Số BN quay lại điều trị phải là số nguyên dương");
            addExcelError("bnqldt", row, cols, style);
        }
        if (!checkInteger(item1.getBndt12t())) {
            errors.add("Số BN điều trị trên 12 tháng phải là số nguyên dương");
            addExcelError("bndt12t", row, cols, style);
        }
        if (!checkInteger(item1.getBnnhtk())) {
            errors.add("Số BN nhỡ hẹn tái khám phải là số nguyên dương");
            addExcelError("bnnhtk", row, cols, style);
        }
        if (!checkLong(item1.getTlbnnhtk())) {
            errors.add("Tỷ lệ BN nhỡ hẹn tái khám phải là số dương");
            addExcelError("tlbnnhtk", row, cols, style);
        }
        if (!checkInteger(item1.getBnbttk())) {
            errors.add("Số BN bỏ trị trong kỳ phải là số nguyên dương");
            addExcelError("bnbttk", row, cols, style);
        }
        if (!checkInteger(item1.getBnbtlk())) {
            errors.add("Số BN bỏ trị lũy kế phải là số nguyên dương");
            addExcelError("bnbtlk", row, cols, style);
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
            double v = Double.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private PqmShiArtEntity getEntity(PqmShiArtForm item, Map<String, String> siteMaps) {

        PqmShiArtEntity model = pqmService.findByProvinceIdAndMonthAndYearAndSiteID(Long.valueOf(siteMaps.get(item.getSiteCode())), item.getProvinceID(), Integer.valueOf(item.getMonth()), Integer.valueOf(item.getYear()));

        if (model == null) {
            model = new PqmShiArtEntity();
        }

        model.setSiteID(Long.valueOf(siteMaps.get(item.getSiteCode())));
        model.setSiteCode(item.getSiteCode());
        model.setSiteName(item.getSiteName());
        model.setZipCode(item.getZipCode());
        model.setProvinceID(item.getProvinceID());
        model.setMonth(Integer.valueOf(item.getMonth()));
        model.setYear(Integer.valueOf(item.getYear()));
        model.setBnnt(StringUtils.isEmpty(item.getBnnt()) ? 0 : Integer.valueOf(item.getBnnt()));
        model.setBnm(StringUtils.isEmpty(item.getBnm()) ? 0 : Integer.valueOf(item.getBnm()));
        model.setBnqldt(StringUtils.isEmpty(item.getBnqldt()) ? 0 : Integer.valueOf(item.getBnqldt()));
        model.setBndt12t(StringUtils.isEmpty(item.getBndt12t()) ? 0 : Integer.valueOf(item.getBndt12t()));
        model.setBnnhtk(StringUtils.isEmpty(item.getBnnhtk()) ? 0 : Integer.valueOf(item.getBnnhtk()));
        model.setTlbnnhtk(StringUtils.isEmpty(item.getTlbnnhtk()) ? Double.valueOf("0") : Double.valueOf(item.getTlbnnhtk()));
        model.setBnbttk(StringUtils.isEmpty(item.getBnbttk()) ? 0 : Integer.valueOf(item.getBnbttk()));
        model.setBnbtlk(StringUtils.isEmpty(item.getBnbtlk()) ? 0 : Integer.valueOf(item.getBnbtlk()));

        return model;
    }

    @Override
    public PqmShiArtForm mapping(Map<String, String> cols, List<String> cells) {
        PqmShiArtForm item = new PqmShiArtForm();
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
