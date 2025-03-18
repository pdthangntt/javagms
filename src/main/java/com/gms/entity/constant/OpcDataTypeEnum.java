package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum OpcDataTypeEnum implements BaseEnum<String> {
    IMS {
        @Override
        public String getKey() {
            return "ims";
        }
    },
    HIS_VNPT {
        @Override
        public String getKey() {
            return "hvnpt";
        }
    }
}
