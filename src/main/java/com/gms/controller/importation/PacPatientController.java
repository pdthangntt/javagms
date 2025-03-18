package com.gms.controller.importation;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.InsuranceEnum;
import com.gms.entity.constant.InsuranceTypeEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.PacPatientImportForm;
import com.gms.entity.validate.PacPatientImportValidate;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

@Controller(value = "importation_pac_patient")
public class PacPatientController extends BaseController<PacPatientImportForm> {

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private PacPatientImportValidate patientImportValidate;

    @Autowired
    private PacPatientService pacPatientService;

    @GetMapping(value = {"/import-pac-patient/index.html", "/import-pac-patient/read.html", "/import-pac-patient/validate.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.TYPE_OF_PATIENT);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.SYSMPTOM);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.AIDS_STATUS);
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE);
        options = parameterService.getOptionsByTypes(parameterTypes, null);

        // Lọc lại danh sách Nguồn thông tin người nhiễm
        HashMap<String, String> service = options.get(ParameterEntity.SERVICE);
        if (service.isEmpty()) {
            return options;
        }

        options.put(ParameterEntity.SERVICE, new HashMap<String, String>());
        options.get(ParameterEntity.SERVICE).put("100", service.get("100"));
        options.get(ParameterEntity.SERVICE).put("500", service.get("500"));
        options.get(ParameterEntity.SERVICE).put("103", service.get("103"));
        options.get(ParameterEntity.SERVICE).put("ttyt", service.get("ttyt"));
        options.get(ParameterEntity.SERVICE).put("tyt", service.get("tyt"));

        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-pac-patient/index.html");
        form.setReadUrl("/import-pac-patient/read.html");
        form.setValidateUrl("/import-pac-patient/validate.html");
        form.setSaveUrl("/import-pac-patient/save.json");
        form.setTitle("Thêm khách hàng sử dụng excel");
        form.setSmallTitle("Quản lý người nhiễm");
        form.setSmallUrl(UrlUtils.pacPatientImport());
        form.setTemplate(fileTemplate("pac.xlsx"));
        form.setCols((new PacPatientInfoEntity()).getAttributeLabels());
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
    @PostMapping(value = "/import-pac-patient/read.html")
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
    public PacPatientImportForm mapping(Map<String, String> cols, List<String> cells) {
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientImportForm item = new PacPatientImportForm();

        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    case "genderID":
                        HashMap<String, String> genderOption = options.get(ParameterEntity.GENDER);
                        Set<String> genderKeys = genderOption.keySet();
                        for (String key : genderKeys) {
                            if (TextUtils.removeDiacritical(genderOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setGenderID(key);
                                break;
                            }
                        }
                        break;
                    case "yearOfBirth":
                        item.setYearOfBirth(cells.get(i) == null ? "" : cells.get(i).replaceFirst("\\.0+$", ""));
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
                    case "modeOfTransmissionID":
                        HashMap<String, String> modeOfTransmissionOption = options.get(ParameterEntity.MODE_OF_TRANSMISSION);
                        Set<String> modeOfTransmissionKeys = modeOfTransmissionOption.keySet();
                        for (String key : modeOfTransmissionKeys) {
                            if (TextUtils.removeDiacritical(modeOfTransmissionOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setModeOfTransmissionID(key);
                                break;
                            }
                        }
                        break;
                    case "statusOfResidentID":
                        HashMap<String, String> statusOfResidentOption = options.get(ParameterEntity.STATUS_OF_RESIDENT);
                        Set<String> statusOfResidentKeys = statusOfResidentOption.keySet();
                        for (String key : statusOfResidentKeys) {
                            if (TextUtils.removeDiacritical(statusOfResidentOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setStatusOfResidentID(key);
                                break;
                            }
                        }
                        break;
                    case "sourceServiceID":
                        HashMap<String, String> sourceServiceOption = options.get(ParameterEntity.SERVICE);
                        Set<String> sourceServiceKeys = sourceServiceOption.keySet();
                        for (String key : sourceServiceKeys) {
                            if (TextUtils.removeDiacritical(sourceServiceOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSourceServiceID(key);
                                break;
                            }
                        }
                        break;
                    case "siteConfirmID":
                        HashMap<String, String> siteConfirmOption = options.get(ParameterEntity.PLACE_TEST);
                        Set<String> siteConfirmKeys = siteConfirmOption.keySet();
                        for (String key : siteConfirmKeys) {
                            if (TextUtils.removeDiacritical(siteConfirmOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSiteConfirmID(key);
                                break;
                            }
                        }
                        break;
                    case "confirmTime":
                        item.setConfirmTime(cells.get(i));
                        break;
                    case "bloodBaseID":
                        HashMap<String, String> bloodBaseOption = options.get(ParameterEntity.BLOOD_BASE);
                        Set<String> bloodBaseKeys = bloodBaseOption.keySet();
                        for (String key : bloodBaseKeys) {
                            if (TextUtils.removeDiacritical(bloodBaseOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setBloodBaseID(key);
                                break;
                            }
                        }
                        break;
                    case "statusOfTreatmentID":
                        HashMap<String, String> statusOfTreatmentOption = options.get(ParameterEntity.STATUS_OF_TREATMENT);
                        Set<String> statusOfTreatmentKeys = statusOfTreatmentOption.keySet();
                        for (String key : statusOfTreatmentKeys) {
                            if (TextUtils.removeDiacritical(statusOfTreatmentOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setStatusOfTreatmentID(key);
                                break;
                            }
                        }
                        break;
                    case "startTreatmentTime":
                        item.setStartTreatmentTime(cells.get(i));
                        break;
                    case "siteTreatmentFacilityID":
                        HashMap<String, String> siteTreatmentFacilityOption = options.get(ParameterEntity.TREATMENT_FACILITY);
                        Set<String> siteTreatmentFacilityKeys = siteTreatmentFacilityOption.keySet();
                        for (String key : siteTreatmentFacilityKeys) {
                            if (TextUtils.removeDiacritical(siteTreatmentFacilityOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSiteTreatmentFacilityID(key);
                                break;
                            }
                        }
                        break;
                    case "treatmentRegimenID":
                        HashMap<String, String> treatmentRegimenOption = options.get(ParameterEntity.TREATMENT_REGIMEN);
                        Set<String> treatmentRegimenIDKeys = treatmentRegimenOption.keySet();
                        for (String key : treatmentRegimenIDKeys) {
                            if (treatmentRegimenOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setTreatmentRegimenID(key);
                                break;
                            }
                        }
                        break;
                    case "symptomID":
                        HashMap<String, String> symptomOption = options.get(ParameterEntity.SYSMPTOM);
                        Set<String> symptomKeys = symptomOption.keySet();
                        for (String key : symptomKeys) {
                            if (TextUtils.removeDiacritical(symptomOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setSymptomID(key);
                                break;
                            }
                        }
                        break;
                    case "riskBehaviorID":
                        HashMap<String, String> riskBehaviorOption = options.get(ParameterEntity.RISK_BEHAVIOR);
                        Set<String> riskBehaviorKeys = riskBehaviorOption.keySet();
                        for (String key : riskBehaviorKeys) {
                            if (riskBehaviorOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setRiskBehaviorID(key);
                            }
                        }
                        break;
                    case "causeOfDeath":
                        HashMap<String, String> causeOfDeathOption = options.get(ParameterEntity.CAUSE_OF_DEATH);
                        Set<String> causeOfDeathKeys = causeOfDeathOption.keySet();
                        for (String key : causeOfDeathKeys) {
                            if (causeOfDeathOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setCauseOfDeath(key);
                            }
                        }
                        break;
                    case "virusLoadConfirm":
                        HashMap<String, String> virusLoadConfirmOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> virusLoadConfirmKeys = virusLoadConfirmOption.keySet();
                        for (String key : virusLoadConfirmKeys) {
                            if (virusLoadConfirmOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setVirusLoadConfirm(key);
                            }
                        }
                        break;
                    case "virusLoadArv":
                        HashMap<String, String> virusLoadArvOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> virusLoadArvKeys = virusLoadArvOption.keySet();
                        for (String key : virusLoadArvKeys) {
                            if (virusLoadArvOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setVirusLoadArv(key);
                            }
                        }
                        break;
                    case "virusLoadArvCurrent":
                        HashMap<String, String> virusLoadArvCurrentOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> virusLoadArvCurrentKeys = virusLoadArvCurrentOption.keySet();
                        for (String key : virusLoadArvCurrentKeys) {
                            if (virusLoadArvCurrentOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setVirusLoadArvCurrent(key);
                            }
                        }
                        break;
                    case "statusOfChangeTreatmentID":
                        HashMap<String, String> statusOfChangeTreatmentIDOption = options.get(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);
                        Set<String> statusOfChangeTreatmentIDKeys = statusOfChangeTreatmentIDOption.keySet();
                        for (String key : statusOfChangeTreatmentIDKeys) {
                            if (statusOfChangeTreatmentIDOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setStatusOfChangeTreatmentID(key);
                            }
                        }
                        break;
                    case "aidsStatus":
                        HashMap<String, String> aidsStatusOption = options.get(ParameterEntity.AIDS_STATUS);
                        Set<String> aidsStatusKeys = aidsStatusOption.keySet();
                        for (String key : aidsStatusKeys) {
                            if (aidsStatusOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setAidsStatus(key);
                            }
                        }
                        break;
                    case "clinicalStage":
                        HashMap<String, String> clinicalStageOption = options.get(ParameterEntity.CLINICAL_STAGE);
                        Set<String> clinicalStageKeys = clinicalStageOption.keySet();
                        for (String key : clinicalStageKeys) {
                            if (clinicalStageOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setClinicalStage(key);
                            }
                        }
                        break;
                    case "earlyHiv":
                        HashMap<String, String> earlyHivOption = options.get(ParameterEntity.EARLY_HIV);
                        Set<String> earlyHivKeys = earlyHivOption.keySet();
                        for (String key : earlyHivKeys) {
                            if (earlyHivOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setEarlyHiv(key);
                            }
                        }
                        break;
                    case "deathTime":
                        item.setDeathTime(cells.get(i));
                        break;
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }

    @PostMapping(value = "/import-pac-patient/validate.html")
    public String actionValidate(Model model,
            @ModelAttribute("form") ImportForm input,
            RedirectAttributes redirectAttributes) {

        ImportForm form = initForm();
        form.setMapCols(input.getMapCols());
        form.setFilePath(input.getFilePath());
        try {
            return renderValidate(model, "importation/pac/patient", form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @PostMapping(value = "/import-pac-patient/save.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<?> actionSave(@Valid @RequestBody PacPatientImportForm form,
            BindingResult bindingResult) {

        patientImportValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }
        PacPatientInfoEntity pacPatient = new PacPatientInfoEntity();
        try {
            pacPatient.setYearOfBirth(form.getYearOfBirth() != null ? Integer.parseInt(form.getYearOfBirth()) : 0);
        } catch (Exception e) {
        }
        pacPatient.setFullname(form.getFullname());
        pacPatient.setGenderID(form.getGenderID());
        pacPatient.setRaceID(form.getRaceID());
        pacPatient.setJobID(form.getJobID());
        pacPatient.setHealthInsuranceNo(form.getHealthInsuranceNo());
        pacPatient.setHasHealthInsurance(StringUtils.isNotEmpty(pacPatient.getHealthInsuranceNo()) ? InsuranceEnum.TRUE.getKey() : null);
        pacPatient.setIdentityCard(form.getIdentityCard());
        pacPatient.setObjectGroupID(form.getObjectGroupID());
        pacPatient.setModeOfTransmissionID(form.getModeOfTransmissionID());
        List<String> risk = new ArrayList<>();
        risk.add(form.getRiskBehaviorID());
        pacPatient.setRiskBehaviorID(risk);
        List<String> cause = new ArrayList<>();
        cause.add(form.getCauseOfDeath());
        pacPatient.setCauseOfDeath(cause);

        pacPatient.setPermanentAddressNo(form.getPermanentAddressNo());
        pacPatient.setPermanentAddressGroup(form.getPermanentAddressGroup());
        pacPatient.setPermanentAddressStreet(form.getPermanentAddressStreet());
        pacPatient.setPermanentProvinceID(form.getPermanentProvinceID());
        pacPatient.setPermanentDistrictID(form.getPermanentDistrictID());
        pacPatient.setPermanentWardID(form.getPermanentWardID());
        pacPatient.setSourceServiceID(StringUtils.isEmpty(form.getSourceServiceID()) ? "" : form.getSourceServiceID());

        pacPatient.setCurrentAddressNo(form.getCurrentAddressNo());
        pacPatient.setCurrentAddressGroup(form.getCurrentAddressGroup());
        pacPatient.setCurrentAddressStreet(form.getCurrentAddressStreet());
        pacPatient.setCurrentProvinceID(form.getCurrentProvinceID());
        pacPatient.setCurrentDistrictID(form.getCurrentDistrictID());
        pacPatient.setCurrentWardID(form.getCurrentWardID());

        pacPatient.setStatusOfResidentID(form.getStatusOfResidentID());
        pacPatient.setConfirmTime(TextUtils.convertDate(form.getConfirmTime(), FORMATDATE));

        pacPatient.setSiteConfirmID(form.getSiteConfirmID());
        pacPatient.setBloodBaseID(form.getBloodBaseID());
        pacPatient.setStatusOfTreatmentID(form.getStatusOfTreatmentID());
        pacPatient.setStartTreatmentTime(TextUtils.convertDate(form.getStartTreatmentTime(), FORMATDATE));
        pacPatient.setSiteTreatmentFacilityID(form.getSiteTreatmentFacilityID());
        pacPatient.setTreatmentRegimenID(form.getTreatmentRegimenID());
        pacPatient.setSymptomID(form.getSymptomID());
        pacPatient.setDeathTime(TextUtils.convertDate(form.getDeathTime(), FORMATDATE));

        //Mặc định cơ sở phát hiện
        pacPatient.setDetectProvinceID(getCurrentUser().getSite().getProvinceID());
        pacPatient.setDetectDistrictID(getCurrentUser().getSite().getDistrictID());
        //Quản lý
        pacPatient.setProvinceID(getCurrentUser().getSite().getProvinceID());
        pacPatient.setDistrictID(getCurrentUser().getSite().getDistrictID());
        pacPatient.setWardID(getCurrentUser().getSite().getWardID());

        pacPatient.setPatientPhone(form.getPatientPhone());
        pacPatient.setConfirmCode(form.getConfirmCode());
        pacPatient.setEarlyHiv(form.getEarlyHiv());
        pacPatient.setEarlyHivTime(TextUtils.convertDate(form.getEarlyHivTime(), FORMATDATE));
        pacPatient.setVirusLoadConfirm(form.getVirusLoadConfirm());
        pacPatient.setVirusLoadConfirmDate(TextUtils.convertDate(form.getVirusLoadConfirmDate(), FORMATDATE));
        pacPatient.setCdFourResult(form.getCdFourResult() == null || form.getCdFourResult().equals("") ? Long.valueOf("0") : Long.valueOf(form.getCdFourResult()));
        pacPatient.setCdFourResultDate(TextUtils.convertDate(form.getCdFourResultDate(), FORMATDATE));
        pacPatient.setCdFourResultCurrent(form.getCdFourResultCurrent() == null || form.getCdFourResultCurrent().equals("") ? Long.valueOf("0") : Long.valueOf(form.getCdFourResultCurrent()));
        pacPatient.setCdFourResultLastestDate(TextUtils.convertDate(form.getCdFourResultLastestDate(), FORMATDATE));
        pacPatient.setVirusLoadArv(form.getVirusLoadArv());
        pacPatient.setVirusLoadArvDate(TextUtils.convertDate(form.getVirusLoadArvDate(), FORMATDATE));
        pacPatient.setVirusLoadArvCurrent(form.getVirusLoadArvCurrent());
        pacPatient.setVirusLoadArvLastestDate(TextUtils.convertDate(form.getVirusLoadArvLastestDate(), FORMATDATE));
        pacPatient.setClinicalStage(form.getClinicalStage());
        pacPatient.setClinicalStageDate(TextUtils.convertDate(form.getClinicalStageDate(), FORMATDATE));
        pacPatient.setOpcCode(form.getOpcCode());
        pacPatient.setAidsStatus(form.getAidsStatus());
        pacPatient.setAidsStatusDate(TextUtils.convertDate(form.getAidsStatusDate(), FORMATDATE));
        pacPatient.setStatusOfChangeTreatmentID(form.getStatusOfChangeTreatmentID());
        pacPatient.setChangeTreatmentDate(TextUtils.convertDate(form.getChangeTreatmentDate(), FORMATDATE));
        pacPatient.setRequestDeathTime(TextUtils.convertDate(form.getRequestDeathTime(), FORMATDATE));

        try {
            PacPatientInfoEntity model = pacPatientService.save(pacPatient);
            pacPatientService.log(model.getID(), "Thêm khách hàng từ import excel");
            if (model == null) {
                return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
            }
            return new Response<>(true, model);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi cập nhật thông tin. ");
        }
    }
}
