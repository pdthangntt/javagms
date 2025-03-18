package com.gms.entity.rabbit;

import com.gms.entity.db.HtcConfirmEntity;

/**
 *
 * @author vvthanh
 */
public class HtcConfirmMessage extends RabbitMessage {

    private HtcConfirmEntity confirm;

    public HtcConfirmEntity getConfirm() {
        return confirm;
    }

    public void setConfirm(HtcConfirmEntity confirm) {
        this.confirm = confirm;
    }

}
