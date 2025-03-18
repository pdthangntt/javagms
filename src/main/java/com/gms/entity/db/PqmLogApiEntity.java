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
@Table(name = "PQM_LOG_API")
@DynamicInsert
@DynamicUpdate
public class PqmLogApiEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "TYPE", length = 50, nullable = true)
    private String type;

    @Column(name = "REQUEST_METHOD", length = 50, nullable = true)
    private String requestMethod;

    @Column(name = "SITE_ID", nullable = true)
    private Long siteID;

    @Column(name = "CHECKSUM", nullable = true)
    private String checksum;

    @Column(name = "PUBLIC_KEY", nullable = true)
    private String publicKey;

    @Column(name = "REQUEST_BODY", columnDefinition = "TEXT", nullable = true)
    private String requestBody;

    @Column(name = "STATUS", nullable = true)
    private boolean status; //Trạng thái

    @Column(name = "MESSAGE", length = 220, nullable = true)
    private String message; //Thông báo

    @Column(name = "EXR", columnDefinition = "TEXT", nullable = true)
    private String exr; //Dữ liệu trả về

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false, nullable = false)
    private Date time;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExr() {
        return exr;
    }

    public void setExr(String exr) {
        this.exr = exr;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
