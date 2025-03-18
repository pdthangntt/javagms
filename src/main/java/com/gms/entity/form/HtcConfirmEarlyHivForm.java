package com.gms.entity.form;

/**
 *
 * @author pdThang
 */
public class HtcConfirmEarlyHivForm {

    private Long ID;

    private String earlyHivDate; // Ngày xn Nhiễm mới

    private String earlyHiv; //kq xn moi nhiem

    private String earlyBioName; // Tên sinh phẩm nhiễm mới

    private String earlyDiagnose; // Kết luận chẩn đoán nhiễm mới

    private String virusLoad; //Tải lượng virut

    private String virusLoadDate; // Ngày xét nghiệm tải lượng virus

    private String virusLoadNumber; //Kết quả xét nghiệm TLVR - nhập số
    private String confirmTime; //Kết quả xét nghiệm TLVR - nhập số

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

    public String getEarlyHivDate() {
        return earlyHivDate;
    }

    public void setEarlyHivDate(String earlyHivDate) {
        this.earlyHivDate = earlyHivDate;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getEarlyBioName() {
        return earlyBioName;
    }

    public void setEarlyBioName(String earlyBioName) {
        this.earlyBioName = earlyBioName;
    }

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getVirusLoadDate() {
        return virusLoadDate;
    }

    public void setVirusLoadDate(String virusLoadDate) {
        this.virusLoadDate = virusLoadDate;
    }

    public String getVirusLoadNumber() {
        return virusLoadNumber;
    }

    public void setVirusLoadNumber(String virusLoadNumber) {
        this.virusLoadNumber = virusLoadNumber;
    }

}
