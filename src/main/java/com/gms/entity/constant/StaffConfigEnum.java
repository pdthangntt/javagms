package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum StaffConfigEnum implements BaseParameterEnum<String> {
    LAYTEST_STAFF_CODE {
        @Override
        public String getKey() {
            return "u_l_code";
        }

        @Override
        public String getLabel() {
            return "Mã của cán bộ xét nghiệm tại cộng đồng";
        }
    },
    HTC_STAFF_CODE {
        @Override
        public String getKey() {
            return "h_l_code";
        }

        @Override
        public String getLabel() {
            return "Mã chi nhánh (Khi khác với mã của cơ sở)";
        }
    },
    LAYTEST_SITE_CONFIRM {
        @Override
        public String getKey() {
            return "u_l_siteconfirm";
        }

        @Override
        public String getLabel() {
            return "Nơi gửi mẫu của cán bộ xét nghiệm tại cộng đồng";
        }
    },
    LAYTEST_OBJECT_GROUP {
        @Override
        public String getKey() {
            return "u_l_objectgroup";
        }

        @Override
        public String getLabel() {
            return "Nhóm đối tượng của cán bộ xét nghiệm tại cộng đồng";
        }
    },
    SITE_ADDTESS {
        @Override
        public String getKey() {
            return "s_addresss";
        }

        @Override
        public String getLabel() {
            return "Địa chỉ của cơ sở (Nếu khác với thông tin cơ sở quản lý/Chi nhánh khác)";
        }
    },
    SITE_PHONE {
        @Override
        public String getKey() {
            return "s_phone";
        }

        @Override
        public String getLabel() {
            return "Số điện thoại của cơ sở (Nếu khác với thông tin cơ sở quản lý/Chi nhánh khác)";
        }
    }
}
