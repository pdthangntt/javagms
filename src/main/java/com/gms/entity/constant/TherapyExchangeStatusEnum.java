package com.gms.entity.constant;

/**
 * Trạng thái chuyển gửi điều trị
 * 
 * @author TrangBN
 */
public enum TherapyExchangeStatusEnum implements BaseParameterEnum<String>{

    CHUA_CHUYEN {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa chuyển";
        }
    },
    DA_CHUYEN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã chuyển gửi";
        }
    },
    CHUYEN_THANH_CONG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Chuyển thành công";
        }
    },
    DA_TIEP_NHAN {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Đã tiếp nhận";
        }
    }
}
