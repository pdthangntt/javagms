select 
    main.d, 
    sum(main.nam) as nam,
    sum(main.nu) as nu
FROM (
    SELECT 
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        count(e.id) as nam,
        0 as nu
    FROM opc_arv_revision as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_stage as s ON e.stage_id = s.id 
    WHERE e.is_remove = 0 
        AND s.is_remove = 0 
        AND e.treatment_time IS NOT NULL 
        AND e.site_id IN (@site_id)
        AND p.gender_id = '1'
        AND DATE_FORMAT(e.treatment_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        AND e.end_time IS NULL 
        AND e.status_of_treatment_id = '3'
        AND e.tranfer_from_time IS NULL
        AND e.id IN (SELECT m.id 
                    FROM (SELECT log.id, log.arv_id 
                        FROM opc_arv_revision as log 
                        WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
                            AND site_id IN (@site_id) 
                            AND is_remove = 0 
                        ORDER BY created_at DESC, id DESC) as m  
                    GROUP BY m.arv_id)
    GROUP BY d

    UNION ALL 

    SELECT 
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        0 as nam,
        count(e.id) as nu
    FROM opc_arv_revision as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_stage as s ON e.stage_id = s.id 
    WHERE e.is_remove = 0 
        AND s.is_remove = 0 
        AND e.treatment_time IS NOT NULL 
        AND e.site_id IN (@site_id)
        AND p.gender_id = '2'
        AND e.status_of_treatment_id = '3'
        AND e.end_time IS NULL 
        AND e.tranfer_from_time IS NULL 
        AND DATE_FORMAT(e.treatment_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        AND e.id IN (SELECT m.id 
                    FROM (SELECT log.id, log.arv_id 
                        FROM opc_arv_revision as log 
                        WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
                            AND site_id IN (@site_id) 
                            AND is_remove = 0 
                        ORDER BY created_at DESC, id DESC) as m  
                    GROUP BY m.arv_id)
    GROUP BY d
) main GROUP BY main.d