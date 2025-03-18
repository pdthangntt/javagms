SELECT 
    info.mode_of_transmission_id AS duonglay,
    COUNT(info.id) as soluong
FROM pac_patient_info as info
WHERE info.is_remove = 0
    AND (DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @toTime)
    AND info.province_id = @province
    AND info.accept_time IS NOT NULL
    AND info.review_ward_time IS NOT NULL
    AND info.review_province_time IS NOT NULL
    AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
    AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
    AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
    AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
 GROUP BY info.mode_of_transmission_id

UNION ALL 

SELECT 
    0 AS duonglay,
    COUNT(info.id) as soluong
FROM pac_patient_info as info
WHERE info.is_remove = 0
    AND (info.mode_of_transmission_id IS NULL OR info.mode_of_transmission_id = '')
    AND (DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @toTime)
    AND info.province_id = @province
    AND info.accept_time IS NOT NULL
    AND info.review_ward_time IS NOT NULL
    AND info.review_province_time IS NOT NULL
    AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
    AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
    AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
    AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)