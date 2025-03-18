package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum HasHealthInsuranceEnum implements BaseParameterEnum<Integer> {
    CO {
        @Override
        public Integer getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Có";
        }

    },
    KHONG {
        @Override
        public Integer getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Không";
        }

    },
    KHONG_CO_THONG_TIN {
        @Override
        public Integer getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return "Không có thông tin";
        }

    }
}
