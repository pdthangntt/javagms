package com.gms.controller.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * OPC scan mã bệnh nhận qr
 *
 * @author Văn Thành
 */
@Controller(value = "backend_opc_qr")
public class OpcQRController extends OpcController {

    @RequestMapping(value = "/opc-qr/index.html", method = RequestMethod.GET)
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("options", getOptions(true, null));
        model.addAttribute("title", "Quét mã QR");
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        return "backend/opc_arv/qr";
    }
}
