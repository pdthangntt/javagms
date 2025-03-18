SELECT 
mt_dl.drug_name,
 mt_dl.unit,
 sum(mt_dl.`ending`) as ton_cuoi_ky,
(select sum(dl.`patient`) from pqm_drug_plan dl where dl.month =@month_d1 and dl.year =@year_d and dl.site_code =mt_dl.site_code and dl.current_province = mt_dl.current_province and dl.drug_name = mt_dl.drug_name and dl.unit = mt_dl.unit) d1,
(select sum(dl.`patient`) from pqm_drug_plan dl where dl.month =@month_d2 and dl.year =@year_d and dl.site_code =mt_dl.site_code and dl.current_province = mt_dl.current_province and dl.drug_name = mt_dl.drug_name and dl.unit = mt_dl.unit) d2,
(select sum(dl.`patient`) from pqm_drug_plan dl where dl.month =@month_d3 and dl.year =@year_d and dl.site_code =mt_dl.site_code and dl.current_province = mt_dl.current_province and dl.drug_name = mt_dl.drug_name and dl.unit = mt_dl.unit) d3
, mt_dl.site_code, mt_dl.site_name
FROM `pqm_drug_plan` mt_dl
 WHERE 
mt_dl.year=@year 
and mt_dl.month=@month
and mt_dl.current_province = @province_id 
GROUP by mt_dl.site_code,mt_dl.current_province,mt_dl.drug_name,mt_dl.unit