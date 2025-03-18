package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.entity.db.SiteVnptFkEntity;
import com.gms.entity.form.opc_arv.VNPTConfigForm;
import com.gms.service.VNPTConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pdThang
 */
@RestController
public class VNPTConfigController extends BaseController {

    @Autowired
    private VNPTConfigService configService;

    @RequestMapping(value = "/vnpt-config/get.json", method = RequestMethod.GET)
    Response<?> actionGet(@RequestParam(name = "id") Long ID) {
        SiteVnptFkEntity model = configService.findOne(ID);
        if (model == null) {
            return new Response<>(false, "Không tìm thấy cấu hình");
        }
        VNPTConfigForm form = new VNPTConfigForm();
        form.setID(String.valueOf(model.getID()));
        form.setActive(model.isActive() == false ? "1" : "0");
        form.setVpntSiteID(model.getVpntSiteID());

        return new Response<>(true, form);
    }

}
