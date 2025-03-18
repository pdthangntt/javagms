package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 * Form báo cáo huyện xã
 *
 * @author TrangBN
 */
public class PacLocalTableForm implements Serializable, Cloneable {

    private String provinceName;
    private String provinceID;
    private String districtName;
    private String districtID;
    private String wardName;
    private String wardID;
    private String displayName;

    private int stt;
    private int numDead;
    private int numAlive;
    private int notReviewYet;
    private int needReview;
    private int reviewed;
    private int outprovince;

    private List<PacLocalTableForm> childs;

    public int getOutprovince() {
        return outprovince;
    }

    public void setOutprovince(int outprovince) {
        this.outprovince = outprovince;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public int getNumDead() {
        return numDead;
    }

    public void setNumDead(int numDead) {
        this.numDead = numDead;
    }

    public int getNumAlive() {
        return numAlive;
    }

    public void setNumAlive(int numAlive) {
        this.numAlive = numAlive;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getNotReviewYet() {
        return notReviewYet;
    }

    public void setNotReviewYet(int notReviewYet) {
        this.notReviewYet = notReviewYet;
    }

    public int getNeedReview() {
        return needReview;
    }

    public void setNeedReview(int needReview) {
        this.needReview = needReview;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }

    public List<PacLocalTableForm> getChilds() {
        return childs;
    }

    public void setChilds(List<PacLocalTableForm> childs) {
        this.childs = childs;
    }

    /**
     * Lũy tích còn sống tử vong
     *
     * @return
     */
    public int getCumulative() {
        return numAlive + numDead;
    }

    public int getManageTotal() {
        return this.getCumulative();
    }

    @Override
    public PacLocalTableForm clone() throws CloneNotSupportedException {
        return (PacLocalTableForm) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
