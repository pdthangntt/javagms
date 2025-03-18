package com.gms.entity.input;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 *
 * @author vvthanh
 */
public class HtcConfirmEarlySearch {

    private Long siteID;
    private String code;
    private String sourceID;
    private String fullname;
    private String confirmTimeFrom; //Ngày xét nghiệm từ
    private String confirmTimeTo; //Ngày xét nghiệm đến
    private String earlyHivTimeFrom; //Ngày xét nghiệm nhiễm mới từ
    private String earlyHivTimeTo; //Ngày xét nghiệm nhiễm mới đến
    private String resultsID;
    private List<Long> sourceSiteID;
    private String acceptDateFrom; //Ngày xét tiếp nhận từ
    private String acceptDateTo; //Ngày xét tiếp nhận đến
    private String sourceSampleDateFrom; //Ngày gửi mẫu từ
    private String confirmStatus; //Ngày gửi mẫu từ
    private boolean wait;
    private boolean received;
    private boolean result;
    private boolean remove;
    private boolean update;//tab cập nhật thông tin kh
    private String confirmFeedback;
    private String gsphStatus; // Trạng thái chuyển GSPH
    private String requestStatus; // Trạng thái rà saoits

    public List<Long> getSourceSiteID() {
        return sourceSiteID;
    }

    public void setSourceSiteID(List<Long> sourceSiteID) {
        this.sourceSiteID = sourceSiteID;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
    private boolean isRequestAdditional;
    private String requestHtcTimeFrom; //Ngày xét tiếp nhận từ
    private String requestHtcTimeTo; //Ngày xét tiếp nhận đến

    public String getRequestHtcTimeFrom() {
        return requestHtcTimeFrom;
    }

    public void setRequestHtcTimeFrom(String requestHtcTimeFrom) {
        this.requestHtcTimeFrom = requestHtcTimeFrom;
    }

    public String getRequestHtcTimeTo() {
        return requestHtcTimeTo;
    }

    public void setRequestHtcTimeTo(String requestHtcTimeTo) {
        this.requestHtcTimeTo = requestHtcTimeTo;
    }

    public boolean isIsRequestAdditional() {
        return isRequestAdditional;
    }

    public void setIsRequestAdditional(boolean isRequestAdditional) {
        this.isRequestAdditional = isRequestAdditional;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getConfirmFeedback() {
        return confirmFeedback;
    }

    public void setConfirmFeedback(String confirmFeedback) {
        this.confirmFeedback = confirmFeedback;
    }

    public String getSourceID() {
        return sourceID;
    }

    public String getEarlyHivTimeFrom() {
        return earlyHivTimeFrom;
    }

    public void setEarlyHivTimeFrom(String earlyHivTimeFrom) {
        this.earlyHivTimeFrom = earlyHivTimeFrom;
    }

    public String getEarlyHivTimeTo() {
        return earlyHivTimeTo;
    }

    public void setEarlyHivTimeTo(String earlyHivTimeTo) {
        this.earlyHivTimeTo = earlyHivTimeTo;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    private Sort sortable;

    public Sort getSortable() {
        return sortable;
    }

    public void setSortable(Sort sortable) {
        this.sortable = sortable;
    }

    public String getSourceSampleDateFrom() {
        return sourceSampleDateFrom;
    }

    public void setSourceSampleDateFrom(String sourceSampleDateFrom) {
        this.sourceSampleDateFrom = sourceSampleDateFrom;
    }

    public String getSourceSampleDateTo() {
        return sourceSampleDateTo;
    }

    public void setSourceSampleDateTo(String sourceSampleDateTo) {
        this.sourceSampleDateTo = sourceSampleDateTo;
    }
    private String sourceSampleDateTo; //Ngày gửi mẫu đến

    public String getAcceptDateFrom() {
        return acceptDateFrom;
    }

    public void setAcceptDateFrom(String acceptDateFrom) {
        this.acceptDateFrom = acceptDateFrom;
    }

    public String getAcceptDateTo() {
        return acceptDateTo;
    }

    public void setAcceptDateTo(String acceptDateTo) {
        this.acceptDateTo = acceptDateTo;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    //Phân trang
    private int pageIndex;
    private int pageSize;

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getGsphStatus() {
        return gsphStatus;
    }

    public void setGsphStatus(String gsphStatus) {
        this.gsphStatus = gsphStatus;
    }
}
