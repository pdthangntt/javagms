SELECT 
    a.id as arv_id,
    r.stage_id,
    r.regiment_date,
    r.treatment_regiment_id,
    s.causes_change,
    r.treatment_time
FROM opc_arv_revision as r  INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
WHERE a.is_remove = 0 
    AND s.is_remove = 0 
    AND s.causes_change IS NOT NULL 
    AND r.id IN ( 
        SELECT m.id FROM (SELECT * FROM (SELECT *
            FROM opc_arv_revision as logmax 
            WHERE logmax.site_id IN (@site_id) AND DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
            ORDER BY logmax.created_at desc, logmax.id desc) as m1 
        WHERE m1.is_remove = 0 
            AND m1.status_of_treatment_id = '3'  
            AND m1.regiment_date IS NOT NULL 
            AND m1.treatment_regiment_id IS NOT NULL
        GROUP BY m1.arv_id) as m 
WHERE DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end)