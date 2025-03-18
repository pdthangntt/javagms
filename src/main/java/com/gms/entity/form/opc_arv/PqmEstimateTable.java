package com.gms.entity.form.opc_arv;

public class PqmEstimateTable {

    private String drugNameLowercase;

    private String drugUomLowercase;

    private int plannedQuantity;//mẫu

    private int dispensedQuantity;//tử

    public String getDrugNameLowercase() {
        return drugNameLowercase;
    }

    public void setDrugNameLowercase(String drugNameLowercase) {
        this.drugNameLowercase = drugNameLowercase;
    }

    public String getDrugUomLowercase() {
        return drugUomLowercase;
    }

    public void setDrugUomLowercase(String drugUomLowercase) {
        this.drugUomLowercase = drugUomLowercase;
    }

    public int getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(int plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public int getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(int dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }

}
