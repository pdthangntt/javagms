package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.CdServiceEnum;
import com.gms.entity.constant.ConfirmTypeEnum;
import com.gms.entity.constant.CustomerTypeEnum;
import com.gms.entity.constant.ExchangeServiceEnum;
import com.gms.entity.constant.ExchangeStatusEnum;
import com.gms.entity.constant.FeedbackStatusEnum;
import com.gms.entity.constant.GsphStatusEnum;
import com.gms.entity.constant.LaoVariableEnum;
import com.gms.entity.constant.ResultAntiEnum;
import com.gms.entity.constant.ResultPcrHivEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.TestMethodEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.HtcVisitForm;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.validate.OpcArvNewValidate;
import com.gms.entity.validate.OpcArvUpdateValidate;
import com.gms.service.HtcVisitService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DSNAnh
 */
@Controller(value = "backend_opc_receive_htc")
public class OpcReceiveHtcController extends OpcController {

    @Autowired
    OpcArvNewValidate opcArvNewValidate;
    @Autowired
    OpcArvUpdateValidate opcArvUpdateValidate;
    @Autowired
    OpcStageService opcStageService;
    @Autowired
    OpcTestService opcTestService;
    @Autowired
    OpcViralLoadService opcViralLoadService;
    @Autowired
    HtcVisitService htcVisitService;
    @Autowired
    SiteService siteService;
    @Autowired
    ParameterService parameterService;
    @Autowired
    StaffService staffService;
    @Autowired
    HtcVisitService htcService;

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

