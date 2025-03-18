SELECT 
'permanent' as permanent,
main.province,
coalesce(SUM(main.hdodp), 0) AS hdodp,
coalesce(SUM(main.dlax), 0) AS dlax,
coalesce(SUM(main.dt), 0)   AS dt,
coalesce(SUM(main.cdnk), 0) AS cdnk,
coalesce(SUM(main.ctt), 0)  AS ctt,
coalesce(SUM(main.md), 0)   AS md,
coalesce(SUM(main.cxdd), 0) AS cxdd,
coalesce(SUM(main.kctt), 0) AS kctt,
coalesce(SUM(main.khongthongtin), 0) as khongthongtin
FROM (	SELECT 	m.permanent_province_id province, 
                CASE WHEN m.residentID = '2' THEN COUNT(m.id) ELSE 0 END                            AS hdodp,
                CASE WHEN m.residentID = '7' THEN COUNT(m.id) ELSE 0 END                            AS dlax,
                CASE WHEN m.residentID = '4' THEN COUNT(m.id) ELSE 0 END                            AS dt,
                CASE WHEN m.residentID = '5' THEN COUNT(m.id) ELSE 0 END                            AS cdnk,
                CASE WHEN m.residentID = '8' THEN COUNT(m.id) ELSE 0 END                            AS ctt,
                CASE WHEN m.residentID = '1' THEN COUNT(m.id) ELSE 0 END                            AS md,
                CASE WHEN m.residentID = '6' THEN COUNT(m.id) ELSE 0 END                            AS cxdd,
                CASE WHEN m.residentID = '3' THEN COUNT(m.id) ELSE 0 END                            AS kctt,
                CASE WHEN m.residentID <> '1' 
                      AND m.residentID <> '2' 
                      AND m.residentID <> '3' 
                      AND m.residentID <> '4' 
                      AND m.residentID <> '5' 
                      AND m.residentID <> '6' 
                      AND m.residentID <> '7' 
                      AND m.residentID <> '8' 
                      OR  m.residentID is null THEN COUNT(m.id) ELSE 0 END                          AS khongthongtin
	FROM (
		SELECT 	e.id,
                        e.permanent_province_id,
                        e.detect_province_id,
                        e.province_id,
                        e.status_of_resident_id as residentID
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
			-- Dieu kien
                        
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
                 
	) as m GROUP BY m.permanent_province_id, m.residentID
) as main GROUP BY main.province

UNION ALL


SELECT 
'notmanage' as notmanage,
main.province,
coalesce(SUM(main.hdodp), 0) AS hdodp,
coalesce(SUM(main.dlax), 0) AS dlax,
coalesce(SUM(main.dt), 0)   AS dt,
coalesce(SUM(main.cdnk), 0) AS cdnk,
coalesce(SUM(main.ctt), 0)  AS ctt,
coalesce(SUM(main.md), 0)   AS md,
coalesce(SUM(main.cxdd), 0) AS cxdd,
coalesce(SUM(main.kctt), 0) AS kctt,
coalesce(SUM(main.khongthongtin), 0) as khongthongtin
FROM (	SELECT 	m.detect_province_id province, 
                CASE WHEN m.residentID = '2' THEN COUNT(m.id) ELSE 0 END                            AS hdodp,
                CASE WHEN m.residentID = '7' THEN COUNT(m.id) ELSE 0 END                            AS dlax,
                CASE WHEN m.residentID = '4' THEN COUNT(m.id) ELSE 0 END                            AS dt,
                CASE WHEN m.residentID = '5' THEN COUNT(m.id) ELSE 0 END                            AS cdnk,
                CASE WHEN m.residentID = '8' THEN COUNT(m.id) ELSE 0 END                            AS ctt,
                CASE WHEN m.residentID = '1' THEN COUNT(m.id) ELSE 0 END                            AS md,
                CASE WHEN m.residentID = '6' THEN COUNT(m.id) ELSE 0 END                            AS cxdd,
                CASE WHEN m.residentID = '3' THEN COUNT(m.id) ELSE 0 END                            AS kctt,
                CASE WHEN m.residentID <> '1' 
                      AND m.residentID <> '2' 
                      AND m.residentID <> '3' 
                      AND m.residentID <> '4' 
                      AND m.residentID <> '5' 
                      AND m.residentID <> '6' 
                      AND m.residentID <> '7' 
                      AND m.residentID <> '8' 
                      OR  m.residentID is null THEN COUNT(m.id) ELSE 0 END                          AS khongthongtin
	FROM (
		SELECT 	e.id,
                        e.permanent_province_id,
                        e.detect_province_id,
                        e.province_id,
                        e.status_of_resident_id as residentID
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
			-- Dieu kien
                        AND e.REF_ID is null AND e.status = 'new'

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
                 
	) as m GROUP BY m.detect_province_id, m.residentID
) as main GROUP BY main.province

UNION ALL

SELECT 
'hasmanage' as hasmanage,
main.province,
coalesce(SUM(main.hdodp), 0) AS hdodp,
coalesce(SUM(main.dlax), 0) AS dlax,
coalesce(SUM(main.dt), 0)   AS dt,
coalesce(SUM(main.cdnk), 0) AS cdnk,
coalesce(SUM(main.ctt), 0)  AS ctt,
coalesce(SUM(main.md), 0)   AS md,
coalesce(SUM(main.cxdd), 0) AS cxdd,
coalesce(SUM(main.kctt), 0) AS kctt,
coalesce(SUM(main.khongthongtin), 0) as khongthongtin
FROM (	SELECT 	m.province_id province, 
                CASE WHEN m.residentID = '2' THEN COUNT(m.id) ELSE 0 END                            AS hdodp,
                CASE WHEN m.residentID = '7' THEN COUNT(m.id) ELSE 0 END                            AS dlax,
                CASE WHEN m.residentID = '4' THEN COUNT(m.id) ELSE 0 END                            AS dt,
                CASE WHEN m.residentID = '5' THEN COUNT(m.id) ELSE 0 END                            AS cdnk,
                CASE WHEN m.residentID = '8' THEN COUNT(m.id) ELSE 0 END                            AS ctt,
                CASE WHEN m.residentID = '1' THEN COUNT(m.id) ELSE 0 END                            AS md,
                CASE WHEN m.residentID = '6' THEN COUNT(m.id) ELSE 0 END                            AS cxdd,
                CASE WHEN m.residentID = '3' THEN COUNT(m.id) ELSE 0 END                            AS kctt,
                CASE WHEN m.residentID <> '1' 
                      AND m.residentID <> '2' 
                      AND m.residentID <> '3' 
                      AND m.residentID <> '4' 
                      AND m.residentID <> '5' 
                      AND m.residentID <> '6' 
                      AND m.residentID <> '7' 
                      AND m.residentID <> '8' 
                      OR  m.residentID is null THEN COUNT(m.id) ELSE 0 END                          AS khongthongtin
	FROM (
		SELECT 	e.id,
                        e.permanent_province_id,
                        e.detect_province_id,
                        e.province_id,
                        e.status_of_resident_id as residentID
		FROM pac_patient_info e 
		WHERE e.is_remove = 0 AND  e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS  NULL 
			AND e.permanent_province_id <> e.detect_province_id
                        AND e.REQUEST_VAAC_TIME is null
			-- Dieu kien
                        AND e.REF_ID is not null

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
                 
	) as m GROUP BY m.province_id, m.residentID
) as main GROUP BY main.province


