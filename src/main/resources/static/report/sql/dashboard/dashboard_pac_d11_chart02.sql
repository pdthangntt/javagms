SELECT 
    tbl.mahuyen as mahuyen,
    tbl.tenhuyen as tenhuyen,
    SUM(tbl.sotichluy) as sotichluy
FROM
(
    SELECT 
        info.permanent_district_id as mahuyen,
        d.name AS tenhuyen,
        CASE WHEN (info.confirm_time IS NOT NULL) THEN 1 ELSE 0 END as sotichluy
    FROM pac_patient_info as info LEFT JOIN district as d ON info.permanent_district_id = d.id 
    WHERE info.is_remove = 0
        AND (DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @toTime)
        AND info.province_id = @province
        AND info.accept_time IS NOT NULL
        AND info.review_ward_time IS NOT NULL
        AND info.review_province_time IS NOT NULL
        AND info.permanent_district_id IN (@district) 
        AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
        AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
        AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
) as tbl
 GROUP BY tbl.mahuyen 
