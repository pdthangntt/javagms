SELECT 
    a.id as arv_id,
    r.stage_id,
    r.registration_time,
    p.fullname,
    a.code,
    p.gender_id,
    p.dob,
    a.permanent_address,
    a.permanent_address_group,
    a.permanent_address_street,
    a.permanent_ward_id,
    a.permanent_district_id,
    a.permanent_province_id,
    a.registration_status,
    a.clinical_stage,
    a.cd4,
    a.patient_weight,
    a.patient_height,
    r.status_of_treatment_id,
    r.end_case,
    r.end_time,
    a.patient_phone,
    p.identity_card,
    a.insurance_no,
    r.tranfer_from_time,
    r.treatment_time,
    YEAR(r.registration_time) as year
FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
WHERE a.is_remove = 0 
    AND s.is_remove = 0 
    AND r.site_id IN (@site_id)
    AND (@word IS NULL OR @word = '' OR 
        p.fullname LIKE CONCAT('%',@word, '%') OR 
        p.identity_card LIKE CONCAT('%',@word, '%') OR 
        a.patient_phone LIKE CONCAT('%',@word, '%') OR 
        a.insurance_no LIKE CONCAT('%',@word, '%') OR 
        a.code LIKE CONCAT('%',@word, '%'))
    AND r.id IN (
        SELECT m.id FROM (SELECT * FROM (SELECT *
            FROM opc_arv_revision as logmax 
            WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
            ORDER BY logmax.created_at desc , logmax.id desc) as m1  
        WHERE m1.status_of_treatment_id = '2'
        GROUP BY m1.arv_id) as m 
        WHERE m.is_remove = 0 
             
            AND DATE_FORMAT(m.registration_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)