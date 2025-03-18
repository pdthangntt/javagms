package com.gms.entity.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vvthanh
 * @param <E>
 */
public class ResponseHubData<E> {

    private int total;
    private int updated;
    private int error_count;
    private List<E> error_rows;

    public ResponseHubData() {
    }

    public ResponseHubData(E error_row) {
        this.error_rows = new ArrayList<>();
        this.error_rows.add(error_row);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getError_count() {
        return error_count;
    }

    public void setError_count(int error_count) {
        this.error_count = error_count;
    }

    public List<E> getError_rows() {
        return error_rows;
    }

    public void setError_rows(List<E> error_rows) {
        this.error_rows = error_rows;
    }

}
