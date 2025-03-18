package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum HtcConfirmStatusEnum implements BaseParameterEnum<String> {
    PENDING {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chờ khẳng định";
        }

    },
    DOING {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã tiếp nhận";
        }

    },
    DONE {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đã có kết quả";
        }

    },
    PAID {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Đã trả kết quả";
        }

    },
    IGNORE {
        @Override
        public String getKey() {
            return "-1";
        }

        @Override
        public String getLabel() {
            return "Bỏ qua đồng bộ";
        }

    }
}
