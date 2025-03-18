package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum SiteConfigEnum implements BaseParameterEnum<String> {
    HTC_RETURN_DAY {
        @Override
        public String getKey() {
            return "htc_return_day";
        }

        @Override
        public String getLabel() {
            return "Số ngày hẹn trả kết quả";
        }

    },
    HTC_RQ_ID {
        @Override
        public String getKey() {
            return "htc_qr_id";
        }

        @Override
        public String getLabel() {
            return "Bắt buộc nhập CMND khi có phản ứng";
        }

    },
    ALERT_SITE_EMAIL {
        @Override
        public String getKey() {
            return "site_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo của cơ sở";
        }

    },
    ALERT_OPC_EMAIL {
        @Override
        public String getKey() {
            return "opc_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo điều trị ngoại trú";
        }

    },
    ALERT_OPC_MANAGER_EMAIL {
        @Override
        public String getKey() {
            return "opcm_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo khoa điều trị";
        }

    },
    ALERT_HTC_EMAIL {
        @Override
        public String getKey() {
            return "htc_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo Tư vấn & Xét nghiệm";
        }
    },
    ALERT_GSPH_EMAIL {
        @Override
        public String getKey() {
            return "gspt_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo giám sát phát hiện";
        }
    },
    ALERT_CONFIRM_EMAIL {
        @Override
        public String getKey() {
            return "confirm_email";
        }

        @Override
        public String getLabel() {
            return "Email nhận thông báo khẳng định";
        }
    },
    SHOW_FUNCTION {
        @Override
        public String getKey() {
            return "s_function";
        }

        @Override
        public String getLabel() {
            return "Hiển thị cột chức năng";
        }
    },
    BIOLOGY_PRODUCT_TEST {
        @Override
        public String getKey() {
            return "s_biology";
        }

        @Override
        public String getLabel() {
            return "Sinh phẩm xét nghiệm";
        }
    },
    PAC_FROM_HTC {
        @Override
        public String getKey() {
            return "pac_fromhtc";
        }

        @Override
        public String getLabel() {
            return "Cho phép TVXN chuyển gửi PAC/CDC";
        }
    },
    PAC_FROM_CONFIRM {
        @Override
        public String getKey() {
            return "pac_fromconfirm";
        }

        @Override
        public String getLabel() {
            return "Cho phép khẳng định chuyển gửi PAC/CDC";
        }
    },
    PAC_PDF_KINHGUI {
        @Override
        public String getKey() {
            return "pac_pdfkg";
        }

        @Override
        public String getLabel() {
            return "Hiển thị <b>kính gửi</b> DS phát hiện/tử vong";
        }
    },
    PAC_PDF_SO_NGUOI_NHIEM {
        @Override
        public String getKey() {
            return "pac_pdfline";
        }

        @Override
        public String getLabel() {
            return "Hiển thị dòng <b>Số người nhiễm cộng dồn ...</b> DS phát hiện/tử vong";
        }
    },
    PAC_PDF_CK2 {
        @Override
        public String getKey() {
            return "pac_pdfck2";
        }

        @Override
        public String getLabel() {
            return "Chữ ký khác DS phát hiện/tử vong";
        }
    },
    VISIT_PRINT_ANSWER_RESULT {
        @Override
        public String getLabel() {
            return "Phần ký mẫu phiếu in <b>Trả kết quả xét nghiệm</b>";
        }

        @Override
        public String getKey() {
            return "visit_answer_result";
        }
    },
    CONFIRM_PRINT_ANSWER_RESULT {
        @Override
        public String getLabel() {
            return "Phần ký mẫu phiếu in Trả kết quả xét nghiệm";
        }

        @Override
        public String getKey() {
            return "confirm_answer_result";
        }
    },
    CONFIRM_ANSWER_RESULT {
        @Override
        public String getLabel() {
            return "Mẫu phiếu in trả kết quả xét nghiệm";
        }

        @Override
        public String getKey() {
            return "c_answer_result";
        }
    },
    OPC_QR {
        @Override
        public String getLabel() {
            return "In mã QR trên phiếu <b>Chuyển gửi điều trị</b>";
        }

        @Override
        public String getKey() {
            return "opc_as_qr";
        }
    },
    OPC_QR_LIST {
        @Override
        public String getLabel() {
            return "In phiếu QR";
        }

        @Override
        public String getKey() {
            return "opc_asl_qr";
        }
    },
    CONFIRM_QR {
        @Override
        public String getLabel() {
            return "In mã QR trên phiếu <b>Trả kết quả xét nghiệm</b>";
        }

        @Override
        public String getKey() {
            return "confirm_as_qr";
        }
    },
    VISIT_QR {
        @Override
        public String getLabel() {
            return "In mã QR trên phiếu <b>Chuyển gửi điều trị</b>";
        }

        @Override
        public String getKey() {
            return "visit_as_qr";
        }
    },
    HTC_PRINT_ANSWER_RESULT {
        @Override
        public String getLabel() {
            return "Phần chữ ký mẫu phiếu in <b>Trả kết quả xét nghiệm</b>";
        }

        @Override
        public String getKey() {
            return "htc_answer_result";
        }
    },
    HTC_CSCQ_ANSWER_RESULT {
        @Override
        public String getLabel() {
            return "Tên đơn vị <b>Trả kết quả xét nghiệm</b>";
        }

        @Override
        public String getKey() {
            return "htc_cscq_answer";
        }
    },
    HTC_BIOLOGY_PRODUCT {
        @Override
        public String getKey() {
            return "h_s_biology";
        }

        @Override
        public String getLabel() {
            return "Sinh phẩm xét nghiệm";
        }
    },
    LAYTEST_BIOLOGY_PRODUCT {
        @Override
        public String getKey() {
            return "l_s_biology";
        }

        @Override
        public String getLabel() {
            return "Sinh phẩm xét nghiệm";
        }
    },
    HTC_TICKET_CODE {
        @Override
        public String getKey() {
            return "h_ticket_code";
        }

        @Override
        public String getLabel() {
            return "Hiển thị mã khách hàng tại <b>phiếu đồng ý xét nghiệm</b> theo mẫu quốc gia";
        }
    },
    HTC_TICKET_CSCQ {
        @Override
        public String getKey() {
            return "h_ticket_cscq";
        }

        @Override
        public String getLabel() {
            return "Hiển thị cơ sở chủ quản <b>phiếu đồng ý xét nghiệm</b> theo mẫu quốc gia";
        }
    },
    HTC_PHIEU_XN {
        @Override
        public String getKey() {
            return "htc_phieu_xn";
        }

        @Override
        public String getLabel() {
            return "Mẫu <b>phiếu đồng ý xét nghiệm</b>";
        }
    },
    OPC_REGIMEN {
        @Override
        public String getKey() {
            return "opc_regimen";
        }

        @Override
        public String getLabel() {
            return "<b>Phác đồ điều trị</b> hay sử dụng";
        }
    },
}
