SELECT 
    info.permanent_district_id,
    d.name,
    COUNT(info.id) as quantity
FROM pac_patient_info as info
INNER JOIN district as d on d.id = info.permanent_district_id
WHERE info.is_remove = 0
    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND info.death_time is null
    AND YEAR(info.confirm_time) <= @year
    AND info.province_id = @province
    AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
    AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
    AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
    AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
GROUP BY info.permanent_district_id