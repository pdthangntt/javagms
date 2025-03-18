package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
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
 * Tổng quan: phần Dashboard Pac
 *
 * @author vvthanh
 */
@Repository
public class DashboardPacImpl extends BaseRepositoryImpl {

    /**
     * @param year
     * @param district
     * @auth vvthanh
     * @param province
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacChartGeo")
    public List<Map<String, String>> getPacChartGeo(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", year);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_chartgeo.sql", params).getResultList();

        if (result == null) {
            return null;
        }

        List<Map<String, String>> data = new ArrayList<>();

        Map<String, String> item;
        for (Object[] object : result) {
            item = new HashMap<>();
            item.put("id", object[0].toString());
            item.put("name", object[1].toString());
            item.put("quantity", String.valueOf(object[2]));
            data.add(item);
        }
        return data;
    }

    /**
     * Kết quả thực hiện mục tiêu 90-90-90
     *
     * @param province
     * @param district
     * @param year
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @param estimate
     * @return
     */
//    @Cacheable(value = "dashboard_getPacChart02")
    public List<Map<String, Object>> getPacChart02(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup, Integer estimate, Integer totalViral) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
//        params.put("district", district);
        params.put("year", year);
//        params.put("gender", gender);
//        params.put("modesOfTransmision", modesOfTransmision);
//        params.put("testObjectGroup", testObjectGroup);
        params.put("est", estimate);
        List<Object[]> result = query("dashboard/dashboard_pac_chart02.sql", params).getResultList();

        if (result == null) {
            return null;
        }

        Object[] resultArray = result.get(0);

        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("phanloai", "Số ước tính nhiễm HIV");
        Long soca = Long.valueOf(resultArray[0] != null ? resultArray[0].toString() : "0");
        item.put("soca", soca);
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số người biết tình trạng nhiễm HIV");
        soca = Long.valueOf(resultArray[1] != null ? resultArray[1].toString() : "0");
        item.put("soca", soca);
        Double col1 = estimate * 0.9;
        item.put("them", String.format("%.0f", col1 - soca <= 0 ? 0 : col1 - soca));
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số bệnh nhân đang điều trị ARV");
        soca = Long.valueOf(resultArray[2] != null ? resultArray[2].toString() : "0");
        item.put("soca", soca);
        col1 = col1 * 0.9;
        item.put("them", String.format("%.0f", col1 - soca <= 0 ? 0 : col1 - soca));
        listResult.add(item);

        item = new HashMap<>();
        soca = Long.valueOf(resultArray[3] != null ? resultArray[3].toString() : "0");
        Double percent1000 = Double.valueOf(0);
        if (totalViral > 0) {
            percent1000 = Math.round((Double.valueOf(soca) / Double.valueOf(totalViral) * 100) * 100.0) / 100.0;
        }
        item.put("phanloai", "Số khống chế tải lượng virus < 1000 \n (" + percent1000 + "%)");
        item.put("soca", soca);
        listResult.add(item);

        item = new HashMap<>();
        soca = Long.valueOf(resultArray[4] != null ? resultArray[4].toString() : "0");
        Double percent200 = Double.valueOf(0);
        if (totalViral > 0) {
            percent200 = Math.round((Double.valueOf(soca) / Double.valueOf(totalViral) * 100) * 100.0) / 100.0;
        }
        item.put("phanloai", "Số không phát hiện được virus < 200 \n (" + percent200 + "%)");
        item.put("soca", soca);
        listResult.add(item);

