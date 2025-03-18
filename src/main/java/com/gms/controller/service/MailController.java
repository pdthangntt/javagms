package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.service.EmailService;
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
public class MailController extends BaseController {

    @Autowired
    private EmailService emailServices;

    /**
     * popup email
     *
     * @author pdThang
     * @param oid
     * @return
     */
    @RequestMapping(value = "/mail/detail.json", method = RequestMethod.GET)
    public Response<?> actionDetal(@RequestParam(name = "oid") Long oid) {
        return new Response<>(true, emailServices.findOne(oid));
    }

}
