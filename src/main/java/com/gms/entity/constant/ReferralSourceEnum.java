package com.gms.entity.constant;

/**
 * Nguồn giới thiệu sử dụng dịch vụ TVXN
 * 
 * @author TrangBN
 */
public enum ReferralSourceEnum implements BaseParameterEnum<String> {
    // Tiếp cận cộng đồng
    TC_CONG_DONG {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Tiếp cận cộng đồng";
        }
    },
    KENH_BTBC {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Kênh xét nghiệm theo dấu bạn tình/bạn chích";
        }
    },
    INTERNET {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Internet, mạng xã hội";
        }
    },
    KHAC {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Các kênh khác";
        }
    },
    CAN_BO_YTE {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Cán bộ y tế";
        }
    }
}
