package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum BooleanEnum implements BaseParameterEnum<Integer> {
    TRUE {
        @Override
        public Integer getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Có";
        }

    },
    FALSE {
        @Override
        public Integer getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Không";
        }

    }
}
