/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.io.Serializable;

/**
 *
 * @author DSNAnh
 */
public class HivInfoImportForm extends BaseEntity implements Serializable {
    private String id; 
    private String code; 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
    
}
