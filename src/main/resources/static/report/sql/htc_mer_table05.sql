SELECT
    e.service_id AS serviceID,
    e.object_group_id AS objectGroupID,
    SUM(IF(e.confirm_results_id = '1' OR e.test_results_id = '1',1,0)) AS amtinh,
    SUM(IF(e.confirm_results_id = '2',1,0)) AS duongtinh,
    COALESCE(e.confirm_results_id, '1') AS confirmResultsID
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
    AND ((e.results_patien_time IS NOT NULL AND e.confirm_results_id IN ('1' , '2')) OR ( e.results_time IS NOT NULL AND e.test_results_id = '1'))
    AND (( DATE_FORMAT(e.results_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time) OR
           (DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time))
    AND e.service_id IN (@services)
    AND e.site_id = @siteID 
    AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
GROUP BY e.object_group_id, confirmResultsID, e.service_id;