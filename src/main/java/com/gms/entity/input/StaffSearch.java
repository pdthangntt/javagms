package com.gms.entity.input;

import java.util.Set;

/**
 *
 * @author vvthanh
 */
public class StaffSearch {

    public static final String ID_DESC = "id_desc";
    public static final String ID_ASC = "id_asc";

    private int pageIndex;
    private int pageSize;
    private String sort;

    private String email;
    private String phone;
    private String userName;
    private String name;
    private boolean isActive;
    private Long siteID;
    private Long ID;
    private Set<Long> siteIDs;

    public Set<Long> getSiteIDs() {
        return siteIDs;
    }

    public void setSiteIDs(Set<Long> siteIDs) {
        this.siteIDs = siteIDs;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
