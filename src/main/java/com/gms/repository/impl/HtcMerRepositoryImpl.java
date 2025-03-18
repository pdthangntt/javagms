package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.EarlyHivResultEnum;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.form.htc.MerTable01Form;
import com.gms.entity.form.htc.MerTable02Form;
import com.gms.entity.form.htc.MerTable03Form;
import com.gms.entity.form.htc.MerTable04Form;
import com.gms.entity.form.htc.MerTable05Form;
import static com.gms.repository.impl.BaseRepositoryImpl.FORMATDATE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class HtcMerRepositoryImpl extends BaseRepositoryImpl {

    private final String INTRODUCED_STATUS = "introduced";
    private final String AGREED_STATUS = "agreed";

    /**
     * Bảng 01: Số bạn tình bạn chích được giới thiệu và đồng ý xét nghiệm qua
     * kênh "Theo dấu bạn tình bạn chích"
     *
     * @param siteID
     * @param serviceID
     * @param fromTime
     * @param toTime
     * @param objects
     * @return
     */
    public HashMap<String, HashMap<String, MerTable01Form>> getMerTable01(Long siteID, List<String> serviceID, String fromTime, String toTime, List<String> objects) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", serviceID);
        params.put("objects", objects);
        params.put("siteID", siteID);
        List<Object[]> resultList = query("htc_mer_table01.sql", params).getResultList();

        MerTable01Form merTable01Form;
        List<MerTable01Form> result = new ArrayList<>();
        for (Object[] obj : resultList) {
            merTable01Form = new MerTable01Form();
            merTable01Form.setStatus(String.valueOf(obj[0]));
            merTable01Form.setServiceID(String.valueOf(obj[1]));
            merTable01Form.setGenderID(String.valueOf(obj[2]));
            merTable01Form.setAgreePreTest(String.valueOf(obj[3]));
            merTable01Form.setAdvisoryTime(String.valueOf(obj[4]));
            merTable01Form.setPreTestTime(String.valueOf(obj[5]));
            merTable01Form.setUnDefinedAge(Integer.parseInt(obj[6].toString()));
            merTable01Form.setUnderOneAge(Integer.parseInt(obj[7].toString()));
            merTable01Form.setOneToFour(Integer.parseInt(obj[8].toString()));
            merTable01Form.setFiveToNine(Integer.parseInt(obj[9].toString()));
            merTable01Form.setTenToFourteen(Integer.parseInt(obj[10].toString()));
            merTable01Form.setFifteenToNineteen(Integer.parseInt(obj[11].toString()));
            merTable01Form.setTwentyToTwentyFour(Integer.parseInt(obj[12].toString()));
            merTable01Form.setTwentyFiveToTwentyNine(Integer.parseInt(obj[13].toString()));
            merTable01Form.setThirtyToThirtyFour(Integer.parseInt(obj[14].toString()));
            merTable01Form.setThirtyFiveToThirtyNine(Integer.parseInt(obj[15].toString()));
            merTable01Form.setFortyToFortyFour(Integer.parseInt(obj[16].toString()));
            merTable01Form.setFortyFiveToFortyNine(Integer.parseInt(obj[17].toString()));
            merTable01Form.setOverFifty(Integer.parseInt(obj[18].toString()));
            result.add(merTable01Form);
        }

        LinkedHashMap<String, HashMap<String, MerTable01Form>> hashMapResult = new LinkedHashMap<>();
        if (result.isEmpty()) {
            return null;
        }

        // Set data
        hashMapResult.put("result", new HashMap<String, MerTable01Form>() );
        for (MerTable01Form table01 : result) {
            addToRowTable01(hashMapResult.get("result"), table01);
        }
        return hashMapResult;
    }

    /**
     * Bảng 02 MER
     *
     * @auth Đào Anh
     * @param siteID
     * @param serviceID
     * @param fromTime
     * @param toTime
     * @param objects
     * @return
     */
    public HashMap<String, HashMap<String, MerTable02Form>> getMerTable02(Long siteID, List<String> serviceID, String fromTime, String toTime, List<String> objects) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", serviceID);
        params.put("objects", objects);
        params.put("siteID", siteID);
        List<Object[]> resultList = query("htc_mer_table02.sql", params).getResultList();

        MerTable02Form merTable02Form;
        List<MerTable02Form> result = new ArrayList<>();
        for (Object[] obj : resultList) {
            merTable02Form = new MerTable02Form();
            merTable02Form.setSiteID(String.valueOf(obj[0]));
            merTable02Form.setServiceID(String.valueOf(obj[1]));
            merTable02Form.setGenderID(String.valueOf(obj[2]));
            merTable02Form.setEarlyHiv(String.valueOf(obj[3]));
            merTable02Form.setUnDefinedAge(Integer.parseInt(obj[4].toString()));
            merTable02Form.setUnderFifteen(Integer.parseInt(obj[5].toString()));
            merTable02Form.setFifteenToNineteen(Integer.parseInt(obj[6].toString()));
            merTable02Form.setTwentyToTwentyFour(Integer.parseInt(obj[7].toString()));
            merTable02Form.setTwentyFiveToTwentyNine(Integer.parseInt(obj[8].toString()));
            merTable02Form.setThirtyToThirtyFour(Integer.parseInt(obj[9].toString()));
            merTable02Form.setThirtyFiveToThirtyNine(Integer.parseInt(obj[10].toString()));
            merTable02Form.setFortyToFortyFour(Integer.parseInt(obj[11].toString()));
            merTable02Form.setFortyFiveToFortyNine(Integer.parseInt(obj[12].toString()));
            merTable02Form.setOverFifty(Integer.parseInt(obj[13].toString()));
            result.add(merTable02Form);
        }

        LinkedHashMap<String, HashMap<String, MerTable02Form>> hashMapResult = new LinkedHashMap<>();
        
        if (!result.isEmpty()) {
            // Thêm các record có dịch vụ không null
            for (MerTable02Form table02 : result) {

                // Thêm các record có dịch vụ không null
                String key = table02.getServiceID();
                if (hashMapResult.get(key) != null) {
                    addToRowTable02(hashMapResult.get(key), table02);
                    continue;
                }
                hashMapResult.put(key, new HashMap<String, MerTable02Form>());
                addToRowTable02(hashMapResult.get(key), table02);
            }
        }

        // Bổ sung đủ các dịch vụ đã tìm kiếm
        LinkedHashMap<String, HashMap<String, MerTable02Form>> sortedResult = new LinkedHashMap<>();
        LinkedHashMap<String, HashMap<String, MerTable02Form>> sortedResultSum = new LinkedHashMap<>();
        sortedResultSum.put("tong", new HashMap<String, MerTable02Form>());
        sortedResultSum.get("tong").put("duongtinh-nam", new MerTable02Form());
        sortedResultSum.get("tong").put("duongtinh-nu", new MerTable02Form());
        sortedResultSum.get("tong").put("amtinh-nam", new MerTable02Form());
        sortedResultSum.get("tong").put("amtinh-nu", new MerTable02Form());
        
        boolean flag;
        HashMap<String, MerTable02Form> sum = new HashMap<>();
        
        for (String service : serviceID) {
                flag = false;
                String key = service;
                for (Map.Entry<String, HashMap<String, MerTable02Form>> entry : hashMapResult.entrySet()) {
                    if (entry.getKey().equals(key)) {
                        sortedResult.put(key, entry.getValue());
                        flag = true;

                        // Tính tổng
                        if (entry.getValue().containsKey("duongtinh-nam")) {
                            getSum02("duongtinh-nam", sum, entry, sortedResultSum);
                        }
                        if (entry.getValue().containsKey("duongtinh-nu")) {
                            getSum02("duongtinh-nu", sum, entry, sortedResultSum);
                        }
                        if (entry.getValue().containsKey("amtinh-nam")) {
                            getSum02("amtinh-nam", sum, entry, sortedResultSum);
                        }
                        if (entry.getValue().containsKey("amtinh-nu")) {
                            getSum02("amtinh-nu", sum, entry, sortedResultSum);
                        }
                    }
                }

                if (flag) {
                    continue;
                }

                if (sortedResult.get(key) == null) {
                    sortedResult.put(key, new HashMap<String, MerTable02Form>());
                    addToRowTable02(sortedResult.get(key), null);
                }
        }
        
        // Thêm tổng
        sortedResult.put("tong", sortedResultSum.get("tong"));
        
        return sortedResult;
    }

    /**
     * Lấy giá trị bảng 03
     *
     * @param siteID
     * @param serviceID
     * @param fromTime
     * @param toTime
     * @param objects
     * @return
     */
    public HashMap<String, HashMap<String, MerTable03Form>> getMerTable03(Long siteID, List<String> serviceID, String fromTime, String toTime, List<String> objects) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", serviceID);
        params.put("objects", objects);
        params.put("siteID", siteID);

        List<Object[]> resultList = query("htc_mer_table03.sql", params).getResultList();
        MerTable03Form merTable03Form;
        HashMap<String, MerTable03Form> result = new HashMap<>();

        String number;
        String objectGroup;
        String key;

        for (Object[] obj : resultList) {

            merTable03Form = new MerTable03Form();
            merTable03Form.setServiceID(String.valueOf(obj[0]));
            merTable03Form.setObjectGroupID(String.valueOf(obj[1]));
            merTable03Form.setEarlyHiv(String.valueOf(obj[4]));
            number = EarlyHivResultEnum.OVER_TWELVE_MONTH.getKey().equals(merTable03Form.getEarlyHiv()) ? String.valueOf(obj[3]) : EarlyHivResultEnum.LESS_EQUAL_SIX_MONTH.getKey().equals(merTable03Form.getEarlyHiv()) ? String.valueOf(obj[2]) : "0";
            objectGroup = String.valueOf(obj[1]);

            key = String.valueOf(obj[0]) + "-" + String.valueOf(obj[4]) + "-" + String.valueOf(obj[1]);

            if (objectGroup.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())) {
                merTable03Form.setNcmt(Integer.parseInt(number));
            } else if (objectGroup.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())) {
                merTable03Form.setPhuNuBanDam(Integer.parseInt(number));
            } else if (objectGroup.equals(TestObjectGroupEnum.MSM.getKey())) {
                merTable03Form.setMsm(Integer.parseInt(number));
            } else if (objectGroup.equals(TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey())) {
                merTable03Form.setChuyenGioi(Integer.parseInt(number));
            } else if (objectGroup.equals(TestObjectGroupEnum.PHAM_NHAN.getKey())) {
                merTable03Form.setPhamNhan(Integer.parseInt(number));
            } else {

                key = String.valueOf(obj[0]) + "-" + String.valueOf(obj[4]) + "-khac";
                if (result.containsKey(key)) {
                    result.get(key).setKhac(result.get(key).getKhac() + Integer.parseInt(number));
                    continue;
                } else {
                    merTable03Form.setKhac(Integer.parseInt(number));
                    result.put(key, merTable03Form);
                    continue;
                }
            }
            result.put(key, merTable03Form);

        }

        LinkedHashMap<String, HashMap<String, MerTable03Form>> hashMapResult = new LinkedHashMap<>();

        // Set dữ liệu cho các dịch vụ có dữ liệu
        for (Map.Entry<String, MerTable03Form> entry : result.entrySet()) {
            String k = entry.getValue().getServiceID();
            if (hashMapResult.get(k) != null) {
                addToRowTable03(hashMapResult.get(k), entry.getValue());
                continue;
            }
            hashMapResult.put(k, new HashMap<String, MerTable03Form>());
            addToRowTable03(hashMapResult.get(k), entry.getValue());
        }

        // Bổ sung đủ các dịch vụ đã tìm kiếm cho kết quả, sắp xếp lại các dịch vụ
        LinkedHashMap<String, HashMap<String, MerTable03Form>> sortedResult = new LinkedHashMap<>();
        LinkedHashMap<String, HashMap<String, MerTable03Form>> sortedResultSum = new LinkedHashMap<>();
        sortedResultSum.put("tong", new HashMap<String, MerTable03Form>());
        sortedResultSum.get("tong").put("nho6", new MerTable03Form());
        sortedResultSum.get("tong").put("lon12", new MerTable03Form());
        
        boolean flag;
        HashMap<String, MerTable03Form> sum;
        
        for (String service : serviceID) {
            flag = false;
            for (Map.Entry<String, HashMap<String, MerTable03Form>> entry : hashMapResult.entrySet()) {
                if (entry.getKey().equals(service)) {
                    sortedResult.put(service, entry.getValue());
                    flag = true;
                    
                    // Tính tổng
                    sum = sortedResultSum.get("tong");
                    if (sum.containsKey("nho6")) {
                        sum.get("nho6").setChuyenGioi(sum.get("nho6").getChuyenGioi() +  ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getChuyenGioi(): 0));
                        sum.get("nho6").setMsm(sum.get("nho6").getMsm() +  ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getMsm(): 0));
                        sum.get("nho6").setNcmt(sum.get("nho6").getNcmt() + ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getNcmt(): 0));
                        sum.get("nho6").setPhuNuBanDam(sum.get("nho6").getPhuNuBanDam() + ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getPhuNuBanDam():0));
                        sum.get("nho6").setPhamNhan(sum.get("nho6").getPhamNhan() + ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getPhamNhan():0));
                        sum.get("nho6").setKhac(sum.get("nho6").getKhac() + ( entry.getValue().get("nho6") != null ? entry.getValue().get("nho6").getKhac():0));
                        sortedResultSum.get("tong").put("nho6", sum.get("nho6"));
                    }
                    
                    if (sum.containsKey("lon12")) {
                        sum.get("lon12").setChuyenGioi(sum.get("lon12").getChuyenGioi() + ( entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getChuyenGioi():0));
                        sum.get("lon12").setMsm(sum.get("lon12").getMsm() + (entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getMsm():0));
                        sum.get("lon12").setNcmt(sum.get("lon12").getNcmt() + (entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getNcmt():0));
                        sum.get("lon12").setPhuNuBanDam(sum.get("lon12").getPhuNuBanDam() + (entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getPhuNuBanDam():0));
                        sum.get("lon12").setPhamNhan(sum.get("lon12").getPhamNhan() + (entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getPhamNhan():0));
                        sum.get("lon12").setKhac(sum.get("lon12").getKhac() + (entry.getValue().get("lon12") != null ? entry.getValue().get("lon12").getKhac():0));
                        sortedResultSum.get("tong").put("lon12", sum.get("lon12"));
                    }
                    
                }
            }

            if (flag) {
                continue;
            }

            if (sortedResult.get(service) == null) {
                sortedResult.put(service, new HashMap<String, MerTable03Form>());
                addToRowTable03(sortedResult.get(service), null);
            }
        }

        // Thêm tổng
        sortedResult.put("tong", sortedResultSum.get("tong"));
                
        return sortedResult;
    }
    
    /**
     * Lấy thông tin bảng 04 báo cáo MER
     *
     * @param siteID
     * @param serviceID
     * @param fromTime
     * @param toTime
     * @param objects
     * @return
     */
    public HashMap<String, HashMap<String, MerTable04Form>> getMerTable04(Long siteID, List<String> serviceID, String fromTime, String toTime, List<String> objects) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", serviceID);
        params.put("objects", objects);
        params.put("siteID", siteID);
        List<Object[]> resultList = query("htc_mer_table04.sql", params).getResultList();

        MerTable04Form merTable04Form;
        List<MerTable04Form> result = new ArrayList<>();
        for (Object[] obj : resultList) {
            merTable04Form = new MerTable04Form();
            merTable04Form.setDistrictID(String.valueOf(obj[0]));
            merTable04Form.setServiceID(String.valueOf(obj[1]));
            merTable04Form.setGenderID(String.valueOf(obj[2]));
            merTable04Form.setConfirmResultsID(String.valueOf(obj[3]));
            merTable04Form.setUnDefinedAge(Integer.parseInt(obj[4].toString()));
            merTable04Form.setUnderOneAge(Integer.parseInt(obj[5].toString()));
            merTable04Form.setOneToFour(Integer.parseInt(obj[6].toString()));
            merTable04Form.setFiveToNine(Integer.parseInt(obj[7].toString()));
            merTable04Form.setTenToFourteen(Integer.parseInt(obj[8].toString()));
            merTable04Form.setFifteenToNineteen(Integer.parseInt(obj[9].toString()));
            merTable04Form.setTwentyToTwentyFour(Integer.parseInt(obj[10].toString()));
            merTable04Form.setTwentyFiveToTwentyNine(Integer.parseInt(obj[11].toString()));
            merTable04Form.setThirtyToThirtyFour(Integer.parseInt(obj[12].toString()));
            merTable04Form.setThirtyFiveToThirtyNine(Integer.parseInt(obj[13].toString()));
            merTable04Form.setFortyToFortyFour(Integer.parseInt(obj[14].toString()));
            merTable04Form.setFortyFiveToFortyNine(Integer.parseInt(obj[15].toString()));
            merTable04Form.setOverFifty(Integer.parseInt(obj[16].toString()));
            merTable04Form.setOverFifty(Integer.parseInt(obj[16].toString()));
            result.add(merTable04Form);
        }

        LinkedHashMap<String, HashMap<String, MerTable04Form>> hashMapResult = new LinkedHashMap<>();
        if (!result.isEmpty()) {
            // Thêm các record có dịch vụ không null
            for (MerTable04Form table04 : result) {

                // Thêm các record có dịch vụ không null
                String key = table04.getServiceID();
                if (hashMapResult.get(key) != null) {
                    addToRowTable04(hashMapResult.get(key), table04);
                    continue;
                }
                hashMapResult.put(key, new HashMap<String, MerTable04Form>());
                addToRowTable04(hashMapResult.get(key), table04);
            }
        }

        // Bổ sung đủ các dịch vụ đã tìm kiếm
        LinkedHashMap<String, HashMap<String, MerTable04Form>> sortedResult = new LinkedHashMap<>();
        LinkedHashMap<String, HashMap<String, MerTable04Form>> sortedResultSum = new LinkedHashMap<>();
        sortedResultSum.put("tong", new HashMap<String, MerTable04Form>());
        sortedResultSum.get("tong").put("duongtinh-nam", new MerTable04Form());
        sortedResultSum.get("tong").put("duongtinh-nu", new MerTable04Form());
        sortedResultSum.get("tong").put("amtinh-nam", new MerTable04Form());
        sortedResultSum.get("tong").put("amtinh-nu", new MerTable04Form());
        
        boolean flag;
        HashMap<String, MerTable04Form> sum = new HashMap<>();
        
        for (String service : serviceID) {
            flag = false;
            for (Map.Entry<String, HashMap<String, MerTable04Form>> entry : hashMapResult.entrySet()) {
                if (entry.getKey().equals(service)) {
                    sortedResult.put(service, entry.getValue());
                    flag = true;
                    
                    // Tính tổng
                    if (entry.getValue().containsKey("duongtinh-nam")) {
                        getSum04("duongtinh-nam", sum, entry, sortedResultSum);
                    }
                    if (entry.getValue().containsKey("duongtinh-nu")) {
                        getSum04("duongtinh-nu", sum, entry, sortedResultSum);
                    }
                    if (entry.getValue().containsKey("amtinh-nam")) {
                        getSum04("amtinh-nam", sum, entry, sortedResultSum);
                    }
                    if (entry.getValue().containsKey("amtinh-nu")) {
                        getSum04("amtinh-nu", sum, entry, sortedResultSum);
                    }
                }
            }

            if (flag) {
                continue;
            }

            if (sortedResult.get(service) == null) {
                sortedResult.put(service, new HashMap<String, MerTable04Form>());
                addToRowTable04(sortedResult.get(service), null);
            }
        }

        // Thêm tổng
        sortedResult.put("tong", sortedResultSum.get("tong"));
        return sortedResult;
    }
    
    /**
     * Lấy thông tin bảng 05 báo cáo MER
     *
     * @param siteID
     * @param serviceID
     * @param fromTime
     * @param toTime
     * @param objects
     * @return
     */
