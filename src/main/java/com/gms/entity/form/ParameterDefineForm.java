package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author vvthanh
 */
public class ParameterDefineForm implements Serializable {

    @NotNull(message = "Loại tham số Không được để trống")
    private String type;
    
    @NotNull(message = "CODE Không được để trống")
    private String key;
    
    @NotNull(message = "Không được để trống")
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
