package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.form.opc_arv.Mer02QuarterlyForm;
import com.gms.entity.form.opc_arv.Mer04QuarterlyForm;
import com.gms.entity.form.opc_arv.OpcMerFormAges;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TrangBN
 */
@Repository
@Transactional
public class OpcMerRepositoryImpl extends BaseRepositoryImpl {

    private static final String UNDER_ONE_AGE = "under-one-age";
    private static final String ONE_FOURTEEN = "one-fourteen";
    private static final String OVER_FIFTEEN = "over-fifteen";

    /**
     * Báo cáo MER bảng 01: Dịch vụ trước điều trị
     *
     * @auth TrangBN
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, HashMap<String, Integer>> getMerTable01(Long siteID, Date fromDate, Date toDate) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
//        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_table01.sql", params).getResultList();

        HashMap<String, HashMap<String, Integer>> data = new HashMap<>();
        for (Object[] object : result) {
            if (object[0] == null || StringUtils.isEmpty(object[0].toString()) || object[0].toString().equals(GenderEnum.NONE.getKey())) {
                continue;
            }

            if (object[0].toString().equals(GenderEnum.MALE.getKey())) {
                data.put(object[0].toString(), new HashMap<String, Integer>());
                data.get(object[0].toString()).put(UNDER_ONE_AGE, Integer.parseInt(object[1].toString()));
                data.get(object[0].toString()).put(ONE_FOURTEEN, Integer.parseInt(object[2].toString()));
                data.get(object[0].toString()).put(OVER_FIFTEEN, Integer.parseInt(object[3].toString()));
            }
        }

        if (data.get("1") == null) {
            data.put("1", new HashMap<String, Integer>());
            data.get("1").put(UNDER_ONE_AGE, 0);
            data.get("1").put(ONE_FOURTEEN, 0);
            data.get("1").put(OVER_FIFTEEN, 0);
        }
        if (data.get("2") == null) {
            data.put("2", new HashMap<String, Integer>());
            data.get("2").put(UNDER_ONE_AGE, 0);
            data.get("2").put(ONE_FOURTEEN, 0);
            data.get("2").put(OVER_FIFTEEN, 0);
        }
        return data;
    }

    /**
     * Báo cao dịch vụ ARV
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, HashMap<String, OpcMerFormAges>> getMerTable01Arv(Long siteID, Date fromDate, Date toDate) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_table01_arv.sql", params).getResultList();

        String key = "";
        String gender = "";
        HashMap<String, HashMap<String, OpcMerFormAges>> table01Arv = new HashMap<>();

        // Khởi tạo giá trị cho từng nhóm tuổi theo giới tính
        HashSet<String> keys = new HashSet<>();
        keys.add("onArv");
        keys.add("startArv");
        keys.add("reTreatment");
        keys.add("arrival");
        keys.add("moveAway");
        keys.add("dead");
        keys.add("quitArv");
        for (String obj : keys) {
            table01Arv.put(obj, new HashMap<String, OpcMerFormAges>());
            table01Arv.get(obj).put(GenderEnum.MALE.getKey(), new OpcMerFormAges());
            table01Arv.get(obj).put(GenderEnum.FEMALE.getKey(), new OpcMerFormAges());
            table01Arv.get(obj).put("total", new OpcMerFormAges());
        }

        if (result == null || result.isEmpty()) {
            return table01Arv;
        }

        OpcMerFormAges formOne = new OpcMerFormAges();
        for (Object[] objects : result) {
            if (objects == null || StringUtils.isEmpty(objects[0].toString())
                    || (!objects[1].toString().equals(GenderEnum.MALE.getKey()) && !objects[1].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }

            gender = objects[1].toString();
            key = objects[0].toString();
            keys.add(key);

            formOne = new OpcMerFormAges();
            formOne.setUnderOneAge(Integer.parseInt(objects[2].toString()));
            formOne.setOneToFourteen(Integer.parseInt(objects[3].toString()));
            formOne.setOverFifteen(Integer.parseInt(objects[4].toString()));

            if (table01Arv.containsKey(key)) {
                formOne.setSum(formOne.getSum() + table01Arv.get(key).get("total").getSum());
                table01Arv.get(key).put(gender, formOne);
                table01Arv.get(key).put("total", formOne);
            }
        }

        return table01Arv;
    }

    public HashMap<String, HashMap<String, OpcMerFormAges>> getMerTable02Arv(Long siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_table02_arv.sql", params).getResultList();
        HashMap<String, HashMap<String, OpcMerFormAges>> table02Arv = new HashMap<>();
        String key = "";
        String gender = "";
        HashSet<String> keys = new HashSet<>();

        // Khởi tạo giá trị cho từng nhóm tuổi theo giới tính
        keys.add("denominator");
        keys.add("numerator");
        for (String obj : keys) {

            table02Arv.put(obj, new HashMap<String, OpcMerFormAges>());
            table02Arv.get(obj).put(GenderEnum.MALE.getKey(), new OpcMerFormAges());
            table02Arv.get(obj).put(GenderEnum.FEMALE.getKey(), new OpcMerFormAges());
            table02Arv.get(obj).put("total", new OpcMerFormAges());
        }

        if (result == null || result.isEmpty()) {
            return table02Arv;
        }

        OpcMerFormAges formOne = new OpcMerFormAges();

        for (Object[] objects : result) {
            if (objects == null || StringUtils.isEmpty(objects[0].toString())
                    || (!objects[1].toString().equals(GenderEnum.MALE.getKey()) && !objects[1].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }

            gender = objects[1].toString();
            key = objects[0].toString();
            keys.add(key);

            formOne = new OpcMerFormAges();
            formOne.setUnderOneAge(Integer.parseInt(objects[2].toString()));
            formOne.setOneToFourteen(Integer.parseInt(objects[3].toString()));
            formOne.setOverFifteen(Integer.parseInt(objects[4].toString()));

            if (table02Arv.containsKey(key)) {
                formOne.setSum(formOne.getSum() + table02Arv.get(key).get("total").getSum());
                table02Arv.get(key).put(gender, formOne);
                table02Arv.get(key).put("total", formOne);
                continue;
            }
        }
        return table02Arv;
    }

    public HashMap<String, HashMap<String, OpcMerFormAges>> getMerTable03Quarterly(List<Long> siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_quarterly_03.sql", params).getResultList();
        HashMap<String, HashMap<String, OpcMerFormAges>> table03Quarterly = new HashMap<>();
        String key = "";
        String gender = "";
        HashSet<String> keys = new HashSet<>();

        // Khởi tạo giá trị cho từng nhóm tuổi theo giới tính
        keys.add("tong");
        keys.add("tuvong");
        keys.add("matdau");
        keys.add("duoi3thang");
        keys.add("tren3thang");
        keys.add("chuyendi");
        keys.add("botri");
        keys.add("quaylai");
        for (String obj : keys) {

            table03Quarterly.put(obj, new HashMap<String, OpcMerFormAges>());
            table03Quarterly.get(obj).put(GenderEnum.MALE.getKey(), new OpcMerFormAges());
            table03Quarterly.get(obj).put(GenderEnum.FEMALE.getKey(), new OpcMerFormAges());
            table03Quarterly.get(obj).put("total", new OpcMerFormAges());
        }

        if (result == null || result.isEmpty()) {
            return table03Quarterly;
        }

        OpcMerFormAges formOne = new OpcMerFormAges();

        for (Object[] objects : result) {
            if (objects == null || StringUtils.isEmpty(objects[0].toString())
                    || (!objects[1].toString().equals(GenderEnum.MALE.getKey()) && !objects[1].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }

            gender = objects[1].toString();
            key = objects[0].toString();
            keys.add(key);

            formOne = new OpcMerFormAges();
            formOne.setUnderOneAge(Integer.parseInt(objects[2].toString()));
            formOne.setOneToFourteen(Integer.parseInt(objects[3].toString()));
            formOne.setOverFifteen(Integer.parseInt(objects[4].toString()));

            if (table03Quarterly.containsKey(key)) {
                formOne.setSum(formOne.getSum() + table03Quarterly.get(key).get("total").getSum());
                table03Quarterly.get(key).put(gender, formOne);
                table03Quarterly.get(key).put("total", formOne);
                continue;
            }
        }
        OpcMerFormAges tongNamMatdau = new OpcMerFormAges();
        OpcMerFormAges tongNuMatdau = new OpcMerFormAges();
        OpcMerFormAges tongMatdau = new OpcMerFormAges();
        for (String obj : keys) {
            if(!"duoi3thang".equals(obj) && !"tren3thang".equals(obj)){
                continue;
            }
            tongNamMatdau.setUnderOneAge(tongNamMatdau.getUnderOneAge() + table03Quarterly.get(obj).get("1").getUnderOneAge());
            tongNamMatdau.setOneToFourteen(tongNamMatdau.getOneToFourteen() + table03Quarterly.get(obj).get("1").getOneToFourteen());
            tongNamMatdau.setOverFifteen(tongNamMatdau.getOverFifteen() + table03Quarterly.get(obj).get("1").getOverFifteen());
            
            tongNuMatdau.setUnderOneAge(tongNuMatdau.getUnderOneAge() + table03Quarterly.get(obj).get("2").getUnderOneAge());
            tongNuMatdau.setOneToFourteen(tongNuMatdau.getOneToFourteen() + table03Quarterly.get(obj).get("2").getOneToFourteen());
            tongNuMatdau.setOverFifteen(tongNuMatdau.getOverFifteen() + table03Quarterly.get(obj).get("2").getOverFifteen());
            
            tongMatdau.setSum(tongMatdau.getTotal()+ table03Quarterly.get(obj).get("total").getTotal());
        }
        table03Quarterly.get("matdau").put(GenderEnum.MALE.getKey(), tongNamMatdau);
        table03Quarterly.get("matdau").put(GenderEnum.FEMALE.getKey(), tongNuMatdau);
        table03Quarterly.get("matdau").put("total", tongMatdau);
        
        OpcMerFormAges maleTong = new OpcMerFormAges();
        OpcMerFormAges femaleTong = new OpcMerFormAges();
        OpcMerFormAges totalTong = new OpcMerFormAges();
        for (String obj : keys) {
            if("duoi3thang".equals(obj) || "tren3thang".equals(obj) ||  "quaylai".equals(obj)){
                continue;
            }
            maleTong.setUnderOneAge(maleTong.getUnderOneAge() + table03Quarterly.get(obj).get("1").getUnderOneAge());
            maleTong.setOneToFourteen(maleTong.getOneToFourteen() + table03Quarterly.get(obj).get("1").getOneToFourteen());
            maleTong.setOverFifteen(maleTong.getOverFifteen() + table03Quarterly.get(obj).get("1").getOverFifteen());
            
            femaleTong.setUnderOneAge(femaleTong.getUnderOneAge() + table03Quarterly.get(obj).get("2").getUnderOneAge());
            femaleTong.setOneToFourteen(femaleTong.getOneToFourteen() + table03Quarterly.get(obj).get("2").getOneToFourteen());
            femaleTong.setOverFifteen(femaleTong.getOverFifteen() + table03Quarterly.get(obj).get("2").getOverFifteen());
            
            totalTong.setSum(totalTong.getTotal()+ table03Quarterly.get(obj).get("total").getTotal());
            
        }
        table03Quarterly.get("tong").put(GenderEnum.MALE.getKey(), maleTong);
        table03Quarterly.get("tong").put(GenderEnum.FEMALE.getKey(), femaleTong);
        table03Quarterly.get("tong").put("total", totalTong);
        return table03Quarterly;
    }

    /**
     * Thông tin chỉ số hàng quý phần 1 tới 1.1
     *
     * @auth TrangBN
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, HashMap<String, OpcMerFormAges>> getMerTable01Quarterly(List<Long> siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_quarterly_01.sql", params).getResultList();

        String key = "";
        String gender = "";
        HashMap<String, HashMap<String, OpcMerFormAges>> table01Arv = new HashMap<>();

        // Khởi tạo giá trị cho từng nhóm tuổi theo giới tính
        HashSet<String> keys = new HashSet<>();
        keys.add("28days");
        keys.add("monthArv");
        keys.add("underThreeMonth");
        keys.add("threeToSixMonth");
        keys.add("fromSixMonth");
        for (String obj : keys) {
            table01Arv.put(obj, new HashMap<String, OpcMerFormAges>());
            table01Arv.get(obj).put(GenderEnum.MALE.getKey(), new OpcMerFormAges());
            table01Arv.get(obj).put(GenderEnum.FEMALE.getKey(), new OpcMerFormAges());
            table01Arv.get(obj).put("total", new OpcMerFormAges());
        }

        if (result == null || result.isEmpty()) {
            return table01Arv;
        }

        OpcMerFormAges formOne = new OpcMerFormAges();
        for (Object[] objects : result) {
            if (objects == null || StringUtils.isEmpty(objects[0].toString())
                    || (!objects[1].toString().equals(GenderEnum.MALE.getKey()) && !objects[1].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }

            gender = objects[1].toString();
            key = objects[0].toString();
            keys.add(key);

            formOne = new OpcMerFormAges();
            formOne.setUnderOneAge(Integer.parseInt(objects[2].toString()));
            formOne.setOneToFourteen(Integer.parseInt(objects[3].toString()));
            formOne.setOverFifteen(Integer.parseInt(objects[4].toString()));

            if (table01Arv.containsKey(key)) {
                formOne.setSum(formOne.getSum() + table01Arv.get(key).get("total").getSum());
                table01Arv.get(key).put(gender, formOne);
                table01Arv.get(key).put("total", formOne);
            }
        }

        HashMap<String, OpcMerFormAges> tong = new HashMap<>();
        tong.put("total", new OpcMerFormAges());
        tong.put(GenderEnum.MALE.getKey(), new OpcMerFormAges());
        tong.put(GenderEnum.FEMALE.getKey(), new OpcMerFormAges());

        for (Map.Entry<String, HashMap<String, OpcMerFormAges>> entry : table01Arv.entrySet()) {
            if (entry.getKey().equals("28days") || entry.getKey().equals("monthArv")) {
                continue;
            }
            tong.get(GenderEnum.MALE.getKey()).setUnderOneAge(tong.get(GenderEnum.MALE.getKey()).getUnderOneAge() + entry.getValue().get(GenderEnum.MALE.getKey()).getUnderOneAge());
            tong.get(GenderEnum.MALE.getKey()).setOneToFourteen(tong.get(GenderEnum.MALE.getKey()).getOneToFourteen() + entry.getValue().get(GenderEnum.MALE.getKey()).getOneToFourteen());
            tong.get(GenderEnum.MALE.getKey()).setOverFifteen(tong.get(GenderEnum.MALE.getKey()).getOverFifteen() + entry.getValue().get(GenderEnum.MALE.getKey()).getOverFifteen());
            tong.get(GenderEnum.FEMALE.getKey()).setUnderOneAge(tong.get(GenderEnum.FEMALE.getKey()).getUnderOneAge() + entry.getValue().get(GenderEnum.FEMALE.getKey()).getUnderOneAge());
            tong.get(GenderEnum.FEMALE.getKey()).setOneToFourteen(tong.get(GenderEnum.FEMALE.getKey()).getOneToFourteen() + entry.getValue().get(GenderEnum.FEMALE.getKey()).getOneToFourteen());
            tong.get(GenderEnum.FEMALE.getKey()).setOverFifteen(tong.get(GenderEnum.FEMALE.getKey()).getOverFifteen() + entry.getValue().get(GenderEnum.FEMALE.getKey()).getOverFifteen());
            tong.get("total").setSum(tong.get("total").getTotal() + entry.getValue().get("total").getTotal());
        }

        table01Arv.put("28days", tong);
        table01Arv.put("monthArv", tong);

        return table01Arv;
    }

    public Mer04QuarterlyForm getMerTable04Quarterly(List<Long> siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_quarterly_04.sql", params).getResultList();
        Mer04QuarterlyForm table04Quarterly = new Mer04QuarterlyForm();
        if (result == null || result.isEmpty()) {
            return table04Quarterly;
        }
        for (Object[] objects : result) {
            table04Quarterly.setNcmt(objects[0] == null ? 0 : Integer.parseInt(objects[0].toString()));
            table04Quarterly.setMsm(objects[1] == null ? 0 : Integer.parseInt(objects[1].toString()));
            table04Quarterly.setPnmd(objects[3] == null ? 0 : Integer.parseInt(objects[3].toString()));
            table04Quarterly.setPhamnhan(objects[4] == null ? 0 : Integer.parseInt(objects[4].toString()));
            table04Quarterly.setChuyengioi(objects[2] == null ? 0 : Integer.parseInt(objects[2].toString()));
            table04Quarterly.setTong(objects[5] == null ? 0 : Integer.parseInt(objects[5].toString()));
        }
        return table04Quarterly;
    }

    /**
     * Lấy thông tin phần 1_2
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Mer02QuarterlyForm getMerTable012Quarterly(List<Long> siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_quarterly_01.2.sql", params).getResultList();
        Mer02QuarterlyForm table02Quarterly = new Mer02QuarterlyForm();
        if (result == null || result.isEmpty()) {
            return table02Quarterly;
        }

        for (Object[] objects : result) {
            if (objects[0].toString().equals("1")) {
                table02Quarterly.setNcmt(Integer.parseInt(objects[1].toString()));
            }
            if (objects[0].toString().equals("3")) {
                table02Quarterly.setMsm(Integer.parseInt(objects[1].toString()));
            }
            if (objects[0].toString().equals("13")) {
                table02Quarterly.setChuyengioi(Integer.parseInt(objects[1].toString()));
            }
            if (objects[0].toString().equals("2") || objects[0].toString().equals("17")) {
                table02Quarterly.setPnmd(Integer.parseInt(objects[1].toString()));
            }
            if (objects[0].toString().equals("40")) {
                table02Quarterly.setPhamnhan(Integer.parseInt(objects[1].toString()));
            }
        }
        return table02Quarterly;
    }

    /**
     * Lấy thông tin bảng 2 BC quý
     *
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public Mer02QuarterlyForm getMerTable02Quarterly(List<Long> siteID, Date fromDate, Date toDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("opc/mer_quarterly_02.sql", params).getResultList();
        Mer02QuarterlyForm table02Quarterly = new Mer02QuarterlyForm();
        if (result == null || result.isEmpty()) {
            return table02Quarterly;
        }

        for (Object[] objects : result) {
            if (objects[0].toString().equals("normal") && objects[1].toString().equals("1")) {
                table02Quarterly.setNcmt(Integer.parseInt(objects[2].toString()));
            }
            if (objects[0].toString().equals("normal") && objects[1].toString().equals("3")) {
                table02Quarterly.setMsm(Integer.parseInt(objects[2].toString()));
            }
            if (objects[0].toString().equals("normal") && objects[1].toString().equals("13")) {
                table02Quarterly.setChuyengioi(Integer.parseInt(objects[2].toString()));
            }
            if (objects[0].toString().equals("normal") && (objects[1].toString().equals("2") || objects[1].toString().equals("17"))) {
                table02Quarterly.setPnmd(Integer.parseInt(objects[2].toString()));
            }
            if (objects[0].toString().equals("normal") && objects[1].toString().equals("40")) {
                table02Quarterly.setPhamnhan(Integer.parseInt(objects[2].toString()));
            }
            if (objects[0].toString().equals("hasChild")) {
                table02Quarterly.setHasChild(Integer.parseInt(objects[2].toString()));
            }
        }
        return table02Quarterly;
    }
}
