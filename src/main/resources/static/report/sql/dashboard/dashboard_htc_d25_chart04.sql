select 
    v.d, 
    sum(v.ageunder15) as ageunder15, 
    sum(v.age15to24) as age15to24, 
    sum(v.age25to49) as age25to49, 
    sum(v.ageon49) as ageon49 
FROM (
    SELECT 
        case 
            when e.virus_load_date IS NOT NULL then CONCAT(QUARTER(e.virus_load_date), '/', YEAR(e.virus_load_date))
        END as d, 
        case 
            when e.virus_load_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.virus_load_date) - e.year < 15) then 1 else 0 
        END as ageunder15,
        case 
            when e.virus_load_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.virus_load_date) - e.year >= 15 AND YEAR(e.virus_load_date) - e.year <= 24) then 1 else 0 
        END as age15to24,
        case 
            when e.virus_load_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.virus_load_date) - e.year >= 25 AND YEAR(e.virus_load_date) - e.year <= 49) then 1 else 0 
        END as age25to49,
        case 
            when e.virus_load_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.virus_load_date) - e.year > 49) then 1 else 0 
        END as ageon49
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_confirm) 
        AND e.source_site_id IN (@source_site_id) 
        AND e.service_id IN (@services) 
        AND e.virus_load IN ('1', '2', '3') 
        AND (e.virus_load_date is not null AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') <= @to_time)
   ) as v 
GROUP BY v.d