SELECT 
    'normal' as normalO,
    tblUnderThree.object_group_id,
    COUNT(tblUnderThree.id) quantity
FROM
(
    SELECT t.id,
            t.object_group_id,
            p.gender_id,
            t.treatment_time,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                INNER JOIN (select * from (SELECT   logmax.id  as id_log ,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc, logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
                ON p.id = t.patient_id  AND main_log.stage_id_log = t.stage_id AND main_log.arv_id_log = t.arv_id  AND main_log.id_log = t.id
        WHERE 
            DATE_FORMAT(t.treatment_time , '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.status_of_treatment_id = '3'
            AND t.registration_type NOT IN ('2' ,'3')
            AND t.object_group_id IN ('1', '3', '13', '2', '40', '17')
            AND t.is_remove = 0 
            AND t.site_id IN (@site_id) 
) tblUnderThree GROUP BY object_group_id

UNION ALL 

SELECT 
    'hasChild' as hasChild,
    0 as object_group_id,
    COUNT(tblUnderThree.id) quantity
FROM
(
    SELECT t.id,
            t.object_group_id,
            p.gender_id,
            t.treatment_time,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                INNER JOIN (select * from (SELECT   logmax.id  as id_log ,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc, logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
                ON p.id = t.patient_id  AND main_log.stage_id_log = t.stage_id AND main_log.arv_id_log = t.arv_id  AND main_log.id_log = t.id
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id) 
        WHERE 
            DATE_FORMAT(t.treatment_time , '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND DATE_FORMAT(t.treatment_time , '%Y-%m-%d') >= DATE_FORMAT(t.pregnant_end_date, '%Y-%m-%d')
            AND t.status_of_treatment_id = '3'
            AND t.is_remove = 0 
            AND t.site_id IN (@site_id) 
) tblUnderThree