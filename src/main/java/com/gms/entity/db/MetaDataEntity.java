package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@DynamicUpdate
@DynamicInsert
@Table(name = "META_DATA", indexes = {
    @Index(name = "META_DATA_NAME", columnList = "CONTENT")
    ,@Index(name = "META_DATA_SEARCH", columnList = "SEARCH")
})
public class MetaDataEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    private String ID;

    @Column(name = "CONTENT", length = 220, nullable = false)
    private String content;

    @Column(name = "SEARCH", length = 220, nullable = false)
    private String search;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME", insertable = true, updatable = false)
    private Date time;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("time");
    }

}
