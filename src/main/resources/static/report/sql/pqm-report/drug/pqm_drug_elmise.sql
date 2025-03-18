SELECT main.site_id,
       main.drug_name,
       COALESCE(SUM(main.ton_kho), 0) AS ton_kho,
       COALESCE(SUM(main.thang), 0) AS thang,
       COALESCE(SUM(main.suDung), 0) AS suDung,
       COALESCE(SUM(main.trongKy), 0) AS trongKy
FROM
  (SELECT e.site_id,
          e.drug_name,
          SUM(e.ton_kho) AS ton_kho,
          0 AS thang,
          0 AS suDung,
          0 AS trongKy
   FROM pqm_drug_elmise e
   WHERE e.year = @year
     AND e.province_id = @province_id
     AND e.quarter = @quarter
   GROUP BY e.site_id,
            e.drug_name
   UNION ALL SELECT e.site_id,
                    e.drug_name,
                    0 AS ton_kho,
                    SUM(e.tong_su_dung) AS thang,
                    0 AS suDung,
                    0 AS trongKy
   FROM pqm_drug_elmise e
   WHERE e.year = @yearx
     AND e.province_id = @province_id
     AND e.quarter = @quarterx
   GROUP BY e.site_id,
            e.drug_name
   UNION ALL SELECT e.site_id,
                    e.drug_name,
                    0 AS ton_kho,
                    0 AS thang,
                    SUM(e.tong_su_dung) AS suDung,
                    0 AS trongKy
   FROM pqm_drug_elmise e
   WHERE e.year = @year
     AND e.province_id = @province_id
     AND e.quarter = @quarter
   GROUP BY e.site_id,
            e.drug_name
   UNION ALL SELECT e.site_id,
                    e.drug_name,
                    0 AS ton_kho,
                    0 AS thang,
                    0 AS suDung,
                    SUM(e.du_tru) AS trongKy
   FROM pqm_drug_elmise e
   WHERE e.year = @year
     AND e.province_id = @province_id
     AND e.quarter = @quarter
   GROUP BY e.site_id,
            e.drug_name) AS main
GROUP BY main.site_id,
         main.drug_name