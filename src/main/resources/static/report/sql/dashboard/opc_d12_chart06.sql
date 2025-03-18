SELECT coalesce(SUM(table03.nho1), 0) AS nho1,
       coalesce(SUM(table03.tu1den3), 0) AS tu1den3,
       coalesce(SUM(table03.lon3), 0) AS lon3
FROM
  (SELECT (CASE
               WHEN (table02.trehen >= 7
                     AND table02.trehen < 30) THEN count(table02.arv_id)
               ELSE 0
           END) AS nho1,
          (CASE
               WHEN (table02.trehen >= 30
                     AND table02.trehen <= 90) THEN count(table02.arv_id)
               ELSE 0
           END) AS tu1den3,
          (CASE
               WHEN (table02.trehen > 90) THEN count(table02.arv_id)
               ELSE 0
           END) AS lon3
   FROM
     (SELECT table01.arv_id_visit as arv_id,
             DATEDIFF(@to_date, DATE_FORMAT(table01.appoinment_time, '%Y-%m-%d')) AS trehen
      FROM
        (SELECT *
         FROM
           (SELECT *
            FROM
              (SELECT sub_max.arv_id AS arv_id_visit,
                      sub_max.stage_id AS stage_id_visit,
                      sub_max.appoinment_time
               FROM opc_visit AS sub_max
               WHERE sub_max.is_remove = 0
                 AND DATE_FORMAT(sub_max.appoinment_time, '%Y-%m-%d') <= @to_date
                 AND sub_max.site_id IN (@site_id)
               ORDER BY sub_max.appoinment_time DESC, sub_max.id DESC) AS sub
            GROUP BY sub.arv_id_visit) AS main_visit
            INNER JOIN (select arv.id as id_arv_main from opc_arv as arv where arv.is_remove = false) as main_arv ON main_arv.id_arv_main = main_visit.arv_id_visit
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
                        order by stage.REGISTRATION_TIME desc, stage.id desc ) as m1 group by m1.arv_stage_id) 
                        as main_stage 
                ON main_stage.arv_stage_id = main_visit.arv_id_visit AND main_visit.stage_id_visit = main_stage.stage_id
         WHERE main_visit.appoinment_time IS NOT NULL
           AND ( main_stage.end_time_stage is null OR ( main_stage.end_time_stage is not null AND DATE_FORMAT(main_stage.end_time_stage, '%Y-%m-%d') > @to_date)  )
                AND  ( main_stage.tranfer_from_time_stage is null OR ( main_stage.tranfer_from_time_stage is not null AND DATE_FORMAT(main_stage.tranfer_from_time_stage, '%Y-%m-%d') > @to_date)  )       
) AS table01) AS table02
   GROUP BY table02.trehen) AS table03