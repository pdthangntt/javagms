package com.gms.entity.form.htc;

/**
 * Form Sổ tư vấn xét nghiệm
 * @author TrangBN
 */
public class VisitBookTableForm {
    
    private Long ID;
    
    //----- A. Thông tin khách hàng
    
    private String advisoryeTime; //Ngày tư vấn
    private String serviceID;
    private String yearOfBirth;
    private String patientID;
    private String fixedServiceID;
    private String code;
    private String patientName;
    private String gender;
    private String objectGroupID; //Nhóm đối tượng
    private String permanentAddress; //Địa chỉ thường trú
    
    //----------B. Xét nghiệm sàng lọc
    
    private String testResultsID; //Kết quả xét nghiệm sàng lọc
    private String resultsTime; //Ngày trả kết quả xét nghiệm sàng lọc
    private Boolean isAgreePreTest; // Đồng ý tiếp tục xét nghiệm hay không
    private Boolean isAgreeTest; // Đồng ý tiếp tục xét nghiệm hay không
    
    //----------C. Xét nghiệm khẳng định
    
    private String confirmTestNo; // Mã xét nghiệm khẳng định
    private String confirmResult; //Kết quả xét nghiệm khẳng định
    private String confirmResultID; //Kết quả xét nghiệm khẳng định
    private String resultsPatienTime; //Ngày trả kết quả xét nghiệm khẳng định cho khách hàng
    
    //-----------D. Chuyển gửi điều trị ARV
    
    private String exchangeConsultTime; // Ngày thực hiện tư vấn chuyển gửi điều trị
    private String arvExchangeResult; // Kết quả tư vấn chuyển điều trị ARV
    private String exchangeTime; // Ngày chuyển gửi
    private String arrivalSite; // Tên cơ sở điều trị khách hàng được chuyển đến
    private String serviceAfterConsult; // Dịch vụ sau tư vấn

    //-----------E. Tư vấn viên
    private String staffBeforeTest; //Nhân viên tư vấn trước xét nghiệm
    private String staffAfter; // Nhân viên tư vấn sau xét nghiệm
    //-------- Other
    private Long siteID;
    private String exchangeService;

    public String getExchangeService() {
        return exchangeService;
    }

    public void setExchangeService(String exchangeService) {
        this.exchangeService = exchangeService;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getFixedServiceID() {
        return fixedServiceID;
    }

    public void setFixedServiceID(String fixedServiceID) {
        this.fixedServiceID = fixedServiceID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
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

    public Boolean getIsAgreePreTest() {
        return isAgreePreTest;
    }

    public void setIsAgreePreTest(Boolean isAgreePreTest) {
        this.isAgreePreTest = isAgreePreTest;
    }

    public Boolean getIsAgreeTest() {
        return isAgreeTest;
    }

    public void setIsAgreeTest(Boolean isAgreeTest) {
        this.isAgreeTest = isAgreeTest;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(String confirmResult) {
        this.confirmResult = confirmResult;
    }

    public String getResultsPatienTime() {
        return resultsPatienTime;
    }

    public void setResultsPatienTime(String resultsPatienTime) {
        this.resultsPatienTime = resultsPatienTime;
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

    public String getArrivalSite() {
        return arrivalSite;
    }

    public void setArrivalSite(String arrivalSite) {
        this.arrivalSite = arrivalSite;
    }

    public String getStaffBeforeTest() {
        return staffBeforeTest;
    }

    public void setStaffBeforeTest(String staffBeforeTest) {
        this.staffBeforeTest = staffBeforeTest;
    }

    public String getStaffAfter() {
        return staffAfter;
    }

    public void setStaffAfter(String staffAfter) {
        this.staffAfter = staffAfter;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getServiceAfterConsult() {
        return serviceAfterConsult;
    }

    public void setServiceAfterConsult(String serviceAfterConsult) {
        this.serviceAfterConsult = serviceAfterConsult;
    }

    public String getConfirmResultID() {
        return confirmResultID;
    }

    public void setConfirmResultID(String confirmResultID) {
        this.confirmResultID = confirmResultID;
    }
    
}
