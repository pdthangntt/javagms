package com.gms.entity.json.hivinfo.patient;

import java.io.Serializable;

/**
 * Lưu thông tin mẫu giám sát phát hiện HIV
 *
 */
public class GsphInput implements Serializable {

    private String code;
    private Integer createBy; // Mã người nhập
    private String createAt; // Ngày nhập
    private Integer updateBy; // Mã người sửa
    private String updateDate; // Ngày sửa
    private Integer deleteBy; // Mã người xóa
    private String deleteDate; // Ngày xóa
    private String aidsInputDate; // Ngày nhập AIDS
    private String deadInputDate; // Ngày nhập tử vong
    private String receiveDate; // Ngày nhận

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(Integer deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getAidsInputDate() {
        return aidsInputDate;
    }

    public void setAidsInputDate(String aidsInputDate) {
        this.aidsInputDate = aidsInputDate;
    }

    public String getDeadInputDate() {
        return deadInputDate;
    }

    public void setDeadInputDate(String deadInputDate) {
        this.deadInputDate = deadInputDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

}
