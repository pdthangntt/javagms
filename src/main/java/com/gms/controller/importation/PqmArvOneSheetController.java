package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmArvSheet2ImportForm;
import com.gms.entity.form.PqmArvSheet4ImportForm;
import com.gms.entity.form.PqmArvOneSheetImportForm;
import com.gms.service.ParameterService;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmLogService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_pqm_arv_1sheet")
public class PqmArvOneSheetController extends BaseController<PqmArvOneSheetImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 2;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PqmArvService arvService;
    @Autowired
    private PqmArvStageService stageService;
    @Autowired
    private PqmLogService logService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-arv/one-sheet/import.html");
        form.setSmallUrl("/backend/pqm-arv/index.html");
        form.setReadUrl("/pqm-arv/one-sheet/import.html");
        form.setTitle("Nhập dữ liệu ARV 1 Sheet");
        form.setSmallTitle("Khách hàng điều trị");
        form.setTemplate(fileTemplate("pqm_arv_one_sheet.xlsx"));
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
        for (SiteEntity site : siteOpc) {
            if (StringUtils.isNotEmpty(site.getHub()) && site.getHub().equals("1")) {
                options.get("siteOpc").put(site.getPqmSiteCode(), String.valueOf(site.getID()));
            }
        }
        return options;
    }

    @GetMapping(value = {"/pqm-arv/one-sheet/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        return "importation/pqm/upload_one_sheet";
    }

    @PostMapping(value = "/pqm-arv/one-sheet/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        try {

            form.setData(readFileFormattedCell(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmArvOneSheetImportForm> data = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() <= (firstRow - 2) || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(0) == null || row.getValue().get(0).equals("")))) {
                    continue;
                }
                PqmArvOneSheetImportForm item = mapping(cols(), row.getValue());
                if (StringUtils.isNotEmpty(item.getCode())) {
                    String siteID = item.getCode().substring(0, 5);
                    System.out.println("xxxx " + siteID);
                    try {
                        item.setSiteID(Long.valueOf(options.get("siteOpc").getOrDefault(siteID, null)));

                    } catch (Exception e) {
                        item.setSiteID(null);
                    }

                    item.setCode(item.getCode().substring(6));
                    item.setCode(item.getCode().trim());

                }

                data.add(item);
            }
            Set<Long> siteIDs = new HashSet<>();
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = firstRow - 1;
            int success = 0;

            for (PqmArvOneSheetImportForm item : data) {
                i += 1;
                HashMap<String, List< String>> object = validateImport(item);
                if (!object.get("errors").isEmpty()) {
                    errors.put(String.valueOf(i), object.get("errors"));
                    continue;
                }
                if (item.getRegistrationType().equals("Điều trị phơi nhiễm")) {
                    continue;
                }
                item.setSiteID(item.getSiteID());

                //Set to entity
                PqmArvEntity entity = getArv(item, item.getSiteID());
                try {
                    //ARV
                    entity = arvService.save(entity);
                    siteIDs.add(entity.getSiteID());

                    //STAGE
                    PqmArvStageEntity stage = getStage(item, entity.getID());
                    stage.setSiteID(entity.getSiteID());
                    stageService.save(stage);
                    success += 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    errors.put(String.valueOf(i), new ArrayList<String>());
                    errors.get(String.valueOf(i)).add(e.getMessage());

                }

            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", data.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

            for (Long siteID : siteIDs) {
                logService.log("Nhập dữ liệu ARV 1 sheet", data.size(), success, data.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", siteID, "Điều trị ARV");
            }

            return "importation/pqm/arv_one_sheet";
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private PqmArvStageEntity getStage(PqmArvOneSheetImportForm item, Long arvID) {

        List<PqmArvStageEntity> stages = stageService.findByArvIDOderByTreatmentTimeDesc(arvID);

        PqmArvStageEntity model = null;
        PqmArvStageEntity modelOld = null;
        if (stages == null) {
            model = new PqmArvStageEntity();
        } else {
            model = stages.get(0);
        }

        model.setArvID(arvID);

        Date registrationTime = TextUtils.convertDate(item.getRegistrationTime(), FORMATDATE);

        model.setRegistrationTime(registrationTime);
        model.setRegistrationType(getRegistrationType(item.getRegistrationType()));

        Date treatmentTime = null;
        if (item.getRegistrationType().equals("BN HIV mới đăng ký lần đầu") || item.getRegistrationType().equals("BN chưa ARV nơi khác chuyển tới") || item.getRegistrationType().equals("Trẻ dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV")) {
            treatmentTime = TextUtils.convertDate(item.getStartTime(), FORMATDATE);
        } else if (item.getRegistrationType().equals("BN ARV nơi khác chuyển tới") || item.getRegistrationType().equals("BN ARV bỏ trị tái điều trị") || item.getRegistrationType().equals("BN chưa ARV đăng ký lại")) {
            treatmentTime = registrationTime;
        }

        model.setTreatmentTime(treatmentTime);
        model.setEndTime(item.getEndTime() == null ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));
        model.setEndCase(item.getEndTime() == null ? null : getEndCase(item.getEndCase()));
        if (model.getTreatmentTime() == null && (StringUtils.isEmpty(model.getEndCase()) || model.getEndCase().equals("3") || model.getEndCase().equals("5"))) {
            model.setStatusOfTreatmentID("2");
        }
        if (model.getTreatmentTime() != null && (StringUtils.isEmpty(model.getEndCase()) || model.getEndCase().equals("3") || model.getEndCase().equals("5"))) {
            model.setStatusOfTreatmentID("3");
        }
        if (model.getEndTime() != null && model.getEndCase().equals("1")) {
            model.setStatusOfTreatmentID("4");
        }
        if (model.getEndTime() != null && model.getEndCase().equals("4")) {
            model.setStatusOfTreatmentID("6");
        }
        if (model.getEndTime() != null && model.getEndCase().equals("2")) {
            model.setStatusOfTreatmentID("7");
        }

        List<PqmArvStageEntity> stageOlds = stageService.findByArvIDOderByTreatmentTimeDesc(arvID);
        if (stageOlds == null) {
            return model;
        } else {
            modelOld = stageOlds.get(0);
            modelOld.setEndTime(model.getEndTime());
            modelOld.setEndCase(model.getEndCase());
            modelOld.setStatusOfTreatmentID(model.getStatusOfTreatmentID());
            return modelOld;
        }
    }

    private String getEndCase(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        switch (param) {
            case "Chuyển đi":
                param = "3";
                break;
            case "Bỏ trị":
                param = "1";
                break;
            case "Tử vong":
                param = "2";
                break;
            case "Mất dấu":
                param = "4";
                break;
            case "Kết thúc điều trị":
                param = "5";
                break;
            default:
                param = "5";
        }

        return param;
    }

    private PqmArvEntity getArv(PqmArvOneSheetImportForm item, Long sites) {

        PqmArvEntity model = arvService.findBySiteIDAndCode(sites, item.getCode().toUpperCase());
        if (model == null) {
            model = new PqmArvEntity();
        }
        model.setPatientPhone(item.getPatientPhone());
        model.setSiteID(sites);
        model.setCode(item.getCode().toUpperCase());
        model.setFullName(item.getFullName() == null ? "" : item.getFullName().toUpperCase());
        model.setYear(Integer.valueOf(item.getDob()));
        model.setGenderID(getGender(item.getGenderID()));
        model.setIdentityCard(item.getIdentityCard());
        model.setInsuranceNo(item.getInsuranceNo());
        model.setPermanentAddress(item.getAddress());
//        model.setCurrentAddress(item.getAddress());
        model.setFirstTreatmentTime(StringUtils.isEmpty(item.getStartTime()) ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setDob(TextUtils.convertDate("01/01/" + item.getDob(), FORMATDATE));
        return model;
    }

    private HashMap<String, List<String>> validateImport(PqmArvOneSheetImportForm form) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        validateNull(form.getCode(), "Mã bệnh án", errors);
        validateNull(form.getDob(), "Năm sinh", errors);
        if (StringUtils.isNotEmpty(form.getDob())) {
            try {
                Integer.valueOf(form.getDob());
                if (form.getDob().length() != 4) {
                    errors.add("Năm sinh không hợp lệ");
                }
            } catch (Exception e) {
                errors.add("Năm sinh không hợp lệ");
            }
        }

        validateNull(getGender(form.getGenderID()), "Giới tính", errors);
        if (form.getSiteID() == null || form.getSiteID().equals(Long.valueOf("0"))) {
            errors.add("Không tìm thấy mã cơ sở tại tỉnh");
        }
//        validateNull(form.getStartTime(), "Ngày bắt đầu điều trị không được để trống", errors);
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            errors.add("Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
        }
        validateNull(form.getRegistrationTime(), "Ngày đăng ký", errors);
        if (StringUtils.isNotBlank(form.getRegistrationTime()) && !isThisDateValid(form.getRegistrationTime())) {
            errors.add("Ngày đăng ký không đúng định dạng dd/mm/yyyy");
        }
        validateNull(getRegistrationType(form.getRegistrationType()), "Loại đăng ký", errors);

        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("Ngày kết thúc không đúng định dạng dd/mm/yyyy");
        }
        if (getCompeDate(form.getRegistrationTime(), form.getEndTime())) {
            errors.add("Ngày kết thúc phải lớn hơn ngày đăng ký");
        }

        object.put("errors", errors);

        return object;
    }

    private String getRegistrationType(String registrationType) {
        if (StringUtils.isEmpty(registrationType)) {
            return "";
        }
        switch (registrationType) {
            case "BN ARV nơi khác chuyển tới":
                return "3";
            case "BN chưa ARV nơi khác chuyển tới":
                return "3";
            case "BN HIV mới đăng ký lần đầu":
                return "1";
            case "BN ARV bỏ trị tái điều trị":
                return "2";
            case "BN chưa ARV đăng ký lại":
                return "2";
            case "Trẻ dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV":
                return "4";
            default:
                return "";
        }

    }

    private List<String> validateArv(PqmArvOneSheetImportForm form) {

        List<String> errors = new ArrayList<>();

        validateNull(form.getCode(), "Mã bệnh án", errors);
        validateNull(form.getDob(), "Ngày sinh", errors);
        validateNull(form.getGenderID(), "Giới tính", errors);
        if (StringUtils.isNotBlank(form.getDob()) && !isThisDateValid(form.getDob())) {
            errors.add("Ngày sinh không đúng định dạng dd/mm/yyyy");
        }
        return errors;
    }

    private String getGender(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }
        switch (param) {
            case "Nam":
                return "1";
            case "Nữ":
                return "2";
            case "Không rõ":
                return "3";
            default:
                return "";
        }
    }

    private void validateNull(String text, String message, List<String> errors) {
        if (text == null || "".equals(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
        }

    }

    private String getEmty(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        if ("null".equals(key)) {
            return "";
        }
        return key;
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

    @Override
    public PqmArvOneSheetImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmArvOneSheetImportForm item = new PqmArvOneSheetImportForm();
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

    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "code");
        cols.put("2", "fullName");
        cols.put("3", "dob");
        cols.put("4", "genderID");
        cols.put("5", "address");
        cols.put("6", "identityCard");
        cols.put("7", "");
        cols.put("8", "");
        cols.put("9", "");
        cols.put("10", "");
        cols.put("11", "");
        cols.put("12", "");
        cols.put("13", "");
        cols.put("14", "startTime");
        cols.put("15", "registrationTime");
        cols.put("16", "registrationType");
        cols.put("17", "endTime");
        cols.put("18", "endCase");
        cols.put("19", "");
        cols.put("20", "insuranceNo");
        cols.put("21", "");
        cols.put("22", "");
        cols.put("23", "");

        return cols;
    }

    private boolean getCompeDate(String date1, String date2) {
        if (!isThisDateValid(date1) || !isThisDateValid(date1)) {
            return false;
        }
        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            if (StringUtils.isNotEmpty(date1) && StringUtils.isNotEmpty(date1)) {
                Date b = sdfrmt.parse(date1);
                Date a = sdfrmt.parse(date2);
                if (b.compareTo(a) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
