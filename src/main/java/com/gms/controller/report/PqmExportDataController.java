package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvEndCaseEnum;
import com.gms.entity.constant.PqmPrepResultEnum;
import com.gms.entity.constant.PqmPrepStatusEnum;
import com.gms.entity.constant.PqmPrepTreatmentEnum;
import com.gms.entity.constant.PqmPrepTypeEnum;
import com.gms.entity.constant.RegisterTherapyStatusEnum;
import com.gms.entity.constant.RegistrationTypeEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.pac.PqmExportAllExcel;
import com.gms.entity.excel.pac.PqmExportEstimateExcel;
import com.gms.entity.excel.pac.PqmExportPlanExcel;
import com.gms.entity.excel.pac.PqmShiArtExcel;
import com.gms.entity.excel.pac.PqmShiMMDExcel;
import com.gms.entity.form.opc_arv.PqmExportAllForm;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmDrugSearch;
import com.gms.entity.input.PqmDrugeLMISESearch;
import com.gms.entity.input.PqmPrepSearch;
import com.gms.entity.input.PqmShiArtSearch;
import com.gms.entity.input.PqmShiMmdSearch;
import com.gms.entity.input.PqmVctRecencySearch;
import com.gms.entity.input.PqmVctSearch;
import com.gms.service.PqmArvService;
import com.gms.service.PqmDataService;
import com.gms.service.PqmDrugEstimateDataService;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugNewDataService;
import com.gms.service.PqmDrugPlanDataService;
import com.gms.service.PqmDrugPlanService;
import com.gms.service.PqmDrugeLMISEDataService;
import com.gms.service.PqmDrugeLMISEService;
import com.gms.service.PqmPrepService;
import com.gms.service.PqmShiArtService;
import com.gms.service.PqmShiDataService;
import com.gms.service.PqmShiMmdService;
import com.gms.service.PqmVctRecencyService;
import com.gms.service.PqmVctService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BC PQM HTC
 *
 * @author pdThang
 */
@Controller(value = "pqm_reportdddss_controllers")
public class PqmExportDataController extends BaseController {

    @Autowired
    private PqmVctService vctService;
    @Autowired
    private PqmVctRecencyService vctRecencyService;
    @Autowired
    private PqmPrepService prepService;
    @Autowired
    private PqmArvService arvService;
    @Autowired
    private PqmDrugEstimateService drugEstimateService;
    @Autowired
    private PqmDrugPlanService drugPlanService;
    @Autowired
    private PqmDrugeLMISEService drugeLMISEService;

    @Autowired
    private PqmShiArtService shiArtService;
    @Autowired
    private PqmShiMmdService shiMmdService;

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        addEnumOption(options, ParameterEntity.ARV_END_CASE, ArvEndCaseEnum.values(), ""); //Lý do kết thúc đợt điều trị
        addEnumOption(options, ParameterEntity.LAO_END_CASE, ArvEndCaseEnum.values(), "");
        addEnumOption(options, "registerTherapyStatus", RegisterTherapyStatusEnum.values(), "Tất cả");
        addEnumOption(options, ParameterEntity.REGISTRATION_TYPE, RegistrationTypeEnum.values(), "Chọn loại đăng ký");

        options.get(ParameterEntity.EARLY_DIAGNOSE).put("3", "Không xét nghiệm");

        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        options.get("siteOpc").put("", "Tất cả");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> sitePrEP = siteService.getSitePrEP(getCurrentUser().getSite().getProvinceID());
        options.put("sitePrEP", new HashMap<String, String>());
        options.get("sitePrEP").put("", "Tất cả");
        for (SiteEntity site : sitePrEP) {
            options.get("sitePrEP").put(String.valueOf(site.getID()), site.getName());
        }
        List<SiteEntity> siteHtc = siteService.getSiteHtc(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtc", new HashMap<String, String>());
        options.get("siteHtc").put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get("siteHtc").put(String.valueOf(site.getID()), site.getName());
        }

        List<SiteEntity> siteHtcConfirm = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        options.put("siteHtcConfirm", new HashMap<String, String>());
        options.get("siteHtcConfirm").put("", "Tất cả");
        for (SiteEntity site : siteHtcConfirm) {
            options.get("siteHtcConfirm").put(String.valueOf(site.getID()), site.getName());
        }

        options.put("type", new HashMap<String, String>());
        options.get("type").put("", "Tất cả");
        options.get("type").put(PqmPrepTypeEnum.MOI.getKey(), PqmPrepTypeEnum.MOI.getLabel());
        options.get("type").put(PqmPrepTypeEnum.CU.getKey(), PqmPrepTypeEnum.CU.getLabel());

        options.put("treatment", new HashMap<String, String>());
        options.get("treatment").put(PqmPrepTreatmentEnum.HANG_NGAY.getKey(), PqmPrepTreatmentEnum.HANG_NGAY.getLabel());
        options.get("treatment").put(PqmPrepTreatmentEnum.TINH_HUONG.getKey(), PqmPrepTreatmentEnum.TINH_HUONG.getLabel());

        options.put("result", new HashMap<String, String>());
        options.get("result").put(PqmPrepResultEnum.DUONG_TINH.getKey(), PqmPrepResultEnum.DUONG_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.AM_TINH.getKey(), PqmPrepResultEnum.AM_TINH.getLabel());
        options.get("result").put(PqmPrepResultEnum.KHONG_RO.getKey(), PqmPrepResultEnum.KHONG_RO.getLabel());

