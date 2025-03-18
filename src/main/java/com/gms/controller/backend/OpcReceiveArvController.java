package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcArvNewForm;
import com.gms.entity.input.OpcReceiveSearch;
import com.gms.entity.validate.OpcArvNewValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.OpcVisitService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
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
 * OPC quản lý thông tin bệnh nhân
 *
 * @author pdThang
 */
@Controller(value = "backend_opc_receive_arv")
public class OpcReceiveArvController extends OpcController {

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcArvNewValidate opcArvNewValidate;
    @Autowired
    private SiteService siteServices;
    @Autowired
    private StaffService staffServices;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcTestService opcTestService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;

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

    @RequestMapping(value = {"/opc-from-arv/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "all") String tab,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "confirm_time", required = false) String confirmTime,
            @RequestParam(name = "confirm_code", required = false) String confirmCode,
            @RequestParam(name = "tranfer_time", required = false) String tranferTime,
            @RequestParam(name = "source_site_id", required = false) String sourceSiteID,
            @RequestParam(name = "status", required = false, defaultValue = "-1") String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            RedirectAttributes redirectAttributes) {
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }

        LoggedUser currentUser = getCurrentUser();
        OpcReceiveSearch search = new OpcReceiveSearch();
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        if (StringUtils.isNotEmpty(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        search.setRemove("remove".equals(tab));
        search.setCode(code);
        search.setConfirmCode(confirmCode);
        search.setConfirmTime(isThisDateValid(confirmTime) ? confirmTime : null);
        search.setTranferTime(isThisDateValid(tranferTime) ? tranferTime : null);
        search.setStatus("not".equals(tab) ? "0" : "receiced".equals(tab) ? "1" : status);
        search.setCurrentSiteID(currentUser.getSite().getID());
        search.setSourceSiteID(StringUtils.isNotEmpty(sourceSiteID) ? Long.parseLong(sourceSiteID) : null);
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<OpcArvEntity> dataPage = opcArvService.findReveice(search);
        dataPage.setData(opcArvService.getPatients(dataPage.getData()));

        HashMap<String, String> statusOptions = new LinkedHashMap<>();
        statusOptions.put("-1", "Tất cả");
        statusOptions.put("0", "Chưa tiếp nhận");
        statusOptions.put("1", "Đã tiếp nhận");

        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("options", options);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("statusOptions", statusOptions);

        model.addAttribute("title", "Tiếp nhận từ cơ sở điều trị khác");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        return "backend/opc_arv/receive_index";
    }

    //Xóa bệnh nhân
    @RequestMapping(value = "/opc-from-arv/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {

        OpcArvEntity arv = opcArvService.findOne(ID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcReceiveIndex());
        }
        try {
            arv.setRemoteAT(new Date());
            arv.setRemoveTranfer(true);
            opcArvService.update(arv);
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Xoá bệnh án");
            redirectAttributes.addFlashAttribute("success", "Xóa bệnh án thành công.");
            return redirect(UrlUtils.opcReceiveIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Xóa bệnh án thất bại.");
            return redirect(UrlUtils.opcReceiveIndex());
        }
    }

    @RequestMapping(value = "/opc-from-arv/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        OpcArvEntity arv = opcArvService.findOne(ID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.opcReceiveIndex());
        }
        try {
            arv.setRemoveTranfer(false);
            opcArvService.update(arv);
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Khôi phục bệnh án");
            redirectAttributes.addFlashAttribute("success", "Khôi phục bệnh án đã xóa thành công");
            redirectAttributes.addFlashAttribute("success_id", arv.getID());
            return redirect(UrlUtils.opcReceiveIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Khôi phục thất bại");
            return redirect(UrlUtils.opcReceiveIndex());
        }
    }

    /**
     * Xem thông tin bệnh nhân chuyển gửi
     *
     * @param model
     * @param ID
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-from-arv/view.html"}, method = RequestMethod.GET)
    public String actionArvView(Model model,
            @RequestParam(name = "id") Long ID,
            @RequestParam(name = "tab", defaultValue = "all") String tab,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcReceiveIndex(tab));
        }
        //Tìm entity
        OpcArvEntity entity = opcArvService.findOne(ID);
        //Kiểm tra điều kiện
        if (entity == null || entity.isRemove() || entity.getPatient() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án %s", ID));
            return redirect(UrlUtils.opcReceiveIndex(tab));
        }

        // Kiểm tra bệnh nhân chuyển gửi đến có thuộc cơ sở nhận
        if (entity.getTransferSiteID() == null || !entity.getTransferSiteID().equals(currentUser.getSite().getID()) || entity.getTranferFromTime() == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy bệnh án có mã %s", entity.getCode()));
            return redirect(UrlUtils.opcReceiveIndex(tab));
        }

        model.addAttribute("urlBack", UrlUtils.opcReceiveIndex(tab));
        model.addAttribute("urlCurrentPage", UrlUtils.opcReceiveView(ID.toString(), tab));
        model.addAttribute("urlBreadcrumb", UrlUtils.opcReceiveIndex());
        model.addAttribute("breadcrumbTitle", "Tiếp nhận từ cơ sở điều trị khác");
        model.addAttribute("title", "Xem thông tin bệnh án");

        model.addAttribute("entity", entity);
        model.addAttribute("options", getOptions(false, null));
        return "backend/opc_arv/view";

    }

    /**
     * Thêm mói từ tiếp nhận
     *
     * @param model
     * @param form
     * @param htcID
     * @param opcID
     * @param printable
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/opc-from-arv/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model, OpcArvNewForm form,
            @RequestParam(name = "htc_id", defaultValue = "", required = false) String htcID,
            @RequestParam(name = "opc_id", defaultValue = "", required = false) String opcID,
            @RequestParam(name = "printable", defaultValue = "", required = false) String printable,
            RedirectAttributes redirectAttributes) throws ParseException {
        LoggedUser currentUser = getCurrentUser();
        String currentSiteID = currentUser.getSite().getID().toString();
        String currentDate = TextUtils.formatDate(new Date(), "dd/MM/yyyy");

        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }
        OpcArvEntity arvEntity = opcArvService.findOne(Long.parseLong(opcID));
        if (arvEntity == null || !currentSiteID.equals(arvEntity.getTransferSiteID().toString())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bản ghi cần tiếp nhận");
            return redirect(UrlUtils.opcReceiveIndex());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(true, arvEntity);
        List<OpcStageEntity> stages = opcStageService.findByPatientID(arvEntity.getPatientID(), false, arvEntity.getSiteID());
        List<OpcVisitEntity> visits = opcVisitService.findByPatientAsc(arvEntity.getPatientID(), false);
        List<OpcTestEntity> tests = opcTestService.findByPatientIDorderByCD4Time(arvEntity.getPatientID(), false, arvEntity.getSiteID());
        List<OpcViralLoadEntity> virusLoads = opcViralLoadService.findByPatientID(arvEntity.getPatientID(), false, arvEntity.getSiteID());

        OpcStageEntity firstStage = new OpcStageEntity();
        OpcVisitEntity firstVisit = new OpcVisitEntity();
        OpcTestEntity firstTest = new OpcTestEntity();
        OpcViralLoadEntity firstLoad = new OpcViralLoadEntity();
        if (stages != null && !stages.isEmpty()) {
            for (OpcStageEntity stage : stages) {
                if (stage.getTreatmentTime() == null) {
                    continue;
                }
                firstStage = stage;
                break;
            }
        }
        if (visits != null && !visits.isEmpty()) {
            firstVisit = visits.get(0);
        }
        if (tests != null && !tests.isEmpty()) {
            firstTest = tests.get(0);
        }
        if (virusLoads != null && !virusLoads.isEmpty()) {
            firstLoad = virusLoads.get(0);
        }

        form = new OpcArvNewForm(arvEntity);

        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        form.setTherapySiteID(currentUser.getSite().getID().toString());
        form.setRegistrationTime(currentDate);
        form.setRegistrationType("3");
        form.setSourceSiteID(arvEntity.getSiteID() == null ? "" : arvEntity.getSiteID().toString());
        form.setSourceCode(arvEntity.getCode());
        form.setSourceID(arvEntity.getID().toString());
        form.setSourceServiceID(ServiceEnum.OPC.getKey());
        form.setTranferToTime(TextUtils.formatDate(new Date(), FORMATDATE));
        form.setDateOfArrival(TextUtils.formatDate(arvEntity.getTranferFromTime(), FORMATDATE));
        form.setRegistrationStatus(arvEntity.getRegistrationStatus());
        form.setPatientWeight(arvEntity.getPatientWeight() == 0.0 ? "" : "" + arvEntity.getPatientWeight());
        form.setPatientHeight(arvEntity.getPatientHeight() == 0.0 ? "" : "" + (int) arvEntity.getPatientHeight());
        form.setLaoResult(arvEntity.getLaoResult());
        form.setLaoTreatment(arvEntity.isLaoTreatment() ? "1" : "0");
        form.setLaoStartTime(TextUtils.formatDate(arvEntity.getLaoStartTime(), FORMATDATE));
        form.setLaoEndTime(TextUtils.formatDate(arvEntity.getLaoEndTime(), FORMATDATE));
        form.setQualifiedTreatmentTime(TextUtils.formatDate(arvEntity.getQualifiedTreatmentTime(), FORMATDATE));
        form.setReceivedWard(arvEntity.getReceivedWard());
        form.setViralLoadResultNumber(arvEntity.getViralLoadResultNumber());
        //Phần I
        form.setTreatmentStageID("4");
        form.setEndTime("");
        form.setEndCase("");
        form.setTransferSiteID("");
        form.setTransferSiteName("");
        form.setTransferCase("");

        //cũ của anh NA
        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        form.setIsOtherSite(currentSiteID.equals(form.getSiteID()));
        form.setPageRedirect(printable);

        //set mở
//        form.setIsDisplayCopy(true);
        // Ngày ARV đầu tiên, Phác đồ đầu tiên
        if (firstStage.getID() != null) {
            if (StringUtils.isEmpty(form.getFirstTreatmentTime())) {
                form.setFirstTreatmentTime(TextUtils.formatDate(firstStage.getTreatmentTime(), FORMATDATE));
            }
            if (StringUtils.isEmpty(form.getFirstTreatmentRegimenID())) {
                form.setFirstTreatmentRegimenID(firstStage.getTreatmentRegimenID());
            }
        }

        // Ngày XN đầu tiên, Kết quả XN đầu tiên, Lý do XN đầu tiên
        if (firstTest.getID() != null) {
            if (arvEntity.getFirstCd4Time() == null) {
                form.setFirstCd4Time(TextUtils.formatDate(firstTest.getCd4TestTime(), FORMATDATE));
            }
            if (StringUtils.isEmpty(arvEntity.getFirstCd4Result())) {
                form.setFirstCd4Result(firstTest.getCd4Result());
            }
            if (arvEntity.getFirstCd4Causes() == null || arvEntity.getFirstCd4Causes().isEmpty()) {
                form.setFirstCd4Causes(firstTest.getCd4Causes());
            }
        } else {
            form.setFirstCd4Time(TextUtils.formatDate(arvEntity.getFirstCd4Time(), FORMATDATE));
            form.setFirstCd4Result(arvEntity.getFirstCd4Result());
            form.setFirstCd4Causes(arvEntity.getFirstCd4Causes());
        }

        // Ngày đầu tiên XN tải lương virus
        if (firstLoad.getID() != null) {
            if (arvEntity.getFirstViralLoadTime() == null) {
                form.setFirstViralLoadTime(TextUtils.formatDate(firstLoad.getTestTime(), FORMATDATE));
            }
            if (StringUtils.isEmpty(arvEntity.getFirstViralLoadResult())) {
                form.setFirstViralLoadResult(firstLoad.getResult());
            }
            if (arvEntity.getFirstViralLoadCauses() == null || arvEntity.getFirstViralLoadCauses().isEmpty()) {
                form.setFirstViralLoadCauses(firstLoad.getCauses());
            }
        } else {
            form.setFirstViralLoadTime(TextUtils.formatDate(arvEntity.getFirstViralLoadTime(), FORMATDATE));
            form.setFirstViralLoadResult(arvEntity.getFirstViralLoadResult());
            form.setFirstViralLoadCauses(arvEntity.getFirstViralLoadCauses());
        }

        model.addAttribute("title", "Thêm mới bệnh án");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("form", form);
        model.addAttribute("options", options);
        model.addAttribute("isARV", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());
        return "backend/opc_arv/receive_action";
    }

    /**
     * Submit form thêm mới
     *
     * @param model
     * @param opcID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-from-arv/new.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "opc_id", defaultValue = "", required = false) String opcID,
            @ModelAttribute("form") @Valid OpcArvNewForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.home());
        }
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        LoggedUser currentUser = getCurrentUser();
        String currentSiteID = currentUser.getSite().getID().toString();
        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        form.setRegistrationType("3");
        if (isOPC() && !isOpcManager()) {
            form.setTherapySiteID(currentSiteID);
        }
        model.addAttribute("title", "Thêm mới bệnh nhân");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("form", form);
        model.addAttribute("options", options);
        model.addAttribute("isARV", isOPC());
        model.addAttribute("isOpcManager", isOpcManager());

        OpcArvEntity arvEntity = opcArvService.findOne(Long.parseLong(opcID));
        if (arvEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Thêm mới bệnh án không thành công");
            return redirect(UrlUtils.opcReceiveIndex());
        }

        //Set lại giá trị cho những cần thiết sau khi Validate
        form.setTherapySiteID(currentUser.getSite().getID().toString());
        form.setRegistrationType(RegistrationTypeEnum.TRANSFER.getKey());
        form.setSourceSiteID(arvEntity.getSiteID() != null ? arvEntity.getSiteID().toString() : "");
        form.setSourceCode(arvEntity.getCode());

        //Set lại giá trị cho những trường dissible
        form.setFullName(arvEntity.getPatient().getFullName());
        form.setGenderID(arvEntity.getPatient().getGenderID());
        form.setDob(TextUtils.formatDate(arvEntity.getPatient().getDob(), FORMATDATE));
        form.setIdentityCard(arvEntity.getPatient().getIdentityCard());
        form.setRaceID(arvEntity.getPatient().getRaceID());
        form.setConfirmCode(arvEntity.getPatient().getConfirmCode());
        form.setConfirmTime(TextUtils.formatDate(arvEntity.getPatient().getConfirmTime(), FORMATDATE));
        form.setConfirmSiteID(arvEntity.getPatient().getConfirmSiteID() == null ? "" : arvEntity.getPatient().getConfirmSiteID().toString());
        form.setConfirmSiteName(arvEntity.getPatient().getConfirmSiteName());
        form.setSourceSiteName(arvEntity.getSourceArvSiteName());
        form.setTreatmentStageID(arvEntity.getRegistrationType());

        form.setCurrentSiteID(currentSiteID);
        form.setIsOpcManager(isOpcManager());
        form.setIsOtherSite(currentSiteID.equals(form.getSiteID()));
        form.setPatientID(arvEntity.getPatientID());

        if (form.getTreatmentRegimenStage() != null && form.getTreatmentRegimenStage().contains("string:")) {
            form.setTreatmentRegimenStage(form.getTreatmentRegimenStage().replace("string:", ""));
        }

        opcArvNewValidate.validate(form, bindingResult);
        form.setIsStageTimeError(bindingResult.getFieldError("treatmentStageTime") != null);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới bệnh án không thành công.");
            return "backend/opc_arv/receive_action";
        }

        try {
            OpcArvEntity arv = form.setToEntity(null);

            //set trường mặc định bản ghi mới
            arv.setSourceArvSiteID(currentUser.getSite().getID());
            arv.setSourceServiceID(ServiceEnum.OPC.getKey());
            arv.setSourceID(arvEntity.getID());
            arv.setSourceArvCode(arvEntity.getCode());
            arv.setSourceArvSiteID(arvEntity.getSiteID());
            arv.setSourceSiteID(arvEntity.getSiteID());
            arv.setRegistrationType(RegistrationTypeEnum.TRANSFER.getKey());
            arv.setPatientID(arvEntity.getPatientID());
            arv.setID(null);
//            arv.setTranferToTime(new Date());
            arv.setDateOfArrival(StringUtils.isEmpty(form.getDateOfArrival()) ? arvEntity.getTranferFromTime() : TextUtils.convertDate(form.getDateOfArrival(), FORMATDATE));
            if (StringUtils.isNotEmpty(arv.getEndCase()) && arv.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey())) {
                arv.setTranferFromTime(arv.getEndTime());
            }
            OpcArvEntity cloneEntity = arvEntity.clone();

            // Cập nhật lại bản ghi cũ
            cloneEntity.setTranferToTimeOpc(arv.getTranferToTime());

            // Cập nhật mã cơ sở, mã bệnh án, tên cơ sở nguồn gửi đến
            arv.setSiteID(isOpcManager() ? arv.getSiteID() : currentUser.getSite().getID());
            
            arv = opcArvService.create(arv);//tạo bản ghi ở cơ sở mới
            opcArvService.logArv(arv.getID(), arv.getPatientID(), "Tự động tạo bệnh án khi tiếp nhận từ OPC");
            //Mail
            if (arv.getTransferSiteID() != null && !arv.getTransferSiteID().equals(0) && (arv.getEndCase() != null && arv.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey()))) {
                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity transferSite = siteServices.findOne(arv.getTransferSiteID());
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

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "printable".equals(form.getPageRedirect())) {
                // Đồng ý phản hổi tiếp nhận
                arv.setFeedbackResultsReceivedTime(StringUtils.isEmpty(form.getFeedbackResultsReceivedTime()) ? new Date() : TextUtils.convertDate(form.getFeedbackResultsReceivedTime(), FORMATDATE));
                cloneEntity.setFeedbackResultsReceivedTimeOpc(arv.getFeedbackResultsReceivedTime());
                opcArvService.update(cloneEntity);//cập nhật bản ghi cũ

                opcArvService.logArv(arv.getID(), cloneEntity.getPatientID(), "Gửi phản hồi tiếp nhận bệnh nhân được chuyển gửi từ " + options.get("siteOpcTo").get(cloneEntity.getSiteID() != null ? cloneEntity.getSiteID().toString() : ""));
                redirectAttributes.addFlashAttribute("success", "Bệnh nhân chuyển gửi điều trị được tiếp nhận thành công");

                //Gửi email thông báo
                Map<String, Object> variables = new HashMap<>();
                SiteEntity sourceSite = siteServices.findOne(arv.getSourceArvSiteID());
                if (sourceSite != null) {
                    variables.put("title", "Phản hồi tiếp nhận điều trị thành công");
                    variables.put("transferSiteName", sourceSite == null ? "" : sourceSite.getName());
                    variables.put("currentSiteName", getCurrentUser().getSite().getName());
                    variables.put("fullName", arv.getPatient().getFullName());
                    variables.put("code", arv.getCode());
                    variables.put("tranferToTime", TextUtils.formatDate(arv.getTranferToTime(), FORMATDATE));
                    sendEmail(getSiteEmail(sourceSite.getID(), ServiceEnum.OPC), "[Thông báo] Phản hồi tiếp nhận điều trị thành công", EmailoutboxEntity.OPC_ARV_RECEIVE_SUCCESS_MAIL, variables);
                }
                staffServices.sendMessage(arvEntity.getUpdatedBY(), "Tiếp nhận khách hàng chuyển gửi điều trị thành công", String.format("Bệnh nhân: %s", arvEntity.getCode()), String.format("%s?code=%s", UrlUtils.opcIndex(), arvEntity.getCode()));
                return redirect(UrlUtils.opcArvIndex(arv.getID(), form.getPageRedirect()));
            }

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "confirm".equals(form.getPageRedirect())) {
                opcArvService.update(cloneEntity);//cập nhật bản ghi cũ
                opcArvService.logArv(arv.getID(), cloneEntity.getPatientID(), "Tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + options.get("siteOpcTo").get(cloneEntity.getSiteID() != null ? cloneEntity.getSiteID().toString() : ""));
                redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
                return redirect(UrlUtils.opcReceiveNew(cloneEntity.getID(), arv.getID()));
            }

            opcArvService.update(cloneEntity);//cập nhật bản ghi cũ
            opcArvService.logArv(arv.getID(), cloneEntity.getPatientID(), "Tiếp nhận bệnh nhân được chuyển gửi từ cơ sở " + options.get("siteOpcTo").get(cloneEntity.getSiteID() != null ? cloneEntity.getSiteID().toString() : ""));

            redirectAttributes.addFlashAttribute("success", "Thêm mới bệnh án thành công");
            return redirect(UrlUtils.opcReceiveIndex());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Thêm mới bệnh án không thành công");
            return redirect(UrlUtils.opcReceiveIndex());
        }
    }

}
