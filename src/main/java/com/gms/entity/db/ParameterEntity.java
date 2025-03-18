package com.gms.entity.db;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vvthanh
 */
@Entity
@Table(
        name = "PARAMETER",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"type", "code"})
        },
        indexes = {
            @Index(name = "PARAMETER_TYPE", columnList = "TYPE")
            ,@Index(name = "PARAMETER_CODE", columnList = "CODE")
            ,@Index(name = "PARAMETER_CODE_STATUS", columnList = "CODE, STATUS")
            ,@Index(name = "PARAMETER_CODE_STATUS_position", columnList = "CODE, STATUS, position")
            ,@Index(name = "PARAMETER_ATTRIBUTE_01", columnList = "ATTRIBUTE_01")
            ,@Index(name = "PARAMETER_ATTRIBUTE_02", columnList = "ATTRIBUTE_02")
        }
)
@DynamicInsert
@DynamicUpdate
public class ParameterEntity extends BaseEntity implements Serializable, Cloneable {

    /**
     * Parameter type
     */
    //Định nghĩa các tham số
    public static final String SYSTEMS_PARAMETER = "sys_parameter";
    //Hướng dẫn sử dụng trên hệ thống
    public static final String SYSTEMS_USER_MANUAL = "user-manual";
    //Quốc gia
    public static final String COUNTRY = "country";
    //Chủng tộc
    public static final String RACE = "race";
    //Giới tính
    public static final String GENDER = "gender";
    //Công việc, nghề nghiệp
    public static final String JOB = "job";
    //Dịch vụ
    public static final String SERVICE = "service";

    //Nhóm đối tượng xét nghiệm - HIV INFO: Đối tượng
    public static final String TEST_OBJECT_GROUP = "test-object-group";
    //Kết quả xét nghiệm
    public static final String TEST_RESULTS = "test-results";
    //Dịch vụ sau khi xét nghiệm
    public static final String SERVICE_AFTER_TEST = "service-after-test";
    //Đường lây nhiễm
    public static final String MODE_OF_TRANSMISSION = "modes-of-transmision";
    //Nguy cơ lây nhiễm
    public static final String RISK_BEHAVIOR = "risk-behavior";
    //Dịch vụ elog
    public static final String SERVICE_TEST = "service-test";
    //HIV Info: Nơi xét nghiệm
    public static final String PLACE_TEST = "place-test";
    //HIV Info: Cơ sở điều trị
    public static final String TREATMENT_FACILITY = "treatment-facility";
    //HIV Info: Triệu chứng
    public static final String SYSMPTOM = "symptom";
    //HIV Info: Phác đồ điều trị
    public static final String TREATMENT_REGIMEN = "treatment-regimen";
    //HIV Info: Nguyên nhân tử vong
    public static final String CAUSE_OF_DEATH = "cause-of-death";
    //HIV Info: Nơi lấy máu
    public static final String BLOOD_BASE = "blood-base";
    //HIV Info: Địa điểm giám sát
    public static final String LOCATION_MONITORING = "location-monitoring";
    //HIV Info: Sinh phẩm xét nghiệm
    public static final String BIOLOGY_PRODUCT_TEST = "biology-product-test";
    //HIV Info: Bệnh lây truyền
    public static final String COMMUNICABLE_DISEAS = "communicable-diseas";
    //HIV Info: Vị trí lấy mẫu
    public static final String LOCATION_OF_BLOOD = "location-of-blood";
    //Kết quả xét nghiệm khẳng định
    public static final String TEST_RESULTS_CONFIRM = "test-result-confirm";
    //HTC ELog: Nguồn giới thiệu REFERENT_SOURCE
    public static final String REFERENT_SOURCE = "referent-source";
    //HTC ELog: Dịch vụ sử dụng khi đến cơ sở ( sau khi chọn dịch vụ cố định) BNTrang
    public static final String FIXED_SERVICE = "fixed-service";
    //HTC ELog: Kết quả xét nghiệm nhanh nhiễm mới Asante HIV BNTrang
    public static final String ASANTE_INFECT_TEST = "asante-infect-test";
    //HTC ELog: Kết quả tư vấn chuyển gửi điều trị ARV BNTrang
    public static final String ARV_CONSULTANT_EXCHANGE_RESULT = "arv-consultant-exchange";
    //HTC ELog: Kết quả tư vấn cung cấp thông tin thực hiện xét nghiệm theo dấu bạn tình/bạn chích BNTrang
    public static final String PARTNER_INFO_PROVIDE_RESULT = "partner-provide-result";
    //Hiv info Hiện trạng cư trú - tình trạng hiện tại
    public static final String STATUS_OF_RESIDENT = "status-of-resident";
    //Hiv info loại bệnh nhân
    public static final String TYPE_OF_PATIENT = "type-of-patient";
    //Hiv info trạng thái điều trị
    public static final String STATUS_OF_TREATMENT = "status-of-treatment";
    //Trạng thái xét nghiệm khẳng định
    public static final String CONFIRM_TEST_STATUS = "confirm-test-status";
    //Trạng thái giám sát phát hiện 
    public static final String GSPH_STATUS = "gsph-status";
    //Trạng thái dự phòng lấy truyền mẹ con
    public static final String DPLTMC_STATUS = "dpltmc-status";
    //Trạng thái chuyển gừi điều trị
    public static final String THERAPY_EXCHANGE_STATUS = "therapy-exchange-status";
    //Chất lượng mẫu
    public static final String SAMPLE_QUALITY = "sample-quality";
    //Kết quả sinh phẩm
    public static final String BIO_NAME_RESULT = "bio-name-result";
    //Kết quả xét nghiệm nhiễm mới HIV
    public static final String EARLY_HIV = "early-hiv";
    //Kết quả xét nghiệm tải lượng virus
    public static final String VIRUS_LOAD = "virus-load";
    //Lựa chọn thẻ bảo hiểm
    public static final String HAS_HEALTH_INSURANCE = "health-insurance";
    // Lần xét nghiệm gần nhất
    public static final String MOST_RECENT_TEST = "most-recent-test";
    //Dự án của cơ sở
    public static final String SITE_PROJECT = "site-project";
    //Đối chiếu thông tin cá nhân
    public static final String INFO_COMPARE = "info-compare";
    //Chỉ số báo cáo
    public static final String DASHBOARD_INDICATOR = "dashboard-indicator";
    //Giai đoạn lâm sàng
    public static final String CLINICAL_STAGE = "clinical-stage";
    //Tình trạng AIDS
    public static final String AIDS_STATUS = "aids-status";
    //Hình thức thông báo
    public static final String ALERT_TYPE = "alert-type";
    //Quan hệ với bệnh nhân OPC
    public static final String SUPPORTER_RELATION = "supporter-relation";
    //Loại đăng kí của OPC
    public static final String REGISTRATION_TYPE = "registration-type";
    //Kết quả điều trị PCR
    public static final String TEST_RESULTS_PCR = "test-results-pcr";
    //Hiv info trạng thái biến động  điều trị
    public static final String STATUS_OF_CHANGE_TREATMENT = "status-of-change-treatment";
    //Lý do kết thúc (biến dộng điều trị)
    public static final String STOP_REGISTRATION_REASON = "stop-registration-reason";
    //Tuyến đăng ký bảo hiểm
    public static final String ROUTE = "route";
    // Kết luận chẩn đoán nhiễm mới
    public static final String EARLY_DIAGNOSE = "early-diagnose";
    //Link HUB DASBOARD
    public static final String LINK_PQM = "link-pqm";

