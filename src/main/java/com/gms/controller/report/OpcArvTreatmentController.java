package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.TreatmentFluctuationsExcel;
import com.gms.entity.form.opc_arv.OpcTreatmentFluctuationsForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DS biến động điều trị
 *
 * @author vvthành
 */
@Controller(value = "opc_arv_treatment_report")
public class OpcArvTreatmentController extends OpcController {

    private static final String TEMPLATE_REPORT = "report/opc/treatment-fluctuations-pdf.html";
    protected static final String PDF_FILE_NAME = "DS bien dong dieu tri_";
    
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    SiteService siteService;
    
    /**
     * Hiển thị DS biến động điều trị
     * @author DSNAnh
     * @param model
     * @param treatmentStageTimeFrom
     * @param treatmentStageTimeTo
     * @param treatmentStageID
     * @param permanentDistrictID
     * @param permanentProvinceID
     * @param keywords
     * @param siteID
     * @param page
     * @param size
     * @return
     * @throws java.text.ParseException
     */
    @GetMapping(value = {"/opc-arv-treatment/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {
        OpcTreatmentFluctuationsForm form = getData(siteID, treatmentStageTimeFrom, treatmentStageTimeTo, treatmentStageID, permanentDistrictID, permanentProvinceID,keywords,statusOfTreatmentID, page, size);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Danh sách biến động điều trị");
        model.addAttribute("small_title", "DS biến động điều trị");
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("siteID", siteID);
        return "report/opc/treatment-fluctuations";
    }

    @GetMapping(value = {"/opc-arv-treatment/email.html"})
    public String actionSendEmail(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcTreatmentFluctuations());
        }
        OpcTreatmentFluctuationsForm form = getData(siteID, treatmentStageTimeFrom, treatmentStageTimeTo, treatmentStageID, permanentDistrictID, permanentProvinceID,keywords,statusOfTreatmentID, page, size);
        HashMap<String, Object> context = new HashMap<>();
        form.setSiteName(!isOpcManager() ? getCurrentUser().getSite().getName() : StringUtils.isEmpty(siteID) ? getCurrentUser().getSite().getName() : siteService.findOne(Long.parseLong(siteID)).getName());
        form.setTitle("DANH SÁCH BỆNH NHÂN BIẾN ĐỘNG ĐIỀU TRỊ");
        context.put("form", form);
        String startDate = StringUtils.isNotEmpty(form.getTreatmentStageTimeFrom()) ? form.getTreatmentStageTimeFrom() : StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : "";
        String dateTitle = String.format(" (Từ ngày %s đến ngày %s)",startDate , StringUtils.isNotEmpty(form.getTreatmentStageTimeTo()) ? form.getTreatmentStageTimeTo() : TextUtils.formatDate(new Date(), FORMATDATE));
        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s%s", "Danh sách biến động điều trị", StringUtils.isNotEmpty(startDate) ? dateTitle : ""),
                EmailoutboxEntity.TEMPLATE_OPC_TREATMENT_FLUCTUATIONS,
                context,
                new TreatmentFluctuationsExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcTreatmentFluctuations());
    }

    @GetMapping(value = {"/opc-arv-treatment/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "9999999") int size) throws Exception {
        OpcTreatmentFluctuationsForm form = getData(siteID, treatmentStageTimeFrom, treatmentStageTimeTo, treatmentStageID, permanentDistrictID, permanentProvinceID,keywords,statusOfTreatmentID, page, size);
        form.setTitle("DANH SÁCH BỆNH NHÂN BIẾN ĐỘNG ĐIỀU TRỊ");
        form.setSiteName(!isOpcManager() ? getCurrentUser().getSite().getName() : StringUtils.isEmpty(siteID) ? getCurrentUser().getSite().getName() : siteService.findOne(Long.parseLong(siteID)).getName());
        return exportExcel(new TreatmentFluctuationsExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-arv-treatment/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size) throws Exception {
        OpcTreatmentFluctuationsForm form = getData(siteID, treatmentStageTimeFrom, treatmentStageTimeTo, treatmentStageID, permanentDistrictID, permanentProvinceID, keywords,statusOfTreatmentID, page, size);
        HashMap<String, Object> context = new HashMap<>();
        form.setTitle("Danh sách bệnh nhân biến động điều trị");
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isOpcManager", isOpcManager());
        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);
    }

    private OpcTreatmentFluctuationsForm getData(String siteID, String treatmentStageTimeFrom, String treatmentStageTimeTo, String treatmentStageID, String permanentDistrictID, String permanentProvinceID, String keywords, String statusOfTreatmentID, int page, int size) throws ParseException {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcTreatmentFluctuationsForm form = new OpcTreatmentFluctuationsForm();
        
        Set<Long> siteIDs = new HashSet<>();
        if (isOpcManager()) {
            if (StringUtils.isEmpty(siteID)) {
                HashMap<String, String> siteList = options.get(ParameterEntity.TREATMENT_FACILITY);
                if (siteList != null && siteList.size() > 0) {
                    for (Map.Entry<String, String> entry : siteList.entrySet()) {
                        String key = entry.getKey();
                        if (StringUtils.isEmpty(key)) {
                            continue;
                        }
                        siteIDs.add(Long.parseLong(key));
                    }
                }
            } else {
                siteIDs.add(Long.parseLong(siteID));
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        
        OpcArvSearch search = new OpcArvSearch();
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        search.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        search.setTreatmentStageID(treatmentStageID);
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setKeywords(StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        
        List<String> treatmentStageIDs;
        if(StringUtils.isNotEmpty(treatmentStageID)){
            treatmentStageIDs = Arrays.asList(treatmentStageID.split(",", -1));
            if (treatmentStageIDs.size() == 1 && treatmentStageIDs.contains("")) {
                search.setTreatmentStageIDs(null);
            } else {
                search.setTreatmentStageIDs(new HashSet<String>());
                for (String object : treatmentStageIDs) {
                    search.getTreatmentStageIDs().add(object);
                }
            }
        }
        
        List<String> statusOfTreatmentIDs;
        if(StringUtils.isNotEmpty(statusOfTreatmentID)){
            statusOfTreatmentIDs = Arrays.asList(statusOfTreatmentID.split(",", -1));
            if (statusOfTreatmentIDs.size() == 1 && statusOfTreatmentIDs.contains("")) {
                search.setStatusOfTreatmentIDs(null);
            } else {
                search.setStatusOfTreatmentIDs(new HashSet<String>());
                for (String object : statusOfTreatmentIDs) {
                    search.getStatusOfTreatmentIDs().add(object);
                }
            }
        }
        
        DataPage<OpcArvEntity> dataPage = opcArvService.findTreatmentFluctuations(search);
        
        search.setPageIndex(1);
        search.setPageSize(99999999);
        List<OpcArvEntity> list = opcArvService.findTreatmentFluctuations(search).getData();
        if(list != null && list.size() > 0){
            form.setStartDate(TextUtils.formatDate(list.get(list.size() - 1).getTreatmentStageTime(), FORMATDATE));
        }
        
        form.setTitle("DANH SÁCH BIẾN ĐỘNG ĐIỀU TRỊ");
        form.setFileName("DSBienDongDieuTri_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        form.setIsOpcManager(isOpcManager());
        if(StringUtils.isNotEmpty(permanentDistrictID)){
            form.setDistrictName(locationsService.findDistrict(permanentDistrictID).getName());
        }
        if(StringUtils.isNotEmpty(permanentProvinceID)){
            form.setProvinceName(locationsService.findProvince(permanentProvinceID).getName());
        }
        form.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        form.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        form.setDataPage(dataPage);
        form.setOptions(options);
        form.setSearch(search);
        form.setItems(dataPage.getData());
        
        return form;
    }
    
    /**
     * Check valid date
     * @param dateToValidate
     * @return
     */
    private boolean isThisDateValid(String dateToValidate) {
        if (StringUtils.isEmpty(dateToValidate)) {
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
