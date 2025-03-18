SELECT 
	SUM(IF(
        (@from_time1 IS NOT null AND DATE_FORMAT(m.confirm_time, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.confirm_time, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.confirm_time, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.confirm_time, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.confirm_time, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5) , 1, 0)) as phathienduong,
        SUM(IF(
        (@from_time1 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5) , 1, 0)) as lamnhiemmoi,
        SUM(IF((
        	(@from_time1 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5)
        ) AND m.early_hiv = '4' , 1, 0)) as ketquanhiemmoi,
        SUM(IF(
        	(@from_time1 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5), 1, 0)) as tailuongvirus,
        SUM(IF(
        	(
        	(@from_time1 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.early_hiv_date, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5)
        ) AND m.early_hiv = '4'
        AND (
            (@from_time1 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time1 AND @to_time1) OR
        (@from_time2 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time2 AND @to_time2) OR
        (@from_time3 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time3 AND @to_time3) OR
        (@from_time4 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time4 AND @to_time4) OR
        (@from_time5 IS NOT null AND DATE_FORMAT(m.virus_load_date, '%Y-%m-%d') BETWEEN @from_time5 AND @to_time5)) AND
            m.virus_load = '4', 1, 0)) as virusmoinhiem
FROM
(
    SELECT * FROM htc_confirm c
    WHERE 
    c.is_remove = 0
    AND c.results_id = '2'
    AND c.site_id IN (@site_id)
    AND c.source_site_id IN (@source_site_id)
    AND c.service_id IN (@services)
    AND (DATE_FORMAT(confirm_time, '%Y-%m-%d') BETWEEN @first_day_year AND @last_day_year)
    
    UNION 

    SELECT * FROM htc_confirm c
    WHERE 
    c.is_remove = 0
    AND c.results_id = '2'
    AND c.site_id IN (@site_id)
    AND c.source_site_id IN (@source_site_id)
    AND c.service_id IN (@services)
    AND (DATE_FORMAT(early_hiv_date, '%Y-%m-%d') BETWEEN @first_day_year AND @last_day_year)
    
    UNION 

    SELECT * FROM htc_confirm c
    WHERE 
    c.is_remove = 0
    AND c.results_id = '2'
    AND c.site_id IN (@site_id)
    AND c.source_site_id IN (@source_site_id)
    AND c.service_id IN (@services)

    AND (DATE_FORMAT(virus_load_date, '%Y-%m-%d') BETWEEN @first_day_year AND @last_day_year)
) m