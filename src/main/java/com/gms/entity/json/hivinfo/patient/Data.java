package com.gms.entity.json.hivinfo.patient;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public class Data {

    private String type;
    private Gsph gsph;
    private GsphHiv hiv;
    private GsphHivDieuTri opc;
    private GsphTuVong deal;
    private GsphInput input;
    private GsphAids aids;
    private List<Integer> deadCause;

    public GsphAids getAids() {
        return aids;
    }

    public void setAids(GsphAids aids) {
        this.aids = aids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getDeadCause() {
        return deadCause;
    }

    public void setDeadCause(List<Integer> deadCause) {
        this.deadCause = deadCause;
    }

    public GsphInput getInput() {
        return input;
    }

    public void setInput(GsphInput input) {
        this.input = input;
    }

    public GsphHiv getHiv() {
        return hiv;
    }

    public void setHiv(GsphHiv hiv) {
        this.hiv = hiv;
    }

    public GsphHivDieuTri getOpc() {
        return opc;
    }

    public void setOpc(GsphHivDieuTri opc) {
        this.opc = opc;
    }

    public GsphTuVong getDeal() {
        return deal;
    }

    public void setDeal(GsphTuVong deal) {
        this.deal = deal;
    }

    public Gsph getGsph() {
        return gsph;
    }

    public void setGsph(Gsph gsph) {
        this.gsph = gsph;
    }

}
