package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PacHivDetectSearch;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author NamAnh_HaUI
 */
@RestController
public class PacDetectHivController extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private SiteService siteService;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.SYSMPTOM);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        return options;
    }

    /**
     * Get data khi click vào bảng BC mới phát hiện theo giới tính
     *
     * @author TrangBN
     * @param search
     * @return
     * @throws java.text.ParseException
     */
    @RequestMapping(value = {"/pac-detecthiv-gender/search.json"}, method = RequestMethod.POST)
    public Response<?> actionGenderSearch(@RequestBody PacHivDetectSearch search) throws ParseException {

        Map<String, Object> data = new HashMap<>();
        List<PacPatientInfoEntity> models = pacPatientService.getAddress(pacPatientService.getMetaData(pacPatientService.findDetectHivGender(search)));

        data.put("options", getOptions());
        if (models == null || models.isEmpty()) {
            return new Response<>(false, "Không có dữ liệu", data);
        }

        data.put("models", models);
        return new Response<>(true, null, data);
    }

    @RequestMapping(value = {"/pac-detecthiv-object/search.json"}, method = RequestMethod.POST)
    public Response<?> actionObjectSearch(@RequestBody PacHivDetectSearch search) {

        Set<String> list = new HashSet<>();

        List<ParameterEntity> objectTest = parameterService.getTestObjectGroup();
        for (ParameterEntity entity : objectTest) {
            list.add(entity.getCode());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<PacPatientInfoEntity> models = null;

        if ("1".equals(search.getFlag())) {
            list.remove(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
            list.remove(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
            list.remove(TestObjectGroupEnum.MSM.getKey());
            list.remove(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
            list.remove(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
            list.remove(TestObjectGroupEnum.PREGNANT_PERIOD.getKey());
            list.remove(TestObjectGroupEnum.GIVING_BIRTH.getKey());
            list.remove(TestObjectGroupEnum.LAO.getKey());
        }
        if ("2".equals(search.getFlag())) {
            list.removeAll(list);
            list.add(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
            list.add(TestObjectGroupEnum.PREGNANT_PERIOD.getKey());
            list.add(TestObjectGroupEnum.GIVING_BIRTH.getKey());
        }
        models = pacPatientService.findPacDetectHivObject(
                StringUtils.isEmpty(search.getFromTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getFromTime()),
                StringUtils.isEmpty(search.getToTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getToTime()),
                "2".equals(search.getFlag()) ? null : search.getTestObject(),
                "1".equals(search.getFlag()) || "2".equals(search.getFlag()) ? list : search.getTestObjects(),
                "null".equals(search.getStatusTreatment()) ? "" : search.getStatusTreatment(),
                "null".equals(search.getStatusResident()) ? "" : search.getStatusResident(),
                "null".equals(search.getJob()) ? "" : search.getJob(),
                search.getAddressType(),
                search.getSearchTime(),
                search.getStatusAlive().contains(",") ? "" : search.getStatusAlive(),
                getCurrentUser().getSite().getProvinceID(),
                "null".equals(search.getOtherProvinceID()) ? "" : search.getOtherProvinceID(),
                "null".equals(search.getDistrictID()) || !search.getOtherProvinceID().isEmpty() ? "" : search.getDistrictID(),
                "null".equals(search.getWardID()) ? "" : search.getWardID(), search.getFlag());
        if (models != null) {
            pacPatientService.getAddress(models);
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }

    @RequestMapping(value = {"/pac-detecthiv-transmission/search.json"}, method = RequestMethod.POST)
    public Response<?> actionTransmissionSearch(@RequestBody PacHivDetectSearch search) {

        String fromTimeConvert = StringUtils.isEmpty(search.getFromTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getFromTime());
        String toTimeConvert = StringUtils.isEmpty(search.getToTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getToTime());
        
        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<PacPatientInfoEntity> models = null;

        models = pacPatientService.findPacDetectHivTransmission(
                fromTimeConvert,
                toTimeConvert,
                search.getModeOfTransmissionID() == null ? new HashSet<>(Arrays.asList("-1")) : new HashSet<>(Arrays.asList(search.getModeOfTransmissionID().split(","))),
                "null".equals(search.getStatusTreatment()) ? "" : search.getStatusTreatment(),
                "null".equals(search.getStatusResident()) ? "" : search.getStatusResident(),
                "null".equals(search.getJob()) ? "" : search.getJob(),
                "null".equals(search.getTestObject()) ? "" : search.getTestObject(),
                search.getAddressType(),
                search.getSearchTime(),
                search.getStatusAlive().contains(",") ? "" : search.getStatusAlive(),
                getCurrentUser().getSite().getProvinceID(),
                "null".equals(search.getOtherProvinceID()) ? "" : search.getOtherProvinceID(),
                "null".equals(search.getDistrictID()) || !search.getOtherProvinceID().isEmpty() ? "" : search.getDistrictID(),
                "null".equals(search.getWardID()) ? "" : search.getWardID(), search.getFlag());
        if (models != null) {
            pacPatientService.getAddress(models);
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }

    @RequestMapping(value = {"/pac-detecthiv-resident/search.json"}, method = RequestMethod.POST)
    public Response<?> actionResidentSearch(@RequestBody PacHivDetectSearch search) {
        String provinceID = getCurrentUser().getSite().getProvinceID();
        Set<String> list = new HashSet<>();
        List<ParameterEntity> treatment = parameterService.getStatusOfTreatment();
        for (ParameterEntity entity : treatment) {
            list.add(entity.getCode());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<PacPatientInfoEntity> models = null;
        
        List<Long> siteLocals = new ArrayList<>();
        for(SiteEntity model : siteService.findByProvinceID(provinceID)){
            if(model.getProvinceID().equals(provinceID)){
                siteLocals.add(model.getID());
            }
        }

        models = pacPatientService.findPacDetectHivResident(
                StringUtils.isEmpty(search.getFromTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getFromTime()),
                StringUtils.isEmpty(search.getToTime()) ? null : TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", search.getToTime()),
                "null".equals(search.getStatusTreatment()) ? "" : search.getStatusTreatment(),
                "null".equals(search.getStatusResident()) ? "" : search.getStatusResident(),
                "null".equals(search.getJob()) ? "" : search.getJob(),
                "null".equals(search.getTestObject()) ? "" : search.getTestObject(),
                search.getAddressType(),
                search.getSearchTime(),
                search.getStatusAlive().contains(",") ? "" : search.getStatusAlive(),
                getCurrentUser().getSite().getProvinceID(),
                "null".equals(search.getOtherProvinceID()) ? "" : search.getOtherProvinceID(),
                "null".equals(search.getDistrictID()) || !search.getOtherProvinceID().isEmpty() ? "" : search.getDistrictID(),
                "null".equals(search.getWardID()) ? "" : search.getWardID(),
                search.getFlag(), siteLocals);
        if (models != null) {
            pacPatientService.getAddress(models);
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }

    
    /**
     * Lấy dữ liệu detail click search
     * 
     * @param search
     * @return
     * @throws ParseException 
     */
    @RequestMapping(value = {"/pac-detecthiv-age/search.json"}, method = RequestMethod.POST)
    public Response<?> actionAgeSearch( @RequestBody PacHivDetectSearch search) throws ParseException {
        
        Map<String, Object> data = new HashMap<>();
        List<PacPatientInfoEntity> models = pacPatientService.getAddress(pacPatientService.getMetaData(pacPatientService.findDetectHivAge(search)));
        
        data.put("options", getOptions());
        if (models == null || models.isEmpty()) {
            return new Response<>(false,  "Không có dữ liệu", data);
        }
        
        data.put("models", models);
        return new Response<>(true,  null, data);
    }

}
