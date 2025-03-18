package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.htc.DashboardObjectGroupPercentForm;
import com.gms.entity.form.tt03.Table02Form;
import com.gms.entity.form.tt09.TablePhuluc01Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class HtcVisitRepositoryImpl extends BaseRepositoryImpl {

    @Value("${app.report.tt03.table02.sort}")
    private String tt03Table02Sort;

    @Value("${app.report.tt09.phuluc01.sort}")
    private String tt09Phuluc01Sort;

    /**
     * Danh sách báo cáo cơ sở theo phụ lục 02 - Thông tư 03
     *
     * @param code
     * @auth vvThành
     * @param siteID
     * @param fromDate
     * @param toDate
     * @param service
     * @return
     */
    @Cacheable("getTable02TT03")
    public List<Table02Form> getTable02TT03(List<Long> siteID, Date fromDate, Date toDate, List<String> service, String code, String customerType) {
        ArrayList<String> table02Sort = new ArrayList<>();
        table02Sort.addAll(Arrays.asList(tt03Table02Sort.split(",")));

        List<Table02Form> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        params.put("services", service);
        params.put("code", code);
        params.put("customerType", customerType);
        List<Object[]> result = query("htc_tt03_phuluc02.sql", params).getResultList();

        for (Object[] object : result) {
            Table02Form form = new Table02Form();
            form.setoCode(object[0].toString());
            form.setDanhMucBaoCao(object[1].toString());
            form.setTuVanTruocXetNghiem(Integer.valueOf(object[2].toString()));
            form.setDuocXetNghiemTong(Integer.valueOf(object[3].toString()));
            form.setDuocXetNghiemDuongTinh(Integer.valueOf(object[4].toString()));
            form.setNhanKetQuaTong(Integer.valueOf(object[5].toString()));
            form.setNhanKetQuaDuongtinh(Integer.valueOf(object[6].toString()));
            form.setoID(Long.valueOf(object[7].toString()));
            form.setoParentID(Long.valueOf(object[8].toString()));
            //Get stt theo code được cấu hình tại application.properties
            form.setPosition(table02Sort.indexOf(object[0].toString()));

            if (form.getDanhMucBaoCao().contains("Phụ nữ mang thai")) {
                form.setDanhMucBaoCao("Số phụ nữ mang thai được xét nghiệm HIV");
            }
            data.add(form);
        }

        Collections.sort(data, new BeanComparator("position"));
        int index = 1;
        for (Table02Form item : data) {

            //Cộng giá trị con và giá trị tra p = Sum child
            for (Table02Form chid : data) {
                if (Objects.equals(item.getoID(), chid.getoParentID())) {
                    item.setoCode(item.getoCode() + "," + chid.getoCode());
                    item.setTuVanTruocXetNghiem(item.getTuVanTruocXetNghiem() + chid.getTuVanTruocXetNghiem());
                    item.setDuocXetNghiemTong(item.getDuocXetNghiemTong() + chid.getDuocXetNghiemTong());
                    item.setDuocXetNghiemDuongTinh(item.getDuocXetNghiemDuongTinh() + chid.getDuocXetNghiemDuongTinh());
                    item.setNhanKetQuaTong(item.getNhanKetQuaTong() + chid.getNhanKetQuaTong());
                    item.setNhanKetQuaDuongtinh(item.getNhanKetQuaDuongtinh() + chid.getNhanKetQuaDuongtinh());
                }
            }
            //Cộng những giá trị không được cấu hình hiển thị vào giá trị cuối cùng (thường là khác)
            if (item.getPosition() == -1) {
                Table02Form lastItem = data.get(data.size() - 1);
                lastItem.setoCode(lastItem.getoCode() + "," + item.getoCode());
                lastItem.setTuVanTruocXetNghiem(lastItem.getTuVanTruocXetNghiem() + item.getTuVanTruocXetNghiem());
                lastItem.setDuocXetNghiemTong(lastItem.getDuocXetNghiemTong() + item.getDuocXetNghiemTong());
                lastItem.setDuocXetNghiemDuongTinh(lastItem.getDuocXetNghiemDuongTinh() + item.getDuocXetNghiemDuongTinh());
                lastItem.setNhanKetQuaTong(lastItem.getNhanKetQuaTong() + item.getNhanKetQuaTong());
                lastItem.setNhanKetQuaDuongtinh(lastItem.getNhanKetQuaDuongtinh() + item.getNhanKetQuaDuongtinh());
            } else if (item.getoParentID().equals(Long.parseLong("0"))) {
                item.setStt(index++);
            }
        }
        return data;
    }

    /**
     * Lấy thông tin table phụ lục 01 - Thông thư 09
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @param service
     * @param code
     * @param customerType
     * @return
     */
    @Cacheable("getTablePhuluc01TT09")
    public List<TablePhuluc01Form> getTablePhuluc01TT09(List<Long> siteID, Date fromDate, Date toDate, List<String> service, String code, String customerType) {
        ArrayList<String> table02Sort = new ArrayList<>();
        table02Sort.addAll(Arrays.asList(tt09Phuluc01Sort.split(",")));

        List<TablePhuluc01Form> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        params.put("services", service);
        params.put("code", code);
        params.put("customerType", customerType);
        List<Object[]> result = query("htc_tt09_phuluc01.sql", params).getResultList();

        for (Object[] object : result) {
            TablePhuluc01Form form = new TablePhuluc01Form();
            form.setoCode(object[0].toString());
            form.setDoituongxetnghiem(object[1].toString());

            form.setAge0_15DuongTinh(Integer.valueOf(object[2].toString()));
            form.setAge0_15Tong(Integer.valueOf(object[3].toString()));

            form.setAge15_25DuongTinh(Integer.valueOf(object[4].toString()));
            form.setAge15_25Tong(Integer.valueOf(object[5].toString()));

            form.setAge25_49DuongTinh(Integer.valueOf(object[6].toString()));
            form.setAge25_49Tong(Integer.valueOf(object[7].toString()));

            form.setAge49_maxDuongTinh(Integer.valueOf(object[8].toString()));
            form.setAge49_maxTong(Integer.valueOf(object[9].toString()));

            form.setGenderMaleDuongTinh(Integer.valueOf(object[10].toString()));
            form.setGenderMaleTong(Integer.valueOf(object[11].toString()));

            form.setGenderFemaleDuongTinh(Integer.valueOf(object[12].toString()));
            form.setGenderFemaleTong(Integer.valueOf(object[13].toString()));

            form.setoID(Long.valueOf(object[14].toString()));
            form.setoParentID(Long.valueOf(object[15].toString()));

            form.setAge_noneDuongtinh(Integer.valueOf(object[16].toString()));
            form.setAge_noneTong(Integer.valueOf(object[17].toString()));

            //Get stt theo code được cấu hình tại application.properties
            form.setPosition(table02Sort.indexOf(object[0].toString()));
            data.add(form);
        }

        Collections.sort(data, new BeanComparator("position"));
        int index = 1;
        TablePhuluc01Form clone = null;
        try {
            clone = data.get(data.size() - 1).clone();
        } catch (Exception ex) {
        }
        for (TablePhuluc01Form item : data) {

            //Cộng giá trị con và giá trị tra p = Sum child
            for (TablePhuluc01Form chid : data) {
                if (Objects.equals(item.getoID(), chid.getoParentID())) {
                    item.setoCode(item.getoCode() + "," + chid.getoCode());
                    item.setAge0_15DuongTinh(item.getAge0_15DuongTinh() + chid.getAge0_15DuongTinh());
                    item.setAge0_15Tong(item.getAge0_15Tong() + chid.getAge0_15Tong());
                    item.setAge15_25DuongTinh(item.getAge15_25DuongTinh() + chid.getAge15_25DuongTinh());
                    item.setAge15_25Tong(item.getAge15_25Tong() + chid.getAge15_25Tong());
                    item.setAge25_49DuongTinh(item.getAge25_49DuongTinh() + chid.getAge25_49DuongTinh());
                    item.setAge25_49Tong(item.getAge25_49Tong() + chid.getAge25_49Tong());
                    item.setAge49_maxDuongTinh(item.getAge49_maxDuongTinh() + chid.getAge49_maxDuongTinh());
                    item.setAge49_maxTong(item.getAge49_maxTong() + chid.getAge49_maxTong());
                    item.setAge_noneDuongtinh(item.getAge_noneDuongtinh() + chid.getAge_noneDuongtinh());
                    item.setAge_noneTong(item.getAge_noneTong() + chid.getAge_noneTong());

                    item.setGenderFemaleDuongTinh(item.getGenderFemaleDuongTinh() + chid.getGenderFemaleDuongTinh());
                    item.setGenderFemaleTong(item.getGenderFemaleTong() + chid.getGenderFemaleTong());
                    item.setGenderMaleDuongTinh(item.getGenderMaleDuongTinh() + chid.getGenderMaleDuongTinh());
                    item.setGenderMaleTong(item.getGenderMaleTong() + chid.getGenderMaleTong());
                }
            }
            //Cộng những giá trị không được cấu hình hiển thị vào giá trị cuối cùng (thường là khác)
            if (item.getPosition() == -1) {
                if (!item.getoParentID().equals(Long.parseLong("0"))) {
                    continue;
                }
                TablePhuluc01Form lastItem = data.get(data.size() - 1);
                lastItem.setoCode(lastItem.getoCode() + "," + item.getoCode());
                lastItem.setAge0_15DuongTinh(lastItem.getAge0_15DuongTinh() + item.getAge0_15DuongTinh());
                lastItem.setAge0_15Tong(lastItem.getAge0_15Tong() + item.getAge0_15Tong());
                lastItem.setAge15_25DuongTinh(lastItem.getAge15_25DuongTinh() + item.getAge15_25DuongTinh());
                lastItem.setAge15_25Tong(lastItem.getAge15_25Tong() + item.getAge15_25Tong());
                lastItem.setAge25_49DuongTinh(lastItem.getAge25_49DuongTinh() + item.getAge25_49DuongTinh());
                lastItem.setAge25_49Tong(lastItem.getAge25_49Tong() + item.getAge25_49Tong());
                lastItem.setAge49_maxDuongTinh(lastItem.getAge49_maxDuongTinh() + item.getAge49_maxDuongTinh());
                lastItem.setAge49_maxTong(lastItem.getAge49_maxTong() + item.getAge49_maxTong());
                lastItem.setAge_noneDuongtinh(lastItem.getAge_noneDuongtinh() + item.getAge_noneDuongtinh());
                lastItem.setAge_noneTong(lastItem.getAge_noneTong() + item.getAge_noneTong());

                lastItem.setGenderFemaleDuongTinh(lastItem.getGenderFemaleDuongTinh() + item.getGenderFemaleDuongTinh());
                lastItem.setGenderFemaleTong(lastItem.getGenderFemaleTong() + item.getGenderFemaleTong());
                lastItem.setGenderMaleDuongTinh(lastItem.getGenderMaleDuongTinh() + item.getGenderMaleDuongTinh());
                lastItem.setGenderMaleTong(lastItem.getGenderMaleTong() + item.getGenderMaleTong());
            } else if (item.getoParentID().equals(Long.parseLong("0"))) {
                item.setStt(index++);
            }
        }
        if (clone != null) {
            clone.setPosition(-1);
            if (clone.getTong() > 0 || clone.getTongDuongTinh() > 0) {
                data.add(clone);
            }
        }
        return data;
    }

    /**
     * Page tổng quan, Đối tượng nguy cơ cao
     *
     * @param fromTime
     * @param toTime
     * @auth vvThành
     * @param service
     * @param siteID
     * @return
     */
    @Cacheable("getDashboardObjectGroup")
    public Map<String, Long> getDashboardObjectGroup(Long siteID, String fromTime, String toTime, List<String> service) {
        Query query = getQuery("SELECT "
                + "   p.id, "
                + "   p.parent_id, "
                + "   p.value as o, "
                + "   coalesce(v_main.c, 0) as c "
                + "FROM (SELECT "
                + "         e.object_group_id, "
                + "         count(e.id) as c "
                + "     FROM htc_visit e WHERE e.is_remove = 0 AND e.site_id = :site_id AND e.is_agree_pretest = 1"
                + "         AND e.pre_test_time BETWEEN :from_date AND :to_date "
                + "         AND e.service_id IN (:services)"
                + "         GROUP BY e.object_group_id) as v_main "
                + "RIGHT JOIN parameter p on p.code = v_main.object_group_id "
                + "WHERE p.type = 'test-object-group' "
                + "GROUP BY p.value, p.id, p.parent_id  ORDER BY p.position asc");

        query.setParameter("site_id", siteID);
        query.setParameter("from_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        query.setParameter("to_date", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        query.setParameter("services", service);

        Map<String, Long> data = new HashMap();
        List<Table02Form> items = new ArrayList<>();
        Table02Form item;
        List<Object[]> result = query.getResultList();
        for (Object[] object : result) {
            item = new Table02Form();
            item.setoID(Long.valueOf(object[0].toString()));
            item.setoParentID(Long.valueOf(object[1].toString()));
            item.setDanhMucBaoCao(object[2].toString());
            item.setNhanKetQuaTong(Integer.valueOf(object[3].toString()));
            items.add(item);
        }
        for (Table02Form sub : items) {
            if (!sub.getoParentID().equals(Long.parseLong("0"))) {
                continue;
            }
            for (Table02Form child : items) {
                if (Objects.equals(sub.getoID(), child.getoParentID())) {
                    sub.setNhanKetQuaTong(sub.getNhanKetQuaTong() + child.getNhanKetQuaTong());
                }
            }
            data.put(sub.getDanhMucBaoCao(), (long) sub.getNhanKetQuaTong());
        }
        return data;
    }

    /**
     * Page tổng quan, Lượt xét nghiệm
     *
     * @auth vvThành
     * @param siteID
     * @param fromTime
     * @param toTime
     * @param service
     * @return
     */
    @Cacheable("getDashboardVisit")
    public Map<String, List<Long>> getDashboardVisit(Long siteID, String fromTime, String toTime, List<String> service) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", service);
        List<Object[]> result = query("htc_dashboard_visit.sql", params).getResultList();
        Map<String, List<Long>> data = new HashMap();
        for (Object[] object : result) {
            String k = object[0].toString();
            data.put(k, new ArrayList<Long>());
            data.get(k).add(Long.valueOf(object[1].toString())); //Dương tính
            data.get(k).add(Long.valueOf(object[2].toString())); //Tổng ca xét nghiệm
        }
        return data;
    }

    /**
     * Danh sách ca dương tính phục vụ rà soát
     *
     * @author vvThành
     * @param parentSiteID
     * @return
     */
    @Cacheable("getTT09Review")
    public List<HtcVisitEntity> getTT09Review(Long parentSiteID) {
        Query query = getQuery("SELECT e.* FROM htc_visit as e "
                + "INNER JOIN site_path as p ON p.site_id = e.site_id "
                + "LEFT JOIN pac_patient_crawl as c on  c.source_site_id = e.site_id AND c.source_type='htc' "
                + "WHERE e.is_remove = 0 "
                + "AND p.ancestor_id = :site_id "
                + "AND c.source_site_id is null "
                + "AND e.confirm_results_id = '2'", HtcVisitEntity.class);

        query.setParameter("site_id", parentSiteID);
        List<HtcVisitEntity> result = query.getResultList();
        return result;
    }

    /**
     * Hàm lấy chỉ tiêu và số lượng thực tế theo quý của năm hiện tại
     *
     * @param siteID
     * @param confirmTestResult
     * @param service
     * @return
     */
    @Cacheable("getDashboardTarget")
    public Map<String, List<Long>> getDashboardTarget(Long siteID, Long confirmTestResult, List<String> service) {

        HashMap<String, Object> params = new HashMap<>();
        List<Object[]> result = new ArrayList<>();
        if (confirmTestResult != null) {
            params.put("siteID", siteID);
            params.put("services", service);
            params.put("confirmTestResult", confirmTestResult);
            result = query("htc_dashboard_target_positive.sql", params).getResultList();
        } else {
            params.put("siteID", siteID);
            params.put("services", service);
            result = query("htc_dashboard_target_test.sql", params).getResultList();
        }

        if (result == null) {
            return new HashMap<>();
        }

        Map<String, List<Long>> data = new HashMap();
        for (Object[] object : result) {

            if (Long.parseLong(object[0].toString()) == 0) {
                data.put("total", new ArrayList<Long>());
                data.get("total").add(Long.valueOf(object[0].toString()));
                data.get("total").add(Long.valueOf(object[1].toString()));
                data.get("total").add(Long.valueOf(object[2].toString()));
                data.get("total").add(Long.valueOf(object[3].toString()));
                data.get("total").add(Math.round(Double.valueOf(object[4].toString()) / 4)); // Trung bình chỉ tiêu  4
                data.get("total").add(Long.valueOf(object[4].toString()));   // Total target  5
                continue;
            }

            String k = object[0].toString();
            data.put(k, new ArrayList<Long>());
            data.get(k).add(Long.valueOf(object[0].toString()));// quater 0
            data.get(k).add(Long.valueOf(object[1].toString()));// year   1
            data.get(k).add(Long.valueOf(object[2].toString()));// number of test 2
            data.get(k).add(Long.valueOf(object[3].toString()));// total of year  3 
            data.get(k).add(data.get("total").get(4));          // Trung bình chỉ tiêu  4
            data.get(k).add(Long.valueOf(object[4].toString()));   // Total target  5
        }
        return data;
    }

    /**
     * Lấy số lượng trường hợp dương tính theo quý Số lượng KH chuyển gửi thành
     * công
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @param services
     * @auth TrangBN
     * @return
     */
    @Cacheable("getDashboardPositiveTransfer")
    public Map<String, List<Long>> getDashboardPositiveTransfer(Long siteID, String fromDate, String toDate, List<String> services) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromDate));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toDate));
        params.put("services", services);
        List<Object[]> result = query("htc_dashboard_positive_transfer.sql", params).getResultList();

        Map<String, List<Long>> data = new HashMap();
        for (Object[] object : result) {
            String k = object[0].toString();
            data.put(k, new ArrayList<Long>());
            // Số lượng dương tính
            data.get(k).add(Long.valueOf(object[1].toString()));
            // Số lượng khách được chuyển gửi điều trị thành công
            data.get(k).add(Long.valueOf(object[2].toString()));
        }
        return data;
    }

    /**
     * Get số lượng khách hàng dương tính theo nhóm đối tượng
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @param services
     *
     * @auth TrangBN
     * @return Map<String, Long>
     */
    @Cacheable("getDashboardPositiveObjectGroup")
    public Map<String, Long> getDashboardPositiveObjectGroup(Long siteID, String fromDate, String toDate, List<String> services) {

        Query query = getQuery("SELECT "
                + "	p.id, "
                + "	p.parent_id,  "
                + "	p.value as o, "
                + "	coalesce(v_main.c, 0) as c  "
                + " FROM "
                + "	(SELECT "
                + "       e.object_group_id, "
                + "       COUNT(e.id) as c FROM htc_visit e"
                + "	WHERE e.is_remove = 0 "
                + "       AND e.site_id = :siteID "
                + "       AND e.confirm_results_id = 2"
                + "       AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') >= :fromDate AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') <= :toDate"
                + "       AND e.service_id IN (:services)"
                + "       GROUP BY e.object_group_id) v_main RIGHT JOIN parameter p ON p.code = v_main.object_group_id "
                + "       WHERE p.type = 'test-object-group' GROUP BY p.value, p.id, p.parent_id  ORDER BY p.position asc");

        query.setParameter("services", services);
        query.setParameter("siteID", siteID);
        query.setParameter("fromDate", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromDate));
        query.setParameter("toDate", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toDate));

        Map<String, Long> data = new HashMap();
        Map<Long, List<Object>> parentData = new HashMap();
        List<Object[]> result = query.getResultList();

        for (Object[] object : result) {
            parentData.put(Long.parseLong(object[0].toString()), Arrays.asList(object[1], object[2], object[3]));
        }

        long sum = 0;
        for (Map.Entry<Long, List<Object>> entry : parentData.entrySet()) {
            if (Long.parseLong(entry.getValue().get(0).toString()) != 0) {
                sum = 0;
                // Get sum of the object's count which have same parentID
                for (Map.Entry<Long, List<Object>> entry2 : parentData.entrySet()) {
                    if (Long.parseLong(entry.getValue().get(0).toString()) == Long.parseLong(entry2.getValue().get(0).toString())) {
                        sum += Long.parseLong(entry2.getValue().get(2).toString());
                    }
                }
                data.put(parentData.get(Long.parseLong(entry.getValue().get(0).toString())).get(1).toString(), sum);
            }

            // Put data for oject has no parentID
            if (Long.parseLong(entry.getValue().get(0).toString()) == 0) {
                data.put(entry.getValue().get(1).toString(), Long.parseLong(entry.getValue().get(2).toString()));
            }
        }
        return data;
    }

    /**
     * Get data tỷ lệ dương tính theo quý từng nhớm đối tượng
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @param services
     * @param objectGroups
     * @return
     */
    @Cacheable("getDashboardObjectGroupPercent")
    public Map<String, DashboardObjectGroupPercentForm> getDashboardObjectGroupPercent(Long siteID, String fromDate, String toDate, List<String> services, List<ParameterEntity> objectGroups) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromDate));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toDate));
        params.put("site_id", siteID);
        params.put("services", services);
        // Set data from database to map
        List<Object[]> result = query("htc_dashboard_object_group_percent.sql", params).getResultList();

        Map<String, DashboardObjectGroupPercentForm> data = new HashMap();
        DashboardObjectGroupPercentForm form;
        String k;
        for (Object[] object : result) {
            for (ParameterEntity objectGroup : objectGroups) {
                k = String.format("%s-%s", objectGroup.getCode(), object[1].toString());
                if (data.getOrDefault(k, null) == null) {
                    form = new DashboardObjectGroupPercentForm();
                    form.setTime(object[1].toString());
                    form.setDuongtinh(0);
                    form.setXetnghiem(0);
                    form.setoCode(objectGroup.getCode());
                    form.setoID(objectGroup.getID());
                    form.setoParentID(objectGroup.getParentID());
                    data.put(k, form);
                }
            }

            form = new DashboardObjectGroupPercentForm();
            form.setoCode(object[0].toString());
            form.setTime(object[1].toString());
            form.setDuongtinh(Integer.valueOf(object[2].toString()));
            form.setXetnghiem(Integer.valueOf(object[3].toString()));
            form.setoID(Long.valueOf(object[4].toString()));
            form.setoParentID(Long.valueOf(object[5].toString()));

            k = String.format("%s-%s", object[0].toString(), object[1].toString());
            data.put(k, form);
        }

        for (Map.Entry<String, DashboardObjectGroupPercentForm> entry : data.entrySet()) {
            String key = entry.getKey();
            DashboardObjectGroupPercentForm item = entry.getValue();
            for (Map.Entry<String, DashboardObjectGroupPercentForm> entryChild : data.entrySet()) {
                String keyChild = entryChild.getKey();
                DashboardObjectGroupPercentForm itemChild = entryChild.getValue();
                if (Objects.equals(item.getoID(), itemChild.getoParentID()) && itemChild.getoCode().substring(0, 1).equals(item.getoCode().substring(0, 1)) && itemChild.getTime().equals(item.getTime())) {
                    data.get(key).setDuongtinh(data.get(key).getDuongtinh() + itemChild.getDuongtinh());
                    data.get(key).setXetnghiem(data.get(key).getXetnghiem() + itemChild.getXetnghiem());
                }
//                 && key.substring(0, 1).equals(keyChild.substring(0, 1))
            }
        }
        return data;
    }

    /**
     * Job findJobCanUpdateStatus
     *
     * @auth vvThanh
     * @return
     */
    public List<HtcVisitEntity> findJobCanUpdateStatus() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
        String strQuery = "SELECT * FROM htc_visit as e WHERE e.is_remove = 0"
                + " AND e.sample_sent_date is not null"
                + " AND e.site_confirm_test is not null"
                + " AND (e.confirm_results_id is null OR e.confirm_results_id = '')"
                + " AND (e.CONFIRM_TEST_STATUS NOT IN ('-1', '2', '3'))"
                + " AND (e.IS_AGREE_TEST = true)"
                //                + " AND DATE_FORMAT(e.updated_at, '%Y-%m-%d') <= :time"
                + " ORDER BY e.updated_at asc";
        Query query = getQuery(strQuery, HtcVisitEntity.class);
//        query.setParameter("time", TextUtils.formatDate(c.getTime(), FORMATDATE));
        List<HtcVisitEntity> result = query.getResultList();
        return result;
    }
}