        options.put("test-resultss", new HashMap<String, String>());
        options.get("test-resultss").put(TestResultEnum.KHONG_PHAN_UNG.getKey(), TestResultEnum.KHONG_PHAN_UNG.getLabel());
        options.get("test-resultss").put(TestResultEnum.CO_PHAN_UNG.getKey(), TestResultEnum.CO_PHAN_UNG.getLabel());
        options.get("test-resultss").put(TestResultEnum.KHONG_RO.getKey(), TestResultEnum.KHONG_RO.getLabel());
        options.get("test-resultss").put(TestResultEnum.ONLY_NOTICE.getKey(), TestResultEnum.ONLY_NOTICE.getLabel());

        options.put("prep-status", new HashMap<String, String>());
        options.get("prep-status").put(PqmPrepStatusEnum.TIEP_TUC.getKey(), PqmPrepStatusEnum.TIEP_TUC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getKey(), PqmPrepStatusEnum.DUNG_KHONG_CON_NGUY_CO.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getKey(), PqmPrepStatusEnum.DUNG_DO_NHIEM_HIV.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_KHAC.getKey(), PqmPrepStatusEnum.DUNG_DO_KHAC.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getKey(), PqmPrepStatusEnum.DUNG_DO_DOC_TINH.getLabel());
        options.get("prep-status").put(PqmPrepStatusEnum.MAT_DAU.getKey(), PqmPrepStatusEnum.MAT_DAU.getLabel());

        options.put("blocked", new HashMap<String, String>());
        options.get("blocked").put("1", "Không được ghi đè");
        options.get("blocked").put("0", "Được phép ghi đè");

        int year = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        options.put("years", new LinkedHashMap<String, String>());
//        options.get("years").put("0", "Tất cả");
        for (int i = year; i >= 1990; i--) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }
