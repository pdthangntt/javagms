package com.gms.repository.impl;

import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.form.laytest.LaytestTable02Form;
import com.gms.entity.form.opc_arv.PqmeLMISETable;
import com.gms.entity.form.report.PqmDrugPlanForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author othoa
 */
@Repository
public class PqmDrugPlanImpl extends BaseRepositoryImpl {

    public List<PqmDrugPlanForm> getDataDrugPlanData(Integer year, Integer month, String province_id) {
        Integer month_d1, month_d2, month_d3;
        month_d1 = month_d2 = month_d3 = 0;
        Integer year_d = year;
        if (month <= 3) {
            month_d1 = 10;
            month_d2 = 11;
            month_d3 = 12;
            year_d = year - 1;
        } else if (month > 3 && month <= 6) {
            month_d1 = 1;
            month_d2 = 2;
            month_d3 = 3;
        } else if (month > 6 && month <= 9) {
            month_d1 = 4;
            month_d2 = 5;
            month_d3 = 6;
        } else if (month > 9 && month <= 12) {
            month_d1 = 7;
            month_d2 = 8;
            month_d3 = 9;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
//        if (!StringUtils.isEmpty(site_code)) {
//            params.put("site_code", site_code);
//        }
        params.put("province_id", province_id);
        params.put("month_d1", month_d1);
        params.put("month_d2", month_d2);
        params.put("month_d3", month_d3);
        params.put("year_d", year_d);
        List<Object[]> result = query("pqm-report/drug/pqm_drug_plan.sql", params).getResultList();
        System.out.println("resultresultresult: " + result.size());
        List<PqmDrugPlanForm> listRs = new LinkedList<PqmDrugPlanForm>();
        for (Object[] objects : result) {
            PqmDrugPlanForm form = new PqmDrugPlanForm();
            form.setDrugName(objects[0].toString());
            form.setUnit(objects[1].toString());
            form.setTotalEndPeriod(objects[2] != null ? Long.valueOf(objects[2].toString()) : 0);
            form.setD1(objects[3] != null ? Long.valueOf(objects[3].toString()) : 0);
            form.setD2(objects[4] != null ? Long.valueOf(objects[4].toString()) : 0);
            form.setD3(objects[5] != null ? Long.valueOf(objects[5].toString()) : 0);
            form.setSiteCode(objects[6].toString());
            form.setSiteName(objects[7].toString());
            listRs.add(form);
        }
        return listRs;
    }

    public Map<String, String> getAllSites(Integer year, Integer month, String province_id) {
        Map<String, String> sites = new HashMap<>();
        Query query = getQuery("SELECT "
                + "   p.site_code, "
                + "   p.site_name  "
                + "FROM pqm_drug_plan p where p.year = :year and p.month = :month and p.current_province = :province_id");

        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("province_id", province_id);
        List<Object[]> result = query.getResultList();
        for (Object[] object : result) {
            sites.put(object[0].toString(), object[1].toString());
        }
        return sites;
    }

    public List<PqmeLMISETable> getARV_2MoS(Integer quarter, Integer year, String province_id) {
        List<PqmeLMISETable> listData = new ArrayList<>();

        HashMap<String, Object> params = new HashMap<>();

        params.put("year", year);
        params.put("quarter", quarter);
        params.put("yearx", quarter == 1 ? year - 1 : year);
        params.put("quarterx", quarter == 1 ? 4 : quarter - 1);
        params.put("province_id", province_id);

        List<Object[]> result = query("pqm-report/drug/pqm_drug_elmise.sql", params).getResultList();

        for (Object[] object : result) {

            Double thang = object[3] != null ? Double.valueOf(object[3].toString()) : Double.valueOf("0");

            PqmeLMISETable item = new PqmeLMISETable();
            item.setSiteID(Long.valueOf(object[0].toString()));
            item.setDrugName(object[1].toString());
            item.setTon(object[2] != null ? Long.valueOf(object[2].toString()) : Long.valueOf("0"));
            item.setThang(getThang(thang));
            item.setSuDung(object[4] != null ? Long.valueOf(object[4].toString()) : Long.valueOf("0"));
            item.setTrongKy(object[5] != null ? Long.valueOf(object[5].toString()) : Long.valueOf("0"));
            listData.add(item);
        }
        return listData;
    }

    private Double getThang(Double thang) {
             System.out.println("x " + thang);
        
        thang = (thang / 3) * 2;
        thang = Math.round(thang * 100.0) / 100.0;

        System.out.println("y " + thang);

        return thang;
    }

    public List<PqmDrugPlanDataEntity> getDataIndex(Integer year, Integer month, String province_id, String site_code) {
        List<PqmDrugPlanDataEntity> listData = new ArrayList<>();
        Query query = getQuery("SELECT "
                + "   p.drug_name, "
                + "   p.unit , "
                + "   p.year,  "
                + "   p.month,  "
                + "   sum(total_drug_backlog) total_drug_backlog,  "
                + "   sum(total_drug_plan_2month) total_drug_plan_2month,  "
                + "   p.site_code, "
                + "   p.site_name "
                + "FROM pqm_drug_plan_data p where p.year = :year and p.month = :month and p.province_id = :province_id and (p.site_code =:site_code or  :site_code is null) GROUP By p.drug_name,p.unit,p.year,p.month,p.province_id");
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("province_id", province_id);
        query.setParameter("site_code", null);
        if (!StringUtils.isEmpty(site_code)) {
            query.setParameter("site_code", site_code);
        }
        List<Object[]> result = query.getResultList();
        for (Object[] object : result) {
            PqmDrugPlanDataEntity item = new PqmDrugPlanDataEntity();
            item.setDrugName(object[0].toString());
            item.setUnit(object[1].toString());
            item.setYear(object[2] != null ? Integer.valueOf(object[2].toString()) : 0);
            item.setMonth(object[3] != null ? Integer.valueOf(object[3].toString()) : 0);
            item.setTotalDrugBacklog(object[4] != null ? Long.valueOf(object[4].toString()) : 0);
            item.setTotalDrugPlan2Month(object[5] != null ? Long.valueOf(object[5].toString()) : 0);
            item.setSiteCode(object[6].toString());
            item.setSiteName(object[7] != null ? object[7].toString() : "");
            item.setProvinceID(province_id);
            listData.add(item);
        }
        return listData;
    }

}
