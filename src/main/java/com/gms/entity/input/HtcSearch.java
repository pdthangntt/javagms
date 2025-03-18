package com.gms.entity.input;

import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 *
 * @author vvthanh
 */
public class HtcSearch implements Cloneable {

    private Long ID;
    private String advisoryeTime;

    private String advisoryeTimeFrom; //Ngày tư vấn từ
    private String advisoryeTimeTo; //Ngày tư vấn đến

    private String confirmTimeFrom; //Ngày xn khẳng định từ
    private String confirmTimeTo; //Ngày xn khẳng định đến

    private String preTestTimeFrom; //Ngày xét nghiệm sàng lọc từ
    private String preTestTimeTo; //Ngày xét nghiệm sàng lọc đến

    private String exchangeTimeFrom; //Ngày chuyển gửi từ
    private String exchangeTimeTo; //Ngày chuyển gửi đến

    private String resultsSiteTimeFrom; //Ngày xét Cơ sở nhận kết quả sàng lọc từ
    private String resultsSiteTimeTo; //đến

    private int yearOfBirthFrom; //Năm sinh từ
    private int yearOfBirthTo; //Đến

    private Set<String> objectGroupID; //Đối tượng 
    private Set<String> serviceID; //Dịch vụ cơ sở xét nghiệm
    private Set<String> searchServiceID; //Dịch vụ cơ sở xét nghiệm khi tìm kiếm
    private Set<String> genderID; //Giơi tính
    private String name;
    private String code;
    private String confirmTestNo;
    private String confirmResultsID; //Kết quả xét nghiệm khẳng định
    private String testResultsID; //Kết quả xét nghiệm sàng lọc
    private String confirmTestStatus; //Trạng thái XN khẳng định 
    private String therapyExchangeStatus; //Trạng thái chuyển gửi
    private String feedbackStatus; //Trạng thái đối chiếu
    private String earlyDiagnose; // Kết luận nhiễm mới
    private Set<Long> siteID;
    private boolean opc; //Có chuyển gửi điều trị
    private boolean confirm; //Khách hàng đồng ý XN khẳng định
    private boolean remove;
    private boolean removeTranfer;
    private boolean notReceive; // Khách hàng chưa nhận kết quả
    private boolean cofirmCreated; // Khách hàng tạo từ khẳng định
    private int filter; //Đánh dấu lọc theo dịch vụ phân quyền dữ liệu nhân viên
    private Long createdBY; //Nhân viên tạo
    private String gsphStatus; // Trạng thái chuyển GSPH
    private String receiveStatus;//Trạng thái tiếp nhận
    private String requestStatus; // Trạng thái rà saoits
    private String customerType;
    //Phân trang
    private int pageIndex;
    private int pageSize;
    private Sort sortable;

    public String getEarlyDiagnose() {
        return earlyDiagnose;
    }

    public void setEarlyDiagnose(String earlyDiagnose) {
        this.earlyDiagnose = earlyDiagnose;
    }
    
    public boolean isCofirmCreated() {
        return cofirmCreated;
    }

    public void setCofirmCreated(boolean cofirmCreated) {
        this.cofirmCreated = cofirmCreated;
    }
    
    public boolean isNotReceive() {
        return notReceive;
    }

    public void setNotReceive(boolean notReceive) {
        this.notReceive = notReceive;
    }
    
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public boolean isRemoveTranfer() {
        return removeTranfer;
    }

