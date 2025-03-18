package com.gms.entity.form;

import com.gms.entity.db.*;
import java.io.Serializable;

/**
 *
 * @author vvthanh
 */
public class PqmHtcElogImportForm extends BaseEntity implements Serializable {

    private String no; //TT
    private String siteCode; //A3. Mã cơ sở 
    private String code; //A4. Số thứ tự khách hàng
    private String service; //A2. Loại dịch vụ
    private String targer;//A2.1 Mục đích khách hàng đến cơ sở cố định// Biến tạm

    private String laytestStaff; //A3.1 Nhân viên dịch vụ không chuyên
    private String patientName; //A5. Tên khách hàng
    private String patientPhone; //A6. Số điện thoại
    private String patientID; //A7. Số chứng minh
    private String raceID; //A8. Dân tộc
    private String genderID; //A9. Giới tính
    private String yearOfBirth; //A10. Năm sinh
    private String permanentAddress; //A11.4  Địa chỉ thường trú
    private String permanentWardID; //A11.3 Phường /xã
    private String permanentDistrictID; //A11.2 Quận /Huyện
    private String permanentProvinceID; //A11.1 Tỉnh/Thành phố

    private String currentAddress; //A12.4. Địa chỉ tạm trú
    private String currentWardID; //A12.3 Phường / Xã
    private String currentDistrictID; //A12.2 Quận / Huyện
    private String currentProvinceID; //A12.1 Tỉnh / Thành phố

    private String jobID; //A13. Nghề nghiệp
    private String objectGroupID_ncmt; //A14.1 Nghiện chích ma túy (NCMT)
    private String objectGroupID_pnbd; //A14.2 Người bán dâm (PNBD)
    private String objectGroupID_pnmangthai; //A14.3 Phụ nữ mang thai
    private String objectGroupID_nguoihienmau; //A14.4 Người hiến máu
    private String objectGroupID_lao; //A14.5 Bệnh nhân lao
    private String objectGroupID_laytruyenquatd; //A14.6 Người mắc nhiễm trùng lây truyền qua đường TD
    private String objectGroupID_nghiavuquansu; //A14.7 Thanh niên khám tuyển nghĩa vụ quân sự
    private String objectGroupID_msm; //A14.8 Nam quan hệ tình dục với nam (MSM)
    private String objectGroupID_chuyengioi; //A14.9 Người chuyển giới
    private String objectGroupID_vcbantinhnguoinhiem; //A14.10 Vợ/chồng/ban tình của người nhiễm HIV
    private String objectGroupID_vcbantinhnguoincc; //A14.11 Vợ/chồng/ban tình của người NCC
    private String objectGroupID_benhnhannghingoaids; //A14.12Bệnh nhân nghi ngờ AIDS
    private String objectGroupID_other; //A14.13 Các đối tượng khác
    private String riskBehaviorID_tiemtrich; //A15.1 Tiêm chích ma túy
    private String riskBehaviorID_quanhetinhducnguoimuabandam; //A15.2 Quan hệ tình dục với người mua/bán dâm
    private String riskBehaviorID_msm; //A15.3 Quan hệ tình dục đồng giới nam
    private String riskBehaviorID_quanhenhieunguoi; //A15.4 Quan hệ với nhiều người (ko vì tiềm & ma túy)
    private String riskBehaviorID_other; //A15.5 Nguy cơ khác của bản thân
    private String referralSource_tiepcancongdong; //A17.1 Tiếp cận cộng đồng
    private String referralSource_xetnghiemtheodau; //A17.2 Kênh xét nghiệm theo dấu bạn tình/bạn chích 
    private String referralSource_canboyte; //A17.3 Cán Bộ y tế
    private String referralSource_mangxahoi; //A17.4 Mạng xã hội
    private String referralSource_other; //A17.5 Khác

    //Biến tạm
    private String causer;// Nguyên nhân khách hàng biết đến dịch vụ(Cũ, ko dùng nữa)

    private String approacherNo;  //A17.1. Mã số của tiếp cận cộng đồng:
    private String youInjectCode;  //A17.2 Mã số theo dấu bạn tình/bạn chích
    private String preTestTime;  // A1. Ngày lấy máu xét nghiệm sàng lọc
    private String isAgreePreTest;  // B. Xét nghiệm sàng lọc
    private String testResultsID;  // B1.Kết quả xét nghiệm sàng lọc

