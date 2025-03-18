SELECT 
    DATE_FORMAT(MAX(r.registration_time), '%Y-%m-%d') as regis
FROM 
    opc_arv_revision r
WHERE
    r.is_remove = 0

UNION ALL

SELECT 
    DATE_FORMAT(MIN(r.registration_time), '%Y-%m-%d') as regis
FROM 
    opc_arv_revision r
WHERE
    r.is_remove = 0