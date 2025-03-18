package com.gms.entity.form;

import java.io.Serializable;

/**
 *
 * @author TrangBN
 */
public class HtcVisitLaytestForm implements Serializable {

    private Long ID;
    // Ngày tiếp nhận
    private String acceptDate;
    // Nhân viên thực hiện tiếp nhận
    private Long acceptStaffId;
    // ID của khách hàng bên xét nghiệm không chuyên
    private Long sourceID;
    // ID của cơ sở nơi xét nghiệm không chuyên
    private Long sourceSiteID;
    // Mã của nhân viên nơi xét nghiệm không chuyên
    private Long sourceStaffID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Long getAcceptStaffId() {
        return acceptStaffId;
    }

    public void setAcceptStaffId(Long acceptStaffId) {
        this.acceptStaffId = acceptStaffId;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Long getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(Long sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public Long getSourceStaffID() {
        return sourceStaffID;
    }

    public void setSourceStaffID(Long sourceStaffID) {
        this.sourceStaffID = sourceStaffID;
    }
}
