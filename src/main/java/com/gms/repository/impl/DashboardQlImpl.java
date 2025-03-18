package com.gms.repository.impl;

import com.gms.entity.form.pac.PacQLTableForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Tổng quan: phần Dashboard QL
 *
 * @author vvthanh
 */
@Repository
public class DashboardQlImpl extends BaseRepositoryImpl {

    public Map<String, Map<String, String>> getChart01(int from_month, int to_month, int year) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("from_month", from_month);
        params.put("to_month", to_month);
        params.put("year", year);
        List<Object[]> result = query("dashboard-ql/chart-01.sql", params).getResultList();

        Map<String, Map<String, String>> data = new HashMap<>();
        if (result == null) {
            return data;
        }

        Map<String, String> item;
        for (Object[] object : result) {
            String id = object[0].toString();
            item = new HashMap<>();
            item.put("htc", object[1] == null ? "0" : object[1].toString());
            item.put("early", object[2] == null ? "0" : object[2].toString());
            data.put(id, item);
        }
        return data;
    }

    public Map<String, Map<String, String>> getChart04(int from_month, int to_month, int year) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("from_month", from_month);
        params.put("to_month", to_month);
        params.put("year", year);
        List<Object[]> result = query("dashboard-ql/chart-04.sql", params).getResultList();

        Map<String, Map<String, String>> data = new HashMap<>();
        if (result == null) {
            return data;
        }

        Map<String, String> item;
        for (Object[] object : result) {
            String id = object[0].toString();
            item = new HashMap<>();
            item.put("phat_hien", object[1] == null ? "0" : object[1].toString());
            item.put("xn_moi_nhiem", object[2] == null ? "0" : object[2].toString());
            item.put("co_phan_ung", object[3] == null ? "0" : object[3].toString());
            data.put(id, item);
        }
        return data;
    }

    private Map<String, Integer> getTime(String time) {
        Map<String, Integer> map = new HashMap<>();
        if (StringUtils.isNotEmpty(time)) {
            time = time.length() == 6 ? ("02/0" + time) : ("02/" + time);
            int month = Integer.valueOf(time.substring(3, 5));
            int year = Integer.valueOf(time.substring(time.length() - 4));

            map.put("month", month);
            map.put("year", year);
        }
        return map;
    }

    public Map<String, Integer> getChart02_colorOfprovince(String provinceID, String time1, String time2, String time3) {
        Map<String, Integer> map = new HashMap<>();
        List<PacQLTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

        params.put("month1", getTime(time1).get("month"));
        params.put("month2", getTime(time2).get("month"));
        params.put("month3", getTime(time3).get("month"));

        params.put("year1", getTime(time1).get("year"));
        params.put("year2", getTime(time2).get("year"));
        params.put("year3", getTime(time3).get("year"));

        params.put("provinceID", provinceID);

        List<Object[]> result = query("dashboard-ql/chart-02-d.sql", params).getResultList();

        PacQLTableForm localForm;
        for (Object[] objects : result) {
            localForm = new PacQLTableForm();
            localForm.setDistrictID(objects[0] == null ? null : objects[0].toString());
            localForm.setMonth1(Integer.valueOf(objects[10].toString()));
            localForm.setMonth2(Integer.valueOf(objects[11].toString()));
            localForm.setMonth3(Integer.valueOf(objects[12].toString()));

            localForm.setAgv1(objects[7] == null ? 0 : Double.valueOf(objects[7].toString()));
            localForm.setAgv2(objects[8] == null ? 0 : Double.valueOf(objects[8].toString()));
            localForm.setAgv3(objects[9] == null ? 0 : Double.valueOf(objects[9].toString()));
            data.add(localForm);
        }
        List<PacQLTableForm> list = new ArrayList<>();
        for (PacQLTableForm p : data) {
            p.setColor1(getColor(p.getMonth1(), p.getAgv1()));
            p.setColor2(getColor(p.getMonth2(), p.getAgv2()));
            p.setColor3(getColor(p.getMonth3(), p.getAgv3()));
            list.add(p);
        }
        map.put("1", 1);
        map.put("2", 1);
        map.put("3", 1);
        for (PacQLTableForm pacQLTableForm : list) {
            map.put("1", map.get("1") > pacQLTableForm.getColor1() ? map.get("1") : pacQLTableForm.getColor1());
            map.put("2", map.get("2") > pacQLTableForm.getColor2() ? map.get("2") : pacQLTableForm.getColor2());
            map.put("3", map.get("3") > pacQLTableForm.getColor3() ? map.get("3") : pacQLTableForm.getColor3());
        }

        return map;
    }

    public Map<String, Integer> getChart02_colorOfDistrict(String provinceID, String time1, String time2, String time3, String level) {
        Map<String, Integer> map = new HashMap<>();
        List<PacQLTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

        params.put("month1", getTime(time1).get("month"));
        params.put("month2", getTime(time2).get("month"));
        params.put("month3", getTime(time3).get("month"));

        params.put("year1", getTime(time1).get("year"));
        params.put("year2", getTime(time2).get("year"));
        params.put("year3", getTime(time3).get("year"));

        params.put("provinceID", provinceID);

        List<Object[]> result = query("dashboard-ql/chart-02-d.sql", params).getResultList();

        PacQLTableForm localForm;
        for (Object[] objects : result) {
            localForm = new PacQLTableForm();
            localForm.setDistrictID(objects[0] == null ? null : objects[0].toString());
            localForm.setMonth1(Integer.valueOf(objects[10].toString()));
            localForm.setMonth2(Integer.valueOf(objects[11].toString()));
            localForm.setMonth3(Integer.valueOf(objects[12].toString()));

            localForm.setAgv1(objects[7] == null ? 0 : Double.valueOf(objects[7].toString()));
            localForm.setAgv2(objects[8] == null ? 0 : Double.valueOf(objects[8].toString()));
            localForm.setAgv3(objects[9] == null ? 0 : Double.valueOf(objects[9].toString()));
            data.add(localForm);
        }
        List<PacQLTableForm> list = new ArrayList<>();
        for (PacQLTableForm p : data) {
            p.setColor1(getColor(p.getMonth1(), p.getAgv1()));
            p.setColor2(getColor(p.getMonth2(), p.getAgv2()));
            p.setColor3(getColor(p.getMonth3(), p.getAgv3()));
            list.add(p);
        }

        for (PacQLTableForm pacQLTableForm : list) {
            map.put(pacQLTableForm.getDistrictID(), level.equals("1") ? pacQLTableForm.getColor1() : level.equals("2") ? pacQLTableForm.getColor2() : pacQLTableForm.getColor3());
        }
        return map;
    }
    
    public Map<String, Integer> getChart02_EarlyDistrict(String provinceID, String time1, String time2, String time3, String level) {
        Map<String, Integer> map = new HashMap<>();
        List<PacQLTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

        params.put("month1", getTime(time1).get("month"));
        params.put("month2", getTime(time2).get("month"));
        params.put("month3", getTime(time3).get("month"));

        params.put("year1", getTime(time1).get("year"));
        params.put("year2", getTime(time2).get("year"));
        params.put("year3", getTime(time3).get("year"));

        params.put("provinceID", provinceID);

        List<Object[]> result = query("dashboard-ql/chart-02-d.sql", params).getResultList();

        PacQLTableForm localForm;
        for (Object[] objects : result) {
            localForm = new PacQLTableForm();
            localForm.setDistrictID(objects[0] == null ? null : objects[0].toString());
            localForm.setMonth1(Integer.valueOf(objects[10].toString()));
            localForm.setMonth2(Integer.valueOf(objects[11].toString()));
            localForm.setMonth3(Integer.valueOf(objects[12].toString()));

            localForm.setAgv1(objects[7] == null ? 0 : Double.valueOf(objects[7].toString()));
            localForm.setAgv2(objects[8] == null ? 0 : Double.valueOf(objects[8].toString()));
            localForm.setAgv3(objects[9] == null ? 0 : Double.valueOf(objects[9].toString()));
            data.add(localForm);
        }
        

        for (PacQLTableForm pacQLTableForm : data) {
            map.put(pacQLTableForm.getDistrictID(), level.equals("1") ? pacQLTableForm.getMonth1() : level.equals("2") ? pacQLTableForm.getMonth2() : pacQLTableForm.getMonth3());
        }
        return map;
    }

    private int getColor(int a, double b) {
        if (a >= 2 || b > 0.2) {
            return 4;
        } else if (a == 1 || (0 < b && b <= 0.2)) {
            return 3;
        }
        return 1;
    }

    public List<PacQLTableForm> getChart02(String provinceID, String levelDisplay, String time1, String time2, String time3) {
        List<PacQLTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

        params.put("month1", getTime(time1).get("month"));
        params.put("month2", getTime(time2).get("month"));
        params.put("month3", getTime(time3).get("month"));

        params.put("year1", getTime(time1).get("year"));
        params.put("year2", getTime(time2).get("year"));
        params.put("year3", getTime(time3).get("year"));

        params.put("provinceID", provinceID);
        params.put("levelDisplay", levelDisplay);

        List<Object[]> result = query("dashboard-ql/chart-02.sql", params).getResultList();

        PacQLTableForm localForm;
        for (Object[] objects : result) {
            localForm = new PacQLTableForm();
            localForm.setProvinceID(objects[0] == null ? null : objects[0].toString());
            localForm.setDistrictID(objects[1] == null ? null : objects[1].toString());
            localForm.setMonth1(Integer.valueOf(objects[2].toString()));
            localForm.setMonth2(Integer.valueOf(objects[3].toString()));
            localForm.setMonth3(Integer.valueOf(objects[4].toString()));
            data.add(localForm);
        }
        return data;
    }

    public Map<String, Map<String, String>> getChart03(String province_id, String district_id, String ward_id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province_id", province_id);
        params.put("district_id", district_id);
        params.put("ward_id", ward_id);
        List<Object[]> result = query("dashboard-ql/chart-03.sql", params).getResultList();
        Map<String, Map<String, String>> data = new HashMap<>();
        if (result == null) {
            return data;
        }
        Map<String, String> item;
        for (Object[] object : result) {
            String year = object[0].toString();
            item = new HashMap<>();
            item.put("nhiem_moi", object[1].toString());
            item.put("tu_vong", object[2].toString());
            data.put(year, item);
        }
        return new TreeMap<>(data);
    }
}
