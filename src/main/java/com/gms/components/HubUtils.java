package com.gms.components;

import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PqmApiLogEntity;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmShiDataEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.drug.ConvertDrugForm;
import com.gms.entity.form.drug.DataDrugForm;
import com.gms.entity.form.drug.DatasDrugForm;
import com.gms.entity.form.drug.MainDrugForm;
import com.gms.entity.form.drug.OptionalDataForm;
import com.gms.entity.form.hub.ConvertForm;
import com.gms.entity.form.hub.DataForm;
import com.gms.entity.form.hub.DatasForm;
import com.gms.entity.form.hub.MainForm;
import com.gms.entity.form.opc_arv.PqmEstimateForm;
import com.gms.entity.form.opc_arv.PqmForm;
import com.gms.entity.form.opc_arv.PqmPlanForm;
import com.gms.entity.form.opc_arv.PqmShiForm;
import com.gms.entity.form.opc_arv.PqmShiTable;
import com.gms.entity.form.opc_arv.PqmeLMISEForm;
import com.gms.entity.form.shi_hub.ConvertShiForm;
import com.gms.entity.form.shi_hub.DataShiForm;
import com.gms.entity.form.shi_hub.DatasShiForm;
import com.gms.entity.form.shi_hub.MainShiForm;
import com.gms.service.PqmApiService;
import com.gms.service.SiteService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.form.opc_arv.PqmDrugNewForm;

/**
 *
 * @author pdThang
 */
@Component
public class HubUtils {

//URL TEST API HUB 
//urlApi = "https://openhimcore-service.bakco.vn/api/gimasys-to-pqm-staging"

    private final String secret = "gimasys-qlnn";

    @Value("${app.pqm}")
    private String baseUrl; //Url pqm data đẩy đi

    @Value("${app.hub}")
    private String hubUrl; //Url hub.dulieuhiv.vn

    @Autowired
    private Gson gson;

    @Autowired
    private PqmApiService pqmApiService;
    @Autowired
    private SiteService siteService;

    private HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    protected String checksum(String... contents) {
        String key = "";
        for (String content : contents) {
            key += "/" + content;
        }
        //string: [secret]/[string1]/[string2]
        key = String.format("%s%s", secret, key);
        return DigestUtils.md5Hex(key).toUpperCase();
    }

    public Map<String, Object> sendToPqm(PqmForm form) throws Exception {
        MainForm main = new MainForm();
        main.setProvince_code(form.getProvincePQMcode());
        main.setMonth(String.valueOf(form.getMonth()));
        main.setYear(String.valueOf(form.getYear()));

        List<ConvertForm> listConverts = new ArrayList<>();
        ConvertForm convert;

        for (Map.Entry<String, List<PqmDataEntity>> entry : form.getMapDataHub().entrySet()) {
            String key = entry.getKey();
            List<PqmDataEntity> value = entry.getValue();
            for (PqmDataEntity p : value) {
                convert = new ConvertForm();
                convert.setIndicator_code(p.getIndicatorCode());
                convert.setDistrict_code(form.getDistrictPQM().get(p.getDistrictID()));
                convert.setSite_code(siteHubCode(p.getProvinceID()).getOrDefault(key, ""));

                convert.setValue(p.getQuantity().toString());
                convert.setSex(p.getSexID().equals("nam") ? "Male" : p.getSexID().equals("nu") ? "Female" : "N/A");
                convert.setAge_group(StringUtils.isEmpty(p.getAgeGroup()) ? "N/A" : p.getAgeGroup().equals("none") ? "N/A" : p.getAgeGroup());
                convert.setKey_population(StringUtils.isEmpty(p.getObjectGroupID()) ? "N/A" : mapObject().getOrDefault(p.getObjectGroupID(), "Others"));
                convert.setType("month");

                listConverts.add(convert);
            }
        }

        //Xử lý duy nhất
        Map<String, ConvertForm> mapConverts = new HashMap<>();
        for (ConvertForm item : listConverts) {
            if (StringUtils.isEmpty(item.getValue())) {
                continue;
            }
            String key = item.getIndicator_code() + item.getSite_code() + item.getSex() + item.getAge_group() + item.getKey_population();
            if (mapConverts.getOrDefault(key, null) == null) {
                mapConverts.put(key, item);
            } else {
                Long value = Long.valueOf(mapConverts.get(key).getValue()) + Long.valueOf(item.getValue());
                mapConverts.get(key).setValue(String.valueOf(value));
            }

        }

        List<DatasForm> listDatas = new ArrayList<>();
        DatasForm datas;
        DataForm data;

        for (Map.Entry<String, ConvertForm> entry : mapConverts.entrySet()) {
            String key = entry.getKey();
            ConvertForm value = entry.getValue();

            datas = new DatasForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("month");

            datas.setData(data);
            listDatas.add(datas);

        }

        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }

