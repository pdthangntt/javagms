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
    from (
SELECT d_table.* FROM(
SELECT t.id, 
            t.site_id, 
            t.gender_id, 
            t.object_group_id, 
            t.year_of_birth,
            visit.st_time as st_time,
            DATE_FORMAT(@to_date, '%Y') - CAST(t.year_of_birth AS UNSIGNED) as age
        FROM `pqm_prep` as t
        INNER JOIN (SELECT v.prep_id, v.examination_time , stage.start_time as st_time
                                            FROM pqm_prep_visit v
                                            INNER JOIN 
                                            (SELECT tage . *
                                                FROM (
                                                SELECT s.id, s.prep_id, s.start_time
                                                FROM pqm_prep_stage s
                                                WHERE DATE_FORMAT(s.start_time, '%Y-%m-%d') <= @to_date
                                                ORDER BY s.start_time DESC
                                                ) AS tage
                                                ORDER BY tage.prep_id) AS stage
                                            ON v.stage_id = stage.id
                                            WHERE v.examination_time is not null 
                                            
--          AND DATE_FORMAT(v.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND DATE_FORMAT(v.examination_time, '%Y-%m-%d') <= @to_date  
            AND DATE_FORMAT(v.appoinment_time, '%Y-%m-%d') >= @from_date  
        ) as visit ON t.id = visit.prep_id
        WHERE 1 = 1
             AND t.site_id IN (@site_id) 
             AND ((( t.end_time IS NULL OR ( t.end_time IS NOT NULL AND DATE_FORMAT(t.end_time, '%Y-%m-%d')  > @to_date )) AND DATEDIFF(@to_date, DATE_FORMAT(visit.st_time, '%Y-%m-%d')) >= 83 )
   OR ((t.end_time IS NOT NULL AND DATE_FORMAT(t.end_time, '%Y-%m-%d')  < @to_date ) AND DATEDIFF(DATE_FORMAT(t.end_time, '%Y-%m-%d'), DATE_FORMAT(visit.st_time, '%Y-%m-%d')) >= 83) )
) as d_table
GROUP BY d_table.id   
) as v_main) as v_group
    GROUP BY v_group.gender_id, v_group.object_group_id, v_group.age_group