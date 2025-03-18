SELECT 
    main.site_id,
    coalesce(SUM(main.tlvr), 0) as tlvr,
    coalesce(SUM(main.tlvr1000), 0) as tlvr1000,
    coalesce(SUM(main.month), 0) as month,
    coalesce(SUM(main.month1000), 0) as month1000
FROM (
    SELECT m.site_id, 
           count(m.arv_id) as tlvr, 
           0 as tlvr1000,
           0 as month,
           0 as month1000
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '6') as cause,
                        (select sub.status_of_treatment_id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_stage,
                        (select sub.treatment_time from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_time,
                        (select sub.is_remove from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) AS is_remove
                    FROM opc_viral_load as t 
                    WHERE t.is_remove = 0  
                    AND t.id = (select MAX(v.id) from opc_viral_load as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.cause = '6' AND sub.is_remove = 0 
            AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) >= 5
            AND sub.treatment_stage='3'
            GROUP BY sub.site_id, sub.arv_id
    ) as m GROUP BY m.site_id

    UNION ALL

    SELECT m.site_id, 
            0 as tlvr, 
            count(m.arv_id) as tlvr1000,
            0 as month,
            0 as month1000
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '6') as cause,
                        (select sub.status_of_treatment_id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_stage,
                        (select sub.treatment_time from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_time,
                        (select sub.is_remove from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) AS is_remove
                    FROM opc_viral_load as t 
                    WHERE t.is_remove = 0  AND t.id = (select MAX(v.id) from opc_viral_load as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                    AND t.site_id IN (@site_id) 
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.cause = '6'  AND sub.is_remove = 0
                AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) >= 5
                AND sub.treatment_stage='3'
                  AND (sub.result = '1' OR  sub.result ='2' OR sub.result = '3' OR sub.result = '6' )
            GROUP BY sub.site_id, sub.arv_id
    ) as m GROUP BY m.site_id

    UNION ALL

    SELECT m.site_id, 
            0 as tlvr, 
            0 as tlvr1000,
            count(m.arv_id) as month,
            0 as month1000
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '3') as cause,
                        (select sub.status_of_treatment_id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_stage,
                        (select sub.treatment_time from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_time,
                        (select sub.is_remove from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) AS is_remove
                    FROM opc_viral_load as t 
                    WHERE t.is_remove = 0  AND t.site_id IN (@site_id) AND t.id = (select MAX(v.id) from opc_viral_load as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.cause = '3' AND sub.is_remove = 0
            AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) >= 11
            AND sub.treatment_stage='3'
            GROUP BY sub.site_id, sub.arv_id
    ) as m GROUP BY m.site_id

    UNION ALL

    SELECT m.site_id, 
            0 as tlvr, 
            0 as tlvr1000,
            0 as month,
            count(m.arv_id) as month1000
    FROM (
            SELECT
            sub.site_id,
            sub.arv_id
            FROM (  SELECT 
                        t.id,
                        t.site_id,
                        t.arv_id,
                        t.test_time,
                        t.result,
                        (select v.parameter_id from opc_parameter as v where v.type='viralload' AND v.object_id = t.id AND v.parameter_id = '3') as cause,
                        (select sub.status_of_treatment_id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_stage,
                        (select sub.treatment_time from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)  AS treatment_time,
                        (select sub.is_remove from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) AS is_remove
                    FROM opc_viral_load as t 
                    WHERE t.is_remove = 0  AND t.site_id IN (@site_id) AND t.id = (select MAX(v.id) from opc_viral_load as v where v.arv_id = t.arv_id AND v.is_remove = 0)
                    AND t.test_time BETWEEN @from_date AND @to_date
                  ) as sub
            WHERE sub.cause = '3'  AND sub.is_remove = 0
                    AND TIMESTAMPDIFF(MONTH, LAST_DAY(sub.treatment_time), LAST_DAY(sub.test_time)  + INTERVAL 1 DAY) >= 11
                    AND sub.treatment_stage='3'
                    AND (sub.result = '1' OR  sub.result ='2' OR sub.result = '3' OR sub.result = '6' )
            GROUP BY sub.site_id, sub.arv_id
    ) as m GROUP BY m.site_id

) as main GROUP BY main.site_id