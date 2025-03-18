package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum InsurancePayEnum implements BaseParameterEnum<String> {
    ONEHUNDRED {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "100%";
        }
    },
    
    NINETYFIVE {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "95%";
        }
    },
    
    EIGHTY {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "80%";
        }
    },
    
    SIXTIES {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "60%";
        }
    },
    
    FORTY {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "40%";
        }
    },
    OTHER {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
