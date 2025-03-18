SELECT 
    p.province_id as provinceID,
    p.district_id as districtID,
    p.ward_id as wardID,
    COALESCE(count(p.id), 0) as aliveNum,
    0 as deadNum
FROM pac_patient_info p
WHERE 
    ((@provinceID IS null AND p.district_id = @districtID ) OR p.province_id = @provinceID)
      AND ((@districtID = null AND p.ward_id = @wardID ) OR p.district_id = @districtID OR @districtID = null)
      AND p.confirm_time < @toTime AND (p.death_time IS NULL OR (p.death_time IS NOT NULL AND p.death_time > @toTime)
      group by p.ward_id, p.district_id, p.province_id;