SELECT * FROM (SELECT * FROM (
    (SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log
    FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (SELECT m.id FROM (SELECT * FROM (SELECT *
                        FROM opc_arv_revision as logmax 
                        WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                        ORDER BY logmax.created_at desc , logmax.id desc) as m1  
                    GROUP BY m1.arv_id) as m 
                    WHERE m.is_remove = 0 
                        AND m.status_of_treatment_id = '2' 
                         
                        AND DATE_FORMAT(m.registration_time, '%Y-%m-%d') BETWEEN @beginYear AND @endYear)) as log 
    INNER JOIN (SELECT 
                t.stage_id as stage_id_test,
                t.arv_id as arv_test,
                t.status_of_treatment_id as status_of_treatment_test,
                YEAR(t.cd4_test_time) as year,
                QUARTER(t.cd4_test_time) as quarter,
                MONTH(t.cd4_test_time) as month,
                t.cd4_result as result,
                t.cd4_test_time as time,
                'cd4' as type
            FROM `opc_test` as t 
            WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
                AND (t.cd4_test_time is not null AND DATE_FORMAT(t.cd4_test_time, '%Y-%m-%d') BETWEEN @start AND @end)) as sub 
    ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test)
 
UNION ALL 

SELECT * FROM (
    (SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log
    FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (SELECT m.id FROM (SELECT * FROM (SELECT *
                        FROM opc_arv_revision as logmax 
                        WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                        ORDER BY logmax.created_at desc , logmax.id desc) as m1  
                    GROUP BY m1.arv_id) as m 
                    WHERE m.is_remove = 0 
                        AND m.status_of_treatment_id = '2' 
                        AND DATE_FORMAT(m.registration_time, '%Y-%m-%d') BETWEEN @beginYear AND @endYear)) as log 
    INNER JOIN (SELECT 
                t.stage_id as stage_id_test,
                t.arv_id as arv_test,
                t.status_of_treatment_id as status_of_treatment_test,
                YEAR(t.inh_from_time) as year,
                QUARTER(t.inh_from_time) as quarter,
                MONTH(t.inh_from_time) as month,
                '' as result,
                t.inh_from_time,
                'inh' as type
            FROM `opc_test` as t  
            WHERE t.is_remove = 0 AND t.site_id IN (@site_id)  
                AND (t.inh_from_time is not null AND DATE_FORMAT(t.inh_from_time, '%Y-%m-%d') BETWEEN @start AND @end)) as sub 
    ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test)  

UNION ALL 

SELECT * FROM (
    (SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log
    FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (SELECT m.id FROM (SELECT * FROM (SELECT *
                        FROM opc_arv_revision as logmax 
                        WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                        ORDER BY logmax.created_at desc , logmax.id desc) as m1  
                    GROUP BY m1.arv_id) as m 
                    WHERE m.is_remove = 0 
                        AND m.status_of_treatment_id = '2'  
                         
                        AND DATE_FORMAT(m.registration_time, '%Y-%m-%d') BETWEEN @beginYear AND @endYear)) as log  
    INNER JOIN (SELECT 
                t.stage_id as stage_id_test,
                t.arv_id as arv_test,
                t.status_of_treatment_id as status_of_treatment_test,
                YEAR(t.cotrimoxazole_from_time) as year,
                QUARTER(t.cotrimoxazole_from_time) as quarter,
                MONTH(t.cotrimoxazole_from_time) as month,
                '' as result,
                t.cotrimoxazole_from_time,
                'ctx' as type
            FROM `opc_test` as t 
            WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
                AND (t.cotrimoxazole_from_time is not null AND DATE_FORMAT(t.cotrimoxazole_from_time, '%Y-%m-%d') BETWEEN @start AND @end)) as sub 
    ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test)

UNION ALL 

SELECT * FROM (
    (SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        '' as stage_id_test,
        '' as arv_test,
        r.status_of_treatment_id as status_of_treatment_test,
        YEAR(r.end_time) as year,
        QUARTER(r.end_time) as quarter,
        MONTH(r.end_time) as month,
        '' as result,
        r.end_time as time,
        CASE WHEN r.end_case = '2' THEN 'TV'
            WHEN r.end_case = '4' || r.end_case = '1' THEN 'B'
        ELSE '' END as type
    FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (SELECT m.id FROM (SELECT * FROM (SELECT *
                        FROM opc_arv_revision as logmax 
                        WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                        ORDER BY logmax.created_at desc , logmax.id desc) as m1  
                    GROUP BY m1.arv_id) as m 
                    WHERE m.is_remove = 0 
                        AND m.end_case IN ('2','4','1')
                        AND m.treatment_time IS NULL 
                        AND DATE_FORMAT(m.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)) as log)

UNION ALL 

SELECT * FROM (
    (SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        '' as stage_id_test,
        '' as arv_test,
        r.status_of_treatment_id as status_of_treatment_test,
        YEAR(r.tranfer_from_time) as year,
        QUARTER(r.tranfer_from_time) as quarter,
        MONTH(r.tranfer_from_time) as month,
        '' as result,
        r.tranfer_from_time as time,
        'CD' as type
    FROM opc_arv_revision as r INNER JOIN opc_patient as p ON r.patient_id = p.id INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (SELECT m.id FROM (SELECT * FROM (SELECT *
                        FROM opc_arv_revision as logmax 
                        WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                        ORDER BY logmax.created_at desc , logmax.id desc) as m1  
                    GROUP BY m1.arv_id) as m 
                    WHERE m.is_remove = 0 
                        AND m.end_case = '3' 
                        AND m.treatment_time IS NULL 
                        AND DATE_FORMAT(m.tranfer_from_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)) as log)
) as main
