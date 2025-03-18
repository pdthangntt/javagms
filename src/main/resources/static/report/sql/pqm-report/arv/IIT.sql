SELECT v_group.gender_id,
       v_group.object_group_id,
       v_group.age_group,
       count(v_group.id) AS quantity
FROM
  (SELECT v_main.*,
          CASE
              WHEN 1=1 THEN CASE
                                WHEN v_main.age < 15 THEN "10-14"
                                WHEN v_main.age >= 15
                                     AND v_main.age <=19 THEN "15-19"
                                WHEN v_main.age >= 20
                                     AND v_main.age <=24 THEN "20-24"
                                WHEN v_main.age >= 25
                                     AND v_main.age <=29 THEN "25-29"
                                WHEN v_main.age >= 30
                                     AND v_main.age <=34 THEN "30-34"
                                WHEN v_main.age >= 35
                                     AND v_main.age <=39 THEN "35-39"
                                WHEN v_main.age >= 40
                                     AND v_main.age <=44 THEN "40-44"
                                WHEN v_main.age >= 45
                                     AND v_main.age <=49 THEN "45-49"
                                WHEN v_main.age >= 50 THEN "50+"
                                ELSE "none"
                            END
              ELSE "none"
          END AS age_group
   FROM
     (SELECT a_main.*
      FROM
        (SELECT t.id,
                t.site_id,
                t.arv_id,
                t.gender_id,
                t.object_group_id,
                t.dob,
                DATE_FORMAT(@to_date, '%Y') - DATE_FORMAT(t.dob, '%Y') AS age
         FROM
           (SELECT stage.id,
                   stage.arv_id,
                   stage.site_id,
                   stage.treatment_time,
                   stage.end_time,
                   arv.dob,
                   arv.gender_id,
                   arv.OBJECT_GROUP_ID,
                   visit.examination_time
            FROM
              (SELECT s.id,
                      s.arv_id,
                      s.site_id,
                      s.treatment_time,
                      s.end_time
               FROM pqm_arv_stage AS s
                WHERE s.site_id IN (@site_id)
                AND s.treatment_time IS NOT NULL
                AND (s.end_time IS NULL
                OR (s.end_time IS NOT NULL
                    AND DATE_FORMAT(s.end_time, '%Y-%m-%d') > @from_date))
) AS stage
            INNER JOIN pqm_arv AS arv ON stage.arv_id = arv.id
            INNER JOIN
              (SELECT v.arv_id,
                      v.examination_time
               FROM (
                   SELECT arv_id,
                         examination_time,
                         APPOINMENT_TIME
                  FROM
                    (SELECT v4.arv_id AS arv_id,
                            v4.examination_time AS examination_time,
                            v4.APPOINMENT_TIME AS APPOINMENT_TIME
                     FROM pqm_arv_visit AS v4
                     ORDER BY v4.APPOINMENT_TIME DESC) AS vr
                  GROUP BY vr.arv_id)

 AS v
               WHERE   DATE_FORMAT(v.examination_time, '%Y-%m-%d') < @to_date
           AND (28 < DATEDIFF(@to_date, DATE_FORMAT(v.APPOINMENT_TIME, '%Y-%m-%d')))
           AND (DATEDIFF(@to_date, DATE_FORMAT(v.APPOINMENT_TIME, '%Y-%m-%d')) <= (28 + (DATEDIFF(@to_date, @from_date)))) 
 ) AS visit ON visit.arv_id = stage.arv_id) AS t
--          WHERE 
--            AND t.site_id IN (@site_id)
--            AND t.treatment_time IS NOT NULL
--             (t.end_time IS NULL
--                 OR (t.end_time IS NOT NULL
--                     AND DATE_FORMAT(t.end_time, '%Y-%m-%d') > @to_date))
--            AND DATE_FORMAT(t.examination_time, '%Y-%m-%d') < @to_date
--            AND (28 < DATEDIFF(@to_date, DATE_FORMAT(t.examination_time, '%Y-%m-%d')))
--            AND (DATEDIFF(@to_date, DATE_FORMAT(t.examination_time, '%Y-%m-%d')) <= (28 + (DATEDIFF(@to_date, @from_date)))) 
) AS a_main
      GROUP BY a_main.arv_id) AS v_main) AS v_group
GROUP BY v_group.gender_id,
         v_group.object_group_id,
         v_group.age_group