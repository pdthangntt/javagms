package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum GenderEnum implements BaseParameterEnum<String> {
    
    MALE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Nam";
        }
    },
    
    FEMALE {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Nữ";
        }
    },
    
    NONE {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Không rõ";
        }
    }
}
