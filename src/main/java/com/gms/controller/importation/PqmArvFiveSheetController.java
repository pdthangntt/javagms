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
import com.gms.entity.form.PqmArvFiveSheetImportForm;
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

@Controller(value = "importation_pqm_arv_five_sheet")
public class PqmArvFiveSheetController extends BaseController<PqmArvFiveSheetImportForm> {

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
    @Autowired
    private PqmLogService logService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-arv/five-sheet/import.html");
        form.setSmallUrl("/backend/pqm-arv/index.html");
        form.setReadUrl("/pqm-arv/five-sheet/import.html");
        form.setTitle("Nhập dữ liệu ARV 5 Sheet");
        form.setSmallTitle("Khách hàng điều trị");
        form.setTemplate(fileTemplate("arv_five_sheet_template.xlsx"));
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

    @GetMapping(value = {"/pqm-arv/five-sheet/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        return "importation/pqm/upload_arv";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-arv/five-sheet/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        if (!isPAC()) {
            sites = getCurrentUser().getSite().getID().toString();
        }

        System.out.println("xxx " + sites);
        if (StringUtils.isEmpty(sites)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
            return redirect(form.getUploadUrl());
        }
        try {
            //xoas du lieu
            List<PqmArvEntity> dataObdld = arvService.findBySiteID(Long.valueOf(sites));
            Set<Long> arvIDg = new HashSet<>();
            if (dataObdld != null && !dataObdld.isEmpty()) {
                for (PqmArvEntity dat : dataObdld) {
                    arvIDg.add(dat.getID());
                }
                stageService.resetDataByArvID(arvIDg);
                visitService.resetDataByArvID(arvIDg);
                viralService.resetDataByArvID(arvIDg);
                testService.resetDataByArvID(arvIDg);
            }

            arvService.resetDataBySite(Long.valueOf(sites));

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());

            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmArvFiveSheetImportForm> data = new ArrayList<>();
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 4) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Mã BA"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Họ và tên"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Số CMTND/ CCCD"))) {
                        ok = true;
                    }
                }
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 5 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(0) == null || row.getValue().get(0).equals("")))) {
                    continue;
                }
                PqmArvFiveSheetImportForm item = mapping(colsSheet1(), row.getValue());
                if (StringUtils.isNotEmpty(item.getCode())) {
                    item.setCode(item.getCode().substring(6));
                    item.setCode(item.getCode().trim());
                }
                item.setRow(row.getKey() + 1 + "");
                data.add(item);
            }

            model.addAttribute("site", options.get("siteOpc").get(sites));
            model.addAttribute("form", form);
            model.addAttribute("h", "ok");

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            //DU LIEU SHEET 2
            Map<Integer, List<String>> dataSheet2 = readFileFormattedCell(workbook, 1, model, -1);
            List<PqmArvSheet2ImportForm> dataSheet2s = new ArrayList<>();
            List<String> codePrEP = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> row : dataSheet2.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet2ImportForm item = mappingSheet2(colsSheet2(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                if (StringUtils.isNotEmpty(item.getArvCode())) {
                    item.setArvCode(item.getArvCode().substring(6));
                    item.setArvCode(item.getArvCode().trim());
                }
                if (item.getRegistrationType() != null && item.getRegistrationType().equals("Điều trị phơi nhiễm")) {
                    if (item.getArvCode() != null && !item.getArvCode().equals("")) {
                        codePrEP.add(item.getArvCode().toUpperCase());
                    }
                }
                dataSheet2s.add(item);

            }
            //DU LIEU SHEET 3
            Map<Integer, List<String>> dataSheet3 = readFileFormattedCell(workbook, 2, model, -1);
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
                if (StringUtils.isNotEmpty(item.getArvCode())) {
                    item.setArvCode(item.getArvCode().substring(6));
                    item.setArvCode(item.getArvCode().trim());
                }
                dataSheet3s.add(item);

            }
            //DU LIEU SHEET 4
            Map<Integer, List<String>> dataSheet4 = readFileFormattedCell(workbook, 3, model, -1);
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
                if (StringUtils.isNotEmpty(item.getArvCode())) {
                    item.setArvCode(item.getArvCode().substring(6));
                    item.setArvCode(item.getArvCode().trim());
                }
                dataSheet4s.add(item);

            }
            //DU LIEU SHEET 5
            Map<Integer, List<String>> dataSheet5 = readFileFormattedCell(workbook, 4, model, -1);
            List<PqmArvSheet5ImportForm> dataSheet5s = new LinkedList<>();

            for (Map.Entry<Integer, List<String>> row : dataSheet5.entrySet()) {
                if (row == null || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (row.getKey() < 2 || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(1) == null || row.getValue().get(1).equals("")))) {
                    continue;
                }
                PqmArvSheet5ImportForm item = mappingSheet5(colsSheet5(), row.getValue());
                item.setRow(row.getKey() + 1 + "");
                if (StringUtils.isNotEmpty(item.getArvCode())) {
                    item.setArvCode(item.getArvCode().substring(6));
                    item.setArvCode(item.getArvCode().trim());
                }
                dataSheet5s.add(item);

            }

            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet1 = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols1 = colsSheet1();

            int st = 0;
            int vs = 0;
            int vr = 0;
            int te = 0;

            //check lỗi ARV
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            //Lấy các arv code để find các stage, visit,... cũ
            //arv
            int success = 0;
            PqmArvEntity itemArv;
            for (PqmArvFiveSheetImportForm arv : data) {
                Row row1 = sheet1.getRow(Integer.valueOf(arv.getRow()) - 1);

                List< String> error = validateArv(arv, sites, row1, style, cols1);
                if (error.isEmpty()) {
                    try {
                        if (codePrEP.contains(arv.getCode().toUpperCase())) {
                            continue;
                        }
                        success += 1;
                    } catch (Exception e) {
                        errors.get(String.valueOf(arv.getRow())).add(e.getMessage());
                    }
                } else {
                    errors.put(String.valueOf(arv.getRow()), error);
                }

            }

            if (errors.isEmpty()) {
                for (PqmArvFiveSheetImportForm arv : data) {
                    List< String> error = validateArvSencond(arv, sites);
                    if (error.isEmpty()) {
                        try {
                            if (codePrEP.contains(arv.getCode().toUpperCase())) {
                                continue;
                            }
                            itemArv = getArv(arv, sites);
                            itemArv.setSiteID(Long.valueOf(sites));
                            arvService.save(itemArv);
                        } catch (Exception e) {
                        }
                    }

                }

                //Map để gắn ID vào các stage, visit,....
                Map<String, Long> mapIDs = new HashMap<>();
                List<PqmArvEntity> arvs = arvService.findBySiteID(Long.valueOf(sites));
                if (arvs != null) {
                    for (PqmArvEntity item : arvs) {
                        mapIDs.put(item.getCode(), item.getID());
                    }
                }

                HashMap<String, List< String>> stageErrors = new LinkedHashMap<>();
                //stage

                //Lấy sheet và tạo màu Màu đổ
                Sheet sheet2 = workbook.getSheetAt(1);
                Map<String, String> cols2 = colsSheet2();

                PqmArvStageEntity stage;
                for (PqmArvSheet2ImportForm row : dataSheet2s) {
                    if (row.getArvCode() != null && codePrEP.contains(row.getArvCode().toUpperCase())) {
                        continue;
                    }
                    Row row2 = sheet2.getRow(Integer.valueOf(row.getRow()) - 1);

                    List< String> error = validateStage(row, row2, style, cols2);
                    if (error.isEmpty()) {
                        List<PqmArvStageEntity> stages = stageService.findByArvIDAndRegistrationTime(mapIDs.getOrDefault(row.getArvCode().toUpperCase(), Long.valueOf("-1")), row.getRegistrationTime());
                        if (!checkArv(row.getArvCode().toUpperCase(), sites)) {
                            stageErrors.put(String.valueOf(row.getRow()), error);
                            stageErrors.get(String.valueOf(row.getRow())).add("Không tìm thấy bệnh án có mã " + row.getArvCode().toUpperCase());
                            continue;
                        }
                        try {
                            if (stages == null) {
                                stage = new PqmArvStageEntity();
                                stage = getStage(stage, row);
                                stage.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                stage.setSiteID(Long.valueOf(sites));
                                //save
                                stageService.save(stage);
                                st += 1;
                            } else if (stages.size() == 1) {
                                stage = stages.get(0);
                                stage = getStage(stage, row);
                                stage.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                stage.setSiteID(Long.valueOf(sites));
                                //save
                                stageService.save(stage);
                                st += 1;
                            } else {
                                error.add("Bệnh án có " + stages.size() + " đợt điều trị có cùng ngày đăng ký trong hệ thống");
                                stageErrors.put(String.valueOf(row.getRow()), error);
                            }
                        } catch (Exception e) {
                            stageErrors.put(String.valueOf(row.getRow()), error);
                            stageErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                        }
                    } else {
                        stageErrors.put(String.valueOf(row.getRow()), error);
                    }
                }

                //Sheet 3
                //Lấy sheet và tạo màu Màu đổ
                Sheet sheet3 = workbook.getSheetAt(2);
                Map<String, String> cols3 = colsSheet3();

                HashMap<String, List< String>> visitErrors = new LinkedHashMap<>();
                PqmArvVisitEntity visit;
                for (PqmArvSheet3ImportForm row : dataSheet3s) {
                    if (row.getArvCode() != null && codePrEP.contains(row.getArvCode().toUpperCase())) {
                        continue;
                    }
                    Row row3 = sheet3.getRow(Integer.valueOf(row.getRow()) - 1);
                    List< String> error = validateVisit(row, row3, style, cols3);
                    if (error.isEmpty()) {
                        List<PqmArvVisitEntity> visits = visitService.findByArvIDAndExaminationTime(mapIDs.getOrDefault(row.getArvCode().toUpperCase(), Long.valueOf("-1")), row.getExaminationTime());
                        if (!checkArv(row.getArvCode().toUpperCase(), sites)) {
                            visitErrors.put(String.valueOf(row.getRow()), error);
                            visitErrors.get(String.valueOf(row.getRow())).add("Không tìm thấy bệnh án có mã " + row.getArvCode().toUpperCase());
                            continue;
                        }
                        try {
                            if (visits == null) {
                                visit = new PqmArvVisitEntity();
                                visit = getVisit(visit, row);
                                visit.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                //save
                                visitService.save(visit);
                                vs += 1;
                            } else if (visits.size() == 1) {
                                visit = visits.get(0);
                                visit = getVisit(visit, row);
                                visit.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                //save
                                visitService.save(visit);
                                vs += 1;
                            } else {
                                error.add("Bệnh án có " + visits.size() + " lần khám có cùng ngày khám trong hệ thống");
                                visitErrors.put(String.valueOf(row.getRow()), error);
                            }

                        } catch (Exception e) {
                            visitErrors.put(String.valueOf(row.getRow()), error);
                            visitErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                        }
                    } else {
                        visitErrors.put(String.valueOf(row.getRow()), error);

                    }
                }

                //Sheet 5
                //Lấy sheet và tạo màu Màu đổ
                Sheet sheet5 = workbook.getSheetAt(4);
                Map<String, String> cols5 = colsSheet5();

                HashMap<String, List< String>> viralErrors = new LinkedHashMap<>();
                PqmArvViralLoadEntity viral;
                for (PqmArvSheet5ImportForm row : dataSheet5s) {
                    if (row.getArvCode() != null && codePrEP.contains(row.getArvCode().toUpperCase())) {
                        continue;
                    }

                    Row row5 = sheet5.getRow(Integer.valueOf(row.getRow()) - 1);
                    List< String> error = validateViral(row, row5, style, cols5);
                    if (error.isEmpty()) {
                        List<PqmArvViralLoadEntity> virals = viralService.findByArvIDAndTestTime(mapIDs.getOrDefault(row.getArvCode().toUpperCase(), Long.valueOf("-1")), row.getTestTime());
                        if (!checkArv(row.getArvCode().toUpperCase(), sites)) {
                            viralErrors.put(String.valueOf(row.getRow()), error);
                            viralErrors.get(String.valueOf(row.getRow())).add("Không tìm thấy bệnh án có mã: " + row.getArvCode().toUpperCase());
                            continue;
                        }
                        try {
                            if (mapIDs.get(row.getArvCode().toUpperCase()) == null) {
                                viralErrors.put(String.valueOf(row.getRow()), error);
                                viralErrors.get(String.valueOf(row.getRow())).add("Không tìm thấy bệnh án có mã " + row.getArvCode().toUpperCase());
                                continue;
                            }
                            if (virals == null) {
                                viral = new PqmArvViralLoadEntity();
                                viral = getViral(viral, row);
                                viral.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                //save
                                viralService.save(viral);
                                vr += 1;
                            } else if (virals.size() == 1) {
                                viral = virals.get(0);
                                viral = getViral(viral, row);
                                viral.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                //save
                                viralService.save(viral);
                                vr += 1;
                            } else {
                                error.add("Bệnh án có " + virals.size() + " lần TLVR có cùng ngày khám trong hệ thống");
                                viralErrors.put(String.valueOf(row.getRow()), error);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            viralErrors.put(String.valueOf(row.getRow()), error);
                            viralErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                        }

                    } else {
                        viralErrors.put(String.valueOf(row.getRow()), error);

                    }
                }

                //Lấy sheet và tạo màu Màu đổ
                Sheet sheet4 = workbook.getSheetAt(3);
                Map<String, String> cols4 = colsSheet4();

                PqmArvTestEntity test;
                HashMap<String, List< String>> testErrors = new LinkedHashMap<>();
                for (PqmArvSheet4ImportForm row : dataSheet4s) {
                    if (row.getType() != null && row.getType().contains("phòng")) {
                        //validate
                        if (row.getArvCode() != null && codePrEP.contains(row.getArvCode().toUpperCase())) {
                            continue;
                        }

                        Row row4 = sheet4.getRow(Integer.valueOf(row.getRow()) - 1);
                        List< String> error = validateTest(row, row4, style, cols4);
                        if (error.isEmpty()) {
                            List<PqmArvTestEntity> tests = testService.findByArvIDAndInhFromTime(mapIDs.getOrDefault(row.getArvCode().toUpperCase(), Long.valueOf("-1")), row.getStartTime());
                            if (!checkArv(row.getArvCode().toUpperCase(), sites)) {
                                testErrors.put(String.valueOf(row.getRow()), error);
                                testErrors.get(String.valueOf(row.getRow())).add("Không tìm thấy bệnh án có mã " + row.getArvCode().toUpperCase());
                                continue;
                            }
                            try {
                                if (tests == null) {
                                    test = new PqmArvTestEntity();
                                    test = getTest(test, row);
                                    test.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                    //save
                                    testService.save(test);
                                    te += 1;
                                } else if (tests.size() == 1) {
                                    test = tests.get(0);
                                    test = getTest(test, row);
                                    test.setArvID(mapIDs.get(row.getArvCode().toUpperCase()));
                                    //save
                                    testService.save(test);
                                    te += 1;
                                } else {
                                    error.add("Bệnh án có " + tests.size() + " lần khám dự phòng có cùng ngày bắt đầu INH trong hệ thống");
                                    testErrors.put(String.valueOf(row.getRow()), error);
                                }

                            } catch (Exception e) {
                                testErrors.put(String.valueOf(row.getRow()), error);
                                testErrors.get(String.valueOf(row.getRow())).add(e.getMessage());
                            }

                        } else {
                            testErrors.put(String.valueOf(row.getRow()), error);

                        }
                    }

                }

                //Lưu tạm file vào thư mục tạm
                model.addAttribute("filePath", saveFile(workbook, file));

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
                model.addAttribute("successx", success + codePrEP.size());
                model.addAttribute("form", form);

                model.addAttribute("ok", true);
                model.addAttribute("oks", errors.isEmpty()
                        && stageErrors.isEmpty()
                        && testErrors.isEmpty()
                        && viralErrors.isEmpty()
                        && visitErrors.isEmpty()
                );

                logService.log("Nhập dữ liệu ARV 5 sheet", data.size(), success, data.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", Long.valueOf(sites), "Điều trị ARV");
                return "importation/pqm/arv_five_sheet";
            } else {

                //Lưu tạm file vào thư mục tạm
                model.addAttribute("filePath", saveFile(workbook, file));

                model.addAttribute("errorsw", errors);
                model.addAttribute("stageErrors", null);
                model.addAttribute("testErrors", null);
                model.addAttribute("visitErrors", null);
                model.addAttribute("viralErrors", null);
                model.addAttribute("st", st);
                model.addAttribute("vs", vs);
                model.addAttribute("te", te);
                model.addAttribute("vr", vr);
                model.addAttribute("site", options.get("siteOpc").get(sites));
                model.addAttribute("total", data.size());
                model.addAttribute("successx", success + codePrEP.size());
                model.addAttribute("form", form);

                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                model.addAttribute("oks", false);
                model.addAttribute("h", "ok");

                return "importation/pqm/arv_five_sheet";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }

    }

    private PqmArvVisitEntity getVisit(PqmArvVisitEntity model, PqmArvSheet3ImportForm item) {
        model.setExaminationTime(item.getExaminationTime() == null ? null : TextUtils.convertDate(item.getExaminationTime(), FORMATDATE));
        model.setAppointmentTime(item.getAppointmentTime() == null ? null : TextUtils.convertDate(item.getAppointmentTime(), FORMATDATE));
        model.setMutipleMonth(item.getMutipleMonth());
        try {
            model.setDaysReceived(Integer.valueOf(item.getDaysReceived()));
        } catch (Exception e) {
            model.setDaysReceived(Integer.valueOf("0"));
        }
        return model;

    }

    private List<String> validateStage(PqmArvSheet2ImportForm form, Row row1, CellStyle style, Map<String, String> cols) {

        String registrationTime = StringUtils.isEmpty(form.getRegistrationTime()) ? null : form.getRegistrationTime();
        String registrationType = StringUtils.isEmpty(form.getRegistrationType()) ? null : getRegistrationType(form.getRegistrationType());
        String endTime = StringUtils.isEmpty(form.getEndTime()) ? null : form.getEndTime();

        String row = StringUtils.isEmpty(form.getRow()) ? null : form.getRow();

        List<String> errors = new ArrayList<>();

        validateNull(form.getArvCode(), "SHEET2: Mã bệnh án", errors);
        if (StringUtils.isEmpty(form.getArvCode())) {
            addExcelError("arvCode", row1, cols, style);
        }
        validateNull(registrationTime, "SHEET2: Ngày đăng ký", errors);
        if (StringUtils.isEmpty(registrationTime)) {
            addExcelError("registrationTime", row1, cols, style);
        }
        validateNull(getRegistrationType(form.getRegistrationType()), "SHEET2: Loại đăng ký", errors);
        if (StringUtils.isEmpty(getRegistrationType(form.getRegistrationType()))) {
            addExcelError("registrationType", row1, cols, style);
        }
        if (StringUtils.isNotBlank(registrationTime) && !isThisDateValid(registrationTime)) {
            errors.add("SHEET2- Ngày đăng ký không đúng định dạng dd/mm/yyyy");
            addExcelError("registrationTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(registrationTime) && isThisDateValid(registrationTime)) {
            Date today = new Date();
            Date time = TextUtils.convertDate(registrationTime, FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET2- Ngày đăng ký không được sau ngày hiện tại");
                addExcelError("registrationTime", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("SHEET2: Ngày kết thúc không đúng định dạng dd/mm/yyyy");
            addExcelError("endTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && isThisDateValid(form.getEndTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getEndTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET2- Ngày kết thúc không được sau ngày hiện tại");
                addExcelError("endTime", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getTreatmentTime()) && !isThisDateValid(form.getTreatmentTime())) {
            errors.add("SHEET2- Ngày điều trị không đúng định dạng dd/mm/yyyy");
            addExcelError("treatmentTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getTreatmentTime()) && isThisDateValid(form.getTreatmentTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getTreatmentTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET2- Ngày điều trị không được sau ngày hiện tại");
                addExcelError("treatmentTime", row1, cols, style);
            }
        }

        return errors;
    }

    private PqmArvStageEntity getStage(PqmArvStageEntity model, PqmArvSheet2ImportForm importForm) {

        Date registrationTime = StringUtils.isEmpty(importForm.getRegistrationTime()) ? null : TextUtils.convertDate(importForm.getRegistrationTime(), FORMATDATE);
        String registrationType = StringUtils.isEmpty(importForm.getRegistrationType()) ? null : importForm.getRegistrationType();

        model.setRegistrationTime(registrationTime);
        model.setRegistrationType(getRegistrationType(registrationType));

        model.setEndTime(importForm.getEndTime() == null ? null : TextUtils.convertDate(importForm.getEndTime(), FORMATDATE));
        model.setEndCase(importForm.getEndTime() == null ? null : getEndCase(importForm.getEndCase()));

        Date treatmentTime = null;
        if (importForm.getRegistrationType().equals("BN HIV mới đăng ký lần đầu") || importForm.getRegistrationType().equals("BN chưa ARV nơi khác chuyển tới") || importForm.getRegistrationType().equals("Trẻ dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV") || importForm.getRegistrationType().equals("BN chưa ARV đăng ký lại")) {
            treatmentTime = TextUtils.convertDate(importForm.getTreatmentTime(), FORMATDATE);
        } else if (importForm.getRegistrationType().equals("BN ARV nơi khác chuyển tới") || importForm.getRegistrationType().equals("BN ARV bỏ trị tái điều trị")) {
            treatmentTime = registrationTime;
        }
        model.setTreatmentTime(treatmentTime);

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
        return model;
    }

    private List<String> validateViral(PqmArvSheet5ImportForm form, Row row1, CellStyle style, Map<String, String> cols) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getTestTime(), "SHEET5: Ngày XN", errors);
        if (StringUtils.isEmpty(form.getTestTime())) {
            addExcelError("testTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getTestTime()) && !isThisDateValid(form.getTestTime())) {
            errors.add("SHEET5: Ngày XN không đúng định dạng dd/mm/yyyy");
            addExcelError("testTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getTestTime()) && isThisDateValid(form.getTestTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getTestTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET5: Ngày XN không được sau ngày hiện tại");
                addExcelError("testTime", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getResultTime()) && !isThisDateValid(form.getResultTime())) {
            errors.add("SHEET5: Ngày nhận kết quả không đúng định dạng dd/mm/yyyy");
            addExcelError("resultTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getResultTime()) && isThisDateValid(form.getResultTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getResultTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET5: Ngày nhận kết quả không được sau ngày hiện tại");
                addExcelError("resultTime", row1, cols, style);
            }
        }

        return errors;
    }

    private List<String> validateVisit(PqmArvSheet3ImportForm form, Row row1, CellStyle style, Map<String, String> cols) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getExaminationTime(), "SHEET3: Ngày khám", errors);
        if (StringUtils.isEmpty(form.getExaminationTime())) {
            addExcelError("examinationTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getExaminationTime()) && !isThisDateValid(form.getExaminationTime())) {
            errors.add("SHEET3: Ngày khám không đúng định dạng dd/mm/yyyy");
            addExcelError("examinationTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getExaminationTime()) && isThisDateValid(form.getExaminationTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getExaminationTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET3: Ngày khám không được sau ngày hiện tại");
                addExcelError("examinationTime", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getAppointmentTime()) && !isThisDateValid(form.getAppointmentTime())) {
            errors.add("SHEET3: Ngày hẹn khám không đúng định dạng dd/mm/yyyy");
            addExcelError("appointmentTime", row1, cols, style);
        }
        try {
            if (StringUtils.isNotEmpty(form.getDaysReceived())) {
                int a = Integer.valueOf(form.getDaysReceived());
                if (a < 0) {
                    errors.add("SHEET3: Số ngày nhận thuốc chỉ được nhập nguyên dương");
                    addExcelError("daysReceived", row1, cols, style);
                }
            }
        } catch (Exception e) {
            errors.add("SHEET3: Số ngày nhận thuốc chỉ được nhập nguyên dương");
            addExcelError("daysReceived", row1, cols, style);
        }

        return errors;
    }

    private List<String> validateTest(PqmArvSheet4ImportForm form, Row row1, CellStyle style, Map<String, String> cols) {
        List<String> errors = new ArrayList<>();

        validateNull(form.getStartTime(), "SHEET4: Ngày bắt đầu", errors);
        if (StringUtils.isEmpty(form.getStartTime())) {
            addExcelError("startTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            errors.add("SHEET4: Ngày bắt đầu không đúng định dạng dd/mm/yyyy");
            addExcelError("startTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && isThisDateValid(form.getStartTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getStartTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET4: Ngày bắt đầu không được sau ngày hiện tại");
                addExcelError("startTime", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("SHEET4: Ngày kết thúc không đúng định dạng dd/mm/yyyy");
            addExcelError("endTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && isThisDateValid(form.getEndTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getEndTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("SHEET4: Ngày kết thúc không được sau ngày hiện tại");
                addExcelError("endTime", row1, cols, style);
            }
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

    private PqmArvViralLoadEntity getViral(PqmArvViralLoadEntity model, PqmArvSheet5ImportForm item) {
        model.setTestTime(item.getTestTime() == null ? null : TextUtils.convertDate(item.getTestTime(), FORMATDATE));
        model.setResultTime(item.getResultTime() == null ? null : TextUtils.convertDate(item.getResultTime(), FORMATDATE));
        model.setResultNumber(getResultNumber(item.getResultNumber()));
        model.setResult(getResultViral(item.getResultNumber()));

        return model;

    }

    private String getResultNumber(String virusNumber) {
        if (StringUtils.isEmpty(virusNumber)) {
            return "0";
        } else {
            return StringUtils.isEmpty(virusNumber.replaceAll("[^0-9]", "")) ? "0" : virusNumber.replaceAll("[^0-9]", "");
        }
    }

    private String getResultViral(String ketQua) {
        if (ketQua == null) {
            return "1";
        }
        try {
            ketQua = ketQua.replaceAll("[^0-9]", "");
            int result = Integer.parseInt(ketQua);
            if (result <= 20) {
                ketQua = "1";
            } else if (result > 20 && result < 200) {
                ketQua = "2";
            } else if (result >= 200 && result < 1000) {
                ketQua = "3";
            } else if (result >= 1000) {
                ketQua = "4";
            }
        } catch (NumberFormatException ex) {
            ketQua = "1";
        }
        return ketQua;
    }

    private PqmArvTestEntity getTest(PqmArvTestEntity model, PqmArvSheet4ImportForm item) {

        model.setInhFromTime(item.getStartTime() == null ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setInhToTime(item.getEndTime() == null ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));

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
            case "Trẻ dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV":
                return "4";
            default:
                return "";
        }

    }

    private boolean checkArv(String code, String site) {
        if (StringUtils.isNotEmpty(code)) {
            PqmArvEntity model = arvService.findBySiteIDAndCode(Long.valueOf(site), code.toUpperCase());
            if (model != null) {
                return true;
            }
        }
        return false;
    }

    private List<String> validateArv(PqmArvFiveSheetImportForm form, String sites, Row row1, CellStyle style, Map<String, String> cols) {

        List<String> errors = new ArrayList<>();

        validateNull(form.getCode(), "Mã bệnh án", errors);
        if (StringUtils.isEmpty(form.getCode())) {
            addExcelError("code", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getDob()) && !isThisDateValid(form.getDob())) {
            errors.add("Ngày sinh không đúng định dạng dd/mm/yyyy");
            addExcelError("dob", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getDob()) && isThisDateValid(form.getDob())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getDob(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày sinh không được sau ngày hiện tại");
                addExcelError("dob", row1, cols, style);
            }
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && !isThisDateValid(form.getFirstTreatmentTime())) {
            errors.add("Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
            addExcelError("firstTreatmentTime", row1, cols, style);
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && isThisDateValid(form.getFirstTreatmentTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getFirstTreatmentTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày bắt đầu điều trị không được sau ngày hiện tại");
                addExcelError("firstTreatmentTime", row1, cols, style);
            }
        }
        return errors;
    }

    private List<String> validateArvSencond(PqmArvFiveSheetImportForm form, String sites) {

        List<String> errors = new ArrayList<>();

        validateNull(form.getCode(), "Mã bệnh án", errors);
        if (StringUtils.isNotBlank(form.getDob()) && !isThisDateValid(form.getDob())) {
            errors.add("Ngày sinh không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getDob()) && isThisDateValid(form.getDob())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getDob(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày sinh không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && !isThisDateValid(form.getFirstTreatmentTime())) {
            errors.add("Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && isThisDateValid(form.getFirstTreatmentTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getFirstTreatmentTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }
        return errors;
    }

    private PqmArvEntity getArv(PqmArvFiveSheetImportForm item, String sites) {
        PqmArvEntity model = arvService.findBySiteIDAndCode(Long.valueOf(sites), item.getCode().toUpperCase());
        if (model == null) {
            model = new PqmArvEntity();
        }
        model.setCode(item.getCode().toUpperCase());
        model.setDob(StringUtils.isEmpty(item.getDob()) ? null : TextUtils.convertDate(item.getDob(), FORMATDATE));
        model.setFirstTreatmentTime(StringUtils.isEmpty(item.getFirstTreatmentTime()) ? null : TextUtils.convertDate(item.getFirstTreatmentTime(), FORMATDATE));
        model.setFullName(item.getName() == null ? "" : TextUtils.toFullname(item.getName()));
        model.setGenderID(getGender(item.getGender()));
        model.setIdentityCard(item.getCmnd());
        model.setInsuranceNo(item.getBhyt());
        model.setPatientPhone(item.getMobile());
        model.setPermanentAddress(getAddress(item.getPermanentAddress(), item.getPermanentWardID(), item.getPermanentDistrictID(), item.getPermanentProvinceID()));
        model.setCurrentAddress(getAddress(
                StringUtils.isEmpty(item.getCurrentAddress()) ? item.getPermanentAddress() : item.getCurrentAddress(),
                StringUtils.isEmpty(item.getCurrentWardID()) ? item.getPermanentWardID() : item.getCurrentWardID(),
                StringUtils.isEmpty(item.getCurrentDistrictID()) ? item.getPermanentDistrictID() : item.getCurrentDistrictID(),
                StringUtils.isEmpty(item.getCurrentProvinceID()) ? item.getPermanentProvinceID() : item.getCurrentProvinceID())
        );
        return model;
    }

    private String getAddress(String address, String ward, String district, String province) {
        address = address == null || address.equals("") ? "" : address + ", ";
        ward = ward == null || ward.equals("") ? "" : ward + ", ";
        district = district == null || district.equals("") ? "" : district + ", ";
        province = province == null || province.equals("") ? "" : province;
        return address + ward + district + province;
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
        cols.put("2", "name");
        cols.put("3", "cmnd");
        cols.put("4", "dob");
        cols.put("5", "gender");
        cols.put("6", "mobile");
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
        cols.put("19", "firstTreatmentTime");
        cols.put("20", "");
        cols.put("21", "");
        cols.put("22", "bhyt");

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
    public PqmArvFiveSheetImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmArvFiveSheetImportForm item = new PqmArvFiveSheetImportForm();
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
        cols.put("3", "treatmentTime");
        cols.put("4", "registrationType");
        cols.put("5", "registrationTime");
        cols.put("6", "endCase");
        cols.put("7", "endTime");

        return cols;
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

}
