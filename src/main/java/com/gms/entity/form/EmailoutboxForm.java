
package com.gms.entity.form;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author NamAnh_HaUI
 */
public class EmailoutboxForm implements Serializable{

    private static final long serialVersionUID = -1425839458042652039L;
        
    private Long ID;
    private String from;
    @Pattern(regexp=".+@.+\\..+", message="Email không đúng định dạng(ví dụ: examplate@domain.com)")
    private String to;
    @NotEmpty(message = "Tiêu đề mail không được để trống")
    private String subject;
    @NotEmpty(message = "Nội dung mail không được để trống")
    private String content;
    private boolean isRun;
    private String errorMessage;
    private Date createAT;
    private Date sendAt;
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }
}
