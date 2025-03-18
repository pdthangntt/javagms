package com.gms.entity.form;

import com.gms.entity.db.BaseEntity;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author pdThang
 */
public class OpcRegimenImportSoft implements Comparator<OpcRegimenImportForm> {

    @Override
    public int compare(OpcRegimenImportForm o1, OpcRegimenImportForm o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

}
