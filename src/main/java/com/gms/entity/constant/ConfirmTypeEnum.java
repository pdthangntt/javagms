package com.gms.entity.constant;

/**
 * Loại hình XN khẳng định
 * 
 * @author TrangBN
 */
public enum ConfirmTypeEnum implements BaseParameterEnum<String> {
    
    PCR_HIV {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "PCR HIV";
        }
    },
    // Huyết thanh
    SEROUS {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm huyết thanh";
        }
    }
}
