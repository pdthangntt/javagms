SELECT 
    e.year,
    sum(e.permanent_htc) as nhiem_moi,
    sum(e.permanent_dead) as tu_vong
FROM ql_report as e 
WHERE (e.province_id = @province_id OR @province_id is null)
and (e.district_id = @district_id OR @district_id is null)
and (e.ward_id = @ward_id OR @ward_id is null)
GROUP BY e.year order by e.year asc