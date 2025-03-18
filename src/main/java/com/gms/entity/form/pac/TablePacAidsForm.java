package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author DSNAnh
 */
public class TablePacAidsForm implements Serializable, Cloneable {

    private int stt;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String displayName;
    private int male;
    private int female;
    private int other;
    private int maleDeath;
    private int femaleDeath;
    private int otherDeath;
    private int maleAlive;
    private int femaleAlive;
    private int otherAlive;
    private List<TablePacAidsForm> childs;

    public int getMaleDeath() {
        return maleDeath;
    }

    public void setMaleDeath(int maleDeath) {
        this.maleDeath = maleDeath;
    }

    public int getFemaleDeath() {
        return femaleDeath;
    }

    public void setFemaleDeath(int femaleDeath) {
        this.femaleDeath = femaleDeath;
    }

    public int getOtherDeath() {
        return otherDeath;
    }

    public void setOtherDeath(int otherDeath) {
        this.otherDeath = otherDeath;
    }

    public int getMaleAlive() {
        return maleAlive;
    }

    public void setMaleAlive(int maleAlive) {
        this.maleAlive = maleAlive;
    }

    public int getFemaleAlive() {
        return femaleAlive;
    }

    public void setFemaleAlive(int femaleAlive) {
        this.femaleAlive = femaleAlive;
    }

    public int getOtherAlive() {
        return otherAlive;
    }

    public void setOtherAlive(int otherAlive) {
        this.otherAlive = otherAlive;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public List<TablePacAidsForm> getChilds() {
        return childs;
    }

    public void setChilds(List<TablePacAidsForm> childs) {
        this.childs = childs;
    }

    public String getOtherPercent() {
        return TextUtils.toPercent(this.other, this.getSum());
    }

    public String getMalePercent() {
        return TextUtils.toPercent(this.male, this.getSum());
    }

    public String getFemalePercent() {
        return TextUtils.toPercent(this.female, this.getSum());
    }

    public String getSumPercent(int total) {
        return TextUtils.toPercent(this.getSum(), total);
    }

    public int getSum() {
        return male + female + other;
    }
    
    public int getSumDeath() {
        return maleDeath + femaleDeath + otherDeath;
    }
    
    public int getSumAlive() {
        return maleAlive + femaleAlive + otherAlive;
    }

    @Override
    public TablePacAidsForm clone() throws CloneNotSupportedException {
        return (TablePacAidsForm) super.clone();
    }

}
