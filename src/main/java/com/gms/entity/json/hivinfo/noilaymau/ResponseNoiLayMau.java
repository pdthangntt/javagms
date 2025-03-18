package com.gms.entity.json.hivinfo.noilaymau;

import com.gms.entity.json.hivinfo.noixetnghiem.*;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class ResponseNoiLayMau {

    private boolean success;
    private String message;
    private List<NoiLayMau> data;

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

    public List<NoiLayMau> getData() {
        return data;
    }

    public void setData(List<NoiLayMau> data) {
        this.data = data;
    }

}
