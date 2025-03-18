package com.gms.controller.service;

import com.gms.components.QLNNUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.ConfirmFeedbackEnum;
import com.gms.entity.constant.ExchangeServiceEnum;
import com.gms.entity.constant.FeedbackStatusEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.HtcVisitLogEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.HtcVisitSendConfirmForm;
import com.gms.entity.form.HtcVisitConfirmReceiveForm;
import com.gms.entity.form.HtcVisitForm;
import com.gms.entity.form.htc.HtcTransferTreatmentForm;
import com.gms.entity.input.HtcMERSearch;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.validate.HtcRequestAdditionValidate;
import com.gms.entity.validate.HtcTransferTreatmentValidate;
import com.gms.entity.validate.HtcUpdReceiveDateValidate;
import com.gms.service.CommonService;
import com.gms.service.DistrictService;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import com.gms.service.ProvinceService;
import com.gms.service.WardService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HtcVisitController extends BaseController {

    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private WardService wardService;
    @Autowired
    private HtcTransferTreatmentValidate htcTransferTreatmentValidate;
    @Autowired
    HtcRequestAdditionValidate htcRequestAdditionValidate;
    @Autowired
    HtcUpdReceiveDateValidate htcUpdReceiveDateValidate;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private QLNNUtils qlnnUtils;

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

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.ARV_CONSULTANT_EXCHANGE_RESULT);
        parameterTypes.add(ParameterEntity.PARTNER_INFO_PROVIDE_RESULT);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.EXCHANGE_SERVICE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getShortName());
        }
        options.get("siteOpc").put("-1", "Cơ sở khác");
        addEnumOption(options, ParameterEntity.EXCHANGE_SERVICE, ExchangeServiceEnum.values(), "");

        return options;
    }

    private void getOtherObject(Set<String> objectGroupIDs) {
        if (objectGroupIDs != null) {
            objectGroupIDs.remove(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
            objectGroupIDs.remove(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
            objectGroupIDs.remove(TestObjectGroupEnum.MSM.getKey());
            objectGroupIDs.remove(TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey());
            objectGroupIDs.remove(TestObjectGroupEnum.PHAM_NHAN.getKey());
        }
        if (objectGroupIDs.isEmpty()) {
            objectGroupIDs.add("-1");
        }
    }

    /**
     * In phiếu gửi mẫu xét nghiệm
     *
     * @author pdThang
     * @param oids
     * @return
     */
    @RequestMapping(value = "/htc/data-modal.json", method = RequestMethod.GET)
    public Response<?> actionrRedirect(@RequestParam(name = "oid") String oids) {
        Set<Long> htcIds = new HashSet<>();
//        LoggedUser currentUser = getCurrentUser();

        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            htcIds.add(Long.parseLong(string));
        }

        List<HtcVisitEntity> models = htcVisitService.findAllByIDs(htcIds);
        if (models == null || models.isEmpty()) {
            return new Response<>(false, "Không có dữ liệu");
        }

        Set<Long> sIDs = new HashSet<>();
        for (HtcVisitEntity item : models) {
            if (StringUtils.isNotEmpty(item.getSiteConfirmTest())) {
                sIDs.add(Long.valueOf(item.getSiteConfirmTest()));
            }
        }
        Map<Long, SiteEntity> siteOptions = new LinkedHashMap<>();
        if (sIDs.size() > 0) {
            for (SiteEntity model : siteService.findByIDs(sIDs)) {
                siteOptions.put(model.getID(), model);
            }
        }

        Map<String, List<HtcVisitEntity>> result = new LinkedHashMap();
        String keyNot = "keyNot";
        if (result.get(keyNot) == null) {
            result.put(keyNot, new ArrayList<HtcVisitEntity>());
        }
        String key = null;
        for (HtcVisitEntity item : models) {
            if (item.getIsAgreeTest() != null && item.getIsAgreeTest() && item.getSiteConfirmTest() != null && !"".equals(item.getSiteConfirmTest()) && (item.getSampleSentDate() != null && !"".equals(item.getSampleSentDate()))) {
                key = siteOptions.get(Long.valueOf(item.getSiteConfirmTest())) == null ? "Cơ sở bị khóa" : siteOptions.get(Long.valueOf(item.getSiteConfirmTest())).getName();
                if (result.get(key) == null) {
                    result.put(key, new ArrayList<HtcVisitEntity>());
                }
                result.get(key).add(item);
            } else {
                result.get(keyNot).add(item);
            }
        }

        // Sắp xếp tăng dần theo ngày lấy mẫu
        for (Map.Entry<String, List<HtcVisitEntity>> entry : result.entrySet()) {
            if ("keyNot".equals(entry.getKey())) {
                continue;
            }
            Collections.sort(entry.getValue(), new Comparator<HtcVisitEntity>() {
                @Override
                public int compare(HtcVisitEntity o1, HtcVisitEntity o2) {
                    return o1.getPreTestTime().compareTo(o2.getPreTestTime());
                }
            });
        }

        Map<String, Object> data = new HashMap();
        data.put("options", getOptions());
        data.put("items", result);
        return new Response<>(true, data);
    }

    @RequestMapping(value = {"/htc/search.json"}, method = RequestMethod.POST)
    public Response<?> actionSearch(
            @RequestParam(name = "type", defaultValue = "", required = false) String type,
            @RequestBody HtcSearch search) {
        search.setPageSize(search.getPageSize() == 0 ? 9999999 : search.getPageSize());
        search.setPageIndex(search.getPageIndex() == 0 ? 1 : search.getPageIndex());
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(getCurrentUser().getSite().getID());
        search.setRemove(false);
        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<HtcVisitEntity> models = null;
        switch (type) {
            case "songuoixetnghiemhiv":
                models = htcVisitService.findSoNguoiDuocXetNghiemHIV(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(), null,
                        search.getPreTestTimeFrom(),
                        search.getPreTestTimeTo(),
                        search.getCustomerType());

                break;
            case "songuoixetnghiemhivduongtinh":
                Set<String> rid = new HashSet<>();
                rid.add("2");
                rid.add("3");
                models = htcVisitService.findSoNguoiDuocXetNghiemHIV(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(), rid,
                        search.getPreTestTimeFrom(),
                        search.getPreTestTimeTo(),
                        search.getCustomerType());
                break;
            case "nhanketquaxetnghiem":
                models = htcVisitService.findNhanKetQuaXetNghiemHIV(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(), null,
                        search.getPreTestTimeFrom(),
                        search.getPreTestTimeTo(),
                        search.getCustomerType());
                break;
            case "nhanketquaxetnghiemduongtinh":
                models = htcVisitService.findNhanKetQuaXetNghiemHIV(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(), "2",
                        search.getPreTestTimeFrom(),
                        search.getPreTestTimeTo(),
                        search.getCustomerType());
                break;
            case "phuluc01tt09":
                models = htcVisitService.findPhuLuc01TT09(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(),
                        search.getConfirmResultsID(),
                        search.getYearOfBirthFrom(),
                        search.getYearOfBirthTo(),
                        search.getGenderID(),
                        search.getPreTestTimeFrom(),
                        search.getPreTestTimeTo(),
                        search.getCustomerType());
                break;
            default:
                models = htcVisitService.find(search).getData();
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }

    /**
     * Tìm cơ sở xét nghiệm khẳng định
     *
     * @return
     */
    @RequestMapping(value = {"/htc/site-confirmatory-test.json", "/laytest/site-confirmatory-test.json"}, method = RequestMethod.GET)
    public Response<?> actionGetByServiceAndProvince() {
        return new Response<>(true, null, siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID()));
    }

    /**
     * Last visit
     *
     * @return
     */
    @RequestMapping(value = "/htc/last-visit.json", method = RequestMethod.GET)
    public Response<?> actionGetLastVisit() {
        LoggedUser currentUser = getCurrentUser();
        String code = currentUser.getSite().getCode();
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(currentUser.getUser().getID());
        if (!staffConfig.getOrDefault(StaffConfigEnum.HTC_STAFF_CODE.getKey(), "").equals("")) {
            code = staffConfig.get(StaffConfigEnum.HTC_STAFF_CODE.getKey());
        }
        return new Response<>(true, htcVisitService.findLastBysiteID(currentUser.getSite().getID(), code));
    }

    @RequestMapping(value = "/htc/log.json", method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<HtcVisitLogEntity> logs = htcVisitService.getLogs(oID);
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

    @RequestMapping(value = "/htc/log-create.json", method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody HtcVisitLogEntity form, BindingResult bindingResult) {
        htcVisitService.log(form.getHtcVisitID(), form.getContent());
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }

    @RequestMapping(value = "/htc/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") Long oID) {
        LoggedUser currentUser = getCurrentUser();
        HtcVisitEntity visit = htcVisitService.findOne(oID);
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        Map<String, Object> data = new HashMap();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), currentUser.getSite().getProvinceID());
        HashMap<String, HashMap<String, String>> options = getOptions();
        String confirmSite = "confirmSite";
        options.put(confirmSite, new HashMap<String, String>());
        for (SiteEntity site : sites) {
            options.get(confirmSite).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("options", options);
        data.put("model", visit);
        data.put("confirmModel", null);
        data.put("transferStaff", currentUser.getUser().getName());
        return new Response<>(true, data);
    }

    /**
     * Thông tin cập nhật ngày khách hàng nhận kết quả
     *
     * @param oID
     * @return
     */
    @RequestMapping(value = "/htc/receive-date.json", method = RequestMethod.GET)
    public Response<?> actionGetReceiveDate(@RequestParam(name = "oid") Long oID) {
        LoggedUser currentUser = getCurrentUser();
        HtcVisitEntity visit = htcVisitService.findOne(oID);
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        Map<String, Object> data = new HashMap();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), currentUser.getSite().getProvinceID());
        HashMap<String, HashMap<String, String>> options = getOptions();
        String confirmSite = "confirmSite";
        options.put(confirmSite, new HashMap<String, String>());
        for (SiteEntity site : sites) {
            options.get(confirmSite).put(String.valueOf(site.getID()), site.getName());
        }
        data.put("options", options);
        data.put("model", visit);
