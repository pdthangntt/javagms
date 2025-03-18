package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum OpcParameterAttributeEnum implements BaseEnum<String> {
    LAO_SYMPTOM {
        @Override
        public String getKey() {
            return "lao_symptom";
        }
    },
    INH_END_CAUSE {
        @Override
        public String getKey() {
            return "inh_end_cause";
        }
    },
    NTCH_SYMPTOM {
        @Override
        public String getKey() {
            return "ntch_symptom";
        }
    },
    COTRIMOXAZOLE_END_CAUSE {
        @Override
        public String getKey() {
            return "cotrimoxazole_end_cause";
        }
    },
    FIRST_CD4_CAUSE {
        @Override
        public String getKey() {
            return "first_cd4_cause";
        }
    },
    CD4_CAUSE {
        @Override
        public String getKey() {
            return "cd4_cause";
        }
    },
    FIRST_VIRAL_LOAD_CAUSE {
        @Override
        public String getKey() {
            return "first_viralload_cause";
        }
    },
    VIRAL_LOAD_CAUSE {
        @Override
        public String getKey() {
            return "viralload_cause";
        }
    },
}
