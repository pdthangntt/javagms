package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmHubEnum implements BaseParameterEnum<String> {
    NHAP_DU_LIEU_THU_CAP {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Nhập thứ cấp";
        }
    },
    IMPORT_DU_LIEU {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Import Excel";
        }
    },
    FROM_IMS {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "API IMS";
        }
    }

}
