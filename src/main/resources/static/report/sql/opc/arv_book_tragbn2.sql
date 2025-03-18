SELECT *
FROM
  (SELECT   
            test.patient_id, 
            test.id,
            test.lao_start_time, 
            test.inh_from_time, 
            test.cotrimoxazole_from_time, 
            test.cd4_result, 
            test.cd4_test_time, 
            arv.code,
            test.stage_id,
            patient.fullname,
            patient.identity_card,
            arv.patient_phone,
            arv.insurance_no,
            rev.treatment_time
   FROM opc_test test
   INNER JOIN opc_patient patient ON patient.id = test.patient_id
        AND test.site_id = @siteID
        AND test.status_of_treatment_id = '3'
        AND test.is_remove = 0
   INNER JOIN opc_arv_revision rev 
        ON rev.arv_id = test.arv_id
        AND rev.stage_id = test.stage_id
        AND rev.is_remove = 0
        AND DATE_FORMAT(rev.treatment_time, '%Y-%m-%d') <= @toDate
        AND DATE_FORMAT(treatment_time, '%Y-%m-%d') >= @fromDate
    INNER JOIN opc_arv arv
        ON arv.id = test.arv_id
  ) AS main_final
where (@keywords IS NULL OR @keywords = '' OR main_final.code LIKE CONCAT('%',@keywords, '%')
        OR main_final.fullname LIKE CONCAT('%',@keywords, '%') 
        OR main_final.identity_card LIKE CONCAT('%',@keywords, '%') 
        OR main_final.insurance_no LIKE CONCAT('%',@keywords, '%') 
        OR main_final.patient_phone LIKE CONCAT('%',@keywords, '%'))
GROUP BY main_final.patient_id,
         main_final.id