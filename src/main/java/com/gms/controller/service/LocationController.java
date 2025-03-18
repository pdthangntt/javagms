package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController extends BaseController {

    @Autowired
    private LocationsService locationsService;

    @RequestMapping("/location.json")
    public Response<?> actionLocations() {
        return new Response<>(true, locationsService.findAll());
    }

    @RequestMapping("/location/ward.json")
    public Response<?> actionWard(@RequestParam(name = "district_id") String districtID) {
        return new Response<>(true, locationsService.findWardByDistrictID(districtID));
    }
}
