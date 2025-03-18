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
        AND (stage.registration_type = '1' OR stage.registration_type = '4' OR stage.registration_type = '5')
        AND stage.site_id IN (@site_id)
        AND YEAR(stage.registration_time ) = YEAR(@lastDate)
    ) as main GROUP BY main.site_id, main.arv_id
    ) as m GROUP BY m.site_id