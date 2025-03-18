package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public enum InsuranceTypeEnum implements BaseParameterEnum<String> {
    DN {
        @Override
        public String getKey() {
            return "1";
        }

        @Override
        public String getLabel() {
            return "DN: Người lao động làm việc trong các doanh nghiệp thành lập, hoạt động theo Luật Doanh nghiệp, Luật Đầu tư";
        }
    },
    HX {
        @Override
        public String getKey() {
            return "2";
        }

        @Override
        public String getLabel() {
            return "HX: Người lao động làm việc trong các hợp tác xã, liên hiệp hợp tác xã thành lập và hoạt động theo Luật Hợp tác xã";
        }
    },
    CH {
        @Override
        public String getKey() {
            return "3";
        }

        @Override
        public String getLabel() {
            return "CH: Người lao động làm việc trong các cơ quan nhà nước, đơn vị sự nghiệp, lực lượng vũ trang, tổ chức chính trị, tổ chức chính trị xã hội, tổ chức chính trị xã hội nghề nghiệp, tổ chức xã hội nghề nghiệp và tổ chức xã hội khác";
        }
    },
    NN {
        @Override
        public String getKey() {
            return "4";
        }

        @Override
        public String getLabel() {
            return "NN: Người lao động làm việc trong các cơ quan, tổ chức nước ngoài hoặc tổ chức quốc tế tại Việt Nam, trừ trường hợp Điều ước quốc tế mà nước Cộng hòa xã hội chủ nghĩa Việt Nam là thành viên tham gia có quy định khác.";
        }
    },
    TK {
        @Override
        public String getKey() {
            return "5";
        }

        @Override
        public String getLabel() {
            return "TK: Người lao động làm việc trong các tổ chức khác có sử dụng lao động được thành lập và hoạt động theo quy định của pháp luật.";
        }
    },
    HC {
        @Override
        public String getKey() {
            return "6";
        }

        @Override
        public String getLabel() {
            return "HC: Cán bộ, công chức, viên chức theo quy định của pháp luật về cán bộ, công chức, viên chức.";
        }
    },
    XK {
        @Override
        public String getKey() {
            return "7";
        }

        @Override
        public String getLabel() {
            return "XK: Người hoạt động không chuyên trách ở xã, phường, thị trấn theo quy định của pháp luật về cán bộ, công chức;";
        }
    },
    HT {
        @Override
        public String getKey() {
            return "8";
        }

        @Override
        public String getLabel() {
            return "HT: Người hưởng lương hưu, trợ cấp mất sức lao động hàng tháng;";
        }
    },
    TB {
        @Override
        public String getKey() {
            return "9";
        }

        @Override
        public String getLabel() {
            return "TB: Người đang hưởng trợ cấp bảo hiểm xã hội hàng tháng do bị tai nạn lao động, bệnh nghề nghiệp;";
        }
    },
    NO {
        @Override
        public String getKey() {
            return "10";
        }

        @Override
        public String getLabel() {
            return "NO: Người lao động nghỉ việc đang hưởng chế độ ốm đau theo quy định của pháp luật về bảo hiểm xã hội do mắc bệnh thuộc danh mục bệnh cần chữa trị dài ngày theo quy định của Bộ trưởng Bộ Y tế;";
        }
    },
    CT {
        @Override
        public String getKey() {
            return "11";
        }

        @Override
        public String getLabel() {
            return "CT: Người từ đủ 80 tuổi trở lên đang hưởng trợ cấp tuất hàng tháng;";
        }
    },
    XB {
        @Override
        public String getKey() {
            return "12";
        }

        @Override
        public String getLabel() {
            return "XB: Cán bộ xã, phường, thị trấn đã nghỉ việc đang hưởng trợ cấp bảo hiểm xã hội hàng tháng;";
        }
    },
    TN {
        @Override
        public String getKey() {
            return "13";
        }

        @Override
        public String getLabel() {
            return "TN: Người đang hưởng trợ cấp thất nghiệp theo quy định của pháp luật về bảo hiểm thất nghiệp;";
        }
    },
    CS {
        @Override
        public String getKey() {
            return "14";
        }

        @Override
        public String getLabel() {
            return "CS: Công nhân cao su nghỉ việc đang hưởng trợ cấp hàng tháng theo Quyết định số 206/CP ngày 30/5/1979 của Hội đồng Chính phủ (nay là Chính phủ) về chính sách đối với công nhân mới giải phóng làm nghề nặng nhọc, có hại sức khỏe nay già yếu phải thôi việc;";
        }
    },
    QN {
        @Override
        public String getKey() {
            return "15";
        }

        @Override
        public String getLabel() {
            return "QN: Sỹ quan, quân nhân chuyên nghiệp, hạ sỹ quan, binh sỹ Quân đội nhân dân Việt Nam đang tại ngũ; người làm công tác cơ yếu hưởng lương như đối với quân nhân đang công tác tại Ban Cơ yếu Chính phủ; học viên cơ yếu hưởng sinh hoạt phí từ ngân sách Nhà nước theo chế độ, chính sách như đối với học viên Quân đội;";
        }
    },
    CA {
        @Override
        public String getKey() {
            return "16";
        }

        @Override
        public String getLabel() {
            return "CA: Sỹ quan, hạ sỹ quan nghiệp vụ và sỹ quan, hạ sỹ quan chuyên môn kỹ thuật, hạ sỹ quan, chiến sỹ nghĩa vụ đang công tác trong lực lượng công an nhân dân, học viên công an nhân dân hưởng sinh hoạt phí từ ngân sách Nhà nước;";
        }
    },
    CY {
        @Override
        public String getKey() {
            return "17";
        }

        @Override
        public String getLabel() {
            return "CY: Người làm công tác cơ yếu hưởng lương như đối với quân nhân đang công tác tại các tổ chức cơ yếu thuộc các Bộ, ngành, địa phương;";
        }
    },
    XN {
        @Override
        public String getKey() {
            return "18";
        }

        @Override
        public String getLabel() {
            return "XN: Cán bộ xã, phường, thị trấn đã nghỉ việc đang hưởng trợ cấp hàng tháng từ ngân sách Nhà nước;";
        }
    },
    MS {
        @Override
        public String getKey() {
            return "19";
        }

        @Override
        public String getLabel() {
            return "MS: Người đã thôi hưởng trợ cấp mất sức lao động đang hưởng trợ cấp hàng tháng từ ngân sách Nhà nước;";
        }
    },
    CC {
        @Override
        public String getKey() {
            return "20";
        }

        @Override
        public String getLabel() {
            return "CC: Người có công với cách mạng, bao gồm: Người hoạt động cách mạng trước ngày 01/01/1945; người hoạt động cách mạng từ ngày 01/01/1945 đến ngày khởi nghĩa tháng 8/1945; Bà mẹ Việt Nam anh hùng; thương binh, người hưởng chính sách như thương binh, thương binh loại B, bệnh binh suy giảm khả năng lao động từ 81% trở lên;";
        }
    },
    CK {
        @Override
        public String getKey() {
            return "21";
        }

        @Override
        public String getLabel() {
            return "CK: Người có công với cách mạng theo quy định pháp luật về người có công với cách mạng, trừ các đối tượng được cấp mã CC;";
        }
    },
    CB {
        @Override
        public String getKey() {
            return "22";
        }

        @Override
        public String getLabel() {
            return "CB: Cựu chiến binh theo quy định pháp luật về cựu chiến binh";
        }
    },
    KC {
        @Override
        public String getKey() {
            return "23";
        }

        @Override
        public String getLabel() {
            return "KC: Người trực tiếp tham gia kháng chiến chống Mỹ cứu nước; người tham gia chiến tranh bảo vệ Tổ quốc, làm nhiệm vụ quốc tế ở Căm-pu-chia, giúp bạn Lào sau ngày 30/4/1975; thanh niên xung phong thời kỳ kháng chiến chống Pháp và thanh niên xung phong đã hoàn thành nhiệm vụ trong kháng chiến; dân công hỏa tuyến tham gia kháng chiến chống Pháp, chống Mỹ, chiến tranh bảo vệ Tổ quốc và làm nhiệm vụ quốc tế theo quy định tại các Quyết định của Thủ tướng Chính phủ, trừ các đối tượng được cấp mã CC, CK và CB;";
        }
    },
    HD {
        @Override
        public String getKey() {
            return "24";
        }

        @Override
        public String getLabel() {
            return "HD: Đại biểu Quốc hội, đại biểu Hội đồng nhân dân các cấp đương nhiệm;";
        }
    },
    TE {
        @Override
        public String getKey() {
            return "25";
        }

        @Override
        public String getLabel() {
            return "TE: Trẻ em dưới 6 tuổi, kể cả trẻ đủ 72 tháng tuổi mà trong năm đó chưa đến kỳ nhập học;";
        }
    },
    BT {
        @Override
        public String getKey() {
            return "26";
        }

        @Override
        public String getLabel() {
            return "BT: Người thuộc diện hưởng trợ cấp bảo trợ xã hội hàng tháng theo quy định của pháp luật;";
        }
    },
    HN {
        @Override
        public String getKey() {
            return "27";
        }

        @Override
        public String getLabel() {
            return "HN: Người thuộc hộ gia đình nghèo;";
        }
    },
    DT {
        @Override
        public String getKey() {
            return "28";
        }

        @Override
        public String getLabel() {
            return "DT: Người dân tộc thiểu số đang sinh sống tại vùng có điều kiện kinh tế xã hội khó khăn, đặc biệt khó khăn;";
        }
    },
    DK {
        @Override
        public String getKey() {
            return "29";
        }

        @Override
        public String getLabel() {
            return "DK: Người đang sinh sống tại vùng có điều kiện kinh tế xã hội đặc biệt khó khăn";
        }
    },
    XD {
        @Override
        public String getKey() {
            return "30";
        }

        @Override
        public String getLabel() {
            return "XD: Người đang sinh sống tại xã đảo, huyện đảo";
        }
    },
    TS {
        @Override
        public String getKey() {
            return "31";
        }

        @Override
        public String getLabel() {
            return "TS: Thân nhân của người có công với cách mạng là cha đẻ, mẹ đẻ, vợ hoặc chồng, con của liệt sỹ; người có công nuôi dưỡng liệt sỹ";
        }
    },
    TC {
        @Override
        public String getKey() {
            return "32";
        }

        @Override
        public String getLabel() {
            return "TC: Thân nhân của người có công với cách mạng, bao gồm: cha đẻ, mẹ đẻ, vợ hoặc chồng, con từ trên 6 tuổi đến dưới 18 tuổi hoặc từ đủ 18 tuổi trở lên nếu còn tiếp tục đi học hoặc bị khuyết tật nặng, khuyết tật đặc biệt nặng của các đối tượng: Người hoạt động cách mạng trước ngày 01/01/1945; người hoạt động cách mạng từ ngày 01/01/1945 đến ngày khởi nghĩa tháng Tám năm 1945; Anh hùng Lực lượng vũ trang nhân dân, Anh hùng Lao động trong thời kỳ kháng chiến; thương binh, bệnh binh suy giảm khả năng lao động từ 61% trở lên; người hoạt động kháng chiến bị nhiễm chất độc hóa học suy giảm khả năng lao động từ 61% trở lên; con đẻ từ trên 6 tuổi của người hoạt động kháng chiến bị nhiễm chất độc hóa học bị dị dạng, dị tật do hậu quả của chất độc hóa học không tự lực được trong sinh hoạt hoặc suy giảm khả năng tự lực trong sinh hoạt, trừ các đối tượng được cấp mã TS";
        }
    },
    TQ {
        @Override
        public String getKey() {
            return "33";
        }

        @Override
        public String getLabel() {
            return "TQ: Thân nhân của đối tượng được cấp mã QN";
        }
    },
    TA {
        @Override
        public String getKey() {
            return "34";
        }

        @Override
        public String getLabel() {
            return "TA: Thân nhân của đối tượng được cấp mã CA";
        }
    },
    TY {
        @Override
        public String getKey() {
            return "35";
        }

        @Override
        public String getLabel() {
            return "TY: Thân nhân của đối tượng được cấp mã CY";
        }
    },
    HG {
        @Override
        public String getKey() {
            return "36";
        }

        @Override
        public String getLabel() {
            return "HG: Người đã hiến bộ phận cơ thể người theo quy định của pháp luật";
        }
    },
    LS {
        @Override
        public String getKey() {
            return "37";
        }

        @Override
        public String getLabel() {
            return "LS: Người nước ngoài đang học tập tại Việt Nam được cấp học bổng từ ngân sách của Nhà nước Việt Nam";
        }
    },
    PV {
        @Override
        public String getKey() {
            return "38";
        }

        @Override
        public String getLabel() {
            return "PV: Người phục vụ người có công với cách mạng, bao gồm: người phục vụ Bà mẹ Việt Nam anh hùng sống ở gia đình; người phục vụ thương binh, bệnh binh suy giảm khả năng lao động từ 81% trở lên ở gia đình; người phục vụ người hoạt động kháng chiến bị nhiễm chất độc hóa học suy giảm khả năng lao động từ 81% trở lên sống ở gia đình";
        }
    },
    CN {
        @Override
        public String getKey() {
            return "39";
        }

        @Override
        public String getLabel() {
            return "CN: Người thuộc hộ gia đình cận nghèo";
        }
    },
    HS {
        @Override
        public String getKey() {
            return "40";
        }

        @Override
        public String getLabel() {
            return "HS: Học sinh đang theo học tại các cơ sở giáo dục và đào tạo thuộc hệ thống giáo dục quốc dân";
        }
    },
    SV {
        @Override
        public String getKey() {
            return "41";
        }

        @Override
        public String getLabel() {
            return "SV: Sinh viên đang theo học tại các cơ sở giáo dục và đào tạo, cơ sở dạy nghề thuộc hệ thống giáo dục quốc dân";
        }
    },
    GB {
        @Override
        public String getKey() {
            return "42";
        }

        @Override
        public String getLabel() {
            return "GB: Người thuộc hộ gia đình làm nông nghiệp, lâm nghiệp, ngư nghiệp và diêm nghiệp có mức sống trung bình theo quy định của pháp luật";
        }
    },
    GD {
        @Override
        public String getKey() {
            return "43";
        }

        @Override
        public String getLabel() {
            return "GD: Người tham gia BHYT theo hộ gia đình gồm những người thuộc hộ gia đình";
        }
    }
}
