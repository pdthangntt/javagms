package com.gms.entity.form;

import com.gms.components.annotation.PhoneVietNam;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author vvthanh
 */
public class StaffForm implements Serializable {

    private Long ID;

    private List<Long> roleID;

    private String serviceVisitID;

    public final static String SPECIAL_CHAR_WITHOUT_SPACE = "(^[^-](?!.*--)[a-zA-Z0-9-]+[^-]$)";
    private final String REGEX_ALLOWED_CHAR = "^[a-z_0-9,-]{3,30}$";

    @NotNull(message = "Họ và tên Không được để trống")
    @Size(min = 6, max = 30, message = "Họ và tên có độ dài từ 6 đến 30 kí tự")
    private String name;

    @NotNull(message = "Tên đăng nhập Không được để trống")
    @Size(min = 3, max = 30, message = "Tên đăng nhập có độ dài từ 3 đến 30 kí tự")
    @Pattern(regexp = REGEX_ALLOWED_CHAR, message = "Tên đăng nhập không đúng định dạng. Chỉ chứa các kí tự 'a-z', '0-9', '_' và '-'")
    private String username;

    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Số điện thoại không được để trống")
    @PhoneVietNam(message = "Số điện thoại không đúng định dạng")
    private String phone;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu có độ dài từ 6 đến 30 kí tự")
    private String pwd;

    @NotEmpty(message = "Nhập lại mật khẩu không được để trống")
    private String confirmPwd;

    private boolean isActive = true;

    public String getServiceVisitID() {
        return serviceVisitID;
    }

    public void setServiceVisitID(String serviceVisitID) {
        this.serviceVisitID = serviceVisitID;
    }

    public List<Long> getRoleID() {
        return roleID;
    }

    public void setRoleID(List<Long> roleID) {
        this.roleID = roleID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

}