    private Map<String, String> mapObject() {
        Map<String, String> map = new HashMap<>();
        map.put("4", "PP");
        map.put("8", "PP");
        map.put("14", "PP");
        map.put("16", "PP");
        map.put("18", "PP");
        map.put("33", "PP");
        map.put("13", "TG");
        map.put("2", "FSW");
        map.put("1", "PWID");
        map.put("3", "MSM");
        map.put("7", "Others");
        return map;
    }

    private Map<String, String> siteHubCode(String province) {
        Map<String, String> map = new HashMap<>();
        for (SiteEntity site : siteService.findAll()) {
            if (StringUtils.isNotEmpty(site.getHubSiteCode())) {
                map.put(site.getID().toString(), site.getHubSiteCode());
            }
        }
        return map;
    }

    public PqmVctEntity visit(HtcVisitEntity visit) throws Exception {
        String body = Base64.getEncoder().encodeToString(gson.toJson(visit).getBytes());
        String url = String.format("%s/api/v1/pqm-vct.api", hubUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("checksum", checksum(String.valueOf(visit.getID()), String.valueOf(visit.getSiteID())));

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Map<String, Object>> response = rest.exchange(url, HttpMethod.POST, entity, responseType);
        Map<String, Object> result = response.getBody();
        boolean success = Boolean.valueOf(String.valueOf(result.get("success")));
        String message = String.valueOf(result.get("message"));
        String data = String.valueOf(result.get("data"));
        if (!success) {
            throw new Exception(message);
        }
        String json = new String(Base64.getDecoder().decode(data));
        PqmVctEntity vct = gson.fromJson(json, PqmVctEntity.class);
        return vct;
    }

    public Map<String, Object> sendShiToPqm(PqmShiForm form) throws Exception {
        MainShiForm main = new MainShiForm();
        main.setProvince_code(form.getProvincePQMcode());
        main.setMonth(String.valueOf(form.getMonth()));
        main.setYear(String.valueOf(form.getYear()));

        List<ConvertShiForm> listConverts = new ArrayList<>();
        ConvertShiForm convert;

        for (PqmShiDataEntity item : form.getApiDatas()) {
            //ART
            convert = new ConvertShiForm();
            convert.setIndicator_code("shi_art");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteID(), ""));

            convert.setValue(String.valueOf(item.getShi_art()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("month");
            if (!convert.getValue().equals("0")) {
                listConverts.add(convert);
            }

            //MMD
            convert = new ConvertShiForm();
            convert.setIndicator_code("shi_mmd");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteID(), ""));

            convert.setValue(String.valueOf(item.getShi_mmd()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("month");

            if (!convert.getValue().equals("0")) {
                listConverts.add(convert);
            }
        }

        List<DatasShiForm> listDatas = new ArrayList<>();
        DatasShiForm datas;
        DataShiForm data;

        for (ConvertShiForm value : listConverts) {
            datas = new DatasShiForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataShiForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("month");

            datas.setData(data);
            listDatas.add(datas);
        }
        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainShiForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }

    public Map<String, Object> sendDrugPlanToPqm(PqmEstimateForm form) throws Exception {
        MainDrugForm main = new MainDrugForm();
        main.setProvince_code(form.getProvincePQMcode());
        //đổi quý sang tháng mặc định
        main.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));

