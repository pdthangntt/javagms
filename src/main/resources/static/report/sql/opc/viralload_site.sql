SELECT 
coalesce(SUM(tableFinal.xnTrongThang), 0) as xnTrongThang,       
coalesce(SUM(tableFinal.xnLan1Duoi200), 0) as xnLan1Duoi200,
coalesce(SUM(tableFinal.xnLan1Tu200Den1000), 0) as xnLan1Tu200Den1000,
coalesce(SUM(tableFinal.xnLan1Tren1000), 0) as xnLan1Tren1000,
coalesce(SUM(tableFinal.xnLan2Duoi200), 0) as xnLan2Duoi200,
coalesce(SUM(tableFinal.xnLan2Tu200Den1000), 0) as xnLan2Tu200Den1000,
coalesce(SUM(tableFinal.xnLan2Tren1000), 0) as xnLan2Tren1000,
coalesce(SUM(tableFinal.arvBac2Duoi200), 0) as arvBac2Duoi200,
coalesce(SUM(tableFinal.arvBac2Tu200Den1000), 0) as arvBac2Tu200Den1000,
coalesce(SUM(tableFinal.arvBac2Tren1000), 0) as arvBac2Tren1000,
coalesce(SUM(tableFinal.xn12ThangDuoi200), 0) as xn12ThangDuoi200,
coalesce(SUM(tableFinal.xn12ThangTu200Den1000), 0) as xn12ThangTu200Den1000,
coalesce(SUM(tableFinal.xn12ThangTren1000), 0) as xn12ThangTren1000
FROM (

SELECT 
COUNT(finalMain.arv_id) as xnTrongThang,
0 as xnLan1Duoi200,
0 as xnLan1Tu200Den1000,
0 as xnLan1Tren1000,
0 as xnLan2Duoi200,
0 as xnLan2Tu200Den1000,
0 as xnLan2Tren1000,
0 as arvBac2Duoi200,
0 as arvBac2Tu200Den1000,
0 as arvBac2Tren1000,
0 as xn12ThangDuoi200,
0 as xn12ThangTu200Den1000,
0 as xn12ThangTren1000
FROM (
SELECT
            main.arv_id,
            main.retry_time AS retry_time,
            MAX(main.test_time),
            MAX(main.id) AS id
        FROM opc_viral_load as main 
        WHERE main.test_time is not null AND main.retry_time is not null
            AND main.is_remove = 0  
            AND main.site_id = @site_id 
            AND main.id IN (	SELECT 
		c.id 
		FROM (select * FROM (SELECT
		m.site_id,
		m.arv_id,
		m.test_time, 
		m.retry_time, 
		m.id as id
   FROM opc_viral_load as m 
   WHERE m.test_time is not null AND  m.retry_time is not null
	AND m.is_remove = 0  
	AND m.retry_time BETWEEN @from_date AND @to_date
	AND m.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = m.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= m.test_time) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)

            GROUP BY main.arv_id
) AS finalMain
WHERE finalMain.retry_time BETWEEN @from_date AND @to_date 
 

UNION ALL

SELECT 
    0 as xnTrongThang,
    sum(table_main.result_11) as xnLan1Duoi200,
    sum(table_main.result_12) as xnLan1Tu200Den1000,
    sum(table_main.result_13) as xnLan1Tren1000,
    sum(table_main.result_21) as xnLan2Duoi200,
    sum(table_main.result_22) as xnLan2Tu200Den1000,
    sum(table_main.result_23) as xnLan2Tren1000,
    0 as arvBac2Duoi200,
    0 as arvBac2Tu200Den1000,
    0 as arvBac2Tren1000,
    0 as xn12ThangDuoi200,
    0 as xn12ThangTu200Den1000,
    0 as xn12ThangTren1000
 from (select 
	(case When  (main.result = '1' OR main.result = '2' OR main.result = '6') AND main.status = '1' then count(main.arv_id) else 0 end) AS result_11,
	(case When  main.result = '3' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_12,
	(case When  main.result = '4' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_13,
	(case When  (main.result = '1' OR main.result = '2' OR main.result = '6') AND main.status = '2' then count(main.arv_id) else 0 end) AS result_21,
	(case When  main.result = '3' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_22,
	(case When  main.result = '4' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_23
    from (SELECT ttwo.arv_id,
              ttwo.result_two AS RESULT,
              (CASE
                   WHEN tone.result IS NULL
                        OR tone.result <> '4' THEN 1
                   ELSE 2
               END) AS status
       FROM
         (SELECT vmain.id,
                 vmain.arv_id,
                 vmain.test_time,
                 vmain.retry_time,
                 vmain.result AS result_two
          FROM opc_viral_load AS vmain
          INNER JOIN
            (SELECT *
             FROM
               (SELECT m.site_id,
                       m.arv_id,
                       m.test_time,
                       m.id AS id
                FROM opc_viral_load AS m
                WHERE m.test_time IS NOT NULL
                  AND m.is_remove = 0
                  AND m.site_id = @site_id
                  AND DATE_FORMAT(m.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                  AND m.arv_id IN
                    (SELECT m1.arv_id
                     FROM
                       (SELECT *
                        FROM
                          (SELECT *
                           FROM opc_arv_revision AS logmax
                           WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                             AND logmax.site_id IN (@site_id)
                           ORDER BY logmax.created_at DESC , logmax.id DESC) AS m
                        GROUP BY m.arv_id,
                                 m.stage_id) AS m1
                     WHERE m1.stage_id = m.stage_id
                       AND m1.is_remove = '0'
                       AND m1.status_of_treatment_id = '3'
                       AND m1.treatment_time <= m.test_time )
                ORDER BY m.test_time DESC, m.id DESC) AS vmain
             GROUP BY vmain.arv_id) AS subMax ON subMax.id = vmain.id) AS ttwo
       LEFT JOIN
        ( SELECT * FROM
 (SELECT t.arv_id,
                 t.id AS id,
                 t.test_time,
                 t.retry_time,
                 t.result
          FROM opc_viral_load AS t
          WHERE t.id not in
              (SELECT c.id
               FROM
                 (SELECT *
                  FROM
                    (SELECT m.site_id,
                            m.arv_id,
                            m.test_time,
                            m.id AS id
                     FROM opc_viral_load AS m
                     WHERE m.test_time IS NOT NULL
                       AND m.is_remove = 0
                       AND DATE_FORMAT(m.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                       AND m.arv_id IN
                         (SELECT m1.arv_id
                          FROM
                            (SELECT *
                             FROM
                               (SELECT *
                                FROM opc_arv_revision AS logmax
                                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                                  AND logmax.site_id IN (@site_id)
                                ORDER BY logmax.created_at DESC , logmax.id DESC) AS m
                             GROUP BY m.arv_id,
                                      m.stage_id) AS m1
                          WHERE m1.stage_id = m.stage_id
                            AND m1.is_remove = '0'
                            AND m1.status_of_treatment_id = '3'
                            AND m1.treatment_time <= m.test_time)
                     ORDER BY m.test_time DESC, m.id DESC) AS vmain
                  GROUP BY vmain.site_id,
                           vmain.arv_id) AS c)
            AND t.is_remove = 0
            AND DATE_FORMAT(t.test_time, '%Y-%m-%d') <= @to_date
            AND t.site_id = @site_id
            AND t.arv_id IN
              (SELECT m1.arv_id
               FROM
                 (SELECT *
                  FROM
                    (SELECT *
                     FROM opc_arv_revision AS logmax
                     WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date
                       AND logmax.site_id IN (@site_id)
                     ORDER BY logmax.created_at DESC , logmax.id DESC) AS m
                  GROUP BY m.arv_id,
                           m.stage_id) AS m1
               WHERE m1.stage_id = t.stage_id
                 AND m1.is_remove = '0'
                 AND m1.status_of_treatment_id = '3'
                 AND m1.treatment_time <= t.test_time)
   ORDER BY t.test_time DESC, t.id DESC) as vx GROUP BY vx.arv_id
 ) AS tone ON tone.arv_id = ttwo.arv_id) as main group by main.result, main.status) as table_main



UNION ALL

SELECT 
    0 as xnTrongThang,
    0 as xnLan1Duoi200,
    0 as xnLan1Tu200Den1000,
    0 as xnLan1Tren1000,
    0 as xnLan2Duoi200,
    0 as xnLan2Tu200Den1000,
    0 as xnLan2Tren1000,
    sum(table_main.duoi200) as arvBac2Duoi200,
    sum(table_main.tu200den1000) as arvBac2Tu200Den1000,
    sum(table_main.tren1000) as arvBac2Tren1000,
    0 as xn12ThangDuoi200,
    0 as xn12ThangTu200Den1000,
    0 as xn12ThangTren1000
 from (
select 
	(case When  (main.result = '1' OR main.result = '2' OR main.result = '6')  then count(main.arv_id) else 0 end) AS duoi200,
	(case When  main.result = '3' then count(main.arv_id) else 0 end) AS tu200den1000,
	(case When  main.result = '4' then count(main.arv_id) else 0 end) AS tren1000
FROM (
SELECT  m.arv_id as arv_id,
		m.result as result,
                m.test_time as test_time
    FROM (
           SELECT 
		t.id,
                t.arv_id as arv_id,
                t.test_time as test_time,
				
                t.result as result,
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
                                                    logmax.is_remove as is_remove,
                                                    logmax.treatment_time as treatment_time
                                                    FROM opc_arv_revision as logmax 
                                                    WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                                    order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) 
                as main_log
                ON main_log.arv_id_log = t.arv_id AND main_log.stage_id_log = t.stage_id
                WHERE t.is_remove = 0  AND t.id IN (	SELECT 
		c.id 
		FROM (select * FROM (SELECT
		m.site_id,
		m.arv_id,
		m.test_time, 
		m.id as id
   FROM opc_viral_load as m 
   WHERE m.test_time is not null 
	AND m.is_remove = 0  
	AND DATE_FORMAT(m.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
	AND m.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = m.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= m.test_time) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                AND t.site_id = @site_id 
                AND t.test_time BETWEEN @from_date AND @to_date
				group by t.arv_id ) as m
                where m.treatment_stage = '3' AND m.TREATMENT_REGIMENT_STAGE = '2' AND m.is_remove = 0 AND m.treatment_time <= m.test_time
) as main group by main.result 

) as table_main

UNION ALL

SELECT 
    0 as xnTrongThang,
    0 as xnLan1Duoi200,
    0 as xnLan1Tu200Den1000,
    0 as xnLan1Tren1000,
    0 as xnLan2Duoi200,
    0 as xnLan2Tu200Den1000,
    0 as xnLan2Tren1000,
    0 as arvBac2Duoi200,
    0 as arvBac2Tu200Den1000,
    0 as arvBac2Tren1000,
    sum(table_main.duoi200)as xn12ThangDuoi200,
    sum(table_main.tu200den1000)as xn12ThangTu200Den1000,
    sum(table_main.tren1000) as xn12ThangTren1000
 from (
select 
	(case When  (main.result = '1' OR main.result = '2' OR main.result = '6')  then count(main.arv_id) else 0 end) AS duoi200,
	(case When  main.result = '3' then count(main.arv_id) else 0 end) AS tu200den1000,
	(case When  main.result = '4' then count(main.arv_id) else 0 end) AS tren1000
FROM (
SELECT  m.arv_id as arv_id,
		m.result as result
    FROM (
           SELECT 
		t.id,
                t.arv_id as arv_id,
                t.test_time as test_time,		
                t.result as result,
                main_log.treatment_time AS treatment_time,
                main_log.is_remove AS is_remove
                FROM opc_viral_load as t
                INNER JOIN (select * from (SELECT   logmax.id,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log,
                                                    logmax.status_of_treatment_id as treatment_stage,
                                                    logmax.TREATMENT_REGIMENT_STAGE as TREATMENT_REGIMENT_STAGE,
                                                    logmax.is_remove as is_remove,
                                                    logmax.treatment_time as treatment_time
                                                    FROM opc_arv_revision as logmax 
                                                    WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                                    order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) 
                as main_log
                ON main_log.arv_id_log = t.arv_id AND main_log.stage_id_log = t.stage_id 
                WHERE t.is_remove = 0  AND t.id IN (	SELECT 
		c.id 
		FROM (select * FROM (SELECT
		m.site_id,
		m.arv_id,
		m.test_time, 
		m.id as id
   FROM opc_viral_load as m 
   WHERE m.test_time is not null 
	AND m.is_remove = 0  
	AND DATE_FORMAT(m.test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
	AND m.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = m.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= m.test_time) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                AND t.site_id = @site_id 
                AND t.test_time BETWEEN @from_date AND @to_date
				group by t.arv_id ) as m
                where m.is_remove = 0 
				AND TIMESTAMPDIFF(MONTH, LAST_DAY(m.treatment_time), LAST_DAY(m.test_time)  + INTERVAL 1 DAY) = 11
                                AND m.treatment_time <= m.test_time
) as main group by main.result 

) as table_main


) as tableFinal


