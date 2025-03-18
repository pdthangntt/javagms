package com.gms.controller.backend;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmHubEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmProportionEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmProportionForm;
import com.gms.entity.form.PqmProportionRow;
import com.gms.entity.form.PqmSiteForm;
import com.gms.entity.form.PqmSiteRow;
import com.gms.service.LocationsService;
import com.gms.service.PqmProportionService;
import com.gms.service.SiteService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdThang
 */
@Controller(value = "pqm_protortion")
public class PqmProportionController extends PqmController {

    @Autowired
    protected PqmProportionService proportionService;
    @Autowired
    private LocationsService locationsService;

    @RequestMapping(value = {"/pqm-protortion/index.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            PqmProportionForm form,
            RedirectAttributes redirectAttributes) {

        form.setItems(new LinkedList<PqmProportionRow>());
        LoggedUser currentUser = getCurrentUser();
        Set<String> provinces = new HashSet<>();
        provinces.add(currentUser.getSite().getProvinceID());

        List<PqmProportionEntity> pqmProportions = proportionService.findByProvince(provinces);
        PqmProportionRow row;
        for (PqmProportionEntity proportion : pqmProportions) {
            row = setForm(proportion);
            form.getItems().add(row);
        }
        PqmProportionForm formUpdate = new PqmProportionForm();

        model.addAttribute("title", "Tỷ trọng thuốc");
        model.addAttribute("form", form);
        model.addAttribute("formUpdate", formUpdate);

        return "backend/pqm/protortion";
    }

    private PqmProportionRow setForm(PqmProportionEntity pro) {
        PqmProportionRow form = new PqmProportionRow();
        form.setID(pro.getID());
        form.setCode(pro.getCode());
        form.setName(pro.getName());
        form.setValue(pro.getValue());
        form.setProvince(pro.getProvinceID());
        return form;
    }

//    public static void main(String[] args) {
//        PqmProportionEntity model;
//        model = new PqmProportionEntity();
//        model.setCode("");
//        model.setCode("");
//        proportionService.save(model);
//    }
}
