SELECT * FROM (
    SELECT * FROM (
        (SELECT 
            a.id as arv_id,
            r.stage_id,
            r.status_of_treatment_id as status_of_treatment_log,
            r.treatment_regiment_id,
            r.end_time
        FROM opc_arv_revision as r  INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
        WHERE a.is_remove = 0 
            AND s.is_remove = 0 
            AND r.site_id IN (@site_id)
            AND r.id IN (
                SELECT m.id FROM (SELECT * FROM (SELECT * 
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id) 
                ORDER BY logmax.created_at desc, logmax.id desc ) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.status_of_treatment_id = '3' 
                AND m.end_time IS NULL 
                AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end 
            )
        ) as log 
        INNER JOIN 
        (SELECT 
            t.arv_id as arv_test,
            t.stage_id as stage_id_test,
            t.status_of_treatment_id as status_of_treatment_test,
            t.cd4_result,
            t.cd4_test_time as time,
            'cd4' as type
        FROM `opc_test` as t
        WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
            AND (t.cd4_test_time is not null AND DATE_FORMAT(t.cd4_test_time, '%Y-%m-%d') BETWEEN @beginTime AND @endTime)
        ) as sub 
        ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test
    )

    UNION ALL 

    SELECT * FROM (
        (SELECT 
            a.id as arv_id,
            r.stage_id,
            r.status_of_treatment_id as status_of_treatment_log,
            r.treatment_regiment_id,
            r.end_time
        FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
        WHERE a.is_remove = 0 
            AND s.is_remove = 0 
            AND r.site_id IN (@site_id)
            AND r.id IN (
                SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc ) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.status_of_treatment_id = '3' 
                AND m.end_time IS NULL 
                AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end 
            )
        ) as log 
        INNER JOIN 
        (SELECT 
            t.arv_id as arv_test,
            t.stage_id as stage_id_test,
            t.status_of_treatment_id as status_of_treatment_test,
            '' as result,
            t.inh_from_time as time,
            'inh' as type
        FROM `opc_test` as t 
        WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
            AND (t.inh_from_time is not null AND DATE_FORMAT(t.inh_from_time, '%Y-%m-%d') BETWEEN @beginTime AND @endTime)
        ) as sub 
        ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test
    )

    UNION ALL 

    SELECT * FROM (
        (SELECT 
            a.id as arv_id,
            r.stage_id,
            r.status_of_treatment_id as status_of_treatment_log,
            r.treatment_regiment_id,
            r.end_time
        FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
        WHERE a.is_remove = 0 
            AND s.is_remove = 0 
            AND r.site_id IN (@site_id)
            AND r.id IN (
                SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc ) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.status_of_treatment_id = '3' 
                AND m.end_time IS NULL 
                AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end 
            )
        ) as log  
        INNER JOIN 
        (SELECT 
            t.arv_id as arv_test,
            t.stage_id as stage_id_test,
            t.status_of_treatment_id as status_of_treatment_test,
            '' as result,
            t.cotrimoxazole_from_time as time,
            'ctx' as type
        FROM `opc_test` as t
        WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
            AND (t.cotrimoxazole_from_time is not null AND DATE_FORMAT(t.cotrimoxazole_from_time, '%Y-%m-%d') BETWEEN @beginTime AND @endTime)
        ) as sub 
        ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test
    )

    UNION ALL 

    SELECT * FROM (
        (SELECT 
            a.id as arv_id,
            r.stage_id,
            r.status_of_treatment_id as status_of_treatment_log,
            r.treatment_regiment_id,
            r.end_time
        FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
        WHERE a.is_remove = 0 
            AND s.is_remove = 0 
            AND r.site_id IN (@site_id)
            AND r.id IN (
                SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc ) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.status_of_treatment_id = '3' 
                AND m.end_time IS NULL 
                AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @start AND @end 
            )
        ) as log  
        INNER JOIN 
        (SELECT 
            t.arv_id as arv_test,
            t.stage_id as stage_id_test,
            t.status_of_treatment_id as status_of_treatment_test,
            '' as result,
            t.lao_start_time as time,
            'lao' as type
        FROM `opc_test` as t
        WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
            AND (t.lao_start_time is not null AND DATE_FORMAT(t.lao_start_time, '%Y-%m-%d') BETWEEN @beginTime AND @endTime)
        ) as sub 
        ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_test AND log.status_of_treatment_log = sub.status_of_treatment_test
    )

    UNION ALL 

    SELECT * FROM (
        (SELECT 
            a.id as arv_id,
            r.stage_id,
            r.status_of_treatment_id as status_of_treatment_log,
            r.treatment_regiment_id,
            r.end_time
        FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
        WHERE a.is_remove = 0 
            AND s.is_remove = 0 
            AND r.site_id IN (@site_id)
            AND r.id IN (
                SELECT m.id FROM (SELECT * FROM (SELECT *
                    FROM opc_arv_revision as logmax 
                    WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                    ORDER BY logmax.created_at desc, logmax.id desc) as m1  
                GROUP BY m1.arv_id, m1.stage_id) as m 
                WHERE m.is_remove = 0 AND m.end_case IN ('1','4')  
                AND m.treatment_time IS NOT NULL 
                AND DATE_FORMAT(m.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            )
        ) as log
        INNER JOIN 
        (SELECT 
            t.arv_id as arv_test,
            t.stage_id as stage_id_visit,
            '' as status_of_treatment_test,
            '' as result,
            t.examination_time as time,
            'B' as type
        FROM (SELECT * FROM (SELECT * FROM `opc_visit` ORDER BY examination_time DESC) as m GROUP BY m.arv_id, m.stage_id) as t
        WHERE t.is_remove = 0 AND t.site_id IN (@site_id)
        ) as sub 
        ON  log.arv_id = sub.arv_test AND log.stage_id = sub.stage_id_visit
    )

    UNION ALL 

    SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        r.treatment_regiment_id,
        r.end_time,
        '' as stage_id_test,
        '' as arv_test,
        '' as status_of_treatment_test,
        '' as result,
        r.end_time as time,
        CASE WHEN r.end_case = '2' THEN 'TV'
            WHEN r.end_case = '5' THEN 'KT'
        ELSE '' END as type
    FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (
            SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.end_case IN ('2','5') 
                AND m.treatment_time IS NOT NULL 
                AND DATE_FORMAT(m.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            )

    UNION ALL 

    SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        r.treatment_regiment_id,
        r.end_time,
        '' as stage_id_test,
        '' as arv_test,
        '' as status_of_treatment_test,
        '' as result,
        r.tranfer_from_time as time,
        'CD' as type
    FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id)
        AND r.id IN (
            SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 
                AND m.end_case = '3'  
                AND m.treatment_time IS NOT NULL 
                AND DATE_FORMAT(m.tranfer_from_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        )

    UNION ALL 

    SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        r.treatment_regiment_id,
        r.end_time,
        '' as stage_id_test,
        '' as arv_test,
        '' as status_of_treatment_test,
        '' as result,
        a.date_of_arrival as time,
        'CT' as type
    FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id) 
        AND DATE_FORMAT(a.date_of_arrival, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND r.id IN (
            SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 AND m.registration_type = '3' 
            AND m.treatment_time IS NOT NULL 
            AND DATE_FORMAT(a.date_of_arrival, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        )

    UNION ALL 

    SELECT 
        a.id as arv_id,
        r.stage_id,
        r.status_of_treatment_id as status_of_treatment_log,
        r.treatment_regiment_id,
        r.end_time,
        '' as stage_id_test,
        '' as arv_test,
        '' as status_of_treatment_test,
        '' as result,
        r.treatment_time as time,
        'DTL' as type
    FROM opc_arv_revision as r INNER JOIN opc_arv as a ON a.id = r.arv_id INNER JOIN opc_stage as s ON s.id = r.stage_id 
    WHERE a.is_remove = 0 
        AND s.is_remove = 0 
        AND r.site_id IN (@site_id) 
        AND r.id IN (
            SELECT m.id FROM (SELECT * FROM (SELECT *
                FROM opc_arv_revision as logmax 
                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                ORDER BY logmax.created_at desc, logmax.id desc) as m1  
            GROUP BY m1.arv_id, m1.stage_id) as m 
            WHERE m.is_remove = 0 AND m.registration_type = '2'  
                AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        )
) as main 