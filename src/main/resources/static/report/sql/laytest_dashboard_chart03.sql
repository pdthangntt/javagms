select 
    v.d as rule, 
    sum(v.duongtinh) as so_duongtinh, 
    sum(v.total) as so_xn 
FROM (
    SELECT 
        case 
             when (e.confirm_results_id = '2' and e.confirm_time IS NOT NULL) then DATE_FORMAT(e.confirm_time, '%c')
        END as d, 
        case when (e.confirm_results_id = '2' AND e.confirm_time IS NOT NULL) then 1 else 0 end as duongtinh, 
        0 as total 
    FROM htc_laytest as e 
    WHERE e.is_remove = 0 AND e.site_id = @site_id AND e.created_by = @staff_id 
        AND (e.confirm_results_id = '2' AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') <= @to_time)
    UNION ALL

    SELECT 
        case 
             when (e.test_results_id IS NOT NULL AND e.advisory_time IS NOT NULL) then DATE_FORMAT(e.advisory_time, '%c')
        END as d, 
        0 as duongtinh, 
        case when (e.advisory_time IS NOT NULL) then 1 else 0 end as total 
    FROM htc_laytest as e 
    WHERE e.is_remove = 0 AND e.site_id = @site_id AND e.created_by = @staff_id 
        AND (e.test_results_id is not null AND e.advisory_time is not null AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time) 
) as v WHERE v.d is not null GROUP BY v.d