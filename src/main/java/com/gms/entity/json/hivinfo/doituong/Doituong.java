package com.gms.entity.json.hivinfo.doituong;

/**
 *
 * @author vvthanh
 */
public class Doituong {

    private int id;
    private String parentID;
    private String name;
    private String shortName;
    private boolean mauGSPH;
    private boolean active;
    private boolean remove;

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public boolean isMauGSPH() {
        return mauGSPH;
    }

    public void setMauGSPH(boolean mauGSPH) {
        this.mauGSPH = mauGSPH;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
