SELECT
        @year as y,
	count(info.id) as c
 FROM pac_patient_info info
 WHERE
	info.is_remove = false
    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND info.province_id = @province
    AND (DATE_FORMAT(info.death_time , '%Y-%m-%d') > @pramtodate OR info.death_time IS NULL OR info.death_time = '') 
    AND YEAR(info.confirm_time) <= @year
    AND DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @pramtodate
    AND ( info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
    AND ( info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL )
    AND ( info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL )
    AND ( info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL  )

    
 