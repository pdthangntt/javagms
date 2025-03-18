SELECT 
    main.site_id,
    main.treatment_regiment_stage,
    coalesce(SUM(main.preArv), 0) as preArv,                -- cuối kỳ bc trước
    coalesce(SUM(main.firstRegister), 0) as firstRegister,  -- 2.1 bắt đầu điều trị lần đầu tiên
    coalesce(SUM(main.backTreatment), 0) as backTreatment,  -- 2.2 điều trị lại
    coalesce(SUM(main.transferTo), 0) as transferTo,        -- 2.3 chuyển đến
    coalesce(SUM(main.moveAway), 0) as moveAway,            -- 2.4 chuyển đi
    coalesce(SUM(main.quitTreatment), 0) as quitTreatment,  -- 2.5 bỏ trị
    coalesce(SUM(main.dead), 0) as dead,                    -- từ vong
    coalesce(SUM(main.arv), 0) as arv,                      -- arv cuối kì báo cáo
    coalesce(SUM(main.ward), 0) as ward                     -- arv tại tuyến xã
FROM (
-- điều trị ARV cuối kỳ báo cáo trước
    SELECT t.site_id, t.treatment_regiment_stage,
            count(t.id) as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') < @from_date
        AND (t.tranfer_from_time IS NULL OR ( t.tranfer_from_time IS NOT NULL AND DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d') >= @from_date ))
        AND t.status_of_treatment_id = '3'
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- bắt đầu điều trị lần đầu tiên
    SELECT t.site_id, t.treatment_regiment_stage,
            0 as preArv, 
            count(t.id) as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.status_of_treatment_id = '3'
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- Số bệnh nhân điều trị lại
    SELECT t.site_id, t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            count(t.id) as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.registration_type = '2'
        AND t.status_of_treatment_id = '3'
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- bệnh nhân chuyển đến
    SELECT t.site_id,  t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            count(t.id) as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t join opc_arv arv
            on t.arv_id = arv.id
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.registration_type = '3'
        AND t.status_of_treatment_id = '3'
        AND DATE_FORMAT(arv.date_of_arrival, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL

-- Chuyển đi
    SELECT t.site_id,  t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            count(t.id) as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.status_of_treatment_id = '3'
        AND DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d')
        AND t.end_case = '3'
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- Bỏ điều trị
    SELECT t.site_id,  t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            count(t.id) as quitTreatment,
            0 as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.end_case = '1'
        AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- Tử vong
    SELECT t.site_id,  t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            count(t.id) as dead,
            0 as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.end_case = '2'
        AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) 
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
-- Điều trị cuối kỳ báo cáo
    SELECT t.site_id,  t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            count(t.id) as arv,
            0 as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
        AND t.status_of_treatment_id = '3'
        AND (t.tranfer_from_time IS NULL OR ( t.tranfer_from_time IS NOT NULL AND DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d') > @to_date ))
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id) 
    GROUP BY t.site_id, t.treatment_regiment_stage

    UNION ALL
    -- Ngày nhận thuốc tại xã
    SELECT t.site_id, t.treatment_regiment_stage,
            0 as preArv, 
            0 as firstRegister,
            0 as backTreatment,
            0 as transferTo,
            0 as moveAway,
            0 as quitTreatment,
            0 as dead,
            0 as arv,
            count(t.id) as ward
    FROM `opc_arv_revision` as t 
    WHERE t.is_remove = 0 AND t.site_id IN (@site_id) 
        AND t.received_ward_date is not null
        AND (
                (DATE_FORMAT(t.received_ward_date, '%Y-%m-%d') BETWEEN @from_date AND @to_date) 
                OR ((DATE_FORMAT(DATE_ADD(t.received_ward_date, INTERVAL t.days_received DAY), '%Y-%m-%d')) BETWEEN  @from_date AND @to_date )
                OR (@from_date BETWEEN DATE_FORMAT(t.received_ward_date, '%Y-%m-%d') AND DATE_FORMAT(DATE_ADD(t.received_ward_date, INTERVAL t.days_received DAY), '%Y-%m-%d')) 
                OR (@to_date BETWEEN DATE_FORMAT(t.received_ward_date, '%Y-%m-%d') AND DATE_FORMAT(DATE_ADD(t.received_ward_date, INTERVAL t.days_received DAY), '%Y-%m-%d'))
            )
        AND t.treatment_time is not null AND t.treatment_time <> ''
        AND t.status_of_treatment_id = '3'
        AND t.id = (select sub.id from (SELECT * FROM (SELECT * FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date 
							order by logmax.created_at desc , logmax.id desc) as m1   group by m1.arv_id) as sub 
                                                    WHERE sub.stage_id = t.stage_id AND sub.arv_id = t.arv_id)   
    GROUP BY t.site_id, t.treatment_regiment_stage
) as main WHERE main.treatment_regiment_stage IN ('1', '2') GROUP BY main.site_id, main.treatment_regiment_stage