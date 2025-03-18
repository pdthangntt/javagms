SELECT 
    rs_info.province,
    rs_info.district,
    SUM(rs_info.month1) month1,
    SUM(rs_info.month2) month2,
    SUM(rs_info.month3) month3,
    SUM(rs_info.agv1) agv1,
    SUM(rs_info.agv2) agv2,
    SUM(rs_info.agv3) agv3
FROM (
SELECT 
    m_info.month1 month1,
    m_info.month2 month2,
    m_info.month3 month3,
    m_info.agv1 agv1,
    m_info.agv2 agv2,
    m_info.agv3 agv3,
    m_info.province AS province,
    CASE 
        WHEN (@levelDisplay = 'province') THEN 0
        ELSE m_info.district
    END AS district
FROM
(SELECT 
    info.province_id province,
    info.district_id district,
    sum(info.permanent_htc) AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3
 FROM ql_report info
 WHERE
	info.month = @month1 AND info.year =  @year1 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id
UNION ALL
SELECT 
    info.province_id province,
    info.district_id district,
    0 AS month1,
    sum(info.permanent_htc) AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3
 FROM ql_report info
 WHERE
	info.month = @month2 AND info.year =  @year2 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id
    UNION ALL
SELECT 
    info.province_id province,
    info.district_id district,
    0 AS month1,
    0 AS month2,
    sum(info.permanent_htc) AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id

UNION ALL
SELECT 
    info.province_id province,
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    sum(info.permanent_early) AS agv1,
    0 AS agv2,
    0 AS agv3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id

UNION ALL
SELECT 
    info.province_id province,
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    sum(info.permanent_early)  AS agv2,
    0 AS agv3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id

UNION ALL
SELECT 
    info.province_id province,
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    sum(info.permanent_early)  AS agv3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND (@levelDisplay = 'province' OR (@levelDisplay <> 'province' AND info.province_id = @provinceID))
    GROUP BY info.province_id, info.district_id

) AS m_info ) AS rs_info GROUP BY rs_info.province, rs_info.district; 