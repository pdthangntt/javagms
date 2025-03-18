package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardHtcImpl;
import com.gms.service.HtcConfirmService;
import com.gms.service.LocationsService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardHtc24Controller extends DashboardController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private DashboardHtcImpl dashboardHtcImpl;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private SiteService siteService;

    /**
     * pdThang
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) {
        LoggedUser currentUser = getCurrentUser();
        List<Long> siteConfirms = new ArrayList<>();
        for (SiteEntity siteEntity : siteService.getSiteConfirm(currentUser.getSite().getProvinceID())) {
            siteConfirms.add(siteEntity.getID());
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(Long.valueOf("-1"));
        }

        Date d = new Date();
        Date fromDate = new Date();
        HtcConfirmEntity confirm = confirmService.getFirst(new HashSet<>(siteConfirms));
        if (confirm != null && confirm.getConfirmTime() != null) {
            fromDate = confirm.getConfirmTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(d), FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart01d24(siteConfirms, form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = TextUtils.formatDate(beginCalendar.getTime(), "M/yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new LinkedHashMap<>();
                item.put("thang", "0");
                item.put("nhohon6", Long.parseLong("0"));
                item.put("lonhon12", Long.parseLong("0"));
                item.put("tongxnnhiemmoi", Long.parseLong("0"));
            }
            item.put("thang", String.format("%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return new Response<>(true, result);

    }

    /**
     * @author DSNAnh
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        //Lấy ID các cơ sở khẳng định trong tỉnh
        List<SiteEntity> sites = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        List<Long> siteConfirms = new ArrayList<>();
        for (SiteEntity site : sites) {
            siteConfirms.add(site.getID());
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);
        Date fromDate = TextUtils.getFirstDayOfYear(cal.getTime());
        Date toDate = TextUtils.getLastDayOfYear(cal.getTime());
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(toDate, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD24Chart02(siteConfirms, form.getSite(), form.getServiceTest(), fromTime, toTime);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        List<String> districtList = new ArrayList<>();
        List<DistrictEntity> district = locationService.findDistrictByIDs(new HashSet(form.getDistrict()));
        for (DistrictEntity e : district) {
            districtList.add(e.getName());
        }

        for (String districtName : districtList) {
            if (data.get(districtName) == null) {
                item = new LinkedHashMap<>();
                item.put("huyen", districtName);
                item.put("tongxnnhiemmoi", Long.valueOf("0"));
                data.put(item.get("huyen").toString(), item);
            }
        }
        for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
            item = entry.getValue();
            result.add(item);
        }
        return new Response<>(true, result);
    }

    /**
     * Số ca có kết quả XN nhiễm mới <6 tháng
     *
     * @auth TrangBN
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) {

        Calendar cal = Calendar.getInstance();

        // Lấy ID cơ sở khẳng định của tỉnh
        List<SiteEntity> siteConfirms = siteService.getSiteConfirm(getCurrentUser().getSiteProvince().getID());
        List<Long> confirmIDs = new ArrayList<>();
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            confirmIDs.add(-1L);
        } else {
            confirmIDs = new ArrayList<>(CollectionUtils.collect(siteConfirms, TransformerUtils.invokerTransformer("getID")));
        }

        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date lastDayQ4 = TextUtils.getLastDayOfYear(cal.getTime());
        Date firstDayQ1 = TextUtils.getFirstDayOfYear(cal.getTime());

        String fromTime = TextUtils.formatDate(firstDayQ1, FORMATDATE);
        String toTime = TextUtils.formatDate(lastDayQ4, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD24Chart03(form.getSite(), form.getServiceTest(), fromTime, toTime, confirmIDs);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));

        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = ((beginCalendar.get(Calendar.MONTH)) / 3 + 1) + TextUtils.formatDate(beginCalendar.getTime(), "/yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("nam", Long.valueOf("0"));
                item.put("nu", Long.valueOf("0"));
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * @author DSNAnh
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        //Lấy ID các cơ sở khẳng định trong tỉnh
        List<SiteEntity> sites = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        List<Long> siteConfirms = new ArrayList<>();
        for (SiteEntity site : sites) {
            siteConfirms.add(site.getID());
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);
        Date fromDate = TextUtils.getFirstDayOfYear(cal.getTime());
        Date toDate = TextUtils.getLastDayOfYear(cal.getTime());
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(toDate, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD24Chart04(siteConfirms, form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));

        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
//        finishCalendar.add(Calendar.MONTH, 3);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = ((beginCalendar.get(Calendar.MONTH)) / 3 + 1) + TextUtils.formatDate(beginCalendar.getTime(), "/yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new LinkedHashMap<>();
                item.put("quy", "0");
                item.put("duoi15", Long.valueOf("0"));
                item.put("15den24", Long.valueOf("0"));
                item.put("25den49", Long.valueOf("0"));
                item.put("tren49", Long.valueOf("0"));
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * @author DSNAnh
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        //Lấy ID các cơ sở khẳng định trong tỉnh
        List<SiteEntity> sites = siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID());
        List<Long> siteConfirms = new ArrayList<>();
        for (SiteEntity site : sites) {
            siteConfirms.add(site.getID());
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);
        Date fromDate = TextUtils.getFirstDayOfYear(cal.getTime());
        Date toDate = TextUtils.getLastDayOfYear(cal.getTime());
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(toDate, FORMATDATE);
        Map<String, Long> data = dashboardHtcImpl.getHtcD24Chart05(siteConfirms, form.getSite(), form.getServiceTest(), fromTime, toTime);
        return new Response<>(true, getTestObjects(data));
    }

    /**
     * D24 Chart 60 Số ca có kết quả XN nhiễm mới <6 tháng @
     *
     *
     * authh TrangBN @param form @return
     */
    @RequestMapping(value = "/dashboard-htc/d24-chart06.json", method = RequestMethod.POST)
    public Response<?> actionChart06(@RequestBody DashboardForm form) {

        Map<String, HashMap<String, String>> options = getOptions(form);

        // Lấy ID cơ sở khẳng định của tỉnh
        List<SiteEntity> siteConfirms = siteService.getSiteConfirm(getCurrentUser().getSiteProvince().getID());
        List<Long> confirmIDs = new ArrayList<>();
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            confirmIDs.add(-1L);
        } else {
            confirmIDs = new ArrayList<>(CollectionUtils.collect(siteConfirms, TransformerUtils.invokerTransformer("getID")));
        }

        //Lấy ngày của năm
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date d = cal.getTime();
        Date fromDate = TextUtils.getFirstDayOfYear(d);

        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(d), FORMATDATE);
        Map<String, Long> items = new HashMap<>();

        Map<String, Long> data = dashboardHtcImpl.getHtcChart06d24(form.getSite(), form.getServiceTest(), fromTime, toTime, confirmIDs);
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (key != null && !"".equals(key)) {
                items.put(options.get(ParameterEntity.SERVICE_TEST).get(key), value);
            }
        }
        return new Response<>(true, items);
    }

}
