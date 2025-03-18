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
import com.gms.entity.form.opc_arv.PqmArvViralImportForm;
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
@Controller(value = "importation_pqm_viral")
public class PqmViralController extends BaseController<PqmArvViralImportForm> {

    private static int firstRow = 13;
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
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());

        }

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-pqm-viral/index.html");
        form.setSmallUrl("/backend/pqm-arv/index.html");
        form.setReadUrl("/import-pqm-viral/index.html");
        form.setTitle("Nhập dữ liệu XN TLVR");
        form.setSmallTitle("Khách hàng điều trị");
        form.setTemplate(fileTemplate("pqm_arv_viral.xlsx"));
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
        cols.put("1", "name");
        cols.put("2", "male");
        cols.put("3", "female");
        cols.put("4", "insuranceNo");
        cols.put("5", "");
        cols.put("6", "sampleTime");
        cols.put("7", "");
        cols.put("8", "");
        cols.put("9", "");
        cols.put("10", "");
        cols.put("11", "");
        cols.put("12", "");
        cols.put("13", "");
        cols.put("14", "");
        cols.put("15", "result");

        return cols;
    }

    @GetMapping(value = {"/import-pqm-viral/index.html"})
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

    @PostMapping(value = "/import-pqm-viral/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        ImportForm form = initForm();
        int index = 0;
        try {

            Workbook workbook = ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename());
            form.setData(readFile(workbook, 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<PqmArvViralImportForm> items = new LinkedList<>();
            boolean ok = false;
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                if (row.getKey() == 10) {
                    System.out.println("111 " + row.getValue().get(1));
                    System.out.println("222 " + row.getValue().get(2));
                    System.out.println("333 " + row.getValue().get(4));
                    if (row.getValue() != null && !row.getValue().isEmpty()
                            && StringUtils.isNotEmpty(row.getValue().get(1)) && convertText(row.getValue().get(1)).equals(convertText("Họ tên BN"))
                            && StringUtils.isNotEmpty(row.getValue().get(2)) && convertText(row.getValue().get(2)).equals(convertText("Năm sinh"))
                            && StringUtils.isNotEmpty(row.getValue().get(4)) && convertText(row.getValue().get(4)).equals(convertText("Mã số Bn tại PKNT"))) {
                        ok = true;
                    }
                }
                index++;
                if (index < firstRow) {
                    continue;
                }
                try {
                    if ((StringUtils.isEmpty(row.getValue().get(1)) && StringUtils.isEmpty(row.getValue().get(4)))
                            || (StringUtils.isNotEmpty(row.getValue().get(1)) && row.getValue().get(1).trim().toUpperCase().equals("NGƯỜI GỬI"))) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                row.getValue();
                PqmArvViralImportForm item = mapping(cols(), row.getValue());
                if (item.getName() != null) {
                    item.setName(StringUtils.normalizeSpace(item.getName().trim().toLowerCase().replace("\n", " ")));
                }

                items.add(item);
            }

            model.addAttribute("form", form);

            if (!ok) {
                redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();

            int i = firstRow - 1;
            if (form.getData().get(i).get(0) == null) {
                i = firstRow;
            }
            int success = 0;

            Set<Long> siteIDs = new HashSet<>();
            //tạm
            //Lấy sheet và tạo màu Màu đổ
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Map<String, String> cols = cols();

            Set<PqmArvViralLoadEntity> datas = new HashSet<>();

            PqmArvViralLoadEntity e;
            for (PqmArvViralImportForm item1 : items) {
                i++;
                try {
                    if (item1.getSampleTime() != null) {
                        String sampleTime = item1.getSampleTime().replace("\n", " ");
                        sampleTime = sampleTime.replace("\'", "");
                        item1.setSampleTime(sampleTime);
                    }

                    //Lấy dòng excel
                    Row row = sheet.getRow(i - 1);
                    HashMap<String, List< String>> object = validateCustom(item1, row, style, cols);

                    if (object.get("errors").isEmpty()) {
                        //Set entity
                        if (StringUtils.isBlank(item1.getInsuranceNo())) {
                            item1.setInsuranceNo("");
                        }
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
                    logService.log("Nhập dữ liệu XN TLVR", items.size(), success, items.size() - success, isPAC() ? "Tuyến tỉnh" : "Cơ sở", siteID, "Điều trị ARV");
                }
                return "importation/opc-arv/opc_viral_pqm";
            } else {
                model.addAttribute("error", "Không thể thực hiện nhập dữ liệu excel. Vui lòng tải tệp tin excel và chỉnh sửa bản ghi lỗi đã được bôi màu nếu có thông báo lỗi bên dưới.");
                model.addAttribute("ok", false);
                return "importation/opc-arv/opc_viral_pqm";
            }

        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "File dữ liệu không đúng. Vui lòng kiểm tra lại!");
            return redirect(form.getUploadUrl());
        }
    }

    private PqmArvViralLoadEntity toEntity(PqmArvViralImportForm item1) {
        LoggedUser currentUser = getCurrentUser();
        List<String> causes = new ArrayList();
        causes.add(ViralLoadSymtomEnum.DINHKY3THANG.getKey());
        int age = 0;
        try {
            age = StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? Integer.parseInt(item1.getMale()) : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? Integer.parseInt(item1.getFemale()) : 0;
        } catch (Exception e) {
            age = 0;
        }
        String hour = item1.getSampleTime().split(" ")[0];
        String date = item1.getSampleTime().split(" ")[1];
        if (item1.getSampleTime().split(" ").length == 3) {
            hour = item1.getSampleTime().split(" ")[0] + item1.getSampleTime().split(" ")[1];
            date = item1.getSampleTime().split(" ")[2];
        }

        String year = date.split("/")[2].length() == 2 ? "20" + date.split("/")[2] : date.split("/")[2];
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Integer.parseInt(year), Integer.parseInt(date.split("/")[1]) - 1, Integer.parseInt(date.split("/")[0]),
                Integer.parseInt(hour.split("h")[0]), Integer.parseInt(hour.split("h").length == 1 ? "0" : hour.split("h")[1]), 0);

        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            for (Map.Entry<String, String> entry : getOptions().get("siteOpc").entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                siteIDs.add(Long.valueOf(key));

            }
        } else {
            siteIDs.add(getCurrentUser().getSite().getID());
        }
        List<PqmArvEntity> arvs = arvService.findBySiteAndInsuranceNo(siteIDs, item1.getInsuranceNo());

        PqmArvViralLoadEntity viral = arvViralService.findByArvAndInsuranceNoAndTestTime(arvs.get(0).getID(), item1.getInsuranceNo(), TextUtils.formatDate(cal.getTime(), FORMATDATE_SQL));

        if (viral == null) {
            viral = new PqmArvViralLoadEntity();
        }
        viral.setArvID(arvs.get(0).getID());
        //set từ form import
        viral.setTestTime(cal.getTime());
        viral.setResultNumber(item1.getResult());
        viral.setResult(getKQXN(item1.getResult()));
        viral.setInsuranceNo(item1.getInsuranceNo());
        if (viral.getResultTime() == null) {
            viral.setResultTime(cal.getTime());
        }

        return viral;
    }

    private String getKQXN(String ketQua) {
        if (ketQua != null) {
            ketQua = ketQua.replace("<", "").trim();
            ketQua = ketQua.replaceAll(" ", "").trim();
            try {
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
                if ("kph".equals(ketQua.toLowerCase())) {
                    ketQua = "1";
                } else {
                    ketQua = "";
                }
            }
        } else {
            ketQua = "";
        }
        return ketQua;
    }

    private HashMap<String, List<String>> validateCustom(PqmArvViralImportForm item1, Row row, CellStyle style, Map<String, String> cols) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int age = 0;
        try {
            age = StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? Integer.parseInt(item1.getMale()) : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? Integer.parseInt(item1.getFemale()) : 0;
        } catch (Exception e) {
            errors.add("Năm sinh không hợp lệ");
            addExcelError(StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "male" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "female" : "xxx", row, cols, style);
        }
        validateNull(item1.getName(), "Họ và tên ", errors);
        if (StringUtils.isEmpty(item1.getName())) {
            addExcelError("name", row, cols, style);
        }
        if (StringUtils.isBlank(item1.getInsuranceNo())) {
            errors.add("Không có số thẻ BHYT, kiểm tra lại thông tin ");
            addExcelError("insuranceNo", row, cols, style);
        }
        try {
            if (StringUtils.isNotEmpty(item1.getSampleTime()) && !isThisDateValid(item1.getSampleTime().split(" ").length == 3 ? item1.getSampleTime().split(" ")[2] : item1.getSampleTime().split(" ")[1])) {
                errors.add("Sai định dạng ngày, kiểm tra lại thông tin ");
                addExcelError("sampleTime", row, cols, style);
            }
            if (StringUtils.isNotEmpty(item1.getSampleTime()) && isThisDateValid(item1.getSampleTime().split(" ").length == 3 ? item1.getSampleTime().split(" ")[2] : item1.getSampleTime().split(" ")[1])) {
                Date today = new Date();
                Date time = TextUtils.convertDate(item1.getSampleTime().split(" ").length == 3 ? item1.getSampleTime().split(" ")[2] : item1.getSampleTime().split(" ")[1], FORMATDATE);
                if (time.compareTo(today) > 0) {
                    errors.add("Ngày XN không được sau ngày hiện tại");
                    addExcelError("sampleTime", row, cols, style);
                }
            }
            String hour = item1.getSampleTime().split(" ")[0];
            String date = item1.getSampleTime().split(" ")[1];
            if (item1.getSampleTime().split(" ").length == 3) {
                hour = item1.getSampleTime().split(" ")[0] + item1.getSampleTime().split(" ")[1];
                date = item1.getSampleTime().split(" ")[2];
            }
            String year = date.split("/")[2].length() == 2 ? "20" + date.split("/")[2] : date.split("/")[2];
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(Integer.parseInt(year), Integer.parseInt(date.split("/")[1]) - 1, Integer.parseInt(date.split("/")[0]),
                    Integer.parseInt(hour.split("h")[0]), Integer.parseInt(hour.split("h").length == 1 ? "0" : hour.split("h")[1]), 0);
        } catch (Exception e) {
            errors.add("Sai định dạng ngày, kiểm tra lại thông tin ");
            addExcelError("sampleTime", row, cols, style);
        }

        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            for (Map.Entry<String, String> entry : getOptions().get("siteOpc").entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                siteIDs.add(Long.valueOf(key));

            }
        } else {
            siteIDs.add(getCurrentUser().getSite().getID());
        }

        List<PqmArvEntity> arvs = arvService.findBySiteAndInsuranceNo(siteIDs, item1.getInsuranceNo());
        if (arvs == null) {
            errors.add("Không tìm thấy bệnh án từ thẻ BHYT");
            object.put("errors", errors);
            addExcelError("insuranceNo", row, cols, style);
            return object;
        }
        if (arvs.size() > 1) {
            String benhAnTrung = "";
            for (PqmArvEntity arv : arvs) {
                benhAnTrung = benhAnTrung + "#" + arv.getCode() + "; ";
            }
            errors.add("Tồn tại " + arvs.size() + " bệnh án trùng thông tin (" + benhAnTrung + " ), kiểm tra lại thông tin");
            addExcelError("insuranceNo", row, cols, style);
            object.put("errors", errors);
            return object;
        }

        object.put("errors", errors);

        return object;
    }

    @Override
    public PqmArvViralImportForm mapping(Map<String, String> cols, List<String> cells) {
        PqmArvViralImportForm item = new PqmArvViralImportForm();
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
