SELECT 
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
                ON p.id = t.patient_id 
            JOIN (  SELECT  m.*
                    FROM 
                        (SELECT * FROM opc_visit e 
                        WHERE e.is_remove = 0
                            -- AND e.site_id IN (@site_id)
                            AND e.appoinment_time is not null 
                            -- AND DATE_FORMAT(e.appoinment_time, '%Y-%m-%d') < @from_date
                        ORDER BY e.appoinment_time desc, e.id desc) as m 
                    group by m.arv_id
                ) as visit
                INNER JOIN (select * from (SELECT   logmax.id as id_log,
                                                    logmax.arv_id as arv_id_log,
                                                    logmax.stage_id as stage_id_log
					FROM opc_arv_revision as logmax 
							WHERE DATE_FORMAT(logmax.created_at, '%Y-%m-%d') <= @to_date  AND logmax.site_id IN (@site_id)
							order by logmax.created_at desc, logmax.id desc ) as m1 group by m1.arv_id_log, m1.stage_id_log) as main_log
                ON visit.arv_id = t.arv_id AND visit.stage_id = t.stage_id AND main_log.stage_id_log = t.stage_id AND main_log.arv_id_log = t.arv_id AND main_log.id_log = t.id
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id) 
        WHERE 
             t.status_of_treatment_id = '3'
            AND (t.end_time IS NULL OR t.end_time = '')
            AND (t.tranfer_from_time IS NULL OR t.tranfer_from_time = '')
            AND DATE_FORMAT(DATE_ADD(visit.appoinment_time , INTERVAL 28 DAY), '%Y-%m-%d') < @from_date
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
            AND t.object_group_id IN ('1', '3', '13', '2', '40' , '17')
) tblUnderThree GROUP BY object_group_id

