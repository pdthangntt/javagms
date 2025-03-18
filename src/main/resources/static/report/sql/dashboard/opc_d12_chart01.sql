SELECT COALESCE(COUNT(main.arv_id), 0)
FROM
  (SELECT m.arv_id
   FROM (
           (SELECT stage.arv_id
            FROM opc_stage stage
            JOIN opc_arv arv ON arv.id = stage.arv_id
            AND arv.is_remove = 0
            AND stage.is_remove = 0
            AND (stage.tranfer_from_time IS NULL
                 OR DATE_FORMAT(stage.tranfer_from_time, '%Y-%m-%d') > @lastDate)
            AND (stage.end_time IS NULL
                 OR DATE_FORMAT(stage.end_time, '%Y-%m-%d') > @lastDate)
            AND DATE_FORMAT(stage.registration_time, '%Y-%m-%d') <= @lastDate
            AND stage.site_id IN (@site_id))) AS m
   GROUP BY m.arv_id) AS main