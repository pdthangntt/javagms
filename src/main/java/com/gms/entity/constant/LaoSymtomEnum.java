package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum LaoSymtomEnum implements BaseParameterEnum<String> {
    NOT {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Không có";
        }
    },
    HO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Ho trên 2 tuần";
        }
    },
    SOT {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Sốt trên 2 tuần";
        }
    },
    SUTCAN {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Sụt cân trên 2 tuần";
        }
    },
    RAMOHOI {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Ra mồ hôi trên 2 tuần";
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
