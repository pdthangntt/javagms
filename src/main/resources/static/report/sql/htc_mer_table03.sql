SELECT
    e.service_id AS serviceID,
    e.object_group_id AS objectGroupID,
    SUM(IF(e.early_hiv = '4',1,0)) AS nho6,
    SUM(IF(e.early_hiv = '2',1,0)) AS lon12,
    e.early_hiv AS early_hiv
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
	AND ( e.early_hiv = '4' or e.early_hiv = '2')
        AND ( DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time AND @to_time)
        AND e.service_id IN (@services)
        AND e.site_id = @siteID 
        AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
GROUP BY e.object_group_id, e.early_hiv, e.service_id;