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
 * @author pdThang
 * @des Log api
 */
@Entity
@Table(name = "PQM_API_LOG") 
@DynamicUpdate
@DynamicInsert
public class PqmApiLogEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SUCCEED")
    private boolean succeed; //Thành công

    @Column(name = "BODY", columnDefinition = "LONGTEXT", nullable = true)
    private String body;

    @Column(name = "CONTENT", columnDefinition = "LONGTEXT", nullable = true)
    private String content; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = true)
    private Date createAT;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "ERROR", columnDefinition = "TEXT", nullable = true)
    private String error;

    @Column(name = "ERROR_IMS", columnDefinition = "TEXT", nullable = true)
    private String errorIms;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getErrorIms() {
        return errorIms;
    }

    public void setErrorIms(String errorIms) {
        this.errorIms = errorIms;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

}
