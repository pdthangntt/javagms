package com.gms.entity.constant;

/**
 * Trạng thái giám sát phát hiện
 * 
 * @author TrangBN
 */
public enum GsphStatusEnum implements BaseParameterEnum<String>{

    CHUA_CHUYEN {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa chuyển";
        }
    },
    DA_CHUYEN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã chuyển";
        }
    }
}
