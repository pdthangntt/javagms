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
public enum SuspiciousSymptomsEnum implements BaseParameterEnum<String>  {
    CO_TRIEU_CHUNG {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Có triệu chứng";
        }
    },
    KHONG_TRIEU_CHUNG {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Không có triệu chứng";
        }
    }
}
