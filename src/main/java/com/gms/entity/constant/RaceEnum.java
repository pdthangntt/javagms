package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum RaceEnum implements BaseParameterEnum<String> {
    KINH {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Kinh";
        }
    }
}
