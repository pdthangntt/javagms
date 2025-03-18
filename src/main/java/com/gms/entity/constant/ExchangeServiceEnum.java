package com.gms.entity.constant;

/**
 * Dịch vụ tư vấn chuyển gửi
 * 
 * @author TrangBN
 */
public enum ExchangeServiceEnum implements BaseParameterEnum<String> {
    
    ARV {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Điều trị ARV";
        }

    },
    LAO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Khám và sàng lọc Lao";
        }
    },
    TINH_DUC {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Các bệnh lây truyền qua đường tình dục";
        }
    },
    MMT {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "MMT";
        }
    },
    KHAC {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
    
}
