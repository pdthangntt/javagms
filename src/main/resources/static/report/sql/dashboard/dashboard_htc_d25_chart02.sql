SELECT 
    d.name as name, 
    SUM(tbl.total) as total 
FROM(
    SELECT 
        s.district_id , 
        s.short_name, 
        e.site_id, 
        case 
            when (e.virus_load != 4 AND e.virus_load_date IS NOT NULL) then COUNT(e.id) else 0 
        END as total
    FROM `htc_confirm` as e INNER JOIN `site` as s ON e.source_site_id = s.id 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_confirm) 
        AND e.source_site_id IN (@source_site_id) 
        AND e.service_id IN (@services) 
        AND e.virus_load != 4 
        AND (e.virus_load_date is not null AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.virus_load_date, '%Y-%m-%d') <= @to_time)
    GROUP BY e.source_site_id
) as tbl INNER JOIN district as d ON tbl.district_id = d.id
GROUP BY tbl.district_id