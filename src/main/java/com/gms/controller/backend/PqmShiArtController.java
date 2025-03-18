package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.input.PqmShiArtSearch;
import com.gms.service.PqmShiArtService;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdthang
 */
@Controller(value = "pqm-shi-art")
public class PqmShiArtController extends PqmController {
    
    @Autowired
    private PqmShiArtService shiArtService;
    
    @GetMapping(value = {"/pqm-shi-art/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "year", required = false, defaultValue = "") String year,
            @RequestParam(name = "month", required = false, defaultValue = "") String month,
            @RequestParam(name = "sites", required = false, defaultValue = "") String sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmShiArtSearch search) {
        LoggedUser currentUser = getCurrentUser();
        
        int mo = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));
        int m = StringUtils.isEmpty(month) ? mo : Integer.valueOf(month);
        int y = StringUtils.isEmpty(year) ? Calendar.getInstance().get(Calendar.YEAR) : Integer.valueOf(year);
        
        
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setYear(y);
        search.setMonth(m);
        search.setSiteID(StringUtils.isEmpty(sites) ? null : Long.valueOf(sites));
        search.setProvinceID(currentUser.getSite().getProvinceID());
        
        DataPage<PqmShiArtEntity> dataPage = shiArtService.find(search);
        model.addAttribute("title", "A07.2. Theo dõi bệnh nhân nhận thuốc ARV");
        model.addAttribute("parent_title", "Điều trị ARV theo bảo hiểm y tế");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("options", getOptions());
        model.addAttribute("sites", sites);
        model.addAttribute("month", m);
        return "backend/pqm_shi/art";
    }
    
}
