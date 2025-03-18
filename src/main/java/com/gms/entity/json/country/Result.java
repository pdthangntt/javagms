package com.gms.entity.json.country;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class Result {

    private String code;
    private List<Item> result;
    private List<Object> extra;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Item> getResult() {
        return result;
    }

    public void setResult(List<Item> result) {
        this.result = result;
    }

    public List<Object> getExtra() {
        return extra;
    }

    public void setExtra(List<Object> extra) {
        this.extra = extra;
    }

}
