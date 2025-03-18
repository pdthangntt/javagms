SELECT 
    v.d, 
    sum(v.nam) as nam, 
    sum(v.nu) as nu
FROM (
    SELECT 
        case 
            when e.virus_load_date IS NOT NULL then CONCAT(QUARTER(e.virus_load_date), '/', YEAR(e.virus_load_date))
        END as d, 
        case 
            when e.virus_load_date IS NOT NULL AND (e.gender_id = '1') then 1 else 0 
        END as nam,
        case 
            when e.virus_load_date IS NOT NULL AND (e.gender_id = '2')  then 1 else 0 
        END as nu
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.source_site_id IN (@source_site_id)
        AND e.virus_load IN ('1','2','3')
        AND service_id IN (@services)
        AND DATE_FORMAT(virus_load_date, '%Y-%m-%d') BETWEEN @from_time AND @to_time
   ) as v 
GROUP BY v.d