    //Biến tạm
    private String result;//// B1.1 Chọn khi kết quả XN là có phản ứng 
    private String exechangeService; //othoa them cot C7 - dich vu tu van chuyen gui
    private String resultsTime; //(B1.2+C7) . Ngày khách hàng nhận kết quả xét nghiệm 
    private String isAgreeTest; //B2. Khách hàng đồng ý tiếp tục XN tại cơ sở y tế  
    private String siteVisit; //B2.1. Tên cơ sở  XN sàng lọc
    private String siteConfirmTest; //C1. Tên cơ sở xét nghiệm khẳng định HIV
    private String confirmTestNo; //C2. Mã xét nghiệm khẳng định do cơ sở TVXN cố định cấp

    //Biến tạm
    private String c2;//////C2. 1. Loại hình xét nghiệm khẳng định

    private String confirmResultsID;//C3. Kết quả xét nghiệm khẳng định
    private String earlyHiv;//C3a. Kết quả xét nghiệm nhanh nhiễm mới
    private String modeOfTransmission; //C4a. Đường lây truyền HIV
    //Biến tạm
    private String c4a;////C4b. Loại xét nghiệm PCR HIV bổ sung
    //Biến tạm
    private String c4b;////C4b. Kết quả xét nghiệm PCR HIV :
    private String confirmTime; //C5.  Ngày xét nghiệm khẳng định
    private String resultsSiteTime; //C6. Ngày cơ sở nhận kết quả xét nghiệm khẳng định
    private String virusLoad; //C8. Kết quả xét nghiệm tải lượng virus
    //Biến tạm
    private String c8b;////C8b. Kết quả xét nghiệm tải lượng virus
    private String arv_hiv;//Chăm sóc điều trị HIV/AIDS
    private String arv_lnqdtd;//Điều trị các bệnh LNQĐTD
    private String arv_lao;//Điều trị Lao
    private String arv_pmtct;// 
    private String arv_khhgd;//KHHGĐ
    private String arv_chamsocytekhac;//Chăm sóc y tế khác
    private String arv_tiepcancongdong;//Tiếp cận cộng đồng
    private String arv_nhomhotro;//Nhóm hỗ trợ
    private String arv_cainghiencongdong;//Cai nghiện cộng đồng
    private String arv_tuvangiamnguyco;//Tư vấn giảm nguy cơ bổ sung
    private String arv_laymauxnkhangdinhhiv;//Lấy máu xét nghiệm khẳng định HIV
    private String arv_other;//Khác
    private String khongchuyengui; //Không chuyển gửi
    private String note; //Ghi chú 
    private String exchangeConsultTime; //D1. Ngày thực hiện tư vấn chuyển gửi điều trị ARV:
    private String arvExchangeResult; //D1a. Kết quả tư vấn chuyển gửi điều trị ARV
    private String exchangeTime; //D1.1 Ngày chuyển gửi điều trị ARV
    private String partnerProvideResult; //D1.2. Kết quả tư vấn cung cấp thông tin thực hiện xét nghiệm theo dấu bạn tình/bạn chích 
    private String arrivalSite; //D2. Tên cơ sở điều trị mà khách hàng được chuyển gửi tới
    //Biến tạm
    private String d2;////D2. Mã cơ sở điều trị mà khách hàng được chuyển gửi tới
    private String exchangeProvinceID; //D2.1 Tỉnh/Thành phố chuyển đến
    private String exchangeDistrictID; //D2.2 Huyện chuyển đến
    private String registerTherapyTime; //D3. Ngày đăng ký điều trị ARV
    private String registeredTherapySite; //D4. Tên cơ sở mà khách hành đã đăng ký điều trị
    //Biến tạm
    private String d4;////D4. Mã cơ sở mà khách hàng đã đăng ký điều trị
    private String therapyRegistProvinceID; //D4.1 Tỉnh/Thành phố  điều trị
    private String therapyRegistDistrictID; //D4.2 Huyện   điều trị
    private String therapyNo; //D5. Mã số điều trị HIV
    private String staffBeforeTestID; //Tư vấn viên 01
    private String staffAfterID; //Tư vấn viên 02
    //Biến tạm
    private String BookingID;////Booking ID
    //Biến tạm
    private String GUID;////GUID
    //Biến tạm
    private String UPDATEupdateTime;////UPDATE updateTime
    //Biến tạm
    private String updateTime;////updateTime

