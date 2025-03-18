SELECT
    main.province_id,
    main.drug_name,
    main.unit,
    main.site_code,
    main.quarter,
    COALESCE(SUM(main.dispensedQuantity),
    0) AS dispensedQuantity,
    COALESCE(SUM(main.plannedQuantity),
    0) AS plannedQuantity
FROM
    (
    SELECT
        m.province_id,
        m.drug_name,
        m.unit,
        m.site_code,
        m.quarter,
        SUM(m.patient) AS dispensedQuantity,
        0 AS plannedQuantity
    FROM
        (
        SELECT
            `current_province` AS province_id,
            CASE WHEN `month` > 0 AND `month` < 4 THEN '1' WHEN `month` > 3 AND `month` < 7 THEN '2' WHEN `month` > 6 AND `month` < 10 THEN '3' WHEN `month` > 9 AND `month` < 13 THEN '4' ELSE "none"
    END AS quarter,
    `drug_name`,
    `unit`,
    `site_code`,
    `patient`
FROM
    `pqm_drug_plan`
WHERE
    `current_province` = @province_id AND `year` = @year
) AS m WHERE m.quarter = @quarter
GROUP BY
    m.province_id,
    m.quarter,
    m.site_code,
    m.drug_name,
    m.unit
UNION ALL
SELECT
    m.province_id,
    m.drug_name,
    m.unit,
    m.site_code,
    m.quarter,
    0 AS dispensedQuantity,
    SUM(m.quantity) AS plannedQuantity
FROM
    (
    SELECT
        `province_id`,
        `drug_name`,
        `unit`,
        `site_code`,
        `exigency0` AS quantity,
        1 AS quarter
    FROM
        `pqm_drug_estimate`
    WHERE
        `year` = @year AND `province_id` = @province_id
    UNION ALL
SELECT
    `province_id`,
    `drug_name`,
    `unit`,
    `site_code`,
    `exigency1` AS quantity,
    2 AS quarter
FROM
    `pqm_drug_estimate`
WHERE
    `year` = @year AND `province_id` = @province_id
UNION ALL
SELECT
    `province_id`,
    `drug_name`,
    `unit`,
    `site_code`,
    `exigency2` AS quantity,
    3 AS quarter
FROM
    `pqm_drug_estimate`
WHERE
    `year` = @year AND `province_id` = @province_id
UNION ALL
SELECT
    `province_id`,
    `drug_name`,
    `unit`,
    `site_code`,
    `exigency3` AS quantity,
    4 AS quarter
FROM
    `pqm_drug_estimate`
WHERE
    `year` = @year AND `province_id` = @province_id
) AS m where 1 = 1
GROUP BY
    m.province_id,
    m.drug_name,
    m.unit,
    m.site_code,
    m.quarter
) AS main WHERE main.quarter = @quarter
GROUP BY
    main.province_id,
    main.drug_name,
    main.unit,
    main.site_code,
    main.quarter