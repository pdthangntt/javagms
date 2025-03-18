package com.gms.entity.form.htc;

import java.util.Date;

/**
 *
 * @author TrangBN
 */
public class SampleSentTableForm {
    
    private Long ID;
    private String fullname;
    private String code;
    private String patientID;
    private String year;
    private String address;
    private String objectGroupID;
    private Date sourceReceiveSampleTime; // Ngày nhận mẫu
    private String resultsID; // Kết quả XN khẳng định
    private String roomTestNo; // Mã số phòng XN
    private String gender; // Giới tính
    private String bioName; // Giới tính

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
    
    

    public String getBioName() {
        return bioName;
    }

    public void setBioName(String bioName) {
        this.bioName = bioName;
    }
    
    

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getObjectGroupID() {
        return objectGroupID;
    }

    public void setObjectGroupID(String objectGroupID) {
        this.objectGroupID = objectGroupID;
    }

    public Date getSourceReceiveSampleTime() {
        return sourceReceiveSampleTime;
    }

    public void setSourceReceiveSampleTime(Date sourceReceiveSampleTime) {
        this.sourceReceiveSampleTime = sourceReceiveSampleTime;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

    public String getRoomTestNo() {
        return roomTestNo;
    }

    public void setRoomTestNo(String roomTestNo) {
        this.roomTestNo = roomTestNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
