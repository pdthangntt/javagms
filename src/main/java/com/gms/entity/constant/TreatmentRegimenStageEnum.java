package com.gms.entity.constant;

/**
 * Bậc điều trị
 *
 * @author vvthanh
 */
public enum TreatmentRegimenStageEnum implements BaseParameterEnum<String> {
    ONE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Bậc 1";
        }
    },
    TWO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Bậc 2";
        }
    },
    EXPOSURE {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Phơi nhiễm";
        }
    }
}
