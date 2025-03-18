package com.gms.entity.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @Author vvThành
 */
@Entity
@Table(name = "SITE_VNPT_FK", uniqueConstraints = {
    @UniqueConstraint(name = "SITE_VNPT_FK_UNIQUE", columnNames = {"ID", "VNPT_SITE_ID"})
})
@DynamicInsert
@DynamicUpdate
public class SiteVnptFkEntity extends BaseEntity implements Serializable {

    @Id
    private Long ID; //Mã cơ sở

    @Column(name = "VNPT_SITE_ID")
    private String vpntSiteID; //Mã cơ sở phần mềm mềm his-hiv Tây Ninh - phải là duy nhất với id

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getVpntSiteID() {
        return vpntSiteID;
    }

    public void setVpntSiteID(String vpntSiteID) {
        this.vpntSiteID = vpntSiteID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

}
