select 
    v.d, 
    sum(v.total) as total 
FROM (
    SELECT 
        case 
             when (e.service_id IS NOT NULL ) then e.service_id
        END as d, 
        case when  e.virus_load_date IS NOT NULL then 1 else 0 end as total 
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.source_site_id IN (@source_site_id)
        AND e.virus_load IN ('1','2','3')
        AND e.service_id IN (@services) 
        AND e.virus_load_date is not null AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') <= @to_time
) as v GROUP BY v.d