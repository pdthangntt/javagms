package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.PqmDrugEstimateImportForm;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmLogService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 *
 * @author pdThang
 */
@Controller(value = "importation_pqm_drug_estimate")
public class PqmDrugEstimateController extends BaseController<PqmDrugEstimateImportForm> {

    private static int firstRow = 11;
    private static String textTitle = "";

    @Autowired
    private PqmDrugEstimateService drugEstimateService;
    @Autowired
    private PqmLogService logService;
    @Autowired
    private PqmDrugPlanService drugPlanService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());

        }

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/pqm-drug-estimate/import.html");
        form.setSmallUrl("/backend/pqm-drug-estimate/index.html");
        form.setReadUrl("/pqm-drug-estimate/import.html");
        form.setTitle("Nhập dữ liệu kế hoạch cung ứng thuốc ARV");
        form.setSmallTitle("Quản lý thuốc ARV");
        form.setTemplate(fileTemplate("KeHoachDuTruThuoc.xls"));
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
        cols.put("1", "provinceID");
        cols.put("2", "siteCode");
        cols.put("3", "siteName");
        cols.put("4", "source");
        cols.put("5", "drugName");
        cols.put("6", "packing");
        cols.put("7", "unit");
        cols.put("8", "lotNumber");
        cols.put("9", "expiryDate");
        cols.put("10", "beginning");
        cols.put("11", "inThePeriod");
        cols.put("12", "patient");
        cols.put("13", "transfer");
        cols.put("14", "loss");
        cols.put("15", "ending");

        return cols;
    }

    @GetMapping(value = {"/pqm-drug-estimate/import.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/pqm-drug-estimate/import.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        int index = 0;
        try {

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            try {
                form.setData(readFileFormattedCell(workbook, 2, model, -1));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            //Sheet danh mục cơ sở
            Map<String, String> siteMap = new HashMap<>();
            Map<Integer, List<String>> dataSheet2 = readFile(workbook, 1, model);
            int indexSite = 0;
            for (Map.Entry<Integer, List<String>> entry : dataSheet2.entrySet()) {
                indexSite++;
                List<String> row = entry.getValue();
                if (indexSite < 11) {
                    continue;
                }
                if (row == null || row.isEmpty() || StringUtils.isEmpty(row.get(4)) || StringUtils.isEmpty(row.get(5))) {
                    continue;
                }
                siteMap.put(convertText(row.get(4)), row.get(5));

            }

            String year = "";
            Map<String, LinkedList<PqmDrugEstimateImportForm>> map = new HashMap<>();
            Map<String, PqmDrugEstimateImportForm> mapData = new HashMap<>();
            map.put("BHYT", new LinkedList<PqmDrugEstimateImportForm>());
            map.put("NSNN", new LinkedList<PqmDrugEstimateImportForm>());
            map.put("VT", new LinkedList<PqmDrugEstimateImportForm>());
            int BHYT = 0;
            boolean isBHYT = true;//dánh dấu kết thúc ko lấy bản ghi trống
            boolean isNSNN = true;
            int NSNN = 0;
            int VT = 0;

            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {

                if (row.getKey() == 9) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(2));
                    System.out.println("333 " + row.getValue().get(0));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(0)) && convertText(row.getValue().get(0)).equals(convertText("STT"))
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Đơn vị"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Phác đồ Điều trị"))) {
                        ok = true;
                    }
                }
                index++;
                if (index < 6 || row.getValue() == null || row.getValue().isEmpty()) {
                    continue;
                }
                if (index == 6) {
                    try {
                        String time = row.getValue().get(2);
                        String text = time == null ? null : time.substring(time.length() - 5);
                        text = time == null ? null : time.replaceAll("\\)", "");
                        year = text == null ? null : text.substring(text.length() - 4);
                        Integer.valueOf(year);
                        if (year == null || !isThisDateValid("01/01/" + year)) {
                            redirectAttributes.addFlashAttribute("error", "Không lấy được năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                            return redirect(form.getUploadUrl());
                        }
                        System.out.println("year: " + year);

                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error", "Không đọc được tháng, năm từ tệp excel. Vui lòng xem lại file mẫu. ");
                        return redirect(form.getUploadUrl());
                    }

                }
                try {

                    String row1 = row.getValue().get(0);
                    String row2 = row.getValue().get(1);
                    if (StringUtils.isNotEmpty(row1) && StringUtils.isNotEmpty(row2) && row1.equals("II.") && row2.contains("Ước tính nhu cầu thuốc ARV nguồn BHYT")) {
                        BHYT = index;

                    } else if (StringUtils.isNotEmpty(row1) && StringUtils.isNotEmpty(row2) && row1.equals("III.") && row2.contains("Ước tính nhu cầu thuốc ARV nguồn NSNN")) {
                        NSNN = index;
                    } else if (StringUtils.isNotEmpty(row1) && StringUtils.isNotEmpty(row2) && row1.equals("IV.") && row2.contains("Ước tính nhu cầu thuốc ARV nguồn viện trợ")) {
                        VT = index;
                    } else {
                    }
                    if (BHYT > NSNN) {
                        PqmDrugEstimateImportForm item = mapping(row.getValue());
                        item.setIndex(index);
                        item.setYear(year);
                        item.setSource("BHYT");
                        if (item.getSiteName() != null && item.getSiteName().equals("0")) {
                            isBHYT = false;//dánh dấu kết thúc
                            continue;
                        }
                        if (isBHYT) {
                            item.setKey(row.getKey());
                            map.get("BHYT").add(item);
                        }
                    }
                    if (NSNN > VT) {
                        PqmDrugEstimateImportForm item = mapping(row.getValue());
                        item.setIndex(index);
                        item.setYear(year);
                        item.setSource("NSNN");
                        if (item.getSiteName() != null && item.getSiteName().equals("0")) {
                            isNSNN = false;
                            continue;
                        }
                        if (isNSNN) {
                            item.setKey(row.getKey());
                            map.get("NSNN").add(item);
                        }
                    }
                    if (VT > 0) {
                        PqmDrugEstimateImportForm item = mapping(row.getValue());
                        item.setIndex(index);
                        item.setYear(year);
                        item.setSource("Viện trợ");
                        if (item.getSiteName() != null && item.getSiteName().equals("0")) {
                            break;
                        }
                        item.setKey(row.getKey());
                        map.get("VT").add(item);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            }

            System.out.println("index:BHYT: " + BHYT + " iNSNN: " + NSNN + " iVT: " + VT + " site " + siteMap);
            HashMap<String, List< String>> errors = new LinkedHashMap<>();
            List<PqmDrugEstimateImportForm> items = new LinkedList<>();
            List<PqmDrugEstimateImportForm> itemBHYT = new LinkedList<>();
            List<PqmDrugEstimateImportForm> itemNSNN = new LinkedList<>();
            List<PqmDrugEstimateImportForm> itemVT = new LinkedList<>();

            Map<String, PqmDrugEstimateImportForm> mapNSNN = new HashMap<>();
            String keyNSNN = "";
            int indexNSNN = 0;
            for (PqmDrugEstimateImportForm row : map.get("NSNN")) {
                indexNSNN++;
                if (indexNSNN < 5) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && (row.getSiteName().equals("Đơn vị") || row.getSiteName().equals("(b)"))) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && row.getDrugName().equals("0")) {
                    break;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && !row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && !row.getDrugName().equals("0")) {
                    keyNSNN = row.getSiteName() + row.getDrugName();
                    if (mapNSNN.getOrDefault(keyNSNN, null) == null) {
                        List<String> lastItem = form.getData().get(row.getKey() + 7);
                        List<String> lastItem2 = form.getData().get(row.getKey() + 8);
                        if (lastItem.get(9) != null && lastItem.get(9).equals("Tổng số")) {
                            row.setExigency0(lastItem.get(10));
                            row.setExigency1(lastItem.get(11));
                            row.setExigency2(lastItem.get(12));
                            row.setExigency3(lastItem.get(13));
                            row.setExigencyRow(row.getKey() + 7);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem.get(10) + " 11 " + lastItem.get(11) + " 12 " + lastItem.get(12));
                        } else {
                            row.setExigency0(lastItem2.get(10));
                            row.setExigency1(lastItem2.get(11));
                            row.setExigency2(lastItem2.get(12));
                            row.setExigency3(lastItem2.get(13));
                            row.setExigencyRow(row.getKey() + 8);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem2.get(10) + " 11 " + lastItem2.get(11) + " 12 " + lastItem2.get(12));
                        }

                        mapNSNN.put(keyNSNN, row);
                    }
                }
