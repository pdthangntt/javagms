package com.gms.controller.importation;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.HtcConfirmImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.validate.HtcConfirmImportValidate;
import com.gms.service.HtcConfirmService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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

/**
 *
 * @author TrangBN
 */
@Controller(value = "importation_htcconfirm")
public class HtcConfirmController extends BaseController<HtcConfirmImportForm> {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private HtcConfirmImportValidate htcConfirmImportValidate;

    @Override
    public HtcConfirmImportForm mapping(Map<String, String> cols, List<String> cells) {

        HashMap<String, HashMap<String, String>> options = getOptions();
        HtcConfirmImportForm item = new HtcConfirmImportForm();

        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }
        Map<String, String> sitesMap = getSite(siteIDs);

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
                    case "year":
                        item.setYear(cells.get(i) == null ? "" : cells.get(i).replaceFirst("\\.0+$", ""));
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
                    case "modeOfTransmission":
                        HashMap<String, String> modeOfTransmissionOption = options.get(ParameterEntity.MODE_OF_TRANSMISSION);
                        Set<String> modeOfTransmissionKeys = modeOfTransmissionOption.keySet();
                        for (String key : modeOfTransmissionKeys) {
                            if (TextUtils.removeDiacritical(modeOfTransmissionOption.get(key)).equals(cells.get(i) == null ? "" : TextUtils.removeDiacritical(cells.get(i)))) {
                                item.setModeOfTransmission(key);
                                break;
                            }
                        }
                        break;
                    case "confirmTime":
                        item.setConfirmTime(cells.get(i));
                        break;
                    case "virusLoad":
                        HashMap<String, String> virusLoadConfirmOption = options.get(ParameterEntity.VIRUS_LOAD);
                        Set<String> virusLoadConfirmKeys = virusLoadConfirmOption.keySet();
                        for (String key : virusLoadConfirmKeys) {
                            if (virusLoadConfirmOption.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setVirusLoad(key);
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
                    case "sourceSiteID":
                        for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
                            if (entry.getValue().equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setSourceSiteID(entry.getKey());
                            }
                        }
                        break;
                    case "testResultsID":
                        HashMap<String, String> resultsId = options.get(ParameterEntity.TEST_RESULTS);
                        Set<String> results = resultsId.keySet();
                        for (String key : results) {
                            if (resultsId.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setTestResultsID(key);
                            }
                        }
                        break;
                    case "serviceID":
                        HashMap<String, String> serviceTest = options.get(ParameterEntity.SERVICE_TEST);
                        Set<String> services = serviceTest.keySet();
                        for (String key : services) {
                            if (serviceTest.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setServiceID(key);
                            }
                        }
                        break;
                    case "bioName1":
                        HashMap<String, String> bioName1 = options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST);
                        Set<String> bio = bioName1.keySet();
                        for (String key : bio) {
                            if (bioName1.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioName1(key);
                            }
                        }
                        break;
                    case "bioName2":
                        HashMap<String, String> bioName2 = options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST);
                        Set<String> bio2 = bioName2.keySet();
                        for (String key : bio2) {
                            if (bioName2.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioName2(key);
                            }
                        }
                        break;
                    case "bioName3":
                        HashMap<String, String> bioName3 = options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST);
                        Set<String> bio3 = bioName3.keySet();
                        for (String key : bio3) {
                            if (bioName3.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioName3(key);
                            }
                        }
                        break;
                    case "bioNameResult1":
                        HashMap<String, String> bioNameResult1 = options.get(ParameterEntity.BIO_NAME_RESULT);
                        Set<String> bioResult1 = bioNameResult1.keySet();
                        for (String key : bioResult1) {
                            if (bioNameResult1.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioNameResult1(key);
                            }
                        }
                        break;
                    case "bioNameResult2":
                        HashMap<String, String> bioNameResult2 = options.get(ParameterEntity.BIO_NAME_RESULT);
                        Set<String> bioResult2 = bioNameResult2.keySet();
                        for (String key : bioResult2) {
                            if (bioNameResult2.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioNameResult2(key);
                            }
                        }
                        break;
                    case "bioNameResult3":
                        HashMap<String, String> bioNameResult3 = options.get(ParameterEntity.BIO_NAME_RESULT);
                        Set<String> bioResult3 = bioNameResult3.keySet();
                        for (String key : bioResult3) {
                            if (bioNameResult3.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setBioNameResult3(key);
                            }
                        }
                        break;
                    case "resultsID":
                        HashMap<String, String> resultConfirm = options.get(ParameterEntity.TEST_RESULTS_CONFIRM);
                        Set<String> resultCf = resultConfirm.keySet();
                        for (String key : resultCf) {
                            if (resultConfirm.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setResultsID(key);
                            }
                        }
                        break;
                    case "sampleQuality":
                        HashMap<String, String> sampleQuality = options.get(ParameterEntity.SAMPLE_QUALITY);
                        Set<String> sample = sampleQuality.keySet();
                        for (String key : sample) {
                            if (sampleQuality.get(key).equals(cells.get(i) == null ? "" : cells.get(i))) {
                                item.setSampleQuality(key);
                            }
                        }
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

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.SAMPLE_QUALITY);
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        options = parameterService.getOptionsByTypes(parameterTypes, null);
        
        String siteList = "siteList";
        options.put(siteList, new HashMap<String, String>());
        options.get(siteList).put("", "Chọn cơ sở gửi mẫu");
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), getCurrentUser().getSite().getProvinceID());
        Set<Long> siteIDs = new HashSet<>();
        if (sites != null && sites.size() > 0) {
            siteIDs.addAll(CollectionUtils.collect(sites, TransformerUtils.invokerTransformer("getID")));
            List<SiteEntity> siteModels = siteService.findByIDs(siteIDs);
            for (SiteEntity site : siteModels) {
                options.get(siteList).put(String.valueOf(site.getID()), site.getName());
            }
        }
        options.put("status", new HashMap<String, String>());
        options.get("status").put("0", "Không");
        options.get("status").put("1", "Có");

        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-htc-confirm/index.html");
        form.setReadUrl("/import-htc-confirm/read.html");
        form.setValidateUrl("/import-htc-confirm/validate.html");
        form.setSaveUrl("/import-htc-confirm/save.json");
        form.setTitle("Thêm khách hàng sử dụng excel");
        form.setSmallTitle("Xét nghiệm khẳng định");
        form.setSmallUrl(UrlUtils.htcConfirmIndex());
        form.setTemplate(fileTemplate("htcconfirm.xlsx"));
        form.setCols((new HtcConfirmEntity()).getAttributeLabels());
        return form;
    }

    /**
     * Action upload
     *
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping(value = {"/import-htc-confirm/index.html", "/import-htc-confirm/read.html", "/import-htc-confirm/validate.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    /**
     * Action đọc dữ liệu từ file
     *
     * @param model
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping(value = "/import-htc-confirm/read.html")
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

    @PostMapping(value = "/import-htc-confirm/validate.html")
    public String actionValidate(Model model,
            @ModelAttribute("form") ImportForm input,
            RedirectAttributes redirectAttributes) {

        ImportForm form = initForm();
        form.setMapCols(input.getMapCols());
        form.setFilePath(input.getFilePath());
        try {
            return renderValidate(model, "importation/htc-confirm/view", form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @PostMapping(value = "/import-htc-confirm/save.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<?> actionSave(@Valid @RequestBody HtcConfirmImportForm form,
            BindingResult bindingResult) {

        form.setSiteID(getCurrentUser().getSite().getID());
        form.setInsurance(StringUtils.isNotEmpty(form.getInsuranceNo()) ? "1" : "0");
        htcConfirmImportValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }
        HtcConfirmEntity htcConfirmEntity = new HtcConfirmEntity();
        htcConfirmEntity.setFullname(form.getFullname());
        htcConfirmEntity.setCode(StringUtils.isNotEmpty(form.getCode()) ? form.getCode().toUpperCase() : form.getCode());
        htcConfirmEntity.setGenderID(form.getGenderID());
        htcConfirmEntity.setYear(Integer.parseInt(form.getYear()));
        htcConfirmEntity.setAddress(form.getAddress());
        htcConfirmEntity.setCurrentAddress(form.getAddress());
        htcConfirmEntity.setObjectGroupID(form.getObjectGroupID());
        htcConfirmEntity.setSourceID(form.getSourceID());
        htcConfirmEntity.setSampleReceiveTime(TextUtils.convertDate(form.getSampleReceiveTime(), FORMATDATE));
        htcConfirmEntity.setConfirmTime(TextUtils.convertDate(form.getConfirmTime(), FORMATDATE));
        htcConfirmEntity.setResultsID(form.getResultsID());
        htcConfirmEntity.setSourceSiteID(Long.parseLong(form.getSourceSiteID()));
        htcConfirmEntity.setSiteID(getCurrentUser().getSite().getID());
        htcConfirmEntity.setRemove(false);
        htcConfirmEntity.setSourceServiceID(ServiceEnum.HTC_CONFIRM.getKey());
        
        htcConfirmEntity.setPermanentAddressGroup(form.getPermanentAddressGroup());
        htcConfirmEntity.setPermanentAddressStreet(form.getPermanentAddressStreet());
        htcConfirmEntity.setProvinceID(form.getProvinceID());
        htcConfirmEntity.setDistrictID(form.getDistrictID());
        htcConfirmEntity.setWardID(form.getWardID());
        htcConfirmEntity.setCurrentAddressStreet(form.getCurrentAddressStreet());
        htcConfirmEntity.setCurrentAddressGroup(form.getCurrentAddressGroup());
        htcConfirmEntity.setCurrentProvinceID(form.getCurrentProvinceID());
        htcConfirmEntity.setCurrentDistrictID(form.getCurrentDistrictID());
        htcConfirmEntity.setCurrentWardID(form.getCurrentWardID());
        
        htcConfirmEntity.setPatientID(form.getPatientID());
        htcConfirmEntity.setInsuranceNo(form.getInsuranceNo()); 
        htcConfirmEntity.setModeOfTransmission(form.getModeOfTransmission()); 
        htcConfirmEntity.setSourceSiteID(StringUtils.isNotEmpty(form.getSourceSiteID()) ? Long.valueOf(form.getSourceSiteID()) : null); 
        htcConfirmEntity.setSourceID(form.getSourceID()); 
        htcConfirmEntity.setSourceReceiveSampleTime(TextUtils.convertDate(form.getSourceReceiveSampleTime(), FORMATDATE));
        htcConfirmEntity.setTestResultsID(form.getTestResultsID());
        htcConfirmEntity.setSampleQuality(form.getSampleQuality());
        htcConfirmEntity.setSourceSampleDate(TextUtils.convertDate(form.getSourceSampleDate(), FORMATDATE));
        htcConfirmEntity.setServiceID(form.getServiceID());
        htcConfirmEntity.setSampleReceiveTime(TextUtils.convertDate(form.getSampleReceiveTime(), FORMATDATE));
        htcConfirmEntity.setBioName1(form.getBioName1());
        htcConfirmEntity.setBioNameResult1(form.getBioNameResult1());
        htcConfirmEntity.setFirstBioDate(TextUtils.convertDate(form.getFirstBioDate(), FORMATDATE));
        htcConfirmEntity.setBioName2(form.getBioName2());
        htcConfirmEntity.setBioNameResult2(form.getBioNameResult2());
        htcConfirmEntity.setSecondBioDate(TextUtils.convertDate(form.getSecondBioDate(), FORMATDATE));
        htcConfirmEntity.setBioName3(form.getBioName3());
        htcConfirmEntity.setBioNameResult3(form.getBioNameResult3());
        htcConfirmEntity.setThirdBioDate(TextUtils.convertDate(form.getThirdBioDate(), FORMATDATE));
        htcConfirmEntity.setResultsID(form.getResultsID());
        htcConfirmEntity.setSampleSaveCode(form.getSampleSaveCode());
        htcConfirmEntity.setEarlyHiv(form.getEarlyHiv());
        htcConfirmEntity.setEarlyHivDate(TextUtils.convertDate(form.getEarlyHivDate(), FORMATDATE));
        htcConfirmEntity.setVirusLoad(form.getVirusLoad());
        htcConfirmEntity.setVirusLoadDate(TextUtils.convertDate(form.getVirusLoadDate(), FORMATDATE));
        htcConfirmEntity.setResultsReturnTime(TextUtils.convertDate(form.getResultsReturnTime(), FORMATDATE));
        htcConfirmEntity.setTestStaffId(form.getTestStaffId());
        htcConfirmEntity.setInsurance(form.getInsurance());
        
        try {
            HtcConfirmEntity entity = htcConfirmService.save(htcConfirmEntity);
            htcConfirmService.log(entity.getID(), "Thêm khách hàng từ import excel");
            if (entity == null) {
                return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
            }
            return new Response<>(true, entity);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi cập nhật thông tin. " + e);
        }
    }

    /**
     * Lấy danh sách các cơ sở gửi mẫu
     *
     * @param siteIDs
     * @return
     */
    private Map<String, String> getSiteSentOption(Set<Long> siteIDs) {
        Map<String, String> siteOption = getSite(siteIDs);
        return siteOption;
    }

    /**
     * Lấy thông tin cs gửi mẫu
     *
     * @param siteIDs
     * @return
     */
    private Map<String, String> getSite(Set<Long> siteIDs) {
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        options.put("", "Chọn tên cơ sở gửi mẫu");
        if (siteIDs != null && siteIDs.size() > 0) {
            List<SiteEntity> sites = siteService.findByIDs(siteIDs);
            for (SiteEntity site : sites) {
                options.put(String.valueOf(site.getID()), site.getName());
            }
        }
        return options;
    }
}