    public String getTarger() {
        return targer;
    }

    public void setTarger(String targer) {
        this.targer = targer;
    }

    public String getCauser() {
        return causer;
    }

    public void setCauser(String causer) {
        this.causer = causer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC4a() {
        return c4a;
    }

    public void setC4a(String c4a) {
        this.c4a = c4a;
    }

    public String getC4b() {
        return c4b;
    }

    public void setC4b(String c4b) {
        this.c4b = c4b;
    }

    public String getC8b() {
        return c8b;
    }

    public void setC8b(String c8b) {
        this.c8b = c8b;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getD4() {
        return d4;
    }

    public void setD4(String d4) {
        this.d4 = d4;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String BookingID) {
        this.BookingID = BookingID;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getUPDATEupdateTime() {
        return UPDATEupdateTime;
    }

    public void setUPDATEupdateTime(String UPDATEupdateTime) {
        this.UPDATEupdateTime = UPDATEupdateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLaytestStaff() {
        return laytestStaff;
    }

    public void setLaytestStaff(String laytestStaff) {
        this.laytestStaff = laytestStaff;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentProvinceID() {
        return permanentProvinceID;
    }

    public void setPermanentProvinceID(String permanentProvinceID) {
        this.permanentProvinceID = permanentProvinceID;
    }

    public String getPermanentDistrictID() {
        return permanentDistrictID;
    }

    public void setPermanentDistrictID(String permanentDistrictID) {
        this.permanentDistrictID = permanentDistrictID;
    }

    public String getPermanentWardID() {
        return permanentWardID;
    }

    public void setPermanentWardID(String permanentWardID) {
        this.permanentWardID = permanentWardID;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentProvinceID() {
        return currentProvinceID;
    }

    public void setCurrentProvinceID(String currentProvinceID) {
        this.currentProvinceID = currentProvinceID;
    }

    public String getCurrentDistrictID() {
        return currentDistrictID;
    }

    public void setCurrentDistrictID(String currentDistrictID) {
        this.currentDistrictID = currentDistrictID;
    }

    public String getCurrentWardID() {
        return currentWardID;
    }

    public void setCurrentWardID(String currentWardID) {
        this.currentWardID = currentWardID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getObjectGroupID_ncmt() {
        return objectGroupID_ncmt;
    }

    public void setObjectGroupID_ncmt(String objectGroupID_ncmt) {
        this.objectGroupID_ncmt = objectGroupID_ncmt;
    }

    public String getObjectGroupID_pnbd() {
        return objectGroupID_pnbd;
    }

    public void setObjectGroupID_pnbd(String objectGroupID_pnbd) {
        this.objectGroupID_pnbd = objectGroupID_pnbd;
    }

    public String getObjectGroupID_pnmangthai() {
        return objectGroupID_pnmangthai;
    }

    public void setObjectGroupID_pnmangthai(String objectGroupID_pnmangthai) {
        this.objectGroupID_pnmangthai = objectGroupID_pnmangthai;
    }

    public String getObjectGroupID_nguoihienmau() {
        return objectGroupID_nguoihienmau;
    }

    public void setObjectGroupID_nguoihienmau(String objectGroupID_nguoihienmau) {
        this.objectGroupID_nguoihienmau = objectGroupID_nguoihienmau;
    }

    public String getObjectGroupID_lao() {
        return objectGroupID_lao;
    }

    public void setObjectGroupID_lao(String objectGroupID_lao) {
        this.objectGroupID_lao = objectGroupID_lao;
    }

    public String getObjectGroupID_laytruyenquatd() {
        return objectGroupID_laytruyenquatd;
    }

    public void setObjectGroupID_laytruyenquatd(String objectGroupID_laytruyenquatd) {
        this.objectGroupID_laytruyenquatd = objectGroupID_laytruyenquatd;
    }

    public String getObjectGroupID_nghiavuquansu() {
        return objectGroupID_nghiavuquansu;
    }

    public void setObjectGroupID_nghiavuquansu(String objectGroupID_nghiavuquansu) {
        this.objectGroupID_nghiavuquansu = objectGroupID_nghiavuquansu;
    }

    public String getObjectGroupID_msm() {
        return objectGroupID_msm;
    }

    public void setObjectGroupID_msm(String objectGroupID_msm) {
        this.objectGroupID_msm = objectGroupID_msm;
    }

    public String getObjectGroupID_chuyengioi() {
        return objectGroupID_chuyengioi;
    }

    public void setObjectGroupID_chuyengioi(String objectGroupID_chuyengioi) {
        this.objectGroupID_chuyengioi = objectGroupID_chuyengioi;
    }

    public String getObjectGroupID_vcbantinhnguoinhiem() {
        return objectGroupID_vcbantinhnguoinhiem;
    }

    public void setObjectGroupID_vcbantinhnguoinhiem(String objectGroupID_vcbantinhnguoinhiem) {
        this.objectGroupID_vcbantinhnguoinhiem = objectGroupID_vcbantinhnguoinhiem;
    }

    public String getObjectGroupID_vcbantinhnguoincc() {
        return objectGroupID_vcbantinhnguoincc;
    }

    public void setObjectGroupID_vcbantinhnguoincc(String objectGroupID_vcbantinhnguoincc) {
        this.objectGroupID_vcbantinhnguoincc = objectGroupID_vcbantinhnguoincc;
    }

    public String getObjectGroupID_benhnhannghingoaids() {
        return objectGroupID_benhnhannghingoaids;
    }

    public void setObjectGroupID_benhnhannghingoaids(String objectGroupID_benhnhannghingoaids) {
        this.objectGroupID_benhnhannghingoaids = objectGroupID_benhnhannghingoaids;
    }

    public String getObjectGroupID_other() {
        return objectGroupID_other;
    }

    public void setObjectGroupID_other(String objectGroupID_other) {
        this.objectGroupID_other = objectGroupID_other;
    }

    public String getRiskBehaviorID_tiemtrich() {
        return riskBehaviorID_tiemtrich;
    }

    public void setRiskBehaviorID_tiemtrich(String riskBehaviorID_tiemtrich) {
        this.riskBehaviorID_tiemtrich = riskBehaviorID_tiemtrich;
    }

    public String getRiskBehaviorID_quanhetinhducnguoimuabandam() {
        return riskBehaviorID_quanhetinhducnguoimuabandam;
    }

    public void setRiskBehaviorID_quanhetinhducnguoimuabandam(String riskBehaviorID_quanhetinhducnguoimuabandam) {
        this.riskBehaviorID_quanhetinhducnguoimuabandam = riskBehaviorID_quanhetinhducnguoimuabandam;
    }

    public String getRiskBehaviorID_msm() {
        return riskBehaviorID_msm;
    }

    public void setRiskBehaviorID_msm(String riskBehaviorID_msm) {
        this.riskBehaviorID_msm = riskBehaviorID_msm;
    }

    public String getRiskBehaviorID_quanhenhieunguoi() {
        return riskBehaviorID_quanhenhieunguoi;
    }

    public void setRiskBehaviorID_quanhenhieunguoi(String riskBehaviorID_quanhenhieunguoi) {
        this.riskBehaviorID_quanhenhieunguoi = riskBehaviorID_quanhenhieunguoi;
    }

    public String getRiskBehaviorID_other() {
        return riskBehaviorID_other;
    }

    public void setRiskBehaviorID_other(String riskBehaviorID_other) {
        this.riskBehaviorID_other = riskBehaviorID_other;
    }

    public String getReferralSource_tiepcancongdong() {
        return referralSource_tiepcancongdong;
    }

    public void setReferralSource_tiepcancongdong(String referralSource_tiepcancongdong) {
        this.referralSource_tiepcancongdong = referralSource_tiepcancongdong;
    }

    public String getReferralSource_xetnghiemtheodau() {
        return referralSource_xetnghiemtheodau;
    }

    public void setReferralSource_xetnghiemtheodau(String referralSource_xetnghiemtheodau) {
        this.referralSource_xetnghiemtheodau = referralSource_xetnghiemtheodau;
    }

    public String getReferralSource_canboyte() {
        return referralSource_canboyte;
    }

    public void setReferralSource_canboyte(String referralSource_canboyte) {
        this.referralSource_canboyte = referralSource_canboyte;
    }

    public String getReferralSource_mangxahoi() {
        return referralSource_mangxahoi;
    }

    public void setReferralSource_mangxahoi(String referralSource_mangxahoi) {
        this.referralSource_mangxahoi = referralSource_mangxahoi;
    }

    public String getReferralSource_other() {
        return referralSource_other;
    }

    public void setReferralSource_other(String referralSource_other) {
        this.referralSource_other = referralSource_other;
    }

    public String getApproacherNo() {
        return approacherNo;
    }

    public void setApproacherNo(String approacherNo) {
        this.approacherNo = approacherNo;
    }

    public String getYouInjectCode() {
        return youInjectCode;
    }

    public void setYouInjectCode(String youInjectCode) {
        this.youInjectCode = youInjectCode;
    }

    public String getPreTestTime() {
        return preTestTime;
    }

    public void setPreTestTime(String preTestTime) {
        this.preTestTime = preTestTime;
    }

    public String getIsAgreePreTest() {
        return isAgreePreTest;
    }

    public void setIsAgreePreTest(String isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public String getResultsTime() {
        return resultsTime;
    }

    public void setResultsTime(String resultsTime) {
        this.resultsTime = resultsTime;
    }

    public String getIsAgreeTest() {
        return isAgreeTest;
    }

    public void setIsAgreeTest(String isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public String getSiteVisit() {
        return siteVisit;
    }

    public void setSiteVisit(String siteVisit) {
        this.siteVisit = siteVisit;
    }

    public String getSiteConfirmTest() {
        return siteConfirmTest;
    }

    public void setSiteConfirmTest(String siteConfirmTest) {
        this.siteConfirmTest = siteConfirmTest;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getEarlyHiv() {
        return earlyHiv;
    }

    public void setEarlyHiv(String earlyHiv) {
        this.earlyHiv = earlyHiv;
    }

    public String getModeOfTransmission() {
        return modeOfTransmission;
    }

    public void setModeOfTransmission(String modeOfTransmission) {
        this.modeOfTransmission = modeOfTransmission;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getResultsSiteTime() {
        return resultsSiteTime;
    }

    public void setResultsSiteTime(String resultsSiteTime) {
        this.resultsSiteTime = resultsSiteTime;
    }

    public String getVirusLoad() {
        return virusLoad;
    }

    public void setVirusLoad(String virusLoad) {
        this.virusLoad = virusLoad;
    }

    public String getArv_hiv() {
        return arv_hiv;
    }

    public void setArv_hiv(String arv_hiv) {
        this.arv_hiv = arv_hiv;
    }

    public String getArv_lnqdtd() {
        return arv_lnqdtd;
    }

    public void setArv_lnqdtd(String arv_lnqdtd) {
        this.arv_lnqdtd = arv_lnqdtd;
    }

    public String getArv_lao() {
        return arv_lao;
    }

    public void setArv_lao(String arv_lao) {
        this.arv_lao = arv_lao;
    }

    public String getArv_pmtct() {
        return arv_pmtct;
    }

    public void setArv_pmtct(String arv_pmtct) {
        this.arv_pmtct = arv_pmtct;
    }

    public String getArv_khhgd() {
        return arv_khhgd;
    }

    public void setArv_khhgd(String arv_khhgd) {
        this.arv_khhgd = arv_khhgd;
    }

    public String getArv_chamsocytekhac() {
        return arv_chamsocytekhac;
    }

    public void setArv_chamsocytekhac(String arv_chamsocytekhac) {
        this.arv_chamsocytekhac = arv_chamsocytekhac;
    }

    public String getArv_tiepcancongdong() {
        return arv_tiepcancongdong;
    }

    public void setArv_tiepcancongdong(String arv_tiepcancongdong) {
        this.arv_tiepcancongdong = arv_tiepcancongdong;
    }

    public String getArv_nhomhotro() {
        return arv_nhomhotro;
    }

    public void setArv_nhomhotro(String arv_nhomhotro) {
        this.arv_nhomhotro = arv_nhomhotro;
    }

    public String getArv_cainghiencongdong() {
        return arv_cainghiencongdong;
    }

    public void setArv_cainghiencongdong(String arv_cainghiencongdong) {
        this.arv_cainghiencongdong = arv_cainghiencongdong;
    }

    public String getArv_tuvangiamnguyco() {
        return arv_tuvangiamnguyco;
    }

    public void setArv_tuvangiamnguyco(String arv_tuvangiamnguyco) {
        this.arv_tuvangiamnguyco = arv_tuvangiamnguyco;
    }

    public String getArv_laymauxnkhangdinhhiv() {
        return arv_laymauxnkhangdinhhiv;
    }

    public void setArv_laymauxnkhangdinhhiv(String arv_laymauxnkhangdinhhiv) {
        this.arv_laymauxnkhangdinhhiv = arv_laymauxnkhangdinhhiv;
    }

    public String getArv_other() {
        return arv_other;
    }

    public void setArv_other(String arv_other) {
        this.arv_other = arv_other;
    }

    public String getKhongchuyengui() {
        return khongchuyengui;
    }

    public void setKhongchuyengui(String khongchuyengui) {
        this.khongchuyengui = khongchuyengui;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExchangeConsultTime() {
        return exchangeConsultTime;
    }

    public void setExchangeConsultTime(String exchangeConsultTime) {
        this.exchangeConsultTime = exchangeConsultTime;
    }

    public String getArvExchangeResult() {
        return arvExchangeResult;
    }

    public void setArvExchangeResult(String arvExchangeResult) {
        this.arvExchangeResult = arvExchangeResult;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getPartnerProvideResult() {
        return partnerProvideResult;
    }

    public void setPartnerProvideResult(String partnerProvideResult) {
        this.partnerProvideResult = partnerProvideResult;
    }

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getExchangeProvinceID() {
        return exchangeProvinceID;
    }

    public void setExchangeProvinceID(String exchangeProvinceID) {
        this.exchangeProvinceID = exchangeProvinceID;
    }

    public String getExchangeDistrictID() {
        return exchangeDistrictID;
    }

    public void setExchangeDistrictID(String exchangeDistrictID) {
        this.exchangeDistrictID = exchangeDistrictID;
    }

    public String getRegisterTherapyTime() {
        return registerTherapyTime;
    }

    public void setRegisterTherapyTime(String registerTherapyTime) {
        this.registerTherapyTime = registerTherapyTime;
    }

    public String getRegisteredTherapySite() {
        return registeredTherapySite;
    }

    public void setRegisteredTherapySite(String registeredTherapySite) {
        this.registeredTherapySite = registeredTherapySite;
    }

    public String getTherapyRegistProvinceID() {
        return therapyRegistProvinceID;
    }

    public void setTherapyRegistProvinceID(String therapyRegistProvinceID) {
        this.therapyRegistProvinceID = therapyRegistProvinceID;
    }

    public String getTherapyRegistDistrictID() {
        return therapyRegistDistrictID;
    }

    public void setTherapyRegistDistrictID(String therapyRegistDistrictID) {
        this.therapyRegistDistrictID = therapyRegistDistrictID;
    }

    public String getTherapyNo() {
        return therapyNo;
    }

    public void setTherapyNo(String therapyNo) {
        this.therapyNo = therapyNo;
    }

    public String getStaffBeforeTestID() {
        return staffBeforeTestID;
    }

    public void setStaffBeforeTestID(String staffBeforeTestID) {
        this.staffBeforeTestID = staffBeforeTestID;
    }

    public String getStaffAfterID() {
        return staffAfterID;
    }

    public void setStaffAfterID(String staffAfterID) {
        this.staffAfterID = staffAfterID;
    }

    public String getExechangeService() {
        return exechangeService;
    }

    public void setExechangeService(String exechangeService) {
        this.exechangeService = exechangeService;
    }

}
