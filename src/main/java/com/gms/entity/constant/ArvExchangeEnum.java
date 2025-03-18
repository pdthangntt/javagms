package com.gms.entity.constant;

/**
 * Trạng thái Tư vấn chuyển gửi ARV
 * 
 * @author TrangBN
 */
public enum ArvExchangeEnum implements BaseParameterEnum<String>{

    DONG_Y {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đồng ý chuyển gửi";
        }
    },
    CHUA_DONG_Y {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Chưa đồng ý chuyển gửi";
        }
    }
}
