package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.opc_arv.OpcTestForm;
import com.gms.entity.validate.OpcNewTestValidate;
import com.gms.entity.validate.OpcTestValidate;
import com.gms.service.OpcArvService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * @author pdThang
 */
@Controller(value = "backend_opc_test")
public class OpcTestController extends OpcController {

    @Autowired
    private SiteService siteService;
    @Autowired
    private OpcTestService testService;
    @Autowired
    private OpcArvService arvService;
    @Autowired
    private OpcTestValidate validate;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcNewTestValidate opcTestValidate;

    /**
     * Sửa thông tin khác
     *
     * @param model
     * @param arvID
     * @param tab
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/opc-test/update.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "id") Long arvID,
            @RequestParam(name = "tab", defaultValue = "all", required = false) String tab,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        if (!isOPC() && !isOpcManager()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có dịch vụ");
            return redirect(UrlUtils.opcIndex());
        }
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

        if (arv.getTranferToTimeOpc() != null) {
            redirectAttributes.addFlashAttribute("error", String.format("Bệnh án #%s đã được tiếp nhận không thể sửa", arv.getCode()));
            return redirect(UrlUtils.opcArvView(arv.getID()));
        }

        List<OpcTestEntity> models = testService.findByPatientID(arv.getPatientID(), !tab.equals("all"));
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        HashMap<String, String> stages = new HashMap<>();
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
        }
        HashMap<String, String> stagePatients = new HashMap<>();
        List<OpcStageEntity> stagePatient = opcStageService.findByPatientID(arv.getPatientID());
        if (stagePatient != null && !stagePatient.isEmpty()) {
            for (OpcStageEntity stage : stagePatient) {
                stagePatients.put(String.valueOf(stage.getID()), stage.getName());
            }
        }
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
//        options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());

        model.addAttribute("stages", stages);
        model.addAttribute("stagePatients", stagePatients);
        model.addAttribute("title", "Thông tin bệnh án");
        model.addAttribute("options", options);
        model.addAttribute("models", models);
        model.addAttribute("tab", tab);
        model.addAttribute("arv", arv);
        model.addAttribute("siteID", getCurrentUser().getSite().getID());
        model.addAttribute("currentSiteID", currentUser.getSite().getID().toString());
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("arvID", arvID);
        return "backend/opc_arv/test";
    }

    /**
     * Xoá vĩnh viễn khỏi hệ thống
     *
     * @param redirectAttributes
     * @param arvID
     * @param ID
     * @return
     */
    @RequestMapping(value = "/opc-test/delete.html", method = RequestMethod.GET)
    public String actionDelete(@RequestParam(name = "arvid") Long arvID,
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
            OpcTestEntity model = testService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lần khám");
            }
//            if (!model.getSiteID().equals(currentUser.getSite().getID())) {
//                throw new Exception("Bạn không có quyền xoá lượt khám này");
//            }
            testService.del(ID);
            testService.logTest(model.getArvID(), model.getPatientID(), model.getID(), "Lượt xét nghiệm đã bị xóa vĩnh viễn");
            redirectAttributes.addFlashAttribute("success", "Lượt xét nghiệm được xoá vĩnh viễn khỏi hệ thống");
            return redirect(UrlUtils.opcTest(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcTest(arvID, "all"));
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
    @RequestMapping(value = "/opc-test/remove.html", method = RequestMethod.GET)
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
            OpcTestEntity model = testService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lần khám");
            }
//            if (!model.getSiteID().equals(currentUser.getSite().getID())) {
//                throw new Exception("Bạn không có quyền xoá lượt khám này");
//            }
            model.setRemove(true);
            model.setRemoteAT(new Date());
            testService.save(model);
            testService.logTest(model.getArvID(), model.getPatientID(), model.getID(), "Lượt xét nghiệm đã bị xóa");
            redirectAttributes.addFlashAttribute("success", "Lượt xét nghiệm được xóa thành công");
            return redirect(UrlUtils.opcTest(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcTest(arvID, "all"));
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
    @RequestMapping(value = "/opc-test/restore.html", method = RequestMethod.GET)
    public String actionRestore(@RequestParam(name = "arvid") Long arvID,
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
            OpcTestEntity model = testService.findOne(ID);
            if (model == null || !model.getPatientID().equals(arv.getPatientID())) {
                throw new Exception("Không tìm thấy lần khám");
            }
//            if (!model.getSiteID().equals(currentUser.getSite().getID())) {
//                throw new Exception("Bạn không có quyền khôi phục lượt khám này");
//            }
            model.setRemove(false);
            model.setRemoteAT(null);
            testService.save(model);
            testService.logTest(model.getArvID(), model.getPatientID(), model.getID(), "Lượt xét nghiệm được khôi phục");
            redirectAttributes.addFlashAttribute("success", "Lượt xét nghiệm đã được khôi phục");
            return redirect(UrlUtils.opcTest(arvID, "all"));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(UrlUtils.opcTest(arvID, "all"));
        }

    }

