package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum IndicatorInputTypeEnum implements BaseParameterEnum<Integer> {

    INPUT {
        @Override
        public Integer getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Nhập tay";
        }
    },
    JOB {
        @Override
        public Integer getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Tự động";
        }
    }

}
