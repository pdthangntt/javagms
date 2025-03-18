package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.form.htc_confirm.PQMForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pdThang
 */
@Repository
@Transactional
public class HtcConfirmRepositoryImpl extends BaseRepositoryImpl {

    public List<PQMForm> getTablePQM(List<Long> sourceSiteID, String siteID, Date fromDate, Date toDate) {

        List<PQMForm> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("site", siteID);
        params.put("sourceSites", sourceSiteID == null || sourceSiteID.isEmpty() ? null : sourceSiteID);
        params.put("from_date", TextUtils.formatDate(fromDate, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(toDate, FORMATDATE));
        List<Object[]> result = query("pqm/htc_confirm.sql", params).getResultList();

        for (Object[] object : result) {
            PQMForm form = new PQMForm();
            form.setIndicator(object[0].toString());
            form.setGender(object[1].toString());
            form.setObjectGroup(object[2].toString());
            form.setAgeGroup(object[3].toString());
            form.setQuantity(Integer.valueOf(object[4].toString()));
            data.add(form);
        }
        return data;
    }

}
