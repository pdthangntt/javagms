package com.gms.entity.constant;

/**
 * Hình thức thông báo trong thêm mới laytest
 * 
 * @author TrangBN
 */
public enum HtcLaytestAlertTypeEnum implements BaseParameterEnum<String> {
    
    //Tự thông báo
    TU_THONG_BAO {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Tự thông báo";
        }
    },
    //Cùng thông báo
    CUNG_THONG_BAO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Cùng thông báo";
        }
    },
    // Nhân viên tư vấn thông báo
    NVTN_THONG_BAO {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Nhân viên tư vấn thông báo";
        }
    },
    // Được phép tiết lộ danh tính người nhiễm HIV và NVTV thông báo
    TIET_LO_DANH_TINH {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Được phép tiết lộ danh tính người nhiễm HIV và NVTV thông báo";
        }
    },
    // Không được phép tiết lộ danh tính người nhiễm HIV
    KO_TIET_LO_DANH_TINH {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Không được phép tiết lộ danh tính người nhiễm HIV";
        }
    }
}
