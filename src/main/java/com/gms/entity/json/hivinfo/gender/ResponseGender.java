package com.gms.entity.json.hivinfo.gender;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseGender {

    private boolean success;
    private String message;
    private List<Gender> data;

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

    public List<Gender> getData() {
        return data;
    }

    public void setData(List<Gender> data) {
        this.data = data;
    }

}
