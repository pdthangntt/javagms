package com.gms.entity.form;

import com.gms.components.TextUtils;

/**
 *
 *
 * @author vvThành
 */
public abstract class BaseForm {

    protected boolean printable;

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public String displayNumber(int number) {
        if (number == 0) {
            return this.printable ? String.valueOf(number) : "";
        }
        return TextUtils.numberFormat(number);
    }

    public String displayPercent(String percent) {
        if (percent == null || percent.equals("") || percent.equals("0%")) {
            return this.printable ? percent : "";
        }
        return percent;
    }

    public String numberFormat(Object number) {
        return TextUtils.numberFormat(number);
    }
}
