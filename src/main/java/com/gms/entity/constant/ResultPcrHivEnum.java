package com.gms.entity.constant;

/**
 * Xét nghiệm PCR HIV bổ sung kết quả PCR HIV
 * 
 * @author TrangBN
 */
public enum ResultPcrHivEnum implements BaseParameterEnum<String> {
    
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
    
    DUONG_TINH {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dương tính";
        }
    }
}
