SELECT 
    numberPositive.quar,
    numberPositive.ryear,
    numberPositive.resultIds,
    SUM(numberPositive.totalOfYear),
    SUM(numberPositive.totalTagetPositive)
FROM
    (SELECT 
        QUARTER(r.confirm_time) AS quar,
            YEAR(r.confirm_time) AS ryear,
            COUNT(r.id) AS resultIds,
            0 AS totalOfYear,
            0 AS totalTagetPositive
    FROM
        htc_visit AS r
    WHERE
        YEAR(r.confirm_time) = YEAR(CURDATE())
            AND r.confirm_results_id = @confirmTestResult
            AND r.is_remove = '0'
            AND r.site_id = @siteID
            AND r.service_id IN (@services)
    GROUP BY quar , ryear 
    UNION ALL 
    SELECT 
        0 AS quar,
            0 AS ryear,
            0 AS resultIds,
            COUNT(r.id) AS totalOfYear,
            0 AS totalTagetPositive
    FROM
        htc_visit AS r
    WHERE
            r.is_remove = '0'
            AND r.confirm_results_id = @confirmTestResult
            AND r.site_id = @siteID
            AND r.service_id IN (@services)
            AND r.confirm_time BETWEEN MAKEDATE(YEAR(NOW()), 1) AND CURDATE()
    GROUP BY quar , ryear 
    UNION ALL 
    SELECT 
        0 AS quar,
            0 AS ryear,
            0 AS resultIds,
            0 AS totalOfYear,
            t.number_positive AS totalTagetPositive
    FROM
        htc_target t
    WHERE
        t.years = YEAR(CURDATE()) AND t.site_id = @siteID) AS numberPositive
GROUP BY numberPositive.quar , numberPositive.ryear
ORDER BY numberPositive.quar , numberPositive.ryear;