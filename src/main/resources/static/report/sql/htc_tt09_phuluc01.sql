select 
    p.code,
    p.value as doituongxetnghiem,
    coalesce(sum(v_main.duongtinh15), 0) as duongtinh15,
    coalesce(sum(v_main.amtinh15), 0) as amtinh15,
    coalesce(sum(v_main.duongtinh25), 0) as duongtinh25,
    coalesce(sum(v_main.amtinh25), 0) as amtinh25,
    coalesce(sum(v_main.duongtinh49), 0) as duongtinh49,
    coalesce(sum(v_main.amtinh49), 0) as amtinh49,
    coalesce(sum(v_main.duongtinhmax), 0) as duongtinhmax,
    coalesce(sum(v_main.amtinhmax), 0) as amtinhmax,
    coalesce(sum(v_main.male_duongtinh), 0) as male_duongtinh,
    coalesce(sum(v_main.male_amtinh), 0) as male_amtinh,
    coalesce(sum(v_main.female_duongtinh), 0) as female_duongtinh,
    coalesce(sum(v_main.female_amtinh), 0) as female_amtinh,
    p.id,
    p.parent_id,
    coalesce(sum(v_main.duongtinhnone), 0) as duongtinhnone,
    coalesce(sum(v_main.amtinhnone), 0) as amtinhnone