//                if (StringUtils.isNotEmpty(keyNSNN) && StringUtils.isEmpty(row.getSiteName()) && StringUtils.isEmpty(row.getDrugName())) {
//                    mapNSNN.get(keyNSNN).setExigency0(row.getExigency0());
//                    mapNSNN.get(keyNSNN).setExigency1(row.getExigency1());
//                    mapNSNN.get(keyNSNN).setExigency2(row.getExigency2());
//                    mapNSNN.get(keyNSNN).setExigency3(row.getExigency3());
//
//                    mapNSNN.get(keyNSNN).setKey(row.getKey());
//                }
            }
            for (Map.Entry<String, PqmDrugEstimateImportForm> entry : mapNSNN.entrySet()) {
                PqmDrugEstimateImportForm value = entry.getValue();
                itemNSNN.add(value);
            }

            Map<String, PqmDrugEstimateImportForm> mapBHYT = new HashMap<>();
            String keyBHYT = "";
            int indexBHYT = 0;
            for (PqmDrugEstimateImportForm row : map.get("BHYT")) {
                indexBHYT++;
                if (indexBHYT < 5) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && (row.getSiteName().equals("Đơn vị") || row.getSiteName().equals("(b)"))) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && row.getDrugName().equals("0")) {
                    break;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && !row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && !row.getDrugName().equals("0")) {
                    keyBHYT = row.getSiteName() + row.getDrugName();
                    if (mapBHYT.getOrDefault(keyBHYT, null) == null) {
                        List<String> lastItem = form.getData().get(row.getKey() + 7);
                        List<String> lastItem2 = form.getData().get(row.getKey() + 8);
                        if (lastItem.get(9) != null && lastItem.get(9).equals("Tổng số")) {
                            row.setExigency0(lastItem.get(10));
                            row.setExigency1(lastItem.get(11));
                            row.setExigency2(lastItem.get(12));
                            row.setExigency3(lastItem.get(13));
                            row.setExigencyRow(row.getKey() + 7);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem.get(10) + " 11 " + lastItem.get(11) + " 12 " + lastItem.get(12));
                        } else {
                            row.setExigency0(lastItem2.get(10));
                            row.setExigency1(lastItem2.get(11));
                            row.setExigency2(lastItem2.get(12));
                            row.setExigency3(lastItem2.get(13));
                            row.setExigencyRow(row.getKey() + 8);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem2.get(10) + " 11 " + lastItem2.get(11) + " 12 " + lastItem2.get(12));
                        }

                        mapBHYT.put(keyBHYT, row);
                    }
                }
