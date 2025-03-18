package com.gms.entity.json.export;

import java.util.HashMap;

/**
 *
 * @author vvthanh
 */
public class Parameter {

    private String type;
    private HashMap<String, String> variables;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, String> variables) {
        this.variables = variables;
    }

    
    
}
