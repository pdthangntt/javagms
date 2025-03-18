SELECT main_rev.*,
       main_vs.examination_time,
       arv.date_of_arrival
FROM
  (SELECT rev.arv_id,
          rev.stage_id,
          rev.status_of_treatment_id,
          MONTH(rev.treatment_time) treattime,
          rev.treatment_time,
          rev.end_case,
          rev.end_time,
          rev.registration_type,
          rev.tranfer_from_time,
          rev.treatment_regiment_id
   FROM
     (SELECT *
      FROM opc_arv_revision
      WHERE is_remove = 0
        AND DATE_FORMAT(treatment_time, '%Y-%m-%d') >= @fromDate AND DATE_FORMAT(treatment_time, '%Y-%m-%d') <= @toDate
        AND (treatment_time IS NOT NULL
             OR end_time IS NOT NULL)
        AND site_id = @siteID
      ORDER BY treatment_time DESC) AS rev
   GROUP BY rev.arv_id,
            rev.stage_id,
            rev.treatment_time,
            rev.status_of_treatment_id) AS main_rev
INNER JOIN
  (SELECT *
   FROM opc_arv
   WHERE is_remove = 0 and site_id = @siteID)AS arv ON arv.id = main_rev.arv_id
LEFT JOIN
  (SELECT *
   FROM
     (SELECT *
      FROM opc_visit
      WHERE is_remove = 0
        AND site_id = @siteID
      ORDER BY examination_time DESC) AS vs
   GROUP BY vs.arv_id,
            vs.stage_id) AS main_vs ON main_rev.arv_id = main_vs.arv_id
AND main_rev.stage_id = main_vs.stage_id