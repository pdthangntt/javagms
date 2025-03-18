SELECT
    m.province,
    m.district,
    m.ward,
    CASE WHEN m.gender_id = '1' THEN COUNT(m.id) ELSE 0 END AS male,
    CASE WHEN m.gender_id = '2' THEN COUNT(m.id) ELSE 0 END AS female,
    CASE WHEN m.gender_id <> '1' AND m.gender_id <> '2' THEN COUNT(m.id) ELSE 0 END AS other
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
    e.gender_id AS gender_id
FROM pac_patient_info as e
WHERE 
    e.is_remove = false
    AND e.death_time is not null
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
AND (( DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') >= @requestDeathTimeFrom OR @requestDeathTimeFrom IS NULL OR @requestDeathTimeFrom = '') AND (DATE_FORMAT(e.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @requestDeathTimeTo OR @requestDeathTimeTo IS NULL OR @requestDeathTimeTo = '')) 
AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 
        
AND ((YEAR(e.death_time) - e.year_of_birth) >= @ageFrom OR @ageFrom IS NULL OR  @ageFrom = '') 
    AND ((YEAR(e.death_time) - e.year_of_birth) <= @ageTo OR @ageTo IS NULL OR  @ageTo = '')
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
GROUP BY m.province, m.district, m.ward, m.gender_id
