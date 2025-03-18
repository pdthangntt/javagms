package com.gms.entity.form.pqm_elmis_api;

import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Main extends BaseEntity {

    private List<Elmis> datas;

    public List<Elmis> getDatas() {
        return datas;
    }

    public void setDatas(List<Elmis> datas) {
        this.datas = datas;
    }

}
