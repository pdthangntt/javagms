package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.PqmDrugNewImportForm;
import com.gms.service.PqmDrugNewDataService;
import com.gms.service.PqmDrugNewService;
import com.gms.service.PqmLogService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
@Controller(value = "importation_pqm_drug_new")
public class PqmDrugNewController extends BaseController<PqmDrugNewImportForm> {

    private static int firstRow = 11;
    private static String textTitle = "";

    @Autowired
    private PqmDrugNewService drugNewService;
    @Autowired
    private PqmDrugNewDataService drugNewDataService;
    @Autowired
    private PqmLogService logService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/drug-new/import.html");
        form.setSmallUrl("/report/drug-new/index.html");
        form.setReadUrl("/drug-new/import.html");
        form.setTitle("Nhập dữ liệu tình hình sử dụng và tồn kho thuốc ARV (HMED)");
        form.setSmallTitle("Quản lý thuốc ARV");
        form.setTemplate(fileTemplate("TinhHinhSuDung_TonKho.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "stt");
        cols.put("1", "source");
        cols.put("2", "");
        cols.put("3", "unit");
        cols.put("4", "");
        cols.put("5", "");
        cols.put("6", "");
        cols.put("7", "tdk");
        cols.put("8", "ndk");
        cols.put("9", "nk");
        cols.put("10", "xcbntk");
        cols.put("11", "xdctk");
        cols.put("12", "hh");
        cols.put("13", "tck");

        return cols;
    }

    @GetMapping(value = {"/drug-new/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/drug-new/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();

        LoggedUser currentUser = getCurrentUser();

        ImportForm form = initForm();
        int index = 0;
        try {
            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            try {
                form.setData(readFileFormattedCell(workbook, 0, model, -1));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            List<PqmDrugNewImportForm> items = new LinkedList<>();
            String year = "";
            String month = "";
            String text = "";
            boolean flagCheckDate = false;
            boolean checkSameTime = false;
            boolean isTemplateTrue = false;
            //lọc dòng tên thuốc
            List<Integer> keys = new LinkedList<>();
            List<Integer> keyends = new LinkedList<>();
            //Map index - tên thuốc
            Map<Integer, String> drugNames = new HashMap<>();

            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (index == 9) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("STT"))
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Nguồn thuốc"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Quy cách đóng gói"))) {
                        isTemplateTrue = true;
                    }
                }
                if (index == 7) {
                    try {
                        String time = row.getValue().get(0);
                        text = time == null ? null : time.substring(time.length() - 8);
                        text = text == null ? null : text.substring(0, 7);
                        month = text == null ? null : text.substring(0, 2);
                        year = text == null ? null : text.substring(text.length() - 4);
                        Integer.valueOf(year);
                        Integer.valueOf(month);

                        String firstTime = row.getValue().get(0);
                        String text2 = firstTime == null ? null : firstTime.substring(13, 21).trim();

                        String day1 = "01/" + text;
                        String day2 = "01/" + text2;

                        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                        if (isThisDateValid(day1) && isThisDateValid(day2)) {
                            Date timesd = TextUtils.convertDate("01/" + (Integer.valueOf(month) < 10 ? "0" + Integer.valueOf(month) : Integer.valueOf(month)) + "/" + year, FORMATDATE);
                            Date first = TextUtils.getFirstDayOfQuarter(TextUtils.getQuarter(timesd), Integer.valueOf(year));
                            Date last = TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(timesd), Integer.valueOf(year));
                            String firstDate = TextUtils.formatDate(first, FORMATDATE);
                            String lastDate = TextUtils.formatDate(last, FORMATDATE);

                            Date date1 = sdfrmt.parse(day1);
                            Date date2 = sdfrmt.parse(day2);

                            Date firstDayOfQuarter = sdfrmt.parse(firstDate);
                            Date lastDayOfQuarter = sdfrmt.parse(lastDate);

                            if (date1.compareTo(firstDayOfQuarter) < 0 || date1.compareTo(lastDayOfQuarter) > 0) {
                                checkSameTime = true;
                            }
                            if (date2.compareTo(firstDayOfQuarter) < 0 || date2.compareTo(lastDayOfQuarter) > 0) {
                                checkSameTime = true;
                            }

                        } else {
                            flagCheckDate = true;
                        }

                    } catch (Exception e) {
                        flagCheckDate = true;
                    }
                }
                //Xử lý lấy index
                if (index > 11) {
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0))
                            && StringUtils.isEmpty(row.getValue().get(1))
                            && StringUtils.isEmpty(row.getValue().get(2))
                            && StringUtils.isEmpty(row.getValue().get(3))) {
                        keys.add(index);
                        drugNames.put(index, row.getValue().get(0));
                    }
                    if (row.getValue() == null || row.getValue().isEmpty()
                            || (StringUtils.isEmpty(row.getValue().get(0))
                            && StringUtils.isEmpty(row.getValue().get(1))
                            && StringUtils.isEmpty(row.getValue().get(2))
                            && StringUtils.isEmpty(row.getValue().get(3)))) {
                        if (!keys.isEmpty() && (keys.size() > keyends.size())) {
                            keyends.add(index);
                        }
                    }

                }
            }
            //Map bắt đầu - kết thúc của 1 loại thuốc
            Map<Integer, Integer> mapKeyends = new HashMap<>();

            for (int i = 0; i < keys.size(); i++) {
                mapKeyends.put(keys.get(i), keyends.get(i));
            }

            if (!isTemplateTrue) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }
            if (month == null || year == null || !isThisDateValid("01/" + text)) {
                redirectAttributes.addFlashAttribute("error", "Không lấy được tháng, năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }
            if (flagCheckDate) {
                redirectAttributes.addFlashAttribute("error", "Không đọc được tháng, năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }
            if (checkSameTime) {
                redirectAttributes.addFlashAttribute("error", "Dữ liệu nhập không trong cùng 1 quý. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }
            String currentDate = TextUtils.formatDate(new Date(), "yyyyMM");
            String bcDate = TextUtils.formatDate(TextUtils.convertDate("15/" + text, FORMATDATE), "yyyyMM");

            System.out.println("currentDate " + Long.valueOf(currentDate));
            System.out.println("bcDate " + Long.valueOf(bcDate));
            if (Long.valueOf(bcDate) > Long.valueOf(currentDate)) {
                redirectAttributes.addFlashAttribute("error", "Dữ liệu nhập vào thuộc tháng lớn hơn tháng hiện tại. Vui lòng xem lại file mẫu. ");
                return redirect(form.getUploadUrl());
            }

            Map<Integer, List<String>> data = form.getData();
            List<PqmDrugNewImportForm> data2 = new LinkedList<>();
            PqmDrugNewImportForm item1;
            for (Map.Entry<Integer, Integer> entry : mapKeyends.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                String name = data.get(key - 1).get(0);
                for (int i = key + 1; i < value; i++) {
                    item1 = mapping(cols(), data.get(i - 1));
                    item1.setDrugName(name.substring(2).trim());
                    item1.setStt(i);//set dòng excel báo lỗi
                    data2.add(item1);
                }

            }

            //validate
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            int success = 0;
            //tạm
            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();
            Set<PqmDrugNewImportForm> datas = new HashSet<>();

            for (PqmDrugNewImportForm item : data2) {
                Row row = sheet.getRow(item.getStt() - 1);
                HashMap<String, List< String>> object = validateCustom(item, row, style, cols);
                if (object.get("errors").isEmpty()) {
                    datas.add(item);
                } else {
                    errors.put(String.valueOf(item.getStt()), object.get("errors"));
                }

            }
            //gôp các bản ghi cùng tên thuốc, nguồn
            List<PqmDrugNewImportForm> lastItems = new ArrayList<>();
            if (errors.isEmpty()) {
                Map<String, Set<PqmDrugNewImportForm>> mapDatas = new HashMap<>();
                for (PqmDrugNewImportForm data1 : datas) {
                    String kd = data1.getDrugName() + "keyssfdsf" + data1.getSource();
                    kd = kd.trim();
                    if (mapDatas.getOrDefault(kd, null) != null) {
                        mapDatas.get(kd).add(data1);
                    } else {
                        mapDatas.put(kd, new HashSet<PqmDrugNewImportForm>());
                        mapDatas.get(kd).add(data1);
                    }
                }
                for (Map.Entry<String, Set<PqmDrugNewImportForm>> entry : mapDatas.entrySet()) {
                    String key = entry.getKey();
                    Set<PqmDrugNewImportForm> value = entry.getValue();
                    item1 = new PqmDrugNewImportForm();
                    for (PqmDrugNewImportForm p : value) {
                        //
                        item1.setProvinceID(currentUser.getSite().getProvinceID());
                        item1.setSiteID(currentUser.getSite().getID().toString());
                        item1.setYear(Integer.valueOf(year));
                        item1.setMonth(Integer.valueOf(month));

                        item1.setDrugName(p.getDrugName().trim());
                        item1.setSource(p.getSource());
                        item1.setUnit(p.getUnit());
                        item1.setTdk(toNum(item1.getTdk(), p.getTdk()));
                        item1.setNdk(toNum(item1.getNdk(), p.getNdk()));
                        item1.setNk(toNum(item1.getNk(), p.getNk()));
                        item1.setXcbntk(toNum(item1.getXcbntk(), p.getXcbntk()));
                        item1.setXdctk(toNum(item1.getXdctk(), p.getXdctk()));
                        item1.setHh(toNum(item1.getHh(), p.getHh()));
                        item1.setTck(toNum(item1.getTck(), p.getTck()));
                    }
                    lastItems.add(item1);
                }
            }
            //set entity
            List<PqmDrugNewEntity> list = new ArrayList<>();
            for (PqmDrugNewImportForm lastItem : lastItems) {
                PqmDrugNewEntity e = toEntity(lastItem);
                list.add(e);
            }
            //save

            if (errors.isEmpty()) {

                Date timeas = TextUtils.convertDate("01/" + (Integer.valueOf(month) < 10 ? "0" + Integer.valueOf(month) : Integer.valueOf(month)) + "/" + year, FORMATDATE);

                drugNewService.deleteBySiteIDAndMonthAndYear(currentUser.getSite().getID(), TextUtils.getQuarter(timeas) + 1, Integer.valueOf(year));
                for (PqmDrugNewEntity itema : list) {
                    try {
                        drugNewService.save(itema);
                        success += 1;
                    } catch (Exception e) {
                    }
                }
            }
            List<Integer> errorIDs = new ArrayList<>();
            for (Map.Entry<String, List< String>> entry : errors.entrySet()) {
                String key = entry.getKey();
                errorIDs.add(Integer.valueOf(key));
            }
            Collections.sort(errorIDs);
            HashMap<String, List< String>> errorsw = new LinkedHashMap<>();
            for (Integer errorID : errorIDs) {
                errorsw.put(errorID.toString(), errors.getOrDefault(errorID.toString(), null));
            }

            model.addAttribute("filePath", saveFile(workbook, file));
            model.addAttribute("errorsw", errorsw);
            model.addAttribute("errorSize", errors.size());
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            model.addAttribute("form", form);
            if (errors.isEmpty()) {
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                model.addAttribute("ok", true); 
                
                logService.log("Tải file excel nhập BC tình hình sử dụng và tồn kho thuốc ARV (HMED)", items.size(), success, items.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở" , getCurrentUser().getSite().getID(), "Tình hình sử dụng và tồn kho thuốc ARV (HMED)");
                
                return "importation/pqm/drug_new";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/drug_new";
            }
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private String toNum(String st1, String st2) {
        Long text = Long.valueOf(0);
        if (StringUtils.isEmpty(st1)) {
            st1 = "0";
        }
        if (StringUtils.isEmpty(st2)) {
            st2 = "0";
        }
        text = Long.valueOf(st1) + Long.valueOf(st2);
        return text.toString();
    }

    private PqmDrugNewEntity toEntity(PqmDrugNewImportForm item) {

        PqmDrugNewEntity model = new PqmDrugNewEntity();

        model.setProvinceID(item.getProvinceID());
        model.setSiteID(Long.valueOf(item.getSiteID()));

        Date time = TextUtils.convertDate("01/" + (item.getMonth() < 10 ? "0" + item.getMonth() : item.getMonth()) + "/" + item.getYear(), FORMATDATE);

        model.setYear(item.getYear());
        model.setQuarter(TextUtils.getQuarter(time) + 1);

        model.setDrugName(item.getDrugName());
        model.setSource(item.getSource());
        model.setUnit(item.getUnit());
        model.setTdk(Long.valueOf(item.getTdk()));
        model.setNdk(Long.valueOf(item.getNdk()));
        model.setNk(Long.valueOf(item.getNk()));
        model.setXcbntk(Long.valueOf(item.getXcbntk()));
        model.setXdctk(Long.valueOf(item.getXdctk()));
        model.setHh(Long.valueOf(item.getHh()));
        model.setTck(Long.valueOf(item.getTck()));

        return model;
    }

    private HashMap<String, List<String>> validateCustom(PqmDrugNewImportForm item, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        validateNull(item.getDrugName(), "Tên thuốc", errors, "drugName", row, style, cols);
        validateNull(item.getSource(), "Nguồn thuốc", errors, "source", row, style, cols);
        validateNull(item.getUnit(), "Đơn vị tính", errors, "unit", row, style, cols);

        if (!checkNumber(item.getTdk())) {
            errors.add("Tồn đầu kỳ chỉ được nhập số nguyên dương");
            addExcelError("tdk", row, cols, style);
        }
        if (!checkNumber(item.getNdk())) {
            errors.add("Nhập định kỳ chỉ được nhập số nguyên dương");
            addExcelError("ndk", row, cols, style);
        }
        if (!checkNumber(item.getNk())) {
            errors.add("Nhập khác chỉ được nhập số nguyên dương");
            addExcelError("nk", row, cols, style);
        }
        if (!checkNumber(item.getXcbntk())) {
            errors.add("Xuất cho bệnh nhân trong kỳ chỉ được nhập số nguyên dương");
            addExcelError("xcbntk", row, cols, style);
        }
        if (!checkNumber(item.getXdctk())) {
            errors.add("Xuất điều chuyển trong kỳ chỉ được nhập số nguyên dương");
            addExcelError("xdctk", row, cols, style);
        }
        if (!checkNumber(item.getHh())) {
            errors.add("Hư hao chỉ được nhập số nguyên dương");
            addExcelError("hh", row, cols, style);
        }
        if (!checkNumber(item.getTck())) {
            errors.add("Tồn cuối kỳ chỉ được nhập số nguyên dương");
            addExcelError("tck", row, cols, style);
        }
        object.put("errors", errors);

        return object;
    }

    private boolean checkNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return true;
        }
        try {
            Integer.valueOf(number);
            if (Integer.valueOf(number) < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private void validateNull(String text, String message, List<String> errors, String colName, Row row, CellStyle style, Map<String, String> cols) {
        if (StringUtils.isBlank(text)) {
            errors.add(String.format("<span> %s </span> <i class=\"text-danger\"> không được để trống </i>", message));
            addExcelError(colName, row, cols, style);
        }

    }

    @Override
    public PqmDrugNewImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmDrugNewImportForm item = new PqmDrugNewImportForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    case "tck":
                        String text = "";
                        if (StringUtils.isNotEmpty(cells.get(i))) {
                            if (cells.get(i).contains(".0")) {
                                text = cells.get(i).replace(".0", "");
                            } else {
                                text = cells.get(i).toString();
                            }
                        }
                        item.setTck(text);
                        break;
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
//                getLogger().warn(ex.getMessage());
            }
        }

        return item;
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

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

}
