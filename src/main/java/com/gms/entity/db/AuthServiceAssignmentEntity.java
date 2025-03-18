package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
        name = "AUTH_SERVICE_ASSIGNMENT"
)
public class AuthServiceAssignmentEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SERVICE_ID", length = 50, nullable = false)
    private String serviceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTION_ID")
    private AuthActionEntity action;

    @Column(name = "ACTION_ID", insertable = false, updatable = false)
    private Long actionID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Transient
    private ParameterEntity service;

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public ParameterEntity getService() {
        return service;
    }

    public void setService(ParameterEntity service) {
        this.service = service;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public AuthActionEntity getAction() {
        return action;
    }

    public void setAction(AuthActionEntity action) {
        this.action = action;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

}
