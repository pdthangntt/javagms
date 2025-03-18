SELECT 	SUM(coalesce(table02.co, 0)) as co,
        SUM(coalesce(table02.khong, 0)) as khong
FROM (
        SELECT 	(case When ( table01.insurance_no is not null AND table01.insurance_no <> '' ) then count(table01.arv_id) else 0 end)  AS co,
                (case When ( table01.insurance_no is null OR  table01.insurance_no = ''  ) then count(table01.arv_id) else 0 end)  AS khong
        FROM (SELECT * 
                FROM 
                        (SELECT * 
                            FROM (  SELECT 
                                        sub_max.arv_id as arv_id, 
                                        sub_max.stage_id as stage_id_visit, 
                                        sub_max.examination_time,
                                        sub_max.insurance_expiry,
                                        sub_max.insurance_no
                                    FROM opc_visit as sub_max
                                    WHERE sub_max.is_remove = 0 
                                            AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                                            AND sub_max.site_id IN (@site_id) 
                                    ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
                INNER JOIN (select arv.id as id_arv_main from opc_arv as arv where arv.is_remove = false) as main_arv ON main_arv.id_arv_main = main_visit.arv_id
                INNER JOIN (
                SELECT *
                FROM (  SELECT    stage.arv_id as arv_stage_id,
                                stage.id as stage_id,
                                stage.is_remove as stage_remove,
                                stage.treatment_time,
                                stage.end_time as end_time_stage,
                                stage.tranfer_from_time as tranfer_from_time_stage,
                                stage.status_of_treatment_id
                        FROM opc_stage as stage 
                        WHERE stage.is_remove = 0 AND DATE_FORMAT(stage.REGISTRATION_TIME, '%Y-%m-%d') <= @to_date  AND stage.site_id IN (@site_id) 
                        order by stage.REGISTRATION_TIME desc, stage.id desc  ) as m1 group by m1.arv_stage_id) 
                        as main_stage 
                ON main_stage.arv_stage_id = main_visit.arv_id AND main_visit.stage_id_visit = main_stage.stage_id
                WHERE ( main_stage.end_time_stage is null OR ( main_stage.end_time_stage is not null AND DATE_FORMAT(main_stage.end_time_stage, '%Y-%m-%d') > @to_date)  )
                AND  ( main_stage.tranfer_from_time_stage is null OR ( main_stage.tranfer_from_time_stage is not null AND DATE_FORMAT(main_stage.tranfer_from_time_stage, '%Y-%m-%d') > @to_date)  )       
) AS table01 group by table01.insurance_no , table01.insurance_expiry
) AS table02