package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum JobEnum implements BaseParameterEnum<String> {
    KHAC {
        @Override
        public String getKey() {
            return "i9";
        }

        @Override
        public String getLabel() {
            return "Nghề khác";
        }
    }
}