    @RequestMapping(value = {"/opc-from-htc/index.html"}, method = RequestMethod.GET)
    public String actionReceiveIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "htc_id", required = false) Long htcID,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "confirm_test_no", required = false, defaultValue = "") String confirmTestNo,
            @RequestParam(name = "confirm_time", required = false) String confirmTime,
            @RequestParam(name = "exchange_time", required = false) String exchangeTime,
            @RequestParam(name = "transfer_site", required = false, defaultValue = "") String transferSite,
            @RequestParam(name = "receive_status", required = false, defaultValue = "") String receiveStatus,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {
        HashMap<String, HashMap<String, String>> options = getOptions(false, null);
        HashMap<String, String> siteHTC = new HashMap<>();
        Set<Long> siteHTCs = new HashSet<>();
        HashMap<String, String> siteConfirm = new HashMap<>();
        Set<String> siteConfirms = new HashSet<>();
        String key = "receiveStatus";
        options.put(key, new HashMap<String, String>());
        options.get(key).put("", "Tất cả");
        options.get(key).put("1", "Chưa tiếp nhận");
        options.get(key).put("2", "Đã tiếp nhận");

        HtcSearch search = new HtcSearch();
        Set<Long> siteIDs = new HashSet<>();
        if (StringUtils.isNotEmpty(transferSite)) {
            siteIDs.add(Long.parseLong(transferSite));
        }
        if (StringUtils.isNotEmpty(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        search.setRemove(false);
        search.setConfirmTestNo(confirmTestNo.trim());
        search.setConfirmTimeFrom(isThisDateValid(confirmTime) ? confirmTime : null);
        search.setExchangeTimeFrom(isThisDateValid(exchangeTime) ? exchangeTime : null);
        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setReceiveStatus("not_received".equals(tab) ? "1" : "received".equals(tab) ? "2" : receiveStatus);
        search.setRemoveTranfer("remove".equals(tab));
        search.setPageIndex(page);
        search.setPageSize(size);
        DataPage<HtcVisitEntity> dataPage = htcVisitService.findReceiveHtc(search);

        if (dataPage.getData() != null && !dataPage.getData().isEmpty()) {
            siteHTCs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSiteID")));
        }
        if (dataPage.getData() != null && !dataPage.getData().isEmpty()) {
            siteConfirms.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSiteConfirmTest")));
        }
        if (!siteHTCs.isEmpty()) {
            List<SiteEntity> lists = siteService.findByIDs(siteHTCs);
            if (!lists.isEmpty()) {
                for (SiteEntity list : lists) {
                    siteHTC.put(list.getID().toString(), list.getName());
                }
            }
        }
        Set<Long> siteConfirmsFinal = null;
        if (!siteConfirms.isEmpty()) {
            siteConfirmsFinal = new HashSet<>();
            for (String siteID : siteConfirms) {
                if (siteID != null) {
                    siteConfirmsFinal.add(Long.parseLong(siteID));
                }

            }
            List<SiteEntity> lists = siteService.findByIDs(siteConfirmsFinal);
            if (!lists.isEmpty()) {
                for (SiteEntity list : lists) {
                    siteConfirm.put(list.getID().toString(), list.getName());
                }
            }
        }

        model.addAttribute("title", "Tiếp nhận từ cơ sở tư vấn xét nghiệm");
        model.addAttribute("smallTitle", "Tiếp nhận từ TVXN");
        model.addAttribute("parentTitle", "Điều trị ngoại trú");
        model.addAttribute("options", options);
        model.addAttribute("siteHTC", siteHTC);
        model.addAttribute("siteConfirm", siteConfirm);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tabIndex", "null".equals(tab) ? "" : tab);
        return "backend/opc_arv/htc_receive";
    }

    //Xóa khách hàng chuyển gửi
    @RequestMapping(value = "/opc-from-htc/remove.html", method = RequestMethod.GET)
    public String actionRemoveHtcReceive(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {
        HtcVisitEntity htcVisit = htcVisitService.findOne(ID);
        if (htcVisit == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại"));
            return redirect(UrlUtils.opcArvReceiveHTC());
        }
        htcVisitService.deleteReceive(htcVisit.getID());
        htcVisitService.log(ID, "Xoá khi chuyển gửi khách hàng");

        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect(UrlUtils.opcArvReceiveHTC());
    }

    @RequestMapping(value = "/opc-from-htc/restore.html", method = RequestMethod.GET)
    public String actionRestoreHtcReceive(Model model,
            @RequestParam(name = "oid") Long visitID,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisit = htcVisitService.findOne(visitID);
        if (htcVisit == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại"));
            return redirect(UrlUtils.opcArvReceiveHTC());
        }
        htcVisit.setRemoveTranfer(false);
        htcVisitService.save(htcVisit);
        htcVisitService.log(htcVisit.getID(), "Khôi phục khách hàng đã xóa khi chuyển gửi");
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", htcVisit.getID());
        return redirect(UrlUtils.opcArvReceiveHTC());
    }

    @RequestMapping(value = {"/opc-from-htc/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long visitID,
            @RequestParam(name = "printable", defaultValue = "") String printable,
            HtcVisitForm form,
            RedirectAttributes redirectAttributes) {

        HtcVisitEntity htcVisitEntity = htcVisitService.findOne(visitID);
        HashMap<String, HashMap<String, String>> htcOptions = getHtcOptions(true);
        if (htcVisitEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", visitID));
            return redirect(UrlUtils.opcArvReceiveHTC());
        }

        form.setFromVisit(htcVisitEntity);
        model.addAttribute("title", "Xem thông tin khách hàng");
        model.addAttribute("smallTitle", "Tiếp nhận từ TVXN");
        model.addAttribute("parentTitle", "Điều trị ngoại trú");
        model.addAttribute("siteConfirmTest", htcOptions.get("siteConfirm").get(form.getSiteConfirmTest()));
        model.addAttribute("options", htcOptions);

        // Setting display for update
        form.setSiteID(htcVisitEntity.getSiteID());
        form.setID(visitID);
//        form.setSiteConfirmTest(siteService.findOne(Long.valueOf(htcVisitEntity.getSiteConfirmTest())) == null ? "Cơ sở khác" : siteService.findOne(Long.valueOf(htcVisitEntity.getSiteConfirmTest())).getName());

        model.addAttribute("form", form);
        return "backend/opc_arv/htc_receive_view";
    }

    protected HashMap<String, HashMap<String, String>> getHtcOptions(boolean scan) {
        LoggedUser currentUser = getCurrentUser();

        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.FIXED_SERVICE);
        parameterTypes.add(ParameterEntity.ARV_CONSULTANT_EXCHANGE_RESULT);
        parameterTypes.add(ParameterEntity.ASANTE_INFECT_TEST);
        parameterTypes.add(ParameterEntity.PARTNER_INFO_PROVIDE_RESULT);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.REFERENT_SOURCE);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.CONFIRM_TEST_STATUS);
        parameterTypes.add(ParameterEntity.GSPH_STATUS);
        parameterTypes.add(ParameterEntity.DPLTMC_STATUS);
        parameterTypes.add(ParameterEntity.THERAPY_EXCHANGE_STATUS);
        parameterTypes.add(ParameterEntity.SAMPLE_QUALITY);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.HAS_HEALTH_INSURANCE);
        parameterTypes.add(ParameterEntity.MOST_RECENT_TEST);
        parameterTypes.add(ParameterEntity.INFO_COMPARE);
        parameterTypes.add(ParameterEntity.ALERT_TYPE);
        parameterTypes.add(ParameterEntity.SITE_PROJECT);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        // Modify elements in test object group 
        HashMap<String, String> testObjects = options.get(ParameterEntity.TEST_OBJECT_GROUP);
        testObjects.remove(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
        testObjects.put(TestObjectGroupEnum.PREGNANT_PERIOD.getKey(), TestObjectGroupEnum.PREGNANT_PERIOD.getLabel());
        testObjects.put(TestObjectGroupEnum.GIVING_BIRTH.getKey(), TestObjectGroupEnum.GIVING_BIRTH.getLabel());

        if (currentUser.getSite().getServiceIDs().contains(ServiceEnum.PMTCT.getKey())) {
//            options.get(ParameterEntity.SERVICE_TEST).put(ServiceEnum.PMTCT.getKey(), ServiceEnum.PMTCT.getLabel());
        }

        List<StaffEntity> staffModels = staffService.findBySite(getCurrentUser().getSite().getID());
        String staffType = "staffs";
        options.put(staffType, new HashMap<String, String>());
        options.get(staffType).put("", "Chọn nhân viên");
        for (StaffEntity staffModel : staffModels) {
            options.get(staffType).put(String.valueOf(staffModel.getID()), staffModel.getName());
        }

        // Trả lời đồng ý xét nghiệm
        String isAgreeTestKey = "isAgreeTest";
        options.put(isAgreeTestKey, new HashMap<String, String>());
        options.get(isAgreeTestKey).put("", "Chọn câu trả lời");
        options.get(isAgreeTestKey).put("false", "Không");
        options.get(isAgreeTestKey).put("true", "Có");

        String agreePreTest = "agreePreTest";
        options.put(agreePreTest, new HashMap<String, String>());
        options.get(agreePreTest).put("", "Chọn câu trả lời");
        options.get(agreePreTest).put(String.valueOf(BooleanEnum.TRUE.getKey()), "Có");
        options.get(agreePreTest).put(String.valueOf(BooleanEnum.FALSE.getKey()), "Không");

        String patientIdAuthen = "patientIdAuthen";
        options.put(patientIdAuthen, new HashMap<String, String>());
        options.get(patientIdAuthen).put("", "Chọn câu trả lời");
        options.get(patientIdAuthen).put("true", "Có");
        options.get(patientIdAuthen).put("false", "Không");

        //Trạng thái chuyển gửi từ cơ sở không chuyên
        String sendStatus = "sendStatus";
        options.put(sendStatus, new HashMap<String, String>());
        options.get(sendStatus).put("", "Lựa chọn");
        options.get(sendStatus).put("1", "Chưa chuyển");
        options.get(sendStatus).put("2", "Đã chuyển");
        options.get(sendStatus).put("3", "Đã tiếp nhận");

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

        // Thêm tham số
        addEnumOption(options, ParameterEntity.CD_SERVICE, CdServiceEnum.values(), "Chọn loại dịch vụ");
        addEnumOption(options, ParameterEntity.CONFIRM_TYPE, ConfirmTypeEnum.values(), "Chọn loại hình xét nghiệm khẳng định");
        addEnumOption(options, ParameterEntity.EXCHANGE_SERVICE, ExchangeServiceEnum.values(), "Chọn dịch vụ tư vấn chuyển gửi");
        addEnumOption(options, ParameterEntity.RESULT_ANTI, ResultAntiEnum.values(), "Chọn KQXN Kháng nguyên/Kháng thể");
        addEnumOption(options, ParameterEntity.RESULT_PCR_HIV, ResultPcrHivEnum.values(), "Chọn kết quả xét nghiệm PCR HIV");
        addEnumOption(options, ParameterEntity.TEST_METHOD, TestMethodEnum.values(), "Chọn phương pháp XN sàng lọc");
        addEnumOption(options, ParameterEntity.CUSTOMER_TYPE, CustomerTypeEnum.values(), "Chọn loại bệnh nhân");
        addEnumOption(options, ParameterEntity.LAO_VARIABLE, LaoVariableEnum.values(), "Chọn thể lao");

        //Nơi xét nghiệm sàng lọc
        List<SiteEntity> siteHtc = siteService.getSiteHtc(currentUser.getSite().getProvinceID());
        String key = "siteHtc";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Tất cả");
        for (SiteEntity site : siteHtc) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }

        //Nơi xét nghiệm khẳng định
        List<SiteEntity> siteConfirm = siteService.getSiteConfirm(currentUser.getSite().getProvinceID());
        key = "siteConfirm";
        options.put(key, new LinkedHashMap<String, String>());
        options.get(key).put("", "Chọn nơi xét nghiệm");
        for (SiteEntity site : siteConfirm) {
            options.get(key).put(String.valueOf(site.getID()), site.getName());
        }

        if (scan) {
            HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
            //very config sinh phẩm
            if (siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null).equals("")) {
                options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null).split(",")));
            }
        }
        return options;
    }
}
