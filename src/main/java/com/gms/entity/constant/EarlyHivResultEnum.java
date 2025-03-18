package com.gms.entity.constant;

/**
 * Enum cho thông tin KQXN nhiễm mới HIV
 * 
 * @author TrangBN
 */
public enum EarlyHivResultEnum implements BaseParameterEnum<String> {
    
    //  NHỎ HƠN 6 THÁNG
    LESS_EQUAL_SIX_MONTH {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "nhiễm mới HIV ≤ 6 tháng";
        }

    },
    OVER_TWELVE_MONTH {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Nhiễm HIV > 12 tháng";
        }

    },
    NO_TEST {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Không xét nghiệm";
        }
    }
}
