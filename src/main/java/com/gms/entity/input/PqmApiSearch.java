package com.gms.entity.input;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 *
 * @author pdThang
 */
public class PqmApiSearch {

    private String from; //Ngày từ
    private String to; //Ngày đến
    private String province; //Ngày đến

    //Phân trang
    private int pageIndex;
    private int pageSize;
    private Sort sortable;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

}