        main.setYear(String.valueOf(form.getYear()));

        List<ConvertDrugForm> listConverts = new ArrayList<>();
        ConvertDrugForm convert;

        for (PqmDrugEstimateDataEntity item : form.getItemAPI()) {
            //ART
            convert = new ConvertDrugForm();
            convert.setIndicator_code("drugs_plan");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getFacilityCode(), ""));

            convert.setValue(String.valueOf(item.getPlannedQuantity()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("quarter");

            convert.setValue2(String.valueOf(item.getDispensedQuantity()));
            convert.setValue_name("");
            convert.setDrug_name(item.getDrugNameLowercase());
            convert.setUnit_name(item.getDrugUomLowercase());

            if (!convert.getValue().equals("0") || !convert.getValue2().equals("0")) {
                listConverts.add(convert);
            }
        }

        List<DatasDrugForm> listDatas = new ArrayList<>();
        DatasDrugForm datas;
        DataDrugForm data;
        OptionalDataForm optionalDataForm;

        for (ConvertDrugForm value : listConverts) {
            datas = new DatasDrugForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataDrugForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("quarter");

            optionalDataForm = new OptionalDataForm();
            optionalDataForm.setValue(value.getValue2());
            optionalDataForm.setDrug_name(value.getDrug_name());
            optionalDataForm.setUnit_name(value.getUnit_name());
            optionalDataForm.setValue_name("denominator");
            optionalDataForm.setData_source("HMED");

            datas.setData(data);
            datas.setOptional_data(optionalDataForm);

            listDatas.add(datas);
        }
        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainDrugForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }

    public Map<String, Object> sendDrugPlanToPqmELMIS(PqmeLMISEForm form) throws Exception {
        MainDrugForm main = new MainDrugForm();
        main.setProvince_code(form.getProvincePQMcode());
        //đổi quý sang tháng mặc định
        main.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));

        main.setYear(String.valueOf(form.getYear()));

        List<ConvertDrugForm> listConverts = new ArrayList<>();
        ConvertDrugForm convert;

        for (PqmDrugeLMISEDataEntity item : form.getItemAPI()) {
            //ART
            convert = new ConvertDrugForm();
            convert.setIndicator_code("drugs_plan");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteID() + "", ""));

            convert.setValue(String.valueOf(item.getSuDung()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("quarter");

            convert.setValue2(String.valueOf(item.getTrongKy()));
            convert.setValue_name("");
            convert.setDrug_name(item.getDrugName());
            convert.setUnit_name("N/A");

            if (!convert.getValue().equals("0") || !convert.getValue2().equals("0")) {
                listConverts.add(convert);
            }
        }

        List<DatasDrugForm> listDatas = new ArrayList<>();
        DatasDrugForm datas;
        DataDrugForm data;
        OptionalDataForm optionalDataForm;

        for (ConvertDrugForm value : listConverts) {
            datas = new DatasDrugForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataDrugForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("quarter");

            optionalDataForm = new OptionalDataForm();
            optionalDataForm.setValue(value.getValue2());
            optionalDataForm.setDrug_name(value.getDrug_name());
            optionalDataForm.setUnit_name(value.getUnit_name());
            optionalDataForm.setValue_name("denominator");
            optionalDataForm.setData_source("ELMis");

            datas.setData(data);
            datas.setOptional_data(optionalDataForm);

            listDatas.add(datas);
        }
        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainDrugForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }

    public Map<String, Object> sendDrugPlanToPqmELMIS2(PqmeLMISEForm form) throws Exception {
        MainDrugForm main = new MainDrugForm();
        main.setProvince_code(form.getProvincePQMcode());
        //đổi quý sang tháng mặc định
        main.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));

        main.setYear(String.valueOf(form.getYear()));

        List<ConvertDrugForm> listConverts = new ArrayList<>();
        ConvertDrugForm convert;

        for (PqmDrugeLMISEDataEntity item : form.getItemAPI()) {
            //ART
            convert = new ConvertDrugForm();
            convert.setIndicator_code("drugs_safety_mos");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteID() + "", ""));

            convert.setValue(String.valueOf(item.getTon()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("quarter");

            convert.setValue2(String.valueOf(item.getThang()));
            convert.setValue_name("");
            convert.setDrug_name(item.getDrugName());
            convert.setUnit_name("N/A");

            if (!convert.getValue().equals("0") || !convert.getValue2().equals("0.0")) {
                listConverts.add(convert);
            }
        }

        List<DatasDrugForm> listDatas = new ArrayList<>();
        DatasDrugForm datas;
        DataDrugForm data;
        OptionalDataForm optionalDataForm;

        for (ConvertDrugForm value : listConverts) {
            datas = new DatasDrugForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataDrugForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("quarter");

            optionalDataForm = new OptionalDataForm();
            optionalDataForm.setValue(value.getValue2());
            optionalDataForm.setDrug_name(value.getDrug_name());
            optionalDataForm.setUnit_name(value.getUnit_name());
            optionalDataForm.setValue_name("denominator");
            optionalDataForm.setData_source("ELMis");

            datas.setData(data);
            datas.setOptional_data(optionalDataForm);

            listDatas.add(datas);
        }
        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainDrugForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth() == 1 ? "1" : form.getMonth() == 2 ? "4" : form.getMonth() == 3 ? "7" : form.getMonth() == 4 ? "10" : "1"));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }

    public Map<String, Object> sendDrugPlan2ToPqm(PqmPlanForm form) throws Exception {
        MainDrugForm main = new MainDrugForm();
        main.setProvince_code(form.getProvincePQMcode());
        main.setMonth(String.valueOf(form.getMonth()));
        main.setYear(String.valueOf(form.getYear()));

        List<ConvertDrugForm> listConverts = new ArrayList<>();
        ConvertDrugForm convert;

        for (PqmDrugPlanDataEntity item : form.getItems()) {
            //ART
            convert = new ConvertDrugForm();
            convert.setIndicator_code("drugs_safety_mos");
            convert.setDistrict_code(form.getDistrictPQMcode());
            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteCode(), ""));

            convert.setValue(String.valueOf(item.getTotalDrugBacklog()));
            convert.setSex("N/A");
            convert.setAge_group("N/A");
            convert.setKey_population("N/A");
            convert.setType("month");

            convert.setValue2(String.valueOf(item.getTotalDrugPlan2Month()));
            convert.setValue_name("");
            convert.setDrug_name(StringUtils.isEmpty(item.getDrugName()) ? "" : item.getDrugName().toLowerCase());
            convert.setUnit_name(StringUtils.isEmpty(item.getUnit()) ? "" : item.getUnit().toLowerCase());

            if (!convert.getValue().equals("0") || !convert.getValue2().equals("0")) {
                listConverts.add(convert);
            }
        }

        List<DatasDrugForm> listDatas = new ArrayList<>();
        DatasDrugForm datas;
        DataDrugForm data;
        OptionalDataForm optionalDataForm;

        for (ConvertDrugForm value : listConverts) {
            datas = new DatasDrugForm();
            datas.setIndicator_code(value.getIndicator_code());
            datas.setDistrict_code(value.getDistrict_code());
            datas.setSite_code(value.getSite_code());

            data = new DataDrugForm();
            data.setValue(value.getValue());
            data.setSex(value.getSex());
            data.setAge_group(value.getAge_group());
            data.setKey_population(value.getKey_population());
            data.setType("month");

            optionalDataForm = new OptionalDataForm();
            optionalDataForm.setValue(value.getValue2());
            optionalDataForm.setDrug_name(value.getDrug_name());
            optionalDataForm.setUnit_name(value.getUnit_name());
            optionalDataForm.setValue_name("denominator");
            optionalDataForm.setData_source("HMED");

            datas.setData(data);
            datas.setOptional_data(optionalDataForm);

            listDatas.add(datas);
        }
        main.setDatas(listDatas);

        String body = gson.toJson(main);

        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        System.out.println("body: " + body);
        HttpEntity<MainDrugForm> entity = new HttpEntity<>(main, headers);
        RestTemplate rest = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
        messageConverters.add(converter);
        rest.setMessageConverters(messageConverters);

        //url 3 tỉnh
        String province = form.getCurrentProvinceID();
        String urlApi = "";
        switch (province) {
            case "72":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
                break;
            case "75":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
                break;
            case "82":
                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
                break;
            default:
                break;
        }

        ResponseEntity<Map<String, Object>> response = null;
        ResponseEntity<String> responseEntity = null;

        try {
            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        }

        String text = "";
        if (responseEntity != null) {
            text = responseEntity.getBody();
            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(false);
            model.setBody(body);
            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            pqmApiService.save(model);
            return erresponse;

        } else {
            Map<String, Object> result = response.getBody();
            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));

            PqmApiLogEntity model = new PqmApiLogEntity();
            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
            model.setError(result.get("error") == null ? "" : result.get("error").toString());
            model.setProvince(form.getCurrentProvinceID());
            model.setMonth(String.valueOf(form.getMonth()));
            model.setYear(String.valueOf(form.getYear()));
            model.setCreateAT(new Date());
            model.setContent(body);

            if (!succeed) {
                model.setErrorIms(gson.toJson(result));
                pqmApiService.save(model);
                throw new Exception(gson.toJson(result));
            }
            pqmApiService.save(model);
            return result;
        }

    }
    
