package com.gms.repository.impl;

import com.gms.entity.form.pac.PacDetectHivTreatmentTableForm;
import com.gms.entity.form.pac.TableDetectHivAgeForm;
import com.gms.entity.form.pac.TableDetectHivGenderForm;
import com.gms.entity.form.pac.TableDetectHivInsuranceForm;
import com.gms.entity.form.pac.TableDetectHivObjectForm;
import com.gms.entity.form.pac.TableDetectHivTransmissionForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Báo cáo trường hợp phát hiện
 *
 * @author Admin
 */
@Repository
@Transactional
public class DetectHivRepositoryImpl extends BaseRepositoryImpl {

    /**
     * Lấy thông tin người nhiễm mới HIV theo giới tính
     *
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
     * @param patientStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param updateFrom
     * @param updateTo
     * @param job
     * @param race
     * @param testObject
     * @param statusResident
     * @param statusTreatment
     * @param transmission
     * @param isTTYT
     * @param isTYT
     * @param placeTest
     * @param facility
     * @return
     */
    public List<TableDetectHivGenderForm> getTableDetectHivGender(
            boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String updateFrom, String updateTo, String job, String race,
            String testObject, String statusResident, String statusTreatment, String transmission, boolean isTTYT, boolean isTYT, String placeTest, String facility, String hasHealthInsurance) {

        List<TableDetectHivGenderForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("updateTimeFrom", updateFrom);
        params.put("updateTimeTo", updateTo);
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);

        List<Object[]> result = query("pac/detect_hiv_gender.sql", params).getResultList();
        TableDetectHivGenderForm row;
        for (Object[] object : result) {
            row = new TableDetectHivGenderForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            row.setMale(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
            row.setFemale(object[4] == null ? 0 : Integer.parseInt(object[4].toString()));
            row.setOther(object[5] == null ? 0 : Integer.parseInt(object[5].toString()));
            data.add(row);
        }
        return data;
    }

