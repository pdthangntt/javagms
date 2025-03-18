package com.gms.entity.constant;

/**
 *
 * @author NamAnh_HaUI
 */
public enum ChangeStatusEnum implements BaseParameterEnum<String>{
    
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
    RETURN {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Điều trị lại";
        }
    },
    MOVE_IN {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Chuyển đến";
        }
    },
    MOVE_OUT {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Chuyển đi";
        }
    },
    REVOKE {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Bỏ điều trị";
        }
    },
    DEAD {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Tử vong";
        }
    }
}
