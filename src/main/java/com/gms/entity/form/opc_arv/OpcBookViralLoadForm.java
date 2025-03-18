package com.gms.entity.form.opc_arv;

import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.TreatmentRegimenStageEnum;
import com.gms.entity.form.BaseForm;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

/**
 * Form sổ tải lượng virus
 * 
 * @author TrangBN
 */
public class OpcBookViralLoadForm  extends BaseForm {

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
    private String startDate;
    // Điều kiện ngày kết thúc tìm kiếm
    private String endDate;
    // ID của cơ sở hiện tại
    private Long siteID;
    // Site provider
    private String siteProvince;
    //Danh sách định nghĩa cần hiển thị
    private HashMap<String, HashMap<String, String>> options;
    //Tìm trong tháng
    private boolean searchInMonth;
    
    // Từ khóa
    private String keywords;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public boolean isSearchInMonth() {
        return searchInMonth;
    }

    public void setSearchInMonth(boolean searchInMonth) {
        this.searchInMonth = searchInMonth;
    }

    //Danh sách bệnh án
    private DataPage<OpcBookViralLoadTableForm> table;
    private DataPage<OpcBookViralLoadTableForm> table2All;
    private OpcBookViralLoadTable2Form table02;
    
    public OpcBookViralLoadMasterForm getTableMonth(){
        
        OpcBookViralLoadMasterForm masterTbl = new OpcBookViralLoadMasterForm();
        DataPage<OpcBookViralLoadTableForm> tbl2 = null;
        
        if (table.getData() == null && table2All == null) {
            return masterTbl;
        }
        
        tbl2 = table2All != null ? table2All : table; 
        
        for (OpcBookViralLoadTableForm tbl : tbl2.getData()) {
            
            if (StringUtils.isNotEmpty(tbl.getResultKq1())) {
                masterTbl.setXnLan1(masterTbl.getXnLan1() + 1);
                System.out.println("code: " + tbl.getCode());
            }
            if (tbl.getTreatmentRegimentStage() != null && (tbl.getTreatmentRegimentStage().equals(TreatmentRegimenStageEnum.TWO.getKey()))) {
                masterTbl.setDtBac2(masterTbl.getDtBac2() + 1);
            }
            if (tbl.getIdKq1() != null && StringUtils.isNotEmpty(tbl.getResult()) && (tbl.getResult().equals("1") || tbl.getResult().equals("6"))) {
                masterTbl.setXnLan1DuoiNguongPhatHien(masterTbl.getXnLan1DuoiNguongPhatHien() + 1);
            }
            if (tbl.getIdKq1() != null && StringUtils.isNotEmpty(tbl.getResult()) && tbl.getResult().equals("2")) {
                masterTbl.setXnLan1Duoi200(masterTbl.getXnLan1Duoi200() + 1);
            }
            if (tbl.getIdKq1() != null && StringUtils.isNotEmpty(tbl.getResult()) && tbl.getResult().equals("3")) {
                masterTbl.setXnLan1F200T1000(masterTbl.getXnLan1F200T1000() + 1);
            }
            if (tbl.getIdKq1() != null && StringUtils.isNotEmpty(tbl.getResult()) && tbl.getResult().equals("4")) {
                masterTbl.setXnLan1From1000(masterTbl.getXnLan1From1000() + 1);
            }
            if (tbl.getIdKq2() != null && StringUtils.isNotEmpty(tbl.getResult()) && tbl.getResult().equals("4")) {
                masterTbl.setXnLan2From1000(masterTbl.getXnLan2From1000() + 1);
            }
            if (tbl.getIdKq2() != null && StringUtils.isNotEmpty(tbl.getResult()) && tbl.getResult().equals("4") && tbl.getRegimenDate() != null) {
                masterTbl.setTbdt(masterTbl.getTbdt() + 1);
            }
        }
        return masterTbl;
    }
    
    public OpcBookViralLoadTable2Form getTable02() {
        return table02;
    }

    public void setTable02(OpcBookViralLoadTable2Form table02) {
        this.table02 = table02;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public DataPage<OpcBookViralLoadTableForm> getTable() {
        return table;
    }

    public void setTable(DataPage<OpcBookViralLoadTableForm> table) {
        this.table = table;
    }

    public DataPage<OpcBookViralLoadTableForm> getTable2All() {
        return table2All;
    }

    public void setTable2All(DataPage<OpcBookViralLoadTableForm> table2All) {
        this.table2All = table2All;
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
