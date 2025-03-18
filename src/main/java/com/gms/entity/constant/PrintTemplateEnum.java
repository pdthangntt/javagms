package com.gms.entity.constant;

/**
 *
 * @author TrangBN
 */
public enum PrintTemplateEnum implements BaseParameterEnum<String> {
    NATION {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Quy định quốc gia";
        }
    },
    SHIFT {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Dự án SHIFT";
        }

    }
}
