package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum OpcParameterTypeEnum implements BaseEnum<String> {
    ARV {
        @Override
        public String getKey() {
            return "arv";
        }
    },
    STAGE {
        @Override
        public String getKey() {
            return "stage";
        }
    },
    TEST {
        @Override
        public String getKey() {
            return "test";
        }
    },
    VIRAL_LOAD {
        @Override
        public String getKey() {
            return "viralload";
        }
    },
}
