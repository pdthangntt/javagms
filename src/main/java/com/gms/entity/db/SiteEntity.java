package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(name = "SITE", indexes = {
    @Index(name = "SITE_INDEX_TYPE", columnList = "TYPE"),
    @Index(name = "SITE_INDEX_TYPE_ACTIVE", columnList = "TYPE, IS_ACTIVE"),
    @Index(name = "SITE_INDEX_CODE_ACTIVE", columnList = "CODE, IS_ACTIVE"),
    @Index(name = "SITE_INDEX_PARENT", columnList = "PARENT_ID"),
    @Index(name = "SITE_INDEX_PROVINCE_ID", columnList = "PROVINCE_ID"),
    @Index(name = "SITE_INDEX_PARENT_ACTIVE", columnList = "PARENT_ID, IS_ACTIVE")
})
public class SiteEntity extends BaseEntity implements Serializable, Cloneable {

    public static final int TYPE_VAAC = 100;
    public static final int TYPE_PROVINCE = 101;
    public static final int TYPE_DISTRICT = 102;
    public static final int TYPE_WARD = 103;

    public static Map<Integer, String> typeLabel;

    static {
        typeLabel = new HashMap<>();
        typeLabel.put(TYPE_VAAC, "Cấp trung ương");
        typeLabel.put(TYPE_PROVINCE, "Cấp tỉnh");
        typeLabel.put(TYPE_DISTRICT, "Cấp huyện");
        typeLabel.put(TYPE_WARD, "Cấp xã");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "CODE", length = 100, unique = true, nullable = false)
    private String code;

    @Column(name = "NAME", length = 220)
    private String name;

    @Column(name = "SHORT_NAME", length = 100, nullable = true)
    private String shortName;

    @Column(name = "DESCRIPTION", length = 500, nullable = true)
    private String description;

    @Column(name = "ADDRESS", length = 1000, nullable = true)
    private String address;

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID;

    @Column(name = "DISTRICT_ID", length = 5, nullable = false)
    private String districtID;

    @Column(name = "WARD_ID", length = 5, nullable = false)
    private String wardID;

    @Column(name = "PARENT_ID", nullable = true)
    private Long parentID;

    @Column(name = "PARENT_AGENCY", length = 225, nullable = true)
    private String parentAgency; //Tên cơ quan chủ quản

    @Column(name = "TYPE", length = 50)
    private int type;

    @Column(name = "EMAIL", length = 220)
    private String email;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "CONTACT_NAME", length = 100)
    private String contactName;

    @Column(name = "CONTACT_POSITION", length = 220)
    private String contactPosition;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

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

    @Column(name = "PROJECT_ID", insertable = true, updatable = true, nullable = true, length = 50)
    private String projectID; //Dự án tài trợ cho cơ sở

    @Column(name = "HUB", length = 10, nullable = true)
    private String hub;

    @Column(name = "HUB_SITE_CODE", length = 100)
    private String hubSiteCode;

    @Column(name = "PQM_SITE_CODE", length = 100)
    private String pqmSiteCode;

    @Column(name = "ELOG_SITE_CODE", length = 100)
    private String elogSiteCode;

    @Column(name = "ARV_SITE_CODE", length = 100)
    private String arvSiteCode;

    @Column(name = "PREP_SITE_CODE", length = 100)
    private String prepSiteCode;

    @Column(name = "hmed_SITE_CODE", length = 100)
    private String hmedSiteCode;

    @Transient
    private List<SiteEntity> path;

    @Transient
    private List<Long> pathID;

    @Transient
    private List<ParameterEntity> services;

    @Transient
    private List<String> serviceIDs;

    public static Map<Integer, String> getTypeLabel() {
        return typeLabel;
    }

    public static void setTypeLabel(Map<Integer, String> typeLabel) {
        SiteEntity.typeLabel = typeLabel;
    }

    public String getElogSiteCode() {
        return elogSiteCode;
    }

    public void setElogSiteCode(String elogSiteCode) {
        this.elogSiteCode = elogSiteCode;
    }

    public String getArvSiteCode() {
        return arvSiteCode;
    }

    public void setArvSiteCode(String arvSiteCode) {
        this.arvSiteCode = arvSiteCode;
    }

    public String getPrepSiteCode() {
        return prepSiteCode;
    }

    public void setPrepSiteCode(String prepSiteCode) {
        this.prepSiteCode = prepSiteCode;
    }

    public String getHmedSiteCode() {
        return hmedSiteCode;
    }

    public void setHmedSiteCode(String hmedSiteCode) {
        this.hmedSiteCode = hmedSiteCode;
    }

    public String getPqmSiteCode() {
        return pqmSiteCode;
    }

    public void setPqmSiteCode(String pqmSiteCode) {
        this.pqmSiteCode = pqmSiteCode;
    }

    public String getHubSiteCode() {
        return hubSiteCode;
    }

    public void setHubSiteCode(String hubSiteCode) {
        this.hubSiteCode = hubSiteCode;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getParentAgency() {
        return parentAgency;
    }

    public void setParentAgency(String parentAgency) {
        this.parentAgency = parentAgency;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public List<String> getServiceIDs() {
        return serviceIDs;
    }

    public void setServiceIDs(List<String> serviceIDs) {
        this.serviceIDs = serviceIDs;
    }

    public List<ParameterEntity> getServices() {
        return services;
    }

    public void setServices(List<ParameterEntity> services) {
        this.services = services;
    }

    public List<Long> getPathID() {
        return pathID;
    }

    public void setPathID(List<Long> pathID) {
        this.pathID = pathID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<SiteEntity> getPath() {
        return path;
    }

    public void setPath(List<SiteEntity> path) {
        this.path = path;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String viewName() {
        return shortName == null || shortName.equals("") ? name : shortName;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("code");
    }

    @Override
    public SiteEntity clone() throws CloneNotSupportedException {
        return (SiteEntity) super.clone();
    }

}
