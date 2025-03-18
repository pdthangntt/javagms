SELECT 
    'denominator' AS denominator,
    n.gender_id as gender_id,
    SUM(IF(n.treatment_time IS NOT NULL AND n.dob <> '' AND (DATEDIFF(n.treatment_time, n.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(n.treatment_time IS NOT NULL AND n.dob <> '' AND (DATEDIFF(n.treatment_time, n.dob) >= 1) AND (DATEDIFF(n.treatment_time, n.dob) <= 14), 1, 0)) oneToFourteen, 
    SUM(IF(n.treatment_time IS NOT NULL AND n.dob <> '' AND (DATEDIFF(n.treatment_time, n.dob) >= 15), 1, 0)) overFifteen
FROM (SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p ON p.id = t.patient_id 
    WHERE t.treatment_time BETWEEN DATE_ADD(DATE_ADD(LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR )),INTERVAL 1 DAY),INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR )) 
        AND t.is_remove = 0 
        AND t.site_id IN (@site_id) 
        AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR )) GROUP BY arv_id) 
) as n GROUP BY denominator, n.gender_id

UNION ALL
    
SELECT 
    'numerator' AS numerator,
    v.gender_id as gender_id,
    SUM(IF(v.treatment_time IS NOT NULL AND v.dob <> '' AND (DATEDIFF(v.treatment_time, v.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(v.treatment_time IS NOT NULL AND v.dob <> '' AND (DATEDIFF(v.treatment_time, v.dob) >= 1) AND (DATEDIFF(v.treatment_time, v.dob) <= 14), 1, 0)) oneToFourteen, 
    SUM(IF(v.treatment_time IS NOT NULL AND v.dob <> '' AND (DATEDIFF(v.treatment_time, v.dob) >= 15), 1, 0)) overFifteen
FROM (SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p ON p.id = t.patient_id 
    WHERE (t.end_time IS NULL OR t.end_time > @from_date)
        AND t.treatment_time BETWEEN DATE_ADD(DATE_ADD(LAST_DAY(@from_date),INTERVAL 1 DAY),INTERVAL -1 MONTH) AND LAST_DAY(@from_date) 
        AND t.is_remove = 0 
        AND t.site_id IN (@site_id) 
        AND t.id IN (SELECT Max(Id) 
                    FROM opc_arv_revision as log 
                    WHERE log.stage_id = t.stage_id 
                        AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= LAST_DAY(@from_date)
                        AND t.arv_id IN (
                            SELECT sub.arv_id
                            FROM `opc_arv_revision` AS sub
                            WHERE  sub.treatment_time BETWEEN DATE_ADD(DATE_ADD(LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR )),INTERVAL 1 DAY),INTERVAL -1 MONTH) AND LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR ))
                                AND sub.is_remove = 0 
                                AND sub.site_id IN (@site_id) 
                                AND sub.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = sub.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= LAST_DAY(DATE_SUB(@from_date, INTERVAL 1 YEAR )) GROUP BY arv_id) 
                        )  GROUP BY arv_id)   
) as v GROUP BY numerator, v.gender_id
