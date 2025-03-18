package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.htc.HtcElogExcel;
import com.gms.entity.form.htc.HtcElogForm;
import com.gms.entity.input.HtcElogSearch;
import com.gms.entity.json.Locations;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author pdThang
 */
@Controller(value = "report_htc_elogg")
public class HtcElogController extends BaseController {

    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private SiteService siteService;

    protected HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.TEST_RESULTS);
        parameterTypes.add(ParameterEntity.TEST_RESULTS_CONFIRM);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.SERVICE_TEST);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        if (options == null || options.isEmpty()) {
            return null;
        }

        return options;
    }

    public HashMap<String, HashMap<String, String>> getAllOptions() {

        HashMap<String, HashMap<String, String>> options = new HashMap<>();

        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.REFERENT_SOURCE);

        for (String item : parameterTypes) {
            if (options.get(item) == null) {
                options.put(item, new HashMap<String, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getHivInfoCode() == null || entity.getHivInfoCode().equals("")) {
                continue;
            }
            options.get(entity.getType()).put(entity.getCode(), entity.getElogCode());
        }

        return options;
    }

    public HashMap<String, HashMap<String, String>> getLocations() {
        HashMap<String, HashMap<String, String>> oMap = new HashMap<>();

        Locations data = locationsService.findAll();
        //Tỉnh thành
        oMap.put("provinces", new HashMap<String, String>());
        for (ProvinceEntity provinceEntity : data.getProvince()) {
            oMap.get("provinces").put(provinceEntity.getID(), provinceEntity.getElogCode());
        }
        //Quận huyện
        oMap.put("districts", new HashMap<String, String>());
        for (DistrictEntity districtEntity : data.getDistrict()) {
            oMap.get("districts").put(districtEntity.getID(), districtEntity.getElogCode());
        }
        return oMap;
    }

    public HashMap<String, String> getWards(Set<String> wardIDs) {
        if (wardIDs.isEmpty()) {
            return null;
        }
        HashMap<String, String> wards = new HashMap<>();
        for (String wardID : wardIDs) {
            if (StringUtils.isEmpty(wardID)) {
                continue;
            }
            WardEntity ward = locationsService.findWard(wardID);
            wards.put(ward.getID(), ward.getName());
        }
        return wards;
    }

    @RequestMapping(value = {"/htc-elog-grid/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "advisorye_time_from", required = false) String advisoryeTimeFrom,
            @RequestParam(name = "advisorye_time_to", required = false) String advisoryeTimeTo,
            @RequestParam(name = "service", required = false) String service,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {

        DataPage<HtcVisitEntity> dataPage = getData(advisoryeTimeFrom, advisoryeTimeTo, service, resultID, page, size);

        model.addAttribute("title", "Xuất file excel HTC Elog");
        model.addAttribute("smallTitle", "Tư vấn & xét nghiệm");
        model.addAttribute("options", getOptions());
        model.addAttribute("dataPage", dataPage);

        return "backend/htc_elog/index";
    }

    @GetMapping(value = {"/htc-elog-grid/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "advisorye_time_from", required = false) String advisoryeTimeFrom,
            @RequestParam(name = "advisorye_time_to", required = false) String advisoryeTimeTo,
            @RequestParam(name = "service", required = false) String service,
            @RequestParam(name = "result_id", required = false) String resultID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "99999999") int size) throws Exception {

        LoggedUser currentUser = getCurrentUser();
        DataPage<HtcVisitEntity> dataPage = getData(advisoryeTimeFrom, advisoryeTimeTo, service, resultID, page, size);

        //form search 
        HtcElogSearch search = new HtcElogSearch();
        search.setAdvisoryeTimeFrom(advisoryeTimeFrom);
        search.setAdvisoryeTimeTo(advisoryeTimeTo);
        search.setResultID(resultID);
        search.setService(service);

        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());
        search.setPageIndex(page);
        search.setPageSize(size);

        //form Excel
        HtcElogForm form = new HtcElogForm();
        form.setSearch(search);
        form.setItems(new ArrayList<HtcVisitEntity>());
        if (dataPage.getData() != null) {
            for (HtcVisitEntity htcVisitEntity : dataPage.getData()) {
                if (htcVisitEntity == null) {
                    continue;
                }
                form.getItems().add(htcVisitEntity);
            }
        }

        form.setTitle("Sổ theo dõi xét nghiệm HIV");
        form.setFileName("SoTheoDoiXetNghiemHIV" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteCode(currentUser.getSite().getCode());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setOptions(getAllOptions());
        form.setLocations(getLocations());

        //thời gian 
        List<Long> advisoryeTime = new ArrayList<>();
        if (form.getItems() != null) {
            for (HtcVisitEntity model : form.getItems()) {
                if (model.getAdvisoryeTime() != null && !advisoryeTime.contains(model.getAdvisoryeTime().getTime())) {
                    advisoryeTime.add(model.getAdvisoryeTime().getTime());
                }
            }
            Collections.sort(advisoryeTime);
            if (advisoryeTime.size() >= 1) {
                String start = TextUtils.formatDate(new Date(advisoryeTime.get(0)), FORMATDATE);
                String end = TextUtils.formatDate(new Date(advisoryeTime.get(advisoryeTime.size() - 1)), FORMATDATE);
                form.setAdvisoryeTime(start.equals(end) ? String.format("Ngày tư vấn %s", start) : String.format("Ngày tư vấn từ %s đến %s", start, end));
            }
        }
        Set<String> siteIDs = new HashSet<>();
        Set<String> districIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            districIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getPermanentWardID")));
            districIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getCurrentWardID")));
            siteIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getSiteConfirmTest")));

        }
        form.setWards(getWards(districIDs));

        Set<Long> siteIDss = new HashSet<>();
        for (String siteID : siteIDs) {
            if (siteID == null) {
                continue;
            }
            siteIDss.add(Long.valueOf(siteID));
        }

        HashMap<String, String> siteConfirm = new HashMap<>();
        if (siteIDs.size() > 0) {
            List<SiteEntity> models = siteService.findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), getCurrentUser().getSite().getProvinceID());
            for (SiteEntity model : models) {
                siteConfirm.put(String.valueOf(model.getID()), model.getName());
            }
        }
        form.setConfirmSite(siteConfirm);

        return exportExcel(new HtcElogExcel(form, EXTENSION_EXCEL_X));
    }

    private DataPage<HtcVisitEntity> getData(String advisoryeTimeFrom, String advisoryeTimeTo, String service, String resultID, int page, int size) {

        LoggedUser currentUser = getCurrentUser();
        HtcElogSearch search = new HtcElogSearch();

        //param
        search.setAdvisoryeTimeFrom(advisoryeTimeFrom);
        search.setAdvisoryeTimeTo(advisoryeTimeTo);
        search.setResultID(resultID);
        search.setService(service);

        search.setSiteID(new HashSet<Long>());
        search.getSiteID().add(currentUser.getSite().getID());
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<HtcVisitEntity> dataPage = htcVisitService.findElog(search);

        return dataPage;
    }

}
