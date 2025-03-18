package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum PqmIndicatorEnum implements BaseParameterEnum<String> {
    HTS_TST_POS {
        @Override
        public String getKey() {
            return "HTS_TST_POS";
        }

        @Override
        public String getLabel() {
            return "Số ca dương tính";
        }
    },
    HTS_TST_Recency {
        @Override
        public String getKey() {
            return "HTS_TST_RECENCY";
        }

        @Override
        public String getLabel() {
            return "Số ca có kết luận nhiễm mới";
        }
    },
    POS_TO_ART {
        @Override
        public String getKey() {
            return "POS_TO_ART";
        }

        @Override
        public String getLabel() {
            return "Số ca chuyển gửi điều trị thành công";
        }
    },
    TX_New {
        @Override
        public String getKey() {
            return "TX_NEW";
        }

        @Override
        public String getLabel() {
            return "Số ca mới điều trị";
        }
    },
    TX_CURR {
        @Override
        public String getKey() {
            return "TX_CURR";
        }

        @Override
        public String getLabel() {
            return "Số ca đang điều trị";
        }
    },
    MMD {
        @Override
        public String getKey() {
            return "MMD";
        }

        @Override
        public String getLabel() {
            return "Số ca được cấp thuốc nhiều tháng";
        }
    },
    IIT {
        @Override
        public String getKey() {
            return "TX_ML";
        }

        @Override
        public String getLabel() {
            return "Số ca gián đoạn điều trị";
        }
    },
    VL_detectable {
        @Override
        public String getKey() {
            return "VL_UNSUPRESSED";
        }

        @Override
        public String getLabel() {
            return "Số ca có TLVR từ ngưỡng phát hiện";
        }
    },
    TB_PREV {
        @Override
        public String getKey() {
            return "TB_PREV";
        }

        @Override
        public String getLabel() {
            return "Số ca hoàn thành điều trị dự phòng Lao";
        }
    },
    PrEP_New {
        @Override
        public String getKey() {
            return "PREP_NEW";
        }

        @Override
        public String getLabel() {
            return "Số ca mới điều trị PrEP lần đầu";
        }
    },
    PrEP_CURR {
        @Override
        public String getKey() {
            return "PREP_CURR";
        }

        @Override
        public String getLabel() {
            return "Số ca đang điều trị PrEP";
        }
    },
    PrEP_3M {
        @Override
        public String getKey() {
            return "PREP_3M";
        }

        @Override
        public String getLabel() {
            return "Số ca sử dụng PrEP liên tục từ 3 tháng trở lên";
        }
    },
    PrEP_Over_90 {
        @Override
        public String getKey() {
            return "PrEP_Over_90";
        }

        @Override
        public String getLabel() {
            return "Số ca điều trị PrEP trên 90 ngày";
        }
    },
    ////////////////////////////// CHỈ SỐ CŨ< KO CÓ TRONG PQM REPORT LÀM 16.02.2020

    HTS_TST_RECENCY {
        @Override
        public String getKey() {
            return "HTS_TST_RECENCY";
        }

        @Override
        public String getLabel() {
            return "Kết luận Nhiễm mới";
        }
    },
    IMS_TST_POS {
        @Override
        public String getKey() {
            return "IMS_TST_POS";
        }

        @Override
        public String getLabel() {
            return "Dương tính nhận kết quả";
        }
    },
    IMS_POS_ART {
        @Override
        public String getKey() {
            return "IMS_POS_ART";
        }

        @Override
        public String getLabel() {
            return "Dương tính chuyển gửi điều trị";
        }
    },
    IMS_TST_RECENCY {
        @Override
        public String getKey() {
            return "IMS_TST_RECENCY";
        }

        @Override
        public String getLabel() {
            return "Kết quả xét nghiệm ban đầu là nhiễm mới";
        }
    }

}
