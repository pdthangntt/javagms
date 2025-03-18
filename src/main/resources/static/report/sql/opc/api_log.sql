
SELECT 
    sum(main.success) as success,
    sum(main.error) as error
FROM (
        SELECT 
            (case When t.status = true then count(t.ARV_CODE) else 0 end) AS success,
            (case When t.status = false then count(t.ARV_CODE) else 0 end) AS error
            FROM (
                SELECT * 
                    FROM (
                        SELECT * 
                            FROM `opc_api_log` as e 
                            WHERE e.SOURCE_SITE_ID = @ID
                                AND ( :status = '' OR ( :status = '1' AND e.status = true) OR ( :status = '2' AND e.status = false ) ) 
                                AND (DATE_FORMAT(e.time, '%Y-%m-%d') >= @from OR @from IS NULL) 
                                AND (DATE_FORMAT(e.time, '%Y-%m-%d') <= @to OR @to IS NULL)
                            ORDER BY e.time desc
                        ) as x GROUP BY x.ARV_CODE
        ) as t GROUP BY t.status 
) as main 
    