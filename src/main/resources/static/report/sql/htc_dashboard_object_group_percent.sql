SELECT
        v_main.object_group_id as code,
        v_main.time,
        COALESCE(SUM(v_main.positive), 0) AS positive,
        COALESCE(SUM(v_main.test), 0) AS test,
        p.id,
        p.parent_id
    FROM
        ( select 
            sub.time,
            sub.object_group_id,
            0 AS test,
            COUNT(sub.id) AS positive
          FROM (SELECT
            CONCAT(QUARTER(e.confirm_time), '/', YEAR(e.confirm_time)) AS time,
            e.object_group_id,
            0 AS test,
            e.id
          FROM
              htc_visit AS e
          WHERE e.is_remove = 0 AND e.site_id = @site_id
            AND e.confirm_results_id = 2
            AND e.confirm_time BETWEEN @from_time AND @to_time
            AND e.service_id IN (:services) ORDER BY e.confirm_time asc) as sub
           
          GROUP BY sub.object_group_id , sub.time 

    UNION ALL
    select 
        sub.time,
        sub.object_group_id,
        COUNT(sub.id) AS test,
        0 AS positive
      FROM (SELECT
            CONCAT(QUARTER(e.pre_test_time), '/', YEAR(e.pre_test_time)) AS time,
            e.object_group_id,
            e.id
    FROM htc_visit AS e
    WHERE e.is_remove = 0 AND e.site_id = @site_id
        AND e.pre_test_time BETWEEN @from_time AND @to_time
        AND e.service_id IN (:services) ORDER BY e.pre_test_time asc) as sub

    GROUP BY sub.object_group_id , sub.time 
) AS v_main 
INNER JOIN parameter p on p.code = v_main.object_group_id
WHERE p.type = 'test-object-group'
GROUP BY v_main.object_group_id , v_main.time
