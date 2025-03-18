package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.PqmApiTokenEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.input.PqmApiTokenSearch;
import com.gms.service.PqmApiTokenService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "pqm_api_token")
public class PqmApiTokenController extends PqmController {

    @Autowired
    private PqmApiTokenService service;

    @GetMapping(value = {"/pqm-api-token/index.html"})
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "sites", required = false, defaultValue = "") Long sites,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size, PqmApiTokenSearch search,
            PqmApiTokenEntity entity
    ) {
        LoggedUser currentUser = getCurrentUser();

        search.setSiteID(sites);
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<PqmApiTokenEntity> dataPage = service.find(search);
        model.addAttribute("title", "Quản lý Token API");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("sites", sites);
        model.addAttribute("options", getOptions());

        Map<Long, String> siteMaps = new HashMap<>();
        siteMaps.put(null, "Tất cả");
        for (SiteEntity site : siteService.findAll()) {
            siteMaps.put(site.getID(), site.getName());
        }
        model.addAttribute("siteMaps", siteMaps);
        model.addAttribute("form", entity);

        return "backend/pqm/api_token";
    }

    @PostMapping(value = {"/pqm-api-token/edit.html"})
    public String doActionUpdate(Model model,
            @ModelAttribute("form") @Valid PqmApiTokenEntity form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        form.setKeyToken(UUID.randomUUID().toString());
        form.setSecret(UUID.randomUUID().toString());

        service.save(form);
        redirectAttributes.addFlashAttribute("success", "Thêm mới Token thành công!");
        return redirect(UrlUtils.pqmApiToken());
    }
    
    @GetMapping(value = {"/pqm-api-token/remove.html"})
    public String actionRemove(Model model,
            @RequestParam(name = "oid", required = false, defaultValue = "") String ID,
            RedirectAttributes redirectAttributes) {
        service.delete(Long.parseLong(ID));
        redirectAttributes.addFlashAttribute("success", "Xóa Token thành công!");
        return redirect(UrlUtils.pqmApiToken()); 
    }

}
