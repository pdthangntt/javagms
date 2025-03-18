package com.gms.controller;

import com.gms.components.UrlUtils;
import com.gms.components.onesignal.Onesignal;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.View;
import com.gms.entity.constant.BaseParameterEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.service.EmailService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author vvthanh
 */
public abstract class WebController implements Serializable {

    protected static final String UTF_8 = "UTF-8";
    protected static final String FORMATDATETIME = "hh:mm:ss dd/MM/yyyy";
    protected static final String FORMATDATE = "dd/MM/yyyy";
    protected static final String FORMATDATE_SQL = "yyyy-MM-dd";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected static HashMap<String, String> parameters;

    @Autowired
    protected View view;
    @Autowired
    protected EmailService emailService;
    @Autowired
    protected SiteService siteService;
    @Autowired
    protected StaffService staffService;
    @Autowired
    protected ParameterService parameterService;
    @Autowired
    protected Gson gson;

    protected HashMap<String, String> getFinalParameter() {
        if (parameters == null) {
            parameters = new LinkedHashMap<>();
            parameters.put(ParameterEntity.SERVICE, "Dịch vụ");
            parameters.put(ParameterEntity.COUNTRY, "Quốc gia");
            parameters.put(ParameterEntity.SYSTEMS_USER_MANUAL, "Hướng dẫn sử dụng hệ thống");

            parameters.put(ParameterEntity.GENDER, "Giới tính");
            parameters.put(ParameterEntity.JOB, "Nghề nghiệp");
            parameters.put(ParameterEntity.RACE, "Dân tộc");

            parameters.put(ParameterEntity.SERVICE_TEST, "Loại dịch vụ");

            parameters.put(ParameterEntity.TEST_OBJECT_GROUP, "Nhóm đối tượng xét nghiệm");
            parameters.put(ParameterEntity.SERVICE_AFTER_TEST, "Dịch vụ sau xét nghiệm");
            parameters.put(ParameterEntity.MODE_OF_TRANSMISSION, "Đường lây nhiễm");
            parameters.put(ParameterEntity.RISK_BEHAVIOR, "Nguy cơ lây nhiễm");
            parameters.put(ParameterEntity.TEST_RESULTS, "Kết quả xét nghiệm");
            parameters.put(ParameterEntity.PLACE_TEST, "Nơi xét nghiệm");
            parameters.put(ParameterEntity.TREATMENT_FACILITY, "Cơ sở điều trị");
            parameters.put(ParameterEntity.SYSMPTOM, "Triệu chứng");
            parameters.put(ParameterEntity.TREATMENT_REGIMEN, "Phác đồ điều trị");
            parameters.put(ParameterEntity.CAUSE_OF_DEATH, "Nguyên nhân tử vong");
            parameters.put(ParameterEntity.BLOOD_BASE, "Nơi lấy máu");
            parameters.put(ParameterEntity.LOCATION_MONITORING, "Địa điểm giám sát");
            parameters.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, "Sinh phẩm xét nghiệm");
            parameters.put(ParameterEntity.COMMUNICABLE_DISEAS, "Bệnh lây truyền");
            parameters.put(ParameterEntity.LOCATION_OF_BLOOD, "Vị trí lấy mẫu");
            parameters.put(ParameterEntity.REFERENT_SOURCE, "Nguồn giới thiệu sử dụng dịch vụ TVXN");
            parameters.put(ParameterEntity.FIXED_SERVICE, "Loại dịch vụ");
            parameters.put(ParameterEntity.ASANTE_INFECT_TEST, "Kết quả XN nhanh nhiễm mới asante");
            parameters.put(ParameterEntity.ARV_CONSULTANT_EXCHANGE_RESULT, "Kết quả TV CGĐT ARV");
            parameters.put(ParameterEntity.PARTNER_INFO_PROVIDE_RESULT, "Kết quả TVXN theo dấu");
            parameters.put(ParameterEntity.TEST_RESULTS_CONFIRM, "Kết quả xét nghiệm khẳng định");
            parameters.put(ParameterEntity.STATUS_OF_RESIDENT, "Hiện trạng cư trú");
            parameters.put(ParameterEntity.TYPE_OF_PATIENT, "Loại bệnh nhân");
            parameters.put(ParameterEntity.STATUS_OF_TREATMENT, "Trạng thái điều trị");
            parameters.put(ParameterEntity.CONFIRM_TEST_STATUS, "Trạng thái XN khẳng định.");
            parameters.put(ParameterEntity.DPLTMC_STATUS, "Trạng thái DPLTMC");
            parameters.put(ParameterEntity.GSPH_STATUS, "Trạng thái giám sát phát hiện");
            parameters.put(ParameterEntity.THERAPY_EXCHANGE_STATUS, "Trạng thái chuyển gửi điều trị");
            parameters.put(ParameterEntity.SAMPLE_QUALITY, "Chất lượng mẫu");
            parameters.put(ParameterEntity.BIO_NAME_RESULT, "Kết quả XN dùng sinh phẩm");
            parameters.put(ParameterEntity.EARLY_HIV, "Kết quả xn nhiễm mới");
            parameters.put(ParameterEntity.VIRUS_LOAD, "Kết quả xn tải lượng virus");
            parameters.put(ParameterEntity.HAS_HEALTH_INSURANCE, "Có BHYT hay không");
            parameters.put(ParameterEntity.MOST_RECENT_TEST, "Thời gian làm xét nghiệm gần nhất");
            parameters.put(ParameterEntity.SITE_PROJECT, "Dự án tài trợ cho cơ sở");
            parameters.put(ParameterEntity.INFO_COMPARE, "Đối chiếu thông tin cá nhân");
            parameters.put(ParameterEntity.CLINICAL_STAGE, "Giai đoạn lâm sàng");
            parameters.put(ParameterEntity.AIDS_STATUS, "Tình trạng AIDS");
            parameters.put(ParameterEntity.ALERT_TYPE, "Hình thức thông báo");
            parameters.put(ParameterEntity.SUPPORTER_RELATION, "Quan hệ với bệnh nhân");
            parameters.put(ParameterEntity.REGISTRATION_TYPE, "Loại đăng ký");
            parameters.put(ParameterEntity.TEST_RESULTS_PCR, "Kết quả");
            parameters.put(ParameterEntity.STATUS_OF_CHANGE_TREATMENT, "Trạng thái biến động điều trị");
            parameters.put(ParameterEntity.STOP_REGISTRATION_REASON, "Lý do kết thúc"); // Lý do kết thúc đợt ( biến động điều trị)
            parameters.put(ParameterEntity.ROUTE, "Tuyến đăng ký bảo hiểm");
            parameters.put(ParameterEntity.ARV_REGISTRATION_STATUS, "Tình trạng đăng ký");
            parameters.put(ParameterEntity.EARLY_DIAGNOSE, "Kết luận chẩn đoán nhiễm mới");
            parameters.put(ParameterEntity.LINK_PQM, "Liên kết biểu đồ PQM");
        }
        return parameters;
    }

    @ModelAttribute("view")
    public View viewAttribute() {
        return view;
    }

    public String redirect(String url) {
        return "redirect:" + url;
    }

    public Logger getLogger() {
        return logger;
    }

    public LoggedUser getCurrentUser() {
        try {
            return (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Gửi thông báo đến email
     *
     * @auth vvThành
     * @param to
     * @param subject
     * @param message
     */
    public void sendEmailNotify(String to, String subject, String message) {
        if (to == null || "".equals(to)) {
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("contents", message);
        sendEmail(to, String.format("[Thông báo] %s", subject), EmailoutboxEntity.TEMPLATE_NOTIFY, variables);
    }

    /**
     * Lấy email theo dịch vụ
     *
     * @auth vvThành
     * @param siteID
     * @param service
     * @return
     */
    public String getSiteEmail(Long siteID, ServiceEnum service) {
        SiteEntity site = siteService.findOne(siteID, false, true);
        if (service == null || site == null || site.getServiceIDs() == null || site.getServiceIDs().isEmpty() || !site.getServiceIDs().contains(service.getKey())) {
            return null;
        }
        HashMap<String, String> config = parameterService.getSiteConfig(site.getID());
        String email = site.getEmail() == null || site.getEmail().equals("") ? null : site.getEmail();
        if (service.getKey().equals(ServiceEnum.HTC.getKey())) {
            email = config.getOrDefault(SiteConfigEnum.ALERT_HTC_EMAIL.getKey(), null);
        } else if (service.getKey().equals(ServiceEnum.HTC_CONFIRM.getKey())) {
            email = config.getOrDefault(SiteConfigEnum.ALERT_CONFIRM_EMAIL.getKey(), null);
        } else if (service.getKey().equals(ServiceEnum.PAC.getKey())) {
            email = config.getOrDefault(SiteConfigEnum.ALERT_GSPH_EMAIL.getKey(), null);
        } else if (service.getKey().equals(ServiceEnum.OPC.getKey())) {
            email = config.getOrDefault(SiteConfigEnum.ALERT_OPC_EMAIL.getKey(), null);
        } else if (service.getKey().equals(ServiceEnum.OPC_MANAGEMENT.getKey())) {
            email = config.getOrDefault(SiteConfigEnum.ALERT_OPC_MANAGER_EMAIL.getKey(), null);
        } else {
            email = config.getOrDefault(SiteConfigEnum.ALERT_SITE_EMAIL.getKey(), null);
        }
        return email;
    }

    /**
     * Gửi email và thông báo
     *
     * @param siteID
     * @param service
     * @param subject
     * @param message
     * @param url
     */
    public void sendNotifyToSite(Long siteID, ServiceEnum service, String subject, String message, String url) {
        String email = getSiteEmail(siteID, service);
        if (email == null) {
            return;
        }
        //Send email
        Map<String, Object> variables = new HashMap<>();
        variables.put("contents", message);
        sendEmail(email, String.format("[Thông báo] %s", subject), EmailoutboxEntity.TEMPLATE_NOTIFY, variables);
        //Send thông báo
        List<StaffEntity> staffs = staffService.findByEmail(email);
        if (staffs == null || staffs.isEmpty()) {
            return;
        }
        for (StaffEntity staff : staffs) {
            staffService.sendMessage(staff.getID(), "Thông báo", subject, url == null ? UrlUtils.backendHome() : url);
        }
    }

    /**
     * Gửi email
     *
     * @auth vvThành
     * @param to
     * @param subject
     * @param type
     * @param variables
     */
    public void sendEmail(String to, String subject, String type, Map<String, Object> variables) {
        if (to != null && !"".equals(to)) {
            emailService.send(to, subject, type, variables);
        }
    }

    /**
     * Lọc option key
     *
     * @auth vvThành
     * @param items
     * @param keys
     * @return
     */
    protected HashMap<String, String> findOptions(HashMap<String, String> items, String[] keys) {
        HashMap<String, String> option = new HashMap<>();
        if (items.getOrDefault("", null) != null) {
            option.put("", items.get(""));
        }
        List<String> skeys = Arrays.asList(keys);
        for (Map.Entry<String, String> entry : items.entrySet()) {
            if (skeys.contains(entry.getKey())) {
                option.put(entry.getKey(), entry.getValue());
            }
        }
        return option;
    }

    /**
     * convert enum to option
     *
     * @auth vvThành
     * @param options
     * @param key
     * @param enums
     * @param defaultLabel
     */
    protected void addEnumOption(HashMap<String, HashMap<String, String>> options, String key, BaseParameterEnum[] enums, String defaultLabel) {
        HashMap<String, String> map = new LinkedHashMap<>();
        if (defaultLabel != null) {
            map.put("", defaultLabel);
        }
        for (BaseParameterEnum item : enums) {
            map.put(String.valueOf(item.getKey()), item.getLabel());
        }
        options.put(key, map);
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải PAC không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isPAC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getType() == SiteEntity.TYPE_PROVINCE
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.PAC.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải Trung tâm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_DISTRICT
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TTYT.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải Trạm y tế không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isTYT() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getType() == SiteEntity.TYPE_WARD
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.TYT.getKey());
    }

    /**
     * Kiểm tra cơ sở hiện tại có phải VAAC không
     *
     * @auth vvThành
     * @return
     */
    protected boolean isVAAC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getType() == SiteEntity.TYPE_VAAC
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.VAAC.getKey());
    }

    /**
     * Kiểm tra khoa điều trị
     *
     * @return
     */
    protected boolean isOpcManager() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.OPC_MANAGEMENT.getKey());
    }

    /**
     * Kiểm tra cơ sở điều trị
     *
     * @return
     */
    protected boolean isOPC() {
        LoggedUser currentUser = getCurrentUser();
        return currentUser.getSite().getProvinceID() != null
                && !currentUser.getSite().getProvinceID().equals("")
                && currentUser.getSite().getServiceIDs().contains(ServiceEnum.OPC.getKey());
    }
}
