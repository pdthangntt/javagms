package com.gms.controller.backend;

import com.gms.controller.WebController;
import com.gms.entity.db.AuthActionEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author vvthanh
 */
@RequestMapping("backend")
public abstract class BaseController extends WebController {

    /**
     * Convert danh sách quyền trên hệ thống
     *
     * @param entitys
     * @return
     */
    public Map<String, ArrayList<AuthActionEntity>> buildRoleTree(List<AuthActionEntity> entitys) {
        Map<String, ArrayList<AuthActionEntity>> result = new LinkedHashMap<>();
        for (AuthActionEntity entity : entitys) {
            String[] splitName = entity.getName().split("/");

            String key = "other";

            if (splitName.length >= 3) {
                try {
                    if (!splitName[1].contains("backend") && !splitName[1].contains("service")
                            && !splitName[1].contains("report") && !splitName[1].startsWith("/import")) {
                        key = splitName[1];
                    } else {
                        key = splitName[2].split("\\.")[0];
                    }
                } catch (Exception e) {
                    key = splitName[2];
                }
            } else if (splitName.length == 2) {
                try {
                    key = splitName[1].split("\\.")[0];
                } catch (Exception e) {
                    key = splitName[1];
                }
            }

            if (result.get(key) == null) {
                result.put(key, new ArrayList<AuthActionEntity>());
            }
            result.get(key).add(entity);
        }
        return result;
    }
}
