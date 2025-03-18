package com.gms.components;

import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.google.gson.Gson;
import java.util.Base64;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vvthanh
 */
@Component
public class QLNNUtils {

    private final String secret = "gimasys-qlnn";

    @Value("${app.qlnn}")
    private String baseUrl;

    @Autowired
    private Gson gson;

    protected String checksum(String... contents) {
        String key = "";
        for (String content : contents) {
            key += "/" + content;
        }
        //string: [secret]/[string1]/[string2]
        key = String.format("%s%s", secret, key);
        return DigestUtils.md5Hex(key).toUpperCase();
    }

    public PacPatientInfoEntity visit(HtcVisitEntity visit) throws Exception {
        String body = Base64.getEncoder().encodeToString(gson.toJson(visit).getBytes());
        String url = String.format("%s/api/v1/pac-visit.api", baseUrl);

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
        PacPatientInfoEntity patient = gson.fromJson(json, PacPatientInfoEntity.class);
        return patient;
    }

    public PacPatientInfoEntity confirm(HtcConfirmEntity confirm) throws Exception {
        String body = Base64.getEncoder().encodeToString(gson.toJson(confirm).getBytes());
        String url = String.format("%s/api/v1/pac-confirm.api", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("checksum", checksum(String.valueOf(confirm.getID()), String.valueOf(confirm.getSiteID())));

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
        PacPatientInfoEntity patient = gson.fromJson(json, PacPatientInfoEntity.class);
        return patient;
    }

    public PacPatientInfoEntity arv(OpcArvEntity arv) throws Exception {
        String body = Base64.getEncoder().encodeToString(gson.toJson(arv).getBytes());
        String url = String.format("%s/api/v1/pac-arv.api", baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("checksum", checksum(String.valueOf(arv.getID()), String.valueOf(arv.getSiteID())));

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
        PacPatientInfoEntity patient = gson.fromJson(json, PacPatientInfoEntity.class);
        return patient;
    }

    public PacPatientInfoEntity getByConfirm(String confirmCode, String cardId, String province) throws Exception {
        String url = String.format("%s/api/v1/pac-get.api?confirm_code=%s&card_id=%s&province=%s", baseUrl, confirmCode, cardId, province);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("checksum", checksum(confirmCode, cardId));

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Map<String, Object>> response = rest.exchange(url, HttpMethod.GET, entity, responseType);
        Map<String, Object> result = response.getBody();
        boolean success = Boolean.valueOf(String.valueOf(result.get("success")));
        String message = String.valueOf(result.get("message"));
        String data = String.valueOf(result.get("data"));
        if (!success) {
            throw new Exception(message);
        }
        String json = new String(Base64.getDecoder().decode(data));
        PacPatientInfoEntity patient = gson.fromJson(json, PacPatientInfoEntity.class);
        return patient;
    }

}
