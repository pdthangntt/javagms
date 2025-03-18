SELECT 
    rs_info.province,
    rs_info.district,
    rs_info.ward,
    SUM(rs_info.numAlive) alive,
    SUM(rs_info.numDead) dead,
    SUM(rs_info.notReviewYet) notReviewYet,
    SUM(rs_info.needReview) needReview,
    SUM(rs_info.reviewed) reviewed,
    SUM(rs_info.outprovince) outprovince
FROM (SELECT 
    m_info.numAlive numAlive,
    m_info.numDead numDead,
    m_info.notReviewYet notReviewYet,
    m_info.needReview needReview,
    m_info.reviewed reviewed,
    m_info.outprovince outprovince,
    m_info.province AS province,
    CASE 
        WHEN (@levelDisplay = 'province') THEN 0
        ELSE m_info.district
    END AS district,
    CASE 
        WHEN (@levelDisplay = 'province' OR @levelDisplay = 'district') THEN 0
        ELSE m_info.ward
    END AS ward
FROM (
-- Còn sống
SELECT 
    info.province_id province,
    info.district_id district,
    info.ward_id ward,
    COALESCE(count(info.id),0) AS numAlive,
    0 AS numDead,
    0 AS notReviewYet,
    0 AS needReview,
    0 AS reviewed,
    0 AS outprovince
 FROM pac_patient_info info
 WHERE
    info.is_remove = false

    AND (info.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info.district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info.ward_id = @wardID OR @wardID IS NULL OR @wardID = '')
    
    AND (info.province_id IN (@searchProvinceID) OR coalesce(@searchProvinceID, null) IS NULL)
    AND (info.district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info.ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)

    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND ((info.death_time IS NULL OR info.death_time = '' OR (info.death_time IS NOT NULL AND info.death_time > @toTime)) 
          AND ((@fromTime IS NULL OR @fromTime = '' OR info.confirm_time >= @fromTime) AND (@toTime IS NULL OR @toTime = '' OR info.confirm_time <= @toTime))
        )
    GROUP BY info.province_id, info.district_id, info.ward_id
    
 UNION ALL
-- Tử vong 
 SELECT 
    info1.province_id province,
    info1.district_id district,
    info1.ward_id ward,
    0 AS numAlive,
    COALESCE(count(info1.id),0) AS numDead,
    0 AS notReviewYet,
    0 AS needReview,
    0 AS reviewed,
    0 AS outprovince
 FROM pac_patient_info info1
 WHERE
    info1.is_remove = false
    AND info1.accept_time is not null
    AND info1.review_ward_time is not null
    AND info1.review_province_time is not null

    AND (info1.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info1.district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info1.ward_id = @wardID OR @wardID IS NULL OR @wardID = '')

    AND (info1.province_id IN (@searchProvinceID) OR coalesce(@searchProvinceID, null) IS NULL)
    AND (info1.district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info1.ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)
   
    AND (info1.death_time IS NOT NULL )
    AND (@fromTime IS NULL OR @fromTime = '' OR info1.death_time >= @fromTime)
    AND (@toTime IS NULL OR @toTime = '' OR info1.death_time <= @toTime)
    GROUP BY info1.province_id, info1.district_id, info1.ward_id

UNION ALL
 -- Chưa rà soát
 SELECT 
    info2.province_id province,
    info2.permanent_district_id district,
    info2.permanent_ward_id ward,
    0 AS numAlive,
    0 AS numDead,
    COALESCE(count(info2.id),0) AS notReviewYet,
    0 AS needReview,
    0 AS reviewed,
    0 AS outprovince
 FROM pac_patient_info info2
 WHERE
    @isVAAC is false
    AND info2.is_remove = false
    AND ( info2.accept_time IS NULL AND info2.review_ward_time IS NULL AND info2.review_province_time IS NULL AND info2.source_service_id <> '103' AND ((info2.permanent_province_id = @provinceID AND info2.province_id = @provinceID AND info2.detect_province_id = @provinceID) OR (info2.request_vaac_time is not null AND info2.province_id = @provinceID)))

    AND (info2.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info2.permanent_district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info2.permanent_ward_id = @wardID OR @wardID IS NULL OR @wardID = '')

    AND (info2.permanent_district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info2.permanent_ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)
   
    AND (@fromTime IS NULL OR @fromTime = '' OR info2.confirm_time >= @fromTime)
    AND (@toTime IS NULL OR @toTime = '' OR info2.confirm_time <= @toTime)
    GROUP BY info2.province_id, info2.permanent_district_id, info2.permanent_ward_id

UNION ALL
 -- Cần rà soát
 SELECT 
    info3.province_id province,
    info3.district_id district,
    info3.ward_id ward,
    0 AS numAlive,
    0 AS numDead,
    0 AS notReviewYet,
    COALESCE(count(info3.id),0) AS needReview,
    0 AS reviewed,
    0 AS outprovince
 FROM pac_patient_info info3
 WHERE
    @isVAAC is false
    AND info3.is_remove = false
    AND info3.accept_time IS NOT NULL 
    AND info3.review_ward_time IS NULL 
    AND info3.review_province_time IS NULL

    AND (info3.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info3.district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info3.ward_id = @wardID OR @wardID IS NULL OR @wardID = '')

    AND (info3.district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info3.ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)
   
    AND (@fromTime IS NULL OR @fromTime = '' OR info3.confirm_time >= @fromTime)
    AND (@toTime IS NULL OR @toTime = '' OR info3.confirm_time <= @toTime)
    GROUP BY info3.province_id, info3.district_id, info3.ward_id

UNION ALL
 -- Đã rà soát
 SELECT 
    info4.province_id province,
    info4.district_id district,
    info4.ward_id ward,
    0 AS numAlive,
    0 AS numDead,
    0 AS notReviewYet,
    0 AS needReview,
    COALESCE(count(info4.id),0) AS reviewed,
    0 AS outprovince
 FROM pac_patient_info info4
 WHERE
    @isVAAC is false
    AND info4.is_remove = false
    AND info4.accept_time IS NOT NULL 
    AND info4.review_ward_time IS NOT NULL 
    AND info4.review_province_time IS NULL

    AND (info4.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info4.district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info4.ward_id = @wardID OR @wardID IS NULL OR @wardID = '')

    AND (info4.district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info4.ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)
   
    AND (@fromTime IS NULL OR @fromTime = '' OR info4.confirm_time >= @fromTime)
    AND (@toTime IS NULL OR @toTime = '' OR info4.confirm_time <= @toTime)
    GROUP BY info4.province_id, info4.district_id, info4.ward_id

UNION ALL
 -- ngoại tỉnh
 SELECT 
    info4.province_id province,
    info4.district_id district,
    info4.ward_id ward,
    0 AS numAlive,
    0 AS numDead,
    0 AS notReviewYet,
    0 AS needReview,
    0 AS reviewed,
    COALESCE(count(info4.id),0) AS outprovince
 FROM pac_patient_info info4
 WHERE info4.is_remove = 0 AND  info4.accept_time IS NULL AND info4.review_ward_time IS NULL AND info4.review_province_time IS  NULL 
			AND info4.permanent_province_id <> info4.detect_province_id
                        AND info4.REQUEST_VAAC_TIME is null

    AND (info4.province_id = @provinceID OR @provinceID IS NULL OR @provinceID = '')
    AND (info4.district_id = @districtID  OR @districtID IS NULL OR @districtID = '')
    AND (info4.ward_id = @wardID OR @wardID IS NULL OR @wardID = '')

    AND (info4.district_id IN (@searchDistrictID) OR coalesce(@searchDistrictID, null) IS NULL)
    AND (info4.ward_id IN (@searchWardID) OR coalesce(@searchWardID, null) IS NULL)
   
    AND (@fromTime IS NULL OR @fromTime = '' OR info4.confirm_time >= @fromTime)
    AND (@toTime IS NULL OR @toTime = '' OR info4.confirm_time <= @toTime)
    GROUP BY info4.province_id, info4.district_id, info4.ward_id


) AS m_info ) AS rs_info GROUP BY rs_info.province, rs_info.district, rs_info.ward;