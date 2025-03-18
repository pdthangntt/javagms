/**
 * Author:  NamAnh_HaUI
 * Created: Nov 1, 2019
 */
select
  CONCAT('0',v.time) as time,
  SUM(v.c_duong_tinh) as c_duong_tinh,
  SUM(v.c_chuyen_gui) as c_chuyen_gui
FROM (SELECT
          v_main.t as time,
          count(v_main.id) as c_duong_tinh,
          0 as c_chuyen_gui
      FROM (SELECT
              CONCAT(QUARTER(h.confirm_time), '/', YEAR(h.confirm_time)) AS t,
              h.id as id
      FROM htc_laytest as h WHERE h.is_remove = 0 AND h.confirm_results_id = 2
              AND h.site_id = :site_id
              AND h.created_by = :staff_id
              AND h.confirm_time BETWEEN @from_time AND @to_time
      ORDER BY h.confirm_time ASC) as v_main GROUP BY v_main.t

      UNION ALL

      SELECT
        v_main.t as time,
        0 as c_duong_tinh,
        count(v_main.id) as c_chuyen_gui
      FROM (SELECT
              CONCAT(QUARTER(h.exchange_time), '/', YEAR(h.exchange_time)) AS t,
              h.id as id
        FROM htc_laytest as h WHERE h.is_remove = 0 
              AND h.confirm_results_id = 2 
              AND h.exchange_status = 1
              AND h.site_id = :site_id 
              AND h.created_by = :staff_id 
              AND h.exchange_time BETWEEN @from_time AND @to_time 
        ORDER BY h.exchange_time ASC) 
    as v_main GROUP BY v_main.t) 
as v GROUP BY v.time

