package com.gms.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin latlong
 */
@Entity
@Table(name = "PAC_LOCATION")
@DynamicUpdate
@DynamicInsert
public class PacLocationEntity extends BaseEntity implements Serializable {

    @Id
    private Long ID; //Mã người nhiễm PatientInfo

    @Column(name = "PERMANENT_LNG")
    private double permanentLng;

    @Column(name = "PERMANENT_LAT")
    private double permanentLat;

    @Column(name = "PERMANENT_PLACE", length = 220)
    private String permanentPlace;

    @Column(name = "PERMANENT_ADDRESS", columnDefinition = "TEXT", nullable = true)
    private String permanentAddress;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public double getPermanentLng() {
        return permanentLng;
    }

    public void setPermanentLng(double permanentLng) {
        this.permanentLng = permanentLng;
    }

    public double getPermanentLat() {
        return permanentLat;
    }

    public void setPermanentLat(double permanentLat) {
        this.permanentLat = permanentLat;
    }

    public String getPermanentPlace() {
        return permanentPlace;
    }

    public void setPermanentPlace(String permanentPlace) {
        this.permanentPlace = permanentPlace;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

}
