select 
    v.d, 
    sum(v.total) as total 
FROM (
    SELECT 
        case 
             when (e.service_id IS NOT NULL ) then e.service_id
        END as d, 
        case when  e.results_patien_time IS NOT NULL then 1 else 0 end as total 
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.confirm_results_id = '2'
        AND e.service_id IN (@services) 
        AND e.results_patien_time is not null AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') <= @to_time
) as v GROUP BY v.d