from(
select 
    v.object_group_id,
    case when v.ages = "0-15" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh15,
    case when v.ages = "0-15" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh15,
    case when v.ages = "15-25" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh25,
    case when v.ages = "15-25" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh25,
    case when v.ages = "25-49" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh49,
    case when v.ages = "25-49" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh49,
    case when v.ages = "49-max" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinhmax,
    case when v.ages = "49-max" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinhmax,
    0 as male_duongtinh,
    0 as male_amtinh,
    0 as female_duongtinh,
    0 as female_amtinh,
    case when v.ages = "Không rõ" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinhnone,
    case when v.ages = "Không rõ" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinhnone
	from (select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.id,
        CASE 
            WHEN (t.confirm_results_id = '2' AND (t.confirm_time BETWEEN @from_date AND @to_date)) THEN '2'
            ELSE '0' 
        END as confirm_result, 
        CASE WHEN (t.year_of_birth REGEXP '^[0-9]{4}$') THEN
            CASE
                WHEN t.age < 15 THEN "0-15"
                WHEN t.age >= 15 AND t.age <25 THEN "15-25"
                WHEN t.age >= 25 AND t.age <=49 THEN "25-49"
                WHEN t.age >49 THEN "49-max"
                ELSE "Không rõ"
            END
        ELSE "Không rõ" END as ages
from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (SELECT 
        h.id,
        h.object_group_id,
        h.year_of_birth,
        h.confirm_results_id,
        h.confirm_time,
        h.pre_test_time,
        DATE_FORMAT(h.confirm_time, '%Y') - CAST(h.year_of_birth AS UNSIGNED) as age
FROM htc_visit as h 
WHERE   h.is_remove = 0
        AND h.site_id IN (@site_id) 
        AND h.is_agree_pretest='1'
        AND h.confirm_results_id = '2' AND h.confirm_time is not null AND h.confirm_time BETWEEN @from_date AND @to_date
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1) as v
GROUP BY v.object_group_id, v.ages, v.confirm_result

UNION ALL

select 
    v.object_group_id,
    case when v.ages = "0-15" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh15,
    case when v.ages = "0-15" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh15,
    case when v.ages = "15-25" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh25,
    case when v.ages = "15-25" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh25,
    case when v.ages = "25-49" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinh49,
    case when v.ages = "25-49" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinh49,
    case when v.ages = "49-max" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinhmax,
    case when v.ages = "49-max" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinhmax,
    0 as male_duongtinh,
    0 as male_amtinh,
    0 as female_duongtinh,
    0 as female_amtinh,
    case when v.ages = "Không rõ" and v.confirm_result = '2' then COUNT(v.id) else 0 end as duongtinhnone,
    case when v.ages = "Không rõ" and v.confirm_result = '1' then COUNT(v.id) else 0 end as amtinhnone
	from (select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.id,
        '1' as confirm_result, 
        CASE WHEN (t.year_of_birth REGEXP '^[0-9]{4}$') THEN
            CASE
                WHEN t.age < 15 THEN "0-15"
                WHEN t.age >= 15 AND t.age <25 THEN "15-25"
                WHEN t.age >= 25 AND t.age <=49 THEN "25-49"
                WHEN t.age >49 THEN "49-max"
                ELSE "Không rõ"
            END
        ELSE "Không rõ" END as ages
from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (SELECT 
        h.id,
        h.object_group_id,
        h.year_of_birth,
        h.confirm_results_id,
        h.confirm_time,
        h.pre_test_time,
        DATE_FORMAT(h.pre_test_time, '%Y') - CAST(h.year_of_birth AS UNSIGNED) as age
FROM htc_visit as h 
WHERE   h.is_remove = 0
        AND h.site_id IN (@site_id)
        AND h.is_agree_pretest='1'
        AND h.pre_test_time is not null AND h.pre_test_time BETWEEN @from_date AND @to_date
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1) as v
GROUP BY v.object_group_id, v.ages, v.confirm_result

UNION ALL

select 
    v.object_group_id,
    0 as duongtinh15,
    0 as amtinh15,
    0 as duongtinh25,
    0 as amtinh25,
    0 as duongtinh49,
    0 as amtinh49,
    0 as duongtinhmax,
    0 as amtinhmax,
    case when v.gender = "1" and v.confirm_result = '2' then COUNT(v.id) else 0 end as male_duongtinh,
    case when v.gender = "1" and v.confirm_result = '1' then COUNT(v.id) else 0 end as male_amtinh,
    case when v.gender != "1" and v.confirm_result = '2' then COUNT(v.id) else 0 end as female_duongtinh,
    case when v.gender != "1" and v.confirm_result = '1' then COUNT(v.id) else 0 end as female_amtinh,
    0 as duongtinhnone,
    0 as amtinhnone
from (select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.id,
        CASE 
            WHEN (t.confirm_results_id = '2' AND (t.confirm_time BETWEEN @from_date AND @to_date)) THEN '2' 
        ELSE '0' 
        END as confirm_result, 
        CASE WHEN t.gender_id = '2' THEN 2 ELSE 1 END as gender
from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (SELECT 
        h.id,
        h.object_group_id,
        h.gender_id,
        h.confirm_results_id,
        h.confirm_time
FROM htc_visit as h 
WHERE   h.is_remove = 0
        AND h.gender_id IN ('1', '2')
        AND h.site_id IN (@site_id) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
        AND h.is_agree_pretest='1'
        AND ( 
            (h.confirm_results_id = '2' AND h.confirm_time is not null AND h.confirm_time BETWEEN @from_date AND @to_date)
        )
        AND h.service_id IN (@services) 
) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1) as v
GROUP BY v.object_group_id, v.gender, v.confirm_result


UNION ALL

select 
    v.object_group_id,
    0 as duongtinh15,
    0 as amtinh15,
    0 as duongtinh25,
    0 as amtinh25,
    0 as duongtinh49,
    0 as amtinh49,
    0 as duongtinhmax,
    0 as amtinhmax,
    case when v.gender = "1" and v.confirm_result = '2' then COUNT(v.id) else 0 end as male_duongtinh,
    case when v.gender = "1" and v.confirm_result = '1' then COUNT(v.id) else 0 end as male_amtinh,
    case when v.gender != "1" and v.confirm_result = '2' then COUNT(v.id) else 0 end as female_duongtinh,
    case when v.gender != "1" and v.confirm_result = '1' then COUNT(v.id) else 0 end as female_amtinh,
    0 as duongtinhnone,
    0 as amtinhnone
from (select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.id,
        '1' as confirm_result, 
        CASE WHEN t.gender_id = '2' THEN 2 ELSE 1 END as gender
from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (SELECT 
        h.id,
        h.object_group_id,
        h.gender_id,
        h.confirm_results_id,
        h.confirm_time
FROM htc_visit as h 
WHERE   h.is_remove = 0
        AND h.gender_id IN ('1', '2')
        AND h.site_id IN (@site_id)
        AND h.is_agree_pretest='1'
        AND ( 
            (h.test_results_id is not null AND h.pre_test_time is not null AND h.pre_test_time BETWEEN @from_date AND @to_date)
        )
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1) as v
GROUP BY v.object_group_id, v.gender, v.confirm_result

) as v_main RIGHT JOIN parameter p on p.code = v_main.object_group_id
WHERE p.type = 'test-object-group'
GROUP BY p.value, p.code ORDER BY p.position asc