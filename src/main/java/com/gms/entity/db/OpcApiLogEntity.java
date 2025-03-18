package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin đồng bộ API
 */
@Entity
@Table(name = "OPC_API_LOG")
@DynamicInsert
@DynamicUpdate
public class OpcApiLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "ARV_ID", nullable = true)
    private Long arvID; //Mã ARV

    @Column(name = "ARV_CODE", length = 50, nullable = true)
    private String arvCode; //Mã bệnh án

    @Column(name = "SOURCE_SITE_ID", length = 50, nullable = true)
    private String sourceSiteID; //Mã Cơ sở vnpt

    @Column(name = "SITE_ID", nullable = true)
    private Long siteID; //Mã Cơ sở  

    @Column(name = "CHECKSUM", nullable = true)
    private String checksum; //Key

    @Column(name = "TYPE", nullable = true)
    private String type; //Loại API

    @Column(name = "STATUS", nullable = true)
    private boolean status; //Trạng thái

    @Column(name = "MESSAGE", length = 220, nullable = true)
    private String message; //Thông báo

    @Column(name = "DATA", columnDefinition = "TEXT", nullable = true)
    private String data; //Dữ liệu trả về

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false, nullable = false)
    private Date time;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
    }

    public String getArvCode() {
        return arvCode;
    }

    public void setArvCode(String arvCode) {
        this.arvCode = arvCode;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
