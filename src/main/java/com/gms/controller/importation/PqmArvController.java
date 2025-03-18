package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvTestEntity;
import com.gms.entity.db.PqmArvViralLoadEntity;
import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmArvSheet2ImportForm;
import com.gms.entity.form.PqmArvImportForm;
import com.gms.entity.form.PqmArvSheet3ImportForm;
import com.gms.entity.form.PqmArvSheet4ImportForm;
import com.gms.entity.form.PqmArvSheet5ImportForm;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmArvTestService;
import com.gms.service.PqmArvViralService;
import com.gms.service.PqmArvVisitService;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_pqm_arv")
public class PqmArvController extends BaseController<PqmArvImportForm> {

    private static HashMap<String, HashMap<String, String>> options;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PqmArvService arvService;
    @Autowired
    private PqmArvStageService stageService;
    @Autowired
    private PqmArvVisitService visitService;
    @Autowired
    private PqmArvViralService viralService;
    @Autowired
    private PqmArvTestService testService;
//    @Autowired
//    private PqmLogService logService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-arv/import1.html");
        form.setSmallUrl("/backend/pqm-arv/index.html");
        form.setReadUrl("/pqm-arv/import1.html");
        form.setTitle("Tải file excel của điều trị ngoại trú ARV");
        form.setSmallTitle("Khách hàng điều trị");
        form.setTemplate(fileTemplate("arv_template.xlsx"));
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
        List<SiteEntity> sitePrEP = siteService.getSitePrEP(getCurrentUser().getSite().getProvinceID());
        options.put("sitePrEP", new HashMap<String, String>());
        options.get("sitePrEP").put("", "Chọn cơ sở");
        for (SiteEntity site : sitePrEP) {
            if (StringUtils.isNotEmpty(site.getHub()) && site.getHub().equals("1")) {
                options.get("sitePrEP").put(String.valueOf(site.getID()), site.getName());
            }
        }
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtc", new HashMap<String, String>());
        options.get("siteHtc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteHtc) {
            options.get("siteHtc").put(String.valueOf(site.getID()), site.getName());
        }

