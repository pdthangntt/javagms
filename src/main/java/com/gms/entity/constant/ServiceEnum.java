package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum ServiceEnum implements BaseParameterEnum<String> {
    NONE {
        @Override
        public String getKey() {
            return "";
        }

        @Override
        public String getLabel() {
            return "Hệ thống/Đồng bộ";
        }
    },
    HTC {
        @Override
        public String getKey() {
            return "100";
        }

        @Override
        public String getLabel() {
            return "Tư vấn & xét nghiệm";
        }
    },
    OPC {
        @Override
        public String getKey() {
            return "103";
        }

        @Override
        public String getLabel() {
            return "Điều trị ARV";
        }
    },
    OPC_MANAGEMENT {
        @Override
        public String getKey() {
            return "mopc";
        }

        @Override
        public String getLabel() {
            return "Khoa điều trị";
        }
    },
    HTC_CONFIRM {
        @Override
        public String getKey() {
            return "500";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm khẳng định";
        }

    },
    LAYTEST {
        @Override
        public String getKey() {
            return "105";
        }

        @Override
        public String getLabel() {
            return "Xét nghiệm tại cộng đồng";
        }

    },
    PMTCT {
        @Override
        public String getKey() {
            return "102";
        }

        @Override
        public String getLabel() {
            return "Dự phòng LTMC";
        }

    },
    PREP {
        @Override
        public String getKey() {
            return "prep";
        }

        @Override
        public String getLabel() {
            return "Điều trị PrEP";
        }

    },
    PAC {
        @Override
        public String getKey() {
            return "ttpc";
        }

        @Override
        public String getLabel() {
            return "TTPC/PAC tỉnh";
        }

    },
    TYT {
        @Override
        public String getKey() {
            return "tyt";
        }

        @Override
        public String getLabel() {
            return "Trạm Y tế";
        }

    },
    TTYT {
        @Override
        public String getKey() {
            return "ttyt";
        }

        @Override
        public String getLabel() {
            return "Trung tâm Y tế";
        }

    },
    VAAC {
        @Override
        public String getKey() {
            return "vaac";
        }

        @Override
        public String getLabel() {
            return "Cục Vaac";
        }

    }
}
