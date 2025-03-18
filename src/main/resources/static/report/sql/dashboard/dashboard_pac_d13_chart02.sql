SELECT 
    info.permanent_district_id as mahuyen,
    d.name AS tenhuyen,
    COUNT(info.id) as sotichluy
FROM pac_patient_info as info LEFT JOIN district as d ON info.permanent_district_id = d.id 
WHERE info.is_remove = 0
    AND (info.death_time is null  OR ( info.death_time is not null AND DATE_FORMAT(info.death_time , '%Y-%m-%d') > @toTime))
    AND (info.confirm_time is not null AND DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @toTime)
    AND info.province_id = @province
    AND info.accept_time IS NOT NULL
    AND info.review_ward_time IS NOT NULL
    AND info.review_province_time IS NOT NULL
    AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
    AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
    AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
    AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
 GROUP BY info.permanent_district_id


