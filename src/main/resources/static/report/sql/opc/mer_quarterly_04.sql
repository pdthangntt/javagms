SELECT 
    SUM(IF(n.object_group_id = '1', 1, 0)) ncmt, 
    SUM(IF(n.object_group_id = '3', 1, 0)) msm, 
    SUM(IF(n.object_group_id = '13', 1, 0)) chuyengioi, 
    SUM(IF(n.object_group_id IN ('2','17'), 1, 0)) pnmd, 
    SUM(IF(n.object_group_id = '40', 1, 0)) phamnhan,
    SUM(IF(n.object_group_id IN ('1','2','3','13','40','17'), 1, 0)) tong
FROM (SELECT 
        tbl.arv_id,
        tbl.object_group_id
    FROM(SELECT 
        v.arv_id,
        a.object_group_id
    FROM `opc_visit` as v JOIN `opc_arv_revision` as a ON a.stage_id = v.stage_id JOIN `opc_patient` as p ON p.id = a.patient_id 
    WHERE v.appoinment_time is not null 
        AND v.is_remove = 0  
        AND v.site_id IN (@site_id) 
        AND v.id IN (
        SELECT 
            c.id
        FROM (select * 
            FROM (SELECT
                    m.site_id,
                    m.arv_id,
                    m.appoinment_time, 
                    m.id as id
                FROM opc_visit as m 
                WHERE m.appoinment_time is not null 
                    AND m.is_remove = 0
                    AND DATEDIFF(@to_date, m.appoinment_time) > 28 
                    AND DATEDIFF(@to_date, m.appoinment_time) < 118 
                    AND m.arv_id IN (SELECT m1.arv_id 
                                    FROM (select * 
                                        from (SELECT *
                                            FROM opc_arv_revision as logmax 
                                            WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
                                            order by logmax.created_at desc , logmax.id desc ) as m group by m.arv_id, m.stage_id
                                        ) as m1 JOIN opc_arv as s on s.id = m1.arv_id 
                                    WHERE m1.stage_id = m.stage_id 
                                    AND s.is_remove = 0 
                                    AND m1.is_remove = '0' 
                                    AND m1.registration_type = '2' 
                                    AND m1.treatment_time BETWEEN @from_date AND @to_date 
                                    AND m1.treatment_time <= m.appoinment_time) 
                ORDER BY  m.appoinment_time desc, m.id desc) 
            as vmain GROUP BY vmain.site_id, vmain.arv_id
        ) as c) 
    ORDER BY a.arv_id desc, a.created_at desc) as tbl GROUP BY tbl.arv_id
) as n