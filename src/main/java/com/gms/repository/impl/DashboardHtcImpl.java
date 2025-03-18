package com.gms.repository.impl;

import com.gms.components.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Tổng quan: phần htc
 *
 * @author vvthanh
 */
@Repository
public class DashboardHtcImpl extends BaseRepositoryImpl {

    /**
     * D2_Tổng quan TV&XN - Chart 01
     *
     * @param fromTime
     * @param toTime
     * @auth vvThành
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01")
    public Map<String, Map<String, Object>> getHtcChart01(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("nhanketqua", Long.valueOf(object[1].toString()));
            item.put("soxn", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);

        }
        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart 02
     *
     * @param fromTime
     * @param toTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart02")
    public Map<String, Map<String, Object>> getHtcChart02(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("xetnghiemduongtinh", Long.valueOf(object[2].toString()));
            item.put("duongtinhnhanketqua", Long.valueOf(object[1].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart 03
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart03")
    public Map<String, Map<String, Object>> getHtcChart03(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duongtinh", object[1].toString());
            item.put("chuyengui", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart 01d21
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01d21")
    public Map<String, Map<String, Object>> getHtcChart01d21(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01d21.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("thang", object[0].toString());
            item.put("nhanketqua", Long.valueOf(object[1].toString()));
            item.put("soxn", Long.valueOf(object[2].toString()));
            data.put(item.get("thang").toString(), item);
        }
        return data;
    }

    /**
     * Lấy danh sách ca dương tính theo thời gian báo cáo
     *
     * @auth TrangBN
     * @param confirmIDs
     * @param services
     * @param site
     * @param timeParam
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart04")
    public List<Map<String, Object>> getHtcChart04(List<Long> site, List<String> services, Map<String, String> timeParam, List<Long> confirmIDs) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", confirmIDs);
        params.put("source_site_id", site);
        params.put("services", services);
        params.put("from_time1", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("fromDateQ1", null)));
        params.put("from_time2", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("fromDateQ2", null)));
        params.put("from_time3", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("fromDateQ3", null)));
        params.put("from_time4", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("fromDateQ4", null)));
        params.put("from_time5", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("fromDateQ5", null)));
        params.put("to_time1", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("toDateQ1", null)));
        params.put("to_time2", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("toDateQ2", null)));
        params.put("to_time3", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("toDateQ3", null)));
        params.put("to_time4", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("toDateQ4", null)));
        params.put("to_time5", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("toDateQ5", null)));
        params.put("first_day_year", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("firstDayYear", null)));
        params.put("last_day_year", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, timeParam.getOrDefault("lastDayYear", null)));

        List<Object[]> result = query("dashboard/dashboard_htc_chart04.sql", params).getResultList();
        if (result == null) {
            return null;
        }

        Object[] resultArray = result.get(0);

        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("phanloai", "Số ca dương tính phát hiện");
        item.put("soca", Long.valueOf(resultArray[0] != null ? resultArray[0].toString() : "0"));
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số ca làm xét nghiệm phát hiện nhiễm mới");
        item.put("soca", Long.valueOf(resultArray[1] != null ? resultArray[1].toString() : "0"));
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số ca xét nghiệm kết quả nhiễm mới");
        item.put("soca", Long.valueOf(resultArray[2] != null ? resultArray[2].toString() : "0"));
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số ca lấy mẫu làm tải lượng virus");
        item.put("soca", Long.valueOf(resultArray[3] != null ? resultArray[3].toString() : "0"));
        listResult.add(item);

        item = new HashMap<>();
        item.put("phanloai", "Số ca khẳng định nhiễm mới có tải lượng virus > 1.000cps/ml");
        item.put("soca", Long.valueOf(resultArray[4] != null ? resultArray[4].toString() : "0"));
        listResult.add(item);

        return listResult;
    }

    /**
     * Lấy số lượng xn và nhận kq theo huyện
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD21Chart02")
    public Map<String, Map<String, Object>> getHtcD21Chart02(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d21_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;

        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("huyen", object[0].toString());
            item.put("nhanketqua", Long.valueOf(object[1].toString()));
            data.put(item.get("huyen").toString(), item);
        }
        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart d21 06
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart06d21")
    public Map<String, Long> getHtcChart06d21(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart06d21.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }
        }

        return data;
    }

    /**
     * D2_Tổng quan TV&XN - Chart d21 05
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart05d21")
    public Map<String, Long> getHtcChart05d21(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart05d21.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            if (object[0] != null && object[1] != null) {
                data.put(object[0].toString(), Long.valueOf(object[1].toString()));
            }

        }

        return data;
    }

    /**
     * Lấy thông tin chart03
     *
     * @auth TrangBN
     * @param fromTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD21Chart03")
    public Map<String, Map<String, Object>> getHtcD21Chart03(List<Long> site, List<String> service, String fromTime, String toTime) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03d21.sql", params).getResultList();
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
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01d22")
    public Map<String, Map<String, Object>> getHtcChart01d22(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01d22.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("thang", object[0].toString());
            item.put("nhanketqua", Long.valueOf(object[1].toString()));
            item.put("soxn", Long.valueOf(object[2].toString()));
            data.put(item.get("thang").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số lượng xét nghiệm và nhận kết quả theo nhóm tuổi
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD21Chart04")
    public Map<String, Map<String, Object>> getHtcD21Chart04(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d21_chart04.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duoi15", Long.valueOf(object[1].toString()));
            item.put("15den24", Long.valueOf(object[2].toString()));
            item.put("25den49", Long.valueOf(object[3].toString()));
            item.put("tren49", Long.valueOf(object[4].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * pdThang
     *
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01d23")
    public Map<String, Map<String, Object>> getHtcChart01d23(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01d23.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("thang", object[0].toString());
            item.put("sochuyenguithanhcong", Long.valueOf(object[1].toString()));
            item.put("sochuyengui", Long.valueOf(object[2].toString()));
            data.put(item.get("thang").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số lượng xét nghiệm dương tính và nhận kq theo nhóm tuổi
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD22Chart04")
    public Map<String, Map<String, Object>> getHtcD22Chart04(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d22_chart04.sql", params).getResultList();
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duoi15", Long.valueOf(object[1].toString()));
            item.put("15den24", Long.valueOf(object[2].toString()));
            item.put("25den49", Long.valueOf(object[3].toString()));
            item.put("tren49", Long.valueOf(object[4].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * pdThang
     *
     * @param site Ma co so khang dinh cua tinh
     * @param sourceSite Ma co so can tim kiem - co so sang loc
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01d24")
    public Map<String, Map<String, Object>> getHtcChart01d24(List<Long> site, List<Long> sourceSite, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(Long.valueOf("-1"));
        }
        if (sourceSite == null || sourceSite.isEmpty()) {
            sourceSite = new ArrayList<>();
            sourceSite.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("source_site_id", sourceSite);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01d24.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("thang", object[0].toString());
            item.put("nhohon6", Long.valueOf(object[1].toString()));
            item.put("lonhon12", Long.valueOf(object[2].toString()));
            item.put("tongxnnhiemmoi", Long.valueOf(object[3].toString()));
            data.put(item.get("thang").toString(), item);
        }
        return data;
    }

    /**
     * pdThang
     *
     * @param site
     * @param sourceSite
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart01d25")
    public Map<String, Map<String, Object>> getHtcChart01d25(List<Long> site, List<Long> sourceSite, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(Long.valueOf("-1"));
        }
        if (sourceSite == null || sourceSite.isEmpty()) {
            sourceSite = new ArrayList<>();
            sourceSite.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("source_site_id", sourceSite);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart01d25.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("thang", object[0].toString());
            item.put("nhohon6", Long.valueOf(object[1].toString()));
            item.put("lonhon12", Long.valueOf(object[2].toString()));
            item.put("tongxntailuongvirus", Long.valueOf(object[3].toString()));
            data.put(item.get("thang").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số lượng xn dương tính và nhận kết quả theo huyện
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD22Chart02")
    public Map<String, Map<String, Object>> getHtcD22Chart02(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d22_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;

        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("huyen", object[0].toString());
            item.put("soxn", Long.valueOf(object[1].toString()));
            data.put(item.get("huyen").toString(), item);
        }
        return data;
    }

    /**
     * Lấy thông tin chart03 D22 Số xét nghiệm dương tính và nhận kết quả
     *
     * @auth TrangBN
     * @param fromTime
     * @param toTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD22Chart03")
    public Map<String, Map<String, Object>> getHtcD22Chart03(List<Long> site, List<String> service, String fromTime, String toTime) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03d22.sql", params).getResultList();
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
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy thông tin chart03 D23 Số chuyển gửi điều trị thành công
     *
     * @auth TrangBN
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD23Chart03")
    public Map<String, Map<String, Object>> getHtcD23Chart03(List<Long> site, List<String> service, String fromTime, String toTime) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03d23.sql", params).getResultList();
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
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số lượng CGĐT thành công theo huyện
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD23Chart02")
    public Map<String, Map<String, Object>> getHtcD23Chart02(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d23_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;

        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("huyen", object[0].toString());
            item.put("sochuyenguithanhcong", Long.valueOf(object[1].toString()));
            data.put(item.get("huyen").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số lượng CGĐT thành công theo nhóm tuổi
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD23Chart04")
    public Map<String, Map<String, Object>> getHtcD23Chart04(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d23_chart04.sql", params).getResultList();
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duoi15", Long.valueOf(object[1].toString()));
            item.put("15den24", Long.valueOf(object[2].toString()));
            item.put("25den49", Long.valueOf(object[3].toString()));
            item.put("tren49", Long.valueOf(object[4].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số ca có KQ XN nhiễm mới dưới 6 tháng theo huyện
     *
     * @author DSNAnh
     * @param siteConfirms
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD24Chart02")
    public Map<String, Map<String, Object>> getHtcD24Chart02(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d24_chart02.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;

        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("huyen", object[0].toString());
            item.put("tongxnnhiemmoi", Long.valueOf(object[1].toString()));
            data.put(item.get("huyen").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số ca có KQ XN nhiễm mới dưới 6 tháng theo nhóm tuổi
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD24Chart04")
    public Map<String, Map<String, Object>> getHtcD24Chart04(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d24_chart04.sql", params).getResultList();
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duoi15", Long.valueOf(object[1].toString()));
            item.put("15den24", Long.valueOf(object[2].toString()));
            item.put("25den49", Long.valueOf(object[3].toString()));
            item.put("tren49", Long.valueOf(object[4].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy thông tin chart03 D24 Số ca có kết quả XN nhiễm mới < 6 tháng
     *
     * @param toTime
     * @param confirmIDs
     * @auth TrangBN
     * @param fromTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD24Chart03")
    public Map<String, Map<String, Object>> getHtcD24Chart03(List<Long> site, List<String> service, String fromTime, String toTime, List<Long> confirmIDs) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", confirmIDs);
        params.put("source_site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03d24.sql", params).getResultList();
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
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy thông tin chart03 D25 Số ca có kết quả XN tải lượng virus <1.000 bản
     * sao/ml
     *
     * @param toTime
     * @auth TrangBN
     * @param fromTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD25Chart03")
    public Map<String, Map<String, Object>> getHtcD25Chart03(List<Long> site, List<String> service, String fromTime, String toTime, List<Long> confirmIDs) {

        if (site == null || site.isEmpty()) {
            site = new ArrayList<>();
            site.add(-1L);
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", confirmIDs);
        params.put("source_site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart03d25.sql", params).getResultList();
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
            item.put("quy", object[0].toString());
            item.put("nam", Long.valueOf(object[1].toString()));
            item.put("nu", Long.valueOf(object[2].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số ca có KQ XN tải lượng virus dưới 1.000 bản sao/ml theo huyện
     *
     * @author DSNAnh
     * @param siteConfirms
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD25Chart02")
    public Map<String, Map<String, Object>> getHtcD25Chart02(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d25_chart02.sql", params).getResultList();

        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("huyen", object[0].toString());
            item.put("tongxntailuongvirus", Long.valueOf(object[1].toString()));
            data.put(item.get("huyen").toString(), item);
        }
        return data;
    }

    /**
     * Lấy số ca có KQ XN tải lượng virus dưới 1.000 bản sao/ml theo nhóm tuổi
     *
     * @author DSNAnh
     * @param siteConfirms
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD25Chart04")
    public Map<String, Map<String, Object>> getHtcD25Chart04(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d25_chart04.sql", params).getResultList();
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        for (Object[] object : result) {
            item = new LinkedHashMap<>();
            item.put("quy", object[0].toString());
            item.put("duoi15", Long.valueOf(object[1].toString()));
            item.put("15den24", Long.valueOf(object[2].toString()));
            item.put("25den49", Long.valueOf(object[3].toString()));
            item.put("tren49", Long.valueOf(object[4].toString()));
            data.put(item.get("quy").toString(), item);
        }
        return data;
    }

    /**
     * Số ca có kết quả XN tải lượng virus <1.000 bản sao/ml theo nhóm đối tượng
     * @param siteConfirms
     * @p
     *
     * a
     * ram fromTime
     * @auth DSNAnh @param fr
     *
     *
     *
     * o
     * mTime
     * @param toTime
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD25Chart05")
    public Map<String, Long> getHtcD25Chart05(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d25_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;
    }

    /**
     * D22 chart 06 Số xét nghiệm dương tính và nhận kết quả
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart06d22")
    public Map<String, Long> getHtcChart06d22(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart06d22.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;
    }

    /**
     * D23 chart 06 Số chuyển gửi điều trị thành công
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart06d23")
    public Map<String, Long> getHtcChart06d23(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart06d23.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;
    }

    /**
     * Số ca có kết quả XN nhiễm mới <6 tháng theo nhóm đối tượng @author DSNAnh
     *
     * @param siteConfirms
     * @param site
     * @
     * param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD24Chart05")
    public Map<String, Long> getHtcD24Chart05(List<Long> siteConfirms, List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        if (siteConfirms == null || siteConfirms.isEmpty()) {
            siteConfirms = new ArrayList<>();
            siteConfirms.add(-1L);
        }
        params.put("source_site_id", site);
        params.put("site_confirm", siteConfirms);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d24_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;

    }

    /**
     * D24 Chart 06 Số ca có kết quả XN nhiễm mới <6 tháng
     *
     * @param fromTime
     * @param toTime
     * @auth pdThang
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart06d24")
    public Map<String, Long> getHtcChart06d24(List<Long> site, List<String> service, String fromTime, String toTime, List<Long> confirmIDs) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", confirmIDs);
        params.put("source_site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart06d24.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;
    }

    /**
     * Số chuyển gửi điều trị thành công theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD23Chart05")
    public Map<String, Long> getHtcD23Chart05(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d23_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;

    }

    /**
     * Số xét nghiệm dương tính và nhận kết quả theo nhóm đối tượng
     *
     * @author DSNAnh
     * @param site
     * @param service
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable(value = "dashboard_getHtcD22Chart05")
    public Map<String, Long> getHtcD22Chart05(List<Long> site, List<String> service, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_d22_chart05.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;

    }

    /**
     * D25 Chart 06 Số ca có kết quả XN tải lượng virus <1.000 bản sao/ml
     *
     * @param fromTime
     * @param toTime
     * @auth TrangBN
     * @param site
     * @param service
     * @return
     */
    @Cacheable(value = "dashboard_getHtcChart06d25")
    public Map<String, Long> getHtcChart06d25(List<Long> site, List<String> service, String fromTime, String toTime, List<Long> confirmIDs) {
        HashMap<String, Object> params = new HashMap<>();
        if (site.isEmpty()) {
            site.add(Long.valueOf("-1"));
        }
        params.put("site_id", confirmIDs);
        params.put("source_site_id", site);
        params.put("services", service);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("dashboard/dashboard_htc_chart06d25.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Long> data = new LinkedHashMap();
        for (Object[] object : result) {
            data.put(object[0].toString(), Long.valueOf(object[1].toString()));
        }
        return data;
    }
}
