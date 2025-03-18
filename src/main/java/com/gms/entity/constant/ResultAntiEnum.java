package com.gms.entity.constant;

/**
 * KQXN Kháng nguyên/Kháng thể
 * 
 * @author TrangBN
 */
public enum ResultAntiEnum implements BaseParameterEnum<String> {
    
    KHANG_NGUYEN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Kháng nguyên (+)";
        }

    },
    KHANG_THE {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Kháng thể (+)";
        }
    },
    KHANG_NGUYEN_THE {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Kháng nguyên (+) và Kháng thể (+)";
        }
    }
}
