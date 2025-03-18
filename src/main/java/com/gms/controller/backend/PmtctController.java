package com.gms.controller.backend;

import com.gms.entity.db.ParameterEntity;
import com.gms.service.ParameterService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
public abstract class PmtctController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    /**
     * Danh sách tham số từ thư viện
     *
     * @return
     * @auth vvThành
     */
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }
}
