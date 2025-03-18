package com.gms.entity.form;

import com.gms.components.annotation.PhoneVietNam;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Form stores profile information  
 * @author TrangBN
 */
public class ProfileForm implements Serializable {
    
    private Long ID;
    
    @Email(message = "Email không đúng định dạng")
    private String email;
    
    @NotNull(message = "Họ và tên Không được để trống")
    @Size(min = 6, max = 30, message = "Họ và tên có độ dài từ 6 đến 30 kí tự")
    private String name;
    
    @NotNull(message = "Số điện thoại không được để trống")
    @PhoneVietNam(message = "Số điện thoại không đúng định dạng")
    private String phone;
    
    private String username;
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
