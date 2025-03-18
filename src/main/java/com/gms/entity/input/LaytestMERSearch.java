package com.gms.entity.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 *
 * @author NamAnh_HaUI
 */
public class LaytestMERSearch {
    private Long staffID;
    private String advisoryeTimeFrom; //Ngày tư vấn từ
    private String advisoryeTimeTo; //Ngày tư vấn đến
    private Set<Integer> yearOfBirth;
    private Set<String> genderID;
    private Set<Long> siteID;
    private Set<String> confirmResultsID;
    private Set<String> ObjectGroupID;
    private String age;
    //Phân trang
    private int pageIndex;
    private int pageSize;
    private Sort sortable;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Set<String> getObjectGroupID() {
        return ObjectGroupID;
    }

    public void setObjectGroupID(Set<String> ObjectGroupID) {
        this.ObjectGroupID = ObjectGroupID;
    }

    public Set<String> getGenderID() {
        return genderID;
    }

    public void setGenderID(Set<String> genderID) {
        this.genderID = genderID;
    }

    public Set<String> getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(Set<String> confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }




    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public String getAdvisoryeTimeFrom() {
        return advisoryeTimeFrom;
    }

    public void setAdvisoryeTimeFrom(String advisoryeTimeFrom) {
        this.advisoryeTimeFrom = advisoryeTimeFrom;
    }

    public String getAdvisoryeTimeTo() {
        return advisoryeTimeTo;
    }

    public void setAdvisoryeTimeTo(String advisoryeTimeTo) {
        this.advisoryeTimeTo = advisoryeTimeTo;
    }

    public Set<Integer> getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Set<Integer> yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
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

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }
    
    
}
