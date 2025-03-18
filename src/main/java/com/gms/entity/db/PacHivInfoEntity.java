package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @des Thông tin mã bệnh nhân bên hiv
 */
@Entity
@Table(name = "PAC_HIV_INFO",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"HIV_CODE"})
        },
        indexes = {
            @Index(columnList = "HIV_CODE")
        })
@DynamicUpdate
@DynamicInsert
public class PacHivInfoEntity extends BaseEntity implements Serializable {

    @Id
    private Long ID; //Mã người nhiễm PatientInfo

    @Column(name = "HIV_CODE", length = 50, nullable = true)
    private String code; //Mã bản ghi bên HIV Info - thông tin bệnh nhân HIV

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = true)
    private Date updateAt;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
