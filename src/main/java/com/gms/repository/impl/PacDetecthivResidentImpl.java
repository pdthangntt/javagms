package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.form.pac.PacDetectHivResidentTableForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository báo cáo resident
 *
 * @author pdthang
 */
@Repository
@Transactional
public class PacDetecthivResidentImpl extends BaseRepositoryImpl {

    public List<PacDetectHivResidentTableForm> getTableDetectHivResident(
            boolean isVAAC, String levelDisplay, String manageStatus,
            String provinceID, String districtID, String wardID,
            List<String> permanentProvinceID, List<String> permanentDistrictID, List<String> permanentWardID,
            String addressFilter, String patientStatus, String manageTimeFrom, String manageTimeTo,
            String inputTimeFrom, String inputTimeTo, String confirmTimeFrom, String confirmTimeTo,
            String updateTimeFrom, String updateTimeTo, String aidsFrom, String aidsTo, String gender,
            String job, String race, String testObject, String statusResident, String statusTreatment,
            String transmission, boolean isPac, String placeTest, String facility, String hasHealthInsurance) {

        List<PacDetectHivResidentTableForm> data = new ArrayList<>();
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
        params.put("updateTimeFrom", updateTimeFrom);
        params.put("updateTimeTo", updateTimeTo);
        params.put("aidsFrom", aidsFrom);
        params.put("aidsTo", aidsTo);
        params.put("gender", gender);
        params.put("isPac", isPac);
        params.put("placeTest", placeTest);
        params.put("facility", facility);
        params.put("hasHealthInsurance", hasHealthInsurance);

        List<Object[]> result = query("pac/detect_hiv_resident.sql", params).getResultList();
        PacDetectHivResidentTableForm row;
        for (Object[] object : result) {
            row = new PacDetectHivResidentTableForm();
            row.setProvinceID(object[0] == null ? "" : object[0].toString());
            row.setDistrictID(object[1] == null ? "" : object[1].toString());
            row.setWardID(object[2] == null ? "" : object[2].toString());
            row.setDangODiaPhuong(object[3] == null ? 0 : Integer.parseInt(object[3].toString()));
            row.setDiLamAnXa(object[4] == null ? 0 : Integer.parseInt(object[4].toString()));
            row.setDiTrai(object[5] == null ? 0 : Integer.parseInt(object[5].toString()));
            row.setChuyenDiNoiKhac(object[6] == null ? 0 : Integer.parseInt(object[6].toString()));
            row.setChuyenTrongTinh(object[7] == null ? 0 : Integer.parseInt(object[7].toString()));
            row.setMatDau(object[8] == null ? 0 : Integer.parseInt(object[8].toString()));
            row.setChuaXacDinhDuoc(object[9] == null ? 0 : Integer.parseInt(object[9].toString()));
            row.setKhongCoThucTe(object[10] == null ? 0 : Integer.parseInt(object[10].toString()));
            row.setUnclear(object[11] == null ? 0 : Integer.parseInt(object[11].toString()));
            data.add(row);
        }
        return data;
    }
}
