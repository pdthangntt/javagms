package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author TrangBN
 */
public class OpcStageForm implements Serializable {

    protected static final String FORMATDATE = "dd/MM/yyyy";
    // Thông tin bệnh nhân
    private String name;
    private String birthday;
    private String dob; // ngày sinh
    private String identityCard; // CMND
    private String genderID; // Giới tính
    private String confirmCode;
    private String confirmTime;
    private String confirmSiteID; // Nơi XNKĐ
    private String confirmSiteName; // Nơi XNKĐ
    
    // Thông tin đăng ký điều trị
    private Long ID;
    private String siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi
    private Long arvID; //Mã ARV
    private Long patientID; //FK thông tin cơ bản của bệnh nhân
    private String registrationTime; //NGày đăng ký vào OPC
    private String registrationType; //Loại đăng ký - Sử dụng enum
    private String sourceSiteID; //mã cơ sở nguồn gửi đến - Tự động lấy từ place-test hoặc nguồn chuyển
    private String sourceSiteName; //Tên cơ sở nguồn gửi đến
    private String sourceCode; //Mã bệnh án từ cơ sở khác chuyển tới
    private String sourceCodeDefault; //Mã bệnh án từ cơ sở khác chuyển tới mặc định nếu loại đăng ký là chuyển đến
    private String clinicalStage; // Giai đoạn lâm sàng
    private String cd4; //CD4 hoặc cd4%
    private String statusOfTreatmentID; //Trạng thái điều trị
    private String firstTreatmentTime; //Ngày điều trị ARV đầu tiên tại cơ sở OPC
    private String treatmentTime; //Ngày ARV tại cơ sở OPC
    private String treatmentRegimenStage; //Bậc phác đồ điều trị - sử dụng enum
    private String treatmentRegimenID; //Phác đồ điều trị tại cơ sở OPC
    private String lastExaminationTime; //Ngày khám bệnh gần nhất
    private String medicationAdherence; //Tuân thủ điều trị
    private String daysReceived; //Số ngày nhận thuốc
    private String appointmentTime; // Ngày hẹn khám   
    private String examinationNote; //Các vấn đề trong lần khám gần nhất
    private String endTime; // Ngày kết thúc tại OPC
    private String endCase; //Lý do kết thúc điều trị
    private String transferSiteID; //Cơ sở chuyển đi
    private String transferSiteName; //Tên Cơ sở chuyển đi
    private String transferCase; //Lý do chuyển đi
    private String note; // Ghi chú
    private boolean remove; //Xoá logic
    private String remoteAT;

    // Thông tin điều trị ARV
    private String treatmentStageTime; // Phác đồ điều tri đầu tiên    
    private String treatmentStageID; // Trạng thái biến động điều trị
    private String regimenStageDate; // Ngày thay đổi bậc phác đồ
    private String regimenDate; // Ngày thay đổi phác đồ
    private String oldTreatmentRegimenStage; //Bậc phác đồ điều trị cũ- sử dụng enum 
    private String baseTreatmentRegimenStage; //Bậc phác đồ điều trị cũ - lưu db đem so sánh
    private String oldTreatmentRegimenID; //Phác đồ điều trị cũ tại cơ sở OPC  
    private String baseTreatmentRegimenID; //Phác đồ điều trị cũ lưu db - lưu db đem so sánh
    private String causesChange; // Lý do thay đổi
    private String supplyMedicinceDate;// Ngày đầu tiên cáp thuốc nhiều tháng
    private String receivedWardDate; // Ngày nhận thuốc tại xã
    private String arvCode; // Mã bệnh án

    // Thông tin thêm
    private String action;
    private String createAt;
    private String updateAt;
    private boolean OPC;
    private boolean OpcManager;
    private boolean editable;
    private Long currentSiteID;
    private String qualifiedTreatmentTime;
    private String objectGroupID; //Nhóm đối tượng
    private String pregnantStartDate; // Ngày bắt đầu thai ký
    private String pregnantEndDate; // Ngày chuyển dạ đẻ
    private String birthPlanDate; // Ngày dự sinh
    
    private List<OpcChildForm> children; // Danh sách trẻ em được sinh từ mẹ nhiễm HIV

    public String getBirthPlanDate() {
        return birthPlanDate;
    }

    public void setBirthPlanDate(String birthPlanDate) {
        this.birthPlanDate = birthPlanDate;
    }
    
