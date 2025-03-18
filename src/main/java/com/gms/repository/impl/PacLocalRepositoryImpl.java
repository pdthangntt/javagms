package com.gms.repository.impl;

import com.gms.entity.form.pac.PacLocalTableForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository báo cáo huyện xã
 *
 * @author TrangBN
 */
@Repository
@Transactional
public class PacLocalRepositoryImpl extends BaseRepositoryImpl {

    /**
     * Danh sách tử vong, còn sống tại huyện xã
     *
     * @param searchProvinceID
     * @param searchDistrictID
     * @param searchWardID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @param levelDisplay
     * @param fromTime
     * @param toTime
     * @param isVAAC
     * @return
     */
    public List<PacLocalTableForm> getData(List<String> searchProvinceID, List<String> searchDistrictID, List<String> searchWardID, String provinceID, String districtID, String wardID, String levelDisplay, String fromTime, String toTime, boolean isVAAC) {

        List<PacLocalTableForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("provinceID", provinceID);
        params.put("districtID", districtID);
        params.put("wardID", wardID);
        params.put("searchProvinceID", searchProvinceID.isEmpty() ? null : searchProvinceID);
        params.put("searchDistrictID", searchDistrictID.isEmpty() ? null : searchDistrictID);
        params.put("searchWardID", searchWardID.isEmpty() ? null : searchWardID);

        params.put("fromTime", fromTime);
        params.put("toTime", toTime);
        params.put("isVAAC", isVAAC);

        List<Object[]> result = query("pac/local.sql", params).getResultList();

        PacLocalTableForm localForm;
        for (Object[] objects : result) {
            localForm = new PacLocalTableForm();
            localForm.setProvinceID(objects[0] == null ? null : objects[0].toString());
            localForm.setDistrictID(objects[1] == null ? null : objects[1].toString());
            localForm.setWardID(objects[2] == null ? null : objects[2].toString());
            localForm.setNumAlive(Integer.valueOf(objects[3].toString()));
            localForm.setNumDead(Integer.valueOf(objects[4].toString()));
            localForm.setNotReviewYet(Integer.valueOf(objects[5].toString()));
            localForm.setNeedReview(Integer.valueOf(objects[6].toString()));
            localForm.setReviewed(Integer.valueOf(objects[7].toString()));
            localForm.setOutprovince(Integer.valueOf(objects[8].toString()));
            data.add(localForm);
        }
        return data;
    }
}
