package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Báo cáo đồng bộ
 */
@Entity
@Table(name = "QL_REPORT",
        indexes = {
            @Index(name = "QL_REPORT_LOCAL_P", columnList = "MONTH, YEAR, PROVINCE_ID"),
            @Index(name = "QL_REPORT_LOCAL_D", columnList = "MONTH, YEAR, PROVINCE_ID, DISTRICT_ID"),},
        uniqueConstraints = {
            @UniqueConstraint(name = "QL_REPORT_UNIQUE", columnNames = {"MONTH", "YEAR", "PROVINCE_ID", "DISTRICT_ID", "WARD_ID"})
        })
@DynamicUpdate
@DynamicInsert
public class QlReport extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "MONTH", length = 2)
    private int month;

    @Column(name = "YEAR", length = 4)
    private int year;

    @Column(name = "PROVINCE_ID", length = 5)
    private String provinceID;

    @Column(name = "DISTRICT_ID", length = 5)
    private String districtID;

    @Column(name = "WARD_ID", length = 5)
    private String wardID;

    @Column(name = "PERMANENT_EARLY")
    private int permanentEarly; // Số nhiễm mới

    @Column(name = "PERMANENT_NEW_EARLY")
    private int permanentNewEarly; // Số Mới nhiễm

    @Column(name = "PERMANENT_DEAD")
    private int permanentDead; // Số tử vong

    @Column(name = "PERMANENT_AVG_30_MONTH")
    private double permanentAvg30Month; // Số trung bình tháng của 30 tháng trước

    @Column(name = "PERMANENT_HTC")
    private int permanentHtc; // Số xét nghiệm phát hiện mới trong tháng

//    @Column(name = "CURRENT_EARLY")
//    private int currentEarly; // Số nhiễm mới trong tháng
//
//    @Column(name = "CURRENT_AVG_30_MONTH")
//    private double currentAvg30Month; // Số trung bình tháng của 30 tháng trước
//
//    @Column(name = "CURRENT_HTC")
//    private int currentHtc; // Số xét nghiệm phát hiện mới trong tháng
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    public int getPermanentDead() {
        return permanentDead;
    }

    public void setPermanentDead(int permanentDead) {
        this.permanentDead = permanentDead;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public int getPermanentEarly() {
        return permanentEarly;
    }

    public void setPermanentEarly(int permanentEarly) {
        this.permanentEarly = permanentEarly;
    }

    public double getPermanentAvg30Month() {
        return permanentAvg30Month;
    }

    public void setPermanentAvg30Month(double permanentAvg30Month) {
        this.permanentAvg30Month = permanentAvg30Month;
    }

    public int getPermanentHtc() {
        return permanentHtc;
    }

    public void setPermanentHtc(int permanentHtc) {
        this.permanentHtc = permanentHtc;
    }

    public int getPermanentNewEarly() {
        return permanentNewEarly;
    }

    public void setPermanentNewEarly(int permanentNewEarly) {
        this.permanentNewEarly = permanentNewEarly;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
