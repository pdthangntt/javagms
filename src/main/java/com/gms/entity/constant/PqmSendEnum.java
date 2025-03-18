package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmSendEnum implements BaseParameterEnum<String> {
    
    CHUA_GUI {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa gửi";
        }
    },
    DA_GUI {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã gửi";
        }
    },
    DA_TONG_HOP {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đã tổng hợp";
        }
    },

}
