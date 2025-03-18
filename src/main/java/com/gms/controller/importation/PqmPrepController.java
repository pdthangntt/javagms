package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.constant.PqmPrepResultEnum;
import com.gms.entity.constant.PqmPrepStatusEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepStageEntity;
import com.gms.entity.db.PqmPrepVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PqmPrepImportForm;
import com.gms.entity.form.PqmPrepTableImportForm;
import com.gms.entity.form.PqmPrepVisitTableImportForm;
import com.gms.service.PqmLogService;
import com.gms.service.PqmPrepService;
import com.gms.service.PqmPrepStageService;
import com.gms.service.PqmPrepVisitService;
import java.io.IOException;
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

@Controller(value = "importation_pqm_prep")
public class PqmPrepController extends BaseController<PqmPrepImportForm> {

    private static HashMap<String, HashMap<String, String>> options;
    private static int firstRow = 4;

    @Autowired
    private PqmPrepService pqmService;
    @Autowired
    private PqmPrepStageService stageService;
    @Autowired
    private PqmPrepVisitService visitService;
    @Autowired
    private PqmLogService logService;

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-prep/import.html");
        form.setSmallUrl("/backend/pqm-prep/index.html");
        form.setReadUrl("/pqm-prep/import.html");
        form.setTitle("Tải file excel của PrEP");
        form.setSmallTitle("Khách hàng PrEP");
        form.setTemplate(fileTemplate("Template_Khach hang PrEP.xlsx"));
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

        List<SiteEntity> sitePrEP = siteService.getSitePrEP(getCurrentUser().getSite().getProvinceID());
        options.put("sitePrEP", new HashMap<String, String>());
        options.get("sitePrEP").put("", "Chọn cơ sở");
        for (SiteEntity site : sitePrEP) {
            if (StringUtils.isNotEmpty(site.getHub()) && site.getHub().equals("1")) {
                options.get("sitePrEP").put(String.valueOf(site.getID()), site.getName());
            }
        }

