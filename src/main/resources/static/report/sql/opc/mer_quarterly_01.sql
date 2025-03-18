SELECT 
    'underThreeMonth' AS underThreeMonth,
    tblUnderThree.gender_id,
    SUM(IF(tblUnderThree.treatment_time IS NOT NULL AND tblUnderThree.dob <> '' AND YEAR(tblUnderThree.treatment_time) - YEAR(tblUnderThree.dob) < 1 , 1, 0)) underOneAge, 
    SUM(IF(tblUnderThree.treatment_time IS NOT NULL AND tblUnderThree.dob <> '' AND YEAR(tblUnderThree.treatment_time) - YEAR(tblUnderThree.dob) <= 14 AND YEAR(tblUnderThree.treatment_time) - YEAR(tblUnderThree.dob) >= 1 , 1, 0)) oneToFourteen, 
    SUM(IF(tblUnderThree.treatment_time IS NOT NULL AND tblUnderThree.dob <> '' AND YEAR(tblUnderThree.treatment_time) - YEAR(tblUnderThree.dob) >= 15, 1, 0)) overFifteen
FROM
(
    SELECT t.id,
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
            AND (t.days_received > 0 AND t.days_received < 84)
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
) tblUnderThree GROUP BY underThreeMonth, gender_id

UNION ALL 

SELECT 
    'threeToSixMonth' AS threeToSixMonth,
    tblThreeToSix.gender_id,
    SUM(IF(tblThreeToSix.treatment_time IS NOT NULL AND tblThreeToSix.dob <> '' AND YEAR(tblThreeToSix.treatment_time) - YEAR(tblThreeToSix.dob) < 1 , 1, 0)) underOneAge, 
    SUM(IF(tblThreeToSix.treatment_time IS NOT NULL AND tblThreeToSix.dob <> '' AND YEAR(tblThreeToSix.treatment_time) - YEAR(tblThreeToSix.dob) <= 14 AND YEAR(tblThreeToSix.treatment_time) - YEAR(tblThreeToSix.dob) >= 1 , 1, 0)) oneToFourteen, 
    SUM(IF(tblThreeToSix.treatment_time IS NOT NULL AND tblThreeToSix.dob <> '' AND YEAR(tblThreeToSix.treatment_time) - YEAR(tblThreeToSix.dob) >= 15, 1, 0)) overFifteen
FROM
(
    SELECT t.id,
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
            AND t.days_received >= 84 AND t.days_received < 180
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
) tblThreeToSix GROUP BY threeToSixMonth, gender_id

UNION ALL 

SELECT 
    'fromSixMonth' AS fromSixMonth,
    tblSixMonth.gender_id,
    SUM(IF(tblSixMonth.treatment_time IS NOT NULL AND tblSixMonth.dob <> '' AND YEAR(tblSixMonth.treatment_time) - YEAR(tblSixMonth.dob) < 1 , 1, 0)) underOneAge, 
    SUM(IF(tblSixMonth.treatment_time IS NOT NULL AND tblSixMonth.dob <> '' AND YEAR(tblSixMonth.treatment_time) - YEAR(tblSixMonth.dob) <= 14 AND YEAR(tblSixMonth.treatment_time) - YEAR(tblSixMonth.dob) >= 1 , 1, 0)) oneToFourteen, 
    SUM(IF(tblSixMonth.treatment_time IS NOT NULL AND tblSixMonth.dob <> '' AND YEAR(tblSixMonth.treatment_time) - YEAR(tblSixMonth.dob) >= 15, 1, 0)) overFifteen
FROM
(
    SELECT t.id,
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
            AND t.days_received >= 180
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
) tblSixMonth GROUP BY fromSixMonth, gender_id
