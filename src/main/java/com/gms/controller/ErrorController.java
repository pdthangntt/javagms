package com.gms.controller;

import com.gms.components.UrlUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Văn Thành
 */
@Controller(value = "error")
public class ErrorController extends WebController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping(value = {"/403.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model) {
        return "backend/error/page403";
    }

    @RequestMapping(value = {"/error", "/error.html"}, method = RequestMethod.GET)
    public String handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        model.addAttribute("content", message);
        if(message != null && message.toString().contains("thymeleaf")) {
            return "backend/error/page_none";
        }
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("code", "404");
                model.addAttribute("message", "Không tìm thấy trang yêu cầu.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("code", "500");
                model.addAttribute("message", "Có lỗi kỹ thuật! Vui lòng liên hệ đội kỹ thuật để được hỗ trợ.");
                return "backend/error/page405";
            }
        }
        return "backend/error/page404";
    }

    @Override
    public String getErrorPath() {
        return UrlUtils.error();
    }

}