        return options;
    }

    @GetMapping(value = {"/pqm-arv/import1.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        return "importation/pqm/upload";
    }

    @PostMapping(value = "/pqm-arv/import1.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        System.out.println("xxx " + sites);
        if (StringUtils.isEmpty(sites)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
            return redirect(form.getUploadUrl());
        }
        try {
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());

            form.setData(readFile(workbook, 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            //List arv cũ
            Set<String> arvCodes = new HashSet<>();

            List<PqmArvImportForm> data = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 5 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(2) == null || row.getValue().get(2).equals("")))) {
                    continue;
                }
                PqmArvImportForm item = mapping(colsSheet1(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                arvCodes.add(item.getCode());
                data.add(item);
            }

            //DU LIEU SHEET 2
            Map<Integer, List<String>> dataSheet2 = readFile(workbook, 1, model);
            List<PqmArvSheet2ImportForm> dataSheet2s = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> row : dataSheet2.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet2ImportForm item = mappingSheet2(colsSheet2(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                dataSheet2s.add(item);

            }
            //DU LIEU SHEET 3
            Map<Integer, List<String>> dataSheet3 = readFile(workbook, 2, model);
            List<PqmArvSheet3ImportForm> dataSheet3s = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> row : dataSheet3.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet3ImportForm item = mappingSheet3(colsSheet3(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                dataSheet3s.add(item);

            }
            //DU LIEU SHEET 4
            Map<Integer, List<String>> dataSheet4 = readFile(workbook, 3, model);
            List<PqmArvSheet4ImportForm> dataSheet4s = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> row : dataSheet4.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet4ImportForm item = mappingSheet4(colsSheet4(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                dataSheet4s.add(item);

            }
            //DU LIEU SHEET 5
            Map<Integer, List<String>> dataSheet5 = readFile(workbook, 4, model);
            List<PqmArvSheet5ImportForm> dataSheet5s = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> row : dataSheet5.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet5ImportForm item = mappingSheet5(colsSheet5(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                dataSheet5s.add(item);

            }

            //List dữ liệu Mới
//            List<PqmArvEntity> arvs = new ArrayList<>();
//            List<PqmArvStageEntity> stages = new ArrayList<>();
//            List<PqmArvVisitEntity> visits = new ArrayList<>();
//            List<PqmArvViralLoadEntity> virals = new ArrayList<>();
//            List<PqmArvTestEntity> tests = new ArrayList<>();
            int st = 0;
            int vs = 0;
            int vr = 0;
            int te = 0;

            //check lỗi ARV
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            //Map để gắn ID vào các stage, visit,....
            Map<String, Long> mapIDs = new HashMap<>();
            Map<Long, String> mapCodes = new HashMap<>();
            //Lấy các arv code để find các stage, visit,... cũ
            Set<Long> arvIDs = new HashSet<>();
            //arv
            int success = 0;
            PqmArvEntity itemArv;
            for (PqmArvImportForm arv : data) {
                List< String> error = validateArv(arv);
                if (error.isEmpty()) {
                    try {
                        itemArv = getArv(arv, sites);
                        itemArv.setSiteID(Long.valueOf(sites));
                        arvService.save(itemArv);
                        mapIDs.put(itemArv.getCode(), itemArv.getID());
                        mapCodes.put(itemArv.getID(), itemArv.getCode());
                        arvIDs.add(itemArv.getID());
                        success += 1;
                    } catch (Exception e) {
                        errors.get(String.valueOf(arv.getRow())).add(e.getMessage());
                    }
                } else {
                    errors.put(String.valueOf(arv.getRow()), error);
                }

            }
            //Tổng hợp các bản ghi cũ
            List<PqmArvStageEntity> oldStages = stageService.findByArvIDs(arvIDs);
            List<PqmArvVisitEntity> oldVisits = visitService.findByArvIDs(arvIDs);
            List<PqmArvViralLoadEntity> oldVirals = viralService.findByArvIDs(arvIDs);
            List<PqmArvTestEntity> oldTests = testService.findByArvIDs(arvIDs);
            //Map dữ liệu cũ
            Map<String, PqmArvStageEntity> stageOldMaps = new HashMap<>();
            Map<String, PqmArvVisitEntity> visitOldMaps = new HashMap<>();
            Map<String, PqmArvViralLoadEntity> viralOldMaps = new HashMap<>();
            Map<String, PqmArvTestEntity> testOldMaps = new HashMap<>();
            if (oldStages != null) {
                for (PqmArvStageEntity oldStage : oldStages) {
                    String code = mapCodes.getOrDefault(oldStage.getArvID(), null);
                    if (code == null) {
                        continue;
                    }
                    stageOldMaps.put(code + TextUtils.formatDate(oldStage.getTreatmentTime(), FORMATDATE), oldStage);
                }
            }
            if (oldVisits != null) {
                for (PqmArvVisitEntity oldVisit : oldVisits) {
                    String code = mapCodes.getOrDefault(oldVisit.getArvID(), null);
                    if (code == null) {
                        continue;
                    }
                    visitOldMaps.put(code + TextUtils.formatDate(oldVisit.getExaminationTime(), FORMATDATE), oldVisit);
                }
            }
            if (oldVirals != null) {
                for (PqmArvViralLoadEntity oldViral : oldVirals) {
                    String code = mapCodes.getOrDefault(oldViral.getArvID(), null);
                    if (code == null) {
                        continue;
                    }
                    viralOldMaps.put(code + TextUtils.formatDate(oldViral.getTestTime(), FORMATDATE), oldViral);
                }
            }
            if (oldTests != null) {
                for (PqmArvTestEntity oldTest : oldTests) {
                    String code = mapCodes.getOrDefault(oldTest.getArvID(), null);
                    if (code == null) {
                        continue;
                    }
                    testOldMaps.put(code + TextUtils.formatDate(oldTest.getInhFromTime(), FORMATDATE), oldTest);
                }
            }

            //stage
            //map ngày đki arv, lý do theo sheet 2 gán vào arv code
            Map<String, PqmArvSheet2ImportForm> sheet2Map = new HashMap<>();
            for (PqmArvSheet2ImportForm sheet2 : dataSheet2s) {
                sheet2Map.put(sheet2.getArvCode(), sheet2);
            }
            //Du lieu sheet 4
            PqmArvStageEntity stage;
            PqmArvTestEntity test;
            HashMap<String, List< String>> stageErrors = new LinkedHashMap<>();
            HashMap<String, List< String>> testErrors = new LinkedHashMap<>();
            for (PqmArvSheet4ImportForm row : dataSheet4s) {
                if (row.getType() != null && row.getType().equals("Điều trị ARV")) {
                    //validate
                    List< String> error = validateStage(row, sheet2Map);
                    if (error.isEmpty()) {
                        stage = getStage(row, sheet2Map, stageOldMaps);
                        stage.setArvID(mapIDs.get(row.getArvCode()));
                        stage.setSiteID(Long.valueOf(sites));
                        try {
                            //save
                            stageService.save(stage);
                            st += 1;
                        } catch (Exception e) {
                            stageErrors.put(String.valueOf(row.getRow()), error);
                            stageErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                        }
                    } else {
                        stageErrors.put(String.valueOf(row.getRow()), error);

                    }
                }
                if (row.getType() != null && row.getType().contains("phòng")) {
                    //validate
                    List< String> error = validateTest(row);
                    if (error.isEmpty()) {
                        test = getTest(row, testOldMaps);
                        test.setArvID(mapIDs.get(row.getArvCode()));
                        try {
                            //save
                            testService.save(test);
                            te += 1;
                        } catch (Exception e) {
                            testErrors.put(String.valueOf(row.getRow()), error);
                            testErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                        }
                    } else {
                        testErrors.put(String.valueOf(row.getRow()), error);

                    }
                }

            }
            //Sheet 3
            HashMap<String, List< String>> visitErrors = new LinkedHashMap<>();
            PqmArvVisitEntity visit;
            for (PqmArvSheet3ImportForm row : dataSheet3s) {
                List< String> error = validateVisit(row);
                if (error.isEmpty()) {
                    visit = getVisit(row, visitOldMaps);
                    visit.setArvID(mapIDs.get(row.getArvCode()));
                    try {
                        //save
                        visitService.save(visit);
                        vs += 1;
                    } catch (Exception e) {
                        visitErrors.put(String.valueOf(row.getRow()), error);
                        visitErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                    }
                } else {
                    visitErrors.put(String.valueOf(row.getRow()), error);

                }
            }
            //Sheet 5
            HashMap<String, List< String>> viralErrors = new LinkedHashMap<>();
            PqmArvViralLoadEntity viral;
            for (PqmArvSheet5ImportForm row : dataSheet5s) {
                List< String> error = validateViral(row);
                if (error.isEmpty()) {
                    viral = getViral(row, viralOldMaps);
                    viral.setArvID(mapIDs.get(row.getArvCode()));
                    try {
                        //save
                        viralService.save(viral);
                        vr += 1;
                    } catch (Exception e) {
                        viralErrors.put(String.valueOf(row.getRow()), error);
                        viralErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                    }
                } else {
                    viralErrors.put(String.valueOf(row.getRow()), error);

                }
            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("stageErrors", stageErrors);
            model.addAttribute("testErrors", testErrors);
            model.addAttribute("visitErrors", visitErrors);
            model.addAttribute("viralErrors", viralErrors);
            model.addAttribute("st", st);
            model.addAttribute("vs", vs);
            model.addAttribute("te", te);
            model.addAttribute("vr", vr);
            model.addAttribute("site", options.get("siteOpc").get(sites));
            model.addAttribute("total", data.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

//            logService.log("Tải file excel của điều trị ngoại trú ARV", data.size(), success, data.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", Long.valueOf(sites), "Điều trị ARV");

            return "importation/pqm/arv";
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private List<String> validateViral(PqmArvSheet5ImportForm form) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getTestTime(), "SHEET5: Ngày XN không được để trống", errors);
        if (StringUtils.isNotBlank(form.getTestTime()) && !isThisDateValid(form.getTestTime())) {
            errors.add("SHEET5: Ngày XN không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getResultTime()) && !isThisDateValid(form.getResultTime())) {
            errors.add("SHEET5: Ngày nhận kết quả không đúng định dạng dd/mm/yyyy");
        }
        try {
            if (StringUtils.isNotEmpty(form.getResultNumber()) && !form.getResultNumber().equals("KPH")) {
                int a = Integer.valueOf(form.getResultNumber());
                if (a < 0) {
                    errors.add("SHEET5: Kết quả XN chỉ được nhập nguyên dương");
                }
            }

        } catch (Exception e) {
            errors.add("SHEET5: Kết quả XN chỉ được nhập nguyên dương");
        }

        return errors;
    }

    private List<String> validateVisit(PqmArvSheet3ImportForm form) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getExaminationTime(), "SHEET3: Ngày khám không được để trống", errors);
        if (StringUtils.isNotBlank(form.getExaminationTime()) && !isThisDateValid(form.getExaminationTime())) {
            errors.add("SHEET3: Ngày khám không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getAppointmentTime()) && !isThisDateValid(form.getAppointmentTime())) {
            errors.add("SHEET3: Ngày hẹn khám không đúng định dạng dd/mm/yyyy");
        }
        try {
            if (StringUtils.isNotEmpty(form.getDaysReceived())) {
                int a = Integer.valueOf(form.getDaysReceived());
                if (a < 0) {
                    errors.add("SHEET3: Số ngày nhận thuốc chỉ được nhập nguyên dương");
                }
            }
        } catch (Exception e) {
            errors.add("SHEET3: Số ngày nhận thuốc chỉ được nhập nguyên dương");
        }

        return errors;
    }

    private List<String> validateStage(PqmArvSheet4ImportForm form, Map<String, PqmArvSheet2ImportForm> sheet2Map) {

        PqmArvSheet2ImportForm importForm = sheet2Map.getOrDefault(form.getArvCode(), null);
        String registrationTime = importForm == null || StringUtils.isEmpty(importForm.getRegistrationTime()) ? null : importForm.getRegistrationTime();
        String registrationType = importForm == null || StringUtils.isEmpty(importForm.getRegistrationType()) ? null : importForm.getRegistrationType();
        String row = importForm == null || StringUtils.isEmpty(importForm.getRow()) ? null : importForm.getRow();

        List<String> errors = new ArrayList<>();

        validateNull(registrationTime, "SHEET2: Ngày đăng ký không được để trống", errors);
        if (StringUtils.isNotBlank(registrationTime) && !isThisDateValid(registrationTime)) {
            errors.add("SHEET2-Dòng: " + row + " Ngày đăng ký không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            errors.add("SHEET4: Ngày điều trị không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("SHEET4: Ngày kết thúc không đúng định dạng dd/mm/yyyy");
        }
        return errors;
    }

    private List<String> validateTest(PqmArvSheet4ImportForm form) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getStartTime(), "SHEET4: Ngày bắt đầu không được để trống", errors);
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            errors.add("SHEET4: Ngày bắt đầu không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("SHEET4: Ngày kết thúc không đúng định dạng dd/mm/yyyy");
        }
        return errors;
    }

    protected String buildAddress(String address, String province, String district, String ward) {
        address = address == null ? "" : address;
        if (StringUtils.isNotEmpty(ward)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + ward;
        }
        if (StringUtils.isNotEmpty(district)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + district;
        }
        if (StringUtils.isNotEmpty(province)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + province;
        }

        return address;
    }

    private PqmArvViralLoadEntity getViral(PqmArvSheet5ImportForm item, Map<String, PqmArvViralLoadEntity> mapCurrentDatas) {

        PqmArvViralLoadEntity model = mapCurrentDatas.getOrDefault(item.getArvCode() + item.getTestTime(), null);
        if (model == null) {
            model = new PqmArvViralLoadEntity();
        }

        model.setTestTime(item.getTestTime() == null ? null : TextUtils.convertDate(item.getTestTime(), FORMATDATE));
        model.setResultTime(item.getResultTime() == null ? null : TextUtils.convertDate(item.getResultTime(), FORMATDATE));
        model.setResultNumber(item.getResultNumber());
        model.setResult(getResultViral(item.getResultNumber()));
        //thieu Set ly do keets thuc

        return model;

    }

    private String getResultViral(String result) {

        if (StringUtils.isEmpty(result)) {
            return "";
        }
        if (result.equals("KPH")) {
            return "1";
        }
        try {
            int a = Integer.valueOf(result);
            if (a > 0 && a < 200) {
                return "2";
            } else if (a >= 200 && a <= 1000) {
                return "3";
            } else if (a > 1000) {
                return "4";
            } else {
                return "";
            }

        } catch (Exception e) {
        }
        return "";
    }

    private PqmArvVisitEntity getVisit(PqmArvSheet3ImportForm item, Map<String, PqmArvVisitEntity> mapCurrentDatas) {

        PqmArvVisitEntity model = mapCurrentDatas.getOrDefault(item.getArvCode() + item.getExaminationTime(), null);
        if (model == null) {
            model = new PqmArvVisitEntity();
        }

        model.setExaminationTime(item.getExaminationTime() == null ? null : TextUtils.convertDate(item.getExaminationTime(), FORMATDATE));
        model.setAppointmentTime(item.getAppointmentTime() == null ? null : TextUtils.convertDate(item.getAppointmentTime(), FORMATDATE));
        model.setMutipleMonth(item.getMutipleMonth());
        try {
            model.setDaysReceived(Integer.valueOf(item.getDaysReceived()));
        } catch (Exception e) {
            model.setDaysReceived(Integer.valueOf("0"));
        }

        //thieu Set ly do keets thuc
        return model;

    }

    private PqmArvStageEntity getStage(PqmArvSheet4ImportForm item, Map<String, PqmArvSheet2ImportForm> sheet2Map, Map<String, PqmArvStageEntity> mapCurrentDatas) {

        PqmArvStageEntity model = mapCurrentDatas.getOrDefault(item.getArvCode() + item.getStartTime(), null);
        if (model == null) {
            model = new PqmArvStageEntity();
        }
        PqmArvSheet2ImportForm importForm = sheet2Map.getOrDefault(item.getArvCode(), null);
        Date registrationTime = importForm == null || StringUtils.isEmpty(importForm.getRegistrationTime()) ? null : TextUtils.convertDate(importForm.getRegistrationTime(), FORMATDATE);
        String registrationType = importForm == null || StringUtils.isEmpty(importForm.getRegistrationType()) ? null : importForm.getRegistrationType();

        model.setRegistrationTime(registrationTime);
        model.setRegistrationType(getRegistrationType(registrationType));
        model.setTreatmentTime(item.getStartTime() == null ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setStatusOfTreatmentID(model.getTreatmentTime() == null ? "2" : "3");
        model.setEndTime(item.getEndTime() == null ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));
        model.setEndCase(item.getEndTime() == null ? "" : "5");
        //thieu Set ngay chuyen di nua

        return model;

    }

    private PqmArvTestEntity getTest(PqmArvSheet4ImportForm item, Map<String, PqmArvTestEntity> mapCurrentDatas) {

        PqmArvTestEntity model = mapCurrentDatas.getOrDefault(item.getArvCode() + item.getStartTime(), null);
        if (model == null) {
            model = new PqmArvTestEntity();
        }

        model.setInhFromTime(item.getStartTime() == null ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setInhToTime(item.getEndTime() == null ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));
        //thieu Set ly do keets thuc

        return model;

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
            case "Điều trị phơi nhiễm":
                return "5";
            default:
                return "";
        }

    }

    private List<String> validateArv(PqmArvImportForm form) {

        List<String> errors = new ArrayList<>();

        validateNull(form.getCode(), "Mã bệnh án", errors);
        validateNull(form.getDob(), "Ngày sinh", errors);
        validateNull(form.getGenderID(), "Giới tính", errors);
        if (StringUtils.isNotBlank(form.getDob()) && !isThisDateValid(form.getDob())) {
            errors.add("Ngày sinh không đúng định dạng dd/mm/yyyy");
        }
        return errors;
    }

    private PqmArvEntity getArv(PqmArvImportForm item, String sites) {

        PqmArvEntity model = arvService.findBySiteIDAndCode(Long.valueOf(sites), item.getCode().toUpperCase());
        if (model == null) {
            model = new PqmArvEntity();
        }
        model.setPatientPhone(item.getPatientPhone());
//        model.setSiteID(currentUser.getSite().getID());
        model.setCode(item.getCode().toUpperCase());
        model.setFullName(item.getFullName() == null ? "" : item.getFullName().toUpperCase());
        model.setDob(StringUtils.isEmpty(item.getDob()) ? null : TextUtils.convertDate(item.getDob(), FORMATDATE));
        model.setGenderID(getGender(item.getGenderID()));
        model.setIdentityCard(item.getIdentityCard());
        model.setInsuranceNo(item.getInsuranceNo());
        model.setPermanentAddress(buildAddress(item.getPermanentAddress(), item.getPermanentProvinceID(), item.getPermanentDistrictID(), item.getPermanentWardID()));
        model.setCurrentAddress(buildAddress(item.getCurrentAddress(), item.getCurrentProvinceID(), item.getCurrentDistrictID(), item.getCurrentWardID()));

        return model;
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

    public Map<String, String> colsSheet1() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "code");
        cols.put("2", "fullName");
        cols.put("3", "identityCard");
        cols.put("4", "dob");
        cols.put("5", "genderID");
        cols.put("6", "patientPhone");
        cols.put("7", "");
        cols.put("8", "permanentAddress");
        cols.put("9", "permanentWardID");
        cols.put("10", "permanentDistrictID");
        cols.put("11", "permanentProvinceID");
        cols.put("12", "currentAddress");
        cols.put("13", "currentWardID");
        cols.put("14", "currentDistrictID");
        cols.put("15", "currentProvinceID");
        cols.put("16", "");
        cols.put("17", "");
        cols.put("18", "");
        cols.put("19", "");
        cols.put("20", "");
        cols.put("21", "");
        cols.put("22", "insuranceNo");

        return cols;
    }

    public Map<String, String> colsSheet3() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "arvCode");
        cols.put("2", "");
        cols.put("3", "examinationTime");
        cols.put("4", "appointmentTime");
        cols.put("5", "daysReceived");
        cols.put("6", "mutipleMonth");
        return cols;
    }

    public Map<String, String> colsSheet5() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "arvCode");
        cols.put("2", "");
        cols.put("3", "");
        cols.put("4", "");
        cols.put("5", "testTime");
        cols.put("6", "resultTime");
        cols.put("7", "resultNumber");
        return cols;
    }

    public Map<String, String> colsSheet4() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "arvCode");
        cols.put("2", "");
        cols.put("3", "type");
        cols.put("4", "");
        cols.put("5", "");
        cols.put("6", "");
        cols.put("7", "startTime");
        cols.put("8", "endCase");
        cols.put("9", "endTime");

        return cols;
    }

    @Override
    public PqmArvImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmArvImportForm item = new PqmArvImportForm();
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

    public PqmArvSheet2ImportForm mappingSheet2(Map<String, String> cols, List<String> cells) {
        PqmArvSheet2ImportForm item = new PqmArvSheet2ImportForm();
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

    public PqmArvSheet3ImportForm mappingSheet3(Map<String, String> cols, List<String> cells) {
        PqmArvSheet3ImportForm item = new PqmArvSheet3ImportForm();
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

    public PqmArvSheet4ImportForm mappingSheet4(Map<String, String> cols, List<String> cells) {
        PqmArvSheet4ImportForm item = new PqmArvSheet4ImportForm();
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

    public PqmArvSheet5ImportForm mappingSheet5(Map<String, String> cols, List<String> cells) {
        PqmArvSheet5ImportForm item = new PqmArvSheet5ImportForm();
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

    public Map<String, String> colsSheet2() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "arvCode");
        cols.put("2", "");
        cols.put("3", "");
        cols.put("4", "registrationType");
        cols.put("5", "registrationTime");

        return cols;
    }

}
