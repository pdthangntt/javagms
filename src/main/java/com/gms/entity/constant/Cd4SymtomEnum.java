package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum Cd4SymtomEnum implements BaseParameterEnum<String> {
    DINHKY3THANG {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm định kỳ 3 tháng";
        }
    },
    DINHKY6THANG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm định kỳ 6 tháng";
        }
    },
    DINHKY12THANG {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm định kỳ 12 tháng";
        }
    },
    MIENDICH {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Đánh giá mức độ suy giảm miễn dịch và giai đoạn bệnh trong nhiễm HIV";
        }
    },
    VIRUS {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Chỉ định điều trị thuốc kháng virus.";
        }
    },
    DANHGIA {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Theo dõi hiệu quả điều trị và đánh giá thành công hay thất bại trong điều trị";
        }
    },
    OTHER {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
