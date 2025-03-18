package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.OpcVisitImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcVisitService;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
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

@Controller(value = "importation_opc_visit_tg")
public class OpcVisitTgController extends BaseController<OpcVisitImportForm> {
    
    private static int firstRow = 7;
    private static String textTitle = "";
    
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    
    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
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
        form.setUploadUrl("/import-opc-visit-tg/index.html");
        form.setSmallUrl("/import-opc-visit-tg/index.html");
        form.setReadUrl("/import-opc-visit-tg/index.html");
        form.setTitle("Thêm lượt khám sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("sokham.xlsx"));
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
        cols.put("2", "");
        cols.put("3", "");
        cols.put("4", "name");
        cols.put("5", "male");
        cols.put("6", "female");
        cols.put("7", "");
        cols.put("8", "insuranceNo");
        cols.put("9", "");
        cols.put("10", "");
        cols.put("11", "diagnostic");
        cols.put("12", "point");
        
        return cols;
    }
    
    @GetMapping(value = {"/import-opc-visit-tg/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }
    
    @PostMapping(value = "/import-opc-visit-tg/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        int index = 0;
        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            
            List<OpcVisitImportForm> items = new LinkedList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (index == 2) {
                    textTitle = row.getValue().get(1);
                }
                if (index < firstRow) {
                    continue;
                }
                if (StringUtils.isEmpty(row.getValue().get(1)) && StringUtils.isEmpty(row.getValue().get(4))) {
                    break;
                }
                
                row.getValue();
                OpcVisitImportForm item = mapping(cols(), row.getValue());
                item.setStt(index);
                if(StringUtils.isNotEmpty(row.getValue().get(11)) && row.getValue().get(11).contains("Z20")){
                    continue;
                }
                item.setName(StringUtils.normalizeSpace(item.getName().trim().toLowerCase()));
                items.add(item);
            }
            
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            
            int success = 0;
            //tạm
            //check ngày
            List<String> dates = getDate();
            if (!dates.get(0).equals(dates.get(1)) || !isThisDateValid(dates.get(0))) {
                model.addAttribute("error", "Tệp dữ liệu có ngày khám không hợp lệ, yêu cầu kiểm tra lại");
                model.addAttribute("errorsw", errors);
                model.addAttribute("total", items.size());
                model.addAttribute("successx", success);
                model.addAttribute("form", form);
                
                return "importation/opc-arv/opc_visit_tg";
            }
            
            OpcVisitEntity e;
            for (OpcVisitImportForm item1 : items) {
                HashMap<String, List< String>> object = validateCustom(item1);
                opcVisitService.findByArvID(Long.MAX_VALUE, true);
                if (object.get("errors").isEmpty()) {
                    //Set entity
                    if (StringUtils.isBlank(item1.getInsuranceNo())) {
                        item1.setInsuranceNo("");
                    }
                    e = toEntity(item1);
                    e.setImportable(true);
                    //thông tin cơ bản 
                    boolean b = e.getID() == null;
                    
                    opcVisitService.save(e);
                    opcVisitService.logVisit(e.getArvID(), e.getPatientID(), e.getID(), b ? "Thêm lượt khám sử dụng import excel" : "Cập nhật lượt khám sử dụng import excel");
                    success += 1;
                    continue;
                }
                
                errors.put(String.valueOf(item1.getStt()), object.get("errors"));
                
            }
            
            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);
            
            return "importation/opc-arv/opc_visit_tg";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }
    
