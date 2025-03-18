package com.gms.entity.form;

import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class HtcVisitSendConfirmForm implements Serializable {

    private String siteConfirmTest;
    private String sampleSentDate;

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public String getSampleSentDate() {
        return sampleSentDate;
    }

    public void setSampleSentDate(String sampleSentDate) {
        this.sampleSentDate = sampleSentDate;
    }

}
