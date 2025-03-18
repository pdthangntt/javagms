package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class PqmProportionForm extends BaseEntity implements Serializable {

    //Trường cơ bản prep
    private List<PqmProportionRow> items;

    public List<PqmProportionRow> getItems() {
        return items;
    }

    public void setItems(List<PqmProportionRow> items) {
        this.items = items;
    }

}
