package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum IndicatorHtcEnum implements BaseParameterEnum<String> {
    HTC_01 {
        @Override
        public String getLabel() {
            return "Số người được tư vấn trước xét nghiệm HIV";
        }

        @Override
        public String getKey() {
            return "1";
        }
    },
    HTC_02 {
        @Override
        public String getLabel() {
            return "Số người được xét nghiệm HIV";
        }

        @Override
        public String getKey() {
            return "2";
        }
    },
    HTC_03 {
        @Override
        public String getLabel() {
            return "Số người xét nghiệm HIV có kết quả khẳng định dương tính ";
        }

        @Override
        public String getKey() {
            return "3";
        }
    },
    HTC_04 {
        @Override
        public String getLabel() {
            return "Số người xét nghiệm HIV nhận kết quả xét nghiệm";
        }

        @Override
        public String getKey() {
            return "4";
        }
    },
    HTC_05 {
        @Override
        public String getLabel() {
            return "Số người có kết quả xét nghiệm HIV dương tính nhận kết quả xét nghiệm";
        }

        @Override
        public String getKey() {
            return "5";
        }
    },
    HTC_06 {
        @Override
        public String getLabel() {
            return "Số người có kết quả khẳng định HIV dương tính được chuyển gửi CSĐT thành công";
        }

        @Override
        public String getKey() {
            return "6";
        }
    }
}
