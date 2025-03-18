package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum InsuranceEnum implements BaseParameterEnum<String> {
    TRUE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Có";
        }
    },
}
