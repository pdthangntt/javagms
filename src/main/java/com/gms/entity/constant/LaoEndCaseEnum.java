package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum LaoEndCaseEnum implements BaseParameterEnum<String> {
    PHANUNGPHU {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Phản ứng phụ";
        }
    },
    DIEUTRILAO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Điều trị Lao";
        }
    },
    THUOCMOI {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Điều trị thuốc mới";
        }
    },
    KHONGCOTHUOC {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Không có thuốc";
        }
    },
    MIENDICH {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Thất bại miễn dịch";
        }
    },
    LAMSANG {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Thất bại lâm sàng";
        }
    },
    VIRUS {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Thất bại virus";
        }
    },
    INH {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Hoàn thành điều trị INH";
        }
    },
    OTHER {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
