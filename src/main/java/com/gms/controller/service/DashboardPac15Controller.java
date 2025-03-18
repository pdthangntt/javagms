package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardPacImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * D1.5_Điều trị
 *
 * @author vvthanh
 */
@RestController
public class DashboardPac15Controller extends DashboardController {

    @Autowired
    private DashboardPacImpl dashboardPacImpl;

    @RequestMapping(value = "/dashboard-pac/d15-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) {
        return new Response<>(true, null);
    }

    @RequestMapping(value = "/dashboard-pac/d15-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        return new Response<>(true, null);
    }

    @RequestMapping(value = "/dashboard-pac/d15-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) {
        return new Response<>(true, null);
    }

    @RequestMapping(value = "/dashboard-pac/d15-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        return new Response<>(true, null);
    }

    @RequestMapping(value = "/dashboard-pac/d15-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {
        return new Response<>(true, null);
    }
}
