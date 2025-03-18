package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.form.laytest.LaytestTable01Form;
import com.gms.entity.form.laytest.LaytestTable02Form;
import com.gms.entity.form.laytest.MerTableForm;
import static com.gms.repository.impl.BaseRepositoryImpl.FORMATDATE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthanh
 */
@Repository
public class HtcLaytestRepositoryImpl extends BaseRepositoryImpl {

    @Value("${app.report.tt03.table02.sort}")
    private String tt03Table02Sort;
    
    @Value("${app.report.tt09.phuluc01.sort}")
    private String tt09Phuluc01Sort;
    /**
     * KC1.7a_TQ_XN theo nhóm
     *
     * @param siteID
     * @param confirmResult
     * @param staffID
     * @param fromTime
     * @param toTime
     * @auth TrangBN
     * @return
     */
    @Cacheable("getDashboardChart01")
    public Map<String, Map<String, Object>> getDashboardChart01(Long siteID, String confirmResult, Long staffID, String fromTime, String toTime) {

        Query query = getQuery("SELECT "
                + "   p.id, "
                + "   p.parent_id, "
                + "   p.value as o, "
                + "   coalesce(v_main.c, 0) as c "
                + "FROM (SELECT "
                + "         e.object_group_id, "
                + "         count(e.id) as c "
                + "     FROM htc_laytest e WHERE e.is_remove = 0 "
                + "         AND e.site_id = :site_id "
                + "         AND e.created_by = :staff_id "
                + "         AND e.confirm_results_id = :confirm_result "
                + "         AND e.confirm_time BETWEEN :from_date AND :to_date "
                + "         GROUP BY e.object_group_id) as v_main "
                + "RIGHT JOIN parameter p on p.code = v_main.object_group_id "
                + "WHERE p.type = 'test-object-group' "
                + "GROUP BY p.value, p.id, p.parent_id  ORDER BY p.position asc");

        query.setParameter("site_id", siteID);
        query.setParameter("confirm_result", confirmResult);
        query.setParameter("from_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        query.setParameter("to_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        query.setParameter("staff_id", staffID);

        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        List<LaytestTable01Form> table = new ArrayList<>();
        LaytestTable01Form form;
        List<Object[]> result = query.getResultList();
        for (Object[] object : result) {
            form = new LaytestTable01Form();
            form.setoID(Long.valueOf(object[0].toString()));
            form.setoParentID(Long.valueOf(object[1].toString()));
            form.setDanhMucBaoCao(object[2].toString());
            form.setDuongTinhTong(Integer.valueOf(object[3].toString()));
            table.add(form);
        }
        for (LaytestTable01Form sub : table) {
            if (!sub.getoParentID().equals(Long.parseLong("0"))) {
                continue;
            }
            for (LaytestTable01Form child : table) {
                if (Objects.equals(sub.getoID(), child.getoParentID())) {
                    sub.setDuongTinhTong(sub.getDuongTinhTong() + child.getDuongTinhTong());
                }
            }
            item = new LinkedHashMap<>();
            if (sub.getDuongTinhTong() > 0) {
                item.put("danhmuc", sub.getDanhMucBaoCao());
                item.put("so_xn", Long.valueOf(sub.getDuongTinhTong()));
                data.put(item.get("danhmuc").toString(), item);
            }
        }
        return data;
    }

    /**
     * KC1.7b_TQ_XN (+) theo nhóm
     *
     * @param siteID
     * @param staffID
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable("getDashboardChart02")
    public Map<String, Map<String, Object>> getDashboardChart02(Long siteID, Long staffID, String fromTime, String toTime) {
        Query query = getQuery("SELECT "
                + "   p.id, "
                + "   p.parent_id, "
                + "   p.value as o, "
                + "   coalesce(v_main.c, 0) as c "
                + "FROM (SELECT "
                + "         e.object_group_id, "
                + "         count(e.id) as c "
                + "     FROM htc_laytest e WHERE e.is_remove = 0 "
                + "         AND e.site_id = :site_id "
                + "         AND e.created_by = :staff_id "
                + "         AND e.advisory_time BETWEEN :from_date AND :to_date "
                + "         GROUP BY e.object_group_id) as v_main "
                + "RIGHT JOIN parameter p on p.code = v_main.object_group_id "
                + "WHERE p.type = 'test-object-group' "
                + "GROUP BY p.value, p.id, p.parent_id  ORDER BY p.position asc");

        query.setParameter("site_id", siteID);
        query.setParameter("from_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        query.setParameter("to_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        query.setParameter("staff_id", staffID);
        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        List<LaytestTable02Form> table = new ArrayList<>();
        LaytestTable02Form form;
        List<Object[]> result = query.getResultList();
        for (Object[] object : result) {
            form = new LaytestTable02Form();
            form.setoID(Long.valueOf(object[0].toString()));
            form.setoParentID(Long.valueOf(object[1].toString()));
            form.setDanhMucBaoCao(object[2].toString());
            form.setDuocXetNghiemTong(Integer.valueOf(object[3].toString()));
            table.add(form);
        }
        for (LaytestTable02Form sub : table) {
            if (!sub.getoParentID().equals(Long.parseLong("0"))) {
                continue;
            }
            for (LaytestTable02Form child : table) {
                if (Objects.equals(sub.getoID(), child.getoParentID())) {
                    sub.setDuocXetNghiemTong(sub.getDuocXetNghiemTong() + child.getDuocXetNghiemTong());
                }
            }
            item = new LinkedHashMap<>();
            if (sub.getDuocXetNghiemTong() > 0) {
                item.put("danhmuc", sub.getDanhMucBaoCao());
                item.put("so_xn", Long.valueOf(sub.getDuocXetNghiemTong()));
                data.put(item.get("danhmuc").toString(), item);
            }
        }
        return data;
    }

    /**
     * KC1.7c_TQ_XN &(+) theo tháng
     *
     * @param siteID
     * @param staffID
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable("getDashboardChart03")
    public Map<String, Map<String, Object>> getDashboardChart03(Long siteID, Long staffID, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("staff_id", staffID);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("laytest_dashboard_chart03.sql", params).getResultList();

        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        if (result != null) {
            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("rule", object[0].toString());
                item.put("so_duongtinh", Long.valueOf(object[1].toString()));
                item.put("so_xn", Long.valueOf(object[2].toString()));
                data.put(item.get("rule").toString(), item);
            }
        }
        return data;
    }

    /**
     * KC1.7d_TQ_Chuyển gửi
     *
     * @param siteID
     * @param staffID
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable("getDashboardChart04")
    public Map<String, Map<String, Object>> getDashboardChart04(Long siteID, Long staffID, String fromTime, String toTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("staff_id", staffID);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        List<Object[]> result = query("laytest_dasboard_chart04.sql", params).getResultList();

        Map<String, Map<String, Object>> data = new HashMap();
        Map<String, Object> item;
        if (result != null) {
            for (Object[] object : result) {
                item = new LinkedHashMap<>();
                item.put("month", String.format("Quý %s", object[0].toString()));
                item.put("col1", Long.valueOf(object[1].toString()));
                item.put("col2", Long.valueOf(object[2].toString()));
                data.put(item.get("month").toString(), item);
            }
        }
        return data;
    }

    /**
     * Lấy thông tin bảng 01 báo cáo MER
     *
     * @param siteID
     * @param staffID
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable("getMerTable01")
    public List<MerTableForm> getMerTable01(Long siteID, Long staffID, String fromTime, String toTime) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("siteID", siteID);
        params.put("staffID", staffID);
        List<Object[]> resultList = query("laytest_mer_table01.sql", params).getResultList();

        MerTableForm merTableForm;
        List<MerTableForm> result = new ArrayList<>();
        for (Object[] obj : resultList) {
            merTableForm = new MerTableForm();
            merTableForm.setAge(String.valueOf(obj[0].toString()) + " tuổi");
            merTableForm.setPositiveMale(Integer.parseInt(obj[1].toString()));
            merTableForm.setPositiveFemale(Integer.parseInt(obj[2].toString()));
            merTableForm.setPositive(Integer.parseInt(obj[3].toString()));
            merTableForm.setNegativeMale(Integer.parseInt(obj[4].toString()));
            merTableForm.setNegativeFemale(Integer.parseInt(obj[5].toString()));
            merTableForm.setNegative(Integer.parseInt(obj[6].toString()));
            result.add(merTableForm);
        }
        return result;
    }
    
    /**
     * Lấy thông tin bảng 02 báo cáo MER
     * @auth NamAnh
     * @param siteID
     * @param staffID
     * @param fromTime
     * @param toTime
     * @return
     */
    @Cacheable("getMerTable02")
    public List<MerTableForm> getMerTable02(Long siteID, Long staffID, String fromTime, String toTime) {
        
        ArrayList<String> table02Sort = new ArrayList<>();
        table02Sort.addAll(Arrays.asList(tt09Phuluc01Sort.split(",")));
        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("siteID", siteID);
        params.put("staffID", staffID);
        List<Object[]> resultList = query("laytest_mer_table02.sql", params).getResultList();

        MerTableForm merTableForm;
        List<MerTableForm> table = new ArrayList<>();
        List<MerTableForm> result = new ArrayList<>();
        for (Object[] obj : resultList) {
            merTableForm = new MerTableForm();
            merTableForm.setoID(Long.parseLong(obj[0].toString()));
            merTableForm.setoValue(String.valueOf(obj[1].toString()));
            merTableForm.setoGroupID(obj[2] == null ? "" : String.valueOf(obj[2].toString()));
            merTableForm.setoParentID(obj[3] == null ? 0 : Long.parseLong(obj[3].toString()));
            merTableForm.setPositiveMale(obj[4] == null ? 0 : Integer.parseInt(obj[4].toString()));
            merTableForm.setPositiveFemale(obj[5] == null ? 0 : Integer.parseInt(obj[5].toString()));
            merTableForm.setPositive(obj[6] == null ? 0 : Integer.parseInt(obj[6].toString()));
            merTableForm.setNegativeMale(obj[7] == null ? 0 : Integer.parseInt(obj[7].toString()));
            merTableForm.setNegativeFemale(obj[8] == null ? 0 : Integer.parseInt(obj[8].toString()));
            merTableForm.setNegative(obj[9] == null ? 0 : Integer.parseInt(obj[9].toString()));
            merTableForm.setPosition(table02Sort.indexOf(obj[10].toString()));
            
            table.add(merTableForm);
        }
        Collections.sort(table, new BeanComparator("position"));
        for (MerTableForm sub : table) {
//            if (!sub.getoParentID().equals(Long.parseLong("0"))) {
//                continue;
//            }
            for (MerTableForm child : table) {
                if (Objects.equals(sub.getoID(), child.getoParentID())) {
                    sub.setPositiveMale(sub.getPositiveMale() + child.getPositiveMale());
                    sub.setPositiveFemale(sub.getPositiveFemale() + child.getPositiveFemale());
                    sub.setPositive(sub.getPositive() + child.getPositive());
                    sub.setNegativeMale(sub.getNegativeMale() + child.getNegativeMale());
                    sub.setNegativeFemale(sub.getNegativeFemale() + child.getNegativeFemale());
                    sub.setNegative(sub.getNegative() + child.getNegative());
                }
            }
            if (sub.getPosition() == -1) {
                if (!sub.getoParentID().equals(Long.parseLong("0"))) {
                    continue;
                }
                sub.setTong(sub.getPositiveMale() + sub.getPositiveFemale() + sub.getPositive() + sub.getNegativeMale() + sub.getNegativeFemale() + sub.getNegative());
                MerTableForm lastItem = table.get(table.size() - 1);
                lastItem.setoCode(lastItem.getoCode() + "," + sub.getoCode());
                lastItem.setPositiveMale(lastItem.getPositiveMale() + sub.getPositiveMale());
                lastItem.setPositiveFemale(lastItem.getPositiveFemale() + sub.getPositiveFemale());
                lastItem.setPositive(lastItem.getPositive() + sub.getPositive());
                lastItem.setNegativeMale(lastItem.getNegativeMale() + sub.getNegativeMale());
                lastItem.setNegativeFemale(lastItem.getNegativeFemale() + sub.getNegativeFemale());
                lastItem.setNegative(lastItem.getNegative() + sub.getNegative());

            }
//            if (sub.getPositiveMale() + sub.getPositiveFemale() + sub.getPositive() + sub.getNegativeMale() + sub.getNegativeFemale() + sub.getNegative() > 0) {
//                result.add(sub);
//            }
        }
        return table;
    }
    
    /**
     * @auth NamAnh
     * Lấy dữ liệu báo cáo hoạt động
     * @param fromDate
     * @param toDate
     * @param siteID
     * @param staffID
     * @return
     */
    public List<LaytestTable02Form> getTable02TT03Laytest(Long siteID, Long staffID, Date fromDate, Date toDate) {
        ArrayList<String> table02Sort = new ArrayList<>();
        table02Sort.addAll(Arrays.asList(tt03Table02Sort.split(",")));

        List<LaytestTable02Form> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("staff_id", staffID);
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("laytest_tt03_phuluc02.sql", params).getResultList();

        for (Object[] object : result) {
            LaytestTable02Form form = new LaytestTable02Form();
            form.setoCode(object[0].toString());
            form.setDanhMucBaoCao(object[1].toString());
            form.setTuVanTruocXetNghiem(Integer.valueOf(object[2].toString()));
            form.setDuocXetNghiemTong(Integer.valueOf(object[2].toString()));
            form.setDuocXetNghiemDuongTinh(Integer.valueOf(object[3].toString()));
            form.setNhanKetQuaTong(Integer.valueOf(object[2].toString()));
            form.setNhanKetQuaDuongtinh(Integer.valueOf(object[3].toString()));
            form.setGioiThieuBanChich(Integer.valueOf(object[4].toString()));
            form.setChuyenGuiDieuTri(Integer.valueOf(object[5].toString()));
            form.setoID(Long.valueOf(object[6].toString()));
            form.setoParentID(Long.valueOf(object[7].toString()));
            //Get stt theo code được cấu hình tại application.properties
            form.setPosition(table02Sort.indexOf(object[0].toString()));

            if (form.getDanhMucBaoCao().contains("Phụ nữ mang thai")) {
                form.setDanhMucBaoCao("Số phụ nữ mang thai được xét nghiệm HIV");
            }
            data.add(form);
        }

        Collections.sort(data, new BeanComparator("position"));
        int index = 1;
        for (LaytestTable02Form item : data) {
            //Cộng giá trị con và giá trị cha p = Sum child
            for (LaytestTable02Form chid : data) {
                if (Objects.equals(item.getoID(), chid.getoParentID())) {
                    item.setoCode(item.getoCode() + "," + chid.getoCode());
                    item.setTuVanTruocXetNghiem(item.getTuVanTruocXetNghiem() + chid.getTuVanTruocXetNghiem());
                    item.setDuocXetNghiemTong(item.getDuocXetNghiemTong()+ chid.getDuocXetNghiemTong());
                    item.setDuocXetNghiemDuongTinh(item.getDuocXetNghiemDuongTinh() + chid.getDuocXetNghiemDuongTinh());
                    item.setNhanKetQuaTong(item.getNhanKetQuaTong()+ chid.getNhanKetQuaTong());
                    item.setNhanKetQuaDuongtinh(item.getNhanKetQuaDuongtinh()+ chid.getNhanKetQuaDuongtinh());
                    item.setGioiThieuBanChich(item.getGioiThieuBanChich() + chid.getGioiThieuBanChich());
                    item.setChuyenGuiDieuTri(item.getChuyenGuiDieuTri() + chid.getChuyenGuiDieuTri());
                }
            }
            //Cộng những giá trị không được cấu hình hiển thị vào giá trị cuối cùng (thường là khác)
            if (item.getPosition() == -1) {
                LaytestTable02Form lastItem = data.get(data.size() - 1);
                lastItem.setoCode(lastItem.getoCode() + "," + item.getoCode());
                lastItem.setTuVanTruocXetNghiem(lastItem.getTuVanTruocXetNghiem() + item.getTuVanTruocXetNghiem());
                lastItem.setDuocXetNghiemTong(lastItem.getDuocXetNghiemTong()+ item.getDuocXetNghiemTong());
                lastItem.setDuocXetNghiemDuongTinh(lastItem.getDuocXetNghiemDuongTinh() + item.getDuocXetNghiemDuongTinh());
                lastItem.setNhanKetQuaTong(lastItem.getNhanKetQuaTong()+ item.getNhanKetQuaTong());
                lastItem.setNhanKetQuaDuongtinh(lastItem.getNhanKetQuaDuongtinh()+ item.getNhanKetQuaDuongtinh());
                lastItem.setGioiThieuBanChich(lastItem.getGioiThieuBanChich() + item.getGioiThieuBanChich());
                lastItem.setChuyenGuiDieuTri(lastItem.getChuyenGuiDieuTri() + item.getChuyenGuiDieuTri());
            } else if (item.getoParentID().equals(Long.parseLong("0"))) {
                item.setStt(index++);
            }
        }
        return data;
    }
}
