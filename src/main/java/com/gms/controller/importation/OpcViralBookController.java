package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.OpcViralBookImportForm;
import com.gms.repository.impl.OpcBookViralRepositoryImpl;
import com.gms.service.OpcArvService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.ParameterService;
import java.io.Console;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author TrangBN
 */
@Controller(value = "importation_opc_viral_book")
public class OpcViralBookController extends BaseController<OpcViralBookImportForm>  {
    private static final int FIRST_ROW = 8;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcBookViralRepositoryImpl bookViralImpl;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");
        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-opc-viral-book/index.html");
        form.setSmallUrl("/import-opc-viral-book/index.html");
        form.setReadUrl("/import-opc-viral-book/index.html");
        form.setTitle("Thêm lượt xét nghiệm TLVR sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("opc_arv_viral_book.xls"));
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
        cols.put("2", "code");
        cols.put("3", "");
        cols.put("4", "");
        cols.put("5", "");
        cols.put("6", "");
        cols.put("7", "");
        cols.put("8", "");
        cols.put("9", "causes0");   // Sau 6 tháng ĐT ARV
        cols.put("10", "causes1"); // Sau 12 tháng ĐT ARV
        cols.put("11", "causes2"); // Định kỳ khác
        cols.put("12", "causes3"); // Nghi ngờ thất bại điều trị
        cols.put("13", "causes4"); // PN mang thai PN cho con bú
        cols.put("14", "sampleTime"); // Ngày lấy mẫu
        cols.put("15", "resultTime");
        cols.put("16", "resultNumber0");
        cols.put("17", "resultNumber1");
        cols.put("18", "resultNumber2");
        cols.put("19", "resultNumber3");
        cols.put("20", "");
        cols.put("21", "");
        cols.put("22", "resultNumber4");
        cols.put("23", "resultNumber5");
        cols.put("24", "");
        cols.put("25", "note");
        return cols;
    }

