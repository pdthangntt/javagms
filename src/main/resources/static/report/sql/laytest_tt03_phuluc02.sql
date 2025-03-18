/**
 * Author:  NamAnh_HaUI
 * Created: Oct 29, 2019
 */
select
    p.code,
    p.value as danhMucBaoCao,
    coalesce(SUM(tbl.tuVanTruocXetNghiem), 0) as tuVanTruocXetNghiem,
    coalesce(SUM(tbl.duocXetNghiemDuongTinh), 0) as duocXetNghiemDuongTinh,
    coalesce(SUM(tbl.gioiThieuBanChich), 0) as gioiThieuBanChich,
    coalesce(SUM(tbl.chuyenGuiDieuTri), 0) as chuyenGuiDieuTri,
    p.id,
    p.parent_id
from (
    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemDuongTinh,
        t.gioiThieuBanChich,
        t.chuyenGuiDieuTri
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        count(h.id) as tuVanTruocXetNghiem,
        0 as duocXetNghiemDuongTinh,
        0 as gioiThieuBanChich,
        0 as chuyenGuiDieuTri
    from htc_laytest h
    where h.is_remove = 0 AND h.site_id = @site_id AND h.created_by = @staff_id 
    	AND h.advisory_time BETWEEN @from_date AND @to_date 
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemDuongTinh,
        t.gioiThieuBanChich,
        t.chuyenGuiDieuTri
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
   		select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
   		select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        count(h.id) as duocXetNghiemDuongTinh,
        0 as gioiThieuBanChich,
        0 as chuyenGuiDieuTri
    from htc_laytest h
    where  h.is_remove = 0 AND h.site_id = @site_id AND h.created_by = @staff_id  
    	AND h.confirm_time BETWEEN @from_date AND @to_date 
        AND h.confirm_results_id = 2 
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemDuongTinh,
        t.gioiThieuBanChich,
        t.chuyenGuiDieuTri
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
                select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
                select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemDuongTinh,
        count(h.id) as gioiThieuBanChich,
        0 as chuyenGuiDieuTri
    from htc_laytest h
    where h.is_remove = 0 AND h.site_id = @site_id AND h.created_by = @staff_id  
        AND h.referral_source = 2 
        AND h.advisory_time BETWEEN @from_date AND @to_date 
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1

    UNION

    select 
        SUBSTRING_INDEX(SUBSTRING_INDEX(t.object_group_id, ';', numbers.n), ';', -1) as object_group_id,
        t.tuVanTruocXetNghiem,
        t.duocXetNghiemDuongTinh,
        t.gioiThieuBanChich,
        t.chuyenGuiDieuTri
    from (select 1 n union all select 2 union all select 3 union all select 4 union all 
                select 5 union all select 6 union all select 7 union all select 8 union all select 9 union all 
                select 10 union all select 11 union all select 12 union all select 13 union all select 14) numbers 
        inner join (select h.object_group_id,
        0 as tuVanTruocXetNghiem,
        0 as duocXetNghiemDuongTinh,
        0 as gioiThieuBanChich,
        count(h.id) as chuyenGuiDieuTri
    from htc_laytest h
    where h.is_remove = 0 AND h.site_id = @site_id AND h.created_by = @staff_id  
        AND h.exchange_time BETWEEN @from_date AND @to_date 
        AND h.exchange_status = 1
    GROUP BY h.object_group_id) as t on CHAR_LENGTH(t.object_group_id) - CHAR_LENGTH(REPLACE(t.object_group_id, ';', '')) >=numbers.n-1
) as tbl
RIGHT JOIN parameter p on p.code = tbl.object_group_id
WHERE p.type = 'test-object-group'
GROUP BY p.position asc, p.value, p.code

