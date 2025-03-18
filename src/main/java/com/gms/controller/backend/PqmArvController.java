package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvTestEntity;
import com.gms.entity.db.PqmArvViralLoadEntity;
import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmArvTestService;
import com.gms.service.PqmArvViralService;
import com.gms.service.PqmArvVisitService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_arv")
public class PqmArvController extends PqmController {

    @Autowired
    private PqmArvService arvService;
    @Autowired
    private PqmArvStageService stageService;
    @Autowired
    private PqmArvVisitService visitService;
    @Autowired
    private PqmArvViralService viralService;
    @Autowired
    private PqmArvTestService testService;

    @RequestMapping(value = {"/pqm-arv/view.html"}, method = RequestMethod.GET)
    public String actionStage(Model model,
            @RequestParam(name = "oid") Long arvID,
            @RequestParam(name = "tab", defaultValue = "", required = false) String tab,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PqmArvEntity arv = arvService.findOne(arvID);
        if (arv == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bệnh án");
            return redirect(UrlUtils.pqmArv());
        }
        List<PqmArvStageEntity> stage = null;
        List<PqmArvVisitEntity> visit = null;
        List<PqmArvViralLoadEntity> viral = null;
        List<PqmArvTestEntity> test = null;
        if (tab.equals("stage")) {
            stage = stageService.findByArvID(arvID);
        } else if (tab.equals("visit")) {
            visit = visitService.findByArvID(arvID);
        } else if (tab.equals("viral")) {
            viral = viralService.findByArvID(arvID);
        } else if (tab.equals("test")) {
            test = testService.findByArvID(arvID);
        }
        model.addAttribute("stage", stage);
        model.addAttribute("visit", visit);
        model.addAttribute("viral", viral);
        model.addAttribute("test", test);
        model.addAttribute("arvCode", arv.getCode());
        model.addAttribute("title", "Bệnh án");
        model.addAttribute("parent_title", tab.equals("stage") ? "Giai đoạn điều trị"
                : tab.equals("visit") ? "Lượt khám"
                : tab.equals("viral") ? "Tải lượng virus"
                : tab.equals("test") ? "Điều trị dự phòng Lao" : "Xem bệnh án");
        model.addAttribute("options", getOptions());
        model.addAttribute("tab", tab);
        model.addAttribute("arvID", arvID);
        return "backend/pqm/arv_view";
    }

    @GetMapping(value = {"/pqm-arv/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "genderID", required = false) String genderID,
            @RequestParam(name = "objectGroupID", required = false) String objectGroupID,
            @RequestParam(name = "statusOfTreatmentID", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatmentTimeFrom", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatmentTimeTo", required = false) String treatmentTimeTo,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmArvSearch search) {
        LoggedUser currentUser = getCurrentUser();

        List<String> siteList = sites.equals("") ? null : new ArrayList<>(Arrays.asList(sites.split(",", -1)));
        Set<Long> siteIDs = new HashSet<>();
        if (isPAC()) {
            if (siteList != null) {
                for (String s : siteList) {
                    if (!StringUtils.isEmpty(s)) {
                        siteIDs.add(Long.valueOf(s));
                    }
                }
            } else {
                for (Map.Entry<String, String> entry : getOptions().get("siteOpc").entrySet()) {
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

        search.setSiteID(siteIDs.isEmpty() ? null : siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setKeyword(StringUtils.isEmpty(keyword) ? null : keyword);
        search.setGenderID(StringUtils.isEmpty(genderID) ? null : genderID);
        search.setObjectGroupID(StringUtils.isEmpty(objectGroupID) ? null : objectGroupID);
        search.setStatusOfTreatmentID(StringUtils.isEmpty(statusOfTreatmentID) ? null : statusOfTreatmentID);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, treatmentTimeFrom) : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, treatmentTimeTo) : null);

        DataPage<PqmArvEntity> dataPage = arvService.find(search);

        model.addAttribute("title", "Khách hàng điều trị");
        model.addAttribute("parent_title", "Điều trị ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("options", getOptions());
        return "backend/pqm/arv";
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