//        int month = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        options.put("months", new LinkedHashMap<String, String>());
//        options.get("months").put("0", "Tất cả");
        for (int i = 1; i <= 12; i++) {
            options.get("months").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        options.put("month", new LinkedHashMap<String, String>());
        for (int i = 1; i <= 12; i++) {
            options.get("month").put(String.valueOf(i), String.format("Tháng %s", i));
        }
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options;
    }

    @GetMapping(value = {"/pqm-vct/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "tab", required = false) String tab,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "earlyDiagnose", required = false) String earlyDiagnose,
            @RequestParam(name = "confirmTimeFrom", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirmTimeTo", required = false) String confirmTimeTo,
            @RequestParam(name = "earlyHivDateFrom", required = false) String earlyHivDateFrom,
            @RequestParam(name = "earlyHivDateTo", required = false) String earlyHivDateTo,
            @RequestParam(name = "exchangeTimeFrom", required = false) String exchangeTimeFrom,
            @RequestParam(name = "exchangeTimeTo", required = false) String exchangeTimeTo,
            @RequestParam(name = "registerTherapyTimeFrom", required = false) String registerTherapyTimeFrom,
            @RequestParam(name = "registerTherapyTimeTo", required = false) String registerTherapyTimeTo,
            @RequestParam(name = "registerTherapyStatus", required = false) String registerTherapyStatus,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmVctSearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);

        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("siteHtc").entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        siteIDs.add(Long.valueOf(key));
                    }
                }
                if (siteIDs.isEmpty()) {
                    siteIDs.add(Long.valueOf("-2"));
                }
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }

        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setRegisterTherapyStatus(!StringUtils.isEmpty(registerTherapyStatus) ? registerTherapyStatus : null);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setEarlyDiagnose(StringUtils.isEmpty(earlyDiagnose) ? null : earlyDiagnose);
        search.setConfirmTimeFrom(isThisDateValid(confirmTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, confirmTimeFrom) : null);
        search.setConfirmTimeTo(isThisDateValid(confirmTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, confirmTimeTo) : null);

        search.setEarlyHivDateFrom(isThisDateValid(earlyHivDateFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateFrom) : null);
        search.setEarlyHivDateTo(isThisDateValid(earlyHivDateTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateTo) : null);
        search.setExchangeTimeFrom(isThisDateValid(exchangeTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, exchangeTimeFrom) : null);
        search.setExchangeTimeTo(isThisDateValid(exchangeTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, exchangeTimeTo) : null);
        search.setRegisterTherapyTimeFrom(isThisDateValid(registerTherapyTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, registerTherapyTimeFrom) : null);
        search.setRegisterTherapyTimeTo(isThisDateValid(registerTherapyTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, registerTherapyTimeTo) : null);

        DataPage<PqmVctEntity> dataPage = vctService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Khách hàng xét nghiệm");
        form.setFileName("KhachHangXetNghiem");
        form.setSheetName("KhachHangXetNghiem");

        form.setFilter(new LinkedHashMap<>());
        if (StringUtils.isNotEmpty(search.getTab())) {
            form.getFilter().put("Tab:", search.getTab() == null ? "Tất cả" : "Thiếu thông tin");
        }
        if (StringUtils.isNotEmpty(search.getKeyword())) {
            form.getFilter().put("Từ khoá:", search.getKeyword());
        }
        if (StringUtils.isNotEmpty(search.getGenderID())) {
            form.getFilter().put("Giới tính:", search.getGenderID().equals("1") ? "Nam" : search.getGenderID().equals("2") ? "Nữ" : search.getGenderID().equals("3") ? "Không rõ" : "");
        }
        if (StringUtils.isNotEmpty(search.getObjectGroupID())) {
            form.getFilter().put("Nhóm đối tượng:", getOptions().get("test-object-group").getOrDefault(search.getObjectGroupID(), ""));
        }
        if (isThisDateValid(confirmTimeFrom)) {
            form.getFilter().put("Ngày XN khẳng định từ:", confirmTimeFrom);
        }
        if (isThisDateValid(confirmTimeTo)) {
            form.getFilter().put("Ngày XN khẳng định đến:", confirmTimeTo);
        }
        if (StringUtils.isNotEmpty(search.getEarlyDiagnose())) {
            form.getFilter().put("Kết luận nhiễm mới:", getOptions().get("early-diagnose").getOrDefault(search.getEarlyDiagnose(), ""));
        }
        if (isThisDateValid(earlyHivDateFrom)) {
            form.getFilter().put("Ngày XN nhiễm mới từ:", earlyHivDateFrom);
        }
        if (isThisDateValid(earlyHivDateTo)) {
            form.getFilter().put("Ngày XN nhiễm mới đến:", earlyHivDateTo);
        }
        if (isThisDateValid(registerTherapyTimeFrom)) {
            form.getFilter().put("Ngày ĐK điều trị từ:", registerTherapyTimeFrom);
        }
        if (isThisDateValid(registerTherapyTimeTo)) {
            form.getFilter().put("Ngày ĐK điều trị đến:", registerTherapyTimeTo);
        }
        if (StringUtils.isNotEmpty(search.getRegisterTherapyStatus())) {
            form.getFilter().put("Trạng thái đăng ký điều trị:", getOptions().get("registerTherapyStatus").getOrDefault(search.getRegisterTherapyStatus(), ""));
        }
        if (search.getSiteID() != null && search.getSiteID().size() < (getOptions().get("siteHtc").size() - 1)) {
            String text = "";
            for (Long long1 : search.getSiteID()) {
                text = text + getOptions().get("siteHtc").getOrDefault(long1 + "", "") + "; ";
            }
            form.getFilter().put("Cơ sở tư vấn xét nghiệm:", text);
        }
        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", currentUser.getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("Nguồn dữ liệu");
        headers.add("Mã khách hàng");
        headers.add("Họ và tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Nhóm đối tượng");
        headers.add("Ngày XN khẳng định");
        headers.add("Mã XN khẳng định");
        headers.add("Ngày XN nhiễm mới");
        headers.add("Kết luận nhiễm mới");
        headers.add("Ngày chuyển gửi điều trị");
        headers.add("Ngày đăng ký điều trị");
        headers.add("Mã số điều trị");
        headers.add("Cơ sở điều trị");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Số điện thoại");
        headers.add("Địa chỉ thường trú");
        headers.add("Ngày tư vấn");
        headers.add("Cơ sở tư vấn xét nghiệm");
        headers.add("Ghi chú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Ngày xét nghiệm sàng lọc");
        headers.add("Kết quả xét nghiệm sàng lọc");
        headers.add("Kết quả xét ngiệm khẳng định");
        headers.add("Ngày CS nhận kết quả KĐ");
        headers.add("Ngày KH nhận kết quả");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        for (PqmVctEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(p.getSource());
            item.add(p.getCode());
            item.add(p.getPatientName());
            item.add(p.getYearOfBirth());
            item.add(StringUtils.isEmpty(p.getGenderID()) ? "" : getOptions().get("gender").getOrDefault(p.getGenderID(), ""));
            item.add(StringUtils.isEmpty(p.getObjectGroupID()) ? "" : getOptions().get("test-object-group").getOrDefault(p.getObjectGroupID(), ""));
            item.add(TextUtils.formatDate(p.getConfirmTime(), FORMATDATE));
            item.add(p.getConfirmTestNo());
            item.add(TextUtils.formatDate(p.getEarlyHivDate(), FORMATDATE));
            item.add(StringUtils.isEmpty(p.getEarlyDiagnose()) ? "" : getOptions().get("early-diagnose").getOrDefault(p.getEarlyDiagnose(), ""));
            item.add(TextUtils.formatDate(p.getExchangeTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getRegisterTherapyTime(), FORMATDATE));
            item.add(p.getTherapyNo());
            item.add(p.getRegisteredTherapySite());
            item.add(p.getIdentityCard());
            item.add(p.getInsuranceNo());
            item.add(p.getPatientPhone());
            item.add(p.getPermanentAddress());
            item.add(TextUtils.formatDate(p.getAdvisoryeTime(), FORMATDATE));
            item.add(getOptions().get("siteHtc").getOrDefault(p.getSiteID() == null ? "x" : p.getSiteID() + "", ""));
            item.add(p.getNote());
            item.add(p.getCurrentAddress());
            item.add(TextUtils.formatDate(p.getPreTestTime(), FORMATDATE));
            item.add(StringUtils.isEmpty(p.getTestResultsID()) ? "" : getOptions().get("test-results").getOrDefault(p.getTestResultsID(), ""));
            item.add(StringUtils.isEmpty(p.getConfirmResultsID()) ? "" : getOptions().get("test-result-confirm").getOrDefault(p.getConfirmResultsID(), ""));
            item.add(TextUtils.formatDate(p.getResultsSiteTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getResultsTime(), FORMATDATE));

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportAllExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-vct-recency/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportRece(
            @RequestParam(name = "tab", required = false) String tab,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "earlyDiagnose", required = false) String earlyDiagnose,
            @RequestParam(name = "earlyHivDateFrom", required = false) String earlyHivDateFrom,
            @RequestParam(name = "earlyHivDateTo", required = false) String earlyHivDateTo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmVctRecencySearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);

        Set<Long> siteIDs = new HashSet<>();
        if (!isPAC()) {
            siteIDs.add(getCurrentUser().getSite().getID());
        } else {
            search.setProvinceID(Long.valueOf(getCurrentUser().getSiteProvince().getID()));
        }
        System.out.println("HOAAAAAAAa:" + getOptions().get("test-object-group").get("11"));
