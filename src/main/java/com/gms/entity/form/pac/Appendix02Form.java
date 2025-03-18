package com.gms.entity.form.pac;

import com.gms.entity.db.PacPatientInfoEntity;
import java.util.HashMap;
import java.util.List;

/**
 * Form lưu thông tin gửi mẫu XN
 * 
 * @author TrangBN
 */
public class Appendix02Form {
    
    // File information
    private String title;
    private String fileName;
    private String siteName;
    private String staffName;
    private String siteAgency;
    private String siteProvince;
    private HashMap<String, String> siteOptions;
    List<PacPatientInfoEntity> listPatient;

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

    public String getSiteProvince() {
        return siteProvince;
    }

    public void setSiteProvince(String siteProvince) {
        this.siteProvince = siteProvince;
    }

    public HashMap<String, String> getSiteOptions() {
        return siteOptions;
    }

    public void setSiteOptions(HashMap<String, String> siteOptions) {
        this.siteOptions = siteOptions;
    }

    public List<PacPatientInfoEntity> getListPatient() {
        return listPatient;
    }

    public void setListPatient(List<PacPatientInfoEntity> listPatient) {
        this.listPatient = listPatient;
    }
}
