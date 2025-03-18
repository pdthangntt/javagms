
package com.gms.entity.form;

import com.gms.components.annotation.PhoneVietNam;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author NamAnh_HaUI
 */
public class FeedbackForm implements Serializable{
    
    private Long ID;
    
    private Long siteID;
    
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên có độ dài tối đa 100 kí tự.")
    //@Pattern(regexp = "^[a-zA-Z]*$", message = "Họ và tên chỉ chứa kí tự chữ!")
    private String name;
    
    @NotBlank(message = "Nội dung góp ý không được để trống")
    @Size(min = 20, message = "Nội dung góp ý có độ dài tối thiểu 20 kí tự.")
    private String content;
    
//    @NotBlank(message = "Trạng thái phản hồi không được để trống")
//    @Size(max = 30, message = "Trạng thái phản hồi có độ dài tối đa 30 kí tự.")
    private String status;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @PhoneVietNam(message = "Số điện thoại không đúng định dạng")
    private String sendPhone;
    
    @NotBlank(message = "Email không được để trống")
    @Size(max = 100, message = "Email có độ dài tối đa 100 kí tự.")
    @Pattern(regexp=".+@.+\\..+", message="Email không đúng định dạng(ví dụ: examplate@domain.com)")
    private String sendEmail;
    
    private Date createAT;

    private Date updateAt;

    private Long createdBY;

    private Long updatedBY;
    
    private boolean isRemove;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

    public boolean isIsRemove() {
        return isRemove;
    }

    public void setIsRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }
    
    
}
