package com.gms.repository.impl;

import com.gms.components.TextUtils;
import static com.gms.repository.impl.BaseRepositoryImpl.FORMATDATE;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * Tong hop bc pqm
 *
 * @author pdThang
 */
@Repository
public class PqmDataImpl extends BaseRepositoryImpl {

    //HTC
    public List<Object[]> getHTS_TST_POS(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/htc/HTS_TST_POS.sql", params).getResultList();
    }

    public List<Object[]> getHTS_TST_Recency(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/htc/HTS_TST_Recency.sql", params).getResultList();
    }

    public List<Object[]> getPOS_TO_ART(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/htc/POS_TO_ART.sql", params).getResultList();
    }

    //ARV
    public List<Object[]> getTX_New(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/TX_New.sql", params).getResultList();
    }

    public List<Object[]> getTX_CURR(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
//        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/TX_CURR.sql", params).getResultList();
    }
    public List<Object[]> getMMD(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
//        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/MMD.sql", params).getResultList();
    }
    public List<Object[]> getIIT(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/IIT.sql", params).getResultList();
    }
    public List<Object[]> getVL_detectable(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
//        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/VL_detectable.sql", params).getResultList();
    }
    public List<Object[]> getTB_PREV(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/arv/TB_PREV.sql", params).getResultList();
    }
    //prep
    public List<Object[]> getPrEP_New(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/prep/PrEP_New.sql", params).getResultList();
    }
    public List<Object[]> getPrEP_CURR(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/prep/PrEP_CURR.sql", params).getResultList();
    }
    public List<Object[]> getPrEP_3M(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/prep/PrEP_3M.sql", params).getResultList();
    }
    public List<Object[]> getPrEP_Over_90(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm-report/prep/PrEP_Over_90.sql", params).getResultList();
    }

}
