package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author NamAnh_HaUI
 */
public class ProvinceForm implements Serializable {

    @NotEmpty(message = "ID không được để trống")
    @Size(max = 5, message = "Độ dài ID phải từ 1 đến 5")
    private String ID;

    @NotEmpty(message = "Tên tỉnh thành không được để trống")
    @Size(max = 100, message = "Tên tỉnh thành không quá 100 kí tự")
    private String name;

    @Size(max = 30, message = "Cấp tỉnh thành không quá 30 kí tự")
    private String type;

    @NotNull(message = "Thứ tự không được để trống")
    private int position;

    @Size(max = 50, message = "Mã HTC Elog không quá 50 kí tự")
    private String elogCode;

    @Size(max = 50, message = "Mã HIV Info không quá 50 kí tự")
    private String hivInfoCode;

    @Size(max = 50, message = "Mã Hub không quá 50 kí tự")
    private String pqmCode;

    public String getPqmCode() {
        return pqmCode;
    }

    public void setPqmCode(String pqmCode) {
        this.pqmCode = pqmCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

}
