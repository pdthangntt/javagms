package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.CdServiceEnum;
import com.gms.entity.constant.ConfirmTypeEnum;
import com.gms.entity.constant.CustomerTypeEnum;
import com.gms.entity.constant.ExchangeServiceEnum;
import com.gms.entity.constant.ExchangeStatusEnum;
import com.gms.entity.constant.FeedbackStatusEnum;
import com.gms.entity.constant.GsphStatusEnum;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import com.gms.entity.constant.LaoVariableEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.ReferralSourceEnum;
import com.gms.entity.constant.ResultAntiEnum;
import com.gms.entity.constant.ResultPcrHivEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.constant.TestMethodEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.HtcVisitLaytestEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.HtcLaytestForm;
import com.gms.entity.form.HtcVisitForm;
import com.gms.entity.input.LaytestSearch;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.validate.HtcVisitPaidValidate;
import com.gms.entity.validate.HtcVisitTransferGsphValidate;
import com.gms.entity.validate.HtcVisitValidate;
import com.gms.service.CommonService;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcLaytestAgencyService;
import com.gms.service.HtcLaytestService;
import com.gms.service.HtcVisitLaytestService;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcArvService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_htc")
public class HtcVisitController extends HtcController {

    @Autowired
    private HtcLaytestService htcLaytestService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private HtcVisitValidate htcVisitValidate;
    @Autowired
    private HtcVisitPaidValidate htcVisitPaidValidate;
    @Autowired
    private HtcVisitTransferGsphValidate gsphValidate;
    @Autowired
    private CommonService commonService;
    @Autowired
    private HtcVisitLaytestService htcVisitLaytestService;
    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private HtcLaytestAgencyService agencyService;
    @Autowired
    private OpcArvService opcArvService;

    protected HashMap<String, HashMap<String, String>> getOptions(boolean scan) {
        HashMap<String, HashMap<String, String>> options = super.getOptions();

        options.put("tookTest", new HashMap<String, String>());
        options.get("tookTest").put("", "Chọn câu trả lời");
        options.get("tookTest").put(AnswerEnum.YES.getKey().toString(), AnswerEnum.YES.getLabel());
        options.get("tookTest").put(AnswerEnum.NO.getKey().toString(), AnswerEnum.NO.getLabel());

        options.put("feedbackStatus", new HashMap<String, String>());
        options.get("feedbackStatus").put("", "Chọn trạng thái đối chiếu");
        options.get("feedbackStatus").put(FeedbackStatusEnum.NOT.getKey(), FeedbackStatusEnum.NOT.getLabel());
        options.get("feedbackStatus").put(FeedbackStatusEnum.COLLATED.getKey(), FeedbackStatusEnum.COLLATED.getLabel());
        options.get("feedbackStatus").put(FeedbackStatusEnum.WAITED.getKey(), FeedbackStatusEnum.WAITED.getLabel());
        options.get("feedbackStatus").put(FeedbackStatusEnum.REFUSED.getKey(), FeedbackStatusEnum.REFUSED.getLabel());
        options.get("feedbackStatus").put(FeedbackStatusEnum.CONFIRMED.getKey(), FeedbackStatusEnum.CONFIRMED.getLabel());

        options.put("controlLine", new HashMap<String, String>());
        options.get("controlLine").put("", "Chọn câu trả lời");
        options.get("controlLine").put(AnswerEnum.YES.getKey().toString(), AnswerEnum.YES.getLabel());
        options.get("controlLine").put(AnswerEnum.NO.getKey().toString(), AnswerEnum.NO.getLabel());

        options.put("testLine", new HashMap<String, String>());
        options.get("testLine").put("", "Chọn câu trả lời");
        options.get("testLine").put(AnswerEnum.YES.getKey().toString(), AnswerEnum.YES.getLabel());
        options.get("testLine").put(AnswerEnum.NO.getKey().toString(), AnswerEnum.NO.getLabel());

        options.put("exchangeStatus", new HashMap<String, String>());
        options.get("exchangeStatus").put("", "Chọn câu trả lời");
        options.get("exchangeStatus").put(ExchangeStatusEnum.SUCCESS.getKey(), ExchangeStatusEnum.SUCCESS.getLabel());
        options.get("exchangeStatus").put(ExchangeStatusEnum.FAIL.getKey(), ExchangeStatusEnum.FAIL.getLabel());

        options.put("gsphStatus", new HashMap<String, String>());
        options.get("gsphStatus").put("", "Chọn trạng thái chuyển GSPH");
        options.get("gsphStatus").put(GsphStatusEnum.CHUA_CHUYEN.getKey(), "Chưa chuyển");
        options.get("gsphStatus").put(GsphStatusEnum.DA_CHUYEN.getKey(), "Đã chuyển");

        //Cơ sở điều trị
        List<SiteEntity> siteOpc = siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID());
        options.put("siteOpc", new HashMap<String, String>());
        options.get("siteOpc").put("", "Chọn cơ sở điều trị");
        for (SiteEntity site : siteOpc) {
            options.get("siteOpc").put(String.valueOf(site.getID()), site.getName());
        }
        options.get("siteOpc").put("-1", "Cơ sở khác");

