package com.gms.entity.constant;

/**
 * Loại bệnh nhân
 * 
 * @author TrangBN
 */
public enum CustomerTypeEnum implements BaseParameterEnum<String> {
    
    RESIDENT {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Nội trú";
        }
    },
    
    NON_RESIDENT {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Ngoại trú";
        }
    }
}
