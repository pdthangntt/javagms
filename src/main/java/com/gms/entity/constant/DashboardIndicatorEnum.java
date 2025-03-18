package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum DashboardIndicatorEnum implements BaseParameterEnum<String> {
    GENDER {
        @Override
        public String getKey() {
            return "100";
        }

        @Override
        public String getLabel() {
            return "Giới tính";
        }

    }
}
