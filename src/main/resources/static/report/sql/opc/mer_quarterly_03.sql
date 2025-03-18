SELECT 
    'tuvong' AS tuvong,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
            (case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
            (case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
            (case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
                    table01.gender_id,
                    YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                        logmax.arv_id as arv_id_log,
                                                        logmax.stage_id as stage_id_log,
                                                        logmax.end_case,
                                                        logmax.end_time,
							logmax.is_remove,
                                                        logmax.registration_type,
							logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.end_case = '2'
                AND main_log.end_time BETWEEN @from_date AND @to_date  
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL 

SELECT 
    'duoi3thang' AS duoi3thang,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
		(case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
		(case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
		(case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
				table01.gender_id,
				YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                logmax.arv_id as arv_id_log,
                                                logmax.stage_id as stage_id_log,
                                                logmax.end_case,
                                       		logmax.end_time,
                                                logmax.is_remove,
                                       		logmax.registration_type,
                                                logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.end_case = '4'
                AND main_log.end_time BETWEEN @from_date AND @to_date  
             	AND (MONTH(@to_date) - MONTH(main_log.treatment_time) + 1 + (YEAR(@to_date) - YEAR(main_log.treatment_time)) * 12) < 3
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL 

SELECT 
    'tren3thang' AS tren3thang,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
		(case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
		(case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
		(case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
				table01.gender_id,
				YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log,
                                                    logmax.end_case,
                                                    logmax.end_time,
                                                    logmax.is_remove,
                                                    logmax.registration_type,
                                                    logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.end_case = '4'
                AND main_log.end_time BETWEEN @from_date AND @to_date  
             	AND (MONTH(@to_date) - MONTH(main_log.treatment_time) + 1 + (YEAR(@to_date) - YEAR(main_log.treatment_time)) * 12) >= 3
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL 

SELECT 
    'chuyendi' AS chuyendi,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
		(case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
		(case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
		(case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
				table01.gender_id,
				YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                logmax.arv_id as arv_id_log,
                                                logmax.stage_id as stage_id_log,
                                                logmax.end_case,
                                       		logmax.end_time,
                                                logmax.is_remove,
                                       		logmax.registration_type,
                                                logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.end_case = '3'
                AND main_log.end_time BETWEEN @from_date AND @to_date  
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id


UNION ALL 

SELECT 
    'botri' AS botri,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
		(case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
		(case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
		(case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
				table01.gender_id,
				YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                logmax.arv_id as arv_id_log,
                                                logmax.stage_id as stage_id_log,
                                                logmax.end_case,
                                       		logmax.end_time,
                                                logmax.is_remove,
                                       		logmax.registration_type,
                                                logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.end_case IN ('1','5') 
                AND main_log.end_time BETWEEN @from_date AND @to_date  
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL 

SELECT 
    'quaylai' AS quaylai,
    table03.gender_id,
coalesce(SUM(table03.underOneAge), 0) as underOneAge,       
coalesce(SUM(table03.oneToFourteen), 0) as oneToFourteen,
coalesce(SUM(table03.overFifteen), 0) as overFifteen
FROM (
	SELECT table02.gender_id,
		(case When table02.age < 1 then count(table02.arv_id) else 0 end) AS underOneAge,
		(case When table02.age >= 1 AND table02.age <= 14 then count(table02.arv_id) else 0 end) AS oneToFourteen,
		(case When table02.age >= 15 then count(table02.arv_id) else 0 end) AS overFifteen
	FROM (
		select 	table01.arv_id,
				table01.gender_id,
				YEAR(table01.treatment_time) - YEAR(table01.dob) as age
		 from (
			SELECT *
			FROM (SELECT m.patient_id,
					m.arv_id as arv_id,
					m.appoinment_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_visit e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.appoinment_time is not null 
						AND DATEDIFF(@to_date, e.appoinment_time) > 28 
                        AND DATEDIFF(@to_date, e.appoinment_time) < 118 
					ORDER BY e.appoinment_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
                                                logmax.arv_id as arv_id_log,
                                                logmax.stage_id as stage_id_log,
                                                logmax.end_case,
                                       		logmax.end_time,
                                                logmax.is_remove,
                                       		logmax.registration_type,
                                                logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1  group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0
             	AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_test.appoinment_time, '%Y-%m-%d')
             	AND main_log.registration_type  = '2' 
                AND main_log.treatment_time BETWEEN @from_date AND @to_date  
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id



