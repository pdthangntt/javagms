package com.gms.controller.service;

import com.gms.components.QLNNUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.components.rabbit.RabbitMQSender;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.CdServiceEnum;
import com.gms.entity.constant.ConfirmFeedbackEnum;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.ConfirmTypeEnum;
import com.gms.entity.constant.FeedbackStatusEnum;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcConfirmLogEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.HtcConfirmReceivedForm;
import com.gms.entity.form.HtcConfirmUpdateResultForm;
import com.gms.entity.rabbit.HtcConfirmMessage;
import com.gms.service.CommonService;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author pdThang
 */
@RestController
public class HtcConfirmController extends BaseController {

    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private RabbitMQSender rabbitSender;
    @Autowired
    private CommonService commonService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    private QLNNUtils qlnnUtils;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
        if (siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).equals("")) {
            options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).split(",")));
        }

        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        return options;
    }

    /**
     * Lấy thông tin mapping của cơ sở hiện tại với thông tin quản lý người
     * nhiẽm
     *
     * @auth TrangBN
     * @return
     */
    private Map<String, Map<Long, String>> getSiteOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.PLACE_TEST);

        Map<String, Map<Long, String>> options = new HashMap<>();
        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<Long, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getSiteID() == null || entity.getSiteID().equals(Long.valueOf("0"))) {
                continue;
            }
            options.get(entity.getType()).put(entity.getSiteID(), entity.getCode());
        }
        return options;
    }

    /**
     * Trả kết quả cho cơ sở gửi mẫu
     *
     * @author pdThang
     * @param oids
     * @return
     */
    @RequestMapping(value = "/htc-confirm/redirect.json", method = RequestMethod.GET)
    public Response<?> actionrRedirect(@RequestParam(name = "oid") String oids) {
        List<Long> confirmIds = new ArrayList<>();
        LoggedUser currentUser = getCurrentUser();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        Map<String, Object> data = new HashMap();
        List<HtcConfirmEntity> datas = htcConfirmService.findAllByIdOrderByConfirmTime(confirmIds);
        int i = 0;
        for (HtcConfirmEntity item1 : datas) {
            if (item1.getResultsID() != null && !"".equals(item1.getResultsID()) && item1.getResultsReturnTime() == null) {
                i += 1;
            }
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        HashMap<String, HashMap<String, String>> options = getOptions();
        String siteSent = "siteSent";
        options.put(siteSent, new HashMap<String, String>());
        for (SiteEntity site : sites) {
            options.get(siteSent).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("i", i);
        data.put("options", options);
        data.put("entities", datas);
        return new Response<>(true, "Xác nhận trả kết quả cho cơ sở gửi mẫu thành công", data);
    }

    @RequestMapping(value = "/htc-confirm/transfer.json", method = RequestMethod.GET)
    public Response<?> actionTransfer(@RequestParam(name = "oid") String oids) {
        List<Long> confirmIds = new ArrayList<>();
        LoggedUser currentUser = getCurrentUser();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        Map<String, Object> data = new HashMap();
        List<HtcConfirmEntity> datas = htcConfirmService.findAllByIdOrderByConfirmTime(confirmIds);
        int i = 0;
        for (HtcConfirmEntity item1 : datas) {
            if (item1.getResultsID() != null && !"".equals(item1.getResultsID()) && "2".equals(item1.getResultsID()) && item1.getPacSentDate() == null) {
                i += 1;
            }
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        HashMap<String, HashMap<String, String>> options = getOptions();
        String siteSent = "siteSent";
        options.put(siteSent, new HashMap<String, String>());
        for (SiteEntity site : sites) {
            options.get(siteSent).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("i", i);
        data.put("options", options);
        data.put("entities", datas);
        return new Response<>(true, "", data);
    }

    /**
     * Trả kết quả cho cơ sở gửi mẫu
     *
     * @author pdThang
     * @param oids
     * @return
     */
    @RequestMapping(value = "/htc-confirm/confirm.json", method = RequestMethod.GET)
    public Response<?> actionrConfirm(@RequestParam(name = "oid") String oids) {
        List<Long> confirmIds = new ArrayList<>();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        Date currentDate = new Date();
        List<HtcConfirmEntity> data = htcConfirmService.findAllByIds(confirmIds);
        List<HtcConfirmEntity> result = new ArrayList<>();
        List<HtcVisitEntity> resultHtc = new ArrayList<>();
        HtcVisitEntity htcEntity;
        for (HtcConfirmEntity entity : data) {
            if (entity.getResultsID() != null && !"".equals(entity.getResultsID()) && entity.getResultsReturnTime() == null) {
                entity.setResultsReturnTime(currentDate);
                entity.setUpdatedAt(currentDate);
                result.add(entity);

                // Lấy thông tin khách hàng bên HTC
                htcEntity = htcVisitService.findByCodeAndSite(entity.getSourceID(), entity.getSourceSiteID(), false);

                if (htcEntity != null ) {
                    if (StringUtils.isNotEmpty(htcEntity.getSiteConfirmTest()) && !htcEntity.getSiteConfirmTest().equals(String.valueOf(entity.getSiteID()))) {
                        continue;
                    }
                    htcEntity.setFeedbackStatus(FeedbackStatusEnum.NOT.getKey());
                    htcEntity.setConfirmResutl(false);
                    //thêm ngày 12/07/2020
                    htcEntity.setEarlyBioName(entity.getEarlyBioName());
                    htcEntity.setEarlyHivDate(entity.getEarlyHivDate());
                    htcEntity.setEarlyHiv(entity.getEarlyHiv());
                    htcEntity.setVirusLoadDate(entity.getVirusLoadDate());
                    htcEntity.setVirusLoadNumber(entity.getVirusLoadNumber());
                    htcEntity.setVirusLoad(entity.getVirusLoad());
                    htcEntity.setEarlyDiagnose(entity.getEarlyDiagnose());
                    htcEntity.setSourceCreated("1"); // có bản ghi sàng lọc và chuyển gửi bình thường
                    if (StringUtils.isEmpty(htcEntity.getConfirmTestStatus())) {
                        htcEntity.setConfirmTestStatus("2");
                        htcEntity.setSourceCreated("2"); // TH 2 Có bản ghi sàng lọc nhưng chưa gửi 
                        if (StringUtils.isEmpty(htcEntity.getSiteConfirmTest())) {
                            htcEntity.setSiteConfirmTest(String.valueOf(entity.getSiteID()));
                            htcEntity.setIsAgreeTest(true);
                        }
                    }
                    
                    resultHtc.add(htcEntity);
                }
                
                // Không tìm thấy mã sàng lọc tại cơ sở
                if (htcEntity == null) {
                    htcEntity = new HtcVisitEntity();

                    // Thêm các thông tin HTC
                    htcEntity.setFeedbackStatus(FeedbackStatusEnum.NOT.getKey());
                    htcEntity.setConfirmResutl(false);
                    htcEntity.setAdvisoryeTime(entity.getSourceReceiveSampleTime());
                    htcEntity.setServiceID(entity.getServiceID());
                    htcEntity.setCdService( StringUtils.isNotEmpty(entity.getServiceID()) && entity.getServiceID().equals(ServiceTestEnum.CD.getKey()) ? CdServiceEnum.SANG_LOC.getKey(): "");
                    htcEntity.setCode(entity.getSourceID());
                    htcEntity.setPatientName(entity.getFullname());
                    htcEntity.setYearOfBirth(entity.getYear() == null ? "" : String.valueOf(entity.getYear()));
                    htcEntity.setGenderID(entity.getGenderID());
                    htcEntity.setRaceID(RaceEnum.KINH.getKey());
                    htcEntity.setPatientID(entity.getPatientID());
                    htcEntity.setPatientIDAuthen("3");
                    htcEntity.setHasHealthInsurance(entity.getInsurance());
                    htcEntity.setHealthInsuranceNo(entity.getInsuranceNo());
                    htcEntity.setPermanentAddress(entity.getAddress());
                    htcEntity.setPermanentAddressStreet(entity.getPermanentAddressStreet());
                    htcEntity.setPermanentAddressGroup(entity.getPermanentAddressGroup());
                    htcEntity.setPermanentProvinceID(entity.getProvinceID());
                    htcEntity.setPermanentDistrictID(entity.getDistrictID());
                    htcEntity.setPermanentWardID(entity.getWardID());
                    htcEntity.setCurrentAddress(entity.getCurrentAddress());
                    htcEntity.setCurrentAddressStreet(entity.getCurrentAddressStreet());
                    htcEntity.setCurrentAddressGroup(entity.getCurrentAddressGroup());
                    htcEntity.setCurrentProvinceID(entity.getCurrentProvinceID());
                    htcEntity.setCurrentDistrictID(entity.getCurrentDistrictID());
                    htcEntity.setCurrentWardID(entity.getCurrentWardID());
                    htcEntity.setObjectGroupID(entity.getObjectGroupID());
                    htcEntity.setModeOfTransmission(entity.getModeOfTransmission());
                    htcEntity.setIsAgreePreTest("1");
                    htcEntity.setPreTestTime(entity.getSourceReceiveSampleTime());
                    
                    htcEntity.setTestResultsID(entity.getTestResultsID());
                    htcEntity.setIsAgreeTest(true);
                    htcEntity.setTestNoFixSite(htcEntity.getCode());
                    htcEntity.setSiteConfirmTest(String.valueOf(entity.getSiteID()));
                    htcEntity.setConfirmType(ConfirmTypeEnum.SEROUS.getKey());
                    htcEntity.setEarlyBioName(entity.getEarlyBioName());
                    htcEntity.setEarlyHivDate(entity.getEarlyHivDate());
                    htcEntity.setEarlyHiv(entity.getEarlyHiv());
                    htcEntity.setVirusLoadDate(entity.getVirusLoadDate());
                    htcEntity.setVirusLoadNumber(entity.getVirusLoadNumber());
                    htcEntity.setVirusLoad(entity.getVirusLoad());
                    htcEntity.setConfirmTestStatus("2");
                    htcEntity.setEarlyDiagnose(entity.getEarlyDiagnose());
                    htcEntity.setSiteID(entity.getSourceSiteID());
                    htcEntity.setSourceCreated("3");
                    htcEntity.setCreateAT(new Date());
                    htcEntity.setUpdateAt(new Date());
                    htcEntity.setCreatedBY(0L);
                    htcEntity.setUpdatedBY(0L);
                    htcEntity.setStaffBeforeTestID("Chưa xác định");
                    if (htcEntity.getTestResultsID().equals(TestResultEnum.CO_PHAN_UNG.getKey()) || htcEntity.getTestResultsID().equals(TestResultEnum.KHONG_RO.getKey())) {
                        htcEntity.setTestMethod( "1");
                        htcEntity.setConfirmType("2");
                    }
                    if (htcEntity.getTestResultsID().equals(TestResultEnum.ONLY_NOTICE.getKey())) {
                        htcEntity.setTestMethod( "2");
                        htcEntity.setConfirmType("1");
                    }

                    resultHtc.add(htcEntity);
                }
                
            }
        }

        // Cập nhật khẳng định
        List<HtcConfirmEntity> saveAll = htcConfirmService.saveAll(result);

        // Cập nhật sàng lọc
        if (resultHtc.size() > 0) {
            htcVisitService.saveAll(resultHtc);
        }

        //rabbit job update confirm status from htc
        HtcConfirmMessage message;
        for (HtcConfirmEntity entity : saveAll) {
            HtcVisitEntity visit = htcVisitService.findByCodeAndSite(entity.getSourceID(), entity.getSourceSiteID(), true);
            if (visit != null) {
                Set<Long> ids = new HashSet<>();
                ids.add(visit.getCreatedBY());
                ids.add(visit.getUpdatedBY());
                staffService.sendMessage(ids, String.format("Đã xác nhận trả kết quả KH %s ", entity.getSourceID()), String.format("Mã khách hàng: %s", entity.getSourceID()), String.format("%s?code=%s", UrlUtils.htcIndex(), visit.getCode()));
            }

            String email = parameterService.getSiteConfig(entity.getSourceSiteID(), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());
            if (email != null || !"".equals(email)) {
                sendEmailNotify(email, String.format("%s đã xác nhận trả kết quả thông tin khách hàng mã %s ", getCurrentUser().getSite().getName(), entity.getSourceID()), String.format("Khách hàng: %s - %s được xác nhận trả kết quả ngày %s", entity.getSourceID(), entity.getFullname(), TextUtils.formatDate(new Date(), FORMATDATE)));
            }

            htcConfirmService.log(entity.getID(), "Xác nhận trả kết quả cho cơ sở gửi mẫu");

            message = new HtcConfirmMessage();
            message.setConfirm(entity);
            rabbitSender.sendVisit(message);
        }

        return new Response<>(true, "Xác nhận trả kết quả cho cơ sở gửi mẫu thành công", data);
    }

    @RequestMapping(value = "/htc-confirm/transfer-confirm.json", method = RequestMethod.GET)
    public Response<?> actionTransferConfirm(@RequestParam(name = "oid") String oids) throws Exception {
        List<Long> confirmIds = new ArrayList<>();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        List<HtcConfirmEntity> data = htcConfirmService.findAllByIds(confirmIds);
        if (data == null || data.isEmpty()) {
            return new Response<>(false, "Không tìm thấy khách hàng", data);
        }

        // bắt đầu
        Map<String, Map<Long, String>> options = getSiteOptions();

        for (HtcConfirmEntity confirm : data) {
            if (confirm.getResultsID() != null && !"".equals(confirm.getResultsID()) && "2".equals(confirm.getResultsID()) && confirm.getPacSentDate() == null) {
                PacPatientInfoEntity patient = qlnnUtils.confirm(confirm);

                //Lưu gsph API
                confirm.setPacSentDate(new Date());
                confirm.setPacPatientID(patient.getID());
                htcConfirmService.save(confirm);
                htcConfirmService.log(confirm.getID(), "Chuyển gửi giám sát phát hiện: #" + patient.getID());

                //Gửi email
                List<SiteEntity> pacSite = siteService.findByServiceAndProvince(ServiceEnum.PAC.getKey(), getCurrentUser().getSite().getProvinceID());
                Map<String, Object> variables = new HashMap<>();
                variables.put("code", confirm.getCode());
                variables.put("sentMessage", "Chuyển gửi thông tin khách hàng qua quản lý người nhiễm");
                variables.put("pacSentDate", TextUtils.formatDate(confirm.getPacSentDate(), "dd/MM/yyyy"));
                variables.put("sourceSentSite", getCurrentUser().getSite().getName());

                for (SiteEntity siteEntity : pacSite) {
                    String email = parameterService.getSiteConfig(siteEntity.getID(), SiteConfigEnum.ALERT_GSPH_EMAIL.getKey());
                    if (email != null && !"".equals(email)) {
                        sendEmail(email, "[Thông báo] Chuyển gửi GSPH khách hàng(" + confirm.getCode() + ")", EmailoutboxEntity.TEMPLATE_SENT_PAC, variables);
                    }
                }
            }
        }

        return new Response<>(true, "Khách hàng đã được chuyển sang báo cáo giám sát phát hiện thành công", data);
    }

    /**
     * Tiếp nhận khach hang khang dinh
     *
     * @author pdThang
     * @param oids
     * @return
     */
    @RequestMapping(value = "/htc-confirm/received.json", method = RequestMethod.GET)
    public Response<?> actionrReceived(@RequestParam(name = "oid") String oids) {
        List<Long> confirmIds = new ArrayList<>();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            confirmIds.add(Long.parseLong(string));
        }
        List<HtcConfirmEntity> data = htcConfirmService.findAllByIds(confirmIds);
        List<HtcConfirmEntity> result = new ArrayList<>();
        for (HtcConfirmEntity entity : data) {
            if (entity.getAcceptDate() == null && StringUtils.isEmpty(entity.getResultsID())) {
                entity.setSampleReceiveTime(new Date());
                entity.setAcceptDate(new Date());
                entity.setUpdatedAt(new Date());
                // Gán mã KĐ bằng mã kh sàng lọc, nếu đã tồn tại tự tăng
//                entity.setCode(htcConfirmService.existCodeAndSiteIDConfirm(entity.getSourceID(), entity.getSiteID()) ? htcConfirmService.getCode() : entity.getSourceID());
            }
            result.add(entity);
        }
        List<HtcConfirmEntity> saveAll = htcConfirmService.saveAll(result);
        HtcConfirmMessage message;
        for (HtcConfirmEntity entity : saveAll) {
            HtcVisitEntity visit = htcVisitService.findByCodeAndSite(entity.getSourceID(), entity.getSourceSiteID(), true);
            Set<Long> ids = new HashSet<>();
            ids.add(visit.getCreatedBY());
            ids.add(visit.getUpdatedBY());
            String email = parameterService.getSiteConfig(entity.getSourceSiteID(), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());
            if (email != null || !"".equals(email)) {
                sendEmailNotify(email, String.format("Cơ sở xét nghiệm khẳng định %s tiếp nhận khách hàng mã %s ", getCurrentUser().getSite().getName(), entity.getSourceID()), String.format("Khách hàng: %s - %s được tiếp nhận vào ngày %s", entity.getSourceID(), entity.getFullname(), TextUtils.formatDate(new Date(), FORMATDATE)));
            }
            staffService.sendMessage(ids, String.format("%s đã tiếp nhận KH ", getCurrentUser().getSite().getName()), String.format("Mã khách hàng: %s", entity.getSourceID()), String.format("%s?code=%s", UrlUtils.htcIndex(), entity.getSourceID()));
            htcConfirmService.log(entity.getID(), "Tiếp nhận khách hàng");

            HtcVisitEntity htcEntity = htcVisitService.findByCodeAndSite(entity.getSourceID(), entity.getSourceSiteID(), false);

            // Cập nhật KH trùng code mà chưa có trạng thái khẳng định
            if (htcEntity.getConfirmTestStatus() != null && StringUtils.isNotEmpty(htcEntity.getConfirmTestStatus())) {
                //Rabbit job tiếp nhận khách hàng
                message = new HtcConfirmMessage();
                message.setConfirm(entity);
                rabbitSender.sendVisit(message);
            }
        }
        return new Response<>(true, "Khách hàng đã được tiếp nhận thành công", data);
    }

    @RequestMapping(value = "/htc-confirm/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oid) {
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmEntity confirm = htcConfirmService.findOne(Long.parseLong(oid));
        if (confirm == null || confirm.getRemove() || !confirm.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();

        //DSNAnh - Lấy DS cơ sở
        String siteList = "siteList";
        options.put(siteList, new HashMap<String, String>());
        List<HtcConfirmEntity> confirms = htcConfirmService.findAll();
        Set<Long> siteIDs = new HashSet<>();
        if (confirms != null && confirms.size() > 0) {
            siteIDs.addAll(CollectionUtils.collect(confirms, TransformerUtils.invokerTransformer("getSiteID")));
            List<SiteEntity> siteModels = siteService.findByIDs(siteIDs);
            for (SiteEntity site : siteModels) {
                options.get(siteList).put(String.valueOf(site.getID()), site.getName());
            }
        }

        data.put("staffName", getCurrentUser().getUser().getName());
        data.put("options", options);
        data.put("model", confirm);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/htc-confirm/update-result.json", method = RequestMethod.POST)
    public Response<?> actionUpdateResult(@RequestParam(name = "oid") String oid,
            @RequestBody HtcConfirmUpdateResultForm form) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();
        HtcConfirmEntity confirm = htcConfirmService.findOne(Long.parseLong(oid));
        if (confirm == null || confirm.getRemove() || !confirm.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        if (!form.getConfirmTime().contains("/") && form.getConfirmTime() != null && !"".equals(form.getConfirmTime())) {
            form.setConfirmTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getConfirmTime()));
        }
        if (!form.getEarlyHivDate().contains("/") && form.getEarlyHivDate() != null && !"".equals(form.getEarlyHivDate())) {
            form.setEarlyHivDate(form.getEarlyHivDate().substring(0, 2) + "/" + form.getEarlyHivDate().substring(2, 4) + "/" + form.getEarlyHivDate().substring(4, 8));
        }
        System.out.println("xx " + form.getEarlyHivDate());
        if (!form.getVirusLoadDate().contains("/") && form.getVirusLoadDate() != null && !"".equals(form.getVirusLoadDate())) {
            form.setVirusLoadDate(form.getVirusLoadDate().substring(0, 2) + "/" + form.getVirusLoadDate().substring(2, 4) + "/" + form.getVirusLoadDate().substring(4, 8));
        }
        if (!form.getSampleReceiveTime().contains("/") && form.getSampleReceiveTime() != null && !"".equals(form.getSampleReceiveTime())) {
            form.setSampleReceiveTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getSampleReceiveTime()));
        }
        if (!form.getSourceSampleDate().contains("/") && form.getSourceSampleDate() != null && !"".equals(form.getSourceSampleDate())) {
            form.setSourceSampleDate(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getSourceSampleDate()));
        }
        if (!"".equals(form.getBioNameResult1()) && "".equals(form.getBioName1())) {
            errors.put("bioName1", "Chọn tên sinh phẩm 1");
        }
        if (!"".equals(form.getBioNameResult2()) && "".equals(form.getBioName2())) {
            errors.put("bioName2", "Chọn tên sinh phẩm 2");
        }
        if (!"".equals(form.getBioNameResult3()) && "".equals(form.getBioName3())) {
            errors.put("bioName3", "Chọn tên sinh phẩm 3");
        }

        if ("".equals(form.getBioNameResult1()) && !"".equals(form.getBioName1())) {
            errors.put("bioNameResult1", "Chọn kết quả XN sinh phẩm 1");
        }
        if ("".equals(form.getBioNameResult2()) && !"".equals(form.getBioName2())) {
            errors.put("bioNameResult2", "Chọn kết quả XN sinh phẩm 2");
        }
        if ("".equals(form.getBioNameResult3()) && !"".equals(form.getBioName3())) {
            errors.put("bioNameResult3", "Chọn kết quả XN sinh phẩm 3");
        }

        if (form.getTestStaffId() == null || "".equals(form.getTestStaffId())) {
            errors.put("testStaffId", "Tên cán bộ XN không được để trống");
        }
        if (form.getCode() == null || "".equals(form.getCode())) {
            errors.put("code", "Mã XN khẳng định không được để trống");
        }
        if (form.getCode() != null && !"".equals(form.getCode()) && htcConfirmService.existCodeAndSiteID(form.getCode(), confirm.getSiteID(), confirm.getID())) {
            errors.put("code", "Mã XN khẳng định đã tồn tại");
        }
        if (form.getConfirmTime() == null || "".equals(form.getConfirmTime())) {
            errors.put("confirmTime", "Ngày XN khẳng định không được để trống");
        }
        if (StringUtils.isEmpty(form.getEarlyHivDate()) && StringUtils.isNotEmpty(form.getEarlyHiv())) {
            errors.put("earlyHivDate", "Ngày XN nhiễm mới HIV không được để trống");
        }
        if (StringUtils.isEmpty(form.getVirusLoadDate()) && StringUtils.isNotEmpty(form.getVirusLoad())) {
            errors.put("virusLoadDate", "Ngày XN tải lượng virus không được để trống");
        }
        if (StringUtils.isEmpty(form.getSampleReceiveTime())) {
            errors.put("sampleReceiveTime", "Ngày nhận mẫu không được để trống");
        }
        if (StringUtils.isEmpty(form.getSourceSampleDate())) {
            errors.put("sourceSampleDate", "Ngày gửi mẫu không được để trống");
        }
        if (form.getResultsID() == null || "".equals(form.getResultsID())) {
            errors.put("resultsID", "Kết quả XN khẳng định không được để trống");
        }

        if ((!"".equals(form.getBioName1()) && form.getBioName1() != null)
                && (!"".equals(form.getBioName2()) && form.getBioName2() != null)
                && (!"".equals(form.getBioName3()) && form.getBioName3() != null)) {

            if (form.getBioName1().equals(form.getBioName2())) {
                errors.put("bioName1", "Tên sinh phẩm không được trùng");
                errors.put("bioName2", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName2().equals(form.getBioName3())) {
                errors.put("bioName2", "Tên sinh phẩm không được trùng");
                errors.put("bioName3", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName3().equals(form.getBioName1())) {
                errors.put("bioName1", "Tên sinh phẩm không được trùng");
                errors.put("bioName3", "Tên sinh phẩm không được trùng");
            }
            if (form.getBioName1().equals(form.getBioName2()) && form.getBioName2().equals(form.getBioName3()) && form.getBioName3().equals(form.getBioName1())) {
                errors.put("bioName1", "Tên sinh phẩm không được trùng");
                errors.put("bioName2", "Tên sinh phẩm không được trùng");
                errors.put("bioName3", "Tên sinh phẩm không được trùng");
            }
        }
        if ("1".equals(form.getResultsID()) && !("0".equals(form.getBioNameResult1()) || "0".equals(form.getBioNameResult2()) || "0".equals(form.getBioNameResult3()))) {
            errors.put("resultsID", "Phải có tối thiểu một kết quả XN sinh phẩm âm tính");
        }
        if ("2".equals(form.getResultsID())) {
            if (!"1".equals(form.getBioNameResult1())) {
                errors.put("bioNameResult1", "Kết quả XN sinh phẩm phải là dương tính");
            }
            if (!"1".equals(form.getBioNameResult2())) {
                errors.put("bioNameResult2", "Kết quả XN sinh phẩm phải là dương tính");
            }
            if (!"1".equals(form.getBioNameResult3())) {
                errors.put("bioNameResult3", "Kết quả XN sinh phẩm phải là dương tính");
            }
        }
        if ("".equals(form.getResultsID()) && !("".equals(form.getBioNameResult1()) && "".equals(form.getBioNameResult2()) && "".equals(form.getBioNameResult3()))) {
            if ("1".equals(form.getBioNameResult1()) && "1".equals(form.getBioNameResult2()) && "1".equals(form.getBioNameResult3())) {
                errors.put("resultsID", "Kết quả XN khẳng định phải là dương tính");
            }
        }

        if (StringUtils.isNotEmpty(form.getVirusLoadNumber())) {
            if (form.getVirusLoadNumber().length() > 9) {
                errors.put("virusLoadNumber", "Nồng độ virus không được quá 9 chữ số");
            } else {
                try {
                    Integer.valueOf(form.getVirusLoadNumber());
                    if (Integer.valueOf(form.getVirusLoadNumber()) < 0) {
                        errors.put("virusLoadNumber", "Nồng độ virus phải nhập số nguyên dương");
                    } else if (Integer.valueOf(form.getVirusLoadNumber()) > 999999999) {
                        errors.put("virusLoadNumber", "Nồng độ virus không được quá 9 chữ số");
                    }
                } catch (Exception e) {
                    errors.put("virusLoadNumber", "Nồng độ virus phải nhập số nguyên dương");
                }
            }
        }

        try {
            if (StringUtils.isEmpty(form.getEarlyHiv()) && StringUtils.isNotEmpty(form.getEarlyHivDate())) {
                errors.put("earlyHiv", "KQXN nhiễm mới HIV không được để trống");
            }
            if (StringUtils.isEmpty(form.getVirusLoad()) && StringUtils.isNotEmpty(form.getVirusLoadDate())) {
                errors.put("virusLoad", "KQXN tải lượng virus không được để trống");
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getEarlyHivDate())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date earlyHivDate = sdfrmt.parse(form.getEarlyHivDate());
                if (confirmTime.compareTo(earlyHivDate) > 0) {
                    errors.put("earlyHivDate", "Ngày XN nhiễm mới HIV không được nhỏ hơn ngày XN khẳng định");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getVirusLoadDate())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date virusLoadDate = sdfrmt.parse(form.getVirusLoadDate());
                if (confirmTime.compareTo(virusLoadDate) > 0) {
                    errors.put("virusLoadDate", "Ngày XN tải lượng virus không được nhỏ hơn ngày XN khẳng định");
                }
            }
            if (StringUtils.isNotEmpty(form.getVirusLoadDate())) {
                try {
                    Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
                    if (now.compareTo(sdfrmt.parse(form.getVirusLoadDate())) < 0) {
                        errors.put("virusLoadDate", "Ngày XN tải lượng virus không được lớn hơn ngày hiện tại");
                    }
                    if (!isThisDateValid(form.getVirusLoadDate())) {
                        errors.put("virusLoadDate", "Định dạng ngày không đúng dd/mm/yyyy");
                    }
                } catch (ParseException ex) {
                    errors.put("virusLoadDate", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }
            if (StringUtils.isNotEmpty(form.getEarlyHivDate())) {
                try {
                    Date now = sdfrmt.parse(TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
                    if (now.compareTo(sdfrmt.parse(form.getEarlyHivDate())) < 0) {
                        errors.put("earlyHivDate", "Ngày XN nhiễm mới HIV không được lớn hơn ngày hiện tại");
                    }
                    if (!isThisDateValid(form.getEarlyHivDate())) {
                        errors.put("earlyHivDate", "Định dạng ngày không đúng dd/mm/yyyy");
                    }
                } catch (ParseException ex) {
                    errors.put("earlyHivDate", "Định dạng ngày không đúng dd/mm/yyyy");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date today = new Date();
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(today) > 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được sau ngày hiện tại");
                }
            }

            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getSampleReceiveTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date sampleReceiveTime = sdfrmt.parse(form.getSampleReceiveTime());
                if (confirmTime.compareTo(sampleReceiveTime) < 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được trước ngày nhận mẫu : " + TextUtils.formatDate(confirm.getSampleReceiveTime(), FORMATDATE));
                }
            }
            if (StringUtils.isNotEmpty(form.getSourceSampleDate())) {
                Date today = new Date();
                Date sourceSampleDate = sdfrmt.parse(form.getSourceSampleDate());
                if (sourceSampleDate.compareTo(today) > 0) {
                    errors.put("sourceSampleDate", "Ngày gửi mẫu không được sau ngày hiện tại. ");
                }
            }
            if (StringUtils.isNotEmpty(form.getSampleReceiveTime())) {
                Date today = new Date();
                Date sampleReceiveTime = sdfrmt.parse(form.getSampleReceiveTime());
                if (sampleReceiveTime.compareTo(today) > 0) {
                    errors.put("sampleReceiveTime", "Ngày nhận mẫu không được sau ngày hiện tại. ");
                }
            }
            if (StringUtils.isNotEmpty(form.getSourceSampleDate()) && StringUtils.isNotEmpty(form.getSampleReceiveTime())) {
                Date sampleReceiveTime = sdfrmt.parse(form.getSampleReceiveTime());
                Date sourceSampleDate = sdfrmt.parse(form.getSourceSampleDate());
                Date sourceReceiveSampleTime = sdfrmt.parse(TextUtils.formatDate(confirm.getSourceReceiveSampleTime(), FORMATDATE));
                if (sampleReceiveTime.compareTo(sourceSampleDate) < 0) {
                    errors.put("sourceSampleDate", "Ngày gửi mẫu không được sau ngày nhận mẫu. ");
                }
                if (sourceSampleDate.compareTo(sourceReceiveSampleTime) < 0) {
                    errors.put("sourceSampleDate", "Ngày gửi mẫu không được trước ngày lấy mẫu (" + TextUtils.formatDate(confirm.getSourceReceiveSampleTime(), FORMATDATE) + ")");
                }
            }
        } catch (Exception ex) {

        }
        if (StringUtils.isNotEmpty(form.getSampleSaveCode()) && form.getSampleSaveCode().length() > 20) {
            errors.put("sampleSaveCode", "Mã số lưu mẫu không quá 20 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getSampleSaveCode()) && !TextUtils.removeDiacritical(form.getSampleSaveCode().trim()).matches(RegexpEnum.CODE_CONFIRM)) {
            errors.put("sampleSaveCode", "Mã không đúng định dạng, bao gồm số, chữ, \"-\" và \"/\"");
        }
        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }
        confirm.setBioName1(form.getBioName1());
        confirm.setBioName2(form.getBioName2());
        confirm.setBioName3(form.getBioName3());
        confirm.setBioNameResult1(form.getBioNameResult1());
        confirm.setBioNameResult2(form.getBioNameResult2());
        confirm.setBioNameResult3(form.getBioNameResult3());
        confirm.setTestStaffId(form.getTestStaffId());
        confirm.setCode(form.getCode());
        confirm.setConfirmTime(TextUtils.convertDate(form.getConfirmTime(), FORMATDATE));
        confirm.setResultsID(form.getResultsID());
        confirm.setEarlyHiv(form.getEarlyHiv());
        confirm.setVirusLoad(form.getVirusLoad());
        if (confirm.getObjectGroupID().equals("19")) {
            confirm.setSampleSentSource(form.getSampleSentSource());
        }

        confirm.setEarlyHivDate(TextUtils.convertDate(form.getEarlyHivDate(), FORMATDATE));
        confirm.setVirusLoadDate(TextUtils.convertDate(form.getVirusLoadDate(), FORMATDATE));
        confirm.setSampleReceiveTime(TextUtils.convertDate(form.getSampleReceiveTime(), FORMATDATE));
        confirm.setAcceptDate(TextUtils.convertDate(form.getSampleReceiveTime(), FORMATDATE));
        confirm.setSourceSampleDate(TextUtils.convertDate(form.getSourceSampleDate(), FORMATDATE));
        confirm.setSampleSaveCode(form.getSampleSaveCode());
        confirm.setVirusLoadNumber(form.getVirusLoadNumber());
        confirm.setEarlyBioName(form.getEarlyBioName());
        confirm.setEarlyDiagnose(form.getEarlyDiagnose());

        // Tự động set ngay làm snh phẩm xét nghiệm
        confirm.setFirstBioDate(StringUtils.isNotEmpty(form.getBioName1()) ? (confirm.getFirstBioDate() == null ? confirm.getConfirmTime() : confirm.getFirstBioDate()) : null);
        confirm.setSecondBioDate(StringUtils.isNotEmpty(form.getBioName2()) ? (confirm.getSecondBioDate() == null ? confirm.getConfirmTime() : confirm.getSecondBioDate()) : null);
        confirm.setThirdBioDate(StringUtils.isNotEmpty(form.getBioName3()) ? (confirm.getThirdBioDate() == null ? confirm.getConfirmTime() : confirm.getThirdBioDate()) : null);

        try {
            htcConfirmService.save(confirm);
            //rabbit job update confirm status from htc
            HtcConfirmMessage message = new HtcConfirmMessage();
            message.setConfirm(confirm);
            rabbitSender.sendVisit(message);

            return new Response<>(true, "Khách hàng đã được cập nhật kết quả KĐ thành công.", confirm);
        } catch (Exception e) {
            htcConfirmService.log(confirm.getID(), "Lỗi cập nhật thông tin. ");
            return new Response<>(false, "Lỗi cập nhật thông tin.");
        }
    }
//check valid date

    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/htc-confirm/log.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<HtcConfirmLogEntity> logs = htcConfirmService.getLogs(oID);
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", logs);
        data.put("staffs", staffs);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/htc-confirm/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody HtcConfirmLogEntity form, BindingResult bindingResult) {
        htcConfirmService.log(form.getHtcConfirmID(), form.getContent());
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }

    /**
     * nút Tiếp nhận thông tin
     *
     * @author pdThang
     * @param oid
     * @return
     */
    @RequestMapping(value = "/htc-confirm/received-info.json", method = RequestMethod.GET)
    public Response<?> actionReceivedInfo(@RequestParam(name = "oid") Long oid) {

        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        HtcConfirmEntity dataConfirm = htcConfirmService.findOne(oid);
        if (dataConfirm == null) {
            return new Response<>(false, "Khách hàng không tồn tại ");
        }
        HtcVisitEntity dataVisit = htcVisitService.findByCodeAndSite(dataConfirm.getSourceID(), dataConfirm.getSourceSiteID(), true);
        if (dataVisit != null && dataVisit.isIsRemove() == false) {
            data.put("options", options);
            data.put("dataConfirm", dataConfirm);
            data.put("dataVisit", dataVisit);
            return new Response<>(true, data);
        }
        return new Response<>(false, "Khách hàng không tồn tại bên Cơ sở sàng lọc hoặc đã bị xóa");
    }

    /**
     * nút Tiếp nhận thông tin
     *
     * @author pdThang
     * @param oid
     * @param form
     * @return
     */
    @RequestMapping(value = "/htc-confirm/received-info-update.json", method = RequestMethod.POST)
    public Response<?> actionUpdateReceivedInfo(@RequestParam(name = "oid") Long oid,
            @RequestBody HtcConfirmReceivedForm form) {
        LoggedUser currentUser = getCurrentUser();
        try {
            HtcConfirmEntity confirm = htcConfirmService.findOne(oid);
            if (confirm == null || confirm.getRemove() == true) {
                throw new Exception(String.format("Không tìm thấy khách hàng có ID %s", oid));
            }

            HtcVisitEntity visit = htcVisitService.findByCodeAndSite(confirm.getSourceID(), confirm.getSourceSiteID(), true);
            if (visit == null || visit.isIsRemove() == true) {
                throw new Exception(String.format("Không tìm thấy khách hàng có ID %s", oid));
            }

            if ("1".equals(form.getAccept())) {
                confirm.setFeedbackStatus(ConfirmFeedbackEnum.DA_XAC_NHAN.getKey());
                confirm.setSourceID(visit.getCode());
                confirm.setFullname(visit.getPatientName());
                confirm.setYear(Integer.valueOf(visit.getYearOfBirth()));
                confirm.setGenderID(visit.getGenderID());
                confirm.setAddress(visit.getPermanentAddress());
                confirm.setPermanentAddressGroup(visit.getPermanentAddressGroup());
                confirm.setPermanentAddressStreet(visit.getPermanentAddressStreet());
                confirm.setDistrictID(visit.getPermanentDistrictID());
                confirm.setWardID(visit.getPermanentWardID());
                confirm.setCode(visit.getConfirmTestNo());
                confirm.setConfirmTime(visit.getConfirmTime());
                confirm.setUpdatedAt(new Date());
            } else {
                confirm.setFeedbackStatus(ConfirmFeedbackEnum.TU_CHOI.getKey());
                confirm.setUpdatedAt(new Date());
            }

            confirm.setFeedbackMessage(form.getFeedbackMessage());
            htcConfirmService.save(confirm);
            //Cập nhật luôn trạng thái visit
            visit.setFeedbackStatus("1".equals(form.getAccept()) ? FeedbackStatusEnum.CONFIRMED.getKey() : FeedbackStatusEnum.REFUSED.getKey());
            htcVisitService.save(visit);

            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "THÔNG BÁO XÁC NHẬN YÊU CẦU THAY ĐỔI THÔNG TIN KHÁCH HÀNG");
            variables.put("code", visit.getCode());
            variables.put("messageVisit", visit.getFeedbackMessage());
            variables.put("messageConfirm", confirm.getFeedbackMessage());
            variables.put("siteConfirmName", currentUser.getSite().getName());

            Set<Long> staffIds = new HashSet<>();
            staffIds.add(visit.getCreatedBY());
            staffIds.add(visit.getUpdatedBY());

            String email = parameterService.getSiteConfig(confirm.getSourceSiteID(), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());

            if (ConfirmFeedbackEnum.DA_XAC_NHAN.getKey().equals(confirm.getFeedbackStatus())) {
                htcConfirmService.log(confirm.getID(), String.format("Đồng ý yêu cầu thay đổi thông tin KH. Lý do: %s", confirm.getFeedbackMessage()));
                htcVisitService.log(visit.getID(), String.format("Cơ sở %s đồng ý yêu cầu thay đổi thông tin KH. Lý do: %s", currentUser.getSite().getName(), confirm.getFeedbackMessage()));
                sendEmail(email, String.format("[Thông báo] Đã xác nhận thay đổi thông tin khách hàng mã %s", confirm.getSourceID()), EmailoutboxEntity.RECEIVED_MAIL, variables);
                staffService.sendMessage(staffIds, "Đồng ý yêu cầu thay đổi thông tin KH", String.format("Mã khách hàng: %s", confirm.getSourceID()), String.format("%s?code=%s", UrlUtils.htcIndex(), visit.getCode()));
            } else {
                htcConfirmService.log(confirm.getID(), String.format("Từ chối yêu cầu thay đổi thông tin KH. Lý do: %s", confirm.getFeedbackMessage()));
                htcVisitService.log(visit.getID(), String.format("Cơ sở %s từ chối yêu cầu thay đổi thông tin KH. Lý do: %s", currentUser.getSite().getName(), confirm.getFeedbackMessage()));
                sendEmail(email, String.format("[Thông báo] Từ chối xác nhận thay đổi thông tin khách hàng %s", confirm.getSourceID()), EmailoutboxEntity.RECEIVED_MAIL, variables);
                staffService.sendMessage(staffIds, "Từ chối yêu cầu thay đổi thông tin KH", String.format("Mã khách hàng: %s", confirm.getSourceID()), String.format("%s?code=%s", UrlUtils.htcIndex(), visit.getCode()));
            }

            return new Response<>(true, "Thông tin đã được phản hồi tới cơ sở gửi mẫu thành công", confirm);
        } catch (Exception e) {
            return new Response<>(false, String.format("Lỗi cập nhật thông tin. Error: %s", e.getMessage()));
        }
    }

    // ==================================================|
    // ---------------Thêm cấu hình tự động -------------|
    // ==================================================|
    /**
     * Convert dữ liệu sang định dạng phục vụ page define
     *
     * @author vvthanh
     * @return
     */
    private HashMap<String, HashMap<String, String>> buildFinalParameter() {
        HashMap<String, HashMap<String, String>> dataModels = new LinkedHashMap<>();
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);

        for (Map.Entry<String, String> entry : getFinalParameter().entrySet()) {
            String key = entry.getKey();
            if (dataModels.get(key) == null) {
                dataModels.put(key, new HashMap<String, String>());
                dataModels.get(key).put("title", "");
                dataModels.get(key).put("icon", "");
                dataModels.get(key).put("description", "");
                dataModels.get(key).put("hivinfo", "");
                dataModels.get(key).put("elog", "");
                dataModels.get(key).put("sitemap", "");
                dataModels.get(key).put("useparent", "");
            }
        }

        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2) {
                continue;
            }
            if (dataModels.get(splitCode[0]) == null) {
                continue;
            }
            dataModels.get(splitCode[0]).put(splitCode[1], parameter.getValue());
        }
        return dataModels;
    }

    /**
     * Tự động thêm cấu hình khi chưa có
     *
     * @param type
     * @param siteID
     * @return
     */
    private ParameterEntity addConfig(String type, SiteEntity site) {

        HashMap<String, String> buildTypeParameter = buildFinalParameter().get(type);

        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            return null;
        }

        try {
            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setType(type);
            parameterEntity.setStatus(BooleanEnum.TRUE.getKey());
            parameterEntity.setCode(String.format("s-%s", site.getID()));
            parameterEntity.setPosition(1);
            parameterEntity.setParentID(0L);
            parameterEntity.setSiteID(site.getID());
            parameterEntity.setValue(site.getName());

            parameterEntity = parameterService.save(parameterEntity);
            if (parameterEntity == null) {
                throw new Exception("Thêm dữ liệu tham số thất bại!");
            }
            return parameterEntity;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/htc-confirm/getAppendix02.json", method = RequestMethod.GET)
    public Response<?> actionUpdateResult(@RequestParam(name = "oid") String oids) {
        List<Long> pIDs = new ArrayList<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pIDs.add(Long.parseLong(string));
        }
        List<HtcConfirmEntity> items = htcConfirmService.findAllByIds(pIDs);
        List<HtcConfirmEntity> models = new ArrayList<>();
        if (!items.isEmpty()) {
            for (HtcConfirmEntity item : items) {
                if (item.getResultsID() != null && !"".equals(item.getResultsID())) {
                    if ("2".equals(item.getResultsID())) {
                        models.add(item);
                    }
                }
            }
        }
        if (models.isEmpty()) {
            return new Response<>(false, "Khách hàng đã chọn phải có kết quả xét nghiệm khẳng định là dương tính");
        }
        return new Response<>(true, models);
    }

    @RequestMapping(value = "/htc-confirm/get-verify-request.json", method = RequestMethod.GET)
    public Response<?> actionGetVerifyRequest(@RequestParam(name = "oid") String oid) {
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmEntity confirm = htcConfirmService.findOne(Long.parseLong(oid));
        if (confirm == null || confirm.getRemove() || !confirm.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        HtcVisitEntity visit = htcVisitService.findByCodeAndSite(confirm.getSourceID(), confirm.getSourceSiteID(), true);
        if (visit == null || visit.isIsRemove()) {
            return new Response<>(false, "Không tồn tại khách hàng ở cơ sở sàng lọc");
        }
        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        ArrayList<String> pIDs = new ArrayList<>();
        pIDs.add(visit.getPermanentProvinceID());
        pIDs.add(confirm.getProvinceID());
        pIDs.add(visit.getCurrentProvinceID());
        pIDs.add(confirm.getCurrentProvinceID());
        data.put("location", getLocation(pIDs));
        data.put("options", options);
        data.put("confirm", confirm);
        data.put("visit", visit);
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/htc-confirm/verify-request.json", method = RequestMethod.POST)
    public Response<?> actionVerifyRequest(
            @RequestParam(name = "confirm_id") String confirmID,
            @RequestParam(name = "htc_id") String htcID,
            @RequestParam(name = "verify") boolean verify,
            @RequestParam(name = "cause") String cause) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();
        HtcConfirmEntity confirm = htcConfirmService.findOne(Long.parseLong(confirmID));
        HtcVisitEntity visit = htcVisitService.findOne(Long.parseLong(htcID));
        if (StringUtils.isEmpty(cause)) {
            errors.put("cause", "Lý do phản hồi không được để trống");
        }
        if (StringUtils.isNotEmpty(cause) && cause.length() > 255) {
            errors.put("cause", "Lý do phản hồi không được quá 255 ký tự");
        }
        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }

        visit.setVerifyHtcTime(new Date());
        confirm.setVerifyHtcTime(new Date());

        if (verify) {
            confirm.setSourceID(visit.getCode());
            confirm.setFullname(visit.getPatientName());
            confirm.setYear(Integer.parseInt(visit.getYearOfBirth()));
            confirm.setGenderID(visit.getGenderID());
            confirm.setPatientID(visit.getPatientID());
            confirm.setObjectGroupID(visit.getObjectGroupID());
            confirm.setModeOfTransmission(visit.getModeOfTransmission());
            confirm.setAddress(visit.getPermanentAddress());
            confirm.setPermanentAddressGroup(visit.getPermanentAddressGroup());
            confirm.setPermanentAddressStreet(visit.getPermanentAddressStreet());
            confirm.setWardID(visit.getPermanentWardID());
            confirm.setDistrictID(visit.getPermanentDistrictID());
            confirm.setProvinceID(visit.getPermanentProvinceID());
            confirm.setCurrentAddress(visit.getCurrentAddress());
            confirm.setCurrentAddressGroup(visit.getCurrentAddressGroup());
            confirm.setCurrentAddressStreet(visit.getCurrentAddressStreet());
            confirm.setCurrentWardID(visit.getCurrentWardID());
            confirm.setCurrentDistrictID(visit.getCurrentDistrictID());
            confirm.setCurrentProvinceID(visit.getCurrentProvinceID());
            confirm.setTestResultsID(visit.getTestResultsID());
            confirm.setSourceSampleDate(visit.getSampleSentDate());
        }

        htcVisitService.save(visit);
        htcConfirmService.save(confirm);
        htcConfirmService.log(confirm.getID(), !verify ? String.format("Từ chối yêu cầu thay đổi thông tin khách hàng của cơ sở gửi mẫu %s \n .Lý do : %s", siteService.findOne(visit.getSiteID()).getName(), cause) : "Xác nhận thay đổi thông tin khách hàng");
        htcVisitService.log(visit.getID(), !verify ? String.format("Cơ sở khẳng định %s đã từ chối yêu cầu thay đổi thông tin khách hàng \n .Lý do : %s", siteService.findOne(confirm.getSiteID()).getName(), cause) : String.format("Cơ sở khẳng định %s đã xác nhận thay đổi thông tin khách hàng", siteService.findOne(confirm.getSiteID()).getName()));

        return new Response<>(true, verify ? "Xác nhận thay đổi khách hàng công" : "Từ chối xác nhận thay đổi thành công");
    }

    public Map<String, String> getLocation(List<String> pIDs) {
        Map<String, String> location = new HashMap<>();
        for (String pID : pIDs) {
            if (pID != null && !"".equals(pID)) {
                location.put(pID, locationsService.findProvince(pID).getName());

                Set<String> districtIDs = new HashSet<>();
                for (DistrictEntity e : locationsService.findDistrictByProvinceID(pID)) {
                    location.put(e.getID(), e.getName());
                    districtIDs.add(e.getID());
                }
                for (WardEntity e : locationsService.findByDistrictID(districtIDs)) {
                    location.put(e.getID(), e.getName());
                }
            }
        }
        return location;
    }
}
