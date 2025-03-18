package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.PqmArvViralLoadEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.PqmArvViralBookImportForm;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvViralService;
import com.gms.service.PqmLogService;
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
@Controller(value = "importation_pqm_viral_book")
public class PqmViralBookController extends BaseController<PqmArvViralBookImportForm> {

    private static int firstRow = 8;
    private static String textTitle = "";

    @Autowired
    private PqmArvViralService arvViralService;
    @Autowired
    private PqmArvService arvService;
    @Autowired
    private PqmLogService logService;

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
        options.get("siteOpc").put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-pqm-viral-book/index.html");
        form.setSmallUrl("/backend/pqm-arv/index.html");
        form.setReadUrl("/import-pqm-viral-book/index.html");
        form.setTitle("Nhập dữ liệu Sổ TLVR");
        form.setSmallTitle("Khách hàng điều trị");
        form.setTemplate(fileTemplate("Pqm_So TLVR_Template.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        int i = -1;
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "arvCode");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "testTime");
        cols.put(String.valueOf(i++), "resultTime");
        cols.put(String.valueOf(i++), "resultQ");
        cols.put(String.valueOf(i++), "resultR");
        cols.put(String.valueOf(i++), "resultS");
        cols.put(String.valueOf(i++), "resultT");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "");
        cols.put(String.valueOf(i++), "resultNumberW");
        cols.put(String.valueOf(i++), "resultNumberX");

        return cols;
    }

    @GetMapping(value = {"/import-pqm-viral-book/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("form", initForm());
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("sites", getOptions().get("siteOpc"));
        return "importation/pqm/upload_viraload";
    }

    private String convertText(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        text = StringUtils.normalizeSpace(text.trim());
        text = text.toLowerCase();
        return text;
    }

    @PostMapping(value = "/import-pqm-viral-book/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        int index = 0;
        try {

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());

            form.setData(readFileFormattedCell(workbook, 0, model, -1));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            if (!isPAC()) {
                sites = getCurrentUser().getSite().getID().toString();
            }
            System.out.println("xxx " + sites);
            if (StringUtils.isEmpty(sites)) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn cơ sở để nhập dữ liệu");
                return redirect(form.getUploadUrl());
            }

            List<PqmArvViralBookImportForm> items = new LinkedList<>();
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 4) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(2));
                    System.out.println("333 " + row.getValue().get(3));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Họ tên bệnh nhân"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Mã số"))
                            && StringUtils.isNotEmpty(row.getValue().get(3)) && convertText(row.getValue().get(3)).equals(convertText("Năm sinh"))) {
                        ok = true;
                    }
                }
                index++;
                if (index < firstRow) {
                    continue;
                }
                try {
                    if ((StringUtils.isEmpty(row.getValue().get(0)) && StringUtils.isEmpty(row.getValue().get(1)))) {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }

                PqmArvViralBookImportForm item = mapping(cols(), row.getValue());
                item.setRow(index + "");
                item.setSiteID(Long.valueOf(sites));
                if (StringUtils.isNotBlank(item.getArvCode())) {
//                    item.setArvCode(item.getArvCode().replaceAll(" ", ""));
                    item.setArvCode(item.getArvCode().trim());
                    item.setArvCode(item.getArvCode().toUpperCase());

                }

                items.add(item);
            }
            model.addAttribute("form", form);

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
            }

            Set<Long> siteIDs = new HashSet<>();
            HashMap<String, List< String>> errors = new LinkedHashMap<>();

            int i = firstRow - 1;
            if (form.getData().get(i).get(0) == null) {
                i = firstRow;
            }
            int success = 0;
            //tạm
            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();
            PqmArvViralLoadEntity e;
            Set<PqmArvViralLoadEntity> datas = new HashSet<>();
            for (PqmArvViralBookImportForm item1 : items) {
                i++;
                try {
                    Row row = sheet.getRow(i - 1);

                    HashMap<String, List< String>> object = validateCustom(item1, row, style, cols);

                    if (object.get("errors").isEmpty()) {
                        //Set entity
                        e = toEntity(item1);
                        datas.add(e);

                        success += 1;
                        continue;
                    }

                    errors.put(String.valueOf(i), object.get("errors"));
                } catch (Exception exs) {
                    HashMap<String, List< String>> object = new HashMap<>();
                    object.put("errors", new ArrayList<String>());
                    object.get("errors").add("Đã xảy ra lỗi với bản ghi, vui lòng kiểm tra lại!");
                    errors.put(String.valueOf(i), object.get("errors"));

                }

            }

            //Lưu tạm file vào thư mục tạm
            model.addAttribute("filePath", saveFile(workbook, file));

            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

            if (errors.isEmpty()) {
                for (PqmArvViralLoadEntity data : datas) {
                    data = arvViralService.save(data);
                    PqmArvEntity arv = arvService.findOne(data.getArvID());
                    if (arv != null) {
                        siteIDs.add(arv.getSiteID());
                    }
                }
                model.addAttribute("success", "Đã tiến hành import excel thành công");
                model.addAttribute("ok", true);
                for (Long siteID : siteIDs) {
                    logService.log("Nhập dữ liệu Sổ TLVR", items.size(), success, items.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", siteID, "Điều trị ARV");
                }
                return "importation/opc-arv/opc_viral_book_pqm";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");

                model.addAttribute("ok", false);
                return "importation/opc-arv/opc_viral_book_pqm";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private PqmArvViralLoadEntity toEntity(PqmArvViralBookImportForm item1) {
        LoggedUser currentUser = getCurrentUser();

        Set<Long> siteIDs = new HashSet<>();
        Set<String> arvCodes = new HashSet<>();
        arvCodes.add(item1.getArvCode());
        siteIDs.add(item1.getSiteID());

        List<PqmArvEntity> arvs = arvService.findBySiteIDsAndCodes(siteIDs, arvCodes);

        List<PqmArvViralLoadEntity> virals = arvViralService.findByArvAndTestTime(arvs.get(0).getID(), item1.getTestTime());
        PqmArvViralLoadEntity viral;
        if (virals == null) {
            viral = new PqmArvViralLoadEntity();
        } else {
            viral = virals.get(0);
        }
        viral.setArvID(arvs.get(0).getID());
        //set từ form import
        viral.setTestTime(TextUtils.convertDate(item1.getTestTime(), FORMATDATE));
        viral.setResultTime(StringUtils.isEmpty(item1.getResultTime()) ? null : TextUtils.convertDate(item1.getResultTime(), FORMATDATE));
        try {

            Long.valueOf(getResultNumber(item1));
            viral.setResultNumber(getResultNumber(item1));

        } catch (Exception e) {
            viral.setResultNumber("");
        }

        viral.setResult(getResults(item1));
        return viral;
    }

    private String getResults(PqmArvViralBookImportForm item1) {
        String result = "";
        if (StringUtils.isNotEmpty(item1.getResultNumberX())) {
            return "4";
        }
        if (StringUtils.isNotEmpty(item1.getResultNumberW())) {
            return "3";
        }
        if (StringUtils.isNotEmpty(item1.getResultT())) {
            return "4";
        }
        if (StringUtils.isNotEmpty(item1.getResultS())) {
            return "3";
        }
        if (StringUtils.isNotEmpty(item1.getResultR())) {
            return "2";
        }
        if (StringUtils.isNotEmpty(item1.getResultQ())) {
            return "1";
        }
        return result;

    }

    private String getResultNumber(PqmArvViralBookImportForm item1) {
        String result = "";
        if (StringUtils.isNotEmpty(item1.getResultNumberX())) {
            return item1.getResultNumberX().replaceAll("[^0-9]", "");
        }
        if (StringUtils.isNotEmpty(item1.getResultNumberW())) {
            return item1.getResultNumberW().replaceAll("[^0-9]", "");
        }
        if (StringUtils.isNotEmpty(item1.getResultT())) {
            return item1.getResultT().replaceAll("[^0-9]", "");
        }
        if (StringUtils.isNotEmpty(item1.getResultS())) {
            return item1.getResultS().replaceAll("[^0-9]", "");
        }
        if (StringUtils.isNotEmpty(item1.getResultR())) {
            return item1.getResultR().replaceAll("[^0-9]", "");
        }
        if (StringUtils.isNotEmpty(item1.getResultQ())) {
            return item1.getResultQ().replaceAll("[^0-9]", "");
        }
        return result;

    }

//    private String getResultNumber(PqmArvViralBookImportForm item1) {
//        String result = "";
//        if (StringUtils.isNotEmpty(item1.getResultNumberW()) || StringUtils.isNotEmpty(item1.getResultNumberX())) {
//            result = StringUtils.isNotEmpty(item1.getResultNumberW()) ? item1.getResultNumberW() : item1.getResultNumberX();
//            return result.replaceAll("[^0-9]", "");
//        } else {
//            result = StringUtils.isNotEmpty(item1.getResultQ()) ? item1.getResultQ()
//                    : StringUtils.isNotEmpty(item1.getResultR()) ? item1.getResultR()
//                    : StringUtils.isNotEmpty(item1.getResultS()) ? item1.getResultS()
//                    : StringUtils.isNotEmpty(item1.getResultT()) ? item1.getResultT()
//                    : "";
//            return result.replaceAll("[^0-9]", "");
//        }
//
//    }
//    private String getKQXN(String ketQua) {
//        if (ketQua != null) {
//            ketQua = ketQua.replace("<", "").trim();
//            ketQua = ketQua.replaceAll(" ", "").trim();
//            try {
//                if ("kph".equals(ketQua.toLowerCase()) || ketQua.toLowerCase().equals("khôngpháthiện".toLowerCase())) {
//                    return "1";
//                }
//                ketQua = ketQua.replaceAll("[^0-9]", "");
//                int result = Integer.parseInt(ketQua);
//                if (result <= 20) {
//                    ketQua = "1";
//                } else if (result > 20 && result < 200) {
//                    ketQua = "2";
//                } else if (result >= 200 && result < 1000) {
//                    ketQua = "3";
//                } else if (result >= 1000) {
//                    ketQua = "4";
//                }
//            } catch (NumberFormatException ex) {
//                if ("kph".equals(ketQua.toLowerCase()) || ketQua.toLowerCase().equals("khôngpháthiện".toLowerCase())) {
//                    ketQua = "1";
//                } else {
//                    ketQua = "";
//                }
//            }
//        } else {
//            ketQua = "";
//        }
//        return ketQua;
//    }
    private HashMap<String, List<String>> validateCustom(PqmArvViralBookImportForm item1, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();

        validateNull(item1.getArvCode(), "Mã bệnh án được để trống", errors);
        if (StringUtils.isEmpty(item1.getArvCode())) {
            addExcelError("arvCode", row, cols, style);
        }
        validateNull(item1.getTestTime(), "Ngày xét nghiệm không được để trống", errors);
        if (StringUtils.isEmpty(item1.getTestTime())) {
            addExcelError("testTime", row, cols, style);
        }
        if (StringUtils.isNotBlank(item1.getTestTime()) && !isThisDateValid(item1.getTestTime())) {
            errors.add("Ngày xét nghiệm TLVR không hợp lệ");
            addExcelError("testTime", row, cols, style);
        }
        if (StringUtils.isNotEmpty(item1.getTestTime()) && isThisDateValid(item1.getTestTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(item1.getTestTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày xét nghiệm TLVR không được sau ngày hiện tại");
                addExcelError("testTime", row, cols, style);
            }
        }
        if (StringUtils.isNotBlank(item1.getResultTime()) && !isThisDateValid(item1.getResultTime())) {
            errors.add("Ngày nhận kết quả không hợp lệ");
            addExcelError("resultTime", row, cols, style);
        }
        if (StringUtils.isNotEmpty(item1.getResultTime()) && isThisDateValid(item1.getResultTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(item1.getResultTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.add("Ngày nhận kết quả không được sau ngày hiện tại");
                addExcelError("resultTime", row, cols, style);
            }
        }

        if (StringUtils.isNotEmpty(item1.getArvCode())) {
            Set<Long> siteIDs = new HashSet<>();
            Set<String> arvCodes = new HashSet<>();
            arvCodes.add(item1.getArvCode());
            siteIDs.add(item1.getSiteID());

            List<PqmArvEntity> arvs = arvService.findBySiteIDsAndCodes(siteIDs, arvCodes);
            if (arvs == null) {
                errors.add("Không tìm thấy bệnh án");
                object.put("errors", errors);
                addExcelError("arvCode", row, cols, style);
                return object;
            }
            if (arvs.size() > 1) {
                String benhAnTrung = "";
                for (PqmArvEntity arv : arvs) {
                    benhAnTrung = benhAnTrung + "#" + arv.getCode() + "; ";
                }
                errors.add("Tồn tại " + arvs.size() + " bệnh án trùng thông tin (" + benhAnTrung + " ), kiểm tra lại thông tin");
                object.put("errors", errors);
                return object;
            }
            if (arvs != null && arvs.size() == 1) {
                List<PqmArvViralLoadEntity> virals = arvViralService.findByArvAndTestTime(arvs.get(0).getID(), item1.getTestTime());
                if (virals != null && virals.size() > 1) {
                    errors.add("Tồn tại " + virals.size() + " lượt TLVR của bệnh án mã #" + item1.getArvCode() + " có cùng ngày khám, kiểm tra lại thông tin");
                    object.put("errors", errors);
                    return object;
                }

            }

        }

        validateNull(getResults(item1), "Kết quả xét nghiệm nhập không chính xác hoặc đang để trống", errors);

        object.put("errors", errors);

        return object;
    }

    @Override
    public PqmArvViralBookImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmArvViralBookImportForm item = new PqmArvViralBookImportForm();
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
        if (dateToValidate.length() > 10) {
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
