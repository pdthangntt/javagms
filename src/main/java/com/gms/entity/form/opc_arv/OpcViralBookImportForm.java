package com.gms.entity.form.opc_arv;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 * Thêm mới tải lượng virus từ sổ TLVR
 * 
 * @author TrangBN
 */
public class OpcViralBookImportForm extends BaseEntity {
    
    private Long siteID;
    private Long arvID;
    private Long viralID;
    private Long stageID;
    private Long patientID;
    private String code;
    private String result; // Kết quả xn
    private List<String> causes;
    private String causes0;
    private String causes1;
    private String causes2;
    private String causes3;
    private String causes4;
    private String sampleTime;
    private String testTime;
    private String resultTime;
    private String resultNumber;
    private String resultNumber0;
    private String resultNumber1;
    private String resultNumber2;
    private String resultNumber3;
    private String resultNumber4;
    private String resultNumber5;
    private String note;

    public Long getPatientID() {
        return patientID;
    }

    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }
    
    public Long getStageID() {
        return stageID;
    }

    public void setStageID(Long stageID) {
        this.stageID = stageID;
    }
    
    public Long getArvID() {
        return arvID;
    }

    public void setArvID(Long arvID) {
        this.arvID = arvID;
    }

    public Long getViralID() {
        return viralID;
    }

    public void setViralID(Long viralID) {
        this.viralID = viralID;
    }
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    public String getResultNumber0() {
        return resultNumber0;
    }

    public void setResultNumber0(String resultNumber0) {
        this.resultNumber0 = resultNumber0;
    }
    
    public String getCauses0() {
        return causes0;
    }

    public void setCauses0(String causes0) {
        this.causes0 = causes0;
    }
    
    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
    
    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }
    
    public String getResultNumber4() {
        return resultNumber4;
    }

    public void setResultNumber4(String resultNumber4) {
        this.resultNumber4 = resultNumber4;
    }

    public String getResultNumber5() {
        return resultNumber5;
    }

    public void setResultNumber5(String resultNumber5) {
        this.resultNumber5 = resultNumber5;
    }

    public String getResultNumber1() {
        return resultNumber1;
    }

    public void setResultNumber1(String resultNumber1) {
        this.resultNumber1 = resultNumber1;
    }

    public String getResultNumber2() {
        return resultNumber2;
    }

    public void setResultNumber2(String resultNumber2) {
        this.resultNumber2 = resultNumber2;
    }

    public String getResultNumber3() {
        return resultNumber3;
    }

    public void setResultNumber3(String resultNumber3) {
        this.resultNumber3 = resultNumber3;
    }
    
    public String getCauses1() {
        return causes1;
    }

    public void setCauses1(String causes1) {
        this.causes1 = causes1;
    }

    public String getCauses2() {
        return causes2;
    }

    public void setCauses2(String causes2) {
        this.causes2 = causes2;
    }

    public String getCauses3() {
        return causes3;
    }

    public void setCauses3(String causes3) {
        this.causes3 = causes3;
    }

    public String getCauses4() {
        return causes4;
    }

    public void setCauses4(String causes4) {
        this.causes4 = causes4;
    }
    
    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
