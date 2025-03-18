select 
    main.d, 
    sum(main.nam) as nam,
    sum(main.nu) as nu
FROM (SELECT 'sum' as d, 
        case when (sub_main.gender_id = '1') then 1 else 0 end as nam,   
        case when (sub_main.gender_id = '2') then 1 else 0 end as nu
     FROM (SELECT 
            e.registration_time as registration_time,
            e.arv_id as arv_id,
            p.gender_id as gender_id,
            CASE WHEN (YEAR(@from_date) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END as type
        FROM opc_stage as e JOIN opc_patient as p ON e.patient_id = p.id JOIN opc_arv as a ON a.id = e.arv_id 
        WHERE e.is_remove = 0 
            AND a.is_remove = 0 
            AND e.site_id IN (@site_id)
            AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date 
            AND e.registration_type IN ('1','4','5')
        GROUP BY e.arv_id    
    ) as sub_main
) main GROUP BY main.d