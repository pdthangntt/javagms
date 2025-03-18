SELECT 
    m.site_id,
    COALESCE(COUNT(m.arv_id), 0) 
FROM 
(SELECT * from 
    (  SELECT stage.* from opc_stage stage
        JOIN opc_arv as arv 
        ON arv.id = stage.arv_id 
        AND arv.is_remove = 0
        AND stage.is_remove = 0
        AND stage.site_id IN (@site_id)
        AND DATE_FORMAT(stage.registration_time, '%Y-%m-%d') <= @lastDate
    ) as main GROUP BY main.site_id, main.arv_id
    ) as m GROUP BY m.site_id