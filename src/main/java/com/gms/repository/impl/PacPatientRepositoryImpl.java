package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.constant.GenderEnum;
import com.gms.entity.constant.StatusOfTreatmentEnum;
import com.gms.entity.constant.TestObjectGroupEnum;
import com.gms.entity.constant.TransmissionEnum;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.form.pac.PacOutProvinceAgeTable;
import com.gms.entity.form.pac.PacOutProvinceGenderTable;
import com.gms.entity.form.pac.PacOutProvinceObjectTable;
import com.gms.entity.form.pac.PacOutProvinceResidentTable;
import com.gms.entity.form.pac.PacOutProvinceTransmissionTable;
import com.gms.entity.form.pac.PacOutProvinceTreatmentTable;
import com.gms.entity.form.pac.PacWardTableForm;
import com.gms.entity.form.pac.TablePacAidsForm;
import com.gms.entity.form.pac.TablePacDeadForm;
import com.gms.entity.form.pac.TablePacEarlyHivForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository pac
 *
 * @author pdThang
 */
@Repository
@Transactional
public class PacPatientRepositoryImpl extends BaseRepositoryImpl {

    /**
     * Báo cáo tử vong
     *
     * @author DSNAnh
     * @param isVAAC
     * @param levelDisplay
     * @param manageStatus
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param addressFilter
     * @param job
     * @param testObject
     * @param deathTimeFrom
     * @param deathTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param requestDeathTimeFrom
     * @param requestDeathTimeTo
     * @param updateTimeFrom
     * @param ageFrom
     * @param ageTo
     * @param updateTimeTo
     * @param statusResident
     * @param gender
     * @param statusTreatment
     * @param isTTYT
     * @param isTYT
     * @return
     */
    public List<TablePacDeadForm> getTableDead(boolean isVAAC, boolean isTTYT, boolean isTYT,
            String levelDisplay, String manageStatus, String addressFilter,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String deathTimeFrom, String deathTimeTo, String aidsFrom, String aidsTo, String requestDeathTimeFrom, String requestDeathTimeTo, String updateTimeFrom, String updateTimeTo, String ageFrom, String ageTo,
            String job, String gender, String testObject, String statusResident, String statusTreatment) {

        List<TablePacDeadForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("gender", gender);
        params.put("job", job);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("deathTimeFrom", deathTimeFrom);
        params.put("deathTimeTo", deathTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("requestDeathTimeFrom", requestDeathTimeFrom);
        params.put("requestDeathTimeTo", requestDeathTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("ageFrom", "".equals(ageFrom) ? null : Integer.parseInt(ageFrom));
        params.put("ageTo", "".equals(ageTo) ? null : Integer.parseInt(ageTo));
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);

        List<Object[]> result = query("pac/dead.sql", params).getResultList();
        TablePacDeadForm row;
        for (Object[] object : result) {
            row = new TablePacDeadForm();
            row.setProvinceID(object[0] == null ? null : object[0].toString());
            row.setDistrictID(object[1] == null ? null : object[1].toString());
            row.setWardID(object[2] == null ? null : object[2].toString());

            row.setMale(Integer.valueOf(object[3].toString()));
            row.setFemale(Integer.valueOf(object[4].toString()));
            row.setOther(Integer.valueOf(object[5].toString()));
            data.add(row);
        }
        return data;
    }

    /**
     * Lấy thông tin người nhiễm mới HIV
     *
     * @param provinceID
     * @param genderID
     * @param yearOld
     * @param job
     * @param testObject
     * @param risk
     * @param blood
     * @param statusTreatment
     * @param statusResident
     * @param fromTime
     * @param toTime
     * @return
     */
    public List<TablePacEarlyHivForm> getTableEarlyHiv(List<String> provinceID, List<String> districtID, List<String> wardID, String genderID, String yearOld, String job, String testObject, String risk, String blood, String statusTreatment, String statusResident, Date fromTime, Date toTime) {
        List<TablePacEarlyHivForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID.isEmpty() ? null : provinceID);
        params.put("districtID", districtID.isEmpty() ? null : districtID);
        params.put("wardID", wardID.isEmpty() ? null : wardID);
        params.put("gender_id", genderID);
        params.put("job", job);
        params.put("test_object", testObject);
        params.put("risk", risk);
        params.put("blood", blood);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("from_date", fromTime);
        params.put("to_date", toTime);
        params.put("yearOld", yearOld);

        List<Object[]> result = query("pac_early_hiv.sql", params).getResultList();
        TablePacEarlyHivForm row;
        for (Object[] object : result) {
            row = new TablePacEarlyHivForm();
            row.setWardID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1].toString());
            row.setMale(Integer.valueOf(object[2].toString()));
            row.setFemale(Integer.valueOf(object[3].toString()));
            row.setOther(Integer.valueOf(object[4].toString()));
            data.add(row);
        }
        return data;
    }

    /**
     * Lấy thông tin theo toạ độ hiển thị tổng quan pac
     *
     * @auth vvthành
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     */
    public Map<String, Map<String, String>> getChartGeo(String provinceID, String districtID, String wardID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("province", provinceID);
        params.put("district", districtID);
        params.put("ward", wardID);
        List<Object[]> result = query("pac_dashboard_chartgeo.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> item;
        for (Object[] object : result) {
            item = new HashMap<>();
            item.put("permanent_lat", object[1].toString());
            item.put("permanent_lng", object[2].toString());
            item.put("death_time", object[3] == null ? null : object[3].toString());
            item.put("district_id", object[4] == null ? null : object[4].toString());
            item.put("ward_id", object[5] == null ? null : object[5].toString());
            data.put(object[0].toString(), item);
        }
        return data;
    }

    /**
     * Lấy thông tin theo hiển thị tổng quan pac
     *
     * @auth pdThang
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     */
    public Map<String, String> getDataDasboard(String provinceID, String districtID, String wardID) {
        String firstDayOfYear = TextUtils.formatDate(TextUtils.getFirstDayOfYear(new Date()), "yyyy-MM-dd");
        String now = TextUtils.formatDate(new Date(), "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("province", provinceID);
        params.put("district", districtID);
        params.put("ward", wardID);
        params.put("fromDate", firstDayOfYear);
        params.put("toDate", now);

        List<Object[]> result = query("pac_dashboard_main.sql", params).getResultList();
        if (result == null) {
            return null;
        }
        Map<String, String> data = new HashMap<>();
        for (Object[] object : result) {
            if (!object[0].toString().equals("0")) {
                data.put("total", object[0].toString());
            }
            if (!object[1].toString().equals("0")) {
                data.put("numAlive", object[1].toString());
            }
            if (!object[2].toString().equals("0")) {
                data.put("numDead", object[2].toString());
            }
            if (!object[3].toString().equals("0")) {
                data.put("numTreatment", object[3].toString());
            }
        }
        return data;
    }

    /**
     * Lấy thông tin đối tượng đã kết nối cho NNQL
     *
     * @return
     */
    public Map<String, String> getPacPatientConnected() {

        HashMap<String, Object> params = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>();
        params.put("pa", 1);

        List<Object[]> result = query("pac_quanly_connected.sql", params).getResultList();
        if (result == null || result.isEmpty()) {
            resultMap.put("", "");
            return resultMap;
        }

        for (Object[] entry : result) {
            resultMap.put(entry[1] != null ? entry[1].toString() : "", entry[0] != null ? entry[0].toString() : "");
        }
        return resultMap;
    }

    public List<TablePacAidsForm> getTableAids(boolean isVAAC, boolean isTTYT, boolean isTYT,
            String levelDisplay, String manageStatus, String addressFilter,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String deathTimeFrom, String deathTimeTo, String aidsFrom, String aidsTo, String updateTimeFrom, String updateTimeTo, String requestDeathTimeFrom, String requestDeathTimeTo, String ageFrom, String ageTo,
            String job, String gender, String testObject, String statusResident, String statusTreatment) {

        List<TablePacAidsForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("gender", gender);
        params.put("job", job);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("deathTimeFrom", deathTimeFrom);
        params.put("deathTimeTo", deathTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("requestDeathTimeFrom", requestDeathTimeFrom);
        params.put("requestDeathTimeTo", requestDeathTimeTo);
        params.put("ageFrom", "".equals(ageFrom) ? null : Integer.parseInt(ageFrom));
        params.put("ageTo", "".equals(ageTo) ? null : Integer.parseInt(ageTo));
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);

        List<Object[]> result = query("pac/pac_aids.sql", params).getResultList();
        TablePacAidsForm row;
        for (Object[] object : result) {
            row = new TablePacAidsForm();
            row.setProvinceID(object[0] == null ? null : object[0].toString());
            row.setDistrictID(object[1] == null ? null : object[1].toString());
            row.setWardID(object[2] == null ? null : object[2].toString());

            row.setMale(Integer.valueOf(object[3].toString()));
            row.setFemale(Integer.valueOf(object[4].toString()));
            row.setOther(Integer.valueOf(object[5].toString()));
            
            row.setMaleDeath(Integer.valueOf(object[6].toString()));
            row.setFemaleDeath(Integer.valueOf(object[7].toString()));
            row.setOtherDeath(Integer.valueOf(object[8].toString()));
            
            row.setMaleAlive(Integer.valueOf(object[9].toString()));
            row.setFemaleAlive(Integer.valueOf(object[10].toString()));
            row.setOtherAlive(Integer.valueOf(object[11].toString()));
            data.add(row);
        }
        return data;
    }

    public List<PacWardTableForm> getWardTable(String statusDeath , String manageStatus, boolean isPac, String provinceID, String districtID, String wardID, String toTime, String gender, String testObject, String endTime) {

        List<PacWardTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

//        params.put("levelDisplay", levelDisplay);
        params.put("provinceID", provinceID);
        params.put("districtID", districtID);
        params.put("wardID", wardID);
        params.put("isPac", isPac);
        params.put("manageStatus", manageStatus);
        params.put("testObject", testObject);
        params.put("gender", gender);
        params.put("endTime", endTime);
        params.put("statusDeath", statusDeath);

        //số liệu danh sách quản lý
        List<Object[]> resultManage = query("pac_ward.sql", params).getResultList();
        List<Object[]> resultResident = query("pac_ward_resident.sql", params).getResultList();
        PacWardTableForm wardForm;
        for (Object[] objects : resultManage) {
            wardForm = new PacWardTableForm();
            wardForm.setDistrictID(objects[0] == null ? "-9" : objects[0].toString());
            wardForm.setWardID(objects[1] == null ? "-99" : objects[1].toString());
            wardForm.setHiv(Integer.valueOf(objects[2].toString()));
            wardForm.setAids(Integer.valueOf(objects[3].toString()));
            wardForm.setTv(Integer.valueOf(objects[4].toString()));
            wardForm.setUnder15(Integer.valueOf(objects[5].toString()));
            wardForm.setOver15(Integer.valueOf(objects[6].toString()));
            data.add(wardForm);
        }
        for (Object[] objects : resultResident) {
            wardForm = new PacWardTableForm();
            wardForm.setDistrictID(objects[0] == null ? "-9" : objects[0].toString());
            wardForm.setWardID(objects[1] == null ? "-99" : objects[1].toString());
            wardForm.setResident(objects[2] == null ? null : objects[2].toString());
            wardForm.setCount(Integer.valueOf(objects[3].toString()));
            data.add(wardForm);
        }

        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceGenderTable>> getDataOutprovince(List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {

        Map<String, HashMap<String, PacOutProvinceGenderTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);

        List<Object[]> result = query("pac/out_province_gender.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceGenderTable>());
            data.get(obj).put("permanent", new PacOutProvinceGenderTable());
            data.get(obj).put("notmanage", new PacOutProvinceGenderTable());
            data.get(obj).put("hasmanage", new PacOutProvinceGenderTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceGenderTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceGenderTable();
            item.setNam(Integer.parseInt(object[2].toString()));
            item.setNu(Integer.parseInt(object[3].toString()));
            item.setKhongro(Integer.parseInt(object[4].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }
        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceAgeTable>> getDataOutprovinceAge(String dateFilter, List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {
        
        Map<String, HashMap<String, PacOutProvinceAgeTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);

        List<Object[]> result = query(dateFilter.equals("hientai") ? "pac/out_province_age.sql" : "pac/out_province_age02.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceAgeTable>());
            data.get(obj).put("permanent", new PacOutProvinceAgeTable());
            data.get(obj).put("notmanage", new PacOutProvinceAgeTable());
            data.get(obj).put("hasmanage", new PacOutProvinceAgeTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceAgeTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceAgeTable();
            item.setUnder15(Integer.parseInt(object[2].toString()));
            item.setFrom15to24(Integer.parseInt(object[3].toString()));
            item.setFrom25to49(Integer.parseInt(object[4].toString()));
            item.setOver49(Integer.parseInt(object[5].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }
        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceObjectTable>> getDataOutprovinceObject(List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {

        Map<String, HashMap<String, PacOutProvinceObjectTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        //params object
        Set<String> pnmt = new HashSet<>();
        pnmt.add(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
        pnmt.add(TestObjectGroupEnum.PREGNANT_PERIOD.getKey());
        pnmt.add(TestObjectGroupEnum.GIVING_BIRTH.getKey());

        Set<String> khac = new HashSet<>();
        khac.add(TestObjectGroupEnum.PREGNANT_WOMEN.getKey());
        khac.add(TestObjectGroupEnum.PREGNANT_PERIOD.getKey());
        khac.add(TestObjectGroupEnum.GIVING_BIRTH.getKey());
        khac.add(TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
        khac.add(TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
        khac.add(TestObjectGroupEnum.MSM.getKey());
        khac.add(TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        khac.add(TestObjectGroupEnum.LAO.getKey());
        khac.add(TestObjectGroupEnum.KHONG_RO.getKey());
        //push
        params.put("ntmt", TestObjectGroupEnum.NGHIEN_TRICH_MA_TUY.getKey());
        params.put("md", TestObjectGroupEnum.PHU_NU_BAN_DAM.getKey());
        params.put("msm", TestObjectGroupEnum.MSM.getKey());
        params.put("vcbtbc", TestObjectGroupEnum.VO_CHONG_BANTINH_NGUOI_NHIEM_HIV.getKey());
        params.put("pnmt", pnmt);
        params.put("lao", TestObjectGroupEnum.LAO.getKey());
        params.put("khac", khac);
        params.put("khongthongtin", TestObjectGroupEnum.KHONG_RO.getKey());

        List<Object[]> result = query("pac/out_province_object.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceObjectTable>());
            data.get(obj).put("permanent", new PacOutProvinceObjectTable());
            data.get(obj).put("notmanage", new PacOutProvinceObjectTable());
            data.get(obj).put("hasmanage", new PacOutProvinceObjectTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceObjectTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceObjectTable();
            item.setNtmt(Integer.parseInt(object[2].toString()));
            item.setMd(Integer.parseInt(object[3].toString()));
            item.setMsm(Integer.parseInt(object[4].toString()));
            item.setVcbtbc(Integer.parseInt(object[5].toString()));
            item.setPnmt(Integer.parseInt(object[6].toString()));
            item.setLao(Integer.parseInt(object[7].toString()));
            item.setKhongthongtin(Integer.parseInt(object[8].toString()));
            item.setKhac(Integer.parseInt(object[9].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }

        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceTransmissionTable>> getDataOutprovinceTransmission(List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {

        Map<String, HashMap<String, PacOutProvinceTransmissionTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        //params object
        Set<String> blood = new HashSet<>();
        blood.add(TransmissionEnum.LQDM.getKey());
        blood.add(TransmissionEnum.NTMT.getKey());
        blood.add(TransmissionEnum.TM.getKey());

        Set<String> sexuality = new HashSet<>();
        sexuality.add(TransmissionEnum.LQDTD.getKey());
        sexuality.add(TransmissionEnum.TDKG.getKey());
        sexuality.add(TransmissionEnum.TĐG.getKey());

        Set<String> unclear = new HashSet<>();
        unclear.add(TransmissionEnum.KR.getKey());
        unclear.add(TransmissionEnum.TNNG.getKey());

        //push
        params.put("blood", blood);
        params.put("sexuality", sexuality);
        params.put("momtochild", TransmissionEnum.MTSC.getKey());
        params.put("unclear", unclear);

        List<Object[]> result = query("pac/out_province_transmission.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceTransmissionTable>());
            data.get(obj).put("permanent", new PacOutProvinceTransmissionTable());
            data.get(obj).put("notmanage", new PacOutProvinceTransmissionTable());
            data.get(obj).put("hasmanage", new PacOutProvinceTransmissionTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceTransmissionTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceTransmissionTable();

            item.setBlood(Integer.parseInt(object[2].toString()));
            item.setSexuality(Integer.parseInt(object[3].toString()));
            item.setMomtochild(Integer.parseInt(object[4].toString()));
            item.setUnclear(Integer.parseInt(object[5].toString()));
            item.setNoinformation(Integer.parseInt(object[6].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }

        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceTreatmentTable>> getDataOutprovinceTreatment(List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {

        Map<String, HashMap<String, PacOutProvinceTreatmentTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        //params object
        //params object
        Set<String> khongthongtin = new HashSet<>();
        khongthongtin.add(StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.BO_TRI.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.MAT_DAU.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.TU_VONG.getKey());
        khongthongtin.add(StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());

        //push
        params.put("dangdieutri", StatusOfTreatmentEnum.DANG_DUOC_DIEU_TRI.getKey());
        params.put("chuadieutri", StatusOfTreatmentEnum.CHUA_DIEU_TRI.getKey());
        params.put("chodieutri", StatusOfTreatmentEnum.DS_CHO_DIEU_TRI.getKey());
        params.put("bodieutri", StatusOfTreatmentEnum.BO_TRI.getKey());
        params.put("matdau", StatusOfTreatmentEnum.MAT_DAU.getKey());
        params.put("tuvong", StatusOfTreatmentEnum.TU_VONG.getKey());
        params.put("chuacothongtin", StatusOfTreatmentEnum.CHUA_CO_THONG_TIN.getKey());
        params.put("khongthongtin", khongthongtin);

        List<Object[]> result = query("pac/out_province_treatment.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceTreatmentTable>());
            data.get(obj).put("permanent", new PacOutProvinceTreatmentTable());
            data.get(obj).put("notmanage", new PacOutProvinceTreatmentTable());
            data.get(obj).put("hasmanage", new PacOutProvinceTreatmentTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceTreatmentTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceTreatmentTable();

            item.setDangdieutri(Integer.parseInt(object[2].toString()));
            item.setChuadieutri(Integer.parseInt(object[3].toString()));
            item.setChodieutri(Integer.parseInt(object[4].toString()));
            item.setBodieutri(Integer.parseInt(object[5].toString()));
            item.setMatdau(Integer.parseInt(object[6].toString()));
            item.setTuvong(Integer.parseInt(object[7].toString()));
            item.setChuacothongtin(Integer.parseInt(object[8].toString()));
            item.setKhongthongtin(Integer.parseInt(object[9].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }

        return data;
    }

    public Map<String, HashMap<String, PacOutProvinceResidentTable>> getDataOutprovinceResident(List<String> provinceIDs,
            String updateTimeFrom, String updateTimeTo,
            String patientStatus,
            String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo,
            String aidsFrom, String aidsTo,
            String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment,
            String transmission, String placeTest, String facility) {

        Map<String, HashMap<String, PacOutProvinceResidentTable>> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("placeTest", placeTest);
        params.put("facility", facility);

        List<Object[]> result = query("pac/out_province_resident.sql", params).getResultList();

        for (String obj : provinceIDs) {
            data.put(obj, new HashMap<String, PacOutProvinceResidentTable>());
            data.get(obj).put("permanent", new PacOutProvinceResidentTable());
            data.get(obj).put("notmanage", new PacOutProvinceResidentTable());
            data.get(obj).put("hasmanage", new PacOutProvinceResidentTable());
        }

        if (result == null || result.isEmpty()) {
            return data;
        }

        PacOutProvinceResidentTable item;
        for (Object[] object : result) {
            if (object[0] == null || object[1] == null) {
                continue;
            }
            item = new PacOutProvinceResidentTable();

            item.setDangODiaPhuong(Integer.parseInt(object[2].toString()));
            item.setDiLamAnXa(Integer.parseInt(object[3].toString()));
            item.setDiTrai(Integer.parseInt(object[4].toString()));
            item.setChuyenDiNoiKhac(Integer.parseInt(object[5].toString()));
            item.setChuyenTrongTinh(Integer.parseInt(object[6].toString()));
            item.setMatDau(Integer.parseInt(object[7].toString()));
            item.setChuaXacDinhDuoc(Integer.parseInt(object[8].toString()));
            item.setKhongCoThucTe(Integer.parseInt(object[9].toString()));
            item.setUnclear(Integer.parseInt(object[10].toString()));

            data.get(object[1].toString()).put(object[0].toString(), item);

        }

        return data;
    }

}
