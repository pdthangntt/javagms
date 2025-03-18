package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.excel.report.TT09Excel;
import com.gms.entity.form.report.TT09Form;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import com.gms.service.HtcVisitService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * BC TT09/2012/TT-BYT quốc gia
 *
 * @author vvthành
 */
@Controller(value = "opc_report_tt09")
public class ReportTT09Controller extends BaseController {

    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private HtcVisitRepositoryImpl visitRepositoryImpl;

    private HashMap<String, SiteEntity> getSites() {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, SiteEntity> option = new LinkedHashMap<>();
        option.put(String.valueOf(currentUser.getSite().getID()), currentUser.getSite());
        List<Long> ids = siteService.getProgenyID(currentUser.getSite().getID());
        Set<String> services = new HashSet<>();
        services.add(ServiceEnum.HTC.getKey());
        for (SiteEntity site : siteService.findByServiceAndSiteID(services, new HashSet<>(ids))) {
            option.put(String.valueOf(site.getID()), site);
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

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.SERVICE_TEST);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        Set<Long> siteIds = getSiteIds();
        Calendar c = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        HtcVisitEntity visit = visitService.getFirst(siteIds);
        if (visit != null) {
            c.setTime(visit.getAdvisoryeTime());
        }
        options.put("years", new LinkedHashMap<String, String>());
        for (int i = c.get(Calendar.YEAR); i <= year; i++) {
            options.get("years").put(String.valueOf(i), String.format("Năm %s", i));
        }

        return options;
    }

    private TT09Form getData(String month, String year, String services, String district, String sites) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        HashMap<String, SiteEntity> siteOptions = getSites();

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        Date firstDay = TextUtils.getFirstDayOfMonth(m, y);
        Date lastDay = TextUtils.getLastDayOfMonth(m, y);

        TT09Form form = new TT09Form();
        form.setFileName(String.format("PhuLuc01_TT092012TTBYT__thang%s_%s_xuat%s", m, y, TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy")));
        form.setTitle("BÁO CÁO KẾT QUẢ XÉT NGHIỆM HIV TRONG THÁNG");
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setDistrict(district);
        form.setMonth(String.valueOf(m));
        form.setYear(String.valueOf(y));
        form.setStartDate(TextUtils.formatDate(firstDay, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(lastDay, FORMATDATE));
        form.setServices(services.equals("") ? null : new ArrayList<>(Arrays.asList(services.split(",", -1))));
        form.setSites(sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1))));
        form.setTTYT(isTTYT());
        form.setPAC(isPAC());
        form.setVAAC(isVAAC());

        List<String> mServices = new ArrayList<>();
        if (form.getServices() != null) {
            mServices.addAll(form.getServices());
        } else {
            mServices = new ArrayList<>(options.get(ParameterEntity.SERVICE_TEST).keySet());
            mServices.removeAll(Collections.singleton(""));
        }

        List<Long> mSites = new ArrayList<>();
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            //Khi search cơ sở
            for (String siteID : form.getSites()) {
                mSites.add(Long.valueOf(siteID));
            }
        } else if (StringUtils.isNotEmpty(form.getDistrict())) {
            //Khi có search district nhưng không search cơ sở
            for (Map.Entry<String, SiteEntity> entry : siteOptions.entrySet()) {
                SiteEntity site = entry.getValue();
                if (site.getDistrictID().equals(form.getDistrict())) {
                    mSites.add(site.getID());
                }
            }
        } else {
            //Mặc định lấy tất cả
            for (Map.Entry<String, SiteEntity> entry : siteOptions.entrySet()) {
                SiteEntity site = entry.getValue();
                mSites.add(site.getID());
            }
        }

        //set label
        form.setServiceLabel("Tất cả");
        form.setSiteLabel("Tất cả");
        if (form.getServices() != null && !form.getServices().isEmpty()) {
            String str = "";
            for (String service : mServices) {
                str = str.concat(options.get(ParameterEntity.SERVICE_TEST).getOrDefault(service, String.format("#%s", service))).concat(";");
            }
            form.setServiceLabel(str);
        }
        if (form.getSites() != null && !form.getSites().isEmpty()) {
            String str = "";
            for (Long siteID : mSites) {
                SiteEntity site = siteOptions.getOrDefault(siteID, null);
                if (site == null) {
                    continue;
                }
                str = str.concat(site.getShortName()).concat(";");
            }
            form.setSiteLabel(str);
        }

        form.setTable(visitRepositoryImpl.getTablePhuluc01TT09(mSites, firstDay, lastDay, mServices, null, null));
        return form;
    }

    @GetMapping(value = {"/tt09/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "district", defaultValue = "") String district,
            @RequestParam(name = "sites", defaultValue = "") String sites) {
        TT09Form form = getData(month, year, services, district, sites);
        model.addAttribute("title", "Báo cáo TT09/2012/TT-BYT");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        model.addAttribute("sites", getSites());
        return "report/report/tt09-index";
    }

    @GetMapping(value = {"/tt09/email.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "district", defaultValue = "") String district,
            @RequestParam(name = "sites", defaultValue = "") String sites) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.tt09("index"));
        }
        TT09Form form = getData(month, year, services, district == null || "undefined".equals(district) ? "" : district, sites);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo TT09/2012/TT-BYT tháng %s năm %s", form.getMonth(), form.getYear()),
                EmailoutboxEntity.TEMPLATE_REPORT_09_PHULUC01,
                context,
                new TT09Excel(form, getOptions(), getSites(), EXTENSION_EXCEL_X));
        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.tt09("index"));
    }

    @GetMapping(value = {"/tt09/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "district", defaultValue = "") String district,
            @RequestParam(name = "sites", defaultValue = "") String sites) throws Exception {
        return exportExcel(new TT09Excel(getData(month, year, services, district == null || "undefined".equals(district) ? "" : district, sites), getOptions(), getSites(), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/tt09/pdf.html"})
    public ResponseEntity<InputStreamResource> actionIn(
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "services", defaultValue = "") String services,
            @RequestParam(name = "district", defaultValue = "") String district,
            @RequestParam(name = "sites", defaultValue = "") String sites,
            RedirectAttributes redirectAttributes) throws Exception {
        TT09Form form = getData(month, year, services, district == null || "undefined".equals(district) ? "" : district, sites);
        form.setPrintable(true);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("options", getOptions());
        context.put("sites", getSites());
        return exportPdf(form.getFileName(), "report/report/tt09-pdf.html", context);
    }
}