//                if (StringUtils.isNotEmpty(keyBHYT) && StringUtils.isEmpty(row.getSiteName()) && StringUtils.isEmpty(row.getDrugName())) {
//                    mapBHYT.get(keyBHYT).setExigency0(row.getExigency0());
//                    mapBHYT.get(keyBHYT).setExigency1(row.getExigency1());
//                    mapBHYT.get(keyBHYT).setExigency2(row.getExigency2());
//                    mapBHYT.get(keyBHYT).setExigency3(row.getExigency3());
//                    mapBHYT.get(keyBHYT).setKey(row.getKey());
//                }
            }
            for (Map.Entry<String, PqmDrugEstimateImportForm> entry : mapBHYT.entrySet()) {
                PqmDrugEstimateImportForm value = entry.getValue();
                itemBHYT.add(value);
            }

            Map<String, PqmDrugEstimateImportForm> mapVT = new HashMap<>();
            String key = "";
            int indexVT = 0;
            for (PqmDrugEstimateImportForm row : map.get("VT")) {
                indexVT++;
                if (indexVT < 5) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && (row.getSiteName().equals("Đơn vị") || row.getSiteName().equals("(b)"))) {
                    continue;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && row.getDrugName().equals("0")) {
                    break;
                }
                if (StringUtils.isNotEmpty(row.getSiteName()) && !row.getSiteName().equals("0")
                        && StringUtils.isNotEmpty(row.getDrugName()) && !row.getDrugName().equals("0")) {
                    key = row.getSiteName() + row.getDrugName();
                    if (mapVT.getOrDefault(key, null) == null) {
                        List<String> lastItem = form.getData().get(row.getKey() + 7);
                        List<String> lastItem2 = form.getData().get(row.getKey() + 8);
                        if (lastItem.get(9) != null && lastItem.get(9).equals("Tổng số")) {
                            row.setExigency0(lastItem.get(10));
                            row.setExigency1(lastItem.get(11));
                            row.setExigency2(lastItem.get(12));
                            row.setExigency3(lastItem.get(13));
                            row.setExigencyRow(row.getKey() + 7);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem.get(10) + " 11 " + lastItem.get(11) + " 12 " + lastItem.get(12));
                        } else {
                            row.setExigency0(lastItem2.get(10));
                            row.setExigency1(lastItem2.get(11));
                            row.setExigency2(lastItem2.get(12));
                            row.setExigency3(lastItem2.get(13));
                            row.setExigencyRow(row.getKey() + 8);
                            System.out.println("r " + row.getKey() + " 10 " + lastItem2.get(10) + " 11 " + lastItem2.get(11) + " 12 " + lastItem2.get(12));
                        }

                        mapVT.put(key, row);
                    }

                }
