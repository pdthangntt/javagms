package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "PQM_PREP",
        indexes = {
            @Index(name = "OS_ID", columnList = "ID"),
            @Index(name = "OS_CODE", columnList = "CODE"),
            @Index(name = "OS_END_TIME", columnList = "END_TIME"),
            @Index(name = "OS_GENDER_ID", columnList = "GENDER_ID"),
            @Index(name = "OS_IDENTITY_CARD", columnList = "IDENTITY_CARD"),
            @Index(name = "OS_OBJECT_GROUP_ID", columnList = "OBJECT_GROUP_ID"),
            @Index(name = "OS_SITE_ID", columnList = "SITE_ID"),
            @Index(name = "OS_START_TIME", columnList = "START_TIME"),
            @Index(name = "OS_TYPE", columnList = "TYPE"),
            @Index(name = "OS_YEAR_OF_BIRTH", columnList = "YEAR_OF_BIRTH"),
            @Index(name = "OS_APPOINMENT_TIME", columnList = "APPOINMENT_TIME"),
            @Index(name = "OS_DAYS_RECEIVED", columnList = "DAYS_RECEIVED"),
            @Index(name = "OS_EXAMINATION_TIME", columnList = "EXAMINATION_TIME"),
            @Index(name = "OS_INSURANCE_NO", columnList = "INSURANCE_NO"),
            @Index(name = "OS_RESULTS_ID", columnList = "RESULTS_ID"),
            @Index(name = "OS_TREATMENT_REGIMENT", columnList = "TREATMENT_REGIMENT"),
            @Index(name = "OS_TREATMENT_STATUS", columnList = "TREATMENT_STATUS")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SITE_ID", "CODE"}, name = "pqm_prep_unique_site_code")
        })
public class PqmPrepEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Column(name = "FULLNAME", length = 100)
    private String fullName; //Họ và tên

    @Column(name = "PATIENT_PHONE", length = 100, nullable = true)
    private String patientPhone; //Số điện thoại bệnh nhân

    @Column(name = "type", length = 50, nullable = false)
    private String type; //Loại khách hàng

    @Column(name = "GENDER_ID", length = 50, nullable = true)
    private String genderID;

    @Column(name = "YEAR_OF_BIRTH", length = 4, nullable = true)
    private String yearOfBirth;

    @Column(name = "OBJECT_GROUP_ID", length = 50, nullable = true)
    private String objectGroupID; //Nhóm đối tượng

    @Column(name = "IDENTITY_CARD", length = 100, nullable = true)
    private String identityCard; //Chứng minh thư nhân dân

    @Column(name = "INSURANCE_NO", length = 100)
    private String insuranceNo; //BHYT

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME", nullable = true)
    private Date startTime; //Ngày bắt đầu điều trị

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME", nullable = true)
    private Date endTime; //Ngày kết thúc điều trị

    @Column(name = "END_CAUSE")
    private String endCause; //BHYT

    //Trường lưu thông tin lượt khám mới nhất
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXAMINATION_TIME", nullable = true)
    private Date examinationTime; //Ngày khám bệnh - Ngày bắt đầu điều trị với T0

    @Column(name = "RESULTS_ID", length = 50)
    private String resultsID; // Kết quả XN - Lấy thời enum kết quả khẳng định

    @Column(name = "TREATMENT_STATUS")
    private String treatmentStatus; //Kết quả điều trị - Sử dụng enum

    @Column(name = "TREATMENT_REGIMENT")
    private String treatmentRegimen; //Phác đồ điều trị

    @Column(name = "DAYS_RECEIVED", nullable = true)
    private int daysReceived; //Số ngày nhận thuốc

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPOINMENT_TIME", nullable = true)
    private Date appointmentTime; // Ngày hẹn khám

    @Column(name = "SOURCE")
    private String source; //Nguồn chuyển gửi

    @Column(name = "OTHER_SITE")
    private String otherSite; //Chuyển từ cơ sở khác (ghi rõ tên cs)

    @Column(name = "PROJECT_SUPPORT")
    private String projectSupport; //Dự án hỗ trợ

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    @Column(name = "month", columnDefinition = "integer default 0")
    private Integer month;

    @Column(name = "year", columnDefinition = "integer default 0")
    private Integer year;

    @Column(name = "quantity", columnDefinition = "integer default 0")
    private Integer quantity;

    @Column(name = "province_code", length = 50, nullable = true)
    private String province_code;

    @Column(name = "customer_id", length = 50, nullable = true)
    private String customer_id;

    @Column(name = "district_code", length = 50, nullable = true)
    private String district_code;

    @Column(name = "client_address", length = 500, nullable = true)
    private String client_address;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prep_start_date", nullable = true)
    private Date prep_start_date; //Ngày bắt đầu điều trị PrEP của cả quá trình điều trị

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public Date getPrep_start_date() {
        return prep_start_date;
    }

    public void setPrep_start_date(Date prep_start_date) {
        this.prep_start_date = prep_start_date;
    }
    
    

    public String getEndCause() {
        return endCause;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOtherSite() {
        return otherSite;
    }

    public void setOtherSite(String otherSite) {
        this.otherSite = otherSite;
    }

    public String getProjectSupport() {
        return projectSupport;
    }

    public void setProjectSupport(String projectSupport) {
        this.projectSupport = projectSupport;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public String getTreatmentRegimen() {
        return treatmentRegimen;
    }

    public void setTreatmentRegimen(String treatmentRegimen) {
        this.treatmentRegimen = treatmentRegimen;
    }

    public int getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(int daysReceived) {
        this.daysReceived = daysReceived;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
