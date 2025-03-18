package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.DashboardForm;
import com.gms.repository.impl.DashboardOpcImpl;
import com.gms.repository.impl.OpcArvRepositoryImpl;
import com.gms.service.OpcArvService;
import com.ibm.icu.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardOpcController extends DashboardController {
    
    @Autowired
    private OpcArvRepositoryImpl arvRepositoryImpl;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private DashboardOpcImpl dashboardOpcImpl;
    
    @RequestMapping(value = "/dashboard-opc/indicator.json", method = RequestMethod.POST)
    public Response<?> actionIndicator(@RequestBody DashboardForm form) {
        LoggedUser currentUser = getCurrentUser();
        form.setIndicatorName(form.getIndicatorName() == null ? "opc" : form.getIndicatorName());
        form.setYear(form.getYear() == null || form.getYear().equals("") || form.getYear().equals("-1") ? String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) : form.getYear());
        
        Map<String, Object> result = new HashMap<>();
        result.put("options", getOptions(form));
        result.put("form", form);
        
        HashMap<String, SiteEntity> siteOptions = new HashMap<>();
        for (SiteEntity item : siteService.getSiteOpc(currentUser.getSite().getProvinceID())) {
            siteOptions.put(String.valueOf(item.getID()), item);
        }
        result.put("siteOptions", siteOptions);
        return new Response<>(true, result);
    }
    
    @RequestMapping(value = "/dashboard-opc/opc-chart01.json", method = RequestMethod.POST)
    public Response<?> actionOpcChart01(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);
        
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item;
        
        while (beginCalendar.before(finishCalendar)) {
            String key = String.format("%s/%s", TextUtils.getQuarter(beginCalendar.getTime()) + 1, beginCalendar.get(Calendar.YEAR));
            Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcChart01(form.getSite(),
                    form.getGender(),
                    form.getAges(),
                    TextUtils.getFirstDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)),
                    TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)),
                    key);
            
            item = data.getOrDefault(key, null);
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }

    /**
     * Chart 02 Số đang quản lý và số đang điều trị
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/dashboard-opc/opc-chart02.json", method = RequestMethod.POST)
    public Response<?> actionChart02(@RequestBody DashboardForm form) {
        
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);
        
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance().getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        
        List<Map<String, Object>> result = new ArrayList<>();
        LinkedHashMap<String, HashMap<Integer, Integer>> quarters = new LinkedHashMap<>();
        int q = 0;
        String key;
        
        while (beginCalendar.before(finishCalendar)) {
            q = TextUtils.getQuarter(beginCalendar.getTime());
            key = String.format("%s-%s", String.valueOf(q), beginCalendar.get(Calendar.YEAR));
            if (!quarters.containsKey(key)) {
                quarters.put(key, new HashMap<Integer, Integer>());
                quarters.get(key).put(q, beginCalendar.get(Calendar.YEAR));
            }
            beginCalendar.add(java.util.Calendar.MONTH, 1);
        }

        // Tính theo từng quý
        Map<String, Object> item = new HashMap<>();
        Map<String, HashMap<String, String>> options = getOptions(form);
        
        for (Map.Entry<String, HashMap<Integer, Integer>> entry : quarters.entrySet()) {
            for (Map.Entry<Integer, Integer> e : entry.getValue().entrySet()) {
                item = dashboardOpcImpl.getOpcChart02(form.getSite(),
                        TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(e.getKey(), e.getValue()), FORMATDATE),
                        TextUtils.formatDate(TextUtils.getLastDayOfQuarter(e.getKey(), e.getValue()), FORMATDATE),
                        e.getKey(),
                        form.getGender(),
                        form.getAges()
                );
                item.put("quy", String.format("Q%s/%s", String.valueOf(e.getKey() + 1), String.valueOf(e.getValue())));
                result.add(item);
            }
        }
        return new Response<>(true, result);
    }
    
    @RequestMapping(value = "/dashboard-opc/opc-chart03.json", method = RequestMethod.POST)
    public Response<?> actionChart03(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();
        
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);
        
        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcChart03(form.getSite(), form.getGender(), form.getAges(),
                TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", fromTime),
                TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", toTime));
        
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
                item.put("tong", "0");
                item.put("khongphathien", "0");
                item.put("duoinguongphathien", "0");
                item.put("khongphathien", "0");
                item.put("duoi200", "0");
                item.put("tu200den1000", "0");
                item.put("tren1000", "0");
            }
            item.put("quy", String.format("Q%s (%s)", key, item.get("tong")));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }
    
    @RequestMapping(value = "/dashboard-opc/opc-chart04.json", method = RequestMethod.POST)
    public Response<?> actionChart04(@RequestBody DashboardForm form) {
        Calendar cal = Calendar.getInstance();

        //Lấy ngày tư vấn đầu tiên có khách hàng, nếu thời gian 5 quý vượt qua
        Date fromDate = new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime();
        OpcArvEntity arv = opcArvService.getFirst(new HashSet<>(form.getSite()));
        String fromTime = TextUtils.formatDate(fromDate, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfQuarter(new Date()), FORMATDATE);
        
        Map<String, Map<String, Object>> data = dashboardOpcImpl.getOpcChart04(form.getSite(), form.getGender(), form.getAges(),
                TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", fromTime),
                TextUtils.formatDate(FORMATDATE, "yyyy-MM-dd", toTime));
        
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
                item.put("dangkymoi", "0");
                item.put("dieutrilai", "0");
                item.put("botri", "0");
                item.put("tuvong", "0");
            }
            item.put("quy", String.format("Q%s", key));
            result.add(item);
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }
    
}
