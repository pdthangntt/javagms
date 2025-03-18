SELECT 
    main.site_id,
    coalesce(SUM(main.truocKhiCoThai), 0) as truocKhiCoThai,
    coalesce(SUM(main.trongKhiMangThai), 0) as trongKhiMangThai,
    coalesce(SUM(main.khiChuyenDaDe), 0) as khiChuyenDaDe,
    coalesce(SUM(main.conDuocDuPhong), 0) as conDuocDuPhong,
    coalesce(SUM(main.conDuocDuPhongCotri), 0) as conDuocDuPhongCotri,
    coalesce(SUM(main.pcrLanMotDuongTinh), 0) as pcrLanMotDuongTinh
FROM (
    SELECT
	l.site_id,
	COUNT(l.arv_id) as truocKhiCoThai,
        0 as trongKhiMangThai,
        0 as khiChuyenDaDe,
        0 as conDuocDuPhong,
        0 as conDuocDuPhongCotri,
        0 as pcrLanMotDuongTinh
    FROM opc_arv_revision as l 
    WHERE  l.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = l.arv_id
                   group by log.arv_id)
	AND l.is_remove = 0
        AND l.status_of_treatment_id = '3'
    	AND l.treatment_time is not null
    	AND l.pregnant_start_date is not null
    	AND l.treatment_time < l.pregnant_start_date
        AND l.site_id IN (@site_id) 
    GROUP BY l.site_id

    UNION ALL

    SELECT
	l.site_id,
	0 as truocKhiCoThai,
        COUNT(l.arv_id) as trongKhiMangThai,
        0 as khiChuyenDaDe,
        0 as conDuocDuPhong,
        0 as conDuocDuPhongCotri,
        0 as pcrLanMotDuongTinh
    FROM opc_arv_revision as l 
    WHERE  l.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = l.arv_id
                   group by log.arv_id)
	AND l.is_remove = 0
	AND l.status_of_treatment_id = '3'
    	AND l.treatment_time is not null
    	AND l.pregnant_start_date is not null
    	AND l.treatment_time BETWEEN @from_date AND @to_date
    	AND l.treatment_time >= l.pregnant_start_date
        AND l.site_id IN (@site_id) 
    GROUP BY l.site_id

    UNION ALL

    SELECT
	l.site_id,
	0 as truocKhiCoThai,
        0 as trongKhiMangThai,
        COUNT(l.arv_id) as khiChuyenDaDe,
        0 as conDuocDuPhong,
        0 as conDuocDuPhongCotri,
        0 as pcrLanMotDuongTinh
    FROM opc_arv_revision as l 
    WHERE  l.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = l.arv_id
                   group by log.arv_id)
	AND l.is_remove = 0
	AND l.status_of_treatment_id = '3'
    	AND l.treatment_time is not null
    	AND l.pregnant_end_date is not null
    	AND l.treatment_time BETWEEN @from_date AND @to_date
    	AND l.treatment_time >= l.pregnant_end_date
        AND l.site_id IN (@site_id) 
    GROUP BY l.site_id

    UNION ALL

    SELECT
	mainm.site_id  as site,
	0 as truocKhiCoThai,
        0 as trongKhiMangThai,
        0 as khiChuyenDaDe,
        COUNT(mainm.id) as conDuocDuPhong,
        0 as conDuocDuPhongCotri,
        0 as pcrLanMotDuongTinh
        FROM (
            SELECT  log2.site_id as site_id,
                    log2.arv_id,
                    log2.id as id_log,
                    c.id as id
        FROM opc_child as c INNER JOIN opc_arv_revision as log2 ON log2.arv_id = c.arv_id 
                                                                AND log2.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = log2.arv_id
                   group by log.arv_id)
        WHERE log2.is_remove = 0 
        AND c.dob is not null AND c.dob BETWEEN @from_date AND @to_date
        AND ( ( c.preventive_date is not null AND c.preventive_date BETWEEN @from_date AND @to_date ) OR (c.preventive_end_date is not null AND c.preventive_end_date BETWEEN @from_date AND @to_date ) )
        ) as mainm
    GROUP BY site

    UNION ALL

    SELECT
	mainm.site_id  as site,
	0 as truocKhiCoThai,
        0 as trongKhiMangThai,
        0 as khiChuyenDaDe,
        0 as conDuocDuPhong,
        COUNT(mainm.id) as conDuocDuPhongCotri,
        0 as pcrLanMotDuongTinh
        FROM (
            SELECT  log2.site_id as site_id,
                    log2.arv_id,
                    log2.id as id_log,
                    c.id as id
        FROM opc_child as c INNER JOIN opc_arv_revision as log2 ON log2.arv_id = c.arv_id 
                                                                AND log2.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = log2.arv_id
                   group by log.arv_id)
        WHERE log2.is_remove = 0 
        AND  c.dob is not null AND DATE_FORMAT(c.dob, '%Y-%m-%d') <= @to_date
        AND ( ( c.cotrimoxazole_from_time is not null AND c.cotrimoxazole_from_time BETWEEN @from_date AND @to_date ) OR (c.cotrimoxazole_to_time is not null AND c.cotrimoxazole_to_time BETWEEN @from_date AND @to_date ) )
        AND TIMESTAMPDIFF(MONTH, c.dob, c.cotrimoxazole_from_time) <= 1
            
        ) as mainm
    GROUP BY site

    UNION ALL

SELECT
	mainm.site_id  as site,
	0 as truocKhiCoThai,
        0 as trongKhiMangThai,
        0 as khiChuyenDaDe,
        0 as conDuocDuPhong,
        0 as conDuocDuPhongCotri,
        COUNT(mainm.id) as pcrLanMotDuongTinh
        FROM (
            SELECT  log2.site_id as site_id,
                    log2.arv_id,
                    log2.id as id_log,
                    c.id as id
        FROM opc_child as c INNER JOIN opc_arv_revision as log2 ON log2.arv_id = c.arv_id 
                                                                AND log2.id = (SELECT
                        max(id)
                   FROM opc_arv_revision as log where DATE_FORMAT(log.created_at, '%Y-%m-%d') <= @to_date AND log.arv_id = log2.arv_id
                   group by log.arv_id)
        WHERE log2.is_remove = 0 
        AND  c.dob is not null AND DATE_FORMAT(c.dob, '%Y-%m-%d') <= @to_date
         AND c.pcr_one_result = '1'
         AND c.treatment_time is not null AND c.treatment_time BETWEEN @from_date AND @to_date
            
        ) as mainm
GROUP BY site

) as main GROUP BY main.site_id