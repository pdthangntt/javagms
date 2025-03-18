package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum AidsStatusEnum implements BaseParameterEnum<String> {
    HIV {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Giai đoạn HIV";
        }
    },
    AIDS {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Giai đoạn AIDS";
        }

    }
}
