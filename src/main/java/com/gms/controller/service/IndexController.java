package com.gms.controller.service;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.StaffNotificationEntity;
import com.gms.service.StaffService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping("/ping.json")
    public Response<?> ping(Principal principal) {
        try {
            LoggedUser loginedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
            Map<String, Object> data = new HashMap<>();
            data.put("notifications", staffService.getNotifications(loginedUser.getUser().getID(), 1, 10));
            return new Response<>(true, data);
        } catch (Exception e) {
            return new Response<>(false, e.getMessage());
        }
    }

    /**
     * Đánh dấu đã đọc
     *
     * @author pdThang
     * @param principal
     * @param oid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/read.json", method = RequestMethod.GET)
    public Response<?> actionReaded(Principal principal, @RequestParam(name = "oid", defaultValue = "0") Long oid) throws Exception {
        LoggedUser loginedUser = null;
        List<StaffNotificationEntity> datas = new ArrayList<>();
        if (oid != 0) {
            StaffNotificationEntity model = staffService.findOneNotify(oid);
            model.setRead(true);
            datas.add(model);
        } else {
            loginedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
            List<StaffNotificationEntity> data = staffService.findAllByStaffID(loginedUser.getUser().getID());
            for (StaffNotificationEntity model : data) {
                model.setRead(true);
                datas.add(model);
            }
        }
        staffService.saveAllNotify(datas);
        return new Response<>(true, "");
    }

    @RequestMapping(value = "/clear-cache.json")
    public Response<?> clearCache(@RequestParam(name = "k", defaultValue = "", required = false) String key) {
        for (String name : cacheManager.getCacheNames()) {
            if (key.equals("") || key.startsWith(key)) {
                cacheManager.getCache(name).clear();
            }
        }
        return new Response<>(true, "");
    }
}
