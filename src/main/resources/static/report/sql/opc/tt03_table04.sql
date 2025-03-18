SELECT 
    main.site_id,
    coalesce(SUM(main.register), 0) as register,
    coalesce(SUM(main.total), 0) as total,
    coalesce(SUM(main.inh), 0) as inh
FROM (
    SELECT
	l.site_id,
	COUNT(l.arv_id) as register,
        0 as total,
        0 as inh
    FROM opc_arv_revision as l 
    WHERE 
        l.is_remove = 0
	AND l.registration_time BETWEEN @from_date AND @to_date
        AND l.status_of_treatment_id = '2'
        AND (l.tranfer_from_time IS NULL OR (l.tranfer_from_time IS NOT NULL AND DATE_FORMAT(l.tranfer_from_time, '%Y-%m-%d') > @to_date))
        AND l.site_id IN (@site_id) 
                    AND l.id IN (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.is_remove = 0 AND sub.arv_id = l.arv_id) 
    GROUP BY l.site_id

    UNION ALL

    SELECT kq.site_id, 
        0 as register, 
        kq.tong as total,
        0 as inh
    FROM (
        SELECT to_end.site_id, 
            SUM(to_end.tong) as tong
        FROM 
            (   SELECT l.site_id, 
                count(l.arv_id) as tong
                FROM opc_arv_revision as l 
                WHERE 
                    l.is_remove = 0
                    AND DATE_FORMAT(l.registration_time, '%Y-%m-%d') <= @to_date
                    AND l.status_of_treatment_id = '2'
                    AND (l.tranfer_from_time IS NULL OR ( l.tranfer_from_time IS NOT NULL AND DATE_FORMAT(l.tranfer_from_time, '%Y-%m-%d') > @to_date ))
                    AND l.site_id IN (@site_id) 
                    AND l.id IN (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.is_remove = 0 AND sub.arv_id = l.arv_id) 
                GROUP BY l.site_id
        ) as to_end 

        GROUP BY to_end.site_id
    ) as kq 

    UNION ALL

    SELECT 
        m.site_id,  
        0 as register, 
        0 as total,
        count(m.arv_id) as inh
    FROM (
        SELECT 
                t.site_id,
                t.arv_id,
                MAX(t.inh_from_time),
                (select sub.is_remove from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) AS remove
                FROM `opc_test` as t
           WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
                AND t.inh_from_time is not null AND t.inh_from_time BETWEEN @from_date AND @to_date
                AND t.id = (select MAX(v.id) from opc_test as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                GROUP BY t.arv_id
    ) as m
    WHERE m.remove = 0 GROUP BY m.site_id, m.arv_id 
) as main GROUP BY main.site_id