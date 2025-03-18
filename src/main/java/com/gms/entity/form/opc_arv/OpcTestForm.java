package com.gms.entity.form.opc_arv;

import com.gms.components.TextUtils;
import com.gms.entity.db.BaseEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcTestEntity;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author TrangBN
 */
public class OpcTestForm extends BaseEntity implements Serializable {

    protected static final String FORMATDATE = "dd/MM/yyyy";

    private String ID; //Mã cơ sở quản lý - cơ sở tạo bản ghi
    private String siteID; //Mã cơ sở quản lý - cơ sở tạo bản ghi
    private String lao; //Có sàng lọc lao
    private String laoTestTime; //Ngày xét nghiệm lao
    private String laoOtherSymptom; // triệu chứng khác
    private String inh; //Điều trị dự phòng INH - sử dụng enum
    private String inhFromTime; //Lao từ ngày
    private String inhToTime; //Lao đến ngày
    private String ntch; //Nhiễm trùng cơ hội - ntch - sử dụng enum
    private String ntchOtherSymptom; // triệu chứng khác ntch
    private String cotrimoxazoleFromTime; //cotrimoxazole từ ngày
    private String cotrimoxazoleToTime; //cotrimoxazole đến ngày
    private String cd4SampleTime; // Ngày lấy mẫu
    private String cd4TestTime; // Ngày xét nghiệm
    private String firstCd4Time; //Ngày xét nghiệm cd4 đầu tiên
    private String cd4TestSiteID; //Cơ sở xét nghiệm
    private String cd4TestSiteName; //Hoặc Tên Cơ sở xét nghiệm
    private String cd4Result; //Kết quả xét nghiệm
    private String cd4ResultTime; //NGày trả kết quả
    private String cd4RetryTime; // Ngày hẹn xét nghiệm lại
    private String hbv; //Có xn hbv hay không - sử dụng enum
    private String hbvTime; // Ngày xét nghiệm hbv
    private String hbvResult; //Kết quả XN HBV
    private String hbvCase; //Lý do XN HBV
    private String hcv; //Có xn HCV hay không - sử dụng enum
    private String hcvTime; // Ngày xét nghiệm HCV
    private String hcvResult; //Kết quả XN HCV
    private String hcvCase; //Lý do XN HCV
    private String note; //Ghi chú
    private String laoResult;
    private String laoTreatment;
    private String laoStartTime;
    private String laoEndTime;
    private String stageID;

    private List<String> laoSymptoms; //Các biểu hiện nghi lao
    private List<String> inhEndCauses; //Lý do kết thúc lao - inh
    private List<String> ntchSymptoms; //Các biểu hiện ntch
    private List<String> cotrimoxazoleEndCauses; //Lý do kết thúc cotrimoxazole
    private List<String> cd4Causes; //Lý do xét nghiệm cd4 gần nhất
    
    private String suspiciousSymptoms;
    private String examinationAndTest;
    private String laoTestDate;
    private String laoDiagnose;
    private String cotrimoxazoleOtherEndCause;
    private String statusOfTreatmentID;
    
    private String print;

    public String getStatusOfTreatmentID() {
        return statusOfTreatmentID;
    }

