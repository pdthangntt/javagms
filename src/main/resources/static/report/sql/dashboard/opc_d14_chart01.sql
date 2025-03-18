SELECT COALESCE(COUNT(main.arv_id), 0)
FROM
  (SELECT m.arv_id
   FROM (
           (SELECT stage.arv_id
            FROM opc_stage stage
            JOIN opc_arv arv ON arv.id = stage.arv_id
            AND arv.is_remove = 0
            AND stage.is_remove = 0
            AND YEAR(stage.registration_time ) = YEAR(@lastDate)
            AND (stage.registration_type = '1' OR stage.registration_type = '4' OR stage.registration_type = '5')
            AND stage.site_id IN (@site_id))) AS m
   GROUP BY m.arv_id) AS main