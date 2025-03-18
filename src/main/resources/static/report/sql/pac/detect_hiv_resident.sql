SELECT
    m.province,
    m.district,
    m.ward,
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
    e.status_of_resident_id AS residentID
FROM pac_patient_info as e
WHERE 
    e.is_remove = false
    AND (e.has_health_insurance = @hasHealthInsurance OR @hasHealthInsurance IS NULL OR @hasHealthInsurance = '')
    AND (
        (
            @manageStatus = '-1' 
            AND (
                (@isPac AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)))
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)
            )
        )
        OR (@isPac AND @manageStatus = '1' AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND ((e.permanent_province_id = @provinceID AND e.province_id = @provinceID AND e.detect_province_id = @provinceID) OR (e.request_vaac_time is not null AND e.province_id = @provinceID)))
        OR (@manageStatus = '2' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)     
    )
    AND (@patientStatus IS NULL OR @patientStatus = '' OR (@patientStatus = 'alive' AND (e.death_time IS NULL OR e.death_time = '')) OR (@patientStatus = 'die' AND (e.death_time IS NOT NULL OR e.death_time <> '')))
    AND (e.job_id = @job OR @job IS NULL OR @job = '' )
    AND (e.race_id = @race OR @race IS NULL OR @race = '' )
    AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )

    AND (e.status_of_treatment_id = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (e.status_of_resident_id = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (e.mode_of_transmission_id = @transmission OR @transmission IS NULL OR @transmission = '' )
    AND (( DATE_FORMAT(e.updated_at , '%Y-%m-%d') >= @updateTimeFrom OR @updateTimeFrom IS NULL OR @updateTimeFrom = '') AND (DATE_FORMAT(e.updated_at , '%Y-%m-%d') <= @updateTimeTo OR @updateTimeTo IS NULL OR @updateTimeTo = '')) 

    AND (( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') >= @confirmTimeFrom OR @confirmTimeFrom IS NULL OR @confirmTimeFrom = '') AND (DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @confirmTimeTo OR @confirmTimeTo IS NULL OR @confirmTimeTo = '')) 
    AND (( DATE_FORMAT(e.review_province_time , '%Y-%m-%d') >= @manageTimeFrom OR @manageTimeFrom IS NULL OR @manageTimeFrom = '') AND (DATE_FORMAT(e.review_province_time , '%Y-%m-%d') <= @manageTimeTo OR @manageTimeTo IS NULL OR @manageTimeTo = '')) 
    AND (( DATE_FORMAT(e.created_at , '%Y-%m-%d') >= @inputTimeFrom OR @inputTimeFrom IS NULL OR @inputTimeFrom = '') AND (DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @inputTimeTo OR @inputTimeTo IS NULL OR @inputTimeTo = '')) 
    AND (( DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') >= @aidsFrom OR @aidsFrom IS NULL OR @aidsFrom = '') AND (DATE_FORMAT(e.aids_status_date , '%Y-%m-%d') <= @aidsTo OR @aidsTo IS NULL OR @aidsTo = '')) 
    
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e.province_id = @provinceID OR @provinceID is null)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)
    AND (e.confirm_test_id = @placeTest OR @placeTest is null OR @placeTest = '')
    AND (e.site_treatment_facility_id = @facility OR @facility is null OR @facility = '')    

    AND ((@addressFilter = 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.permanent_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.permanent_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.permanent_ward_id IN (@permanentWardID)))
    OR (@addressFilter <> 'hokhau' AND ('-1' IN (@permanentProvinceID) OR e.current_province_id IN (@permanentProvinceID))
        AND ('-1' IN (@permanentDistrictID) OR e.current_district_id IN (@permanentDistrictID))
        AND ('-1' IN (@permanentWardID) OR e.current_ward_id IN (@permanentWardID))))
) AS m
GROUP BY m.province, m.district, m.ward, m.residentID
