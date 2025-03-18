package com.gms.entity.constant;

/**
 *
 * @author NamAnh_HaUI
 */
public enum ConnectStatusEnum implements BaseParameterEnum<String>{
    
    NOT_CONNECT {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Chưa kết nối";
        }
    },
    CONNECT {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đã kết nối";
        }
    }
}
