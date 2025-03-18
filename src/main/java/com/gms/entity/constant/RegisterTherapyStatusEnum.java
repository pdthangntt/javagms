package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum RegisterTherapyStatusEnum implements BaseParameterEnum<String> {
    NO_REGISTER {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Chưa đăng ký điều trị";
        }
    },
    REGISTED {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đã đăng ký điều trị";
        }
    }
}
