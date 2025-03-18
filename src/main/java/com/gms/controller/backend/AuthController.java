package com.gms.controller.backend;

import com.gms.components.PasswordUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.controller.WebController;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.ForgotForm;
import com.gms.entity.form.ResetPwdForm;
import com.gms.service.StaffService;
import java.util.Date;
import java.util.Random;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Văn Thành
 */
@Controller(value = "backend_auth")
public class AuthController extends WebController {

    @Autowired
    private StaffService staffService;

    @RequestMapping(value = {"/signin.html"}, method = RequestMethod.GET)
    public String actionSignin(Model model, @RequestHeader(name = "X-Forwarded-Host", required = false) String host,
            @RequestParam(value = "error", defaultValue = "false") boolean isError) {
        getLogger().info("Header request host: " + host);

        if (getCurrentUser() != null) {
            return redirect(UrlUtils.home());
        }
        model.addAttribute("title", "Đăng nhập hệ thống thông tin HIV");
        if (host != null && host.contains("hub")) {
            model.addAttribute("title", "Đăng nhập hệ thống HUB PQM");
        }
        model.addAttribute("isError", isError);
        model.addAttribute("host", host);
        return "backend/auth/signin_v2";
    }

    @RequestMapping(value = {"/logout.html"}, method = RequestMethod.GET)
    public String actionLogout(Model model) {
        return redirect(UrlUtils.home());
    }

    @RequestMapping(value = {"/auth/forgot-password.html"}, method = RequestMethod.GET)
    public String actionForgotPwd(Model model) {
        ForgotForm form = new ForgotForm();
        model.addAttribute("title", "Quên mật khẩu");
        model.addAttribute("form", form);
        return "backend/auth/forgot";
    }

    @RequestMapping(value = {"/auth/forgot-password.html"}, method = RequestMethod.POST)
    public String doActionForgotPwd(Model model,
            @ModelAttribute("form") @Valid ForgotForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Quên mật khẩu");
            model.addAttribute("form", form);
            return "backend/auth/forgot";
        }

        StaffEntity staff = staffService.findByUsername(form.getEmail());
        if (staff == null) {
            model.addAttribute("error", String.format("%s chưa đăng ký", form.getEmail()));
            return "backend/auth/forgot";
        }
        if (staff.getEmail() == null || staff.getEmail().equals("")) {
            model.addAttribute("error", String.format("%s chưa đăng ký email", form.getEmail()));
            return "backend/auth/forgot";
        }

        String code = String.format("%04d", new Random().nextInt(10000));
        staff.setToken(PasswordUtils.encrytePassword(code));
        staff.setCode(code);
        staff.setTokenTime(new Date());
        staffService.save(staff);

        sendEmailNotify(staff.getEmail(), String.format("Mã xác thực  %s", code), "Mã xác thực lấy lại mật khẩu");

        redirectAttributes.addFlashAttribute("success", "Vui lòng kiểm tra email để lấy mã xác thực!");
        return redirect("/auth/reset-password.html?token=" + staff.getToken());
    }

    @RequestMapping(value = {"/auth/reset-password.html"}, method = RequestMethod.GET)
    public String actionResetPwd(Model model, @RequestParam(name = "token") String token) {
        ResetPwdForm form = new ResetPwdForm();
        model.addAttribute("title", "Đặt lại mật khẩu");
        model.addAttribute("form", form);
        model.addAttribute("token", token);
        return "backend/auth/reset";
    }

    @RequestMapping(value = {"/auth/reset-password.html"}, method = RequestMethod.POST)
    public String doActionResetPwd(Model model, @RequestParam(name = "token") String token,
            @ModelAttribute("form") @Valid ResetPwdForm form,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "backend/auth/reset";
        }

        StaffEntity staff = staffService.findByToken(token);
        if (staff == null) {
            model.addAttribute("error", "Mã xác thực hết hạn");
            return "backend/auth/reset";
        }
        if (staff.getTokenTime() == null || !TextUtils.formatDate(staff.getTokenTime(), FORMATDATE).equals(TextUtils.formatDate(new Date(), FORMATDATE))) {
            model.addAttribute("error", "Mã xác thực hết hạn");
            return "backend/auth/reset";
        }
        if (staff.getCode() == null || !staff.getCode().equals(form.getCode().trim())) {
            bindingResult.rejectValue("code", "code.error.message", "Mã xác không chính xác");
            return "backend/auth/reset";
        }

        staff.setPwd(PasswordUtils.encrytePassword(form.getPassword()));
        staff.setCode(null);
        staff.setToken(null);
        staff.setTokenTime(null);
        staffService.save(staff);
        redirectAttributes.addFlashAttribute("success", "Đặt lại mật khẩu thành công!");
        return redirect(UrlUtils.signin());
    }
}
