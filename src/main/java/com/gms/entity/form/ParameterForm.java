package com.gms.entity.form;

import com.gms.components.annotation.ExistFK;
import com.gms.service.ParameterService;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author vvthanh
 */
public class ParameterForm implements Serializable {

    private Long ID;

    @NotNull(message = "Loại tham số Không được để trống")
    private String type;

    @NotNull(message = "Giá trị Không được để trống")
    @NotBlank(message = "Giá trị không được để trống")
    private String code;

    @NotNull(message = "Nhãn không được để trống")
    @NotBlank(message = "Nhãn không được để trống")
    private String value;

    @ExistFK(service = ParameterService.class, fieldName = "parentID", message = "Tham số cấp trên không tồn tại")
    private long parentID;
    private int status;
    private int position;
    private String note;
    private String elogCode;
    private String pqmCode;
    private String hivInfoCode;
    private String attribute01;
    private String attribute02;
    private String attribute03;
    private String attribute04;
    private String attribute05;
    private Long siteID;
    private String provinceID;

    public String getPqmCode() {
        return pqmCode;
    }

    public void setPqmCode(String pqmCode) {
        this.pqmCode = pqmCode;
    }

    public String getAttribute03() {
        return attribute03;
    }

    public void setAttribute03(String attribute03) {
        this.attribute03 = attribute03;
    }

    public String getAttribute04() {
        return attribute04;
    }

    public void setAttribute04(String attribute04) {
        this.attribute04 = attribute04;
    }

    public String getAttribute05() {
        return attribute05;
    }

    public void setAttribute05(String attribute05) {
        this.attribute05 = attribute05;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getAttribute01() {
        return attribute01;
    }

    public void setAttribute01(String attribute01) {
        this.attribute01 = attribute01;
    }

    public String getAttribute02() {
        return attribute02;
    }

    public void setAttribute02(String attribute02) {
        this.attribute02 = attribute02;
    }

    public String getElogCode() {
        return elogCode;
    }

    public void setElogCode(String elogCode) {
        this.elogCode = elogCode;
    }

    public String getHivInfoCode() {
        return hivInfoCode;
    }

    public void setHivInfoCode(String hivInfoCode) {
        this.hivInfoCode = hivInfoCode;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
