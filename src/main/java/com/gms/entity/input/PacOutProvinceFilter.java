package com.gms.entity.input;

/**
 *
 * @author vvThành
 */
public class PacOutProvinceFilter {

    private int pageIndex;
    private int pageSize;
    private boolean fullname;
    private boolean gender;
    private boolean address;
    private boolean identity;
    private boolean insurance;
    private boolean phone;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isFullname() {
        return fullname;
    }

    public void setFullname(boolean fullname) {
        this.fullname = fullname;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isAddress() {
        return address;
    }

    public void setAddress(boolean address) {
        this.address = address;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

}
