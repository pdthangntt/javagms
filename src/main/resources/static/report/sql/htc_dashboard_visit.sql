select 
    v.d, 
    sum(v.duongtinh) as duongtinh, 
    sum(v.total) as total 
FROM (
    SELECT 
        case 
             when (e.confirm_results_id = '2' and e.confirm_time IS NOT NULL) then DATE_FORMAT(e.confirm_time, '%c')
        END as d, 
        case when (e.confirm_results_id = '2' AND e.confirm_time IS NOT NULL) then 1 else 0 end as duongtinh, 
        0 as total 
    FROM htc_visit as e 
    WHERE e.is_remove = 0 AND e.site_id = @site_id 
        AND (e.confirm_results_id = '2' AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') <= @to_time) 
        AND e.service_id IN (@services) 
 
    UNION ALL

    SELECT 
        case 
             when (e.test_results_id IS NOT NULL AND e.pre_test_time IS NOT NULL) then DATE_FORMAT(e.pre_test_time, '%c')
        END as d, 
        0 as duongtinh, 
        case when (e.is_agree_pretest=1 AND e.pre_test_time IS NOT NULL) then 1 else 0 end as total 
    FROM htc_visit as e 
    WHERE e.is_remove = 0 AND e.site_id = @site_id 
        AND (e.test_results_id is not null AND e.pre_test_time is not null AND DATE_FORMAT(e.pre_test_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.pre_test_time, '%Y-%m-%d') <= @to_time) 
        AND e.service_id IN (@services) 
) as v GROUP BY v.d