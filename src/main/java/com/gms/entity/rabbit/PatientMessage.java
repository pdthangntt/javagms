package com.gms.entity.rabbit;

import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.SiteEntity;

/**
 *
 * @author vvthanh
 */
public class PatientMessage {

    private String sourceType;
    private SiteEntity site;
    private HtcVisitEntity visit;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public SiteEntity getSite() {
        return site;
    }

    public void setSite(SiteEntity site) {
        this.site = site;
    }

    public HtcVisitEntity getVisit() {
        return visit;
    }

    public void setVisit(HtcVisitEntity visit) {
        this.visit = visit;
    }

}
