package com.gms.controller.report;

import com.gms.entity.db.ParameterEntity;
import com.gms.service.ParameterService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import com.gms.entity.constant.ManageStatusEnum;

/**
 * Báo cáo người nhiễm phát hiện base controller
 *
 * @author vvthanh
 */
public abstract class PacDetectHivController extends BaseController {

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
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        String patientStatus = "patientStatus";
        options.put(patientStatus, new HashMap<String, String>());
        options.get(patientStatus).put("", "Tất cả");
        options.get(patientStatus).put("alive", "Còn sống");
        options.get(patientStatus).put("die", "Tử vong");

        String manageStatus = "manageStatus";
        options.put(manageStatus, new LinkedHashMap<String, String>());
        options.get(manageStatus).put("-1", "Tất cả");
        options.get(manageStatus).put(ManageStatusEnum.NN_PHAT_HIEN.getKey(), ManageStatusEnum.NN_PHAT_HIEN.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_CAN_RA_SOAT.getKey(), ManageStatusEnum.NN_CAN_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_DA_RA_SOAT.getKey(), ManageStatusEnum.NN_DA_RA_SOAT.getLabel());
        options.get(manageStatus).put(ManageStatusEnum.NN_QUAN_LY.getKey(), ManageStatusEnum.NN_QUAN_LY.getLabel());
        return options;
    }

    /**
     * Dùng để giới hạn những tỉnh
     *
     * @param addressFilter
     * @param pID
     * @return
     */
    protected boolean isProvince(String addressFilter, String pID) {
        if (!addressFilter.equals("hokhau")) {
            return true;
        }
        return pID.equals("82") || pID.equals("72");
    }
}
