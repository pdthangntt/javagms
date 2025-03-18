select coalesce(COUNT(tbl.code), 0) from ( SELECT 
       o.code,
       o.insurance_no,
       o.patient_phone,
       p.fullname,
       YEAR(p.dob),
       p.gender_id,
       p.identity_card,
       viralload.test_time,
       viralload.id as viral_id,
       viralload.sample_time,
       viralload.result_time,
       final_rev.treatment_time,
       final_rev.status_of_treatment_id,
       final_rev.treatment_regiment_stage
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
         AND DATE_FORMAT(r.created_at, '%Y-%m-%d') <= @to_date ) main_r
      ORDER BY main_r.created_at DESC , id DESC) main_rev
   GROUP BY main_rev.arv_id) AS final_rev
JOIN opc_viral_load viralload ON final_rev.viral_id = viralload.id AND final_rev.status_of_treatment_id = '3'
JOIN opc_patient p ON p.id = final_rev.patient_id
JOIN opc_arv o ON o.id = final_rev.arv_id
AND o.is_remove = 0
) AS tbl WHERE (@keywords IS NULL OR @keywords = '' OR tbl.code LIKE CONCAT('%',@keywords, '%') OR tbl.fullname LIKE CONCAT('%',@keywords, '%') 
    OR tbl.identity_card LIKE CONCAT('%',@keywords, '%') 
    OR tbl.insurance_no LIKE CONCAT('%',@keywords, '%') 
    OR tbl.patient_phone LIKE CONCAT('%',@keywords, '%'))