//        }
        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setTab(StringUtils.isEmpty(tab) ? null : tab);
        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setEarlyDiagnose(StringUtils.isEmpty(earlyDiagnose) ? null : earlyDiagnose);
        search.setEarlyHivDateFrom(isThisDateValid(earlyHivDateFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateFrom) : null);
        search.setEarlyHivDateTo(isThisDateValid(earlyHivDateTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, earlyHivDateTo) : null);

        DataPage<PqmVctRecencyEntity> dataPage = vctRecencyService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Khách hàng nhiễm mới");
        form.setFileName("KhachHangNhiemMoi");
        form.setSheetName("KhachHangNhiemMoi");

        form.setFilter(new LinkedHashMap<>());
        if (StringUtils.isNotEmpty(search.getTab())) {
            form.getFilter().put("Tab:", search.getTab() == null ? "Tất cả" : "Không có trong danh sách sàng lọc");
        }
        if (StringUtils.isNotEmpty(search.getKeyword())) {
            form.getFilter().put("Từ khoá:", search.getKeyword());
        }
        if (StringUtils.isNotEmpty(search.getGenderID())) {
            form.getFilter().put("Giới tính:", search.getGenderID().equals("1") ? "Nam" : search.getGenderID().equals("2") ? "Nữ" : search.getGenderID().equals("3") ? "Không rõ" : "");
        }
        if (StringUtils.isNotEmpty(search.getObjectGroupID())) {
            form.getFilter().put("Nhóm đối tượng:", getOptions().get("test-object-group").getOrDefault(search.getObjectGroupID(), ""));
        }
        if (isThisDateValid(earlyHivDateFrom)) {
            form.getFilter().put("Ngày XN nhiễm mới từ:", earlyHivDateFrom);
        }
        if (isThisDateValid(earlyHivDateTo)) {
            form.getFilter().put("Ngày XN nhiễm mới đến:", earlyHivDateTo);
        }
        if (StringUtils.isNotEmpty(search.getEarlyDiagnose())) {
            form.getFilter().put("Kết luận nhiễm mới:", getOptions().get("early-diagnose").getOrDefault(search.getEarlyDiagnose(), ""));
        }

