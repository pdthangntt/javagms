SELECT
    m.province,
    m.district,
    m.ward,
    SUM(m.male) AS male,
    SUM(m.female) AS female,
    SUM(m.other) AS other,
    SUM(m.male_death) AS male_death,
    SUM(m.female_death) AS female_death,
    SUM(m.other_death) AS other_death,
    SUM(m.male_alive) AS male_alive,
    SUM(m.female_alive) AS female_alive,
    SUM(m.other_alive) AS other_alive
FROM
(
SELECT 
    e.id,
    CASE WHEN @addressFilter = 'hokhau' THEN e.permanent_province_id ELSE e.current_province_id END AS province,
    CASE 
        WHEN (@levelDisplay = 'province') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_district_id 
        ELSE e.current_district_id 
    END AS district,
    CASE 
        WHEN (@levelDisplay = 'province' OR @levelDisplay = 'district') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_ward_id 
        ELSE e.current_ward_id 
    END AS ward,
    CASE WHEN e.gender_id = '1' THEN 1 ELSE 0 END AS male,
    CASE WHEN e.gender_id = '2' THEN 1 ELSE 0 END AS female,
    CASE WHEN e.gender_id <> '1' AND e.gender_id <> '2' THEN 1 ELSE 0 END AS other,
    0 as male_death, 
    0 as female_death, 
    0 as other_death, 
    0 as male_alive, 
    0 as female_alive, 
    0 as other_alive
FROM pac_patient_info as e
WHERE 
    e.is_remove = false 
    AND e.aids_status_date is not null 
    AND (
        (
            ((@manageStatus = '-1' AND @isTTYT = FALSE AND @isTYT = FALSE) OR (:manageStatus = '-1' AND (@isTTYT = TRUE OR @isTYT = TRUE) AND (e.source_service_id = '103' OR e.accept_time IS NOT NULL)))
            AND (
                (e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)))
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)
            )
        )
        OR (@manageStatus = '1' AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)) AND @isTTYT = FALSE AND @isTYT = FALSE)
        OR (@manageStatus = '2' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)     
    )
    
    
    AND (e.job_id = @job OR @job IS NULL OR @job = '' )
    AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e.race_id = @race OR @race IS NULL OR @race = '' )
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
    AND (( DATE_FORMAT(e.death_time , '%Y-%m-%d') >= @deathTimeFrom OR @deathTimeFrom IS NULL OR @deathTimeFrom = '') AND (DATE_FORMAT(e.death_time , '%Y-%m-%d') <= @deathTimeTo OR @deathTimeTo IS NULL OR @deathTimeTo = '')) 
    AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
    AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
    AND (( DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') >= @requestDeathTimeFrom OR @requestDeathTimeFrom IS NULL OR @requestDeathTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @requestDeathTimeTo OR @requestDeathTimeTo IS NULL OR @requestDeathTimeTo = '')) 
      
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) >= @ageFrom OR @ageFrom IS NULL OR  @ageFrom = '') 
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) <= @ageTo OR @ageTo IS NULL OR  @ageTo = '')
    AND (e.province_id = @provinceID OR @provinceID is null)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)  

    AND ((@addressFilter = 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.permanent_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.permanent_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.permanent_ward_id IN (@permanentWardID)))
    OR (@addressFilter <> 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.current_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.current_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.current_ward_id IN (@permanentWardID))))

UNION ALL 

SELECT 
    e.id,
    CASE WHEN @addressFilter = 'hokhau' THEN e.permanent_province_id ELSE e.current_province_id END AS province,
    CASE 
        WHEN (@levelDisplay = 'province') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_district_id 
        ELSE e.current_district_id 
    END AS district,
    CASE 
        WHEN (@levelDisplay = 'province' OR @levelDisplay = 'district') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_ward_id 
        ELSE e.current_ward_id 
    END AS ward,
    0 as male, 
    0 as female, 
    0 as other, 
    CASE WHEN e.gender_id = '1' THEN 1 ELSE 0 END AS male_death,
    CASE WHEN e.gender_id = '2' THEN 1 ELSE 0 END AS female_death,
    CASE WHEN e.gender_id <> '1' AND e.gender_id <> '2' THEN 1 ELSE 0 END AS other_death,
    0 as male_alive, 
    0 as female_alive, 
    0 as other_alive
