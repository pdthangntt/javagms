package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcChildEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcChildForm;
import com.gms.entity.form.opc_arv.OpcVisitForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.entity.validate.OpcVisitValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcChildService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcVisitService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DSNAnh
 */
@Controller(value = "backend_opc_visit")
public class OpcVisitController extends OpcController {

    @Autowired
    private OpcStageService stageService;
    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcVisitService visitService;
    @Autowired
    SiteService siteService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcVisitValidate validate;
    @Autowired
    OpcChildService opcChildService;

    /**
     * 
     * @param model
     * @param tab
     * @param id
     * @param pageRedirect
     * @param code
     * @param name
     * @param identityCard
     * @param insurance
     * @param insuranceType
     * @param insuranceNo
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param registrationTimeFrom
     * @param registrationTimeTo
     * @param viralLoadTimeFrom
     * @param viralLoadTimeTo
     * @param resultID
     * @param treatmentStageTimeFrom
     * @param treatmentStageTimeTo
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param lastExaminationTimeFrom
     * @param lastExaminationTimeTo
     * @param statusOfTreatmentID
     * @param treatmentStageID
     * @param therapySiteID
     * @param page
     * @param size
     * @param redirectAttributes
     * @return 
     */
    @RequestMapping(value = {"/opc-visit/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "insurance", required = false) String insurance,
            @RequestParam(name = "insurance_type", required = false) String insuranceType,
            @RequestParam(name = "insurance_no", required = false) String insuranceNo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "viral_load_time_from", required = false) String viralLoadTimeFrom,//ngày hẹn khám
            @RequestParam(name = "viral_load_time_to", required = false) String viralLoadTimeTo,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "last_examination_time_from", required = false) String lastExaminationTimeFrom,
            @RequestParam(name = "last_examination_time_to", required = false) String lastExaminationTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "therapy_site_id", required = false) String therapySiteID,
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
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
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

        search.setRemove(false);
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);
        search.setInsurance(insurance);
        search.setInsuranceType(insuranceType);
        search.setLastExaminationTimeFrom(isThisDateValid(lastExaminationTimeFrom) ? lastExaminationTimeFrom : null);
        search.setLastExaminationTimeTo(isThisDateValid(lastExaminationTimeTo) ? lastExaminationTimeTo : null);
        search.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        search.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        search.setTreatmentStageID(treatmentStageID);
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
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setResultID(resultID);

        if (!StringUtils.isEmpty(viralLoadTimeFrom)) {
            search.setTreatmentTimeFrom(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);//ngày hẹn khám từ
            search.setTreatmentTimeTo(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);//ngày hẹn khám từ
        } else {
            search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);//ngày hẹn khám từ
            search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);//ngày hẹn khám từ
        }
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPageIndex(page);
        search.setPageSize(size);

        //tab - date 
        LocalDate localDate = LocalDate.now().minusDays(91);
        LocalDate localDate90 = LocalDate.now().minusDays(90);
        LocalDate localDate31 = LocalDate.now().minusDays(31);

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date90 = Date.from(localDate90.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date31 = Date.from(localDate31.atStartOfDay(ZoneId.systemDefault()).toInstant());
        search.setTime84(TextUtils.formatDate(date, FORMATDATE));
        search.setTime90(TextUtils.formatDate(date90, FORMATDATE));
        search.setTime30(TextUtils.formatDate(date31, FORMATDATE));
        
        search.setTab("all".equals(tab) ? "0" : "late".equals(tab) ? "2" : "late84".equals(tab) ? "3" : "late30".equals(tab) ? "4" : "1");

        DataPage<OpcArvEntity> dataPage = opcArvService.findVisit(search);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("options", options);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tabindex", tab);
        model.addAttribute("viral", "viral");

        model.addAttribute("title", "Danh sách bệnh nhân tái khám");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        return "backend/opc_arv/visit_index";
    }

    /**
     * DS lượt khám (Sửa bệnh án)
     *
     * @param model
     * @param arvID
     * @param tab
     * @param id
     * @param pageRedirect
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-visit/update.html"}, method = RequestMethod.GET)
    public String actionStage(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "tab", defaultValue = "all") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có quyền sử dụng dịch vụ này");
            return redirect(UrlUtils.opcIndex());
        }

        OpcArvEntity arv = arvService.findOne(arvID);
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        if (isOpcManager()) {
            if (arv == null || !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(arv.getSiteID().toString())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }
        } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcIndex());
        }
        if (arv.getTranferToTimeOpc() != null) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh án #%s đã được tiếp nhận không thể sửa", arv.getCode()));
            return redirect(UrlUtils.opcArvView(arv.getID()));
        }
        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), false));
        String treatmentRegimenStage = "";
        String treatmentRegimenID = "";
        String treatmentStage = "";
        List<OpcStageEntity> stageList = stageService.findByPatientID(arv.getPatientID(), false);
        if (stageList != null && stageList.size() > 0) {
            treatmentStage = stageList.get(0).getID().toString();
        }

        List<OpcVisitEntity> models = visitService.find(arv.getPatientID(), !tab.equals("all"));
        if (models != null && models.size() > 0) {
            treatmentRegimenStage = models.get(0).getTreatmentRegimenStage();
            treatmentRegimenID = models.get(0).getTreatmentRegimenID();
        }

        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("options", options);
        model.addAttribute("isOpc", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("stageOptions", getTreatmentStage(arv.getPatientID(), null, false));
        model.addAttribute("arv", arv);
        model.addAttribute("items", models);
        model.addAttribute("tab", tab);
        model.addAttribute("treatmentRegimenStage", treatmentRegimenStage);
        model.addAttribute("treatmentRegimenID", treatmentRegimenID);
        model.addAttribute("treatmentStage", treatmentStage);
        model.addAttribute("currentSiteID", currentUser.getSite().getID());
        return "backend/opc_arv/visit";
    }

    /**
     * Thêm mới lượt khám
     *
     * @author DSNAnh
     * @param model
     * @param form
     * @param arvID
     * @param ID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-visit/new.html"}, method = RequestMethod.GET)
    public String actionNewGet(Model model,
            OpcVisitForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "visitid", required = false) Long ID,
            @RequestParam(name = "stageid", required = false) Long stageID,
            RedirectAttributes redirectAttributes) {
        OpcArvEntity arv = arvService.findOne(arvID);
        HashMap<String, HashMap<String, String>> options = getOptions(true, arv);
        OpcStageEntity stage = new OpcStageEntity();
        if (stageID != null) {
            stage = stageService.findOne(stageID);
        }

        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), true));
        LoggedUser currentUser = getCurrentUser();
        List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        List<Long> siteIDs = new ArrayList<>();
        siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));
        if (arv == null || (!arv.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (isOpcManager() && !siteIDs.contains(arv.getSiteID()))) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }
        List<OpcStageEntity> stageList;
        if (isOpcManager()) {
            stageList = stageService.findByPatientID(arv.getPatientID(), false);
        } else {
            stageList = stageService.findByPatientID(arv.getPatientID(), false, currentUser.getSite().getID());
        }
        form = new OpcVisitForm(arv, stage);
        if (stageList != null && stageList.size() > 0) {
            OpcStageEntity lastStage = stageList.get(isOpcManager() ? 0 : stageList.size() - 1);
            String treatmentRegimenStage = StringUtils.isEmpty(lastStage.getTreatmentRegimenStage()) ? "0" : lastStage.getTreatmentRegimenStage();
            String treatmentRegimenID = StringUtils.isEmpty(lastStage.getTreatmentRegimenID()) ? "0" : lastStage.getTreatmentRegimenID();
            form.setStageID(lastStage.getID().toString() + "-" + treatmentRegimenStage + "-" + treatmentRegimenID + "-" + (StringUtils.isEmpty(lastStage.getStatusOfTreatmentID()) ? "99" : lastStage.getStatusOfTreatmentID()));
            form.setBaseTreatmentRegimenID(lastStage.getTreatmentRegimenID());
            form.setBaseTreatmentRegimenStage(lastStage.getTreatmentRegimenStage());
            form.setEndTime(TextUtils.formatDate(lastStage.getEndTime(), "dd/MM/yyyy"));
            form.setStatusOfTreatmentID(lastStage.getStatusOfTreatmentID());
            form.setRegistrationTime(TextUtils.formatDate(lastStage.getRegistrationTime(), "dd/MM/yyyy"));
            form.setTreatmentTime(TextUtils.formatDate(lastStage.getTreatmentTime(), "dd/MM/yyyy"));
        }
        model.addAttribute("title", "Thêm mới lượt khám");
        model.addAttribute("smallTitle", "Thêm mới lượt khám");
        model.addAttribute("form", form);
        model.addAttribute("arv", arv);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", options);

        return "backend/opc_arv/visit_new";
    }

    /**
     * Thực hiện thêm mới lượt khám
     *
     * @author DSNAnh
     * @param model
     * @param arvID
     * @param ID
     * @param stageID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/opc-visit/new.html"}, method = RequestMethod.POST)
    public String actionNewPost(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "visitid", required = false) Long ID,
            @RequestParam(name = "stageid", required = false) Long stageID,
            @ModelAttribute("form") @Valid OpcVisitForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws Exception {

        // Lọc lại danh sách
        List<OpcChildForm> childList = new ArrayList<>();
        if (form.getChilds() != null && form.getChilds().size() > 0) {
            for (OpcChildForm child : form.getChilds()) {
                childList.add(child);
            }
        }
        form.setChilds(childList);

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(arvID);
        HashMap<String, HashMap<String, String>> options = getOptions(true, arv);

        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        List<Long> siteIDs = new ArrayList<>();
        siteIDs.addAll(CollectionUtils.collect(siteOpc, TransformerUtils.invokerTransformer("getID")));
        if (arv == null || (!arv.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (isOpcManager() && !siteIDs.contains(arv.getSiteID()))) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }
        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), true));

        //set thông tin từ đợt điều trị
        if (StringUtils.isNotEmpty(form.getStageID())) {
            OpcStageEntity stage = stageService.findOne(Long.parseLong(form.getStageID().split("-")[0]));
            form.setBaseTreatmentRegimenID(stage.getTreatmentRegimenID());
            form.setBaseTreatmentRegimenStage(stage.getTreatmentRegimenStage());
            form.setEndTime(TextUtils.formatDate(stage.getEndTime(), "dd/MM/yyyy"));
            form.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
            form.setRegistrationTime(TextUtils.formatDate(stage.getRegistrationTime(), "dd/MM/yyyy"));
            form.setTreatmentTime(TextUtils.formatDate(stage.getTreatmentTime(), "dd/MM/yyyy"));
        }
        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }
        validate.validate(form, bindingResult);
        //Trả lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin");
            model.addAttribute("title", "Thêm mới lượt khám");
            model.addAttribute("smallTitle", "Thêm mới lượt khám");
            model.addAttribute("arv", arv);
            model.addAttribute("isOpcManager", isOpcManager());
            model.addAttribute("form", form);
            model.addAttribute("options", options);
            return "backend/opc_arv/visit_new";
        }
        OpcVisitEntity entity = new OpcVisitEntity();
        OpcVisitEntity result = form.setToEntity(entity);
        result.setArvID(arv.getID());
        result.setSiteID(arv.getSiteID());
        result.setPatientID(arv.getPatientID());
        if (isOpcManager()) {
            result.setSiteID(arv.getSiteID());
        } else {
            result.setSiteID(currentUser.getSite().getID());
        }
        visitService.save(result);
        // Lưu thông tin người được giới thiệu
        if (form.getChilds() != null && form.getChilds().size() > 0) {
            List<OpcChildEntity> childs = new ArrayList<>();
            OpcChildEntity childEntity;
            for (OpcChildForm child : form.getChilds()) {
                childEntity = new OpcChildEntity();
                child.setToEntity(childEntity);

                childEntity.setArvID(arv.getID());
                childEntity.setPatientID(result.getPatientID());
                childEntity.setStageID(result.getStageID());
                childEntity.setVisitID(result.getID());
                childs.add(childEntity);
            }

            // Lưu thông tin người được giới thiệu
            opcChildService.saveAll(arvID, result.getStageID(), result.getID(), childs);
        } else {
            // Lưu thông tin người được giới thiệu
            opcChildService.saveAll(arvID, result.getStageID(), result.getID(), null);
        }
        visitService.logVisit(result.getArvID(), result.getPatientID(), result.getID(), "Thêm mới lượt khám");

        List<OpcVisitEntity> models = visitService.findByArvID(arv.getID(), false);
        setToStageAndArv(models, result, arv);
        redirectAttributes.addFlashAttribute("success", "Thêm mới lượt khám thành công");
        if ("yes".equals(form.getIsPrinted())) {
            return redirect(String.format("%s&id=%s&page_redirect=yes", UrlUtils.opcVisit(arv.getID(), "all"), result.getID()));
        } else {
            return redirect(UrlUtils.opcVisit(arv.getID(), "all"));
        }
    }

    /**
     * Cập nhật lượt khám
     *
     * @author DSNAnh
     * @param model
     * @param form
     * @param arvID
     * @param ID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-visit/update-visit.html"}, method = RequestMethod.GET)
    public String actionUpdateGet(Model model,
            OpcVisitForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "visitid") Long ID,
            @RequestParam(name = "stageid", required = false) Long stageID,
            RedirectAttributes redirectAttributes) {
        OpcArvEntity arv = arvService.findOne(arvID);

        OpcStageEntity stage = new OpcStageEntity();
        OpcVisitEntity visitEntity = visitService.findOne(ID);
        if (visitEntity.getStageID() != null) {
            stage = stageService.findOne(visitEntity.getStageID());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, arv, stage, visitEntity);
        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), true));
        LoggedUser currentUser = getCurrentUser();
        List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        List<Long> siteIDs = new ArrayList<>();
        siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));
        if (arv == null || (!arv.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (isOpcManager() && !siteIDs.contains(arv.getSiteID()))) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }
        form = new OpcVisitForm(visitEntity);
        //set thông tin từ đợt điều trị
        if (stage != null) {
            String treatmentRegimenID = StringUtils.isEmpty(stage.getTreatmentRegimenID()) ? "0" : stage.getTreatmentRegimenID();
            String treatmentRegimenStage = StringUtils.isEmpty(stage.getTreatmentRegimenStage()) ? "0" : stage.getTreatmentRegimenStage();
            form.setStageID(stage.getID().toString() + "-" + treatmentRegimenStage + "-" + treatmentRegimenID + "-" + (StringUtils.isEmpty(stage.getStatusOfTreatmentID()) ? "99" : stage.getStatusOfTreatmentID()));
            form.setBaseTreatmentRegimenID(visitEntity.getTreatmentRegimenID());
            form.setBaseTreatmentRegimenStage(visitEntity.getTreatmentRegimenStage());
            form.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
            form.setEndTime(TextUtils.formatDate(stage.getEndTime(), "dd/MM/yyyy"));
            form.setRegistrationTime(TextUtils.formatDate(stage.getRegistrationTime(), "dd/MM/yyyy"));
            form.setTreatmentTime(TextUtils.formatDate(stage.getTreatmentTime(), "dd/MM/yyyy"));
        }
        List<OpcChildEntity> childList = opcChildService.findByArvIDAndStageIDAndVisitID(arv.getID(), visitEntity.getStageID(), visitEntity.getID());
        List<OpcChildForm> childs = new ArrayList<>();
        if (childList != null && childList.size() > 0) {
            for (OpcChildEntity entity : childList) {
                childs.add(new OpcChildForm(entity));
            }
        }
        form.setChilds(childs);
        model.addAttribute("title", "Cập nhật lượt khám");
        model.addAttribute("smallTitle", "Cập nhật lượt khám");
        model.addAttribute("form", form);
        model.addAttribute("arv", arv);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("visit", visitEntity);
        model.addAttribute("options", options);

        return "backend/opc_arv/visit_new";
    }

    /**
     * Thực hiện cập nhật lượt khám
     *
     * @author DSNAnh
     * @param model
     * @param arvID
     * @param ID
     * @param stageID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/opc-visit/update-visit.html"}, method = RequestMethod.POST)
    public String actionUpdatePost(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "visitid", required = false) Long ID,
            @RequestParam(name = "stageid", required = false) Long stageID,
            @ModelAttribute("form") @Valid OpcVisitForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws Exception {
        // Lọc lại danh sách
        List<OpcChildForm> childList = new ArrayList<>();
        if (form.getChilds() != null && form.getChilds().size() > 0) {
            for (OpcChildForm child : form.getChilds()) {
                childList.add(child);
            }
        }

        form.setChilds(childList);

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(arvID);

        List<SiteEntity> siteOpc = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        List<Long> siteIDs = new ArrayList<>();
        siteIDs.addAll(CollectionUtils.collect(siteOpc, TransformerUtils.invokerTransformer("getID")));
        if (arv == null || (!arv.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (isOpcManager() && !siteIDs.contains(arv.getSiteID()))) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }
        OpcVisitEntity entity = visitService.findOne(ID);
        if (form.getBaseTreatmentRegimenID() != null && form.getTreatmentRegimenID() != null && form.getBaseTreatmentRegimenID().equals(form.getTreatmentRegimenID())) {
            form.setRegimenDate(TextUtils.formatDate(entity.getRegimenDate(), FORMATDATE));
        }
        if (form.getBaseTreatmentRegimenStage() != null && form.getTreatmentRegimenStage() != null && form.getBaseTreatmentRegimenStage().equals(form.getTreatmentRegimenStage())) {
            form.setRegimenStageDate(TextUtils.formatDate(entity.getRegimenStageDate(), FORMATDATE));
        }

        HashMap<String, HashMap<String, String>> options = getOptions(true, arv, null, entity);
        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), true));
        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }
        validate.validate(form, bindingResult);

        //Trả lỗi
        if (bindingResult.hasErrors()) {

            form.setID(ID.toString());
            model.addAttribute("error", "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin");
            model.addAttribute("title", "Cập nhật lượt khám");
            model.addAttribute("smallTitle", "Cập nhật lượt khám");
            model.addAttribute("arv", arv);
            model.addAttribute("visit", entity);
            model.addAttribute("isOpcManager", isOpcManager());
            model.addAttribute("form", form);
            model.addAttribute("options", options);
            return "backend/opc_arv/visit_new";
        }

        OpcVisitEntity result = form.setToEntity(entity);
        visitService.save(result);

        if (form.getChilds() != null && form.getChilds().size() > 0) {
            List<OpcChildEntity> childs = new ArrayList<>();
            OpcChildEntity childEntity;
            for (OpcChildForm child : form.getChilds()) {
                if (StringUtils.isEmpty(child.getDob())) {
                    continue;
                }
                childEntity = new OpcChildEntity();
                child.setToEntity(childEntity);

                childEntity.setArvID(arv.getID());
                childEntity.setStageID(result.getStageID());
                childEntity.setVisitID(result.getID());
                childs.add(childEntity);
            }

            opcChildService.saveAll(arv.getID(), result.getStageID(), result.getID(), childs);
        } else {
            opcChildService.saveAll(arv.getID(), result.getStageID(), result.getID(), null);
        }

        visitService.logVisit(result.getArvID(), result.getPatientID(), result.getID(), "Cập nhật lượt khám");

        List<OpcVisitEntity> models = visitService.findByArvID(arv.getID(), false);
        setToStageAndArv(models, result, arv);
        redirectAttributes.addFlashAttribute("success", "Cập nhật lượt khám thành công");
        if ("yes".equals(form.getIsPrinted())) {
            return redirect(String.format("%s&id=%s&page_redirect=yes", UrlUtils.opcVisit(arv.getID(), "all"), result.getID()));
        } else {
            return redirect(UrlUtils.opcVisit(arv.getID(), "all"));
        }
    }

    /**
     * Xem thông tin lượt khám
     *
     * @author DSNAnh
     * @param model
     * @param form
     * @param arvID
     * @param ID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-visit/view-visit.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            OpcVisitForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "visitid") Long ID,
            @RequestParam(name = "stageid", required = false) Long stageID,
            RedirectAttributes redirectAttributes) {
        OpcArvEntity arv = arvService.findOne(arvID);
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);

        OpcStageEntity stage = new OpcStageEntity();
        OpcVisitEntity visitEntity = visitService.findOne(ID);
        if (visitEntity.getStageID() != null) {
            stage = stageService.findOne(visitEntity.getStageID());
        }
        options.put("treatment-stage", getTreatmentStage(arv.getPatientID(), arv.getSiteID(), false));
        LoggedUser currentUser = getCurrentUser();
        List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
        List<Long> siteIDs = new ArrayList<>();
        siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));
        if (arv == null || (!arv.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                || (isOpcManager() && !siteIDs.contains(arv.getSiteID()))) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcIndex());
        }
        form = new OpcVisitForm(visitEntity);
        //set thông tin từ đợt điều trị
        if (stage != null) {
            form.setStageID(stage.getID().toString());
            form.setBaseTreatmentRegimenID(stage.getTreatmentRegimenID());
            form.setBaseTreatmentRegimenStage(stage.getTreatmentRegimenStage());
            form.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
            form.setRegistrationTime(TextUtils.formatDate(stage.getRegistrationTime(), "dd/MM/yyyy"));
            form.setTreatmentTime(TextUtils.formatDate(stage.getTreatmentTime(), "dd/MM/yyyy"));
        }
        List<OpcChildEntity> childList = opcChildService.findByArvIDAndStageIDAndVisitID(arv.getID(), visitEntity.getStageID(), visitEntity.getID());
        List<OpcChildForm> childs = new ArrayList<>();
        if (childList != null && childList.size() > 0) {
            for (OpcChildEntity entity : childList) {
                childs.add(new OpcChildForm(entity));
            }
        }
        form.setChilds(childs);
        model.addAttribute("title", "Xem lượt khám");
        model.addAttribute("smallTitle", "Xem lượt khám");
        model.addAttribute("form", form);
        model.addAttribute("arv", arv);
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("visit", visitEntity);
        model.addAttribute("options", options);

        return "backend/opc_arv/visit_view";
    }

    /**
     * Xoá vĩnh viễn khỏi hệ thống
     *
     * @param redirectAttributes
     * @auth vvThành
     * @param arvID
     * @param ID
     * @return
     */
    @RequestMapping(value = "/opc-visit/delete.html", method = RequestMethod.GET)
    public String actionDelete(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            HashMap<String, HashMap<String, String>> options = getOptions(true, arv);
            if (isOpcManager()) {
                if (arv == null || !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(arv.getSiteID().toString())) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                    return redirect(UrlUtils.opcIndex());
                }
            } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }
            OpcVisitEntity model = visitService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lượt khám");
            }
            if ((!model.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                    || (isOpcManager() && !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(model.getSiteID().toString()))) {
                throw new Exception("Bạn không có quyền xoá lượt khám này");
            }
            visitService.del(ID);
            redirectAttributes.addFlashAttribute("success", "Lượt khám được xoá vĩnh viễn khỏi hệ thống");
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        }
    }

    /**
     * Xoá tạm thời khỏi hệ thống
     *
     * @param arvID
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-visit/remove.html", method = RequestMethod.GET)
    public String actionRemove(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            HashMap<String, HashMap<String, String>> options = getOptions(true, arv);
            if (isOpcManager()) {
                if (arv == null || !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(arv.getSiteID().toString())) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                    return redirect(UrlUtils.opcIndex());
                }
            } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }
            OpcVisitEntity model = visitService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lượt khám");
            }
            if ((!model.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                    || (isOpcManager() && !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(model.getSiteID().toString()))) {
                throw new Exception("Bạn không có quyền xoá lượt khám này");
            }
            model.setRemove(true);
            model.setRemoteAT(new Date());
            visitService.save(model);
            visitService.logVisit(model.getArvID(), model.getPatientID(), model.getID(), "Xoá lượt khám");
            redirectAttributes.addFlashAttribute("success", "Xóa lượt khám thành công");
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        }
    }

    /**
     * Khôi phục lượt tái khám
     *
     * @param arvID
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-visit/restore.html", method = RequestMethod.GET)
    public String actionRestore(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            HashMap<String, HashMap<String, String>> options = getOptions(true, arv);
            if (isOpcManager()) {
                if (arv == null || !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(arv.getSiteID().toString())) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                    return redirect(UrlUtils.opcIndex());
                }
            } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }
            OpcVisitEntity model = visitService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lượt khám");
            }
            if ((!model.getSiteID().equals(currentUser.getSite().getID()) && isOPC() && !isOpcManager())
                    || (isOpcManager() && !options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(model.getSiteID().toString()))) {
                throw new Exception("Bạn không có quyền khôi phục lượt khám này");
            }
            model.setRemove(false);
            model.setRemoteAT(null);
            visitService.save(model);
            visitService.logVisit(model.getArvID(), model.getPatientID(), model.getID(), "Lượt khám được khôi phục");
            redirectAttributes.addFlashAttribute("success", "Lượt khám đã được khôi phục");
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcVisit(arvID, "all"));
        }

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

    //subtract date vs current
    public static int subtractDate(Date date) {

        if (date == null) {
            return 0;
        }
        Date currentDate = new Date();
        long diff = currentDate.getTime() - date.getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        return diffDays;
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
    @RequestMapping(value = {"opc-visit/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcVisitIndex());
        }
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);
        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcVisitIndex());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        if (!(currentUser.getSite().getID().equals(entity.getSiteID()) || (options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcVisitIndex());
        }

        model.addAttribute("urlBack", UrlUtils.opcVisitIndex());
        model.addAttribute("urlCurrentPage", UrlUtils.opcVisitView(ID));
        model.addAttribute("urlBreadcrumb", UrlUtils.opcVisitIndex());
        model.addAttribute("breadcrumbTitle", "Quản lý tái khám");
        model.addAttribute("title", "Xem thông tin bệnh án");

        model.addAttribute("entity", entity);
        model.addAttribute("options", options);
        return "backend/opc_arv/view";
    }

    public void setToStageAndArv(List<OpcVisitEntity> models, OpcVisitEntity model, OpcArvEntity arv) throws Exception {
        if (models != null && models.size() > 0) {
            OpcVisitEntity lastVisit = models.get(0);
            List<OpcStageEntity> stages = stageService.findByArvID(arv.getID(), false);
            OpcStageEntity stageEntity = stageService.findOne(model.getStageID());
            stageEntity.setTreatmentRegimenStage(lastVisit.getTreatmentRegimenStage());
            stageEntity.setTreatmentRegimenID(lastVisit.getTreatmentRegimenID());
            stageEntity.setLastExaminationTime(lastVisit.getExaminationTime());
            stageEntity.setMedicationAdherence(lastVisit.getMedicationAdherence());
            stageEntity.setDaysReceived(lastVisit.getDaysReceived());
            stageEntity.setAppointmentTime(lastVisit.getAppointmentTime());

            stageEntity.setSupplyMedicinceDate(lastVisit.getSupplyMedicinceDate());
            stageEntity.setReceivedWardDate(lastVisit.getReceivedWardDate());
            stageEntity.setObjectGroupID(lastVisit.getObjectGroupID());
            stageEntity.setPregnantStartDate(lastVisit.getPregnantStartDate());
            stageEntity.setPregnantEndDate(lastVisit.getPregnantEndDate());
            stageEntity.setBirthPlanDate(lastVisit.getBirthPlanDate());
            stageEntity.setRegimenStageDate(lastVisit.getRegimenStageDate());
            stageEntity.setRegimenDate(lastVisit.getRegimenDate());
            stageEntity.setOldTreatmentRegimenStage(lastVisit.getOldTreatmentRegimenStage());
            stageEntity.setOldTreatmentRegimenID(lastVisit.getOldTreatmentRegimenID());
            stageEntity.setCausesChange(lastVisit.getCausesChange());
            stageEntity.setExaminationNote(lastVisit.getNote());
            stageEntity.setUpdateAt(new Date());
            stageEntity.setUpdatedBY(getCurrentUser().getUser().getID());
            stageService.save(stageEntity);
            stageService.logStage(arv.getID(), arv.getPatientID(), stageEntity.getID(), "Cập nhật giai đoạn điều trị");
            if (stages != null && stages.size() > 0) {
                if (Objects.equals(stages.get(stages.size() - 1).getID(), stageEntity.getID())) {
                    if (StringUtils.isNotEmpty(lastVisit.getInsuranceNo())) {
                        arv.setInsurance(lastVisit.getInsurance());
                        arv.setInsuranceNo(lastVisit.getInsuranceNo());
                        arv.setInsuranceType(lastVisit.getInsuranceType());
                        arv.setInsuranceSite(lastVisit.getInsuranceSite());
                        arv.setInsuranceExpiry(lastVisit.getInsuranceExpiry());
                        arv.setInsurancePay(lastVisit.getInsurancePay());
                        arv.setRouteID(lastVisit.getRouteID());
                    }
                    arv.setTreatmentRegimenStage(lastVisit.getTreatmentRegimenStage());
                    arv.setTreatmentRegimenID(lastVisit.getTreatmentRegimenID());
                    arv.setLastExaminationTime(lastVisit.getExaminationTime());
                    arv.setMedicationAdherence(lastVisit.getMedicationAdherence());
                    arv.setDaysReceived(lastVisit.getDaysReceived());
                    arv.setAppointmentTime(lastVisit.getAppointmentTime());
                    arv.setSupplyMedicinceDate(lastVisit.getSupplyMedicinceDate());
                    arv.setReceivedWardDate(lastVisit.getReceivedWardDate());
                    arv.setObjectGroupID(lastVisit.getObjectGroupID());
                    arv.setPregnantStartDate(lastVisit.getPregnantStartDate());
                    arv.setPregnantEndDate(lastVisit.getPregnantEndDate());
                    arv.setJoinBornDate(lastVisit.getBirthPlanDate());
                    if (lastVisit.getPatientWeight() != 0.0) {
                        arv.setPatientWeight(lastVisit.getPatientWeight());
                    }
                    if (lastVisit.getPatientHeight() != 0.0) {
                        arv.setPatientHeight(lastVisit.getPatientHeight());
                    }

                    arv.setExaminationNote(lastVisit.getNote());
                    arv.setUpdateAt(new Date());
                    arv.setUpdatedBY(getCurrentUser().getUser().getID());
                    arvService.update(arv);
                    arvService.logArv(arv.getID(), arv.getPatientID(), "Cập nhật bệnh án");
                }
            }
        }
    }

    //Lấy danh sách các giai đoạn điều trị của bệnh án
    public HashMap<String, String> getTreatmentStage(Long patientID, Long siteID, boolean isModified) {
        HashMap<String, String> treatmentStage = new HashMap<>();
        treatmentStage.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> stageList;
        if (siteID != null) {
            stageList = stageService.findByPatientID(patientID, false, siteID);
        } else {
            stageList = stageService.findByPatientID(patientID, false);
        }
        if (stageList != null) {
            for (OpcStageEntity entity : stageList) {
                String treatmentRegimenStage = StringUtils.isEmpty(entity.getTreatmentRegimenStage()) ? "0" : entity.getTreatmentRegimenStage();
                String treatmentRegimenID = StringUtils.isEmpty(entity.getTreatmentRegimenID()) ? "0" : entity.getTreatmentRegimenID();
                String statusOfTreatmentID = StringUtils.isEmpty(entity.getStatusOfTreatmentID()) ? "99" : entity.getStatusOfTreatmentID();
                String startTime = entity.getTreatmentTime() == null ? TextUtils.formatDate(entity.getRegistrationTime(), FORMATDATE) : TextUtils.formatDate(entity.getTreatmentTime(), FORMATDATE);
                String endTime = entity.getEndTime() == null ? "hiện tại" : TextUtils.formatDate(entity.getEndTime(), FORMATDATE);
                String value = "Giai đoạn: " + startTime + " - " + endTime;
                String modified = isModified ? String.format("-%s-%s-%s", treatmentRegimenStage, treatmentRegimenID, statusOfTreatmentID) : "";
                treatmentStage.put(entity.getID().toString() + modified, value);
            }
        }
        return treatmentStage;
    }
}
