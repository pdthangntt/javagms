package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmPrepStatusEnum implements BaseParameterEnum<String> {
    TIEP_TUC {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Tiếp tục sử dụng PrEP";
        }
    },
    DUNG_DO_NHIEM_HIV {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dừng sử dụng PrEP do nhiễm mới HIV";
        }
    },
    DUNG_DO_DOC_TINH {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Dừng sử dụng PrEP do độc tính của thuốc";
        }
    },
    DUNG_KHONG_CON_NGUY_CO {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Dừng sử dụng PrEP do khách hàng không còn nguy cơ";
        }
    },
    DUNG_DO_KHAC {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Dừng do nguyên nhân khác (di chuyển nơi ở,...)";
        }
    },
    MAT_DAU {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Mất dấu khách hàng (trong vòng 30 ngày kể từ ngày hẹn tái khám)";
        }
    }
}
