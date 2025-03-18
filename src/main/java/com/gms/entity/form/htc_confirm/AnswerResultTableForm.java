package com.gms.entity.form.htc_confirm;

/**
 *
 * @author pdThang
 */
public class AnswerResultTableForm {

    private Long ID;
    private String fullname;
    private String patientID;
    private String code;
    private Integer year;
    private String genderID; // Giới tính
    private String address;
    private String objectGroupID;
    private String bioName1;
    private String bioName2;
    private String bioName3;
    private String bioNameResult1;
    private String bioNameResult2;
    private String bioNameResult3;
    private String otherTechnical;
    private String resultsID; // Kết quả XN khẳng định
    private String note;

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenderID() {
        return genderID;
    }

    public void setGenderID(String genderID) {
        this.genderID = genderID;
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

    public String getBioName1() {
        return bioName1;
    }

    public void setBioName1(String bioName1) {
        this.bioName1 = bioName1;
    }

    public String getBioName2() {
        return bioName2;
    }

    public void setBioName2(String bioName2) {
        this.bioName2 = bioName2;
    }

    public String getBioName3() {
        return bioName3;
    }

    public void setBioName3(String bioName3) {
        this.bioName3 = bioName3;
    }

    public String getBioNameResult1() {
        return bioNameResult1;
    }

    public void setBioNameResult1(String bioNameResult1) {
        this.bioNameResult1 = bioNameResult1;
    }

    public String getBioNameResult2() {
        return bioNameResult2;
    }

    public void setBioNameResult2(String bioNameResult2) {
        this.bioNameResult2 = bioNameResult2;
    }

    public String getBioNameResult3() {
        return bioNameResult3;
    }

    public void setBioNameResult3(String bioNameResult3) {
        this.bioNameResult3 = bioNameResult3;
    }

    public String getOtherTechnical() {
        return otherTechnical;
    }

    public void setOtherTechnical(String otherTechnical) {
        this.otherTechnical = otherTechnical;
    }

    public String getResultsID() {
        return resultsID;
    }

    public void setResultsID(String resultsID) {
        this.resultsID = resultsID;
    }

}
