package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum PacTabEnum implements BaseEnum<String> {
    NEW_IN_PROVINCE {
        @Override
        public String getKey() {
            return "new_in_province";
        }
        
    },
    NEW_OUT_PROVINCE {
        @Override
        public String getKey() {
            return "new_out_province";
        }
        
    },
    NEW_OTHER_PROVINCE {
        @Override
        public String getKey() {
            return "new_other_province";
        }
        
    },
    DELETE {
        @Override
        public String getKey() {
            return "delete";
        }
        
    }
}
