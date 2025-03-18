-- Hiện tại đang được điều trị ARV đến cuối kỳ BC
SELECT 
    'onArv' AS onArv,
    tblOnArv.gender_id,
    SUM(IF(tblOnArv.treatment_time IS NOT NULL AND tblOnArv.dob <> '' AND (DATEDIFF(tblOnArv.treatment_time, tblOnArv.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblOnArv.treatment_time IS NOT NULL AND tblOnArv.dob <> '' AND (DATEDIFF(tblOnArv.treatment_time, tblOnArv.dob) > 0) AND (DATEDIFF(tblOnArv.treatment_time, tblOnArv.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblOnArv.treatment_time IS NOT NULL AND tblOnArv.dob <> '' AND (DATEDIFF(tblOnArv.treatment_time, tblOnArv.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            t.treatment_time,
            p.dob
        FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id) 
        WHERE 
            DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
            AND t.status_of_treatment_id = '3'
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblOnArv GROUP BY onArv, gender_id

UNION ALL 

-- Bắt đầu được điều trị ARV tại cơ sở trong kỳ báo cáo
SELECT 
    'startArv' AS startArv,
    tblStartArv.gender_id,
    SUM(IF(tblStartArv.treatment_time IS NOT NULL AND tblStartArv.dob <> '' AND (DATEDIFF(tblStartArv.treatment_time, tblStartArv.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblStartArv.treatment_time IS NOT NULL AND tblStartArv.dob <> '' AND (DATEDIFF(tblStartArv.treatment_time, tblStartArv.dob) > 0) AND (DATEDIFF(tblStartArv.treatment_time, tblStartArv.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblStartArv.treatment_time IS NOT NULL AND tblStartArv.dob <> '' AND (DATEDIFF(tblStartArv.treatment_time, tblStartArv.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            t.treatment_time,
            p.dob
        FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id) 
        WHERE 
            DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.status_of_treatment_id = '3'
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblStartArv GROUP BY startArv, gender_id

-- Điều trị lại trong kỳ báo cáo
UNION ALL 

SELECT 
    'reTreatment' AS reTreatment,
    tblReTreatment.gender_id,
    SUM(IF(tblReTreatment.treatment_time IS NOT NULL AND tblReTreatment.dob <> '' AND (DATEDIFF(tblReTreatment.treatment_time, tblReTreatment.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblReTreatment.treatment_time IS NOT NULL AND tblReTreatment.dob <> '' AND (DATEDIFF(tblReTreatment.treatment_time, tblReTreatment.dob) > 0) AND (DATEDIFF(tblReTreatment.treatment_time, tblReTreatment.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblReTreatment.treatment_time IS NOT NULL AND tblReTreatment.dob <> '' AND (DATEDIFF(tblReTreatment.treatment_time, tblReTreatment.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            t.treatment_time,
            p.dob
        FROM `opc_arv_revision` AS t JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id) 
        WHERE 
            DATE_FORMAT(t.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.status_of_treatment_id = '3'
            AND t.registration_type = '2'
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblReTreatment GROUP BY reTreatment, gender_id

UNION ALL 

-- Chuyển tới trong kì báo cáo
SELECT 
    'arrival' AS arrival,
    tblArrival.gender_id,
    SUM(IF(tblArrival.date_of_arrival IS NOT NULL AND tblArrival.dob <> '' AND (DATEDIFF(tblArrival.date_of_arrival, tblArrival.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblArrival.date_of_arrival IS NOT NULL AND tblArrival.dob <> '' AND (DATEDIFF(tblArrival.date_of_arrival, tblArrival.dob) > 0) AND (DATEDIFF(tblArrival.date_of_arrival, tblArrival.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblArrival.date_of_arrival IS NOT NULL AND tblArrival.dob <> '' AND (DATEDIFF(tblArrival.date_of_arrival, tblArrival.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            arv.date_of_arrival,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id)
            JOIN opc_arv arv
                ON t.arv_id = arv.id
        WHERE 
            t.status_of_treatment_id = '3'
            AND t.registration_type = '3'
            AND DATE_FORMAT(arv.date_of_arrival, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblArrival GROUP BY arrival, gender_id

UNION ALL 

-- Chuyển đi trong kì báo cáo
SELECT 
    'moveAway' AS moveAway,
    tblMoveAway.gender_id,
    SUM(IF(tblMoveAway.tranfer_from_time IS NOT NULL AND tblMoveAway.dob <> '' AND (DATEDIFF(tblMoveAway.tranfer_from_time, tblMoveAway.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblMoveAway.tranfer_from_time IS NOT NULL AND tblMoveAway.dob <> '' AND (DATEDIFF(tblMoveAway.tranfer_from_time, tblMoveAway.dob) > 0) AND (DATEDIFF(tblMoveAway.tranfer_from_time, tblMoveAway.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblMoveAway.tranfer_from_time IS NOT NULL AND tblMoveAway.dob <> '' AND (DATEDIFF(tblMoveAway.tranfer_from_time, tblMoveAway.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            arv.tranfer_from_time,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id)
            JOIN opc_arv arv
                ON t.arv_id = arv.id
        WHERE 
            t.status_of_treatment_id = '3'
            AND t.end_case = '3'
            AND DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.tranfer_from_time, '%Y-%m-%d')
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblMoveAway GROUP BY moveAway, gender_id

UNION ALL 

-- Tử vong trong kỳ báo cáo
SELECT 
    'dead' AS dead,
    tblDead.gender_id,
    SUM(IF(tblDead.end_time IS NOT NULL AND tblDead.dob <> '' AND (DATEDIFF(tblDead.end_time, tblDead.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblDead.end_time IS NOT NULL AND tblDead.dob <> '' AND (DATEDIFF(tblDead.end_time, tblDead.dob) > 0) AND (DATEDIFF(tblDead.end_time, tblDead.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblDead.end_time IS NOT NULL AND tblDead.dob <> '' AND (DATEDIFF(tblDead.end_time, tblDead.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            arv.end_time,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id)
            JOIN opc_arv arv
                ON t.arv_id = arv.id
        WHERE 
            t.end_case = '2'
            AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.end_time, '%Y-%m-%d')
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblDead GROUP BY dead, gender_id

UNION ALL 

-- Tử vong trong kỳ báo cáo
SELECT 
    'quitArv' AS quitArv,
    tblQuit.gender_id,
    SUM(IF(tblQuit.end_time IS NOT NULL AND tblQuit.dob <> '' AND (DATEDIFF(tblQuit.end_time, tblQuit.dob) < 1), 1, 0)) underOneAge, 
    SUM(IF(tblQuit.end_time IS NOT NULL AND tblQuit.dob <> '' AND (DATEDIFF(tblQuit.end_time, tblQuit.dob) > 0) AND (DATEDIFF(tblQuit.end_time, tblQuit.dob) < 15), 1, 0)) oneToFourteen, 
    SUM(IF(tblQuit.end_time IS NOT NULL AND tblQuit.dob <> '' AND (DATEDIFF(tblQuit.end_time, tblQuit.dob) >= 15), 1, 0)) overFifteen
FROM
(
    SELECT t.id,
            p.gender_id,
            arv.end_time,
            p.dob
        FROM `opc_arv_revision` AS t 
            JOIN opc_patient AS p 
                ON p.id = t.patient_id 
                AND t.is_remove = 0 
                AND t.site_id IN (@site_id)
            JOIN opc_arv arv
                ON t.arv_id = arv.id
        WHERE 
            t.end_case = '1'
            AND DATE_FORMAT(t.end_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
            AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(t.end_time, '%Y-%m-%d')
            AND t.id IN (SELECT Max(Id) FROM opc_arv_revision as log WHERE log.stage_id = t.stage_id AND DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date GROUP BY arv_id)
) tblQuit GROUP BY quitArv, gender_id