FROM pac_patient_info as e
WHERE 
    e.is_remove = false 
    AND e.death_time IS NOT NULL 
    AND e.aids_status_date is not null
    AND (
        (
            ((@manageStatus = '-1' AND @isTTYT = FALSE AND @isTYT = FALSE) OR (:manageStatus = '-1' AND (@isTTYT = TRUE OR @isTYT = TRUE) AND (e.source_service_id = '103' OR e.accept_time IS NOT NULL)))
            AND (
                (e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)))
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)
            )
        )
        OR (@manageStatus = '1' AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)) AND @isTTYT = FALSE AND @isTYT = FALSE)
        OR (@manageStatus = '2' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)     
    )
    
    
    AND (e.job_id = @job OR @job IS NULL OR @job = '' )
    AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e.race_id = @race OR @race IS NULL OR @race = '' )
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
    AND (( DATE_FORMAT(e.death_time , '%Y-%m-%d') >= @deathTimeFrom OR @deathTimeFrom IS NULL OR @deathTimeFrom = '') AND (DATE_FORMAT(e.death_time , '%Y-%m-%d') <= @deathTimeTo OR @deathTimeTo IS NULL OR @deathTimeTo = '')) 
    AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
    AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
    AND (( DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') >= @requestDeathTimeFrom OR @requestDeathTimeFrom IS NULL OR @requestDeathTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @requestDeathTimeTo OR @requestDeathTimeTo IS NULL OR @requestDeathTimeTo = '')) 
      
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) >= @ageFrom OR @ageFrom IS NULL OR  @ageFrom = '') 
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) <= @ageTo OR @ageTo IS NULL OR  @ageTo = '')
    AND (e.province_id = @provinceID OR @provinceID is null)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)  

    AND ((@addressFilter = 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.permanent_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.permanent_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.permanent_ward_id IN (@permanentWardID)))
    OR (@addressFilter <> 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.current_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.current_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.current_ward_id IN (@permanentWardID))))

UNION ALL 

SELECT 
    e.id,
    CASE WHEN @addressFilter = 'hokhau' THEN e.permanent_province_id ELSE e.current_province_id END AS province,
    CASE 
        WHEN (@levelDisplay = 'province') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_district_id 
        ELSE e.current_district_id 
    END AS district,
    CASE 
        WHEN (@levelDisplay = 'province' OR @levelDisplay = 'district') THEN 0
        WHEN @addressFilter = 'hokhau' THEN e.permanent_ward_id 
        ELSE e.current_ward_id 
    END AS ward,
    0 as male, 
    0 as female, 
    0 as other, 
    0 as male_death, 
    0 as female_death, 
    0 as other_death,
    CASE WHEN e.gender_id = '1' THEN 1 ELSE 0 END AS male_alive,
    CASE WHEN e.gender_id = '2' THEN 1 ELSE 0 END AS female_alive,
    CASE WHEN e.gender_id <> '1' AND e.gender_id <> '2' THEN 1 ELSE 0 END AS other_alive
FROM pac_patient_info as e
WHERE 
    e.is_remove = false 
    AND e.death_time IS NULL 
    AND e.aids_status_date is not null
    AND (
        (
            ((@manageStatus = '-1' AND @isTTYT = FALSE AND @isTYT = FALSE) OR (:manageStatus = '-1' AND (@isTTYT = TRUE OR @isTYT = TRUE) AND (e.source_service_id = '103' OR e.accept_time IS NOT NULL)))
            AND (
                (e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)))
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)
            )
        )
        OR (@manageStatus = '1' AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)) AND @isTTYT = FALSE AND @isTYT = FALSE)
        OR (@manageStatus = '2' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)     
    )
    
    
    AND (e.job_id = @job OR @job IS NULL OR @job = '' )
    AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e.race_id = @race OR @race IS NULL OR @race = '' )
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
    AND (( DATE_FORMAT(e.death_time , '%Y-%m-%d') >= @deathTimeFrom OR @deathTimeFrom IS NULL OR @deathTimeFrom = '') AND (DATE_FORMAT(e.death_time , '%Y-%m-%d') <= @deathTimeTo OR @deathTimeTo IS NULL OR @deathTimeTo = '')) 
    AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
    AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
    AND (( DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') >= @requestDeathTimeFrom OR @requestDeathTimeFrom IS NULL OR @requestDeathTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @requestDeathTimeTo OR @requestDeathTimeTo IS NULL OR @requestDeathTimeTo = '')) 
      
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) >= @ageFrom OR @ageFrom IS NULL OR  @ageFrom = '') 
    AND ((YEAR(e.aids_status_date) - e.year_of_birth) <= @ageTo OR @ageTo IS NULL OR  @ageTo = '')
    AND (e.province_id = @provinceID OR @provinceID is null)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)  

    AND ((@addressFilter = 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.permanent_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.permanent_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.permanent_ward_id IN (@permanentWardID)))
    OR (@addressFilter <> 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.current_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.current_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.current_ward_id IN (@permanentWardID))))
) AS m
GROUP BY m.province, m.district, m.ward
