package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardHtcImpl;
import com.gms.service.HtcVisitService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardHtcController extends DashboardController {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private DashboardHtcImpl dashboardHtcImpl;
    @Autowired
    private SiteService siteService;
    @Autowired
    private HtcVisitService visitService;

    @RequestMapping(value = "/dashboard-htc/indicator.json", method = RequestMethod.POST)
    public Response<?> actionIndicator(@RequestBody DashboardForm form) {
        LoggedUser currentUser = getCurrentUser();

        form.setIndicatorName(form.getIndicatorName() == null ? "htc" : form.getIndicatorName());
        form.setYear(form.getYear() == null ? String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) : form.getYear());
        form.setProvinceID(form.getProvinceID() == null ? currentUser.getSite().getProvinceID() : form.getProvinceID());

        Map<String, Object> result = new HashMap<>();
        result.put("options", getOptions(form));
        result.put("form", form);

        HashMap<String, SiteEntity> siteOptions = new HashMap<>();
        for (SiteEntity item : siteService.getSiteHtc(currentUser.getSite().getProvinceID())) {
            siteOptions.put(String.valueOf(item.getID()), item);
        }
        for (SiteEntity item : siteService.getSiteConfirm(currentUser.getSite().getProvinceID())) {
            siteOptions.put(String.valueOf(item.getID()), item);
        }
        result.put("siteOptions", siteOptions);
        return new Response<>(true, result);
    }

    /**
     * D2_Tổng quan TV&XN Chart 01
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/htc-chart01.json", method = RequestMethod.POST)
    public Response<?> actionHtcChart01(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        HtcVisitEntity visit = visitService.getFirst(new HashSet<>(form.getSite()));
        if (visit != null && visit.getAdvisoryeTime() != null && visit.getAdvisoryeTime().getTime() > fromDate.getTime()) {
            fromDate = visit.getAdvisoryeTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart01(form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));

        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;

        while (beginCalendar.before(finishCalendar)) {
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("soxn", Long.parseLong("0"));
                item.put("nhanketqua", Long.parseLong("0"));
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * D2_Tổng quan TV&XN Chart 03
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/htc-chart03.json", method = RequestMethod.POST)
    public Response<?> actionHtcChart03(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        HtcVisitEntity visit = visitService.getFirst(new HashSet<>(form.getSite()));
        if (visit != null && visit.getAdvisoryeTime() != null && visit.getAdvisoryeTime().getTime() > fromDate.getTime()) {
            fromDate = visit.getAdvisoryeTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart03(form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("duongtinh", "0");
                item.put("chuyengui", "0");
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-htc/htc-chart02.json", method = RequestMethod.POST)
    public Response<?> actionHtcChart02(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        HtcVisitEntity visit = visitService.getFirst(new HashSet<>(form.getSite()));
        if (visit != null && visit.getAdvisoryeTime() != null && visit.getAdvisoryeTime().getTime() > fromDate.getTime()) {
            fromDate = visit.getAdvisoryeTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart02(form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("xetnghiemduongtinh", Long.parseLong("0"));
                item.put("duongtinhnhanketqua", Long.parseLong("0"));
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * Phân tích ca dương tính theo thời gian báo cáo
     *
     * @auth TrangBN
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/htc-chart04.json", method = RequestMethod.POST)
    public Response<?> actionHtcChart04(@RequestBody DashboardForm form) {

        Calendar cal = Calendar.getInstance();
        
        // Lấy ID cơ sở khẳng định của tỉnh
        List<SiteEntity> siteConfirms = siteService.getSiteConfirm(getCurrentUser().getSiteProvince().getID());
        List<Long> confirmIDs = new ArrayList<>();
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            confirmIDs.add(-1L);
        } else {
            confirmIDs = new ArrayList<>(CollectionUtils.collect(siteConfirms, TransformerUtils.invokerTransformer("getID")));
        }

        // Lấy ngày bắt đầu và kết thúc của 5 quý
        Date fromDateQ1 = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        Date toDateQ1 = getLastDayOfQuarter(cal, fromDateQ1);

        Date fromDateQ2 = addDays(cal, toDateQ1, 1);
        Date toDateQ2 = getLastDayOfQuarter(cal, fromDateQ2);

        Date fromDateQ3 = addDays(cal, toDateQ2, 1);
        Date toDateQ3 = getLastDayOfQuarter(cal, fromDateQ3);

        Date fromDateQ4 = addDays(cal, toDateQ3, 1);
        Date toDateQ4 = getLastDayOfQuarter(cal, fromDateQ4);

        Date fromDateQ5 = addDays(cal, toDateQ4, 1);
        Date toDateQ5 = getLastDayOfQuarter(cal, fromDateQ5);

        HashMap<String, String> timeParam = new HashMap<>();
        for (String quarter : form.getQuarter()) {
            if (getStringQuarter(cal, toDateQ1).equals(quarter)) {
                timeParam.put("fromDateQ1", TextUtils.formatDate(fromDateQ1, FORMATDATE));
                timeParam.put("toDateQ1", TextUtils.formatDate(toDateQ1, FORMATDATE));
            }
            if (getStringQuarter(cal, toDateQ2).equals(quarter)) {
                timeParam.put("fromDateQ2", TextUtils.formatDate(fromDateQ2, FORMATDATE));
                timeParam.put("toDateQ2", TextUtils.formatDate(toDateQ2, FORMATDATE));
            }
            if (getStringQuarter(cal, toDateQ3).equals(quarter)) {
                timeParam.put("fromDateQ3", TextUtils.formatDate(fromDateQ3, FORMATDATE));
                timeParam.put("toDateQ3", TextUtils.formatDate(toDateQ3, FORMATDATE));
            }
            if (getStringQuarter(cal, toDateQ4).equals(quarter)) {
                timeParam.put("fromDateQ4", TextUtils.formatDate(fromDateQ4, FORMATDATE));
                timeParam.put("toDateQ4", TextUtils.formatDate(toDateQ4, FORMATDATE));
            }
            if (getStringQuarter(cal, toDateQ5).equals(quarter)) {
                timeParam.put("fromDateQ5", TextUtils.formatDate(fromDateQ5, FORMATDATE));
                timeParam.put("toDateQ5", TextUtils.formatDate(toDateQ5, FORMATDATE));
            }
        }
        
        timeParam.put("firstDayYear", TextUtils.formatDate(fromDateQ1, FORMATDATE));
        timeParam.put("lastDayYear", TextUtils.formatDate(toDateQ5, FORMATDATE));

        List<Map<String, Object>> data = dashboardHtcImpl.getHtcChart04(form.getSite(), form.getServiceTest(), timeParam, confirmIDs);

        if (data == null) {
            return new Response<>(false, null);
        }

        return new Response<>(true, data);
    }

    /**
     * D2_Tổng quan TV&XN Chart 01
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/htc-chart01d21.json", method = RequestMethod.POST)
    public Response<?> actionHtcChart01d21(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày của năm
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart01d21(form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = TextUtils.formatDate(beginCalendar.getTime(), "M");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("q", "0");
                item.put("xn", Long.parseLong("0"));
                item.put("nhanketqua", Long.parseLong("0"));
            }
            item.put("q", String.format("Tháng %s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return new Response<>(true, result);
    }

    /**
     * Lấy ngày cuối cùng của quý
     *
     * @auth TrangBN
     * @param cal
     * @param date
     * @return
     */
    private static Date getLastDayOfQuarter(Calendar cal, Date date) {
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * Cộng thêm ngày
     *
     * @auth TrangBN
     * @param cal
     * @param date
     * @param days
     * @return
     */
    private static Date addDays(Calendar cal, Date date, int days) {
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * Get quarter
     *
     * @param cal
     * @param date
     * @return
     */
    private static String getStringQuarter(Calendar cal, Date date) {
        cal.setTime(date);
        return String.format("%s/%s", String.valueOf(TextUtils.getQuarter(cal.getTime()) + 1), String.valueOf(cal.get(Calendar.YEAR)));
    }

}
