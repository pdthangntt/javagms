SELECT 
    d.name as name, 
    SUM(tbl.total) as total 
FROM(
    SELECT 
        s.district_id , 
        s.short_name, 
        e.site_id, 
        case 
            when (e.register_therapy_time is not null ) then COUNT(e.id) else 0 
        END as total
    FROM `htc_visit` as e INNER JOIN `site` as s ON e.site_id = s.id 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services) 
        AND e.confirm_results_id = 2 
        AND (e.register_therapy_time is not null AND DATE_FORMAT(e.register_therapy_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.register_therapy_time, '%Y-%m-%d') <= @to_time)
    GROUP BY e.site_id
) as tbl INNER JOIN district as d ON tbl.district_id = d.id
GROUP BY tbl.district_id