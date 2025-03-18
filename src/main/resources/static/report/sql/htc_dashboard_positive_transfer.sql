select
  CONCAT('0',v.time) as time,
  SUM(v.c_duong_tinh) as c_duong_tinh,
  SUM(v.c_chuyen_gui) as c_chuyen_gui
  FROM (SELECT
    v_main.t as time,
    count(v_main.id) as c_duong_tinh,
    0 as c_chuyen_gui
  FROM (SELECT
        CONCAT(QUARTER(htc.confirm_time), '/', YEAR(htc.confirm_time)) AS t,
        htc.id as id
  FROM htc_visit as htc WHERE htc.is_remove = 0 AND htc.confirm_results_id = 2
        AND htc.site_id = :site_id
        AND htc.service_id IN (:services)
        AND htc.confirm_time BETWEEN @from_time AND @to_time
  ORDER BY htc.confirm_time ASC) as v_main GROUP BY v_main.t

UNION ALL

SELECT
  v_main.t as time,
  0 as c_duong_tinh,
  count(v_main.id) as c_chuyen_gui
FROM (SELECT
        CONCAT(QUARTER(htc.register_therapy_time), '/', YEAR(htc.register_therapy_time)) AS t,
        htc.id as id
  FROM htc_visit as htc WHERE htc.is_remove = 0 AND htc.confirm_results_id = 2
        AND htc.arrival_site is not null AND htc.therapy_no is not null
        AND htc.site_id = :site_id
        AND htc.service_id IN (:services)
        AND htc.register_therapy_time BETWEEN @from_time AND @to_time
  ORDER BY htc.confirm_time ASC) as v_main GROUP BY v_main.t) as v GROUP BY v.time
