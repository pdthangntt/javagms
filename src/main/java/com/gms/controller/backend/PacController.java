package com.gms.controller.backend;

import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.PacPatientStatusEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
public abstract class PacController extends BaseController {

    @Autowired
    private LocationsService locationsService;

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
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.TYPE_OF_PATIENT);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.SYSMPTOM);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.CLINICAL_STAGE);
        parameterTypes.add(ParameterEntity.AIDS_STATUS);
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.LINK_PQM);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        // Lọc lại danh sách Nguồn thông tin người nhiễm
        HashMap<String, String> service = options.get(ParameterEntity.SERVICE);
        if (service.isEmpty()) {
            return options;
        }

        HashMap<String, String> provinces = new HashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        HashMap<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("1", "Chưa rà soát");
        status.put("2", "Đã rà soát");
        options.put("status-review", status);

        HashMap<String, String> statusPatient = new HashMap<>();
        statusPatient.put("", "Tất cả");
        statusPatient.put("new", "Ca mới");
        statusPatient.put("old", "Ca cũ");
        options.put("statusPatient", statusPatient);

        HashMap<String, String> statusManage = new HashMap<>();
        statusManage.put("", "Tất cả");
        statusManage.put("1", "Chưa chuyển");
        statusManage.put("2", "Đã chuyển");
        options.put("statusManage", statusManage);

        options.put(ParameterEntity.SERVICE, new HashMap<String, String>());
        options.get(ParameterEntity.SERVICE).put("", "Chọn dịch vụ");
        options.get(ParameterEntity.SERVICE).put("100", service.get("100"));
        options.get(ParameterEntity.SERVICE).put("500", service.get("500"));
        options.get(ParameterEntity.SERVICE).put("103", service.get("103"));
        options.get(ParameterEntity.SERVICE).put("ttyt", service.get("ttyt"));
        options.get(ParameterEntity.SERVICE).put("tyt", service.get("tyt"));
        options.get(ParameterEntity.SERVICE).put("vaac", service.get("vaac"));

        addEnumOption(options, "status", PacPatientStatusEnum.values(), "Chọn trạng thái ngoại tỉnh");
        addEnumOption(options, "registrationTypes", RegistrationTypeEnum.values(), "Chọn loại đăng ký");
        addEnumOption(options, "endCases", ArvEndCaseEnum.values(), "Chọn lý do");
        //custom kết quả tlvr nhiễm mới
        HashMap<String, String> viralloads = new HashMap<>();
        for (Map.Entry<String, String> entry : options.get(ParameterEntity.VIRUS_LOAD).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            viralloads.put(key, value);
        }
        options.put(ParameterEntity.VIRUS_LOAD + "_custom", viralloads);
        options.get(ParameterEntity.VIRUS_LOAD + "_custom").remove("5");

        return options;
    }

    /**
     * @auth pdThang
     * @param siteIDs
     * @return
     */
    public Map<String, String> getSite(Set<Long> siteIDs) {
        Map<String, String> options = new HashMap<>();
        if (siteIDs != null && siteIDs.size() > 0) {
            List<SiteEntity> sites = siteService.findByIDs(siteIDs);
            for (SiteEntity site : sites) {
                options.put(String.valueOf(site.getID()), site.getName());
            }
        }
        return options;
    }

    /**
     * Danh sách ID cơ sở trực thuộc
     *
     * @auth vvThành
     * @param siteID
     * @return
     */
    public List<Long> getLeaf(Long siteID) {
        return siteService.getLeafID(siteID);
    }

    /**
     * Kiểm tra thuộc danh sách chưa rà soát
     *
     * @param entity
     * @param provinceID Tỉnh quản lý
     * @return
     */
    public boolean isNew(PacPatientInfoEntity entity, String provinceID) {
        return (entity.getAcceptTime() == null && entity.getReviewWardTime() == null && entity.getReviewProvinceTime() == null && !entity.getSourceServiceID().equals(ServiceEnum.OPC.getKey())) || (entity.getAcceptTime() != null && entity.getDetectProvinceID().equals(provinceID) && !entity.getSourceServiceID().equals(ServiceEnum.OPC.getKey()));
    }

    /**
     * Kiểm tra thuộc danh sách cần rà soát rà soát
     *
     * @param entity
     * @return
     */
    public boolean isNeedReview(PacPatientInfoEntity entity) {
        return entity.getAcceptTime() != null && entity.getReviewWardTime() == null && entity.getReviewProvinceTime() == null;
    }

    /**
     * Kiểm tra thuộc danh sách đã rà soát rà soát
     *
     * @param entity
     * @return
     */
    public boolean isReviewed(PacPatientInfoEntity entity) {
        return entity.getAcceptTime() != null && entity.getReviewWardTime() != null && entity.getReviewProvinceTime() == null;
    }

    /**
     * Kiểm tra thuộc danh sách quản lý
     *
     * @param entity
     * @return
     */
    public boolean isManaged(PacPatientInfoEntity entity) {
        return entity.getAcceptTime() != null && entity.getReviewWardTime() != null && entity.getReviewProvinceTime() != null;
    }

    /**
     * Kiểm tra thuộc danh sách biến động điều trị
     *
     * @param entity
     * @return
     */
    public boolean isOpc(PacPatientInfoEntity entity) {
        return entity.getSourceServiceID().equals(ServiceEnum.OPC.getKey());
    }
}
