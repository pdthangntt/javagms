package com.gms.entity.constant;

/**
 *
 * @author NamAnh_HaUI
 */
public enum FeedbackStatusEnum implements BaseParameterEnum<String>{
    NOT {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa đối chiếu";
        }

    },
    COLLATED {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã đối chiếu";
        }

    },
    WAITED {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Chờ phản hồi";
        }

    },
    
    REFUSED {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Từ chối yêu cầu";
        }

    },
    CONFIRMED {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Đã xác nhận yêu cầu";
        }

    }
    
}
