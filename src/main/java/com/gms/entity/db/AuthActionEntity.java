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
@Table(name = "AUTH_ACTION")
public class AuthActionEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "NAME", length = 64, nullable = false, unique = true)
    private String name;

    @Column(name = "TITLE", length = 64, nullable = true)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updateAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "action")
    private Set<AuthRoleActionEntity> roleActions = new HashSet<AuthRoleActionEntity>(0);

    public AuthActionEntity() {
    }

    public AuthActionEntity(String name) {
        this.name = name;
    }

    public AuthActionEntity(String name, Set<AuthRoleActionEntity> roleActions) {
        this.name = name;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<AuthRoleActionEntity> getRoleActions() {
        return roleActions;
    }

    public void setRoleActions(Set<AuthRoleActionEntity> roleActions) {
        this.roleActions = roleActions;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }

    @Override
    public void setAttributeLabels() {
    }

}
