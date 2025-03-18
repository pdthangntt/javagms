package com.gms.entity.form;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author vvthanh
 */
public class ServiceForm implements Serializable {

    @NotNull(message = "Dịch vụ không được để trống")
    private String serviceID;

    private List<Long> action;

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public List<Long> getAction() {
        return action;
    }

    public void setAction(List<Long> action) {
        this.action = action;
    }

}
