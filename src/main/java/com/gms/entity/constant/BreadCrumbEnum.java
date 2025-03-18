package com.gms.entity.constant;

/**
 *
 * @author NamAnh_HaUI
 */
public enum BreadCrumbEnum implements BaseParameterEnum<String>{

    HTC_TITLE {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Tư vấn & xét nghiệm";
        }
    },
    
    OPC_ARV {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "Điều trị ngoại trú";
        }
    }
    
}
