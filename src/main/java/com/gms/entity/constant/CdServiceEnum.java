package com.gms.entity.constant;

/**
 * Enum cho khách hàng đến cơ sở TVXN cố định để nhận dịch vụ
 * 
 * @author TrangBN
 */
public enum CdServiceEnum implements BaseParameterEnum<String> {
    
    // Xét nghiệm sàng lọc
    SANG_LOC {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm sàng lọc";
        }

    },
    // Lấy máu hỗ trợ xét nghiệm khẳng định
    GET_BLOOD {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Lấy máu hỗ trợ xét nghiệm khẳng định";
        }
    }
}
