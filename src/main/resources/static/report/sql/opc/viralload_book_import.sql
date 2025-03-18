SELECT * FROM (SELECT *
FROM
  (SELECT *
   FROM
     (SELECT o.id,
             m_load.id vr_id,
             m_load.test_time,
             m_stage.id stage_id,
             o.code
      FROM
        (SELECT *
         FROM
           (SELECT *
            FROM opc_stage
            WHERE is_remove = 0
              AND site_id = @site_id
            ORDER BY registration_time DESC, id DESC) stage
         GROUP BY stage.arv_id) m_stage
      JOIN opc_arv o ON m_stage.arv_id = o.id
      AND o.is_remove = 0
      AND o.code = @code
      LEFT JOIN
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0) m_load ON m_load.arv_id = m_stage.arv_id
      AND m_load.stage_id = m_stage.id) main
   ORDER BY main.test_time DESC, main.id DESC) AS fn
GROUP BY fn.test_time) AS final_main ORDER BY final_main.test_time DESC