//        if (search.getSiteID() != null && search.getSiteID().size() < (getOptions().get("siteHtc").size() - 1)) {
//            String text = "";
//            for (Long long1 : search.getSiteID()) {
//                text = text + getOptions().get("siteHtc").getOrDefault(long1 + "", "") + "; ";
//            }
//            form.getFilter().put("Cơ sở tư vấn xét nghiệm:", text);
//        } 
        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", currentUser.getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("Mã khách hàng");
        headers.add("Họ và tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Nhóm đối tượng");
        headers.add("Số CMND");
        headers.add("Mã XN khẳng định");
        headers.add("Ngày XN nhiễm mới");
        headers.add("Kết luận nhiễm mới");
        headers.add("Địa chỉ");
        headers.add("Cơ sở gửi mẫu");
        headers.add("Cơ sở khẳng định");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        for (PqmVctRecencyEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(p.getCode());
            item.add(p.getPatientName());
            item.add(p.getYearOfBirth());
            item.add(getOptions().get("gender").getOrDefault(StringUtils.isEmpty(p.getGenderID()) ? "x" : p.getGenderID(), ""));
            item.add(getOptions().get("test-object-group").getOrDefault(StringUtils.isEmpty(p.getObjectGroupID()) ? "x" : p.getObjectGroupID(), ""));
            item.add(p.getIdentityCard());
            item.add(p.getConfirmTestNo());
            item.add(TextUtils.formatDate(p.getEarlyHivDate(), FORMATDATE));
            item.add(getOptions().get("early-diagnose").getOrDefault(p.getEarlyDiagnose(), ""));

            item.add(p.getAddress());
            item.add(p.getSiteTesting());
            item.add(getOptions().get("siteHtcConfirm").getOrDefault(p.getSiteID() == null ? "x" : p.getSiteID() + "", ""));

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportAllExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-prep/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportPrep(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "startTimeFrom", required = false) String startTimeFrom,
            @RequestParam(name = "startTimeTo", required = false) String startTimeTo,
            @RequestParam(name = "examinationTimeFrom", required = false) String examinationTimeFrom,
            @RequestParam(name = "examinationTimeTo", required = false) String examinationTimeTo,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmPrepSearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        System.out.println("xx " + isPAC());
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("sitePrEP").entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        siteIDs.add(Long.valueOf(key));
                    }
                }
                if (siteIDs.isEmpty()) {
                    siteIDs.add(Long.valueOf("-2"));
                }

            }
        } else {
            siteIDs.add(getCurrentUser().getSite().getID());
        }

        search.setSiteID(siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setType(StringUtils.isEmpty(type) ? null : type);
        search.setExaminationTimeFrom(isThisDateValid(examinationTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, examinationTimeFrom) : null);
        search.setExaminationTimeTo(isThisDateValid(examinationTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, examinationTimeTo) : null);
        search.setStartTimeFrom(isThisDateValid(startTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, startTimeFrom) : null);
        search.setStartTimeTo(isThisDateValid(startTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, startTimeTo) : null);

        DataPage<PqmPrepEntity> dataPage = prepService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Khách hàng PrEP");
        form.setFileName("KhachHangPrep");
        form.setSheetName("KhachHangPrep");

        form.setFilter(new LinkedHashMap<>());
        if (StringUtils.isNotEmpty(search.getKeyword())) {
            form.getFilter().put("Từ khoá:", search.getKeyword());
        }
        if (StringUtils.isNotEmpty(search.getGenderID())) {
            form.getFilter().put("Giới tính:", search.getGenderID().equals("1") ? "Nam" : search.getGenderID().equals("2") ? "Nữ" : search.getGenderID().equals("3") ? "Không rõ" : "");
        }
        if (StringUtils.isNotEmpty(search.getObjectGroupID())) {
            form.getFilter().put("Nhóm đối tượng:", getOptions().get("test-object-group").getOrDefault(search.getObjectGroupID(), ""));
        }
        if (StringUtils.isNotEmpty(search.getType())) {
            form.getFilter().put("Loại khách hàng:", getOptions().get("type").getOrDefault(search.getType(), ""));
        }
        if (isThisDateValid(startTimeFrom)) {
            form.getFilter().put("Ngày BĐ điều trị từ:", startTimeFrom);
        }
        if (isThisDateValid(startTimeTo)) {
            form.getFilter().put("Ngày BĐ điều trị đến:", startTimeTo);
        }
        if (isThisDateValid(examinationTimeFrom)) {
            form.getFilter().put("Ngày khám từ:", examinationTimeFrom);
        }
        if (isThisDateValid(examinationTimeTo)) {
            form.getFilter().put("Ngày khám đến:", examinationTimeTo);
        }

        if (search.getSiteID() != null && search.getSiteID().size() < (getOptions().get("sitePrEP").size() - 1)) {
            String text = "";
            for (Long long1 : search.getSiteID()) {
                text = text + getOptions().get("sitePrEP").getOrDefault(long1 + "", "") + "; ";
            }
            form.getFilter().put("Cơ sở điều trị:", text);
        }
        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", currentUser.getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("Mã khách hàng");
        headers.add("Họ và tên");
        headers.add("Năm sinh");
        headers.add("Giới tính");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Số điện thoại");
        headers.add("Nhóm đối tượng");
        headers.add("Loại khách hàng");
        headers.add("Ngày bắt đầu điều trị");
        headers.add("Phác đồ PrEP");
        headers.add("Số thuốc được phát");
        headers.add("Ngày khám cuối cùng");
        headers.add("Ngày hẹn tái khám");
        headers.add("Cơ sở điều trị");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        for (PqmPrepEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(p.getCode());
            item.add(p.getFullName());
            item.add(p.getYearOfBirth());
            item.add(getOptions().get("gender").getOrDefault(p.getGenderID(), ""));

            item.add(p.getIdentityCard());
            item.add(p.getInsuranceNo());
            item.add(p.getPatientPhone());
            item.add(getOptions().get("test-object-group").getOrDefault(p.getObjectGroupID(), ""));
            item.add(getOptions().get("type").getOrDefault(p.getType(), ""));
            item.add(TextUtils.formatDate(p.getStartTime(), FORMATDATE));

            item.add(p.getTreatmentRegimen());
            item.add(p.getDaysReceived() + "");
            item.add(TextUtils.formatDate(p.getExaminationTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getAppointmentTime(), FORMATDATE));
            item.add(getOptions().get("sitePrEP").getOrDefault(p.getSiteID() == null ? "x" : p.getSiteID() + "", ""));
            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportAllExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-arv/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportAArv(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "statusOfTreatmentID", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatmentTimeFrom", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatmentTimeTo", required = false) String treatmentTimeTo,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmArvSearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();

        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("siteOpc").entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        siteIDs.add(Long.valueOf(key));
                    }
                }
                if (siteIDs.isEmpty()) {
                    siteIDs.add(Long.valueOf("-2"));
                }
            }
        } else {
            siteIDs.add(getCurrentUser().getSite().getID());
        }

        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setStatusOfTreatmentID(StringUtils.isEmpty(statusOfTreatmentID) ? null : statusOfTreatmentID);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, treatmentTimeFrom) : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, treatmentTimeTo) : null);

        DataPage<PqmArvEntity> dataPage = arvService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Khách hàng điều trị");
        form.setFileName("KhachHangARV");
        form.setSheetName("KhachHangARV");

        form.setFilter(new LinkedHashMap<>());
        if (StringUtils.isNotEmpty(search.getKeyword())) {
            form.getFilter().put("Từ khoá:", search.getKeyword());
        }
        if (StringUtils.isNotEmpty(search.getGenderID())) {
            form.getFilter().put("Giới tính:", search.getGenderID().equals("1") ? "Nam" : search.getGenderID().equals("2") ? "Nữ" : search.getGenderID().equals("3") ? "Không rõ" : "");
        }
        if (StringUtils.isNotEmpty(search.getObjectGroupID())) {
            form.getFilter().put("Nhóm đối tượng:", getOptions().get("test-object-group").getOrDefault(search.getObjectGroupID(), ""));
        }
        if (StringUtils.isNotEmpty(search.getStatusOfTreatmentID())) {
            form.getFilter().put("Trạng thái điều trị:", getOptions().get("status-of-treatment").getOrDefault(search.getStatusOfTreatmentID(), ""));
        }
        if (isThisDateValid(treatmentTimeFrom)) {
            form.getFilter().put("Ngày điều trị ARV từ:", treatmentTimeFrom);
        }
        if (isThisDateValid(treatmentTimeTo)) {
            form.getFilter().put("Ngày điều trị ARV đến:", treatmentTimeTo);
        }

        if (search.getSiteID() != null && search.getSiteID().size() < (getOptions().get("sitePrEP").size() - 1)) {
            String text = "";
            for (Long long1 : search.getSiteID()) {
                text = text + getOptions().get("siteOpc").getOrDefault(long1 + "", "") + "; ";
            }
            form.getFilter().put("Cơ sở điều trị:", text);
        }
        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", currentUser.getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("Mã bệnh án");
        headers.add("Họ và tên");
        headers.add("Ngày sinh");
        headers.add("Giới tính");
        headers.add("Trạng thái điều trị");
        headers.add("Ngày bắt đầu điều trị ARV");
        headers.add("Ngày điều trị ARV");
        headers.add("Ngày đăng ký");
        headers.add("Ngày khám gần nhất");
        headers.add("Ngày XN TLVR gần nhất");
        headers.add("Ngày bắt đầu dự phòng Lao");
        headers.add("Ngày kết thúc");
        headers.add("Số CMND");
        headers.add("Số thẻ BHYT");
        headers.add("Số điện thoại");
        headers.add("Nhóm đối tượng");
        headers.add("Địa chỉ thường trú");
        headers.add("Địa chỉ hiện tại");
        headers.add("Cơ sở điều trị");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        for (PqmArvEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(p.getCode());
            item.add(p.getFullName());
            item.add(TextUtils.formatDate(p.getDob(), FORMATDATE));
            item.add(getOptions().get("gender").getOrDefault(p.getGenderID(), ""));
            item.add(getOptions().get("status-of-treatment").getOrDefault(StringUtils.isEmpty(p.getStatusOfTreatmentID()) ? "x" : p.getStatusOfTreatmentID() + "", ""));
            item.add(TextUtils.formatDate(p.getFirstTreatmentTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getTreatmentTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getRegistrationTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getExaminationTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getTestTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getInhFromTime(), FORMATDATE));
            item.add(TextUtils.formatDate(p.getEndTime(), FORMATDATE));

            item.add(p.getIdentityCard());
            item.add(p.getInsuranceNo());
            item.add(p.getPatientPhone());
            item.add(getOptions().get("test-object-group").getOrDefault(p.getObjectGroupID(), ""));
            item.add(p.getPermanentAddress());
            item.add(p.getCurrentAddress());
            item.add(getOptions().get("siteOpc").getOrDefault(p.getSiteID() == null ? "x" : p.getSiteID() + "", ""));

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportAllExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-drug-estimates/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportEstimate(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999999") int size, PqmDrugSearch search
    ) throws Exception {
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(99999999);
        search.setYear(y);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugEstimateEntity> dataPages = drugEstimateService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        if (dataPages.getData() != null && !dataPages.getData().isEmpty()) {
            for (PqmDrugEstimateEntity e : dataPages.getData()) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : sites);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);

        DataPage<PqmDrugEstimateEntity> dataPage = drugEstimateService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Kế hoạch cung ứng thuốc ARV");
        form.setFileName("KeHoachCungUngThuoc");
        form.setSheetName("KeHoachCungUngThuoc");

        form.setFilter(new LinkedHashMap<>());
        form.getFilter().put("Năm:", search.getYear() + "");

        if (StringUtils.isNotEmpty(search.getSiteID())) {
            form.getFilter().put("Cơ sở:", siteMap.getOrDefault(search.getSiteID() + "", ""));
        }

        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", getCurrentUser().getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("STT");
        headers.add("Mã KCB");
        headers.add("Cơ sở");
        headers.add("Nguồn thuốc");
        headers.add("Tên thuốc, hàm lượng");
        headers.add("Đơn vị tính");
        headers.add("Dạng bào chế");
        headers.add("Đường dùng");
        headers.add("Ước tính tồn kho đầu năm");
        headers.add("Ước tính sử dụng trong năm");
        headers.add("Ước tính tồn kho cuối năm");
        headers.add("Tổng nhu cầu");
        headers.add("Nhu cầu phân bổ");
        headers.add("");
        headers.add("");
        headers.add("");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        int i = 1;
        for (PqmDrugEstimateEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(i++ + "");
            item.add(p.getSiteCode());
            item.add(p.getSiteName());
            item.add(p.getSource());
            item.add(p.getDrugName());
            item.add(p.getUnit());
            item.add(p.getPacking());
            item.add(p.getHowToUse());
            item.add(p.getEarlyEstimate() + "");
            item.add(p.getInEstimate() + "");
            item.add(p.getFinalEstimate() + "");
            item.add(p.getExigencyTotal() + "");
            item.add(p.getExigency0() + "");
            item.add(p.getExigency1() + "");
            item.add(p.getExigency2() + "");
            item.add(p.getExigency3() + "");

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportEstimateExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-drug-plans/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportPlan(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999999") int size, PqmDrugSearch search
    ) throws Exception {
        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(999999999);
        search.setMonth(m);
        search.setYear(y);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugPlanEntity> dataPages = drugPlanService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        if (dataPages.getData() != null && !dataPages.getData().isEmpty()) {
            for (PqmDrugPlanEntity e : dataPages.getData()) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : sites);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setMonth(m);
        search.setYear(y);

        DataPage<PqmDrugPlanEntity> dataPage = drugPlanService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Tình hình sử dụng thuốc ARV");
        form.setFileName("TinhHinhSuDungThuocARV");
        form.setSheetName("TinhHinhSuDungThuocARV");

        form.setFilter(new LinkedHashMap<>());
        form.getFilter().put("Tháng:", search.getMonth() + "");
        form.getFilter().put("Năm:", search.getYear() + "");

        if (StringUtils.isNotEmpty(search.getSiteID())) {
            form.getFilter().put("Cơ sở:", siteMap.getOrDefault(search.getSiteID() + "", ""));
        }

        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", getCurrentUser().getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("STT");
        headers.add("Mã KCB");
        headers.add("Cơ sở");
        headers.add("Nguồn thuốc");
        headers.add("Tên thuốc, hàm lượng, đường dùng, dạng bào chế");
        headers.add("Quy cách đóng gói");
        headers.add("Đơn vị tính");
        headers.add("Số lô");
        headers.add("Hạn sử dụng");
        headers.add("Số lượng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        int i = 1;
        for (PqmDrugPlanEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(i++ + "");
            item.add(p.getSiteCode());
            item.add(p.getSiteName());
            item.add(p.getSource());
            item.add(p.getDrugName());
            item.add(p.getPacking());
            item.add(p.getUnit());

            item.add(p.getLotNumber() + "");
            item.add(p.getExpiryDate() + "");
            item.add(p.getBeginning() + "");
            item.add(p.getInThePeriod() + "");
            item.add(p.getPatient() + "");
            item.add(p.getTransfer() + "");
            item.add(p.getLoss() + "");
            item.add(p.getEnding() + "");

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportPlanExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-drug-elmises/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportAElmis(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "quarter", required = false, defaultValue = "") String quarter,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999999") int size, PqmDrugeLMISESearch search
    ) throws Exception {
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        int q = StringUtils.isEmpty(quarter) ? TextUtils.getQuarter(new Date()) + 1 : Integer.valueOf(quarter);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(999999999);
        search.setYear(y);
        search.setQuarter(q);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugeLMISEEntity> dataPages = drugeLMISEService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : siteOpc) {
            siteMap.put(String.valueOf(site.getID()), site.getName());

        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);

        DataPage<PqmDrugeLMISEEntity> dataPage = drugeLMISEService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Tình hình cung ứng, sử dụng thuốc ARV");
        form.setFileName("TinhHinhCungUngSuDung");
        form.setSheetName("TinhHinhCungUngSuDung");

        form.setFilter(new LinkedHashMap<>());
        form.setFilter(new LinkedHashMap<>());
        form.getFilter().put("Quý:", search.getQuarter() + "");
        form.getFilter().put("Năm:", search.getYear() + "");

        if (search.getSiteID() != null) {
            form.getFilter().put("Cơ sở:", siteMap.getOrDefault(search.getSiteID() + "", ""));
        }

        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", getCurrentUser().getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("STT");
        headers.add("Mã CSKCB");
        headers.add("Tên CSKCB");
        headers.add("Mã thuốc");
        headers.add("Tên thuốc");
        headers.add("Số đăng ký");
        headers.add("Quyết định thầu");
        headers.add("Số lượng dự trù");
        headers.add("Số lượng sử dụng");
        headers.add("Số lượng tồn kho");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        int i = 1;
        for (PqmDrugeLMISEEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(i++ + "");
            item.add(p.getSiteCode());
            item.add(p.getSiteName());
            item.add(p.getDrugCode());
            item.add(p.getDrugName());
            item.add(p.getSoDangKy());
            item.add(p.getTtThau());
            item.add(p.getDuTru() + "");
            item.add(p.getTongSuDung() + "");
            item.add(p.getTonKho() + "");

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmExportAllExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-shi-arts/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportART(
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmShiArtSearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();

        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int m = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);
        search.setMonth(m);
        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setProvinceID(currentUser.getSite().getProvinceID());

        DataPage<PqmShiArtEntity> dataPage = shiArtService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Theo dõi bệnh nhân nhận thuốc ARV");
        form.setFileName("TheoDoiBenhNhanNhanThuoc");
        form.setSheetName("TheoDoiBenhNhanNhanThuoc");

        form.setFilter(new LinkedHashMap<>());
        form.getFilter().put("Tháng:", search.getMonth() + "");
        form.getFilter().put("Năm:", search.getYear() + "");

        if (search.getSiteID() != null) {
            form.getFilter().put("Cơ sở:", getOptions().get("siteOpc").getOrDefault(search.getSiteID() + "", ""));
        }

        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", getCurrentUser().getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("STT");
        headers.add("Mã CSKCB");
        headers.add("Tên CSKCB");
        headers.add("Mã vùng");
        headers.add("Số bệnh nhân");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Nhỡ hẹn tái khám");
        headers.add("");
        headers.add("Bỏ trị");
        headers.add("");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        int i = 1;
        for (PqmShiArtEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(i++ + "");
            item.add(p.getSiteCode());
            item.add(getOptions().get("siteOpc").getOrDefault(p.getSiteID() + "", ""));
            item.add(p.getZipCode());

            item.add(p.getBnnt() + "");
            item.add(p.getBnm() + "");
            item.add(p.getBnqldt() + "");
            item.add(p.getBndt12t() + "");
            item.add(p.getBnnhtk() + "");
            item.add(p.getTlbnnhtk() + "");
            item.add(p.getBnbttk() + "");
            item.add(p.getBnbtlk() + "");

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmShiArtExcel(form, EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pqm-shi-mmds/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportMMD(
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size, PqmShiMmdSearch search
    ) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);
        Integer currentMonth = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        search.setYear(year > 0 ? year : Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")));
        search.setMonth(month > 0 ? month : currentMonth);
        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setProvinceID(currentUser.getSite().getProvinceID());
        DataPage<PqmShiMmdEntity> dataPage = shiMmdService.find(search);

        PqmExportAllForm form = new PqmExportAllForm();
        form.setTitle("Thống kê tình hình kê đơn thuốc ARV");
        form.setFileName("ThongKeTinhHinhDonThuoc");
        form.setSheetName("ThongKeTinhHinhDonThuoc");

        form.setFilter(new LinkedHashMap<>());
        form.getFilter().put("Tháng:", search.getMonth() + "");
        form.getFilter().put("Năm:", search.getYear() + "");

        if (search.getSiteID() != null) {
            form.getFilter().put("Cơ sở:", getOptions().get("siteOpc").getOrDefault(search.getSiteID() + "", ""));
        }

        form.getFilter().put(" ", "");
        form.getFilter().put("Đơn vị xuất báo cáo:", getCurrentUser().getSite().getName());
        form.getFilter().put("Thời gian xuất báo cáo:", TextUtils.formatDate(new Date(), FORMATDATE));

        LinkedList<String> headers = new LinkedList<>();
        headers.add("STT");
        headers.add("Mã CSKCB");
        headers.add("Tên CSKCB");
        headers.add("Mã vùng");
        headers.add("Tổng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Dưới 1 tháng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("1-2 tháng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("2-3 tháng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("3 tháng");
        headers.add("");
        headers.add("");
        headers.add("");
        headers.add("Tỷ lệ BN cấp thuốc 3 tháng(%)");
        headers.add("");

        form.setHead(headers);

        LinkedList<LinkedList<String>> datas = new LinkedList<>();
        int i = 1;
        for (PqmShiMmdEntity p : dataPage.getData()) {
            LinkedList<String> item = new LinkedList<>();
            item.add(i++ + "");
            item.add(p.getSiteCode());
            item.add(getOptions().get("siteOpc").getOrDefault(p.getSiteID() + "", ""));
            item.add(p.getZipCode());

            item.add(p.getTotalTurnTk() + "");
            item.add(p.getTotalTurnLk() + "");
            item.add(p.getTotalPatientTk() + "");
            item.add(p.getTotalPatientLk() + "");
            item.add(p.getTotalTurnLess1MonthTk() + "");
            item.add(p.getTotalTurnLess1MonthLk() + "");
            item.add(p.getTotalPatientLess1MonthTk() + "");
            item.add(p.getTotalPatientLess1MonthLk() + "");
            item.add(p.getTotalTurn1To2MonthTk() + "");
            item.add(p.getTotalTurn1To2MonthLk() + "");
            item.add(p.getTotalPatient1To2MonthTk() + "");
            item.add(p.getTotalPatient1To2MonthLk() + "");
            item.add(p.getTotalTurn2To3MonthTk() + "");
            item.add(p.getTotalTurn2To3MonthLk() + "");
            item.add(p.getTotalPatient2To3MonthTk() + "");
            item.add(p.getTotalPatient2To3MonthLk() + "");
            item.add(p.getTotalTurn3MonthTk() + "");
            item.add(p.getTotalTurn3MonthLk() + "");
            item.add(p.getTotalPatient3MonthTk() + "");
            item.add(p.getTotalPatient3MonthLk() + "");
            item.add(p.getRatio3MonthTk() + "");
            item.add(p.getRatio3MonthLk() + "");

            datas.add(item);
        }

        form.setDatas(datas);

        return exportExcel(new PqmShiMMDExcel(form, EXTENSION_EXCEL_X));
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

}
