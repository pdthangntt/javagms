SELECT 
    main.d, 
    sum(main.te) as te,
    sum(main.nl) as nl
FROM (SELECT case 
            when (sub_main.registration_time IS NOT NULL ) then CONCAT(QUARTER(sub_main.registration_time), '/', YEAR(sub_main.registration_time))
        END as d, 
        case when (sub_main.type = '1') then 1 else 0 end as te,   
        case when (sub_main.type = '2') then 1 else 0 end as nl
     FROM (SELECT 
            e.registration_time as registration_time,
            e.id as id,
            p.gender_id as gender_id,
            CASE WHEN (@year - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON a.id = e.arv_id 
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') BETWEEN MAKEDATE(YEAR(@from_date), 1) + INTERVAL QUARTER(@from_date) QUARTER - INTERVAL    1 QUARTER 
            AND (MAKEDATE(YEAR(@from_date), 1) + INTERVAL QUARTER(@from_date) QUARTER - INTERVAL 1 DAY)
            AND e.registration_type IN ('1','4','5')
        GROUP BY arv_id
    ) as sub_main
) as main 
GROUP BY main.d