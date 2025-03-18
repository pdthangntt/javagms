package com.gms.entity.form.pac;

import com.gms.components.TextUtils;
import com.gms.entity.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author pdThang
 */
public class PacOutProvinceGenderTableTotal extends BaseForm implements Serializable {

    public PacOutProvinceGenderTableTotal() {
        permanentnam = 0;
        permanentnu = 0;
        permanentkhongro = 0;
        notmanagenam = 0;
        notmanagenu = 0;
        notmanagekhongro = 0;
        hasmanagenam = 0;
        hasmanagenu = 0;
        hasmanagekhongro = 0;
    }

    private int permanentnam;
    private int permanentnu;
    private int permanentkhongro;
    private int notmanagenam;
    private int notmanagenu;
    private int notmanagekhongro;
    private int hasmanagenam;
    private int hasmanagenu;
    private int hasmanagekhongro;
    private int sum;

    public int getSum() {
        return permanentnam
                + permanentnu
                + permanentkhongro
                + notmanagenam
                + notmanagenu
                + notmanagekhongro
                + hasmanagenam
                + hasmanagenu
                + hasmanagekhongro;

    }

    public int getPermanentnam() {
        return permanentnam;
    }

    public void setPermanentnam(int permanentnam) {
        this.permanentnam = permanentnam;
    }

    public int getPermanentnu() {
        return permanentnu;
    }

    public void setPermanentnu(int permanentnu) {
        this.permanentnu = permanentnu;
    }

    public int getPermanentkhongro() {
        return permanentkhongro;
    }

    public void setPermanentkhongro(int permanentkhongro) {
        this.permanentkhongro = permanentkhongro;
    }

    public int getNotmanagenam() {
        return notmanagenam;
    }

    public void setNotmanagenam(int notmanagenam) {
        this.notmanagenam = notmanagenam;
    }

    public int getNotmanagenu() {
        return notmanagenu;
    }

    public void setNotmanagenu(int notmanagenu) {
        this.notmanagenu = notmanagenu;
    }

    public int getNotmanagekhongro() {
        return notmanagekhongro;
    }

    public void setNotmanagekhongro(int notmanagekhongro) {
        this.notmanagekhongro = notmanagekhongro;
    }

    public int getHasmanagenam() {
        return hasmanagenam;
    }

    public void setHasmanagenam(int hasmanagenam) {
        this.hasmanagenam = hasmanagenam;
    }

    public int getHasmanagenu() {
        return hasmanagenu;
    }

    public void setHasmanagenu(int hasmanagenu) {
        this.hasmanagenu = hasmanagenu;
    }

    public int getHasmanagekhongro() {
        return hasmanagekhongro;
    }

    public void setHasmanagekhongro(int hasmanagekhongro) {
        this.hasmanagekhongro = hasmanagekhongro;
    }

}
