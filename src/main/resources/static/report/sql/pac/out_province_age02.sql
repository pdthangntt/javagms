SELECT 
'permanent' as permanent,
main.province,
coalesce(SUM(main.under15), 0) as under15,       
coalesce(SUM(main.from15to24), 0) as from15to24,
coalesce(SUM(main.from25to49), 0) as from25to49,
coalesce(SUM(main.over49), 0) as over49
FROM (
		SELECT 	m.permanent_province_id province, 
			(case When m.age < 15 then count(m.id) else 0 end) AS under15,
			(case When ( m.age >= 15 AND m.age <= 24) then count(m.id) else 0 end) AS from15to24,
			(case When ( m.age >= 25 AND m.age <= 49) then count(m.id) else 0 end) AS from25to49,
			(case When m.age > 49 then count(m.id) else 0 end) AS over49
	FROM (
		SELECT 	e.id,
				e.permanent_province_id,
				e.detect_province_id,
				e.province_id,
                YEAR(e.confirm_time) - e.year_of_birth as age
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
AND e.year_of_birth is not null AND e.year_of_birth > 0
			-- Dieu kien

                         AND e.confirm_time is not null
                        AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
                        AND (@patientStatus IS NULL OR @patientStatus = '' OR (@patientStatus = 'alive' AND (e.death_time IS NULL OR e.death_time = '')) OR (@patientStatus = 'die' AND (e.death_time IS NOT NULL OR e.death_time <> '')))
                        AND (e.job_id = @job OR @job IS NULL OR @job = '' )
                        AND (e.race_id = @race OR @race IS NULL OR @race = '' )
                        AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
                        AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
                        AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
                        AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
                        AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
                        AND (( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') >= @confirmTimeFrom OR @confirmTimeFrom IS NULL OR @confirmTimeFrom = '') AND (DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @confirmTimeTo OR @confirmTimeTo IS NULL OR @confirmTimeTo = '')) 
                        AND (( DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') >= @manageTimeFrom OR @manageTimeFrom IS NULL OR @manageTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') <= @manageTimeTo OR @manageTimeTo IS NULL OR @manageTimeTo = '')) 
                        AND (( DATE_FORMAT(e.created_at , '%Y-%m-%d') >= @inputTimeFrom OR @inputTimeFrom IS NULL OR @inputTimeFrom = '') AND (DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @inputTimeTo OR @inputTimeTo IS NULL OR @inputTimeTo = '')) 
                        AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
                        AND (e.confirm_test_id = @placeTest OR @placeTest is null OR @placeTest = '')
                        AND (e.site_treatment_facility_id = @facility OR @facility is null OR @facility = '')
                 
	) as m GROUP BY m.permanent_province_id, m.age
) as main GROUP BY main.province

UNION ALL

SELECT 
'notmanage' as permanent,
main.province,
coalesce(SUM(main.under15), 0) as under15,       
coalesce(SUM(main.from15to24), 0) as from15to24,
coalesce(SUM(main.from25to49), 0) as from25to49,
coalesce(SUM(main.over49), 0) as over49
FROM (
		SELECT 	m.detect_province_id province, 
			(case When m.age < 15 then count(m.id) else 0 end) AS under15,
			(case When ( m.age >= 15 AND m.age <= 24) then count(m.id) else 0 end) AS from15to24,
			(case When ( m.age >= 25 AND m.age <= 49) then count(m.id) else 0 end) AS from25to49,
			(case When m.age > 49 then count(m.id) else 0 end) AS over49
	FROM (
		SELECT 	e.id,
				e.permanent_province_id,
				e.detect_province_id,
				e.province_id,
                YEAR(e.confirm_time) - e.year_of_birth as age
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
AND e.year_of_birth is not null AND e.year_of_birth > 0
			-- Dieu kien
                        AND e.REF_ID is null AND e.status = 'new'
 AND e.confirm_time is not null
                        AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
                        AND (@patientStatus IS NULL OR @patientStatus = '' OR (@patientStatus = 'alive' AND (e.death_time IS NULL OR e.death_time = '')) OR (@patientStatus = 'die' AND (e.death_time IS NOT NULL OR e.death_time <> '')))
                        AND (e.job_id = @job OR @job IS NULL OR @job = '' )
                        AND (e.race_id = @race OR @race IS NULL OR @race = '' )
                        AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
                        AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
                        AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
                        AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
                        AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
                        AND (( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') >= @confirmTimeFrom OR @confirmTimeFrom IS NULL OR @confirmTimeFrom = '') AND (DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @confirmTimeTo OR @confirmTimeTo IS NULL OR @confirmTimeTo = '')) 
                        AND (( DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') >= @manageTimeFrom OR @manageTimeFrom IS NULL OR @manageTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') <= @manageTimeTo OR @manageTimeTo IS NULL OR @manageTimeTo = '')) 
                        AND (( DATE_FORMAT(e.created_at , '%Y-%m-%d') >= @inputTimeFrom OR @inputTimeFrom IS NULL OR @inputTimeFrom = '') AND (DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @inputTimeTo OR @inputTimeTo IS NULL OR @inputTimeTo = '')) 
                        AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
                        AND (e.confirm_test_id = @placeTest OR @placeTest is null OR @placeTest = '')
                        AND (e.site_treatment_facility_id = @facility OR @facility is null OR @facility = '')
                 
	) as m GROUP BY m.detect_province_id, m.age
) as main GROUP BY main.province

UNION ALL

SELECT 
'hasmanage' as permanent,
main.province,
coalesce(SUM(main.under15), 0) as under15,       
coalesce(SUM(main.from15to24), 0) as from15to24,
coalesce(SUM(main.from25to49), 0) as from25to49,
coalesce(SUM(main.over49), 0) as over49
FROM (
		SELECT 	m.province_id province, 
			(case When m.age < 15 then count(m.id) else 0 end) AS under15,
			(case When ( m.age >= 15 AND m.age <= 24) then count(m.id) else 0 end) AS from15to24,
			(case When ( m.age >= 25 AND m.age <= 49) then count(m.id) else 0 end) AS from25to49,
			(case When m.age > 49 then count(m.id) else 0 end) AS over49
	FROM (
		SELECT 	e.id,
				e.permanent_province_id,
				e.detect_province_id,
				e.province_id,
                YEAR(e.confirm_time) - e.year_of_birth as age
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
AND e.year_of_birth is not null AND e.year_of_birth > 0
			-- Dieu kien
                        AND e.REF_ID is not null AND e.confirm_time is not null

                        AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
                        AND (@patientStatus IS NULL OR @patientStatus = '' OR (@patientStatus = 'alive' AND (e.death_time IS NULL OR e.death_time = '')) OR (@patientStatus = 'die' AND (e.death_time IS NOT NULL OR e.death_time <> '')))
                        AND (e.job_id = @job OR @job IS NULL OR @job = '' )
                        AND (e.race_id = @race OR @race IS NULL OR @race = '' )
                        AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
                        AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
                        AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
                        AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
                        AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
                        AND (( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') >= @confirmTimeFrom OR @confirmTimeFrom IS NULL OR @confirmTimeFrom = '') AND (DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @confirmTimeTo OR @confirmTimeTo IS NULL OR @confirmTimeTo = '')) 
                        AND (( DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') >= @manageTimeFrom OR @manageTimeFrom IS NULL OR @manageTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_VAAC_TIME , '%Y-%m-%d') <= @manageTimeTo OR @manageTimeTo IS NULL OR @manageTimeTo = '')) 
                        AND (( DATE_FORMAT(e.created_at , '%Y-%m-%d') >= @inputTimeFrom OR @inputTimeFrom IS NULL OR @inputTimeFrom = '') AND (DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @inputTimeTo OR @inputTimeTo IS NULL OR @inputTimeTo = '')) 
                        AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
                        AND (e.confirm_test_id = @placeTest OR @placeTest is null OR @placeTest = '')
                        AND (e.site_treatment_facility_id = @facility OR @facility is null OR @facility = '')
                 
	) as m GROUP BY m.province_id, m.age
) as main GROUP BY main.province



