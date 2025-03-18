package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.input.PqmShiArtSearch;
import com.gms.entity.input.PqmShiMmdSearch;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmArvTestService;
import com.gms.service.PqmArvViralService;
import com.gms.service.PqmArvVisitService;
import com.gms.service.PqmShiArtService;
import com.gms.service.PqmShiMmdService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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
@Controller(value = "pqm-shi-mmd")
public class PqmShiMmdController extends PqmController {

    @Autowired
    private PqmShiMmdService shiMmdService;

    @GetMapping(value = {"/pqm-shi-mmd/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "year", required = false, defaultValue = "0") int year,
            @RequestParam(name = "month", required = false, defaultValue = "0") int month,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmShiMmdSearch search) {
        LoggedUser currentUser = getCurrentUser();
        search.setPageIndex(page);
        search.setPageSize(size);
        Integer currentMonth = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        search.setYear(year > 0 ? year : Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy")));
        search.setMonth(month > 0 ? month : currentMonth);
        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setProvinceID(currentUser.getSite().getProvinceID());
        DataPage<PqmShiMmdEntity> dataPage = shiMmdService.find(search);
        model.addAttribute("title", "A05. Thống kê tình hình kê đơn thuốc ARV");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("currentMonth", currentMonth.toString());
        model.addAttribute("options", getOptions());
        model.addAttribute("sites", sites);
        return "backend/pqm_shi/mmd";
    }

}