//                if (StringUtils.isNotEmpty(key) && StringUtils.isEmpty(row.getSiteName()) && StringUtils.isEmpty(row.getDrugName())) {
//                    mapVT.get(key).setExigency0(row.getExigency0());
//                    mapVT.get(key).setExigency1(row.getExigency1());
//                    mapVT.get(key).setExigency2(row.getExigency2());
//                    mapVT.get(key).setExigency3(row.getExigency3());
//                    mapVT.get(key).setKey(row.getKey());
//                }
            }
            for (Map.Entry<String, PqmDrugEstimateImportForm> entry : mapVT.entrySet()) {
                PqmDrugEstimateImportForm value = entry.getValue();
                itemVT.add(value);
            }
            int success = 0;

            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(2);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();
            Set<PqmDrugEstimateEntity> datas = new HashSet<>();
            int i = 0;
            items.addAll(itemBHYT);
            items.addAll(itemNSNN);
            items.addAll(itemVT);
            for (PqmDrugEstimateImportForm item : items) {
                i = item.getIndex();
                Row row = sheet.getRow(i - 1);
                Row row2 = sheet.getRow(item.getExigencyRow());
                System.out.println("dd " + item.getKey());

                HashMap<String, List< String>> object = validateCustom(getSiteCode(item, siteMap), row, style, cols, row2);
                if (object.get("errors").isEmpty()) {
                    PqmDrugEstimateEntity entity = toEntity(item);
                    try {
                        datas.add(entity);
                        success += 1;
                    } catch (Exception e) {
                        object.put("errors", new ArrayList<String>());
                        object.get("errors").add("Đã xảy ra lỗi với bản ghi, vui lòng kiểm tra lại!");
                        errors.put(String.valueOf(i), object.get("errors"));
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
            model.addAttribute("year", year);
            model.addAttribute("form", form);
            if (errors.isEmpty()) {
                for (PqmDrugEstimateEntity data : datas) {
                    List<PqmDrugPlanEntity> plans = drugPlanService.findUpdateUnitToEstimate(data.getProvinceID(), data.getDrugName(), data.getSiteCode(), Integer.valueOf(year));
                    try {
                        if (StringUtils.isEmpty(data.getUnit()) || data.getUnit().equals("Không rõ")) {
                            data.setUnit((plans == null || plans.isEmpty() || plans.get(0) == null || StringUtils.isEmpty(plans.get(0).getUnit())) ? "Không rõ" : plans.get(0).getUnit());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        data.setUnit("Không rõ");
                    }
                    data = drugEstimateService.save(data);
                }
                logService.log("Tải file excel nhập DL kế hoạch cung ứng thuốc", items.size(), success, items.size() - success, "Tuyến tỉnh", getCurrentUser().getSite().getID(), "Kế hoạch cung ứng thuốc ARV");
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                model.addAttribute("ok", true);
                return "importation/pqm/drug";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/pqm/drug";
            }
//            return "importation/pqm/drug";
        } catch (Exception ex) {
            if (ex.getMessage().contains("Sheet index")) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
                return redirect(form.getUploadUrl());
            } else {
                redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
                return redirect(form.getUploadUrl());
            }
        }
    }

    private PqmDrugEstimateEntity toEntity(PqmDrugEstimateImportForm item) {
        String provinceID = getCurrentUser().getSite().getProvinceID();
        PqmDrugEstimateEntity model = drugEstimateService.findByUniqueConstraints(Integer.valueOf(item.getYear()), provinceID, item.getDrugName(), item.getSiteCode(), item.getSource());
        if (model == null) {
            model = new PqmDrugEstimateEntity();
        }
        model.setYear(Integer.valueOf(item.getYear()));
        model.setProvinceID(provinceID);
        model.setSiteCode(item.getSiteCode());
        model.setSiteName(item.getSiteName());
        model.setSource(item.getSource());
        model.setDrugName(item.getDrugName());
        model.setPacking(StringUtils.isEmpty(item.getPacking()) ? "" : item.getPacking());
        model.setHowToUse(item.getHowToUse());

        model.setEarlyEstimate(StringUtils.isEmpty(item.getEarlyEstimate()) ? Long.valueOf(0) : Long.valueOf(item.getEarlyEstimate()));
        model.setInEstimate(StringUtils.isEmpty(item.getInEstimate()) ? Long.valueOf(0) : Long.valueOf(item.getInEstimate()));
        model.setFinalEstimate(StringUtils.isEmpty(item.getFinalEstimate()) ? Long.valueOf(0) : Long.valueOf(item.getFinalEstimate()));
        model.setExigencyTotal(StringUtils.isEmpty(item.getExigencyTotal()) ? Long.valueOf(0) : Long.valueOf(item.getExigencyTotal()));
        model.setExigency0(StringUtils.isEmpty(item.getExigency0()) ? Long.valueOf(0) : Long.valueOf(item.getExigency0()));
        model.setExigency1(StringUtils.isEmpty(item.getExigency1()) ? Long.valueOf(0) : Long.valueOf(item.getExigency1()));
        model.setExigency2(StringUtils.isEmpty(item.getExigency2()) ? Long.valueOf(0) : Long.valueOf(item.getExigency2()));
        model.setExigency3(StringUtils.isEmpty(item.getExigency3()) ? Long.valueOf(0) : Long.valueOf(item.getExigency3()));

        return model;
    }

    private PqmDrugEstimateImportForm getSiteCode(PqmDrugEstimateImportForm item, Map<String, String> siteMap) {
        if (item.getSiteName() == null || item.getSiteName().equals("0")) {
            return null;
        }
        item.setSiteCode(siteMap.getOrDefault(convertText(item.getSiteName()), null));
        return item;
    }

    private PqmDrugEstimateImportForm mapping(List<String> value) {
        PqmDrugEstimateImportForm item = new PqmDrugEstimateImportForm();
        if (value == null || value.isEmpty()) {
            return new PqmDrugEstimateImportForm();
        }

        try {
            item.setSiteName(value.get(1));
            item.setDrugName(value.get(2));
            item.setPacking(value.get(3));
            item.setHowToUse(value.get(4));
            item.setEarlyEstimate(value.get(5));
            item.setInEstimate(value.get(6));
            item.setFinalEstimate(value.get(7));
            item.setExigencyTotal(value.get(8));
            item.setExigency0(value.get(10));
            item.setExigency1(value.get(11));
            item.setExigency2(value.get(12));
            item.setExigency3(value.get(13));
        } catch (Exception e) {
            e.printStackTrace();
            return new PqmDrugEstimateImportForm();
        }
        return item;
    }

    private HashMap<String, List<String>> validateCustom(PqmDrugEstimateImportForm item, Row row, CellStyle style, Map<String, String> cols, Row row2) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(item.getSiteCode())) {
            errors.add("Đơn vị không được để trống hoặc không lấy được tại sheet DM_DONVI");
            addExcelErrorByCol(row, 1, style);
        }
        validateNull(item.getSiteName(), "Tên cơ sở", errors, "siteName", row, style, cols);
        validateNull(item.getSource(), "Nguồn thuốc", errors, "source", row, style, cols);
        validateNull(item.getDrugName(), "Tên thuốc", errors, "drugName", row, style, cols);

        if (!checkNumber(item.getEarlyEstimate())) {
            errors.add("Ước tính tồn kho đầu năm chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row, 5, style);
        }
        if (!checkNumber(item.getInEstimate())) {
            errors.add("Ước tính sử dụng trong năm chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row, 6, style);
        }
        if (!checkNumber(item.getFinalEstimate())) {
            errors.add("Ước tính tồn kho cuối năm chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row, 7, style);
        }
        if (!checkNumber(item.getExigencyTotal())) {
            errors.add("Tổng nhu cầu chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row, 8, style);
        }

        if (!checkNumber(item.getExigency0())) {
            errors.add("Nhu cầu phân bổ quý I chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row2, 10, style);
        }

        if (!checkNumber(item.getExigency1())) {
            errors.add("Nhu cầu phân bổ quý II chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row2, 11, style);
        }
        if (!checkNumber(item.getExigency2())) {
            errors.add("Nhu cầu phân bổ quý III chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row2, 12, style);
        }
        if (!checkNumber(item.getExigency3())) {
            errors.add("Nhu cầu phân bổ quý IV chỉ được nhập số nguyên dương");
            addExcelErrorByCol(row2, 13, style);
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
    public PqmDrugEstimateImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmDrugEstimateImportForm item = new PqmDrugEstimateImportForm();
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

}
