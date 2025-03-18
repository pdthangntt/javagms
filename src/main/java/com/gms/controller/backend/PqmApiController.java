package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.PqmApiLogEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PqmApiSearch;
import com.gms.entity.validate.PacPatientValidate;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import com.gms.service.PqmApiService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Giám sát phát hiện -> Người nhiễm quản lý
 *
 * @author Văn Thành
 */
@Controller(value = "pqm_api_log")
public class PqmApiController extends PacController {

    @Autowired
    private PqmApiService pqmApiService;
    @Autowired
    private LocationsService locationsService;

    @RequestMapping(value = {"/pqm-api-log/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {

        PqmApiSearch search = new PqmApiSearch();

        from = isThisDateValid(from) ? from : null;
        to = isThisDateValid(to) ? to : null;

        search.setFrom(from);
        search.setTo(to);
        search.setProvince(getCurrentUser().getSite().getProvinceID());

        search.setPageIndex(page);
        search.setPageSize(size);
//        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));

        DataPage<PqmApiLogEntity> dataPage = pqmApiService.find(search);

        Map<String, String> provinces = new HashMap<>();
        provinces.put("", "Chọn tỉnh thành");
        provinces.put("82", "Tiền Giang");
        provinces.put("72", "Tây Ninh");
        provinces.put("75", "Đồng Nai");

        model.addAttribute("title", "Log API");
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("provinces", provinces);
        model.addAttribute("options", getOptions());
        return "backend/pqm/api_log";
    }

    @RequestMapping(value = {"/pqm-link/index.html"}, method = RequestMethod.GET)
    public String actionLinkHub(Model model, RedirectAttributes redirectAttributes) {
        String province = getCurrentUser().getSite().getProvinceID();
        String link = "";
        switch (province) {
            case "72":
                link = getOptions().get(ParameterEntity.LINK_PQM).get("tay-ninh");
                break;
            case "75":
                link = getOptions().get(ParameterEntity.LINK_PQM).get("dong-nai");
                break;
            case "82":
                link = getOptions().get(ParameterEntity.LINK_PQM).get("tien-giang");
                break;
            default:
                break;
        }
        return "redirect:" + link;
    
    }

    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = super.getOptions();
        HashMap<String, String> provinces = new HashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        return options;
    }

    // Validate date
    private boolean isThisDateValid(String dateToValidate) {

        if (StringUtils.isEmpty(dateToValidate)) {
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
