package com.gms.entity.form.htc_confirm;

/**
 * Form table hiển thị sổ nhận mẫu và trả kết quả HIV
 * 
 * @author TrangBN
 */
public class ConfirmReceiveBookTableForm {
    
    // ID bản ghi khách hàng làm xét nghiệm
    private Long ID;
    // Cơ sở  gửi mẫu 
    private String sourceSiteID;
    // Ngày nhận mẫu
    private String sampleReceiveTime;
    // Số lượng mẫu
    private String numberSample;
    // Người nhận mẫu
    private String receiveSampleStaff;
    // Ngày làm xét nghiệm khẳng định
    private String confirmTime;
    // Ngày trả kết quả xét nghiêm khẳng định
    private String resultsReturnTime;
    // Người trả kết quả
    private String returnSampleStaff;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(String sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getSampleReceiveTime() {
        return sampleReceiveTime;
    }

    public void setSampleReceiveTime(String sampleReceiveTime) {
        this.sampleReceiveTime = sampleReceiveTime;
    }

    public String getNumberSample() {
        return numberSample;
    }

    public void setNumberSample(String numberSample) {
        this.numberSample = numberSample;
    }

    public String getReceiveSampleStaff() {
        return receiveSampleStaff;
    }

    public void setReceiveSampleStaff(String receiveSampleStaff) {
        this.receiveSampleStaff = receiveSampleStaff;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getResultsReturnTime() {
        return resultsReturnTime;
    }

    public void setResultsReturnTime(String resultsReturnTime) {
        this.resultsReturnTime = resultsReturnTime;
    }

    public String getReturnSampleStaff() {
        return returnSampleStaff;
    }

    public void setReturnSampleStaff(String returnSampleStaff) {
        this.returnSampleStaff = returnSampleStaff;
    } 
}
