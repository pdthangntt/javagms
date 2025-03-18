SELECT COALESCE(COUNT(revision.arv_id), 0)
FROM
  (SELECT r.*
   FROM opc_arv_revision r
   JOIN
     (SELECT
             *
      FROM
        (SELECT re.*
         FROM opc_arv_revision re
         WHERE (re.site_id IN (@site_id))
           AND re.is_remove = 0
           AND DATE_FORMAT(re.created_at, '%Y-%m-%d') <= @lastDate ORDER BY re.created_at DESC, id DESC) AS m
      GROUP BY m.arv_id) AS max_time ON 
    r.id = max_time.id
    AND (r.treatment_time IS NOT NULL AND r.treatment_time <> '' AND DATE_FORMAT(r.treatment_time, '%Y-%m-%d') <= @lastDate)
    AND (r.tranfer_from_time IS NULL OR r.tranfer_from_time = '')
    AND (r.end_time IS NULL  OR r.end_time = '')
    AND r.status_of_treatment_id = '3'
) AS revision
JOIN opc_stage AS stage ON stage.id = revision.stage_id
    AND stage.arv_id = revision.arv_id
    AND stage.is_remove = 0