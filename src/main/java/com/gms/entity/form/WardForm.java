package com.gms.entity.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author NamAnh_HaUI
 */
public class WardForm {

    @NotEmpty(message = "ID không được để trống")
    @Size(max = 5, message = "Độ dài ID phải từ 1 đến 5")
    private String ID;
    private String districtID;
    @NotEmpty(message = "Tên phường xã không được để trống")
    @Size(max = 100, message = "Tên phường xã không quá 100 kí tự")
    private String name;
    @Size(max = 30, message = "Cấp phường xã không quá 30 kí tự")
    private String type;
    @NotNull(message = "Thứ tự không được để trống")
    private int position;
    private String elogCode;
    @NotNull(message = "Mã hivInfo không được để trống")
    private String hivInfoCode;

    @NotBlank(message = "Trạng thái không được để trống")
    private String active;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
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
