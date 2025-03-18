package com.gms.entity.form.opc_arv;

/**
 *
 * @author TrangBN
 */
public class SoKhangTheArvTableForm implements Comparable<SoKhangTheArvTableForm>{
    
    private String stt;
    private String code;
    private String patientID;
    private String fullname;
    private String genderID;
    private String dob;
    private String arvID;
    private String stageID;
    private String statusOfTreatment;
    private String treatment;
    private String treatmentTime;
    private String endCase; // Lý do kết thúc
    private String endTime; // Thời gian kết thúc
    private String registrationType;
    private String tranferFromTime;
    private String treatmentRegimentID;
    private String treatmentRegimentIDChange; // Phác đồ thay thế
    private String examinationTime;
    private String dateOfArrival;
    private String clinicalStage;
    private String cd4;
    private String weight;
    private String height;
    private String regimenDate; // Ngày thay đổi phác đồ
    private String causesChange; // Lý do thay đổi phác đồ
    private String treatmentRegimenStage; // Bậc phác đồ hiện tại
    
    private String laoStartTime;
    private String inhStartTime;
    private String cotrimoxazoleStartTime;
    private String cd4Result;
    private String cd4TestTime;
    private String keyLao;
    private String keyInh;
    private String keyCotri;
    private String keyCd4;
    private String line;
    private String regimentKey; // Cờ ngày đánh dấu ngày thay đổi phác đồ

    public String getRegimentKey() {
        return regimentKey;
    }

    public void setRegimentKey(String regimentKey) {
        this.regimentKey = regimentKey;
    }

    public String getKeyLao() {
        return keyLao;
    }

    public void setKeyLao(String keyLao) {
        this.keyLao = keyLao;
    }

    public String getKeyInh() {
        return keyInh;
    }

    public void setKeyInh(String keyInh) {
        this.keyInh = keyInh;
    }

    public String getKeyCotri() {
        return keyCotri;
    }

    public void setKeyCotri(String keyCotri) {
        this.keyCotri = keyCotri;
    }

    public String getKeyCd4() {
        return keyCd4;
    }

    public void setKeyCd4(String keyCd4) {
        this.keyCd4 = keyCd4;
    }
    
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
    
    public String getLaoStartTime() {
        return laoStartTime;
    }

    public void setLaoStartTime(String laoStartTime) {
        this.laoStartTime = laoStartTime;
    }

    public String getInhStartTime() {
        return inhStartTime;
    }

    public void setInhStartTime(String inhStartTime) {
        this.inhStartTime = inhStartTime;
    }

    public String getCotrimoxazoleStartTime() {
        return cotrimoxazoleStartTime;
    }

    public void setCotrimoxazoleStartTime(String cotrimoxazoleStartTime) {
        this.cotrimoxazoleStartTime = cotrimoxazoleStartTime;
    }

    public String getCd4Result() {
        return cd4Result;
    }

    public void setCd4Result(String cd4Result) {
        this.cd4Result = cd4Result;
    }

    public String getCd4TestTime() {
        return cd4TestTime;
    }

    public void setCd4TestTime(String cd4TestTime) {
        this.cd4TestTime = cd4TestTime;
    }
    
    public String getTreatmentRegimentIDChange() {
        return treatmentRegimentIDChange;
    }

    public void setTreatmentRegimentIDChange(String treatmentRegimentIDChange) {
        this.treatmentRegimentIDChange = treatmentRegimentIDChange;
    }
    
    public String getTreatmentRegimenStage() {
        return treatmentRegimenStage;
    }

    public void setTreatmentRegimenStage(String treatmentRegimenStage) {
        this.treatmentRegimenStage = treatmentRegimenStage;
    }
    
    public String getCausesChange() {
        return causesChange;
    }

    public void setCausesChange(String causesChange) {
        this.causesChange = causesChange;
    }

    public String getRegimenDate() {
        return regimenDate;
    }

    public void setRegimenDate(String regimenDate) {
        this.regimenDate = regimenDate;
    }

    public String getDateOfArrival() {
        return dateOfArrival;
    }
    
    public SoKhangTheArvTableForm() {
    }

    public SoKhangTheArvTableForm(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }
    
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCd4() {
        return cd4;
    }

    public void setCd4(String cd4) {
        this.cd4 = cd4;
    }
    
    public String getClinicalStage() {
        return clinicalStage;
    }

    public void setClinicalStage(String clinicalStage) {
        this.clinicalStage = clinicalStage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }
    
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getArvID() {
        return arvID;
    }

    public void setArvID(String arvID) {
        this.arvID = arvID;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getStatusOfTreatment() {
        return statusOfTreatment;
    }

    public void setStatusOfTreatment(String statusOfTreatment) {
        this.statusOfTreatment = statusOfTreatment;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getEndCase() {
        return endCase;
    }

    public void setEndCase(String endCase) {
        this.endCase = endCase;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getTreatmentRegimentID() {
        return treatmentRegimentID;
    }

    public void setTreatmentRegimentID(String treatmentRegimentID) {
        this.treatmentRegimentID = treatmentRegimentID;
    }

    public String getStringOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(String dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public String getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTranferFromTime() {
        return tranferFromTime;
    }

    public void setTranferFromTime(String tranferFromTime) {
        this.tranferFromTime = tranferFromTime;
    }

    public String getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(String examinationTime) {
        this.examinationTime = examinationTime;
    }
    
    @Override
    public int compareTo(SoKhangTheArvTableForm item) {
        return this.getTreatmentTime().compareTo(item.getTreatmentTime());
    }
}
