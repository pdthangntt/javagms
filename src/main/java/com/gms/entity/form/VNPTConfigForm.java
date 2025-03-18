package com.gms.entity.form;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author pdThang
 */
public class VNPTConfigForm implements Serializable {

    private String ID; //Mã cơ sở
    private String IDcheck;

    private String vpntSiteID; //Mã cơ sở phần mềm mềm his-hiv Tây Ninh - phải là duy nhất với id

    private String active;

    private Date createAt;

    public String getIDcheck() {
        return IDcheck;
    }

    public void setIDcheck(String IDcheck) {
        this.IDcheck = IDcheck;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

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
