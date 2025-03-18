SELECT 
    e.permanent_ward_id, 
    e.permanent_district_id, 
    CASE WHEN e.gender_id = '1' THEN COUNT(e.id) ELSE 0 END AS m,
    CASE WHEN e.gender_id = '2' THEN COUNT(e.id) ELSE 0 END AS f,
    CASE WHEN e.gender_id != '1' AND e.gender_id != '2' THEN COUNT(e.id) ELSE 0 END AS o
FROM pac_patient_info as e
WHERE  e.permanent_district_id is not null AND e.IS_REMOVE = false
    AND e.accept_time is not null
    AND e.review_ward_time is not null
    AND e.review_province_time is not null
    AND (e.province_id IN (@provinceID) OR coalesce(@provinceID, null) IS NULL)
    AND (e.permanent_district_id IN (@districtID) OR coalesce(@districtID, null) IS NULL)
    AND (e.permanent_ward_id IN (@wardID) OR coalesce(@wardID, null) IS NULL)
    AND (@gender_id IS NULL OR @gender_id = '' OR e.gender_id = @gender_id )
    AND (e.job_id = @job OR @job IS NULL OR @job = '' )
    AND (e.OBJECT_GROUP_ID = @test_object OR @test_object IS NULL OR @test_object = '' )
    AND (e.RISK_BEHAVIOR_ID = @risk OR @risk IS NULL OR @risk = '' )
    AND (e.BLOOD_BASE_ID = @blood OR @blood IS NULL OR @blood = '' )
    AND (e.STATUS_OF_TREATMENT_ID = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (e.STATUS_OF_RESIDENT_ID = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (@from_date IS NULL OR @from_date = '' OR e.confirm_time >= @from_date)
    AND (@to_date IS NULL OR @to_date = '' OR e.confirm_time <= @to_date)
    AND ( 
        @yearOld IS NULL OR @yearOld = '' OR ( @yearOld = '1' AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) < 15 ) 
        OR ( @yearOld = '2' AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) >= 15  AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) <= 25 )
        OR ( @yearOld = '3' AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) >= 26  AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) <= 49 )
        OR ( @yearOld = '4' AND (YEAR(CURDATE()) - e.YEAR_OF_BIRTH) >= 50))
group by e.permanent_ward_id, e.permanent_district_id, e.gender_id;