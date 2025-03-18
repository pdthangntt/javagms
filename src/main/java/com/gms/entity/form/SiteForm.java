package com.gms.entity.form;

import com.gms.components.annotation.ExistFK;
import com.gms.components.annotation.PhoneVietNam;
import com.gms.entity.constant.RegexpEnum;
import com.gms.service.LocationsService;
import com.gms.service.SiteService;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author vvthanh
 */
public class SiteForm implements Serializable {

    private Long ID;

    @Size(max = 100, message = "Mã cơ sở có độ dài tối đa 100 kí tự.")
    @Pattern(regexp = RegexpEnum.SPECIAL_CHAR_WITHOUT_SPACE, message = "Mã cơ sở chỉ chứa kí tự chữ, số và dấu -")
    private String code;

    @NotBlank(message = "Tên cơ sở không được để trống")
    @Size(max = 100, message = "Tên cơ sở có độ dài tối đa 220 kí tự.")
    private String name;

    @NotBlank(message = "Tên viết tắt không được để trống")
    @Size(max = 100, message = "Tên viết tắt có độ dài tối đa 100 kí tự.")
    private String shortName;

    @Size(max = 100, message = "Mô tả có độ dài tối đa 500 kí tự.")
    private String description;

    @NotBlank(message = "Địa chỉ cơ sở không được để trống")
    @Size(max = 100, message = "Mô tả có độ dài tối đa 1000 kí tự.")
    private String address;

    @NotBlank(message = "Tỉnh thành không được để trống")
    @ExistFK(fieldName = "provinceID", service = LocationsService.class)
    private String provinceID;

    @NotBlank(message = "Quận huyện không được để trống")
    @ExistFK(fieldName = "districtID", service = LocationsService.class)
    private String districtID;

    @NotBlank(message = "Phường xã không được để trống")
    @ExistFK(fieldName = "wardID", service = LocationsService.class)
    private String wardID;

    @ExistFK(fieldName = "parentID", service = SiteService.class)
    private Long parentID;

    @ExistFK(fieldName = "type", service = SiteService.class)
    private int type;

    private boolean isActive;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng(ví dụ: examplate@domain.com)")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
//    @PhoneVietNam(message = "Số điện thoại không đúng định dạng")
    @Size(max = 50, message = "Số điện thoại không được quá 50 ký tự")
    private String phone;

    @NotNull(message = "Dịch vụ không được để trống")
    private List<String> service;

    @Size(max = 100, message = "Tên người liên hệ có độ dài tối đa 50 kí tự.")
    private String contactName;

    @NotBlank(message = "Chức vụ người liên hệ không được để trống")
    @Size(max = 220, message = "Chức vụ người liên hệ có độ dài tối đa 220 kí tự.")
    private String contactPosition;

    @NotBlank(message = "Cơ quan chủ quản không được để trống")
    @Size(max = 220, message = "Cơ quan chủ quản độ dài tối đa 220 kí tự.")
    private String parentAgency;

    @NotBlank(message = "Dự án tài trợ không được để trống")
    private String projectID; //Dự án tài trợ cho cơ sở

    private String hub;

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getParentAgency() {
        return parentAgency;
    }

    public void setParentAgency(String parentAgency) {
        this.parentAgency = parentAgency;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
