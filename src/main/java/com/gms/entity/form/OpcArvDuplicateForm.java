package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class OpcArvDuplicateForm extends BaseEntity {

    private String row;
    private String code;
    private String child;
    private List<String> childs;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }
    
    

    public List<String> getChilds() {
        return childs;
    }

    public void setChilds(List<String> childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

}
