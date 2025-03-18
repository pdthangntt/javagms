SELECT 
    e.province_id, 
    SUM(e.permanent_htc) as htc, 
    SUM(e.permanent_early) as early 
FROM ql_report as e 
WHERE e.month >= @from_month AND e.month <= @to_month AND e.year = @year 
GROUP BY e.province_id