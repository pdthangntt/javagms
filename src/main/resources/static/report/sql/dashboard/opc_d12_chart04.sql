SELECT 
    main.d, 
    sum(main.te) as te,
    sum(main.nl) as nl
FROM (
    SELECT 
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        count(sub.arv_id) as nl,
        0 as te
    FROM(
        SELECT 
            e.arv_id as arv_id
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id  JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.registration_time IS NOT NULL 
            AND e.site_id IN (@site_id)
            AND (@year - YEAR(p.dob)) >= 15
            AND (e.end_time IS NULL OR e.end_time > MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY)
            AND (e.tranfer_from_time IS NULL OR e.tranfer_from_time > MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY)
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        GROUP BY e.arv_id) as sub
    GROUP BY d

    UNION ALL 

    SELECT
        CONCAT(QUARTER(@to_date), '/', YEAR(@to_date)) as d,
        0 as nl,
        count(sub.arv_id) as te
    FROM (
        SELECT 
            e.arv_id as arv_id
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id  JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.registration_time IS NOT NULL 
            AND e.site_id IN (@site_id)
            AND (@year - YEAR(p.dob)) < 15
            AND (e.end_time IS NULL OR e.end_time > MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY)
            AND (e.tranfer_from_time IS NULL OR e.tranfer_from_time > MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY)
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') <= MAKEDATE(YEAR(@to_date), 1) + INTERVAL QUARTER(@to_date) QUARTER - INTERVAL 1 DAY
        GROUP BY e.arv_id) as sub 
    GROUP BY d
) as main 
GROUP BY main.d