package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.form.EmailoutboxForm;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.input.EmailSearch;
import com.gms.service.EmailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author TrangBN
 */
@Controller(value = "backend_mail")
public class EmailOutboxController extends BaseController {

    @Autowired
    EmailService emailServices;

    @RequestMapping(value = {"/mail/compose.html"}, method = RequestMethod.GET)
    public String actionCompose(Model model, EmailoutboxForm form) {
        model.addAttribute("title", "Soạn email");
        model.addAttribute("form", form);
        return "backend/mail/compose";
    }

    @RequestMapping(value = {"/mail/detail.html"}, method = RequestMethod.GET)
    public String actionDetail(Model model,
            @RequestParam(name = "oid") Long emailID,
            RedirectAttributes redirectAttributes) {
        EmailoutboxEntity item = emailServices.findOne(emailID);
        if (item == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy email có mã %s", emailID));
            return redirect(UrlUtils.mailIndex());
        }

        model.addAttribute("title", "Chi tiết email");
        model.addAttribute("item", item);
        return "backend/mail/detail";
    }

    @RequestMapping(value = {"/mail/compose.html"}, method = RequestMethod.POST)
    public String actionSend(Model model,
            @ModelAttribute("form") @Valid EmailoutboxForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Soạn email");
            return UrlUtils.mailCompose();
        }
        sendEmailNotify(form.getTo(), form.getSubject(), form.getContent());
        redirectAttributes.addFlashAttribute("success", "Email đã được gửi thành công!");
        return redirect(UrlUtils.mailIndex());
    }

    /**
     *
     * @param model
     * @param tab
     * @param subject
     * @param from
     * @param to
     * @param sendAt
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = {"/mail/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "subject", required = false, defaultValue = "") String subject,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "send_at", required = false, defaultValue = "") String sendAt,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {

        EmailSearch search = new EmailSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setFrom(from);
        search.setTo(to);
        search.setSendAtFrom(sendAt);
        search.setSendAtTo(sendAt);
        if (subject != null && !"".equals(subject)) {
            search.setSubject(StringUtils.normalizeSpace(subject.trim()));
        }
        switch (tab) {
            case "sent":
                search.setTab(1);
                break;
            case "error":
                search.setTab(2);
                break;
            case "not":
                search.setTab(3);
                break;
        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"sendAt"}));
        DataPage<EmailoutboxEntity> dataPage = emailService.find(search);
        model.addAttribute("title", "Quản lý Email");
        model.addAttribute("dataPage", dataPage);
        return "backend/mail/index";
    }

    /**
     * Perform resend email
     *
     * @param model
     * @param emailID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/mail/resend.html"}, method = RequestMethod.GET)
    public String actionResend(Model model,
            @RequestParam(name = "emailid", required = false) Long emailID,
            RedirectAttributes redirectAttributes) {

        EmailoutboxEntity emailoutboxEntity = emailServices.findOne(emailID);
        if (emailoutboxEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Email không hợp lệ");
            return redirect(UrlUtils.mailIndex());
        }

        emailoutboxEntity.setIsRun(false);
        emailServices.save(emailoutboxEntity);

        redirectAttributes.addFlashAttribute("success", "Đã gửi lại email");

        return redirect(UrlUtils.mailDetail(emailID));
    }
}