        return options;
    }

    /**
     * Mapping cols - cell excel
     *
     * @param isPAC
     * @return
     */
    public Map<String, String> cols(int endNumber) {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "fullName");
        cols.put("2", "yearOfBirth");
        cols.put("3", "genderID");
        cols.put("4", "code");
        cols.put("5", "type");
        cols.put("6", "identityCard");
        cols.put("7", "patientPhone");
        cols.put("8", "objectGroupID");
        cols.put("9", "source");
        cols.put("10", "otherSite");
        cols.put("11", "projectSupport");

        cols.put("12", "startTime");
        cols.put("13", "treatmentRegimen");
        cols.put("14", "daysReceived");
        cols.put("15", "appointmentTime");
        cols.put("16", "resultsID");

        cols.put("17", "creatinin");
        cols.put("18", "hBsAg");
        cols.put("19", "ganC");
        cols.put("20", "giangMai");
        cols.put("21", "clamydia");
        cols.put("22", "lau");
        cols.put("23", "sot");
        cols.put("24", "metMoi");
        cols.put("25", "dauCo");
        cols.put("26", "phatBan");

        cols.put(endNumber + "", "endTime");
        cols.put((endNumber + 1) + "", "endCause");

        return cols;
    }

    @GetMapping(value = {"/pqm-prep/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("sites", getOptions().get("sitePrEP"));
        return "importation/pqm/upload_prep";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-prep/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();

        if (!isPAC()) {
            sites = getCurrentUser().getSite().getID().toString();
        }

        if (StringUtils.isEmpty(sites)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
            return redirect(form.getUploadUrl());
        }
        try {
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmPrepTableImportForm> data = new ArrayList<>();
            int endNumber = -1;
            int firstVisit = -1;
            boolean isPAC = true;
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 1) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(2));
                    System.out.println("333 " + row.getValue().get(3));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && ((StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Tên khách hàng"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Năm sinh"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Giới tính"))))) {
                        ok = true;
                    }
                }

                if (row.getKey() == 1 && row.getValue() != null && !row.getValue().isEmpty()) {
                    endNumber = row.getValue().indexOf("Ngày kết thúc dùng PrEP");
                    firstVisit = row.getValue().indexOf("Kì tái khám 1");

                    if (endNumber == -1 || firstVisit == -1) {
                        redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                        return redirect(form.getUploadUrl());
                    }
                }

                if (row.getKey() <= (firstRow - 2) || (row.getValue() != null && !row.getValue().isEmpty() && (row.getValue().get(0) == null || row.getValue().get(0).equals("")))) {
                    continue;
                }
                LinkedList<PqmPrepImportForm> visits = new LinkedList<>();
                PqmPrepImportForm visit;
                if (row.getValue() != null && !row.getValue().isEmpty()) {

                    for (int i = firstVisit; i < endNumber; i = i + 18) {
                        int y = i;
                        visit = new PqmPrepImportForm();
                        visit.setFirstIndex(y);

                        visit.setExaminationTime1(row.getValue().get(y++));
                        visit.setTreatmentStatus1(row.getValue().get(y++));
                        visit.setTreatmentRegimen1(row.getValue().get(y++));
                        visit.setDaysReceived1(row.getValue().get(y++));
                        visit.setAppointmentTime1(row.getValue().get(y++));
                        visit.setResultsID1(row.getValue().get(y++));

                        visit.setCreatinin1(row.getValue().get(y++));
                        visit.sethBsAg1(row.getValue().get(y++));
                        visit.setGanC1(row.getValue().get(y++));
                        visit.setGiangMai1(row.getValue().get(y++));
                        visit.setClamydia1(row.getValue().get(y++));
                        visit.setLau1(row.getValue().get(y++));

                        visit.setTuanThuDieuTri1(row.getValue().get(y++));
                        visit.setTacDungPhu1(row.getValue().get(y++));

                        visit.setSot1(row.getValue().get(y++));
                        visit.setMetMoi1(row.getValue().get(y++));
                        visit.setDauCo1(row.getValue().get(y++));
                        visit.setPhatBan1(row.getValue().get(y++));
                        visits.add(visit);
                    }
                }
                PqmPrepTableImportForm item = mapDataToForm(mapping(cols(endNumber), row.getValue()), visits);
                data.add(item);
            }

            model.addAttribute("site", options.get("sitePrEP").get(sites));
            model.addAttribute("form", form);

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int i = firstRow - 1;
            int success = 0;

            //Xử lý dữ liệu cũ mới
            Set<Long> oldPrepIDs = new HashSet<>();
            Set<Long> oldStageIDs = new HashSet<>();
            Set<Long> oldVisitIDs = new HashSet<>();
            //tìm bản ghi sẵn có
            Map<String, PqmPrepVisitEntity> mapCurrentVisitDatas = new HashMap<>();
            Map<Long, String> mapCodes = new HashMap<>();
            List<PqmPrepEntity> list = pqmService.findBySiteID(Long.valueOf(sites));
            Set<Long> prepIDs = new HashSet<>();
            if (list != null) {
                for (PqmPrepEntity entity : list) {
                    oldPrepIDs.add(entity.getID());
                    prepIDs.add(entity.getID());
                    mapCodes.put(entity.getID(), entity.getCode());
                }
            }
            List<PqmPrepVisitEntity> listVisit = visitService.findByPrepIDs(prepIDs);
            if (listVisit != null) {
                for (PqmPrepVisitEntity entity : listVisit) {
                    oldVisitIDs.add(entity.getID());
                    mapCurrentVisitDatas.put(entity.getPrepID() + TextUtils.formatDate(entity.getExaminationTime(), FORMATDATE), entity);
                }
            }
            List<PqmPrepStageEntity> listStage = stageService.findByPrepIDs(prepIDs);
            if (listStage != null) {
                for (PqmPrepStageEntity entity : listStage) {
                    oldStageIDs.add(entity.getID());
                }
            }

            //end
            List<PqmPrepVisitEntity> visits = new ArrayList<>();
            PqmPrepEntity entity;

            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols(endNumber);

            for (PqmPrepTableImportForm item1 : data) {
                i += 1;
                //Lấy dòng excel
                Row row = sheet.getRow(i - 1);
                HashMap<String, List< String>> object = validateImport(item1, row, style, cols);
                if (!object.get("errors").isEmpty()) {
                    errors.put(String.valueOf(i), object.get("errors"));
                    continue;
                }
                success += 1;

            }

            //Lưu tạm file vào thư mục tạm
            model.addAttribute("filePath", saveFile(workbook, file));

            HashMap<String, Set< String>> errorsw = new LinkedHashMap<>();

            if (!errors.isEmpty()) {
                for (Map.Entry<String, List< String>> entry : errors.entrySet()) {
                    String key = entry.getKey();
                    List< String> value = entry.getValue();
                    errorsw.put(key, new HashSet<>());
                    for (String string : value) {
                        errorsw.get(key).add(string);
                    }

                }
            } 

            model.addAttribute("errorsw", errorsw);
            model.addAttribute("total", data.size());
            model.addAttribute("successx", success);
            model.addAttribute("site", options.get("sitePrEP").get(sites));
            model.addAttribute("form", form);

            if (errors.isEmpty()) {
                //Xoá dữ liệu cũ theo cơ sở
                pqmService.deleteAll(oldPrepIDs);
                stageService.deleteAll(oldStageIDs);
                visitService.deleteAll(oldVisitIDs);

                int y = firstRow - 1;
                for (PqmPrepTableImportForm item1 : data) {
                    y += 1;
                    //Lấy dòng excel
                    HashMap<String, List< String>> object = validateImportSencont(item1);
                    if (!object.get("errors").isEmpty()) {
                        errors.put(String.valueOf(y), object.get("errors"));
                        continue;
                    }

                    item1.setSite(sites);
                    //Set to entity prep
                    entity = getPrep(item1);
                    entity.setSiteID(Long.valueOf(sites));

                    try {
                        entity = pqmService.save(entity);
                        //STAGE
                        PqmPrepStageEntity stage = getStage(entity);
                        stage = stageService.save(stage);
                        //VISIT
                        visits.addAll(getPrepVisit(item1, entity.getID(), mapCurrentVisitDatas, stage.getID()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errors.put(String.valueOf(i), new ArrayList<String>());
                        errors.get(String.valueOf(i)).add(e.getMessage());

                    }

                }
                for (PqmPrepVisitEntity e : visits) {
                    e = visitService.save(e);
                }

                model.addAttribute("success", "Đã tiến hành import excel thành công");
                logService.log("Tải file excel của PrEP", data.size(), success, data.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", Long.valueOf(sites), "Dịch vụ PrEP");
                model.addAttribute("ok", true);
                return "importation/pqm/prep";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/prep";
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            model.addAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private PqmPrepStageEntity getStage(PqmPrepEntity entity) {
        List<PqmPrepStageEntity> items = stageService.findByPrepIDAndStartTime(entity.getID(), TextUtils.formatDate(entity.getStartTime(), FORMATDATE_SQL));
        PqmPrepStageEntity item;
        if (items == null) {
            item = new PqmPrepStageEntity();
        } else {
            item = items.get(0);
        }
        item.setPrepID(entity.getID());
        item.setStartTime(entity.getStartTime());
        item.setEndTime(entity.getEndTime());
        item.setEndCause(entity.getEndCause());
        item.setType(entity.getType());

        return item;
    }

    @Override
    public PqmPrepImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmPrepImportForm item = new PqmPrepImportForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    default:
                        item.set(col, StringUtils.isEmpty(cells.get(i)) || cells.get(i).equals("null") ? "" : cells.get(i));
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
                getLogger().warn(ex.getMessage());
            }
        }

        return item;
    }

    private PqmPrepEntity getPrep(PqmPrepTableImportForm item) {
        PqmPrepEntity model = pqmService.findBySiteAndCode(Long.valueOf(item.getSite()), item.getCode());
        if (model == null) {
            model = new PqmPrepEntity();
        }
        model.setCode(item.getCode().toUpperCase());
        model.setType(item.getType());

        //Trường cơ bản prep
        model.setGenderID(item.getGenderID());
        model.setYearOfBirth(item.getYearOfBirth());
        model.setObjectGroupID(item.getObjectGroupID());
        model.setIdentityCard(item.getIdentityCard());
        model.setFullName(TextUtils.toFullname(item.getFullName()));
        model.setPatientPhone(item.getPatientPhone());

        model.setStartTime(StringUtils.isEmpty(item.getStartTime()) ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setEndTime(StringUtils.isEmpty(item.getEndTime()) ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));
        model.setEndCause(item.getEndCause());
        model.setSource(item.getSource());
        model.setOtherSite(item.getOtherSite());
        model.setProjectSupport(item.getProjectSupport());

        return model;
    }

    private List<PqmPrepVisitEntity> getPrepVisit(PqmPrepTableImportForm item, Long prepID, Map<String, PqmPrepVisitEntity> mapCurrentVisitDatas, Long stageID) {

        List<PqmPrepVisitEntity> list = new ArrayList<>();
        if (item.getVisits() != null && !item.getVisits().isEmpty()) {
            PqmPrepVisitEntity model;
            for (PqmPrepVisitTableImportForm visit : item.getVisits()) {
                model = mapCurrentVisitDatas.getOrDefault(prepID + visit.getExaminationTime(), null);
                if (model == null) {
                    model = new PqmPrepVisitEntity();
                }
                model.setStageID(stageID);
                model.setPrepID(prepID);
                model.setExaminationTime(StringUtils.isEmpty(visit.getExaminationTime()) ? null : TextUtils.convertDate(visit.getExaminationTime(), FORMATDATE));
                model.setResultsID(visit.getResultsID());
                model.setTreatmentStatus(visit.getTreatmentStatus());
                model.setAppointmentTime(StringUtils.isEmpty(visit.getAppointmentTime()) ? null : TextUtils.convertDate(visit.getAppointmentTime(), FORMATDATE));
                model.setDaysReceived(StringUtils.isEmpty(visit.getDaysReceived()) ? Integer.valueOf("0") : Integer.valueOf(visit.getDaysReceived()));
                model.setTreatmentRegimen(visit.getTreatmentRegimen());

                model.setCreatinin(visit.getCreatinin());
                model.sethBsAg(visit.gethBsAg());
                model.setGanC(visit.getGanC());
                model.setGiangMai(visit.getGiangMai());
                model.setClamydia(visit.getClamydia());
                model.setLau(visit.getLau());
                model.setSot(visit.getSot());
                model.setMetMoi(visit.getMetMoi());
                model.setDauCo(visit.getDauCo());
                model.setPhatBan(visit.getPhatBan());
                model.setTuanThuDieuTri(visit.getTuanThuDieuTri());
                model.setTacDungPhu(visit.getTacDungPhu());

                list.add(model);
            }
        }
        return list;
    }

    private HashMap<String, List<String>> validateImport(PqmPrepTableImportForm form, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        if (!validateNull(form.getStartTime(), "Ngày bắt đầu điều trị không được để trống", errors)) {
            addExcelError("startTime", row, cols, style);
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            addExcelError("startTime", row, cols, style);
            errors.add("Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isBlank(form.getCode())) {
            addExcelError("code", row, cols, style);
            errors.add("Mã khách hàng không được để trống");
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && isThisDateValid(form.getStartTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getStartTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                addExcelError("startTime", row, cols, style);
                errors.add("Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }

        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            addExcelError("endTime", row, cols, style);
            errors.add("Ngày kết thúc dùng PrEP không đúng định dạng dd/mm/yyyy");
        }
        if (getCompeDate(form.getStartTime(), form.getEndTime())) {
            addExcelError("endTime", row, cols, style);
            errors.add("Ngày kết thúc phải lớn hơn ngày bắt đầu điều trị");
        }

        if (form.getVisits() != null && !form.getVisits().isEmpty()) {
            for (PqmPrepVisitTableImportForm visit : form.getVisits()) {
                if (StringUtils.isNotBlank(visit.getExaminationTime()) && !isThisDateValid(visit.getExaminationTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày KH đến khám không đúng định dạng dd/mm/yyyy");
                    if (visit.getTime().equals("0")) {
                        addExcelError("startTime", row, cols, style);
                    } else {
                        addExcelErrorByCol(row, visit.getFirstIndex(), style);
                    }
                }
                if (StringUtils.isNotBlank(visit.getExaminationTime()) && isThisDateValid(visit.getExaminationTime())) {
                    Date today = new Date();
                    Date time = TextUtils.convertDate(visit.getExaminationTime(), FORMATDATE);
                    if (time.compareTo(today) > 0) {
                        errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày KH đến khám không được sau ngày hiện tại");
                        if (visit.getTime().equals("0")) {
                            addExcelError("startTime", row, cols, style);
                        } else {
                            addExcelErrorByCol(row, visit.getFirstIndex(), style);
                        }
                    }
                }
                if (StringUtils.isNotBlank(visit.getAppointmentTime()) && !isThisDateValid(visit.getAppointmentTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày hẹn khám tiếp theo không đúng định dạng dd/mm/yyyy");
                    if (visit.getTime().equals("0")) {
                        addExcelError("appointmentTime", row, cols, style);
                    } else {
                        addExcelErrorByCol(row, visit.getFirstIndex() + 4, style);
                    }
                }
                if (getCompeDate(visit.getExaminationTime(), visit.getAppointmentTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày hẹn khám tiếp theo phải lớn hơn ngày KH đến khám");
                    if (visit.getTime().equals("0")) {
                        addExcelError("appointmentTime", row, cols, style);
                    } else {
                        addExcelErrorByCol(row, visit.getFirstIndex() + 4, style);
                    }
                }
                if (!StringUtils.isEmpty(visit.getDaysReceived())) {
                    try {
                        if (visit.getDaysReceived().contains(".") || visit.getDaysReceived().contains("-")) {
                            errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Số thuốc được phát phải là số nguyên dương");
                            addExcelErrorByCol(row, visit.getFirstIndex() + 3, style);
                        }
                        Integer.valueOf(visit.getDaysReceived());
                    } catch (Exception e) {
                        addExcelErrorByCol(row, visit.getFirstIndex() + 3, style);
                        errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Số thuốc được phát phải là số nguyên dương");
                    }
                }

            }
        }

        object.put("errors", errors);

        return object;
    }

    private HashMap<String, List<String>> validateImportSencont(PqmPrepTableImportForm form) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        validateNull(form.getStartTime(), "Ngày bắt đầu điều trị không được để trống", errors);
        if (StringUtils.isNotBlank(form.getStartTime()) && !isThisDateValid(form.getStartTime())) {
            errors.add("Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
        }
        if (StringUtils.isBlank(form.getCode())) {
            errors.add("Mã khách hàng không được để trống");
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && isThisDateValid(form.getStartTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getStartTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }

        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.add("Ngày kết thúc dùng PrEP không đúng định dạng dd/mm/yyyy");
        }
        if (getCompeDate(form.getStartTime(), form.getEndTime())) {
            errors.add("Ngày kết thúc phải lớn hơn ngày bắt đầu điều trị");
        }

        if (form.getVisits() != null && !form.getVisits().isEmpty()) {
            for (PqmPrepVisitTableImportForm visit : form.getVisits()) {
                if (StringUtils.isNotBlank(visit.getExaminationTime()) && !isThisDateValid(visit.getExaminationTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày KH đến khám không đúng định dạng dd/mm/yyyy");
                }
                if (StringUtils.isNotBlank(visit.getExaminationTime()) && isThisDateValid(visit.getExaminationTime())) {
                    Date today = new Date();
                    Date time = TextUtils.convertDate(visit.getExaminationTime(), FORMATDATE);
                    if (time.compareTo(today) > 0) {
                        errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày KH đến khám không được sau ngày hiện tại");
                    }
                }
                if (StringUtils.isNotBlank(visit.getAppointmentTime()) && !isThisDateValid(visit.getAppointmentTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày hẹn khám tiếp theo không đúng định dạng dd/mm/yyyy");
                }
                if (getCompeDate(visit.getExaminationTime(), visit.getAppointmentTime())) {
                    errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Ngày hẹn khám tiếp theo phải lớn hơn ngày KH đến khám");
                }
                if (!StringUtils.isEmpty(visit.getDaysReceived())) {
                    try {
                        if (visit.getDaysReceived().contains(".") || visit.getDaysReceived().contains("-")) {
                            errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Số thuốc được phát phải là số nguyên dương");
                        }
                        Integer.valueOf(visit.getDaysReceived());
                    } catch (Exception e) {
                        errors.add((visit.getTime().equals("0") ? " Lần khám đầu tiên " : "Kỳ tái khám " + visit.getTime() + ": ") + "Số thuốc được phát phải là số nguyên dương");
                    }
                }

            }
        }

        object.put("errors", errors);

        return object;
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

    private boolean validateNull(String text, String message, List<String> errors) {
        if (text == null || "".equals(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
            return false;
        }
        return true;
    }

    private String errorMsg(String attribute, String msg) {
        return String.format("<span> %s </span> <i class=\"text-danger\"> %s </i>", attribute, msg);
    }

    private String getType(String type) {
        if (type == null || "".equals(type)) {
            return "";
        } else if (type.contains("quay")) {
            return "2";
        } else if (type.contains("mới")) {
            return "1";
        } else {
            return "";
        }
    }

    private String getGender(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }
        if (param.contains("Nam")) {
            return "1";
        } else if (param.contains("Nữ")) {
            return "2";
        } else {
            return "3";
        }

    }

    private String getObjectGroup(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }

        if (param.contains("Nam QHTD đồng giới (MSM)")) {
            return "3";
        } else if (param.contains("Chuyển giới nữ")) {
            return "102";

        } else if (param.contains("Chuyển giới nam")) {
            return "102";

        } else if (param.contains("Tiêm chích ma túy")) {
            return "1";
        } else if (param.contains("Người bán dâm")) {
            return "2";

        } else if (param.contains("Bạn tình dị nhiễm")) {
            return "4";
        } else if (param.contains("Khác")) {
            return "7";
        }
        return "7";
    }

    private String getResultsID(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }
        if (param.contains("Dương")) {
            return PqmPrepResultEnum.DUONG_TINH.getKey();
        } else if (param.contains("Âm")) {
            return PqmPrepResultEnum.AM_TINH.getKey();
        } else if (param.contains("Không")) {
            return PqmPrepResultEnum.KHONG_RO.getKey();
        }
        return "";

    }

//    private String getTreatmentRegimen(String param) {
//        if (param == null || "".equals(param)) {
//            return "";
//        }
//        if (param.contains("hàng ngày")) {
//            return PqmPrepTreatmentEnum.HANG_NGAY.getKey();
//        } else if (param.contains("tình huống")) {
//            return PqmPrepTreatmentEnum.TINH_HUONG.getKey();
//        }
//        return "";
//
//    }
    private String getTreatmentStatus(String param) {
        if (param == null || "".equals(param)) {
            return "";
        }
        switch (param) {
            case "Tiếp tục sử dụng PrEP":
                return PqmPrepStatusEnum.TIEP_TUC.getKey();
            case "Dừng sử dụng PrEP do nhiễm mới HIV":
                return PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getKey();
            case "Dừng sử dụng PrEP do độc tính của thuốc":
                return PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getKey();
            case "Dừng sử dụng PrEP do khách hàng không còn nguy cơ":
                return PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getKey();
            case "Dừng do nguyên nhân khác (di chuyển nơi ở,...)":
                return PqmPrepStatusEnum.DUNG_DO_KHAC.getKey();
            case "Mất dấu khách hàng (trong vòng 30 ngày kể từ ngày hẹn tái khám)":
                return PqmPrepStatusEnum.MAT_DAU.getKey();
            default:
                return "";
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

    private PqmPrepTableImportForm mapDataToForm(PqmPrepImportForm item, LinkedList<PqmPrepImportForm> visits) {
        PqmPrepTableImportForm model = new PqmPrepTableImportForm();
        PqmPrepVisitTableImportForm visit;
        List<PqmPrepVisitTableImportForm> listVisit = new ArrayList<>();

        //Trường cơ bản prep
        model.setCode(item.getCode().toUpperCase());
        model.setType(getType(item.getType()));
        model.setGenderID(getGender(item.getGenderID()));
        model.setYearOfBirth(item.getYearOfBirth());
        model.setObjectGroupID(getObjectGroup(item.getObjectGroupID()));
        model.setIdentityCard(item.getIdentityCard());
        model.setStartTime(item.getStartTime());
        model.setEndTime(item.getEndTime());
        model.setEndCause(item.getEndCause());
        model.setFullName(item.getFullName());
        model.setPatientPhone(item.getPatientPhone());

        model.setSource(item.getSource());
        model.setOtherSite(item.getOtherSite());
        model.setProjectSupport(item.getProjectSupport());

        //Các lượt khám
        visit = new PqmPrepVisitTableImportForm();
        visit.setTime("0");
        visit.setExaminationTime(item.getStartTime());
//        visit.setTreatmentRegimen(getTreatmentRegimen(item.getTreatmentRegimen()));
        visit.setTreatmentRegimen(item.getTreatmentRegimen());
        visit.setDaysReceived(getEmty(item.getDaysReceived()));
        visit.setAppointmentTime(item.getAppointmentTime());
        visit.setResultsID(getResultsID(item.getResultsID()));

        visit.setCreatinin(item.getCreatinin());
        visit.sethBsAg(item.gethBsAg());
        visit.setGanC(item.getGanC());
        visit.setGiangMai(item.getGiangMai());
        visit.setClamydia(item.getClamydia());
        visit.setLau(item.getLau());
        visit.setSot(item.getSot());
        visit.setMetMoi(item.getMetMoi());
        visit.setDauCo(item.getDauCo());
        visit.setPhatBan(item.getPhatBan());

        if (StringUtils.isNotBlank(visit.getExaminationTime())) {
            listVisit.add(visit);
        }
        if (visits != null) {
            int i = 1;
            for (PqmPrepImportForm visit1 : visits) {
                visit = new PqmPrepVisitTableImportForm();
                visit.setFirstIndex(visit1.getFirstIndex());
                visit.setTime(String.valueOf(i++));
                visit.setExaminationTime(visit1.getExaminationTime1());
                visit.setResultsID(getResultsID(visit1.getResultsID1()));
                visit.setTreatmentStatus(visit1.getTreatmentStatus1());
//                visit.setTreatmentRegimen(getTreatmentRegimen(visit1.getTreatmentRegimen1()));
                visit.setTreatmentRegimen(visit1.getTreatmentRegimen1());
                visit.setDaysReceived(getEmty(visit1.getDaysReceived1()));
                visit.setAppointmentTime(visit1.getAppointmentTime1());

                visit.setCreatinin(visit1.getCreatinin1());
                visit.sethBsAg(visit1.gethBsAg1());
                visit.setGanC(visit1.getGanC1());
                visit.setGiangMai(visit1.getGiangMai1());
                visit.setClamydia(visit1.getClamydia1());
                visit.setLau(visit1.getLau1());
                visit.setSot(visit1.getSot1());
                visit.setMetMoi(visit1.getMetMoi1());
                visit.setDauCo(visit1.getDauCo1());
                visit.setPhatBan(visit1.getPhatBan1());

                visit.setTuanThuDieuTri(visit1.getTuanThuDieuTri1());
                visit.setTacDungPhu(visit1.getTacDungPhu1());

                if (StringUtils.isNotBlank(visit1.getExaminationTime1())) {
                    listVisit.add(visit);
                }
            }
        }

        model.setVisits(listVisit);

        return model;
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null || dateToValidate.length() < 10) {
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
