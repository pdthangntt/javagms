SELECT coalesce(SUM(tableFinal.xnTrongThang), 0) AS xnTrongThang,
       coalesce(SUM(tableFinal.arvBac2Duoi1000), 0) AS arvBac2Duoi1000,
       coalesce(SUM(tableFinal.arvBac2Tren1000), 0) AS arvBac2Tren1000
FROM
  (SELECT COUNT(finalMain.arv_id) AS xnTrongThang,
          0 AS arvBac2Duoi1000,
          0 AS arvBac2Tren1000
   FROM
     (SELECT *
      FROM
        (SELECT main.arv_id,
                main.retry_time AS retry_time,
                main.id AS id
         FROM opc_viral_load AS main
         WHERE main.retry_time IS NOT NULL
           AND main.is_remove = 0
           AND main.site_id = @site_id
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
                    JOIN opc_arv v ON v.id = mmax.arv_id
                    JOIN opc_patient p ON p.id = v.patient_id
                    WHERE mmax.retry_time IS NOT NULL
                      AND DATE_FORMAT(mmax.retry_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                      AND (@keywords IS NULL
                           OR @keywords = ''
                           OR v.code LIKE CONCAT('%', @keywords, '%')
                           OR p.fullname LIKE CONCAT('%', @keywords, '%')
                           OR p.identity_card LIKE CONCAT('%', @keywords, '%')
                           OR v.insurance_no LIKE CONCAT('%', @keywords, '%')
                           OR v.patient_phone LIKE CONCAT('%', @keywords, '%'))
                      AND mmax.is_remove = 0
                      AND mmax.arv_id IN
                        (SELECT m1.arv_id
                         FROM
                           (SELECT *
                            FROM
                              (SELECT *
                               FROM opc_arv_revision AS logmax
                               WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                                 AND logmax.site_id = @site_id
                               ORDER BY logmax.created_at DESC , logmax.id DESC) AS m
                            GROUP BY m.arv_id,
                                     m.stage_id) AS m1
                         WHERE m1.stage_id = mmax.stage_id
                           AND m1.is_remove = '0'
                           AND m1.status_of_treatment_id = '3'
                           AND m1.treatment_time <= mmax.retry_time)
                    ORDER BY mmax.retry_time DESC, mmax.id DESC) AS vmain
                 GROUP BY vmain.site_id,
                          vmain.arv_id) AS c)
         ORDER BY main.retry_time DESC, main.id DESC) AS xxx
      GROUP BY xxx.arv_id) AS finalMain
      JOIN opc_arv o ON o.id = finalMain.arv_id
                        AND o.is_remove = 0
   UNION ALL SELECT 0 AS xnTrongThang,
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
           (SELECT *
            FROM
              (SELECT t.id,
                      t.arv_id AS arv_id,
                      t.test_time AS test_time,
                      t.result AS RESULT,
                      main_log.treatment_stage AS treatment_stage,
                      main_log.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
                      main_log.treatment_time AS treatment_time,
                      main_log.is_remove AS is_remove
               FROM opc_viral_load AS t
               INNER JOIN
                 (SELECT *
                  FROM
                    (SELECT logmax.id,
                            logmax.arv_id AS arv_id_log,
                            logmax.stage_id AS stage_id_log,
                            logmax.status_of_treatment_id AS treatment_stage,
                            logmax.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
                            logmax.treatment_time AS treatment_time,
                            logmax.is_remove AS is_remove
                     FROM opc_arv_revision AS logmax
                     WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                       AND logmax.site_id = @site_id
                     ORDER BY logmax.created_at DESC , logmax.id DESC) AS m1
                  GROUP BY m1.arv_id_log,
                           m1.stage_id_log) AS main_log ON main_log.arv_id_log = t.arv_id
               AND main_log.stage_id_log = t.stage_id
               JOIN opc_arv v ON v.id = t.arv_id
               JOIN opc_patient p ON p.id = v.patient_id
               WHERE t.is_remove = 0
                 AND (@keywords IS NULL
                      OR @keywords = ''
                      OR v.code LIKE CONCAT('%', @keywords, '%')
                      OR p.fullname LIKE CONCAT('%', @keywords, '%')
                      OR p.identity_card LIKE CONCAT('%', @keywords, '%')
                      OR v.insurance_no LIKE CONCAT('%', @keywords, '%')
                      OR v.patient_phone LIKE CONCAT('%', @keywords, '%'))
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
                            AND DATE_FORMAT(mmax.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                            AND mmax.arv_id IN
                              (SELECT m1.arv_id
                               FROM
                                 (SELECT *
                                  FROM
                                    (SELECT *
                                     FROM opc_arv_revision AS logmax
                                     WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                                       AND logmax.site_id = @site_id
                                     ORDER BY logmax.created_at DESC , logmax.id DESC) AS m
                                  GROUP BY m.arv_id,
                                           m.stage_id) AS m1
                               WHERE m1.stage_id = mmax.stage_id
                                 AND m1.is_remove = '0'
                                 AND m1.status_of_treatment_id = '3'
                                 AND m1.treatment_time <= mmax.test_time)
                          ORDER BY mmax.test_time DESC, mmax.id DESC) AS vmain
                       GROUP BY vmain.site_id,
                                vmain.arv_id) AS c)
                 AND t.site_id = @site_id
                 AND DATE_FORMAT(t.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
               ORDER BY t.test_time DESC, t.id DESC) AS xxx
            GROUP BY xxx.arv_id) AS m
            JOIN opc_arv o ON o.id = m.arv_id AND o.is_remove = 0
         WHERE m.treatment_stage = '3'
           AND m.TREATMENT_REGIMENT_STAGE = '2'
           AND m.is_remove = 0 ) AS main
      GROUP BY main.result) AS table_main) AS tableFinal