SELECT 
    main.gender_id,
    SUM(IF(main.registration_time IS NOT NULL AND main.dob <> '' AND (DATEDIFF(main.registration_time, main.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(main.registration_time IS NOT NULL AND main.dob <> '' AND (DATEDIFF(main.registration_time, main.dob) > 0) AND (DATEDIFF(main.registration_time, main.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(main.registration_time IS NOT NULL AND main.dob <> '' AND (DATEDIFF(main.registration_time, main.dob) >= 15), 1, 0)) overFifteen
FROM(
    SELECT 
        p.gender_id,
        r.registration_time,
        p.dob dob
    FROM 
        opc_arv_revision r join opc_patient p
    ON
        r.patient_id = p.id
        AND r.is_remove = 0
    WHERE
        r.status_of_treatment_id = '2'
        AND r.site_id = @site_id
        AND r.registration_time <= @to_date
        AND r.id IN (SELECT Max(id) FROM opc_arv_revision as log WHERE log.stage_id = r.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date)
    ) main GROUP BY main.gender_id