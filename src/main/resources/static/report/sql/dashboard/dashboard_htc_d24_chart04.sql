select 
    v.d, 
    sum(v.ageunder15) as ageunder15, 
    sum(v.age15to24) as age15to24, 
    sum(v.age25to49) as age25to49, 
    sum(v.ageon49) as ageon49 
FROM (
    SELECT 
        case 
            when e.early_hiv_date IS NOT NULL then CONCAT(QUARTER(e.early_hiv_date), '/', YEAR(e.early_hiv_date))
        END as d, 
        case 
            when e.early_hiv_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND  (YEAR(e.early_hiv_date) - e.year < 15) then 1 else 0 
        END as ageunder15,
        case 
            when e.early_hiv_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.early_hiv_date) - e.year >= 15 AND YEAR(e.early_hiv_date) - e.year <= 24) then 1 else 0 
        END as age15to24,
        case 
            when e.early_hiv_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.early_hiv_date) - e.year >= 25 AND YEAR(e.early_hiv_date) - e.year <= 49) then 1 else 0 
        END as age25to49,
        case 
            when e.early_hiv_date IS NOT NULL AND e.year != '' AND e.year IS NOT NULL AND (YEAR(e.early_hiv_date) - e.year > 49) then 1 else 0 
        END as ageon49
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_confirm) 
        AND e.source_site_id IN (@source_site_id)  
        AND e.service_id IN (@services) 
        AND e.early_hiv = 4 
        AND (e.early_hiv_date is not null AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') <= @to_time)
   ) as v 
GROUP BY v.d