package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmPrepTreatmentEnum implements BaseParameterEnum<String> {
    HANG_NGAY {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "PrEP hàng ngày";
        }
    },
    TINH_HUONG {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "PrEP theo tình huống";
        }
    }
}
