select 
    "1" as indicator,
    
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "<15"
            WHEN v_main.age >= 15 AND v_main.age <25 THEN "15-25"
            WHEN v_main.age >= 25 AND v_main.age <=49 THEN "25-49"
            WHEN v_main.age >49 THEN ">49"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
             
            t.gender_id, t.object_group_id, t.year, 
            DATE_FORMAT(t.confirm_time, '%Y') - CAST(t.year AS UNSIGNED) as age
        FROM `htc_confirm` as t
        WHERE t.is_remove = 0
            -- dieu kien
            AND t.site_id = @site
            AND (t.source_site_id IN (@sourceSites) OR coalesce(@sourceSites, null) IS NULL)
            AND DATE_FORMAT(t.confirm_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.results_id = '2'
    ) as v_main) as v_group
    GROUP BY  v_group.gender_id, v_group.object_group_id, v_group.age_group

UNION ALL

select 
    "2" as indicator,
    
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "<15"
            WHEN v_main.age >= 15 AND v_main.age <25 THEN "15-25"
            WHEN v_main.age >= 25 AND v_main.age <=49 THEN "25-49"
            WHEN v_main.age >49 THEN ">49"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
             
            t.gender_id, t.object_group_id, t.year, 
            DATE_FORMAT(t.confirm_time, '%Y') - CAST(t.year AS UNSIGNED) as age
        FROM `htc_confirm` as t
        WHERE t.is_remove = 0
            -- dieu kien
            AND t.site_id = @site
            AND (t.source_site_id IN (@sourceSites) OR coalesce(@sourceSites, null) IS NULL)
            AND DATE_FORMAT(t.early_hiv_date, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.results_id = '2'
            AND t.early_hiv = '8'
    ) as v_main) as v_group
    GROUP BY  v_group.gender_id, v_group.object_group_id, v_group.age_group

UNION ALL

select 
    "3" as indicator,
    
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "<15"
            WHEN v_main.age >= 15 AND v_main.age <25 THEN "15-25"
            WHEN v_main.age >= 25 AND v_main.age <=49 THEN "25-49"
            WHEN v_main.age >49 THEN ">49"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
             
            t.gender_id, t.object_group_id, t.year, 
            DATE_FORMAT(t.confirm_time, '%Y') - CAST(t.year AS UNSIGNED) as age
        FROM `htc_confirm` as t
        WHERE t.is_remove = 0
            -- dieu kien
            AND t.site_id = @site
            AND (t.source_site_id IN (@sourceSites) OR coalesce(@sourceSites, null) IS NULL)
            AND DATE_FORMAT(t.virus_load_date, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.results_id = '2'
            AND t.virus_load = '4'
    ) as v_main) as v_group
    GROUP BY  v_group.gender_id, v_group.object_group_id, v_group.age_group
UNION ALL

select 
    "4" as indicator,
    
    v_group.gender_id, 
    v_group.object_group_id, 
    v_group.age_group,
    count(v_group.id) as quantity
from (select 
	v_main.*,
	CASE WHEN (v_main.year REGEXP '^[0-9]{4}$') THEN
        CASE
            WHEN v_main.age < 15 THEN "<15"
            WHEN v_main.age >= 15 AND v_main.age <25 THEN "15-25"
            WHEN v_main.age >= 25 AND v_main.age <=49 THEN "25-49"
            WHEN v_main.age >49 THEN ">49"
            ELSE "none"
        END
        ELSE "none" END as age_group
    from (SELECT t.id, 
             
            t.gender_id, t.object_group_id, t.year, 
            DATE_FORMAT(t.confirm_time, '%Y') - CAST(t.year AS UNSIGNED) as age
        FROM `htc_confirm` as t
        WHERE t.is_remove = 0
            -- dieu kien
            AND t.site_id = @site
            AND (t.source_site_id IN (@sourceSites) OR coalesce(@sourceSites, null) IS NULL)
            AND DATE_FORMAT(t.early_hiv_date, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.results_id = '2'
            AND t.early_diagnose = '1'
    ) as v_main) as v_group
    GROUP BY  v_group.gender_id, v_group.object_group_id, v_group.age_group