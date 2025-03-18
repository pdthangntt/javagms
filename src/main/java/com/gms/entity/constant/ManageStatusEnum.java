package com.gms.entity.constant;

/**
 *
 * @author TrangBN
 */
public enum ManageStatusEnum implements BaseParameterEnum<String> {
    NN_PHAT_HIEN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Người nhiễm chưa rà soát";
        }
    },
    NN_CAN_RA_SOAT {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Người nhiễm cần rà soát";
        }

    },
    NN_DA_RA_SOAT {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Người nhiễm đã rà soát";
        }

    },
    NN_QUAN_LY {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Người nhiễm quản lý";
        }

    }
}
