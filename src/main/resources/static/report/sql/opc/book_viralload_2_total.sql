select tbl.* from ( SELECT 
       o.code,
       o.insurance_no,
       o.patient_phone,
       p.fullname,
       YEAR(p.dob),
       p.gender_id,
       p.identity_card,
       viralload.test_time,
       final_rev.viral_id as viral_id,
       viralload.sample_time,
       viralload.result_time,
       final_rev.treatment_time,
       final_rev.status_of_treatment_id,
       final_rev.treatment_regiment_stage,
       case when ((main_final_kq1.id is not null and main_final_kq1.id <> '') and main_final_kq1.result <> '4') OR  (main_final_kq1.id is null or main_final_kq1.id = '') then main_final_kq1_1.result else null end as result_kq1,
       case when (main_final_kq2.id is not null and main_final_kq2.id <> '')  and main_final_kq2.result = '4' then main_final_kq2.result else main_final_kq2_1.result end as result_kq2,
       case when (main_final_kq2.id is not null and main_final_kq2.id <> '') and main_final_kq2.result = '4' then main_final_kq2.test_time else main_final_kq2_1.test_time end as first_test_time,
       vi.id as visit_id,
       final_rev.regiment_date,
       viralload.result,
       case when ((main_final_kq1.id is not null and main_final_kq1.id <> '') and main_final_kq1.result <> '4') OR (main_final_kq1.id is null or main_final_kq1.id = '') then main_final_kq1_1.id else null end as kq1_id,
       case when (main_final_kq2.id is not null and main_final_kq2.id <> '') and main_final_kq2.result = '4' then main_final_kq2_1.id end as kq2_id,
       final_rev.arv_id arvId,
       final_rev.stage_id stageId
FROM
  (SELECT *
   FROM
     (SELECT *
      FROM
        (SELECT r.*,
                viral.id as viral_id
         FROM
           (SELECT v.arv_id,
                   v.stage_id,
                   v.test_time,
                   v.id
            FROM
              (SELECT *
               FROM opc_viral_load
               WHERE is_remove = 0
                 AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                 AND site_id = @site_id
               ORDER BY test_time DESC , id DESC) AS v
            GROUP BY v.arv_id) AS viral
         JOIN opc_arv_revision r 
         ON viral.arv_id = r.arv_id
         AND viral.stage_id = r.stage_id
         AND DATE_FORMAT(r.created_at, '%Y-%m-%d') <= @to_date) main_r
      ORDER BY main_r.created_at DESC , id DESC) main_rev
   GROUP BY main_rev.arv_id) AS final_rev
JOIN opc_viral_load viralload ON final_rev.viral_id = viralload.id
                                AND final_rev.status_of_treatment_id = '3'
JOIN opc_patient p ON p.id = final_rev.patient_id
JOIN opc_arv o ON o.id = final_rev.arv_id
                AND o.is_remove = 0
LEFT JOIN
  (select * from (SELECT kq1.*
   FROM
     (SELECT *
      FROM
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0
           AND site_id = @site_id
           AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
           AND id NOT IN
             (SELECT main_v.id
              FROM
                (SELECT *
                 FROM opc_viral_load
                 WHERE is_remove = 0
                   AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                   AND site_id = @site_id
                 ORDER BY test_time DESC , id DESC) AS main_v
              GROUP BY main_v.arv_id,
                       main_v.stage_id)
         ORDER BY test_time DESC , id DESC) AS main_v2
      GROUP BY main_v2.arv_id) AS kq1
   JOIN
     (SELECT *
      FROM opc_arv_revision r
      WHERE r.status_of_treatment_id = '3'
        AND r.is_remove = 0 ) AS re ON re.arv_id = kq1.arv_id
   -- AND kq1.result <> '4'
   AND re.stage_id = kq1.stage_id
   AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(kq1.test_time, '%Y-%m-%d')) AS final_kq1 GROUP BY final_kq1.arv_id) as main_final_kq1 
   ON main_final_kq1.arv_id = final_rev.arv_id
LEFT JOIN
(select * from (SELECT kq1.*
   FROM
     (SELECT *
      FROM
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0
           AND site_id = @site_id
           AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
         ORDER BY test_time DESC , id DESC) AS main_v2
      GROUP BY main_v2.arv_id) AS kq1
   JOIN
     (SELECT *
      FROM opc_arv_revision r
      WHERE r.status_of_treatment_id = '3'
        AND r.is_remove = 0 ) AS re ON re.arv_id = kq1.arv_id
   -- AND kq1.result <> '4'
   AND re.stage_id = kq1.stage_id
   AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(kq1.test_time, '%Y-%m-%d')) AS final_kq1 GROUP BY final_kq1.arv_id) as main_final_kq1_1 
   ON main_final_kq1_1.arv_id = final_rev.arv_id
LEFT JOIN
 (select * from (SELECT kq2.*
   FROM
     (SELECT *
      FROM
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0
           AND site_id = @site_id
           AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
           AND id NOT IN
             (SELECT main_v.id
              FROM
                (SELECT *
                 FROM opc_viral_load
                 WHERE is_remove = 0
                   AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                   AND site_id = @site_id
                 ORDER BY test_time DESC , id DESC) AS main_v
              GROUP BY main_v.arv_id,
                       main_v.stage_id)
         ORDER BY test_time DESC , id DESC) AS main_v2
      GROUP BY main_v2.arv_id) AS kq2
   JOIN
     (SELECT *
      FROM opc_arv_revision r
      WHERE r.status_of_treatment_id = '3'
        AND r.is_remove = 0 ) AS re ON re.arv_id = kq2.arv_id
   AND re.stage_id = kq2.stage_id
   -- AND kq2.result = '4'
   AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(kq2.test_time, '%Y-%m-%d')) AS final_kq2 GROUP BY final_kq2.arv_id) as main_final_kq2  
ON main_final_kq2.arv_id = final_rev.arv_id
LEFT JOIN(
    select * from (SELECT kq2.*
   FROM
     (SELECT *
      FROM
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0
           AND site_id = @site_id
           AND DATE_FORMAT(test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
         ORDER BY test_time DESC , id DESC) AS main_v2
      GROUP BY main_v2.arv_id) AS kq2
   JOIN
     (SELECT *
      FROM opc_arv_revision r
      WHERE r.status_of_treatment_id = '3'
        AND r.is_remove = 0 ) AS re ON re.arv_id = kq2.arv_id
   AND re.stage_id = kq2.stage_id
   AND kq2.result = '4'
   AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(kq2.test_time, '%Y-%m-%d')) AS final_kq2 GROUP BY final_kq2.arv_id
) as main_final_kq2_1 on main_final_kq2_1.arv_id = final_rev.arv_id
LEFT JOIN opc_visit vi
    ON vi.is_remove = 0
    AND vi.consult = 1
    AND DATE_FORMAT(vi.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
    AND vi.arv_id = final_rev.arv_id
    AND vi.stage_id = final_rev.stage_id
    AND vi.site_id = @site_id
) AS tbl Where (@keywords IS NULL OR @keywords = '' OR tbl.code LIKE CONCAT('%',@keywords, '%') OR tbl.fullname LIKE CONCAT('%',@keywords, '%') 
        OR tbl.identity_card LIKE CONCAT('%',@keywords, '%') 
        OR tbl.insurance_no LIKE CONCAT('%',@keywords, '%') 
        OR tbl.patient_phone LIKE CONCAT('%',@keywords, '%'))
GROUP BY tbl.arvId, tbl.stageId
ORDER BY code DESC