package com.gms.entity.form;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author vvthanh
 */
public class RoleForm implements Serializable {

    private Long ID;

    @NotNull(message = "Tên quyền Không được để trống")
    private String name;

    private List<Long> action;

    private List<String> service;

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getAction() {
        return action;
    }

    public void setAction(List<Long> action) {
        this.action = action;
    }

}
