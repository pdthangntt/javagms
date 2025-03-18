package com.gms.repository.impl;

import com.gms.components.TextUtils;
import static com.gms.repository.impl.BaseRepositoryImpl.FORMATDATE;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * Tổng quan: phần htc
 *
 * @author vvthanh
 */
@Repository
public class PqmImpl extends BaseRepositoryImpl {

    public List<Object[]> getHTS_TST_POS(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/hts_tst_pos.sql", params).getResultList();
    }
    
    public List<Object[]> getHTS_TST_RECENCY(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/hts_tst_recency.sql", params).getResultList();
    }
    public List<Object[]> getPOS_TO_ART(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/pos_to_art.sql", params).getResultList();
    }
    public List<Object[]> getIMS_TST_POS(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/ims_tst_pos.sql", params).getResultList();
    }
    public List<Object[]> getIMS_POS_ART(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/ims_pos_art.sql", params).getResultList();
    }
    public List<Object[]> getIMS_TST_RECENCY(Set<Long> site, Date startTime, Date endTime) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("site_id", site);
        params.put("from_date", TextUtils.formatDate(startTime, FORMATDATE));
        params.put("to_date", TextUtils.formatDate(endTime, FORMATDATE));
        return query("pqm/ims_tst_recency.sql", params).getResultList();
    }

}
