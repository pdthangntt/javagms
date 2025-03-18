package com.gms.controller.service;

import com.gms.components.QLNNUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.service.PqmVctService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class PqmVctController extends BaseController {

    @Autowired
    private PqmVctService pqmVctService;
    @Autowired
    private QLNNUtils qlnnUtils;

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
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
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

        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        return options;
    }

    @RequestMapping(value = "/pqm-vct/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oid) {
        Map<String, Object> data = new HashMap();
        PqmVctEntity entity = pqmVctService.findOne(Long.valueOf(oid));

        data.put("item", entity);
        data.put("options", getOptions());
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/pqm-vct/scan.json", method = RequestMethod.GET)
    public Response<?> actionScan(@RequestParam(name = "oid") String oid) {
        PqmVctEntity entity = pqmVctService.findOne(Long.valueOf(oid));
        if (entity == null) {
            return new Response<>(false, "Không tìm thấy thông tin");
        }
        PacPatientInfoEntity info = null;
        try {
            info = qlnnUtils.getByConfirm(entity.getConfirmTestNo(), entity.getIdentityCard(), getCurrentUser().getSite().getProvinceID());
        } catch (Exception e) {
            return new Response<>(false, "Không tìm thấy thông tin người nhiễm");
        }

        Map<String, Object> data = new HashMap();
        data.put("exchangeTime", entity.getResultsTime() == null ? "" : TextUtils.formatDate(entity.getResultsTime(), FORMATDATE));
        data.put("earlyHivDate", info.getEarlyHivTime() == null ? "" : TextUtils.formatDate(info.getEarlyHivTime(), FORMATDATE));
        data.put("registerTherapyTime", info.getStartTreatmentTime() == null ? "" : TextUtils.formatDate(info.getStartTreatmentTime(), FORMATDATE));
        data.put("registeredTherapySite", getOptions().get(ParameterEntity.TREATMENT_FACILITY).getOrDefault(info.getSiteTreatmentFacilityID() + "", null));
        data.put("therapyNo", info.getOpcCode());
        data.put("earlyDiagnose", info.getEarlyDiagnose());

        return new Response<>(true, data);
    }

}
