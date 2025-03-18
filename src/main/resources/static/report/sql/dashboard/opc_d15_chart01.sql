SELECT COALESCE(COUNT(main.arv_id), 0)
FROM
  (SELECT main_tlvr.arv_id
   FROM
     (SELECT m_tlvr.arv_id,
             m_tlvr.stage_id,
             m_tlvr.test_time AS max_time
      FROM
        (SELECT *
         FROM opc_viral_load
         WHERE is_remove = 0
           AND YEAR(DATE_FORMAT(test_time, '%Y-%m-%d')) = YEAR(@lastDate)
           AND site_id IN (@site_id)
         ORDER BY test_time DESC, id DESC) AS m_tlvr
      GROUP BY m_tlvr.arv_id) AS main_tlvr
   JOIN
     (SELECT m_revision.*
      FROM
        (SELECT *
         FROM
           (SELECT r.*
            FROM opc_arv_revision r
            WHERE DATE_FORMAT(r.created_at, '%Y-%m-%d') <= DATE_FORMAT(@lastDate, '%Y-%m-%d')
              AND site_id IN (@site_id)
            ORDER BY r.created_at DESC, r.id DESC) AS main_revision
         GROUP BY main_revision.arv_id, main_revision.stage_id) m_revision
      WHERE m_revision.status_of_treatment_id = '3'
        AND m_revision.is_remove = 0 ) AS revision ON revision.arv_id = main_tlvr.arv_id AND revision.stage_id = main_tlvr.stage_id
   AND DATE_FORMAT(revision.treatment_time, '%Y-%m-%d') <= DATE_FORMAT(main_tlvr.max_time, '%Y-%m-%d')
   JOIN opc_stage AS stage ON stage.id = main_tlvr.stage_id
   AND stage.is_remove = 0) AS main