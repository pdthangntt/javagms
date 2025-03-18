package com.gms.entity.rabbit;

import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.json.hivinfo.patient.Data;
import java.util.HashMap;

/**
 *
 * @author vvthanh
 */
public class PacPatientMessage extends RabbitMessage {

    private Data data;
    private PacPatientInfoEntity models;
    HashMap<String, HashMap<String, String>> options;

    public PacPatientMessage() {
    }

    public PacPatientMessage(Data data, PacPatientInfoEntity models, HashMap<String, HashMap<String, String>> options) {
        this.data = data;
        this.models = models;
        this.options = options;
    }

    public PacPatientInfoEntity getModels() {
        return models;
    }

    public void setModels(PacPatientInfoEntity models) {
        this.models = models;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
