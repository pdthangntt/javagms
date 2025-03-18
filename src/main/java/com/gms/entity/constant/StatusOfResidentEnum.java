package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum StatusOfResidentEnum implements BaseParameterEnum<String> {
    MAT_DAU {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Mất dấu";
        }

    },
    DANG_O_DIA_PHUONG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đang ở địa phương";
        }

    },
    KHONG_CO_THUC_TE {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Không có thực tế";
        }

    },
    DI_TRAI {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Đi trại";
        }

    },
    CHUYEN_DI_NOI_KHAC {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Chuyển đi nơi khác";
        }

    },
    DI_LAM_AN_XA {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Đi làm ăn xa";
        }

    },
    CHUYEN_TRONG_TINH{
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Chuyển trong tỉnh";
        }

    },
    CHUA_XAC_DINH {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Chưa xác định được";
        }

    }
}