    //Tham số sử dụng enum
    public static final String ANSWER = "answer"; //câu hỏi
    public static final String INSURANCE_TYPE = "insurance-type"; //loại thẻ BHYT
    public static final String INSURANCE_PAY = "insurance-pay"; //loại thanh toán BHYT
    public static final String LAO_SYMTOM = "lao-symtom"; //Biểu hiện lao
    public static final String LAO_END_CASE = "lao-end-case";
    public static final String NTCH_SYMTOM = "ntch-symtom";
    public static final String NTCH_END_CASE = "ntch-end-case";
    public static final String TREATMENT_REGINMEN_STAGE = "treatment-regimen-stage"; //bậc pháp đồ điều trị
    public static final String MEDICATION_ADHERENCE = "medication-adherence"; //Tuân thủ điều trị
    public static final String CD4_SYMTOM = "cd4-symtom"; //Biểu hiện cd4
    public static final String VIRAL_LOAD_SYMTOM = "viralload-symtom";
    public static final String HCV_SYMTOM = "hcv-symtom"; //Lý do xét nghiệm hcv
    public static final String HBV_SYMTOM = "hbv-symtom"; //Lý do xét nghiệm hbv
    public static final String ARV_END_CASE = "arv-end-case";
    public static final String ARV_REGISTRATION_STATUS = "arv-registration-status";
    public static final String CD_SERVICE = "cd-service"; // Dịch vụ tại cơ sở cố định
    public static final String CONFIRM_TYPE = "confirm-type"; // Loại hình xét nghiệm khẳng định
    public static final String EXCHANGE_SERVICE = "exchange-service"; // Dịch vụ tư vấn chuyển gửi
    public static final String RESULT_ANTI = "result-anti"; // Kết quả Xn kháng nguyên kháng thể
    public static final String RESULT_PCR_HIV = "result-pcr-hiv"; // Kết quả Xn PCR HIV bổ sung
    public static final String TEST_METHOD = "test-method"; // Phương pháp sàng lọc
    public static final String QUICK_TREATMENT = "quick-treatment"; // Điều trị nhanh
    public static final String SUSPICIOUS_SYMPTOMS = "suspicious-symptoms"; //  Lý do kết thúc NTCH
    public static final String LAO_DIAGNOSE = "lao-diagnose"; // Triệu chứng nghi ngờ
    public static final String CUSTOMER_TYPE = "customer-type"; // Loại khách hàng
    public static final String LAO_VARIABLE = "lao-variable"; // Thể Lao

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "TYPE", length = 50, nullable = false)
    private String type;

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Column(name = "VALUE", length = 500, nullable = false)
    private String value;

    @Column(name = "PARENT_ID", nullable = true)
    private Long parentID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentID")
    private Set<ParameterEntity> childrens = new HashSet<ParameterEntity>(0);

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "NOTE", length = 500, nullable = true)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", insertable = true, updatable = false, nullable = false)
    private Date createAT;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT", nullable = false)
    private Date updateAt;

    @Column(name = "CREATED_BY", insertable = true, updatable = false, nullable = false)
    private Long createdBY;

    @Column(name = "UPDATED_BY", nullable = false)
    private Long updatedBY;

    //Map
    @Column(name = "ELOG_CODE", length = 50, nullable = true)
    private String elogCode;

    @Column(name = "HIVINFO_CODE", length = 50, nullable = true)
    private String hivInfoCode;

    @Column(name = "PQM_CODE", length = 50, nullable = true)
    private String pqmCode;

    @Column(name = "ATTRIBUTE_01", length = 50, nullable = true)
    private String attribute01;

    @Column(name = "ATTRIBUTE_02", length = 50, nullable = true)
    private String attribute02;

    @Column(name = "ATTRIBUTE_03", length = 50, nullable = true)
    private String attribute03;

    @Column(name = "ATTRIBUTE_04", length = 50, nullable = true)
    private String attribute04;

    @Column(name = "ATTRIBUTE_05", length = 50, nullable = true)
    private String attribute05;

    @Column(name = "SITE_ID", nullable = true)
    private Long siteID;

    @Column(name = "PROVINCE_ID", length = 5, nullable = true)
    private String provinceID;

    public String getPqmCode() {
        return pqmCode;
    }

    public void setPqmCode(String pqmCode) {
        this.pqmCode = pqmCode;
    }

    public String getAttribute05() {
        return attribute05;
    }

    public void setAttribute05(String attribute05) {
        this.attribute05 = attribute05;
    }

    public String getAttribute03() {
        return attribute03;
    }

    public void setAttribute03(String attribute03) {
        this.attribute03 = attribute03;
    }

    public String getAttribute04() {
        return attribute04;
    }

    public void setAttribute04(String attribute04) {
        this.attribute04 = attribute04;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public Long getSiteID() {
        return siteID;
    }

    public void setSiteID(Long siteID) {
        this.siteID = siteID;
    }

    public Set<ParameterEntity> getChildrens() {
        return childrens;
    }

    public void setChildrens(Set<ParameterEntity> childrens) {
        this.childrens = childrens;
    }

    public String getAttribute02() {
        return attribute02;
    }

    public void setAttribute02(String attribute02) {
        this.attribute02 = attribute02;
    }

    public String getAttribute01() {
        return attribute01;
    }

    public void setAttribute01(String attribute01) {
        this.attribute01 = attribute01;
    }

    public String getElogCode() {
        return elogCode;
    }

    public void setElogCode(String elogCode) {
        this.elogCode = elogCode;
    }

    public String getHivInfoCode() {
        return hivInfoCode;
    }

    public void setHivInfoCode(String hivInfoCode) {
        this.hivInfoCode = hivInfoCode;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateAT() {
        return createAT;
    }

    public void setCreateAT(Date createAT) {
        this.createAT = createAT;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedBY() {
        return createdBY;
    }

    public void setCreatedBY(Long createdBY) {
        this.createdBY = createdBY;
    }

    public Long getUpdatedBY() {
        return updatedBY;
    }

    public void setUpdatedBY(Long updatedBY) {
        this.updatedBY = updatedBY;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    @Override
    public void setIgnoreSet() {
        super.setIgnoreSet();
        ignoreAttributes.add("updatedBY");
        ignoreAttributes.add("createdBY");
        ignoreAttributes.add("updateAt");
        ignoreAttributes.add("createAT");
        ignoreAttributes.add("code");
    }

    @Override
    public void setAttributeLabels() {
        super.setAttributeLabels();
        attributeLabels.put("type", "Loại dữ liệu");
        attributeLabels.put("code", "Giá trị");
        attributeLabels.put("value", "Nhãn");
        attributeLabels.put("parentID", "Tham số cha");
        attributeLabels.put("status", "Trạng thái");
        attributeLabels.put("note", "Ghi chú");
        attributeLabels.put("elogCode", "Mã tham chiếu HTC Elog");
        attributeLabels.put("hivInfoCode", "Mã tham chiếu HIV Info");
    }

    @Override
    public ParameterEntity clone() throws CloneNotSupportedException {
        return (ParameterEntity) super.clone();
    }
}
