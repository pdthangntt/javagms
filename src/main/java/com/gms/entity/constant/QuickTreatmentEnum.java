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
public enum QuickTreatmentEnum implements BaseParameterEnum<String> {
    TRONG_NGAY {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Trong ngày";
        }
    },
    MOT_NGAY {

        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "1 ngày";
        }
    },
    HAI_NGAY {

        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "2 ngày";
        }
    },
    BA_NGAY {

        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "3 ngày";
        }
    },
    BON_DEN_BAY {

        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "4 - 7 ngày";
        }
    }
}
