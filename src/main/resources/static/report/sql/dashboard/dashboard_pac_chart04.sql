SELECT
    tbl.nam as nam,
    SUM(tbl.dangdieutri) as dangdieutri,
    SUM(tbl.moidieutri) as moidieutri
FROM 
(
    SELECT 
        @year AS nam,
        COUNT(info.id) as dangdieutri,
        0 as moidieutri 
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND (YEAR(info.start_treatment_time) <= @year) 
        AND (info.death_time IS NULL OR 
        (YEAR(info.death_time) >= @year AND info.death_time IS NOT NULL AND info.start_treatment_time IS NOT NULL AND DATE_FORMAT(info.start_treatment_time , '%Y-%m-%d') < DATE_FORMAT(info.death_time , '%Y-%m-%d')))
        AND info.status_of_treatment_id = '3'
        AND info.province_id = @province
        AND info.accept_time IS NOT NULL
        AND info.review_ward_time IS NOT NULL
        AND info.review_province_time IS NOT NULL
        AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
        AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
        AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
        AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)

    UNION ALL
    
    SELECT 
        @year AS nam,
        0 as dangdieutri,
        COUNT(info.id) as moidieutri 
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND (YEAR(info.start_treatment_time) = @year)
        AND (info.death_time IS NULL)
        AND info.status_of_treatment_id = '3'
        AND info.province_id = @province
        AND info.accept_time IS NOT NULL
        AND info.review_ward_time IS NOT NULL
        AND info.review_province_time IS NOT NULL
        AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
        AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
        AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
        AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
) as tbl
GROUP BY tbl.nam


