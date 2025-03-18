package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.AnswerEnum;
import com.gms.entity.constant.ExchangeStatusEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.HtcLaytestForm;
import com.gms.entity.input.LaytestAgencySearch;
import com.gms.entity.input.LaytestSearch;
import com.gms.entity.validate.HtcLaytestValidate;
import com.gms.service.HtcLaytestAgencyService;
import com.gms.service.HtcLaytestService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author TrangBN
 */
@Controller(value = "htc_laytest")
public class HtcLaytestController extends HtcController {

    @Autowired
    HtcLaytestService htcLaytestService;
    @Autowired
    private HtcLaytestValidate htcLaytestValidate;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private HtcLaytestAgencyService agencyService;

    /**
     * Lấy mã code của cán bộ không chuyên đang đăng nhập
     *
     * @return
     */
    private String getStaffCode() {
        String code = parameterService.getStaffConfig(getCurrentUser().getUser().getID(), StaffConfigEnum.LAYTEST_STAFF_CODE.getKey());
        return code == null || "".equals(code) ? null : code;
    }

    protected HashMap<String, HashMap<String, String>> getOptions(boolean scan) {
        HashMap<String, HashMap<String, String>> options = super.getOptions();
        options.put("tookTest", new HashMap<String, String>());
        options.get("tookTest").put("", "Chọn câu trả lời");
        options.get("tookTest").put(AnswerEnum.YES.getKey().toString(), AnswerEnum.YES.getLabel());
        options.get("tookTest").put(AnswerEnum.NO.getKey().toString(), AnswerEnum.NO.getLabel());

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

        if (scan) {
            HashMap<String, String> staffConfig = parameterService.getStaffConfig(getCurrentUser().getUser().getID());
            //Nhóm đối tượng
            if (staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null) != null && !staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).equals("")) {
                options.put(ParameterEntity.TEST_OBJECT_GROUP, findOptions(options.get(ParameterEntity.TEST_OBJECT_GROUP), staffConfig.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null).split(",")));
            }
            
            HashMap<String, String> siteConfig = parameterService.getSiteConfig(getCurrentUser().getSite().getID());
            //very config sinh phẩm
            if (siteConfig.getOrDefault(SiteConfigEnum.LAYTEST_BIOLOGY_PRODUCT.getKey(), null) != null && !siteConfig.getOrDefault(SiteConfigEnum.LAYTEST_BIOLOGY_PRODUCT.getKey(), null).equals("")) {
                options.put(ParameterEntity.BIOLOGY_PRODUCT_TEST, findOptions(options.get(ParameterEntity.BIOLOGY_PRODUCT_TEST), siteConfig.getOrDefault(SiteConfigEnum.LAYTEST_BIOLOGY_PRODUCT.getKey(), null).split(",")));
            }
        }

        return options;
    }

    public Map<String, String> getSiteVisits() {
        HashMap<String, String> siteVisits = new HashMap<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteVisits.put(String.valueOf(site.getID()), site.getName());
        }
        return siteVisits;
    }

    public Map<String, String> getSiteConfirms() {
        HashMap<String, String> siteConfirms = new HashMap<>();
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), getCurrentUser().getSite().getProvinceID());
        for (SiteEntity site : sites) {
            siteConfirms.put(String.valueOf(site.getID()), site.getName());
        }
        return siteConfirms;
    }

    @GetMapping(value = {"/laytest/index.html"})
    public String actionIndex(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "full_name", required = false) String name,
            @RequestParam(name = "advisorye_time", required = false) String advisoryeTime,
            @RequestParam(name = "send_status", required = false, defaultValue = "") String sendStatus,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        if (getStaffCode() == null) {
            model.addAttribute("warning", String.format("Vui lòng cập nhật mã cán bộ xét nghiệm tại cộng đồng. Click <a href='%s'>vào đây</a>!", UrlUtils.staffConfig()));
        }

        LoggedUser currentUser = getCurrentUser();
        LaytestSearch search = new LaytestSearch();
        if (code != null && !"".equals(code)) {
            search.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        if (name != null && !"".equals(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        search.setVisitRemove(0);
        search.setAdvisoryeTime(advisoryeTime);
        search.setSendStatus(sendStatus);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());
        search.setStaffID(currentUser.getUser().getID());
        search.setRemove(false);
        if ("delete".equals(tab)) {
            search.setRemove(true);
        }
        DataPage<HtcLaytestEntity> dataPage = htcLaytestService.find(search);

        model.addAttribute("title", "Khách hàng");
        model.addAttribute("smallTitle", "Xét nghiệm tại cộng đồng");
        model.addAttribute("testResultSoptions", getOptions(false).get(ParameterEntity.TEST_RESULTS));
        model.addAttribute("genderOptions", getOptions(false).get(ParameterEntity.GENDER));
        model.addAttribute("testResultOptions", getOptions(false).get(ParameterEntity.TEST_RESULTS_CONFIRM));
        model.addAttribute("referralSourceOptions", getOptions(false).get(ParameterEntity.REFERENT_SOURCE));
        model.addAttribute("siteConfirmOptions", getSiteConfirms());
        model.addAttribute("statusOptions", getOptions(false).get("sendStatus"));
        model.addAttribute("sites", getSiteVisits());
        model.addAttribute("dataPage", dataPage);
        return "backend/htc_laytest/index";
    }

    /**
     * Trang thêm mới khách hàng từ xét nghiệm không chuyên
     *
     * @param model
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/laytest/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            HtcLaytestForm form,
            RedirectAttributes redirectAttributes) {

        if (getStaffCode() == null) {
            redirectAttributes.addFlashAttribute("warning", "Vui lòng cập nhật mã cán bộ xét nghiệm tại cộng đồng");
            return redirect(UrlUtils.staffConfig());
        }

        // Set mã tự tăng 
        String codeGen = htcLaytestService.getCode(parameterService.getStaffConfig(getCurrentUser().getUser().getID(), StaffConfigEnum.LAYTEST_STAFF_CODE.getKey()));
        form.setCode(codeGen);

        form.setServiceID(ServiceTestEnum.KC.getKey());
        form.setRaceID(RaceEnum.KINH.getKey());
        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions(true));

        return "backend/htc_laytest/form";
    }

    /**
     * Thêm mới khách hàng dịch vụ không chuyên
     *
     * @param model
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/laytest/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid HtcLaytestForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        // Lọc lại danh sách agencies
        List<HtcLaytestAgencyEntity> agencyList = new ArrayList<>();
        if (form.getLaytestAgencies() != null && form.getLaytestAgencies().size() > 0) {
            for (HtcLaytestAgencyEntity laytestAgency : form.getLaytestAgencies()) {
                if (laytestAgency.checkNullElement()) {
                    continue;
                }
                agencyList.add(laytestAgency);
            }
        }
        form.setLaytestAgencies(agencyList);

        model.addAttribute("title", "Khách hàng mới");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions(true));

        form.setSiteID(getCurrentUser().getSite().getID());
        htcLaytestValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm mới thông tin khách hàng thất bại");
            return "backend/htc_laytest/form";
        }

        try {
            HtcLaytestEntity laytest = form.getHtcLayTestEntity(getCurrentUser().getSite().getID(), null);

            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-sent-sample".equals(form.getPageRedirect())) {
                laytest.setSampleSentDate(new Date());
                laytest = htcLaytestService.save(laytest);
                htcLaytestService.log(laytest.getID(), "Đã gửi thông tin khách hàng sang " + siteService.findOne(Long.valueOf(laytest.getSiteVisitID())).getName());

                // Gửi mail thông báo
                String email = parameterService.getSiteConfig(Long.valueOf(laytest.getSiteVisitID()), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());
                StaffEntity staff = staffService.findOne(laytest.getUpdatedBY());
                if (staff != null) {
                    sendEmailNotify(email,
                            String.format("Cán bộ xét nghiệm tại cộng đồng %s chuyển KH mã %s", staff.getName(), laytest.getCode()),
                            String.format(
                                    "Khách hàng mã %s được chuyển gửi ngày %s",
                                    laytest.getCode(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                            ));
                }
            } else {
                laytest = htcLaytestService.save(laytest);
            }

            if (laytest == null) {
                throw new Exception("Lỗi hệ thống không thể thêm mới khách hàng");
            }

            // Lưu thông tin người được giới thiệu
            if (form.getLaytestAgencies() != null && form.getLaytestAgencies().size() > 0) {
                List<HtcLaytestAgencyEntity> angencies = new ArrayList<>();
                HtcLaytestAgencyEntity angency;
                for (HtcLaytestAgencyEntity laytestAgency : form.getLaytestAgencies()) {
                    angency = new HtcLaytestAgencyEntity();
                    angency.setLaytestID(laytest.getID());
                    angency.setFullname(TextUtils.toFullname(laytestAgency.getFullname()));
                    angency.setAddress(laytestAgency.getAddress());
                    angency.setAlertType(laytestAgency.getAlertType());
                    angency.setPhone(laytestAgency.getPhone());
                    angency.setIsAgreePreTest(laytestAgency.getIsAgreePreTest());
                    angencies.add(angency);
                }

                // Lưu thông tin người được giới thiệu
                agencyService.saveAll(laytest.getID(), angencies);
            } else {
                // Lưu thông tin người được giới thiệu
                agencyService.saveAll(laytest.getID(), null);
            }
            
            htcLaytestService.log(laytest.getID(), "Thêm mới khách hàng từ dịch vụ xét nghiệm tại cộng đồng");
            redirectAttributes.addFlashAttribute("success", "Khách hàng được đăng ký thành công.");
            redirectAttributes.addFlashAttribute("success_id", laytest.getID());
            return redirect(UrlUtils.laytest());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "backend/htc_laytest/form";
        }
    }

    @RequestMapping(value = {"/laytest/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long visitID,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            HtcLaytestForm form,
            RedirectAttributes redirectAttributes) {
        HtcLaytestEntity laytest = htcLaytestService.findOne(visitID);

        if (laytest == null || laytest.isRemove() || !laytest.getCreatedBY().equals(getCurrentUser().getUser().getID())) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin khách hàng");
            return redirect(UrlUtils.laytest());
        }

        // Kiểm tra khách hàng đã được chuyển gửi ko cho update
        if (laytest.getAcceptDate() != null) {
            redirectAttributes.addFlashAttribute("error", "Khách hàng đã được tiếp nhận, không được sửa thông tin.");
            return redirect(UrlUtils.laytestView(visitID));
        }

        // Lấy danh dách người được giới thiệu
        List<HtcLaytestAgencyEntity> laytestAgencies = agencyService.findByLaytestID(visitID);
        form.setLaytestAgencies(laytestAgencies != null && !laytestAgencies.isEmpty() ? laytestAgencies : null);

        form.setHtcLaytetsForm(laytest);
        model.addAttribute("title", "Cập nhật khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("tab", tab);
        model.addAttribute("options", getOptions(true));
        return "backend/htc_laytest/form";
    }

    @RequestMapping(value = {"/laytest/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long laytestID,
            @ModelAttribute("form") @Valid HtcLaytestForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytest = htcLaytestService.findOne(laytestID);

        if (laytest == null || laytest.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", laytestID));
            return redirect(UrlUtils.laytest());
        }

        // Lọc lại danh sách agencies
        List<HtcLaytestAgencyEntity> agencyList = new ArrayList<>();
        if (form.getLaytestAgencies() != null && form.getLaytestAgencies().size() > 0) {
            for (HtcLaytestAgencyEntity laytestAgency : form.getLaytestAgencies()) {
                if (laytestAgency.checkNullElement()) {
                    continue;
                }
                agencyList.add(laytestAgency);
            }
        }
        form.setLaytestAgencies(agencyList);
        
        model.addAttribute("title", "Cập nhật khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions(true));

        htcLaytestValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật thông tin khách hàng thất bại");
            return "backend/htc_laytest/form";
        }

        try {

            laytest = form.getHtcLayTestEntity(getCurrentUser().getSite().getID(), laytest);
            if (StringUtils.isNotEmpty(form.getPageRedirect()) && "save-sent-sample".equals(form.getPageRedirect())) {
                laytest.setSampleSentDate(new Date());
                laytest = htcLaytestService.save(laytest);
                htcLaytestService.log(laytest.getID(), "Đã gửi thông tin khách hàng sang " + siteService.findOne(Long.valueOf(laytest.getSiteVisitID())).getName());

                // Gửi mail thông báo
                String email = parameterService.getSiteConfig(Long.valueOf(laytest.getSiteVisitID()), SiteConfigEnum.ALERT_HTC_EMAIL.getKey());
                StaffEntity staff = staffService.findOne(laytest.getUpdatedBY());
                if (staff != null) {
                    sendEmailNotify(email,
                            String.format("Cán bộ xét nghiệm tại cộng đồng %s chuyển KH mã %s", staff.getName(), laytest.getCode()),
                            String.format(
                                    "Khách hàng mã %s được chuyển gửi ngày %s",
                                    laytest.getCode(), TextUtils.formatDate(new Date(), "hh:mm:ss dd/MM/yyyy")
                            ));
                }
            } else {
                laytest = htcLaytestService.save(laytest);
            }

            // Lưu thông tin người được giới thiệu
            if (form.getLaytestAgencies() != null && form.getLaytestAgencies().size() > 0) {
                List<HtcLaytestAgencyEntity> angencies = new ArrayList<>();
                HtcLaytestAgencyEntity angency;
                for (HtcLaytestAgencyEntity laytestAgency : form.getLaytestAgencies()) {
                    angency = new HtcLaytestAgencyEntity();
                    angency.setLaytestID(laytest.getID());
                    angency.setFullname(laytestAgency.getFullname());
                    angency.setAddress(laytestAgency.getAddress());
                    angency.setAlertType(laytestAgency.getAlertType());
                    angency.setPhone(laytestAgency.getPhone());
                    angency.setIsAgreePreTest(laytestAgency.getIsAgreePreTest());
                    angencies.add(angency);
                }

                // Lưu thông tin người được giới thiệu
                agencyService.saveAll(laytest.getID(), angencies);
            } else {
                // Lưu thông tin người được giới thiệu
                agencyService.saveAll(laytest.getID(), null);
            }
            htcLaytestService.log(laytest.getID(), "Cập nhật khách hàng từ dịch vụ xét nghiệm tại cộng đồng");

            redirectAttributes.addFlashAttribute("success", "Khách hàng được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", laytest.getID());
            return redirect(UrlUtils.laytest());
        } catch (ParseException e) {
            model.addAttribute("error", e.getMessage());
            return "backend/htc_laytest/form";
        }
    }

    @RequestMapping(value = {"/laytest/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "oid", defaultValue = "") Long ID,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            HtcLaytestForm form,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity htcLaytestEntity = htcLaytestService.findOne(ID);

        if (htcLaytestEntity == null || htcLaytestEntity.isRemove() || !htcLaytestEntity.getCreatedBY().equals(getCurrentUser().getUser().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy khách hàng có mã %s", ID));
            return redirect(UrlUtils.laytest());
        }

        if (!htcLaytestEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.laytest());
        }

        // Lấy danh dách người được giới thiệu
        List<HtcLaytestAgencyEntity> laytestAgencies = agencyService.findByLaytestID(ID);
        form.setLaytestAgencies(laytestAgencies != null && !laytestAgencies.isEmpty() ? laytestAgencies : null);

        form.setHtcLaytetsForm(htcLaytestEntity);
        model.addAttribute("title", "Xem thông tin khách hàng");
        model.addAttribute("parent_title", "Khách hàng");
        model.addAttribute("options", getOptions(true));
        model.addAttribute("sites", getSiteVisits());
        model.addAttribute("tab", tab);
        // Setting display for update
        form.setSiteID(htcLaytestEntity.getSiteID());
        form.setID(ID);
        model.addAttribute("form", form);
        return "backend/htc_laytest/view";
    }

    @RequestMapping(value = "/laytest/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long ID,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytestEntity = htcLaytestService.findOne(ID);
        if (!laytestEntity.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect("/backend/laytest/index.html");
        }
        htcLaytestService.remove(laytestEntity);
        htcLaytestService.log(laytestEntity.getID(), "Xóa khách hàng");
        redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công");
        return redirect("/backend/laytest/index.html");
    }

    /**
     * Khôi phục khách hàng
     *
     * @author PdThang
     * @param model
     * @param laytestID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/laytest/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long laytestID,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytest = htcLaytestService.findOne(laytestID);
        if (!laytest.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.laytest());

        }

        htcLaytestService.restoreLaytest(laytest);
        htcLaytestService.log(laytest.getID(), "Khôi phục khách hàng đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục khách hàng đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", laytest.getID());
        return redirect(UrlUtils.laytest());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param laytestID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/laytest/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long laytestID,
            RedirectAttributes redirectAttributes) {

        HtcLaytestEntity laytest = htcLaytestService.findOne(laytestID);
        if (!laytest.getSiteID().equals(getCurrentUser().getSite().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Khách hàng không tồn tại trong cơ sở"));
            return redirect(UrlUtils.laytestIndex("delete"));

        }
        htcLaytestService.delete(laytest.getID());
        htcLaytestService.log(laytest.getID(), "Xóa vĩnh viễn khách hàng");
        redirectAttributes.addFlashAttribute("success", "Khách hàng đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.laytestIndex("delete"));
    }

    @GetMapping(value = {"/laytest/agency.html"})
    public String actionAgency(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        LoggedUser currentUser = getCurrentUser();
        LaytestAgencySearch search = new LaytestAgencySearch();
        if (code != null && !"".equals(code)) {
            search.setCode(StringUtils.normalizeSpace(code.trim()));
        }
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (phone != null && !"".equals(phone)) {
            search.setPhone(StringUtils.normalizeSpace(phone.trim()));
        }
        search.setStatus(status);
        search.setCreatedBY(currentUser.getUser().getID());
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<HtcLaytestAgencyEntity> dataPage = agencyService.find(search);

        //Hiển thị detail giới thiệu bạn nhiễm bạn tình
        Set<Long> laytestIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            laytestIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getLaytestID")));// get site ID
        }
        Map<String, String> fullnames = new HashMap<>();
        Map<String, String> codes = new HashMap<>();
        if (laytestIDs.size() > 0) {
            for (HtcLaytestEntity laytestEntity : htcLaytestService.findAllByIDs(laytestIDs)) {
                fullnames.put(String.valueOf(laytestEntity.getID()), laytestEntity.getPatientName());
                codes.put(String.valueOf(laytestEntity.getID()), laytestEntity.getCode());
            }
        }

        Map<String, List<HtcLaytestAgencyEntity>> list = new HashMap<>();
        for (Long item : laytestIDs) {
            list.put(String.valueOf(item), new ArrayList<HtcLaytestAgencyEntity>());
        }
        for (HtcLaytestAgencyEntity entity : dataPage.getData()) {
            list.get(String.valueOf(entity.getLaytestID())).add(entity);
        }

        Map<String, String> statusOptions = new HashMap<>();
        statusOptions.put("", "Chọn trạng thái");
        statusOptions.put("1", "Có");
        statusOptions.put("0", "Không");

        model.addAttribute("list", list);
        model.addAttribute("title", "Khách hàng");
        model.addAttribute("smallTitle", "Xét nghiệm không chuyên");
        model.addAttribute("fullnames", fullnames);
        model.addAttribute("codes", codes);
        model.addAttribute("statusOptions", statusOptions);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("alertTypes", getOptions(false).get(ParameterEntity.ALERT_TYPE));
        return "backend/htc_laytest/angency";
    }

}
