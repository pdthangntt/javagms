package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author vvthanh
 */
public class ResetPwdForm implements Serializable {

    @NotBlank(message = "Mã xác thực không được để trống")
    private String code;

    @NotEmpty(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 30, message = "Mật khẩu có độ dài tối thiểu 6 kí tự")
    private String password;

    @NotEmpty(message = "Yêu cầu nhập lại mật khẩu mới")
    private String confirmPwd;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