    public void setRemoveTranfer(boolean removeTranfer) {
        this.removeTranfer = removeTranfer;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getConfirmTestNo() {
        return confirmTestNo;
    }

    public void setConfirmTestNo(String confirmTestNo) {
        this.confirmTestNo = confirmTestNo;
    }

    public String getAdvisoryeTime() {
        return advisoryeTime;
    }

    public void setAdvisoryeTime(String advisoryeTime) {
        this.advisoryeTime = advisoryeTime;
    }

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public boolean isOpc() {
        return opc;
    }

    public void setOpc(boolean opc) {
        this.opc = opc;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getConfirmTimeFrom() {
        return confirmTimeFrom;
    }

    public void setConfirmTimeFrom(String confirmTimeFrom) {
        this.confirmTimeFrom = confirmTimeFrom;
    }

    public String getConfirmTimeTo() {
        return confirmTimeTo;
    }

    public void setConfirmTimeTo(String confirmTimeTo) {
        this.confirmTimeTo = confirmTimeTo;
    }

    public String getConfirmTestStatus() {
        return confirmTestStatus;
    }

    public void setConfirmTestStatus(String confirmTestStatus) {
        this.confirmTestStatus = confirmTestStatus;
    }

    public Set<String> getGenderID() {
        return genderID;
    }

    public void setGenderID(Set<String> genderID) {
        this.genderID = genderID;
    }

    public String getTestResultsID() {
        return testResultsID;
    }

    public void setTestResultsID(String testResultsID) {
        this.testResultsID = testResultsID;
    }

    public int getYearOfBirthFrom() {
        return yearOfBirthFrom;
    }

    public void setYearOfBirthFrom(int yearOfBirthFrom) {
        this.yearOfBirthFrom = yearOfBirthFrom;
    }

    public int getYearOfBirthTo() {
        return yearOfBirthTo;
    }

    public void setYearOfBirthTo(int yearOfBirthTo) {
        this.yearOfBirthTo = yearOfBirthTo;
    }

    public String getConfirmResultsID() {
        return confirmResultsID;
    }

    public void setConfirmResultsID(String confirmResultsID) {
        this.confirmResultsID = confirmResultsID;
    }

    public String getExchangeTimeFrom() {
        return exchangeTimeFrom;
    }

    public void setExchangeTimeFrom(String exchangeTimeFrom) {
        this.exchangeTimeFrom = exchangeTimeFrom;
    }

    public String getExchangeTimeTo() {
        return exchangeTimeTo;
    }

    public void setExchangeTimeTo(String exchangeTimeTo) {
        this.exchangeTimeTo = exchangeTimeTo;
    }

    public String getTherapyExchangeStatus() {
        return therapyExchangeStatus;
    }

    public void setTherapyExchangeStatus(String therapyExchangeStatus) {
        this.therapyExchangeStatus = therapyExchangeStatus;
    }

    public String getPreTestTimeFrom() {
        return preTestTimeFrom;
    }

    public void setPreTestTimeFrom(String preTestTimeFrom) {
        this.preTestTimeFrom = preTestTimeFrom;
    }

    public String getPreTestTimeTo() {
        return preTestTimeTo;
    }

    public void setPreTestTimeTo(String preTestTimeTo) {
        this.preTestTimeTo = preTestTimeTo;
    }

    public String getResultsSiteTimeFrom() {
        return resultsSiteTimeFrom;
    }

    public void setResultsSiteTimeFrom(String resultsSiteTimeFrom) {
        this.resultsSiteTimeFrom = resultsSiteTimeFrom;
    }

    public String getResultsSiteTimeTo() {
        return resultsSiteTimeTo;
    }

    public void setResultsSiteTimeTo(String resultsSiteTimeTo) {
        this.resultsSiteTimeTo = resultsSiteTimeTo;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getAdvisoryeTimeFrom() {
        return advisoryeTimeFrom;
    }

    public void setAdvisoryeTimeFrom(String advisoryeTimeFrom) {
        this.advisoryeTimeFrom = advisoryeTimeFrom;
    }

    public String getAdvisoryeTimeTo() {
        return advisoryeTimeTo;
    }

    public void setAdvisoryeTimeTo(String advisoryeTimeTo) {
        this.advisoryeTimeTo = advisoryeTimeTo;
    }

    public Set<String> getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(Set<String> objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Set<String> getServiceID() {
        return serviceID;
    }

    public void setServiceID(Set<String> serviceID) {
        this.serviceID = serviceID;
    }

    public Set<String> getSearchServiceID() {
        return searchServiceID;
    }

    public void setSearchServiceID(Set<String> searchServiceID) {
        this.searchServiceID = searchServiceID;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Long> getSiteID() {
        return siteID;
    }

    public void setSiteID(Set<Long> siteID) {
        this.siteID = siteID;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getGsphStatus() {
        return gsphStatus;
    }

    public void setGsphStatus(String gsphStatus) {
        this.gsphStatus = gsphStatus;
    }

    @Override
    public HtcSearch clone() throws CloneNotSupportedException {
        return (HtcSearch) super.clone();
    }

}
