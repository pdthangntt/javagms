package com.gms.entity.form.pqm_arv_api;

import com.gms.entity.form.drug_new_api.*;
import com.gms.entity.form.opc_arv.*;
import com.gms.entity.db.BaseEntity;

/**
 *
 * @author pdThang
 */
public class Test extends BaseEntity {

    private String inhFromTime; //Lao từ ngày

    private String inhToTime; //Lao đến ngày

    private String inhEndCause; //Lý do kết thúc lao - inh

    public String getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(String inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public String getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(String inhToTime) {
        this.inhToTime = inhToTime;
    }

    public String getInhEndCause() {
        return inhEndCause;
    }

    public void setInhEndCause(String inhEndCause) {
        this.inhEndCause = inhEndCause;
    }

}
