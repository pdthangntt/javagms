package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum PacPatientStatusEnum implements BaseParameterEnum<String> {
    NEW {  
        @Override
        public String getKey() {
            return "new";
        }

        @Override
        public String getLabel() {
            return "Ca mới";
        }

    },
    OLD {
        @Override
        public String getKey() {
            return "old";
        }

        @Override
        public String getLabel() {
            return "Ca cũ";
        }

    }
}
