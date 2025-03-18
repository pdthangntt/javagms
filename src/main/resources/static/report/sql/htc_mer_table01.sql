SELECT 
    'introduced' AS status,
    e.service_id AS serviceID,
    e.gender_id AS genderID,
    e.is_agree_pretest AS aggreePreTest,
    e.advisory_time AS advisoryTime,
    e.pre_test_time AS preTestTime,
    SUM(IF(e.year_of_birth IS NULL OR e.year_of_birth = '',1,0)) unDefinedAge, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND YEAR(e.advisory_time) - e.year_of_birth < 1 ,1,0)) underOneAge, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 1) AND (YEAR(e.advisory_time) - e.year_of_birth <= 4),1,0)) oneToFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 5) AND (YEAR(e.advisory_time) - e.year_of_birth <= 9),1,0)) fiveToNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 10) AND (YEAR(e.advisory_time) - e.year_of_birth <= 14),1,0)) tenToFourteen, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 15 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 19),1,0)) fifteenToNineteen, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 20 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 24),1,0)) twentyToTwentyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 25 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 29),1,0)) twentyFiveToTwentyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 30 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 34),1,0)) thirtyToThirtyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 35 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 39),1,0)) thirtyFiveToThirtyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 40 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 44),1,0)) fortyToFortyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 45 AND (YEAR(e.advisory_time) - e.year_of_birth) <= 49),1,0)) fortyFiveToFortyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (YEAR(e.advisory_time) - e.year_of_birth >= 50),1,0)) overFifty
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
        AND e.referral_source LIKE CONCAT('%','2', '%')
        AND e.gender_id IN ('1' , '2')
        AND DATE_FORMAT(e.advisory_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time
        AND e.service_id IN (@services) 
        AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
        AND e.site_id = @siteID
GROUP BY e.gender_id

UNION ALL

SELECT 
    'agreed' AS status,
    e.service_id AS serviceID,
    e.gender_id AS genderID,
    e.is_agree_pretest AS aggreePreTest,
    e.advisory_time AS advisoryTime,
    e.pre_test_time AS preTestTime,
    SUM(IF(e.year_of_birth IS NULL OR e.year_of_birth = '',1,0)) unDefinedAge, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND YEAR(e.pre_test_time) - e.year_of_birth < 1,1,0)) underOneAge, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 1) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 4),1,0)) oneToFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 5) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 9),1,0)) fiveToNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 10) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 14),1,0)) tenToFourteen, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 15 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 19),1,0)) fifteenToNineteen, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 20 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 24),1,0)) twentyToTwentyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 25 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 29),1,0)) twentyFiveToTwentyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 30 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 34),1,0)) thirtyToThirtyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 35 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 39),1,0)) thirtyFiveToThirtyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 40 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 44),1,0)) fortyToFortyFour, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 45 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 49),1,0)) fortyFiveToFortyNine, 
    SUM(IF( e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND e.is_agree_pretest IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 50),1,0)) overFifty
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
        AND e.referral_source LIKE CONCAT('%','2', '%')
        AND e.gender_id IN ('1' , '2')
        AND (e.is_agree_pretest IS NOT NULL AND DATE_FORMAT(e.pre_test_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time)
        AND e.service_id IN (@services) 
        AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
        AND e.site_id = @siteID
GROUP BY  e.gender_id