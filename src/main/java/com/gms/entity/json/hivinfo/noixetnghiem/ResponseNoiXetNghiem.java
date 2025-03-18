package com.gms.entity.json.hivinfo.noixetnghiem;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseNoiXetNghiem {

    private boolean success;
    private String message;
    private List<NoiXetNghiem> data;

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

    public List<NoiXetNghiem> getData() {
        return data;
    }

    public void setData(List<NoiXetNghiem> data) {
        this.data = data;
    }

}
