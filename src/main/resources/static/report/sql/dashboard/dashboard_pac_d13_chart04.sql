    SELECT 
        e.object_group_id as d, 
    COUNT(e.id) as soluong
    FROM pac_patient_info as e 
    WHERE e.is_remove = false
    AND e.accept_time is not null
    AND e.review_ward_time is not null
    AND e.review_province_time is not null

    AND ( DATE_FORMAT(e.death_time , '%Y-%m-%d') > @year OR e.death_time IS NULL OR e.death_time = '') 
    AND e.province_id = @province
    AND DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @year   
    AND ( e.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL    )
    AND ( e.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL )
    AND ( e.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL )
    AND ( e.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL  )
GROUP BY d