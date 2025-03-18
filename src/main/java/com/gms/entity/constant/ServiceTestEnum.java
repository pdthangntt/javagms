package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum ServiceTestEnum implements BaseParameterEnum<String> {
    CD {
        @Override
        public String getKey() {
            return "CD";
        }

        @Override
        public String getLabel() {
            return "Cố định";
        }

    },
    LD {
        @Override
        public String getKey() {
            return "LD";
        }

        @Override
        public String getLabel() {
            return "Lưu Động";
        }

    },
    KC {
        @Override
        public String getKey() {
            return "KC";
        }

        @Override
        public String getLabel() {
            return "XN tại cộng đồng";
        }

    },
}
