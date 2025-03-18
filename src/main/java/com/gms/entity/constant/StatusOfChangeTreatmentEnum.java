package com.gms.entity.constant;

/**
 *
 * @author pdThang STATUS_OF_TREATMENT
 */
public enum StatusOfChangeTreatmentEnum implements BaseParameterEnum<String> {

    CHUYEN_DI {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Chuyển đi";
        }
    }
}
