/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.constant;

/**
 *
 * @author Admin
 */
public enum LaoDiagnoseEnum implements BaseParameterEnum<String> {
    LAO_PHOI {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Lao phổi";
        }
    },
    LAO_HACH {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Lao hạch";
        }
    },
    LAO_MANG_NAO {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "Lao màng lão";
        }
    },
    LAO_COT_SONG {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "Lao cột sống";
        }
    },
    LAO_TIET_NIEU {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "Lao tiết niệu - sinh dục";
        }
    },
    LAO_KE {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "Lao kê";
        }
    },
    LAO_MANG_PHOI {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "Tràn dịch màng phổi do lao";
        }
    },
    LAO_MANG_TIM {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "Tràn dịch màng tim do lao";
        }
    },
    LAO_MANG_BUNG {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "Tràn dịch màng bụng do lao";
        }
    },
    KHAC {
        @Override
        public String getKey() {
            return "-1";
        }

        @Override
        public String getLabel() {
            return "Khác";
        }
    },
}
