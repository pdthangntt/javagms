SELECT 
    a.id as arv_id,
    r.stage_id,
    a.code,
    p.fullname,
    p.gender_id,
    p.dob,
    r.treatment_time,
    r.treatment_regiment_id,
    s.clinical_stage,
    s.cd4,
    a.patient_weight,
    a.patient_height,
    '' as regiment_date,
    r.end_case,
    '' as causes_change,
    p.identity_card,
    a.insurance_no,
    a.patient_phone
FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
WHERE a.is_remove = 0 
    AND s.is_remove = 0 
    AND r.site_id IN (@site_id) 
    AND (@word IS NULL OR @word = '' 
        OR p.fullname LIKE CONCAT('%',@word, '%') 
        OR p.identity_card LIKE CONCAT('%',@word, '%') 
        OR a.patient_phone LIKE CONCAT('%',@word, '%') 
        OR a.insurance_no LIKE CONCAT('%',@word, '%') 
        OR a.code LIKE CONCAT('%',@word, '%'))
    AND r.id IN ( 
        SELECT m.id FROM (SELECT * FROM (SELECT *
            FROM opc_arv_revision as logmax 
            WHERE logmax.site_id IN (@site_id) 
                AND ((logmax.treatment_regiment_id IS NOT NULL 
                AND logmax.treatment_regiment_id <> '' AND @flag IS NULL ) OR (@flag IS NOT NULL AND 1 = 1))
            ORDER BY logmax.treatment_time asc, logmax.created_at asc ) as m1  
        WHERE m1.is_remove = 0 
            AND m1.status_of_treatment_id = '3' 
            AND (( @flag = '1' AND m1.treatment_time IS NOT NULL) OR @flag IS NULL) 
        GROUP BY m1.arv_id) as m
        WHERE ((@flag = '1' AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end) OR @flag IS NULL ))
ORDER BY r.treatment_time asc