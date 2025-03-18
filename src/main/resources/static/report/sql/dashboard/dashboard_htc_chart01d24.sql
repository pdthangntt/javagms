SELECT 
    v.d, 
    sum(v.nhohon6) as nhohon6, 
    sum(v.lonhon12) as lonhon12, 
    sum(v.total) as total 
FROM (
    SELECT 
        case 
            when (e.early_hiv_date IS NOT NULL ) then DATE_FORMAT(e.early_hiv_date, '%c/%Y')
        END as d, 
        case 
            when (e.early_hiv IS NOT NULL AND e.early_hiv = '4') then 1 else 0 
        END as nhohon6, 
        0 as lonhon12,
        0 as total 
    FROM htc_confirm as e 
    WHERE e.is_remove = 0
            AND  e.site_id IN (@site_id) 
            AND  e.SOURCE_SITE_ID IN (@source_site_id)
            AND ( e.service_id IN (@services) OR coalesce(@services, null) IS NULL) 
            AND ( e.early_hiv_date is not null AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') <= @to_time) 
    UNION ALL
    SELECT 
        case 
            when (e.early_hiv_date IS NOT NULL ) then DATE_FORMAT(e.early_hiv_date, '%c/%Y')
        END as d, 
        0 as nhohon6,
        case 
            when (e.early_hiv IS NOT NULL AND e.early_hiv = '2') then 1 else 0 
        END as lonhon12,
        0 as total 
    FROM htc_confirm as e 
    WHERE e.is_remove = 0
            AND ( e.site_id IN (@site_id) AND  e.SOURCE_SITE_ID IN (@source_site_id))
            AND ( e.service_id IN (@services) OR coalesce(@services, null) IS NULL) 
            AND ( e.early_hiv_date is not null AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') <= @to_time)  
UNION ALL
    SELECT 
        case 
            when (e.early_hiv_date IS NOT NULL) then DATE_FORMAT(e.early_hiv_date, '%c/%Y')
        END as d, 
        0 as nhohon6, 
        0 as lonhon12,
        case when (e.early_hiv_date IS NOT NULL ) then 1 else 0 end as total 
    FROM htc_confirm as e 
    WHERE e.is_remove = 0 
            AND ( e.site_id IN (@site_id) AND  e.SOURCE_SITE_ID IN (@source_site_id))
            AND ( e.service_id IN (@services) OR coalesce(@services, null) IS NULL) 
            AND ( e.early_hiv_date is not null AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') <= @to_time)     
) as v where v.d is not null GROUP BY v.d
