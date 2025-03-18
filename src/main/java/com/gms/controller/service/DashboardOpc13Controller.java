package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardHtcImpl;
import com.gms.repository.impl.DashboardOpcImpl;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1.1 Luỹ tích
 *
 * @author vvthanh
 */
@RestController
public class DashboardOpc13Controller extends DashboardController {

    @Autowired
    private DashboardOpcImpl dashboardOpcImpl;

    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    @RequestMapping(value = "/dashboard-opc/d13-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {

        int year = Integer.valueOf(form.getYear());

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;

        int i = 0;
        int total = 0;
        int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        int currentQuater = year == currentYear ? TextUtils.getQuarter(new Date()) : 3;
        while (i <= currentQuater) {
            String key = String.format("%s/%s", i + 1, year);
            Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD13Chart05(form.getSite(),
                    TextUtils.getFirstDayOfQuarter(i, year),
                    TextUtils.getLastDayOfQuarter(i, year),
                    key);

            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("co", "0");
                item.put("khong", "0");
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            if (i == currentQuater) {
                total = total + Integer.parseInt(item.get("co").toString()) + Integer.parseInt(item.get("khong").toString());
            }
            i++;
        }

        Map<String, Object> data = new HashMap();
        data.put("data", result);
        data.put("total", total);
        data.put("year", form.getYear());
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/dashboard-opc/d13-chart06.json", method = RequestMethod.POST)
    public Response<?> actionChart06(@RequestBody DashboardForm form) {

        int year = Integer.valueOf(form.getYear());

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        int i = 0;
        int total = 0;
        int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
        int currentQuater = year == currentYear ? TextUtils.getQuarter(new Date()) : 3;
        while (i <= currentQuater) {
            String key = String.format("%s/%s", i + 1, year);
            Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD13Chart06(form.getSite(),
                    TextUtils.getFirstDayOfQuarter(i, year),
                    TextUtils.getLastDayOfQuarter(i, year),
                    key);

            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("nho1", "0");
                item.put("tu1den3", "0");
                item.put("lon3", "0");
            }
            item.put("quy", String.format("Q%s", key));
            if (i == currentQuater) {
                total = total + Integer.parseInt(item.get("nho1").toString()) + Integer.parseInt(item.get("tu1den3").toString()) + Integer.parseInt(item.get("lon3").toString());
            }
            result.add(item);
            i++;
        }

        Map<String, Object> data = new HashMap();
        data.put("data", result);
        data.put("total", total);
        data.put("year", form.getYear());
        return new Response<>(true, data);
    }

    @RequestMapping(value = "/dashboard-opc/d13-chart03.json", method = RequestMethod.POST)
    public Response<?> actionD12Chart03(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = String.format("%s-01-01", form.getYear());
        Date date = sdf.parse(dateInString);

        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD13Chart03(form.getSite(), form.getYear());

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            if (beginCalendar.getTime().compareTo(new Date()) > 0) {
                break;
            }
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("nam", "0");
                item.put("nu", "0");
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    @RequestMapping(value = "/dashboard-opc/d13-chart04.json", method = RequestMethod.POST)
    public Response<?> actionD12Chart04(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = String.format("%s-01-01", form.getYear());
        Date date = sdf.parse(dateInString);

        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD13Chart04(form.getSite(), form.getYear());

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            if (beginCalendar.getTime().compareTo(new Date()) > 0) {
                break;
            }
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("quy", "0");
                item.put("nl", "0");
                item.put("te", "0");
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * Số bệnh nhân đang điều trị từng năm
     *
     * @param form
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/dashboard-opc/d13-chart01.json", method = RequestMethod.POST)
    public Response<?> actionD13Chart01(@RequestBody DashboardForm form) throws ParseException {

        Map<String, Object> time = dashboardOpcImpl.getOpcD11Chart01MinYear();
        if (time == null) {
            return new Response<>(false, null);
        }

        Date minTime = TextUtils.convertDate(String.valueOf(time.get("min")), FORMATDATE_SQL);
        Date maxTime = TextUtils.convertDate(String.valueOf(time.get("max")), FORMATDATE_SQL);
        Calendar cal = Calendar.getInstance();
        if (minTime == null) {
            minTime = new Date();
        }
        if (maxTime == null) {
            maxTime = new Date();
        }
        cal.setTime(minTime);
        int minDate = cal.get(Calendar.YEAR);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        int lastYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int min = minDate; min <= lastYear; min++) {
            item = new HashMap<>();
            Integer opcChrt01 = dashboardOpcImpl.getOpcD13Chart01(form.getSite(), String.valueOf(min));
            item.put("year", opcChrt01);
            item.put("min", TextUtils.formatDate(minTime, "MM/yyyy"));
            item.put("max", TextUtils.formatDate(maxTime, "MM/yyyy"));
            item.put("yearSeries", String.format("Năm %s", min));
            result.add(item);
        }

        return new Response<>(true, result);
    }

    /**
     * Số bệnh nhân đang điều trị theo cơ sở
     *
     * @param form
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/dashboard-opc/d13-chart02.json", method = RequestMethod.POST)
    public Response<?> actionD13Chart02(@RequestBody DashboardForm form) throws ParseException {

        Set<Long> sites = new HashSet<>();
        for (Long long1 : form.getSite()) {
            sites.add(long1);
        }
        List<SiteEntity> siteEntities = siteService.findByIDs(sites);
        Map<String, SiteEntity> siteMap = new HashMap<>();

        for (SiteEntity e : siteEntities) {
            siteMap.put(e.getID().toString(), e);
        }

        Map<String, Integer> resultSet = dashboardOpcImpl.getOpcD13Chart02(form.getSite(), form.getYear());
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();

        if (resultSet == null) {
            for (Map.Entry<String, SiteEntity> entry : siteMap.entrySet()) {
                item = new HashMap<>();
                item.put("site", siteMap.get(entry.getKey()).getShortName());
                item.put("result", 0);
                result.add(item);
            }
            return new Response<>(true, result);
        }

        for (Map.Entry<String, SiteEntity> entry : siteMap.entrySet()) {

            if (!resultSet.containsKey(entry.getKey())) {
                item = new HashMap<>();
                item.put("site", siteMap.get(entry.getKey()).getShortName());
                item.put("result", 0);
                result.add(item);
            }

            for (Map.Entry<String, Integer> rs : resultSet.entrySet()) {
                if (entry.getKey().equals(rs.getKey())) {
                    item = new HashMap<>();
                    item.put("site", siteMap.get(entry.getKey()).getShortName());
                    item.put("result", rs.getValue());
                    result.add(item);
                }
            }
        }

        return new Response<>(true, result);
    }
}
