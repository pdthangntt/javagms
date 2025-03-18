SELECT 
    COALESCE(count(info.id),0) AS total,
    0 AS numAlive,
    0 AS numDead,
    0 AS numTreatment
 FROM pac_patient_info info
 WHERE
	info.is_remove = false
    AND info.province_id = @province
    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND (info.permanent_district_id = @district OR @district IS NULL)
    AND (info.permanent_ward_id = @ward OR @ward IS NULL)
    
 UNION ALL
 
 SELECT 
    0 AS total,
    COALESCE(count(info1.id),0) AS numAlive,
    0 AS numDead,
    0 AS numTreatment
 FROM pac_patient_info info1
 WHERE
	info1.is_remove = false
    AND DATE_FORMAT(info1.review_province_time, '%Y-%m-%d') >= @fromDate AND DATE_FORMAT(info1.review_province_time, '%Y-%m-%d') <=  @toDate
    AND info1.province_id = @province
    AND info1.accept_time is not null
    AND info1.review_ward_time is not null
    AND info1.review_province_time is not null
    AND (info1.permanent_district_id = @district OR @district IS NULL)
    AND (info1.permanent_ward_id = @ward OR @ward IS NULL)
    
 UNION ALL
 
 SELECT 
	0 AS total,
    0 AS numAlive,
    COALESCE(count(info2.id),0) AS numDead,
    0 AS numTreatment
 FROM pac_patient_info info2
 WHERE
	info2.is_remove = false
    AND info2.death_time is null
    AND info2.province_id = @province
    AND info2.accept_time is not null
    AND info2.review_ward_time is not null
    AND info2.review_province_time is not null
    AND (info2.permanent_district_id = @district OR @district IS NULL)
    AND (info2.permanent_ward_id = @ward OR @ward IS NULL)
    
 UNION ALL
 
 SELECT 
    0 AS total,
    0 AS numAlive,
    0 AS numDead,
    COALESCE(count(info3.id),0) AS numTreatment
 FROM pac_patient_info info3
 WHERE
	info3.is_remove = false
    AND info3.status_of_treatment_id IN ('3', '8', '9', '10')
    AND info3.province_id = @province
    AND info3.accept_time is not null
    AND info3.review_ward_time is not null
    AND info3.review_province_time is not null
    AND (info3.permanent_district_id = @district OR @district IS NULL)
    AND (info3.permanent_ward_id = @ward OR @ward IS NULL)