SELECT 
    m_info.province, 
    m_info.district, 
    m_info.ward,
    SUM(m_info.m) mm,
    SUM(m_info.f) ff,
    SUM(m_info.o) oo
FROM (
SELECT 
    info.permanent_province_id province,
    info.permanent_district_id district,
    info.permanent_ward_id ward,
    COALESCE(count(info.id),0) AS m,
    0 AS f,
    0 AS o
 FROM pac_patient_info info
 WHERE info.gender_id = '1'

    AND info.DEATH_TIME is not null AND info.IS_REMOVE = false
    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND (info.permanent_province_id IN (@provinceID) OR coalesce(@provinceID, null) IS NULL)
    AND (info.permanent_district_id IN (@districtID) OR coalesce(@districtID, null) IS NULL)
    AND (info.permanent_ward_id IN (@wardID) OR coalesce(@wardID, null) IS NULL)
    AND (info.gender_id = @gender_id OR @gender_id IS NULL OR @gender_id = '' )
    AND (info.job_id = @job OR @job IS NULL OR @job = '' )
    AND (info.OBJECT_GROUP_ID = @test_object OR @test_object IS NULL OR @test_object = '' )
    AND (info.RISK_BEHAVIOR_ID = @risk OR @risk IS NULL OR @risk = '' )
    AND (info.BLOOD_BASE_ID = @blood OR @blood IS NULL OR @blood = '' )
    AND (info.STATUS_OF_TREATMENT_ID = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (info.STATUS_OF_RESIDENT_ID = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (@from_date IS NULL OR @from_date = '' OR info.death_time >= @from_date )
    AND (@to_date IS NULL OR @to_date = '' OR info.death_time <= @to_date )
    AND (( @yearOld = '1' AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) < 15 ) 
    OR ( @yearOld = '2' AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) >= 15  AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) <= 25 )
    OR ( @yearOld = '3' AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) >= 26  AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) <= 49 )
    OR ( @yearOld = '4' AND (YEAR(CURDATE()) - info.YEAR_OF_BIRTH) >= 50 ) OR @yearOld IS NULL OR @yearOld = '' )
    GROUP BY info.permanent_province_id
    
 UNION ALL
 
 SELECT 
    info1.permanent_province_id province,
    info1.permanent_district_id district,
    info1.permanent_ward_id ward,
    0 AS m,
    COALESCE(count(info1.id),0) AS f,
    0 AS o
 FROM pac_patient_info info1
 WHERE info1.gender_id = '2'

    AND info1.DEATH_TIME is not null AND info1.IS_REMOVE = false
    AND info1.accept_time is not null
    AND info1.review_ward_time is not null
    AND info1.review_province_time is not null
    AND (info1.permanent_province_id IN (@provinceID) OR coalesce(@provinceID, null) IS NULL)
    AND (info1.permanent_district_id IN (@districtID) OR coalesce(@districtID, null) IS NULL)
    AND (info1.permanent_ward_id IN (@wardID) OR coalesce(@wardID, null) IS NULL)
    AND (info1.gender_id = @gender_id OR @gender_id IS NULL OR @gender_id = '' )
    AND (info1.job_id = @job OR @job IS NULL OR @job = '' )
    AND (info1.OBJECT_GROUP_ID = @test_object OR @test_object IS NULL OR @test_object = '' )
    AND (info1.RISK_BEHAVIOR_ID = @risk OR @risk IS NULL OR @risk = '' )
    AND (info1.BLOOD_BASE_ID = @blood OR @blood IS NULL OR @blood = '' )
    AND (info1.STATUS_OF_TREATMENT_ID = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (info1.STATUS_OF_RESIDENT_ID = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (@from_date IS NULL OR @from_date = '' OR info1.death_time >= @from_date )
    AND (@to_date IS NULL OR @to_date = '' OR info1.death_time <= @to_date )
    AND (( @yearOld = '1' AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) < 15 ) 
    OR ( @yearOld = '2' AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) >= 15  AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) <= 25 )
    OR ( @yearOld = '3' AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) >= 26  AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) <= 49 )
    OR ( @yearOld = '4' AND (YEAR(CURDATE()) - info1.YEAR_OF_BIRTH) >= 50 ) OR @yearOld IS NULL OR @yearOld = '' )
    GROUP BY info1.permanent_province_id

UNION ALL

SELECT 
    info2.permanent_province_id province,
    info2.permanent_district_id district,
    info2.permanent_ward_id ward,
    0 AS m,
    0 AS f,
    COALESCE(count(info2.id),0) AS o
 FROM pac_patient_info info2
 WHERE info2.gender_id <> '2' AND info2.gender_id <> '1'

    AND info2.DEATH_TIME is not null 
    AND info2.IS_REMOVE = false
    AND info2.accept_time is not null
    AND info2.review_ward_time is not null
    AND info2.review_province_time is not null
    AND (info2.permanent_province_id IN (@provinceID) OR coalesce(@provinceID, null) IS NULL)
    AND (info2.permanent_district_id IN (@districtID) OR coalesce(@districtID, null) IS NULL)
    AND (info2.permanent_ward_id IN (@wardID) OR coalesce(@wardID, null) IS NULL)
    AND (info2.gender_id = @gender_id OR @gender_id IS NULL OR @gender_id = '' )
    AND (info2.job_id = @job OR @job IS NULL OR @job = '' )
    AND (info2.OBJECT_GROUP_ID = @test_object OR @test_object IS NULL OR @test_object = '' )
    AND (info2.RISK_BEHAVIOR_ID = @risk OR @risk IS NULL OR @risk = '' )
    AND (info2.BLOOD_BASE_ID = @blood OR @blood IS NULL OR @blood = '' )
    AND (info2.STATUS_OF_TREATMENT_ID = @statusTreatment OR @statusTreatment IS NULL OR @statusTreatment = '' )
    AND (info2.STATUS_OF_RESIDENT_ID = @statusResident OR @statusResident IS NULL OR @statusResident = '' )
    AND (@from_date IS NULL OR @from_date = '' OR info2.death_time >= @from_date )
    AND (@to_date IS NULL OR @to_date = '' OR info2.death_time <= @to_date )
    AND (( @yearOld = '1' AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) < 15 ) 
    OR ( @yearOld = '2' AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) >= 15  AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) <= 25 )
    OR ( @yearOld = '3' AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) >= 26  AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) <= 49 )
    OR ( @yearOld = '4' AND (YEAR(CURDATE()) - info2.YEAR_OF_BIRTH) >= 50 ) OR @yearOld IS NULL OR @yearOld = '' )

    GROUP BY info2.permanent_province_id

    ) AS m_info GROUP BY m_info.province, m_info.district, m_info.ward;