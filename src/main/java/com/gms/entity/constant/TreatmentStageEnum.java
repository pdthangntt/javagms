package com.gms.entity.constant;

/**
 * Trạng thái biến động điều trị
 *
 * @author vvthanh
 */
public enum TreatmentStageEnum implements BaseParameterEnum<String> {
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
    MOVED_AWAY {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Chuyển đi";
        }
    },
    TRANSFER {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Chuyển đến";
        }
    },
    LOSE_TRACK {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Mất dấu";
        }
    },
    CANCELLED {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Bỏ điều trị";
        }
    },
    DEAD {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Tử vong";
        }
    }, END {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Kết thúc";
        }
    }
}
