package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.PqmPrepResultEnum;
import com.gms.entity.constant.PqmPrepStatusEnum;
import com.gms.entity.constant.PqmPrepTreatmentEnum;
import com.gms.entity.constant.PqmPrepTypeEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepStageEntity;
import com.gms.entity.db.PqmPrepVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.PqmPrepService;
import com.gms.service.PqmPrepStageService;
import com.gms.service.PqmPrepVisitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author pdThang
 */
@RestController
public class PqmPrepController extends BaseController {

    @Autowired
    private PqmPrepService prepService;

    @Autowired
    private PqmPrepVisitService pqmPrepVisitService;

    @Autowired
    private PqmPrepStageService pqmPrepStageService;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
        if (siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).equals("")) {
            options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).split(",")));
        }

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }

        options.get("siteOpc").put("-1", "Cơ sở khác");

        options.put("type", new HashMap<String, String>());
        options.get("type").put("", "Tất cả");
        options.get("type").put(PqmPrepTypeEnum.MOI.getKey(), PqmPrepTypeEnum.MOI.getLabel());
        options.get("type").put(PqmPrepTypeEnum.CU.getKey(), PqmPrepTypeEnum.CU.getLabel());

        options.put("treatment", new HashMap<String, String>());
        options.get("treatment").put(PqmPrepTreatmentEnum.HANG_NGAY.getKey(), PqmPrepTreatmentEnum.HANG_NGAY.getLabel());
        options.get("treatment").put(PqmPrepTreatmentEnum.TINH_HUONG.getKey(), PqmPrepTreatmentEnum.TINH_HUONG.getLabel());

        options.put("result", new HashMap<String, String>());
        options.get("result").put(PqmPrepResultEnum.DUONG_TINH.getKey(), PqmPrepResultEnum.DUONG_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.AM_TINH.getKey(), PqmPrepResultEnum.AM_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.KHONG_RO.getKey(), PqmPrepResultEnum.KHONG_RO.getLabel());

        options.put("prep-status", new HashMap<String, String>());
        options.get("prep-status").put(PqmPrepStatusEnum.TIEP_TUC.getKey(), PqmPrepStatusEnum.TIEP_TUC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getKey(), PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getKey(), PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_KHAC.getKey(), PqmPrepStatusEnum.DUNG_DO_KHAC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getKey(), PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.MAT_DAU.getKey(), PqmPrepStatusEnum.MAT_DAU.getLabel());

        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        return options;
    }

    @RequestMapping(value = "/pqm-prep/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oid
    ) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap();
        PqmPrepEntity entity = prepService.findOne(Long.valueOf(oid));
        List<PqmPrepVisitEntity> visits = pqmPrepVisitService.findByPrepID(Long.valueOf(oid));

        List<PqmPrepStageEntity> stages = pqmPrepStageService.findByPrepID(Long.valueOf(oid));
        Map<String, String> mapStage = new HashMap<>();
        Map<String, String> mapStageType = new HashMap<>();
        if (stages != null) {
            for (PqmPrepStageEntity stage : stages) {
                String start = TextUtils.formatDate(stage.getStartTime(), FORMATDATE);
                String end = TextUtils.formatDate(stage.getEndTime(), FORMATDATE);

                String content = (start == null ? "" : start) + (end == null ? " - hiện tại" : "- " + end);

                mapStage.put(stage.getID().toString(), content);

                mapStageType.put(stage.getID().toString(), StringUtils.isEmpty(stage.getType()) ? "" : stage.getType().equals("1") ? "Khách hàng mới" : stage.getType().equals("2") ? "Khách hàng cũ quay lại điều trị" : "");

            }
        }

        data.put("item", entity);
        data.put("mapStage", mapStage);
        data.put("mapStageType", mapStageType);
        data.put("itemVisit", visits == null || visits.isEmpty() ? new ArrayList<>() : visits);
        data.put("options", getOptions());

        return new Response<>(true, data);
    }

}
