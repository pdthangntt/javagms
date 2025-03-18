package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.components.rabbit.RabbitMQSender;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.ConfirmFeedbackEnum;
import com.gms.entity.constant.EarlyHivResultEnum;
import com.gms.entity.constant.EarlyHivStatusEnum;
import com.gms.entity.constant.FeedbackStatusEnum;
import com.gms.entity.constant.GsphStatusEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.VirusLoadResultEnum;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.HtcConfirmEarlyHivForm;
import com.gms.entity.form.HtcConfirmForm;
import com.gms.entity.input.HtcConfirmSearch;
import com.gms.entity.rabbit.HtcConfirmMessage;
import com.gms.entity.validate.HtcConfirmEarlyValidate;
import com.gms.entity.validate.HtcConfirmValidate;
import com.gms.entity.validate.HtcConfirmValidateUpdate;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdThang
 */
@Controller(value = "htc_confirm")
public class HtcConfirmController extends HtcController {

    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private HtcConfirmValidate htcConfirmValidate;
    @Autowired
    private HtcConfirmEarlyValidate htcConfirmEarlyValidate;
    @Autowired
    private HtcConfirmValidateUpdate htcConfirmValidateUpdate;
    @Autowired
    private SiteService siteService;
    @Autowired
    private RabbitMQSender rabbitSender;
    @Autowired
    private ParameterService parameterService;

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = super.getOptions();

        options.put("gsphStatus", new HashMap<String, String>());
        options.get("gsphStatus").put("", "Chọn trạng thái chuyển GSPH");
        options.get("gsphStatus").put(GsphStatusEnum.CHUA_CHUYEN.getKey(), "Chưa chuyển");
        options.get("gsphStatus").put(GsphStatusEnum.DA_CHUYEN.getKey(), "Đã chuyển");

        options.put("earlyHivStatus", new HashMap<String, String>());
        options.get("earlyHivStatus").put("", "Tất cả");
        options.get("earlyHivStatus").put(EarlyHivStatusEnum.NO_XN.getKey(), EarlyHivStatusEnum.NO_XN.getLabel());
        options.get("earlyHivStatus").put(EarlyHivStatusEnum.YES_XN.getKey(), EarlyHivStatusEnum.YES_XN.getLabel());

        options.get(ParameterEntity.VIRUS_LOAD).remove("5");

