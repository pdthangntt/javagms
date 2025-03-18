package com.gms.entity.constant;

/**
 *
 * @author pdThang STATUS_OF_TREATMENT
 */
public enum StatusOfTreatmentEnum implements BaseParameterEnum<String> {
    CHUA_CO_THONG_TIN {

        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Chưa có thông tin";
        }
    },
    CHUA_DIEU_TRI {

        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Chưa điều trị";
        }
    },
    DS_CHO_DIEU_TRI {

        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Đang chờ điều trị (Pre-ARV)";
        }
    },
    MAT_DAU {

        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Mất dấu";
        }
    },
    TU_VONG {

        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Tử vong";
        }
    },
    DANG_DUOC_DIEU_TRI {

        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Đang được điều trị";
        }
    },
    BO_TRI {

        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Bỏ trị";
        }
    };

}
