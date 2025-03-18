package com.gms.controller.backend;

import com.gms.service.ParameterService;
import com.gms.service.QlReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_dashboard")
public class DashboardController extends BaseController {
    
    @Autowired
    private QlReportService qlReportService;

    @GetMapping(value = {"/dashboard.html"})
    public String actionIndex(Model model) {
        return "backend/dashboard/index";
    }

    @GetMapping(value = {"/dashboard-htc.html"})
    public String actionHTC(Model model) {
        model.addAttribute("parent_title", "Tổng quan");
        model.addAttribute("title", "Tư vấn và Xét nghiệm");
        return "backend/dashboard/htc";
    }

    @GetMapping(value = {"/dashboard-pac.html"})
    public String actionPAC(Model model) {
        model.addAttribute("parent_title", "Tổng quan");
        model.addAttribute("title", "Quản lý người nhiễm");
        return "backend/dashboard/pac";
    }

    @GetMapping(value = {"/dashboard-opc.html"})
    public String actionOPC(Model model) {
        model.addAttribute("parent_title", "Tổng quan");
        model.addAttribute("title", "Điều trị ngoại trú");
        return "backend/dashboard/opc";
    }
    
    
    @GetMapping(value = {"/dashboard-ql.html"})
    public String actionQL(Model model) {
        model.addAttribute("parent_title", "Tổng quan");
        model.addAttribute("title", "Cảnh báo tình hình dịch");
        model.addAttribute("lastUpdate", qlReportService.getLastUpdateTime());
        return "backend/dashboard/ql";
    }
}
