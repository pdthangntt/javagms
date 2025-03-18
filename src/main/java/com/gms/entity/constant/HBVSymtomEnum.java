package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum HBVSymtomEnum implements BaseParameterEnum<String> {
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
