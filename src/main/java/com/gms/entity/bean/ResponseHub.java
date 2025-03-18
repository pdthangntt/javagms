package com.gms.entity.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
public class ResponseHub<E> {

    private String error;
    private boolean status;
    private ResponseHubData<E> data;

    public ResponseHub(boolean success, String message, int total, int updated) {
        ResponseHubData data = new ResponseHubData();
        data.setTotal(total);
        data.setUpdated(updated);
        data.setError_count(0);

        this.status = success;
        this.data = data;
    }

    public ResponseHub(boolean success, String message, HashMap<String, HashMap<String, String>> data, int total) {
        List<HashMap<String, String>> errors = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, String>> entry : data.entrySet()) {
            String key = entry.getKey();
            HashMap<String, String> value = entry.getValue();
            value.put("customer_code", key);
            errors.add(value);
        }

        this.status = success;
        this.data = new ResponseHubData<>();
        this.data.setTotal(total);
        this.data.setError_count(data.size());
        this.data.setError_rows((List<E>) errors);
    }

    public ResponseHub(boolean success, String message, HashMap<String, HashMap<String, String>> data, int total, int updated) {
        List<HashMap<String, String>> errors = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, String>> entry : data.entrySet()) {
            String key = entry.getKey();
            HashMap<String, String> value = entry.getValue();
            value.put("customer_code", key);
            errors.add(value);
        }

        this.status = success;
        this.data = new ResponseHubData<>();
        this.data.setTotal(total);
        this.data.setUpdated(updated);
        this.data.setError_count(data.size());
        this.data.setError_rows((List<E>) errors);
    }

    public ResponseHub(boolean success, String message, E data) {
        this.status = success;
        this.data = new ResponseHubData<>(data);
        this.data.setError_count(success ? 0 : 1);
        this.data.setUpdated(success ? 1 : 0);
    }

    public ResponseHub(boolean success, String message, String data) {
        this.status = success;
        this.data = new ResponseHubData<>();
        this.data.setError_count(success ? 0 : 1);
        this.data.setUpdated(success ? 1 : 0);

        List<HashMap<String, String>> errors = new ArrayList<>();
        HashMap<String, String> error = new HashMap<>();
        error.put("customer_code", "system");
        error.put("message", data);
        errors.add(error);
        this.data.setError_rows((List<E>) errors);
    }

    public ResponseHub(boolean success, String message, String data, int total) {
        this.status = success;
        this.data = new ResponseHubData<>();
        this.data.setError_count(success ? 0 : 1);
        this.data.setUpdated(success ? 1 : 0);
        this.data.setTotal(total);

        List<HashMap<String, String>> errors = new ArrayList<>();
        HashMap<String, String> error = new HashMap<>();
        error.put("customer_code", "system");
        error.put("message", data);
        errors.add(error);
        this.data.setError_rows((List<E>) errors);
    }

    public ResponseHub(boolean success, String message, E data, int error_count) {
        this.status = success;
        this.data = new ResponseHubData<>(data);
        this.data.setError_count(error_count);
    }

    public String getError() {
        return error;
    }

    public boolean isStatus() {
        return status;
    }

    public ResponseHubData getData() {
        return data;
    }

}
