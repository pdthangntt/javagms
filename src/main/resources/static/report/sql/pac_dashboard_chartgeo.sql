select 
    info.id, 
    loc.permanent_lat, 
    loc.permanent_lng,
    DATE_FORMAT(info.death_time, '%d/%m/%y') death_time,
    info.district_id,
    info.ward_id
from pac_patient_info as info
INNER JOIN pac_location as loc on loc.id = info.id
WHERE info.is_remove = 0
    AND info.permanent_province_id = @province
    AND info.province_id = @province
    
    AND (info.permanent_district_id = @district OR @district is null)
    AND (info.permanent_ward_id = @ward OR @ward is null)