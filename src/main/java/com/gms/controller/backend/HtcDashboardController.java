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
@Controller(value = "backend_htc_dashbord")
public class HtcDashboardController extends HtcController {

    @Autowired
    HtcVisitRepositoryImpl htcVisitRepositoryImpl;

    @RequestMapping(value = {"/htc-dashboard/index.html"}, method = RequestMethod.GET)
    public String actionDashboard(Model model,
            @RequestParam(name = "service", required = false, defaultValue = "") String services) {
        HashMap<String, String> serviceOptions = getOptions().get(ParameterEntity.SERVICE_TEST);
        serviceOptions.put("", "Tất cả");

        model.addAttribute("title", "Tổng quan");
        model.addAttribute("smallTitle", "Tư vấn & Xét nghiệm");
        model.addAttribute("serviceOptions", serviceOptions);
        model.addAttribute("services", services);
        return "backend/htc/dashboard";
    }

}
