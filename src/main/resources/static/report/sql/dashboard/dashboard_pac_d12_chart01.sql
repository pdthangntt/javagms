SELECT
case
when (info.confirm_time IS NOT NULL and @year = -1) then DATE_FORMAT(info.confirm_time, '%Y')
when (info.confirm_time IS NOT NULL and @year > 0 ) then DATE_FORMAT(info.confirm_time, '%c/%Y')
END as d,
count(info.id) as num

FROM pac_patient_info info
WHERE
info.is_remove = false
AND info.accept_time is not null
AND info.review_ward_time is not null
AND info.review_province_time is not null
AND info.province_id = @province
AND ( @year = -1 or YEAR(info.confirm_time) = @year)
AND ( info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL )
AND ( info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL )
AND ( info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL )
AND ( info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL )
group by d;

