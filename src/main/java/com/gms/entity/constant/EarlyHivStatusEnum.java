/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.entity.constant;

/**
 *
 * @author TheHoa
 */
public enum EarlyHivStatusEnum implements BaseParameterEnum<String>{
    
    NO_XN {
        @Override
        public String getKey() {
            return "0";
        }

        @Override
        public String getLabel() {
            return "Không xét nghiệm";
        }

    },
    YES_XN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "Có xét nghiệm";
        }
    }
}
