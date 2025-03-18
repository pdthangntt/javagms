package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcArvRevisionEntity;
import com.gms.entity.db.OpcChildEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcChildForm;
import com.gms.entity.form.opc_arv.OpcStageForm;
import com.gms.entity.validate.OpcStageValidate;
import com.gms.repository.OpcArvRevisionRepository;
import com.gms.service.OpcArvService;
import com.gms.service.OpcChildService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.OpcVisitService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * OPC quản lý thông tin điều trị
 *
 * @author TrangBN
 */
@Controller(value = "backend_opc_stage")
public class OpcStageController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcTestService opcTestService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OpcStageValidate opcStageValidate;
    @Autowired
    private OpcChildService opcChildService;
    @Autowired
    private OpcArvRevisionRepository revisionRepository;

    /**
     * Màn hình thêm mới đợt điều trị
     *
     * @param model
     * @param form
     * @param arvID
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"opc-stage/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model, OpcStageForm form,
            @RequestParam(name = "arvid", required = true) Long arvID,
            RedirectAttributes redirectAttributes) throws ParseException {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = opcArvService.findOne(arvID);

        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arv == null || !siteIDs.contains(arv.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
                return "redirect:update.html?arvid=" + arvID;
            }
        } else if (arv == null || !arv.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return "redirect:update.html?arvid=" + arvID;
        }

        form.setSiteID(isOPC() && !isOpcManager() ? currentUser.getSite().getID().toString() : arv.getSiteID().toString());
        form.setOPC(isOPC() && !isOpcManager());
        form.setOpcManager(!isOPC() && isOpcManager());
        form.setArvID(arvID);
        form.setObjectGroupID(arv.getObjectGroupID());
        form.setFirstTreatmentTime(TextUtils.formatDate(arv.getFirstTreatmentTime(), FORMATDATE));

        if (StringUtils.isNotEmpty(form.getObjectGroupID()) && (form.getObjectGroupID().equals(TestObjectGroupEnum.PREGNANT_WOMEN.getKey())
                || form.getObjectGroupID().equals(TestObjectGroupEnum.PREGNANT_PERIOD.getKey())
                || form.getObjectGroupID().equals(TestObjectGroupEnum.GIVING_BIRTH.getKey()))) {
            form.setPregnantStartDate(TextUtils.formatDate(arv.getPregnantStartDate(), FORMATDATE));
            form.setPregnantEndDate(TextUtils.formatDate(arv.getPregnantEndDate(), FORMATDATE));
            form.setBirthPlanDate(TextUtils.formatDate(arv.getJoinBornDate(), FORMATDATE));
        }

        HashMap<String, HashMap<String, String>> options = getOptions(true, arv);
        model.addAttribute("arv", arv);
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        model.addAttribute("title", "Thêm mới giai đoạn điều trị");
        return "backend/opc_arv/stage_new";
    }

    /**
     * Cập nhật thông tin điều trị
     *
     * @param model
     * @param form
     * @param arvID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-stage/stage-update.html", method = RequestMethod.GET)
    public String actionUpdate(Model model, OpcStageForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại " + arvID);
                return redirect(UrlUtils.opcStage(arvID.toString()));
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại " + arvID);
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        OpcStageEntity stage = opcStageService.findOne(stageID);
        if (model == null || !stage.getPatientID().equals(arvEntity.getPatientID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin điều trị");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        // Tự động fill các trường từ thông tin bệnh án
        form = new OpcStageForm();
        form.setArvID(arvID);
        form.setFromStage(stage);
        form.setOPC(isOPC() && !isOpcManager());
        form.setOpcManager(!isOPC() && isOpcManager());
        form.setBaseTreatmentRegimenID(stage.getTreatmentRegimenID());
        form.setBaseTreatmentRegimenStage(stage.getTreatmentRegimenStage());

        List<OpcChildEntity> childList = opcChildService.findByArvIDAndStageID(arvID, stageID);
        List<OpcChildForm> childs = new ArrayList<>();
        if (childList != null && childList.size() > 0) {
            for (OpcChildEntity entity : childList) {
                childs.add(new OpcChildForm(entity));
            }
            form.setChildren(childs);
        }

        HashMap<String, HashMap<String, String>> options = getOptions(true, arvEntity, stage, null);
        model.addAttribute("arv", arvEntity);
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        model.addAttribute("stage", stage);
        model.addAttribute("title", stage.getName());
        return "backend/opc_arv/stage_new";

    }

    /**
     * Xem thông tin bệnh án
     *
     * @param model
     * @param form
     * @param arvID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-stage/stage-view.html", method = RequestMethod.GET)
    public String actionView(Model model, OpcStageForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại " + arvID);
                return redirect(UrlUtils.opcStage(arvID.toString()));
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại " + arvID);
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        OpcStageEntity stage = opcStageService.findOne(stageID);
        if (model == null || !stage.getPatientID().equals(arvEntity.getPatientID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin điều trị");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        // Tự động fill các trường từ thông tin bệnh án
        form = new OpcStageForm();
        form.setFromStage(stage);
        List<OpcChildEntity> childList = opcChildService.findByArvIDAndStageID(arvID, stageID);
        List<OpcChildForm> childs = new ArrayList<>();
        if (childList != null && childList.size() > 0) {
            for (OpcChildEntity entity : childList) {
                childs.add(new OpcChildForm(entity));
            }
            form.setChildren(childs);
        }

        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("arv", arvEntity);
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        model.addAttribute("stage", stage);
        model.addAttribute("title", "Giai đoạn điều trị bệnh án");
        return "backend/opc_arv/stage_view";

    }

    /**
     * Thêm thông tin giai đoạn điều trị
     *
     * @param model
     * @param form
     * @param arvID
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/opc-stage/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") OpcStageForm form,
            @RequestParam(name = "arvid") Long arvID,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws ParseException {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(form.getArvID());

        HashMap<String, HashMap<String, String>> options = getOptions(true, arvEntity);
        model.addAttribute("arv", arvEntity);
        model.addAttribute("options", options);
        model.addAttribute("title", "Thêm mới giai đoạn điều trị");

        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                model.addAttribute("error", "Thông tin bệnh án không tồn tại");
                return "backend/opc_arv/stage_new";
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            model.addAttribute("error", "Thông tin bệnh án không tồn tại");
            return "backend/opc_arv/stage_new";
        }

        // validate các trường nhập
        form.setFromPatientEntity(arvEntity.getPatient());
        form.setCurrentSiteID(currentUser.getSite().getID());
        form.setFirstTreatmentTime(TextUtils.formatDate(arvEntity.getFirstTreatmentTime(), FORMATDATE));
        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }
        opcStageValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/opc_arv/stage_new";
        }

        if (isOpcManager() && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
            model.addAttribute("error", "Khoa điều trị không được thực hiện chuyển gửi bệnh nhân");
            return "backend/opc_arv/stage_new";
        }
        
        OpcStageEntity stageEntity = form.convertStageForm(null);
        
        stageEntity.setPatientID(arvEntity.getPatientID());
        stageEntity.setArvID(form.getArvID());

        // Validate khoảng thời gian điều trị
        if (!opcStageService.validateStage(stageEntity)) {
            model.addAttribute("error", "Thời gian nhập của giai đoạn điều trị không hợp lệ hoặc ngày kết thúc của đợt điều trị nhập trong quá khứ đang trống");
            return "backend/opc_arv/stage_new";
        }

        if (isOpcManager()) {
            stageEntity.setSiteID(arvEntity.getSiteID());
        } else {
            stageEntity.setSiteID(currentUser.getSite().getID());
        }

        try {
            // Set thông tin chuyển gửi nếu đủ điều kiện
            if (StringUtils.isNotEmpty(stageEntity.getEndCase()) && stageEntity.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                stageEntity.setTranferFromTime(stageEntity.getEndTime());
            }

            stageEntity = opcStageService.save(stageEntity);

            // Lưu thông tin con nếu có
            if (form.getChildren() != null && !form.getChildren().isEmpty()) {
                List<OpcChildEntity> entities = new ArrayList<>();
                OpcChildEntity ent = new OpcChildEntity();
                for (OpcChildForm child : form.getChildren()) {
                    ent = new OpcChildEntity();
                    ent.setArvID(form.getArvID());
                    ent.setStageID(stageEntity.getID());
                    ent.setPatientID(stageEntity.getPatientID());
                    entities.add(child.setToEntity(ent));
                }

                if (!entities.isEmpty()) {
                    opcChildService.saveAllTreatment(arvID, ent.getStageID(), entities);
                }
            }

            // Gửi email
            if (stageEntity.getTransferSiteID() != null) {
                //Gửi email thông báo cơ sở mới thay đổi
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(stageEntity.getTransferSiteID());
                if (stageEntity.getTransferSiteID() != null && !stageEntity.getTransferSiteID().equals(-1l)) {
                    variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arvEntity.getCode());
                    variables.put("transferSiteName", transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("gender", options.get(ParameterEntity.GENDER).get(arvEntity.getPatient().getGenderID()));
                    variables.put("fullName", arvEntity.getPatient().getFullName());
                    variables.put("dob", TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
                    variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                    sendEmail(getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển tiếp điều trị bệnh nhân mã %s", arvEntity.getCode()), EmailoutboxEntity.ARV_TRANSFER_MAIL, variables);
                }
            }

            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), stageEntity.getID(), stageEntity.getID() == null ? "Thêm mới đợt điều trị" : "Cập nhật đợt điều trị");
        } catch (Exception ex) {
            model.addAttribute("error", "Lưu thông tin thất bại");
            return "backend/opc_arv/stage_new";
        }
        redirectAttributes.addFlashAttribute("success", "Đợt điều trị được thêm mới thành công");
        return redirect(UrlUtils.opcStage(arvID.toString()));

    }

    /**
     * Cập nhật giai đoạn điều trị
     *
     * @param model
     * @param arvID
     * @param stageID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/opc-stage/stage-update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            @ModelAttribute("form") @Valid OpcStageForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws ParseException {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arvEntity = opcArvService.findOne(arvID);

        if (isOpcManager()) {
            List<SiteEntity> siteConfirm = siteService.getSiteOpc(currentUser.getSite().getProvinceID());
            List<Long> siteIDs = new ArrayList<>();
            siteIDs.addAll(CollectionUtils.collect(siteConfirm, TransformerUtils.invokerTransformer("getID")));

            if (arvEntity == null || !siteIDs.contains(arvEntity.getSiteID())) {
                redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
                return redirect(UrlUtils.opcStage(arvID.toString()));
            }
        } else if (arvEntity == null || !arvEntity.getSiteID().equals(currentUser.getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", "Thông tin bệnh án không tồn tại");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        OpcStageEntity oldStage = opcStageService.findOne(stageID);
        OpcStageEntity stage = opcStageService.findOne(stageID);

        if (stage == null || !stage.getPatientID().equals(arvEntity.getPatientID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin điều trị");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        HashMap<String, HashMap<String, String>> options = getOptions(true, arvEntity, stage, null);
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("arv", arvEntity);
        model.addAttribute("options", options);
        model.addAttribute("title", "Cập nhật đợt điều trị bệnh án");

        // validate các trường nhập
        form.setConfirmTime(TextUtils.formatDate(arvEntity.getPatient().getConfirmTime(), FORMATDATE));
        form.setCurrentSiteID(currentUser.getSite().getID());
        form.setFirstTreatmentTime(TextUtils.formatDate(arvEntity.getFirstTreatmentTime(), FORMATDATE));

        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }
        
        opcStageValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/opc_arv/stage_new";
        }

        if (isOpcManager() && ArvEndCaseEnum.MOVED_AWAY.getKey().equals(form.getEndCase())) {
            redirectAttributes.addFlashAttribute("error", "Khoa điều trị không được thực hiện chuyển gửi bệnh nhân");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }

        stage = form.convertStageForm(stage);
        stage.setPatientID(arvEntity.getPatientID());
        stage.setArvID(arvID);

        // Validate khoảng thời gian điều trị
        if (!opcStageService.validateStage(stage)) {
            model.addAttribute("error", "Thời gian nhập của giai đoạn điều trị không hợp lệ hoặc ngày kết thúc của đợt điều trị nhập trong quá khứ đang trống");
            return "backend/opc_arv/stage_new";
        }

        try {
            // Thông tin thỏa mãn chuyển đi set lại
            if (StringUtils.isNotEmpty(stage.getEndCase()) && stage.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                stage.setTranferFromTime(stage.getEndTime());
            }

            stage = opcStageService.save(stage);
            // Lưu thông tin con nếu có
            List<OpcChildEntity> entities = new ArrayList<>();
            OpcChildEntity ent = new OpcChildEntity();
            if (form.getChildren() != null && !form.getChildren().isEmpty()) {
                for (OpcChildForm child : form.getChildren()) {
                    if (StringUtils.isEmpty(child.getDob())) {
                        continue;
                    }
                    ent = new OpcChildEntity();
                    ent.setArvID(form.getArvID());
                    ent.setStageID(stage.getID());
                    ent.setPatientID(stage.getPatientID());
                    entities.add(child.setToEntity(ent));
                }
            } else {
                opcChildService.saveAllTreatment(arvID, stageID, null);
            }

            opcChildService.saveAllTreatment(arvID, ent.getStageID(), entities);
            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), stage.getID(), stageID == null ? "Đợt điều trị được thêm mới thành công" : "Đợt điều trị được cập nhật thành công");

            // Gửi mail thông báo nếu có thay đổi cơ sở điều trị khi đã chuyển gửi
            if (stage.getTransferSiteID() != null
                    && oldStage.getTranferFromTime() != null
                    && oldStage.getTransferSiteID() != null
                    && oldStage.getEndCase() != null
                    && StringUtils.isNotEmpty(oldStage.getEndCase())
                    && oldStage.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())
                    && !stage.getTransferSiteID().equals(oldStage.getTransferSiteID())) {
                //Gửi email thông báo cơ sở mới thay đổi
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteService.findOne(stage.getTransferSiteID());
                if (stage.getTransferSiteID() != null && !stage.getTransferSiteID().equals(-1l)) {
                    variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arvEntity.getCode());
                    variables.put("transferSiteName", transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("gender", options.get(ParameterEntity.GENDER).get(arvEntity.getPatient().getGenderID()));
                    variables.put("fullName", arvEntity.getPatient().getFullName());
                    variables.put("dob", TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
                    variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                    sendEmail(getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển tiếp điều trị bệnh nhân mã %s", arvEntity.getCode()), EmailoutboxEntity.ARV_TRANSFER_MAIL, variables);
                }

                //Gửi email thông báo cơ sở cũ bị thay đổi
                if (oldStage.getTransferSiteID() != null && !oldStage.getTransferSiteID().equals(-1l)
                        && stage.getTransferSiteID() != null && !stage.getTransferSiteID().equals(-1l)) {
                    variables = new HashMap<>();
                    SiteEntity oldTransferSite = siteService.findOne(oldStage.getTransferSiteID());
                    variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã: " + arvEntity.getCode());
                    variables.put("transferSiteName", oldTransferSite.getName());
                    variables.put("changedTransferSiteName", transferSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("gender", options.get(ParameterEntity.GENDER).get(arvEntity.getPatient().getGenderID()));
                    variables.put("fullName", arvEntity.getPatient().getFullName());
                    variables.put("dob", TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
                    variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
                    sendEmail(getSiteEmail(oldTransferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Bệnh nhân #%s - %s đã thay đổi cơ sở điều trị sang cơ sở %s", arvEntity.getCode(), arvEntity.getPatient().getFullName(), transferSite.getName()), EmailoutboxEntity.ARV_TRANSFER_MAIL_CHANGED, variables);
                }
            }
            redirectAttributes.addFlashAttribute("success", "Đợt điều trị được cập nhật thành công");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Lưu thông tin thất bại.");
            return redirect(UrlUtils.opcStage(arvID.toString()));
        }
    }

    /**
     * Cập nhật trạng thái điều trị
     *
     * @param model
     * @param arvID
     * @param tab
     * @param action
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-stage/update.html"}, method = RequestMethod.GET)
    public String actionStage(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "tab", defaultValue = "", required = false) String tab,
            @RequestParam(name = "action", defaultValue = "", required = false) String action,
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

        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        List<OpcStageEntity> stageEntities = opcStageService.findByPatientID(arv.getPatientID(), (StringUtils.isNotEmpty(tab) && !tab.equals("all")));
        if (isOpcManager()) {
            options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        }

        // Tự fill thông tin các trường từ arv
        OpcStageForm form = new OpcStageForm();
        String siteIdDefault = arv.getSiteID().toString();

        form.setOPC(isOPC());
        form.setOpcManager(isOpcManager());

        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", options);
        model.addAttribute("form", form);
        model.addAttribute("arv", arv);
        model.addAttribute("siteID", getCurrentUser().getSite().getID());
        model.addAttribute("siteIdDefault", siteIdDefault);
        model.addAttribute("patient", arv.getPatient());
        model.addAttribute("tab", tab);
        model.addAttribute("table", stageEntities);
        return "backend/opc_arv/stage";
    }

    /**
     * Xóa đợt logic điều trị
     *
     * @param model
     * @param arvID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-stage/remove.html", method = RequestMethod.GET)
    public String actionStageRemove(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            RedirectAttributes redirectAttributes) {

        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (arvEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Thông tin bệnh án không tồn tại"));
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Lấy thông tin đợt điều trị
        OpcStageEntity stage = opcStageService.findOne(stageID);
        if (stage == null || !stage.getPatientID().equals(arvEntity.getPatientID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin đợt điều trị: " + stageID);
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Không xóa TH đang điều trị
        if ((stage.getEndTime() == null || StringUtils.isEmpty(stage.getEndCase()))
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không được xóa đợt điều trị chưa kết thúc");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Không xóa khi đang có thông tin lượt khám
        if (opcVisitService.existByStageID(stage.getID())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa lượt điều trị đang có thông tin lượt khám.");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }
        if (opcViralLoadService.existByStageID(stage.getID())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa đợt điều trị đang có lượt xét nghiệm tải lượng virus.");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }
        if (opcTestService.existByStageID(stage.getID())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa đợt điều trị đang có lượt xét nghiệm và dự phòng khác");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        try {
            stage.setRemove(true);
            List<OpcArvRevisionEntity> stages = opcArvService.findByArvIDAndStageAndSite(arvID, stageID, stageID);
            if (stages != null && !stages.isEmpty()) {
                OpcArvRevisionEntity stageRevision = stages.get(0);
                stageRevision.setID(null);
                stageRevision.setRemove(true);
                stageRevision.setCreateAT(new Date());
                revisionRepository.save(stageRevision);
            }

            OpcStageEntity saveStage = opcStageService.save(stage);
            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), saveStage.getID(), "Xóa đợt điều trị");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Xóa đợt điều trị thất bại");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        redirectAttributes.addFlashAttribute("success", "Xóa đợt điều trị thành công");
        return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
    }

    /**
     * Xóa đợt vĩnh viễn điều trị
     *
     * @param model
     * @param arvID
     * @param stageID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/opc-stage/delete.html", method = RequestMethod.GET)
    public String actionStageDelete(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            RedirectAttributes redirectAttributes) {

        if (isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", String.format("Khoa điều trị không được xóa vĩnh viễn"));
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        OpcArvEntity arvEntity = opcArvService.findOne(arvID);
        if (arvEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Thông tin bệnh án không tồn tại"));
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Lấy thông tin đợt điều trị
        OpcStageEntity stage = opcStageService.findOne(stageID);

        if (stage == null || !stage.getArvID().equals(arvID)) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin đợt điều trị: " + stageID);
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Không xóa TH đang điều trị
        if ((stage.getEndTime() == null || StringUtils.isEmpty(stage.getEndCase()))
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())
                && !stage.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không được xóa đợt điều trị chưa kết thúc");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        // Không xóa khi đang có thông tin lượt khám
        if (opcVisitService.existByStageID(stage.getID())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không thể xóa lượt điều trị đang có thông tin lượt khám");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "deleted"));
        }

        try {
            opcStageService.del(stage.getID());
            opcStageService.logStage(arvEntity.getID(), arvEntity.getPatientID(), stage.getID(), "Xóa đợt điều trị");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Xóa đợt điều trị thất bại");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
        }

        redirectAttributes.addFlashAttribute("success", "Xóa đợt điều trị thành công");
        return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
    }

    /**
     * Khôi phục đợt điều trị
     *
     * @param model
     * @param arvID
     * @param stageID
     * @param redirectAttributes
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = "/opc-stage/restore.html", method = RequestMethod.GET)
    public String actionStageRestore(Model model,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "stageid") Long stageID,
            RedirectAttributes redirectAttributes) throws ParseException {

        OpcArvEntity arvEntity = opcArvService.findOne(arvID);

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

        OpcStageEntity opcStageEntity = opcStageService.findOne(stageID);

        if (opcStageEntity == null || !opcStageEntity.getPatientID().equals(arv.getPatientID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin đợt điều trị: " + stageID);
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "deleted"));
        }

        if (!opcStageService.validateStage(opcStageEntity)) {
            redirectAttributes.addFlashAttribute("error", "Đã tồn tại đợt điều trị trùng thời gian, không thể khôi phục được");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "deleted"));
        }

        try {
            opcStageEntity.setRemove(false);

            List<OpcArvRevisionEntity> stages = opcArvService.findByArvIDAndStageAndSite(arvID, stageID, stageID);
            if (stages != null && !stages.isEmpty()) {
                OpcArvRevisionEntity stageRevision = stages.get(0);
                stageRevision.setID(null);
                stageRevision.setRemove(false);
                stageRevision.setCreateAT(new Date());
                revisionRepository.save(stageRevision);
            }

            opcStageService.save(opcStageEntity);
            opcStageService.logStage(arvID, arvEntity.getPatientID(), stageID, "Khôi phục đợt điều trị");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Khôi phục đợt điều trị thất bại");
            return redirect(UrlUtils.opcTabStage(arvID.toString(), "deleted"));
        }

        redirectAttributes.addFlashAttribute("success", "Khôi phục đợt điều trị thành công");
        return redirect(UrlUtils.opcTabStage(arvID.toString(), "all"));
    }

}
