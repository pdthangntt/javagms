package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.db.HtcTargetEntity;
import com.gms.entity.form.HtcTargetForm;
import com.gms.entity.validate.HtcTargetVarlidate;
import com.gms.service.HtcTargetService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author NamAnh_HaUI
 * @update pdThang
 */
@Controller(value = "backend_target")
public class HtcTargetController extends HtcController {

    @Autowired
    HtcTargetService htcTargetService;

    @Autowired
    HtcTargetVarlidate htcTargetValidate;

    /**
     *
     * @param model
     * @return
     * @pdthang
     */
    @GetMapping(value = {"/htc-target/index.html"})
    public String actionIndex(Model model) {
        List<HtcTargetEntity> list = htcTargetService.findAll(getCurrentUser().getSite().getID());
        for (HtcTargetEntity item : list) {
           double myNum = item.getCareForTreatment();
           int precision = 100; //keep 2 digits// tach 2 chu so thap phan cuoi cung
           myNum = Math.floor(myNum * precision +.2)/precision;
           item.setCareForTreatment(myNum);
        }
        model.addAttribute("listT", list);
        model.addAttribute("title", "Chỉ tiêu Tư vấn & Xét nghiệm");
        return "backend/htc/target_index";
    }

    @GetMapping(value = {"/htc-target/new.html"})
    public String actionNew(Model model) {
        HtcTargetForm form = new HtcTargetForm();
        form.setSiteID(getCurrentUser().getSite().getID());
        model.addAttribute("title", "Thêm mới chỉ tiêu TVXN");
        model.addAttribute("form", form);
        return "backend/htc/target_new";
    }

    @PostMapping(value = {"/htc-target/new.html"})
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid HtcTargetForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        //init data
        form.setSiteID(getCurrentUser().getSite().getID());
        //action
        model.addAttribute("title", "Thêm chỉ tiêu TVXN");
        model.addAttribute("form", form);
        htcTargetValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/htc/target_new";
        }
        HtcTargetEntity entity = new HtcTargetEntity();
        entity.setYears(form.getYears());
        entity.setNumberCustomer(form.getNumberCustomer());
        entity.setNumberPositive(form.getNumberPositive());
        entity.setNumberGetResult(form.getNumberGetResult());
        entity.setPositiveAndGetResult(form.getPositiveAndGetResult());
        entity.setCareForTreatment(Double.valueOf(form.getCareForTreatment()));
        entity.setSiteID(getCurrentUser().getSite().getID());

        entity = htcTargetService.save(entity);
        if (entity == null) {
            model.addAttribute("error", "Thêm mới chỉ tiêu không thành công! xin vui lòng thử lại.");
            return "backend/htc/target_new";
        }
        redirectAttributes.addFlashAttribute("success", "Thêm chỉ tiêu thành công");
        return redirect(UrlUtils.htcTarget());
    }

    @RequestMapping(value = {"/htc-target/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid") Long ID,
            HtcTargetForm form,
            RedirectAttributes redirectAttributes) {

        HtcTargetEntity entity = htcTargetService.findOne(ID);
        if (entity == null || entity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            //không tồn tại
        }

        form.setID(entity.getID());
        form.setSiteID(entity.getSiteID());
        form.setYears(entity.getYears());
        form.setNumberCustomer(entity.getNumberCustomer());
        form.setNumberPositive(entity.getNumberPositive());
        form.setNumberGetResult(entity.getNumberGetResult());
        form.setPositiveAndGetResult(entity.getPositiveAndGetResult());
        form.setCareForTreatment(entity.getCareForTreatment().toString());
        model.addAttribute("title", "Cập nhật chỉ tiêu TVXN");
        model.addAttribute("form", form);
        return "backend/htc/target_update";
    }

    @RequestMapping(value = {"/htc-target/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "oid") Long ID,
            @ModelAttribute("form") @Valid HtcTargetForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        HtcTargetEntity entity = htcTargetService.findOne(ID);

        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm chỉ tiêu có mã %s", form.getID()));
            return redirect(UrlUtils.htcTarget());
        }
        form.setSiteID(entity.getSiteID());
        form.setID(entity.getID());
        form.setYears(entity.getYears());
        model.addAttribute("title", "Cập nhật chỉ tiêu TVXN");
        model.addAttribute("form", form);

        htcTargetValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật không thành công! xin vui lòng thử lại");
            return "backend/htc/target_update";
        }

        entity.setNumberCustomer(form.getNumberCustomer());
        entity.setNumberPositive(form.getNumberPositive());
        entity.setNumberGetResult(form.getNumberGetResult());
        entity.setPositiveAndGetResult(form.getPositiveAndGetResult());
        entity.setCareForTreatment(Double.valueOf(form.getCareForTreatment()));
        htcTargetService.save(entity);
        redirectAttributes.addFlashAttribute("success", "Lưu chỉ tiêu thành công");
        return redirect(UrlUtils.htcTarget());
    }

    @RequestMapping(value = {"/htc-target/remove.html"}, method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid", required = false, defaultValue = "") String ID,
            RedirectAttributes redirectAttributes) {
        htcTargetService.remove(Long.parseLong(ID));
        redirectAttributes.addFlashAttribute("success", "Xóa chỉ tiêu TVXN thành công");
        return redirect(UrlUtils.htcTarget());
    }
}
