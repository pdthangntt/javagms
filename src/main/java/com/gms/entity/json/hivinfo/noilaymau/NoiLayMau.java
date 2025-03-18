package com.gms.entity.json.hivinfo.noilaymau;

import com.gms.entity.json.hivinfo.noixetnghiem.*;

/**
 *
 * @author vvthanh
 */
public class NoiLayMau {

    private int id;
    private String parentsID;
    private String name;
    private int idprovince;
    private boolean active;
    private boolean remove;

    public String getParentsID() {
        return parentsID;
    }

    public void setParentsID(String parentsID) {
        this.parentsID = parentsID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdprovince() {
        return idprovince;
    }

    public void setIdprovince(int idprovince) {
        this.idprovince = idprovince;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

}
