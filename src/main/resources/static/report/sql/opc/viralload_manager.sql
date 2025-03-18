SELECT 
    main.site_id,
    coalesce(SUM(main.xnbac1), 0) as xnbac1,
    coalesce(SUM(main.xnbac2), 0) as xnbac2,
    coalesce(SUM(main.xn6thang), 0) as xn6thang,
    coalesce(SUM(main.xn12thang), 0) as xn12thang,
    coalesce(SUM(main.xnkhac), 0) as xnkhac,
    coalesce(SUM(main.tbdt), 0) as tbdt,
    coalesce(SUM(main.pnmt), 0) as pnmt,
    coalesce(SUM(main.xnLan1phatHien), 0) as xnLan1phatHien,
    coalesce(SUM(main.xnLan1Duoi200), 0) as xnLan1Duoi200,
    coalesce(SUM(main.xnLan1Tu200Den1000), 0) as xnLan1Tu200Den1000,
    coalesce(SUM(main.xnLan1Tren1000), 0) as xnLan1Tren1000,
    coalesce(SUM(main.tuvan), 0) as tuvan,
    coalesce(SUM(main.xnLan2duoi1000), 0) as xnLan2duoi1000,
    coalesce(SUM(main.xnLan2tren1000), 0) as xnLan2tren1000,
    coalesce(SUM(main.thatbaiphacdo), 0) as thatbaiphacdo
FROM (
    SELECT
	m.site_id,
	COUNT(m.arv_id) as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE,
            sub.is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.stage_id,
                        t.result,
                        main_log.treatment_stage AS treatment_stage,
                        main_log.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
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
                    WHERE t.is_remove = 0  
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3' AND sub.TREATMENT_REGIMENT_STAGE = '1'
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

    SELECT
	m.site_id,
	0 as xnbac1,
        COUNT(m.arv_id) as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE,
            sub.is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.stage_id,
                        t.result,
                        main_log.treatment_stage AS treatment_stage,
                        main_log.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
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
                    WHERE t.is_remove = 0  
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3' AND sub.TREATMENT_REGIMENT_STAGE = '2'
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

    SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        COUNT(m.arv_id) as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE,
            sub.is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.stage_id,
                        t.result,
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
                    WHERE t.is_remove = 0  
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3') ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) >= 5
            AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) < 11
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        COUNT(m.arv_id) as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE,
            sub.treatment_time as treatment_time,
            sub.is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.stage_id,
                        t.result,
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
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3') ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) = 11
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL


SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        COUNT(m.arv_id) as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage AS treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
            sub.treatment_time AS treatment_time,
            sub.is_remove AS is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '1') as cause1,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '4') as cause4,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '5') as cause5,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '7') as cause7,
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
  
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            AND ( sub.cause1 = '1' OR sub.cause4 = '4' OR sub.cause5 = '5' OR sub.cause7 = '7' )
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        COUNT(m.arv_id) as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage AS treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
            sub.treatment_time AS treatment_time,
            sub.is_remove AS is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '6') as cause,
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
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            AND sub.cause = '6'
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        COUNT(m.arv_id) as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        0 as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id,
            sub.treatment_stage AS treatment_stage,
            sub.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
            sub.treatment_time AS treatment_time,
            sub.is_remove AS is_remove
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '8') as cause,
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
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            AND sub.cause = '8'
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

