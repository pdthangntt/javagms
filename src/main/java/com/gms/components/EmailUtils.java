package com.gms.components;

import java.io.File;
import java.nio.charset.StandardCharsets;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class EmailUtils {

    @Autowired
    public JavaMailSender emailSender;

    /**
     * Gửi email bình thường
     *
     * @param to
     * @param subject
     * @param text
     * @throws MessagingException
     */
    public void send(String to, String subject, String text) throws MessagingException {
        send(to, subject, text, null, null);
    }

    /**
     * Gửi email có file đính kèm
     *
     * @param to
     * @param subject
     * @param text
     * @param fileName
     * @param dataSource
     * @throws MessagingException
     */
    public void send(String to, String subject, String text, String fileName, DataSource dataSource) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(text, true);
        helper.setSubject(subject);
        if (fileName != null) {
            helper.addAttachment(fileName, dataSource);
        }
        emailSender.send(message);

    }
}
