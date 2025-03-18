package com.gms.entity.json.hivinfo.location;

/**
 *
 * @author vvthanh
 */
public class Ward {

    private int id;
    private int iddistrict;
    private String name;
    private boolean active;
    private boolean remove;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIddistrict() {
        return iddistrict;
    }

    public void setIddistrict(int iddistrict) {
        this.iddistrict = iddistrict;
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
