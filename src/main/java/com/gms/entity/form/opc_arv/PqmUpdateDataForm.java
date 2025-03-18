package com.gms.entity.form.opc_arv;

import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form BC pqm
 *
 * @author pdThang
 *
 */
public class PqmUpdateDataForm extends BaseForm {

    private List<PqmTableDataForm> items;

    public List<PqmTableDataForm> getItems() {
        return items;
    }

    public void setItems(List<PqmTableDataForm> items) {
        this.items = items;
    }

}
