select 
    main.d, 
    sum(main.nam) as nam,
    sum(main.nu) as nu
FROM (SELECT 'sum' as d, 
        case when (sub_main.gender_id = '1') then 1 else 0 end as nam,   
        case when (sub_main.gender_id = '2') then 1 else 0 end as nu
     FROM (
            SELECT
                v.arv_id,
                v.site_id as site_id,
                MAX(v.test_time) as test_time,
                v.result as viral_result,
                p.gender_id as gender_id
            FROM opc_viral_load as v INNER JOIN opc_patient as p ON p.id = v.patient_id INNER JOIN opc_stage as s ON s.id = v.stage_id 
            WHERE v.test_time is not null 
                AND s.is_remove = 0  
                AND DATE_FORMAT(v.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                AND v.is_remove = 0  
                AND v.site_id IN (@site_id) 
                AND v.id IN (
                    SELECT 
                        c.id 
                        FROM (SELECT
                        m.site_id,
                        m.arv_id,
                        m.test_time, 
                        m.id as id
                    FROM opc_viral_load as m 
                    WHERE m.test_time is not null 
                         AND m.is_remove = 0  
                         AND m.arv_id IN (SELECT m1.arv_id 
                         FROM (select * from (SELECT *
                                 FROM opc_arv_revision as logmax 
                                                 WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                 order by logmax.id desc , logmax.created_at desc) as m group by m.arv_id, m.stage_id) as m1
                         WHERE m1.stage_id = m.stage_id and m1.is_remove = '0'
                                 and m1.status_of_treatment_id = '3' AND m1.treatment_time <= m.test_time) ORDER BY  m.test_time desc, m.id desc) as c)
            GROUP BY v.arv_id
    ) as sub_main
) main GROUP BY main.d