/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.opc_arv.OpcViralImportForm;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.ParameterService;
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
 * @author DSNAnh
 */
@Controller(value = "importation_opc_viral")
public class OpcViralController extends BaseController<OpcViralImportForm> {

    private static int firstRow = 13;
    private static String textTitle = "";

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;
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
        form.setUploadUrl("/import-opc-viral-tg/index.html");
        form.setSmallUrl("/import-opc-viral-tg/index.html");
        form.setReadUrl("/import-opc-viral-tg/index.html");
        form.setTitle("Thêm lượt xét nghiệm TLVR sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("opc_arv_viral.xlsx"));
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
        cols.put("2", "");
        cols.put("3", "male");
        cols.put("4", "female");
        cols.put("5", "insuranceNo");
        cols.put("6", "");
        cols.put("7", "sampleTime");
        cols.put("8", "");
        cols.put("9", "");
        cols.put("10", "");
        cols.put("11", "");
        cols.put("12", "");
        cols.put("13", "");
        cols.put("14", "");
        cols.put("15", "");
        cols.put("16", "result");

        return cols;
    }

    @GetMapping(value = {"/import-opc-viral-tg/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-opc-viral-tg/index.html")
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

            List<OpcViralImportForm> items = new LinkedList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
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
                OpcViralImportForm item = mapping(cols(), row.getValue());
                if (item.getName() != null) {
                    item.setName(StringUtils.normalizeSpace(item.getName().trim().toLowerCase().replace("\n", " ")));
                }
                items.add(item);
            }

            HashMap<String, List< String>> errors = new LinkedHashMap<>();

            int i = firstRow - 1;
            if (form.getData().get(i).get(0) == null) {
                i = firstRow;
            }
            int success = 0;
            //tạm
//            

            OpcViralLoadEntity e;
            for (OpcViralImportForm item1 : items) {
                i++;
                if (item1.getSampleTime() != null) {
                    String sampleTime = item1.getSampleTime().replace("\n", " ");
                    sampleTime = sampleTime.replace("\'", "");
                    item1.setSampleTime(sampleTime);
                }
                HashMap<String, List< String>> object = validateCustom(item1);

                opcViralLoadService.findByArvID(Long.MAX_VALUE, true);
                if (object.get("errors").isEmpty()) {
                    //Set entity
                    if (StringUtils.isBlank(item1.getInsuranceNo())) {
                        item1.setInsuranceNo("");
                    }
                    e = toEntity(item1);
                    e.setImportable(true);
                    boolean b = e.getID() == null;
                    //thêm 84 ngày hẹn khám
                    if (e.getResult().equals("3") || e.getResult().equals("4")) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(e.getTestTime());
                        c.add(Calendar.DATE, 84);
                        e.setRetryTime(c.getTime());
                    }

                    opcViralLoadService.save(e);
                    opcViralLoadService.logViralLoad(e.getArvID(), e.getPatientID(), e.getID(), b ? "Thêm lượt xét nghiệm TLVR sử dụng import excel" : "Cập nhật lượt xét nghiệm TLVR sử dụng import excel");
                    success += 1;
                    continue;
                }

                errors.put(String.valueOf(i), object.get("errors"));

            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

            return "importation/opc-arv/opc_viral";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private OpcViralLoadEntity toEntity(OpcViralImportForm item1) {
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

        List<OpcArvEntity> arvs = opcArvService.findArvImportExcelViral(item1.getName(), age, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
        List<OpcStageEntity> stages = opcStageService.findByArvIDExcelVisit(arvs.get(0).getID());
        List<OpcViralLoadEntity> virals = opcViralLoadService.findByTestTimeAndStageID(TextUtils.formatDate(cal.getTime(), "yyyy-MM-dd"), stages.get(0).getID());
        OpcViralLoadEntity e;
        if (virals != null) {
            e = virals.get(0);
        } else {
            e = new OpcViralLoadEntity();
        }
        e.setArvID(arvs.get(0).getID());
        e.setSiteID(arvs.get(0).getSiteID());
        e.setPatientID(arvs.get(0).getPatientID());
        e.setSiteID(arvs.get(0).getSiteID());
        e.setStageID(stages.get(0).getID());
        //set từ form import
        e.setTestSiteID(currentUser.getSite().getID());
        e.setSampleTime(cal.getTime());
        e.setTestTime(cal.getTime());
        e.setCauses(causes);
        e.setResultNumber(item1.getResult());
        e.setResult(getKQXN(item1.getResult()));
        return e;
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

    private HashMap<String, List<String>> validateCustom(OpcViralImportForm item1) {

        HashMap<String, List<String>> object = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int age = 0;
        try {
            age = StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? Integer.parseInt(item1.getMale()) : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? Integer.parseInt(item1.getFemale()) : 0;
        } catch (Exception e) {
            errors.add("Năm sinh không hợp lệ");
        }
        validateNull(item1.getName(), "Họ và tên ", errors);
//        if (StringUtils.isBlank(item1.getInsuranceNo())) {
//            errors.add("Không có số thẻ BHYT, kiểm tra lại thông tin ");
//        }
        try {
            if (StringUtils.isNotEmpty(item1.getSampleTime()) && !isThisDateValid(item1.getSampleTime().split(" ").length == 3 ? item1.getSampleTime().split(" ")[2] : item1.getSampleTime().split(" ")[1])) {
                errors.add("Sai định dạng ngày, kiểm tra lại thông tin ");
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
        }
        List<OpcArvEntity> arvs = opcArvService.findArvImportExcelViral(item1.getName(), age, StringUtils.isNotEmpty(item1.getMale()) && StringUtils.isEmpty(item1.getFemale()) ? "1" : StringUtils.isEmpty(item1.getMale()) && StringUtils.isNotEmpty(item1.getFemale()) ? "2" : "0", item1.getInsuranceNo());
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

        object.put("errors", errors);

        return object;
    }

    @Override
    public OpcViralImportForm mapping(Map<String, String> cols, List<String> cells) {
        OpcViralImportForm item = new OpcViralImportForm();
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
