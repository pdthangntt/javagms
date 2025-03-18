package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author vvthanh
 */
public class ForgotForm implements Serializable {

    @NotBlank(message = "Email không được để trống")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
