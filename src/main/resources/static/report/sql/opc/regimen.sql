SELECT 
    vmain.treatment_regiment_id,
    vmain.type,
    SUM(coalesce(vmain.c, 0)) as c,
    SUM(coalesce(vmain.d, 0)) as d,
    SUM(coalesce(vmain.e_chuyendi, 0)) as e_chuyendi,
    SUM(coalesce(vmain.e_tuvong, 0)) as e_tuvong,
    SUM(coalesce(vmain.e_botri, 0)) as e_botri,
    SUM(coalesce(vmain.e_matdau, 0)) as e_matdau,
    SUM(coalesce(vmain.e_chuyenphacdo, 0)) as e_chuyenphacdo,
    SUM(coalesce(vmain.f, 0)) as f
FROM (SELECT  
        main.treatment_regiment_id,
        main.type,
        count(main.arv_id) as c,
        0 as d,
        0 as e_chuyendi,
        0 as e_tuvong,
        0 as e_botri,
        0 as e_matdau,
        0 as e_chuyenphacdo,
        0 as f
    FROM (SELECT 
            m.site_id,
            m.arv_id,
            m.treatment_regiment_id,
            CASE WHEN (YEAR(m.treatment_time) - YEAR(p.dob)) >= 15 THEN 'nl' ELSE 'te' END as type
        FROM `opc_arv_revision` as m 
        INNER JOIN opc_patient as p ON p.id = m.patient_id
        INNER JOIN (SELECT * FROM (SELECT log.arv_id, log.id, log.created_at
                        FROM opc_arv_revision as log 
                        WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date
                            AND log.site_id IN (@site_id) 
                            ORDER BY log.created_at DESC, log.id DESC) as mmax
                        GROUP BY mmax.arv_id) as sub ON sub.id = m.id
        INNER JOIN (SELECT * 
                            FROM (  SELECT 
                                        sub_max.arv_id as arv_id, 
                                        sub_max.stage_id as stage_id_visit, 
                                        sub_max.examination_time,
                                        sub_max.insurance_expiry,
                                        sub_max.insurance_no
                                    FROM opc_visit as sub_max
                                    WHERE sub_max.is_remove = 0 
                                            AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                                            AND sub_max.site_id IN (@site_id) 
                                    ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
        ON m.stage_id = main_visit.stage_id_visit
        WHERE m.is_remove = 0 
            -- dieu kien bao hiem
            AND ( (:insurance = '1' AND ( main_visit.insurance_no is not null AND main_visit.insurance_no <> '' ) ) 
                OR ( :insurance = '2' AND ( main_visit.insurance_no is null OR  main_visit.insurance_no = ''  ))
)


            AND m.treatment_regiment_id is not null
            AND m.site_id IN (@site_id) 
            AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND m.status_of_treatment_id = '3'
            AND p.dob is not null) as main 
    GROUP BY main.treatment_regiment_id, main.type

    UNION ALL
    
    SELECT  
        main.treatment_regiment_id,
        main.type,
        0 as c,
        count(main.arv_id) as d,
        0 as e_chuyendi,
        0 as e_tuvong,
        0 as e_botri,
        0 as e_matdau,
        0 as e_chuyenphacdo,
        0 as f
    FROM (SELECT 
            m.site_id,
            m.arv_id,
            m.treatment_regiment_id,
            CASE WHEN (YEAR(m.treatment_time) - YEAR(p.dob)) >= 15 THEN 'nl' ELSE 'te' END as type
        FROM `opc_arv_revision` as m 
        INNER JOIN opc_patient as p ON p.id = m.patient_id
        INNER JOIN (SELECT * FROM (SELECT 
                        log.arv_id,
                        log.id, 
                        log.created_at
                    FROM opc_arv_revision as log 
                    WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') < @from_date
                        AND log.site_id IN (@site_id) 
                        AND DATE_FORMAT(log.treatment_time, '%Y-%m-%d') < @from_date
                    ORDER BY log.created_at DESC, log.id DESC) as mmax
                    GROUP BY mmax.arv_id) as sub ON sub.id = m.id
                    INNER JOIN (SELECT * 
                            FROM (  SELECT 
                                        sub_max.arv_id as arv_id, 
                                        sub_max.stage_id as stage_id_visit, 
                                        sub_max.examination_time,
                                        sub_max.insurance_expiry,
                                        sub_max.insurance_no
                                    FROM opc_visit as sub_max
                                    WHERE sub_max.is_remove = 0 
                                            AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') < @from_date 
                                            AND sub_max.site_id IN (@site_id) 
                                    ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
        ON m.stage_id = main_visit.stage_id_visit
        WHERE m.is_remove = 0 
            -- dieu kien bao hiem
            AND ( (:insurance = '1' AND ( main_visit.insurance_no is not null AND main_visit.insurance_no <> '' ) ) 
                OR ( :insurance = '2' AND ( main_visit.insurance_no is null OR  main_visit.insurance_no = ''  ))
) 
            AND m.treatment_regiment_id is not null
            AND m.treatment_time is not null
            AND m.site_id IN (@site_id) 
            AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') < @from_date
            AND m.status_of_treatment_id = '3'
            AND p.dob is not null) as main 
    GROUP BY main.treatment_regiment_id, main.type

    UNION ALL
    
    SELECT  
        main.treatment_regiment_id,
        main.type,
        0 as c,
        0 as d,
        CASE WHEN main.end_case = '3' THEN count(main.arv_id) END as e_chuyendi,
        CASE WHEN main.end_case = '2' THEN count(main.arv_id) END as e_tuvong,
        CASE WHEN main.end_case = '1' THEN count(main.arv_id) END as e_botri,
        CASE WHEN main.end_case = '4' THEN count(main.arv_id) END as e_matdau,
        0 as e_chuyenphacdo,
        0 as f
    FROM (SELECT 
            m.site_id,
            m.arv_id,
            m.treatment_regiment_id,
            m.end_case,
            CASE WHEN (YEAR(m.end_time) - YEAR(p.dob)) >= 15 THEN 'nl' ELSE 'te' END as type
        FROM `opc_arv_revision` as m 
        INNER JOIN opc_patient as p ON p.id = m.patient_id
        INNER JOIN (SELECT * FROM (SELECT 
                        log.arv_id,
                        log.id, 
                        log.created_at
                    FROM opc_arv_revision as log 
                    WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date
                        AND log.site_id IN (@site_id) 
                    ORDER BY log.created_at DESC, log.id DESC) as mmax
                GROUP BY mmax.arv_id) as sub ON sub.id = m.id
                    INNER JOIN (SELECT * 
                            FROM (  SELECT 
                                        sub_max.arv_id as arv_id, 
                                        sub_max.stage_id as stage_id_visit, 
                                        sub_max.examination_time,
                                        sub_max.insurance_expiry,
                                        sub_max.insurance_no
                                    FROM opc_visit as sub_max
                                    WHERE sub_max.is_remove = 0 
                                            AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
                                            AND sub_max.site_id IN (@site_id) 
                                    ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
        ON m.stage_id = main_visit.stage_id_visit
        WHERE m.is_remove = 0 
            -- dieu kien bao hiem
            AND ( (:insurance = '1' AND ( main_visit.insurance_no is not null AND main_visit.insurance_no <> '' ) ) 
                OR ( :insurance = '2' AND ( main_visit.insurance_no is null OR  main_visit.insurance_no = ''  ))
)  
            AND m.treatment_regiment_id is not null
            AND m.site_id IN (@site_id) 
            AND DATE_FORMAT(m.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND m.end_case IN ('1','2','3','4')
            AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(m.end_time, '%Y-%m-%d')
            AND p.dob is not null) as main 
    GROUP BY main.treatment_regiment_id, main.end_case, main.type

    UNION ALL
    
    SELECT  
        main.treatment_regiment_id,
        main.type,
        0 as c,
        0 as d,
        0 as e_chuyendi,
        0 as e_tuvong,
        0 as e_botri,
        0 as e_matdau,
        0 as e_chuyenphacdo,
        count(main.arv_id) as f
    FROM (SELECT 
            m.site_id,
            m.arv_id,
            m.treatment_regiment_id,
            CASE WHEN (YEAR(m.treatment_time) - YEAR(p.dob)) >= 15 THEN 'nl' ELSE 'te' END as type
        FROM `opc_arv_revision` as m 
        INNER JOIN opc_patient as p ON p.id = m.patient_id
        INNER JOIN (SELECT * FROM (SELECT 
                        log.arv_id,
                        log.id, 
                        log.created_at
                    FROM opc_arv_revision as log 
                    WHERE DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date
                        AND log.site_id IN (@site_id) 
                    ORDER BY log.created_at DESC, log.id DESC) as mmax
                    GROUP BY mmax.arv_id) as sub ON sub.id = m.id
                    INNER JOIN (SELECT * 
                            FROM (  SELECT 
                                        sub_max.arv_id as arv_id, 
                                        sub_max.stage_id as stage_id_visit, 
                                        sub_max.examination_time,
                                        sub_max.insurance_expiry,
                                        sub_max.insurance_no
                                    FROM opc_visit as sub_max
                                    WHERE sub_max.is_remove = 0 
                                            AND DATE_FORMAT(sub_max.examination_time, '%Y-%m-%d') <= @to_date
                                            AND sub_max.site_id IN (@site_id) 
                                    ORDER BY sub_max.examination_time DESC, sub_max.id DESC) as sub GROUP BY sub.arv_id ) as main_visit
        ON m.stage_id = main_visit.stage_id_visit
        WHERE m.is_remove = 0 
            -- dieu kien bao hiem
            AND ( (:insurance = '1' AND ( main_visit.insurance_no is not null AND main_visit.insurance_no <> '' ) ) 
                OR ( :insurance = '2' AND ( main_visit.insurance_no is null OR  main_visit.insurance_no = ''  ))
)  
            AND m.treatment_regiment_id is not null
            AND m.treatment_time is not null
            AND m.site_id IN (@site_id) 
            AND DATE_FORMAT(m.treatment_time, '%Y-%m-%d') <= @to_date
            AND m.status_of_treatment_id = '3'
            AND p.dob is not null) as main 
    GROUP BY main.treatment_regiment_id, main.type

) vmain GROUP BY vmain.treatment_regiment_id, vmain.type