package com.gms.entity.form.pqm_prep_api;

import com.gms.entity.form.drug_new_api.*;
import com.gms.entity.form.opc_arv.*;
import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Stage extends BaseEntity {

    private String startTime; //Ngày bắt đàu điều trị

    private String endTime; //Ngày kết thúc điều trị

    private String type; // Kết quả XN - Lấy thời enum kết quả khẳng định

    private String endCause; // Lý do kết thúc

    private List<Visit> visits;

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public String getEndCause() {
        return endCause;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