    @GetMapping(value = {"/import-opc-viral-book/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-opc-viral-book/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        
        Long siteID = getCurrentUser().getSite().getID();
        ImportForm form = initForm();
        int index = 0;
        try {
            form.setData(readFileFormattedCell(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            
            List<OpcViralBookImportForm> items = new LinkedList<>();  
            Map<String, OpcViralBookImportForm> itemMap = new LinkedHashMap<>();
            
            OpcViralBookImportForm item = new OpcViralBookImportForm();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (index < FIRST_ROW) {
                    continue;
                }
                try {
                    if (StringUtils.isNotEmpty(row.getValue().get(0)) && row.getValue().get(0).trim().toLowerCase().equals("tổng hợp số liệu trong tháng")) {
                        break;
                    }
                    if (StringUtils.isEmpty(row.getValue().get(2)) && StringUtils.isEmpty(row.getValue().get(3))) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                row.getValue();
                item = mapping(cols(), row.getValue());
                item.setSiteID(siteID);
                correctViral(item);
                items.add(item);
                
                // Chuẩn hóa chuỗi code
                if (StringUtils.isNotEmpty(item.getCode())) {
                    String code = item.getCode().trim().replaceAll("\n ", "");
                    code = code.replaceAll(" ", "");
                    item.setCode(code);
                }
                
                if (itemMap.containsKey(item.getCode())) {
                    item.setCode(item.getCode() + index + "duplicated");
                    itemMap.put(item.getCode() + index+ "duplicated", item);
                } else {
                    itemMap.put(item.getCode(), item);
                }
            }
            
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = FIRST_ROW - 1;
            if(form.getData().get(i).get(0) == null){
                i = FIRST_ROW;
            }
            int success = 0;

            OpcViralLoadEntity e = new OpcViralLoadEntity();
            List<OpcViralBookImportForm> books = new ArrayList<>();
            OpcViralLoadEntity viral = new OpcViralLoadEntity();
            Map<String, OpcViralBookImportForm> bookMap = new HashMap<>();
            
            for (Map.Entry<String, OpcViralBookImportForm> item1 : itemMap.entrySet()) {
                i++;
                e = new OpcViralLoadEntity();
                HashMap<String, List<String>> object = validateCustom(item1.getValue());
                if (object.get("errors").isEmpty()) {
                    books = bookViralImpl.getViralBook(item1.getValue().getCode(), item1.getValue().getSiteID());
                    if (books == null || books.isEmpty()) {
                        continue;
                    }
                    
                    for (OpcViralBookImportForm book : books) {
                        bookMap.put(book.getTestTime(), book);
                    }
                    if (bookMap.get(item1.getValue().getTestTime()) != null && bookMap.get(item1.getValue().getTestTime()).getCode().equals(item1.getValue().getCode())) {
                        viral = opcViralLoadService.findOne(bookMap.get(item1.getValue().getTestTime()).getViralID());
                        e = toEntity(item1.getValue(), viral);
                    } else {
                        item1.getValue().setStageID(books.get(0).getStageID());
                        e = toEntity(item1.getValue(), null);
                    }

                    //Set entity
                    e.setImportable(true);
                    boolean b = e.getID() == null;
                    e = opcViralLoadService.save(e);
                    opcViralLoadService.logViralLoad(e.getArvID(), e.getPatientID(), e.getID(), b ? "Thêm lượt xét nghiệm TLVR sử dụng import excel" : "Cập nhật lượt xét nghiệm TLVR sử dụng import excel");
                    success += 1;
                    continue;
                }
                errors.put(String.valueOf(i), object.get("errors"));
            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

            return "importation/opc-arv/opc_viral";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    /**
     * Convert to OpcViralLoadEntity
     * 
     * @param item
     * @param e
     * @return 
     */
    private OpcViralLoadEntity toEntity(OpcViralBookImportForm item, OpcViralLoadEntity e) {
        
        OpcViralLoadEntity viral  = new OpcViralLoadEntity();
        if (e != null) {
            viral = e;
        } else {
            viral.setArvID(item.getArvID());
            viral.setPatientID(item.getPatientID());
            viral.setStageID(item.getStageID());
            viral.setSiteID(item.getSiteID());
        }
        viral.setResultTime(StringUtils.isNotEmpty(item.getResultTime()) ? TextUtils.convertDate(item.getResultTime().trim(), FORMATDATE) : null);
        viral.setTestSiteID(item.getSiteID());
        viral.setSampleTime(StringUtils.isNotEmpty(item.getSampleTime()) ? TextUtils.convertDate(item.getSampleTime().trim(), FORMATDATE) : null);
        viral.setTestTime(StringUtils.isNotEmpty(item.getSampleTime()) ?  TextUtils.convertDate(item.getSampleTime().trim(), FORMATDATE) : null);
        viral.setCauses(item.getCauses());
        viral.setResultNumber(StringUtils.isNotEmpty(item.getResultNumber()) ? item.getResultNumber().trim() : item.getResultNumber());
        viral.setResult(StringUtils.isNotEmpty(item.getResult()) ?  item.getResult().trim() : item.getResult());
        viral.setNote(StringUtils.isNotEmpty(item.getNote()) ?  item.getNote().trim() : item.getNote());
        return viral;
    }

    private HashMap<String, List<String>> validateCustom(OpcViralBookImportForm item1) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        OpcArvEntity arvs = opcArvService.findByCode(item1.getSiteID(), item1.getCode());
        
        if (StringUtils.isEmpty(item1.getCode()) || item1.getCode().contains("null")) {
            errors.add("Không có mã bệnh án");
            object.put("errors", errors);
            return object;
        }
        
        if (arvs == null) {
            if (StringUtils.isNotEmpty(item1.getCode()) && item1.getCode().contains("duplicated")) {
                errors.add("Mã bệnh án bị trùng");
            } else if (StringUtils.isNotEmpty(item1.getCode())) {
                errors.add("Không tìm thấy bệnh án");
            }
            
            object.put("errors", errors);
            return object;
        } else {
            item1.setArvID(arvs.getID());
            item1.setPatientID(arvs.getPatientID());
        }
        
        if (StringUtils.isNotEmpty(item1.getCode()) && item1.getCode().contains("duplicated")) {
            errors.add("Mã bệnh án bị trùng");
        }
        
        if (StringUtils.isNotEmpty(item1.getSampleTime()) && convertDate(item1.getSampleTime()) == null) {
            errors.add("Ngày lấy mẫu không hợp lệ");
        }
        
        if (StringUtils.isEmpty(item1.getTestTime())) {
            errors.add("Ngày xét nghiệm không được bỏ trống");
        }
        
        if (StringUtils.isNotEmpty(item1.getTestTime()) && convertDate(item1.getTestTime()) == null ) {
            errors.add("Ngày xét nghiệm không hợp lệ");
        }
        
        if (StringUtils.isNotEmpty(item1.getResultTime()) && convertDate(item1.getResultTime()) == null ) {
            errors.add("Ngày nhận kết quả không hợp lệ");
        }
        
        if (item1.getCauses() == null || item1.getCauses().isEmpty()) {
            errors.add("Lý do xét nghiệm không được để trống");
        }
        
        try {
            if (StringUtils.isNotEmpty(item1.getResultTime()) && TextUtils.convertDate(item1.getResultTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(item1.getResultTime(), FORMATDATE)).compareTo(new Date()) > 0) {
                errors.add("Ngày nhận kết quả không được lớn hơn ngày hiện tại");
            }
            if (StringUtils.isNotEmpty(item1.getResultTime()) && StringUtils.isNotEmpty(item1.getTestTime()) && TextUtils.convertDate(item1.getResultTime(), FORMATDATE) != null && getDateWithoutTime(TextUtils.convertDate(item1.getResultTime(), FORMATDATE)).compareTo(getDateWithoutTime(TextUtils.convertDate(item1.getTestTime(), FORMATDATE))) < 0) {
                errors.add("Ngày nhận kết quả không được nhỏ hơn ngày xét nghiệm");
            }
            
        } catch (Exception e) {
        }
        
        object.put("errors", errors);
        return object;
    }

    @Override
    public OpcViralBookImportForm mapping(Map<String, String> cols, List<String> cells) {
        OpcViralBookImportForm item = new OpcViralBookImportForm();
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

    /**
     * Convert sang ngày không có thời gian
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateWithoutTime(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }
    
    /**
     * Set correct field for form
     * 
     * @param o 
     */
    private void correctViral(OpcViralBookImportForm o) {
        
        o.setCauses(new ArrayList<String>());
        o.setTestTime(o.getSampleTime());
        
        // Lý do xét nghiệm
        if (StringUtils.isNotBlank(o.getCauses0())) {
            o.getCauses().add(ViralLoadSymtomEnum.DINHKY6THANG.getKey());
        }
        if (StringUtils.isNotBlank(o.getCauses1())) {
            o.getCauses().add(ViralLoadSymtomEnum.DINHKY12THANG.getKey());
        }
        if (StringUtils.isNotBlank(o.getCauses2())) {
            o.getCauses().add(ViralLoadSymtomEnum.DINHKY3THANG.getKey());
        }
        if (StringUtils.isNotBlank(o.getCauses3())) {
            o.getCauses().add(ViralLoadSymtomEnum.DIEUTRI.getKey());
        }
        if (StringUtils.isNotBlank(o.getCauses4())) {
            o.getCauses().add(ViralLoadSymtomEnum.PREGNANT.getKey());
        }
        // Nồng độ virus và kết quả
        if (StringUtils.isNotBlank(o.getResultNumber0())) {
            o.setResultNumber(o.getResultNumber0().trim());
            o.setResult("6");
        }
        if (StringUtils.isNotBlank(o.getResultNumber1())) {
            o.setResultNumber(o.getResultNumber1().trim());
            o.setResult("2");
        }
        if (StringUtils.isNotBlank(o.getResultNumber2())) {
            o.setResultNumber(o.getResultNumber2().trim());
            o.setResult("3");
        }
        if (StringUtils.isNotBlank(o.getResultNumber3())) {
            o.setResultNumber(o.getResultNumber3().trim());
            o.setResult("4");
        }
        if (StringUtils.isNotBlank(o.getResultNumber4())) {
            o.setResultNumber(o.getResultNumber4().trim());
            long number = 0;
            String rs = o.getResultNumber().toLowerCase();
            if (rs.contains("phát hiện")) {
                o.setResult(o.getResultNumber());
            } else {
                try {
                    if (o.getResultNumber().contains("<")) {
                        number = Long.valueOf(o.getResultNumber().split("<")[1]);
                    } else {
                        number = Long.valueOf(o.getResultNumber());
                    }
                } catch (Exception e) {
                    number = -1;
                }

                if (number < 0) {
                    o.setResult("1");
                }

                if (number < 200) {
                    o.setResult("2");
                }
                if (number > 200 && number < 1000) {
                    o.setResult("3");
                }
            }
        }
        if (StringUtils.isNotBlank(o.getResultNumber5())) {
            o.setResultNumber(o.getResultNumber5().trim());
            o.setResult("4");
        }
    }
    
    /**
     * Chuyển đổi ngày
     * 
     * @param d
     * @return 
     */
    private Date convertDate(String d) {
        
        if (StringUtils.isEmpty(d)) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        try {
            if (d.split("/")[2].length() == 2) {
                String year = d.split("/")[2].length() == 2 ? "20" + d.split("/")[2] : d.split("/")[2];
                cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(Integer.parseInt(year), Integer.parseInt(d.split("/")[1]) - 1, Integer.parseInt(d.split("/")[0]));
                return cal.getTime();
            } else {
                return TextUtils.convertDate(d, FORMATDATE);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
