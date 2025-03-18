SELECT v_group.gender_id,
       v_group.object_group_id,
       v_group.age_group,
       count(v_group.id) AS quantity
FROM
  (SELECT v_main.*,
          CASE
              WHEN 1=1 THEN CASE
                                WHEN v_main.age < 15 THEN "10-14"
                                WHEN v_main.age >= 15 AND v_main.age <=19 THEN "15-19"
                                WHEN v_main.age >= 20 AND v_main.age <=24 THEN "20-24"
                                WHEN v_main.age >= 25 AND v_main.age <=29 THEN "25-29"
                                WHEN v_main.age >= 30 AND v_main.age <=34 THEN "30-34"
                                WHEN v_main.age >= 35 AND v_main.age <=39 THEN "35-39"
                                WHEN v_main.age >= 40 AND v_main.age <=44 THEN "40-44"
                                WHEN v_main.age >= 45 AND v_main.age <=49 THEN "45-49"
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
                t.arv_site,
                t.arv_id,
                t.gender_id,
                t.object_group_id,
                t.dob,
                DATE_FORMAT(@to_date, '%Y') - DATE_FORMAT(t.dob, '%Y') AS age
         FROM
           (SELECT stage.*,
                   arv.site_id as arv_site, 
                   arv.dob,
                   arv.gender_id,
                   arv.OBJECT_GROUP_ID
            FROM
              (SELECT s.*
               FROM pqm_arv_stage AS s) AS stage
            INNER JOIN pqm_arv AS arv ON stage.arv_id = arv.id) AS t
         WHERE 1=1
           AND t.arv_site IN (@site_id)
           AND t.treatment_time IS NOT NULL
           AND DATE_FORMAT(t.treatment_time, '%Y-%m-%d') <= @to_date
           AND (t.end_time IS NULL
                OR (t.end_time IS NOT NULL
                    AND DATE_FORMAT(t.end_time, '%Y-%m-%d') > @to_date)) ) AS a_main
      GROUP BY a_main.arv_id) AS v_main) AS v_group
GROUP BY v_group.gender_id,
         v_group.object_group_id,
         v_group.age_group