select 
    v.d, 
    sum(v.ageunder15) as ageunder15, 
    sum(v.age15to24) as age15to24, 
    sum(v.age25to49) as age25to49, 
    sum(v.ageon49) as ageon49 
FROM (
    SELECT 
        case 
            when ((e.test_results_id = '2' OR e.test_results_id = '3') AND e.results_patien_time IS NOT NULL) then CONCAT(QUARTER(e.results_patien_time), '/', YEAR(e.results_patien_time))
            when (e.test_results_id = '1' AND e.results_time IS NOT NULL) then CONCAT(QUARTER(e.results_time), '/', YEAR(e.results_time))
        END as d, 
        case 
            when ((e.results_time IS NOT NULL OR e.results_patien_time IS NOT NULL) AND (e.pre_test_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth < 15))) then 1 else 0 
        END as ageunder15,
        case 
            when ((e.results_time IS NOT NULL OR e.results_patien_time IS NOT NULL) AND (e.pre_test_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 15) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 24))) then 1 else 0 
        END as age15to24,
        case 
            when ((e.results_time IS NOT NULL OR e.results_patien_time IS NOT NULL) AND (e.pre_test_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth >= 25) AND (YEAR(e.pre_test_time) - e.year_of_birth <= 49))) then 1 else 0 
        END as age25to49,
        case 
            when ((e.results_time IS NOT NULL OR e.results_patien_time IS NOT NULL) AND (e.pre_test_time IS NOT NULL AND e.year_of_birth != '' AND e.year_of_birth IS NOT NULL AND (YEAR(e.pre_test_time) - e.year_of_birth > 49))) then 1 else 0 
        END as ageon49
    FROM htc_visit as e 
    WHERE e.is_remove = 0 
        AND e.site_id IN (@site_id)
        AND e.service_id IN (@services) 
        AND (
                ((e.test_results_id = '2' OR e.test_results_id = '3') AND e.results_patien_time is not null AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.results_patien_time, '%Y-%m-%d') <= @to_time)
             OR (e.test_results_id = '1' AND e.results_time is not null AND DATE_FORMAT(e.results_time, '%Y-%m-%d') >= @from_time AND DATE_FORMAT(e.results_time, '%Y-%m-%d') <= @to_time)
        )
   ) as v 
GROUP BY v.d