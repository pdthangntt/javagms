package com.gms.entity.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author OTHOA
 */
@Entity
@Table(name = "PQM_PROPORTION", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CODE", "PROVINCE_ID"}, name = "pqm_proportion")
        })
public class PqmProportionEntity extends BaseEntity implements Serializable, Cloneable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "CODE", length = 100, unique = true, nullable = false)
    private String code;

    @Column(name = "VALUE", length = 220)
    private String value;

    @Column(name = "PROVINCE_ID", length = 5, nullable = false)
    private String provinceID;

    @Override
    public PqmProportionEntity clone() throws CloneNotSupportedException {
        return (PqmProportionEntity) super.clone();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

}
