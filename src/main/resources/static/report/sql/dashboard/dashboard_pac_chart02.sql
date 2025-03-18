SELECT 
    @est AS est,
    SUM(IF(YEAR(info.confirm_time) <= @year AND (info.death_time IS NULL OR info.death_time = '') , 1,0)) AS hivKnew,
    SUM(IF(info.status_of_treatment_id = '3' AND (info.death_time IS NULL OR 
        (YEAR(info.death_time) >= @year )), 1,0)) AS treatment,
    SUM(IF(YEAR(info.VIRUS_LOAD_ARV_LASTEST_DATE) <= @year AND
            (info.VIRUS_LOAD_ARV_CURRENT <> '4'), 1,0)) AS arv1000,
    SUM(IF(YEAR(info.VIRUS_LOAD_ARV_LASTEST_DATE) <= @year AND
            (info.VIRUS_LOAD_ARV_CURRENT = '1' OR info.VIRUS_LOAD_ARV_CURRENT = '2'), 1,0)) AS arv200
FROM pac_patient_info AS info
WHERE info.is_remove = 0
    AND info.accept_time is not null
    AND info.review_ward_time is not null
    AND info.review_province_time is not null
    AND info.province_id = @province