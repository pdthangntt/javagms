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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "STAFF_ACTIVITY",
        indexes = {
            @Index(name = "STAFF_ACTIVITY_INDEX_STAFF", columnList = "USER_ID")
        }
)
@DynamicInsert
@DynamicUpdate
public class StaffActivityEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "USER_AGENT", nullable = true)
    private String userAgent;

    @Column(name = "IP", length = 100, nullable = true)
    private String ip;

    @Column(name = "EXPIRES", nullable = true)
    private String expires;

    @Column(name = "USER_ID")
    private Long userID;

    @Column(name = "REQUEST_METHOD")
    private String requestMethod;

    @Column(name = "REQUEST_URL")
    private String requestUrl;

    @Column(name = "REQUEST_PARAMETER", columnDefinition = "TEXT")
    private String requestParameter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    public String getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(String requestParameter) {
        this.requestParameter = requestParameter;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }
}
