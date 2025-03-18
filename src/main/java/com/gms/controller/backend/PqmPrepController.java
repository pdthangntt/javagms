package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepStageEntity;
import com.gms.entity.db.PqmPrepVisitEntity;
import com.gms.entity.input.PqmPrepSearch;
import com.gms.service.PqmPrepService;
import com.gms.service.PqmPrepStageService;
import com.gms.service.PqmPrepVisitService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Controller(value = "pqm_prep")
public class PqmPrepController extends PqmController {

    @Autowired
    private PqmPrepService prepService;
    @Autowired
    private PqmPrepStageService stageService;
    @Autowired
    private PqmPrepVisitService visitService;

    @GetMapping(value = {"/pqm-prep/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "startTimeFrom", required = false) String startTimeFrom,
            @RequestParam(name = "startTimeTo", required = false) String startTimeTo,
            @RequestParam(name = "examinationTimeFrom", required = false) String examinationTimeFrom,
            @RequestParam(name = "examinationTimeTo", required = false) String examinationTimeTo,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmPrepSearch search) {
        LoggedUser currentUser = getCurrentUser();
        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        System.out.println("xx " + isPAC());
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("sitePrEP").entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        siteIDs.add(Long.valueOf(key));
                    }
                }
                if (siteIDs.isEmpty()) {
                    siteIDs.add(Long.valueOf("-2"));
                }

            }
        } else {
            siteIDs.add(getCurrentUser().getSite().getID());
        }

        search.setSiteID(siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setType(StringUtils.isEmpty(type) ? null : type);
        search.setExaminationTimeFrom(isThisDateValid(examinationTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, examinationTimeFrom) : null);
        search.setExaminationTimeTo(isThisDateValid(examinationTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, examinationTimeTo) : null);
        search.setStartTimeFrom(isThisDateValid(startTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, startTimeFrom) : null);
        search.setStartTimeTo(isThisDateValid(startTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, startTimeTo) : null);

        DataPage<PqmPrepEntity> dataPage = prepService.find(search);
        model.addAttribute("title", "Khách hàng PrEP");
        model.addAttribute("parent_title", "Tư vấn & xét nghiệm");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("options", getOptions());
        return "backend/pqm/prep";
    }

    @GetMapping(value = {"/pqm-prep/update-info.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        Set<Long> siteIDs = new HashSet<>();
        Set<Long> prepIDs = new HashSet<>();
        for (Map.Entry<String, String> entry : getOptions().get("sitePrEP").entrySet()) {
            String key = entry.getKey();
            if (StringUtils.isNotEmpty(key)) {
                siteIDs.add(Long.valueOf(key));
            }
        }
        List<PqmPrepEntity> preps = prepService.findBySiteIDs(siteIDs);
        if (preps != null) {
            for (PqmPrepEntity prep : preps) {
                prepIDs.add(prep.getID());
            }
        }
        try {
            List<PqmPrepStageEntity> stages = stageService.findByPrepIDs(prepIDs);
            if (stages != null) {
                for (PqmPrepStageEntity stage : stages) {
                    stageService.save(stage);
                }

            }
            List<PqmPrepVisitEntity> visits = visitService.findByPrepIDs(prepIDs);
            if (visits != null) {
                for (PqmPrepVisitEntity visit : visits) {
                    visitService.save(visit);
                }

            }
            redirectAttributes.addFlashAttribute("success", "Thành công khi cập nhật thông tin mới nhất từ giai đoạn và lượt khám");
            return redirect(UrlUtils.pqmPrep());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi khi cập nhật thông tin mới nhất từ giai đoạn và lượt khám");
            return redirect(UrlUtils.pqmPrep());
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
