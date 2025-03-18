package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "EMAIL_OUTBOX",
        indexes = {
            @Index(name = "EMAIL_OUTBOX_JOB", columnList = "IS_RUN, SEND_AT")
            ,
            @Index(name = "EMAIL_OUTBOX_JOB", columnList = "IS_RUN")})
public class EmailoutboxEntity extends BaseEntity implements Serializable {

    public static String TEMPLATE_NOTIFY = "notify";
    public static String TEMPLATE_REPORT_03 = "report_tt03";
    public static String QUICK_TREATMENT = "quick_treatment";
    public static String TEMPLATE_THUAN_TAP = "report_thuan_tap";
    public static String TEMPLATE_REPORT_VIRRALOAD = "report_viral_load";
    public static String TEMPLATE_OUT_PROVINCE = "out_province_gender";
    public static String TEMPLATE_REPORT_MER_SIX_MONTH = "report_mer_six_month";
    public static String TEMPLATE_REPORT_MER_QUARTERLY = "report_mer_quarterly";
    public static String RECEIVED_MAIL = "htc_confirm_received";
    public static String ARV_TRANSFER_MAIL = "arv_transfer_mail";
    public static String HTC_TRANSFER_TREATMENT_MAIL = "htc_transfer_treatment_mail";
    public static String ARV_TRANSFER_MAIL_CHANGED = "arv_transfer_mail_changed";
    public static String ARV_RECEIVE_SUCCESS_MAIL = "arv_receive_success_mail";
    public static String OPC_ARV_RECEIVE_SUCCESS_MAIL = "opc_arv_receive_success_mail";
    public static String TEMPLATE_REPORT_REGIMEN = "regimen_report";
    public static String TEMPLATE_REPORT_09_PHULUC01 = "report_tt09_phuluc01";
    public static String TEMPLATE_REPORT_POSITIVE = "positive_mail";
    public static String TEMPLATE_LAYTEST_BOOK = "laytest_book_mail";
    public static String TEMPLATE_REPORT_TRANSFER_SUCCESS = "transfer_success_mail";
    public static String TEMPLATE_REPORT_HTC_CONFIRM_POSITIVE = "htc_confirm_positive_mail";
    public static String TEMPLATE_REPORT_HTC_CONFIRM_POSITIVE_DISTINCT = "htc_confirm_positive_distinct_mail";
    public static String TEMPLATE_FEEDBACK = "feedback_mail";
    public static String TEMPLATE_CREATE_ACCOUNT = "create_account";
    public static String TEMPLATE_SENT_SITE_ACCOUNT = "send_account_site";
    public static String TEMPLATE_PAC_ACCEPT = "pac_accept_mail";
    public static String TEMPLATE_PAC_OUT_PROVINCE = "pac_out_provinces_mail";
    public static String TEMPLATE_PAC_NEW = "pac_new_mail";
    public static String TEMPLATE_PAC_NEW_EXPORT = "pac_new_export_mail";
    public static String TEMPLATE_PAC_DEAD = "pac_dead_mail";
    public static String TEMPLATE_PAC_AIDS_REPORT = "pac_aids_report";
    public static String TEMPLATE_PAC_REVIEW = "pac_review_mail";
    public static String TEMPLATE_SENT_PAC = "htc_sent_pac_mail";
    public static String TEMPLATE_OPC_ARV_SENT_PAC = "opc_arv_sent_pac_mail";
    public static String TEMPLATE_PAC_UPDATE_REPORT = "pac-update-report-mail";
    public static String TEMPLATE_PAC_PATIENT = "pac_patient_mail";
    public static String TEMPLATE_PAC_PATIENT_CANCEL = "pac_patient_cancel";
    public static String TEMPLATE_PAC_PATIENT_UPDATE = "pac_patient_update";
    public static String TEMPLATE_PAC_DEAD_LOCAL = "pac_dead_local";
    public static String TEMPLATE_PAC_LOCAL = "pac_local";
    public static String TEMPLATE_PAC_EARLY_HIV_LOCAL = "pac_early_hiv_local";
    public static String TEMPLATE_PAC_DETECT_HIV_OBJECT = "pac_detect_hiv_object_mail";
    public static String TEMPLATE_PAC_REGISIDENT_HIV = "pac_detect_hiv_regisident_mail";
    public static String TEMPLATE_PAC_WARD = "pac_ward";
    public static String TEMPLATE_PAC_EXPORT_HIVINFO = "pac_export_hivinfo";
    public static String TEMPLATE_PAC_TREATMENT_HIV = "pac_detect_hiv_treatment_mail";
    public static String TEMPLATE_PAC_TRANSMISION_HIV = "pac_detect_hiv_transmission_mail";
    public static String TEMPLATE_PAC_GENDER_HIV = "pac_detect_hiv_gender_mail";
    public static String TEMPLATE_PAC_INSURANCE_HIV = "pac_detect_hiv_insurance_mail";
    public static String TEMPLATE_PAC_AGE_HIV = "pac_detect_hiv_age_mail";
    public static String TEMPLATE_PAC_PATIENT_A10 = "pac_patient_a10";
    public static String TEMPLATE_OPC_TREATMENT_FLUCTUATIONS = "template_opc_treatment_fluctuations";
    public static String TEMPLATE_OPC_PATIENT = "template_opc_patient";
    public static String TEMPLATE_OPC_PRE_ARV = "template_opc_pre_arv";
    public static String TEMPLATE_ARV_BOOK = "template_arv_book";
    public static String EARLY_HIV_CONFIRM = "early_hiv_confirm";
    public static String TEMPLATE_OPC_VIRALLOAD_BOOK = "template_opc_viralload_book";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "FROM_EMAIL", length = 100, nullable = false)
    private String from;

    @Column(name = "TO_EMAIL", length = 100, nullable = false)
    private String to;

    @Column(name = "SUBJECT", length = 220, nullable = false)
    private String subject;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "IS_RUN", nullable = false)
    private boolean isRun;

    @Column(name = "ERROR_MESSAGE", columnDefinition = "TEXT")
    private String errorMessage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEND_AT")
    private Date sendAt;

    @Lob
    @Column(name = "ATTACHMENT", nullable = true)
    private byte[] attachment;

    @Column(name = "ATTACHMENT_TYPE", nullable = true)
    private String attachmentType;

    @Column(name = "ATTACHMENT_FILE_NAME", nullable = true)
    private String attachmentFileName;

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

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

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("sendAt");
    }

}
