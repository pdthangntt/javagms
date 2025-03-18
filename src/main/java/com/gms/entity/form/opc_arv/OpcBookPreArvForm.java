package com.gms.entity.form.opc_arv;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.OpcArvEntity;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pdThang
 */
public class OpcBookPreArvForm {

    // Tên báo cáo
    private String title;
    // Tên file
    private String fileName;
    // Tên cơ sở
    private String siteName;
    // Tên nhân viên
    private String staffName;
    // Tên cục, sở
    private String siteAgency;
    // Điều kiện ngày bắt đầu tìm kiếm 
    private String start;
    // Điều kiện ngày kết thúc tìm kiếm
    private String end;

    //danh sách bệnh án
    private List<OpcPreArvTableForm> table;
    private DataPage<OpcPreArvTableForm> dataPage;
    // ID của cơ sở hiện tại
    private Long siteID;
    // Site provider
    private String siteProvince;
    //Danh sách định nghĩa cần hiển thị
    private HashMap<String, HashMap<String, String>> options;
    private String year;
    private String word;
    private List<Integer> order;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    
    public DataPage<OpcPreArvTableForm> getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage<OpcPreArvTableForm> dataPage) {
        this.dataPage = dataPage;
    }

    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getSiteAgency() {
        return siteAgency;
    }

    public void setSiteAgency(String siteAgency) {
        this.siteAgency = siteAgency;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<OpcPreArvTableForm> getTable() {
        return table;
    }

    public void setTable(List<OpcPreArvTableForm> table) {
        this.table = table;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public HashMap<String, HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, HashMap<String, String>> options) {
        this.options = options;
    }

}
