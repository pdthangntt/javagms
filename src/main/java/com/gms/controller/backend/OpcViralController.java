package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcViralLoadForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author trangBN
 */
@Controller(value = "backend_opc_viral")
public class OpcViralController extends OpcController {

    @Autowired
    private OpcViralLoadService viralService;
    @Autowired
    private OpcArvService arvService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;

    /**
     * Quản lý tải lượng virut
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
     * @param viralLoadRetryFrom
     * @param viralLoadRetryTo
     * @param resultID
     * @param treatmentStageTimeFrom
     * @param treatmentStageTimeTo
     * @param treatmentTimeFrom
     * @param treatmentTimeTo
     * @param statusOfTreatmentID
     * @param treatmentStageID
     * @param therapySiteID
     * @param page
     * @param size
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-viralload/index.html"}, method = RequestMethod.GET)
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
            @RequestParam(name = "viral_load_time_from", required = false) String viralLoadTimeFrom,
            @RequestParam(name = "viral_load_time_to", required = false) String viralLoadTimeTo,
            @RequestParam(name = "viral_load_retry_from", required = false) String viralLoadRetryFrom,
            @RequestParam(name = "viral_load_retry_to", required = false) String viralLoadRetryTo,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
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
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);

        search.setRemove(false);
        search.setSiteID(siteIDs);
        search.setInsurance(insurance);
        search.setInsuranceType(insuranceType);
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
        search.setRegistrationTimeFrom(isThisDateValid(registrationTimeFrom) ? registrationTimeFrom : null);
        search.setRegistrationTimeTo(isThisDateValid(registrationTimeTo) ? registrationTimeTo : null);
        search.setViralTimeForm(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);
        search.setViralTimeTo(isThisDateValid(viralLoadTimeTo) ? viralLoadTimeTo : null);
        search.setViralRetryForm(isThisDateValid(viralLoadRetryFrom) ? viralLoadRetryFrom : null);
        search.setViralRetryTo(isThisDateValid(viralLoadRetryTo) ? viralLoadRetryTo : null);
        search.setResultID(resultID);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setTab("all".equals(tab) ? "0" : "1");
        HashMap<String, String> countTimeVirals = new HashMap<>();
        List<Long> IDs = new ArrayList<>();

        DataPage<OpcArvEntity> dataPage = opcArvService.findViral(search);
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            IDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getID")));
        }
        for (Long ID : IDs) {
            countTimeVirals.put(String.valueOf(ID), String.valueOf(viralService.countByArvID(ID)));
        }
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("options", options);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("viral", "viral");
        model.addAttribute("countTimeVirals", countTimeVirals);
        model.addAttribute("tabindex", tab);

        model.addAttribute("title", "Danh sách xét nghiệm tải lượng virus");
        model.addAttribute("parent_title", "Điều trị ngoại trú");

        return "backend/opc_arv/viral_index";
    }

    /**
     * Xem thông tin bệnh án
     *
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"opc-viralload/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcViralIndex());
        }
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);
        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcViralIndex());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        if (!(currentUser.getSite().getID().equals(entity.getSiteID()) || (options.get(ParameterEntity.TREATMENT_FACILITY).containsKey(entity.getSiteID().toString()) && isOpcManager()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", ID));
            return redirect(UrlUtils.opcViralIndex());
        }

        model.addAttribute("urlBack", UrlUtils.opcViralIndex());
        model.addAttribute("urlCurrentPage", UrlUtils.opcViralView(ID));
        model.addAttribute("urlBreadcrumb", UrlUtils.opcViralIndex());
        model.addAttribute("breadcrumbTitle", "Quản lý TLVR");
        model.addAttribute("title", "Xem thông tin bệnh án");

        model.addAttribute("entity", entity);
        model.addAttribute("options", options);
        return "backend/opc_arv/view";
    }

    /**
     * Cập nhật thông tin tải lượng virus
     *
     * @param model
     * @param arvID
     * @param tab
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-viralload/update.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "tab", defaultValue = "all", required = false) String tab,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }
        OpcArvEntity arv = opcArvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arv == null || !siteIDs.contains(arv.getSiteID())) {
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

        List<OpcViralLoadEntity> models = viralService.findByPatientID(arv.getPatientID(), StringUtils.isNotEmpty(tab) && !tab.equals("all"));
        OpcViralLoadForm form = new OpcViralLoadForm();
        form.setOpcManager(isOpcManager());
        form.setOPC(isOPC());
        form.setConfirmTime(TextUtils.formatDate(arv.getPatient().getConfirmTime(), FORMATDATE));

        HashMap<String, String> stages = new HashMap<>();
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        OpcStageEntity item = new OpcStageEntity();
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity entity : entitys) {
                stages.put(String.valueOf(entity.getID()), entity.getName());
            }
            item = opcStageService.findByPatientIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        }
        HashMap<String, String> stagePatients = new HashMap<>();
        List<OpcStageEntity> stagePatient = opcStageService.findByPatientID(arv.getPatientID());
        if (stagePatient != null && !stagePatient.isEmpty()) {
            for (OpcStageEntity stage : stagePatient) {
                stagePatients.put(String.valueOf(stage.getID()), stage.getName());
            }
        }

        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("options", getOptions(false, null));
        model.addAttribute("siteID", currentUser.getSite().getID());
        model.addAttribute("form", form);
        model.addAttribute("stages", stages);
        model.addAttribute("stagePatients", stagePatients);
        model.addAttribute("stagesLast", item.getID());
        model.addAttribute("models", models);
        model.addAttribute("tab", tab);
        model.addAttribute("arv", arv);
        return "backend/opc_arv/viral";
    }

    /**
     * Xoá vĩnh viễn khỏi hệ thống
     *
     * @param redirectAttributes
     * @param arvID
     * @param ID
     * @return
     */
    @RequestMapping(value = "/opc-viralload/delete.html", method = RequestMethod.GET)
    public String actionDelete(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            if (isOpcManager()) {
                List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentUser.getSite().getProvinceID());
                List<Long> siteIDs = new ArrayList<>();
                siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

                if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                    return redirect(UrlUtils.opcIndex());
                }
            } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }

            OpcViralLoadEntity model = viralService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thông tin tải lượng virus");
            }
            if (!model.getSiteID().equals(currentUser.getSite().getID())) {
                throw new Exception("Bạn không có quyền xoá lượt khám này");
            }
            viralService.delViralLoad(ID);
            redirectAttributes.addFlashAttribute("success", "Bản ghi này được xóa khỏi cơ sở dữ liệu của hệ thống");
            return redirect(UrlUtils.opcViral(arvID, "deleted"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcViral(arvID, "deleted"));
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
    @RequestMapping(value = "/opc-viralload/remove.html", method = RequestMethod.GET)
    public String actionRemove(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {

        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arv = arvService.findOne(arvID);
            if (isOpcManager()) {
                List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
                List<Long> siteIDs = new ArrayList<>();
                siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

                if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                    return redirect(UrlUtils.opcIndex());
                }
            } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return redirect(UrlUtils.opcIndex());
            }

            OpcViralLoadEntity model = viralService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thông tin tải lượng virus");
            }

            if (!model.getSiteID().equals(currentUser.getSite().getID()) && !isOpcManager()) {
                throw new Exception("Bạn không có quyền xóa lượt xét nghiệm này");
            }

            model.setRemove(true);
            model.setRemoteAT(new Date());
            OpcViralLoadEntity viral = viralService.save(model);
            viralService.logViralLoad(arv.getID(), arv.getPatientID(), viral.getID(), "Xóa thông tin tải lượng virus");
            redirectAttributes.addFlashAttribute("success", "Xóa lượt xét nghiệm tải lượng virus thành công");
            return redirect(UrlUtils.opcViral(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcViral(arvID, "all"));
        }

    }

    /**
     * Khôi phục
     *
     * @param arvID
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-viralload/restore.html", method = RequestMethod.GET)
    public String actionRestore(@RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            OpcArvEntity arvEntity = arvService.findOne(arvID);
            if (isOpcManager()) {
                List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
                List<Long> siteIDs = new ArrayList<>();
                siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

                if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                    throw new Exception("Không tìm thấy bệnh án");
                }
            } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
                throw new Exception("Không tìm thấy bệnh án");
            }

            OpcViralLoadEntity model = viralService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arvEntity.getPatientID())) {
                throw new Exception("Không tìm thấy thông tin tải lượng virus");
            }

            if (!model.getSiteID().equals(currentUser.getSite().getID()) && !isOpcManager()) {
                throw new Exception("Bạn không có quyền khôi phục lượt xét nghiệm này");
            }

            model.setRemove(false);
            model.setRemoteAT(null);
            viralService.save(model);
            viralService.logViralLoad(model.getArvID(), model.getPatientID(), model.getID(), "Khôi phục tải lượng virus");
            redirectAttributes.addFlashAttribute("success", "Thông tin tải lượng virus đã được khôi phục");
            return redirect(UrlUtils.opcViral(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcViral(arvID, "all"));
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
}
