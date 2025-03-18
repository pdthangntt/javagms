package com.gms.entity.form.form_error;

import java.util.List;

/**
 *
 * @author pdThang
 */
public class data {

    private String total;
    private String succeed;
    private String updated;
    private String error_count;
    private List<error_rows> error_rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getError_count() {
        return error_count;
    }

    public void setError_count(String error_count) {
        this.error_count = error_count;
    }

    public List<error_rows> getError_rows() {
        return error_rows;
    }

    public void setError_rows(List<error_rows> error_rows) {
        this.error_rows = error_rows;
    }

}
