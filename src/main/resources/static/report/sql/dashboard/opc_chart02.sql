SELECT SUM(main.manage) AS MANAGE,
       SUM(main.manageBhyt) AS manageBhyt,
       SUM(main.treatment) AS treatment,
       SUM(main.treatmentBhyt) AS treatmentBhyt
FROM
  (-- So dang quan ly
SELECT @quarter AS q,
       COUNT(e.arv_id) AS MANAGE,
       0 AS manageBhyt,
       0 AS treatment,
       0 AS treatmentBhyt
   FROM opc_stage AS e JOIN
     (SELECT m.*
      FROM
        (SELECT s.id,s.arv_id
         FROM opc_stage AS s
         WHERE s.is_remove = 0
           AND (s.end_time IS NULL
                OR DATE_FORMAT(s.end_time, '%Y-%m-%d') > @to_date)
           AND (s.tranfer_from_time IS NULL
                OR DATE_FORMAT(s.tranfer_from_time, '%Y-%m-%d') > @to_date)
           AND (s.site_id IN (@site_id)
                OR coalesce(@site_id, NULL) IS NULL)
           AND DATE_FORMAT(s.registration_time, '%Y-%m-%d') <= @to_date
         ORDER BY s.registration_time DESC, s.id DESC) AS m
      GROUP BY m.arv_id) AS stage ON stage.id = e.id
   JOIN opc_patient AS p ON e.patient_id = p.id
   JOIN opc_arv AS arv ON arv.id = e.arv_id
   AND arv.is_remove = 0
   WHERE e.is_remove = 0
     AND p.gender_id IN (@gender)
     AND DATE_FORMAT(e.registration_time, '%Y-%m-%d') <= @to_date
     AND (coalesce(@age, NULL) IS NULL
          OR ('2' IN (@age)
              AND '1' IN (@age))
          OR (('2' IN (@age)
               AND YEAR(CURDATE()) - YEAR(p.dob) >= 15)
              OR ('1' IN (@age)
                  AND YEAR(CURDATE()) - YEAR(p.dob) < 15)))
   GROUP BY q
   UNION ALL -- so dang quan ly co bao hiem
SELECT @quarter AS q,
       0 AS MANAGE,
       COUNT(e.arv_id) AS manageBhyt,
       0 AS treatment,
       0 AS treatmentBhyt
   FROM opc_stage AS e
   JOIN
     (SELECT m.*
      FROM
        (SELECT e.id,e.arv_id, e.stage_id, e.insurance_no
         FROM opc_visit e
         WHERE e.is_remove = 0
           AND e.examination_time IS NOT NULL
           AND DATE_FORMAT(e.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
         ORDER BY e.examination_time DESC, e.id DESC) AS m
      GROUP BY m.arv_id) AS v ON v.arv_id = e.arv_id
   AND v.stage_id = e.id JOIN
     (SELECT m.*
      FROM
        (SELECT s.id,s.arv_id
         FROM opc_stage AS s
         WHERE s.is_remove = 0
           AND (s.end_time IS NULL
                OR DATE_FORMAT(s.end_time, '%Y-%m-%d') > @to_date)
           AND (s.tranfer_from_time IS NULL
                OR DATE_FORMAT(s.tranfer_from_time, '%Y-%m-%d') > @to_date)
           AND (s.site_id IN (@site_id)
                OR coalesce(@site_id, NULL) IS NULL)
           AND DATE_FORMAT(s.registration_time, '%Y-%m-%d') <= @to_date
         ORDER BY s.registration_time DESC, s.id DESC) AS m
      GROUP BY m.arv_id) AS stage ON stage.id = e.id
   JOIN opc_patient AS p ON e.patient_id = p.id
   JOIN opc_arv AS arv ON arv.id = e.arv_id AND arv.is_remove = 0
   WHERE p.gender_id IN (@gender)
     AND (coalesce(@age, NULL) IS NULL
          OR ('2' IN (@age)
              AND '1' IN (@age))
          OR (('2' IN (@age)
               AND YEAR(CURDATE()) - YEAR(p.dob) >= 15)
              OR ('1' IN (@age)
                  AND YEAR(CURDATE()) - YEAR(p.dob) < 15)))
     AND (v.insurance_no IS NOT NULL
          AND v.insurance_no <> '')
     AND e.is_remove = 0
   GROUP BY q
   UNION ALL -- so dang dieu tri
SELECT @quarter AS q,
       0 AS MANAGE,
       0 AS manageBhyt,
       COUNT(e.id) AS treatment,
       0 AS treatmentBhyt
   FROM opc_arv_revision AS e
   JOIN opc_patient AS p ON e.patient_id = p.id
   JOIN
     (SELECT *
      FROM
        (SELECT re.arv_id, re.id,
                re.treatment_time,
                re.tranfer_from_time,
                re.end_time,
                re.status_of_treatment_id
         FROM opc_arv_revision re
         WHERE re.is_remove = 0
           AND (re.site_id IN (@site_id)
                OR coalesce(@site_id, NULL) IS NULL)
           AND DATE_FORMAT(re.created_at, '%Y-%m-%d') <= @to_date
           -- AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= @to_date
         ORDER BY re.created_at DESC, re.id DESC) AS m
      GROUP BY m.arv_id) AS revision ON revision.id = e.id
   AND DATE_FORMAT(revision.treatment_time, '%Y-%m-%d') <= @to_date
   AND (revision.tranfer_from_time IS NULL
        OR revision.tranfer_from_time = '')
   AND (revision.end_time IS NULL
        OR revision.end_time = '')
   AND revision.status_of_treatment_id = '3'
   JOIN opc_stage AS stage ON stage.id = e.stage_id
   AND stage.is_remove = 0
   JOIN opc_arv AS arv ON arv.id = e.arv_id
   AND arv.is_remove = 0
   WHERE p.gender_id IN (@gender)
     AND (coalesce(@age, NULL) IS NULL
          OR ('2' IN (@age)
              AND '1' IN (@age))
          OR (('2' IN (@age)
               AND YEAR(CURDATE()) - YEAR(p.dob) >= 15)
              OR ('1' IN (@age)
                  AND YEAR(CURDATE()) - YEAR(p.dob) < 15)))
     AND e.is_remove = 0
   GROUP BY q
   UNION ALL -- So dang dieu tri co bh
SELECT @quarter AS q,
       0 AS MANAGE,
       0 AS manageBhyt,
       0 AS treatment,
       COUNT(e.id) AS treatmentBhyt
   FROM opc_arv_revision AS e
   JOIN
     (SELECT m.*
      FROM
        (SELECT e.id,
                e.arv_id,
                e.stage_id,
                e.insurance_no
         FROM opc_visit e
         WHERE e.is_remove = 0
           AND e.examination_time IS NOT NULL
           AND DATE_FORMAT(e.examination_time, '%Y-%m-%d') BETWEEN @from_date AND @to_date
         ORDER BY e.examination_time DESC, e.id DESC) AS m
      GROUP BY m.arv_id) AS v ON v.arv_id = e.arv_id
   AND v.stage_id = e.stage_id
   JOIN
     (SELECT *
      FROM
        (SELECT re.arv_id, re.id,
                re.treatment_time,
                re.tranfer_from_time,
                re.end_time,
                re.status_of_treatment_id
         FROM opc_arv_revision re
         WHERE re.is_remove = 0
           AND (re.site_id IN (@site_id)
                OR coalesce(@site_id, NULL) IS NULL)
              AND DATE_FORMAT(re.created_at, '%Y-%m-%d') <= @to_date
           -- AND DATE_FORMAT(re.treatment_time, '%Y-%m-%d') <= @to_date
         ORDER BY re.created_at DESC, re.id DESC) AS m
      GROUP BY m.arv_id) AS revision ON revision.id = e.id
   AND DATE_FORMAT(revision.treatment_time, '%Y-%m-%d') <= @to_date
   AND (revision.tranfer_from_time IS NULL
        OR revision.tranfer_from_time = '')
   AND (revision.end_time IS NULL
        OR revision.end_time = '')
   AND revision.status_of_treatment_id = '3'
   JOIN opc_patient AS p ON e.patient_id = p.id
   JOIN opc_stage AS stage ON stage.id = e.stage_id
   AND stage.is_remove = 0
   JOIN opc_arv AS arv ON arv.id = e.arv_id
   AND arv.is_remove = 0
   WHERE p.gender_id IN (@gender)
     AND (coalesce(@age, NULL) IS NULL
          OR ('2' IN (@age)
              AND '1' IN (@age))
          OR (('2' IN (@age)
               AND YEAR(CURDATE()) - YEAR(p.dob) >= 15)
              OR ('1' IN (@age)
                  AND YEAR(CURDATE()) - YEAR(p.dob) < 15)))
     AND (v.insurance_no IS NOT NULL
          AND v.insurance_no <> '')
     AND e.is_remove = 0
   GROUP BY q) main