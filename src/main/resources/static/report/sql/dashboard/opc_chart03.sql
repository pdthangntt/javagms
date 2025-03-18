SELECT 
    v.d, 
    SUM(v.tong) AS tong,
    SUM(v.khongphathien) AS khongphathien,
    SUM(v.duoinguongphathien) AS duoinguongphathien,
    SUM(v.duoi200) AS duoi200,
    SUM(v.tu200den1000) AS tu200den1000,
    SUM(v.tren1000) AS tren1000 
FROM (
    SELECT 
        CASE 
            WHEN (finalMain.test_time IS NOT NULL ) THEN CONCAT(QUARTER(finalMain.test_time), '/', YEAR(finalMain.test_time))
        END AS d, 
        CASE WHEN (finalMain.test_time IS NOT NULL) THEN 1 ELSE 0 END AS tong,   
        CASE WHEN (finalMain.viral_result = '1') THEN 1 ELSE 0 END AS khongphathien,    
        CASE WHEN (finalMain.viral_result = '6') THEN 1 ELSE 0 END AS duoinguongphathien,    
        CASE WHEN (finalMain.viral_result = '2') THEN 1 ELSE 0 END AS duoi200,    
        CASE WHEN (finalMain.viral_result = '3') THEN 1 ELSE 0 END AS tu200den1000,    
        CASE WHEN (finalMain.viral_result = '4') THEN 1 ELSE 0 END AS tren1000
    FROM (SELECT * FROM (SELECT
            main.arv_id as arv_id,
            main.site_id AS site_id,
            main.test_time AS test_time,
            main.result AS viral_result,
            p.gender_id AS gender_id,
            CASE WHEN (YEAR(CURDATE()) - YEAR(p.dob)) >= 15 THEN '2' ELSE '1' END AS type
        FROM opc_viral_load AS main INNER JOIN opc_patient AS p ON p.id = main.patient_id INNER JOIN opc_stage AS s ON s.id = main.stage_id 
        WHERE main.test_time IS NOT NULL 
            AND s.is_remove = 0 
            AND main.test_time BETWEEN @from_date AND @to_date
            AND main.is_remove = 0  
            AND main.site_id IN (@site_id) 
            AND main.id IN (
                    SELECT
                        m.id AS id
                    FROM opc_viral_load AS m 
                    WHERE m.test_time IS NOT NULL 
                        AND m.is_remove = 0  
                        AND m.arv_id IN (SELECT m1.arv_id 
                                        FROM (SELECT * 
                                            FROM (SELECT *
                                                FROM opc_arv_revision AS logmax 
                                                WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                                ORDER BY logmax.id DESC , logmax.created_at DESC) AS m 
                                            GROUP BY m.arv_id, m.stage_id) AS m1
                                        WHERE m1.stage_id = m.stage_id 
                                            AND m1.is_remove = '0' 
                                            AND m1.status_of_treatment_id = '3' 
                                            AND m1.treatment_time <= m.test_time) 
                    ) 
                    ORDER BY  main.test_time DESC) as c
            GROUP BY c.arv_id
    ) AS finalMain
    WHERE finalMain.site_id IN (@site_id)
        AND finalMain.gender_id IN (@gender_id)
        AND finalMain.type IN (@age)
) AS v GROUP BY v.d 