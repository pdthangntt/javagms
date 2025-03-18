package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.OpcRegimenImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.OpcRegimenImportSoft;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcVisitService;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

@Controller(value = "importation_opc_regimen")
public class OpcRegimenController extends BaseController<OpcRegimenImportForm> {

    private static int firstRow = 3;

    private HashMap<String, String> mappingRegimen() {
        HashMap<String, String> regimens = new HashMap<>();
        regimens.put("3TC/AZT/NVP 150/300/200", "O33");
        regimens.put("3TC/EFV/TDF 300/400/300", "O9");
        regimens.put("3TC/NVP/AZT 150/200/300", "O33");
        regimens.put("(3TC 150)/(LPV/r 200/50)/(TDF 300)", "O3");
        regimens.put("(3TC/ABC 30/60)/(EFV 200)/(EFV 50)", "O19");
        regimens.put("(TDF/3TC/EFV 300/300/600)", "O10");
        regimens.put("(TDF/3TC/EFV 300/300/400)", "O9");
        regimens.put("(EFV 200)/(ZDV/3TC 60/30)", "O32");
        regimens.put("(LPV/r 200/50)/(ZDV/3TC 300/150)", "O12");
        regimens.put("(3TC 150)/(ABC 300)/(EFV 600)", "O1");
        regimens.put("TDF/3TC/EFV 300/300/400", "O9");
        regimens.put("(3TC/ZDV 150/300)/(LPV/r 200/50)", "O12");
        regimens.put("(3TC 150)/(ABC 300)/(EFV 200)", "O16");
        regimens.put("(3TC/ZDV 150/300)/(LPV/r 200/50)/(TDF 300)", "O8");
        regimens.put("(LPV/r 200/50)/(TDF 300)/(ZDV/3TC 300/150)", "O8");
        regimens.put("(3TC 150)/(ABC 300)/(LPV/r 200/50)", "O2");
        regimens.put("DTG/3TC/TDF 50/300/300", "O14");
        regimens.put("(3TC 150)/(TDF 300)/(LPV/r 200/50)", "O3");
        regimens.put("(3TC 150)/(ABC 300)/(TDF 300)", "O6");
        regimens.put("(ABC 300)/(3TC 150)/(LPV/r 200/50)", "O2");
        regimens.put("(ABC/3TC 60/30)/(LPV/r 80/20)", "O20");
        regimens.put("(ABC/3TC 60/30)/(EFV 200)", "O31");
        regimens.put("(ABC/3TC 60/30)/(LPV/r 100/25)", "O1");
        regimens.put("(EFV 600)/(3TC 150)/(ABC 300)", "O1");
        regimens.put("(TDF 300)/(3TC 150)/(LPV/r 200/50)", "O3");
        regimens.put("(LPV/r 200/50)/(TDF 300)/(3TC 150)", "O3");
        regimens.put("3TC/NVP/ZDV 150/200/300", "O13");
        regimens.put("(ABC/3TC 60/30)/(EFV 50)/(EFV 200)", "O19");
        regimens.put("(EFV 200)/(ZDV/3TC 300/150)", "O22");
        regimens.put("(3TC/ZDV 150/300)/(EFV 200)", "O23");
        regimens.put("(TDF 300)/(ZDV/3TC 300/150)", "O5");
        regimens.put("(3TC 150)/(NVP 200)/(TDF 300)", "O4");
        regimens.put("(3TC 150)/(ABC 300)/(LPV/r 100/25)", "O17");
        regimens.put("(TDF 300)/(LPV/r 200/50)/(3TC 150)", "O3");
        regimens.put("(EFV 50)/(EFV 200)/(ZDV/3TC 60/30)", "O24");

        return regimens;

    }

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
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.TREATMENT_REGINMEN_STAGE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-opc-regimen/index.html");
        form.setSmallUrl("/import-opc-regimen/index.html");
        form.setReadUrl("/import-opc-regimen/index.html");
        form.setTitle("Cập nhật phác đồ sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("regimen_example.xlsx"));
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
        cols.put("1", "code");
        cols.put("2", "name");
        cols.put("3", "type");
        cols.put("4", "");
        cols.put("5", "regimen");
        cols.put("6", "level");
        cols.put("7", "time");
        return cols;
    }

