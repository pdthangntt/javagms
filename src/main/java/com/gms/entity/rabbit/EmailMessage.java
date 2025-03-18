package com.gms.entity.rabbit;

import com.gms.entity.db.EmailoutboxEntity;

/**
 *
 * @author vvthanh
 */
public class EmailMessage extends RabbitMessage {

    private EmailoutboxEntity emailoutbox;

    public EmailMessage() {
    }

    public EmailMessage(EmailoutboxEntity emailoutbox) {
        this.emailoutbox = emailoutbox;
    }

    public EmailoutboxEntity getEmailoutbox() {
        return emailoutbox;
    }

    public void setEmailoutbox(EmailoutboxEntity emailoutbox) {
        this.emailoutbox = emailoutbox;
    }

}
