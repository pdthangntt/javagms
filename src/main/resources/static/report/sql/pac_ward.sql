-- Tổng
SELECT 
	e.district_id as district,
    e.ward_id as ward,
    COUNT(e.id) as hiv,
    0 AS aids,
    0 tv,
    0 under15,
    0 over15
 FROM pac_patient_info e
 WHERE
    e.is_remove = false
    AND (
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
        AND ( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @endTime)
        AND ( DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @endTime)
	-- thêm điều kiện ngày XNKĐ < Ngày báo cáo
-- End
    AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e.province_id = @provinceID)
    AND (e.district_id = @districtID OR @districtID is null)
    AND (e.ward_id = @wardID OR @wardID is null)
    GROUP BY   e.district_id,e.ward_id
    
UNION ALL
 -- aids
SELECT 
	e1.district_id as district,
    e1.ward_id as ward,
    0 as hiv,
    COALESCE(count(e1.id),0) AS aids,
    0 tv,
    0 under15,
    0 over15
 FROM pac_patient_info e1
 WHERE
 -- Điều kiện
	e1.AIDS_STATUS_DATE is not null
	AND e1.confirm_time is not null
    -- Thêm điều kiện ngày chuẩn đoán AIDS < Ngày báo cáo
        AND ( DATE_FORMAT(e1.confirm_time , '%Y-%m-%d') <= @endTime)
        AND ( DATE_FORMAT(e1.AIDS_STATUS_DATE , '%Y-%m-%d') <= @endTime)
 -- End
    and e1.is_remove = false
AND (
        (
            @manageStatus = '-1' 
            AND (
                (@isPac AND e1.accept_time IS NULL AND e1.review_ward_time IS NULL AND e1.review_province_time IS NULL AND e1.source_service_id <> '103' AND e1.permanent_province_id = @provinceID AND e1.province_id = @provinceID)
                OR (e1.accept_time IS NOT NULL AND e1.review_ward_time IS NULL AND e1.review_province_time IS NULL)
                OR (e1.accept_time IS NOT NULL AND e1.review_ward_time IS NULL AND e1.review_province_time IS NULL)
                OR (e1.accept_time IS NOT NULL AND e1.review_ward_time IS NOT NULL AND e1.review_province_time IS NULL)
                OR (e1.accept_time IS NOT NULL AND e1.review_ward_time IS NOT NULL AND e1.review_province_time IS NOT NULL)
            )
        )
        OR (@isPac AND @manageStatus = '1' AND e1.accept_time IS NULL AND e1.review_ward_time IS NULL AND e1.review_province_time IS NULL AND e1.source_service_id <> '103' AND e1.permanent_province_id = @provinceID AND e1.province_id = @provinceID)
        OR (@manageStatus = '2' AND e1.accept_time IS NOT NULL AND e1.review_ward_time IS NULL AND e1.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e1.accept_time IS NOT NULL AND e1.review_ward_time IS NOT NULL AND e1.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e1.accept_time IS NOT NULL AND e1.review_ward_time IS NOT NULL AND e1.review_province_time IS NOT NULL)     
    )
    AND (e1.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e1.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e1.province_id = @provinceID)
    AND (e1.district_id = @districtID OR @districtID is null)
    AND (e1.ward_id = @wardID OR @wardID is null)
    GROUP BY   e1.district_id,e1.ward_id
    
