package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum NTCHSymtomEnum implements BaseParameterEnum<String> {
    NAMHONG {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Nấm họng (tưa miệng)";
        }
    },
    LAO {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Lao";
        }
    },
    MARNEFFEI {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Bệnh do nấm Penicillium marneffei";
        }
    },
    NEOFORMANS {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Viêm màng não do Cryptococcus neoformans";
        }
    },
    PCP {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "PCP";
        }
    },
    TOXOPLASMA {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Toxoplasma não";
        }
    },
    CYTOMEGALOVIRUS {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Viêm võng mạc do Cytomegalovirus (CMV)";
        }
    },
    AVIUM {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Phức hợp Mycobacterium avium (MAC)";
        }
    },
    CRYPTOSPORIDIUM {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "Bệnh do Cryptosporidium";
        }
    },
    CYCLOSPORA {
        @Override
        public String getKey() {
            return "10";
        }

        @Override
        public String getLabel() {
            return "Bệnh do Isospora và Cyclospora";
        }
    },
    OTHER {
        @Override
        public String getKey() {
            return "11";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    }
}
