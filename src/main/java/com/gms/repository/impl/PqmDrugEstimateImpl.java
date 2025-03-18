package com.gms.repository.impl;

import com.gms.components.TextUtils;
import com.gms.entity.form.laytest.LaytestTable02Form;
import com.gms.entity.form.report.PqmDrugPlanForm;
import static com.gms.repository.impl.BaseRepositoryImpl.FORMATDATE;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author othoa
 */
@Repository
public class PqmDrugEstimateImpl extends BaseRepositoryImpl {

    public List<Object[]> getDrugEstimate(int year, int quarter, String provinceID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("quarter", quarter);
        params.put("province_id", provinceID);
        return query("pqm-report/drug/pqm_drug_estimate.sql", params).getResultList();
    }

}