SELECT 
    table_main.site_id,
    0 as xnbac1,
    0 as xnbac2,
    0 as xn6thang,
    0 as xn12thang,
    0 as xnkhac,
    0 as tbdt,
    0 as pnmt,
    sum(table_main.result_11) as xnLan1phatHien,
    sum(table_main.result_12) as xnLan1Duoi200,
    sum(table_main.result_13) as xnLan1Tu200Den1000,
    sum(table_main.result_14) as xnLan1Tren1000,
    0 as tuvan,
    sum(table_main.result_21) as xnLan2duoi1000,
    sum(table_main.result_22) as xnLan2tren1000,
    0 as thatbaiphacdo
 from (select
	main.site_id,
	(case When  (main.result = '1' OR main.result = '6') AND main.status = '1' then count(main.arv_id) else 0 end) AS result_11,
        (case When  main.result = '2' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_12,
	(case When  main.result = '3' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_13,
	(case When  main.result = '4' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_14,
	(case When  main.result <> '4' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_21,
	(case When  main.result = '4' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_22
 from (SELECT
	ttwo.site_id as site_id,
    ttwo.arv_id,
    ttwo.result_two as result,
    (case When  tone.result is null or tone.result <> '4' then 1 else 2 end) AS status
FROM (  SELECT
            vmain.id,
            vmain.site_id,
            vmain.arv_id,
            vmain.test_time,
            vmain.retry_time,
            vmain.result as result_two
        FROM opc_viral_load as vmain
        INNER JOIN (
                    select * FROM (SELECT
                    m.site_id,
                    m.arv_id,
                    m.test_time, 
                    m.id as id
			   FROM opc_viral_load as m 
			   WHERE m.test_time is not null 
				AND m.is_remove = 0  
				AND m.site_id IN (@site_id) AND m.test_time BETWEEN @from_date AND @to_date
				AND m.arv_id IN (SELECT m1.arv_id 
				FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
				WHERE m1.stage_id = m.stage_id  and m1.is_remove = '0'
                and m1.status_of_treatment_id = '3' AND m1.treatment_time <= m.test_time) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as subMax on subMax.id = vmain.id
        ) as ttwo
        LEFT JOIN (
        SELECT * FROM (
         
        SELECT
            t.site_id,
            t.arv_id, 
            t.test_time as  test_time, 
            t.id,
            t.retry_time,
            t.result
        FROM opc_viral_load as t 
        WHERE t.id not in (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.test_time, 
		mmax.id as id
   FROM opc_viral_load as mmax 
   WHERE mmax.test_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.test_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3' AND m1.treatment_time <= mmax.test_time) ORDER BY  mmax.test_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c) 

        AND t.is_remove = 0  
        AND t.site_id IN (@site_id)
        AND DATE_FORMAT(t.test_time, '%Y-%m-%d') <= @to_date
        AND t.arv_id IN (SELECT m1.arv_id 
                                    FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
                                    WHERE m1.stage_id = t.stage_id and m1.is_remove = '0' and m1.status_of_treatment_id = '3' )
        ORDER BY  t.test_time desc, t.id desc
        ) as mm GROUP BY mm.site_id, mm.arv_id
) AS tone ON tone.arv_id = ttwo.arv_id) as main group by main.site_id, main.result, main.status) as table_main group by table_main.site_id

UNION ALL

SELECT
	m.site_id,
	0 as xnbac1,
        0 as xnbac2,
        0 as xn6thang,
        0 as xn12thang,
        0 as xnkhac,
        0 as tbdt,
        0 as pnmt,
        0 as xnLan1phatHien,
        0 as xnLan1Duoi200,
        0 as xnLan1Tu200Den1000,
        0 as xnLan1Tren1000,
        COUNT(m.arv_id) as tuvan,
        0 as xnLan2duoi1000,
        0 as xnLan2tren1000,
        0 as thatbaiphacdo
    FROM (SELECT
            sub.site_id,
            sub.arv_id
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        main_log.treatment_stage AS treatment_stage,
                        main_log.TREATMENT_REGIMENT_STAGE AS TREATMENT_REGIMENT_STAGE,
                        main_log.treatment_time AS treatment_time,
                        main_log.is_remove AS is_remove
                    FROM opc_visit as t
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
                    WHERE t.is_remove = 0 AND t.consult = 1 
                    AND t.id IN (SELECT 
		c.id 
		FROM (select * FROM (SELECT
		mmax.site_id,
		mmax.arv_id,
		mmax.examination_time, 
		mmax.id as id
   FROM opc_visit as mmax 
   WHERE mmax.examination_time is not null 
	AND mmax.is_remove = 0  
	AND mmax.examination_time BETWEEN @from_date AND @to_date
	AND mmax.arv_id IN (SELECT m1.arv_id 
	FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
	WHERE m1.stage_id = mmax.stage_id and m1.is_remove = '0'
		and m1.status_of_treatment_id = '3') ORDER BY  mmax.examination_time desc, mmax.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c)
                    AND t.site_id IN (@site_id) 
                    AND t.examination_time BETWEEN @from_date AND @to_date
                      ) as sub
            WHERE sub.is_remove = 0 
            AND sub.treatment_stage='3'
            GROUP BY sub.site_id, sub.arv_id
        ) as m GROUP BY m.site_id

UNION ALL

    SELECT 
    table_main.site_id,
    0 as xnbac1,
    0 as xnbac2,
    0 as xn6thang,
    0 as xn12thang,
    0 as xnkhac,
    0 as tbdt,
    0 as pnmt,
    0 as xnLan1phatHien,
    0 as xnLan1Duoi200,
    0 as xnLan1Tu200Den1000,
    0 as xnLan1Tren1000,
    0 as tuvan,
    0 as xnLan2duoi1000,
    0 as xnLan2tren1000,
    sum(table_main.result_22) as thatbaiphacdo
 from (select
	main.site_id,
	(case When  (main.result = '1' OR main.result = '6') AND main.status = '1' then count(main.arv_id) else 0 end) AS result_11,
    (case When  main.result = '2' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_12,
	(case When  main.result = '3' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_13,
	(case When  main.result = '4' AND main.status = '1' then count(main.arv_id) else 0 end) AS result_14,
	(case When  main.result <> '4' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_21,
	(case When  main.result = '4' AND main.status = '2' then count(main.arv_id) else 0 end) AS result_22
 from (SELECT
	ttwo.site_id as site_id,
    ttwo.arv_id,
    ttwo.result_two as result,
    (case When  tone.result is null or tone.result <> '4' then 1 else 2 end) AS status
FROM (  SELECT
            vmain.id,
            vmain.site_id,
            vmain.arv_id,
            vmain.test_time,
            vmain.retry_time,
            vmain.result as result_two
        FROM opc_viral_load as vmain
        INNER JOIN (
					select * FROM (SELECT
					m.site_id,
					m.arv_id,
					m.test_time, 
					m.id as id
			   FROM opc_viral_load as m 
			   WHERE m.test_time is not null 
				AND m.is_remove = 0  
				AND m.site_id IN (@site_id) AND m.test_time BETWEEN @from_date AND @to_date
				AND m.arv_id IN (SELECT m1.arv_id 
				FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
				WHERE m1.stage_id = m.stage_id AND m1.treatment_time <= m.test_time and m1.is_remove = '0'
                and m1.status_of_treatment_id = '3' 
                and m1.regiment_date is not null
                and m1.regiment_date BETWEEN @from_date AND @to_date
                
                ) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as subMax on subMax.id = vmain.id
        ) as ttwo
        LEFT JOIN (
        SELECT * FROM (
         
        SELECT
			t.site_id,
            t.arv_id, 
            t.test_time as  test_time, 
            t.id,
            t.retry_time,
            t.result
        FROM opc_viral_load as t 
        WHERE t.id not in (SELECT 
                                c.id 
                           FROM (select * FROM (SELECT
					m.site_id,
					m.arv_id,
					m.test_time, 
					m.id as id
			   FROM opc_viral_load as m 
			   WHERE m.test_time is not null 
				AND m.is_remove = 0  
				AND m.site_id IN (@site_id) AND m.test_time BETWEEN @from_date AND @to_date
				AND m.arv_id IN (SELECT m1.arv_id 
				FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
				WHERE m1.stage_id = m.stage_id AND m1.treatment_time <= m.test_time and m1.is_remove = '0'
                and m1.status_of_treatment_id = '3' ) ORDER BY  m.test_time desc, m.id desc) as vmain GROUP BY vmain.site_id, vmain.arv_id) as c) 

        AND t.is_remove = 0  
        AND t.site_id IN (@site_id) 
        AND t.arv_id IN (SELECT m1.arv_id 
                                    FROM (select * from (SELECT *
		FROM opc_arv_revision as logmax 
				WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                order by  logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id) as m1
                                    WHERE m1.stage_id = t.stage_id and m1.is_remove = '0' AND m1.treatment_time <= t.test_time and m1.status_of_treatment_id = '3')
        ORDER BY  t.test_time desc, t.id desc
        ) as mm GROUP BY mm.site_id, mm.arv_id
) AS tone ON tone.arv_id = ttwo.arv_id) as main group by main.site_id, main.result, main.status) as table_main group by table_main.site_id

) as main GROUP BY main.site_id