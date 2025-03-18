select
	p.id,
        tbl.object_group_id,
        tbl.service_id,
        p.parent_id,
	coalesce(SUM(tbl.tuVanTruocXetNghiem), 0) as so_tu_van,
	coalesce(SUM(tbl.duocXetNghiemTong), 0) as so_xn,
	coalesce(SUM(tbl.duocXetNghiemDuongTinh), 0) as so_xn_d,
	coalesce(SUM(tbl.nhanKetQuaTong), 0) as so_nhankq,
	coalesce(SUM(tbl.nhanKetQuaDuongtinh), 0) as so_nhankq_d
from (
    select 
        t.object_group_id,
        t.service_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select h.object_group_id, h.service_id,
        count(h.id) as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where h.is_remove = 0 AND h.site_id = @site_id
    	AND h.advisory_time BETWEEN @from_date AND @to_date
    GROUP BY h.object_group_id, h.service_id) as t

    UNION
    
    select
        t.object_group_id,
        t.service_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select h.object_group_id, h.service_id,
        0 as tuVanTruocXetNghiem,
        count(h.id) as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 
        AND h.site_id = @site_id
        AND h.is_agree_pretest='1'
        AND (
            (h.test_results_id is not null AND h.pre_test_time is not null AND h.pre_test_time BETWEEN @from_date AND @to_date)
            OR (h.confirm_results_id = '2' AND h.confirm_time is not null AND h.confirm_time BETWEEN @from_date AND @to_date)
        )
        AND h.pre_test_time BETWEEN @from_date AND @to_date
    GROUP BY h.object_group_id, h.service_id) as t

    UNION

    select
        t.object_group_id,
        t.service_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select h.object_group_id, h.service_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        count(h.id) as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id = @site_id
        AND (h.confirm_results_id = '2' AND h.confirm_time BETWEEN @from_date AND @to_date)
    GROUP BY h.object_group_id, h.service_id) as t 

    UNION
    
    select
        t.object_group_id,
        t.service_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select h.object_group_id, h.service_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        count(h.id) as nhanKetQuaTong,
        0 as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id = @site_id
    AND (
           (h.results_patien_time BETWEEN @from_date AND @to_date)
        OR (h.results_time BETWEEN @from_date AND @to_date)
    )
    GROUP BY h.object_group_id, h.service_id) as t

    UNION
    
    select
        t.object_group_id, 
        t.service_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemTong,
        t.duocXetNghiemDuongTinh,
        t.nhanKetQuaTong,
        t.nhanKetQuaDuongtinh
    from (select h.object_group_id, h.service_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemTong,
        0 as duocXetNghiemDuongTinh,
        0 as nhanKetQuaTong,
        count(h.id) as nhanKetQuaDuongtinh
    from htc_visit h
    where  h.is_remove = 0 AND h.site_id = @site_id
        AND (h.confirm_results_id = 2 AND h.results_patien_time is not null AND h.results_patien_time BETWEEN @from_date AND @to_date)
    GROUP BY h.object_group_id, h.service_id) as t
) as tbl
RIGHT JOIN parameter p on p.code = tbl.object_group_id
WHERE p.type = 'test-object-group' AND tbl.service_id is not null
GROUP BY p.value, p.code, tbl.service_id
