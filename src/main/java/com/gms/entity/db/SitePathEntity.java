package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "SITE_PATH",
        indexes = {
            @Index(name = "SITE_PATH_INDEX_SITE", columnList = "SITE_ID")
            ,
            @Index(name = "SITE_PATH_INDEX_ANCESTOR", columnList = "ANCESTOR_ID")
        }
)
public class SitePathEntity extends BaseEntity implements Serializable {

    @Id
//    @SequenceGenerator(sequenceName = "site_path_sequence", allocationSize = 1, name = "site_path_sequence_generator")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "site_path_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "SITE_ID")
    private Long siteID;

    @Column(name = "ANCESTOR_ID")
    private Long ancestorId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false)
    private Date createAT;

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

    public Long getAncestorId() {
        return ancestorId;
    }

    public void setAncestorId(Long ancestorId) {
        this.ancestorId = ancestorId;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

}
