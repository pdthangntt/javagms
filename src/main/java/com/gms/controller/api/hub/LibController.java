package com.gms.controller.api.hub;

import com.gms.entity.bean.Response;
import com.gms.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_lib_api")
public class LibController extends BaseController {

    @Autowired
    private LocationsService locationService;

    @RequestMapping(value = "/hub/v1/lib", method = RequestMethod.GET)
    public Response<?> actionLib(@RequestParam("secret") String secret, @RequestParam("type") String type) {
        if (!checksum(secret)) {
            return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
        }
        switch (type) {
            case "province":
                return new Response<>(true, locationService.findAllProvince());
            case "district":
                return new Response<>(true, locationService.findAllDistrict());
            case "ward":
                return new Response<>(true, locationService.findAllWard());
            default:
                throw new AssertionError();
        }
    }
}
