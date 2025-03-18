package com.gms.entity.form.pqm_shi_mmd_api;

import com.gms.entity.form.pqm_elmis_api.*;
import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Main extends BaseEntity {

    private List<Mmd> datas;

    public List<Mmd> getDatas() {
        return datas;
    }

    public void setDatas(List<Mmd> datas) {
        this.datas = datas;
    }

}
