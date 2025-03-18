package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcChildEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcArvNewForm;
import com.gms.entity.form.opc_arv.OpcArvUpdateForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.entity.validate.OpcArvNewValidate;
import com.gms.entity.validate.OpcArvUpdateValidate;
import com.gms.repository.OpcArvRevisionRepository;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcChildService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.OpcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * OPC quản lý thông tin bệnh nhân
 *
 * @author DSNANH
 */
@Controller(value = "backend_opc_arv")
public class OpcArvController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcArvNewValidate opcArvNewValidate;
    @Autowired
    private OpcArvUpdateValidate opcArvUpdateValidate;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private HtcVisitService htcService;
    @Autowired
    private OpcTestService opcTestService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcChildService opcChildService;
    @Autowired
    private OpcArvRevisionRepository revisionRepository;

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

    /**
     *
     *
     * @param model
     * @param tab
     * @param id
     * @param receiveNotification
     * @param notification
     * @param pageRedirect
     * @param code
     * @param name
     * @param identityCard
     * @param insurance
     * @param insuranceType
     * @param insuranceNo
     * @param permanentDistrictID
     * @param permanentProvinceID
     * @param permanentWardID
     * @param registrationTimeFrom
     * @param registrationTimeTo
     * @param treatmentStageTimeFrom
     * @param treatmentStageTimeTo
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param statusOfTreatmentID
     * @param treatmentStageID
     * @param therapySiteID
     * @param dateOfArrivalFrom
     * @param dateOfArrivalTo
     * @param tranferToTimeFrom
     * @param tranferToTimeTo
     * @param tranferFromTimeFrom
     * @param insuranceExpiry
     * @param gsph
     * @param treatmentRegimenID
     * @param tranferFromTimeTo
     * @param page
     * @param size
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-arv/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
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
            @RequestParam(name = "gsph", required = false) String gsph,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            RedirectAttributes redirectAttributes) {
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }

        LoggedUser currentUser = getCurrentUser();
        OpcArvSearch search = new OpcArvSearch();
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
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

        if (StringUtils.isNotEmpty(statusOfTreatmentID)) {
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

        if (StringUtils.isNotEmpty(treatmentStageID)) {
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

        if (StringUtils.isNotEmpty(treatmentRegimenID)) {
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
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<OpcArvEntity> dataPage = opcArvService.find(search);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("tabIndex", "null".equals(tab) ? "" : tab);
        model.addAttribute("options", options);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("config", parameterService.getSiteConfig(currentUser.getSite().getID()));

        model.addAttribute("title", "Danh sách bệnh án");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("search_status_of_treatment", statusOfTreatmentIDs.size() == parameterService.getStatusOfTreatment().size() ? "" : statusOfTreatmentIDs.toArray(new String[statusOfTreatmentIDs.size()]));
        model.addAttribute("search_treatment_stage", treatmentStageIDs.toArray(new String[treatmentStageIDs.size()]));
        model.addAttribute("search_treatment_regimen", treatmentRegimenIDs.size() == parameterService.getTreatmentRegimen().size() ? "" : treatmentRegimenIDs.toArray(new String[treatmentRegimenIDs.size()]));
        return "backend/opc_arv/arv_index";
    }

    @RequestMapping(value = {"/opc-arv/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model, OpcArvNewForm form,
            @RequestParam(name = "htc_id", defaultValue = "", required = false) String htcID,
            @RequestParam(name = "opc_id", defaultValue = "", required = false) String opcID,
            @RequestParam(name = "feedback", defaultValue = "", required = false) String feedback,
            @RequestParam(name = "printable", defaultValue = "", required = false) String printable,
            RedirectAttributes redirectAttributes) throws ParseException {
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }
        LocalDate date18 = LocalDate.now().minusMonths(18);//trừ 18 tháng so với hiện tại
        Date date = Date.from(date18.atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<String, HashMap<String, String>> options = getOptions(true, null);
        if (isOpcManager()) {
            options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        }
        LoggedUser currentUser = getCurrentUser();
        String currentSiteID = currentUser.getSite().getID().toString();
//        form.setStatusOfTreatmentID("0");
        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        form.setCode(isOpcManager() ? "" : opcArvService.getCode());
        if (StringUtils.isNotEmpty(opcID)) {
            OpcArvEntity arvEntity = opcArvService.findOne(Long.parseLong(opcID));
            if (arvEntity != null) {
                form = new OpcArvNewForm(arvEntity);
                form.setCurrentSiteID(currentSiteID);
                form.setIsOpcManager(isOpcManager());
                form.setIsOtherSite(currentSiteID.equals(form.getSiteID()));
                form.setPageRedirect(printable);
            }
        }

        if (StringUtils.isNotEmpty(htcID)) {
            HtcVisitEntity htcEntity = htcService.findOne(Long.parseLong(htcID));
            form = new OpcArvNewForm(htcEntity);
            form.setCurrentSiteID(currentSiteID);
            form.setIsOtherSite(currentSiteID.equals(form.getSiteID()));
            form.setPageRedirect(printable);
            form.setCode(isOpcManager() ? "" : opcArvService.getCode());
            List<String> listSiteConfirms = new ArrayList<>();
            for (Map.Entry<String, String> entry : options.get("siteConfirm").entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                listSiteConfirms.add(key);
            }
            if (form.getConfirmSiteID() != null && !listSiteConfirms.contains(form.getConfirmSiteID())) {
                form.setConfirmSiteName(siteService.findOne(Long.valueOf(form.getConfirmSiteID())) == null ? "Cơ sở khác" : siteService.findOne(Long.valueOf(form.getConfirmSiteID())).getName());
                form.setConfirmSiteID("-1");
            }

            if (!options.get("siteOpcTo").containsKey(form.getHtcSiteID())) {
                form.setSourceSiteID("-1");
                form.setSourceSiteName(siteService.findOne(Integer.parseInt(form.getHtcSiteID())).getName());
            } else {
                form.setSourceSiteID(form.getHtcSiteID());
            }

        }

        form.setSourceServiceID(StringUtils.isNotEmpty(htcID) ? ServiceEnum.HTC.getKey() : StringUtils.isNotEmpty(opcID) ? ServiceEnum.OPC.getKey() : null);
        form.setDateBefore18mon(formatDateNotNull(date, FORMATDATE));
        model.addAttribute("title", "Thêm mới bệnh án");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("form", form);
        model.addAttribute("options", options);
        model.addAttribute("feedback", feedback);
        model.addAttribute("isARV", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());
        return "backend/opc_arv/arv_new";
    }

    /**
     * Submit form thêm mới
     *
     * @author DSNAnh
     * @param model
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/opc-arv/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid OpcArvNewForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws ParseException {
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, null);
        if (isOpcManager()) {
            options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        }
        LoggedUser currentUser = getCurrentUser();
        String currentSiteID = currentUser.getSite().getID().toString();
        LocalDate date18 = LocalDate.now().minusMonths(18);//trừ 18 tháng so với hiện tại
        Date date = Date.from(date18.atStartOfDay(ZoneId.systemDefault()).toInstant());
        form.setDateBefore18mon(TextUtils.formatDate(date, FORMATDATE));
        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        if (isOPC() && !isOpcManager()) {
            form.setTherapySiteID(currentSiteID);
        }

        model.addAttribute("title", "Thêm mới bệnh nhân");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("form", form);
        model.addAttribute("options", options);
        model.addAttribute("isARV", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());

        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }
        opcArvNewValidate.validate(form, bindingResult);
        form.setIsStageTimeError(bindingResult.getFieldError("treatmentStageTime") != null);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới bệnh án không thành công.");
            return "backend/opc_arv/arv_new";
        }
        if (isOpcManager() && "3".equals(form.getEndCase())) {
            model.addAttribute("error", "Khoa điều trị không được thực hiện chuyển gửi bệnh nhân");
            return "backend/opc_arv/arv_new";
        }
        try {

            OpcArvEntity arv = opcArvService.create(form.setToEntity(null));
            if (StringUtils.isNotEmpty(arv.getEndCase()) && arv.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                arv.setTranferFromTime(arv.getEndTime());
            }
            // TH tiếp nhận từ HTC
            HtcVisitEntity htcEntity = new HtcVisitEntity();
            if (StringUtils.isNotEmpty(form.getHtcID())) {
                htcEntity = htcService.findOne(Long.parseLong(form.getHtcID()));
                if (StringUtils.isNotEmpty(htcEntity.getArvExchangeResult()) && htcEntity.getArvExchangeResult().equals(ArvExchangeEnum.DONG_Y.getKey())) {
                    htcEntity.setArrivalTime(arv.getTranferToTime());
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_TIEP_NHAN.getKey());
                    //                htcEntity.setArrivalTime(arv.getTranferToTime());
                    htcService.save(htcEntity);
                    htcService.log(htcEntity.getID(), String.format("Khách hàng được chuyển gửi đến cơ sở %s đã được tiếp nhận", getCurrentUser().getSite().getName()));
                }

            }

            //Mail
            if (arv.getTransferSiteID() != null && !arv.getTransferSiteID().equals(0L) && (arv.getEndCase() != null && arv.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey()))) {
                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(arv.getTransferSiteID());
                variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arv.getCode());
                variables.put("transferSiteName", transferSite == null ? arv.getTransferSiteID() : transferSite.getName());
                variables.put("currentSiteName", getCurrentUser().getSite().getName());
                variables.put("gender", options.get(ParameterEntity.GENDER).get(arv.getPatient().getGenderID()));
                variables.put("fullName", arv.getPatient().getFullName());
                variables.put("dob", TextUtils.formatDate(arv.getPatient().getDob(), FORMATDATE));
                variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển tiếp điều trị bệnh nhân mã %s", arv.getCode()), EmailoutboxEntity.ARV_TRANSFER_MAIL, variables);
            }
            //end Mail
            if (StringUtils.isEmpty(form.getHtcID())) {
                opcArvService.logArv(arv.getID(), arv.getPatientID(), "Thêm mới bệnh án");
            } else {
                opcArvService.logArv(arv.getID(), arv.getPatientID(), "Tự động tạo bệnh án khi tiếp nhận từ HTC");
            }

            // Gửi phản hồi tiếp nhận 
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "printable".equals(form.getPageRedirect())) {
                arv.setFeedbackResultsReceivedTime(StringUtils.isEmpty(form.getFeedbackResultsReceivedTime()) ? new Date() : TextUtils.convertDate(form.getFeedbackResultsReceivedTime(), FORMATDATE));
                if (StringUtils.isNotEmpty(form.getHtcID())) {
                    htcEntity = htcService.findOne(Long.parseLong(form.getHtcID()));
                    arv.setDateOfArrival(htcEntity.getExchangeTime());
                    Long lastUpdateBy = htcEntity.getUpdatedBY();
                    htcEntity.setRegisterTherapyTime(arv.getRegistrationTime());
                    htcEntity.setRegisteredTherapySite(currentUser.getSite().getName());
                    htcEntity.setTherapyRegistProvinceID(currentUser.getSite().getProvinceID());
                    htcEntity.setTherapyRegistDistrictID(currentUser.getSite().getDistrictID());
                    htcEntity.setTherapyNo(arv.getCode());
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
//                    htcEntity.setArrivalTime(arv.getTranferToTime());
                    htcEntity.setFeedbackReceiveTime(arv.getFeedbackResultsReceivedTime());
                    htcEntity.setUpdateAt(new Date());
                    htcEntity.setUpdatedBY(currentUser.getUser().getID());
                    htcService.save(htcEntity);
                    staffService.sendMessage(lastUpdateBy, "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Khách hàng: %s", arv.getPatient().getConfirmCode()), String.format("%s&code=%s", UrlUtils.htcIndex("opc"), htcEntity.getCode()));
                }
                opcArvService.update(arv);

                if ("1".equals(arv.getRegistrationType()) && arv.getSourceSiteID() != null && arv.getSourceSiteID() != 0) {
                    String sourceSiteName = arv.getSourceSiteID() == -1 ? arv.getSourceArvSiteName() : siteService.findOne(arv.getSourceSiteID()).getName();
                    opcArvService.logArv(arv.getID(), arv.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + sourceSiteName);
                }
                if ("3".equals(arv.getRegistrationType())) {
                    String sourceSiteName = arv.getSourceSiteID() == -1 ? arv.getSourceArvSiteName() : siteService.findOne(arv.getSourceSiteID()).getName();
                    opcArvService.logArv(arv.getID(), arv.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ " + sourceSiteName);
                }
                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(arv.getSourceSiteID());
                if (transferSite != null) {
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", arv.getPatient().getFullName());
                    variables.put("confirmCode", arv.getPatient().getConfirmCode());
                    variables.put("code", arv.getCode());
                    variables.put("tranferToTime", TextUtils.formatDate(arv.getTranferToTime(), FORMATDATE));
                    sendEmail(getSiteEmail(transferSite.getID(), ServiceEnum.HTC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", "1".equals(arv.getRegistrationType()) ? EmailoutboxEntity.ARV_RECEIVE_SUCCESS_MAIL : EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
                }
                if ("1".equals(arv.getRegistrationType())) {
                    redirectAttributes.addFlashAttribute("success", "Gửi phản hồi tiếp nhận khách hàng chuyển gửi điều trị thành công");
                }
                if ("3".equals(arv.getRegistrationType())) {
                    redirectAttributes.addFlashAttribute("success", "Gửi phản hồi tiếp nhận bệnh nhân chuyển gửi điều trị thành công");
                }

                if (StringUtils.isEmpty(form.getHtcID())) {
                    if (RegistrationTypeEnum.NEW.getKey().equals(arv.getRegistrationType()) && arv.getSourceSiteID() != null && arv.getSourceSiteID() != 0) {
                        redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
                        return redirect(UrlUtils.opcArvIndex(arv.getID(), form.getPageRedirect()));
                    }
                }
                if (StringUtils.isEmpty(form.getArvID())) {
                    if (RegistrationTypeEnum.TRANSFER.getKey().equals(arv.getRegistrationType()) && arv.getSourceSiteID() != null && arv.getSourceSiteID() != 0) {
                        redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
                        return redirect(UrlUtils.opcArvIndex(arv.getID(), form.getPageRedirect()));
                    }
                }
//                return redirect(StringUtils.isEmpty(form.getHtcID()) ? UrlUtils.opcArvReceiveHTC() : UrlUtils.opcArvReceiveHTC(arv.getID(), Long.parseLong(form.getHtcID()), form.getPageRedirect()));
                return redirect(UrlUtils.opcArvIndex(arv.getID(), form.getPageRedirect()));
            }

            // Thêm mới Có thông tin cơ sở chuyển đến
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "confirm".equals(form.getPageRedirect())) {
                if (RegistrationTypeEnum.NEW.getKey().equals(arv.getRegistrationType()) && arv.getSourceSiteID() != null && arv.getSourceSiteID() != 0) {
                    String sourceSiteName = arv.getSourceSiteID() == -1 ? arv.getSourceArvSiteName() : siteService.findOne(arv.getSourceSiteID()).getName();
                    opcArvService.logArv(arv.getID(), arv.getPatientID(), "Tiếp nhận khách hàng được chuyển gửi từ cơ sở " + sourceSiteName);
                    redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
                    return redirect(UrlUtils.opcArvNew(arv.getID().toString(), "htc"));
                }

                if (RegistrationTypeEnum.TRANSFER.getKey().equals(arv.getRegistrationType()) && arv.getSourceSiteID() != null && arv.getSourceSiteID() != 0) {
                    redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
                    return redirect(UrlUtils.opcArvNew(arv.getID().toString(), "opc"));
                }
            }
            redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
            return redirect(UrlUtils.opcIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Thêm mới bệnh án không thành công");
            return redirect(UrlUtils.opcIndex());
        }
    }

    /**
     * Cập nhật thông tin bệnh án
     *
     * @param model
     * @param ID
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-arv/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id") Long ID,
            OpcArvUpdateForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        form.setCurrentSiteID(currentUser.getSite().getID().toString());
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }

        LocalDate date18 = LocalDate.now().minusMonths(18);//trừ 18 tháng so với hiện tại
        Date date = Date.from(date18.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);

        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }

        // Đã chuyển gửi sang cơ sở khác không được sửa
        if (entity.getTranferToTimeOpc() != null) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh án #%s đã được tiếp nhận không thể sửa", entity.getCode()));
            return redirect(UrlUtils.opcArvView(entity.getID()));
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, entity);
        if (!(currentUser.getSite().getID().equals(entity.getSiteID()) || (options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }
        form.setIsOtherSite(!(currentUser.getSite().getID().equals(entity.getPatient().getSiteID()) || (isOpcManager() && entity.getPatient().getSiteID().equals(entity.getSiteID()) && options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getPatient().getSiteID().toString()))));
        //Set giá trị cho cho form
        form.setID(String.valueOf(ID));
        form.setArvID(form.getID());
        form.setSourceServiceID(entity.getSourceServiceID());
//        form.setPatientID(String.valueOf(entity.getPatient()));
        form.setCode(entity.getCode() == null ? "" : entity.getCode().toUpperCase());
        form.setFullName(entity.getPatient().getFullName());
        form.setGenderID(entity.getPatient().getGenderID());
        form.setDob(formatDateNotNull(entity.getPatient().getDob(), FORMATDATE));
        form.setIdentityCard(entity.getPatient().getIdentityCard());
        form.setRaceID(entity.getPatient().getRaceID());
        form.setJobID(entity.getJobID());

        form.setObjectGroupID(entity.getObjectGroupID());
        form.setInsurance(entity.getInsurance());
        form.setInsuranceNo(entity.getInsuranceNo());
        form.setInsuranceType(entity.getInsuranceType());
        form.setInsuranceSite(entity.getInsuranceSite());
        form.setInsuranceExpiry(formatDateNotNull(entity.getInsuranceExpiry(), FORMATDATE));
        form.setDateBefore18mon(formatDateNotNull(date, FORMATDATE));
        form.setInsurancePay(entity.getInsurancePay());
        form.setPatientPhone(entity.getPatientPhone());
        form.setRouteID(entity.getRouteID());
        //dia chi
        form.setPermanentAddress(entity.getPermanentAddress());
        form.setPermanentAddressGroup(entity.getPermanentAddressGroup());
        form.setPermanentAddressStreet(entity.getPermanentAddressStreet());
        form.setPermanentProvinceID(entity.getPermanentProvinceID());
        form.setPermanentDistrictID(entity.getPermanentDistrictID());
        form.setPermanentWardID(entity.getPermanentWardID());
        form.setCurrentAddress(entity.getCurrentAddress());
        form.setCurrentAddressGroup(entity.getCurrentAddressGroup());
        form.setCurrentAddressStreet(entity.getCurrentAddressStreet());
        form.setCurrentProvinceID(entity.getCurrentProvinceID());
        form.setCurrentDistrictID(entity.getCurrentDistrictID());
        form.setCurrentWardID(entity.getCurrentWardID());

        form.setSupporterName(entity.getSupporterName());
        form.setSupporterRelation(entity.getSupporterRelation());
        form.setSupporterPhone(entity.getSupporterPhone());

        form.setConfirmCode(entity.getPatient().getConfirmCode() == null ? "" : entity.getPatient().getConfirmCode().toUpperCase());
        form.setConfirmTime(formatDateNotNull(entity.getPatient().getConfirmTime(), FORMATDATE));
        form.setConfirmSiteID(String.valueOf(entity.getPatient().getConfirmSiteID()));
        form.setConfirmSiteName(entity.getPatient().getConfirmSiteName());
        form.setPcrOneTime(formatDateNotNull(entity.getPcrOneTime(), FORMATDATE));
        form.setPcrOneResult(entity.getPcrOneResult());
        form.setPcrTwoTime(formatDateNotNull(entity.getPcrTwoTime(), FORMATDATE));
        form.setPcrTwoResult(entity.getPcrTwoResult());
        form.setNote(entity.getNote());
        //Phần ngày xn đầu tiên
//        form.setStatusOfTreatmentID(entity.getStatusOfTreatmentID());
        form.setFirstTreatmentTime(formatDateNotNull(entity.getFirstTreatmentTime(), FORMATDATE));
        form.setFirstTreatmentRegimenID(entity.getFirstTreatmentRegimenID());
        form.setFirstCd4Time(formatDateNotNull(entity.getFirstCd4Time(), FORMATDATE));
        form.setFirstCd4Result(entity.getFirstCd4Result());
//        form.setFirstCd4Causes(entity.getFirstCd4Causes());
        form.setFirstViralLoadTime(formatDateNotNull(entity.getFirstViralLoadTime(), FORMATDATE));
        form.setFirstViralLoadResult(entity.getFirstViralLoadResult());
        form.setFirstViralLoadCauses(entity.getFirstViralLoadCauses());
        form.setTransferSiteID(entity.getTransferSiteID() == null || entity.getTransferSiteID() == 0 ? "" : entity.getTransferSiteID().toString());

        //Chuyển đến
        form.setRegistrationType(entity.getRegistrationType());
        form.setTranferToTime(formatDateNotNull(entity.getTranferToTime(), FORMATDATE));
        form.setDateOfArrival(formatDateNotNull(entity.getDateOfArrival(), FORMATDATE));
        form.setFeedbackResultsReceivedTime(formatDateNotNull(entity.getFeedbackResultsReceivedTime(), FORMATDATE));

        form.setSourceSiteID(entity.getSourceSiteID() == null || entity.getSourceSiteID() == 0 ? "" : String.valueOf(entity.getSourceSiteID()));

        form.setQualifiedTreatmentTime(formatDateNotNull(entity.getQualifiedTreatmentTime(), FORMATDATE));
        form.setReceivedWard(entity.getReceivedWard());

        form.setEndTime(formatDateNotNull(entity.getEndTime(), FORMATDATE));
        form.setEndCase(entity.getEndCase());
        form.setTransferSiteID(entity.getTransferSiteID() == null || entity.getTransferSiteID() == 0 ? "" : String.valueOf(entity.getTransferSiteID()));
        form.setTransferSiteName(entity.getTransferSiteName());
        form.setTransferCase(entity.getTransferCase());
        form.setTreatmentStageID(entity.getTreatmentStageID());

        form.setTreatmentStageTime(formatDateNotNull(entity.getTreatmentStageTime(), FORMATDATE));
        form.setTranferFromTime(formatDateNotNull(entity.getTranferFromTime(), FORMATDATE));

        form.setFeedbackResultsReceivedTimeOpc(formatDateNotNull(entity.getFeedbackResultsReceivedTimeOpc(), FORMATDATE));

        form.setPregnantStartDate(formatDateNotNull(entity.getPregnantStartDate(), FORMATDATE));
        form.setPregnantEndDate(formatDateNotNull(entity.getPregnantEndDate(), FORMATDATE));
        form.setJoinBornDate(formatDateNotNull(entity.getJoinBornDate(), FORMATDATE));

        //end phần thông tin cập nhật
        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("entity", entity);
        model.addAttribute("arvID", entity.getID());
        model.addAttribute("options", options);
        return "backend/opc_arv/arv_update";
    }

    /**
     * Cập nhật thông tin bệnh án
     *
     * @param model
     * @param ID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-arv/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long ID,
            @ModelAttribute("form") @Valid OpcArvUpdateForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }
        LoggedUser currentUser = getCurrentUser();
        LocalDate date18 = LocalDate.now().minusMonths(18);//trừ 18 tháng so với hiện tại
        Date date = Date.from(date18.atStartOfDay(ZoneId.systemDefault()).toInstant());
        form.setDateBefore18mon(TextUtils.formatDate(date, FORMATDATE));

        form.setCurrentSiteID(currentUser.getSite().getID().toString());
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);
        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, entity);
        //form
        form.setIsOtherSite(!(currentUser.getSite().getID().equals(entity.getPatient().getSiteID()) || (isOpcManager() && entity.getPatient().getSiteID().equals(entity.getSiteID()) && options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getPatient().getSiteID().toString()))));
        if (!(currentUser.getSite().getID().equals(entity.getSiteID()) || (options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }
        form.setCurrentCode(entity.getCode() == null ? "" : entity.getCode().toUpperCase());//check trùng mã trừ tường hợp mã hiện tại
        //set form de validate ngày XN khẳng định
        form.setRegistrationTime(entity.getRegistrationTime() == null ? "" : TextUtils.formatDate(entity.getRegistrationTime(), FORMATDATE));
        form.setTreatmentTime(entity.getTreatmentTime() == null ? "" : TextUtils.formatDate(entity.getTreatmentTime(), FORMATDATE));
        form.setEndTime(entity.getEndTime() == null ? "" : TextUtils.formatDate(entity.getEndTime(), FORMATDATE));

        List<OpcTestEntity> models = opcTestService.findByPatientIDorderByCD4Time(entity.getPatientID(), false, isOPC() ? currentUser.getSite().getID() : entity.getSiteID());
        List<OpcStageEntity> stageModels = opcStageService.findByPatientID(entity.getPatientID(), false, isOPC() ? currentUser.getSite().getID() : entity.getSiteID());
        List<OpcViralLoadEntity> viralModels = opcViralLoadService.findByPatientID(entity.getPatientID(), false, isOPC() ? currentUser.getSite().getID() : entity.getSiteID());

        form.setCd4Time(models == null || models.isEmpty() || models.get(0) == null || models.get(0).getCd4TestTime() == null ? "" : TextUtils.formatDate(models.get(0).getCd4TestTime(), FORMATDATE));
        form.setTreamentTimeValidate(stageModels == null || stageModels.isEmpty() || stageModels.get(0) == null || stageModels.get(0).getTreatmentTime() == null ? "" : TextUtils.formatDate(stageModels.get(0).getTreatmentTime(), FORMATDATE));
        form.setViralTimeValidate(viralModels == null || viralModels.isEmpty() || viralModels.get(0) == null || viralModels.get(0).getTestTime() == null ? "" : TextUtils.formatDate(viralModels.get(0).getTestTime(), FORMATDATE));
//        form.setStatusOfTreatmentID(entity.getStatusOfTreatmentID());
        form.setEndCase(entity.getEndCase());
        form.setTransferSiteID(entity.getTransferSiteID() == null || entity.getTransferSiteID() == 0 ? "" : entity.getTransferSiteID().toString());

        form.setEndTime(formatDateNotNull(entity.getEndTime(), FORMATDATE));
        form.setEndCase(entity.getEndCase());
        form.setTransferSiteID(entity.getTransferSiteID() == null || entity.getTransferSiteID() == 0 ? "" : String.valueOf(entity.getTransferSiteID()));
        form.setTransferSiteName(entity.getTransferSiteName());
        form.setTransferCase(entity.getTransferCase());
        form.setTreatmentStageID(entity.getTreatmentStageID());

        form.setTreatmentStageTime(formatDateNotNull(entity.getTreatmentStageTime(), FORMATDATE));
        form.setTranferFromTime(formatDateNotNull(entity.getTranferFromTime(), FORMATDATE));

        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("entity", entity);
        model.addAttribute("arvID", entity.getID());
        model.addAttribute("options", options);

        opcArvUpdateValidate.validate(form, bindingResult);
        form.setRegistrationType(entity.getRegistrationType());
        form.setSourceSiteID(entity.getSourceSiteID() == null || entity.getSourceSiteID().equals("0") ? null : entity.getSourceSiteID().toString());

        if (form.getIsOtherSite()) {
            form.setFullName(entity.getPatient().getFullName());
            form.setGenderID(entity.getPatient().getGenderID());
            form.setDob(TextUtils.formatDate(entity.getPatient().getDob(), FORMATDATE));
            form.setIdentityCard(entity.getPatient().getIdentityCard());
            form.setRaceID(entity.getPatient().getRaceID());
//            form.setJobID(entity.getJobID());
            form.setConfirmCode(entity.getPatient().getConfirmCode());
            form.setConfirmTime(TextUtils.formatDate(entity.getPatient().getConfirmTime(), FORMATDATE));
            form.setConfirmSiteID(entity.getPatient().getConfirmSiteID().toString());
            form.setConfirmSiteName(entity.getPatient().getConfirmSiteName());

        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", " Cập nhật thông tin bệnh án không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/opc_arv/arv_update";
        }

        // Gán giá trị cho entity
        try {
            entity.setCode(form.getCode());

            OpcPatientEntity patientEntity = entity.getPatient();

            patientEntity.setFullName(form.getFullName());
            patientEntity.setGenderID(form.getGenderID());
            patientEntity.setDob(StringUtils.isEmpty(form.getDob()) ? null : TextUtils.convertDate(form.getDob(), "dd/MM/yyyy"));
            patientEntity.setIdentityCard(form.getIdentityCard());
            patientEntity.setRaceID(form.getRaceID());

            patientEntity.setConfirmCode(form.getConfirmCode());
            patientEntity.setConfirmTime(StringUtils.isEmpty(form.getConfirmTime()) ? null : TextUtils.convertDate(form.getConfirmTime(), "dd/MM/yyyy"));
            patientEntity.setConfirmSiteID(StringUtils.isNotEmpty(form.getConfirmSiteID()) ? Long.valueOf(form.getConfirmSiteID()) : null);
            patientEntity.setConfirmSiteName(form.getConfirmSiteName());
            entity.setPatient(patientEntity);

            entity.setJobID(form.getJobID());
            entity.setObjectGroupID(form.getObjectGroupID());
            entity.setInsurance(form.getInsurance());
            entity.setInsuranceNo(StringUtils.isNotEmpty(form.getInsuranceNo()) ? form.getInsuranceNo().trim().toUpperCase() : "");
            entity.setInsuranceType(form.getInsuranceType());
            entity.setInsuranceSite(form.getInsuranceSite());
            entity.setInsuranceExpiry(StringUtils.isEmpty(form.getInsuranceExpiry()) ? null : TextUtils.convertDate(form.getInsuranceExpiry(), "dd/MM/yyyy"));
            entity.setInsurancePay(form.getInsurancePay());
            entity.setPatientPhone(form.getPatientPhone());
            entity.setPcrOneTime(StringUtils.isEmpty(form.getPcrOneTime()) ? null : TextUtils.convertDate(form.getPcrOneTime(), "dd/MM/yyyy"));
            entity.setPcrOneResult(form.getPcrOneResult());
            entity.setPcrTwoTime(StringUtils.isEmpty(form.getPcrTwoTime()) ? null : TextUtils.convertDate(form.getPcrTwoTime(), "dd/MM/yyyy"));
            entity.setPcrTwoResult(form.getPcrTwoResult());
            entity.setRouteID(form.getRouteID());
//dia chi
            entity.setPermanentAddress(form.getPermanentAddress());
            entity.setPermanentAddressGroup(form.getPermanentAddressGroup());
            entity.setPermanentAddressStreet(form.getPermanentAddressStreet());
            entity.setPermanentProvinceID(form.getPermanentProvinceID());
            entity.setPermanentDistrictID(form.getPermanentDistrictID());
            entity.setPermanentWardID(form.getPermanentWardID());

            entity.setCurrentAddress(form.getIsDisplayCopy() ? form.getPermanentAddress() : form.getCurrentAddress());
            entity.setCurrentAddressGroup(form.getIsDisplayCopy() ? form.getPermanentAddressGroup() : form.getCurrentAddressGroup());
            entity.setCurrentAddressStreet(form.getIsDisplayCopy() ? form.getPermanentAddressStreet() : form.getCurrentAddressStreet());
            entity.setCurrentProvinceID(form.getIsDisplayCopy() ? form.getPermanentProvinceID() : form.getCurrentProvinceID());
            entity.setCurrentDistrictID(form.getIsDisplayCopy() ? form.getPermanentDistrictID() : form.getCurrentDistrictID());
            entity.setCurrentWardID(form.getIsDisplayCopy() ? form.getPermanentWardID() : form.getCurrentWardID());

            entity.setSupporterName(TextUtils.toFullname(form.getSupporterName()));
            entity.setSupporterRelation(form.getSupporterRelation());
            entity.setSupporterPhone(form.getSupporterPhone());
            entity.setNote(form.getNote());
            //Phần ngày xn đầu tiên
//            entity.setStatusOfTreatmentID(form.getStatusOfTreatmentID());
            entity.setFirstTreatmentTime(StringUtils.isEmpty(form.getFirstTreatmentTime()) ? null : TextUtils.convertDate(form.getFirstTreatmentTime(), "dd/MM/yyyy"));
            entity.setFirstTreatmentRegimenID(form.getFirstTreatmentRegimenID());
            entity.setFirstCd4Time(StringUtils.isEmpty(form.getFirstCd4Time()) ? null : TextUtils.convertDate(form.getFirstCd4Time(), "dd/MM/yyyy"));
            entity.setFirstCd4Result(form.getFirstCd4Result());
//            entity.setFirstCd4Causes(form.getFirstCd4Causes());
            entity.setFirstViralLoadTime(StringUtils.isEmpty(form.getFirstViralLoadTime()) ? null : TextUtils.convertDate(form.getFirstViralLoadTime(), "dd/MM/yyyy"));
            entity.setFirstViralLoadResult(form.getFirstViralLoadResult());
            entity.setFirstViralLoadCauses(form.getFirstViralLoadCauses());

            entity.setTranferToTime(StringUtils.isEmpty(form.getTranferToTime()) ? null : TextUtils.convertDate(form.getTranferToTime(), "dd/MM/yyyy"));
            entity.setDateOfArrival(StringUtils.isEmpty(form.getDateOfArrival()) ? null : TextUtils.convertDate(form.getDateOfArrival(), "dd/MM/yyyy"));
            entity.setFeedbackResultsReceivedTime(StringUtils.isEmpty(form.getFeedbackResultsReceivedTime()) ? null : TextUtils.convertDate(form.getFeedbackResultsReceivedTime(), "dd/MM/yyyy"));

            entity.setQualifiedTreatmentTime(TextUtils.convertDate(form.getQualifiedTreatmentTime(), FORMATDATE));
            entity.setReceivedWard(form.getReceivedWard());

            entity.setTranferToTimeOpc(StringUtils.isEmpty(form.getTranferToTimeOpc()) ? null : TextUtils.convertDate(form.getTranferToTimeOpc(), "dd/MM/yyyy"));
            entity.setFeedbackResultsReceivedTimeOpc(StringUtils.isEmpty(form.getFeedbackResultsReceivedTimeOpc()) ? null : TextUtils.convertDate(form.getFeedbackResultsReceivedTimeOpc(), "dd/MM/yyyy"));

            entity.setPregnantStartDate(TextUtils.convertDate(form.getPregnantStartDate(), FORMATDATE));
            entity.setPregnantEndDate(TextUtils.convertDate(form.getPregnantEndDate(), FORMATDATE));
            entity.setJoinBornDate(TextUtils.convertDate(form.getJoinBornDate(), FORMATDATE));

            if (!entity.getObjectGroupID().equals("5") && !entity.getObjectGroupID().equals("5.1") && !entity.getObjectGroupID().equals("5.2")) {
                entity.setPregnantStartDate(null);
                entity.setPregnantEndDate(null);
                entity.setJoinBornDate(null);
            }
            if (StringUtils.isNotEmpty(entity.getEndCase()) && entity.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                entity.setTranferFromTime(entity.getEndTime());
            }
            entity = opcArvService.update(entity);
            opcArvService.logArv(ID, entity.getPatientID(), "Cập nhật bệnh án");
            //Bổ sung 03/06/2020
            HtcVisitEntity htcEntity = new HtcVisitEntity();
            OpcArvEntity oldEntity = new OpcArvEntity();
            if (entity.getSourceID() != null && entity.getSourceServiceID() != null && entity.getRegistrationType().equals(RegistrationTypeEnum.NEW.getKey()) && entity.getSourceServiceID().equals(ServiceEnum.HTC.getKey())) {
                htcEntity = htcService.findOne(entity.getSourceID());
            }
            if (entity.getSourceID() != null && entity.getSourceServiceID() != null && entity.getRegistrationType().equals(RegistrationTypeEnum.TRANSFER.getKey()) && entity.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) {
                oldEntity = opcArvService.findOne(entity.getSourceID());
            }
            if (entity.getSourceServiceID() != null && entity.getRegistrationType().equals(RegistrationTypeEnum.NEW.getKey()) && entity.getSourceServiceID().equals(ServiceEnum.HTC.getKey()) && entity.getFeedbackResultsReceivedTime() != null) {
                if (htcEntity.getID() != null && htcEntity.getFeedbackReceiveTime() == null) {
                    opcArvService.logArv(entity.getID(), entity.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + siteService.findOne(htcEntity.getSiteID()).getName());
                    Long lastUpdateBy = htcEntity.getUpdatedBY();
                    htcEntity.setRegisterTherapyTime(entity.getRegistrationTime());
                    htcEntity.setRegisteredTherapySite(currentUser.getSite().getName());
                    htcEntity.setTherapyRegistProvinceID(currentUser.getSite().getProvinceID());
                    htcEntity.setTherapyRegistDistrictID(currentUser.getSite().getDistrictID());
                    htcEntity.setTherapyNo(entity.getCode());
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
                    htcEntity.setArrivalTime(entity.getTranferToTime());
                    htcEntity.setFeedbackReceiveTime(entity.getFeedbackResultsReceivedTime());
                    htcEntity.setUpdateAt(new Date());
                    htcEntity.setUpdatedBY(currentUser.getUser().getID());
                    htcService.save(htcEntity);
                    //Gửi email thông báo
                    Map<String, Object> variables = new HashMap<>();
                    SiteEntity transferSite = siteService.findOne(htcEntity.getSiteID());
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", transferSite == null ? "" : transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", entity.getPatient().getFullName());
                    variables.put("confirmCode", entity.getPatient().getConfirmCode());
                    variables.put("tranferToTime", TextUtils.formatDate(entity.getTranferToTime(), FORMATDATE));
                    sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.HTC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.ARV_RECEIVE_SUCCESS_MAIL, variables);
                    staffService.sendMessage(lastUpdateBy, "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Khách hàng: %s", entity.getPatient().getConfirmCode()), String.format("%s&code=%s", UrlUtils.htcIndex("opc"), htcEntity.getCode()));
                }
            }
            if (entity.getSourceServiceID() != null && entity.getRegistrationType().equals(RegistrationTypeEnum.TRANSFER.getKey()) && entity.getSourceServiceID().equals(ServiceEnum.OPC.getKey()) && entity.getFeedbackResultsReceivedTime() != null) {
                if (oldEntity.getID() != null && oldEntity.getFeedbackResultsReceivedTimeOpc() == null) {
                    Long lastUpdateBy = oldEntity.getUpdatedBY();
                    // Đồng ý phản hổi tiếp nhận
                    oldEntity.setFeedbackResultsReceivedTimeOpc(entity.getFeedbackResultsReceivedTime());
                    opcArvService.update(oldEntity);//cập nhật bản ghi cũ
                    opcArvService.logArv(entity.getID(), oldEntity.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ " + options.get("siteOpcTo").get(oldEntity.getSiteID() != null ? oldEntity.getSiteID().toString() : ""));
                    //Gửi email thông báo
                    Map<String, Object> variables = new HashMap<>();
                    SiteEntity sourceSite = siteService.findOne(entity.getSourceArvSiteID());
                    if (sourceSite != null) {
                        variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                        variables.put("transferSiteName", sourceSite.getName());
                        variables.put("currentSiteName", getCurrentUser().getSite().getName());
                        variables.put("fullName", entity.getPatient().getFullName());
                        variables.put("code", entity.getCode());
                        variables.put("tranferToTime", TextUtils.formatDate(entity.getTranferToTime(), FORMATDATE));
                        sendEmail(getSiteEmail(sourceSite.getID(), ServiceEnum.OPC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
                    }
                    staffService.sendMessage(lastUpdateBy, "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Bệnh nhân: %s", oldEntity.getCode()), String.format("%s?code=%s", UrlUtils.opcIndex(), oldEntity.getCode()));
                }
            }
            //03/06/2020
            redirectAttributes.addFlashAttribute("success", "Bệnh án được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());
            return redirect(UrlUtils.opcIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/opc_arv/arv_update";
        }
    }

    /**
     * Xem thông tin bệnh án
     *
     * @param model
     * @param ID
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-arv/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);
        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, null);

        if (!(currentUser.getSite().getID().equals(entity.getSiteID()) || (options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcIndex());
        }

        List<OpcChildEntity> listChilds = opcChildService.findByArvID(entity.getID());

        model.addAttribute("urlBack", UrlUtils.opcIndex());
        model.addAttribute("urlCurrentPage", UrlUtils.opcArvView(ID));
        model.addAttribute("urlBreadcrumb", UrlUtils.opcIndex());
        model.addAttribute("breadcrumbTitle", "Điều trị ngoại trú");
        model.addAttribute("title", "Xem thông tin bệnh án");

        model.addAttribute("childs", listChilds);
        model.addAttribute("entity", entity);
        model.addAttribute("options", options);
        return "backend/opc_arv/view";
    }

    //Xóa bệnh nhân
    @RequestMapping(value = "/opc-arv/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = opcArvService.findOne(ID);

        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
                return redirect(UrlUtils.opcIndex());
            }
        } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
            return redirect(UrlUtils.opcIndex());
        }

        List<OpcStageEntity> listStage = opcStageService.findByPatientID(arv.getPatientID(), false);
        OpcStageEntity stage = new OpcStageEntity();
        if (listStage != null && !listStage.isEmpty()) {
            stage = listStage.get(0);
        } else {
            stage = null;
        }

        if (stage != null && (stage.getEndTime() == null || StringUtils.isEmpty(stage.getEndCase()))
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không được xóa bệnh án khi có đợt điều trị chưa kết thúc");
            return redirect(UrlUtils.opcIndex());
        }

        // Kiểm tra đã được chuyển gửi chưa
        if (arv.getTranferToTimeOpc() != null && StringUtils.isNotEmpty(arv.getEndCase()) && arv.getEndTime() != null && !currentUser.getSite().getID().equals(arv.getTransferSiteID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh nhân #%s đã được tiếp nhận không được phép xóa", arv.getCode()));
            return redirect(UrlUtils.opcIndex());
        }

        try {
            arv.setRemoteAT(new Date());
            arv.setRemove(true);
            opcArvService.update(arv);
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Xoá bệnh án");
            List<OpcArvRevisionEntity> listLog = opcArvService.findByArvIDAndSite(arv.getID(), arv.getSiteID());
            if (listLog != null && !listLog.isEmpty()) {
                OpcArvRevisionEntity log = listLog.get(listLog.size() - 1);
                log.setID(null);
                log.setRemove(true);
                log.setCreateAT(new Date());
                revisionRepository.save(log);
            }

            redirectAttributes.addFlashAttribute("success", "Xóa bệnh án thành công.");
            return redirect(UrlUtils.opcIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Xóa bệnh án thất bại.");
            return redirect(UrlUtils.opcIndex());
        }
    }

    /**
     * Khôi phục bệnh nhân
     *
     * @author DSNAnh
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-arv/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = opcArvService.findOne(ID);

        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
                return redirect(UrlUtils.opcIndex());
            }
        } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
            return redirect(UrlUtils.opcIndex());
        }

        // Kiểm tra đã được chuyển gửi chưa
        if (arv.getTranferToTimeOpc() != null && StringUtils.isNotEmpty(arv.getEndCase()) && arv.getEndTime() != null && !currentUser.getSite().getID().equals(arv.getTransferSiteID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh nhân #%s đã được tiếp nhận không được phép khôi phục", arv.getCode()));
            return redirect(UrlUtils.opcIndex());
        }

        try {
            arv.setRemove(false);
            arv.setRemoteAT(null);
            opcArvService.update(arv);

            List<OpcArvRevisionEntity> listLog = opcArvService.findByArvIDAndSite(arv.getID(), arv.getSiteID());
            if (listLog != null && !listLog.isEmpty()) {
                OpcArvRevisionEntity log = listLog.get(listLog.size() - 1);
                log.setID(null);
                log.setRemove(false);
                log.setCreateAT(new Date());
                revisionRepository.save(log);
            }

            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Khôi phục bệnh án");
            redirectAttributes.addFlashAttribute("success", "Khôi phục bệnh án đã xóa thành công");
            redirectAttributes.addFlashAttribute("success_id", arv.getID());
            return redirect(UrlUtils.opcIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Khôi phục thất bại");
            return redirect(UrlUtils.opcIndex());
        }
    }

    /**
     * Xóa vĩnh viễn bệnh nhân
     *
     * @author DSNAnh
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-arv/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = opcArvService.findOne(ID);
        if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }

        // Kiểm tra đã được chuyển gửi chưa
        if (arv.getTranferToTime() != null && StringUtils.isNotEmpty(arv.getEndCase()) && arv.getEndTime() != null && !currentUser.getSite().getID().equals(arv.getTransferSiteID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh nhân #%s đã được chuyển gửi không được phép xóa", arv.getCode()));
            return redirect(UrlUtils.opcIndex());
        }

        try {
            opcArvService.remove(arv.getID());
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Xoá vĩnh viễn bệnh án #" + arv.getCode());
            redirectAttributes.addFlashAttribute("success", "Bệnh án đã bị xóa vĩnh viễn");
            return redirect(UrlUtils.opcIndex() + "?tab=remove");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Xóa vĩnh viễn thất bại." + ex.getMessage());
            return redirect(UrlUtils.opcIndex() + "?tab=remove");
        }
    }

    private String formatDateNotNull(Date date, String format) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(format).format(date);
    }

    @RequestMapping(value = {"/opc-arv/update-treatment-stage.html"}, method = RequestMethod.GET)
    public String actionUpdateTreatmentStage(Model model,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
//        if (!"72".equals(currentUser.getSite().getProvinceID())) {
//            redirectAttributes.addFlashAttribute("error", "Chức năng này chỉ sử dụng cho tỉnh Tây Ninh");
//            return redirect(UrlUtils.opcIndex());
//        }

        try {
            List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            Set<Long> siteIDs = new HashSet<>();
            for (SiteEntity siteEntity : siteOpc) {
                siteIDs.add(siteEntity.getID());
            }
            List<OpcArvEntity> arvs = opcArvService.findBySiteIDs(siteIDs);
            List<OpcVisitEntity> visitOK = new ArrayList<>();
            if (arvs != null) {
                for (OpcArvEntity arv : arvs) {
                    boolean success = false;
                    if (StringUtils.isNotEmpty(arv.getTreatmentRegimenID())) {
                        continue;
                    }
                    List<OpcStageEntity> stages = opcStageService.findByArvID(arv.getID(), false);
                    if (stages != null) {
                        for (OpcStageEntity stage : stages) {
                            if (StringUtils.isNotEmpty(stage.getTreatmentRegimenID())) {
                                continue; 
                            }
                            LinkedList<OpcVisitEntity> visits = opcVisitService.findByStageIDAndOderByExaminationTime(stage.getID());
                            if (visits != null) {
                                String key1 = "";
                                String key2 = "";
                                for (OpcVisitEntity visit : visits) {
                                    if (StringUtils.isNotEmpty(visit.getTreatmentRegimenID())) {
                                        key1 = visit.getTreatmentRegimenID();
                                        key2 = visit.getTreatmentRegimenStage();
                                    } else {
                                        visit.setTreatmentRegimenID(key1);
                                        visit.setTreatmentRegimenStage(StringUtils.isEmpty(key2) ? "1" : key2);
                                        visitOK.add(visit);
                                        success = true;
                                    }
                                }
                            }

                        }

                    }
                    if (success) {
                        opcArvService.logArv(arv.getID(), arv.getPatientID(), "Bệnh án được cập nhật phác đồ qua xử lý tự động");
                    }
                }
            }
            for (OpcVisitEntity opcVisitEntity : visitOK) {
                opcVisitService.save(opcVisitEntity);
            }

            redirectAttributes.addFlashAttribute("success", "Đã tiến hành cập nhật phác đồ cho bệnh án");
            return redirect(UrlUtils.opcIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật phác đồ cho bệnh án thất bại" + ex.getMessage());
            return redirect(UrlUtils.opcIndex());
        }
    }

}
