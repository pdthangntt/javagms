select 
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year_of_birth REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "10-14"
            WHEN v_main.age >= 15 AND v_main.age <=19 THEN "15-19"
            WHEN v_main.age >= 20 AND v_main.age <=24 THEN "20-24"
            WHEN v_main.age >= 25 AND v_main.age <=29 THEN "25-29"
            WHEN v_main.age >= 30 AND v_main.age <=34 THEN "30-34"
            WHEN v_main.age >= 35 AND v_main.age <=39 THEN "35-39"
            WHEN v_main.age >= 40 AND v_main.age <=44 THEN "40-44"
            WHEN v_main.age >= 45 AND v_main.age <=49 THEN "45-49"
            WHEN v_main.age >= 50 THEN "50+"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
            t.site_id, 
            t.gender_id, 
            t.object_group_id, 
            t.year_of_birth, 
            DATE_FORMAT(t.REGISTER_THERAPY_TIME, '%Y') - CAST(t.year_of_birth AS UNSIGNED) as age
        FROM `pqm_vct` as t
        WHERE 1 = 1
            AND t.site_id IN (@site_id) 
            AND t.REGISTER_THERAPY_TIME is not null AND t.confirm_results_id = '2'
            AND t.REGISTER_THERAPY_TIME BETWEEN @from_date AND @to_date
            AND t.REGISTER_THERAPY_SITE is not null
            AND t.THERAPY_NO is not null
            AND t.THERAPY_NO <> ''
            AND t.REGISTER_THERAPY_SITE is not null
            AND t.REGISTER_THERAPY_SITE <> ''
            AND t.EXCHANGE_TIME is not null
    ) as v_main) as v_group
    GROUP BY v_group.gender_id, v_group.object_group_id, v_group.age_group