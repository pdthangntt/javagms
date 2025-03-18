SELECT 
    vmain.site_id, 
    SUM(coalesce(vmain.insurance100, 0)) as insurance100,
    SUM(coalesce(vmain.insurance95, 0)) as insurance95,
    SUM(coalesce(vmain.insurance80, 0)) as insurance80,
    SUM(coalesce(vmain.insuranceOther, 0)) as insuranceOther,
    SUM(coalesce(vmain.insuranceNone, 0)) as insuranceNone,
    SUM(coalesce(vmain.expire1, 0)) as expire1,
    SUM(coalesce(vmain.expire2, 0)) as expire2,
    SUM(coalesce(vmain.expire3, 0)) as expire3,
    SUM(coalesce(vmain.dungtuyen, 0)) as dungtuyen,
    SUM(coalesce(vmain.traituyen, 0)) as traituyen,
    SUM(coalesce(vmain.cobaohiem, 0)) as cobaohiem
 FROM (
    SELECT 
        t.site_id, 
        (CASE WHEN t.insurance_pay = '1' THEN COUNT(t.arv_id) ELSE 0 END) as insurance100,
        (CASE WHEN t.insurance_pay = '2' THEN COUNT(t.arv_id) ELSE 0 END) as insurance95,
        (CASE WHEN t.insurance_pay = '3' THEN COUNT(t.arv_id) ELSE 0 END) as insurance80,
        (CASE WHEN t.insurance_pay not in ('1','2','3') THEN  COUNT(t.arv_id) ELSE 0 END) as insuranceOther,
        0 as insuranceNone,
        0 as expire1,
        0 as expire2,
        0 as expire3,
        0 as dungtuyen,
        0 as traituyen, 0 as cobaohiem
    FROM opc_visit AS t 
    INNER JOIN (SELECT * FROM (SELECT 
                    sub_max.site_id, 
                    sub_max.arv_id, 
                    sub_max.id, 
                    sub_max.examination_time, 
                    sub_max.insurance_pay 
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                    AND sub_max.site_id IN (@site_id) 
                    AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id) as j1 on j1.id = t.id
    WHERE t.is_remove = 0 
        AND t.site_id IN (@site_id) 
        AND DATE_FORMAT(t.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.insurance_no is not null AND t.insurance_no <> '' 
    GROUP BY t.site_id, t.insurance_pay
    
    UNION ALL
    
    SELECT 
        t2.site_id, 
        0 as insurance100,
        0 as insurance95,
        0 as insurance80,
        0 as insuranceOther,
        count(t2.arv_id) as insuranceNone,
        0 as expire1,
        0 as expire2,
        0 as expire3,
        0 as dungtuyen,
        0 as traituyen, 0 as cobaohiem
    FROM `opc_visit` as t2 
    INNER JOIN (SELECT * FROM (SELECT 
                    sub_max.site_id, 
                    sub_max.arv_id, 
                    sub_max.id, 
                    sub_max.examination_time, 
                    sub_max.insurance_pay 
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                    AND sub_max.site_id IN (@site_id) 
                    AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id) as j1 on j1.id = t2.id
    WHERE ( t2.insurance_no is null OR t2.insurance_no = '')
        AND t2.is_remove = 0 
        AND t2.site_id IN (@site_id) 
        AND DATE_FORMAT(t2.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
    GROUP BY t2.site_id

    UNION ALL

    select 
	main.site_id,
        0 as insurance100,
        0 as insurance95,
        0 as insurance80,
        0 as insuranceOther,
        0 as insuranceNone,
        (CASE WHEN main.expiry >= 1 AND main.expiry <= 30 THEN COUNT(main.arv_id) ELSE 0 END) as expire1,
        (CASE WHEN main.expiry > 30 AND main.expiry <= 60 THEN COUNT(main.arv_id) ELSE 0 END) as expire2,
        (CASE WHEN main.expiry > 60 AND main.expiry <= 90 THEN COUNT(main.arv_id) ELSE 0 END) as expire3,
        0 as dungtuyen,
        0 as traituyen, 0 as cobaohiem
    from (SELECT 
            t.site_id, 
            t.arv_id,
            DATEDIFF(DATE_FORMAT(t.insurance_expiry, '%Y-%m-%d'), DATE_FORMAT(@to_date, '%Y-%m-%d')) AS expiry
        FROM opc_visit AS t 
        INNER JOIN (SELECT * FROM (SELECT 
                    sub_max.site_id, 
                    sub_max.arv_id, 
                    sub_max.id, 
                    sub_max.examination_time, 
                    sub_max.insurance_pay 
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                    AND sub_max.site_id IN (@site_id) 
                    AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id) as j1 on j1.id = t.id
        WHERE t.is_remove = 0 AND t.insurance_no is not null AND t.insurance_no <> ''  AND t.insurance_expiry is not null 
        AND DATE_FORMAT(t.INSURANCE_EXPIRY, '%Y-%m-%d') > @to_date
        AND t.site_id IN (@site_id) 
        AND DATE_FORMAT(t.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        GROUP BY t.site_id, t.arv_id) as main WHERE main.expiry > 0 AND main.expiry <= 90  GROUP BY main.site_id, main.expiry
    
    UNION ALL

    select 
	main.site_id,
        0 as insurance100,
        0 as insurance95,
        0 as insurance80,
        0 as insuranceOther,
        0 as insuranceNone,
        0 as expire1,
        0 as expire2,
        0 as expire3,
        (CASE WHEN main.route_id = '1' THEN COUNT(main.arv_id) ELSE 0 END) as dungtuyen,
        (CASE WHEN main.route_id = '2' THEN COUNT(main.arv_id) ELSE 0 END) as traituyen, 0 as cobaohiem
    from (SELECT 
            t.site_id, 
            t.arv_id,
            t.route_id
        FROM opc_visit AS t 
        INNER JOIN (SELECT * FROM (SELECT 
                    sub_max.site_id, 
                    sub_max.arv_id, 
                    sub_max.id, 
                    sub_max.examination_time, 
                    sub_max.insurance_pay 
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                    AND sub_max.site_id IN (@site_id) 
                    AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id) as j1 on j1.id = t.id
        WHERE t.is_remove = 0 
            AND t.insurance_no is not null AND t.insurance_no <> ''
            AND t.route_id is not null
            AND t.site_id IN (@site_id) 
            AND DATE_FORMAT(t.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        GROUP BY t.site_id, t.arv_id) as main GROUP BY main.site_id, main.route_id

UNION ALL

    SELECT 
        t.site_id, 
        0 as insurance100,
        0 as insurance95,
        0 as insurance80,
        0 as insuranceOther,
        0 as insuranceNone,
        0 as expire1,
        0 as expire2,
        0 as expire3,
        0 as dungtuyen,
        0 as traituyen, COUNT(t.arv_id) as cobaohiem
    FROM opc_visit AS t 
    INNER JOIN (SELECT * FROM (SELECT 
                    sub_max.site_id, 
                    sub_max.arv_id, 
                    sub_max.id, 
                    sub_max.examination_time, 
                    sub_max.insurance_pay 
                FROM opc_visit as sub_max
                WHERE sub_max.is_remove = 0 
                    AND sub_max.site_id IN (@site_id) 
                    AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id) as j1 on j1.id = t.id
    WHERE t.is_remove = 0 
        AND t.site_id IN (@site_id) 
        AND DATE_FORMAT(t.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.insurance_no is not null AND t.insurance_no <> '' 
    GROUP BY t.site_id

) as vmain GROUP BY vmain.site_id