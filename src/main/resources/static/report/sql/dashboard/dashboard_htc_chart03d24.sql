SELECT 
    v.d, 
    sum(v.nam) as nam, 
    sum(v.nu) as nu
FROM (
    SELECT 
        case 
            when e.early_hiv_date IS NOT NULL then CONCAT(QUARTER(e.early_hiv_date), '/', YEAR(e.early_hiv_date))
        END as d, 
        case 
            when e.early_hiv_date IS NOT NULL AND (e.gender_id = '1') then 1 else 0 
        END as nam,
        case 
            when e.early_hiv_date IS NOT NULL AND (e.gender_id = '2')  then 1 else 0 
        END as nu
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.source_site_id IN (@source_site_id)
        AND e.early_hiv = '4'
        AND service_id IN (@services)
        AND DATE_FORMAT(early_hiv_date, '%Y-%m-%d') BETWEEN @from_time AND @to_time
   ) as v 
GROUP BY v.d