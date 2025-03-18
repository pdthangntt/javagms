package com.gms.entity.json.hivinfo.vitrilaymau;

import com.gms.entity.json.hivinfo.noilaymau.*;
import com.gms.entity.json.hivinfo.noixetnghiem.*;

/**
 *
 * @author vvthanh
 */
public class ViTriLayMau {

    private int id;
    private String shortName;
    private String name;
    private int formSize;
    private int registerAddressId;
    private boolean active;
    private boolean remove;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFormSize() {
        return formSize;
    }

    public void setFormSize(int formSize) {
        this.formSize = formSize;
    }

    public int getRegisterAddressId() {
        return registerAddressId;
    }

    public void setRegisterAddressId(int registerAddressId) {
        this.registerAddressId = registerAddressId;
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
