package com.gms.entity.constant;

/**
 * Enum cho thông tin KQXN nhiễm mới HIV
 * 
 * @author TrangBN
 */
public enum VirusLoadResultEnum implements BaseParameterEnum<String> {
    
    //  NHỎ HƠN 6 THÁNG
    NOT_DETECT {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Không phát hiện";
        }

    },
    LESS_THAN_200 {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dưới ngưỡng phát hiện 200 bản sao/ml";
        }

    },
    FROM_200_1000 {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Ngưỡng phát hiện từ 200 - 1000 bản sao/ml";
        }
    },
    OVER_1000 {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Trên ngưỡng phát hiện 1000 bản sao/ml";
        }
    },
    NO_TEST {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Không xét nghiệm";
        }
    }
}
