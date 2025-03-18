-- Năm trước
-- Gốc
SELECT 
    'ct1' ct1,
    @year yearNumber,
    @quarter quarter,
    tblCt1.gender_id,
    SUM(IF(tblCt1.treatment_time IS NOT NULL AND tblCt1.dob <> '' AND (YEAR(tblCt1.treatment_time) - YEAR(tblCt1.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt1.treatment_time IS NOT NULL AND tblCt1.dob <> '' AND (YEAR(tblCt1.treatment_time) - YEAR(tblCt1.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            JOIN opc_arv arv
            ON t.arv_id = arv.id
            AND t.is_remove = 0 
            AND t.site_id IN (@site_id)
    WHERE 
        (arv.date_of_arrival IS NULL OR arv.date_of_arrival = '')
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt1 GROUP BY ct1, gender_id

UNION ALL
-- Chuyển tới
SELECT 
    'ct2' AS ct2,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt2.gender_id,
    SUM(IF(tblCt2.treatment_time IS NOT NULL AND tblCt2.dob <> '' AND (YEAR(tblCt2.treatment_time) - YEAR( tblCt2.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt2.treatment_time IS NOT NULL AND tblCt2.dob <> '' AND (YEAR(tblCt2.treatment_time) - YEAR( tblCt2.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            AND t.is_remove = 0 
            AND t.site_id  IN (@site_id) 
    WHERE 
        t.status_of_treatment_id = '3'
        AND t.registration_type = '3' 
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt2 GROUP BY ct2, gender_id

UNION ALL
-- Chuyển đi
SELECT 
    'ct3' AS ct3,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt3.gender_id,
    SUM(IF(tblCt3.treatment_time IS NOT NULL AND tblCt3.dob <> '' AND (YEAR(tblCt3.treatment_time) - YEAR( tblCt3.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt3.treatment_time IS NOT NULL AND tblCt3.dob <> '' AND (YEAR(tblCt3.treatment_time) -YEAR( tblCt3.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            AND t.is_remove = 0 
            AND t.site_id IN (@site_id)
    WHERE 
        t.status_of_treatment_id = '3'
        AND t.end_case = '3' 
        AND DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') >= MAKEDATE(@year, 1) AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d')
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt3 GROUP BY ct3, gender_id

UNION ALL

SELECT 
    'ct5' AS ct5,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt5.gender_id,
    SUM(IF(tblCt5.treatment_time IS NOT NULL AND tblCt5.dob <> '' AND (YEAR(tblCt5.treatment_time) - YEAR( tblCt5.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt5.treatment_time IS NOT NULL AND tblCt5.dob <> '' AND (YEAR(tblCt5.treatment_time) - YEAR(  tblCt5.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            JOIN opc_arv arv
            ON t.arv_id = arv.id
            AND t.is_remove = 0 
            AND t.site_id  IN (@site_id)
    WHERE 
        t.status_of_treatment_id = '3'
        AND (arv.date_of_arrival IS NULL OR arv.date_of_arrival = '')
        AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt5 GROUP BY ct5, gender_id

UNION ALL

SELECT 
    'ct6' AS ct6,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt6.gender_id,
    SUM(IF(tblCt6.treatment_time IS NOT NULL AND tblCt6.dob <> '' AND (YEAR(tblCt6.treatment_time) - YEAR(  tblCt6.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt6.treatment_time IS NOT NULL AND tblCt6.dob <> '' AND (YEAR(tblCt6.treatment_time) - YEAR(  tblCt6.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            AND t.is_remove = 0 
            AND t.site_id  IN (@site_id)
    WHERE 
        DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND ((t.end_case = '4' OR t.end_case = '5') AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') >= MAKEDATE(@year, 1) AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.end_time, '%Y-%m-%d'))
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt6 GROUP BY ct6, gender_id

UNION ALL

SELECT 
    'ct7' AS ct7,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt7.gender_id,
    SUM(IF(tblCt7.treatment_time IS NOT NULL AND tblCt7.dob <> '' AND (YEAR(tblCt7.treatment_time) - YEAR(  tblCt7.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt7.treatment_time IS NOT NULL AND tblCt7.dob <> '' AND (YEAR(tblCt7.treatment_time) - YEAR(  tblCt7.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            AND t.is_remove = 0 
            AND t.site_id  IN (@site_id)
    WHERE 
        (t.end_case = '2' AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)
        AND MAKEDATE(@year , 1) <= DATE_FORMAT(t.treatment_time, '%Y-%m-%d') AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.end_time, '%Y-%m-%d')
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt7 GROUP BY ct7, gender_id

UNION ALL

SELECT 
    'ct8' AS ct8,
    (@year + 1) yearNumber,
    @quarter quarter,
    tblCt8.gender_id,
    SUM(IF(tblCt8.treatment_time IS NOT NULL AND tblCt8.dob <> '' AND (YEAR(tblCt8.treatment_time) - YEAR(  tblCt8.dob) < 15), 1, 0)) underFifteen, 
    SUM(IF(tblCt8.treatment_time IS NOT NULL AND tblCt8.dob <> '' AND (YEAR(tblCt8.treatment_time) - YEAR(  tblCt8.dob) >= 15), 1, 0)) overFifteen
FROM
(SELECT t.id,
        p.gender_id,
        t.treatment_time,
        p.dob
    FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
            ON p.id = t.patient_id 
            AND t.is_remove = 0 
            AND t.site_id  IN (@site_id)
    WHERE 
        (t.end_case = '1' AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date)
        AND MAKEDATE(@year, 1) <= DATE_FORMAT(t.treatment_time, '%Y-%m-%d') AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.end_time, '%Y-%m-%d')
        AND t.created_at IN (SELECT Max(created_at) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblCt8 GROUP BY ct8, gender_id

