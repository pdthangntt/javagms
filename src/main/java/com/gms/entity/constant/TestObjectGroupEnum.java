package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum TestObjectGroupEnum implements BaseParameterEnum<String> {
    PREGNANT_WOMEN {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Phụ nữ mang thai";
        }

    },
    NGUOI_MUA_DAM {
        @Override
        public String getKey() {
            return "17";
        }

        @Override
        public String getLabel() {
            return "Người mua dâm";
        }

    },
    NGUOI_CHUYEN_GIOI {
        @Override
        public String getKey() {
            return "13";
        }

        @Override
        public String getLabel() {
            return "Người chuyển giới";
        }

    },
    PREGNANT_PERIOD {
        @Override
        public String getKey() {
            return "5.1";
        }

        @Override
        public String getLabel() {
            return "5.1. Phụ nữ mang thai (Thời kỳ mang thai)";
        }

    },
    GIVING_BIRTH {
        @Override
        public String getKey() {
            return "5.2";
        }

        @Override
        public String getLabel() {
            return "5.2. Phụ nữ mang thai (Giai đoạn chuyển dạ, đẻ)";
        }
    },
    TAN_BINH {
        @Override
        public String getKey() {
            return "21";
        }

        @Override
        public String getLabel() {
            return "Tân binh";
        }
    },
    NGHIEN_TRICH_MA_TUY {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Nghiện chích ma túy";
        }
    },
    PHU_NU_BAN_DAM {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Người hành nghề mại dâm";
        }
    },
    MSM {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Nam quan hệ tình dục với nam";
        }
    },
    VO_CHONG_BANTINH_NGUOI_NHIEM_HIV {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Vợ/chồng/bạn tình của người nhiễm HIV";
        }
    },
    VO_CHONG_BANTINH_NGUOI_NGUY_CO_CAO {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Vợ/chồng/bạn tình của người nguy cơ cao";
        }
    },
    LAO {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Bệnh nhân lao";
        }
    },
    BENH_NHAN_MAC_CAC_NHIEM_TRUNG_LTQDTD {
        @Override
        public String getKey() {
            return "11";
        }

        @Override
        public String getLabel() {
            return "Bệnh nhân mắc các nhiễm trùng LTQĐTD";
        }
    },
    THANH_NIEM_KHAM_NGHIA_VU_QUAN_SU {
        @Override
        public String getKey() {
            return "12";
        }

        @Override
        public String getLabel() {
            return "Thanh niên khám tuyển nghĩa vụ quân sự";
        }
    },
    NGUOI_BAN_MAU_HIENMAU_CHOMAU {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "Người bán máu / hiến máu / cho máu";
        }
    },
    ME_TRUYEN_CHO_CON {
        @Override
        public String getKey() {
            return "26";
        }

        @Override
        public String getLabel() {
            return "Mẹ truyền cho con";
        }
    },
    KHAC {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Các đối tượng khác";
        }
    },
    NGHI_NGO_AIDS {
        @Override
        public String getKey() {
            return "10";
        }

        @Override
        public String getLabel() {
            return "Bệnh nhân nghi ngờ AIDS";
        }
    },
    PHAM_NHAN {
        @Override
        public String getKey() {
            return "19";
        }

        @Override
        public String getLabel() {
            return "Phạm nhân";
        }
    },
    KHONG_RO {
        @Override
        public String getKey() {
            return "100";
        }

        @Override
        public String getLabel() {
            return "Không rõ";
        }
    }
}
