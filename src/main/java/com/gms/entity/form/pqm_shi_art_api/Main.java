package com.gms.entity.form.pqm_shi_art_api;

import com.gms.entity.form.pqm_elmis_api.*;
import com.gms.entity.db.BaseEntity;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class Main extends BaseEntity {

    private List<Art> datas;

    public List<Art> getDatas() {
        return datas;
    }

    public void setDatas(List<Art> datas) {
        this.datas = datas;
    }

}
