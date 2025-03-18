SELECT coalesce(SUM(tableFinal.xnTrongThang), 0) AS xnTrongThang,
       coalesce(SUM(tableFinal.arvBac2Duoi1000), 0) AS arvBac2Duoi1000,
       coalesce(SUM(tableFinal.arvBac2Tren1000), 0) AS arvBac2Tren1000
FROM
  (SELECT COUNT(finalMain.arv_id) AS xnTrongThang,
          0 AS arvBac2Duoi1000,
          0 AS arvBac2Tren1000
   FROM
     (
SELECT * FROM (
SELECT main.arv_id,
             main.retry_time AS retry_time,
             main.id AS id
      FROM opc_viral_load AS main
      WHERE main.retry_time IS NOT NULL
        AND main.is_remove = 0
        AND main.site_id IN (@site_id)
        AND main.id IN
          (SELECT c.id
           FROM
             (SELECT *
              FROM
                (SELECT mmax.site_id,
                        mmax.arv_id,
                        mmax.retry_time,
                        mmax.id AS id
                 FROM opc_viral_load AS mmax
                 WHERE mmax.retry_time IS NOT NULL
                   AND mmax.is_remove = 0
                   AND mmax.arv_id IN
                     (SELECT m1.arv_id
                      FROM
                        (SELECT *
                         FROM
                           (SELECT *
                            FROM opc_arv_revision AS logmax
                            WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                              AND logmax.site_id IN (@site_id)
                            ORDER BY  logmax.created_at desc , logmax.id desc ) AS m
                         GROUP BY m.arv_id,
                                  m.stage_id) AS m1
                      WHERE m1.stage_id = mmax.stage_id
                        AND m1.is_remove = '0'
                        AND m1.status_of_treatment_id = '3'
                        AND m1.treatment_time <= mmax.retry_time)
                 ORDER BY mmax.retry_time DESC, mmax.id DESC) AS vmain
              GROUP BY vmain.site_id,
                       vmain.arv_id) AS c)
       ORDER BY main.retry_time desc, main.id desc) AS xxx GROUP BY xxx.arv_id
) AS finalMain
   WHERE finalMain.retry_time BETWEEN @from_date AND @to_date
   UNION ALL 
                    SELECT 0 AS xnTrongThang,
                    sum(table_main.duoi1000) AS arvBac2Duoi1000,
                    sum(table_main.tren1000) AS arvBac2Tren1000
   FROM
     (SELECT (CASE
                  WHEN (main.result = '1'
                        OR main.result = '2'
                        OR main.result = '6'
                        OR main.result = '3') THEN count(main.arv_id)
                  ELSE 0
              END) AS duoi1000,
             (CASE
                  WHEN main.result = '4' THEN count(main.arv_id)
                  ELSE 0
              END) AS tren1000
      FROM
        (SELECT m.arv_id AS arv_id,
                m.result AS RESULT,
                m.treatment_stage AS treatment_stage,
                m.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
                m.treatment_time AS treatment_time,
                m.is_remove AS is_remove
         FROM
           (
SELECT * FROM (
            SELECT t.id,
                   t.arv_id AS arv_id,
                   t.test_time AS test_time,
                   t.result AS RESULT,
              main_log.treatment_stage AS treatment_stage,
                        main_log.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
                        main_log.treatment_time AS treatment_time,
                        main_log.is_remove AS is_remove
                    FROM opc_viral_load as t
                    INNER JOIN (select * from (SELECT   logmax.id,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log,
                                                    logmax.status_of_treatment_id as treatment_stage,
                                                    logmax.TREATMENT_REGIMENT_STAGE as TREATMENT_REGIMENT_STAGE,
                                                    logmax.treatment_time as treatment_time,
                                                    logmax.is_remove as is_remove
                                                    
                                                    FROM opc_arv_revision as logmax 
                                                    WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                                    order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) 
                as main_log
                ON main_log.arv_id_log = t.arv_id AND main_log.stage_id_log = t.stage_id
                    WHERE t.is_remove = 0
              AND t.id IN
                (SELECT c.id
                 FROM
                   (SELECT *
                    FROM
                      (SELECT mmax.site_id,
                              mmax.arv_id,
                              mmax.test_time,
                              mmax.id AS id
                       FROM opc_viral_load AS mmax
                       WHERE mmax.test_time IS NOT NULL
                         AND mmax.is_remove = 0
                         AND mmax.test_time BETWEEN @from_date AND @to_date
                         AND mmax.arv_id IN
                           (SELECT m1.arv_id
                            FROM
                              (SELECT *
                               FROM
                                 (SELECT *
                                  FROM opc_arv_revision AS logmax
                                  WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                                    AND logmax.site_id IN (@site_id)
                                  ORDER BY  logmax.created_at desc , logmax.id desc ) AS m
                               GROUP BY m.arv_id,
                                        m.stage_id) AS m1
                            WHERE m1.stage_id = mmax.stage_id
                              AND m1.is_remove = '0'
                              AND m1.status_of_treatment_id = '3'
                              AND m1.treatment_time <= mmax.test_time)
                       ORDER BY mmax.test_time DESC, mmax.id DESC) AS vmain
                    GROUP BY vmain.site_id,
                             vmain.arv_id) AS c)
              AND t.site_id IN (@site_id)
              AND t.test_time BETWEEN @from_date AND @to_date
           ORDER BY t.test_time desc, t.id desc) AS xxx GROUP BY xxx.arv_id
) AS m
         WHERE m.treatment_stage = '3'
           AND m.TREATMENT_REGIMENT_STAGE = '2'
           AND m.is_remove = 0 ) AS main
      GROUP BY main.result) AS table_main) AS tableFinal