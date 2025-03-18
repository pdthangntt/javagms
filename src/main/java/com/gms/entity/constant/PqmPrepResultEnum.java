package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmPrepResultEnum implements BaseParameterEnum<String> {
    DUONG_TINH {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Dương tính";
        }
    },
    AM_TINH {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Âm tính";
        }
    },
    KHONG_RO {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Không xác định";
        }
    }
}
