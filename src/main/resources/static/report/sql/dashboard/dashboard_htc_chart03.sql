select 
    v.d, 
    sum(v.duongtinh) as duongtinh,
    sum(v.chuyengui) as chuyengui 
FROM (
    SELECT 
        case 
            when (e.confirm_time IS NOT NULL ) then CONCAT(QUARTER(e.confirm_time), '/', YEAR(e.confirm_time))
        END as d, 
        case when (e.confirm_results_id = 2) then 1 else 0 end as duongtinh,        
        case when e.register_therapy_time is not null then 1 else 0 end as chuyengui
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services)
        AND e.confirm_time is not null AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') <= @to_time
) as v GROUP BY v.d