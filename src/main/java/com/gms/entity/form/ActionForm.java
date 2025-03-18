package com.gms.entity.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author vvthanh
 */
public class ActionForm implements Serializable {

    @NotNull(message = "Mã quyền Không được để trống")
    private String name;

    @NotNull(message = "Tên quyền Không được để trống")
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