        return options;
    }

    private HashMap<String, HashMap<String, String>> getOptions(boolean scan) {
        HashMap<String, HashMap<String, String>> options = super.getOptions();
        if (scan) {
            HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
            //very config sinh phẩm
            if (siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).equals("")) {
                options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null).split(",")));
            }
        }
        options.get(ParameterEntity.VIRUS_LOAD).remove("5");
        return options;
    }

    private Map<String, String> getTestResultsOption() {
        HashMap<String, String> testResultsOptions = getOptions(true).get(ParameterEntity.TEST_RESULTS);
        testResultsOptions.remove("1");
        return testResultsOptions;
    }

    /**
     * Danh sách khách hang khang dinh
     *
     * @author PDThang
     *
     * @param model
     * @param tab
     * @param code
     * @param fullname
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param requestHtcTimeFrom
     * @param requestHtcTimeTo
     * @param acceptDateFrom
     * @param acceptDateTo
     * @param sourceSampleDateFrom
     * @param sourceSampleDateTo
     * @param resultsID
     * @param sourceSiteID
     * @param confirmStatus
     * @param sourceID
     * @param statusRequest
     * @param confirmFeedback
     * @param page
     * @param size
     * @param gsphStatus
     * @return
     */
    @GetMapping(value = {"/htc-confirm/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "request_htc_time_from", required = false) String requestHtcTimeFrom,
            @RequestParam(name = "request_htc_time_to", required = false) String requestHtcTimeTo,
            @RequestParam(name = "accept_date_from", required = false) String acceptDateFrom,
            @RequestParam(name = "accept_date_to", required = false) String acceptDateTo,
            @RequestParam(name = "source_sample_date_from", required = false) String sourceSampleDateFrom,
            @RequestParam(name = "source_sample_date_to", required = false) String sourceSampleDateTo,
            @RequestParam(name = "results_id", required = false) String resultsID,
            @RequestParam(name = "source_site_id", required = false) Long sourceSiteID,
            @RequestParam(name = "confirm_status", required = false) String confirmStatus,
            @RequestParam(name = "sourceID", required = false) String sourceID,
            @RequestParam(name = "status_feedback", required = false) String confirmFeedback,//Trạng thái phản hồi
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size,
            @RequestParam(name = "early_diagnose", required = false, defaultValue = "") String earlyDiagnose,
            @RequestParam(name = "virus_load", required = false, defaultValue = "") String virusLoad,
            @RequestParam(name = "gsph_status", required = false, defaultValue = "") String gsphStatus,
            @RequestParam(name = "early_hiv_status", required = false, defaultValue = "") String earlyHivStatus,
            @RequestParam(name = "early_hiv_date_from", required = false) String earlyHivDateFrom,
            @RequestParam(name = "early_hiv_date_to", required = false) String earlyHivDateTo,
            @RequestParam(name = "early_hiv", required = false, defaultValue = "") String earlyHiv) {
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmSearch search = new HtcConfirmSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());
        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (code != null && !"".equals(code)) {
            search.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        search.setSourceID(sourceID);
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setEarlyHivDateFrom(earlyHivDateFrom);
        search.setEarlyHivDateTo(earlyHivDateTo);
        search.setRequestHtcTimeFrom(requestHtcTimeFrom);
        search.setRequestHtcTimeTo(requestHtcTimeTo);

        search.setAcceptDateFrom(acceptDateFrom);
        search.setAcceptDateTo(acceptDateTo);
        search.setSourceSampleDateFrom(sourceSampleDateFrom);
        search.setSourceSampleDateTo(sourceSampleDateTo);
        search.setResultsID(resultsID);
        search.setSourceSiteID(sourceSiteID);
        search.setRemove(false);
        search.setWait(false);
        search.setConfirmFeedback(confirmFeedback);
        search.setGsphStatus(StringUtils.isNotEmpty(gsphStatus) ? gsphStatus : null);
        switch (tab) {
            case "return":
                search.setResult(true);
                search.setConfirmStatus("3");
                break;
            case "result":
                search.setResult(true);
                search.setConfirmStatus("2");
                break;
            case "received":
                search.setReceived(true);
                break;
            case "remove":
                search.setRemove(true);
                break;
            case "update":
                search.setUpdate(true);
            case "request":
                search.setIsRequestAdditional(true);
                break;
            case "":
                search.setEarlyDiagnose(earlyDiagnose);
                search.setVirusLoad(virusLoad);
                search.setEarlyHiv(earlyHiv);
//                search.setEarlyHivDateFrom(earlyHivDateFrom);
//                search.setEarlyHivDateTo(earlyHivDateTo);
                search.setEarlyHivStatus(earlyHivStatus);
                break;
        }
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updatedAt"}));
        DataPage<HtcConfirmEntity> dataPage = htcConfirmService.find(search);
        Set<Long> siteIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            siteIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSourceSiteID")));// get site ID
        }
        HashMap<String, String> earlyDia = getOptions().get(ParameterEntity.EARLY_DIAGNOSE);
        earlyDia.put("0", "Không có thông tin");

        model.addAttribute("title", "Khách hàng");
        model.addAttribute("smallTitle", "Xét nghiệm khẳng định");
        model.addAttribute("genderOptions", getOptions().get(ParameterEntity.GENDER));
        model.addAttribute("confirmStatusOption", getOptions().get(ParameterEntity.CONFIRM_TEST_STATUS));
        model.addAttribute("testObjectGroup", getOptions().get(ParameterEntity.TEST_OBJECT_GROUP));
        model.addAttribute("testResultConfirmOptions", getOptions().get(ParameterEntity.TEST_RESULTS_CONFIRM));
        model.addAttribute("gsphStatusOptions", getOptions().get("gsphStatus"));
        model.addAttribute("earlyHivStatusOptions", getOptions().get("earlyHivStatus"));
        model.addAttribute("confirmTestStatus", getOptions().get(ParameterEntity.CONFIRM_TEST_STATUS));
        model.addAttribute("confirmTestStatus", getOptions().get(ParameterEntity.CONFIRM_TEST_STATUS));
        model.addAttribute("virusLoadOption", getOptions().get(ParameterEntity.VIRUS_LOAD));
        model.addAttribute("earlyHivOptions", getOptions().get(ParameterEntity.EARLY_HIV));
        model.addAttribute("earlyDiagnose", earlyDia);

        model.addAttribute("sites", getSite(siteIDs));

        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        //Sắp xếp để hiển thị tìm kiếm Đơn vị gửi mẫu
        Map<String, String> sitesMap = new HashMap<>();
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
        sortedMap.put("", "Tất cả");
        ArrayList<String> list = new ArrayList<>();
        for (SiteEntity site : sites) {
            sitesMap.put(String.valueOf(site.getID()), site.getShortName());
        }
        for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String str, String str1) {
                return (str).compareTo(str1);
            }
        });
        for (String str : list) {
            for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }
        Map<String, String> statusFeedbackOption = new HashMap<>();
        statusFeedbackOption.put("", "Tất cả");
        statusFeedbackOption.put(ConfirmFeedbackEnum.CHUA_XAC_NHAN.getKey(), ConfirmFeedbackEnum.CHUA_XAC_NHAN.getLabel());
        statusFeedbackOption.put(ConfirmFeedbackEnum.TU_CHOI.getKey(), ConfirmFeedbackEnum.TU_CHOI.getLabel());
        statusFeedbackOption.put(ConfirmFeedbackEnum.DA_XAC_NHAN.getKey(), ConfirmFeedbackEnum.DA_XAC_NHAN.getLabel());
        model.addAttribute("statusFeedbackOption", statusFeedbackOption);

        model.addAttribute("siteTests", sortedMap);
        model.addAttribute("dataPage", dataPage);
        return "backend/htc_confirm/index";
    }

    /**
     * @author DSNA
     * @param model
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc-confirm/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            HtcConfirmForm form,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }

        // Set Set mã tự tăng 
//        String codeGen = htcConfirmService.getCode();
//        if (StringUtils.isEmpty(form.getSourceServiceID())) {
//            form.setCode(codeGen);
//        }
        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("form", form);
        model.addAttribute("sourceSites", getSiteSentOption(siteIDs));
        model.addAttribute("options", getOptions(true));
        model.addAttribute("testResultsOptions", getTestResultsOption());
        return "backend/htc_confirm/new";
    }

    /**
     * @author TrangBN
     * @param model
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc-confirm/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid HtcConfirmForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }

        Map<String, String> siteList = getSite(siteIDs);
        siteList.put("", "Chọn tên cơ sở gửi mẫu");

        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("form", form);
        model.addAttribute("sourceSites", siteList);
        model.addAttribute("options", getOptions(true));

        // Validate custom form
        form.setSiteID(getCurrentUser().getSite().getID());
        htcConfirmValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới thông tin khách hàng thất bại.");
            model.addAttribute("testResultsOptions", getTestResultsOption());
            model.addAttribute("sourceSites", siteList);
            return "backend/htc_confirm/new";
        }
        try {
            HtcConfirmEntity htcConfirmEntity = form.getHtcConfirm(getCurrentUser().getSite().getID(), null);
            htcConfirmEntity.setSourceServiceID(ServiceEnum.HTC_CONFIRM.getKey()); // Set mã dịch vụ nguồn thêm mới khách hàng khẳng định
            htcConfirmEntity = htcConfirmService.save(htcConfirmEntity);
            htcConfirmService.log(htcConfirmEntity.getID(), "Thêm mới khách hàng xét nghiệm khẳng định");
            redirectAttributes.addFlashAttribute("success", "Khách hàng được thêm mới thành công");
            redirectAttributes.addFlashAttribute("success_id", htcConfirmEntity.getID());

            HtcConfirmMessage message = new HtcConfirmMessage();
            message.setConfirm(htcConfirmEntity);
            rabbitSender.sendVisit(message);

            return redirect(UrlUtils.htcConfirmIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/htc_confirm/new";
        }
    }

    /**
     * Xóa khách hàng
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID()) || htcConfirm.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());

        }
        htcConfirmService.delete(htcConfirm.getID());
        htcConfirmService.log(htcConfirm.getID(), "Xóa khách hàng");
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect(UrlUtils.htcConfirmIndex());
    }

    /**
     * @author PdThang
     * @param model
     * @param confirmID
     * @param printable
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc-confirm/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long confirmID,
            @RequestParam(name = "printable", defaultValue = "") String printable,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            HtcConfirmForm form,
            RedirectAttributes redirectAttributes) {
        HtcConfirmEntity htcConfirmEntity = htcConfirmService.findOne(confirmID);
        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }
        if (htcConfirmEntity == null || htcConfirmEntity.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", confirmID));
            return redirect(UrlUtils.htcConfirmIndex());
        }

        if (!htcConfirmEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());
        }

        form.setFormConfirm(htcConfirmEntity);

        model.addAttribute("title", "Xem thông tin khách hàng");
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("options", getOptions());
        model.addAttribute("sourceSites", getSite(siteIDs));

        // Setting display for update
        form.setSiteID(htcConfirmEntity.getSiteID());
        form.setID(confirmID);
        model.addAttribute("tab", tab);
        model.addAttribute("form", form);
        return "backend/htc_confirm/view";
    }

    /**
     * Hiển thị màn hình cập nhật khách hàng khẳng định
     *
     * @author TrangBN
     * @param model
     * @param confirmID
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc-confirm/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long confirmID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            HtcConfirmForm form,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirmEntity = htcConfirmService.findOne(confirmID);

        if (htcConfirmEntity.getPacPatientID() != null || htcConfirmEntity.getPacSentDate() != null) {
            redirectAttributes.addFlashAttribute("error", "Khách hàng đã được chuyển gửi GSPH, không thể cập nhật thông tin khách hàng.");
            return redirect(UrlUtils.htcConfirmIndex());
        }

        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }

        if (htcConfirmEntity == null || htcConfirmEntity.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", confirmID));
            return redirect(UrlUtils.htcConfirmIndex());
        }

        if (!htcConfirmEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());
        }

        // Set dữ liệu hiển thị trang update
        form.setFormConfirm(htcConfirmEntity);

//        form.setTestResultsID("2");
        model.addAttribute("title", "Cập nhật thông tin khách hàng");
        model.addAttribute("sourceSites", getSiteSentOption(siteIDs));
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("tab", tab);
        //AnhDSN 10/09/2019 3:55 PM
        model.addAttribute("testResultsOptions", getTestResultsOption());

        // Setting display for update
        form.setSiteID(htcConfirmEntity.getSiteID());
        form.setID(confirmID);

        model.addAttribute("form", form);
        return "backend/htc_confirm/new";
    }

    @RequestMapping(value = {"/htc-confirm/update-early.html"}, method = RequestMethod.GET)
    public String actionUpdateEarly(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long confirmID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            HtcConfirmEarlyHivForm form,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity confirm = htcConfirmService.findOne(confirmID);

        if (confirm == null || confirm.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", confirmID));
            return redirect(UrlUtils.htcConfirmIndex(tab));
        }
        if (!confirm.getResultsID().equals("2")) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng mã %s không là dương tính", confirmID));
            return redirect(UrlUtils.htcConfirmIndex(tab));
        }

        form = new HtcConfirmEarlyHivForm();
        form.setID(confirmID);
        form.setEarlyHivDate(TextUtils.formatDate(confirm.getEarlyHivDate(), FORMATDATE));
        form.setEarlyHiv(confirm.getEarlyHiv());
        form.setEarlyBioName(confirm.getEarlyBioName());
        form.setEarlyDiagnose(confirm.getEarlyDiagnose());
        form.setVirusLoad(confirm.getVirusLoad());
        form.setVirusLoadDate(TextUtils.formatDate(confirm.getVirusLoadDate(), FORMATDATE));
        form.setVirusLoadNumber(confirm.getVirusLoadNumber());

        model.addAttribute("title", "Cập nhật thông tin nhiễm mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("tab", tab);

        model.addAttribute("form", form);
        return "backend/htc_confirm/early";
    }

    @RequestMapping(value = {"/htc-confirm/update-early.html"}, method = RequestMethod.POST)
    public String doActionUpdateEarly(Model model,
            @ModelAttribute("form") @Valid HtcConfirmEarlyHivForm form,
            @RequestParam(name = "oid", defaultValue = "") Long confirmID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity confirmEntity = htcConfirmService.findOne(confirmID);
        if (confirmEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());
        }

        model.addAttribute("title", "Cập nhật thông tin nhiễm mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("form", form);
        model.addAttribute("tab", tab);

        form.setConfirmTime(TextUtils.formatDate(confirmEntity.getConfirmTime(), FORMATDATE));

        htcConfirmEarlyValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thông tin khách hàng thất bại.");
            return "backend/htc_confirm/early";
        }
        // Gán giá trị cho entity
        try {
            confirmEntity.setEarlyBioName(form.getEarlyBioName());
            confirmEntity.setEarlyHivDate(TextUtils.convertDate(form.getEarlyHivDate(), "dd/MM/yyyy"));
            confirmEntity.setEarlyHiv(form.getEarlyHiv());
            confirmEntity.setVirusLoadDate(TextUtils.convertDate(form.getVirusLoadDate(), "dd/MM/yyyy"));
            confirmEntity.setVirusLoadNumber(form.getVirusLoadNumber());
            confirmEntity.setVirusLoad(form.getVirusLoad());
            confirmEntity.setEarlyDiagnose(form.getEarlyDiagnose());
            confirmEntity.setEarlyJob(true);

            confirmEntity = htcConfirmService.save(confirmEntity);
            htcConfirmService.log(confirmEntity.getID(), "Cập nhật thông tin nhiễm mới");
            redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thông tin nhiễm mới thành công.");
            redirectAttributes.addFlashAttribute("success_id", confirmEntity.getID());

//            //Sàng lọc
//            if (confirmEntity.getSourceSiteID() != null && confirmEntity.getSourceID() != null) {
//                HtcVisitEntity visit = htcVisitService.findByCodeAndSite(confirmEntity.getSourceID(), confirmEntity.getSourceSiteID(), false);
//                if (visit != null) {
//                    //Cập nhật thông tin nhiễm mới cho VCT
//                    visit.setEarlyHiv(confirmEntity.getEarlyHiv());
//                    visit.setEarlyHivDate(confirmEntity.getEarlyHivDate());
//                    visit.setEarlyDiagnose(confirmEntity.getEarlyDiagnose());
//                    visit.setEarlyBioName(confirmEntity.getEarlyBioName());
//                    visit.setVirusLoadDate(confirmEntity.getVirusLoadDate());
//                    visit.setVirusLoadNumber(confirmEntity.getVirusLoadNumber());
//                    visit.setVirusLoad(confirmEntity.getVirusLoad());
//                    visit.setUpdateAt(new Date());
//                    visit.setEarlyJob(true);
//                    htcVisitService.update(visit);
//                }
//            }
            return redirect(UrlUtils.htcConfirmIndex(tab));
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/htc_confirm/early";
        }
    }

    /**
     * Cập nhật thông tin khách hàng khẳng định
     *
     * @param model
     * @param form
     * @param confirmID
     * @param tab
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/htc-confirm/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @ModelAttribute("form")
            @Valid HtcConfirmForm form,
            @RequestParam(name = "oid", defaultValue = "") Long confirmID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        String formatDate = "dd/MM/yyyy";
        HtcConfirmEntity confirmEntity = htcConfirmService.findOne(confirmID);
        if (confirmEntity == null || !getCurrentUser().getSite().getID().equals(form.getSiteID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());
        }

//        if (form.isDisabledEarlyHiv()) {
//            form.setEarlyHiv(confirmEntity.getEarlyHiv());
//            form.setEarlyHivDate(TextUtils.formatDate(confirmEntity.getEarlyHivDate(), formatDate));
//        }
//        
//        if (form.isDisabledVirusLoad()) {
//            form.setVirusLoad(confirmEntity.getVirusLoad());
//            form.setVirusLoadDate(TextUtils.formatDate(confirmEntity.getVirusLoadDate(), formatDate));
//        }
        // Lấy thông tin mã cơ sở gửi mẫu
        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteIDs.add(site.getID());
        }

        model.addAttribute("title", "Cập nhật thông tin khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("sourceSites", getSite(siteIDs));
        model.addAttribute("form", form);
        model.addAttribute("tab", tab);

        if (confirmEntity.getResultsReturnTime() == null) {
            htcConfirmValidate.validate(form, bindingResult);
            Set<String> errorDetails = new HashSet<>(CollectionUtils.collect(bindingResult.getAllErrors(), TransformerUtils.invokerTransformer("getField")));
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "Cập nhật thông tin khách hàng thất bại.");
                model.addAttribute("errorDetail", errorDetails);
                model.addAttribute("testResultsOptions", getTestResultsOption());
                return "backend/htc_confirm/new";
            }

            // Gán giá trị cho entity
            try {
                confirmEntity = form.getHtcConfirm(getCurrentUser().getSite().getID(), confirmEntity);
                confirmEntity = htcConfirmService.save(confirmEntity);
                htcConfirmService.log(confirmEntity.getID(), "Cập nhật khách hàng xét nghiệm khẳng định");
                redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thành công.");
                redirectAttributes.addFlashAttribute("success_id", confirmEntity.getID());

                HtcConfirmMessage message = new HtcConfirmMessage();
                message.setConfirm(confirmEntity);
                rabbitSender.sendVisit(message);

                return redirect(UrlUtils.htcConfirmIndex());
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
                return "backend/htc_confirm/new";
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
            form.setConfirmTime(simpleDateFormat.format(confirmEntity.getConfirmTime()));
            htcConfirmValidateUpdate.validate(form, bindingResult);
            if (bindingResult.hasErrors()) {
                String earlyBioName = form.getEarlyBioName();
                String virusLoadNumber = form.getVirusLoadNumber();
                String earlyDiagnose = form.getEarlyDiagnose();

                String earlyHivDate = form.getEarlyHivDate();
                String earlyHiv = form.getEarlyHiv();
                String virusLoad = form.getVirusLoad();
                String virusLoadDate = form.getVirusLoadDate();

                form.setFormConfirm(confirmEntity);
                form.setEarlyHivDate(earlyHivDate);
                if (StringUtils.isEmpty(confirmEntity.getEarlyHiv())) {
                    form.setEarlyHiv(earlyHiv);
                    form.setEarlyHivDate(earlyHivDate);
                }
                if (StringUtils.isEmpty(confirmEntity.getVirusLoad())) {
                    form.setVirusLoad(virusLoad);
                    form.setVirusLoadDate(virusLoadDate);
                }
                form.setVirusLoadDate(virusLoadDate);
                form.setEarlyBioName(earlyBioName);
                form.setVirusLoadNumber(virusLoadNumber);
                form.setEarlyDiagnose(earlyDiagnose);

                Set<String> errorDetails = new HashSet<>(CollectionUtils.collect(bindingResult.getAllErrors(), TransformerUtils.invokerTransformer("getField")));
                model.addAttribute("error", "Cập nhật thông tin khách hàng thất bại.");
                model.addAttribute("errorDetail", errorDetails);
                model.addAttribute("testResultsOptions", getTestResultsOption());
                model.addAttribute("form", form);
                return "backend/htc_confirm/new";
            }

            // Gán giá trị cho entity
            try {
                confirmEntity.setEarlyBioName(form.getEarlyBioName());
                confirmEntity.setEarlyHivDate(TextUtils.convertDate(form.getEarlyHivDate(), "dd/MM/yyyy"));
                confirmEntity.setEarlyHiv(form.getEarlyHiv());
                confirmEntity.setVirusLoadDate(TextUtils.convertDate(form.getVirusLoadDate(), "dd/MM/yyyy"));
                confirmEntity.setVirusLoadNumber(form.getVirusLoadNumber());
                confirmEntity.setVirusLoad(form.getVirusLoad());
                confirmEntity.setEarlyDiagnose(form.getEarlyDiagnose());
                if (StringUtils.isEmpty(confirmEntity.getEarlyHiv()) || (confirmEntity.getEarlyHivDate() == null && !EarlyHivResultEnum.NO_TEST.getKey().equals(confirmEntity.getEarlyHiv()))) {
                    confirmEntity.setEarlyHiv(form.getEarlyHiv());
                    confirmEntity.setEarlyHivDate(TextUtils.convertDate(form.getEarlyHivDate(), "dd/MM/yyyy"));
                }

                if (StringUtils.isEmpty(confirmEntity.getVirusLoad()) || (confirmEntity.getVirusLoadDate() == null && !VirusLoadResultEnum.NO_TEST.getKey().equals(confirmEntity.getVirusLoad()))) {
                    confirmEntity.setVirusLoad(form.getVirusLoad());
                    confirmEntity.setVirusLoadDate(TextUtils.convertDate(form.getVirusLoadDate(), "dd/MM/yyyy"));
                }
                confirmEntity = htcConfirmService.save(confirmEntity);
                htcConfirmService.log(confirmEntity.getID(), "Cập nhật khách hàng xét nghiệm khẳng định");
                redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thành công.");
                redirectAttributes.addFlashAttribute("success_id", confirmEntity.getID());

                HtcConfirmMessage message = new HtcConfirmMessage();
                message.setConfirm(confirmEntity);
                rabbitSender.sendVisit(message);

                return redirect(UrlUtils.htcConfirmIndex());
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
                return "backend/htc_confirm/new";
            }
        }

    }

    /**
     * Lấy danh sách các cơ sở gửi mẫu
     *
     * @param siteIDs
     * @return
     */
    public Map<String, String> getSiteSentOption(Set<Long> siteIDs) {
        Map<String, String> siteOption = getSite(siteIDs);
        return siteOption;
    }

    /**
     * Khôi phục khách hàng
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmIndex());

        }
        htcConfirm.setRemove(false);
        htcConfirmService.save(htcConfirm);
        htcConfirmService.log(htcConfirm.getID(), "Khôi phục khách hàng đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", htcConfirm.getID());
        return redirect(UrlUtils.htcConfirmIndex());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect("/backend/htc-confirm/index.html?tab=remove");

        }
        htcConfirmService.remove(htcConfirm.getID());
        htcConfirmService.log(htcConfirm.getID(), "Xóa vĩnh viễn khách hàng");
        redirectAttributes.addFlashAttribute("success", "Khách hàng đã bị xóa vĩnh viễn");
        return redirect("/backend/htc-confirm/index.html?tab=remove");
    }

    /**
     * Danh sách tiếp nhận khẳng định
     *
     * @author pdThang
     * @param model
     * @param tab
     * @param sourceID
     * @param fullname
     * @param sourceSampleDateFrom
     * @param sourceSampleDateTo
     * @param sourceSiteID
     * @param statusRequests
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = {"/htc-confirm/wait.html"})
    public String actionWait(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "sourceID", required = false) String sourceID,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "source_sample_date_from", required = false) String sourceSampleDateFrom,
            @RequestParam(name = "source_sample_date_to", required = false) String sourceSampleDateTo,
            @RequestParam(name = "source_site_id", required = false) Long sourceSiteID,
            @RequestParam(name = "status-request", required = false) String statusRequests,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmSearch search = new HtcConfirmSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());

        search.setRemove(false);
        if ("remove".equals(tab)) {
            search.setRemove(true);
        }

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (sourceID != null && !"".equals(sourceID)) {
            search.setSourceID(StringUtils.normalizeSpace(sourceID.trim()));
        }
        search.setSourceSampleDateFrom(sourceSampleDateFrom);
        search.setSourceSampleDateTo(sourceSampleDateTo);
        search.setWait(true);
        search.setSourceSiteID(sourceSiteID);
        search.setRequestStatus(StringUtils.isEmpty(statusRequests) ? null : statusRequests);
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updatedAt"}));
        DataPage<HtcConfirmEntity> dataPage = htcConfirmService.find(search);
        Set<Long> siteIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            siteIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSourceSiteID")));// get site ID
        }
        model.addAttribute("title", "Tiếp nhận khẳng định");
//        model.addAttribute("smallTitle", "Xét nghiệm khẳng định");
        model.addAttribute("genderOptions", getOptions().get(ParameterEntity.GENDER));
        model.addAttribute("testObjectGroup", getOptions().get(ParameterEntity.TEST_OBJECT_GROUP));
        model.addAttribute("sites", getSite(siteIDs));

        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID());
        //Sắp xếp để hiển thị tìm kiếm Đơn vị gửi mẫu
        Map<String, String> sitesMap = new HashMap<>();
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
        sortedMap.put("", "Tất cả");
        ArrayList<String> list = new ArrayList<>();
        for (SiteEntity site : sites) {
            sitesMap.put(String.valueOf(site.getID()), site.getName());
        }
        for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String str, String str1) {
                return (str).compareTo(str1);
            }
        });
        for (String str : list) {
            for (Map.Entry<String, String> entry : sitesMap.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }
        String tab2 = "wait";

        HashMap<String, String> statusRequest = new LinkedHashMap<>();
        statusRequest.put("", "Tất cả");
        statusRequest.put("1", "Chưa rà soát");
        statusRequest.put("2", "Đã rà soát");

        model.addAttribute("tab2", tab2);
        model.addAttribute("statusRequest", statusRequest);
        model.addAttribute("siteTests", sortedMap);
        model.addAttribute("dataPage", dataPage);
        return "backend/htc_confirm/wait";
    }

    /**
     * Yêu cầu rà soát tiếp nhận khẳng định
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/wait-request.html", method = RequestMethod.GET)
    public String actionWaitRequest(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID()) || htcConfirm.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmWait());

        }
        HtcVisitEntity htcVisit = htcVisitService.findCode(htcConfirm.getSourceID(), true);
        if (htcVisit == null || htcVisit.isIsRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng mã #" + htcConfirm.getSourceID()) + " tại cơ sở gửi mẫu " + siteService.findOne(htcConfirm.getSourceSiteID()).getName());
            return redirect(UrlUtils.htcConfirmWait());

        }
        htcConfirm.setRequestConfirmTime(new Date());
        htcVisit.setRequestConfirmTime(new Date());
        htcConfirm.setUpdateConfirmTime(null);
        htcVisit.setUpdateConfirmTime(null);

        htcConfirmService.save(htcConfirm);
        htcVisitService.save(htcVisit);

        htcVisitService.log(htcVisit.getID(), "Cơ sở khẳng định " + currentUser.getSite().getName() + " đã yêu cầu rà soát");
        htcConfirmService.log(htcConfirm.getID(), "Gửi yêu cầu rà soát về cơ sở gửi mẫu " + siteService.findOne(htcConfirm.getSourceSiteID()).getName() + " ngày " + TextUtils.formatDate(new Date(), FORMATDATE));
        redirectAttributes.addFlashAttribute("success", "Yêu cầu rà soát thành công");
        return redirect(UrlUtils.htcConfirmWait());
    }

    /**
     * Xóa khách hàng tiếp nhận khẳng định
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/wait-remove.html", method = RequestMethod.GET)
    public String actionWaitRemove(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID()) || htcConfirm.getRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmWait());

        }
        htcConfirmService.delete(htcConfirm.getID());
        htcConfirmService.log(htcConfirm.getID(), "Xóa khách hàng");
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect(UrlUtils.htcConfirmWait());
    }

    /**
     * Khôi phục khách hàng tiếp nhận KĐ
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/wait-restore.html", method = RequestMethod.GET)
    public String actionWaitRestore(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmWait());

        }
        htcConfirm.setRemove(false);
        htcConfirmService.save(htcConfirm);
        htcConfirmService.log(htcConfirm.getID(), "Khôi phục khách hàng đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", htcConfirm.getID());
        return redirect(UrlUtils.htcConfirmWait());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param confirmID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/htc-confirm/wait-delete.html", method = RequestMethod.GET)
    public String actionWaitDelete(Model model,
            @RequestParam(name = "oid") Long confirmID,
            RedirectAttributes redirectAttributes) {

        HtcConfirmEntity htcConfirm = htcConfirmService.findOne(confirmID);
        if (!htcConfirm.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.htcConfirmWait("remove"));

        }
        htcConfirmService.remove(htcConfirm.getID());
        htcConfirmService.log(htcConfirm.getID(), "Xóa vĩnh viễn khách hàng");
        redirectAttributes.addFlashAttribute("success", "Khách hàng đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.htcConfirmWait("remove"));
    }

}
