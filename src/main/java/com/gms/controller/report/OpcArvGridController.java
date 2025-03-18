package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.ArvGridExcel;
import com.gms.entity.excel.opc.OpcPatientExcel;
import com.gms.entity.form.opc_arv.OpcPatientForm;
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
 * DS bệnh nhân quản lý
 *
 * @author DSNAnh
 */
@Controller(value = "opc_book_arv_grid_report")
public class OpcArvGridController extends OpcController {

    private static final String TEMPLATE_REPORT = "report/opc/opc-patient-pdf.html";
    protected static final String PDF_FILE_NAME = "DSBN quan ly_";

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    SiteService siteServices;

    @GetMapping(value = {"/opc-arv-grid/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "inhFrom", required = false) String inhFrom,
            @RequestParam(name = "inhTo", required = false) String inhTo,
            @RequestParam(name = "cotriFrom", required = false) String cotriFrom,
            @RequestParam(name = "cotriTo", required = false) String cotriTo,
            @RequestParam(name = "laoFrom", required = false) String laoFrom,
            @RequestParam(name = "laoTo", required = false) String laoTo,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "registration_type", required = false) String registrationType,
            @RequestParam(name = "treatment_regimen_id", required = false) String treatmentRegimenID,
            @RequestParam(name = "gsph", required = false) String gsph,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {
        OpcPatientForm form = getData(gsph, inhFrom, inhTo, cotriFrom, cotriTo, laoFrom, laoTo, siteID, registrationTimeFrom, registrationTimeTo, treatmentTimeFrom, treatmentTimeTo, statusOfTreatmentID, permanentDistrictID, permanentProvinceID, keywords, registrationType, treatmentRegimenID, page, size);
        HashMap<String, HashMap<String, String>> options = getOptions();
        options.get(ParameterEntity.STATUS_OF_TREATMENT).remove("1");
        options.get(ParameterEntity.TREATMENT_REGIMEN).put("-1", "Chưa có thông tin");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Danh sách bệnh nhân quản lý tại cơ sở");
        model.addAttribute("small_title", "DSBN quản lý tại cơ sở");
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        model.addAttribute("siteID", siteID);
        return "report/opc/opc-patient";
    }

    @GetMapping(value = {"/opc-arv-grid/email.html"})
    public String actionSendEmail(Model model,
            @RequestParam(name = "inhFrom", required = false) String inhFrom,
            @RequestParam(name = "inhTo", required = false) String inhTo,
            @RequestParam(name = "cotriFrom", required = false) String cotriFrom,
            @RequestParam(name = "cotriTo", required = false) String cotriTo,
            @RequestParam(name = "laoFrom", required = false) String laoFrom,
            @RequestParam(name = "laoTo", required = false) String laoTo,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "registration_type", required = false) String registrationType,
            @RequestParam(name = "treatment_regimen_id", required = false) String treatmentRegimenID,
            @RequestParam(name = "gsph", required = false) String gsph,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.opcPatient());
        }
        OpcPatientForm form = getData(gsph, inhFrom, inhTo, cotriFrom, cotriTo, laoFrom, laoTo, siteID, registrationTimeFrom, registrationTimeTo, treatmentTimeFrom, treatmentTimeTo, statusOfTreatmentID, permanentDistrictID, permanentProvinceID, keywords, registrationType, treatmentRegimenID, page, size);
        HashMap<String, Object> context = new HashMap<>();
        form.setTitle("DANH SÁCH BỆNH NHÂN ĐANG QUẢN LÝ TẠI CƠ SỞ");
        form.setSiteName(!isOpcManager() ? getCurrentUser().getSite().getName() : StringUtils.isEmpty(siteID) ? getCurrentUser().getSite().getName() : siteServices.findOne(Long.parseLong(siteID)).getName());
        context.put("form", form);
        String startDate = StringUtils.isNotEmpty(form.getRegistrationTimeFrom()) ? form.getRegistrationTimeFrom() : StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : "";
        String dateTitle = String.format(" (Từ ngày %s đến ngày %s)", startDate, StringUtils.isNotEmpty(form.getRegistrationTimeTo()) ? form.getRegistrationTimeTo() : TextUtils.formatDate(new Date(), FORMATDATE));
        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("%s%s", "Danh sách bệnh nhân quản lý", StringUtils.isNotEmpty(startDate) ? dateTitle : ""),
                EmailoutboxEntity.TEMPLATE_OPC_PATIENT,
                context,
                new OpcPatientExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.opcPatient());
    }

    @GetMapping(value = {"/opc-arv-grid/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(Model model,
            @RequestParam(name = "inhFrom", required = false) String inhFrom,
            @RequestParam(name = "inhTo", required = false) String inhTo,
            @RequestParam(name = "cotriFrom", required = false) String cotriFrom,
            @RequestParam(name = "cotriTo", required = false) String cotriTo,
            @RequestParam(name = "laoFrom", required = false) String laoFrom,
            @RequestParam(name = "laoTo", required = false) String laoTo,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "registration_type", required = false) String registrationType,
            @RequestParam(name = "treatment_regimen_id", required = false) String treatmentRegimenID,
            @RequestParam(name = "gsph", required = false) String gsph,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size) throws Exception {

        OpcPatientForm form = getData(gsph, inhFrom, inhTo, cotriFrom, cotriTo, laoFrom, laoTo, siteID, registrationTimeFrom, registrationTimeTo, treatmentTimeFrom, treatmentTimeTo, statusOfTreatmentID, permanentDistrictID, permanentProvinceID, keywords, registrationType, treatmentRegimenID, page, size);
        form.setTitle("DANH SÁCH BỆNH NHÂN ĐANG QUẢN LÝ TẠI CƠ SỞ");
        form.setSiteName(!isOpcManager() ? getCurrentUser().getSite().getName() : StringUtils.isEmpty(siteID) ? getCurrentUser().getSite().getName() : siteServices.findOne(Long.parseLong(siteID)).getName());
        return exportExcel(new OpcPatientExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/opc-arv-grid/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(Model model,
            @RequestParam(name = "inhFrom", required = false) String inhFrom,
            @RequestParam(name = "inhTo", required = false) String inhTo,
            @RequestParam(name = "cotriFrom", required = false) String cotriFrom,
            @RequestParam(name = "cotriTo", required = false) String cotriTo,
            @RequestParam(name = "laoFrom", required = false) String laoFrom,
            @RequestParam(name = "laoTo", required = false) String laoTo,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "registration_type", required = false) String registrationType,
            @RequestParam(name = "treatment_regimen_id", required = false) String treatmentRegimenID,
            @RequestParam(name = "gsph", required = false) String gsph,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999") int size,
            RedirectAttributes redirectAttributes) throws Exception {
        OpcPatientForm form = getData(gsph, inhFrom, inhTo, cotriFrom, cotriTo, laoFrom, laoTo, siteID, registrationTimeFrom, registrationTimeTo, treatmentTimeFrom, treatmentTimeTo, statusOfTreatmentID, permanentDistrictID, permanentProvinceID, keywords, registrationType, treatmentRegimenID, page, size);
        HashMap<String, Object> context = new HashMap<>();
        form.setTitle("DANH SÁCH BỆNH NHÂN ĐANG QUẢN LÝ TẠI CƠ SỞ");
        context.put("form", form);
        context.put("options", getOptions());
        context.put("isOpcManager", isOpcManager());

        return exportPdf(PDF_FILE_NAME + TextUtils.removeDiacritical(getCurrentUser().getSite().getShortName()), TEMPLATE_REPORT, context);
    }
    
    
    @GetMapping(value = {"/opc-arv-grid-inbound/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcelInbound(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "receive_notification", required = false) String receiveNotification,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "insurance", required = false) String insurance,
            @RequestParam(name = "insurance_type", required = false) String insuranceType,
            @RequestParam(name = "insurance_no", required = false) String insuranceNo,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "therapy_site_id", required = false) String therapySiteID,
            @RequestParam(name = "dateOfArrivalFrom", required = false) String dateOfArrivalFrom,
            @RequestParam(name = "dateOfArrivalTo", required = false) String dateOfArrivalTo,
            @RequestParam(name = "tranferToTimeFrom", required = false) String tranferToTimeFrom,
            @RequestParam(name = "tranferToTimeTo", required = false) String tranferToTimeTo,
            @RequestParam(name = "tranferFromTimeFrom", required = false) String tranferFromTimeFrom,
            @RequestParam(name = "tranferFromTimeTo", required = false) String tranferFromTimeTo,
            @RequestParam(name = "treatmentRegimenID", required = false) String treatmentRegimenID,
            @RequestParam(name = "insurance_expiry", required = false) String insuranceExpiry,
            @RequestParam(name = "gsph", required = false) String gsph) throws Exception {
        OpcPatientForm form = new OpcPatientForm();
        LoggedUser currentUser = getCurrentUser();
        OpcArvSearch search = new OpcArvSearch();
        HashMap<String, HashMap<String, String>> options = getOptions();
        Set<Long> siteIDs = new HashSet<>();
        if (isOpcManager()) {
            if (StringUtils.isEmpty(therapySiteID)) {
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
                siteIDs.add(Long.parseLong(therapySiteID));
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        options.get(ParameterEntity.TREATMENT_REGIMEN).put("-1", "Chưa có thông tin");

        HashMap<String, String> insuranceExpiryOptions = new HashMap();
        insuranceExpiryOptions.put("", "Tất cả");
        insuranceExpiryOptions.put("1", "Trong vòng 01 tháng tới");
        insuranceExpiryOptions.put("2", "Trong vòng 02 tháng tới");
        insuranceExpiryOptions.put("3", "Trong vòng 03 tháng tới");
        options.put("insuranceExpiryOptions", insuranceExpiryOptions);

        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        
        search.setTab(tab);
        search.setRemove("remove".equals(tab));
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);
        search.setInsurance(insurance);
        search.setInsuranceType(insuranceType);
        search.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        search.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        search.setTreatmentStageID(treatmentStageID);
        search.setTreatmentRegimenID(treatmentRegimenID);
        search.setGsph(gsph);

        search.setDateOfArrivalFrom(isThisDateValid(dateOfArrivalFrom) ? dateOfArrivalFrom : null);
        search.setDateOfArrivalTo(isThisDateValid(dateOfArrivalTo) ? dateOfArrivalTo : null);
        search.setTranferToTimeFrom(isThisDateValid(tranferToTimeFrom) ? tranferToTimeFrom : null);
        search.setTranferToTimeTo(isThisDateValid(tranferToTimeTo) ? tranferToTimeTo : null);
        search.setTranferFromTimeFrom(isThisDateValid(tranferFromTimeFrom) ? tranferFromTimeFrom : null);
        search.setTranferFromTimeTo(isThisDateValid(tranferFromTimeTo) ? tranferFromTimeTo : null);
        search.setInsuranceExpiry(insuranceExpiry);

        if (StringUtils.isNotEmpty(code)) {
            search.setCode(code.trim());
        }
        if (StringUtils.isNotEmpty(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        if (StringUtils.isNotEmpty(identityCard)) {
            search.setIdentityCard(identityCard.trim());
        }
        if (StringUtils.isNotEmpty(insuranceNo)) {
            search.setInsuranceNo(insuranceNo.trim());
        }
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setRegistrationTimeFrom(isThisDateValid(registrationTimeFrom) ? registrationTimeFrom : null);
        search.setRegistrationTimeTo(isThisDateValid(registrationTimeTo) ? registrationTimeTo : null);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);

        List<String> statusOfTreatmentIDs = new ArrayList<>();
        List<String> treatmentStageIDs = new ArrayList<>();
        List<String> treatmentRegimenIDs = new ArrayList<>();
        
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
        
        if(StringUtils.isNotEmpty(treatmentRegimenID)){
            treatmentRegimenIDs = Arrays.asList(treatmentRegimenID.split(",", -1));
            if (treatmentRegimenIDs.size() == 1 && treatmentRegimenIDs.contains("")) {
                search.setTreatmentRegimenIDs(null);
            } else {
                search.setTreatmentRegimenIDs(new HashSet<String>());
                for (String object : treatmentRegimenIDs) {
                    search.getTreatmentRegimenIDs().add(object);
                }
            }
        }
        
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPageIndex(1);
        search.setPageSize(9999999);

        DataPage<OpcArvEntity> dataPage = opcArvService.find(search);
        
        form.setTitle(tab.equals("bhyt") ? "DANH SÁCH BỆNH NHÂN THEO BẢO HIỂM Y TẾ" : tab.equals("stage") ? "DANH SÁCH BỆNH NHÂN BIẾN ĐỘNG ĐIỀU TRỊ" : "DANH SÁCH BỆNH NHÂN QUẢN LÝ TẠI CƠ SỞ");
        form.setFileName("DSBNQuanLy_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        form.setIsOpcManager(isOpcManager());
        if (StringUtils.isNotEmpty(permanentDistrictID)) {
            form.setDistrictName(locationsService.findDistrict(permanentDistrictID).getName());
        }
        if (StringUtils.isNotEmpty(permanentProvinceID)) {
            form.setProvinceName(locationsService.findProvince(permanentProvinceID).getName());
        }
        form.setDataPage(dataPage);
        form.setOptions(options);
        form.setSearch(search);
        form.setItems(dataPage.getData());
        form.setSiteName(getCurrentUser().getSite().getName());
        return exportExcel(new ArvGridExcel(form, EXTENSION_EXCEL_X));
    }

    private OpcPatientForm getData(String gpsh, String inhFrom, String inhTo, String cotriFrom, String cotriTo, String laoFrom, String laoTo, String siteID, String registrationTimeFrom, String registrationTimeTo, String treatmentTimeFrom, String treatmentTimeTo, String statusOfTreatmentID, String permanentDistrictID, String permanentProvinceID, String keywords, String registrationType, String treatmentRegimenID, int page, int size) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcPatientForm form = new OpcPatientForm();

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
        search.setRegistrationTimeFrom(isThisDateValid(registrationTimeFrom) ? registrationTimeFrom : null);
        search.setRegistrationTimeTo(isThisDateValid(registrationTimeTo) ? registrationTimeTo : null);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setKeywords(StringUtils.isNotEmpty(keywords) ? keywords.trim() : keywords);
        search.setRegistrationType(registrationType);
        search.setTreatmentRegimenID(treatmentRegimenID);
        search.setGsph(gpsh);

        search.setInhFrom(isThisDateValid(inhFrom) ? inhFrom : null);
        search.setInhTo(isThisDateValid(inhTo) ? inhTo : null);
        search.setCotriFrom(isThisDateValid(cotriFrom) ? cotriFrom : null);
        search.setCotriTo(isThisDateValid(cotriTo) ? cotriTo : null);
        search.setLaoFrom(isThisDateValid(laoFrom) ? laoFrom : null);
        search.setLaoTo(isThisDateValid(laoTo) ? laoTo : null);

        DataPage<OpcArvEntity> dataPage = opcArvService.findOpcPatient(search);
        search.setPageIndex(1);
        search.setPageSize(99999999);
        List<OpcArvEntity> list = dataPage.getData();
        if (list != null && list.size() > 0) {
            form.setStartDate(TextUtils.formatDate(list.get(list.size() - 1).getRegistrationTime(), FORMATDATE));
        }

        form.setTitle("DANH SÁCH BỆNH NHÂN QUẢN LÝ TẠI CƠ SỞ");
        form.setFileName("DSBNQuanLy_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        form.setIsOpcManager(isOpcManager());
        if (StringUtils.isNotEmpty(permanentDistrictID)) {
            form.setDistrictName(locationsService.findDistrict(permanentDistrictID).getName());
        }
        if (StringUtils.isNotEmpty(permanentProvinceID)) {
            form.setProvinceName(locationsService.findProvince(permanentProvinceID).getName());
        }
        form.setRegistrationTimeFrom(isThisDateValid(registrationTimeFrom) ? registrationTimeFrom : null);
        form.setRegistrationTimeTo(isThisDateValid(registrationTimeTo) ? registrationTimeTo : null);
        form.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        form.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        form.setDataPage(dataPage);
        form.setOptions(options);
        form.setSearch(search);
        form.setItems(dataPage.getData());

        return form;
    }

    /**
     * Check valid date
     *
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
