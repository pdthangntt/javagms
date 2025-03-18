package com.gms.entity.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "PMTCT_LOG",
        indexes = {
                @Index(name = "PMTCT_MOM_ID", columnList = "PMTCT_MOM_INFO_ID")
        }
)
public class PmtctLogEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "PMTCT_MOM_INFO_ID")
    private Long pmtctMomInfoId;

    @Column(name = "IS_MOM")
    private Boolean isMom;

    @Column(name = "CONTENT", length = 500)
    private String content;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "STAFF_ID")
    private Long staffId;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getPmtctMomInfoId() {
        return pmtctMomInfoId;
    }

    public void setPmtctMomInfoId(Long pmtctMomInfoId) {
        this.pmtctMomInfoId = pmtctMomInfoId;
    }

    public Boolean getMom() {
        return isMom;
    }

    public void setMom(Boolean mom) {
        isMom = mom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
