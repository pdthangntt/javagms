/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import static com.gms.controller.report.BaseController.EXTENSION_EXCEL_X;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.excel.opc.OpcPatientExcel;
import com.gms.entity.excel.opc.ViralGridExcel;
import com.gms.entity.excel.opc.ViralSiteExcel;
import com.gms.entity.form.opc_arv.OpcPatientForm;
import com.gms.entity.form.opc_arv.ViralGridForm;
import com.gms.entity.input.OpcArvSearch;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
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
 * Xuất DS QLTLVR
 * @author DSNAnh
 */
@Controller(value = "opc_viral_grid_report")
public class OpcViralGridController  extends OpcController {

    @Autowired
    private OpcViralLoadService viralService;
    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    SiteService siteServices;

    @GetMapping(value = {"/opc-viral-grid/excel.html"})
    public ResponseEntity<InputStreamResource> actionExportExcelGrid(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "page_redirect", required = false) String pageRedirect,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "insurance", required = false) String insurance,
            @RequestParam(name = "insurance_type", required = false) String insuranceType,
            @RequestParam(name = "insurance_no", required = false) String insuranceNo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,
            @RequestParam(name = "registration_time_from", required = false) String registrationTimeFrom,
            @RequestParam(name = "registration_time_to", required = false) String registrationTimeTo,
            @RequestParam(name = "viral_load_time_from", required = false) String viralLoadTimeFrom,
            @RequestParam(name = "viral_load_time_to", required = false) String viralLoadTimeTo,
            @RequestParam(name = "viral_load_retry_from", required = false) String viralLoadRetryFrom,
            @RequestParam(name = "viral_load_retry_to", required = false) String viralLoadRetryTo,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "treatment_stage_time_from", required = false) String treatmentStageTimeFrom,
            @RequestParam(name = "treatment_stage_time_to", required = false) String treatmentStageTimeTo,
            @RequestParam(name = "treatment_time_from", required = false) String treatmentTimeFrom,
            @RequestParam(name = "treatment_time_to", required = false) String treatmentTimeTo,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "treatment_stage_id", required = false) String treatmentStageID,
            @RequestParam(name = "therapy_site_id", required = false) String therapySiteID) throws Exception {
        return exportExcel(new ViralGridExcel(getData(tab, id, pageRedirect, code, name, identityCard, insurance, insuranceType, insuranceNo, permanentProvinceID, permanentDistrictID, permanentWardID, registrationTimeFrom, registrationTimeTo, viralLoadTimeFrom, viralLoadTimeTo, viralLoadRetryFrom, viralLoadRetryTo, resultID,  treatmentStageTimeFrom,  treatmentStageTimeTo, treatmentTimeFrom, treatmentTimeTo, statusOfTreatmentID, treatmentStageID, therapySiteID), EXTENSION_EXCEL_X));
    }

    

    private ViralGridForm getData(String tab, Long id, String pageRedirect, String code, String name, String identityCard,String insurance, String insuranceType, String insuranceNo, String permanentProvinceID,String permanentDistrictID, String permanentWardID, String registrationTimeFrom, String registrationTimeTo,String viralLoadTimeFrom, String viralLoadTimeTo, String viralLoadRetryFrom, String viralLoadRetryTo,String resultID, String treatmentStageTimeFrom, String treatmentStageTimeTo, String treatmentTimeFrom,String treatmentTimeTo, String statusOfTreatmentID, String treatmentStageID, String therapySiteID) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        ViralGridForm form = new ViralGridForm();
        form.setIsOpcManager(isOpcManager());
        OpcArvSearch search = new OpcArvSearch();
        options.get(ParameterEntity.TREATMENT_FACILITY).remove("-1");
        Set<Long> siteIDs = new HashSet<>();
        if (isOpcManager()) {
            if (StringUtils.isEmpty(therapySiteID)) {
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
                siteIDs.add(Long.parseLong(therapySiteID));
            }
        } else {
            siteIDs.add(currentUser.getSite().getID());
        }
        Set<Long> siteIDDefaut = new HashSet<>();
        siteIDDefaut.add(Long.valueOf(-999));
        search.setSiteID(siteIDs.isEmpty() ? siteIDDefaut : siteIDs);

        search.setRemove(false);
        search.setSiteID(siteIDs);
        search.setInsurance(insurance);
        search.setInsuranceType(insuranceType);
        search.setTreatmentStageTimeFrom(isThisDateValid(treatmentStageTimeFrom) ? treatmentStageTimeFrom : null);
        search.setTreatmentStageTimeTo(isThisDateValid(treatmentStageTimeTo) ? treatmentStageTimeTo : null);
        search.setTreatmentStageID(treatmentStageID);
        if (StringUtils.isNotEmpty(code)) {
            search.setCode(code.trim());
        }
        if (StringUtils.isNotEmpty(name)) {
            search.setName(StringUtils.normalizeSpace(name.trim()));
        }
        if (StringUtils.isNotEmpty(identityCard)) {
            search.setIdentityCard(identityCard.trim());
        }
        if (StringUtils.isNotEmpty(insuranceNo)) {
            search.setInsuranceNo(insuranceNo.trim());
        }
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setRegistrationTimeFrom(isThisDateValid(registrationTimeFrom) ? registrationTimeFrom : null);
        search.setRegistrationTimeTo(isThisDateValid(registrationTimeTo) ? registrationTimeTo : null);
        search.setViralTimeForm(isThisDateValid(viralLoadTimeFrom) ? viralLoadTimeFrom : null);
        search.setViralTimeTo(isThisDateValid(viralLoadTimeTo) ? viralLoadTimeTo : null);
        search.setViralRetryForm(isThisDateValid(viralLoadRetryFrom) ? viralLoadRetryFrom : null);
        search.setViralRetryTo(isThisDateValid(viralLoadRetryTo) ? viralLoadRetryTo : null);
        search.setResultID(resultID);
        search.setTreatmentTimeFrom(isThisDateValid(treatmentTimeFrom) ? treatmentTimeFrom : null);
        search.setTreatmentTimeTo(isThisDateValid(treatmentTimeTo) ? treatmentTimeTo : null);
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setPageIndex(1);
        search.setPageSize(9999999);
        search.setTab("all".equals(tab) ? "0" : "1");

        HashMap<String, String> countTimeVirals = new HashMap<>();
        List<Long> IDs = new ArrayList<>();

        DataPage<OpcArvEntity> dataPage = opcArvService.findViral(search);
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            IDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getID")));
        }
        for (Long ID : IDs) {
            countTimeVirals.put(String.valueOf(ID), String.valueOf(viralService.countByArvID(ID)));
        }

        form.setTitle("DANH SÁCH QUẢN LÝ TẢI LƯỢNG VIRUS");
        form.setFileName("DSQLTLVR_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistrictID(currentUser.getSite().getDistrictID());
        form.setWardID(currentUser.getSite().getWardID());
        form.setType(currentUser.getSite().getType());
        if (StringUtils.isNotEmpty(permanentDistrictID)) {
            form.setDistrictName(locationsService.findDistrict(permanentDistrictID).getName());
        }
        if (StringUtils.isNotEmpty(permanentProvinceID)) {
            form.setProvinceName(locationsService.findProvince(permanentProvinceID).getName());
        }
        form.setDataPage(dataPage);
        form.setOptions(options);
        form.setSearch(search);
        form.setItems(dataPage.getData());
        form.setCountTimeVirals(countTimeVirals);
        return form;
    }
    
    /**
     * Check valid date
     *
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
