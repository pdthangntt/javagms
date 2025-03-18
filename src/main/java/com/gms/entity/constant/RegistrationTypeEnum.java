package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum RegistrationTypeEnum implements BaseParameterEnum<String> {
    NEW {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đăng ký mới";
        }
    },
    REPLAY {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Điều trị lại";
        }
    },
    TRANSFER {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Chuyển đến";
        }
    },
    CHILDREN_UNDER_18 {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Trẻ em dưới 18 tháng tuổi sinh ra từ mẹ nhiễm HIV";
        }
    },
    EXPOSURE {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Điều trị phơi nhiễm";
        }
    }
}
