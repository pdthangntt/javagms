package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum HCVSymtomEnum implements BaseParameterEnum<String> {
    OTHER {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
