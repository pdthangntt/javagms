SELECT 
'mauSoMoi' as mauSoMoi,
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
					m.inh_from_time,
					m.inh_to_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.inh_from_time is not null 
						AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') < @from_date
					ORDER BY e.inh_from_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'mauSoCu' as mauSoCu,
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
					m.inh_from_time,
					m.inh_to_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.inh_from_time is not null 
						AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') < @from_date
					ORDER BY e.inh_from_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

-- kẾT THÚC TIÊU CHÍ 1

UNION ALL

SELECT 
'tuSoMoi' as tuSoMoi,
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
					m.inh_from_time,
					m.inh_to_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.inh_from_time is not null 
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') < @from_date
					ORDER BY e.inh_from_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc, logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
                                AND main_test.inh_to_time is not null
                                AND DATE_FORMAT(main_test.inh_to_time, '%Y-%m-%d') <= @to_date
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'tuSoCu' as tuSoCu,
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
					m.inh_from_time,
					m.inh_to_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.inh_from_time is not null 
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                                AND DATE_FORMAT(e.inh_from_time, '%Y-%m-%d') < @from_date
					ORDER BY e.inh_from_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
                                AND main_test.inh_to_time is not null
                                AND DATE_FORMAT(main_test.inh_to_time, '%Y-%m-%d') <= @to_date
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

-- kẾT THÚC TIÊU CHÍ 2
UNION ALL

SELECT 
'itNhat1LanMoi' as itNhat1LanMoi,
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
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                                AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
                                AND main_test.lao_test_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'itNhat1LanCu' as mauSoCu,
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
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                                AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
                                AND main_test.lao_test_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id


-- kẾT THÚC TIÊU CHÍ 3
UNION ALL

SELECT 
'sangLocNghiLaoMoi' as itNhat1LanMoi,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                                AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d')  BETWEEN @from_date AND @to_date
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
                                AND main_test.lao_test_time >= main_log.treatment_time
                                AND (select coalesce(SUM(v1.parameter_id), 0) as sum FROM(select v.parameter_id, v.object_id from opc_parameter as v where v.type='test') as v1 where v1.object_id = main_test.test_id) <> 0
                                AND 1 NOT IN (select v.parameter_id from opc_parameter as v where v.type='test' AND v.object_id = main_test.test_id)
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'sangLocNghiLaoCu' as mauSoCu,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                               AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
                                AND main_test.lao_test_time >= main_log.treatment_time
                                AND (select coalesce(SUM(v1.parameter_id), 0) as sum FROM(select v.parameter_id, v.object_id from opc_parameter as v where v.type='test') as v1 where v1.object_id = main_test.test_id) <> 0
                                AND 1 NOT IN (select v.parameter_id from opc_parameter as v where v.type='test' AND v.object_id = main_test.test_id)
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id


-- kẾT THÚC TIÊU CHÍ 4
UNION ALL

SELECT 
'chuanDoanLaoMoi' as itNhat1LanMoi,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                                AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                                                AND e.lao_result = '2'
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
                                AND main_test.lao_test_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'chuanDoanLaoCu' as mauSoCu,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_test_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_test_time is not null 
                                                AND DATE_FORMAT(e.lao_test_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                                                AND e.lao_result = '2'
					ORDER BY e.lao_test_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
                                AND main_test.lao_test_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id
-- kẾT THÚC TIÊU CHÍ 5
UNION ALL

SELECT 
'dieuTriLaoMoi' as itNhat1LanMoi,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_start_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_start_time is not null 
                                                AND DATE_FORMAT(e.lao_start_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
					ORDER BY e.lao_start_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') BETWEEN @before_six_month AND @from_date
                                AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @from_date
                                AND main_test.lao_start_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id

UNION ALL

SELECT 
'dieuTriLaoCu' as mauSoCu,
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
			FROM (SELECT    m.id as test_id,
                                        m.patient_id,
					m.arv_id as arv_id,
					m.inh_from_time,
					m.inh_to_time,
                                        m.lao_start_time,
					m.stage_id
					FROM 
				(SELECT * FROM opc_test e 
					WHERE e.is_remove = 0
						AND e.site_id IN (@site_id)
						AND e.lao_start_time is not null 
                                                AND DATE_FORMAT(e.lao_start_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
					ORDER BY e.lao_start_time desc, e.id desc) as m group by m.arv_id) as main_test 
			INNER JOIN (select * from (SELECT logmax.id,
											logmax.arv_id as arv_id_log,
											logmax.stage_id as stage_id_log,
											logmax.status_of_treatment_id,
											logmax.is_remove,
											logmax.treatment_time
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc , logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id, p.fullname FROM opc_patient as p)  as patient
			ON main_test.arv_id = main_log.arv_id_log AND main_test.stage_id = main_log.stage_id_log AND patient.id_patient = main_test.patient_id
			WHERE main_log.is_remove = 0 
				AND main_log.status_of_treatment_id = '3'
				-- DIEUKIEN thêm VÀO
				AND DATE_FORMAT(main_log.treatment_time, '%Y-%m-%d') < @before_six_month
                                AND main_test.lao_start_time >= main_log.treatment_time
				-- kẾT THÚC DIEUKIEN
	) as table01 ) as table02 group by table02.gender_id, table02.age
) as table03 group by table03.gender_id