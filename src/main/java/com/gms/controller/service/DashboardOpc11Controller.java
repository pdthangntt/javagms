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
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.HtcConfirmService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DashboardOpc11Controller extends DashboardController {

    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private DashboardOpcImpl dashboardOpcImpl;
    @Autowired
    private SiteService siteService;

    private static final String FORMATDATE_SQL = "yyyy-MM-dd";

    /**
     * Số bệnh nhân lũy tích theo năm
     *
     * @param form
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/dashboard-opc/d11-chart01.json", method = RequestMethod.POST)
    public Response<?> actionD11Chart01(@RequestBody DashboardForm form) throws ParseException {

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
            Integer opcChrt01 = dashboardOpcImpl.getOpcD11Chart01(form.getSite(), String.valueOf(min));
            item.put("year", opcChrt01);
            item.put("min", TextUtils.formatDate(minTime, "MM/yyyy"));
            item.put("max", TextUtils.formatDate(maxTime, "MM/yyyy"));
            item.put("yearSeries", String.format("Năm %s", min));
            result.add(item);
        }

        return new Response<>(true, result);
    }

    /**
     * Số bệnh nhận lũy tích theo cơ sở
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-opc/d11-chart02.json", method = RequestMethod.POST)
    public Response<?> actionD11Chart02(@RequestBody DashboardForm form) throws ParseException {

        Set<Long> sites = new HashSet<>();
        for (Long long1 : form.getSite()) {
            sites.add(long1);
        }
        List<SiteEntity> siteEntities = siteService.findByIDs(sites);
        Map<String, SiteEntity> siteMap = new HashMap<>();
        
        for (SiteEntity e : siteEntities) {
            siteMap.put(e.getID().toString(), e);
        }
        
        Map<String, Integer> resultSet = dashboardOpcImpl.getOpcD11Chart02(form.getSite(), form.getYear());
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

    @RequestMapping(value = "/dashboard-opc/d11-chart03.json", method = RequestMethod.POST)
    public Response<?> actionD11Chart03(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = String.format("%s-01-01", form.getYear());
        Date date = sdf.parse(dateInString);

        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD11Chart03(form.getSite(), form.getYear());

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            if(beginCalendar.getTime().compareTo(new Date()) > 0){
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

    @RequestMapping(value = "/dashboard-opc/d11-chart04.json", method = RequestMethod.POST)
    public Response<?> actionD11Chart04(@RequestBody DashboardForm form) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = String.format("%s-01-01", form.getYear());
        Date date = sdf.parse(dateInString);

        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcD11Chart04(form.getSite(), form.getYear());

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            if(beginCalendar.getTime().compareTo(new Date()) > 0){
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
}
