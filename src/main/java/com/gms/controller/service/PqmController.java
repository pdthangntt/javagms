package com.gms.controller.service;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.PqmProportionEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmProportionForm;
import com.gms.entity.form.PqmProportionRow;
import com.gms.entity.form.PqmSiteForm;
import com.gms.entity.form.PqmSiteRow;
import com.gms.entity.form.opc_arv.PqmTableDataForm;
import com.gms.entity.form.opc_arv.PqmUpdateDataForm;
import com.gms.service.PqmDataService;
import com.gms.service.PqmProportionService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 *
 * @author pdThang
 */
@RestController
public class PqmController extends BaseController {

    @Autowired
    private PqmDataService pqmDataService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PqmProportionService proportionService;

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

        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        return options;
    }

    @RequestMapping(value = "/pqm-report/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oid,
            @RequestParam(name = "indicator") String indicator,
            @RequestParam(name = "month") String month,
            @RequestParam(name = "year") String year
    ) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap();
        List<PqmDataEntity> entitys = pqmDataService.findByIndicator(Integer.valueOf(month), Integer.valueOf(year), indicator, currentUser.getSite().getID());
        List<PqmTableDataForm> items = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        Map<String, String> labels = new HashMap<>();
        if (entitys != null) {
            PqmTableDataForm row;
            for (PqmDataEntity entity : entitys) {
                row = new PqmTableDataForm();
                row.setKey(entity.getObjectGroupID() + setAgeGroups().getOrDefault(entity.getAgeGroup(), "") + entity.getSexID());
                row.setQuantity(entity.getQuantity().toString());
                items.add(row);
                labels.put(row.getKey(), String.format("Nhóm đối tượng: %s, Nhóm tuổi: %s, Giới tính: %s", objectGroups().get(entity.getObjectGroupID()), entity.getAgeGroup(), entity.getSexID().equals("nam") ? "Nam" : "Nữ"));
                keys.add(row.getKey());
            }
            PqmTableDataForm row1;
            PqmTableDataForm row2;
            for (Map.Entry<String, String> entry : objectGroups().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                for (String ageGroup : ageGroups()) {
                    row1 = new PqmTableDataForm();
                    row2 = new PqmTableDataForm();
                    row1.setKey(key + ageGroup + "nam");
                    row1.setQuantity("0");
                    if (!keys.contains(row1.getKey())) {
                        items.add(row1);
                    }

                    labels.put(row1.getKey(), String.format("Nhóm đối tượng: %s, Nhóm tuổi: %s, Giới tính: %s", objectGroups().get(key), ageGroup, "Nam"));
                    row2.setKey(key + ageGroup + "nu");
                    row2.setQuantity("0");
                    if (!keys.contains(row2.getKey())) {
                        items.add(row2);
                    }
                    labels.put(row2.getKey(), String.format("Nhóm đối tượng: %s, Nhóm tuổi: %s, Giới tính: %s", objectGroups().get(key), ageGroup, "Nũ"));
                }
            }

        } else {
            PqmTableDataForm row1;
            PqmTableDataForm row2;
            for (Map.Entry<String, String> entry : objectGroups().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                for (String ageGroup : ageGroups()) {
                    row1 = new PqmTableDataForm();
                    row2 = new PqmTableDataForm();
                    row1.setKey(key + ageGroup + "nam");
                    row1.setQuantity("0");
                    items.add(row1);
                    labels.put(row1.getKey(), String.format("Nhóm đối tượng: %s, Nhóm tuổi: %s, Giới tính: %s", objectGroups().get(key), mapAgeGroups().get(ageGroup), "Nam"));
                    row2.setKey(key + ageGroup + "nu");
                    row2.setQuantity("0");
                    items.add(row2);
                    labels.put(row2.getKey(), String.format("Nhóm đối tượng: %s, Nhóm tuổi: %s, Giới tính: %s", objectGroups().get(key), mapAgeGroups().get(ageGroup), "Nữ"));
                }
            }
        }

        PqmUpdateDataForm formUpdate = new PqmUpdateDataForm();

        data.put("items", items);
        data.put("labels", labels);
        data.put("formUpdate", formUpdate);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/pqm-report/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdateResult(
            //            @RequestParam(name = "oid") String oid,
            @RequestBody PqmUpdateDataForm item) {
        LoggedUser currentUser = getCurrentUser();
        try {
            List<PqmDataEntity> list = new ArrayList<>();
            int month = 0;
            int year = 0;
            String indicator = "";
            for (PqmTableDataForm form : item.getItems()) {
                PqmDataEntity entity = pqmDataService.findOne(Integer.valueOf(form.getMonth()), Integer.valueOf(form.getYear()), form.getIndicator(), currentUser.getSite().getID(), form.getGender(), mapAgeGroups().get(form.getAge()), form.getObject());

                if (entity == null) {
                    entity = new PqmDataEntity();
                    entity.setCreateAT(new Date());
                }
                entity.setMonth(Integer.valueOf(form.getMonth()));
                entity.setYear(Integer.valueOf(form.getYear()));
                entity.setIndicatorCode(form.getIndicator());;
                entity.setSiteID(currentUser.getSite().getID());
                entity.setSexID(form.getGender());
                entity.setAgeGroup(mapAgeGroups().get(form.getAge()));
                entity.setObjectGroupID(form.getObject());
                entity.setQuantity(StringUtils.isEmpty(form.getQuantity()) ? Long.valueOf("0") : Long.valueOf(form.getQuantity()));
                entity.setStatus(0);
                entity.setSendDate(null);

//                if (!entity.getQuantity().equals(Long.valueOf("0"))) {
                list.add(entity);
                month = entity.getMonth();
                year = entity.getYear();
                indicator = entity.getIndicatorCode();
//                }
            }

            List<PqmDataEntity> entitys = pqmDataService.findBySiteID(month, year, currentUser.getSite().getID());
            if (entitys != null) {
                for (PqmDataEntity entity : entitys) {
                    if (entity.getStatus() == 2 || entity.getStatus() == 3) {
                        return new Response<>(false, "Cơ sở đã được tỉnh tổng hợp, không thể nhập.");
                    }
                }
            }

            pqmDataService.saveAll(list);
            List<PqmDataEntity> list2 = new ArrayList<>();
            Set<Long> currentSite = new HashSet<>();
            currentSite.add(currentUser.getSite().getID());
            List<PqmDataEntity> listData = pqmDataService.findByIndicator(month, year, currentSite);
            if (listData != null) {
                for (PqmDataEntity e : listData) {
                    e.setStatus(0);
                    list2.add(e);
                }
                pqmDataService.saveAll(list2);
            }
            return new Response<>(true, "Thêm thông tin thành công", item);
        } catch (Exception e) {
            return new Response<>(false, "Lỗi cập nhật thông tin.");
        }
    }

    private LinkedHashMap<String, String> objectGroups() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("1", "Nghiện chích ma túy (NCMT)");
        map.put("2", "Người hành nghề mại dâm");
        map.put("5", "Phụ nữ mang thai");
        map.put("9", "Người bán máu /hiến máu /cho máu");
        map.put("10", "Bệnh nhân nghi ngờ AIDS");
        map.put("6", "Bệnh nhân Lao");
        map.put("25", "Bệnh nhân mắc các nhiễm trùng LTQĐTD");
        map.put("12", "Thanh niên khám tuyển nghĩa vụ quân sự");
        map.put("3", "Nam quan hệ tình dục với nam (MSM)");
        map.put("7", "Các đối tượng khác");

        return map;
    }

    private LinkedList<String> ageGroups() {
        LinkedList< String> list = new LinkedList<>();
        list.add("m10");
        list.add("m15");
        list.add("m20");
        list.add("m25");
        list.add("m30");
        list.add("m35");
        list.add("m40");
        list.add("m45");
        list.add("m50");

        return list;
    }

    private Map<String, String> mapAgeGroups() {
        Map<String, String> map = new HashMap<>();
        map.put("m10", "10-14");
        map.put("m15", "15-19");
        map.put("m20", "20-24");
        map.put("m25", "25-29");
        map.put("m30", "30-34");
        map.put("m35", "35-39");
        map.put("m40", "40-44");
        map.put("m45", "45-49");
        map.put("m50", "50+");

        return map;
    }

    private Map<String, String> setAgeGroups() {
        Map<String, String> map = new HashMap<>();
        map.put("10-14", "m10");
        map.put("15-19", "m15");
        map.put("20-24", "m20");
        map.put("25-29", "m25");
        map.put("30-34", "m30");
        map.put("35-39", "m35");
        map.put("40-44", "m40");
        map.put("45-49", "m45");
        map.put("50+", "m50");

        return map;
    }

    @RequestMapping(value = "/pqm-site/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdateSite(
            @RequestBody PqmSiteForm form) {
        LoggedUser currentUser = getCurrentUser();
        try {

            //check unique
            List<String> siteCode = new ArrayList<>();
            List<String> siteHub = new ArrayList<>();
            List<String> siteIDs = new ArrayList<>();
            for (PqmSiteRow item : form.getItems()) {
                if (StringUtils.isNotEmpty(item.getPqmSiteCode())) {
                    siteCode.add(item.getPqmSiteCode());
                }
                if (StringUtils.isNotEmpty(item.getHubSiteCode())) {
                    siteHub.add(item.getHubSiteCode());
                }
                siteIDs.add(item.getID().toString());
            }
            Set<String> doulicate = findDuplicates(siteCode);
            Set<String> doulicateHub = findDuplicates(siteHub);
            Set<String> doulicateID = new HashSet<>();
            Set<String> doulicateHubID = new HashSet<>();
            for (PqmSiteRow item : form.getItems()) {
                for (String string : doulicate) {
                    if (StringUtils.isNotEmpty(item.getPqmSiteCode()) && item.getPqmSiteCode().contains(string)) {
                        doulicateID.add(item.getID().toString());
                    }
                }
                for (String string : doulicateHub) {
                    if (StringUtils.isNotEmpty(item.getHubSiteCode()) && item.getHubSiteCode().contains(string)) {
                        doulicateHubID.add(item.getID().toString());
                    }
                }
            }
            if (!doulicateID.isEmpty() || !doulicateHubID.isEmpty()) {
                Map<String, Object> data = new HashMap();
                data.put("doulicateID", doulicateID);
                data.put("doulicateHubID", doulicateHubID);
                data.put("siteIDs", siteIDs);
                return new Response<>(false, "Lỗi cập nhật thông tin.", data);
            }

            Set<String> services = new HashSet<>();
            Set<String> provinces = new HashSet<>();
            services.add(ServiceEnum.OPC.getKey());
            services.add(ServiceEnum.PREP.getKey());
            services.add(ServiceEnum.HTC.getKey());
            services.add(ServiceEnum.HTC_CONFIRM.getKey());
            provinces.add(currentUser.getSite().getProvinceID());

            List<SiteEntity> sites = siteService.findByServiceAndProvince(services, provinces);
            List<SiteEntity> siteOK = new ArrayList<>();

            for (SiteEntity site : sites) {
                for (PqmSiteRow item : form.getItems()) {
                    if (site.getID().equals(item.getID())) {
                        site.setHub(StringUtils.isEmpty(item.getHub()) ? null : item.getHub());
                        site.setHubSiteCode(StringUtils.isEmpty(item.getHubSiteCode()) ? null : item.getHubSiteCode());
                        site.setPqmSiteCode(StringUtils.isEmpty(item.getPqmSiteCode()) ? null : item.getPqmSiteCode());

                        site.setElogSiteCode(StringUtils.isEmpty(item.getElogSiteCode()) ? null : item.getElogSiteCode());
                        site.setPrepSiteCode(StringUtils.isEmpty(item.getPrepSiteCode()) ? null : item.getPrepSiteCode());
                        site.setArvSiteCode(StringUtils.isEmpty(item.getArvSiteCode()) ? null : item.getArvSiteCode());
                        site.setHmedSiteCode(StringUtils.isEmpty(item.getHmedSiteCode()) ? null : item.getHmedSiteCode());
                        siteOK.add(site);
                    }
                }

            }
            for (SiteEntity siteEntity : siteOK) {
                SiteEntity entity = siteService.findOne(siteEntity.getID(), true, true);
                entity.setHub(StringUtils.isEmpty(siteEntity.getHub()) ? null : siteEntity.getHub());
                entity.setHubSiteCode(StringUtils.isEmpty(siteEntity.getHubSiteCode()) ? null : siteEntity.getHubSiteCode());
                entity.setPqmSiteCode(StringUtils.isEmpty(siteEntity.getPqmSiteCode()) ? null : siteEntity.getPqmSiteCode());

                entity.setElogSiteCode(StringUtils.isEmpty(siteEntity.getElogSiteCode()) ? null : siteEntity.getElogSiteCode());
                entity.setPrepSiteCode(StringUtils.isEmpty(siteEntity.getPrepSiteCode()) ? null : siteEntity.getPrepSiteCode());
                entity.setArvSiteCode(StringUtils.isEmpty(siteEntity.getArvSiteCode()) ? null : siteEntity.getArvSiteCode());
                entity.setHmedSiteCode(StringUtils.isEmpty(siteEntity.getHmedSiteCode()) ? null : siteEntity.getHmedSiteCode());
                siteService.save(entity);
            }

            return new Response<>(true, "Lưu thông tin thành công", form);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi cập nhật thông tin.");
        }
    }

    @RequestMapping(value = "/pqm-proportion/update.json", method = RequestMethod.POST)
    public Response<?> actionUpdateProportion(
            @RequestBody PqmProportionForm form) {
        LoggedUser currentUser = getCurrentUser();
        try {

            Set<String> provinces = new HashSet<>();
//            provinces.add(currentUser.getSite().getProvinceID());
//            List<PqmProportionEntity> proportions = proportionService.findByProvince(provinces);
            for (PqmProportionRow row : form.getItems()) {
                PqmProportionEntity entity = proportionService.findOne(row.getID());
                entity.setValue(StringUtils.isEmpty(row.getValue()) ? null : row.getValue());
                proportionService.save(entity);
            }

            return new Response<>(true, "Lưu thông tin thành công", form);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi cập nhật thông tin.");
        }
    }

    private Set<String> findDuplicates(List<String> listContainingDuplicates) {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();

        for (String yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }

}
