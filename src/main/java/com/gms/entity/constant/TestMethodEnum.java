package com.gms.entity.constant;

/**
 * Phương pháp XN sàng lọc
 * 
 * @author TrangBN
 */
public enum TestMethodEnum implements BaseParameterEnum<String> {
    
    KHANG_THE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm kháng thể";
        }

    },
    // Kháng nguyên, kháng thể
    KHANG_NGUYEN_KHANG_THE {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm kháng nguyên, kháng thể";
        }
    }
}
