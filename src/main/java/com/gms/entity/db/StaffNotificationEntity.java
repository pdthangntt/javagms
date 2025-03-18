package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "STAFF_NOTIFICATION",
        indexes = {
            @Index(name = "STAFF_NOTIFICATION_INDEX_STAFF", columnList = "STAFF_ID")
        }
)
@DynamicInsert
@DynamicUpdate
public class StaffNotificationEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "STAFF_ID")
    private Long staffID;

    @Column(name = "TOKEN", length = 220, nullable = true)
    private String token;

    @Column(name = "TITLE", length = 220, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 500, nullable = false)
    private String content;

    @Column(name = "URL", columnDefinition = "TEXT", nullable = true)
    private String url;

    @Column(name = "is_read")
    private boolean read;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false, nullable = false)
    private Date time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
