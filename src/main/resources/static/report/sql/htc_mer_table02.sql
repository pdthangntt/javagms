SELECT 
    e.site_id AS siteID,
    e.service_id AS serviceID,
    e.gender_id AS genderID,
    e.early_hiv AS earlyHiv,
    SUM(IF(e.confirm_time IS NULL OR e.year_of_birth IS NULL,1,0)) unDefinedAge, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth < 15),1,0)) underFifteen, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 15 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 19),1,0)) fifteenToNineteen, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 20 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 24),1,0)) twentyToTwentyFour, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 25 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 29),1,0)) twentyFiveToTwentyNine, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 30 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 34),1,0)) thirtyToThirtyFour, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 35 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 39),1,0)) thirtyFiveToThirtyNine, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 40 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 44),1,0)) fortyToFortyFour, 
    SUM(IF((YEAR(e.confirm_time) - e.year_of_birth >= 45 AND (YEAR(e.confirm_time) - e.year_of_birth) <= 49),1,0)) fortyFiveToFortyNine, 
    SUM(IF(YEAR(e.confirm_time) - e.year_of_birth >= 50,1,0)) overFifty
FROM
    htc_visit e
WHERE
    e.is_remove = FALSE
    AND e.early_hiv IN ('2', '4') 
    AND e.gender_id IN ('1' ,'2')
    AND e.early_hiv_date is not null AND DATE_FORMAT(e.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time AND @to_time
    AND e.service_id IN (@services) 
    AND (e.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
    AND e.site_id = @siteID
GROUP BY e.site_id, e.service_id, e.gender_id, e.early_hiv;
