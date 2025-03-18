package com.gms.controller.backend;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.input.PqmDrugSearch;
import com.gms.service.PqmDrugEstimateService;
import com.gms.service.PqmDrugPlanService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_drug")
public class PqmDrugController extends PqmController {

    @Autowired
    private PqmDrugPlanService drugPlanService;
    @Autowired
    private PqmDrugEstimateService drugEstimateService;

    @GetMapping(value = {"/pqm-drug-plan/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size, PqmDrugSearch search) {

        int m = StringUtils.isEmpty(month) ? Calendar.getInstance().get(Calendar.MONTH) + 1 : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(9999);
        search.setMonth(m);
        search.setYear(y);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugPlanEntity> dataPages = drugPlanService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        if (dataPages.getData() != null && !dataPages.getData().isEmpty()) {
            for (PqmDrugPlanEntity e : dataPages.getData()) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : sites);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setMonth(m);
        search.setYear(y);

        DataPage<PqmDrugPlanEntity> dataPage = drugPlanService.find(search);
        Map<String, String> months = new LinkedHashMap<>();
        Map<String, String> years = new LinkedHashMap<>();

        for (int i = 12; i > 0; i--) {
            months.put(String.valueOf(i), "Tháng " + (i < 10 ? "0" + i : String.valueOf(i)));
        }
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i > 1900; i--) {
            years.put(String.valueOf(i), "Năm " + i);
        }

        model.addAttribute("title", "Tình hình sử dụng thuốc ARV");
        model.addAttribute("parent_title", "Điều trị ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("sites", sites);
        model.addAttribute("months", months);
        model.addAttribute("years", years);
        model.addAttribute("month", m + "");
        model.addAttribute("year", y + "");
        model.addAttribute("options", getOptions());
        return "backend/pqm/drug_plan";
    }

    @GetMapping(value = {"/pqm-drug-estimate/index.html"})
    public String actionIndexEstimate(Model model,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size, PqmDrugSearch search) {

        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);

        search.setSiteID(null);
        search.setPageIndex(page);
        search.setPageSize(9999);
        search.setYear(y);
        search.setProvinceID(getCurrentUser().getSite().getProvinceID());

        DataPage<PqmDrugEstimateEntity> dataPages = drugEstimateService.find(search);
        Map<String, String> siteMap = new LinkedHashMap<>();
        siteMap.put("", "Tất cả");
        if (dataPages.getData() != null && !dataPages.getData().isEmpty()) {
            for (PqmDrugEstimateEntity e : dataPages.getData()) {
                siteMap.put(e.getSiteCode(), e.getSiteName());
            }
        }

        search.setSiteID(StringUtils.isEmpty(sites) ? null : sites);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);

        DataPage<PqmDrugEstimateEntity> dataPage = drugEstimateService.find(search);
        Map<String, String> years = new LinkedHashMap<>();

        for (int i = 1900; i < 2200; i++) {
            years.put(String.valueOf(i), "Năm " + i);
        }

        model.addAttribute("title", "Kế hoạch cung ứng thuốc ARV");
        model.addAttribute("parent_title", "Điều trị ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("sites", sites);
        model.addAttribute("years", years);
        model.addAttribute("year", y + "");
        model.addAttribute("options", getOptions());
        return "backend/pqm/drug_estimate";
    }

    @GetMapping(value = {"/pqm-drug-estimate/update-unit.html"})
    public String actionUpdateUnit(Model model, RedirectAttributes redirectAttributes) {
        try {
//            List<PqmDrugEstimateEntity> items = drugEstimateService.findAllByProvinceID(getCurrentUser().getSite().getProvinceID());
//            if (items != null) {
//                for (PqmDrugEstimateEntity item : items) {
//                    List<PqmDrugPlanEntity> plans = drugPlanService.findUpdateUnitToEstimate(item.getProvinceID(), item.getDrugName(), item.getSiteCode());
//                    
//                    System.out.println("plances " + plans);
//                    try {
//                        item.setUnit(plans.get(0).getUnit());
//                        drugEstimateService.save(item);
//                    } catch (Exception e) {
//                        item.setUnit("Viên");
//                        drugEstimateService.save(item);
//                    }
//
//                }
//            }
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật đơn vị tính từ hình sử dụng thuốc thành công.");
            return redirect("/backend/pqm-drug-estimate/index.html");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thất bại khi cập nhật đơn vị từ tình hình sử dụng thuốc.");
            return redirect("/backend/pqm-drug-estimate/index.html");
        }

    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
