package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.opc.HtcConfirmEarlyExcel;
import com.gms.entity.excel.opc.HtcConfirmPqmIndicatorExcel;
import com.gms.entity.form.htc_confirm.PQMForm;
import com.gms.entity.form.opc_arv.EarlyHtcConfirmReportForm;
import com.gms.entity.form.opc_arv.PqmHtcConfirmReportTable;
import com.gms.entity.input.HtcConfirmEarlySearch;
import com.gms.repository.impl.HtcConfirmRepositoryImpl;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.PqmService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DDanh sach nhiem moi
 *
 * @author pdThang
 */
@Controller(value = "htc_confirm_early_report")
public class HtcConfirmEarlyController extends OpcController {

    @Autowired
    private PqmService pqmService;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private LocationsService locationsService;

    private HashMap<String, SiteEntity> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            option.put(siteEntity.getID().toString(), siteEntity);
        }

        return option;
    }

    private Set<Long> getSiteIds() {
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, SiteEntity> entry : getSites().entrySet()) {
            ids.add(entry.getValue().getID());
        }
        return ids;
    }

    private HashMap<String, String> getTestObjects() {
        HashMap<String, String> object = new HashMap<>();
        object.put("1", "TCMT");
        object.put("3", "MSM");
        object.put("2", "Người bán dâm");
        object.put("13", "TG");
        object.put("33", "Vợ/chồng/bạn tình của người NCMT");
        object.put("17", "Người mua dâm");
        object.put("18", "Nhiều bạn tình");
        object.put("10", "Bệnh nhân nghi AIDS");
        object.put("6", "TB");
        object.put("32", "STD");
        object.put("19", "Phạm nhân");
        object.put("5", "PNMT");
        object.put("5.1", "PNMT");
        object.put("5.2", "Phụ nữ chuyển dạ đẻ");
        object.put("34", "Người bán máu");
        object.put("35", "Người hiến máu tình nguyện");
        object.put("36", "Người nhà cho máu");
        object.put("12", "NVQS");
        object.put("7", "Khác");

        return object;
    }

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.BIOLOGY_PRODUCT_TEST);
        parameterTypes.add(ParameterEntity.BIO_NAME_RESULT);
        parameterTypes.add(ParameterEntity.VIRUS_LOAD);
        parameterTypes.add(ParameterEntity.EARLY_HIV);
        parameterTypes.add(ParameterEntity.EARLY_DIAGNOSE);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int to = Calendar.getInstance().get(Calendar.YEAR);

        HtcVisitEntity entity = htcVisitService.getFirst(siteIds);
        if (entity != null) {
            c.setTime(entity.getAdvisoryeTime());
        }
        options.put("tos", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= to; i++) {
            options.get("tos").put(String.valueOf(i), String.format("Năm %s", i));
        }
        options.put("province", new LinkedHashMap<String, String>());
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            options.get("province").put(String.valueOf(provinceEntity.getID()), provinceEntity.getName());
        }
        options.put("district", new LinkedHashMap<String, String>());
        for (DistrictEntity districtEntity : locationsService.findAllDistrict()) {
            options.get("district").put(String.valueOf(districtEntity.getID()), districtEntity.getName());
        }
        options.put("ward", new LinkedHashMap<String, String>());
        for (WardEntity ward : locationsService.findAllWard()) {
            options.get("ward").put(String.valueOf(ward.getID()), ward.getName());
        }
        return options;
    }

    private EarlyHtcConfirmReportForm getData(String from, String to, String sites, int page, int size) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> siteOptions = getSites();
        Date date = new Date();

        from = from == null || from.equals("") || !isThisDateValid(from) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(date), FORMATDATE) : from;
        to = to == null || to.equals("") || !isThisDateValid(to) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(date), FORMATDATE) : to;

        EarlyHtcConfirmReportForm form = new EarlyHtcConfirmReportForm();
        form.setStaffName(currentUser.getUser().getName());
        form.setFileName("DS_NHIEMMOI_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitle("Danh sách khách hàng nhiễm mới");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setStartDate(from);
        form.setEndDate(to);
        form.setSites(sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1))));
        form.setOptions(getOptions());
        List<Long> sourceSites = null;
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            sourceSites = new ArrayList<>();
            for (String site : form.getSites()) {
                sourceSites.add(Long.valueOf(site));
            }
        }

        form.setOpc(isOPC());
        form.setOpcManager(isOpcManager());
        form.setObjectGroups(getTestObjects());

        HashMap<String, String> siteOption = new LinkedHashMap<>();
        siteOption.put("", "Tất cả");
        for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), currentUser.getSite().getProvinceID())) {
            siteOption.put(siteEntity.getID().toString(), siteEntity.getName());
        }

        HtcConfirmEarlySearch search = new HtcConfirmEarlySearch();
        search.setEarlyHivTimeFrom(from);
        search.setEarlyHivTimeTo(to);
        search.setSourceSiteID(sourceSites);
        search.setSiteID(currentUser.getSite().getID());
        search.setPageIndex(1);
        search.setPageSize(99999999);

        List<HtcConfirmEntity> datas = confirmService.findEarlyHIV(search).getData();
        List<HtcConfirmEntity> items = new ArrayList<>();
        List<String> addresss;
        if (datas != null) {
            for (HtcConfirmEntity data : datas) {
                addresss = new LinkedList<>();
                if (!StringUtils.isEmpty(data.getAddress())) {
                    addresss.add(data.getAddress());
                }
                if (!StringUtils.isEmpty(data.getPermanentAddressStreet())) {
                    addresss.add(data.getPermanentAddressStreet());
                }
                if (!StringUtils.isEmpty(data.getPermanentAddressGroup())) {
                    addresss.add(data.getPermanentAddressGroup());
                }

                data.setAddress(addresss.toString().replaceAll("\\[|\\]", ""));
                items.add(data);
            }
        }

        form.setItems(items);
        form.setSiteOptions(siteOption);
        return form;
    }

    @GetMapping(value = {"/htc-confirm/early-index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {
        EarlyHtcConfirmReportForm form = getData(from, to, sites, page, size);
        model.addAttribute("parent_title", "Xét nghiệm khẳng định");
        model.addAttribute("title", "Danh sách khách hàng nhiễm mới");
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        HashMap<String, SiteEntity> listSites = new HashMap<>();
        for (SiteEntity item : siteService.getSiteOpc(getCurrentUser().getSite().getProvinceID())) {
            listSites.put(item.getID().toString(), item);
        }

        model.addAttribute("sites", listSites);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isOpcManager", form.isOpcManager());
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("currentSite", getCurrentUser().getSite().getName());
//        model.addAttribute("title_small", isOpcManager() ? "Khoa điều trị" : "Cơ sở điều trị");
        return "report/htc_confirm/early-index";
    }

    @GetMapping(value = {"/htc-confirm/early-pdf.html"})
    public ResponseEntity<InputStreamResource> actionPDF(@RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {
        EarlyHtcConfirmReportForm form = getData(from, to, sites, page, size);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        return exportPdf("Danh sách nhiễm mới", "report/htc_confirm/early_hiv_pdf.html", context);
    }

    @GetMapping(value = {"/htc-confirm/early-email.html"})
    public String actionPhuluc02email(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.earlyConfirmIndex());
        }
        EarlyHtcConfirmReportForm form = getData(from, to, sites, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Danh sách khách hàng nhiễm mới (%s - %s)", form.getStartDate(), form.getEndDate()),
                EmailoutboxEntity.EARLY_HIV_CONFIRM,
                context,
                new HtcConfirmEarlyExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.earlyConfirmIndex());
    }

    @GetMapping(value = {"/htc-confirm/early-excel.html"})
    public ResponseEntity<InputStreamResource> actionPhuluc02Excel(
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {
        EarlyHtcConfirmReportForm form = getData(from, to, sites, page, size);
        return exportExcel(new HtcConfirmEarlyExcel(form, EXTENSION_EXCEL_X));

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
