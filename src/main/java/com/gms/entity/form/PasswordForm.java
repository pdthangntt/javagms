package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author TrangBN
 */
public class PasswordForm implements Serializable {
    
    @NotEmpty(message = "Mật khẩu cũ không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu có độ dài tối thiểu 6 kí tự")
    private String oldPassword;
    
    @NotEmpty(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu có độ dài tối thiểu 6 kí tự")
    private String password;
    
    @NotEmpty(message = "Yêu cầu nhập lại mật khẩu mới")
    private String confirmPwd;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }
    
}
