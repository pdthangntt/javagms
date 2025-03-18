select 
    main.d, 
    sum(main.nam) as nam,
    sum(main.nu) as nu
FROM (
    SELECT 
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        count(sub.stage_id) as nam,
        0 as nu
    FROM(
        SELECT 
            e.id as stage_id
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id  JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.registration_time IS NOT NULL 
            AND e.site_id IN (@site_id)
            AND p.gender_id = '1'
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        GROUP BY e.arv_id) as sub
    GROUP BY d

    UNION ALL 

    SELECT
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        0 as nam,
        count(sub.stage_id) as nu
    FROM (
        SELECT 
            e.id as stage_id
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id  JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.registration_time IS NOT NULL 
            AND e.site_id IN (@site_id)
            AND p.gender_id = '2'
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        GROUP BY e.arv_id) as sub 
    GROUP BY d
) main GROUP BY main.d