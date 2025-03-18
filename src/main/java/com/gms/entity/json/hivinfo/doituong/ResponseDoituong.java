package com.gms.entity.json.hivinfo.doituong;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseDoituong {

    private boolean success;
    private String message;
    private List<Doituong> data;

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

    public List<Doituong> getData() {
        return data;
    }

    public void setData(List<Doituong> data) {
        this.data = data;
    }

}
