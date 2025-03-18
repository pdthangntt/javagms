package com.gms.entity.constant;

/**
 * Thể Lao
 * 
 * @author TrangBN
 */
public enum LaoVariableEnum implements BaseParameterEnum<String> {
    
    AFB_MINUS {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Lao AFB (-)";
        }
    },
    AFB_ADD {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Lao AFB (+)";
        }
    },
    LAO_NGOAI_PHOI {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Lao ngoài phổi";
        }
    },
    LAO_MANG_PHOI {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Lao màng phổi";
        }
    },
    OTHER {
        @Override
        public String getKey() {
            return "-1";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
