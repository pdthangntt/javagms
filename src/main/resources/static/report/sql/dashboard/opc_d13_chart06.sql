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
     (SELECT table01.arv_id,
             DATEDIFF(@to_date, DATE_FORMAT(table01.appoinment_time, '%Y-%m-%d')) AS trehen
      FROM
        (SELECT *
         FROM
           (SELECT *
            FROM
              (SELECT sub_max.arv_id AS arv_id,
                      sub_max.stage_id AS stage_id,
                      sub_max.appoinment_time
               FROM opc_visit AS sub_max
               WHERE sub_max.is_remove = 0
                 AND DATE_FORMAT(sub_max.appoinment_time, '%Y-%m-%d') <= @to_date
                 AND sub_max.site_id IN (@site_id)
               ORDER BY sub_max.appoinment_time DESC, sub_max.id DESC) AS sub
            GROUP BY sub.arv_id) AS main_visit
         INNER JOIN
           (SELECT *
            FROM
              (SELECT logmax.arv_id AS arv_id_log,
                      logmax.stage_id AS stage_id_log,
                      logmax.is_remove,
                      logmax.registration_time,
                      logmax.end_time,
                      logmax.tranfer_from_time,
                      logmax.status_of_treatment_id
               FROM opc_arv_revision AS logmax
               WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                 AND logmax.site_id IN (@site_id)
               ORDER BY logmax.created_at DESC, logmax.id DESC  ) AS m1
            GROUP BY m1.arv_id_log,
                     m1.stage_id_log) AS main_log ON main_log.arv_id_log = main_visit.arv_id
         AND main_log.stage_id_log = main_visit.stage_id
         WHERE main_log.is_remove = 0
           AND main_log.arv_id_log = ( SELECT stage.arv_id FROM opc_stage as stage WHERE stage.id = main_visit.stage_id AND stage.is_remove = 0 )
           AND main_visit.appoinment_time IS NOT NULL
           AND DATE_FORMAT(main_log.registration_time, '%Y-%m-%d') <= @to_date
           AND main_log.tranfer_from_time IS NULL
           AND main_log.status_of_treatment_id = '3'
           AND main_log.end_time IS NULL ) AS table01) AS table02
   GROUP BY table02.trehen) AS table03