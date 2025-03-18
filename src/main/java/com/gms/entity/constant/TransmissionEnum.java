package com.gms.entity.constant;

/**
 *
 * @author pdThang
 */
public enum TransmissionEnum implements BaseParameterEnum<String> {

    LQDM {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Mã 1 - Lây qua đường máu";
        }

    },
    LQDTD {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Mã 2 - Lây qua đường tình dục";
        }

    },
    MTSC {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Mã 3 - Mẹ truyền sang con";
        }

    },
    NTMT {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Mã 1.1 - Lây qua đường tiêm chích ma túy";
        }

    },
    TM {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Mã 1.2 - Truyền máu";
        }

    },
    KR {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Mã 4 - Không rõ";
        }

    },
    TNNG {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Mã 1.3 - Tai nạn nghề nghiệp";
        }

    },
    TĐG {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Mã 2.1 - Tình dục đồng giới";
        }

    },
    TDKG {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "Mã 2.2 - Tình dục khác giới";
        }

    },
}
