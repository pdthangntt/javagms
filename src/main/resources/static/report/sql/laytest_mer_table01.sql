/**
 * Author:  NamAnh_HaUI
 * Created: Nov 5, 2019
 */
SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (SELECT 
        '' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 2 
        AND e.created_by = @staffID 
        AND e.year_of_birth IS NULL
    GROUP BY e.gender_id
    UNION ALL 
    SELECT 
        '' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 1 
        AND e.created_by = @staffID 
        AND e.year_of_birth IS NULL
    GROUP BY e.gender_id
    UNION ALL
    SELECT '' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '< 1' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 2 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth < 1 
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '< 1' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 1 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth < 1 
    GROUP BY e.gender_id
    UNION ALL
    SELECT '< 1' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '1-9' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 2 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 1 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 9
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '1-9' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 1 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 1 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 9
    GROUP BY e.gender_id
    UNION ALL
    SELECT '1-9' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '10-14' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 2 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 10 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 14
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '10-14' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 1 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 10 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 14
    GROUP BY e.gender_id
    UNION ALL
    SELECT '10-14' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '15-19' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 2 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 15 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 19
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '15-19' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 1 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 15 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 19
    GROUP BY e.gender_id
    UNION ALL
    SELECT '15-19' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '20-24' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 2 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 20 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 24
    GROUP BY e.gender_id
    UNION ALL 
    SELECT 
        '20-24' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 1 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 20 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 24
    GROUP BY e.gender_id
    UNION ALL
    SELECT '20-24' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '25-49' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 2 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 25 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 49
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '25-49' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
        AND e.confirm_results_id = 1 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 25 
     	AND YEAR(e.advisory_time) - e.year_of_birth <= 49
    GROUP BY e.gender_id
    UNION ALL
    SELECT '25-49' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age

UNION ALL

SELECT 
    tbl.age as age,
    SUM(tbl.positiveMale) as positiveMale,
    SUM(tbl.positiveFemale) as positiveFemale,
    SUM(tbl.positive) as positive,
    SUM(tbl.negativeMale) as negativeMale,
    SUM(tbl.negativeFemale) as negativeFemale,
    SUM(tbl.negative) as negative
FROM 
    (
    SELECT 
        '50+' as age,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS positiveMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS positiveFemale,
        COUNT(e.id) AS positive,
        0 AS negativeMale,
        0 AS negativeFemale,
        0 AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 2 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 50
    GROUP BY e.gender_id
    UNION ALL
    SELECT 
        '50+' as age,
        0 AS positiveMale,
        0 AS positiveFemale,
        0 AS positive,
        CASE WHEN e.gender_id = 1 THEN COUNT(e.id) ELSE 0 END AS negativeMale,
        CASE WHEN e.gender_id = 2 THEN COUNT(e.id) ELSE 0 END AS negativeFemale,
        COUNT(e.id) AS negative
    FROM
        htc_laytest e
    WHERE e.is_remove = FALSE
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') >= @from_time 
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') <= @to_time 
        AND e.gender_id IN ('1', '2') 
        AND e.site_id = @siteID 
        AND e.confirm_results_id = 1 
        AND e.created_by = @staffID 
        AND YEAR(e.advisory_time) - e.year_of_birth >= 50
    GROUP BY e.gender_id
    UNION ALL
    SELECT '50+' as age,0 AS positiveMale,0 AS positiveFemale,0 AS positive, 0 AS negativeMale,0 AS negativeFemale,0 AS negative
) as tbl 
GROUP BY tbl.age