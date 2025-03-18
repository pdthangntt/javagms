package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.OpcViralLoadEntity;
import static com.gms.entity.form.opc_arv.OpcStageForm.FORMATDATE;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Form sửa thông tin tải lượng virus
 *
 * @author TrangBN
 */
public class AppointmentViralForm implements Serializable {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    private String siteID; //Mã cơ sở quản lý
    private String code; //Mã bệnh nhân
    private String fullName; //Họ và tên
    private String genderID; //Giới tính
    private String dob; //Ngày/tháng/năm sinh
    private String identityCard; //Chứng minh thư

    private String confirmCode; // Mã XN khẳng định
    private String confirmTime; //Ngày xét nghiệm khẳng định
    private String confirmSiteID;
    private String confirmSiteName;

    private String viralLoadRetryTime; //Ngày hẹn xn
    private List<String> viralLoadCauses; //Lý do XN
    private String note; //Lý do XN

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getViralLoadRetryTime() {
        return viralLoadRetryTime;
    }

    public void setViralLoadRetryTime(String viralLoadRetryTime) {
        this.viralLoadRetryTime = viralLoadRetryTime;
    }

    public List<String> getViralLoadCauses() {
        return viralLoadCauses;
    }

    public void setViralLoadCauses(List<String> viralLoadCauses) {
        this.viralLoadCauses = viralLoadCauses;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
