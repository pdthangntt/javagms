package com.gms.controller.backend;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vvthanh
 */
public abstract class HtcController extends BaseController {

    /**
     * Danh sách tham số từ thư viện
     *
     * @return
     * @auth vvThành
     */
    protected HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();

        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.FIXED_SERVICE);
        parameterTypes.add(ParameterEntity.ARV_CONSULTANT_EXCHANGE_RESULT);
        parameterTypes.add(ParameterEntity.ASANTE_INFECT_TEST);
        parameterTypes.add(ParameterEntity.PARTNER_INFO_PROVIDE_RESULT);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.REFERENT_SOURCE);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.CONFIRM_TEST_STATUS);
        parameterTypes.add(ParameterEntity.GSPH_STATUS);
        parameterTypes.add(ParameterEntity.DPLTMC_STATUS);
        parameterTypes.add(ParameterEntity.THERAPY_EXCHANGE_STATUS);
        parameterTypes.add(ParameterEntity.SAMPLE_QUALITY);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE);
        parameterTypes.add(ParameterEntity.MOST_RECENT_TEST);
        parameterTypes.add(ParameterEntity.INFO_COMPARE);
        parameterTypes.add(ParameterEntity.ALERT_TYPE);
        parameterTypes.add(ParameterEntity.SITE_PROJECT);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        // Modify elements in test object group 
        HashMap<String, String> testObjects = options.get(ParameterEntity.TEST_OBJECT_GROUP);
        testObjects.remove(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
        testObjects.put(TestObjectGroupEnum.PREGNANT_PERIOD.getKey(), TestObjectGroupEnum.PREGNANT_PERIOD.getLabel());
        testObjects.put(TestObjectGroupEnum.GIVING_BIRTH.getKey(), TestObjectGroupEnum.GIVING_BIRTH.getLabel());

        if (currentUser.getSite().getServiceIDs().contains(ServiceEnum.PMTCT.getKey())) {
//            options.get(ParameterEntity.SERVICE_TEST).put(ServiceEnum.PMTCT.getKey(), ServiceEnum.PMTCT.getLabel());
        }

        List<StaffEntity> staffModels = staffService.findBySite(getCurrentUser().getSite().getID());
        String staffType = "staffs";
        options.put(staffType, new HashMap<String, String>());
        options.get(staffType).put("", "Chọn nhân viên");
        for (StaffEntity staffModel : staffModels) {
            options.get(staffType).put(String.valueOf(staffModel.getID()), staffModel.getName());
        }

        // Trả lời đồng ý xét nghiệm
        String isAgreeTestKey = "isAgreeTest";
        options.put(isAgreeTestKey, new HashMap<String, String>());
        options.get(isAgreeTestKey).put("", "Chọn câu trả lời");
        options.get(isAgreeTestKey).put("false", "Không");
        options.get(isAgreeTestKey).put("true", "Có");

        String agreePreTest = "agreePreTest";
        options.put(agreePreTest, new HashMap<String, String>());
        options.get(agreePreTest).put("", "Chọn câu trả lời");
        options.get(agreePreTest).put(String.valueOf(BooleanEnum.TRUE.getKey()), "Có");
        options.get(agreePreTest).put(String.valueOf(BooleanEnum.FALSE.getKey()), "Không");

        String patientIdAuthen = "patientIdAuthen";
        options.put(patientIdAuthen, new HashMap<String, String>());
        options.get(patientIdAuthen).put("", "Chọn câu trả lời");
        options.get(patientIdAuthen).put("true", "Có");
        options.get(patientIdAuthen).put("false", "Không");

        //Trạng thái chuyển gửi từ cơ sở không chuyên
        String sendStatus = "sendStatus";
        options.put(sendStatus, new HashMap<String, String>());
        options.get(sendStatus).put("", "Lựa chọn");
        options.get(sendStatus).put("1", "Chưa chuyển");
        options.get(sendStatus).put("2", "Đã chuyển");
        options.get(sendStatus).put("3", "Đã tiếp nhận");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new LinkedHashMap<String, String>());
        options.get("siteOpc").put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }
        options.get("siteOpc").put("-1", "Cơ sở khác");

        options.put("site-confirm", new HashMap<String, String>());
        for (SiteEntity siteEntity : siteService.findAll()) {
            options.get("site-confirm").put(siteEntity.getID().toString(), siteEntity.getName());
        }

        return options;
    }

    /**
     * @auth vvThành
     * @param siteIDs
     * @return
     */
    public Map<String, String> getSite(Set<Long> siteIDs) {
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        options.put("", "Chọn tên cơ sở gửi mẫu");
        if (siteIDs != null && siteIDs.size() > 0) {
            List<SiteEntity> sites = siteService.findByIDs(siteIDs);
            for (SiteEntity site : sites) {
                options.put(String.valueOf(site.getID()), site.getName());
            }
        }
        return options;
    }
}
