package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.OpcTGImportForm;
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

@Controller(value = "importation_opc_tg")
public class OpcImportTGController extends BaseController<OpcTGImportForm> {

    private static int firstRow = 2;

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private OpcArvService opcArvService;

    private static final String CODE_REGEX = "(^[^-](?!.*--)[a-zA-Z0-9-\\/\\*]+[^-]$)";

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
        form.setUploadUrl("/import-opc-tg/index.html");
        form.setSmallUrl("/import-opc-tg/index.html");
        form.setReadUrl("/import-opc-tg/index.html");
        form.setTitle("Thêm dữ liệu sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("opc_tg_examples.xlsx"));
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
        cols.put("3", "dob");
        cols.put("4", "gender");
        cols.put("5", "address");
        cols.put("6", "ward");
        cols.put("7", "district");
        cols.put("8", "province");
        cols.put("9", "registrationType");
        cols.put("10", "registrationTime");
        cols.put("11", "confirmTime");
        cols.put("12", "treatmentTime");
        cols.put("13", "");
        cols.put("14", "endTime");
        cols.put("15", "endCase");
        cols.put("16", "treatmentRegimenID");
        cols.put("17", "");
        cols.put("18", "");
        cols.put("19", "");
        return cols;
    }

    @GetMapping(value = {"/import-opc-tg/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-opc-tg/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, String> errors = new LinkedHashMap<>();
        ImportForm form = initForm();
        LoggedUser currentUser = getCurrentUser();
        int index = 0;
        int success = 0;

        Map<String, String> provinces = new HashMap<>();
        Map<String, String> treatmentRegiments = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            String name = provinceEntity.getName().replace("Tỉnh ", "");
            name = name.replace("Thành phố ", "");
            provinces.put(provinceEntity.getID() + "", name);
        }

        //Phac đồ
        for (Map.Entry<String, String> entry : options.get(ParameterEntity.TREATMENT_REGIMEN).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            treatmentRegiments.put(value, key);

        }

        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));

            List<OpcTGImportForm> items = new LinkedList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (index < firstRow) {
                    continue;
                }
                if (StringUtils.isEmpty(row.getValue().get(1)) && StringUtils.isEmpty(row.getValue().get(2))) {
                    break;
                }
                OpcTGImportForm item = mapping(cols(), row.getValue());
                item.setStt(index);
                item.setIndex(String.valueOf(index));

                item.setCode(item.getCode().replace(" ", ""));

                item.setDob(item.getDob().replace(" ", ""));
                String provinceValue = "";
                String district = "";
                String ward = "";

                for (Map.Entry<String, String> entry : provinces.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String keyValue = TextUtils.removeDiacritical(value).toLowerCase();
                    String province = TextUtils.removeDiacritical(item.getProvince().toLowerCase());
                    if (keyValue.equals(province)) {
                        provinceValue = key;
                        district = getDistrict(key, item.getDistrict());
                        ward = getWard(district, item.getWard());
                        break;
                    }

                }
                item.setProvince(provinceValue);
                item.setDistrict(district);
                item.setWard(ward);

//                item.setGender(item.getGender());
//                item.setAddress(item.getAddress());
//                item.setWard(item.getWard());
//                item.setDistrict(item.getDistrict());
//                item.setProvince(item.getProvince());
                item.setRegistrationType(getRegistionType(item.getRegistrationType()));
