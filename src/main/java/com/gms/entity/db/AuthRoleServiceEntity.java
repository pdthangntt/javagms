package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "AUTH_ROLE_SERVICE",
        indexes = {
            @Index(name = "AUTH_ROLE_SERVICE_ROLE", columnList = "ROLE_ID")
        }
)
public class AuthRoleServiceEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private AuthRoleEntity role;

    @Column(name = "SERVICE_ID", length = 50, nullable = false)
    private String serviceID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Transient
    private ParameterEntity service;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public AuthRoleEntity getRole() {
        return role;
    }

    public void setRole(AuthRoleEntity role) {
        this.role = role;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public ParameterEntity getService() {
        return service;
    }

    public void setService(ParameterEntity service) {
        this.service = service;
    }

}
