package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.form.opc_arv.OpcThuantapAgesForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TrangBN
 */
@Repository
@Transactional
public class OpcThuanTapRepositoryImpl extends BaseRepositoryImpl {

    /**
     * Báo cáo MER bảng 01: Dịch vụ trước điều trị
     *
     * @param year
     * @param quarter
     * @auth TrangBN
     * @param siteID
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, HashMap<String, OpcThuantapAgesForm>> getTable(List<Long> siteID, Date fromDate, Date toDate, int year, int quarter) {
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", siteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        params.put("year", year);
        params.put("quarter", quarter);
        List<Object[]> result = query("opc/thuan_tap_ct1.sql", params).getResultList();
        String key = "";

        // Khởi tạo giá trị ban đầu
        HashMap<String, HashMap<String, OpcThuantapAgesForm>> data = new HashMap<>();
        List<Integer> years = new ArrayList<>();
        years.add(year);
        years.add(year+1);
                
        HashSet<String> keys = new HashSet<>();
        keys.add("ct1");
        keys.add("ct2");
        keys.add("ct3");
        keys.add("ct4");
        keys.add("ct5");
        keys.add("ct6");
        keys.add("ct7");
        keys.add("ct8");
        
        String keyColumn = "";
        for (String obj : keys) {
            data.put(obj, new HashMap<String, OpcThuantapAgesForm>());
            for (Integer y : years) {
                keyColumn = String.format("%s-%s-%s", String.valueOf(quarter), String.valueOf(y), GenderEnum.MALE.getKey());
                data.get(obj).put(keyColumn, new OpcThuantapAgesForm());
                keyColumn = String.format("%s-%s-%s", String.valueOf(quarter), String.valueOf(y), GenderEnum.FEMALE.getKey());
                data.get(obj).put(keyColumn, new OpcThuantapAgesForm());
            }
        }
        if (result == null || result.isEmpty()) {
            return data;
        }
        
        String keyCol = "";
        OpcThuantapAgesForm ttAge = new OpcThuantapAgesForm();
        for (Object[] objects : result) {
            
            if (objects[3] == null || StringUtils.isEmpty(objects[3].toString()) 
                    || (!objects[3].toString().equals(GenderEnum.MALE.getKey()) && !objects[3].toString().equals(GenderEnum.FEMALE.getKey()))) {
                continue;
            }
            
            key = objects[0].toString();
            keyCol = String.format("%s-%s-%s", objects[2].toString(),objects[1].toString(), objects[3].toString());
            ttAge = new OpcThuantapAgesForm();
            ttAge.setUnderFifteen(Integer.parseInt(objects[4].toString()));
            ttAge.setOverFifteen(Integer.parseInt(objects[5].toString()));
            data.get(key).put(keyCol, ttAge);
        }
        
        // Set thông tin chỉ tiêu 4
        data.put("ct4", getCt4(data.get("ct1"), data.get("ct2"), data.get("ct3"), year, quarter));
        return data;
    }
    
    /**
     * Tính giá trị cho CT4
     * 
     * @param ct1
     * @param ct2
     * @param ct3
     * @param year
     * @return 
     */
    private HashMap<String, OpcThuantapAgesForm> getCt4(HashMap<String, OpcThuantapAgesForm> ct1, HashMap<String, OpcThuantapAgesForm> ct2, HashMap<String, OpcThuantapAgesForm> ct3, int year, int quarter) {
        HashMap<String, OpcThuantapAgesForm> hSrcCt4 = (HashMap<String, OpcThuantapAgesForm>)ct1.clone();
        
        OpcThuantapAgesForm srcCt1 = new OpcThuantapAgesForm();
        OpcThuantapAgesForm srcCt2 = new OpcThuantapAgesForm();
        OpcThuantapAgesForm srcCt3 = new OpcThuantapAgesForm();
        
        HashMap<String, OpcThuantapAgesForm> hSrcCt1 = new HashMap<>();
        HashMap<String, OpcThuantapAgesForm> hSrcCt2 = new HashMap<>();
        HashMap<String, OpcThuantapAgesForm> hSrcCt3 = new HashMap<>();
        
        for (Map.Entry<String, OpcThuantapAgesForm> entry : ct1.entrySet()) {
            srcCt1 = new OpcThuantapAgesForm();
            if (entry.getKey().contains(String.format("%s-%s", year, "1"))) {
                srcCt1.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt1.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt1.put(String.format("%s-%s", year, "1"), srcCt1);
            }
            if (entry.getKey().contains(String.format("%s-%s", year, "2"))) {
                srcCt1.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt1.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt1.put(String.format("%s-%s", year, "2"), srcCt1);
            }
        }
        for (Map.Entry<String, OpcThuantapAgesForm> entry : ct2.entrySet()) {
            srcCt2 = new OpcThuantapAgesForm();
            if (entry.getKey().contains(String.format("%s-%s", year + 1, "1"))) {
                srcCt2.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt2.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt2.put(String.format("%s-%s", year + 1, "1"), srcCt2);
            }
            if (entry.getKey().contains(String.format("%s-%s", year + 1, "2"))) {
                srcCt2.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt2.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt2.put(String.format("%s-%s", year + 1, "2"), srcCt2);
            }
        }
        for (Map.Entry<String, OpcThuantapAgesForm> entry : ct3.entrySet()) {
            srcCt3 = new OpcThuantapAgesForm();
            if (entry.getKey().contains(String.format("%s-%s", year + 1, "1"))) {
                srcCt3.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt3.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt3.put(String.format("%s-%s", year + 1, "1"), srcCt3);
            }
            if (entry.getKey().contains(String.format("%s-%s", year + 1, "2"))) {
                srcCt3.setUnderFifteen(entry.getValue().getUnderFifteen());
                srcCt3.setOverFifteen(entry.getValue().getOverFifteen());
                hSrcCt3.put(String.format("%s-%s", year + 1, "2"), srcCt3);
            }
        }
        
        OpcThuantapAgesForm maleCt1 = hSrcCt1.get(String.format("%s-%s", year, "1"));
        OpcThuantapAgesForm femaleCt1 = hSrcCt1.get(String.format("%s-%s", year, "2"));
        OpcThuantapAgesForm maleCt2 = hSrcCt2.get(String.format("%s-%s", year + 1, "1"));
        OpcThuantapAgesForm femaleCt2 = hSrcCt2.get(String.format("%s-%s", year + 1, "2"));
        OpcThuantapAgesForm maleCt3 = hSrcCt3.get(String.format("%s-%s", year + 1, "1"));
        OpcThuantapAgesForm femaleCt3 = hSrcCt3.get(String.format("%s-%s", year + 1, "2"));
        
        OpcThuantapAgesForm maleCt4 = new OpcThuantapAgesForm();
        OpcThuantapAgesForm femaleCt4 = new OpcThuantapAgesForm();
        maleCt4.setUnderFifteen(maleCt1.getUnderFifteen() + maleCt2.getUnderFifteen() - maleCt3.getUnderFifteen());
        maleCt4.setOverFifteen(maleCt1.getOverFifteen() + maleCt2.getOverFifteen() - maleCt3.getOverFifteen());
        femaleCt4.setUnderFifteen(femaleCt1.getUnderFifteen() + femaleCt2.getUnderFifteen() - femaleCt3.getUnderFifteen());
        femaleCt4.setOverFifteen(femaleCt1.getOverFifteen() + femaleCt2.getOverFifteen() - femaleCt3.getOverFifteen());
        
        hSrcCt4.put(String.format("%s-%s-%s", quarter, year + 1, "1"), maleCt4);
        hSrcCt4.put(String.format("%s-%s-%s", quarter, year + 1, "2"), femaleCt4);
        
        return hSrcCt4;
    }
}