    public String getBaseTreatmentRegimenStage() {
        return baseTreatmentRegimenStage;
    }

    public void setBaseTreatmentRegimenStage(String baseTreatmentRegimenStage) {
        this.baseTreatmentRegimenStage = baseTreatmentRegimenStage;
    }

    public String getBaseTreatmentRegimenID() {
        return baseTreatmentRegimenID;
    }

    public void setBaseTreatmentRegimenID(String baseTreatmentRegimenID) {
        this.baseTreatmentRegimenID = baseTreatmentRegimenID;
    }
    
    public String getArvCode() {
        return arvCode;
    }

    public void setArvCode(String arvCode) {
        this.arvCode = arvCode;
    }
    
    public List<OpcChildForm> getChildren() {
        return children;
    }

    public void setChildren(List<OpcChildForm> children) {
        this.children = children;
    }
    
    public String getPregnantStartDate() {
        return pregnantStartDate;
    }

    public void setPregnantStartDate(String pregnantStartDate) {
        this.pregnantStartDate = pregnantStartDate;
    }

    public String getPregnantEndDate() {
        return pregnantEndDate;
    }

    public void setPregnantEndDate(String pregnantEndDate) {
        this.pregnantEndDate = pregnantEndDate;
    }
    
    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getReceivedWardDate() {
        return receivedWardDate;
    }

    public void setReceivedWardDate(String receivedWardDate) {
        this.receivedWardDate = receivedWardDate;
    }
    
    public String getSupplyMedicinceDate() {
        return supplyMedicinceDate;
    }

