package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum ArvEndCaseEnum implements BaseParameterEnum<String> {
    CANCELLED {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Bỏ trị";
        }
    },
    DEAD {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Tử vong";
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
    LOSE_TRACK {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Mất dấu";
        }
    },
    END {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Kết thúc";
        }
    }
}
