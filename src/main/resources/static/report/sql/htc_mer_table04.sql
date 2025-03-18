SELECT 
    e.permanent_district AS districtID,
    e.service_id AS serviceID,
    e.gender_id AS genderID,
    COALESCE(e.confirm_results_id, '1') AS confirmResultsID,
    SUM(IF(e.year_of_birth IS NULL OR e.year_of_birth = '' ,1,0)) unDefinedAge, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND YEAR(e.confirm_time) - e.year_of_birth < 1) OR (e.pre_test_time IS NOT NULL AND e.confirm_time IS NULL AND YEAR(e.pre_test_time) - e.year_of_birth < 1 ),1,0)) underOneAge, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 1) AND (YEAR(e.confirm_time) - e.year_of_birth <= 4)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 1) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 4)),1,0)) oneToFour, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND(e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 5) AND (YEAR(e.confirm_time) - e.year_of_birth <= 9)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 5) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 9)),1,0)) fiveToNine, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 10) AND (YEAR(e.confirm_time) - e.year_of_birth <= 14)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 10) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 14)),1,0)) tenToFourteen, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 15 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 19)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 15 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 19)),1,0)) fifteenToNineteen, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 20 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 24)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 20 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 24)),1,0)) twentyToTwentyFour, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND(e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 25 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 29)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 25 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 29)),1,0)) twentyFiveToTwentyNine, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND(e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 30 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 34)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 30 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 34)),1,0)) thirtyToThirtyFour, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND(e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 35 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 39)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 35 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 39)),1,0)) thirtyFiveToThirtyNine, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND(e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 40 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 44)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 40 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 44)),1,0)) fortyToFortyFour, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND (e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 45 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 49)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 45 AND (YEAR(e.pre_test_time) - e.year_of_birth) <= 49)),1,0)) fortyFiveToFortyNine, 
    SUM(IF(e.year_of_birth IS NOT NULL AND e.year_of_birth <> '' AND ((e.confirm_time IS NOT NULL AND (YEAR(e.confirm_time) - e.year_of_birth >= 50)) OR
            (e.pre_test_time IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 50))),1,0)) overFifty,
    e.year_of_birth AS yearOfBirth
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
        AND ((e.results_patien_time IS NOT NULL AND e.confirm_results_id IN ('1' , '2')) OR ( e.results_time IS NOT NULL AND e.test_results_id = '1'))
        AND e.gender_id IN ('1' , '2')
        AND (( DATE_FORMAT(e.results_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time) OR
              (DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') BETWEEN @from_time AND @to_time))
        AND e.service_id IN (@services) 
        AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
        AND e.site_id = @siteID
GROUP BY e.service_id, e.gender_id, confirmResultsID;