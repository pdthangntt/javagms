package com.gms.entity.constant;

/**
 *
 * @author TrangBN
 */
public enum RemoveEnum implements BaseParameterEnum<Boolean> {
    
    TRUE {
        @Override
        public Boolean getKey() {
            return true;
        }

        @Override
        public String getLabel() {
            return "Đã xóa";
        }
    },
    
    FALSE {
        @Override
        public Boolean getKey() {
            return false;
        }

        @Override
        public String getLabel() {
            return "Chưa xóa";
        }
    },
}