        return listResult;
    }

    /**
     * @author DSNAnh Get data d1 chart03
     * @param province
     * @param district
     * @param year
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacChart03")
    public Map<String, Map<String, Object>> getPacChart03(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", year);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Map<String, Map<String, Object>> data = new LinkedHashMap();
        for (int i = 9; i >= 0; i--) {
            int presentYear = year - i;
            params.put("year", presentYear);
            List<Object[]> result = query("dashboard/dashboard_pac_chart03.sql", params).getResultList();
            for (Object[] object : result) {
                Map<String, Object> item = new LinkedHashMap<>();
                if (presentYear == calendar.get(Calendar.YEAR)) {
                    item.put("nam", String.format("0%s/%s", String.valueOf(calendar.get(Calendar.MONTH) + 1), String.valueOf(presentYear)));
                } else {
                    item.put("nam", String.valueOf(presentYear));
                }
                item.put("tichluy", Long.valueOf(object[1] == null ? "0" : object[1].toString()));
                item.put("consong", Long.valueOf(object[2] == null ? "0" : object[2].toString()));
                item.put("phathien", Long.valueOf(object[3] == null ? "0" : object[3].toString()));
                item.put("tuvong", Long.valueOf(object[4] == null ? "0" : object[4].toString()));
                data.put(item.get("nam").toString(), item);
            }

        }
        return data;
    }

    @Cacheable(value = "dashboard_getPac11Chart0")
    public Map<String, Map<String, Object>> getPac11Chart01(String province, List<String> district, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        String formatToDate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", formatToDate);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d11_chart01.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("nam", object[0].toString());
            item.put("luytich", Long.valueOf(object[1].toString()));

            data.put(item.get("nam").toString(), item);
        }
        return data;
    }

    @Cacheable(value = "dashboard_getPac12Chart01")
    public Map<String, Map<String, Object>> getPac12Chart01(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", year);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d12_chart01.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("nam", object[0].toString());
            item.put("luytich", Long.valueOf(object[1].toString()));

            data.put(item.get("nam").toString(), item);
        }
        return data;
    }

    @Cacheable(value = "dashboard_getPac13Chart01")
    public Map<String, Map<String, Object>> getPac13Chart01(String province, List<String> district, Date fromYear, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        String pramtodate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("gender", gender);

        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        String formatToDate = TextUtils.formatDate(toYear, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfYear(toYear), FORMATDATE);
//        int year = Integer.parseInt(TextUtils.formatDate(toYear, "yyyy"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(fromYear);
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String year = TextUtils.formatDate(beginCalendar.getTime(), "y");
            params.put("year", year);

            String pramDate = TextUtils.formatDate(TextUtils.getLastDayOfMonth(12, Integer.valueOf(year)), "yyyy-MM-dd");
            params.put("pramtodate", TextUtils.formatDate(toYear, "yyyy").equals(year) ? pramtodate : pramDate);

            List<Object[]> result = query("dashboard/dashboard_pac_d13_chart01.sql", params).getResultList();
            if (result == null) {
                item = new LinkedHashMap<>();
                item.put("nam", year);
                item.put("luytich", 0);
                data.put(String.valueOf(year), item);
                continue;
            }
            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("nam", object[0].toString());
                item.put("luytich", Long.valueOf(object[1].toString()));
                data.put(String.valueOf(year), item);
            }
            beginCalendar.add(Calendar.YEAR, 1);
        }

        return data;
    }

    @Cacheable(value = "dashboard_getPac14Chart01")
    public Map<String, Map<String, Object>> getPac14Chart01(String province, List<String> district, Date fromYear, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        String pramtodate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("gender", gender);
//        params.put("pramtodate", pramtodate);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        String formatToDate = TextUtils.formatDate(toYear, FORMATDATE);
        String toTime = TextUtils.formatDate(TextUtils.getLastDayOfYear(toYear), FORMATDATE);
//        int year = Integer.parseInt(TextUtils.formatDate(toYear, "yyyy"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(fromYear);
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        Map<String, Object> item;
        while (beginCalendar.before(finishCalendar)) {
            String year = TextUtils.formatDate(beginCalendar.getTime(), "y");
            params.put("year", year);

            String pramDate = TextUtils.formatDate(TextUtils.getLastDayOfMonth(12, Integer.valueOf(year)), "yyyy-MM-dd");
            params.put("pramtodate", TextUtils.formatDate(toYear, "yyyy").equals(year) ? pramtodate : pramDate);

            List<Object[]> result = query("dashboard/dashboard_pac_d14_chart01.sql", params).getResultList();
            if (result == null) {
                item = new LinkedHashMap<>();
                item.put("nam", year);
                item.put("luytich", 0);
                data.put(String.valueOf(year), item);
                continue;
            }
            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("nam", object[0].toString());
                item.put("luytich", Long.valueOf(object[1].toString()));
                data.put(String.valueOf(year), item);
            }
            beginCalendar.add(Calendar.YEAR, 1);
        }

        return data;
    }

    @Cacheable(value = "dashboard_getPac11Chart04")
    public Map<String, Long> getPac11Chart04(String province, List<String> district, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        String formatToDate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", formatToDate);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d11_chart04.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }
            if (object[0] == null) {
                data.put("9999", Long.valueOf(object[1].toString()));
            }

        }

        return data;
    }

    @Cacheable(value = "dashboard_getPac12Chart04")
    public Map<String, Long> getPac12Chart04(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", year);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d12_chart04.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }
            if (object[0] == null) {
                data.put("9999", Long.valueOf(object[1].toString()));
            }

        }

        return data;
    }

    @Cacheable(value = "dashboard_getPac13Chart04")
    public Map<String, Long> getPac13Chart04(String province, List<String> district, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        String formatToDate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", formatToDate);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d13_chart04.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }
            if (object[0] == null) {
                data.put("999", Long.valueOf(object[1].toString()));
            }

        }

        return data;
    }

    public Map<String, Long> getPac14Chart04(String province, List<String> district, Date toYear, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        String formatToDate = TextUtils.formatDate(toYear, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("year", formatToDate);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d14_chart04.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }
            if (object[0] == null) {
                data.put("999", Long.valueOf(object[1].toString()));
            }

        }

        return data;
    }

    /**
     * @author DSNAnh Get data d1 chart04
     * @param province
     * @param district
     * @param year
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacChart04")
    public Map<String, Map<String, Object>> getPacChart04(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Map<String, Map<String, Object>> data = new LinkedHashMap();
        for (int i = 9; i >= 0; i--) {
            int presentYear = year - i;
            params.put("year", presentYear);
            List<Object[]> result = query("dashboard/dashboard_pac_chart04.sql", params).getResultList();
            for (Object[] object : result) {
                Map<String, Object> item = new LinkedHashMap<>();
                if (presentYear == calendar.get(Calendar.YEAR)) {
                    item.put("nam", String.format("0%s/%s", String.valueOf(calendar.get(Calendar.MONTH) + 1), String.valueOf(presentYear)));
                } else {
                    item.put("nam", String.valueOf(presentYear));
                }
                item.put("dangdieutri", Long.valueOf(object[1] == null ? "0" : object[1].toString()));
                item.put("moidieutri", Long.valueOf(object[2] == null ? "0" : object[2].toString()));
                data.put(item.get("nam").toString(), item);
            }

        }
        return data;
    }

    /**
     * @author DSNAnh Get data d11 chart02
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD11Chart02")
    public Map<String, Map<String, Object>> getPacD11Chart02(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d11_chart02.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("tenhuyen", object[1].toString());
            item.put("sotichluy", Long.valueOf(object[2].toString()));
            data.put(object[0].toString(), item);
        }
        return data;
    }

    /**
     * @author DSNAnh Get data d11 chart05
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD11Chart05")
    public Map<String, Long> getPacD11Chart05(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d11_chart05.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }

        return data;
    }

    /**
     * @auth vvthanh
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD11Chart03")
    public Map<String, Map<String, Object>> getPacD11Chart03(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d11_chart03.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        for (Object[] object : result) {
            String genderKey = object[0].toString();
            String label = "";
            if (genderKey.equals(GenderEnum.MALE.getKey())) {
                label = GenderEnum.MALE.getLabel();
            } else if (genderKey.equals(GenderEnum.FEMALE.getKey())) {
                label = GenderEnum.FEMALE.getLabel();
            } else {
                label = GenderEnum.NONE.getLabel();
            }
            if (data.get(genderKey) == null) {
                data.put(genderKey, new HashMap<String, Object>());
                data.get(genderKey).put("gender", label);
            }
            data.get(genderKey).put(object[1].toString(), Long.valueOf(object[2].toString()));
        }

        return data;
    }

    @Cacheable(value = "dashboard_getPacD13Chart02")
    public Map<String, Map<String, Object>> getPacD13Chart02(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d13_chart02.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("tenhuyen", object[1].toString());
            item.put("sotichluy", Long.valueOf(object[2].toString()));
            data.put(object[0].toString(), item);
        }
        return data;
    }

    /**
     * @author DSNAnh Get data d13 chart05
     * @param province
     * @param district
     * @param fromTime
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD13Chart05")
    public Map<String, Long> getPacD13Chart05(String province, List<String> district, Date fromTime, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d13_chart05.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }

        return data;
    }

    /**
     * @author DSNAnh Get data d14 chart02
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD14Chart02")
    public Map<String, Map<String, Object>> getPacD14Chart02(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d14_chart02.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new LinkedHashMap();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("tenhuyen", object[1].toString());
            item.put("sotichluy", Long.valueOf(object[2].toString()));
            data.put(object[0].toString(), item);
        }
        return data;
    }

    /**
     * @author DSNAnh Get data d14 chart05
     * @param province
     * @param district
     * @param fromTime
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD14Chart05")
    public Map<String, Long> getPacD14Chart05(String province, List<String> district, Date fromTime, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
//        params.put("fromTime", fromTime);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d14_chart05.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }

        return data;
    }

    /**
     * @auth bntrang
     *
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD13Chart03")
    public Map<String, Map<String, Object>> getPacD13Chart03(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);

        List<Object[]> result = query("dashboard/dashboard_pac_d13_chart03.sql", params).getResultList();

        if (result == null) {
            return null;
        }

        Map<String, Map<String, Object>> data = new HashMap();
        for (Object[] object : result) {
            String genderKey = object[0].toString();
            String label = "";
            if (genderKey.equals(GenderEnum.MALE.getKey())) {
                label = GenderEnum.MALE.getLabel();
            } else if (genderKey.equals(GenderEnum.FEMALE.getKey())) {
                label = GenderEnum.FEMALE.getLabel();
            } else {
                label = GenderEnum.NONE.getLabel();
            }
            if (data.get(genderKey) == null) {
                data.put(genderKey, new HashMap<String, Object>());
                data.get(genderKey).put("gender", label);
            }
            data.get(genderKey).put(object[1].toString(), Long.valueOf(object[2].toString()));
        }
        return data;
    }

    /**
     * @auth bntrang
     *
     * @param province
     * @param district
     * @param toTime
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD14Chart03")
    public Map<String, Map<String, Object>> getPacD14Chart03(String province, List<String> district, Date toTime, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toTime", TextUtils.formatDate(toTime, "yyyy-MM-dd"));
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        List<Object[]> result = query("dashboard/dashboard_pac_d14_chart03.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        for (Object[] object : result) {
            String genderKey = object[0].toString();
            String label = "";
            if (genderKey.equals(GenderEnum.MALE.getKey())) {
                label = GenderEnum.MALE.getLabel();
            } else if (genderKey.equals(GenderEnum.FEMALE.getKey())) {
                label = GenderEnum.FEMALE.getLabel();
            } else {
                label = GenderEnum.NONE.getLabel();
            }
            if (data.get(genderKey) == null) {
                data.put(genderKey, new HashMap<String, Object>());
                data.get(genderKey).put("gender", label);
            }
            data.get(genderKey).put(object[1].toString(), Long.valueOf(object[2].toString()));
        }
        return data;
    }

    /**
     * Số người nhiễm HIV phát hiện mới theo nhóm tuổi
     *
     * @auth TrangBN
     * @param toYear
     * @param province
     * @param district
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD12Chart03")
    public Map<String, Map<String, Object>> getPacD12Chart03(String province, List<String> district, Integer toYear,
            List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("toYear", toYear);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);

        List<Object[]> result = query("dashboard/dashboard_pac_d12_chart03.sql", params).getResultList();

        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        for (Object[] object : result) {
            String genderKey = object[0].toString();
            String label = "";
            if (genderKey.equals(GenderEnum.MALE.getKey())) {
                label = GenderEnum.MALE.getLabel();
            } else if (genderKey.equals(GenderEnum.FEMALE.getKey())) {
                label = GenderEnum.FEMALE.getLabel();
            } else {
                label = GenderEnum.NONE.getLabel();
            }
            if (data.get(genderKey) == null) {
                data.put(genderKey, new HashMap<String, Object>());
                data.get(genderKey).put("gender", label);
            }
            data.get(genderKey).put(object[1].toString(), Long.valueOf(object[2].toString()));
        }
        return data;
    }

    /**
     * @author DSNAnh Get data d12 chart02
     * @param province
     * @param district
     * @param year
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD12Chart02")
    public Map<String, Map<String, Object>> getPacD12Chart02(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Map<String, Map<String, Object>> data = new LinkedHashMap();
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if (year == -1) {
            for (int i = 9; i >= 0; i--) {
                int presentYear = now.get(Calendar.YEAR) - i;
                params.put("year", presentYear);
                List<Object[]> result = query("dashboard/dashboard_pac_d12_chart02.sql", params).getResultList();
                for (Object[] object : result) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("nam", String.valueOf(presentYear));
                    item.put("male", Long.valueOf(object[1] == null ? "0" : object[1].toString()));
                    item.put("female", Long.valueOf(object[2] == null ? "0" : object[2].toString()));
                    item.put("unclear", Long.valueOf(object[3] == null ? "0" : object[3].toString()));
                    data.put(item.get("nam").toString(), item);
                }
            }
        } else {
            params.put("year", year);
            List<Object[]> result = query("dashboard/dashboard_pac_d12_chart02_2.sql", params).getResultList();
            if (result == null) {
                return null;
            }
            Map<String, Object> item;
            for (Object[] object : result) {
                if (object[1] == null || object[2] == null) {
                    continue;
                }
                item = new LinkedHashMap<>();
                item.put("tenhuyen", object[1].toString());
                item.put("sotichluy", Long.valueOf(object[2].toString()));
                data.put(object[0].toString(), item);
            }
        }

        return data;
    }

    /**
     * @author DSNAnh Get data d12 chart05
     * @param province
     * @param district
     * @param year
     * @param gender
     * @param modesOfTransmision
     * @param testObjectGroup
     * @return
     */
    @Cacheable(value = "dashboard_getPacD12Chart05")
    public Map<String, Map<String, Object>> getPacD12Chart05(String province, List<String> district, int year, List<String> gender, List<String> modesOfTransmision, List<String> testObjectGroup) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("district", district);
        params.put("gender", gender);
        params.put("modesOfTransmision", modesOfTransmision);
        params.put("testObjectGroup", testObjectGroup);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Map<String, Map<String, Object>> data = new LinkedHashMap();
        if (year == -1) {
            for (int i = 9; i >= 0; i--) {
                int presentYear = now.get(Calendar.YEAR) - i;
                params.put("year", presentYear);
                List<Object[]> result = query("dashboard/dashboard_pac_d12_chart05.sql", params).getResultList();
                for (Object[] object : result) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("nam", String.valueOf(presentYear));
                    item.put("mau", Long.valueOf(object[1] == null ? "0" : object[1].toString()));
                    item.put("tinhduc", Long.valueOf(object[2] == null ? "0" : object[2].toString()));
                    item.put("mesangcon", Long.valueOf(object[3] == null ? "0" : object[3].toString()));
                    item.put("khongro", Long.valueOf(object[4] == null ? "0" : object[4].toString()));
                    item.put("truyenmau", Long.valueOf(object[1] == null ? "0" : object[5].toString()));
                    item.put("tainannghenghiep", Long.valueOf(object[2] == null ? "0" : object[6].toString()));
                    item.put("tiemchich", Long.valueOf(object[3] == null ? "0" : object[7].toString()));
                    item.put("tdkhacgioi", Long.valueOf(object[4] == null ? "0" : object[8].toString()));
                    item.put("tddonggioi", Long.valueOf(object[5] == null ? "0" : object[9].toString()));
                    item.put("khongcothongtin", Long.valueOf(object[5] == null ? "0" : object[10].toString()));
                    data.put(item.get("nam").toString(), item);
                }

            }
        } else {
            params.put("year", year);
            List<Object[]> result = query("dashboard/dashboard_pac_d12_chart05_2.sql", params).getResultList();
            if (result == null) {
                return null;
            }
            Map<String, Object> item;
            for (Object[] object : result) {
                if (object[0] == null || object[1] == null) {
                    continue;
                }
                item = new LinkedHashMap<>();
                item.put("duonglay", object[0].toString());
                item.put("soluong", Long.valueOf(object[1].toString()));
                data.put(item.get("duonglay").toString(), item);
            }
        }
        return data;
    }
}
