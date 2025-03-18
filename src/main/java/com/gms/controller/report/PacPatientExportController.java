package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.PatientExcel;
import com.gms.entity.excel.pac.PatientExportHivInfoExcel;
import com.gms.entity.form.pac.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
@Controller(value = "report_pac_patient_export_hivinfo")
public class PacPatientExportController extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }

    private static HashMap<String, HashMap<String, String>> sOptions;

    private HashMap<String, HashMap<String, String>> getOptionsHivinfoCode() {
        if (sOptions != null) {
            return sOptions;
        }
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);

        sOptions = new HashMap<>();
        for (String item : parameterTypes) {
            if (sOptions.get(item) == null) {
                sOptions.put(item, new LinkedHashMap<String, String>());
            }
        }
        List<ParameterEntity> entities = parameterService.findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (entity.getHivInfoCode() == null || entity.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(entity.getType()).put(entity.getCode(), entity.getHivInfoCode());
        }

        String key = "province";
        sOptions.put(key, new HashMap<String, String>());
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getID(), item.getHivInfoCode());
        }

        key = "district";
        sOptions.put(key, new HashMap<String, String>());
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            if (item.getHivInfoCode() == null || item.getHivInfoCode().equals("")) {
                continue;
            }
            sOptions.get(key).put(item.getID(), item.getHivInfoCode());
        }

        return sOptions;
    }

    @RequestMapping(value = {"/pac-hivinfo-export/index.html"}, method = RequestMethod.GET)
    public String actionExport(Model model,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "search_time", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size, RedirectAttributes redirectAttributes) {

        if (!isPAC()) {
            redirectAttributes.addFlashAttribute("error", "Cơ sở không có quyền hoặc dịch vụ này");
            return redirect(UrlUtils.backendHome());
        }

        model.addAttribute("title", "Xuất excel HIV Info");
        model.addAttribute("options", getOptions());
        model.addAttribute("sOptions", getOptionsHivinfoCode());
        PacPatientForm form = getData(tab, from, to, page, size);
        form.setDateFilter(dateFilter);
        model.addAttribute("dataPage", form.getDataPage());
        model.addAttribute("form", form);

        return "report/pac/hivinfo_export";
    }

    private PacPatientForm getData(String tab, String from, String to, int page, int size) {
        tab = tab == null ? "" : tab;
        LoggedUser currentUser = getCurrentUser();
        Date currentDate = new Date();
        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        String fromDate = from.isEmpty() ? TextUtils.formatDate(TextUtils.getFirstDayOfQuarter(currentDate), FORMATDATE) : from;
        String toDate = to.isEmpty() ? TextUtils.formatDate(TextUtils.getLastDayOfQuarter(currentDate), FORMATDATE) : to;
        /////form search
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setFrom(fromDate);
        search.setTo(toDate);
        search.setTab("in".equals(tab) ? "1" : "out".equals(tab) ? "2" : "0");

        search.setProvinceID(currentUser.getSite().getProvinceID());
        pIDs.add(currentUser.getSite().getProvinceID());
        if (isTTYT()) {
            search.setPermanentDistrictID(currentUser.getSite().getDistrictID());
            dIDs.add(currentUser.getSite().getDistrictID());
        } else if (isTYT()) {
            search.setPermanentDistrictID(currentUser.getSite().getDistrictID());
            search.setPermanentWardID(currentUser.getSite().getWardID());
            dIDs.add(currentUser.getSite().getDistrictID());
            wIDs.add(currentUser.getSite().getWardID());
        }
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));

        DataPage<PacPatientInfoEntity> dataPage;
        if (tab.equals("new_out_province")) {
            dataPage = pacPatientService.findExportOutProvince(search);
        } else {
            dataPage = pacPatientService.exportPatient(search);
        }


        //Mã HIV Info
        HashMap<String, String> hivCode = new HashMap<>();
        HashMap<String, String> wardOptions = new HashMap<>();
        HashMap<String, String> wardHivInfoCodes = new HashMap<>();
        Set<Long> pacIDs = new HashSet<>();
        Set<String> wardIDs = new HashSet<>();
        if (dataPage.getData() != null && dataPage.getData().size() > 0) {
            pacIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getID")));
            wardIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getPermanentWardID")));
            wardIDs.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getCurrentWardID")));
            for (PacHivInfoEntity hivEntity : pacPatientService.findHivInfoByIDs(pacIDs)) {
                hivCode.put(String.valueOf(hivEntity.getID()), hivEntity.getCode());
            }
            for (WardEntity wardEntity : locationsService.findWardByIDs(wardIDs)) {
                wardOptions.put(wardEntity.getID(), wardEntity.getName());
                wardHivInfoCodes.put(wardEntity.getID(), wardEntity.getHivInfoCode());
            }
        }

        PacPatientForm form = new PacPatientForm();
        form.setTotalNew(pacPatientService.countExportPatient(search));
        form.setTitleLocation(getTitleAddress(pIDs, dIDs, wIDs));
        form.setStartDate(fromDate);
        form.setEndDate(toDate);
        form.setDataPage(dataPage);
        form.setHivCodeOptions(hivCode);
        form.setSearch(search);
        form.setOptions(getOptions());
        form.setsOptions(getOptionsHivinfoCode());
        form.setItems(dataPage.getData());

        form.setTitle("Xuất excel HIV Info");

        String nameTab = "1".equals(search.getTab()) ? "ThuongTruTrongTinh_" : "2".equals(search.getTab()) ? "ThuongTruNgoaiTinh_" : "";
        form.setFileName("XuatExcelHivInfo_" + nameTab + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setDistricts(new HashMap<String, String>());
        for (DistrictEntity districs : locationsService.findAllDistrict()) {
            form.getDistricts().put(districs.getID(), districs.getName());
        }
        form.setProvinces(new HashMap<String, String>());
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            form.getProvinces().put(provinceEntity.getID(), provinceEntity.getName());
        }
        form.setWards(wardOptions);
        form.setWardHivInfoCodes(wardHivInfoCodes);

        List<Long> confirmTime = new ArrayList<>();
        if (form.getItems() != null) {
            for (PacPatientInfoEntity model : form.getItems()) {
                if (model.getConfirmTime() != null && !confirmTime.contains(model.getConfirmTime().getTime())) {
                    confirmTime.add(model.getConfirmTime().getTime());
                }
            }
            Collections.sort(confirmTime);
            if (confirmTime.size() >= 1) {
                String start = TextUtils.formatDate(new Date(confirmTime.get(0)), FORMATDATE);
                String end = TextUtils.formatDate(new Date(confirmTime.get(confirmTime.size() - 1)), FORMATDATE);
                form.setConfirmTime(start.equals(end) ? String.format("Ngày XN khẳng định %s", start) : String.format("Ngày XN khẳng định từ %s đến %s", start, end));
            }
        }

        return form;
    }

    @GetMapping(value = {"/pac-hivinfo-export/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "search_time", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size) throws Exception {

        PacPatientForm form = getData(tab, from, to, page, size);
        return exportExcel(new PatientExportHivInfoExcel(form, EXTENSION_EXCEL_X));
    }
//

    @GetMapping(value = {"/pac-hivinfo-export/email.html"})
    public String actionPositiveEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "from", required = false, defaultValue = "") String from,
            @RequestParam(name = "to", required = false, defaultValue = "") String to,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime,
            @RequestParam(name = "search_time", required = false, defaultValue = "ngayxn") String dateFilter,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "999999999") int size) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.pacExportPatientIndex());
        }
        PacPatientForm form = getData(tab, from, to, page, size);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Xuất HIV info ngày %s", TextUtils.formatDate(new Date(), FORMATDATE)),
                EmailoutboxEntity.TEMPLATE_PAC_EXPORT_HIVINFO,
                context,
                new PatientExportHivInfoExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.pacExportPatientIndex());
    }
}
