SELECT 
    q.ID AS src,
    p.ID AS cur
FROM 
    pac_patient_info p 
JOIN
    pac_patient_info q
ON 
    p.ID =  q.parent_id AND q.parent_id <> 0
    AND q.is_remove IS FALSE
WHERE
    -- Điều kiện là đối tượng được quản lý
    p.province_id IS NOT NULL 
    AND p.province_id <> '0'
--     AND p.accept_time IS NOT NULL
--     AND p.review_ward_time IS NOT NULL
--     AND p.review_province_time IS NOT NULL
    AND @pa = 1