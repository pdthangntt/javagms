SELECT 
    tbl.nam as nam,
    SUM(tbl.mau) as mau,
    SUM(tbl.tinhduc) as tinhduc,
    SUM(tbl.mesangcon) as mesangcon,
    SUM(tbl.khongro) as khongro,
    SUM(tbl.truyenmau) as truyenmau,
    SUM(tbl.tainannghenghiep) as tainannghenghiep,
    SUM(tbl.tiemchich) as tiemchich,
    SUM(tbl.tdkhacgioi) as tdkhacgioi,
    SUM(tbl.tddonggioi) as tddonggioi,
    SUM(tbl.khongcothongtin) as khongcothongtin
FROM
(
    SELECT 
        @year as nam,
        CASE WHEN info.mode_of_transmission_id = '1' THEN COUNT(info.id) ELSE 0 END as mau,
        CASE WHEN info.mode_of_transmission_id = '2' THEN COUNT(info.id) ELSE 0 END as tinhduc,
        CASE WHEN info.mode_of_transmission_id = '3' THEN COUNT(info.id) ELSE 0 END as mesangcon,
        CASE WHEN info.mode_of_transmission_id = '4' THEN COUNT(info.id) ELSE 0 END as khongro,
        CASE WHEN info.mode_of_transmission_id = '5' THEN COUNT(info.id) ELSE 0 END as truyenmau,
        CASE WHEN info.mode_of_transmission_id = '6' THEN COUNT(info.id) ELSE 0 END as tainannghenghiep,
        CASE WHEN info.mode_of_transmission_id = '7' THEN COUNT(info.id) ELSE 0 END as tiemchich,
        CASE WHEN info.mode_of_transmission_id = '8' THEN COUNT(info.id) ELSE 0 END as tdkhacgioi,
        CASE WHEN info.mode_of_transmission_id = '9' THEN COUNT(info.id) ELSE 0 END as tddonggioi,
        CASE WHEN (info.mode_of_transmission_id IS NULL OR info.mode_of_transmission_id = '') THEN COUNT(info.id) ELSE 0 END as khongcothongtin
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
    GROUP BY info.mode_of_transmission_id
) as tbl
GROUP BY tbl.nam
