package com.gms.entity.constant;

/**
 *  Kết quả test sàng lọc
 * 
 * @author TrangBN
 */
public enum TestResultEnum implements BaseParameterEnum<String> {
    KHONG_PHAN_UNG {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Có phản ứng";
        }
    },
    CO_PHAN_UNG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Không phản ứng";
        }
    },
    KHONG_RO {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Không rõ";
        }
    },
    ONLY_NOTICE {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Không có phản ứng và có chỉ báo xét nghiệm nhiễm cấp";
        }
    },
}
