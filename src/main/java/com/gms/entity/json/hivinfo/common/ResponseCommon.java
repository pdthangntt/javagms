package com.gms.entity.json.hivinfo.common;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseCommon {

    private boolean success;
    private String message;
    private List<Item> data;

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

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

}