//                item.setRegistrationTime(item.getRegistrationTime());
//                item.setConfirmTime(item.getConfirmTime());
//                item.setTreatmentTime(item.getTreatmentTime());
//                item.setEndTime(item.getEndTime());
                item.setEndCase(getEndCase(item.getEndCase()));
                item.setTreatmentRegimenID(treatmentRegiments.getOrDefault(item.getTreatmentRegimenID(), ""));

                items.add(item);

            }

            //validate
            for (OpcTGImportForm item : items) {
                String validate = validate(item);
                if (StringUtils.isNotEmpty(validate)) {
                    errors.put("Dòng: " + item.getStt() + " Mã: " + item.getCode(), validate);
                    continue;
                }
                try {
                    OpcArvEntity arv;
                    if (StringUtils.isEmpty(validate)) {
                        arv = toArv(item);
                        opcArvService.create(arv);

                    }

                    success++;
                    System.out.println("s " + success + "x%: " + success / items.size() + "%");
                } catch (Exception e) {
                    errors.put("Dòng: " + item.getStt() + " Mã: " + item.getCode(), e.getMessage());
//                    e.printStackTrace();
                }

            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);//in thanh cong o day
            model.addAttribute("form", form);

            return "importation/opc-arv/opc-tg";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    private String getRegistionType(String type) {
        if (StringUtils.isEmpty(type)) {
            return "";
        }
        if (type.equals("BĐTL")) {
            return "2";
        }
        if (type.equals("ARVCT:ARV CHUYỂN TỚI")) {
            return "3";
        }
        if (type.equals("Mới")) {
            return "1";
        }
        return "";

    }

    private String getEndCase(String endCase) {
        if (StringUtils.isEmpty(endCase)) {
            return "";
        }
        if (endCase.equals("ARVCĐ:ARV CHUYỂN ĐI")) {
            return "3";
        }
        if (endCase.equals("ARVB:ARV BỎ")) {
            return "1";
        }
        if (endCase.equals("ARVTV:ARV TỬ VONG")) {
            return "2";
        }
        return "";

    }

    private String getDistrict(String provinceID, String districtItem) {
        List<DistrictEntity> districts = locationsService.findDistrictByProvinceID(provinceID);
        if (districts == null || districts.isEmpty() || StringUtils.isEmpty(districtItem)) {
            return "";
        }
        for (DistrictEntity districtEntity : districts) {
            String districtID = districtEntity.getName();
            districtID = TextUtils.removeDiacritical(districtID);

            String district = TextUtils.removeDiacritical(districtItem).toLowerCase();
            if (districtID.toLowerCase().equals(district)) {
                return districtEntity.getID();
            }
        }
        return "";
    }

    private String getWard(String districtID, String wardItem) {
        List<WardEntity> wards = locationsService.findWardByDistrictID(districtID);
        if (wards == null || wards.isEmpty() || StringUtils.isEmpty(wardItem)) {
            return "";
        }
        for (WardEntity wardEntity : wards) {
            String ward = wardEntity.getName();
            ward = TextUtils.removeDiacritical(ward);
            String wardItems = TextUtils.removeDiacritical(wardItem).toLowerCase();
            if (wardItems.equals(ward.toLowerCase())) {
                return wardEntity.getID();
            }
        }
        return "";
    }

    private String validate(OpcTGImportForm item) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        String error = "";
        LoggedUser currentUser = getCurrentUser();

        //code
        if (StringUtils.isEmpty(item.getCode())) {
            error = error + "\n" + "Mã bệnh án không được để trống, ";
        } else {
            if (opcArvService.checkExistBySiteIDAndCode(currentUser.getSite().getID(), item.getCode())) {
                error = error + "Mã khách hàng đã tồn tại, ";
            }
            if (!TextUtils.removeDiacritical(item.getCode().trim()).matches(CODE_REGEX)) {
                error = error + "Mã bệnh án không đúng định dạng, chỉ bao gồm số, chữ, \"-\", \"*\" và \"/\", ";
            }
        }

        //Name
        if (StringUtils.isEmpty(item.getName())) {
            error = error + "\n" + "Họ tên không được để trống, ";
        } else {
            if (item.getName().length() > 99) {
                error = error + "\n" + "Họ và tên không được quá 100 ký tự, ";
            }
            if (!TextUtils.removeDiacritical(item.getName().trim()).matches(RegexpEnum.VN_CHAR)) {
                error = error + "\n" + "Họ tên chỉ chứa kí tự chữ cái, ";
            }
        }
        //dob
        if (StringUtils.isEmpty(item.getDob())) {
            error = error + "\n" + "Năm sinh không được để trống, ";
        } else {
            try {
                Long dob = Long.valueOf(item.getDob());
                Long currentYear = Long.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
                if (dob > currentYear) {
                    error = error + "\n" + "Năm sinh không được lớn hơn năm hiện tại, ";
                }
            } catch (Exception e) {
                error = error + "\n" + "Năm sinh phải là số, ";
            }

        }
        //gender
        if (StringUtils.isEmpty(item.getGender())) {
            error = error + "\n" + "Giới tính không được để trống, ";
        } else {
            if (!item.getGender().equals("Nam") && !item.getGender().equals("Nữ")) {
                error = error + "\n" + "Giới tính phải là Nam hoặc Nữ, ";
            }

        }
        //địa chỉ
        if (StringUtils.isEmpty(item.getAddress())) {
            error = error + "\n" + "Số nhà không được để trống, ";
        }
        if (StringUtils.isEmpty(item.getWard())) {
            error = error + "\n" + "Xã không được để trống, ";
        }
        if (StringUtils.isEmpty(item.getDistrict())) {
            error = error + "\n" + "Huyện không được để trống, ";
        }
        if (StringUtils.isEmpty(item.getProvince())) {
            error = error + "\n" + "Tỉnh không được để trống, ";
        }

        if (StringUtils.isEmpty(item.getRegistrationType())) {
            error = error + "\n" + "Loại đăng ký không được để trống, ";
        }

        if (StringUtils.isEmpty(item.getRegistrationTime())) {
            error = error + "\n" + "Ngày đăng ký không được để trống, ";
        } else {
            if (checkDateFormat(item.getRegistrationTime(), sdfrmt)) {
                error = error + "\n" + "Ngày đăng ký không đúng định dạng dd/MM/yyyy, ";
            }
        }
        if (StringUtils.isEmpty(item.getConfirmTime())) {
//            error = error + "\n" + "Ngày khẳng định không được để trống, ";
        } else {
            if (checkDateFormat(item.getConfirmTime(), sdfrmt)) {
                error = error + "\n" + "Ngày khẳng định không đúng định dạng dd/MM/yyyy, ";
            }
        }
        if (StringUtils.isEmpty(item.getTreatmentTime())) {
//            error = error + "\n" + "Ngày điều trị không được để trống, ";
        } else {
            if (checkDateFormat(item.getTreatmentTime(), sdfrmt)) {
                error = error + "\n" + "Ngày điều trị không đúng định dạng dd/MM/yyyy, ";
            }
        }
        if (StringUtils.isNotEmpty(item.getEndTime())) {
            if (checkDateFormat(item.getEndTime(), sdfrmt)) {
                error = error + "\n" + "Ngày kết thúc không đúng định dạng dd/MM/yyyy, ";
            }
        }
