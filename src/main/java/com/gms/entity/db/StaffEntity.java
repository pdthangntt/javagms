package com.gms.entity.db;

import com.gms.components.StringListConverter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "STAFF",
        indexes = {
            @Index(name = "STAFF_INDEX_SITE", columnList = "SITE_ID")
            ,@Index(name = "STAFF_INDEX_EMAIL", columnList = "EMAIL")
            ,@Index(name = "STAFF_INDEX_USERNAME", columnList = "USERNAME")
        }
)
@DynamicUpdate
public class StaffEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID", nullable = false)
    private Long siteID;

    @Column(name = "SERVICE_VISIT_ID", length = 50, nullable = true)
    private String serviceVisitID; //Dịch vụ tư vấn xét nghiệm

    @Column(name = "FULL_NAME", length = 64, nullable = false)
    private String name;

    @Column(name = "USERNAME", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "EMAIL", length = 100, nullable = true)
    private String email;

    @Column(name = "PHONE", length = 50, nullable = true)
    private String phone;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String pwd;

    @Column(name = "IS_ACTIVE")
    private boolean isActive = true;

    @Column(name = "CHANGE_PWD")
    private Boolean changePwd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

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

    @Column(name = "DEVICE", nullable = true)
    @Convert(converter = StringListConverter.class)
    private List<String> device;

    @Column(name = "token", length = 220, nullable = true)
    private String token;

    @Column(name = "code", length = 6, nullable = true)
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "token_time", nullable = true)
    private Date tokenTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<AuthAssignmentEntity> userRoles = new HashSet<AuthAssignmentEntity>(0);

    public String getServiceVisitID() {
        return serviceVisitID;
    }

    public void setServiceVisitID(String serviceVisitID) {
        this.serviceVisitID = serviceVisitID;
    }

    public Boolean getChangePwd() {
        return changePwd;
    }

    public void setChangePwd(Boolean changePwd) {
        this.changePwd = changePwd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
    }

    public List<String> getDevice() {
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device;
    }

    public Set<AuthAssignmentEntity> getUserRoles() {
        return userRoles == null || userRoles.isEmpty() ? new HashSet<AuthAssignmentEntity>(0) : userRoles;
    }

    public void setUserRoles(Set<AuthAssignmentEntity> userRoles) {
        this.userRoles = userRoles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("username");
    }

}
