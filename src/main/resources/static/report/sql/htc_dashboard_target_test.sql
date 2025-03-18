SELECT 
    numberTest.quar,
    numberTest.ryear,
    numberTest.resultIds,
    SUM(numberTest.totalOfYear),
    SUM(numberTest.totalTagetTest)
FROM
    (SELECT 
        QUARTER(r.pre_test_time) AS quar,
            YEAR(r.pre_test_time) AS ryear,
            COUNT(r.id) AS resultIds,
            0 AS totalOfYear,
            0 AS totalTagetTest
    FROM
        htc_visit AS r
    WHERE
        YEAR(r.pre_test_time) = YEAR(CURDATE())
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
            0 AS totalTagetTest
    FROM
        htc_visit AS r
    WHERE
            r.is_remove = '0'
            AND r.site_id = @siteID
            AND r.service_id IN (@services)
            AND r.pre_test_time BETWEEN MAKEDATE(YEAR(NOW()), 1) AND CURDATE()
    GROUP BY quar , ryear 
    UNION ALL 
    SELECT 
        0 AS quar,
            0 AS ryear,
            0 AS resultIds,
            0 AS totalOfYear,
            t.number_customer AS totalTagetTest
    FROM
        htc_target t
    WHERE
        t.years = YEAR(CURDATE()) AND t.site_id = @siteID) AS numberTest
GROUP BY numberTest.quar , numberTest.ryear
ORDER BY numberTest.quar , numberTest.ryear;