//ss
        try {
            Date treatmentTime = sdfrmt.parse(item.getTreatmentTime());
            Date registrationTime = sdfrmt.parse(item.getRegistrationTime());
            if (registrationTime.compareTo(treatmentTime) > 0) {
                error = error + "\n" + "Ngày điều trị không được nhỏ hơn Ngày đăng ký, ";
            }
            if (StringUtils.isNotEmpty(item.getEndTime())) {
                Date endTime = sdfrmt.parse(item.getEndTime());

                if (treatmentTime.compareTo(endTime) > 0) {
                    error = error + "\n" + "Ngày kết thúc không được nhỏ hơn ngày điều trị, ";
                }
                if (registrationTime.compareTo(endTime) > 0) {
                    error = error + "\n" + "Ngày kết thúc không được nhỏ hơn Ngày đăng ký, ";
                }
                if (StringUtils.isEmpty(item.getEndCase())) {
                    error = error + "\n" + "Lý do kết thúc không được để trống, ";
                }

            }

        } catch (Exception e) {
        }

        return error;
    }

    private boolean checkDateFormat(String inputValue, SimpleDateFormat sdfrmt) {
        if (StringUtils.isEmpty(inputValue)) {
            return true;
        }
        sdfrmt.setLenient(false);
        try {
            sdfrmt.parse(inputValue);
        } catch (ParseException ex) {
            return true;
        }
        return false;
    }

    private OpcArvEntity toArv(OpcTGImportForm item) {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = new OpcArvEntity();

        arv.setCode(item.getCode());
        arv.setSiteID(currentUser.getSite().getID());

        //patient
        OpcPatientEntity patientEntity = new OpcPatientEntity();

        patientEntity.setSiteID(currentUser.getSite().getID());

        patientEntity.setFullName(item.getName());
        patientEntity.setGenderID(item.getGender().equals("Nam") ? "1" : item.getGender().equals("Nữ") ? "2" : "0");
        patientEntity.setDob(StringUtils.isEmpty(item.getDob()) ? null : TextUtils.convertDate("01/01/" + item.getDob(), "dd/MM/yyyy"));
        patientEntity.setConfirmTime(StringUtils.isEmpty(item.getConfirmTime()) ? null : TextUtils.convertDate(item.getConfirmTime(), "dd/MM/yyyy"));
        arv.setPatient(patientEntity);

        //arv
        arv.setPermanentAddress(item.getAddress());
        arv.setPermanentProvinceID(item.getProvince());
        arv.setPermanentDistrictID(item.getDistrict());
        arv.setPermanentWardID(item.getWard());
//
        arv.setCurrentAddress(item.getAddress());
        arv.setCurrentProvinceID(item.getProvince());
        arv.setCurrentDistrictID(item.getDistrict());
        System.out.println("xxxxx" + arv.getCurrentDistrictID());
        arv.setCurrentWardID(item.getWard());
        arv.setRegistrationTime(StringUtils.isEmpty(item.getRegistrationTime()) ? null : TextUtils.convertDate(item.getRegistrationTime(), "dd/MM/yyyy"));
        arv.setTreatmentTime(StringUtils.isEmpty(item.getTreatmentTime()) ? null : TextUtils.convertDate(item.getTreatmentTime(), "dd/MM/yyyy"));
        arv.setEndTime(StringUtils.isEmpty(item.getEndTime()) ? null : TextUtils.convertDate(item.getEndTime(), "dd/MM/yyyy"));

        //get loại đăng ký
        arv.setRegistrationType(item.getRegistrationType());
        //lý do kết thúc
        arv.setEndCase(item.getEndCase());
        arv.setTreatmentRegimenID(item.getTreatmentRegimenID());

        if (arv.getEndTime() == null && arv.getTreatmentTime() != null) {
            arv.setStatusOfTreatmentID("3");
        } else if (arv.getEndTime() == null && arv.getTreatmentTime() == null) {
            arv.setStatusOfTreatmentID("2");
        }

        if (arv.getEndTime() != null && arv.getEndCase() != null) {
            if (arv.getEndCase().equals("1")) {
                arv.setStatusOfTreatmentID("4");
            } else if (arv.getEndCase().equals("4")) {
                arv.setStatusOfTreatmentID("6");
            } else if (arv.getEndCase().equals("2")) {
                arv.setStatusOfTreatmentID("7");
            }
        }
        if (arv.getEndTime() != null && arv.getEndCase() != null && arv.getEndCase().equals("3") && arv.getTreatmentTime() != null) {
            arv.setStatusOfTreatmentID("3");
        }
        if (arv.getEndTime() != null && arv.getEndCase() != null && arv.getEndCase().equals("3") && arv.getTreatmentTime() == null) {
            arv.setStatusOfTreatmentID("2");
        }

        if (arv.getEndTime() != null && !StringUtils.isEmpty(arv.getEndCase())) {
            arv.setTreatmentStageTime(arv.getEndTime());
            if (arv.getEndCase().equals("3")) {
                arv.setTreatmentStageID("3");
                arv.setTranferFromTime(arv.getEndTime());
            } else if (arv.getEndCase().equals("4")) {
                arv.setTreatmentStageID("5");
            } else if (arv.getEndCase().equals("1")) {
                arv.setTreatmentStageID("6");
            } else if (arv.getEndCase().equals("2")) {
                arv.setTreatmentStageID("7");
            } else if (arv.getEndCase().equals("5")) {
                arv.setTreatmentStageID("8");
            }
        } else {
            arv.setTreatmentStageTime(arv.getRegistrationTime());
            if (arv.getRegistrationType().equals("4") || arv.getRegistrationType().equals("5")) {
                arv.setTreatmentStageID("1");
            } else if (arv.getRegistrationType().equals("1")) {
                arv.setTreatmentStageID("1");
            } else if (arv.getRegistrationType().equals("2")) {
                arv.setTreatmentStageID("2");
            } else if (arv.getRegistrationType().equals("3")) {
                arv.setTreatmentStageID("4");
            } else {
                arv.setTreatmentStageID("1");
            }

        }

        return arv;
    }

    @Override
    public OpcTGImportForm mapping(Map<String, String> cols, List<String> cells
    ) {
        OpcTGImportForm item = new OpcTGImportForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    default:
                        item.set(col, cells.get(i) == null || cells.get(i).equals("null") ? "" : cells.get(i));
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
                getLogger().warn(ex.getMessage());
            }
        }

        return item;
    }

}
