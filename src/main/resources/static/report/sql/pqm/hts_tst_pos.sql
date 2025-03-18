select 
    v_group.site_id,
    v_group.province_id, 
    v_group.district_id, 
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year_of_birth REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "<15"
            WHEN v_main.age >= 15 AND v_main.age <25 THEN "15-25"
            WHEN v_main.age >= 25 AND v_main.age <=49 THEN "25-49"
            WHEN v_main.age >49 THEN ">49"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
            t.site_id, 
            site.province_id, 
            site.district_id, 
            t.gender_id, t.object_group_id, t.year_of_birth, 
            DATE_FORMAT(t.confirm_time, '%Y') - CAST(t.year_of_birth AS UNSIGNED) as age
        FROM `htc_visit` as t
        INNER JOIN site on site.id = t.site_id
        WHERE t.is_remove = 0
            AND t.site_id IN (@site_id) 
            AND t.confirm_time is not null AND t.confirm_results_id = '2'
            AND t.confirm_time BETWEEN @from_date AND @to_date
    ) as v_main) as v_group
    GROUP BY v_group.province_id, v_group.district_id, v_group.gender_id, v_group.object_group_id, v_group.age_group