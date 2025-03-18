SELECT 
    tbl.nam as nam,
    SUM(tbl.male) as male,
    SUM(tbl.female) as female,
    SUM(tbl.unclear) as unclear
FROM
(
    SELECT 
        @year as nam,
        CASE WHEN info.gender_id = '1' THEN COUNT(info.id) ELSE 0 END as male,
        CASE WHEN info.gender_id = '2' THEN COUNT(info.id) ELSE 0 END as female,
        CASE WHEN info.gender_id = '3' THEN COUNT(info.id) ELSE 0 END as unclear
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
    GROUP BY info.gender_id     
) as tbl
 GROUP BY tbl.nam 