    public void setStatusOfTreatmentID(String statusOfTreatmentID) {
        this.statusOfTreatmentID = statusOfTreatmentID;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getCotrimoxazoleOtherEndCause() {
        return cotrimoxazoleOtherEndCause;
    }

    public void setCotrimoxazoleOtherEndCause(String cotrimoxazoleOtherEndCause) {
        this.cotrimoxazoleOtherEndCause = cotrimoxazoleOtherEndCause;
    }

    public String getSuspiciousSymptoms() {
        return suspiciousSymptoms;
    }

    public void setSuspiciousSymptoms(String suspiciousSymptoms) {
        this.suspiciousSymptoms = suspiciousSymptoms;
    }

    public String getExaminationAndTest() {
        return examinationAndTest;
    }

    public void setExaminationAndTest(String examinationAndTest) {
        this.examinationAndTest = examinationAndTest;
    }

    public String getLaoTestDate() {
        return laoTestDate;
    }

    public void setLaoTestDate(String laoTestDate) {
        this.laoTestDate = laoTestDate;
    }

    public String getLaoDiagnose() {
        return laoDiagnose;
    }

    public void setLaoDiagnose(String laoDiagnose) {
        this.laoDiagnose = laoDiagnose;
    }

    
    
    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLaoResult() {
        return laoResult;
    }

    public void setLaoResult(String laoResult) {
        this.laoResult = laoResult;
    }

    public String getLaoTreatment() {
        return laoTreatment;
    }

    public void setLaoTreatment(String laoTreatment) {
        this.laoTreatment = laoTreatment;
    }

    public String getLaoStartTime() {
        return laoStartTime;
    }

    public void setLaoStartTime(String laoStartTime) {
        this.laoStartTime = laoStartTime;
    }

    public String getLaoEndTime() {
        return laoEndTime;
    }

    public void setLaoEndTime(String laoEndTime) {
        this.laoEndTime = laoEndTime;
    }

    public String getFirstCd4Time() {
        return firstCd4Time;
    }

    public void setFirstCd4Time(String firstCd4Time) {
        this.firstCd4Time = firstCd4Time;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getLao() {
        return lao;
    }

    public void setLao(String lao) {
        this.lao = lao;
    }

    public String getLaoTestTime() {
        return laoTestTime;
    }

    public void setLaoTestTime(String laoTestTime) {
        this.laoTestTime = laoTestTime;
    }

    public String getLaoOtherSymptom() {
        return laoOtherSymptom;
    }

    public void setLaoOtherSymptom(String laoOtherSymptom) {
        this.laoOtherSymptom = laoOtherSymptom;
    }

    public String getInh() {
        return inh;
    }

    public void setInh(String inh) {
        this.inh = inh;
    }

    public String getInhFromTime() {
        return inhFromTime;
    }

    public void setInhFromTime(String inhFromTime) {
        this.inhFromTime = inhFromTime;
    }

    public String getInhToTime() {
        return inhToTime;
    }

    public void setInhToTime(String inhToTime) {
        this.inhToTime = inhToTime;
    }

    public String getNtch() {
        return ntch;
    }

    public void setNtch(String ntch) {
        this.ntch = ntch;
    }

    public String getNtchOtherSymptom() {
        return ntchOtherSymptom;
    }

    public void setNtchOtherSymptom(String ntchOtherSymptom) {
        this.ntchOtherSymptom = ntchOtherSymptom;
    }

    public String getCotrimoxazoleFromTime() {
        return cotrimoxazoleFromTime;
    }

    public void setCotrimoxazoleFromTime(String cotrimoxazoleFromTime) {
        this.cotrimoxazoleFromTime = cotrimoxazoleFromTime;
    }

    public String getCotrimoxazoleToTime() {
        return cotrimoxazoleToTime;
    }

    public void setCotrimoxazoleToTime(String cotrimoxazoleToTime) {
        this.cotrimoxazoleToTime = cotrimoxazoleToTime;
    }

    public String getCd4SampleTime() {
        return cd4SampleTime;
    }

    public void setCd4SampleTime(String cd4SampleTime) {
        this.cd4SampleTime = cd4SampleTime;
    }

    public String getCd4TestTime() {
        return cd4TestTime;
    }

    public void setCd4TestTime(String cd4TestTime) {
        this.cd4TestTime = cd4TestTime;
    }

    public String getCd4TestSiteID() {
        return cd4TestSiteID;
    }

    public void setCd4TestSiteID(String cd4TestSiteID) {
        this.cd4TestSiteID = cd4TestSiteID;
    }

    public String getCd4TestSiteName() {
        return cd4TestSiteName;
    }

    public void setCd4TestSiteName(String cd4TestSiteName) {
        this.cd4TestSiteName = cd4TestSiteName;
    }

    public String getCd4Result() {
        return cd4Result;
    }

    public void setCd4Result(String cd4Result) {
        this.cd4Result = cd4Result;
    }

    public String getCd4ResultTime() {
        return cd4ResultTime;
    }

    public void setCd4ResultTime(String cd4ResultTime) {
        this.cd4ResultTime = cd4ResultTime;
    }

    public String getCd4RetryTime() {
        return cd4RetryTime;
    }

    public void setCd4RetryTime(String cd4RetryTime) {
        this.cd4RetryTime = cd4RetryTime;
    }

    public String getHbv() {
        return hbv;
    }

    public void setHbv(String hbv) {
        this.hbv = hbv;
    }

    public String getHbvTime() {
        return hbvTime;
    }

    public void setHbvTime(String hbvTime) {
        this.hbvTime = hbvTime;
    }

    public String getHbvResult() {
        return hbvResult;
    }

    public void setHbvResult(String hbvResult) {
        this.hbvResult = hbvResult;
    }

    public String getHbvCase() {
        return hbvCase;
    }

    public void setHbvCase(String hbvCase) {
        this.hbvCase = hbvCase;
    }

    public String getHcv() {
        return hcv;
    }

    public void setHcv(String hcv) {
        this.hcv = hcv;
    }

    public String getHcvTime() {
        return hcvTime;
    }

    public void setHcvTime(String hcvTime) {
        this.hcvTime = hcvTime;
    }

    public String getHcvResult() {
        return hcvResult;
    }

    public void setHcvResult(String hcvResult) {
        this.hcvResult = hcvResult;
    }

    public String getHcvCase() {
        return hcvCase;
    }

    public void setHcvCase(String hcvCase) {
        this.hcvCase = hcvCase;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getLaoSymptoms() {
        return laoSymptoms;
    }

    public void setLaoSymptoms(List<String> laoSymptoms) {
        this.laoSymptoms = laoSymptoms;
    }

    public List<String> getInhEndCauses() {
        return inhEndCauses;
    }

    public void setInhEndCauses(List<String> inhEndCauses) {
        this.inhEndCauses = inhEndCauses;
    }

    public List<String> getNtchSymptoms() {
        return ntchSymptoms;
    }

    public void setNtchSymptoms(List<String> ntchSymptoms) {
        this.ntchSymptoms = ntchSymptoms;
    }

    public List<String> getCotrimoxazoleEndCauses() {
        return cotrimoxazoleEndCauses;
    }

    public void setCotrimoxazoleEndCauses(List<String> cotrimoxazoleEndCauses) {
        this.cotrimoxazoleEndCauses = cotrimoxazoleEndCauses;
    }

    public List<String> getCd4Causes() {
        return cd4Causes;
    }

    public void setCd4Causes(List<String> cd4Causes) {
        this.cd4Causes = cd4Causes;
    }

    public OpcTestForm setFrom(OpcTestEntity entity) {
        OpcTestForm form = new OpcTestForm();
        form.setID(entity.getID().toString());
        form.setStatusOfTreatmentID(entity.getStatusOfTreatmentID());
        form.setLao(entity.isLao() == false ? "0" : "1");
        form.setLaoTestTime(TextUtils.formatDate(entity.getLaoTestTime(), FORMATDATE));
        form.setLaoSymptoms(entity.getLaoSymptoms());
        form.setLaoOtherSymptom(entity.getLaoOtherSymptom());
        form.setLaoResult(entity.getLaoResult() == null ? "" : entity.getLaoResult());
        form.setLaoTreatment(entity.isLaoTreatment() ? "1" : "0");
        form.setLaoStartTime(TextUtils.formatDate(entity.getLaoStartTime(), FORMATDATE));
        form.setLaoEndTime(TextUtils.formatDate(entity.getLaoEndTime(), FORMATDATE));

        form.setInh(entity.isInh() == false ? "0" : "1");
        form.setInhFromTime(TextUtils.formatDate(entity.getInhFromTime(), FORMATDATE));
        form.setInhToTime(TextUtils.formatDate(entity.getInhToTime(), FORMATDATE));

        form.setNtch(entity.isNtch() == false ? "0" : "1");
        form.setNtchOtherSymptom(entity.getNtchOtherSymptom());
        form.setCotrimoxazoleFromTime(TextUtils.formatDate(entity.getCotrimoxazoleFromTime(), FORMATDATE));
        form.setCotrimoxazoleToTime(TextUtils.formatDate(entity.getCotrimoxazoleToTime(), FORMATDATE));

        form.setCd4SampleTime(TextUtils.formatDate(entity.getCd4SampleTime(), FORMATDATE));
        form.setCd4TestTime(TextUtils.formatDate(entity.getCd4TestTime(), FORMATDATE));
        form.setCd4TestSiteID(entity.getCd4TestSiteID() == null || entity.getCd4TestSiteID() == 0 ? "" : String.valueOf(entity.getCd4TestSiteID()));
        form.setCd4TestSiteName(entity.getCd4TestSiteName());
        form.setCd4Result(entity.getCd4Result());
        form.setCd4ResultTime(TextUtils.formatDate(entity.getCd4ResultTime(), FORMATDATE));
        form.setCd4RetryTime(TextUtils.formatDate(entity.getCd4RetryTime(), FORMATDATE));

        form.setHbv(entity.isHbv() == false ? "0" : "1");
        form.setHbvTime(TextUtils.formatDate(entity.getHbvTime(), FORMATDATE));
        form.setHbvResult(entity.getHbvResult() == null ? "" : entity.getHbvResult());
        form.setHbvCase(entity.getHbvCase());

        form.setHcv(entity.isHcv() == false ? "0" : "1");
        form.setHcvTime(TextUtils.formatDate(entity.getHcvTime(), FORMATDATE));
        form.setHcvResult(entity.getHcvResult() == null ? "" : entity.getHcvResult());
        form.setHcvCase(entity.getHcvCase());

        form.setInhEndCauses(entity.getInhEndCauses());
        form.setCotrimoxazoleEndCauses(entity.getCotrimoxazoleEndCauses());
        form.setNtchSymptoms(entity.getNtchSymptoms());
        form.setCd4Causes(entity.getCd4Causes());

        form.setNote(entity.getNote());
        form.setStageID(entity.getStageID() == null ? "" : entity.getStageID().toString());
        //cập nhật 26/08/2020
        form.setSuspiciousSymptoms(entity.getSuspiciousSymptoms());
        form.setExaminationAndTest(entity.isExaminationAndTest() == false ? "0" : "1");
        form.setLaoTestDate(TextUtils.formatDate(entity.getLaoTestDate(), FORMATDATE));
        form.setLaoDiagnose(entity.getLaoDiagnose());
        form.setCotrimoxazoleOtherEndCause(entity.getCotrimoxazoleOtherEndCause());
        
        return form;
    }

    public OpcTestEntity toForm(OpcTestEntity testEntity) {
        testEntity.setStatusOfTreatmentID(statusOfTreatmentID);
        testEntity.setLao((getLao() != null && !"".equals(getLao()) && !"0".equals(getLao())));
        testEntity.setLaoTestTime(StringUtils.isEmpty(getLaoTestTime()) ? null : TextUtils.convertDate(getLaoTestTime(), FORMATDATE));
        testEntity.setLaoSymptoms(getLaoSymptoms());
        testEntity.setLaoOtherSymptom(getLaoOtherSymptom());
        testEntity.setLaoResult(getLaoResult());
        if (getLaoTreatment() != null) {
            testEntity.setLaoTreatment("1".equals(getLaoTreatment()));
        }
        testEntity.setLaoStartTime(StringUtils.isEmpty(getLaoStartTime()) ? null : TextUtils.convertDate(getLaoStartTime(), FORMATDATE));
        testEntity.setLaoEndTime(StringUtils.isEmpty(getLaoEndTime()) ? null : TextUtils.convertDate(getLaoEndTime(), FORMATDATE));

        testEntity.setInh((getInh() != null && !"".equals(getInh()) && !"0".equals(getInh())));
        testEntity.setInhFromTime(StringUtils.isEmpty(getInhFromTime()) ? null : TextUtils.convertDate(getInhFromTime(), FORMATDATE));
        testEntity.setInhToTime(StringUtils.isEmpty(getInhToTime()) ? null : TextUtils.convertDate(getInhToTime(), FORMATDATE));

        testEntity.setNtch((getNtch() != null && !"".equals(getNtch()) && !"0".equals(getNtch())));
        testEntity.setNtchOtherSymptom(getNtchOtherSymptom());
        testEntity.setCotrimoxazoleFromTime(StringUtils.isEmpty(getCotrimoxazoleFromTime()) ? null : TextUtils.convertDate(getCotrimoxazoleFromTime(), FORMATDATE));
        testEntity.setCotrimoxazoleToTime(StringUtils.isEmpty(getCotrimoxazoleToTime()) ? null : TextUtils.convertDate(getCotrimoxazoleToTime(), FORMATDATE));

        testEntity.setCd4SampleTime(StringUtils.isEmpty(getCd4SampleTime()) ? null : TextUtils.convertDate(getCd4SampleTime(), FORMATDATE));
        testEntity.setCd4TestTime(StringUtils.isEmpty(getCd4TestTime()) ? null : TextUtils.convertDate(getCd4TestTime(), FORMATDATE));
        testEntity.setCd4TestSiteID(StringUtils.isEmpty(getCd4TestSiteID()) || "null".equals(getCd4TestSiteID()) ? null : Long.valueOf(getCd4TestSiteID()));
        testEntity.setCd4TestSiteName(getCd4TestSiteName());
        testEntity.setCd4Result(getCd4Result());
        testEntity.setCd4ResultTime(StringUtils.isEmpty(getCd4ResultTime()) ? null : TextUtils.convertDate(getCd4ResultTime(), FORMATDATE));
        testEntity.setCd4RetryTime(StringUtils.isEmpty(getCd4RetryTime()) ? null : TextUtils.convertDate(getCd4RetryTime(), FORMATDATE));

        testEntity.setHbv((getHbv() != null && !"".equals(getHbv()) && !"0".equals(getHbv())));
        testEntity.setHbvTime(StringUtils.isEmpty(getHbvTime()) ? null : TextUtils.convertDate(getHbvTime(), FORMATDATE));
        testEntity.setHbvResult(getHbvResult());
        testEntity.setHbvCase(getHbvCase());

        testEntity.setHcv((getHcv() != null && !"".equals(getHcv()) && !"0".equals(getHcv())));
        testEntity.setHcvTime(StringUtils.isEmpty(getHcvTime()) ? null : TextUtils.convertDate(getHcvTime(), FORMATDATE));
        testEntity.setHcvResult(getHcvResult());
        testEntity.setHcvCase(getHcvCase());
        testEntity.setNote(getNote());

        testEntity.setInhEndCauses(getInhEndCauses());
        testEntity.setCotrimoxazoleEndCauses(getCotrimoxazoleEndCauses());
        testEntity.setNtchSymptoms(getNtchSymptoms());
        testEntity.setCd4Causes(getCd4Causes());
        testEntity.setStageID(Long.valueOf(getStageID()));
        
        testEntity.setSuspiciousSymptoms(getSuspiciousSymptoms());
        testEntity.setExaminationAndTest((getExaminationAndTest() != null && !"".equals(getExaminationAndTest()) && !"0".equals(getExaminationAndTest())));
        testEntity.setLaoTestDate(StringUtils.isEmpty(getLaoTestDate()) ? null : TextUtils.convertDate(getLaoTestDate(), FORMATDATE));
        testEntity.setLaoDiagnose(getLaoDiagnose());
        testEntity.setCotrimoxazoleOtherEndCause(getCotrimoxazoleOtherEndCause());
        
        return testEntity;
    }

}
