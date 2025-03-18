package com.gms.entity.constant;

/**
 * 
 * @author TrangBN
 */
public enum InitCodeEnum implements BaseParameterEnum<String> {
    
    // Thêm khơi tạo code
    RIGHT_PADDING {
        @Override
        public String getKey() {
            return "0000001";
        }

        @Override
        public String getLabel() {
            return "Khởi tạo tự đếm mã";
        }
    },
    RIGHT_PADDING_LAYTEST {
        @Override
        public String getKey() {
            return "000001";
        }

        @Override
        public String getLabel() {
            return "Khởi tạo tự đếm mã";
        }
    },
    RIGHT_PADDING_OPC {
        @Override
        public String getKey() {
            return "00001";
        }

        @Override
        public String getLabel() {
            return "Khởi tạo tự đếm mã";
        }
    }
}
