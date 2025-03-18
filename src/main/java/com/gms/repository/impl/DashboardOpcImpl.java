package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Tổng quan: phần Dashboard Opc
 *
 * @author vvthanh
 */
@Repository
public class DashboardOpcImpl extends BaseRepositoryImpl {

    @Cacheable(value = "dashboard_getOpcChart01")
    public Map<String, Map<String, Object>> getOpcChart01(List<Long> site, List<String> gender, List<String> age, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("gender_id", gender);
        params.put("age", age.size() == 2 ? null : age.contains("1") ? "1" : "2");
        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_chart01.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("luytich", object[0].toString());
            item.put("moi", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }

    @Cacheable(value = "dashboard_getOpcD12Chart05")
    public Map<String, Map<String, Object>> getOpcD12Chart05(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d12_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("co", object[0].toString());
            item.put("khong", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    @Cacheable(value = "dashboard_getOpcD12Chart06")
    public Map<String, Map<String, Object>> getOpcD12Chart06(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
//        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d12_chart06.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("nho1", object[0].toString());
            item.put("tu1den3", Long.valueOf(object[1].toString()));
            item.put("lon3", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    @Cacheable(value = "dashboard_getOpcD13Chart06")
    public Map<String, Map<String, Object>> getOpcD13Chart06(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
//        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d13_chart06.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("nho1", object[0].toString());
            item.put("tu1den3", Long.valueOf(object[1].toString()));
            item.put("lon3", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    @Cacheable(value = "dashboard_getOpcD13Chart05")
    public Map<String, Map<String, Object>> getOpcD13Chart05(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d13_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("co", object[0].toString());
            item.put("khong", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    
//    @Cacheable(value = "dashboard_getOpcD14Chart05")
    public Map<String, Map<String, Object>> getOpcD14Chart05(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d14_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("co", object[0].toString());
            item.put("khong", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    @Cacheable(value = "dashboard_getOpcD15Chart05")
    public Map<String, Map<String, Object>> getOpcD15Chart05(List<Long> site, Date fromTime, Date toTime, String quy) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(fromTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toTime, FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d15_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            if (object[0] == null) {
                continue;
            }
            item = new LinkedHashMap<>();
            item.put("quy", quy);
            item.put("co", object[0].toString());
            item.put("khong", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart 02
     *
     * @param site
     * @param fromTime
     * @param toTime
     * @param quarter
     * @param gender
     * @param ages
     * @return
     */
    public Map<String, Object> getOpcChart02(List<Long> site, String fromTime, String toTime, int quarter, List<String> gender, List<String> ages) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("quarter", quarter);
        params.put("gender", gender);
        params.put("age", ages);
        params.put("from_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/opc_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }

        Map<String, Object> item = new HashMap<>();
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("manage", object[0] == null ? 0 : Long.valueOf(object[0].toString()));
            item.put("manageBhyt", object[0] == null ? 0 : Long.valueOf(object[1].toString()));
            item.put("treatment", object[0] == null ? 0 : Long.valueOf(object[2].toString()));
            item.put("treatmentBhyt", object[0] == null ? 0 : Long.valueOf(object[3].toString()));
        }

        return item;
    }

    public Map<String, Map<String, Object>> getOpcChart03(List<Long> site, List<String> genderID, List<String> age, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("gender_id", genderID);
            params.put("age", age);
            result = query("dashboard/opc_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("tong", Long.valueOf(object[1].toString()));
                item.put("khongphathien", Long.valueOf(object[2].toString()));
                item.put("duoinguongphathien", Long.valueOf(object[3].toString()));
                item.put("duoi200", Long.valueOf(object[4].toString()));
                item.put("tu200den1000", Long.valueOf(object[5].toString()));
                item.put("tren1000", Long.valueOf(object[6].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }

    public Map<String, Map<String, Object>> getOpcChart04(List<Long> site, List<String> genderID, List<String> age, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("gender", genderID);
            params.put("age", age);
            result = query("dashboard/opc_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("dangkymoi", Long.valueOf(object[1].toString()));
                item.put("dieutrilai", Long.valueOf(object[2].toString()));
                item.put("botri", Long.valueOf(object[3].toString()));
                item.put("tuvong", Long.valueOf(object[4].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    /**
     * Lấy năm đầu tiên có dữ liệu
     * 
     * @return
     * @throws ParseException 
     */
    public Map<String, Object> getOpcD11Chart01MinYear() throws ParseException {
        List<Object> result = query("dashboard/opc_d11_chart01.sql", null).getResultList();
        if (result == null || result.isEmpty()) {
            return null;
        }
        Map<String, Object> time = new HashMap<>();
        time.put("max", result.get(0));
        time.put("min", result.get(1));
        return time;
    }
    
    /**
     * Lấy thông tin lúy tích theo từng năm
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Integer getOpcD11Chart01(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        Object singleResult = query("dashboard/opc_d11_chart01_1.sql", params).getSingleResult();
        try {
            return Integer.parseInt(String.valueOf(singleResult));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Dếm số bênh nhân theo cơ sở
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Map<String, Integer> getOpcD11Chart02(List<Long> site, String year) throws ParseException {
     
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        
        List<Object[]> result = query("dashboard/opc_d11_chart02.sql", params).getResultList();
        Map<String, Integer> item = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        for (Object[] obj : result) {
            item.put(String.valueOf(obj[0]), obj[1] != null ? Integer.parseInt(String.valueOf(obj[1])) : null);
        }
        
        return item;
    }
    
    
    public Map<String, Map<String, Object>> getOpcD11Chart03(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
            params.put("site_id", site);
            result = query("dashboard/opc_d11_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nam", Long.valueOf(object[1].toString()));
                item.put("nu", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD11Chart04(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d11_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("te", Long.valueOf(object[1].toString()));
                item.put("nl", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD12Chart03(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
            params.put("site_id", site);
            result = query("dashboard/opc_d12_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nam", Long.valueOf(object[1].toString()));
                item.put("nu", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD12Chart04(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d12_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nl", Long.valueOf(object[2].toString()));
                item.put("te", Long.valueOf(object[1].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
//    @Cacheable(value = "dashboard_getOpcD13Chart03")
    public Map<String, Map<String, Object>> getOpcD13Chart03(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            result = query("dashboard/opc_d13_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nam", Long.valueOf(object[1].toString()));
                item.put("nu", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
//    @Cacheable(value = "dashboard_getOpcD13Chart04")
    public Map<String, Map<String, Object>> getOpcD13Chart04(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("to_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d13_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nl", Long.valueOf(object[2].toString()));
                item.put("te", Long.valueOf(object[1].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD14Chart03(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d14_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nam", Long.valueOf(object[1].toString()));
                item.put("nu", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD14Chart04(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d14_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nl", Long.valueOf(object[2].toString()));
                item.put("te", Long.valueOf(object[1].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD15Chart03(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            result = query("dashboard/opc_d15_chart03.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nam", Long.valueOf(object[1].toString()));
                item.put("nu", Long.valueOf(object[2].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD15Chart04(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        List<Object[]> result = null;
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
//            params.put("to_date", TextUtils.formatDate(TextUtils.getLastDayOfQuarter(TextUtils.getQuarter(beginCalendar.getTime()), beginCalendar.get(Calendar.YEAR)), FORMATDATE));
            params.put("site_id", site);
            params.put("year", year);
            result = query("dashboard/opc_d15_chart04.sql", params).getResultList();

            if (result == null) {
                beginCalendar.add(Calendar.MONTH, 3);
                continue;
            }

            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("quy", object[0].toString());
                item.put("nl", Long.valueOf(object[2].toString()));
                item.put("te", Long.valueOf(object[1].toString()));
                data.put(item.get("quy").toString(), item);
            }
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return data;
    }
    
    /**
     * Số bệnh nhân đang quản lý theo năm
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Integer getOpcD12Chart01(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        Object singleResult = query("dashboard/opc_d12_chart01.sql", params).getSingleResult();
        try {
            return Integer.parseInt(String.valueOf(singleResult));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Số bệnh nhân theo cơ sở
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Map<String, Integer> getOpcD12Chart02(List<Long> site, String year) throws ParseException {
     
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        
        List<Object[]> result = query("dashboard/opc_d12_chart02.sql", params).getResultList();
        Map<String, Integer> item = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        for (Object[] obj : result) {
            item.put(String.valueOf(obj[0]), obj[1] != null ? Integer.parseInt(String.valueOf(obj[1])) : null);
        }
        
        return item;
    }
    
    /**
     * Số BN đang điều trị theo năm
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Integer getOpcD13Chart01(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        Object singleResult = query("dashboard/opc_d13_chart01.sql", params).getSingleResult();
        try {
            return Integer.parseInt(String.valueOf(singleResult));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Số đang được điều trị theo cơ sở
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Map<String, Integer> getOpcD13Chart02(List<Long> site, String year) throws ParseException {
     
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        
        List<Object[]> result = query("dashboard/opc_d13_chart02.sql", params).getResultList();
        Map<String, Integer> item = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        for (Object[] obj : result) {
            item.put(String.valueOf(obj[0]), obj[1] != null ? Integer.parseInt(String.valueOf(obj[1])) : null);
        }
        
        return item;
    }
    
    /**
     * Sô BN đăng ký mới theo năm
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Integer getOpcD14Chart01(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        Object singleResult = query("dashboard/opc_d14_chart01.sql", params).getSingleResult();
        try {
            return Integer.parseInt(String.valueOf(singleResult));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Số bệnh nhân đăng ký mới theo cơ sở
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Map<String, Integer> getOpcD14Chart02(List<Long> site, String year) throws ParseException {
     
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        
        List<Object[]> result = query("dashboard/opc_d14_chart02.sql", params).getResultList();
        Map<String, Integer> item = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        for (Object[] obj : result) {
            item.put(String.valueOf(obj[0]), obj[1] != null ? Integer.parseInt(String.valueOf(obj[1])) : null);
        }
        
        return item;
    }
    
    
    /**
     * Số BN XN TLVR theo năm
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Integer getOpcD15Chart01(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        Object singleResult = query("dashboard/opc_d15_chart01.sql", params).getSingleResult();
        try {
            return Integer.parseInt(String.valueOf(singleResult));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Soos BN XN TLVR theo cơ sở
     * 
     * @param site
     * @param year
     * @return
     * @throws ParseException 
     */
    public Map<String, Integer> getOpcD15Chart02(List<Long> site, String year) throws ParseException {
     
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String strLastDate = String.format("%s-12-31", year);
        Date lastDate = sdf.parse(strLastDate);
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("lastDate", TextUtils.formatDate(lastDate, FORMATDATE));
        
        List<Object[]> result = query("dashboard/opc_d15_chart02.sql", params).getResultList();
        Map<String, Integer> item = new HashMap<>();
        
        if (result == null || result.isEmpty()) {
            return null;
        }
        for (Object[] obj : result) {
            item.put(String.valueOf(obj[0]), obj[1] != null ? Integer.parseInt(String.valueOf(obj[1])) : null);
        }
        
        return item;
    }
    
    public Map<String, Map<String, Object>> getD15Chart03Sum(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
        params.put("to_date", TextUtils.formatDate(finishCalendar.getTime(), FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d15_chart03_2.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getD15Chart04Sum(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
        params.put("to_date", TextUtils.formatDate(finishCalendar.getTime(), FORMATDATE));
        params.put("year", year);
        List<Object[]> result = query("dashboard/opc_d15_chart04_2.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("te", Long.valueOf(object[1].toString()));
            item.put("nl", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD14Chart03Sum(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
        params.put("to_date", TextUtils.formatDate(finishCalendar.getTime(), FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d14_chart03_2.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
    
    public Map<String, Map<String, Object>> getOpcD14Chart04Sum(List<Long> site, String year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        String dateInString = String.format("%s-01-01", year);
        Date date = sdf.parse(dateInString);
        HashMap<String, Object> params = new HashMap<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.getFirstDayOfYear(date));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.getLastDayOfYear(date));
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(beginCalendar.getTime(), FORMATDATE));
        params.put("to_date", TextUtils.formatDate(finishCalendar.getTime(), FORMATDATE));
        List<Object[]> result = query("dashboard/opc_d14_chart04_2.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("te", Long.valueOf(object[1].toString()));
            item.put("nl", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }
}
