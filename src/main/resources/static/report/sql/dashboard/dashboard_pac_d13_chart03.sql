select  
    main.gender_id,
    main.age,
    COUNT(main.id) as c
from (select  
	v.gender_id,
	v.id,
	CASE
            WHEN v.year_of_birth is null THEN "khongro"
            WHEN v.age < 15 THEN "015"
            WHEN v.age >= 15 AND v.age <25 THEN "1525"
            WHEN v.age >= 25 AND v.age <=49 THEN "2549"
            WHEN v.age >49 THEN "49max"
    END as age
    FROM (SELECT info.id, info.gender_id, info.year_of_birth, DATE_FORMAT(info.confirm_time, '%Y') - CAST(info.year_of_birth AS UNSIGNED) as age
        FROM pac_patient_info as info WHERE info.is_remove = 0
        AND (info.death_time is null  OR ( info.death_time is not null AND DATE_FORMAT(info.death_time , '%Y-%m-%d') > @toTime))
        AND ( info.confirm_time is not null AND DATE_FORMAT(info.confirm_time , '%Y-%m-%d') <= @toTime)
        AND info.accept_time is not null
        AND info.review_ward_time is not null
        AND info.review_province_time is not null
        AND info.province_id = @province
        AND (coalesce(@district, null) IS NULL OR info.permanent_district_id IN (@district))
        AND (coalesce(@gender, null) IS NULL OR info.gender_id IN (@gender))
        AND (coalesce(@modesOfTransmision, null) IS NULL OR info.mode_of_transmission_id IN (@modesOfTransmision))
        AND (coalesce(@testObjectGroup, null) IS NULL OR info.object_group_id IN (@testObjectGroup))
    ) as v) as main
GROUP BY main.gender_id, main.age