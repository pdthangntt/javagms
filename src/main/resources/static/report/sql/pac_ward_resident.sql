SELECT 
    e.district_id as district,
    e.ward_id as ward,
    e.status_of_resident_id as resident,
    count(e.id)
 FROM pac_patient_info e
 WHERE
    e.is_remove = false    AND (
        (
            @manageStatus = '-1' 
            AND (
                (@isPac AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND e.permanent_province_id = @provinceID AND e.province_id = @provinceID)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
                OR (e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)
            )
        )
        OR (@isPac AND @manageStatus = '1' AND e.accept_time IS NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL AND e.source_service_id <> '103' AND e.permanent_province_id = @provinceID AND e.province_id = @provinceID)
        OR (@manageStatus = '2' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e.accept_time IS NOT NULL AND e.review_ward_time IS NOT NULL AND e.review_province_time IS NOT NULL)     
    )

-- Điều kiện
	AND e.confirm_time is not null 
	AND e.death_time is null 
        AND ( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @endTime)
        AND ( DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @endTime)
	-- thêm điều kiện ngày XNKĐ < Ngày báo cáo
-- End
AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
   AND (e.province_id = @provinceID)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)
    AND @statusDeath = @statusDeath
    GROUP BY  e.district_id,e.ward_id,  e.status_of_resident_id
