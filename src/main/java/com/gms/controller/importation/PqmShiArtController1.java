package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmArvOneSheetImportForm;
import com.gms.entity.form.PqmShiArtImportForm;
import com.gms.service.ParameterService;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmShiArtService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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

@Controller(value = "importation_pqm_shi_art")
public class PqmShiArtController1 extends BaseController<PqmShiArtImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 3;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PqmShiArtService shiArtService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-shi-art/import1.html");
        form.setSmallUrl("/backend/pqm-shi-art/index.html");
        form.setReadUrl("/pqm-shi-art/import1.html");
        form.setTitle("Tải file excel BN nhận thuốc ARV");
        form.setSmallTitle("Điều trị ARV theo BHYT");
        form.setTemplate(fileTemplate("A07 Thong ke BN nhan thuoc_Template.xlsx"));
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
        return options;
    }

    @GetMapping(value = {"/pqm-shi-art/import1.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        return "importation/pqm-shi/upload";
    }

    @PostMapping(value = "/pqm-shi-art/import1.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
//        try {
//
//            form.setData(readFileFormattedCell(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model, -1));
//            form.setFileName(file.getOriginalFilename());
//            form.setFilePath(fileTmp(file));
////
//            List<PqmShiArtImportForm> data = new ArrayList<>();
//            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
//                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
//                    continue;
//                }
//                if (row.getKey() <= (firstRow - 2)) {
//                    continue;
//                }
//                if (!checkRowIsValid(row)) {
//                    continue;
//                }
//                PqmShiArtImportForm item = mapping(cols(), row.getValue());
//                if (StringUtils.isNotEmpty(item.getMonth())) {
//                    String time = item.getMonth();
//                    item.setMonth(time.length() > 4 ? time.substring(item.getMonth().length() - 2) : time);
//                    item.setYear(time.length() > 4 ? time.substring(0, 4) : time);
//                    item.setTime(time);
//                }
//                data.add(item);
//            }
//            HashMap<String, List< String>> errors = new LinkedHashMap<>();
//            int i = firstRow - 1;
//            int success = 0;
//
//            for (PqmShiArtImportForm item : data) {
//                i += 1;
//                HashMap<String, List< String>> object = validateImport(item);
//                if (!object.get("errors").isEmpty()) {
//                    errors.put(String.valueOf(i), object.get("errors"));
//                    continue;
//                }
//                item.setSiteID(getCurrentUser().getSite().getID().toString());
//                //Set to entity
//                PqmShiArtEntity entity = getShiArt(item, getCurrentUser().getSite().getID().toString(), getCurrentUser().getSite().getProvinceID());
//                try {
//                    //ART SHI
//                    shiArtService.save(entity);
//                    success += 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    errors.put(String.valueOf(i), new ArrayList<String>());
//                    errors.get(String.valueOf(i)).add(e.getMessage());
//
//                }
//            }
//            model.addAttribute("success", "Đã tiến hành import excel thành công");
//            model.addAttribute("errorsw", errors);
//            model.addAttribute("total", data.size());
//            model.addAttribute("successx", success);
//            model.addAttribute("form", form);

            return "importation/pqm-shi/arv_shi_art_alert";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
//            return redirect(form.getUploadUrl());
//        }
    }

    private Boolean checkRowIsValid(Map.Entry<Integer, List<String>> row) {
        Boolean flag = false;
        if (row.getValue() != null && !row.getValue().isEmpty()) {
            for (int i = 0; i <= 5; i++) {
                if (row.getValue().get(i) != null && !row.getValue().get(i).equals("")) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private HashMap<String, List<String>> validateImport(PqmShiArtImportForm form) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        validateNull(form.getTime(), "Thời gian", errors);
        if (form.getTime() != null && !form.getTime().equals("")) {
            if (!isValidTime(form.getTime()) || form.getTime().length() < 6) {
                errors.add("Thời gian không đúng định dạng năm tháng (yyyyMM)");
            }
        }
        validateNull(form.getQuantity(), "Hiện nhận thuốc", errors);
        validInteger(form.getQuantityNew(), "Mới", errors);
        validInteger(form.getQuantityNhtk(), "Nhỡ hẹn tái khám trong tháng", errors);
        validInteger(form.getQuantityQldt(), "Quay lại điều trị", errors);
        validInteger(form.getQuantityBt(), "Bỏ trị", errors);
        validInteger(form.getQuantity(), "Hiện nhận thuốc", errors);
        object.put("errors", errors);

        return object;
    }

//    private PqmShiArtEntity getShiArt(PqmShiArtImportForm item, String siteId, String provinceID) {
//        PqmShiArtEntity model = shiArtService.findByProvinceIdAndMonthAndYear(provinceID, Integer.parseInt(item.getMonth()), Integer.parseInt(item.getYear()));
//        if (model == null) {
//            model = new PqmShiArtEntity();
//        }
//        model.setMonth(Integer.parseInt(item.getMonth()));
//        model.setYear(Integer.parseInt(item.getYear()));
//        model.setProvinceID(provinceID);
//        model.setQuantity(Integer.parseInt(item.getQuantity()));
//        model.setQuantityNew(StringUtils.isNotEmpty(item.getQuantityNew()) ? Integer.parseInt(item.getQuantityNew()) : 0);
//        model.setQuantityNhtk(StringUtils.isNotEmpty(item.getQuantityNhtk()) ? Integer.parseInt(item.getQuantityNhtk()) : 0);
//        model.setQuantityQldt(StringUtils.isNotEmpty(item.getQuantityQldt()) ? Integer.parseInt(item.getQuantityQldt()) : 0);
//        model.setQuantityBt(StringUtils.isNotEmpty(item.getQuantityBt()) ? Integer.parseInt(item.getQuantityBt()) : 0);
//        model.setSiteID(Long.valueOf(siteId));
//        return model;
//    }

    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "month");
        cols.put("1", "quantity");
        cols.put("2", "quantityNew");
        cols.put("3", "quantityNhtk");
        cols.put("4", "quantityQldt");
        cols.put("5", "quantityBt");
        return cols;
    }

    @Override
    public PqmShiArtImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmShiArtImportForm item = new PqmShiArtImportForm();
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

    private void validInteger(String text, String message, List<String> errors) {
        Boolean isError = false;
        if (text != null && !"".equals(text)) {
            try {
                Integer.parseInt(text);
            } catch (Exception e) {
                isError = true;
            }
            if (isError) {
                errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> phải là số nguyên </i>", message));
            }
        }
    }
}
