package com.gms.controller.importation;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.Cd4SymtomEnum;
import com.gms.entity.constant.HBVSymtomEnum;
import com.gms.entity.constant.HCVSymtomEnum;
import com.gms.entity.constant.InsurancePayEnum;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.constant.LaoDiagnoseEnum;
import com.gms.entity.constant.LaoEndCaseEnum;
import com.gms.entity.constant.LaoSymtomEnum;
import com.gms.entity.constant.MedicationAdherenceEnum;
import com.gms.entity.constant.NTCHEndCaseEnum;
import com.gms.entity.constant.NTCHSymtomEnum;
import com.gms.entity.constant.OpcTestResultEnum;
import com.gms.entity.constant.QuickTreatmentEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.SuspiciousSymptomsEnum;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.constant.TreatmentStageEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.OpcArvImportForm;
import com.gms.entity.validate.ArvImportValidate;
import com.gms.service.OpcArvService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_opc_arv")
public class OpcArvController extends BaseController<OpcArvImportForm> {
    
    @Autowired
    private ArvImportValidate arvImportValidate;
    @Autowired
    private OpcArvService opcArvService;

//    @Autowired
//    private PacPatientImportValidate patientImportValidate;
//
//    @Autowired
//    private PacPatientService pacPatientService;
    @GetMapping(value = {"/import-opc-arv/index.html", "/import-opc-arv/read.html", "/import-opc-arv/validate.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }
    
    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE); //Câu hỏi thẻ BHYT
        parameterTypes.add(ParameterEntity.SUPPORTER_RELATION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_PCR); //Kết quả XN PCR
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT); //Biến động điều trị
        parameterTypes.add(ParameterEntity.REGISTRATION_TYPE); //Loại đăng ký
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE); //Giai đoạn lâm sàng
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT); //Trạng thái điều trị
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN); //Phác đồ điều trị
        parameterTypes.add(ParameterEntity.PLACE_TEST); //Phác đồ điều trị
//        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
//        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.STOP_REGISTRATION_REASON);
        parameterTypes.add(ParameterEntity.ROUTE);
        parameterTypes.add(ParameterEntity.ARV_REGISTRATION_STATUS);
        
        
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        
        addEnumOption(options, ParameterEntity.ANSWER, BooleanEnum.values(), null); //Sử dụng cho câu hỏi có/không
        addEnumOption(options, "has", BooleanEnum.values(), "Chọn câu trả lời"); //Sử dụng enum boolean
        addEnumOption(options, ParameterEntity.INSURANCE_TYPE, InsuranceTypeEnum.values(), "Chọn loại thẻ BHYT");
        addEnumOption(options, ParameterEntity.INSURANCE_PAY, InsurancePayEnum.values(), "Chọn tỷ lệ thanh toán BHYT"); // Chọn tỉ lệ thanh toán BHYT
        addEnumOption(options, ParameterEntity.LAO_SYMTOM, LaoSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.LAO_END_CASE, LaoEndCaseEnum.values(), null); //Lý do kết thúc điều trị Lao
        addEnumOption(options, ParameterEntity.NTCH_SYMTOM, NTCHSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.NTCH_END_CASE, NTCHEndCaseEnum.values(), null); //Lý do kết thúc điều trị NTCH
        addEnumOption(options, ParameterEntity.TREATMENT_REGINMEN_STAGE, TreatmentRegimenStageEnum.values(), "Chọn bậc phác đồ điều trị");
        addEnumOption(options, ParameterEntity.MEDICATION_ADHERENCE, MedicationAdherenceEnum.values(), "Chọn mức độ tuân thủ");
        addEnumOption(options, ParameterEntity.CD4_SYMTOM, Cd4SymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.VIRAL_LOAD_SYMTOM, ViralLoadSymtomEnum.values(), null);
        addEnumOption(options, ParameterEntity.ARV_END_CASE, ArvEndCaseEnum.values(), "Chọn lý do"); //Lý do kết thúc đợt điều trị
        addEnumOption(options, ParameterEntity.TEST_RESULTS, OpcTestResultEnum.values(), "Chọn kết quả"); //Kết quả xét nghiệm
        addEnumOption(options, ParameterEntity.HBV_SYMTOM, HBVSymtomEnum.values(), "Chọn lý Xét nghiệm HBV");
        addEnumOption(options, ParameterEntity.HCV_SYMTOM, HCVSymtomEnum.values(), "Chọn lý Xét nghiệm HCV");
        addEnumOption(options, ParameterEntity.REGISTRATION_TYPE, RegistrationTypeEnum.values(), "Chọn loại đăng ký");
        addEnumOption(options, ParameterEntity.SUSPICIOUS_SYMPTOMS, SuspiciousSymptomsEnum.values(), "Chọn triệu chứng");
        addEnumOption(options, ParameterEntity.LAO_DIAGNOSE, LaoDiagnoseEnum.values(), "Chọn loại hình Lao");
        //Loại bỏ Chưa được điều trị
        options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        //Kết quả XN đầu tiên - Bỏ Không xét nghiệm
