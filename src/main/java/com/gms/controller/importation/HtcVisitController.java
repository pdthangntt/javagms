package com.gms.controller.importation;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.RemoveEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.HtcVisitImportForm;
import com.gms.entity.form.ImportForm;
import com.gms.entity.validate.HtcVisitImportValidate;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

@Controller(value = "importation_htcvisit")
public class HtcVisitController extends BaseController<HtcVisitImportForm> {
    
    @Autowired
    private ParameterService parameterService;
    
    @Autowired
    private HtcVisitImportValidate htcVisitImportValidate;
    
    @Autowired
    private SiteService siteService;
    
    @Autowired
    private HtcVisitService htcVisitService;
    
    // Create new date time format
    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

    @GetMapping(value = {"/import-htc-visit/index.html", "/import-htc-visit/read.html", "/import-htc-visit/validate.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model); 
    }

    @PostMapping(value = "/import-htc-visit/read.html")
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
    
    /**
     * Validate từng dòng dữ liệu
     * @param model
     * @param input
     * @param redirectAttributes
     * @return 
     */
    @PostMapping(value = "/import-htc-visit/validate.html")
    public String actionValidate(Model model,
            @ModelAttribute("form") ImportForm input,
            RedirectAttributes redirectAttributes) {
        ImportForm form = initForm();
        form.setMapCols(input.getMapCols());
        form.setFilePath(input.getFilePath());
        try {
            return renderValidate(model, "importation/htc/visit", form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @Override
    public HtcVisitImportForm mapping(Map<String, String> cols, List<String> cells) {
        
        HtcVisitImportForm item = new HtcVisitImportForm();
        
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    case "advisoryeTime":
                        item.setAdvisoryeTime(cells.get(i));
                        item.setPreTestTime(cells.get(i));
                        break;
                    case "code":
                        item.setCode(cells.get(i).replaceFirst("\\.0+$", ""));
                        break;
                    case "patientName":
                        item.setPatientName(cells.get(i));
                        break;
                    case "genderID":
                        item.setGenderID(cells.get(i));
                        break;
                    case "yearOfBirth":
                        item.setYearOfBirth(cells.get(i).replaceFirst("\\.0+$", ""));
                        break;
                    case "permanentAddress":
                        item.setPermanentAddress(cells.get(i));
                        break;
                    case "province":
                        item.setProvince(StringUtils.isNotEmpty(cells.get(i)) ? cells.get(i).trim() : null );
                        break;
                    case "district":
                        item.setDistrict(StringUtils.isNotEmpty(cells.get(i)) ? cells.get(i).trim() : null );
                        break;
                    case "ward":
                        item.setWard(StringUtils.isNotEmpty(cells.get(i)) ? cells.get(i).trim() : null );
                        break;
                    case "hasHealthInsurance":
                        item.setHasHealthInsurance(cells.get(i).toLowerCase());
                        break;
                    case "objectGroupID_1":
                        item.setObjectGroupID_1(cells.get(i));
                        break;
                    case "objectGroupID_2":
                        item.setObjectGroupID_2(cells.get(i));
                        break;
                    case "objectGroupID_3":
                        item.setObjectGroupID_3(cells.get(i));
                        break;
                    case "objectGroupID_4":
                        item.setObjectGroupID_4(cells.get(i));
                        break;
                    case "objectGroupID_5":
                        item.setObjectGroupID_5(cells.get(i));
                        break;
                    case "objectGroupID_6":
                        item.setObjectGroupID_6(cells.get(i));
                        break;
                    case "objectGroupID_7":
                        item.setObjectGroupID_7(cells.get(i));
                        break;
                    case "objectGroupID_8":
                        item.setObjectGroupID_8(cells.get(i));
                        break;
                    case "objectGroupID_9":
                        item.setObjectGroupID_9(cells.get(i));
                        break;
                    case "isAgreePreTest":
                        item.setIsAgreePreTest(cells.get(i));
                        break;
                    case "testResultsID":
                        item.setTestResultsID(cells.get(i));
                        break;
                    case "confirmResultsID":
                        item.setConfirmResultsID(cells.get(i));
                        break;
                    case "resultsTime":
                        item.setResultsTime(cells.get(i));
                        break;
                    case "patientID":
                        item.setPatientID(cells.get(i));
                        break;
                    case "staffBeforeTestID":
                        item.setStaffBeforeTestID(cells.get(i));
                        break;
                    case "staffAfterID":
                        item.setStaffAfterID(cells.get(i));
                        break;
                    case "healthInsuranceNo":
                        item.setHealthInsuranceNo(cells.get(i));
                        break;
                    case "phone":
                        item.setPhone(cells.get(i));
                        break;
                    case "confirmTime":
                        item.setConfirmTime(cells.get(i));
                        item.setSampleSentDate(StringUtils.isNotEmpty(item.getConfirmTime()) ? item.getPreTestTime(): null );
                        break;
//                    case "sampleSentDate":
//                        break;
                    default:
                }
            } catch (Exception ex) {
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }

    /**
     * Lưu thông tin upload
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/import-htc-visit/save.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<?> actionSave(@Valid @RequestBody HtcVisitImportForm form,
            BindingResult bindingResult) {
        
        form.setSiteID(getCurrentUser().getSite().getID());
        form.setIsRemove(RemoveEnum.FALSE.getKey());
        htcVisitImportValidate.validate(form, bindingResult);
        
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().get(0);
            return new Response<>(false, bindingResult.getAllErrors());
        }
        
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceTestEnum.CD.getKey(), getCurrentUser().getSite().getProvinceID());
        String site = sites.isEmpty() ? null : sites.get(0).getID().toString();
        
        HtcVisitEntity htcVisitEntity = form.convertToEntity(site);
        
        htcVisitEntity = htcVisitService.save(htcVisitEntity);
        htcVisitService.log(htcVisitEntity.getID(), "Thêm khách hàng từ import excel");
        return new Response<>(true, htcVisitEntity);
    }
    
    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        
        // Type
        options.put("type", new HashMap<String, String>());
        options.get("type").put("", "--");
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);
        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2 || !"title".equals(splitCode[1])) {
                continue;
            }
            options.get("type").put(splitCode[0], parameter.getValue());
        }
        
        // Lấy câu trả lời
        options.put("status", new HashMap<String, String>());
        options.get("status").put("", "Chọn câu trả lời");
        options.get("status").put("không", "Không");
        options.get("status").put("có", "Có");
        
        // Có BHYT hay không
        options.put("hasHealth", new HashMap<String, String>());
        options.get("hasHealth").put("", "Chọn câu trả lời");
        options.get("hasHealth").put("có", "Có");
        options.get("hasHealth").put("không", "Không");
        options.get("hasHealth").put("không có thông tin", "Không có thông tin");
        
        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl(UrlUtils.htcImport());
        form.setReadUrl("/import-htc-visit/read.html");
        form.setValidateUrl("/import-htc-visit/validate.html");
        form.setSaveUrl("/import-htc-visit/save.json");
        form.setTitle("Thêm khách hàng sử dụng excel");
        form.setSmallTitle("Tư vấn & Xét nghiệm");
        form.setSmallUrl(UrlUtils.htcImport());
        form.setTemplate(fileTemplate("htc_visits.xlsx"));
        form.setCols((new HtcVisitEntity()).getAttributeLabels());
        return form;
    }
}
