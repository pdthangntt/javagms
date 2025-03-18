package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pdthang
 */
public class PqmHtcApiRecencyMainForm extends BaseEntity implements Serializable {

    private List<PqmHtcApiRecencyForm> datas;

    public List<PqmHtcApiRecencyForm> getDatas() {
        return datas;
    }

    public void setDatas(List<PqmHtcApiRecencyForm> datas) {
        this.datas = datas;
    }

}
