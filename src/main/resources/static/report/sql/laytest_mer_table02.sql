/**
 * Author:  NamAnh_HaUI
 * Created: Oct 29, 2019
 */
select
    p.id,
    p.value as doituongxetnghiem,
    tbl.object_group_id,
    p.parent_id,
    coalesce(SUM(tbl.positiveMale), 0) as positiveMale,
    coalesce(SUM(tbl.positiveFemale), 0) as positiveFemale,
    coalesce(SUM(tbl.positive), 0) as positive,
    coalesce(SUM(tbl.negativeMale), 0) as negativeMale,
    coalesce(SUM(tbl.negativeFemale), 0) as negativeFemale,
    coalesce(SUM(tbl.negative), 0) as negative,
    p.code
from (
    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        count(e.id) as positiveMale,
        0 as positiveFemale,
        0 as positive,
        0 as negativeMale,
        0 as negativeFemale,
        0 as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time
        AND e.gender_id = 1 
        AND e.confirm_results_id = 2 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        0 as positiveMale,
        count(e.id) as positiveFemale,
        0 as positive,
        0 as negativeMale,
        0 as negativeFemale,
        0 as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time
        AND e.gender_id = 2 
        AND e.confirm_results_id = 2 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        0 as positiveMale,
        0 as positiveFemale,
        count(e.id) as positive,
        0 as negativeMale,
        0 as negativeFemale,
        0 as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time
        AND e.confirm_results_id = 2 
        AND e.gender_id IN ('1','2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        0 as positiveMale,
        0 as positiveFemale,
        0 as positive,
        count(e.id) as negativeMale,
        0 as negativeFemale,
        0 as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time
        AND e.confirm_results_id = 1 
        AND e.gender_id = 1 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1
    
    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        0 as positiveMale,
        0 as positiveFemale,
        0 as positive,
        0 as negativeMale,
        count(e.id) as negativeFemale,
        0 as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time
        AND e.confirm_results_id = 1 
        AND e.gender_id = 2 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1
    
    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.positiveMale,
        t.positiveFemale,
        t.positive,
        t.negativeMale,
        t.negativeFemale,
        t.negative
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (
    select e.object_group_id,
        0 as positiveMale,
        0 as positiveFemale,
        0 as positive,
        0 as negativeMale,
        0 as negativeFemale,
        count(e.id) as negative
    from htc_laytest e
    where e.is_remove = FALSE
        AND e.advisory_time BETWEEN @from_time AND @to_time 
        AND e.confirm_results_id = 1 
        AND e.gender_id IN ('1','2') 
        AND e.site_id = @siteID 
        AND e.created_by = @staffID 
    GROUP BY e.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1 

) as tbl
RIGHT JOIN parameter p on p.code = tbl.object_group_id
WHERE p.type = 'test-object-group'
GROUP BY p.position asc, p.value, p.code

