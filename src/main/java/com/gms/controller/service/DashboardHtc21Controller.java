package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardHtcImpl;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
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

@RestController
public class DashboardHtc21Controller extends DashboardController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private DashboardHtcImpl dashboardHtcImpl;
    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private HtcConfirmService confirmService;

    /**
     * pdThang
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d21-chart01.json", method = RequestMethod.POST)
    public Response<?> actionChart01(@RequestBody DashboardForm form) {

        Date fromDate = new Date();
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        HtcVisitEntity visit = visitService.getFirst(new HashSet<>(form.getSite()));
        if (visit != null && visit.getAdvisoryeTime() != null) {
            fromDate = visit.getAdvisoryeTime();
        }
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(new Date()), FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcChart01d21(form.getSite(), form.getServiceTest(), fromTime, toTime);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        LinkedList<Map<String, Object>> result = new LinkedList<>();
        
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String key = TextUtils.formatDate(beginCalendar.getTime(), "M/yyyy");
            item = data.getOrDefault(key, null);
            if (item == null) {
                item = new HashMap<>();
                item.put("thang", "0");
                item.put("soxn", Long.parseLong("0"));
                item.put("nhanketqua", Long.parseLong("0"));
            }
            item.put("thang", String.format("%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        
        return new Response<>(true, result);

    }

    /**
     *
     * @author DSNAnh
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d21-chart02.json", method = RequestMethod.POST)
    public Response<?> actionD21Chart02(@RequestBody DashboardForm form) {
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);
        Date fromDate = TextUtils.getFirstDayOfYear(cal.getTime());
        Date toDate = TextUtils.getLastDayOfYear(cal.getTime());
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(toDate, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD21Chart02(form.getSite(), form.getServiceTest(), fromTime, toTime);
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
                item.put("nhanketqua", Long.valueOf("0"));
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
     * Số XN & Nhận kết quả theo dương tính
     *
     * @auth TrangBN
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d21-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date lastDayQ4 = TextUtils.getLastDayOfYear(cal.getTime());
        Date firstDayQ1 = TextUtils.getFirstDayOfYear(cal.getTime());

        String fromTime = TextUtils.formatDate(firstDayQ1, FORMATDATE);
        String toTime = TextUtils.formatDate(lastDayQ4, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD21Chart03(form.getSite(), form.getServiceTest(), fromTime, toTime);

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
    @RequestMapping(value = "/dashboard-htc/d21-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        if (form.getSite().isEmpty()) {
            form.setSite(new ArrayList<Long>());
            form.getSite().add(Long.valueOf("-1"));
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date fromDate = TextUtils.getFirstDayOfYear(cal.getTime());
        Date toDate = TextUtils.getLastDayOfYear(cal.getTime());
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(toDate, FORMATDATE);

        Map<String, Map<String, Object>> data = dashboardHtcImpl.getHtcD21Chart04(form.getSite(), form.getServiceTest(), fromTime, toTime);

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
     * @pdThang @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d21-chart06.json", method = RequestMethod.POST)
    public Response<?> actionChart06(@RequestBody DashboardForm form) {
        Map<String, HashMap<String, String>> options = getOptions(form);
        //Lấy ngày của năm
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date d = cal.getTime();
        Date fromDate = TextUtils.getFirstDayOfYear(d);

        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(d), FORMATDATE);
        Map<String, Long> items = new HashMap<>();

        Map<String, Long> data = dashboardHtcImpl.getHtcChart06d21(form.getSite(), form.getServiceTest(), fromTime, toTime);
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (key != null && !"".equals(key)) {
                items.put(options.get(ParameterEntity.SERVICE_TEST).get(key), value);
            }

        }

        return new Response<>(true, items);
    }

    /**
     *
     *
     * @pdThang @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-htc/d21-chart05.json", method = RequestMethod.POST)
    public Response<?> actionChart05(@RequestBody DashboardForm form) {

//Lấy ngày của năm
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, StringUtils.isNotEmpty(form.getYear()) ? Integer.parseInt(form.getYear()) : 0);

        Date d = cal.getTime();
        Date fromDate = TextUtils.getFirstDayOfYear(d);

        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfMonth(d), FORMATDATE);
        Map<String, Long> data = dashboardHtcImpl.getHtcChart05d21(form.getSite(), form.getServiceTest(), fromTime, toTime);
        return new Response<>(true, getTestObjects(data));
    }

}
