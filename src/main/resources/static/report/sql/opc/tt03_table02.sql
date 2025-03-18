SELECT 
    main.site_id,
    coalesce(SUM(main.lao), 0) as lao,
    coalesce(SUM(main.arv), 0) as arv
FROM (
    SELECT 
        m.site_id, 
        count(m.arv_id) as lao, 
        0 as arv 
    FROM (
        SELECT
            sub.site_id,
            sub.arv_id,
            main_log.treatment_stage as treatment
        FROM (SELECT 
                t.id,
                t.site_id,
                t.arv_id,
                t.lao_start_time,
                t.stage_id as stage_id
            FROM `opc_test` as t
            WHERE t.is_remove = 0 AND t.site_id IN (@site_id) AND t.id = (select MAX(v.id) from opc_test as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                AND (t.lao_start_time is not null AND DATE_FORMAT(t.lao_start_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)) as sub
            INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id as treatment_stage,
											logmax.is_remove as is_remove,
											logmax.treatment_time AS treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.id desc , logmax.created_at desc) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
            ON main_log.arv_id_log = sub.arv_id AND main_log.stage_id_log = sub.stage_id
            WHERE main_log.treatment_stage='3' AND main_log.is_remove = 0 AND main_log.treatment_time <= sub.lao_start_time
            GROUP BY sub.site_id, sub.arv_id, main_log.treatment_stage
    ) as m GROUP BY m.site_id 

    UNION ALL

    SELECT 
        m.site_id, 
        0 as lao, 
        count(m.arv_id) as arv 
    FROM (
        SELECT
            sub.site_id,
            sub.arv_id,
            main_log.treatment_stage as treatment
        FROM (SELECT 
                t.id,
                t.site_id,
                t.arv_id,
                t.lao_start_time,
                t.stage_id as stage_id
            FROM `opc_test` as t
            WHERE t.is_remove = 0 AND t.site_id IN (@site_id) AND t.id IN (select MAX(v.id) from opc_test as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                AND (t.lao_end_time is null OR DATE_FORMAT(t.lao_end_time, '%Y-%m-%d') >= @from_date)) as sub
            INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id as treatment_stage,
											logmax.is_remove as is_remove,
											logmax.treatment_time AS treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.id desc , logmax.created_at desc) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
            ON main_log.arv_id_log = sub.arv_id AND main_log.stage_id_log = sub.stage_id
            WHERE main_log.treatment_stage='3' AND main_log.is_remove = 0
                AND main_log.treatment_time >= sub.lao_start_time
                AND main_log.treatment_time BETWEEN @from_date AND @to_date
            GROUP BY sub.site_id, sub.arv_id, main_log.treatment_stage
    ) as m GROUP BY m.site_id  
) as main GROUP BY main.site_id