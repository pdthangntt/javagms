package com.gms.controller.backend;

import com.gms.repository.impl.HtcVisitRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_laytest_dashbord")
public class LaytestDashbordController extends HtcController {

    @Autowired
    HtcVisitRepositoryImpl htcVisitRepositoryImpl;

    @RequestMapping(value = {"/laytest-dashboard/index.html"}, method = RequestMethod.GET)
    public String actionDashboard(Model model) {
        model.addAttribute("title", "Tổng quan");
        model.addAttribute("smallTitle", "Xét nghiệm tại cộng đồng");
        return "backend/htc_laytest/dashboard";
    }

}
