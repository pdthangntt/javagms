package com.gms.entity.form;

import java.io.Serializable;

/**
 *
 * @author TrangBN
 */
public class MailSearchForm  implements Serializable {
    
    private String subject;
    private String subjectOnly;
    private String sender;
    private String receiver;
    private String sendDate;
    private Boolean status;
    private Boolean opening;

    public Boolean getOpening() {
        return opening;
    }

    public void setOpening(Boolean opening) {
        this.opening = opening;
    }

    public String getSubjectOnly() {
        return subjectOnly;
    }

    public void setSubjectOnly(String subjectOnly) {
        this.subjectOnly = subjectOnly;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