UNION ALL
-- tử vong
SELECT 
	e2.district_id as district,
    e2.ward_id as ward,
    0 as hiv,
    0 AS aids,
    COALESCE(count(e2.id),0) tv,
    0 under15,
    0 over15
 FROM pac_patient_info e2
 WHERE
 -- Điều kiện
	e2.death_time is not null
	AND e2.confirm_time is not null
        AND ( DATE_FORMAT(e2.confirm_time , '%Y-%m-%d') <= @endTime)
        AND (( DATE_FORMAT(e2.death_time , '%Y-%m-%d') <= @endTime) OR ( e2.REQUEST_DEATH_TIME is not null AND DATE_FORMAT(e2.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @endTime) )
    -- Thêm điều kiện ngày Tử vong < Ngày báo cáo
 -- End
    and e2.is_remove = false
AND (
        (
            @manageStatus = '-1' 
            AND (
                (@isPac AND e2.accept_time IS NULL AND e2.review_ward_time IS NULL AND e2.review_province_time IS NULL AND e2.source_service_id <> '103' AND e2.permanent_province_id = @provinceID AND e2.province_id = @provinceID)
                OR (e2.accept_time IS NOT NULL AND e2.review_ward_time IS NULL AND e2.review_province_time IS NULL)
                OR (e2.accept_time IS NOT NULL AND e2.review_ward_time IS NULL AND e2.review_province_time IS NULL)
                OR (e2.accept_time IS NOT NULL AND e2.review_ward_time IS NOT NULL AND e2.review_province_time IS NULL)
                OR (e2.accept_time IS NOT NULL AND e2.review_ward_time IS NOT NULL AND e2.review_province_time IS NOT NULL)
            )
        )
        OR (@isPac AND @manageStatus = '1' AND e2.accept_time IS NULL AND e2.review_ward_time IS NULL AND e2.review_province_time IS NULL AND e2.source_service_id <> '103' AND e2.permanent_province_id = @provinceID AND e2.province_id = @provinceID)
        OR (@manageStatus = '2' AND e2.accept_time IS NOT NULL AND e2.review_ward_time IS NULL AND e2.review_province_time IS NULL)
        OR (@manageStatus = '3' AND e2.accept_time IS NOT NULL AND e2.review_ward_time IS NOT NULL AND e2.review_province_time IS NULL)
        OR (@manageStatus = '4' AND e2.accept_time IS NOT NULL AND e2.review_ward_time IS NOT NULL AND e2.review_province_time IS NOT NULL)     
    )
    AND ( ( @statusDeath = '1' AND DATE_FORMAT(e2.death_time , '%Y-%m-%d') <= @endTime ) OR ( @statusDeath = '2' AND e2.REQUEST_DEATH_TIME is not null AND DATE_FORMAT(e2.REQUEST_DEATH_TIME , '%Y-%m-%d') <= @endTime)  OR  @statusDeath = '' OR @statusDeath = null)
    AND (e2.gender_id = @gender OR @gender IS NULL OR @gender = '' )
    AND (e2.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
    AND (e2.province_id = @provinceID)
    AND (e2.district_id = @districtID OR @districtID is null)
    AND (e2.ward_id = @wardID OR @wardID is null)
    GROUP BY  e2.district_id, e2.ward_id

UNION ALL
SELECT 
    main.district,
    main.ward,
    0 as hiv,
    0 AS aids,
    0 tv,
    coalesce(SUM(main.under15), 0) as under15,       
    coalesce(SUM(main.over15), 0) as over15
FROM (
	SELECT 	m.district,
			m.ward, 
			(case When m.age < 15 then count(m.id) else 0 end) AS under15,
			(case When m.age >= 15 then count(m.id) else 0 end) AS over15
	FROM
	(
	SELECT 
		e.district_id as district,
		e.ward_id as ward,
		e.id,
		YEAR(e.confirm_time) - year_of_birth as age
	 FROM pac_patient_info e
	 WHERE
            e.is_remove = false
            AND (
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
                AND ( DATE_FORMAT(e.confirm_time , '%Y-%m-%d') <= @endTime)
                AND ( DATE_FORMAT(e.created_at , '%Y-%m-%d') <= @endTime)
                -- thêm điều kiện ngày XNKĐ < Ngày báo cáo
        -- End
            AND (e.gender_id = @gender OR @gender IS NULL OR @gender = '' )
            AND (e.object_group_id = @testObject OR @testObject IS NULL OR @testObject = '' )
            AND (e.province_id = @provinceID)
            AND (e.district_id = @districtID OR @districtID is null)
            AND (e.ward_id = @wardID OR @wardID is null)
            AND e.death_time is null AND e.status_of_resident_id IN ('2','4','7','8')

	) as m group by m.district, m.ward, m.age
) as main group by main.district, main.ward

