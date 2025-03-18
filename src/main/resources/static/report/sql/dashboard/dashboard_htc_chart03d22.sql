SELECT 
    v.d, 
    sum(v.nam) as nam, 
    sum(v.nu) as nu
FROM (
    SELECT 
        case 
            when e.results_patien_time IS NOT NULL then CONCAT(QUARTER(e.results_patien_time), '/', YEAR(e.results_patien_time))
        END as d, 
        case 
            when e.results_patien_time IS NOT NULL AND (e.gender_id = '1') then 1 else 0 
        END as nam,
        case 
            when e.results_patien_time IS NOT NULL AND (e.gender_id = '2')  then 1 else 0 
        END as nu
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.confirm_results_id = '2'
        AND service_id IN (@services)
        AND DATE_FORMAT(results_patien_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time
   ) as v 
GROUP BY v.d