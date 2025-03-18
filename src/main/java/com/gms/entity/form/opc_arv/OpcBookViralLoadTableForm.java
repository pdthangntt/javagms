package com.gms.entity.form.opc_arv;

import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.ViralLoadSymtomEnum;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Form bảng sổ tlvr
 * 
 * @author TrangBN
 */
public class OpcBookViralLoadTableForm {

    // Tên báo cáo
    private String fullName; //Họ và tên
    private String code; // Mã khách hàng
    private String insuranceNo; // Mã khách hàng
    private String patientPhone; // Số điện thoại bệnh nhân
    private String dob; // Ngày sinh
    private String genderID; //Giới tính
    private Date treatmentTime; // Ngày bắt đầu điều trị ARV
    private Date testTime; // Ngày xét nghiệm TLVR
    private String statusOfTreatmentID; // trạng thái điều trị 
    private List<String> causes; // Lý do xét nghiệm
    private Date sampleTime; // Ngày lấy mẫu
    private Date resultTime; //NGày nhận kết quả
    private String viralloadResult; // Kết quả xét nghiệm TLVR
    private String compliance; // Tăng cường tuân thủ điều trị
    private Date firstTestTime; // Ngày làm xét nghiệm lần một
    private String viralloadResult2; // Kết quả xét nghiệm TLVR lần 2
    private Date regimenDate; // Ngày chuyển phác đồ sau khi khẳng định thất bại điều trị
    private String identityCard; // CMND
    private Long viralloadID; // id tải lượng virus
    private String treatmentRegimentStage; // Bậc phác đồ điều trị
    private String resultKq1; // Kết quả lần 1
    private String resultKq2; // Kết quả lần 2
    private String result; // Kết quả 
    private String visitID; // ID 
    private boolean reasonAfter12Month; // Lý do xn: sau 12 tháng điều trị arv
    private boolean reasonOtherPeriod; // Định kỳ khác
    private boolean reasonFailtreatment; // Nghi ngờ thất bại điều trị
    private boolean reasonPregnant; // reasonFailtreatment
    private Long idKq1; // ID Kết quả lần 1
    private Long idKq2; // ID Kết quả lần 2
    private Long countViral; // Đếm số lần xét nghiệm

    public Long getCountViral() {
        return countViral;
    }

    public void setCountViral(Long countViral) {
        this.countViral = countViral;
    }

    public Long getIdKq1() {
        return idKq1;
    }

    public void setIdKq1(Long idKq1) {
        this.idKq1 = idKq1;
    }

    public Long getIdKq2() {
        return idKq2;
    }

    public void setIdKq2(Long idKq2) {
        this.idKq2 = idKq2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    public boolean isReasonPregnant() {
        this.reasonPregnant = (causes != null && (statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) && causes.contains(ViralLoadSymtomEnum.PREGNANT.getKey()));
        return reasonPregnant;
    }

    public void setReasonPregnant(boolean reasonPregnant) {
        this.reasonPregnant = reasonPregnant;
    }
    
    public boolean isReasonFailtreatment() {
        this.reasonFailtreatment = (causes!= null && (statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) && causes.contains(ViralLoadSymtomEnum.DIEUTRI.getKey() ));
        return reasonFailtreatment;
    }

    public void setReasonFailtreatment(boolean reasonFailtreatment) {
        this.reasonFailtreatment = reasonFailtreatment;
    }
    
    public boolean isReasonOtherPeriod() {
        this.reasonOtherPeriod = (causes != null && (statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey())) && (causes.contains(ViralLoadSymtomEnum.DINHKY3THANG.getKey()) || causes.contains(ViralLoadSymtomEnum.VIRUS.getKey()) || causes.contains(ViralLoadSymtomEnum.MIENDICH.getKey()) || causes.contains(ViralLoadSymtomEnum.OTHER.getKey()) ));
        return this.reasonOtherPeriod;
    }

    public void setReasonOtherPeriod(boolean reasonOtherPeriod) {
        this.reasonOtherPeriod = reasonOtherPeriod;
    }
    
    private Calendar cal = Calendar.getInstance();

    public void setReasonAfter12Month(boolean reasonAfter12Month) {        
        this.reasonAfter12Month = reasonAfter12Month;
    }

    public boolean isReasonAfter12Month() {
        cal.setTime(this.testTime);
        int monthTestTime = cal.get(Calendar.MONTH);
        int yearTestTime = cal.get(Calendar.YEAR);
        cal.setTime(this.treatmentTime);
        int monthTreatmentTime = cal.get(Calendar.MONTH);
        int yearTreatmentYear = cal.get(Calendar.YEAR);
        
        this.reasonAfter12Month = statusOfTreatmentID.equals(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey()) && 
                ((yearTestTime - yearTreatmentYear >= 2) || (yearTestTime - yearTreatmentYear == 0 && monthTestTime - monthTreatmentTime + 1 == 12)
                || (yearTestTime - yearTreatmentYear == 1 && monthTestTime - monthTreatmentTime + 1 >=0 ) );
        return reasonAfter12Month;
    }
    
    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }
    
    public String getResultKq1() {
        return resultKq1;
    }

    public void setResultKq1(String resultKq1) {
        this.resultKq1 = resultKq1;
    }

    public String getResultKq2() {
        return resultKq2;
    }

    public void setResultKq2(String resultKq2) {
        this.resultKq2 = resultKq2;
    }
    
    public String getTreatmentRegimentStage() {
        return treatmentRegimentStage;
    }

    public void setTreatmentRegimentStage(String treatmentRegimentStage) {
        this.treatmentRegimentStage = treatmentRegimentStage;
    }

    public Long getViralloadID() {
        return viralloadID;
    }

    public void setViralloadID(Long viralloadID) {
        this.viralloadID = viralloadID;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInsuranceNo() {
        return insuranceNo;
    }

    public void setInsuranceNo(String insuranceNo) {
        this.insuranceNo = insuranceNo;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public Date getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(Date treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getViralloadResult() {
        return viralloadResult;
    }

    public void setViralloadResult(String viralloadResult) {
        this.viralloadResult = viralloadResult;
    }

    public String getCompliance() {
        return compliance;
    }

    public void setCompliance(String compliance) {
        this.compliance = compliance;
    }

    public Date getFirstTestTime() {
        return firstTestTime;
    }

    public void setFirstTestTime(Date firstTestTime) {
        this.firstTestTime = firstTestTime;
    }

    public String getViralloadResult2() {
        return viralloadResult2;
    }

    public void setViralloadResult2(String viralloadResult2) {
        this.viralloadResult2 = viralloadResult2;
    }

    public Date getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(Date regimenDate) {
        this.regimenDate = regimenDate;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }
    
}
