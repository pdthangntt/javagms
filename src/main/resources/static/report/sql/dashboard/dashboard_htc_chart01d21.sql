select 
    v.d, 
    sum(v.nhanketqua) as nhanketqua, 
    sum(v.total) as total 
FROM (
    SELECT 
        case 
            when (e.results_time IS NOT NULL ) then DATE_FORMAT(e.results_time, '%c/%Y')
            when (e.results_patien_time IS NOT NULL ) then DATE_FORMAT(e.results_patien_time, '%c/%Y')
        END as d, 
        case 
            when (e.results_time IS NOT NULL OR e.results_patien_time IS NOT NULL) then 1 else 0 
        END as nhanketqua, 
        0 as total 
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services) 
        AND (
                ((e.test_results_id = '2' OR e.test_results_id = '3') AND e.results_patien_time is not null AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') <= @to_time)
             OR (e.test_results_id = '1' AND e.results_time is not null AND DATE_FORMAT(e.results_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.results_time, '%Y-%m-%d') <= @to_time)
        )
 
    UNION ALL

    SELECT 
        case 
            when (e.test_results_id IS NOT NULL AND e.pre_test_time IS NOT NULL) then DATE_FORMAT(e.pre_test_time, '%c/%Y')
        END as d, 
        0 as nhanketqua, 
        case when (e.pre_test_time IS NOT NULL) then 1 else 0 end as total 
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services) 
        AND ( e.pre_test_time is not null AND DATE_FORMAT(e.pre_test_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.pre_test_time, '%Y-%m-%d') <= @to_time) 
) as v GROUP BY v.d