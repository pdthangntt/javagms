package com.gms.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvThành
 */
@Entity
@Table(name = "HTC_LAYTEST_AGENCY")
@DynamicUpdate
@DynamicInsert
public class HtcLaytestAgencyEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "LAYTEST_ID", nullable = false)
    private Long laytestID; // Mã thông tin htc_laytest - Lưu để biết, k sử dụng

    @Column(name = "FULLNAME", length = 100, nullable = true)
    private String fullname;

    @Column(name = "PHONE", length = 50, nullable = true)
    private String phone;

    @Column(name = "ADDRESS", length = 500, nullable = true)
    private String address;

    @Column(name = "ALERT_TYPE", length = 50, nullable = true)
    private String alertType; //Hình thức thông báo - Dùng enum
    
    @Column(name = "IS_AGREE_PRETEST", length = 2, nullable = true)
    private String isAgreePreTest;

    public String getIsAgreePreTest() {
        return isAgreePreTest;
    }

    public void setIsAgreePreTest(String isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getLaytestID() {
        return laytestID;
    }

    public void setLaytestID(Long laytestID) {
        this.laytestID = laytestID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    // Kiểm tra phần tử bị null
    public Boolean checkNullElement() {
        if(StringUtils.isEmpty(this.getFullname()) &&  
            StringUtils.isEmpty(this.getAddress()) &&
            StringUtils.isEmpty(this.getPhone()) &&
            StringUtils.isEmpty(this.getAlertType()) &&
            StringUtils.isEmpty(this.getIsAgreePreTest())){
            return true;
        }
        return false;
    }
}
