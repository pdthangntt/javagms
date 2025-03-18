package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum MedicationAdherenceEnum implements BaseParameterEnum<String> {
    NONE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Không nhận xét";
        }
    },
    GOOD {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Tuân thủ tốt";
        }
    },
    NOTGOOD {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Tuân thủ kém";
        }
    }
}
