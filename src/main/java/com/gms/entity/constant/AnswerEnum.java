package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum AnswerEnum implements BaseParameterEnum<Integer> {
    YES {
        @Override
        public Integer getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Có";
        }

    },
    NO {
        @Override
        public Integer getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Không";
        }

    }
}
