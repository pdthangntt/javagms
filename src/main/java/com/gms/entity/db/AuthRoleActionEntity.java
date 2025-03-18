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

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "AUTH_ROLE_ACTION",
        indexes = {
            @Index(name = "AUTH_ROLE_ACTION_ROLE", columnList = "ROLE_ID")
        }
)
public class AuthRoleActionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    private AuthRoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTION_ID")
    private AuthActionEntity action;

    @Column(name = "ACTION_ID", updatable = false, insertable = false)
    private Long actionID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

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