    @RequestMapping(value = "/opc-test/action-new.html", method = RequestMethod.GET)
    public String actionNew(Model model, OpcTestForm form,
            @RequestParam(name = "arvid") Long arvID,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        OpcArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }

        HashMap<String, String> stages = new HashMap<>();
        stages.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        if (entitys == null || entitys.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "Bệnh nhân không có giai đoạn điều trị");
            return redirect(UrlUtils.opcTest(arvID));
        }
        OpcStageEntity item = new OpcStageEntity();
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
            item = opcStageService.findByPatientIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        }
        form.setStageID(item.getID().toString());
        form.setStatusOfTreatmentID(item.getStatusOfTreatmentID());
        if(item.getID() != null && item.getStatusOfTreatmentID() != null && !item.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())){
            options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());
        }
        
        model.addAttribute("stages", stages);
        model.addAttribute("title", "Bệnh án " + "<span class='text-danger'>" + "#" + arv.getCode() + "</span>");
        model.addAttribute("small_title", "Thêm mới lượt xét nghiệm");
        model.addAttribute("options", options);
        model.addAttribute("arvID", arvID);
        model.addAttribute("form", form);
        model.addAttribute("isOPC", isOPC());
        model.addAttribute("urlForm", UrlUtils.opcTestNew(arvID));
        return "backend/opc_arv/test_form";
    }

    @RequestMapping(value = "/opc-test/action-new.html", method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid OpcTestForm form,
            @RequestParam(name = "arvid") Long arvID,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        
        if(StringUtils.isNotEmpty(form.getStageID())){
            OpcStageEntity stageEntity = opcStageService.findOne(Long.parseLong(form.getStageID()));
            if(stageEntity != null && !stageEntity.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())){
                options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());
            }
        }
        LoggedUser currentUser = getCurrentUser();

        OpcArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }

        HashMap<String, String> stages = new HashMap<>();
        stages.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        if (entitys == null || entitys.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "Bệnh nhân không có giai đoạn điều trị");
            return redirect(UrlUtils.opcTest(arvID));
        }
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
        }
        form.setSiteID(String.valueOf(arv.getSiteID()));
        form.setFirstCd4Time(TextUtils.formatDate(arv.getFirstCd4Time(), FORMATDATE));

        model.addAttribute("stages", stages);
        model.addAttribute("title", "Bệnh án " + "<span class='text-danger'>" + "#" + arv.getCode() + "</span>");
        model.addAttribute("small_title", "Thêm mới lượt xét nghiệm");
        model.addAttribute("options", options);
        model.addAttribute("arvID", arvID);
        model.addAttribute("form", form);
        model.addAttribute("isOPC", isOPC());

        //validate form
        opcTestValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/opc_arv/test_form";
        }

        try {
            OpcTestEntity entity = form.toForm(new OpcTestEntity());
            entity.setArvID(arv.getID());
            entity.setPatientID(arv.getPatientID());
            if (isOpcManager()) {
                entity.setSiteID(arv.getSiteID());
            } else {
                entity.setSiteID(currentUser.getSite().getID());
            }
            testService.save(entity);
            testService.logTest(entity.getArvID(), entity.getPatientID(), entity.getID(), "Thêm mới lượt xét nghiệm");
            redirectAttributes.addFlashAttribute("success", "Lượt xét nghiệm được thêm mới thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());
            String print = StringUtils.isEmpty(form.getPrint()) ? "" : "&print=1";
            return redirect(UrlUtils.opcTest(arvID) + print);

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "backend/vnpt_config/form";
    }

    @RequestMapping(value = "/opc-test/action-update.html", method = RequestMethod.GET)
    public String actionUpdate(Model model, OpcTestForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }
        OpcTestEntity entity = testService.findOne(ID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy lần khám " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }
        HashMap<String, String> stages = new HashMap<>();
        stages.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        if (entitys == null || entitys.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "Bệnh nhân không có giai đoạn điều trị");
            return redirect(UrlUtils.opcTest(arvID));
        }
        OpcStageEntity item = new OpcStageEntity();
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
            item = opcStageService.findByPatientIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        }
        form = form.setFrom(entity);

        model.addAttribute("stages", stages);
        model.addAttribute("title", "Bệnh án " + "<span class='text-danger'>" + "#" + arv.getCode() + "</span>");
        model.addAttribute("small_title", "Cập nhật lượt xét nghiệm");
        model.addAttribute("options", options);
        model.addAttribute("arvID", arvID);
        model.addAttribute("form", form);
        model.addAttribute("isOPC", isOPC());

        return "backend/opc_arv/test_form";

    }

    @RequestMapping(value = "/opc-test/action-update.html", method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @ModelAttribute("form") @Valid OpcTestForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        
        if(StringUtils.isNotEmpty(form.getStageID())){
            OpcStageEntity stageEntity = opcStageService.findOne(Long.parseLong(form.getStageID()));
            if(stageEntity != null && !stageEntity.getStatusOfTreatmentID().equals(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey())){
                options.get(ParameterEntity.STATUS_OF_TREATMENT).remove(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());
            }
        }
        LoggedUser currentUser = getCurrentUser();

        OpcArvEntity arv = arvService.findOne(arvID);
        OpcTestEntity entity = testService.findOne(ID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }

        HashMap<String, String> stages = new HashMap<>();
        stages.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        if (entitys == null || entitys.isEmpty()) {
            redirectAttributes.addFlashAttribute("warning", "Bệnh nhân không có giai đoạn điều trị");
            return redirect(UrlUtils.opcTest(arvID));
        }
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
        }

        model.addAttribute("stages", stages);
        model.addAttribute("title", "Bệnh án " + "<span class='text-danger'>" + "#" + arv.getCode() + "</span>");
        model.addAttribute("small_title", "Cập nhật lượt xét nghiệm");
        model.addAttribute("options", options);
        model.addAttribute("arvID", arvID);
        model.addAttribute("form", form);
        model.addAttribute("isOPC", isOPC());

        //validate form
        form.setFirstCd4Time(TextUtils.formatDate(arv.getFirstCd4Time(), FORMATDATE));
        opcTestValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/opc_arv/test_form";
        }

        try {
            //Sửa không được phép sửa cơ sở, mã bệnh án, mã bệnh nhân
            form.toForm(entity);
            testService.save(entity);
            testService.logTest(entity.getArvID(), entity.getPatientID(), entity.getID(), "Cập nhật lượt xét nghiệm");
            redirectAttributes.addFlashAttribute("success", "Lượt xét nghiệm được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());

            String print = StringUtils.isEmpty(form.getPrint()) ? "" : "&print=1";
            return redirect(UrlUtils.opcTest(arvID) + print);

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "backend/vnpt_config/form";

    }

    @RequestMapping(value = "/opc-test/action-view.html", method = RequestMethod.GET)
    public String actionView(Model model, OpcTestForm form,
            @RequestParam(name = "arvid") Long arvID,
            @RequestParam(name = "id") Long ID,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        OpcArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }
        OpcTestEntity entity = testService.findOne(ID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy lần khám " + arvID);
            return redirect(UrlUtils.opcTest(arvID));
        }
        HashMap<String, String> stages = new HashMap<>();
        stages.put("", "Chọn giai đoạn điều trị");
        List<OpcStageEntity> entitys = opcStageService.findByPatientIDIDAndSiteID(arv.getPatientID(), arv.getSiteID());
//        if (entitys == null || entitys.isEmpty()) {
//            redirectAttributes.addFlashAttribute("warning", "Bệnh nhân không có giai đoạn điều trị");
//            return redirect(UrlUtils.opcTest(arvID));
//        }
        OpcStageEntity item = new OpcStageEntity();
        if (entitys != null && !entitys.isEmpty()) {
            for (OpcStageEntity stage : entitys) {
                stages.put(String.valueOf(stage.getID()), stage.getName());
            }
            item = opcStageService.findByPatientIDAndSiteID(arv.getPatientID(), arv.getSiteID());
        }
        form = form.setFrom(entity);

        model.addAttribute("stages", stages);
        model.addAttribute("title", "Bệnh án " + "<span class='text-danger'>" + "#" + arv.getCode() + "</span>");
        model.addAttribute("small_title", "Xem lượt xét nghiệm");
        model.addAttribute("options", getOptions(false, null));
        model.addAttribute("arvID", arvID);
        model.addAttribute("form", form);

        return "backend/opc_arv/test_view";
    }
}
