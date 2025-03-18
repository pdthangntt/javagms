package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.pac.PQMForm;
import com.gms.entity.form.pac.PQMTable01Form;
import com.gms.repository.impl.PQMRepositoryImpl;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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
 * @author pdThang
 */
@Controller(value = "pqm_controller")
public class PqmController extends BaseController {

    @Autowired
    private PQMRepositoryImpl pQMRepositoryImpl;
    @Autowired
    private SiteService siteService;

    @GetMapping(value = {"/pqm/index1.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            RedirectAttributes redirectAttributes) {

        //ngày mặc định
        Date currentDate = new Date();
        from = StringUtils.isEmpty(from) || !isThisDateValid(from) ? TextUtils.formatDate(TextUtils.getFirstDayOfMonth(currentDate), "dd/MM/yyyy") : from;
        to = StringUtils.isEmpty(to) || !isThisDateValid(to) ? TextUtils.formatDate(TextUtils.getLastDayOfMonth(currentDate), "dd/MM/yyyy") : to;

        //TVXN: Bảng 01
        Map<String, String> sites = new HashMap<>();
        List<PQMTable01Form> table01data = pQMRepositoryImpl.getTable01(TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", from), TextUtils.formatDate("dd/MM/yyyy", "yyyy-MM-dd", to));
        List<PQMTable01Form> table01 = new ArrayList<>();
        PQMTable01Form table;
        for (SiteEntity site : siteService.getSiteHtc()) {
            table = new PQMTable01Form();
            table.setSite(site.getName());
            table.setPositive(0);
            table.setRecent(0);
            table.setArt(0);
            for (PQMTable01Form item : table01data) {
                if (item.getSite() == null || !item.getSite().equals(site.getID().toString())) {
                    continue;
                }
                table.setPositive(item.getPositive());
                table.setRecent(item.getRecent());
                table.setArt(item.getArt());
            }
            table01.add(table);

        }

        //Form
        PQMForm form = new PQMForm();
        form.setTitle("Theo dõi sử dụng");
        form.setTable01(table01);
        form.setStartDate(from);
        form.setEndDate(to);

        model.addAttribute("parent_title", "Cấu hình");
        model.addAttribute("title", "Theo dõi sử dụng");

        model.addAttribute("form", form);
        model.addAttribute("sites", sites);

        return "/report/pqm/index";
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
