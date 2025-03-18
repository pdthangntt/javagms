package com.gms.entity.constant;

/**
 * Enum trạng kết quả chuyển gửi điều trị (không chuyên)
 * 
 * @author TrangBN
 */
public enum ExchangeStatusEnum implements BaseParameterEnum<String>{
    
    SUCCESS {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Không thành công";
        }
    },
    FAIL {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Thành công";
        }
    }
}
