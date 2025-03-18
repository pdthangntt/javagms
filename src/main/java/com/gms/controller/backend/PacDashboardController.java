package com.gms.controller.backend;

import com.gms.entity.db.ParameterEntity;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_pac_dashbord")
public class PacDashboardController extends HtcController {

    @RequestMapping(value = {"/pac-dashboard/index.html"}, method = RequestMethod.GET)
    public String actionDashboard(Model model) {
        model.addAttribute("title", "Tổng quan");
        model.addAttribute("smallTitle", "Quản lý người nhiễm");
        return "backend/pac/dashboard";
    }

}
