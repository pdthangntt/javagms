/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.form.HivInfoImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
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
 * @author DSNAnh
 */
@Controller(value = "importation_hiv_info")
public class HivInfoController  extends BaseController<HivInfoImportForm> {
    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 11;
    
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }
    
    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-hiv-info/index.html");
        form.setSmallUrl("/import-hiv-info/index.html");
        form.setReadUrl("/import-hiv-info/index.html");
        form.setTitle("Tải file excel của HIV Info");
        form.setSmallTitle("Mã HIV Info");
        form.setTemplate(fileTemplate("hiv__info.xls"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "id");
        cols.put("1", "code");
        cols.put("2", "");
        cols.put("3", "");
        cols.put("4", "");
        cols.put("5", "");
        cols.put("6", "");
        cols.put("7", "");
        cols.put("8", "");
        cols.put("9", "");
        cols.put("10", "");
        cols.put("11", "");
        cols.put("12", "");
        cols.put("13", "");
        cols.put("14", "");
        cols.put("15", "");
        cols.put("16", "");
        cols.put("17", "");
        cols.put("18", "");
        cols.put("19", "");
        cols.put("20", "");
        cols.put("21", "");
        cols.put("22", "");
        cols.put("23", "");
        cols.put("24", "");
        cols.put("25", "");
        cols.put("26", "");
        cols.put("27", "");
        cols.put("28", "");
        cols.put("29", "");
        return cols;
    }
    
    @GetMapping(value = {"/import-hiv-info/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }
    
    @PostMapping(value = "/import-hiv-info/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        ImportForm form = initForm();
        List<String> existID = new ArrayList<>();
        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            
            //find all item
            List<PacPatientInfoEntity> allItems = pacPatientService.findAllByProvinceId(getCurrentUser().getSite().getProvinceID());
            Map<Long, PacPatientInfoEntity> IDs = new HashMap<>();
            for(PacPatientInfoEntity e : allItems){
                IDs.put(e.getID(),e);
            }
            
            List<PacHivInfoEntity> items = new ArrayList<>();
            int i = firstRow;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() < firstRow && row.getValue() != null && !row.getValue().isEmpty()) {
                    continue;
                }
                if(StringUtils.isEmpty(row.getValue().get(0))){
                    continue;
                }
                try{
                    PacPatientInfoEntity pacPatientInfoEntity = IDs.get(Long.parseLong(row.getValue().get(0).trim()));
                    PacHivInfoEntity pacHivInfoEntity = pacPatientService.findOneHivInfo(Long.parseLong(row.getValue().get(0).trim()));
                    PacHivInfoEntity item = new PacHivInfoEntity();
                    if(pacPatientInfoEntity != null && pacHivInfoEntity == null){
                        item.setID(pacPatientInfoEntity.getID());
                    }
                    if(pacPatientInfoEntity != null && pacHivInfoEntity != null){
                        item = pacHivInfoEntity;
                    }
                    item.setCode(row.getValue().get(1));
                    items.add(item);
                } catch(Exception e){
                    continue;
                }
                
            }
            
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            List<String> doubleCode = new ArrayList<>();
            int success = 0;
            for (PacHivInfoEntity item1 : items) {
                i += 1;
                HashMap<String, List< String>> object = validateHtcVisit(item1, IDs);
                if (object.get("errors").isEmpty()) {
                    item1 = pacPatientService.save(item1);
                    pacPatientService.log(item1.getID(), "Import mã HIV Info từ file excel");
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
            model.addAttribute("existID", StringUtils.join(existID, ", "));
            model.addAttribute("form", form);
            
            return "importation/pac/hiv-info";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }
    
    @Override
    public HivInfoImportForm mapping(Map<String, String> cols, List<String> cells) {
        HivInfoImportForm item = new HivInfoImportForm();
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
    
    private HashMap<String, List<String>> validateHtcVisit(PacHivInfoEntity form, Map<Long, PacPatientInfoEntity> IDs) {
        
        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> doubleCode = new ArrayList<>();

        // Check not null cho Mã HIV Info
        validateNull(form.getCode(), "Mã HIV Info", errors);
        
        // Check not null cho ID HIV Info
        validateNull(String.valueOf(form.getID()), "ID", errors);
        
        if(form.getID() == null){
           errors.add(errorMsg("ID", " không tồn tại ở cơ sở")); 
        }

        // Validate existing code
        if (StringUtils.isNotBlank(form.getCode()) && pacPatientService.existCode(form.getID(),form.getCode())) {
            errors.add(errorMsg("Mã HIV Info", " đã tồn tại"));
            doubleCode.add(form.getCode());
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
