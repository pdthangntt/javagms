package com.gms.entity.bean;

/**
 *
 * @author vvthanh
 */
public class ResponseHubDataError<E> {

    private E row;
    private String error;

    public ResponseHubDataError() {
    }

    public ResponseHubDataError(E row, String error) {
        this.row = row;
        this.error = error;
    }

    public ResponseHubDataError(String error) {
        this.error = error;
    }

    public E getRow() {
        return row;
    }

    public void setRow(E row) {
        this.row = row;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
