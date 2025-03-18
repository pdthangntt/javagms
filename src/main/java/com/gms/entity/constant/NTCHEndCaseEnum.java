package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum NTCHEndCaseEnum implements BaseParameterEnum<String> {
    ON_DINH {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Điều trị ổn định";
        }
    },
    DI_UNG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dị ứng thuốc";
        }
    },
    KHAC {
        @Override
        public String getKey() {
            return "-1";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
