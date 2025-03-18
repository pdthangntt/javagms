select  
    main.gender_id,
    main.age,
    COUNT(main.id) as c
from (select  
	v.gender_id,
	v.id,
	CASE
            WHEN v.year_of_birth is null OR v.confirm_time is null THEN "khongro"
            WHEN v.age < 15 THEN "015"
            WHEN v.age >= 15 AND v.age <25 THEN "1525"
            WHEN v.age >= 25 AND v.age <=49 THEN "2549"
            WHEN v.age >49 THEN "49max"
    END as age
    FROM (SELECT info.id, info.gender_id, info.year_of_birth, info.confirm_time, DATE_FORMAT(info.confirm_time, '%Y') - CAST(info.year_of_birth AS UNSIGNED) as age
        FROM pac_patient_info AS info 
        WHERE info.is_remove = 0
            AND YEAR(info.confirm_time) = @toYear
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