        options.put("biology-product", options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST));
        if (scan) {
            HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
            //very config sinh phẩm
            if (siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null).equals("")) {
                options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null).split(",")));
            }
        }

        // Thêm enum
        addEnumOption(options, ParameterEntity.CD_SERVICE, CdServiceEnum.values(), "Chọn dịch vụ");
        addEnumOption(options, ParameterEntity.CONFIRM_TYPE, ConfirmTypeEnum.values(), "Chọn loại hình xét nghiệm khẳng định");
        addEnumOption(options, ParameterEntity.EXCHANGE_SERVICE, ExchangeServiceEnum.values(), "Chọn dịch vụ tư vấn chuyển gửi");
        addEnumOption(options, ParameterEntity.RESULT_ANTI, ResultAntiEnum.values(), "Chọn KQXN Kháng nguyên/Kháng thể");
        addEnumOption(options, ParameterEntity.RESULT_PCR_HIV, ResultPcrHivEnum.values(), "Chọn kết quả xét nghiệm PCR HIV");
        addEnumOption(options, ParameterEntity.TEST_METHOD, TestMethodEnum.values(), "Chọn phương pháp XN sàng lọc");
        addEnumOption(options, ParameterEntity.CUSTOMER_TYPE, CustomerTypeEnum.values(), "Chọn loại bệnh nhân");
        addEnumOption(options, ParameterEntity.LAO_VARIABLE, LaoVariableEnum.values(), "Chọn thể lao");

        //custom kết quả tlvr nhiễm mới
        HashMap<String, String> viralloads = new HashMap<>();
        for (Map.Entry<String, String> entry : options.get(ParameterEntity.VIRUS_LOAD).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            viralloads.put(key, value);
        }
        options.put(ParameterEntity.VIRUS_LOAD + "-custom", viralloads);
        options.get(ParameterEntity.VIRUS_LOAD + "-custom").remove("5");

        return options;
    }

    @RequestMapping(value = {"/htc/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "test_results_id", required = false, defaultValue = "") String testResultsID,
            @RequestParam(name = "confirm_results_id", required = false, defaultValue = "") String confirmResultsID,
            @RequestParam(name = "confirm_test_status", required = false, defaultValue = "") String confirmTestStatus,
            @RequestParam(name = "advisorye_time", required = false) String advisoryeTime,
            @RequestParam(name = "advisorye_time_from", required = false) String advisoryeTimeFrom,
            @RequestParam(name = "advisorye_time_to", required = false) String advisoryeTimeTo,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "exchange_time_from", required = false) String exchangeTimeFrom,
            @RequestParam(name = "exchange_time_to", required = false) String exchangeTimeTo,
            @RequestParam(name = "therapy_exchange_status", required = false, defaultValue = "") String therapyExchangeStatus,
            @RequestParam(name = "feedback_status", required = false, defaultValue = "") String feedbackStatus,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "gsph_status", required = false, defaultValue = "") String gsphStatus,
            @RequestParam(name = "status-request", required = false) String statusRequests,
            @RequestParam(name = "pid", required = false, defaultValue = "") String pid,
            @RequestParam(name = "early_diagnose", required = false, defaultValue = "") String earlyDiagnose,
            @RequestParam(name = "service", defaultValue = "", required = false) String service,
            @RequestParam(name = "objects", defaultValue = "", required = false) String objects) throws ParseException, CloneNotSupportedException {

        LoggedUser currentUser = getCurrentUser();
        List<String> lobjectIDs = Arrays.asList(objects.split(",", -1));
        List<String> serviceIDs = Arrays.asList(service.split(",", -1));
        HtcSearch search = new HtcSearch();
        search.setAdvisoryeTime(StringUtils.isEmpty(advisoryeTime) ? advisoryeTime : advisoryeTime.contains("d") || advisoryeTime.contains("m") || advisoryeTime.contains("y") ? "" : advisoryeTime);
        search.setAdvisoryeTimeFrom(StringUtils.isEmpty(advisoryeTimeFrom) ? advisoryeTimeFrom : advisoryeTimeFrom.contains("d") || advisoryeTimeFrom.contains("m") || advisoryeTimeFrom.contains("y") ? "" : advisoryeTimeFrom);
        search.setAdvisoryeTimeTo(StringUtils.isEmpty(advisoryeTimeTo) ? advisoryeTimeTo : advisoryeTimeTo.contains("d") || advisoryeTimeTo.contains("m") || advisoryeTimeTo.contains("y") ? "" : advisoryeTimeTo);
        search.setExchangeTimeFrom(StringUtils.isEmpty(exchangeTimeFrom) ? exchangeTimeFrom : exchangeTimeFrom.contains("d") || exchangeTimeFrom.contains("m") || exchangeTimeFrom.contains("y") ? "" : exchangeTimeFrom);
        search.setExchangeTimeTo(StringUtils.isEmpty(exchangeTimeTo) ? exchangeTimeTo : exchangeTimeTo.contains("d") || exchangeTimeTo.contains("m") || exchangeTimeTo.contains("y") ? "" : exchangeTimeTo);
        search.setConfirmTimeFrom(StringUtils.isEmpty(confirmTimeFrom) ? confirmTimeFrom : confirmTimeFrom.contains("d") || confirmTimeFrom.contains("m") || confirmTimeFrom.contains("y") ? "" : confirmTimeFrom);
        search.setConfirmTimeTo(StringUtils.isEmpty(confirmTimeTo) ? confirmTimeTo : confirmTimeTo.contains("d") || confirmTimeTo.contains("m") || confirmTimeTo.contains("y") ? "" : confirmTimeTo);
        search.setTestResultsID(testResultsID);
        search.setConfirmTestStatus(confirmTestStatus);
        search.setTherapyExchangeStatus(therapyExchangeStatus);
        search.setFeedbackStatus(feedbackStatus);
        search.setConfirmResultsID(confirmResultsID);
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());
        if (lobjectIDs.size() == 1 && lobjectIDs.contains("")) {
            search.setObjectGroupID(null);
        } else {
            search.setObjectGroupID(new HashSet<String>());
            for (String object : lobjectIDs) {
                search.getObjectGroupID().add(object);
            }
        }
        if (serviceIDs.size() == 1 && serviceIDs.contains("")) {
            search.setSearchServiceID(null);
        } else {
            search.setSearchServiceID(new HashSet<String>());
            for (String object : serviceIDs) {
                search.getSearchServiceID().add(object);
            }
        }
        if (name != null && !"".equals(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        if (code != null && !"".equals(code)) {
            search.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        if (StringUtils.isNotEmpty(earlyDiagnose)) {
            search.setEarlyDiagnose(earlyDiagnose);
        }

        search.setPageIndex(page);
        search.setPageSize(size);
        search.setRemove(false);
        search.setGsphStatus(StringUtils.isNotEmpty(gsphStatus) ? gsphStatus : null);
        search.setRequestStatus(StringUtils.isEmpty(statusRequests) ? null : statusRequests);
        switch (tab) {
            case "positive":
                search.setConfirmResultsID("2");
                break;
            case "confirm":
                search.setConfirm(true);
                break;
            case "opc":
                search.setOpc(true);
                break;
            case "remove":
                search.setRemove(true);
                break;
            case "not-receive":
                search.setNotReceive(true);
                search.setConfirmResultsID("2");
                break;
            case "confirm-create":
                search.setCofirmCreated(true);
                break;
        }

        //Bắt thêm dịch vụ
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            search.setFilter(1);
            search.setCreatedBY(currentUser.getUser().getID());
            search.setServiceID(new HashSet<String>());
            search.getServiceID().add(currentUser.getUser().getServiceVisitID());
        }

        DataPage<HtcVisitEntity> dataPage = htcVisitService.find(search);

        Set<Long> siteIDs = new HashSet<>();
        if (dataPage.getData() != null) {
            for (HtcVisitEntity item : dataPage.getData()) {
                if (item.getSiteConfirmTest() == null || "".equals(item.getSiteConfirmTest())) {
                    continue;
                }
                try {
                    siteIDs.add(Long.valueOf(item.getSiteConfirmTest()));
                } catch (NumberFormatException e) {
                }
            }
        }
        HashMap<String, String> confirmTestStatusOptions = getOptions().get(ParameterEntity.CONFIRM_TEST_STATUS);
        confirmTestStatusOptions.remove("3");
        HashMap<String, String> therapyExchangeStatusOptions = getOptions().get(ParameterEntity.THERAPY_EXCHANGE_STATUS);
//        therapyExchangeStatusOptions.remove("0");
        //Total tab - all
        HtcSearch totalForm = new HtcSearch();
        totalForm.setRemove(false);
        totalForm.setSiteID(new HashSet<Long>());
        totalForm.getSiteID().add(currentUser.getSite().getID());
        if (currentUser.getUser().getServiceVisitID() != null && !"".equals(currentUser.getUser().getServiceVisitID())) {
            totalForm.setFilter(1);
            totalForm.setCreatedBY(currentUser.getUser().getID());
            totalForm.setServiceID(new HashSet<String>());
            totalForm.getServiceID().add(currentUser.getUser().getServiceVisitID());
        }
        model.addAttribute("totalAll", htcVisitService.count(totalForm));

        HtcSearch positiveForm = totalForm.clone();
        positiveForm.setConfirmResultsID("2");

        model.addAttribute("totalPositive", htcVisitService.count(positiveForm));

        HtcSearch notReceiveForm = totalForm.clone();
        notReceiveForm.setConfirmResultsID("2");
        notReceiveForm.setNotReceive(true);
        model.addAttribute("totalNotReceive", htcVisitService.count(notReceiveForm));

        HtcSearch confirmCreated = totalForm.clone();
        confirmCreated.setCofirmCreated(true);
        model.addAttribute("confirmCreate", htcVisitService.count(confirmCreated));

        HtcSearch confirmForm = totalForm.clone();
        confirmForm.setConfirm(true);
        model.addAttribute("totalConfirm", htcVisitService.count(confirmForm));

        HtcSearch opcForm = totalForm.clone();
        opcForm.setOpc(true);
        model.addAttribute("totalOpc", htcVisitService.count(opcForm));

        HashMap<String, String> statusRequest = new LinkedHashMap<>();
        statusRequest.put("", "Tất cả");
        statusRequest.put("1", "Chưa rà soát");
        statusRequest.put("2", "Đã rà soát");

        HashMap<String, String> earlyDia = getOptions().get(ParameterEntity.EARLY_DIAGNOSE);
        earlyDia.put("0", "Không có thông tin");

        model.addAttribute("title", "Khách hàng");
        model.addAttribute("smallTitle", "Tư vấn & xét nghiệm");
        model.addAttribute("testResultSoptions", getOptions().get(ParameterEntity.TEST_RESULTS));
        model.addAttribute("earlyDiagnose", earlyDia);
        model.addAttribute("testResultConfirmOptions", getOptions().get(ParameterEntity.TEST_RESULTS_CONFIRM));
        model.addAttribute("genderOptions", getOptions().get(ParameterEntity.GENDER));
        model.addAttribute("isPmtct", currentUser.getSite().getServiceIDs().contains(ServiceEnum.PMTCT.getKey()));
        model.addAttribute("confirmTestStatusOptions", confirmTestStatusOptions);
        model.addAttribute("therapyExchangeStatusOptions", therapyExchangeStatusOptions);
        model.addAttribute("feedbackStatusOption", getOptions(false).get("feedbackStatus"));
        model.addAttribute("gsphStatusOptions", getOptions(false).get("gsphStatus"));
        model.addAttribute("options", getOptions());
        model.addAttribute("search_object", lobjectIDs.size() == parameterService.getTestObjectGroup().size() ? "" : lobjectIDs.toArray(new String[lobjectIDs.size()]));
        model.addAttribute("search_service", serviceIDs.size() == parameterService.getServiceTest().size() ? "" : serviceIDs.toArray(new String[serviceIDs.size()]));
        model.addAttribute("sites", getSite(siteIDs));
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("statusRequest", statusRequest);
        model.addAttribute("printTemplate", parameterService.getSiteConfig(currentUser.getSite().getID(), SiteConfigEnum.HTC_PHIEU_XN.getKey()));
        return "backend/htc/index";
    }

    @RequestMapping(value = "/htc/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long visitID,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisit = htcVisitService.findOne(visitID);
        if (!htcVisit.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());

        }

        htcVisitService.delete(htcVisit.getID());
        htcVisitService.log(visitID, "Xoá khách hàng");

        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect(UrlUtils.htcIndex());
    }

    @RequestMapping(value = {"/htc/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            @RequestParam(name = "laytest_id", defaultValue = "") Long laytestID,
            HtcVisitForm form,
            RedirectAttributes redirectAttributes) throws ParseException {

        if (laytestID != null) {

            HtcLaytestEntity entity = htcLaytestService.findOne(laytestID);
            StaffEntity staff = staffService.findOne(entity.getCreatedBY());

            if (form == null) {
                form = new HtcVisitForm();
            }

            // Mặc định lấy dự án của cơ sở hiện tại
            if (entity != null) {
                form.setFromVisit(form.getEntityFromLaytest(entity));
                form.setLaytestID(laytestID);
                form.setReferralSource(new ArrayList<>(Arrays.asList(ReferralSourceEnum.TC_CONG_DONG.getKey())));

                form.setApproacherNo(parameterService.getStaffConfig(staff.getID()).get(StaffConfigEnum.LAYTEST_STAFF_CODE.getKey()));
                form.setAdvisoryeTime(TextUtils.formatDate(entity.getAdvisoryeTime(), FORMATDATE));
                form.setPreTestTime(TextUtils.formatDate(entity.getAdvisoryeTime(), FORMATDATE));
                form.setIsAgreePreTest(AnswerEnum.YES.getKey().toString());
                form.setBioName(entity.getBioName());
                form.setTestResultsID(entity.getTestResultsID());
                form.setCdService(CdServiceEnum.GET_BLOOD.getKey());
                form.setTestMethod(TestMethodEnum.KHANG_THE.getKey());
            }
        }

        // Set mã tự tăng 
        String codeGen = htcVisitService.getCode();
        if (form.getLaytestID() == null) {
            form.setCode(codeGen);
        }

        //Mặc định dân tộc kinh
        form.setDefaultProject(getCurrentUser().getSite().getProjectID());
        form.setDisableEarlyHiv(false);
        form.setDisableVirusLoad(false);
        form.setRaceID(RaceEnum.KINH.getKey());
        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions(true));
        return "backend/htc/new";
    }

    @RequestMapping(value = {"/htc/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid HtcVisitForm form,
            @RequestParam(name = "laytest_id", defaultValue = "") Long laytestID,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws ParseException {

        form.setDefaultProject(getCurrentUser().getSite().getProjectID());
        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);

        form.setSiteID(getCurrentUser().getSite().getID());
        if (!"-1".equals(form.getArrivalSiteID()) && StringUtils.isNotEmpty(form.getArrivalSiteID())) {
            form.setArrivalSite(siteService.findOne(Long.parseLong(form.getArrivalSiteID())).getShortName());
        }

        form.setCurrentSiteID(getCurrentUser().getSite().getID());
        htcVisitValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới thông tin khách hàng thất bại.");
            model.addAttribute("options", getOptions(true));
            return "backend/htc/new";
        }

        // Gán giá trị cho entity
        try {
            if (TherapyExchangeStatusEnum.CHUA_CHUYEN.getKey().equals(form.getTherapyExchangeStatus())) {
                form.setArvExchangeResult(ArvExchangeEnum.CHUA_DONG_Y.getKey());
            }

            HtcVisitEntity htcEntity = form.getHtcVisit(null, getCurrentUser().getSite());

            if (form.getLaytestID() != null) {
                HtcLaytestEntity findOne = htcLaytestService.findOne(form.getLaytestID());

                if (findOne == null) {
                    model.addAttribute("error", "Tiếp nhận thông tin khách hàng thất bại.");
                    model.addAttribute("options", getOptions());
                    return "backend/htc/new";
                }
                htcEntity.setServiceID(ServiceTestEnum.KC.getKey());
                HtcVisitLaytestEntity htcVisitLaytest = htcVisitLaytestService.getHtcVisitLaytest(findOne);
                htcVisitLaytest.setAcceptDate(new Date());
                htcEntity = commonService.saveHtcAndLaytest(htcEntity, htcVisitLaytest, findOne);
                // gửi mail và thông báo
                StaffEntity staff = staffService.findOne(findOne.getUpdatedBY());
                if (staff != null && staff.getEmail() != null && !"".equals(staff.getEmail())) {
                    sendEmailNotify(staff.getEmail(),
                            String.format("Khách hàng mã %s đã được tiếp nhận", findOne.getCode()),
                            String.format(
                                    "Khách hàng mã %s được tiếp nhận bởi cơ sở %s lúc %s ",
                                    findOne.getCode(), getCurrentUser().getSite().getName(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                            ));
                    staffService.sendMessage(findOne.getUpdatedBY(), "Khách hàng đã được tiếp nhận", String.format("Mã khách hàng: %s", findOne.getCode()), String.format("%s?code=%s", UrlUtils.laytest(), findOne.getCode()));
                }
            } else {
                htcEntity = htcVisitService.save(htcEntity);
                if (TherapyExchangeStatusEnum.DA_CHUYEN.getKey().equals(form.getTherapyExchangeStatus())) {
                    String siteName = htcEntity.getArrivalSiteID() == -1 ? htcEntity.getArrivalSite() : siteService.findOne(htcEntity.getArrivalSiteID()).getName();
                    htcVisitService.log(htcEntity.getID(), "Khách hàng được chuyển tới cơ sở " + siteName);
                }
                htcVisitService.log(htcEntity.getID(), "Thêm mới khách hàng");
            }

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "htc-new".equals(form.getPageRedirect())) {
                return redirect(UrlUtils.htcNew());
            }

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-print".equals(form.getPageRedirect())) {
                return redirect(UrlUtils.htcSavePrint(htcEntity.getID(), form.getPageRedirect()));
            }

            // Thực hiện chức năng gửi mẫu xét nghiệm
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-send-sample".equals(form.getPageRedirect())) {
                if (htcConfirmService.existBySourceIDAndSiteID(htcEntity.getCode(), Long.valueOf(htcEntity.getSiteConfirmTest()))) {
                    redirectAttributes.addFlashAttribute("warning", "Khách hàng đã tồn tại ở cơ sở khẳng định.");
                    return redirect(UrlUtils.htcIndex());
                }

                HtcVisitEntity htcSend = commonService.sendConfirm(htcEntity);
                String email = parameterService.getSiteConfig(Long.valueOf(htcSend.getSiteConfirmTest()), SiteConfigEnum.ALERT_CONFIRM_EMAIL.getKey());
                if (email != null || !"".equals(email)) {
                    sendEmailNotify(email, String.format("Cơ sở %s gửi mẫu xét nghiệm của khách hàng mã %s ", getCurrentUser().getSite().getName(),
                            htcSend.getCode()), String.format("Khách hàng %s - %s được gửi ngày %s", htcSend.getCode(),
                            htcSend.getPatientName(), TextUtils.formatDate(new Date(), FORMATDATE)));
                }

                redirectAttributes.addFlashAttribute("success", "Khách hàng được thêm mới thành công");
                redirectAttributes.addFlashAttribute("success_id", htcEntity.getID());
                return redirect(String.format("%s?pid=%s", UrlUtils.htcIndex(), htcEntity.getID()));
            }

            // In phiếu đồng ý xét nghiệm
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "printable".equals(form.getPageRedirect())) {
                // Có phản ứng 
                if (TestResultEnum.KHONG_PHAN_UNG.getKey().equals(htcEntity.getTestResultsID())) {
                    return redirect(UrlUtils.htcSavePrintAgree(htcEntity.getID(), form.getPageRedirect()));
                } else {
                    return redirect(UrlUtils.htcSavePrint(htcEntity.getID(), form.getPageRedirect()));
                }
            }

            redirectAttributes.addFlashAttribute("success", "Khách hàng được thêm mới thành công");
            redirectAttributes.addFlashAttribute("success_id", htcEntity.getID());
            if (form.isIsNew()) {
                return redirect(UrlUtils.htcNew());
            } else {
                return redirect(UrlUtils.htcIndex());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("options", getOptions());
            model.addAttribute("error", ex.getMessage());
            return "backend/htc/new";
        }
    }

    @RequestMapping(value = {"/htc/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long visitID,
            @RequestParam(name = "printable", defaultValue = "") String printable,
            HtcVisitForm form,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(visitID);

        if (htcVisitEntity == null || htcVisitEntity.isIsRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", visitID));
            return redirect(UrlUtils.htcIndex());
        }

        if (!htcVisitEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());
        }

        // Kiểm tra trạng thái xét nghiệm khẳng định trước khi update
        if (HtcConfirmStatusEnum.DOING.getKey().equals(htcVisitEntity.getConfirmTestStatus())) {
            redirectAttributes.addFlashAttribute("error", "Khách hàng đã được tiếp nhận, không được sửa thông tin.");
            return redirect(UrlUtils.htcView(visitID));
        }

        // Kiểm tra khách hàng đã chuyển sang GSPH không được chỉnh sửa thông tin
//        if (htcVisitEntity.getPacSentDate() != null && htcVisitEntity.getPacPatientID() != null) {
//            redirectAttributes.addFlashAttribute("error", "Khách hàng đã chuyển gửi quản lý người nhiễm, không được sửa thông tin.");
//            return redirect(UrlUtils.htcView(visitID));
//        }
        form.setFromVisit(htcVisitEntity);
        form.setDefaultProject(getCurrentUser().getSite().getProjectID());
        model.addAttribute("title", "Cập nhật thông tin khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));

        // Setting display for update
        form.setSiteID(htcVisitEntity.getSiteID());
        form.setID(visitID);

        model.addAttribute("form", form);
        return "backend/htc/new";
    }

    @RequestMapping(value = {"/htc/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long visitID,
            @ModelAttribute("form") @Valid HtcVisitForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws CloneNotSupportedException {

        HtcVisitForm formDb = new HtcVisitForm();
        formDb = (HtcVisitForm) form.clone();

        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(visitID);

        if (htcVisitEntity == null || htcVisitEntity.isIsRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", visitID));
            return redirect(UrlUtils.htcIndex());
        }

        if (!htcVisitEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());
        }

        if (StringUtils.isNotEmpty(form.getArrivalSiteID()) && !"-1".equals(form.getArrivalSiteID())) {
            form.setArrivalSite(siteService.findOne(Long.parseLong(form.getArrivalSiteID())).getName());
        }
        if (htcVisitEntity.getRequestConfirmTime() != null && htcVisitEntity.getUpdateConfirmTime() == null) {
            form.setRequestConfirmTime("x");
            form.setSiteConfirmTest(htcVisitEntity.getSiteConfirmTest());
        }

        form.setCurrentSiteID(getCurrentUser().getSite().getID());
        if (htcVisitEntity.getPacSentDate() != null && htcVisitEntity.getPacPatientID() != null) {
            form.setFromVisit(htcVisitEntity);
            form.setArvInfo(formDb);
            gsphValidate.validate(form, bindingResult);
        } else if (HtcConfirmStatusEnum.DONE.getKey().equals(htcVisitEntity.getConfirmTestStatus()) && htcVisitEntity.getConfirmTime() != null) {
            htcVisitPaidValidate.validate(form, bindingResult);
        } else {
            htcVisitValidate.validate(form, bindingResult);
        }

        form.setDefaultProject(getCurrentUser().getSite().getProjectID());
        model.addAttribute("title", "Cập nhật thông tin khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("form", form);

        if (HtcConfirmStatusEnum.DONE.getKey().equals(htcVisitEntity.getConfirmTestStatus())
                && htcVisitEntity.getConfirmTime() != null) {
            if (htcVisitEntity.isConfirmResutl()) {
                form.setFromVisitReceiveResult(htcVisitEntity);
                form.setConfirmResutl(htcVisitEntity.isConfirmResutl());
            }

            HtcVisitForm formBackup = form;
            form.setConfirmTime(formBackup.getConfirmTime());
            model.addAttribute("form", form);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thông tin khách hàng thất bại.");
            return "backend/htc/new";
        }

        if (!getCurrentUser().getSite().getID().equals(form.getSiteID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());
        }

        // Gán giá trị cho entity
        try {

            if (TherapyExchangeStatusEnum.CHUA_CHUYEN.getKey().equals(form.getTherapyExchangeStatus())) {
                form.setArvExchangeResult(ArvExchangeEnum.CHUA_DONG_Y.getKey());
            }
            HtcVisitEntity htcEntity = form.getHtcVisit(htcVisitEntity, getCurrentUser().getSite());
            if (htcEntity.getExchangeTime() != null && htcEntity.getArvExchangeResult().equals(ArvExchangeEnum.DONG_Y.getKey())) {
                OpcArvEntity arv = opcArvService.findBySourceID(htcEntity.getID(), ServiceEnum.HTC.getKey());
                if (arv != null && arv.getSiteID().equals(htcEntity.getArrivalSiteID())) {
                    SiteEntity site = siteService.findOne(arv.getSiteID());
                    arv.setDateOfArrival(htcEntity.getExchangeTime());
                    arv.setFeedbackResultsReceivedTime(new Date());
                    arv.setTranferToTime(new Date());
                    htcEntity.setArrivalTime(arv.getTranferToTime());
                    htcEntity.setFeedbackReceiveTime(arv.getFeedbackResultsReceivedTime());
                    htcEntity.setRegisterTherapyTime(arv.getRegistrationTime());
                    htcEntity.setRegisteredTherapySite(site.getName());
                    htcEntity.setTherapyNo(arv.getCode());
                    htcEntity.setTherapyRegistProvinceID(site.getProvinceID());
                    htcEntity.setTherapyRegistDistrictID(site.getDistrictID());
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey());
                    htcVisitService.log(htcEntity.getID(), "Khách hàng được chuyển gửi đến cơ sở " + siteService.findOne(arv.getSiteID()).getName() + " đã được phản hồi thành công ");
                    opcArvService.save(arv);
                } else if (arv != null && !arv.getSiteID().equals(htcEntity.getArrivalSiteID())) {
                    htcEntity.setArrivalTime(null);
                    htcEntity.setFeedbackReceiveTime(null);
                    htcEntity.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUA_CHUYEN.getKey());
                }
            }
            if (htcVisitEntity.getRequestConfirmTime() != null && htcVisitEntity.getUpdateConfirmTime() == null) {
                HtcConfirmEntity confirm = htcConfirmService.findBySourceIDSourceSiteID(htcEntity.getCode(), htcEntity.getSiteID());

                if (confirm == null || confirm.getRemove()) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng này tại cơ sở khẳng định");
                    return redirect(UrlUtils.htcIndex());
                }

                confirm.setFullname(htcEntity.getPatientName());
                confirm.setYear(htcEntity.getYearOfBirth() == null ? 0 : Integer.valueOf(htcEntity.getYearOfBirth()));
                confirm.setWardID(htcEntity.getPermanentWardID());
                confirm.setDistrictID(htcEntity.getPermanentDistrictID());
                confirm.setProvinceID(htcEntity.getPermanentProvinceID());
                confirm.setAddress(htcEntity.getPermanentAddress());
                confirm.setPermanentAddressGroup(htcEntity.getPermanentAddressGroup());
                confirm.setPermanentAddressStreet(htcEntity.getPermanentAddressStreet());
                confirm.setObjectGroupID(htcEntity.getObjectGroupID());
                confirm.setSourceServiceID(ServiceEnum.HTC.getKey());
                confirm.setGenderID(htcEntity.getGenderID());
                confirm.setPatientID(htcEntity.getPatientID());
                confirm.setTestResultsID(htcEntity.getTestResultsID());
                confirm.setSourceReceiveSampleTime(htcEntity.getPreTestTime());
                confirm.setServiceID(htcEntity.getServiceID());

                confirm.setModeOfTransmission(htcEntity.getModeOfTransmission());
                confirm.setCurrentAddress(htcEntity.getCurrentAddress());
                confirm.setCurrentAddressGroup(htcEntity.getCurrentAddressGroup());
                confirm.setCurrentAddressStreet(htcEntity.getCurrentAddressStreet());
                confirm.setCurrentProvinceID(htcEntity.getCurrentProvinceID());
                confirm.setCurrentDistrictID(htcEntity.getCurrentDistrictID());
                confirm.setCurrentWardID(htcEntity.getCurrentWardID());
                confirm.setInsurance(htcEntity.getHasHealthInsurance());
                confirm.setInsuranceNo(htcEntity.getHealthInsuranceNo());

                confirm.setUpdateConfirmTime(new Date());
                confirm = htcConfirmService.save(confirm);

                htcVisitEntity.setUpdateConfirmTime(new Date());
                htcEntity = htcVisitService.save(htcEntity);
                htcVisitService.log(htcEntity.getID(), "Đã cập nhật thông tin rà soát khẳng định");

            } else {
                htcEntity = htcVisitService.save(htcEntity);
                htcVisitService.log(htcEntity.getID(), "Cập nhật khách hàng");
            }

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-print".equals(form.getPageRedirect())) {
                return redirect(UrlUtils.htcSavePrint(htcEntity.getID(), form.getPageRedirect()));
            }

            // Thực hiện chức năng gửi mẫu xét nghiệm
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-send-sample".equals(form.getPageRedirect())) {

                if (htcConfirmService.existBySourceIDAndSiteID(htcEntity.getCode(), Long.valueOf(htcEntity.getSiteConfirmTest()))) {
                    redirectAttributes.addFlashAttribute("warning", "Khách hàng đã tồn tại ở cơ sở khẳng định.");
                    return redirect(UrlUtils.htcIndex());
                }

                HtcVisitEntity htcSend = commonService.sendConfirm(htcEntity);
                String email = parameterService.getSiteConfig(Long.valueOf(htcSend.getSiteConfirmTest()), SiteConfigEnum.ALERT_CONFIRM_EMAIL.getKey());
                if (email != null || !"".equals(email)) {
                    sendEmailNotify(email, String.format("Cơ sở %s gửi mẫu xét nghiệm của khách hàng mã %s ",
                            getCurrentUser().getSite().getName(), htcSend.getCode()),
                            String.format("Khách hàng %s - %s được gửi ngày %s", htcSend.getCode(),
                                    htcSend.getPatientName(), TextUtils.formatDate(new Date(), FORMATDATE)));
                }
                return redirect(String.format("%s?pid=%s", UrlUtils.htcIndex(), String.valueOf(htcSend.getID())));
            }

            // In phiếu đồng ý xét nghiệm
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "printable".equals(form.getPageRedirect())) {
                // Có phản ứng 
                if (TestResultEnum.KHONG_PHAN_UNG.getKey().equals(htcEntity.getTestResultsID())) {
                    return redirect(UrlUtils.htcSavePrintAgree(htcEntity.getID(), form.getPageRedirect()));
                } else {
                    return redirect(UrlUtils.htcSavePrint(htcEntity.getID(), form.getPageRedirect()));
                }
            }

            redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", htcEntity.getID());

            return redirect(UrlUtils.htcIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/htc/new";
        }
    }

    /**
     * @author PdThang Xem thông tin khách hàng
     * @param model
     * @param visitID
     * @param printable
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long visitID,
            @RequestParam(name = "printable", defaultValue = "") String printable,
            HtcVisitForm form,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(visitID);

        if (htcVisitEntity == null || htcVisitEntity.isIsRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", visitID));
            return redirect(UrlUtils.htcIndex());
        }

        if (!htcVisitEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());
        }
        form.setFromVisit(htcVisitEntity);
        model.addAttribute("title", "Xem thông tin khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));

        // Setting display for update
        form.setSiteID(htcVisitEntity.getSiteID());
        form.setID(visitID);

        model.addAttribute("form", form);
        return "backend/htc/view";
    }

    /**
     * Khôi phục khách hàng
     *
     * @author PdThang
     * @param model
     * @param visitID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long visitID,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisit = htcVisitService.findOne(visitID);
        if (!htcVisit.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcIndex());

        }
        htcVisit.setIsRemove(false);
        htcVisitService.save(htcVisit);
        htcVisitService.log(htcVisit.getID(), "Khôi phục khách hàng đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", htcVisit.getID());
        return redirect(UrlUtils.htcIndex());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param visitID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long visitID,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisit = htcVisitService.findOne(visitID);
        if (!htcVisit.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect("/backend/htc/index.html?tab=remove");

        }
        htcVisitService.remove(htcVisit.getID());
        htcVisitService.log(htcVisit.getID(), "Xóa vĩnh viễn khách hàng");
        redirectAttributes.addFlashAttribute("success", "Khách hàng đã bị xóa vĩnh viễn");
        return redirect("/backend/htc/index.html?tab=remove");
    }

    @RequestMapping(value = "/htc-laytest/index.html", method = RequestMethod.GET)
    public String actionLaytest(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "advisorye_time", required = false) String advisoryeTime,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {

        LoggedUser currentUser = getCurrentUser();

        LaytestSearch search = new LaytestSearch();
        search.setName(name != null && !"".equals(name) ? StringUtils.normalizeSpace(name.trim()) : null);
        search.setCode(code != null && !"".equals(code) ? StringUtils.normalizeSpace(code.trim()) : null);
        search.setStatus((StringUtils.isNotBlank(status) && "0".equals(status)) ? "0" : (StringUtils.isNotBlank(status) && "1".equals(status)) ? "1" : null);
        search.setRemove(false);
        search.setVisitRemove(1);
        switch (tab) {
            case "1":
                search.setStatus("0");
                break;
            case "2":
                search.setStatus("1");
                break;
            case "3":
                search.setVisitRemove(2);
                break;
        }
        search.setSiteID(null);
        search.setAdvisoryeTimeFrom(advisoryeTime);
        search.setAdvisoryeTimeTo(advisoryeTime);
        search.setSiteVisitID(currentUser.getSite().getID().toString());
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSampleSentDate(true);
        search.setSendStatus(null);
        DataPage<HtcLaytestEntity> dataPage = htcLaytestService.find(search);
        //Lấy site ID
        Set<Long> siteIDs = new HashSet<>();
        Set<Long> staffIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            siteIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSiteID")));// get site ID
            staffIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getCreatedBY")));// get site ID
        }

        model.addAttribute("title", "Tiếp nhận xét nghiệm tại cộng đồng");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", getSite(siteIDs));
        model.addAttribute("staffs", getStaffsOptions(staffIDs));
        model.addAttribute("genderOptions", getOptions().get(ParameterEntity.GENDER));
        model.addAttribute("testResultSoptions", getOptions().get(ParameterEntity.TEST_RESULTS));

        Map<String, String> statusOption = new HashMap<>();
        statusOption.put("", "Tất cả");
        statusOption.put("1", "Đã tiếp nhận");
        statusOption.put("0", "Chưa tiếp nhận");
        model.addAttribute("statusOption", statusOption);

        return "backend/htc/laytest";
    }

    /**
     *
     * @param staffIDs
     * @return
     */
    public Map<Long, String> getStaffsOptions(Set<Long> staffIDs) {
        Map<Long, String> options = new HashMap<>();
        if (staffIDs == null && staffIDs.isEmpty()) {
            return options;
        }
        List<StaffEntity> items = staffService.findAllByIDs(staffIDs);
        for (StaffEntity item : items) {
            options.put(item.getID(), item.getName());
        }
        return options;
    }

    @RequestMapping(value = {"/htc-laytest/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long ID,
            HtcLaytestForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();

        HtcLaytestEntity htcLaytestEntity = htcLaytestService.findOne(ID);

        if (htcLaytestEntity == null || htcLaytestEntity.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", ID));
            return redirect(UrlUtils.htcLaytestIndex());
        }

        if (htcLaytestEntity.getSiteVisitID().equals(String.valueOf(getCurrentUser().getSite().getID()))) {

            List<HtcLaytestAgencyEntity> laytestAgencies = agencyService.findByLaytestID(ID);
            form.setLaytestAgencies(laytestAgencies != null && !laytestAgencies.isEmpty() ? laytestAgencies : null);
            form.setHtcLaytetsForm(htcLaytestEntity);
            model.addAttribute("title", "Thông tin tiếp nhận XN tại cộng đồng");
//        model.addAttribute("parent_title", "Tiếp nhận không chuyên");
            model.addAttribute("options", getOptions(true));
            model.addAttribute("sites", getSiteVisits());
            model.addAttribute("currentSite", currentUser.getSite().getName());
            // Setting display for update
            form.setSiteID(Long.valueOf(htcLaytestEntity.getSiteVisitID()));
            form.setID(ID);
            model.addAttribute("form", form);
            return "backend/htc/view_laytest";
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcLaytestIndex());
        }

    }

    public Map<String, String> getSiteVisits() {
        HashMap<String, String> siteVisits = new HashMap<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteVisits.put(String.valueOf(site.getID()), site.getName());
        }
        return siteVisits;
    }

    /**
     * Xóa khách hàng laytest bên sàng lọc
     *
     * @author pdThang
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-laytest/remove.html", method = RequestMethod.GET)
    public String actionRemoveLaytest(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytestEntity = htcLaytestService.findOne(ID);
        if (laytestEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", ID));
            return redirect(UrlUtils.htcLaytestIndex());
        }

        if (!laytestEntity.getSiteVisitID().equals(String.valueOf(getCurrentUser().getSite().getID()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcLaytestIndex());
        }

        htcLaytestService.removeVisitLaytest(laytestEntity);
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect(UrlUtils.htcLaytestIndex());
    }

    /**
     * Khôi phục khách hàng laytest bên sàng lọc
     *
     * @author pdThang
     * @param model
     * @param ID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-laytest/restore.html", method = RequestMethod.GET)
    public String actionRestoreLaytest(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytestEntity = htcLaytestService.findOne(ID);
        if (laytestEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", ID));
            return redirect(UrlUtils.htcLaytestIndex());
        }

        if (!laytestEntity.getSiteVisitID().equals(String.valueOf(getCurrentUser().getSite().getID()))) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcLaytestIndex());
        }
        laytestEntity.setVisitRemove(false);
        htcLaytestService.save(laytestEntity);
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng thành công");
        return redirect(UrlUtils.htcLaytestIndex());
    }

}
