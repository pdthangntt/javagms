package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.OpcViralLoadEntity;
import static com.gms.entity.form.opc_arv.OpcStageForm.FORMATDATE;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Form sửa thông tin tải lượng virus
 *
 * @author TrangBN
 */
public class OpcViralLoadForm implements Serializable {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    private Long ID;
    private String siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi
    private Long arvID; //Mã ARV
    private Long patientID; //FK thông tin cơ bản của bệnh nhân
    private String sampleTime; // Ngày lấy mẫu
    private String testTime; // TLVR - Ngày xét nghiệm
    private String testSiteID; //Cơ sở xét nghiệm
    private String testSiteName; //Hoặc Tên Cơ sở xét nghiệm
    private String result; //Kết quả xét nghiệm TLVR
    private String resultNumber; //Kết quả xét nghiệm TLVR nhập số
    private String resultTime; //NGày trả kết quả
    private String retryTime; // Ngày hẹn xét nghiệm lại
    private String firstViralLoadTime; // Ngày hẹn xét nghiệm tlvr đâu tiên
    private String note; //Ghi chú
    private boolean remove; //Xoá logic
    private String remoteAT;
    private List<String> causes; //TLVR - lý do xét nghiệm
    private boolean OPC;
    private boolean OpcManager;
    private String stageID; // Mã giai đoạn điều trị
    private HashMap<String, String> stages; // Mã giai đoạn điều trị

    public HashMap<String, String> getStages() {
        return stages;
    }

    public void setStages(HashMap<String, String> stages) {
        this.stages = stages;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    // Thông tin bênh nhân
    private String confirmTime;

    public String getFirstViralLoadTime() {
        return firstViralLoadTime;
    }

    public void setFirstViralLoadTime(String firstViralLoadTime) {
        this.firstViralLoadTime = firstViralLoadTime;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public boolean isOPC() {
        return OPC;
    }

    public void setOPC(boolean OPC) {
        this.OPC = OPC;
    }

    public boolean isOpcManager() {
        return OpcManager;
    }

    public void setOpcManager(boolean OpcManager) {
        this.OpcManager = OpcManager;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
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

    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTestSiteID() {
        return testSiteID;
    }

    public void setTestSiteID(String testSiteID) {
        this.testSiteID = testSiteID;
    }

    public String getTestSiteName() {
        return testSiteName;
    }

    public void setTestSiteName(String testSiteName) {
        this.testSiteName = testSiteName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(String retryTime) {
        this.retryTime = retryTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getRemoteAT() {
        return remoteAT;
    }

    public void setRemoteAT(String remoteAT) {
        this.remoteAT = remoteAT;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    /**
     * Set từ entity sang form
     *
     * @param entity
     */
    public void setForm(OpcViralLoadEntity entity) {

        this.setID(entity.getID());
        this.setSiteID(entity.getSiteID() != null ? entity.getSiteID().toString() : null);
        this.setArvID(entity.getArvID());
        this.setPatientID(entity.getPatientID());
        this.setSampleTime(TextUtils.formatDate(entity.getSampleTime(), FORMATDATE));
        this.setTestTime(TextUtils.formatDate(entity.getTestTime(), FORMATDATE));
        this.setTestSiteID(entity.getTestSiteID() != null ? entity.getTestSiteID().toString() : null);
        this.setTestSiteName(entity.getTestSiteName());
        this.setResult(entity.getResult());
        this.setResultTime(TextUtils.formatDate(entity.getResultTime(), FORMATDATE));
        this.setRetryTime(TextUtils.formatDate(entity.getRetryTime(), FORMATDATE));
        this.setNote(entity.getNote());
        this.setRemove(entity.isRemove());
        this.setRemoteAT(TextUtils.formatDate(entity.getRemoteAT(), FORMATDATE));
        this.setCauses(entity.getCauses());
        this.setResultNumber(entity.getResultNumber());
        this.setStageID(String.valueOf(entity.getStageID()));
    }

    /**
     * Chuyển từ form sang entity
     *
     * @param viralEntity
     * @return
     */
    public OpcViralLoadEntity toForm(OpcViralLoadEntity viralEntity) {
        if (viralEntity == null) {
            viralEntity = new OpcViralLoadEntity();
        }

        viralEntity.setSiteID(StringUtils.isNotEmpty(getSiteID()) ? Long.parseLong(getSiteID()) : null);
        viralEntity.setArvID(getArvID());
        viralEntity.setPatientID(getPatientID());
        viralEntity.setSampleTime(StringUtils.isNotEmpty(getSampleTime()) ? TextUtils.convertDate(!getSampleTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getSampleTime()) : getSampleTime(), FORMATDATE) : null);
        viralEntity.setTestTime(StringUtils.isNotEmpty(getTestTime()) ? TextUtils.convertDate(!getTestTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getTestTime()) : getTestTime(), FORMATDATE) : null);
        viralEntity.setTestSiteID(StringUtils.isNotEmpty(getTestSiteID()) ? Long.parseLong(getTestSiteID()) : null);
        viralEntity.setTestSiteName(getTestSiteName());
        viralEntity.setResult(getResult());
        viralEntity.setResultTime(StringUtils.isNotEmpty(getResultTime()) ? TextUtils.convertDate(!getResultTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getResultTime()) : getResultTime(), FORMATDATE) : null);
        viralEntity.setRetryTime(StringUtils.isNotEmpty(getRetryTime()) ? TextUtils.convertDate(!getRetryTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getRetryTime()) : getRetryTime(), FORMATDATE) : null);
        viralEntity.setNote(getNote());
        viralEntity.setRemove(isRemove());
        viralEntity.setRemoteAT(StringUtils.isNotEmpty(getRemoteAT()) ? TextUtils.convertDate(!getRemoteAT().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getRemoteAT()) : getRemoteAT(), FORMATDATE) : null);
        viralEntity.setCauses(getCauses());
        viralEntity.setResultNumber(getResultNumber());
        viralEntity.setStageID(Long.valueOf(getStageID()));

        return viralEntity;
    }

}
