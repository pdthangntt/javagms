package com.gms.entity.constant;

/**
 *
 * @author TrangBN
 */
public enum DecisionAnswerEnum implements BaseParameterEnum<Boolean> {
    TRUE {
        @Override
        public Boolean getKey() {
            return true;
        }

        @Override
        public String getLabel() {
            return "Đúng";
        }

    },
    FALSE {
        @Override
        public Boolean getKey() {
            return false;
        }

        @Override
        public String getLabel() {
            return "Sai";
        }

    }
}
