SELECT 
    tbl.nam as nam,
    SUM(tbl.tichluy) as tichluy,
    SUM(tbl.consong) as consong,
    SUM(tbl.phathien) as phathien,
    SUM(tbl.tuvong) as tuvong
FROM
(
    SELECT 
        @year AS nam,
        COUNT(info.id) as tichluy,
        0 as consong,
        0 as phathien,
        0 as tuvong
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND YEAR(info.confirm_time) <= @year
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
        0 as tichluy,
        0 as consong,
        COUNT(info.id) as phathien,
        0 as tuvong
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND YEAR(info.confirm_time) = @year
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
        0 as tichluy,
        COUNT(info.id) as consong,
        0 as phathien,
        0 as tuvong
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND (YEAR(info.death_time) > @year OR info.death_time IS NULL OR info.death_time = '') 
        AND YEAR(info.confirm_time) <= @year
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
        0 as tichluy,
        0 as consong,
        0 as phathien,
        COUNT(info.id) as tuvong
    FROM pac_patient_info as info
    WHERE info.is_remove = 0
        AND (info.death_time IS NOT NULL AND info.death_time <> '')
        AND YEAR(info.death_time) = @year
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
