package com.gms.entity.json.hivinfo.noixetnghiem;

/**
 *
 * @author vvthanh
 */
public class NoiXetNghiem {

    private int id;
    private String name;
    private String shortName;
    private int idtinh;
    private boolean active;
    private boolean remove;

    public int getIdtinh() {
        return idtinh;
    }

    public void setIdtinh(int idtinh) {
        this.idtinh = idtinh;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
