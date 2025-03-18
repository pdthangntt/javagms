package com.gms.controller.backend;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PqmHubEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmSiteForm;
import com.gms.entity.form.PqmSiteRow;
import com.gms.service.LocationsService;
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
@Controller(value = "pqm_site")
public class PqmSiteController extends PqmController {

    @Autowired
    protected SiteService siteService;
    @Autowired
    private LocationsService locationsService;

    @RequestMapping(value = {"/pqm-site/index.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            PqmSiteForm form,
            RedirectAttributes redirectAttributes) {

        form.setItems(new LinkedList<PqmSiteRow>());
        LoggedUser currentUser = getCurrentUser();

        HashMap<String, String> provincePQM = new HashMap<>();
        HashMap<String, String> districtPQM = new HashMap<>();
        for (ProvinceEntity p : locationsService.findAllProvince()) {
            provincePQM.put(p.getID(), p.getPqmCode());
        }
        for (DistrictEntity d : locationsService.findAllDistrict()) {
            districtPQM.put(d.getID(), d.getPqmCode());
        }

        Set<String> services = new HashSet<>();
        Set<String> provinces = new HashSet<>();
        services.add(ServiceEnum.OPC.getKey());
        services.add(ServiceEnum.PREP.getKey());
        services.add(ServiceEnum.HTC.getKey());
        services.add(ServiceEnum.HTC_CONFIRM.getKey());
        provinces.add(currentUser.getSite().getProvinceID());

        List<SiteEntity> sites = siteService.findByServiceAndProvince(services, provinces);
        PqmSiteRow row;
        for (SiteEntity siteEntity : sites) {
            row = setForm(siteEntity);

            row.setDistrict(districtPQM.getOrDefault(row.getDistrict(), null));
            row.setProvince(provincePQM.getOrDefault(row.getProvince(), null));

            SiteEntity entity = siteService.findOne(siteEntity.getID(), true, true);
            String service = "";
            if (entity.getServiceIDs() != null && !entity.getServiceIDs().isEmpty()) {
                if (entity.getServiceIDs().contains(ServiceEnum.HTC.getKey())) {
                    service = service + "Tư vấn & xét nghiệm";
                }
                if (entity.getServiceIDs().contains(ServiceEnum.OPC.getKey())) {
                    service = service + (StringUtils.isEmpty(service) ? "" : ", ") + "Điều trị ARV";
                }
                if (entity.getServiceIDs().contains(ServiceEnum.PREP.getKey())) {
                    service = service + (StringUtils.isEmpty(service) ? "" : ", ") + "Dịch vụ PrEP";
                }
                if (entity.getServiceIDs().contains(ServiceEnum.HTC_CONFIRM.getKey())) {
                    service = service + (StringUtils.isEmpty(service) ? "" : ", ") + "Xét nghiệm khẳng định";
                }
                row.setService(service);

            }

            form.getItems().add(row);
        }
        PqmSiteForm formUpdate = new PqmSiteForm();

        model.addAttribute("title", "Cơ sở PQM");
        model.addAttribute("form", form);
        model.addAttribute("formUpdate", formUpdate);
        model.addAttribute("getHubOptions", getHubOptions());

        return "backend/pqm/sites";
    }

    private PqmSiteRow setForm(SiteEntity site) {
        PqmSiteRow form = new PqmSiteRow();
        form.setID(site.getID());
        form.setName(site.getName());
        form.setHub(site.getHub());
        form.setHubSiteCode(site.getHubSiteCode());
        form.setPqmSiteCode(site.getPqmSiteCode());
        form.setDistrict(site.getDistrictID());
        form.setProvince(site.getProvinceID());

        form.setElogSiteCode(site.getElogSiteCode());
        form.setPrepSiteCode(site.getPrepSiteCode());
        form.setHmedSiteCode(site.getHmedSiteCode());
        form.setArvSiteCode(site.getArvSiteCode());
        return form;
    }

    private HashMap<String, String> getHubOptions() {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("", "--Chọn cấu hình-- ");
        map.put(PqmHubEnum.NHAP_DU_LIEU_THU_CAP.getKey(), PqmHubEnum.NHAP_DU_LIEU_THU_CAP.getLabel());
        map.put(PqmHubEnum.IMPORT_DU_LIEU.getKey(), PqmHubEnum.IMPORT_DU_LIEU.getLabel());
        map.put(PqmHubEnum.FROM_IMS.getKey(), PqmHubEnum.FROM_IMS.getLabel());
        return map;
    }

}