    public List<TableDetectHivObjectForm> getTableDetectHivObject(
            boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom, String manageTimeTo, String inputTimeFrom,
            String inputTimeTo, String confirmTimeFrom, String confirmTimeTo, String updateFrom, String updateTo,
            String aidsFrom, String aidsTo, String gender, String job, String race, String testObject,
            String statusResident, String statusTreatment, String transmission, boolean isPac, String placeTest, String facility, String hasHealthInsurance) {

        List<TableDetectHivObjectForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("job", job);
        params.put("race", race);
//        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateFrom);
        params.put("updateTimeTo", updateTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("isPac", isPac);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);

        List<Object[]> result = query("pac/detect_hiv_object.sql", params).getResultList();
        TableDetectHivObjectForm row;
        for (Object[] object : result) {
            row = new TableDetectHivObjectForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            row.setNtmt(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
            row.setMd(object[4] == null ? 0 : Integer.parseInt(object[4].toString()));
            row.setMsm(object[5] == null ? 0 : Integer.parseInt(object[5].toString()));
            row.setVcbtbc(object[6] == null ? 0 : Integer.parseInt(object[6].toString()));
            row.setPnmt(object[7] == null ? 0 : Integer.parseInt(object[7].toString()));
            row.setLao(object[8] == null ? 0 : Integer.parseInt(object[8].toString()));
            row.setKhongthongtin(object[9] == null ? 0 : Integer.parseInt(object[9].toString()));
            row.setHoalieu(object[10] == null ? 0 : Integer.parseInt(object[10].toString()));
            row.setPhamnhan(object[11] == null ? 0 : Integer.parseInt(object[11].toString()));
            row.setMecon(object[12] == null ? 0 : Integer.parseInt(object[12].toString()));
            row.setNghiavu(object[13] == null ? 0 : Integer.parseInt(object[13].toString()));
            row.setKhac(object[14] == null ? 0 : Integer.parseInt(object[14].toString()));
            data.add(row);
        }
        return data;
    }

    public List<TableDetectHivTransmissionForm> getTableDetectHivTransmission(boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String updateFrom, String updateTo, String aidsFrom, String aidsTo, String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment, String transmission, boolean isTTYT, boolean isTYT, String placeTest, String facility, String hasHealthInsurance) {

        List<TableDetectHivTransmissionForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("updateTimeFrom", updateFrom);
        params.put("updateTimeTo", updateTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);

        List<Object[]> result = query("pac/detect_hiv_transmission.sql", params).getResultList();
        TableDetectHivTransmissionForm row;
        for (Object[] object : result) {
            row = new TableDetectHivTransmissionForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            row.setBlood(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
            row.setSexuality(object[4] == null ? 0 : Integer.parseInt(object[4].toString()));
            row.setMomtochild(object[5] == null ? 0 : Integer.parseInt(object[5].toString()));
            row.setUnclear(object[6] == null ? 0 : Integer.parseInt(object[6].toString()));
            row.setNoinformation(object[7] == null ? 0 : Integer.parseInt(object[7].toString()));
//            row.setSum(row.getTong());
            data.add(row);
        }
        return data;
    }

    /**
     * Lấy thông tin theo nhóm tuổi
     *
     * @param permanentProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param addressFilter
     * @param levelDisplay
     * @param patientStatus
     * @param dateFilter
     * @param manageStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param job
     * @param race
     * @param testObject
     * @param statusResident
     * @param statusTreatment
     * @param age
     * @param transmission
     * @param ageGroupName
     * @param isTTYT
     * @param isTYT
     * @return
     */
    public List<TableDetectHivAgeForm> getTableDetectHivAge(List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String provinceID, String districtID, String wardID,
            String addressFilter, String levelDisplay, String patientStatus, String dateFilter, String manageStatus,
            String manageTimeFrom, String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String updateFrom, String updateTo, String gender, String job, String race, String testObject, String statusResident,
            String statusTreatment, String age, String transmission, String ageGroupName, boolean isTTYT, boolean isTYT, String placeTest, String facility, String hasHealthInsurance) {

        if (age.equals("<15")) {
            age = "0-14";
        }
        if (age.equals(">49")) {
            age = "50-99";
        }

        String[] split = age.split("-", -1);

        List<TableDetectHivAgeForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("addressFilter", addressFilter);
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("dateFilter", dateFilter);
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("transmission", transmission); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("updateTimeFrom", updateFrom);
        params.put("updateTimeTo", updateTo);
        params.put("gender", gender);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("from1", Integer.parseInt(split[0]));
        params.put("to1", Integer.parseInt(split[1]));
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);

        List<Object[]> result = query("pac/detect_hiv_age.sql", params).getResultList();
        TableDetectHivAgeForm row;
        for (Object[] object : result) {
            row = new TableDetectHivAgeForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            switch (ageGroupName) {
                case "1":
                    row.setAge1(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
                    break;
                case "2":
                    row.setAge2(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
                    break;
                case "3":
                    row.setAge3(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
                    break;
                case "4":
                    row.setAge4(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
                    break;
                case "5":
                    row.setAge5(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
                    break;
                default:
                    break;
            }
            data.add(row);
        }
        return data;
    }

    public List<PacDetectHivTreatmentTableForm> getTableDetectHivTreatment(boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String updateTimeFrom, String updateTimeTo, String aidsFrom, String aidsTo, String gender, String job, String race,
            String testObject, String statusResident, String statusTreatment, String transmission, List<Long> siteLocals, boolean isTTYT, boolean isTYT, String placeTest, String facility, String hasHealthInsurance) {

        List<PacDetectHivTreatmentTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("gender", gender);
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);
//        params.put("siteLocals", siteLocals);

        List<Object[]> result = query("pac/detect_hiv_treatment.sql", params).getResultList();

        PacDetectHivTreatmentTableForm item;
        for (Object[] objects : result) {
            item = new PacDetectHivTreatmentTableForm();
            item.setProvinceID(objects[0] == null ? "" : objects[0].toString());
            item.setDistrictID(objects[1] == null ? "" : objects[1].toString());
            item.setWardID(objects[2] == null ? "" : objects[2].toString());
            item.setTrongtinh(objects[3] == null ? 0 : Integer.valueOf(objects[3].toString()));
            item.setNgoaitinh(objects[4] == null ? 0 : Integer.valueOf(objects[4].toString()));
            item.setChuadieutri(objects[5] == null ? 0 : Integer.valueOf(objects[5].toString()));
            item.setChodieutri(objects[6] == null ? 0 : Integer.valueOf(objects[6].toString()));
            item.setBodieutri(objects[7] == null ? 0 : Integer.valueOf(objects[7].toString()));
            item.setMatdau(objects[8] == null ? 0 : Integer.valueOf(objects[8].toString()));
            item.setTuvong(objects[9] == null ? 0 : Integer.valueOf(objects[9].toString()));
            item.setKhongthongtin(objects[10] == null ? 0 : Integer.valueOf(objects[10].toString()));
//            item.setTong(item.getTong());
            data.add(item);
        }

        return data;
    }
    
    /**
     * Lấy dữ liệu BC BHYT
     * 
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
     * @param patientStatus
     * @param manageTimeFrom
     * @param manageTimeTo
     * @param inputTimeFrom
     * @param inputTimeTo
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param aidsFrom
     * @param aidsTo
     * @param updateFrom
     * @param updateTo
     * @param job
     * @param race
     * @param testObject
     * @param statusResident
     * @param statusTreatment
     * @param transmission
     * @param isTTYT
     * @param isTYT
     * @param placeTest
     * @param facility
     * @param genderID
     * @return 
     */
    public List<TableDetectHivInsuranceForm> getTableDetectHivInsurance(
            boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom,
            String manageTimeTo, String inputTimeFrom, String inputTimeTo,
            String confirmTimeFrom, String confirmTimeTo, String aidsFrom, String aidsTo, String updateFrom, String updateTo, String job, String race,
            String testObject, String statusResident, String statusTreatment, String transmission, boolean isTTYT, boolean isTYT, String placeTest, String facility, String genderID) {

        List<TableDetectHivInsuranceForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID); //Tỉnh quản lý
        params.put("districtID", districtID); //Huyện quản lý
        params.put("wardID", wardID); //Xã quản lý
        params.put("manageStatus", manageStatus); //Danh sách cần lọc
        params.put("levelDisplay", levelDisplay); //Tiêu trí hiển thị - group by
        params.put("addressFilter", addressFilter); //Lọc theo hộ khẩu hoặc thường trú
        params.put("job", job);
        params.put("race", race);
        params.put("testObject", testObject);
        params.put("statusTreatment", statusTreatment);
        params.put("statusResident", statusResident);
        params.put("transmission", transmission);
        params.put("patientStatus", patientStatus); //Trạng thái còn sống, tử vong
        params.put("permanentProvinceID", permanentProvinceID.isEmpty() ? "-1" : permanentProvinceID);
        params.put("permanentDistrictID", permanentDistrictID.isEmpty() ? "-1" : permanentDistrictID);
        params.put("permanentWardID", permanentWardID.isEmpty() ? "-1" : permanentWardID);
        params.put("manageTimeFrom", manageTimeFrom);
        params.put("manageTimeTo", manageTimeTo);
        params.put("inputTimeFrom", inputTimeFrom);
        params.put("inputTimeTo", inputTimeTo);
        params.put("confirmTimeFrom", confirmTimeFrom);
        params.put("confirmTimeTo", confirmTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("updateTimeFrom", updateFrom);
        params.put("updateTimeTo", updateTo);
        params.put("isTTYT", isTTYT);
        params.put("isTYT", isTYT);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("genderID", genderID);

        List<Object[]> result = query("pac/detect_hiv_insurance.sql", params).getResultList();
        TableDetectHivInsuranceForm row;
        for (Object[] object : result) {
            row = new TableDetectHivInsuranceForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            row.setYes(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
            row.setNo(object[4] == null ? 0 : Integer.parseInt(object[4].toString()));
            row.setOther(object[5] == null ? 0 : Integer.parseInt(object[5].toString()));
            data.add(row);
        }
        return data;
    }
}
