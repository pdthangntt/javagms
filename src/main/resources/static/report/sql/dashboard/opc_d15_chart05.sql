SELECT 	SUM(coalesce(table02.co, 0)) as co,
        SUM(coalesce(table02.khong, 0)) as khong
FROM (
        SELECT 	(case When ( table01.insurance_no is not null AND table01.insurance_no <> '' ) then count(table01.arv_id) else 0 end)  AS co,
                (case When ( table01.insurance_no is null OR  table01.insurance_no = ''  ) then count(table01.arv_id) else 0 end)  AS khong
        FROM (
SELECT * FROM (
SELECT * 
FROM 
	(SELECT * 
        FROM (	SELECT 
                    sub_max.arv_id as arv_id, 
                    sub_max.stage_id as stage_id, 
                    sub_max.examination_time,
                    sub_max.insurance_expiry,
                    sub_max.insurance_no
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                        AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                        AND sub_max.site_id IN (@site_id) 
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
INNER JOIN (
            SELECT *
            FROM (SELECT  logmax.arv_id as arv_id_log,
                                    logmax.stage_id as stage_id_log,
                                    logmax.is_remove as is_remove_log,
                                    logmax.treatment_time,
                                    logmax.end_time,
                                    logmax.tranfer_from_time,
                                    logmax.status_of_treatment_id
                            FROM opc_arv_revision as logmax 
                            WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                            order by logmax.created_at desc, logmax.id desc  ) as m1 group by m1.arv_id_log, m1.stage_id_log) 
            as main_log
INNER JOIN (
	SELECT * 
	FROM (	SELECT 
                    sub_max.arv_id as arv_id_viral, 
                    sub_max.stage_id as stage_id_viral, 
                    sub_max.test_time
                    FROM opc_viral_load as sub_max
                    WHERE sub_max.is_remove = 0 
                            AND DATE_FORMAT(sub_max.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                            AND sub_max.site_id IN (@site_id) 
                    ORDER BY sub_max.test_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id_viral 
                    )  as main_viral
        INNER JOIN (
            SELECT stage.id as stage_id_main, stage.is_remove as is_remove_stage FROM opc_stage as stage WHERE stage.site_id IN (@site_id)
            ) as main_stage 
ON main_log.arv_id_log = main_visit.arv_id AND main_log.stage_id_log = main_visit.stage_id AND main_viral.arv_id_viral = main_visit.arv_id AND main_viral.stage_id_viral = main_visit.stage_id AND main_stage.stage_id_main = main_log.stage_id_log
            WHERE main_log.status_of_treatment_id = '3' AND main_stage.is_remove_stage = 0 AND main_log.is_remove_log  = 0
            AND DATE_FORMAT(main_log.treatment_time , '%Y-%m-%d') <= DATE_FORMAT(main_viral.test_time , '%Y-%m-%d')

            
        
)as xx group by xx.arv_id
) AS table01 group by table01.insurance_no , table01.insurance_expiry
) AS table02