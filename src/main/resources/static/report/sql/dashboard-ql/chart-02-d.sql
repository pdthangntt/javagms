SELECT 
m_f.district,
m_f.month1,
m_f.month2,
m_f.month3,
    m_f.agv1 agv1,
    m_f.agv2 agv2,
    m_f.agv3 agv3,
((m_f.month1 - (m_f.agv1 / 30))/(m_f.agv1 / 30)) as t1,
((m_f.month2 - (m_f.agv2 / 30))/(m_f.agv2 / 30)) as t2,
((m_f.month3 - (m_f.agv3 / 30))/(m_f.agv3 / 30)) as t3,
m_f.early1,
m_f.early2,
m_f.early3
FROM 
(
SELECT 
    rs_info.district,
    SUM(rs_info.month1) month1,
    SUM(rs_info.month2) month2,
    SUM(rs_info.month3) month3,
    SUM(rs_info.agv1) agv1,
    SUM(rs_info.agv2) agv2,
    SUM(rs_info.agv3) agv3,
    SUM(rs_info.early1) early1,
    SUM(rs_info.early2) early2,
    SUM(rs_info.early3) early3
FROM (
SELECT 
    m_info.month1 month1,
    m_info.month2 month2,
    m_info.month3 month3,
    m_info.agv1 agv1,
    m_info.agv2 agv2,
    m_info.agv3 agv3,
    m_info.early1 early1,
    m_info.early2 early2,
    m_info.early3 early3,
    m_info.district AS district
FROM
(
SELECT 
    info.district_id district,
    sum(info.permanent_htc) AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month1 AND info.year =  @year1 AND info.province_id = @provinceID
    GROUP BY info.district_id
UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    sum(info.permanent_htc) AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month2 AND info.year =  @year2 AND info.province_id = @provinceID
    GROUP BY info.district_id
    UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    sum(info.permanent_htc) AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND info.province_id = @provinceID
    GROUP BY info.district_id

UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    sum(info.permanent_avg_30_month) AS agv1,
    0 AS agv2,
    0 AS agv3,
0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month1 AND info.year =  @year1 AND info.province_id = @provinceID
    GROUP BY info.district_id

UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    sum(info.permanent_avg_30_month) AS agv2,
    0 AS agv3,
0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month2 AND info.year =  @year2 AND info.province_id = @provinceID
    GROUP BY info.district_id
UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    sum(info.permanent_avg_30_month) AS agv3,
    0 AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND info.province_id = @provinceID
    GROUP BY info.district_id

UNION ALL

SELECT 
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    sum(info.permanent_early) AS early1,
    0 AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month1 AND info.year =  @year1 AND info.province_id = @provinceID
    GROUP BY info.district_id
UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    0 AS early1,
    sum(info.permanent_early) AS early2,
    0 AS early3
 FROM ql_report info
 WHERE
	info.month = @month2 AND info.year =  @year2 AND info.province_id = @provinceID
    GROUP BY info.district_id
    UNION ALL
SELECT 
    
    info.district_id district,
    0 AS month1,
    0 AS month2,
    0 AS month3,
    0 AS agv1,
    0 AS agv2,
    0 AS agv3,
    0 AS early1,
    0 AS early2,
    sum(info.permanent_early) AS early3
 FROM ql_report info
 WHERE
	info.month = @month3 AND info.year =  @year3 AND info.province_id = @provinceID
    GROUP BY info.district_id

) AS m_info ) AS rs_info GROUP BY  rs_info.district
) as m_f