package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author NamAnh_HaUI
 */
public class HtcTargetForm implements Serializable {

    private Long ID;
    private Long siteID;

    @NotNull(message = "Năm không được để trống")
    @Min(value = 1900, message = "Năm chỉ nhập trong khoảng từ 1900 đến 2100")
    @Max(value = 2100, message = "Năm chỉ nhập trong khoảng từ 1900 đến 2100")
    private Long years;

    @NotNull(message = "Số khách hàng được xét nghiệm HIV không được để trống")
    @Min(value = 0, message = "Số khách hàng được xét nghiệm HIV không được âm")
    @Max(value = 999999999, message = "Số liệu nhập quá lớn")
    private Long numberCustomer;

    @NotNull(message = "Số khách hàng dương tính không được để trống")
    @Min(value = 0, message = "Số khách hàng có kết quả xét nghiệm HIV (+) không được âm")
    @Max(value = 999999999, message = "Số liệu nhập quá lớn")
    private Long numberPositive;

    @NotNull(message = "Số khách hàng quay lại nhận kết quả không được để trống")
    @Min(value = 0, message = "Số khách hàng xét nghiệm và quay lại nhận kết quả không được âm")
    @Max(value = 999999999, message = "Số liệu nhập quá lớn")
    private Long numberGetResult;

    @NotNull(message = "Số KH dương tính và quay lại nhận kết quả không được để trống")
    @Min(value = 0, message = "Số khách hàng dương tính và quay lại nhận kết quả không được âm")
    @Max(value = 999999999, message = "Số liệu nhập quá lớn")
    private Long positiveAndGetResult;

    @NotNull(message = "Tỷ lệ khách hàng đang được chăm sóc điều trị không được để trống")
    @Min(value = 0, message = "Tỷ lệ khách hàng dương tính được chuyển gửi thành công nằm trong khoảng 0% - 100%")
    @Max(value = 100, message = "Tỷ lệ khách hàng dương tính được chuyển gửi thành công nằm trong khoảng 0% - 100%")
    private String careForTreatment;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getYears() {
        return years;
    }

    public void setYears(Long years) {
        this.years = years;
    }

    public Long getNumberCustomer() {
        return numberCustomer;
    }

    public void setNumberCustomer(Long numberCustomer) {
        this.numberCustomer = numberCustomer;
    }

    public Long getNumberPositive() {
        return numberPositive;
    }

    public void setNumberPositive(Long numberPositive) {
        this.numberPositive = numberPositive;
    }

    public Long getNumberGetResult() {
        return numberGetResult;
    }

    public void setNumberGetResult(Long numberGetResult) {
        this.numberGetResult = numberGetResult;
    }

    public Long getPositiveAndGetResult() {
        return positiveAndGetResult;
    }

    public void setPositiveAndGetResult(Long positiveAndGetResult) {
        this.positiveAndGetResult = positiveAndGetResult;
    }

    public String getCareForTreatment() {
        return careForTreatment;
    }

    public void setCareForTreatment(String careForTreatment) {
        this.careForTreatment = careForTreatment;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

}
