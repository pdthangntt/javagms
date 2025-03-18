package com.gms.entity.json.hivinfo.location;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseWard {

    private boolean success;
    private String message;
    private List<Ward> data;

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

    public List<Ward> getData() {
        return data;
    }

    public void setData(List<Ward> data) {
        this.data = data;
    }

}
