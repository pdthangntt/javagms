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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author vvthanh
 * @des Lưu thông tham số
 */
@Entity
@Table(name = "OPC_PARAMETER",
        uniqueConstraints = {
            @UniqueConstraint(name = "opu_key", columnNames = {"OBJECT_ID", "TYPE", "PARAMETER_ID", "ATTR_NAME"})
        },
        indexes = {})
public class OpcParameterEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "OBJECT_ID")  
    private Long objectID; //Đối tượng: ARVID, stageID, ...

    @Column(name = "TYPE", length = 20)
    private String type; //Loại sử dụng enum: ARV, STAGE

    @Column(name = "ATTR_NAME", length = 50)
    private String attrName; //Trường dữ liệu

    @Column(name = "PARAMETER_ID", length = 50)
    private String parameterID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getObjectID() {
        return objectID;
    }

    public void setObjectID(Long objectID) {
        this.objectID = objectID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getParameterID() {
        return parameterID;
    }

    public void setParameterID(String parameterID) {
        this.parameterID = parameterID;
    }

}
