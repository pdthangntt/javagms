package com.gms.entity.json.hivinfo.patient;

import java.util.Map;

/**
 *
 * @author vvthanh
 */
public class ResponsePatient {

    private boolean success;
    private String message;
    private Map<String, Data> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Data> getData() {
        return data;
    }

    public void setData(Map<String, Data> data) {
        this.data = data;
    }

}
