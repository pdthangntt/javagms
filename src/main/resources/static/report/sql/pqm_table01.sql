select 
    main.siteID,
    coalesce(sum(main.positive), 0) as positive,
    coalesce(sum(main.recent), 0) as recent,
    coalesce(sum(main.art), 0) as art
from(
SELECT e.site_id AS siteID, 
    COUNT(e.ID) AS positive,
    0 AS  recent,
    0 AS art
FROM htc_visit as e 
WHERE e.confirm_results_id = '2' 
AND e.confirm_time is not null AND DATE_FORMAT(e.confirm_time, '%Y-%m-%d') BETWEEN @from AND @to
GROUP BY e.site_id

UNION ALL

SELECT e.site_id AS siteID, 
    0 AS positive,
    COUNT(e.ID) AS  recent,
    0 AS art
FROM htc_visit as e 
WHERE e.confirm_results_id = '2' 
AND e.early_diagnose = '1' 
AND e.EARLY_HIV_DATE is not null AND DATE_FORMAT(e.EARLY_HIV_DATE, '%Y-%m-%d') BETWEEN @from AND @to
-- ngay xn nhiem moi
GROUP BY e.site_id

UNION ALL

SELECT e.site_id AS siteID, 
    0 AS positive,
    0 AS  recent,
    COUNT(e.ID) AS art
FROM htc_visit as e 
WHERE e.confirm_results_id = '2' 
AND e.exchange_time is not null -- ngay chuyen gui thuoc ky bao cao
AND e.exchange_time is not null AND DATE_FORMAT(e.exchange_time, '%Y-%m-%d') BETWEEN @from AND @to
AND e.register_therapy_time is not null
AND e.register_therapy_site is not null
AND e.therapy_no is not null
AND e.therapy_exchange_status = '2'

GROUP BY e.site_id



) as main
GROUP BY main.siteID