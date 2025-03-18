SELECT
        v_main.years,
        sum(v_main.number_customer) as number_customer,
        sum(v_main.real_numberCustomer) as real_numberCustomer, 
        sum(v_main.number_positive) AS number_positive, 
        sum(v_main.real_numberPositive) as real_numberPositive 
 FROM (
     SELECT 2019 AS years,
                0 AS number_customer,
                0 as real_numberCustomer,
                0 AS number_positive,
                0 as real_numberPositive
     FROM dual
     UNION ALL
     SELECT 	2018 AS years,
                0 AS number_customer,
                0 as real_numberCustomer,
                0 AS number_positive,
                0 as real_numberPositive
     FROM dual
     UNION ALL

     SELECT 	e.years, 
                e.number_customer, 
                0 as real_numberCustomer, 
                e.number_positive, 
                0 as real_numberPositive  
     FROM htc_target as e
     WHERE e.years IN (@year -1, @year) AND e.site_id = @site_id

     UNION ALL

     SELECT v.y as years, 
                0 as number_customer,
                sum(v.xn) as real_numberCustomer,
                0 as number_positive,
                0 as real_numberPositive 
     FROM (
         SELECT 
            YEAR(e.pre_test_time) as y, 
            case when (e.is_agree_pretest=1 AND e.pre_test_time IS NOT NULL) then 1 else 0 end as xn
         FROM htc_visit as e 
         WHERE e.is_remove = 0 
                AND e.site_id = @site_id
                AND (e.test_results_id is not null AND e.pre_test_time is not null AND YEAR(e.pre_test_time) IN (@year - 1, @year))
                AND e.service_id IN @services) as v GROUP BY v.y
        
    UNION ALL

     SELECT v.y as years, 
                0 as number_customer,
                0 as real_numberCustomer,
                0 as number_positive,
                sum(v.duongtinh) as real_numberPositive 
     FROM (
         SELECT 
             YEAR(e.confirm_time) as y,      
             case when (e.confirm_results_id = 2 AND e.confirm_time IS NOT NULL) then 1 else 0 end as duongtinh
         FROM htc_visit as e 
         WHERE e.is_remove = 0 
                AND e.site_id = @site_id
                AND (e.confirm_results_id = 2 AND e.confirm_time is not null AND YEAR(e.confirm_time) IN (@year - 1, @year)) 
                AND e.service_id IN @services 
        ) as v GROUP BY v.y
) as v_main GROUP BY v_main.years ASC
