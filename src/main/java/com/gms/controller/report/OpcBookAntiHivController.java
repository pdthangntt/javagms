package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.pac.DeadExcel;
import com.gms.entity.form.opc_arv.OpcArvBookForm;
import com.gms.entity.form.opc_arv.OpcTreatmentFluctuationsForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Sổ điều trị thuốc kháng HIV
 *
 * @author vvthành
 */
@Controller(value = "opc_book_anti_hiv_report")
public class OpcBookAntiHivController extends OpcController {

    private static final String TEMPLATE_REPORT = "report/opc/opc-book-pre-arv-pdf.html";
    protected static final String PDF_FILE_NAME = "DS bien dong dieu tri_";
    
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    SiteService siteService;

    @GetMapping(value = {"/opc-book-khang-hiv/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "site_id", required = false) String siteID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        OpcArvBookForm form = getData(siteID, treatmentTimeFrom, treatmentTimeTo, page, size);
        model.addAttribute("parent_title", "Điều trị ngoại trú");
        model.addAttribute("title", "Danh sách biến động điều trị");
        model.addAttribute("small_title", "DS biến động điều trị");
        model.addAttribute("isOpcManager", isOpcManager());
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);
        model.addAttribute("siteID", siteID);
        return "report/opc/opc-book-arv";
    }

    @GetMapping(value = {"/opc-book-khang-hiv/email.html"})
    public String actionSendEmail(Model model, RedirectAttributes redirectAttributes) {
        //Nhớ đổi url chuyển về
        return redirect(UrlUtils.pacDead());
    }

    @GetMapping(value = {"/opc-book-khang-hiv/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcel() throws Exception {
        return exportExcel(new DeadExcel(null, EXTENSION_EXCEL_X));
    }
    
    private OpcArvBookForm getData(String siteID, String treatmentTimeFrom, String treatmentTimeTo, int page, int size) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        OpcArvBookForm form = new OpcArvBookForm();
        
        Set<Long> siteIDs = new HashSet<>();
        if (isOpcManager()) {
            if (StringUtils.isEmpty(siteID)) {
                HashMap<String, String> siteList = options.get(ParameterEntity.TREATMENT_FACILITY);
                if (siteList != null && siteList.size() > 0) {
                    for (Map.Entry<String, String> entry : siteList.entrySet()) {
                        String key = entry.getKey();
                        if (StringUtils.isEmpty(key)) {
                            continue;
                        }
                        siteIDs.add(Long.parseLong(key));
                    }
                }
            } else {
                siteIDs.add(Long.parseLong(siteID));
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        
        OpcArvSearch search = new OpcArvSearch();
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        
        DataPage<OpcArvEntity> dataPage = opcArvService.findArvBook(search);
        
        search.setPageIndex(1);
        search.setPageSize(99999999);
        List<OpcArvEntity> list = opcArvService.findArvBook(search).getData();
        if(list != null && list.size() > 0){
            form.setStartDate(TextUtils.formatDate(list.get(list.size() - 1).getTreatmentTime(), FORMATDATE));
        }
        
        form.setTitle("SỔ ĐIỀU TRỊ BẰNG THUỐC KHÁNG HIV");
        form.setFileName("SoARV_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        form.setIsOpcManager(isOpcManager());
         
        form.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        form.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        form.setDataPage(dataPage);
        form.setOptions(options);
        form.setSearch(search);
        
        List<Integer> order = new LinkedList<>();
        List<OpcArvEntity> entities = new LinkedList<>();
        for (OpcArvEntity item : form.getDataPage().getData()) {
            entities.add(item);
            entities.add(new OpcArvEntity());
        }
        int count = 0;
        for (OpcArvEntity item : entities) {
            if (item.getID() != null) {
                count++;
                order.add(count);
            } else {
                order.add(count);
            }
        }
        form.getDataPage().setData(entities);
        form.setItems(dataPage.getData());
        form.setOrder(order);
        
        return form;
    }

    /**
     * Check valid date
     * @param dateToValidate
     * @return
     */
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