    @GetMapping(value = {"/import-opc-regimen/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-opc-regimen/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> errors = new LinkedHashMap<>();
        ImportForm form = initForm();
        int index = 0;
        int success = 0;
        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<OpcRegimenImportForm> items = new LinkedList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (index < firstRow) {
                    continue;
                }
                if (StringUtils.isEmpty(row.getValue().get(1)) && StringUtils.isEmpty(row.getValue().get(2))) {
                    break;
                }
                OpcRegimenImportForm item = mapping(cols(), row.getValue());
                item.setStt(index);
                item.setIndex(String.valueOf(index));
                item.setName(item.getName().trim().toLowerCase());
                item.setCode(item.getCode().substring(6));
                item.setCode(item.getCode().replaceAll("\\s+", ""));

                item.setDate(TextUtils.convertDate(item.getTime(), FORMATDATE));

                if (StringUtils.isNotEmpty(item.getType()) && StringUtils.normalizeSpace(item.getType().trim()).equals(StringUtils.normalizeSpace("Điều trị ARV".trim()))) {
                    items.add(item);
                }

            }

            //Sắp sếp theo ngày tăng dần
            items.sort(new OpcRegimenImportSoft());

            //Xử lý phác đồ
            HashMap<String, String> treatmentRegimenOption = options.get(ParameterEntity.TREATMENT_REGIMEN);
            Set<String> treatmentRegimenIDKeys = treatmentRegimenOption.keySet();
            OpcStageEntity stage;
            for (OpcRegimenImportForm item : items) {
                if (StringUtils.isEmpty(item.getRegimen())) {
                    errors.put(item.getIndex(), "Bệnh án mã: #" + item.getCode() + ", Tệp excel không nhập phác đồ");
                }
                String regimen = "";   
                for (String key : treatmentRegimenIDKeys) {
                    String value1 = TextUtils.removeDiacritical(treatmentRegimenOption.get(key));
                    String value2 = TextUtils.removeDiacritical(item.getRegimen());
                    value1 = replatePhacDoAndSoft(value1);
                    value2 = replatePhacDoAndSoft(value2); 

                    if (value1.equals(value2)) { 
                        regimen = key;
                        break;
                    }
                }
                //map phác đồ theo cấu hình
                if (StringUtils.isEmpty(regimen)) {
                    for (Map.Entry<String, String> entry : mappingRegimen().entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        String value1 = TextUtils.removeDiacritical(key);
                        String value2 = TextUtils.removeDiacritical(item.getRegimen());

                        value1 = replatePhacDoAndSoft(value1);
                        value2 = replatePhacDoAndSoft(value2);

                        if (value1.equals(value2)) {
                            regimen = value;
                            break;
                        }

                    }
                }
                //Phác đồ không xác định
                if (StringUtils.isEmpty(regimen) && item.getRegimen() != null && TextUtils.removeDiacritical(item.getRegimen().toLowerCase()).contains(TextUtils.removeDiacritical("khac"))) {
                    regimen = "O29";
                }
                if (StringUtils.isEmpty(regimen)) {
                    errors.put(item.getIndex(), "Bệnh án mã: #" + item.getCode() + ", Phác đồ: " + item.getRegimen() + " chưa được cấu hình trong hệ thống IMS");
                } else {
                    List<OpcStageEntity> stages = opcStageService.findByArvCode(item.getCode());
                    if ((stages == null || stages.isEmpty())) {
                        if (!item.getCode().substring(item.getCode().length() - 1).equals("P") && !item.getCode().substring(item.getCode().length() - 1).equals("p")) {
                            errors.put(item.getIndex(), "Bệnh án mã: #" + item.getCode() + ", Không tìm thấy bệnh án hoặc giai đoạn điều trị");
                        } else {
                            success++;
                        }
                    } else {
                        stage = stages.get(0);
                        stage.setTreatmentRegimenID(regimen);
                        stage.setTreatmentRegimenStage(item.getLevel());
                        try {
                            opcStageService.save(stage);
                            success++;
                        } catch (Exception e) {
                            errors.put(item.getIndex(), "Bệnh án mã: #" + item.getCode() + ", Có lỗi khi lưu thông tin, vui lòng kiểm tra lại!");
                        }
                        opcStageService.logStage(stage.getArvID(), stage.getPatientID(), stage.getID(), "Cập nhật phác đồ từ import excel");
                    }

                }

            }

            model.addAttribute("success", "Đã tiến hành cập nhật phác đồ từ excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);//in thanh cong o day
            model.addAttribute("form", form);

            return "importation/opc-arv/opc-regimen";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @Override
    public OpcRegimenImportForm mapping(Map<String, String> cols, List<String> cells
    ) {
        OpcRegimenImportForm item = new OpcRegimenImportForm();
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

    private String replatePhacDoAndSoft(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String s = text;
        s = s.replaceAll("Bậc 1", "");
        s = s.replaceAll("Bậc 1", "");
        s = s.replaceAll("Bậc 2", "");
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\\/", "");
        s = s.replaceAll("\\+", "");
        s = s.replaceAll("\\(", "");
        s = s.replaceAll("\\)", "");
        s = s.replaceAll("\\=", "");
        s = s.replaceAll("\\-", "");

        char[] c = s.toCharArray();        // convert to array of chars 
        Arrays.sort(c);          // sort
        String newString = new String(c);  // convert back to String

        return newString;
    }

}
