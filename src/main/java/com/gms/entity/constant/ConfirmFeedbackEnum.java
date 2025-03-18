package com.gms.entity.constant;

/**
 * Trạng thái phản hồi
 * 
 * @author pdThang
 */
public enum ConfirmFeedbackEnum implements BaseParameterEnum<String> {
    CHUA_XAC_NHAN {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa xác nhận";
        }
    },
    DA_XAC_NHAN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Đã xác nhận yêu cầu";
        }
    },
    TU_CHOI {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Từ chối yêu cầu";
        }
    },

}
