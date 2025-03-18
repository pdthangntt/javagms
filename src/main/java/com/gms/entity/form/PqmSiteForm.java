package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmSiteForm extends BaseEntity implements Serializable {

    //Trường cơ bản prep
    private List<PqmSiteRow> items;

    public List<PqmSiteRow> getItems() {
        return items;
    }

    public void setItems(List<PqmSiteRow> items) {
        this.items = items;
    }

}
