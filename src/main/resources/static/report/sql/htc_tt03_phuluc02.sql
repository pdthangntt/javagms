select
	p.code,
	p.value as danhMucBaoCao,
	coalesce(SUM(tbl.tuVanTruocXetNghiem), 0) as tuVanTruocXetNghiem,
	coalesce(SUM(tbl.duocXetNghiemTong), 0) as duocXetNghiemTong,
	coalesce(SUM(tbl.duocXetNghiemDuongTinh), 0) as duocXetNghiemDuongTinh,
	coalesce(SUM(tbl.nhanKetQuaTong), 0) as nhanKetQuaTong,
	coalesce(SUM(tbl.nhanKetQuaDuongtinh), 0) as nhanKetQuaDuongtinh,
	p.id,
	p.parent_id
from (

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        count(h.id) as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where h.is_remove = 0 AND h.site_id IN (@site_id) 
    	AND h.advisory_time BETWEEN @from_date AND @to_date
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION
    
    select
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        count(h.id) as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 
        AND h.site_id IN (@site_id) 
        AND h.is_agree_pretest='1'
        AND (
            (h.test_results_id is not null AND h.pre_test_time is not null AND h.pre_test_time BETWEEN @from_date AND @to_date)
            OR (h.confirm_results_id = '2' AND h.confirm_time is not null AND h.confirm_time BETWEEN @from_date AND @to_date)
        )
        AND h.pre_test_time BETWEEN @from_date AND @to_date
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)  
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
        AND (h.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL)
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        count(h.id) as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id IN (@site_id) 
        AND (h.confirm_results_id = '2' AND h.confirm_time BETWEEN @from_date AND @to_date)
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)  
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
        AND (h.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL)
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION
    
    select
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id, 
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        count(h.id) as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id IN (@site_id) 
    AND (
           (h.results_patien_time BETWEEN @from_date AND @to_date)
        OR (h.results_time BETWEEN @from_date AND @to_date)
    )
    AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL)  
    AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
    AND h.service_id IN (@services) 
    AND (h.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION
    
    select
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        count(h.id) as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id IN (@site_id) 
        AND (h.confirm_results_id = 2 AND h.results_patien_time is not null AND h.results_patien_time BETWEEN @from_date AND @to_date)
        AND h.service_id IN (@services) 
        AND (h.code LIKE CONCAT('%',@code,'%') OR @code IS NULL) 
        AND (h.customer_type = @customerType OR @customerType IS NULL OR @customerType = '')  
        AND (h.object_group_id IN (@objects) OR coalesce(@objects, null) IS NULL) 
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

) as tbl
RIGHT JOIN parameter p on p.code = tbl.object_group_id
WHERE p.type = 'test-object-group'
GROUP BY p.position asc, p.value, p.code
