select * from (SELECT arv_patient.id patientID,
       arv_patient.fullname,
       arv_patient.gender_id,
       arv_patient.dob,
       
       main_rev.arv_id,
        main_rev.stage_id,
        main_rev.status_of_treatment_id,
        main_rev.treattime,
        main_rev.treatment_time,
        main_rev.end_case,
        main_rev.end_time,
        main_rev.registration_type,
        main_rev.tranfer_from_time,
        main_rev.treatment_regiment_id,
        main_rev.treatment_regiment_stage,
        main_rev.regiment_date,
        main_rev.rev_id,

       main_vs.examination_time,
       arv_patient.date_of_arrival,
       arv_patient.code,
       main_rev.clinical_stage,
       main_rev.cd4,
       main_rev.causes_change,
       arv_patient.patient_weight,
       arv_patient.patient_height,
       arv_patient.insurance_no,
       arv_patient.identity_card,
       arv_patient.patient_phone
FROM
  (SELECT rev.arv_id,
          rev.stage_id,
          rev.status_of_treatment_id,
          MONTH(rev.treatment_time) treattime,
          rev.treatment_time,
          rev.end_case,
          rev.end_time,
          rev.registration_type,
          rev.tranfer_from_time,
          rev.treatment_regiment_id,
          rev.treatment_regiment_stage,
          rev.regiment_date,
          rev.id as rev_id,

          os.clinical_stage,
          os.cd4, 
          os.causes_change
   FROM
     (SELECT *
      FROM opc_arv_revision
      WHERE is_remove = 0
        AND (treatment_time IS NOT NULL)
        AND DATE_FORMAT(treatment_time, '%Y-%m-%d') <= @toDate
        AND DATE_FORMAT(treatment_time, '%Y-%m-%d') >= @fromDate
        AND site_id = @siteID
      ORDER BY treatment_time DESC, stage_id DESC, created_at DESC) AS rev
      LEFT JOIN  opc_stage os
      ON  os.is_remove = 0
        AND os.id = rev.stage_id 
        AND os.arv_id = rev.arv_id
      
   GROUP BY rev.arv_id,
            rev.stage_id,
            rev.treatment_time,
            rev.treatment_regiment_id,
            rev.regiment_date,
            rev.end_time,
            rev.status_of_treatment_id) AS main_rev
INNER JOIN
    (SELECT 
        oa.id arv_id,
        oa.date_of_arrival,
        oa.code,
        oa.patient_weight,
        oa.patient_height,
        oa.patient_phone,
        oa.insurance_no,
        op.id,
        op.fullname,
        op.gender_id,
        op.dob,
        op.identity_card
          FROM opc_arv oa
      INNER JOIN
          opc_patient op ON op.site_id = @siteID 
              AND oa.site_id = @siteID 
              AND op.id = oa.patient_id
              AND oa.is_remove = 0
    )AS arv_patient ON arv_patient.arv_id = main_rev.arv_id

LEFT JOIN
  (SELECT *
   FROM
     (SELECT *
      FROM opc_visit
      WHERE is_remove = 0
        AND site_id = @siteID
      ORDER BY examination_time DESC) AS vs
   GROUP BY vs.arv_id,
            vs.stage_id) AS main_vs ON main_rev.arv_id = main_vs.arv_id
            AND main_rev.stage_id = main_vs.stage_id) as main_final 
WHERE (@keywords IS NULL OR @keywords = '' OR main_final.code LIKE CONCAT('%',@keywords, '%')
        OR main_final.fullname LIKE CONCAT('%',@keywords, '%') 
        OR main_final.identity_card LIKE CONCAT('%',@keywords, '%') 
        OR main_final.insurance_no LIKE CONCAT('%',@keywords, '%') 
        OR main_final.patient_phone LIKE CONCAT('%',@keywords, '%'))
ORDER BY stage_id ASC, rev_id ASC