//        data.put("confirmModel", null);
        data.put("transferStaff", currentUser.getUser().getName());
//        if (visit.getSiteConfirmTest() != null && visit.getCode() != null && visit.getSiteID() != null) {
//            HtcConfirmEntity confirmModel = confirmService.findBySouce(Long.valueOf(visit.getSiteConfirmTest()), visit.getCode(), visit.getSiteID());
//            data.put("checkExist", confirmService.existBySourceIDAndSiteID(visit.getCode(), Long.valueOf(visit.getSiteConfirmTest())));
//            if (confirmModel != null) {
//                data.put("confirmModel", confirmModel);
//            }
//        }
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/htc/send-confirm.json", method = RequestMethod.POST)
    public Response<?> actionSendConfirm(@RequestParam(name = "oid") Long oID,
            @RequestBody HtcVisitSendConfirmForm form) {
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();

        HtcVisitEntity visit = htcVisitService.findOne(oID);
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        if (form.getSiteConfirmTest() == null || "".equals(form.getSiteConfirmTest())) {
            errors.put("siteConfirmTest", "Nơi chuyển máu đến không được để trống");
        }

        if (form.getSampleSentDate() == null || "".equals(form.getSampleSentDate())) {
            errors.put("siteConfirmTest", "Ngày gửi mẫu đến không được để trống");
        }

        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }
        visit.setSampleSentDate(TextUtils.convertDate(form.getSampleSentDate(), FORMATDATE));
        visit.setSiteConfirmTest(form.getSiteConfirmTest());
        try {
            commonService.sendConfirm(visit);
            String email = parameterService.getSiteConfig(Long.valueOf(visit.getSiteConfirmTest()), SiteConfigEnum.ALERT_CONFIRM_EMAIL.getKey());
            if (email != null || !"".equals(email)) {
                sendEmailNotify(email, String.format("Cơ sở %s gửi mẫu xét nghiệm của khách hàng mã %s ", currentUser.getSite().getName(), visit.getCode()), String.format("Khách hàng %s - %s được gửi ngày %s", visit.getCode(), visit.getPatientName(), TextUtils.formatDate(new Date(), FORMATDATE)));
            }
            return new Response<>(true, "Khách hàng đã được gửi thông tin lên cơ sở xét nghiệm khẳng định", visit);

        } catch (Exception e) {
            htcVisitService.log(visit.getID(), "Lỗi gửi thông tin xét nghiệm. " + e.getMessage());
            return new Response<>(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/htc/confirm-receive.json", method = RequestMethod.POST)
    public Response<?> actionConfirmReceive(@RequestParam(name = "oid") Long oID,
            @RequestParam(name = "confirm") boolean confirm,
            @RequestBody HtcVisitConfirmReceiveForm form) {
        LoggedUser currentUser = getCurrentUser();
        SimpleDateFormat sdfrmt = new SimpleDateFormat(FORMATDATE);
        Map<String, String> errors = new HashMap();
        HtcVisitEntity visit = htcVisitService.findOne(oID);
        HtcConfirmEntity confirmModel = confirmService.findBySouce(Long.valueOf(visit.getSiteConfirmTest()), visit.getCode(), visit.getSiteID());
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        if ((form.getFeedbackMessage() == null || "".equals(form.getFeedbackMessage())) && confirm == false) {
            errors.put("feedbackMessage", "Lý do phản hồi không được để trống");
        }

        if (form.getPermanentAddress() == null || "".equals(form.getPermanentAddress())) {
            errors.put("permanentAddress", "Số nhà/tổ/thôn xóm không được để trống");
        }

        if (form.getPermanentProvinceID() == null || "".equals(form.getPermanentProvinceID())) {
            errors.put("permanentProvinceID", "Tỉnh/Thành phố không được để trống");
        }

        if (form.getPermanentDistrictID() == null || "".equals(form.getPermanentDistrictID())) {
            errors.put("permanentDistrictID", "Quận/Huyện không được để trống");
        }

        if (form.getPermanentWardID() == null || "".equals(form.getPermanentWardID())) {
            errors.put("permanentWardID", "Phường/xã không được để trống");
        }

        if (form.getPatientName() == null || "".equals(form.getPatientName()) && errors.get("patientName") == null) {
            errors.put("patientName", "Họ và tên KH không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getPatientName()) && form.getPatientName().length() > 100 && errors.get("patientName") == null) {
            errors.put("patientName", "Họ và tên KH không được quá 100 ký tự");
        }

        if (StringUtils.isNotEmpty(form.getPermanentAddressGroup()) && form.getPermanentAddressGroup().length() > 500 && errors.get("permanentAddressGroup") == null) {
            errors.put("permanentAddressGroup", "Tổ/ấp/Khu phố không được quá 500 ký tự");
        }

        if (StringUtils.isNotEmpty(form.getPermanentAddressStreet()) && form.getPermanentAddressStreet().length() > 500 && errors.get("permanentAddressStreet") == null) {
            errors.put("permanentAddressStreet", "Đường phố không được quá 500 ký tự");
        }

        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)
                && errors.get("patientName") == null) {
            errors.put("patientName", "Họ và tên chỉ chứa các kí tự chữ cái");
        }

        if (StringUtils.isNotEmpty(form.getGenderID()) && !parameterService.fieldParameterExists(form.getGenderID(),
                ParameterEntity.GENDER, false, "") && errors.get("genderID") == null) {
            errors.put("genderID", "Giới tính không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentProvinceID())
                && !provinceService.existsProvinceByID(form.getPermanentProvinceID())
                && errors.get("permanentProvinceID") == null) {
            errors.put("permanentProvinceID", "Tỉnh thành không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentDistrictID())
                && !districtService.existsDistrictByID(form.getPermanentDistrictID(), form.getPermanentProvinceID())
                && errors.get("permanentDistrictID") == null) {
            errors.put("permanentDistrictID", "Quận huyện không tồn tại");
        }

        if (StringUtils.isNotEmpty(form.getPermanentWardID())
                && !wardService.existsWardByID(form.getPermanentWardID(), form.getPermanentDistrictID())
                && errors.get("permanentWardID") == null) {
            errors.put("permanentWardID", "Xã phường không tồn tại");
        }

        if (!form.getResultsSiteTime().contains("/") && !"".equals(form.getResultsSiteTime())) {
            form.setResultsSiteTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getResultsSiteTime()));
        }

        if (form.getResultsSiteTime() == null || "".equals(form.getResultsSiteTime())) {
            errors.put("resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không được để trống");
        }

        if (StringUtils.isEmpty(form.getYearOfBirth()) && errors.get("yearOfBirth") == null) {
            errors.put("yearOfBirth", "Năm sinh không được để trống");
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}") && errors.get("yearOfBirth") == null) {
            errors.put("yearOfBirth", "Năm sinh gồm bốn chữ số vd: 1994");
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR)) && errors.get("yearOfBirth") == null) {
            errors.put("yearOfBirth", "Năm sinh hợp lệ từ 1900 - năm hiện tại");
        }

        try {
            if ((!"".equals(visit.getSampleSentDate()) && visit.getSampleSentDate() != null) && (!"".equals(visit.getConfirmTime()) && visit.getConfirmTime() != null) && (!"".equals(form.getResultsSiteTime()) && form.getResultsSiteTime() != null) && (!"".equals(visit.getPreTestTime()) && visit.getPreTestTime() != null)) {
                Date today = sdfrmt.parse(TextUtils.formatDate(new Date(), FORMATDATE));
                Date confirmTime = sdfrmt.parse(TextUtils.formatDate(visit.getConfirmTime(), FORMATDATE));
                Date sampleSentDate = sdfrmt.parse(TextUtils.formatDate(visit.getSampleSentDate(), FORMATDATE));
                Date resultsSiteTime = sdfrmt.parse(form.getResultsSiteTime());
                Date preTestTime = sdfrmt.parse(TextUtils.formatDate(visit.getPreTestTime(), FORMATDATE));

                if (resultsSiteTime.compareTo(today) > 0) {
                    errors.put("resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không được sau ngày hiện tại");
                }
                if (confirmTime.compareTo(resultsSiteTime) > 0) {
                    errors.put("resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ  không được trước ngày XN khẳng định");
                }
                if (sampleSentDate.compareTo(resultsSiteTime) > 0) {
                    errors.put("resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ  không được trước ngày nhận mẫu");
                }
                if (preTestTime.compareTo(resultsSiteTime) > 0) {
                    errors.put("resultsSiteTime", "Ngày cơ sở nhận kết quả KĐ không được trước ngày XN sàng lọc");
                }
            }
        } catch (ParseException ex) {
        }

        if (errors.size() > 0) {
            return new Response<>(false, errors);
        }

        visit.setPatientName(form.getPatientName());
        visit.setYearOfBirth(form.getYearOfBirth());
        visit.setGenderID(form.getGenderID());
        try {
            visit.setFeedbackTime(sdfrmt.parse(TextUtils.formatDate(new Date(), FORMATDATE)));
        } catch (ParseException ex) {
        }

        if (confirm == true) {
            visit.setFeedbackStatus(FeedbackStatusEnum.COLLATED.getKey());
        } else {
            visit.setFeedbackStatus(FeedbackStatusEnum.WAITED.getKey());
            confirmModel.setFeedbackStatus(ConfirmFeedbackEnum.CHUA_XAC_NHAN.getKey());
        }
        visit.setResultsSiteTime(TextUtils.convertDate(form.getResultsSiteTime(), FORMATDATE));
        visit.setFeedbackMessage(form.getFeedbackMessage());
        visit.setPermanentAddress(form.getPermanentAddress());
        visit.setPermanentAddressGroup(form.getPermanentAddressGroup());
        visit.setPermanentAddressStreet(form.getPermanentAddressStreet());
        visit.setPermanentProvinceID(form.getPermanentProvinceID());
        visit.setPermanentDistrictID(form.getPermanentDistrictID());
        visit.setPermanentWardID(form.getPermanentWardID());
        try {
            commonService.confirmReceive(visit, confirmModel, confirm);
            if (confirm == true) {
                return new Response<>(true, "Kết quả khẳng định đã được xác nhận đối chiếu thành công.", visit);
            } else {
                Map<String, Object> variables = new HashMap<>();
                SiteEntity confirmSite = siteService.findOne(Long.valueOf(visit.getSiteConfirmTest()));
                variables.put("code", visit.getCode());
                variables.put("siteConfirmTest", confirmSite.getName());
                variables.put("feedbackMessage", visit.getFeedbackMessage());
                variables.put("feedbackTime", TextUtils.formatDate(visit.getFeedbackTime(), "dd/MM/yyyy"));
                variables.put("htcName", currentUser.getSite().getName());
                String email = parameterService.getSiteConfig(Long.valueOf(visit.getSiteConfirmTest()), SiteConfigEnum.ALERT_CONFIRM_EMAIL.getKey());
                if (email != null && !"".equals(email)) {
                    sendEmail(email, "[Thông báo] Phản hồi thông tin sai của khách hàng(" + visit.getCode() + ")", EmailoutboxEntity.TEMPLATE_FEEDBACK, variables);
                }
                Set<Long> ids = new HashSet<>();
                ids.add(confirmModel.getCreatedBy());
                ids.add(confirmModel.getUpdatedBy());
                staffService.sendMessage(ids, "Yêu cầu thay đổi thông tin khách hàng", String.format("Khách hàng %s", visit.getCode()), UrlUtils.htcConfirmIndexFeedback(confirmModel.getSourceID()));
                return new Response<>(true, "Thông tin thay đổi của khách hàng đã được gửi phản hồi tới cơ sở khẳng định.", visit);
            }
        } catch (Exception e) {
            htcVisitService.log(visit.getID(), "Có lỗi xảy ra. " + e.getMessage());
            return new Response<>(false, e.getMessage());
        }
    }

    /**
     * Chuyển gửi GSPH từ sàng lọc HTC
     *
     * @auth TrangBN
     * @param visitID
     * @return
     */
    @RequestMapping(value = "/htc/transfer-gsph.json", method = RequestMethod.GET)
    public Response<?> actionTransferGSPH(@RequestParam(name = "oid") Long visitID) throws Exception {

        try {
            Map<String, Map<Long, String>> options = getSiteOptions();
            HtcVisitEntity htc = htcVisitService.findOne(visitID);
            if (htc == null) {
                return new Response<>(false, "Thông tin khách hàng không tồn tại");
            }
            PacPatientInfoEntity patient = qlnnUtils.visit(htc);

            htc.setPacSentDate(new Date());
            htc.setPacPatientID(patient.getID());
            htcVisitService.save(htc);
            htcVisitService.log(htc.getID(), "Cập nhật trạng thái đã chuyển gửi GSPH thành công");

            return new Response<>(true, "Khách hàng đã được chuyển sang báo cáo giám sát phát hiện thành công", patient);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, e.getMessage());
        }
    }

    /**
     * Xác nhận khách hàng không nhận kết quả
     *
     * @auth TrangBN
     * @param visitID
     * @return
     */
    @RequestMapping(value = "/htc/confirm-no-receive.json", method = RequestMethod.GET)
    public Response<?> actionConfirmNotReceive(@RequestParam(name = "oid") Long visitID) throws Exception {

        HtcVisitEntity htc = htcVisitService.findOne(visitID);
        if (htc == null) {
            return new Response<>(false, "Thông tin khách hàng không tồn tại");
        }
        htc.setConfirmNoReceive(true);
        htc = htcVisitService.save(htc);
        htcVisitService.log(htc.getID(), "Khách hàng đã xác nhận không nhận kết quả");

        return new Response<>(true, "Xác nhận thành công", htc);
    }

    @RequestMapping(value = {"/htc-mer/search.json"}, method = RequestMethod.POST)
    public Response<?> actionMERSearch(
            @RequestParam(name = "type", defaultValue = "", required = false) String type,
            @RequestBody HtcMERSearch search) {
        Map<String, Object> data = new HashMap<>();
        data.put("options", getOptions());
        List<HtcVisitEntity> models = null;
        switch (type) {
            case "table01i":
                models = htcVisitService.findTable01MERIntroduced(
                        search.getSiteID(),
                        search.getGenderID(),
                        search.getServiceIDs(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime(),
                        search.getFromAge(),
                        search.getToAge(),
                        search.getFlag());
                break;
            case "table01a":
                models = htcVisitService.findTable01MERAgreed(
                        search.getSiteID(),
                        search.getGenderID(),
                        search.getServiceIDs(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime(),
                        search.getFromAge(),
                        search.getToAge(),
                        search.getFlag());
                break;
            case "":
                models = htcVisitService.findTable02MER(
                        search.getSiteID(),
                        search.getEarlyHiv(),
                        search.getGenderID(),
                        search.getServiceID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime(),
                        search.getFromAge(),
                        search.getToAge(),
                        search.getFlag());
                break;
            case "table03":
                if ("1".equals(search.getFlag())) {
                    getOtherObject(search.getObjectGroupIDs());
                }
                models = htcVisitService.findTable03MER(
                        search.getSiteID(),
                        search.getEarlyHiv(),
                        search.getServiceID(),
                        search.getObjectGroupID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime());
                break;
            case "table04p":
                models = htcVisitService.findTable04MERPositive(
                        search.getSiteID(),
                        search.getGenderID(),
                        search.getServiceID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime(),
                        search.getFromAge(),
                        search.getToAge(),
                        search.getFlag());
                break;
            case "table04n":
                models = htcVisitService.findTable04MERNegative(
                        search.getSiteID(),
                        search.getGenderID(),
                        search.getServiceID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime(),
                        search.getFromAge(),
                        search.getToAge(),
                        search.getFlag());
                break;
            case "table05p":
                if ("1".equals(search.getFlag())) {
                    getOtherObject(search.getObjectGroupIDs());
                }
                models = htcVisitService.findTable05MERPositive(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime());
                break;
            case "table05n":
                if ("1".equals(search.getFlag())) {
                    getOtherObject(search.getObjectGroupIDs());
                }
                models = htcVisitService.findTable05MERNegative(
                        search.getSiteID(),
                        search.getServiceID(),
                        search.getObjectGroupID(),
                        search.getObjectGroupIDs(),
                        search.getFromTime(),
                        search.getToTime());
                break;
            default:
                models = null;
        }
        data.put("models", models);
        return new Response<>(true, null, data);
    }

    /**
     * Tự động thêm cấu hình khi chưa có
     *
     * @param type
     * @param siteID
     * @return
     */
    private ParameterEntity addConfig(String type, SiteEntity site) {

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

    /**
     * Lấy ds khách hàng dương tính
     *
     * @author DSNAnh
     * @param oids
     * @return
     */
    @RequestMapping(value = "/htc/getAppendix02.json", method = RequestMethod.GET)
    public Response<?> actionUpdateResult(@RequestParam(name = "oid") String oids) {
        Set<Long> pIDs = new HashSet<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pIDs.add(Long.parseLong(string));
        }
        List<HtcVisitEntity> items = htcVisitService.findAllByIDs(pIDs);
        List<HtcVisitEntity> models = new ArrayList<>();
        if (!items.isEmpty()) {
            for (HtcVisitEntity item : items) {
                if (item.getConfirmResultsID() != null && !"".equals(item.getConfirmResultsID())) {
                    if ("2".equals(item.getConfirmResultsID())) {
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

    /**
     * Chuyển gửi điều trị
     *
     * @author DSNAnh
     * @param oID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/htc/transfer-treatment.json", method = RequestMethod.POST)
    public Response<?> actionTransferTreatment(@RequestParam(name = "oid") Long oID,
            @RequestBody HtcTransferTreatmentForm form,
            BindingResult bindingResult) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        HtcVisitEntity htc = htcVisitService.findOne(oID);
        if (htc == null) {
            return new Response<>(false, "Thông tin khách hàng không tồn tại");
        }
        form.setResultsPatienTime(TextUtils.formatDate(htc.getResultsPatienTime(), FORMATDATE));

        if (StringUtils.isNotEmpty(form.getExchangeConsultTime()) && !form.getExchangeConsultTime().contains("/")) {
            String exchangeConsultTime = form.getExchangeConsultTime();
            form.setExchangeConsultTime(exchangeConsultTime.substring(0, 2) + "/" + exchangeConsultTime.substring(2, 4) + "/" + exchangeConsultTime.substring(4, 8));
        }
        if (StringUtils.isNotEmpty(form.getExchangeTime()) && !form.getExchangeTime().contains("/")) {
            String exchangeTime = form.getExchangeTime();
            form.setExchangeTime(exchangeTime.substring(0, 2) + "/" + exchangeTime.substring(2, 4) + "/" + exchangeTime.substring(4, 8));
        }
        if (StringUtils.isNotEmpty(form.getArrivalSiteID()) && form.getArrivalSiteID().contains("string:")) {
            form.setArrivalSiteID(form.getArrivalSiteID().replace("string:", ""));
        }
        htcTransferTreatmentValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Cập nhật không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }
        htc.setArvExchangeResult(ArvExchangeEnum.DONG_Y.getKey());
        htc.setExchangeConsultTime(TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE));
        htc.setPartnerProvideResult(form.getPartnerProvideResult());
        htc.setExchangeProvinceID(form.getPermanentProvinceID());
        htc.setExchangeDistrictID(form.getPermanentDistrictID());
        htc.setArrivalSiteID(Long.parseLong(form.getArrivalSiteID()));
        htc.setArrivalSite(form.getArrivalSite());
        htc.setExchangeTime(TextUtils.convertDate(form.getExchangeTime(), FORMATDATE));
        htc.setStaffAfterID(currentUser.getUser().getName());
        htc.setExchangeService("1");
        OpcArvEntity arv = opcArvService.findBySourceID(htc.getID(), ServiceEnum.HTC.getKey());
        if (arv != null && arv.getSiteID().equals(htc.getArrivalSiteID())) {
            SiteEntity site = siteService.findOne(arv.getSiteID());
            arv.setDateOfArrival(htc.getExchangeTime());
            arv.setTranferToTime(new Date());
            arv.setFeedbackResultsReceivedTime(new Date());
            htc.setArrivalTime(arv.getTranferToTime());
            htc.setFeedbackReceiveTime(arv.getFeedbackResultsReceivedTime());
            htc.setRegisterTherapyTime(arv.getRegistrationTime());
            htc.setRegisteredTherapySite(site.getName());
            htc.setTherapyNo(arv.getCode());
            htc.setTherapyRegistProvinceID(site.getProvinceID());
            htc.setTherapyRegistDistrictID(site.getDistrictID());
            htc.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
            opcArvService.save(arv);
        } else {
            htc.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey());
        }

        HtcVisitEntity htcRs = htcVisitService.save(htc);
        String siteName = htc.getArrivalSiteID() == -1 ? htc.getArrivalSite() : siteService.findOne(htc.getArrivalSiteID()).getName();
        htcVisitService.log(htc.getID(), htcRs.getRegisterTherapyTime() == null ? "Khách hàng được chuyển tới cơ sở " + siteName : String.format("Khách hàng được chuyển tới cơ sở %s được phản hồi thành công", siteName));

        //Gửi email thông báo
        Map<String, Object> variables = new HashMap<>();
        SiteEntity transferSite = siteService.findOne(htc.getArrivalSiteID());
        variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã XN khẳng định: " + htc.getConfirmTestNo());
        variables.put("transferSiteName", transferSite == null ? htc.getArrivalSite() : transferSite.getName());
        variables.put("currentSiteName", getCurrentUser().getSite().getName());
        variables.put("gender", options.get(ParameterEntity.GENDER).get(htc.getGenderID()));
        variables.put("fullName", htc.getPatientName());
        variables.put("dob", htc.getYearOfBirth());
        variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
        sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển gửi điều trị khách hàng mã XN khẳng định: #%s", htc.getConfirmTestNo()), EmailoutboxEntity.HTC_TRANSFER_TREATMENT_MAIL, variables);
        return new Response<>(true, "Khách hàng được chuyển gửi tới cơ sở điều trị thành công");
    }

    /**
     * Tìm ds cơ sở điều trị theo tỉnh/huyện được chọn
     *
     * @author DSNAnh
     * @param pID
     * @param dID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/htc/get-transfer-site.json", method = RequestMethod.GET)
    public Response<?> actionGetTransferSite(@RequestParam(name = "pid") String pID,
            @RequestParam(name = "did") String dID) {
        HashMap<String, String> sites = new HashMap<>();
        List<SiteEntity> siteOpc = siteService.getSiteOpc(StringUtils.isEmpty(pID) ? getCurrentUser().getSite().getProvinceID() : pID);
        if (StringUtils.isNotEmpty(dID) && !"undefined".equals(dID)) {
            for (SiteEntity site : siteOpc) {
                if (site.getDistrictID().equals(dID)) {
                    sites.put(String.valueOf(site.getID()), site.getShortName());
                }
            }
        } else {
            for (SiteEntity site : siteOpc) {
                sites.put(String.valueOf(site.getID()), site.getShortName());
            }
        }

        sites.put("-1", "Cơ sở khác");
        return new Response<>(true, "", sites);
    }

    /**
     * Yêu cầu bổ sung thông tin
     *
     * @author DSNAnh
     * @param oID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/htc/additional-request.json", method = RequestMethod.POST)
    public Response<?> actionAdditionalRequest(@RequestParam(name = "oid") Long oID,
            @RequestBody HtcVisitForm form,
            BindingResult bindingResult) {
        LoggedUser currentUser = getCurrentUser();
        HtcVisitEntity visit = htcVisitService.findOne(oID);
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        HtcConfirmEntity confirm = confirmService.findBySouce(Long.parseLong(visit.getSiteConfirmTest()), visit.getCode(), visit.getSiteID(), visit.getConfirmTestNo());
        if (confirm == null || confirm.getRemove()) {
            return new Response<>(false, "Không tồn tại khách hàng ở cơ sở khẳng định");
        }

        htcRequestAdditionValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Gửi yêu cầu không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }

        visit.setPatientName(form.getPatientName());
        visit.setYearOfBirth(form.getYearOfBirth());
        visit.setGenderID(form.getGenderID());
        visit.setPatientID(form.getPatientID());
        visit.setObjectGroupID(form.getObjectGroupID());
        visit.setModeOfTransmission(form.getModeOfTransmission());
        visit.setPermanentAddress(form.getPermanentAddress());
        visit.setPermanentAddressGroup(form.getPermanentAddressGroup());
        visit.setPermanentAddressStreet(form.getPermanentAddressStreet());
        visit.setPermanentWardID(form.getPermanentWardID());
        visit.setPermanentDistrictID(form.getPermanentDistrictID());
        visit.setPermanentProvinceID(form.getPermanentProvinceID());
        visit.setCurrentAddress(form.getCurrentAddress());
        visit.setCurrentAddressGroup(form.getCurrentAddressGroup());
        visit.setCurrentAddressStreet(form.getCurrentAddressStreet());
        visit.setCurrentWardID(form.getCurrentWardID());
        visit.setCurrentDistrictID(form.getCurrentDistrictID());
        visit.setCurrentProvinceID(form.getCurrentProvinceID());
        if ("2".equals(form.getConfirmTestStatus())) {
            visit.setResultsSiteTime(TextUtils.convertDate(form.getResultsSiteTime(), FORMATDATE));
        }
        visit.setRequestHtcTime(new Date());
        visit.setVerifyHtcTime(null);
        confirm.setRequestHtcTime(new Date());
        confirm.setVerifyHtcTime(null);
        htcVisitService.save(visit);
        confirmService.save(confirm);
        confirmService.log(confirm.getID(), "Cơ sở sàng lọc " + siteService.findOne(visit.getSiteID()).getName() + " gửi yêu cầu cập nhật thông tin khách hàng" + "\n" + ".Lý do yêu cầu: " + form.getCauseChange());
        htcVisitService.log(visit.getID(), "Gửi yêu cầu cập nhật thông tin tới cơ sở khẳng định " + siteService.findOne(confirm.getSiteID()).getName());
        return new Response<>(true, "Gửi yêu cầu cập nhật thông tin tới cơ sở khẳng định " + siteService.findOne(confirm.getSiteID()).getName() + " thành công");
    }

    /**
     * Cập nhật thông tin điều trị pop up (thuộc phần cập nhật ngày)
     *
     * @author TrangBN
     * @param oID
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/htc/update-receive-date.json", method = RequestMethod.POST)
    public Response<?> actionUpdReceiveDate(@RequestParam(name = "oid") Long oID,
            @RequestBody HtcVisitForm form,
            BindingResult bindingResult) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        HtcVisitEntity visit = htcVisitService.findOne(oID);
        if (visit == null || visit.isIsRemove() || !visit.getSiteID().equals(currentUser.getSite().getID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        form.setResultsSiteTime(TextUtils.formatDate(visit.getResultsSiteTime(), FORMATDATE));
        form.setResultsPatienTime(normalizeDate(form.getResultsPatienTime()));
        form.setExchangeConsultTime(normalizeDate(form.getExchangeConsultTime()));
        form.setExchangeTime(normalizeDate(form.getExchangeTime()));

        htcUpdReceiveDateValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.put(error.getCode(), error.getDefaultMessage());
            }
            return new Response<>(false, "Cập nhật thông tin khách hàng không thành công. Vui lòng kiểm tra lại thông tin", errors);
        }

        visit.setResultsPatienTime(TextUtils.convertDate(form.getResultsPatienTime(), FORMATDATE));
        visit.setExchangeService(form.getExchangeServiceConfirm());
        visit.setExchangeServiceName(form.getExchangeServiceNameConfirm());
        visit.setExchangeConsultTime(StringUtils.isNotEmpty(form.getExchangeConsultTime()) ? TextUtils.convertDate(form.getExchangeConsultTime(), FORMATDATE) : null);

        visit.setPartnerProvideResult(form.getPartnerProvideResult());
        visit.setArvExchangeResult(form.getArvExchangeResult());
        visit.setExchangeTime(StringUtils.isNotEmpty(form.getExchangeTime()) ? TextUtils.convertDate(form.getExchangeTime(), FORMATDATE) : null);
        visit.setStaffAfterID(form.getStaffAfterID());
        visit.setArrivalSite(form.getArrivalSite());
        visit.setArrivalSiteID(StringUtils.isNotEmpty(form.getArrivalSiteID()) && form.getArrivalSiteID().contains(":") ? Long.valueOf(form.getArrivalSiteID().substring(7)) : StringUtils.isNotEmpty(form.getArrivalSiteID()) ? Long.valueOf(form.getArrivalSiteID()) : null);
        visit.setExchangeDistrictID(form.getPermanentDistrictID());
        visit.setExchangeProvinceID(form.getPermanentProvinceID());
        visit.setConfirmNoReceive(!StringUtils.isNotEmpty(form.getResultsPatienTime()));
        if (visit.getArrivalSiteID() != null) {
            visit.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey());
        }

        visit = htcVisitService.save(visit);
        if (TherapyExchangeStatusEnum.DA_CHUYEN.getKey().equals(visit.getTherapyExchangeStatus())) {
            String siteName = visit.getArrivalSiteID() == -1 ? visit.getArrivalSite() : siteService.findOne(visit.getArrivalSiteID()).getName();
            htcVisitService.log(visit.getID(), "Khách hàng được chuyển tới cơ sở " + siteName);
        }
        htcVisitService.log(visit.getID(), "Cập nhật ngày khách hàng nhận kết quả thành công");

        //Gửi email thông báo
        if (visit.getArrivalSiteID() != null) {
            Map<String, Object> variables = new HashMap<>();
            SiteEntity transferSite = siteService.findOne(visit.getArrivalSiteID());
            variables.put("title", "Thông báo chuyển tiếp điều trị bệnh nhân mã XN khẳng định: " + visit.getConfirmTestNo());
            variables.put("transferSiteName", transferSite == null ? visit.getArrivalSite() : transferSite.getName());
            variables.put("currentSiteName", getCurrentUser().getSite().getName());
            variables.put("gender", options.get(ParameterEntity.GENDER).get(visit.getGenderID()));
            variables.put("fullName", visit.getPatientName());
            variables.put("dob", visit.getYearOfBirth());
            variables.put("transferTime", TextUtils.formatDate(new Date(), FORMATDATE));
            sendEmail(transferSite == null ? "" : getSiteEmail(transferSite.getID(), ServiceEnum.OPC), String.format("[Thông báo] Chuyển gửi điều trị khách hàng mã XN khẳng định: #%s", visit.getConfirmTestNo()), EmailoutboxEntity.HTC_TRANSFER_TREATMENT_MAIL, variables);
        }

        return new Response<>(true, "Cập nhật khách hàng thành công");
    }

    /**
     * Chuẩn hóa ngày
     *
     * @param date
     * @return
     */
    private String normalizeDate(String date) {
        if (StringUtils.isEmpty(date) || date.length() < 8) {
            return "";
        }

        if (StringUtils.isNotEmpty(date) && date.contains("/")) {
            return date;
        }

        StringBuilder sb = new StringBuilder(date);
        sb.insert(4, "/");
        sb.insert(2, "/");

        return sb.toString();
    }
}
