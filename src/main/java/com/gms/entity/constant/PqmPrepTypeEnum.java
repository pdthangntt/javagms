package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmPrepTypeEnum implements BaseParameterEnum<String> {
    MOI {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Khách hàng mới";
        }
    },
    CU {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Khách hàng cũ quay lại điều trị";
        }
    }
}
