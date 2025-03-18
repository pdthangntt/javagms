select 
    v.d, 
    sum(v.ageunder15) as ageunder15, 
    sum(v.age15to24) as age15to24, 
    sum(v.age25to49) as age25to49, 
    sum(v.ageon49) as ageon49 
FROM (
    SELECT 
        case 
            when e.register_therapy_time IS NOT NULL then CONCAT(QUARTER(e.register_therapy_time), '/', YEAR(e.register_therapy_time))
        END as d, 
        case 
            when (e.register_therapy_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND  YEAR(e.register_therapy_time) - e.year_of_birth < 15) then 1 else 0 
        END as ageunder15,
        case 
            when (e.register_therapy_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND  (YEAR(e.register_therapy_time) - e.year_of_birth >= 15 AND YEAR(e.register_therapy_time) - e.year_of_birth <= 24)) then 1 else 0 
        END as age15to24,
        case 
            when (e.register_therapy_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND  (YEAR(e.register_therapy_time) - e.year_of_birth >= 25 AND YEAR(e.register_therapy_time) - e.year_of_birth <= 49)) then 1 else 0 
        END as age25to49,
        case 
            when (e.register_therapy_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND  YEAR(e.register_therapy_time) - e.year_of_birth > 49) then 1 else 0 
        END as ageon49
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services) 
        AND e.confirm_results_id = 2 
        AND (e.register_therapy_time is not null AND DATE_FORMAT(e.register_therapy_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.register_therapy_time, '%Y-%m-%d') <= @to_time)
   ) as v 
GROUP BY v.d