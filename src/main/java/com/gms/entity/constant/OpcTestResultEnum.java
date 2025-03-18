package com.gms.entity.constant;

/**
 * Kết quả test sàng lọc
 *
 * @author TrangBN
 */
public enum OpcTestResultEnum implements BaseParameterEnum<String> {
    DUONG_TINH {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dương tính";
        }
    },
    AM_TINH {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Âm tính";
        }
    },
}
