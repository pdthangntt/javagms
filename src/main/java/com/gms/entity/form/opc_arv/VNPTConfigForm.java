package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;
import java.io.Serializable;



/**
 *
 * @author pdThang
 */
public class VNPTConfigForm extends BaseEntity implements Serializable {

    private String ID; //Mã cơ sở

    private String vpntSiteID; //Mã cơ sở phần mềm mềm his-hiv Tây Ninh - phải là duy nhất với id

    private String active;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVpntSiteID() {
        return vpntSiteID;
    }

    public void setVpntSiteID(String vpntSiteID) {
        this.vpntSiteID = vpntSiteID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
    
}