    public void setSupplyMedicinceDate(String supplyMedicinceDate) {
        this.supplyMedicinceDate = supplyMedicinceDate;
    }
    
    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
    }
    
    public String getOldTreatmentRegimenStage() {
        return oldTreatmentRegimenStage;
    }

    public void setOldTreatmentRegimenStage(String oldTreatmentRegimenStage) {
        this.oldTreatmentRegimenStage = oldTreatmentRegimenStage;
    }

    public String getOldTreatmentRegimenID() {
        return oldTreatmentRegimenID;
    }

    public void setOldTreatmentRegimenID(String oldTreatmentRegimenID) {
        this.oldTreatmentRegimenID = oldTreatmentRegimenID;
    }
    
    public String getRegimenStageDate() {
        return regimenStageDate;
    }

    public void setRegimenStageDate(String regimenStageDate) {
        this.regimenStageDate = regimenStageDate;
    }

    public String getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(String regimenDate) {
        this.regimenDate = regimenDate;
    }
    
    public String getQualifiedTreatmentTime() {
        return qualifiedTreatmentTime;
    }

    public void setQualifiedTreatmentTime(String qualifiedTreatmentTime) {
        this.qualifiedTreatmentTime = qualifiedTreatmentTime;
    }
    
    public String getFirstTreatmentTime() {
        return firstTreatmentTime;
    }

    public void setFirstTreatmentTime(String firstTreatmentTime) {
        this.firstTreatmentTime = firstTreatmentTime;
    }
    
    public Long getCurrentSiteID() {
        return currentSiteID;
    }

    public void setCurrentSiteID(Long currentSiteID) {
        this.currentSiteID = currentSiteID;
    }
    
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getSourceCodeDefault() {
        return sourceCodeDefault;
    }

    public void setSourceCodeDefault(String sourceCodeDefault) {
        this.sourceCodeDefault = sourceCodeDefault;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTreatmentStageTime() {
        return treatmentStageTime;
    }

    public void setTreatmentStageTime(String treatmentStageTime) {
        this.treatmentStageTime = treatmentStageTime;
    }

    public String getTreatmentStageID() {
        return treatmentStageID;
    }

    public void setTreatmentStageID(String treatmentStageID) {
        this.treatmentStageID = treatmentStageID;
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

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSourceSiteName() {
        return sourceSiteName;
    }

    public void setSourceSiteName(String sourceSiteName) {
        this.sourceSiteName = sourceSiteName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public String getCd4() {
        return cd4;
    }

    public void setCd4(String cd4) {
        this.cd4 = cd4;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getTreatmentRegimenStage() {
        return treatmentRegimenStage;
    }

    public void setTreatmentRegimenStage(String treatmentRegimenStage) {
        this.treatmentRegimenStage = treatmentRegimenStage;
    }

    public String getTreatmentRegimenID() {
        return treatmentRegimenID;
    }

    public void setTreatmentRegimenID(String treatmentRegimenID) {
        this.treatmentRegimenID = treatmentRegimenID;
    }

    public String getLastExaminationTime() {
        return lastExaminationTime;
    }

    public void setLastExaminationTime(String lastExaminationTime) {
        this.lastExaminationTime = lastExaminationTime;
    }

    public String getMedicationAdherence() {
        return medicationAdherence;
    }

    public void setMedicationAdherence(String medicationAdherence) {
        this.medicationAdherence = medicationAdherence;
    }

    public String getDaysReceived() {
        return daysReceived;
    }

    public void setDaysReceived(String daysReceived) {
        this.daysReceived = daysReceived;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getExaminationNote() {
        return examinationNote;
    }

    public void setExaminationNote(String examinationNote) {
        this.examinationNote = examinationNote;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getTransferSiteID() {
        return transferSiteID;
    }

    public void setTransferSiteID(String transferSiteID) {
        this.transferSiteID = transferSiteID;
    }

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public String getTransferCase() {
        return transferCase;
    }

    public void setTransferCase(String transferCase) {
        this.transferCase = transferCase;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmSiteID() {
        return confirmSiteID;
    }

    public void setConfirmSiteID(String confirmSiteID) {
        this.confirmSiteID = confirmSiteID;
    }

    public String getConfirmSiteName() {
        return confirmSiteName;
    }

    public void setConfirmSiteName(String confirmSiteName) {
        this.confirmSiteName = confirmSiteName;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    /**
     * Chuyển thông tin bệnh nhân sang OpcStageForm từ OpcPatientEntity
     *
     * @param entity
     */
    public void setFromPatientEntity(OpcPatientEntity entity) {

        setName(entity.getFullName());
        setGenderID(entity.getGenderID());
        setDob(TextUtils.formatDate(entity.getDob(), FORMATDATE));
        setIdentityCard(entity.getIdentityCard());
        setConfirmCode(entity.getConfirmCode());
        setConfirmTime(TextUtils.formatDate(entity.getConfirmTime(), FORMATDATE));
        setConfirmSiteName(entity.getConfirmSiteName());
        setConfirmSiteID(entity.getConfirmSiteID() == null ? null : entity.getConfirmSiteID().toString());
    }

    /**
     * Chuyển thông tin entity sang form từ OpcStageEntity
     *
     * @param entity
     * @return
     */
    public OpcStageForm setFromStage(OpcStageEntity entity) {
        this.setID(entity.getID());
        this.setSiteID(entity.getSiteID() == null ? null : entity.getSiteID().toString());
        this.setArvID(entity.getArvID());
        this.setPatientID(entity.getPatientID());
        this.setRegistrationTime(TextUtils.formatDate(entity.getRegistrationTime(), FORMATDATE)); //NGày đăng ký vào OPC
        this.setRegistrationType(entity.getRegistrationType());
        this.setSourceSiteID(entity.getSourceSiteID() == null || entity.getSourceSiteID().equals(0L) ? null : entity.getSourceSiteID().toString());
        this.setSourceSiteName(entity.getSourceSiteName());
        this.setSourceCode(entity.getSourceCode());
        this.setClinicalStage(entity.getClinicalStage());
        this.setCd4(entity.getCd4());
        this.setStatusOfTreatmentID(entity.getStatusOfTreatmentID());
        this.setTreatmentTime(TextUtils.formatDate(entity.getTreatmentTime(), FORMATDATE));
        this.setTreatmentRegimenStage(entity.getTreatmentRegimenStage());
        this.setTreatmentRegimenID(entity.getTreatmentRegimenID());
        this.setLastExaminationTime(TextUtils.formatDate(entity.getLastExaminationTime(), FORMATDATE));
        this.setMedicationAdherence(entity.getMedicationAdherence());
        this.setDaysReceived(entity.getDaysReceived() == 0 ? "" : String.valueOf(entity.getDaysReceived()));
        this.setAppointmentTime(TextUtils.formatDate(entity.getAppointmentTime(), FORMATDATE));
        this.setExaminationNote(entity.getExaminationNote());
        this.setEndTime(TextUtils.formatDate(entity.getEndTime(), FORMATDATE));
        this.setEndCase(entity.getEndCase());
        this.setTransferSiteID(entity.getTransferSiteID() != null ? entity.getTransferSiteID().toString() : null);
        this.setTransferSiteName(entity.getTransferSiteName());
        this.setTransferCase(entity.getTransferCase());
        this.setNote(entity.getNote());
        this.setRemove(entity.isRemove());
        this.setRemoteAT(TextUtils.formatDate(entity.getRemoteAT(), FORMATDATE));
        this.setCreateAt(TextUtils.formatDate(entity.getCreateAT(), FORMATDATE));
        this.setUpdateAt(TextUtils.formatDate(entity.getUpdateAt(), FORMATDATE));
        this.setTreatmentStageTime(TextUtils.formatDate(entity.getTreatmentStageTime(), FORMATDATE));
        this.setTreatmentStageID(entity.getTreatmentStageID());
        this.setObjectGroupID(entity.getObjectGroupID());
        this.setOldTreatmentRegimenID(entity.getOldTreatmentRegimenID());
        this.setOldTreatmentRegimenStage(entity.getOldTreatmentRegimenStage());
        this.setRegimenStageDate(TextUtils.formatDate(entity.getRegimenStageDate(), FORMATDATE));
        this.setRegimenDate(TextUtils.formatDate(entity.getRegimenDate(), FORMATDATE));
        this.setCausesChange(entity.getCausesChange());
        this.setSupplyMedicinceDate(TextUtils.formatDate(entity.getSupplyMedicinceDate(), FORMATDATE));
        this.setReceivedWardDate(TextUtils.formatDate(entity.getReceivedWardDate(), FORMATDATE));
        this.setPregnantStartDate(TextUtils.formatDate(entity.getPregnantStartDate(), FORMATDATE));
        this.setPregnantEndDate(TextUtils.formatDate(entity.getPregnantEndDate(), FORMATDATE));
        this.setBirthPlanDate(TextUtils.formatDate(entity.getBirthPlanDate(), FORMATDATE));
        this.setQualifiedTreatmentTime(TextUtils.formatDate(entity.getQualifiedTreatmentTime(), FORMATDATE));
        
        return this;
    }

    /**
     * Chuyển từ form sang entity
     *
     * @param entity
     * @return
     */
    public OpcStageEntity convertStageForm(OpcStageEntity entity) {
        if (entity == null) {
            entity = new OpcStageEntity();
        }
        if(StringUtils.isNotEmpty(getRegistrationTime()) && !getRegistrationTime().contains("/")){
            setRegistrationTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, getRegistrationTime()));
        }
        entity.setSiteID(StringUtils.isEmpty(getSiteID()) ? null : Long.parseLong(getSiteID()));
        entity.setArvID(getArvID());
        entity.setPatientID(entity.getPatientID());
        entity.setRegistrationTime(StringUtils.isNotEmpty(getRegistrationTime()) ? (getRegistrationTime().equals(TextUtils.formatDate(new Date(), FORMATDATE)) ? new Date() : TextUtils.convertDate(getRegistrationTime(), FORMATDATE)) : null);
        entity.setRegistrationType(getRegistrationType());
        entity.setSourceSiteID(StringUtils.isEmpty(getSourceSiteID()) ? null : Long.parseLong(getSourceSiteID()));
        entity.setSourceSiteName(getSourceSiteName());
        entity.setSourceCode(getSourceCode());
        entity.setClinicalStage(getClinicalStage());
        entity.setCd4(getCd4());
        entity.setStatusOfTreatmentID(getStatusOfTreatmentID());
        entity.setTreatmentTime(StringUtils.isNotEmpty(getTreatmentTime()) ? TextUtils.convertDate(!getTreatmentTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getTreatmentTime()) : getTreatmentTime(), FORMATDATE) : null);
        entity.setTreatmentRegimenStage(getTreatmentRegimenStage());
        entity.setTreatmentRegimenID(getTreatmentRegimenID());
        entity.setLastExaminationTime(StringUtils.isNotEmpty(getLastExaminationTime()) ? TextUtils.convertDate(!getLastExaminationTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getLastExaminationTime()) : getLastExaminationTime(), FORMATDATE) : null);
        entity.setMedicationAdherence(getMedicationAdherence());
        entity.setDaysReceived(StringUtils.isNotEmpty(getDaysReceived()) ? Integer.parseInt(getDaysReceived()) : 0);
        entity.setAppointmentTime(StringUtils.isNotEmpty(getAppointmentTime()) ? TextUtils.convertDate(!getAppointmentTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getAppointmentTime()) : getAppointmentTime(), FORMATDATE) : null);
        entity.setExaminationNote(getExaminationNote());
        entity.setEndTime(StringUtils.isNotEmpty(getEndTime()) ? TextUtils.convertDate(!getEndTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getEndTime()) : getEndTime(), FORMATDATE) : null);
        entity.setEndCase(getEndCase());
        entity.setTransferSiteID(StringUtils.isNotEmpty(getTransferSiteID()) ? Long.parseLong(getTransferSiteID()) : null);
        entity.setTransferSiteName(getTransferSiteName());
        entity.setTransferCase(getTransferCase());
        entity.setNote(getNote());
        entity.setTreatmentStageTime(StringUtils.isNotEmpty(getTreatmentStageTime()) ? TextUtils.convertDate(getTreatmentStageTime(), FORMATDATE) : null);
        entity.setTreatmentStageID(getTreatmentStageID());
        entity.setObjectGroupID(getObjectGroupID());
        entity.setPregnantStartDate(TextUtils.convertDate(getPregnantStartDate(), FORMATDATE) );
        entity.setPregnantEndDate(TextUtils.convertDate(getPregnantEndDate(), FORMATDATE) );
        entity.setBirthPlanDate(TextUtils.convertDate(getBirthPlanDate(), FORMATDATE) );
        entity.setOldTreatmentRegimenID(getOldTreatmentRegimenID());
        entity.setOldTreatmentRegimenStage(getOldTreatmentRegimenStage());
        entity.setRegimenStageDate(TextUtils.convertDate(getRegimenStageDate(), FORMATDATE));
        entity.setRegimenDate(TextUtils.convertDate(getRegimenDate(), FORMATDATE));
        entity.setQualifiedTreatmentTime(TextUtils.convertDate(getQualifiedTreatmentTime(), FORMATDATE));
        entity.setReceivedWardDate(TextUtils.convertDate(getReceivedWardDate(), FORMATDATE));
        entity.setSupplyMedicinceDate(TextUtils.convertDate(getSupplyMedicinceDate(), FORMATDATE));
        entity.setCausesChange(StringUtils.isNotEmpty(getCausesChange()) ?  getCausesChange().substring(getCausesChange().length() - 1).equals(",") ? getCausesChange().substring(0, getCausesChange().length() - 1) : getCausesChange() : getCausesChange() );
        
        return entity;
    }
    
    /**
     * Form pop up cập nhật kết thúc
     * 
     * @param entity
     * @return 
     */
    public OpcStageEntity convertStage(OpcStageEntity entity) {
        
        entity.setEndTime(StringUtils.isNotEmpty(getEndTime()) ? TextUtils.convertDate(!getEndTime().contains("/") ? TextUtils.formatDate("ddMMyyyy", FORMATDATE, getEndTime()) : getEndTime(), FORMATDATE) : null);
        entity.setEndCase(getEndCase());
        entity.setTransferSiteID(StringUtils.isNotEmpty(getTransferSiteID()) ? Long.parseLong(getTransferSiteID()) : null);
        entity.setTransferSiteName(getTransferSiteName());
        entity.setTransferCase(getTransferCase());
        entity.setTreatmentStageID(getTreatmentStageID());
        entity.setTreatmentStageTime(StringUtils.isNotEmpty(getTreatmentStageTime()) ? TextUtils.convertDate(getTreatmentStageTime(), FORMATDATE) : null);
        
        // Set trạng thái điều trị statusOfTreatmentID
        if (entity.getEndCase().equals(ArvEndCaseEnum.CANCELLED.getKey())) {
            entity.setStatusOfTreatmentID(StatusOfTreatmentEnum.BO_TRI.getKey());
        }
        if (entity.getEndCase().equals(ArvEndCaseEnum.LOSE_TRACK.getKey())) {
            entity.setStatusOfTreatmentID(StatusOfTreatmentEnum.MAT_DAU.getKey());
        }
        if (entity.getEndCase().equals(ArvEndCaseEnum.DEAD.getKey())) {
            entity.setStatusOfTreatmentID(StatusOfTreatmentEnum.TU_VONG.getKey());
        }
        if (entity.getEndCase().equals(ArvEndCaseEnum.MOVED_AWAY.getKey()) || entity.getEndCase().equals(ArvEndCaseEnum.END.getKey())) {
            entity.setStatusOfTreatmentID(getStatusOfTreatmentID());
        }
        
        return entity;
    }

}
