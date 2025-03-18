package com.gms.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "DISTRICT",
        indexes = {
            @Index(name = "district_find_all", columnList = "POSITION")
            ,
            @Index(name = "district_find_by_province", columnList = "POSITION,PROVINCE_ID")
        }
)
public class DistrictEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID", length = 5, nullable = false)
    private String ID;

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "TYPE", length = 30, nullable = false)
    private String type;

    @Column(name = "POSITION")
    private int position;

    @Column(name = "ELOG_CODE", length = 50, nullable = true)
    private String elogCode;

    @Column(name = "HIVINFO_CODE", length = 50, nullable = true)
    private String hivInfoCode;

    @Column(name = "place", length = 500, nullable = true)
    private String place;

    @Column(name = "PQM_CODE", length = 50, nullable = true)
    private String pqmCode;

    public String getPqmCode() {
        return pqmCode;
    }

    public void setPqmCode(String pqmCode) {
        this.pqmCode = pqmCode;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getElogCode() {
        return elogCode;
    }

    public void setElogCode(String elogCode) {
        this.elogCode = elogCode;
    }

    public String getHivInfoCode() {
        return hivInfoCode;
    }

    public void setHivInfoCode(String hivInfoCode) {
        this.hivInfoCode = hivInfoCode;
    }
}
