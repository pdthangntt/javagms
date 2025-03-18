select  
    main.gender_id,
    main.age,
    COUNT(main.id) as c
from (select  
	v.gender_id,
	v.id,
	CASE
            WHEN v.age < 15 THEN "015"
            WHEN v.age >= 15 AND v.age <25 THEN "1525"
            WHEN v.age >= 25 AND v.age <=49 THEN "2549"
            WHEN v.age >49 THEN "49max"
       ELSE "015"
    END as age
    FROM (SELECT info.id, info.gender_id, DATE_FORMAT(info.death_time, '%Y') - CAST(info.year_of_birth AS UNSIGNED) as age
        FROM `pac_patient_info` as info WHERE info.is_remove = 0
        AND DATE_FORMAT(info.death_time , '%Y-%m-%d') <= @toTime
        AND info.death_time is not null
        AND info.accept_time is not null
        AND info.review_ward_time is not null
        AND info.review_province_time is not null
        AND info.province_id = @province
        AND (info.permanent_district_id IN (@district) OR coalesce(@district, null) IS NULL)
        AND (info.gender_id IN (@gender) OR coalesce(@gender, null) IS NULL)
        AND (info.mode_of_transmission_id IN (@modesOfTransmision) OR coalesce(@modesOfTransmision, null) IS NULL)
        AND (info.object_group_id IN (@testObjectGroup) OR coalesce(@testObjectGroup, null) IS NULL)
    ) as v) as main
GROUP BY main.gender_id, main.age