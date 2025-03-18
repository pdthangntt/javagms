package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import java.util.List;
import java.util.Map;

/**
 *
 * @author TrangBN
 */
public class SoKhangTheArvForm implements Comparable<SoKhangTheArvForm>{
    
    private SoKhangTheArvTableForm general;
    private String veryFirstRegiment;
    private String treatmentTime;
    private String changeRegimenIdDate;
    private Map<String, List<SoKhangTheArvTableForm>> data;

    public String getChangeRegimenIdDate() {
        return changeRegimenIdDate;
    }

    public void setChangeRegimenIdDate(String changeRegimenIdDate) {
        this.changeRegimenIdDate = changeRegimenIdDate;
    }
    
    public String getVeryFirstRegiment() {
        return veryFirstRegiment;
    }

    public void setVeryFirstRegiment(String veryFirstRegiment) {
        this.veryFirstRegiment = veryFirstRegiment;
    }
    
    public Map<String, List<SoKhangTheArvTableForm>> getData() {
        return data;
    }

    public SoKhangTheArvTableForm getGeneral() {
        return general;
    }

    public void setGeneral(SoKhangTheArvTableForm general) {
        this.general = general;
    }

    public String getTreatmentTime() {
        return this.general != null ? this.general.getTreatmentTime() : "";
    }

    public void setTreatmentTime(String treatmentTime) {
        this.treatmentTime = treatmentTime;
    }
    
    public void setData(Map<String, List<SoKhangTheArvTableForm>> data) {
        this.data = data;
    }
    
    @Override
    public int compareTo(SoKhangTheArvForm item) {
        try {
            return this.getTreatmentTime() != null ? TextUtils.convertDate(this.getTreatmentTime(), "dd/MM/yyyy").compareTo(TextUtils.convertDate(item.getTreatmentTime(), "dd/MM/yyyy")) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
