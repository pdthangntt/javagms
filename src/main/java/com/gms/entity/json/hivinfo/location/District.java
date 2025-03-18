package com.gms.entity.json.hivinfo.location;

/**
 *
 * @author vvthanh
 */
public class District {

    private int id;
    private int idtinh;
    private String name;
    private boolean active;
    private boolean remove;

    public int getIdtinh() {
        return idtinh;
    }

    public void setIdtinh(int idtinh) {
        this.idtinh = idtinh;
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
