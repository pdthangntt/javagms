SELECT 
    main.site_id,
    coalesce(SUM(main.arvTreatment), 0) as arvTreatment,
    coalesce(SUM(main.arvQuickTreatment), 0) as arvQuickTreatment,
    coalesce(SUM(main.arvTreatmentDuringDay), 0) as arvTreatmentDuringDay,
    coalesce(SUM(main.receiveMedicineMax90), 0) as receiveMedicineMax90,
    coalesce(SUM(main.receiveMedicineThreeMonth), 0) as receiveMedicineThreeMonth
FROM
(SELECT 
       arvTreatment.site_id, 
       COUNT(arvTreatment.id) AS arvTreatment,
       0 AS arvQuickTreatment,
       0 AS arvTreatmentDuringDay,
       0 AS receiveMedicineMax90,
       0 AS receiveMedicineThreeMonth
FROM
  (SELECT r.arv_id id, r.site_id
   FROM opc_arv_revision AS r
   JOIN
     ( SELECT * FROM (SELECT id,
             stage_id,
             arv_id
      FROM opc_arv_revision
      WHERE is_remove=0
        AND DATE_FORMAT(created_at, '%Y-%m-%d') <= @to_date
        ORDER BY created_at DESC, id DESC) AS r1
      GROUP BY r1.arv_id) AS r2 
      ON r.id = r2.id
      AND status_of_treatment_id = '3'
        AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') <= @to_date
        AND (r.end_time IS NULL OR r.end_time = '')
        AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
      AND r.site_id IN (@site_id)
   AND r.arv_id = r2.arv_id) AS arvTreatment GROUP BY arvTreatment.site_id

UNION ALL 

SELECT 
       arvQuickTreatment.site_id,
       0 AS arvTreatment,
       COUNT(arvQuickTreatment.id) AS arvQuickTreatment,
       0 AS arvTreatmentDuringDay,
       0 AS receiveMedicineMax90,
       0 AS receiveMedicineThreeMonth
FROM
(SELECT r.arv_id id, r.site_id
   FROM opc_arv_revision AS r
   JOIN
     (SELECT * FROM (SELECT id,
             stage_id,
             arv_id
      FROM opc_arv_revision
      WHERE is_remove=0
        AND DATE_FORMAT(created_at, '%Y-%m-%d') <= @to_date ORDER BY created_at DESC, id DESC) AS r1
      GROUP BY r1.arv_id) AS r2 
    ON r.id = r2.id
    AND r.status_of_treatment_id = '3'
    AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
    AND (r.end_time IS NULL OR r.end_time = '')
    AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
    AND r.site_id IN (@site_id)
    JOIN opc_arv o 
        ON o.id = r.arv_id
        AND o.pass_treatment = 1
        AND (o.quick_by_treatment_time <> '0' AND o.quick_by_treatment_time <> '5')
   AND r.arv_id = r2.arv_id ) AS arvQuickTreatment GROUP BY arvQuickTreatment.site_id

UNION ALL 

SELECT 
       arvTreatmentDuringDay.site_id,
       0 AS arvTreatment,
       0 AS arvQuickTreatment,
       COUNT(arvTreatmentDuringDay.id) AS arvTreatmentDuringDay,
       0 AS receiveMedicineMax90,
       0 AS receiveMedicineThreeMonth
FROM
(SELECT r.arv_id id, r.site_id
   FROM opc_arv_revision AS r
   JOIN
     (SELECT * FROM (SELECT id,
            stage_id,
             arv_id
      FROM opc_arv_revision
      WHERE is_remove=0
        AND DATE_FORMAT(created_at, '%Y-%m-%d') <= @to_date ORDER BY created_at DESC, id DESC) AS r1
      GROUP BY r1.arv_id) AS r2 
    ON r.id = r2.id
AND r.status_of_treatment_id = '3'
        AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
        AND (r.end_time IS NULL OR r.end_time = '')
        AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
    AND r.site_id IN (@site_id)
    JOIN opc_arv o 
        ON o.id = r.arv_id
        AND o.pass_treatment = '1'
        AND o.quick_by_treatment_time = '0'
   AND r.arv_id = r2.arv_id) AS arvTreatmentDuringDay GROUP BY arvTreatmentDuringDay.site_id

UNION ALL

SELECT 
       receiveMedicineMax90.site_id,
       0 AS arvTreatment,
       0 AS arvQuickTreatment,
       0 AS arvTreatmentDuringDay,
       COUNT(receiveMedicineMax90.id) AS receiveMedicineMax90,
       0 AS receiveMedicineThreeMonth
FROM
  (SELECT r.arv_id id, r.site_id
   FROM opc_arv_revision AS r
   JOIN
     (SELECT * FROM (SELECT id,
             stage_id,
             arv_id
      FROM opc_arv_revision
      WHERE is_remove=0
        AND DATE_FORMAT(created_at, '%Y-%m-%d') <= @to_date ORDER BY created_at DESC, id DESC) AS r1
      GROUP BY r1.arv_id) AS r2 
    ON r.id = r2.id
    AND status_of_treatment_id = '3'
        AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') <= @to_date
        AND (r.end_time IS NULL OR r.end_time = '')
        AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
        AND r.days_received = 90
    AND r.arv_id = r2.arv_id
    AND r.site_id IN (@site_id)
    JOIN opc_patient p 
        ON p.id = r.patient_id
        AND YEAR(r.treatment_time) - YEAR(p.dob) >= 15
    JOIN (
        SELECT v.id, v.arv_id, v.stage_id  FROM opc_visit v 
        JOIN (
                SELECT * FROM (SELECT id,
                    stage_id,
                    arv_id
                FROM opc_visit
                WHERE is_remove = 0
                  AND DATE_FORMAT(examination_time, '%Y-%m-%d') >= @from_date
                  AND DATE_FORMAT(examination_time, '%Y-%m-%d') <= @to_date
                ORDER BY examination_time DESC, id DESC) as main_visit GROUP BY main_visit.arv_id , main_visit.stage_id
                
           ) AS vi ON vi.id = v.id 
        )  as main_v
    ON main_v.arv_id = r.arv_id AND main_v.stage_id = r.stage_id
) AS receiveMedicineMax90 GROUP BY receiveMedicineMax90.site_id

UNION ALL

SELECT 
       receiveMedicineThreeMonth.site_id,
       0 AS arvTreatment,
       0 AS arvQuickTreatment,
       0 AS arvTreatmentDuringDay,
       0 AS receiveMedicineMax90,
       COUNT(receiveMedicineThreeMonth.id) AS receiveMedicineThreeMonth
FROM
  (SELECT r.arv_id id, r.site_id
   FROM opc_arv_revision AS r
   JOIN
     (SELECT * FROM (SELECT id,
             stage_id,
             arv_id
      FROM opc_arv_revision
      WHERE is_remove=0
        AND DATE_FORMAT(created_at, '%Y-%m-%d') <= @to_date ORDER BY created_at DESC, id DESC ) AS r1
      GROUP BY r1.arv_id) AS r2 
    ON r.id = r2.id
    AND status_of_treatment_id = '3'
        AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') <= @to_date
        AND (r.end_time IS NULL OR r.end_time = '')
        AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
    AND r.days_received = 90
    AND r.arv_id = r2.arv_id
    AND r.site_id IN (@site_id)
    JOIN opc_patient p 
        ON p.id = r.patient_id
        AND YEAR(r.treatment_time) - YEAR(p.dob) >= 15
    JOIN (
        SELECT v.id, v.arv_id, v.stage_id  FROM opc_visit v 
        JOIN (
            SELECT * FROM (SELECT id,
                    stage_id,
                    arv_id
                FROM opc_visit
                WHERE is_remove = 0
                  AND DATE_FORMAT(examination_time, '%Y-%m-%d') >= @from_date
                  AND DATE_FORMAT(examination_time, '%Y-%m-%d') <= @to_date
                ORDER BY examination_time DESC, id DESC) as main_visit GROUP BY main_visit.arv_id , main_visit.stage_id
           ) AS vi ON vi.id = v.id AND (v.insurance_no IS NOT NULL && v.insurance_no <> '') AND DATE_FORMAT(v.insurance_expiry, '%Y-%m-%d') > @to_date
        )  as main_v
    ON main_v.arv_id = r.arv_id AND main_v.stage_id = r.stage_id
) AS receiveMedicineThreeMonth GROUP BY receiveMedicineThreeMonth.site_id
) AS main GROUP BY main.site_id