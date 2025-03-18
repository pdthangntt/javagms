package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.FeedbackEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.FeedbackForm;
import com.gms.entity.input.FeedbackSearch;
import com.gms.service.FeedbackService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
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
 * @author NamAnh_HaUI
 */
@Controller(value = "backend_feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private StaffService staffService;
    @Autowired
    private SiteService siteService;

    private final String ADMIN_EMAIL = "hotro.hiv.ims@gmail.com";

    private HashMap<String, String> getStatusTypeOption() {
        HashMap<String, String> list = new HashMap<String, String>();
        list.put("Mới", "Mới");
        list.put("Đang xử lý", "Đang xử lý");
        list.put("Đã xử lý", "Đã xử lý");
        list.put("Tạm ngừng xử lý", "Tạm ngừng xử lý");
        list.put("Bỏ qua", "Bỏ qua");
        return list;
    }

    /**
     *
     * @param entitys
     * @return
     */
    private HashMap<Long, String> getSiteOption(List<FeedbackEntity> entitys) {
        HashMap<Long, String> options = new HashMap<>();
        Set siteIDs = new HashSet();
        for (FeedbackEntity feedbackEntity : entitys) {
            siteIDs.add(feedbackEntity.getSiteID());
        }
        if (!siteIDs.isEmpty()) {
            List<SiteEntity> models = siteService.findByIDs(siteIDs);
            for (SiteEntity model : models) {
                options.put(model.getID(), model.getShortName());
            }
        }
        return options;
    }

    @RequestMapping(value = {"/feedback/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        FeedbackSearch search = new FeedbackSearch();
        search.setStatus(status);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setIsRemove(false);
        DataPage<FeedbackEntity> dataPage = feedbackService.findAll(search);

        model.addAttribute("siteOptions", getSiteOption(dataPage.getData()));
        model.addAttribute("title", "Danh sách góp ý");
        model.addAttribute("dataPage", dataPage);
        return "backend/feedback/index";
    }

    @RequestMapping(value = {"/feedback/compose.html"}, method = RequestMethod.GET)
    public String actionCompose(Model model,
            FeedbackForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        StaffEntity staff = staffService.findOne(currentUser.getUser().getID());
        form.setName(staff.getName());
        form.setSendEmail(staff.getEmail());
        form.setSendPhone(staff.getPhone());
        model.addAttribute("title", "Góp ý hệ thống");
        model.addAttribute("form", form);
        return "backend/feedback/compose";
    }

    @RequestMapping(value = {"/feedback/update.html"}, method = RequestMethod.GET)
    public String actionFeedbackDetail(Model model,
            @RequestParam(name = "oid") Long ID,
            FeedbackForm form,
            RedirectAttributes redirectAttributes) {
        FeedbackEntity feedBackEntity = feedbackService.findOne(ID);
        if (feedBackEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy góp ý có mã %s", ID));
            return redirect(UrlUtils.feedbackIndex());
        }

        form.setID(feedBackEntity.getID());
        form.setSiteID(feedBackEntity.getSiteID());
        form.setName(feedBackEntity.getName());
        form.setSendEmail(feedBackEntity.getSendEmail());
        form.setSendPhone(feedBackEntity.getSendPhone());
        form.setContent(feedBackEntity.getContent());
        form.setStatus(feedBackEntity.getStatus());
        model.addAttribute("title", "Thông tin góp ý");
        model.addAttribute("form", form);
        model.addAttribute("types", getStatusTypeOption());
        return "backend/feedback/update";
    }

    @RequestMapping(value = "/feedback/update.html", method = RequestMethod.POST)
    public String actionUpdateFeedBack(Model model,
            @ModelAttribute("form") @Valid FeedbackForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Cập nhật góp ý");
            model.addAttribute("types", getStatusTypeOption());
            return "backend/feedback/update";
        }
        FeedbackEntity entity = new FeedbackEntity();
        entity.setID(form.getID());
        entity.setName(form.getName());
        entity.setSendEmail(form.getSendEmail());
        entity.setSendPhone(form.getSendPhone());
        entity.setContent(form.getContent());
        entity.setSiteID(form.getSiteID());
        entity.setStatus(form.getStatus());
        feedbackService.save(entity);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        return redirect(UrlUtils.feedbackIndex());
    }

    @RequestMapping(value = {"/feedback/compose.html"}, method = RequestMethod.POST)
    public String actionSendFeedBack(Model model,
            @ModelAttribute("form") @Valid FeedbackForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Góp ý hệ thống");
            redirectAttributes.addFlashAttribute("error", "Gửi góp ý không thành công, vui lòng thử lại!");
            return "backend/feedback/compose";
        }
        LoggedUser currentUser = getCurrentUser();
        FeedbackEntity entity = new FeedbackEntity();
        entity.setName(form.getName());
        entity.setSendEmail(form.getSendEmail());
        entity.setSendPhone(form.getSendPhone());
        entity.setContent(form.getContent());
        entity.setSiteID(currentUser.getSite().getID());
        entity.setStatus("Mới");
        feedbackService.save(entity);
        if (entity.getSendEmail() != null) {
            sendEmailNotify(entity.getSendEmail(),
                    "Góp ý vừa được gửi",
                    String.format(
                            "Xin chào %s <br/>Góp ý của anh/chị vừa được gửi về hệ thống lúc %s.<br/>"
                            + "Nội dung: %s<br/>"
                            + "Cảm ơn ý kiến đóng góp của anh/chị.",
                            entity.getName(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy"), entity.getContent()
                    ));
        }
        sendEmailNotify(ADMIN_EMAIL,
                "Góp ý mới",
                String.format(
                        "Góp ý của người dùng %s vừa được gửi về hệ thống lúc %s.<br/>"
                        + "Nội dung: %s<br/>",
                        entity.getName(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy"), entity.getContent()
                ));
        model.addAttribute("title", "Góp ý hệ thống");
        model.addAttribute("form", form);
        redirectAttributes.addFlashAttribute("success", "Hệ thống đã ghi nhận ý kiến của bạn. Chúng tôi sẽ kiểm tra sớm nhất, xin trân thành cảm ơn.");
        return redirect(UrlUtils.feedbackCompose());
    }

    @RequestMapping(value = "/feedback/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        feedbackService.remove(ID);
        redirectAttributes.addFlashAttribute("success", "Xóa góp ý thành công");
        return redirect(UrlUtils.feedbackIndex());
    }

}