//    @Cacheable("getMerTable05")
    public HashMap<String, HashMap<String, MerTable05Form>> getMerTable05(Long siteID, List<String> serviceID, String fromTime, String toTime, List<String> objects) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("from_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, fromTime));
        params.put("to_time", TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, toTime));
        params.put("services", serviceID);
        params.put("objects", objects);
        params.put("siteID", siteID);

        List<Object[]> resultList = query("htc_mer_table05.sql", params).getResultList();
        MerTable05Form merTable05Form;
        HashMap<String, MerTable05Form> result = new HashMap<>();

        String number;
        String objectGroup;
        String key;

        for (Object[] obj : resultList) {

            merTable05Form = new MerTable05Form();
            merTable05Form.setServiceID(String.valueOf(obj[0]));
            merTable05Form.setConfirmResultsID(String.valueOf(obj[4]));
            number = ConfirmTestResultEnum.DUONG_TINH.equals(merTable05Form.getConfirmResultsID()) ? String.valueOf(obj[3]) : ConfirmTestResultEnum.AM_TINH.equals(merTable05Form.getConfirmResultsID()) ? String.valueOf(obj[2]) : "0";
            objectGroup = String.valueOf(obj[1]);

            key = String.valueOf(obj[0]) + "-" + String.valueOf(obj[4]) + "-" + String.valueOf(obj[1]);

            if (objectGroup.equals(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey())) {
                merTable05Form.setNcmt(Integer.parseInt(number));
            } else 
            if (objectGroup.equals(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey())) {
                merTable05Form.setPhuNuBanDam(Integer.parseInt(number));
            } else
            if (objectGroup.equals(TestObjectGroupEnum.MSM.getKey())) {
                merTable05Form.setMsm(Integer.parseInt(number));
            } else
            if (objectGroup.equals(TestObjectGroupEnum.NGUOI_CHUYEN_GIOI.getKey())) {
                merTable05Form.setChuyenGioi(Integer.parseInt(number));
            } else
            if (objectGroup.equals(TestObjectGroupEnum.PHAM_NHAN.getKey())) {
                merTable05Form.setPhamNhan(Integer.parseInt(number));
            } else {
                
                key = String.valueOf(obj[0]) + "-" + String.valueOf(obj[4]) + "-khac";
                if (result.containsKey(key)) {
                    result.get(key).setKhac(result.get(key).getKhac() + Integer.parseInt(number));
                    continue;
                } else {
                    merTable05Form.setKhac(Integer.parseInt(number));
                    result.put(key, merTable05Form);
                    continue;
                }
            }
                result.put(key, merTable05Form);
        }

        LinkedHashMap<String, HashMap<String, MerTable05Form>> hashMapResult = new LinkedHashMap<>();

        // Set dữ liệu cho các dịch vụ có dữ liệu
        for (Map.Entry<String, MerTable05Form> entry : result.entrySet()) {
            String k = entry.getValue().getServiceID();
            if (hashMapResult.get(k) != null) {
                addToRowTable05(hashMapResult.get(k), entry.getValue());
                continue;
            }
            hashMapResult.put(k, new HashMap<String, MerTable05Form>());
            addToRowTable05(hashMapResult.get(k), entry.getValue());
        }

        // Bổ sung đủ các dịch vụ đã tìm kiếm cho kết quả, sắp xếp lại các dịch vụ
        LinkedHashMap<String, HashMap<String, MerTable05Form>> sortedResult = new LinkedHashMap<>();
        LinkedHashMap<String, HashMap<String, MerTable05Form>> sortedResultSum = new LinkedHashMap<>();
        sortedResultSum.put("tong", new HashMap<String, MerTable05Form>());
        sortedResultSum.get("tong").put("duongtinh", new MerTable05Form());
        sortedResultSum.get("tong").put("amtinh", new MerTable05Form());
        
        boolean flag;
        HashMap<String, MerTable05Form> sum;
        
        for (String service : serviceID) {
            flag = false;
            for (Map.Entry<String, HashMap<String, MerTable05Form>> entry : hashMapResult.entrySet()) {
                if (entry.getKey().equals(service)) {
                    sortedResult.put(service, entry.getValue());
                    flag = true;
                    
                    // Tính tổng
                    sum = sortedResultSum.get("tong");
                    if (sum.containsKey("duongtinh")) {
                        sum.get("duongtinh").setChuyenGioi(sum.get("duongtinh").getChuyenGioi() +  ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getChuyenGioi(): 0));
                        sum.get("duongtinh").setMsm(sum.get("duongtinh").getMsm() +  ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getMsm(): 0));
                        sum.get("duongtinh").setNcmt(sum.get("duongtinh").getNcmt() + ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getNcmt(): 0));
                        sum.get("duongtinh").setPhuNuBanDam(sum.get("duongtinh").getPhuNuBanDam() + ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getPhuNuBanDam():0));
                        sum.get("duongtinh").setPhamNhan(sum.get("duongtinh").getPhamNhan() + ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getPhamNhan():0));
                        sum.get("duongtinh").setKhac(sum.get("duongtinh").getKhac() + ( entry.getValue().get("duongtinh") != null ? entry.getValue().get("duongtinh").getKhac():0));
                        sortedResultSum.get("tong").put("duongtinh", sum.get("duongtinh"));
                    }
                    
                    if (sum.containsKey("amtinh")) {
                        sum.get("amtinh").setChuyenGioi(sum.get("amtinh").getChuyenGioi() + ( entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getChuyenGioi():0));
                        sum.get("amtinh").setMsm(sum.get("amtinh").getMsm() + (entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getMsm():0));
                        sum.get("amtinh").setNcmt(sum.get("amtinh").getNcmt() + (entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getNcmt():0));
                        sum.get("amtinh").setPhuNuBanDam(sum.get("amtinh").getPhuNuBanDam() + (entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getPhuNuBanDam():0));
                        sum.get("amtinh").setPhamNhan(sum.get("amtinh").getPhamNhan() + (entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getPhamNhan():0));
                        sum.get("amtinh").setKhac(sum.get("amtinh").getKhac() + (entry.getValue().get("amtinh") != null ? entry.getValue().get("amtinh").getKhac():0));
                        sortedResultSum.get("tong").put("amtinh", sum.get("amtinh"));
                    }
                }
            }

            if (flag) {
                continue;
            }

            if (sortedResult.get(service) == null) {
                sortedResult.put(service, new HashMap<String, MerTable05Form>());
                addToRowTable05(sortedResult.get(service), null);
            }
        }

        // Thêm tổng
        sortedResult.put("tong", sortedResultSum.get("tong"));

        return sortedResult;
    }

    /**
     * Thêm vào các nhóm đối tượng trong bảng MerTable01
     *
     * @param map
     * @param form
     */
    public void addToRowTable01(HashMap<String, MerTable01Form> map, MerTable01Form form) {

        if (form == null) {
            map.put("introduced-nam", form);
            map.put("introduced-nu", form);
            map.put("agreed-nam", form);
            map.put("agreed-nu", form);
            return;
        }

        // Nam được giới thiệu
        if (INTRODUCED_STATUS.equals(form.getStatus()) && GenderEnum.MALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl01(map, form, "introduced-nam");
        }
        // Nữ được giới thiệu
        if (INTRODUCED_STATUS.equals(form.getStatus()) && GenderEnum.FEMALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl01(map, form, "introduced-nu");
        }
        // Đồng ý xét nghiệm nữ
        if (AGREED_STATUS.equals(form.getStatus()) && GenderEnum.MALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl01(map, form, "agreed-nam");
        }
        // Đồng ý xét nghiệm nữ
        if (AGREED_STATUS.equals(form.getStatus()) && GenderEnum.FEMALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl01(map, form, "agreed-nu");
        }
    }

    /**
     * Thêm data bảng 02
     *
     * @param map
     * @param form
     */
    public void addToRowTable02(HashMap<String, MerTable02Form> map, MerTable02Form form) {

        if (form == null) {
            map.put("duongtinh-nam", form);
            map.put("duongtinh-nu", form);
            map.put("amtinh-nam", form);
            map.put("amtinh-nu", form);
            return;
        }

        // Thêm vào nhóm dương tính nam
        if (EarlyHivResultEnum.LESS_EQUAL_SIX_MONTH.getKey().equals(form.getEarlyHiv()) && "1".equals(form.getGenderID())) {
            map.put("duongtinh-nam", form);
        }
        // Thêm vào nhóm dương tính nữ
        if (EarlyHivResultEnum.LESS_EQUAL_SIX_MONTH.getKey().equals(form.getEarlyHiv()) && "2".equals(form.getGenderID())) {
            map.put("duongtinh-nu", form);
        }
        // Thêm vào nhóm âm tính nam
        if (EarlyHivResultEnum.OVER_TWELVE_MONTH.getKey().equals(form.getEarlyHiv()) && "1".equals(form.getGenderID())) {
            map.put("amtinh-nam", form);
        }
        // Thêm vào nhóm âm tính nu
        if (EarlyHivResultEnum.OVER_TWELVE_MONTH.getKey().equals(form.getEarlyHiv()) && "2".equals(form.getGenderID())) {
            map.put("amtinh-nu", form);
        }

    }

    /**
     * Thêm data bảng 03
     *
     * @param map
     * @param form
     */
    public void addToRowTable03(HashMap<String, MerTable03Form> map, MerTable03Form form) {

        if (form == null) {
            map.put("nho6", null);
            map.put("lon12", null);
            return;
        }

        // Thêm vào nhóm dương tính
        if (EarlyHivResultEnum.LESS_EQUAL_SIX_MONTH.getKey().equals(form.getEarlyHiv())) {
            MerTable03Form tblDuong = map.get("nho6");
            if (tblDuong != null) {
                map.get("nho6").setChuyenGioi(tblDuong.getChuyenGioi() == 0 ? form.getChuyenGioi() : tblDuong.getChuyenGioi());
                map.get("nho6").setMsm(tblDuong.getMsm() == 0 ? form.getMsm() : tblDuong.getMsm());
                map.get("nho6").setNcmt(tblDuong.getNcmt() == 0 ? form.getNcmt() : tblDuong.getNcmt());
                map.get("nho6").setPhuNuBanDam(tblDuong.getPhuNuBanDam() == 0 ? form.getPhuNuBanDam() : tblDuong.getPhuNuBanDam());
                map.get("nho6").setPhamNhan(tblDuong.getPhamNhan() == 0 ? form.getPhamNhan() : tblDuong.getPhamNhan());
                map.get("nho6").setKhac(tblDuong.getKhac() == 0 ? form.getKhac() : tblDuong.getKhac());
            } else {
                map.put("nho6", form);
            }

        }
        // Thêm vào nhóm âm tính
        if (EarlyHivResultEnum.OVER_TWELVE_MONTH.getKey().equals(form.getEarlyHiv())) {
            MerTable03Form tblAm = map.get("lon12");
            if (tblAm != null) {
                map.get("lon12").setChuyenGioi(tblAm.getChuyenGioi() == 0 ? form.getChuyenGioi() : tblAm.getChuyenGioi());
                map.get("lon12").setMsm(tblAm.getMsm() == 0 ? form.getMsm() : tblAm.getMsm());
                map.get("lon12").setNcmt(tblAm.getNcmt() == 0 ? form.getNcmt() : tblAm.getNcmt());
                map.get("lon12").setPhuNuBanDam(tblAm.getPhuNuBanDam() == 0 ? form.getPhuNuBanDam() : tblAm.getPhuNuBanDam());
                map.get("lon12").setPhamNhan(tblAm.getPhamNhan() == 0 ? form.getPhamNhan() : tblAm.getPhamNhan());
                map.get("lon12").setKhac(tblAm.getKhac() == 0 ? form.getKhac() : tblAm.getKhac());
            } else {
                map.put("lon12", form);
            }
        }
    }
    
    /**
     * Thêm vào các nhóm đối tượng trong bảng MerTable04
     *
     * @param map
     * @param form
     */
    public void addToRowTable04(HashMap<String, MerTable04Form> map, MerTable04Form form) {

        if (form == null) {
            map.put("duongtinh-nam", form);
            map.put("duongtinh-nu", form);
            map.put("amtinh-nam", form);
            map.put("amtinh-nu", form);
            return;
        }

        // Thêm vào nhóm dương tính nam
        if (ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID()) && GenderEnum.MALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl04(map, form, "duongtinh-nam");
        }
        // Thêm vào nhóm dương tính nữ
        if (ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID()) && GenderEnum.FEMALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl04(map, form, "duongtinh-nu");
        }
        // Thêm vào nhóm âm tính nam
        if (ConfirmTestResultEnum.AM_TINH.equals(form.getConfirmResultsID()) && GenderEnum.MALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl04(map, form, "amtinh-nam");
        }
        // Thêm vào nhóm âm tính nu
        if (ConfirmTestResultEnum.AM_TINH.equals(form.getConfirmResultsID()) && GenderEnum.FEMALE.getKey().equals(form.getGenderID())) {
            setResultAgeTbl04(map, form, "amtinh-nu");
        }
    }
    
    /**
     * Thêm dữ liệu vào bảng báo cáo MER 05
     *
     * @param map
     * @param form
     */
    public void addToRowTable05(HashMap<String, MerTable05Form> map, MerTable05Form form) {

        if (form == null) {
            map.put("duongtinh", null);
            map.put("amtinh", null);
            return;
        }

        // Thêm vào nhóm dương tính
        if (ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID())) {
            MerTable05Form tblDuong = map.get("duongtinh");
            
            if (tblDuong != null) {
                map.get("duongtinh").setChuyenGioi(tblDuong.getChuyenGioi() == 0 ? form.getChuyenGioi() : tblDuong.getChuyenGioi());
                map.get("duongtinh").setMsm(tblDuong.getMsm() == 0 ? form.getMsm() : tblDuong.getMsm());
                map.get("duongtinh").setNcmt(tblDuong.getNcmt() == 0 ? form.getNcmt() : tblDuong.getNcmt());
                map.get("duongtinh").setPhuNuBanDam(tblDuong.getPhuNuBanDam() == 0 ? form.getPhuNuBanDam() : tblDuong.getPhuNuBanDam());
                map.get("duongtinh").setPhamNhan(tblDuong.getPhamNhan() == 0 ? form.getPhamNhan() : tblDuong.getPhamNhan());
                map.get("duongtinh").setKhac(tblDuong.getKhac() == 0 ? form.getKhac() : tblDuong.getKhac());
            } else {
                map.put("duongtinh", form);
            }

        }
        // Thêm vào nhóm âm tính
        if (ConfirmTestResultEnum.AM_TINH.equals(form.getConfirmResultsID())) {
            MerTable05Form tblAm = map.get("amtinh");
            if (tblAm != null) {
                map.get("amtinh").setChuyenGioi(tblAm.getChuyenGioi() == 0 ? form.getChuyenGioi() : tblAm.getChuyenGioi());
                map.get("amtinh").setMsm(tblAm.getMsm() == 0 ? form.getMsm() : tblAm.getMsm());
                map.get("amtinh").setNcmt(tblAm.getNcmt() == 0 ? form.getNcmt() : tblAm.getNcmt());
                map.get("amtinh").setPhuNuBanDam(tblAm.getPhuNuBanDam() == 0 ? form.getPhuNuBanDam() : tblAm.getPhuNuBanDam());
                map.get("amtinh").setPhamNhan(tblAm.getPhamNhan() == 0 ? form.getPhamNhan() : tblAm.getPhamNhan());
                map.get("amtinh").setKhac(tblAm.getKhac() == 0 ? form.getKhac() : tblAm.getKhac());
            } else {
                map.put("amtinh", form);
            }
        }
    }

    /**
     * Set data cho nhóm tuổi
     *
     * @param map
     * @param form
     * @param key
     */
    private void setResultAgeTbl01(HashMap<String, MerTable01Form> map, MerTable01Form form, String key) {

        MerTable01Form tblResult = map.get(key);
        if (tblResult != null) {
            map.get(key).setUnDefinedAge(tblResult.getUnDefinedAge() == 0 ? form.getUnDefinedAge() : tblResult.getUnDefinedAge());
            map.get(key).setUnderOneAge(tblResult.getUnderOneAge() == 0 ? form.getUnderOneAge() : tblResult.getUnderOneAge());
            map.get(key).setOneToFour(tblResult.getOneToFour() == 0 ? form.getOneToFour() : tblResult.getOneToFour());
            map.get(key).setFiveToNine(tblResult.getFiveToNine() == 0 ? form.getFiveToNine() : tblResult.getFiveToNine());
            map.get(key).setTenToFourteen(tblResult.getTenToFourteen() == 0 ? form.getTenToFourteen() : tblResult.getTenToFourteen());
            map.get(key).setFifteenToNineteen(tblResult.getFifteenToNineteen() == 0 ? form.getFifteenToNineteen() : tblResult.getFifteenToNineteen());
            map.get(key).setTwentyToTwentyFour(tblResult.getTwentyToTwentyFour() == 0 ? form.getTwentyToTwentyFour() : tblResult.getTwentyToTwentyFour());
            map.get(key).setTwentyFiveToTwentyNine(tblResult.getTwentyFiveToTwentyNine() == 0 ? form.getTwentyFiveToTwentyNine() : tblResult.getTwentyFiveToTwentyNine());
            map.get(key).setThirtyToThirtyFour(tblResult.getThirtyToThirtyFour() == 0 ? form.getThirtyToThirtyFour() : tblResult.getThirtyToThirtyFour());
            map.get(key).setThirtyFiveToThirtyNine(tblResult.getThirtyFiveToThirtyNine() == 0 ? form.getThirtyFiveToThirtyNine() : tblResult.getThirtyFiveToThirtyNine());
            map.get(key).setFortyToFortyFour(tblResult.getFortyToFortyFour() == 0 ? form.getFortyToFortyFour() : tblResult.getFortyToFortyFour());
            map.get(key).setFortyFiveToFortyNine(tblResult.getFortyFiveToFortyNine() == 0 ? form.getFortyFiveToFortyNine() : tblResult.getFortyFiveToFortyNine());
            map.get(key).setOverFifty(tblResult.getOverFifty() == 0 ? form.getOverFifty() : tblResult.getOverFifty());
        } else {
            map.put(key, form);
        }
    }

    /**
     * Set data cho nhóm tuổi
     * 
     * @param map
     * @param form
     * @param key 
     */
    private void setResultAgeTbl04(HashMap<String, MerTable04Form> map, MerTable04Form form, String key) {

        MerTable04Form tblResult = map.get(key);
        if (tblResult != null) {
            map.get(key).setUnDefinedAge(tblResult.getUnDefinedAge() == 0 ? form.getUnDefinedAge() : tblResult.getUnDefinedAge());
            map.get(key).setUnderOneAge(tblResult.getUnderOneAge() == 0 ? form.getUnderOneAge() : tblResult.getUnderOneAge());
            map.get(key).setOneToFour(tblResult.getOneToFour() == 0 ? form.getOneToFour() : tblResult.getOneToFour());
            map.get(key).setFiveToNine(tblResult.getFiveToNine() == 0 ? form.getFiveToNine() : tblResult.getFiveToNine());
            map.get(key).setTenToFourteen(tblResult.getTenToFourteen() == 0 ? form.getTenToFourteen() : tblResult.getTenToFourteen());
            map.get(key).setFifteenToNineteen(tblResult.getFifteenToNineteen() == 0 ? form.getFifteenToNineteen() : tblResult.getFifteenToNineteen());
            map.get(key).setTwentyToTwentyFour(tblResult.getTwentyToTwentyFour() == 0 ? form.getTwentyToTwentyFour() : tblResult.getTwentyToTwentyFour());
            map.get(key).setTwentyFiveToTwentyNine(tblResult.getTwentyFiveToTwentyNine() == 0 ? form.getTwentyFiveToTwentyNine() : tblResult.getTwentyFiveToTwentyNine());
            map.get(key).setThirtyToThirtyFour(tblResult.getThirtyToThirtyFour() == 0 ? form.getThirtyToThirtyFour() : tblResult.getThirtyToThirtyFour());
            map.get(key).setThirtyFiveToThirtyNine(tblResult.getThirtyFiveToThirtyNine() == 0 ? form.getThirtyFiveToThirtyNine() : tblResult.getThirtyFiveToThirtyNine());
            map.get(key).setFortyToFortyFour(tblResult.getFortyToFortyFour() == 0 ? form.getFortyToFortyFour() : tblResult.getFortyToFortyFour());
            map.get(key).setFortyFiveToFortyNine(tblResult.getFortyFiveToFortyNine() == 0 ? form.getFortyFiveToFortyNine() : tblResult.getFortyFiveToFortyNine());
            map.get(key).setOverFifty(tblResult.getOverFifty() == 0 ? form.getOverFifty() : tblResult.getOverFifty());
        } else {
            map.put(key, form);
        }
    }
    
    /**
     * Tính tổng bang 04
     * 
     * @param sum
     * @param entry 
     */
    private void getSum04(String key, HashMap<String, MerTable04Form> sum, Map.Entry<String, HashMap<String, MerTable04Form>> entry, LinkedHashMap<String, HashMap<String, MerTable04Form>> sortedResult ) {    
        sum = sortedResult.get("tong");
        if (sum.containsKey(key)) {
            sum.get(key).setUnDefinedAge(sum.get(key).getUnDefinedAge() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getUnDefinedAge() : 0));
            sum.get(key).setUnderOneAge(sum.get(key).getUnderOneAge() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getUnderOneAge() : 0));
            sum.get(key).setOneToFour(sum.get(key).getOneToFour() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getOneToFour() : 0));
            sum.get(key).setFiveToNine(sum.get(key).getFiveToNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFiveToNine() : 0));
            sum.get(key).setTenToFourteen(sum.get(key).getTenToFourteen() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getTenToFourteen() : 0));
            sum.get(key).setFifteenToNineteen(sum.get(key).getFifteenToNineteen() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFifteenToNineteen() : 0));
            sum.get(key).setTwentyToTwentyFour(sum.get(key).getTwentyToTwentyFour() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getTwentyToTwentyFour() : 0));
            sum.get(key).setTwentyFiveToTwentyNine(sum.get(key).getTwentyFiveToTwentyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getTwentyFiveToTwentyNine() : 0));
            sum.get(key).setThirtyToThirtyFour(sum.get(key).getThirtyToThirtyFour() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getThirtyToThirtyFour() : 0));
            sum.get(key).setThirtyFiveToThirtyNine(sum.get(key).getThirtyFiveToThirtyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getThirtyFiveToThirtyNine() : 0));
            sum.get(key).setFortyToFortyFour(sum.get(key).getFortyToFortyFour() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFortyToFortyFour() : 0));
            sum.get(key).setFortyFiveToFortyNine(sum.get(key).getFortyFiveToFortyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFortyFiveToFortyNine() : 0));
            sum.get(key).setOverFifty(sum.get(key).getOverFifty() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getOverFifty() : 0));
            sortedResult.get("tong").put(key, sum.get(key));
        }
    }
    
    /**
     * Tính tổng bang 02
     * 
     * @param sum
     * @param entry 
     */
    private void getSum02(String key, HashMap<String, MerTable02Form> sum, Map.Entry<String, HashMap<String, MerTable02Form>> entry, LinkedHashMap<String, HashMap<String, MerTable02Form>> sortedResult ) {    
        sum = sortedResult.get("tong");
        if (sum.containsKey(key)) {
            sum.get(key).setUnDefinedAge(sum.get(key).getUnDefinedAge() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getUnDefinedAge() : 0));
            sum.get(key).setUnderFifteen(sum.get(key).getUnderFifteen()+ (entry.getValue().get(key) != null ? entry.getValue().get(key).getUnderFifteen(): 0));
            sum.get(key).setFifteenToNineteen(sum.get(key).getFifteenToNineteen() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFifteenToNineteen(): 0));
            sum.get(key).setTwentyToTwentyFour(sum.get(key).getTwentyToTwentyFour()+ (entry.getValue().get(key) != null ? entry.getValue().get(key).getTwentyToTwentyFour(): 0));
            sum.get(key).setTwentyFiveToTwentyNine(sum.get(key).getTwentyFiveToTwentyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getTwentyFiveToTwentyNine(): 0));
            sum.get(key).setThirtyToThirtyFour(sum.get(key).getThirtyToThirtyFour()+ (entry.getValue().get(key) != null ? entry.getValue().get(key).getThirtyToThirtyFour(): 0));
            sum.get(key).setThirtyFiveToThirtyNine(sum.get(key).getThirtyFiveToThirtyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getThirtyFiveToThirtyNine() : 0));
            sum.get(key).setFortyToFortyFour(sum.get(key).getFortyToFortyFour() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFortyToFortyFour() : 0));
            sum.get(key).setFortyFiveToFortyNine(sum.get(key).getFortyFiveToFortyNine() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getFortyFiveToFortyNine() : 0));
            sum.get(key).setOverFifty(sum.get(key).getOverFifty() + (entry.getValue().get(key) != null ? entry.getValue().get(key).getOverFifty() : 0));
            sortedResult.get("tong").put(key, sum.get(key));
        }
    }
}
