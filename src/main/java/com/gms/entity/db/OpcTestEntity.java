package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 * @des Thông tin CD4 && HBV && HCV
 */
@Entity
@Table(name = "OPC_TEST", indexes = {
    @Index(name = "OT_SITE_ID", columnList = "SITE_ID")
    ,@Index(name = "OT_ARV_ID", columnList = "ARV_ID")
    ,@Index(name = "OT_PATIENT_ID", columnList = "PATIENT_ID")
    ,@Index(name = "OT_IS_REMOVE", columnList = "IS_REMOVE")
})
@DynamicUpdate
@DynamicInsert
public class OpcTestEntity extends BaseEntity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi

    @Column(name = "ARV_ID")
    private Long arvID; //Mã ARV

    @Column(name = "PATIENT_ID")
    private Long patientID; //FK thông tin cơ bản của bệnh nhân

    @Column(name = "LAO", nullable = false)
    private boolean lao; //Có sàng lọc lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_TEST_TIME", nullable = true)
    private Date laoTestTime; //Ngày xét nghiệm lao

    @Column(name = "LAO_OTHER_SYMPTOM", nullable = true)
    private String laoOtherSymptom; // triệu chứng khác

    @Column(name = "LAO_TREATMENT", nullable = false)
    private boolean laoTreatment; //Có điều trị lọc lao

    @Column(name = "LAO_RESULT", length = 50, nullable = true)
    private String laoResult; //Kết quả xét nghiệm Lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_START_TIME", nullable = true)
    private Date laoStartTime; //Ngày bắt đầu điều trị lao

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_END_TIME", nullable = true)
    private Date laoEndTime; //Ngày kết thúc điều trị lao

    @Column(name = "INH", nullable = false)
    private boolean inh; //Điều trị dự phòng INH - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_FROM_TIME", nullable = true)
    private Date inhFromTime; //Lao từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INH_TO_TIME", nullable = true)
    private Date inhToTime; //Lao đến ngày

    @Column(name = "NTCH", nullable = true)
    private boolean ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum

    @Column(name = "NTCH_OTHER_SYMPTOM", nullable = true)
    private String ntchOtherSymptom; // triệu chứng khác ntch

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_FROM_TIME", nullable = true)
    private Date cotrimoxazoleFromTime; //cotrimoxazole từ ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COTRIMOXAZOLE_TO_TIME", nullable = true)
    private Date cotrimoxazoleToTime; //cotrimoxazole đến ngày

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CD4_SAMPLE_TIME", nullable = true)
    private Date cd4SampleTime; // Ngày lấy mẫu

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CD4_TEST_TIME", nullable = true)
    private Date cd4TestTime; // Ngày xét nghiệm

    @Column(name = "CD4_TEST_SITE_ID", nullable = true)
    private Long cd4TestSiteID; //Cơ sở xét nghiệm

    @Column(name = "CD4_TEST_SITE_NAME", nullable = true)
    private String cd4TestSiteName; //Hoặc Tên Cơ sở xét nghiệm

    @Column(name = "CD4_RESULT", nullable = true)
    private String cd4Result; //Kết quả xét nghiệm

    @Temporal(TemporalType.DATE)
    @Column(name = "CD4_RESULT_TIME", nullable = true)
    private Date cd4ResultTime; //NGày trả kết quả

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETRY_TIME", nullable = true)
    private Date cd4RetryTime; // Ngày hẹn xét nghiệm lại

    @Column(name = "HBV", nullable = false)
    private boolean hbv; //Có xn hbv hay không - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HBV_TIME", nullable = true)
    private Date hbvTime; // Ngày xét nghiệm hbv

    @Column(name = "HBV_RESULT", length = 1, nullable = true)
    private String hbvResult; //Kết quả XN HBV

    @Column(name = "HBV_CASE", length = 500, nullable = true)
    private String hbvCase; //Lý do XN HBV

    @Column(name = "HCV", nullable = false)
    private boolean hcv; //Có xn HCV hay không - sử dụng enum

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HCV_TIME", nullable = true)
    private Date hcvTime; // Ngày xét nghiệm HCV

    @Column(name = "HCV_RESULT", length = 1, nullable = true)
    private String hcvResult; //Kết quả XN HCV

    @Column(name = "HCV_CASE", length = 500, nullable = true)
    private String hcvCase; //Lý do XN HCV

    @Column(name = "NOTE", length = 500, nullable = true)
    private String note; //Ghi chú

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

    @Column(name = "IS_REMOVE")
    private boolean remove; //Xoá logic

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REMOVE_AT", nullable = true)
    private Date remoteAT;

    @Column(name = "STAGE_ID")
    private Long stageID; // Mã giai đoạn điều trị

    @Column(name = "SUSPICIOUS_SYMPTOMS", length = 50, nullable = true)
    private String suspiciousSymptoms; // Triệu chứng nghi ngờ

    @Column(name = "EXAMINATION_AND_TEST", nullable = false)
    private boolean examinationAndTest; //Chuyển gửi khám và xét nghiệm

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAO_TEST_DATE", nullable = true)
    private Date laoTestDate; // Ngày xét nghiệm Lao

    @Column(name = "LAO_DIAGNOSE", length = 50, nullable = true)
    private String laoDiagnose; // Chẩn đoán loại hình Lao

    @Column(name = "COTRIMOXAZOLE_OTHER_END_CAUSE", length = 255, nullable = true)
    private String cotrimoxazoleOtherEndCause; // Lý do kết thúc cotrimoxazole khác

    @Column(name = "STATUS_OF_TREATMENT_ID", length = 50, nullable = true)
    private String statusOfTreatmentID; //Trạng thái điều trị

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }
    
    
    //Trường chọn nhiều
    @Transient
    private List<String> laoSymptoms; //Các biểu hiện nghi lao

    @Transient
    private List<String> inhEndCauses; //Lý do kết thúc lao - inh

    @Transient
    private List<String> ntchSymptoms; //Các biểu hiện ntch

    @Transient
    private List<String> cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole

    @Transient
    private List<String> cd4Causes; //Lý do xét nghiệm cd4 gần nhất

    public String getCotrimoxazoleOtherEndCause() {
        return cotrimoxazoleOtherEndCause;
    }

    public void setCotrimoxazoleOtherEndCause(String cotrimoxazoleOtherEndCause) {
        this.cotrimoxazoleOtherEndCause = cotrimoxazoleOtherEndCause;
    }

    public String getSuspiciousSymptoms() {
        return suspiciousSymptoms;
    }

    public void setSuspiciousSymptoms(String suspiciousSymptoms) {
        this.suspiciousSymptoms = suspiciousSymptoms;
    }

    public boolean isExaminationAndTest() {
        return examinationAndTest;
    }

    public void setExaminationAndTest(boolean examinationAndTest) {
        this.examinationAndTest = examinationAndTest;
    }

    public Date getLaoTestDate() {
        return laoTestDate;
    }

    public void setLaoTestDate(Date laoTestDate) {
        this.laoTestDate = laoTestDate;
    }

    public String getLaoDiagnose() {
        return laoDiagnose;
    }

    public void setLaoDiagnose(String laoDiagnose) {
        this.laoDiagnose = laoDiagnose;
    }

    public OpcTestEntity() {
        this.laoTreatment = false;
    }

    public boolean isLaoTreatment() {
        return laoTreatment;
    }

    public void setLaoTreatment(boolean laoTreatment) {
        this.laoTreatment = laoTreatment;
    }

    public String getLaoResult() {
        return laoResult;
    }

    public void setLaoResult(String laoResult) {
        this.laoResult = laoResult;
    }

    public Date getLaoStartTime() {
        return laoStartTime;
    }

    public void setLaoStartTime(Date laoStartTime) {
        this.laoStartTime = laoStartTime;
    }

    public Date getLaoEndTime() {
        return laoEndTime;
    }

    public void setLaoEndTime(Date laoEndTime) {
        this.laoEndTime = laoEndTime;
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

    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public boolean isLao() {
        return lao;
    }

    public void setLao(boolean lao) {
        this.lao = lao;
    }

    public Date getLaoTestTime() {
        return laoTestTime;
    }

    public void setLaoTestTime(Date laoTestTime) {
        this.laoTestTime = laoTestTime;
    }

    public String getLaoOtherSymptom() {
        return laoOtherSymptom;
    }

    public void setLaoOtherSymptom(String laoOtherSymptom) {
        this.laoOtherSymptom = laoOtherSymptom;
    }

    public boolean isInh() {
        return inh;
    }

    public void setInh(boolean inh) {
        this.inh = inh;
    }

    public Date getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(Date inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public Date getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(Date inhToTime) {
        this.inhToTime = inhToTime;
    }

    public boolean isNtch() {
        return ntch;
    }

    public void setNtch(boolean ntch) {
        this.ntch = ntch;
    }

    public String getNtchOtherSymptom() {
        return ntchOtherSymptom;
    }

    public void setNtchOtherSymptom(String ntchOtherSymptom) {
        this.ntchOtherSymptom = ntchOtherSymptom;
    }

    public Date getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(Date cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public Date getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(Date cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public Date getCd4SampleTime() {
        return cd4SampleTime;
    }

    public void setCd4SampleTime(Date cd4SampleTime) {
        this.cd4SampleTime = cd4SampleTime;
    }

    public Date getCd4TestTime() {
        return cd4TestTime;
    }

    public void setCd4TestTime(Date cd4TestTime) {
        this.cd4TestTime = cd4TestTime;
    }

    public Long getCd4TestSiteID() {
        return cd4TestSiteID;
    }

    public void setCd4TestSiteID(Long cd4TestSiteID) {
        this.cd4TestSiteID = cd4TestSiteID;
    }

    public String getCd4TestSiteName() {
        return cd4TestSiteName;
    }

    public void setCd4TestSiteName(String cd4TestSiteName) {
        this.cd4TestSiteName = cd4TestSiteName;
    }

    public String getCd4Result() {
        return cd4Result;
    }

    public void setCd4Result(String cd4Result) {
        this.cd4Result = cd4Result;
    }

    public Date getCd4ResultTime() {
        return cd4ResultTime;
    }

    public void setCd4ResultTime(Date cd4ResultTime) {
        this.cd4ResultTime = cd4ResultTime;
    }

    public Date getCd4RetryTime() {
        return cd4RetryTime;
    }

    public void setCd4RetryTime(Date cd4RetryTime) {
        this.cd4RetryTime = cd4RetryTime;
    }

    public boolean isHbv() {
        return hbv;
    }

    public void setHbv(boolean hbv) {
        this.hbv = hbv;
    }

    public Date getHbvTime() {
        return hbvTime;
    }

    public void setHbvTime(Date hbvTime) {
        this.hbvTime = hbvTime;
    }

    public String getHbvResult() {
        return hbvResult;
    }

    public void setHbvResult(String hbvResult) {
        this.hbvResult = hbvResult;
    }

    public String getHbvCase() {
        return hbvCase;
    }

    public void setHbvCase(String hbvCase) {
        this.hbvCase = hbvCase;
    }

    public boolean isHcv() {
        return hcv;
    }

    public void setHcv(boolean hcv) {
        this.hcv = hcv;
    }

    public Date getHcvTime() {
        return hcvTime;
    }

    public void setHcvTime(Date hcvTime) {
        this.hcvTime = hcvTime;
    }

    public String getHcvResult() {
        return hcvResult;
    }

    public void setHcvResult(String hcvResult) {
        this.hcvResult = hcvResult;
    }

    public String getHcvCase() {
        return hcvCase;
    }

    public void setHcvCase(String hcvCase) {
        this.hcvCase = hcvCase;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Date getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(Date remoteAT) {
        this.remoteAT = remoteAT;
    }

    public List<String> getLaoSymptoms() {
        return laoSymptoms;
    }

    public void setLaoSymptoms(List<String> laoSymptoms) {
        this.laoSymptoms = laoSymptoms;
    }

    public List<String> getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(List<String> inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public List<String> getNtchSymptoms() {
        return ntchSymptoms;
    }

    public void setNtchSymptoms(List<String> ntchSymptoms) {
        this.ntchSymptoms = ntchSymptoms;
    }

    public List<String> getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(List<String> cotrimoxazoleEndCauses) {
        this.cotrimoxazoleEndCauses = cotrimoxazoleEndCauses;
    }

    public List<String> getCd4Causes() {
        return cd4Causes;
    }

    public void setCd4Causes(List<String> cd4Causes) {
        this.cd4Causes = cd4Causes;
    }

    @Override
    public OpcTestEntity clone() throws CloneNotSupportedException {
        return (OpcTestEntity) super.clone();
    }

    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
    }

}