    private OpcVisitEntity toEntity(OpcVisitImportForm item1) {
        LoggedUser currentUser = getCurrentUser();
        List<String> dates = getDate();

        //Xu ly so ngay nhan thuoc
        System.out.println(item1.getName() + " : " + getSoNgayNhanThuoc(item1.getPoint()));
        int soNgayNhanThuoc = getSoNgayNhanThuoc(item1.getPoint());
        String examinationTime = TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", dates.get(0));
        int yearTest = Integer.valueOf(TextUtils.formatDate(TextUtils.convertDate(dates.get(0), FORMATDATE), "yyyy"));
        int month = 0;
        try {
            if(StringUtils.isNotEmpty(item1.getMale()) && item1.getMale().toLowerCase().contains("tháng")){
                month = Integer.parseInt(item1.getMale().toLowerCase().replace("tháng", "").trim());
                item1.setMale(Integer.parseInt(item1.getMale().toLowerCase().replace("tháng", "").trim()) + "");
            }
        } catch (NumberFormatException e) {}
        try {
            if(StringUtils.isNotEmpty(item1.getFemale()) && item1.getFemale().toLowerCase().contains("tháng")){
                month = Integer.parseInt(item1.getFemale().toLowerCase().replace("tháng", "").trim());
                item1.setFemale(Integer.parseInt(item1.getFemale().toLowerCase().replace("tháng", "").trim()) + "");
            }
        } catch (NumberFormatException e) {}
        int age = 0;
        try {
            age = StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? Integer.parseInt(item1.getMale()) : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? Integer.parseInt(item1.getFemale()) : 0;
        } catch (NumberFormatException e) {
            age = 0;
        }
        List<OpcArvEntity> arvs;
        if(month != 0){
            arvs = opcArvService.findArvImportExcelVisit(item1.getName(), month, examinationTime, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
        } else {
            arvs = opcArvService.findArvImportExcelViral(item1.getName(), yearTest - age, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
        }
        List<OpcStageEntity> stages = opcStageService.findByArvIDExcelVisit(arvs.get(0).getID());
        List<OpcVisitEntity> visits = opcVisitService.findByExaminationTimeAndStageIDImportVisit(TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", dates.get(0)), stages.get(0).getID());
        OpcVisitEntity e = new OpcVisitEntity();
        
        if (visits != null) {
            e.setID(visits.get(0).getID());
        }
        e.setArvID(arvs.get(0).getID());
        e.setSiteID(arvs.get(0).getSiteID());
        e.setPatientID(arvs.get(0).getPatientID());
        e.setSiteID(arvs.get(0).getSiteID());
        e.setStageID(stages.get(0).getID());
        //set từ form import
        e.setInsurance("1");
        if (StringUtils.isNotBlank(item1.getInsuranceNo())) {
            e.setInsuranceNo(item1.getInsuranceNo().toUpperCase());
//        loai bao hiem
            for (Map.Entry<String, String> entry : getOptions().get(ParameterEntity.INSURANCE_TYPE).entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.substring(0, 2).equals(item1.getInsuranceNo().substring(0, 2))) {
                    e.setInsuranceType(key);
                }
            }
            if (item1.getInsuranceNo().substring(2, 3).equals("1") || item1.getInsuranceNo().substring(2, 3).equals("2") || item1.getInsuranceNo().substring(2, 3).equals("5")) {
                e.setInsurancePay("1");
            } else if (item1.getInsuranceNo().substring(2, 3).equals("3")) {
                e.setInsurancePay("2");
            } else if (item1.getInsuranceNo().substring(2, 3).equals("4")) {
                e.setInsurancePay("3");
            } else {
                e.setInsurancePay("");
            }
        }
        e.setInsuranceSite("");
        e.setInsuranceExpiry(null);
        e.setRouteID("");
        e.setExaminationTime(TextUtils.convertDate(dates.get(0), "dd/MM/yyyy"));
        item1.setDiagnostic(item1.getDiagnostic() == null ? "" : item1.getDiagnostic());
        item1.setPoint(item1.getPoint() == null ? "" : item1.getPoint().replaceAll("\\*", ""));
        String diagnostic = item1.getDiagnostic() + (StringUtils.isNotBlank(item1.getPoint()) && StringUtils.isNotBlank(item1.getDiagnostic()) ? "." : "") + item1.getPoint();
        diagnostic = StringUtils.normalizeSpace(diagnostic.trim());
        e.setDiagnostic(diagnostic);
        
        e.setTreatmentRegimenStage(arvs.get(0).getTreatmentRegimenStage());
        e.setTreatmentRegimenID(arvs.get(0).getTreatmentRegimenID());
        
        if (soNgayNhanThuoc > 0) {
            //thêm ngày
            Calendar c = Calendar.getInstance();
            c.setTime(e.getExaminationTime());
            c.add(Calendar.DATE, soNgayNhanThuoc);//Đếm ngày vô đây
            Date appointmentTime = c.getTime();
            e.setAppointmentTime(appointmentTime);
            e.setDaysReceived(soNgayNhanThuoc);
        }
//        e.setObjectGroupID(objectID);
//        e.setCircuit(StringUtils.isNotEmpty(getCircuit()) ? getCircuit().trim() : "");
//        e.setBloodPressure(StringUtils.isNotEmpty(getBloodPressure()) ? getBloodPressure().trim() : "");
//        e.setBodyTemperature(StringUtils.isNotEmpty(getBodyTemperature()) ? getBodyTemperature().trim() : "");
//        e.setClinical(getClinical());
//        e.setMedicationAdherence(getMedicationAdherence());
//        e.setMsg(getMsg());
//        e.setNote(getNote());
//        if (getPatientWeight() != null) {
//            e.setPatientWeight("".equals(getPatientWeight()) ? 0.0f : Float.parseFloat(getPatientWeight()));
//        }
//        if (getPatientHeight() != null) {
//            e.setPatientHeight("".equals(getPatientHeight()) ? 0.0f : Float.parseFloat(getPatientHeight()));
//        }
//
//        e.setRegimenStageDate(TextUtils.convertDate(getRegimenStageDate(), "dd/MM/yyyy"));
//        e.setRegimenDate(TextUtils.convertDate(getRegimenDate(), "dd/MM/yyyy"));
//        e.setOldTreatmentRegimenID(oldTreatmentRegimenID);
//        e.setOldTreatmentRegimenStage(oldTreatmentRegimenStage);
//        e.setCausesChange(causesChange);
//        e.setSupplyMedicinceDate(TextUtils.convertDate(getSupplyMedicinceDate(), "dd/MM/yyyy"));
//        e.setReceivedWardDate(TextUtils.convertDate(getReceivedWardDate(), "dd/MM/yyyy"));
//        e.setPregnantStartDate(TextUtils.convertDate(getPregnantStartDate(), "dd/MM/yyyy"));
//        e.setPregnantEndDate(TextUtils.convertDate(getPregnantEndDate(), "dd/MM/yyyy"));
//        e.setConsult("1".equals(consult));
        return e;
    }
    
    private int getSoNgayNhanThuoc(String point) {
        int soNgayNhanThuoc = 0;
        if (point != null) {
            int index = point.indexOf("Ngày ");
            if (index > 0) {
                String result = StringUtils.isEmpty(point.substring(index + 5, index + 9)) ? "" : point.substring(index + 5, index + 9).replaceAll("\\D+", "");
                try {
                    soNgayNhanThuoc = Integer.parseInt(result);
                } catch (Exception e) {
                    soNgayNhanThuoc = 0;
                }
                
            }
            
        }
        return soNgayNhanThuoc;
    }
    
    private HashMap<String, List<String>> validateCustom(OpcVisitImportForm item1) {
        
        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> dates = getDate();
        String examinationTime = TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", dates.get(0));
        int yearTest = Integer.valueOf(TextUtils.formatDate(TextUtils.convertDate(dates.get(0), FORMATDATE), "yyyy"));
        int month = 0;
        try {
            if(StringUtils.isNotEmpty(item1.getMale()) && item1.getMale().toLowerCase().contains("tháng")){
                month = Integer.parseInt(item1.getMale().toLowerCase().replace("tháng", "").trim());
            }
        } catch (NumberFormatException e) {}
        try {
            if(StringUtils.isNotEmpty(item1.getFemale()) && item1.getFemale().toLowerCase().contains("tháng")){
                month = Integer.parseInt(item1.getFemale().toLowerCase().replace("tháng", "").trim());
            }
        } catch (NumberFormatException e) {}
        int age = 0;
        try {
            age = StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? Integer.parseInt(item1.getMale().toLowerCase().replace("tháng", "").trim()) : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? Integer.parseInt(item1.getFemale().toLowerCase().replace("tháng", "").trim()) : 0;
        } catch (Exception e) {
            errors.add("Tuổi không hợp lệ");
        }
        validateNull(item1.getName(), "Họ và tên ", errors);
//        if (StringUtils.isBlank(item1.getInsuranceNo())) {
//            errors.add("Không có số thẻ BHYT, kiểm tra lại thông tin ");
//        }
        List<OpcArvEntity> arvs;
        if(month == 0){
            arvs = opcArvService.findArvImportExcelViral(item1.getName(),yearTest - age, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
        } else {
            arvs = opcArvService.findArvImportExcelVisit(item1.getName(), month, examinationTime, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
        }
        if (arvs == null) {
            errors.add("Không tìm thấy bệnh án");
            object.put("errors", errors);
            return object;
        }
        if (arvs.size() > 1) {
            String benhAnTrung = "";
            for (OpcArvEntity arv : arvs) {
                benhAnTrung = benhAnTrung + "#" + arv.getCode() + "; ";
            }
            errors.add("Tồn tại " + arvs.size() + " bệnh án trùng thông tin (" + benhAnTrung + " ), kiểm tra lại thông tin");
            object.put("errors", errors);
            return object;
        }
        List<OpcStageEntity> stages = opcStageService.findByArvIDExcelVisit(arvs.get(0).getID());
        if (stages == null) {
            errors.add("Không có giai đoạn điều trị");
            object.put("errors", errors);
            return object;
        }
        
        Date ngayKham = TextUtils.convertDate(dates.get(0), "dd/MM/yyyy");
        Date ngayARV = stages.get(0).getTreatmentTime() == null ? TextUtils.convertDate(TextUtils.formatDate(stages.get(0).getRegistrationTime(), FORMATDATE), FORMATDATE) : TextUtils.convertDate(TextUtils.formatDate(stages.get(0).getTreatmentTime(), FORMATDATE), FORMATDATE);
        if (ngayARV != null && ngayARV.compareTo(ngayKham) > 0) {
            errors.add(stages.get(0).getTreatmentTime() == null ? "Ngày khám không được nhỏ hơn ngày đăng ký" : "Ngày khám không được nhỏ hơn ngày điều trị ARV");
        }
        if (stages.get(0).getEndTime() != null && stages.get(0).getEndTime().compareTo(ngayKham) < 0) {
            errors.add("Ngày khám không được lớn hơn ngày kết thúc");
        }
        
        object.put("errors", errors);
        
        return object;
    }
    
    private List<String> getDate() {
        List<String> dates = new ArrayList<>();
        textTitle = StringUtils.isEmpty(textTitle) ? "" : textTitle.replaceAll("\\D+", "");
        String day1 = "1";
        String day2 = "2";
        if (StringUtils.isNotEmpty(textTitle)) {
            day1 = textTitle.substring(0, 8);
            day2 = textTitle.substring(8, 16);
            day1 = day1.substring(0, 2) + "/" + day1.substring(2, 4) + "/" + day1.substring(4, 8);
            day2 = day2.substring(0, 2) + "/" + day2.substring(2, 4) + "/" + day2.substring(4, 8);
            
            dates.add(day1);
            dates.add(day2);
        }
        return dates;
    }
    
    @Override
    public OpcVisitImportForm mapping(Map<String, String> cols, List<String> cells
    ) {
        OpcVisitImportForm item = new OpcVisitImportForm();
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
        if (StringUtils.isBlank(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
        }
        
    }
    
    private String errorMsg(String attribute, String msg) {
        return String.format("<span> %s </span> <i class=\"text-danger\"> %s </i>", attribute, msg);
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
}