//        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        //Sửa theo tltk 21/05/2020
        options.put(ParameterEntity.VIRUS_LOAD, new LinkedHashMap<String, String>());
        options.get(ParameterEntity.VIRUS_LOAD).put("", "Chọn kết quả xn tải lượng virus");
        options.get(ParameterEntity.VIRUS_LOAD).put("1", "Không phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("6", "Dưới ngưỡng phát hiện");
        options.get(ParameterEntity.VIRUS_LOAD).put("2", "Từ ngưỡng phát hiện đến < 200 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("3", "Từ 200 - < 1000 bản sao/ml");
        options.get(ParameterEntity.VIRUS_LOAD).put("4", ">= 1000 bản sao/ml");

        //Lấy thêm htc
        List<SiteEntity> siteHtc = siteService.getSiteHtc(currentUser.getSite().getProvinceID());
        //Nơi xét nghiệm khẳng định
        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentUser.getSite().getProvinceID());
        String key = "siteConfirm";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Chọn nơi xét nghiệm");
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }
        options.get(key).put("-1", "Cơ sở khác");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        options.put(ParameterEntity.TREATMENT_FACILITY, new HashMap<String, String>());
        options.get(ParameterEntity.TREATMENT_FACILITY).put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get(ParameterEntity.TREATMENT_FACILITY).put(String.valueOf(site.getID()), site.getName());
        }

        //Cơ sở chuyển đi
        key = "siteOpcFrom";
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            map.put(String.valueOf(site.getID()), site.getName());
        }
        map.put("-1", "Cơ sở khác");
        options.put(key, map);

        //Cơ sở chuyển đến
        key = "siteOpcTo";
        HashMap<String, String> mSiteOpcTo = new LinkedHashMap<>();
        mSiteOpcTo.put("", "Chọn cơ sở");
        for (SiteEntity site : siteOpc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getName());
        }
        for (SiteEntity site : siteHtc) {
            mSiteOpcTo.put(String.valueOf(site.getID()), site.getName());
        }
        mSiteOpcTo.put("-1", "Cơ sở khác");
        options.put(key, mSiteOpcTo);

        //Cơ sở xét nghiệm CD4
        HashMap<String, String> mapCd4 = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key1 = entry.getKey();
            String value = entry.getValue();
            if (!StringUtils.isEmpty(key1)) {
                mapCd4.put(key1, value);
            }
            
        }
        options.put("siteCd4", mapCd4);
        options.get("siteCd4").put("", "Chọn nơi xét nghiệm");
        options.get("siteCd4").put("-1", "Cơ sở khác");
        for (SiteEntity site : siteHtc) {
            options.get("siteCd4").put(String.valueOf(site.getID()), site.getName());
        }

        //Nơi xét nghiệm sàng lọc
        key = "siteHtc";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }
        
        return options;
    }
    
    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-opc-arv/index.html");
        form.setReadUrl("/import-opc-arv/read.html");
        form.setValidateUrl("/import-opc-arv/validate.html");
        form.setSaveUrl("/import-opc-arv/save.json");
        form.setTitle("Thêm bệnh án sử dụng excel");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setSmallUrl(UrlUtils.opcArvImport());
        form.setTemplate(fileTemplate("opc_arv.xlsx"));
        form.setCols((new OpcArvImportForm()).getAttributeLabels());
        return form;
    }

    /**
     * Action đọc dữ liệu từ file
     *
     * @param model
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping(value = "/import-opc-arv/read.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        ImportForm form = initForm();
        try {
            return renderRead(model, form, file);
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }
    
    @Override
    public OpcArvImportForm mapping(Map<String, String> cols, List<String> cells) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcArvImportForm item = new OpcArvImportForm();
        
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null || StringUtils.isBlank(cells.get(i))) {
                continue;
            }
            try {
                switch (col) {
                    case "genderID":
                        HashMap<String, String> option = options.get(ParameterEntity.GENDER);
                        Set<String> optionKeys = option.keySet();
                        for (String key : optionKeys) {
                            if (TextUtils.removeDiacritical(option.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setGenderID(key);
                                break;
                            }
                        }
                        break;
                    case "raceID":
                        HashMap<String, String> raceOption = options.get(ParameterEntity.RACE);
                        Set<String> raceKeys = raceOption.keySet();
                        for (String key : raceKeys) {
                            if (TextUtils.removeDiacritical(raceOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setRaceID(key);
                                break;
                            }
                        }
                        break;
                    case "jobID":
                        HashMap<String, String> jobGroupOption = options.get(ParameterEntity.JOB);
                        Set<String> jobKeys = jobGroupOption.keySet();
                        for (String key : jobKeys) {
                            if (TextUtils.removeDiacritical(jobGroupOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setJobID(key);
                                break;
                            }
                        }
                        break;
                    case "objectGroupID":
                        HashMap<String, String> objectGroupOption = options.get(ParameterEntity.TEST_OBJECT_GROUP);
                        Set<String> objectGroup = objectGroupOption.keySet();
                        for (String key : objectGroup) {
                            if (TextUtils.removeDiacritical(objectGroupOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setObjectGroupID(key);
                                break;
                            }
                        }
                        break;
                    case "insurance":
                        HashMap<String, String> optioninsurance = options.get("health-insurance");
                        Set<String> optioninsuranceKeys = optioninsurance.keySet();
                        for (String key : optioninsuranceKeys) {
                            if (TextUtils.removeDiacritical(optioninsurance.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setInsurance(key);
                                break;
                            }
                        }
                        break;
                    case "insuranceType":
                        HashMap<String, String> insuranceTypeOption = options.get(ParameterEntity.INSURANCE_TYPE);
                        Set<String> insuranceTypeKeys = insuranceTypeOption.keySet();
                        for (String key : insuranceTypeKeys) {
                            if (TextUtils.removeDiacritical(insuranceTypeOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setInsuranceType(key);
                                break;
                            }
                        }
                        break;
                    case "insurancePay":
                        HashMap<String, String> insurancePayOption = options.get(ParameterEntity.INSURANCE_PAY);
                        Set<String> insurancePayKeys = insurancePayOption.keySet();
                        for (String key : insurancePayKeys) {
                            if (TextUtils.removeDiacritical(insurancePayOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setInsurancePay(key);
                                break;
                            }
                        }
                        break;
                    case "pcrOneResult":
                        HashMap<String, String> pcrOneResultOption = options.get(ParameterEntity.TEST_RESULTS_PCR);
                        Set<String> pcrOneResultKeys = pcrOneResultOption.keySet();
                        for (String key : pcrOneResultKeys) {
                            if (TextUtils.removeDiacritical(pcrOneResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setPcrOneResult(key);
                                break;
                            }
                        }
                        break;
                    case "pcrTwoResult":
                        HashMap<String, String> pcrTwoResultOption = options.get(ParameterEntity.TEST_RESULTS_PCR);
                        Set<String> pcrTwoResultKeys = pcrTwoResultOption.keySet();
                        for (String key : pcrTwoResultKeys) {
                            if (TextUtils.removeDiacritical(pcrTwoResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setPcrTwoResult(key);
                                break;
                            }
                        }
                        break;
                    case "therapySiteID":
                        HashMap<String, String> therapySiteIDOption = options.get(ParameterEntity.TREATMENT_FACILITY);
                        Set<String> therapySiteIDKeys = therapySiteIDOption.keySet();
                        for (String key : therapySiteIDKeys) {
                            if (TextUtils.removeDiacritical(therapySiteIDOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setTherapySiteID(key);
                                break;
                            }
                        }
                        break;
                    case "registrationType":
                        HashMap<String, String> registrationTypeOption = options.get(ParameterEntity.REGISTRATION_TYPE);
                        Set<String> registrationTypeKeys = registrationTypeOption.keySet();
                        for (String key : registrationTypeKeys) {
                            if (TextUtils.removeDiacritical(registrationTypeOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setRegistrationType(key);
                                break;
                            }
                        }
                        break;
                    case "clinicalStage":
                        HashMap<String, String> clinicalStageOption = options.get(ParameterEntity.CLINICAL_STAGE);
                        Set<String> clinicalStageKeys = clinicalStageOption.keySet();
                        for (String key : clinicalStageKeys) {
                            if (TextUtils.removeDiacritical(clinicalStageOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setClinicalStage(key);
                                break;
                            }
                        }
                        break;
                    case "lao":
                        HashMap<String, String> laoOption = options.get("has");
                        Set<String> laoKeys = laoOption.keySet();
                        for (String key : laoKeys) {
                            if (TextUtils.removeDiacritical(laoOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setLao(key);
                                break;
                            }
                        }
                        break;
                    case "laoSymptoms":
                        HashMap<String, String> laoSymptomsOption = options.get(ParameterEntity.LAO_SYMTOM);
                        Set<String> laoSymptomsKeys = laoSymptomsOption.keySet();
                        for (String key : laoSymptomsKeys) {
                            if (TextUtils.removeDiacritical(laoSymptomsOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setLaoSymptoms(key);
                                break;
                            }
                        }
                        break;
                    case "inh":
                        HashMap<String, String> inhOption = options.get("has");
                        Set<String> inhKeys = inhOption.keySet();
                        for (String key : inhKeys) {
                            if (TextUtils.removeDiacritical(inhOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setInh(key);
                                break;
                            }
                        }
                        break;
                    case "inhEndCauses":
                        HashMap<String, String> inhEndCausesOption = options.get(ParameterEntity.LAO_END_CASE);
                        Set<String> inhEndCausesKeys = inhEndCausesOption.keySet();
                        for (String key : inhEndCausesKeys) {
                            if (TextUtils.removeDiacritical(inhEndCausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setInhEndCauses(key);
                                break;
                            }
                        }
                        break;
                    case "ntch":
                        HashMap<String, String> ntchOption = options.get("has");
                        Set<String> ntchKeys = ntchOption.keySet();
                        for (String key : ntchKeys) {
                            if (TextUtils.removeDiacritical(ntchOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setNtch(key);
                                break;
                            }
                        }
                        break;
                    case "ntchSymptoms":
                        HashMap<String, String> ntchSymptomsOption = options.get(ParameterEntity.NTCH_SYMTOM);
                        Set<String> ntchSymptomsKeys = ntchSymptomsOption.keySet();
                        for (String key : ntchSymptomsKeys) {
                            if (TextUtils.removeDiacritical(ntchSymptomsOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setNtchSymptoms(key);
                                break;
                            }
                        }
                        break;
                    case "cotrimoxazoleEndCauses":
                        HashMap<String, String> cotrimoxazoleEndCausesOption = options.get(ParameterEntity.NTCH_END_CASE);
                        Set<String> cotrimoxazoleEndCausesKeys = cotrimoxazoleEndCausesOption.keySet();
                        for (String key : cotrimoxazoleEndCausesKeys) {
                            if (TextUtils.removeDiacritical(cotrimoxazoleEndCausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setCotrimoxazoleEndCauses(key);
                                break;
                            }
                        }
                        break;
                    case "statusOfTreatmentID":
                        HashMap<String, String> statusOfTreatmentIDOption = options.get(ParameterEntity.STATUS_OF_TREATMENT);
                        Set<String> statusOfTreatmentIDKeys = statusOfTreatmentIDOption.keySet();
                        for (String key : statusOfTreatmentIDKeys) {
                            if (TextUtils.removeDiacritical(statusOfTreatmentIDOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setStatusOfTreatmentID(key);
                                break;
                            }
                        }
                        break;
                    case "firstTreatmentRegimenID":
                        HashMap<String, String> firstTreatmentRegimenIDOption = options.get(ParameterEntity.TREATMENT_REGIMEN);
                        Set<String> firstTreatmentRegimenIDKeys = firstTreatmentRegimenIDOption.keySet();
                        for (String key : firstTreatmentRegimenIDKeys) {
                            if (TextUtils.removeDiacritical(firstTreatmentRegimenIDOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setFirstTreatmentRegimenID(key);
                                break;
                            }
                        }
                        break;
                    case "treatmentRegimenID":
                        HashMap<String, String> treatmentRegimenIDOption = options.get(ParameterEntity.TREATMENT_REGIMEN);
                        Set<String> treatmentRegimenIDKeys = treatmentRegimenIDOption.keySet();
                        for (String key : treatmentRegimenIDKeys) {
                            if (TextUtils.removeDiacritical(treatmentRegimenIDOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setTreatmentRegimenID(key);
                                break;
                            }
                        }
                        break;
                    case "medicationAdherence":
                        HashMap<String, String> medicationAdherenceOption = options.get(ParameterEntity.MEDICATION_ADHERENCE);
                        Set<String> medicationAdherenceKeys = medicationAdherenceOption.keySet();
                        for (String key : medicationAdherenceKeys) {
                            if (TextUtils.removeDiacritical(medicationAdherenceOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setMedicationAdherence(key);
                                break;
                            }
                        }
                        break;
                    case "firstCd4Causes":
                        HashMap<String, String> firstCd4CausesOption = options.get(ParameterEntity.CD4_SYMTOM);
                        Set<String> firstCd4CausesKeys = firstCd4CausesOption.keySet();
                        for (String key : firstCd4CausesKeys) {
                            if (TextUtils.removeDiacritical(firstCd4CausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setFirstCd4Causes(key);
                                break;
                            }
                        }
                        break;
                    case "cd4Causes":
                        HashMap<String, String> Cd4CausesOption = options.get(ParameterEntity.CD4_SYMTOM);
                        Set<String> Cd4CausesKeys = Cd4CausesOption.keySet();
                        for (String key : Cd4CausesKeys) {
                            if (TextUtils.removeDiacritical(Cd4CausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setCd4Causes(key);
                                break;
                            }
                        }
                        break;
                    case "firstViralLoadCauses":
                        HashMap<String, String> firstViralLoadCausesOption = options.get(ParameterEntity.VIRAL_LOAD_SYMTOM);
                        Set<String> firstViralLoadCausesKeys = firstViralLoadCausesOption.keySet();
                        for (String key : firstViralLoadCausesKeys) {
                            if (TextUtils.removeDiacritical(firstViralLoadCausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setFirstViralLoadCauses(key);
                                break;
                            }
                        }
                        break;
                    case "viralLoadCauses":
                        HashMap<String, String> viralLoadCausesOption = options.get(ParameterEntity.VIRAL_LOAD_SYMTOM);
                        Set<String> viralLoadCausesKeys = viralLoadCausesOption.keySet();
                        for (String key : viralLoadCausesKeys) {
                            if (TextUtils.removeDiacritical(viralLoadCausesOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setViralLoadCauses(key);
                                break;
                            }
                        }
                        break;
                    case "hbv":
                        HashMap<String, String> hbvOption = options.get("has");
                        Set<String> hbvKeys = hbvOption.keySet();
                        for (String key : hbvKeys) {
                            if (TextUtils.removeDiacritical(hbvOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setHbv(key);
                                break;
                            }
                        }
                        break;
                    case "hbvResult":
                        HashMap<String, String> hbvResultOption = options.get(ParameterEntity.TEST_RESULTS);
                        Set<String> hbvResultKeys = hbvResultOption.keySet();
                        for (String key : hbvResultKeys) {
                            if (TextUtils.removeDiacritical(hbvResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setHbvResult(key);
                                break;
                            }
                        }
                        break;
                    case "hcv":
                        HashMap<String, String> hcvOption = options.get("has");
                        Set<String> hcvKeys = hcvOption.keySet();
                        for (String key : hcvKeys) {
                            if (TextUtils.removeDiacritical(hcvOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setHcv(key);
                                break;
                            }
                        }
                        break;
                    case "hcvResult":
                        HashMap<String, String> hcvResultOption = options.get(ParameterEntity.TEST_RESULTS);
                        Set<String> hcvResultKeys = hcvResultOption.keySet();
                        for (String key : hcvResultKeys) {
                            if (TextUtils.removeDiacritical(hcvResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setHcvResult(key);
                                break;
                            }
                        }
                        break;
                    case "endCase":
                        HashMap<String, String> endCaseOption = options.get(ParameterEntity.ARV_END_CASE);
                        Set<String> endCaseKeys = endCaseOption.keySet();
                        for (String key : endCaseKeys) {
                            if (TextUtils.removeDiacritical(endCaseOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setEndCase(key);
                                break;
                            }
                        }
                        break;
                    case "treatmentStageID":
                        HashMap<String, String> treatmentStageIDOption = options.get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);
                        Set<String> treatmentStageIDKeys = treatmentStageIDOption.keySet();
                        for (String key : treatmentStageIDKeys) {
                            if (TextUtils.removeDiacritical(treatmentStageIDOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setTreatmentStageID(key);
                                break;
                            }
                        }
                        break;
                    case "supporterRelation":
                        HashMap<String, String> supporterRelationOption = options.get(ParameterEntity.SUPPORTER_RELATION);
                        Set<String> supporterRelationKeys = supporterRelationOption.keySet();
                        for (String key : supporterRelationKeys) {
                            if (TextUtils.removeDiacritical(supporterRelationOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSupporterRelation(key);
                                break;
                            }
                        }
                        break;
                    case "treatmentRegimenStage":
                        HashMap<String, String> treatmentRegimenStageOption = options.get(ParameterEntity.TREATMENT_REGINMEN_STAGE);
                        Set<String> treatmentRegimenStageKeys = treatmentRegimenStageOption.keySet();
                        for (String key : treatmentRegimenStageKeys) {
                            if (TextUtils.removeDiacritical(treatmentRegimenStageOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setTreatmentRegimenStage(key);
                                break;
                            }
                        }
                        break;
                    case "firstViralLoadResult":
                        HashMap<String, String> firstViralLoadResultOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> firstViralLoadResultKeys = firstViralLoadResultOption.keySet();
                        for (String key : firstViralLoadResultKeys) {
                            if (TextUtils.removeDiacritical(firstViralLoadResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setFirstViralLoadResult(key);
                                break;
                            }
                        }
                        break;
                    case "viralLoadResult":
                        HashMap<String, String> viralLoadResultOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> viralLoadResultKeys = viralLoadResultOption.keySet();
                        for (String key : viralLoadResultKeys) {
                            if (TextUtils.removeDiacritical(viralLoadResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setViralLoadResult(key);
                                break;
                            }
                        }
                        break;
                    case "laoResult":
                        HashMap<String, String> laoResultOption = options.get(ParameterEntity.TEST_RESULTS);
                        Set<String> laoResultKeys = laoResultOption.keySet();
                        for (String key : laoResultKeys) {
                            if (TextUtils.removeDiacritical(laoResultOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setLaoResult(key);
                                break;
                            }
                        }
                        break;
                    case "laoTreatment":
                        HashMap<String, String> laoTreatmentOption = options.get("has");
                        Set<String> laoTreatmentKeys = laoTreatmentOption.keySet();
                        for (String key : laoTreatmentKeys) {
                            if (TextUtils.removeDiacritical(laoTreatmentOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setLaoTreatment(key);
                                break;
                            }
                        }
                        break;
                    case "route":
                        HashMap<String, String> routeOption = options.get(ParameterEntity.ROUTE);
                        Set<String> routeKeys = routeOption.keySet();
                        for (String key : routeKeys) {
                            if (TextUtils.removeDiacritical(routeOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setRoute(key);
                                break;
                            }
                        }
                        break;
                    case "registrationStatus":
                        HashMap<String, String> arvRegistrationStatusOption = options.get(ParameterEntity.ARV_REGISTRATION_STATUS);
                        Set<String> arvRegistrationStatusKeys = arvRegistrationStatusOption.keySet();
                        for (String key : arvRegistrationStatusKeys) {
                            if (TextUtils.removeDiacritical(arvRegistrationStatusOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setRegistrationStatus(key);
                                break;
                            }
                        }
                        break;
                    case "suspiciousSymptoms":
                        HashMap<String, String> suspiciousSymptomsOption = options.get(ParameterEntity.SUSPICIOUS_SYMPTOMS);
                        Set<String> suspiciousSymptomsKeys = suspiciousSymptomsOption.keySet();
                        for (String key : suspiciousSymptomsKeys) {
                            if (TextUtils.removeDiacritical(suspiciousSymptomsOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSuspiciousSymptoms(key);
                                break;
                            }
                        }
                        break;
                    case "examinationAndTest":
                        HashMap<String, String> examinationAndTestOption = options.get("has");
                        Set<String> examinationAndTestKeys = examinationAndTestOption.keySet();
                        for (String key : examinationAndTestKeys) {
                            if (TextUtils.removeDiacritical(examinationAndTestOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setExaminationAndTest(key);
                                break;
                            }
                        }
                        break;
                    case "laoDiagnose":
                        HashMap<String, String> laoDiagnoseOption = options.get(ParameterEntity.LAO_DIAGNOSE);
                        Set<String> laoDiagnoseKeys = laoDiagnoseOption.keySet();
                        for (String key : laoDiagnoseKeys) {
                            if (TextUtils.removeDiacritical(laoDiagnoseOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setLaoDiagnose(key);
                                break;
                            }
                        }
                        break;
                    case "passTreatment":
                        HashMap<String, String> passTreatmentOption = options.get("has");
                        Set<String> passTreatmentKeys = passTreatmentOption.keySet();
                        for (String key : passTreatmentKeys) {
                            if (TextUtils.removeDiacritical(passTreatmentOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setPassTreatment(key);
                                break;
                            }
                        }
                        break;
                    case "fullName":
                        item.set(col, TextUtils.toFullname(String.valueOf(cells.get(i))));
                        break;    
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }
    
    @PostMapping(value = "/import-opc-arv/validate.html")
    public String actionValidate(Model model,
            @ModelAttribute("form") ImportForm input,
            RedirectAttributes redirectAttributes) {
        
        ImportForm form = initForm();
        form.setMapCols(input.getMapCols());
        form.setFilePath(input.getFilePath());
        try {
            return renderValidate(model, "importation/opc-arv/validate", form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }
    
    @PostMapping(value = "/import-opc-arv/save.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<?> actionSave(@Valid @RequestBody OpcArvImportForm form,
            BindingResult bindingResult) {
        
        LoggedUser currentUser = getCurrentUser();
        String currentSiteID = currentUser.getSite().getID().toString();
        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        if (isOPC() && !isOpcManager()) {
            form.setTherapySiteID(currentSiteID);
        }

        //set các giá trị mặc định trước khi validate
        form.setTherapySiteID(currentSiteID);
            if (!StringUtils.isEmpty(form.getTransferSiteName())) {
                form.setTransferSiteID("-1");
            }
            if (StringUtils.isNotEmpty(form.getSourceSiteName())) {
                form.setSourceSiteID("-1");
            }
            if (!StringUtils.isEmpty(form.getConfirmSiteName())) {
                form.setConfirmSiteID("-1");
            }
        //validate
        arvImportValidate.validate(form, bindingResult);
        
        
        
        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }

        //Lưu
        try {
            //Set form to entity
            
            OpcArvEntity arv = form.setToEntity(null);
            if(arv.getRegistrationTime() != null && arv.getTreatmentTime() != null && arv.isPassTreatment()){
                long diff = getWorkingDaysBetweenTwoDates(arv.getRegistrationTime(), arv.getTreatmentTime());
                if(diff == Long.parseLong(QuickTreatmentEnum.TRONG_NGAY.getKey())){
                    arv.setQuickByTreatmentTime(QuickTreatmentEnum.TRONG_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.MOT_NGAY.getKey())){
                    arv.setQuickByTreatmentTime(QuickTreatmentEnum.MOT_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.HAI_NGAY.getKey())){
                    arv.setQuickByTreatmentTime(QuickTreatmentEnum.HAI_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.BA_NGAY.getKey())){
                    arv.setQuickByTreatmentTime(QuickTreatmentEnum.BA_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.BON_DEN_BAY.getKey())){
                    arv.setQuickByTreatmentTime(QuickTreatmentEnum.BON_DEN_BAY.getKey());
                }
            }
            if(arv.getPatient().getConfirmTime() != null && arv.getTreatmentTime() != null && arv.isPassTreatment()){

                long diff = getWorkingDaysBetweenTwoDates(arv.getPatient().getConfirmTime(), arv.getTreatmentTime());
                if(diff == Long.parseLong(QuickTreatmentEnum.TRONG_NGAY.getKey())){
                    arv.setQuickByConfirmTime(QuickTreatmentEnum.TRONG_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.MOT_NGAY.getKey())){
                    arv.setQuickByConfirmTime(QuickTreatmentEnum.MOT_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.HAI_NGAY.getKey())){
                    arv.setQuickByConfirmTime(QuickTreatmentEnum.HAI_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.BA_NGAY.getKey())){
                    arv.setQuickByConfirmTime(QuickTreatmentEnum.BA_NGAY.getKey());
                }
                if(diff == Long.parseLong(QuickTreatmentEnum.BON_DEN_BAY.getKey())){
                    arv.setQuickByConfirmTime(QuickTreatmentEnum.BON_DEN_BAY.getKey());
                }
            }
            
            if(arv.getTranferFromTime() != null){
                arv.setTreatmentStageID(TreatmentStageEnum.MOVED_AWAY.getKey());
                arv.setEndCase(ArvEndCaseEnum.MOVED_AWAY.getKey());
                arv.setTreatmentStageTime(arv.getTranferFromTime());
                arv.setEndTime(arv.getTranferFromTime());
            }
            opcArvService.create(arv);
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Thêm mới bệnh án từ import excel");
            if (arv == null) {
                return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
            }
            return new Response<>(true, arv);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi cập nhật thông tin. ");
        }
    }
    
    public static long getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);        

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        return workDays;
    }
    
}
