SELECT 
    main.d, 
    SUM(main.dangkymoi) as dangkymoi,
    SUM(main.dieutrilai) as dieutrilai,
    SUM(main.botri) as botri,
    SUM(main.tuvong) as tuvong
FROM
    (SELECT CONCAT(QUARTER(sub_main.registration_time), '/', YEAR(sub_main.registration_time)) as d, 
        COUNT(sub_main.id) as dangkymoi,    
        0 as dieutrilai,    
        0 as botri,    
        0 as tuvong
    FROM (SELECT 
            e.registration_time as registration_time,
            e.id as id,
            CASE WHEN (YEAR(CURDATE()) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND p.gender_id IN (@gender)
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND e.registration_type IN ('1','4','5') 
        GROUP BY e.arv_id) as sub_main
    WHERE sub_main.type IN (@age) 
    GROUP BY d

    UNION ALL 
    
    SELECT CONCAT(QUARTER(sub_main.treatment_time), '/', YEAR(sub_main.treatment_time)) as d, 
        0 as dangkymoi,    
        COUNT(sub_main.id) as dieutrilai,    
        0 as botri,    
        0 as tuvong
    FROM (SELECT 
            e.treatment_time as treatment_time,
            e.id as id,
            CASE WHEN (YEAR(CURDATE()) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND p.gender_id IN (@gender)
            AND DATE_FORMAT(e.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND e.registration_type = '2' 
        GROUP BY e.arv_id) as sub_main
    WHERE sub_main.type IN (@age) 
    GROUP BY d

    UNION ALL 
    
    SELECT CONCAT(QUARTER(sub_main.end_time), '/', YEAR(sub_main.end_time)) as d, 
        0 as dangkymoi,    
        0 as dieutrilai,    
        COUNT(sub_main.id) as botri,    
        0 as tuvong
    FROM (SELECT 
            e.end_time as end_time,
            e.id as id,
            CASE WHEN (YEAR(CURDATE()) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND p.gender_id IN (@gender)
            AND DATE_FORMAT(e.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND e.end_case = '1'
        GROUP BY e.arv_id) as sub_main
    WHERE sub_main.type IN (@age) 
    GROUP BY d

    UNION ALL 
    
    SELECT CONCAT(QUARTER(sub_main.end_time), '/', YEAR(sub_main.end_time)) as d, 
        0 as dangkymoi,    
        0 as dieutrilai,    
        0 as botri,    
        COUNT(sub_main.id) as tuvong
    FROM (SELECT 
            e.end_time as end_time,
            e.id as id,
            CASE WHEN (YEAR(CURDATE()) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON e.arv_id = a.id
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND p.gender_id IN (@gender)
            AND DATE_FORMAT(e.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND e.end_case = '2'
    GROUP BY e.arv_id) as sub_main
    WHERE sub_main.type IN (@age) 
    GROUP BY d
) main 
GROUP BY main.d