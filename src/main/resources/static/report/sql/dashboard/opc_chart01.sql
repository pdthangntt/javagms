SELECT 
coalesce(SUM(table03.luytich), 0) as luytich,              
coalesce(SUM(table03.moi), 0) as moi
FROM 
(
	SELECT count(table02.arv_id) as luytich,
		0 as moi
	FROM (SELECT 	tableLevel01.arv_id,
			tableLevel01.patient_id,
			tableLevel01.is_remove,
			tableLevel01.treatment_time,
			tableLevel01.dob,
			tableLevel01.gender_id,
			tableLevel01.age
	FROM (
		SELECT 	main.id,
				main.arv_id,
				main.patient_id,
				main.is_remove,
				main.treatment_time,
				patient.dob,
				patient.gender_id,
				YEAR(CURDATE()) - YEAR(patient.dob) as age
		FROM (
			SELECT 	*
			FROM (	SELECT  logmax.id,
                                        logmax.arv_id,
                                        logmax.patient_id,
                                        logmax.is_remove,
                                        logmax.treatment_time,
                                        logmax.status_of_treatment_id,
                                        logmax.end_time,
                                        logmax.stage_id as stage_id_log
					FROM opc_arv_revision as logmax 
					WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id) AND logmax.is_remove = 0
					order by logmax.created_at desc, logmax.id desc) as m
			group by m.arv_id , m.stage_id_log) as main
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id as gender_id, p.fullname FROM opc_patient as p)  as patient
			INNER JOIN ( SELECT s.id as id_stage, s.is_remove as remove_stage FROM opc_stage as s)  as stage
			ON main.patient_id = patient.id_patient AND stage.id_stage = main.stage_id_log
		WHERE main.is_remove = 0 AND stage.remove_stage = 0 AND main.status_of_treatment_id <> '0'
                AND DATE_FORMAT(main.treatment_time, '%Y-%m-%d')   <= @to_date
	) as tableLevel01
	WHERE   tableLevel01.gender_id IN (@gender_id)  
		AND ( @age is null OR (@age = '1' AND tableLevel01.age < 15 ) OR (@age = '2' AND tableLevel01.age >= 15 ))
                group by tableLevel01.arv_id
	) as table02 

	UNION ALL

	SELECT 0 as luytich,
		count(table02.arv_id) as moi
	FROM (SELECT tableLevel01.arv_id,
			tableLevel01.patient_id,
			tableLevel01.is_remove,
			tableLevel01.treatment_time,
			tableLevel01.dob,
			tableLevel01.gender_id,
			tableLevel01.age
	FROM (
		SELECT 	main.id,
				main.arv_id,
				main.patient_id,
				main.is_remove,
				main.treatment_time,
				patient.dob,
				patient.gender_id,
				YEAR(CURDATE()) - YEAR(patient.dob) as age
		FROM (
			SELECT 	*
			FROM (	SELECT logmax.id,
                                            logmax.arv_id,
                                            logmax.patient_id,
                                            logmax.is_remove,
                                            logmax.treatment_time,
                                            logmax.status_of_treatment_id,
                                            logmax.stage_id as stage_id_log
					FROM opc_arv_revision as logmax 
					WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
					order by logmax.created_at desc, logmax.id desc) as m
			group by m.arv_id , m.stage_id_log) as main
			INNER JOIN ( SELECT p.id as id_patient, p.dob, p.gender_id as gender_id, p.fullname FROM opc_patient as p)  as patient
                        INNER JOIN ( SELECT s.id as id_stage, s.is_remove as remove_stage FROM opc_stage as s)  as stage
			ON main.patient_id = patient.id_patient AND stage.id_stage = main.stage_id_log
		WHERE main.is_remove = 0 AND stage.remove_stage = 0 AND main.status_of_treatment_id <> '0' 
                    AND DATE_FORMAT(main.treatment_time, '%Y-%m-%d') between  @from_date AND @to_date
	) as tableLevel01
	WHERE  tableLevel01.gender_id IN (@gender_id)  
		AND ( @age is null OR (@age = '1' AND tableLevel01.age < 15 ) OR (@age = '2' AND tableLevel01.age >= 15 ))
group by tableLevel01.arv_id
	) as table02 
) as table03