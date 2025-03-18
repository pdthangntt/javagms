package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(name = "AUTH_ROLE")
public class AuthRoleEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "NAME", length = 64, nullable = false, unique = true)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<AuthAssignmentEntity> userRoles = new HashSet<AuthAssignmentEntity>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<AuthRoleActionEntity> roleActions = new HashSet<AuthRoleActionEntity>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<AuthRoleServiceEntity> roleServices = new HashSet<AuthRoleServiceEntity>(0);

    public Set<AuthRoleServiceEntity> getRoleServices() {
        return roleServices;
    }

    public void setRoleServices(Set<AuthRoleServiceEntity> roleServices) {
        this.roleServices = roleServices;
    }

    public Set<AuthAssignmentEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<AuthAssignmentEntity> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<AuthRoleActionEntity> getRoleActions() {
        return roleActions;
    }

    public void setRoleActions(Set<AuthRoleActionEntity> roleActions) {
        this.roleActions = roleActions;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public AuthRoleEntity() {
    }

    public AuthRoleEntity(String name) {
        this.name = name;
    }
}