//    public Map<String, Object> sendDrugNewToPqm(PqmDrugNewForm form) throws Exception {
//        MainDrugForm main = new MainDrugForm();
//        main.setProvince_code(form.getProvincePQMcode());
//        main.setMonth(String.valueOf(form.getMonth()));
//        main.setYear(String.valueOf(form.getYear()));
//
//        List<ConvertDrugForm> listConverts = new ArrayList<>();
//        ConvertDrugForm convert;
//
//        for (PqmDrugNewDataEntity item : form.getItemAPI()) {
//            //ART
//            convert = new ConvertDrugForm();
//            convert.setIndicator_code("drug_efficiency");
//            convert.setDistrict_code(form.getDistrictPQMcode());
//            convert.setSite_code(form.getSitePQMcodes().getOrDefault(item.getSiteID(), ""));
//
//            convert.setSex("N/A");
//            convert.setAge_group("N/A");
//            convert.setKey_population("N/A");
//            convert.setType("month");
//
//            convert.setValue2(String.valueOf(item.getTotalDrugPlan2Month()));
//            convert.setValue_name2("ngon");
//            convert.setDrug_name(StringUtils.isEmpty(item.getDrugName()) ? "" : item.getDrugName().toLowerCase());
//            convert.setUnit_name(StringUtils.isEmpty(item.getUnit()) ? "" : item.getUnit().toLowerCase());
//
//            if (!convert.getValue().equals("0") || !convert.getValue2().equals("0")) {
//                listConverts.add(convert);
//            }
//        }
//
//        List<DatasDrugForm> listDatas = new ArrayList<>();
//        DatasDrugForm datas;
//        DataDrugForm data;
//        OptionalDataForm optionalDataForm;
//
//        for (ConvertDrugForm value : listConverts) {
//            datas = new DatasDrugForm();
//            datas.setIndicator_code(value.getIndicator_code());
//            datas.setDistrict_code(value.getDistrict_code());
//            datas.setSite_code(value.getSite_code());
//
//            data = new DataDrugForm();
//            data.setValue(value.getValue());
//            data.setSex(value.getSex());
//            data.setAge_group(value.getAge_group());
//            data.setKey_population(value.getKey_population());
//            data.setType("month");
//
//            optionalDataForm = new OptionalDataForm();
//            optionalDataForm.setValue(value.getValue2());
//            optionalDataForm.setDrug_name(value.getDrug_name());
//            optionalDataForm.setUnit_name(value.getUnit_name());
//            optionalDataForm.setValue_name("denominator");
//            optionalDataForm.setData_source("HMED");
//
//            datas.setData(data);
//            datas.setOptional_data(optionalDataForm);
//
//            listDatas.add(datas);
//        }
//        main.setDatas(listDatas);
//
//        String body = gson.toJson(main);
//
//        HttpHeaders headers = createHeaders("gimasys_user", "123456a@");
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
//        };
//
//        System.out.println("body: " + body);
//        HttpEntity<MainDrugForm> entity = new HttpEntity<>(main, headers);
//        RestTemplate rest = new RestTemplate();
//
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
//        messageConverters.add(converter);
//        rest.setMessageConverters(messageConverters);
//
//        //url 3 tỉnh
//        String province = form.getCurrentProvinceID();
//        String urlApi = "";
//        switch (province) {
//            case "72":
//                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tay-ninh";
//                break;
//            case "75":
//                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-dong-nai";
//                break;
//            case "82":
//                urlApi = "https://openhimcore-service.quanlyhiv.vn/api/gimasys-to-pqm-tien-giang";
//                break;
//            default:
//                break;
//        }
//
//        ResponseEntity<Map<String, Object>> response = null;
//        ResponseEntity<String> responseEntity = null;
//
//        try {
//            response = rest.exchange(urlApi, HttpMethod.POST, entity, responseType);
//        } catch (HttpClientErrorException e) {
//            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
//        }
//
//        String text = "";
//        if (responseEntity != null) {
//            text = responseEntity.getBody();
//            Map<String, Object> erresponse = new ObjectMapper().readValue(text, HashMap.class);
//
//            PqmApiLogEntity model = new PqmApiLogEntity();
//            model.setSucceed(false);
//            model.setBody(body);
//            model.setError(erresponse.get("error") == null ? "" : erresponse.get("error").toString());
//            model.setProvince(form.getCurrentProvinceID());
//            model.setMonth(String.valueOf(form.getMonth()));
//            model.setYear(String.valueOf(form.getYear()));
//            model.setCreateAT(new Date());
//            model.setContent(body);
//
//            pqmApiService.save(model);
//            return erresponse;
//
//        } else {
//            Map<String, Object> result = response.getBody();
//            boolean succeed = Boolean.valueOf(String.valueOf(result.get("succeed")));
//
//            PqmApiLogEntity model = new PqmApiLogEntity();
//            model.setSucceed(result.get("succeed") == null ? false : Boolean.valueOf(result.get("succeed").toString()));
//            model.setBody(result.get("data") == null ? "" : result.get("data").toString());
//            model.setError(result.get("error") == null ? "" : result.get("error").toString());
//            model.setProvince(form.getCurrentProvinceID());
//            model.setMonth(String.valueOf(form.getMonth()));
//            model.setYear(String.valueOf(form.getYear()));
//            model.setCreateAT(new Date());
//            model.setContent(body);
//
//            if (!succeed) {
//                model.setErrorIms(gson.toJson(result));
//                pqmApiService.save(model);
//                throw new Exception(gson.toJson(result));
//            }
//            pqmApiService.save(model);
//            return result;
//        }
//
//    }

}
