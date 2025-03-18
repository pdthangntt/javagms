SELECT 
    a.id as arv_id,
    r.stage_id,
    r.treatment_time
FROM opc_arv_revision as r  INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
WHERE a.is_remove = 0 
    AND s.is_remove = 0 
    AND r.id IN ( 
        SELECT m.id FROM (SELECT * FROM (SELECT *
            FROM opc_arv_revision as logmax 
            WHERE logmax.site_id IN (@site_id) AND logmax.treatment_time IS NOT NULL 
            ORDER BY logmax.treatment_time asc, logmax.created_at asc) as m1 
        WHERE m1.is_remove = 0  
        GROUP BY m1.arv_id) as m)