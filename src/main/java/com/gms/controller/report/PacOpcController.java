package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ConnectStatusEnum;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.OpcExcel;
import com.gms.entity.excel.pac.PatientExcel;
import com.gms.entity.form.pac.PacOpcForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdThang
 */
@Controller(value = "report_pac_opc")
public class PacOpcController extends BaseController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientRepositoryImpl pacPatientImpl;

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
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());

        HashMap<String, String> provinces = new LinkedHashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);
        HashMap<String, String> districts = new LinkedHashMap<>();
        for (DistrictEntity entity : locationsService.findAllDistrict()) {
            districts.put(entity.getID(), entity.getName());
        }
        options.put("districts", districts);
        HashMap<String, String> wards = new LinkedHashMap<>();
        for (WardEntity entity : locationsService.findAllWard()) {
            wards.put(entity.getID(), entity.getName());
        }
        options.put("wards", wards);
//        for (SiteEntity siteEntity : siteService.getSiteConfirm(getCurrentUser().getSite().getProvinceID())) {
//            options.get(ParameterEntity.PLACE_TEST).put(siteEntity.getID().toString(), siteEntity.getName());
//        }
        

        return options;
    }

    @GetMapping(value = {"/pac-opc/excel.html"})
    public ResponseEntity<InputStreamResource> actionPositiveExcel(
            @RequestParam(name = "tab", required = false, defaultValue = "not_connected") String tab,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "full_name", required = false, defaultValue = "") String fullname,
            @RequestParam(name = "year_of_birth", required = false, defaultValue = "") String yearOfBirth,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "identity_card", required = false, defaultValue = "") String identityCard,
            @RequestParam(name = "health_insurance_no", required = false, defaultValue = "") String healthInsuranceNo,
            @RequestParam(name = "start_treatment_time", required = false) String startTreatmentTime,
            @RequestParam(name = "end_treatment_time", required = false) String endTreatmentTime,
            @RequestParam(name = "opc_status", required = false) String opcStatus,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,
            @RequestParam(name = "facility", required = false) String facility, // Nơi điều trị
            @RequestParam(name = "opc_code", required = false) String opcCode, // Nơi điều trị
            @RequestParam(name = "confirm_code", required = false) String confirmCode, // Nơi điều trị
            @RequestParam(name = "status", required = false) String status // trạng thai dieu tri
    ) throws Exception {

        boolean vaac = isVAAC();

        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(1);
        search.setPageSize(999999999);
        search.setRemove(false);
        if (isPAC()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
        }
        if (isTTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
        }
        if (isTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
            search.setWardID(currentUser.getSite().getWardID());
        }
        search.setSourceSiteID(currentUser.getSite().getProvinceID());
        search.setAddressFilter(addressFilter);
        search.setIsVAAC(vaac);
        search.setStatus(status);
        switch (tab) {
            case "connected":
                search.setConnectStatus(ConnectStatusEnum.CONNECT.getKey());
                break;
            case "not_connected":
                search.setConnectStatus(ConnectStatusEnum.NOT_CONNECT.getKey());
                break;
            case "remove":
                search.setConnectStatus("0");
                search.setRemove(true);
                break;
        }
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (yearOfBirth != null && !yearOfBirth.equals("")) {
            search.setYearOfBirth(Integer.parseInt(yearOfBirth));
        }
        if (opcCode != null && !opcCode.equals("")) {
            search.setOpcCode(StringUtils.normalizeSpace(opcCode.trim()));
        }
        if (confirmCode != null && !confirmCode.equals("")) {
            search.setConfirmCode(StringUtils.normalizeSpace(confirmCode.trim()));
        }
        search.setGenderID(genderID);
        search.setIdentityCard(identityCard.trim());
        search.setHealthInsuranceNo(healthInsuranceNo.trim());
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setStartTreatmentTime(startTreatmentTime);
        search.setEndTreatmentTime(endTreatmentTime);
        search.setOpcStatus(opcStatus);
        search.setFacility(facility);
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findChange(search);

        Map<String, String> pacPatientConnected = pacPatientImpl.getPacPatientConnected();

        //Mã HIV Info
        Set<Long> ids = new HashSet<>();
        Map<String, String> hivCode = new HashMap<>();
        Map<Long, String> parentID = new HashMap<>();
        ids.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getParentID")));

        for (PacPatientInfoEntity item : dataPage.getData()) {
            if (pacPatientConnected.containsKey(item.getParentID() + "")) {
                parentID.put(item.getParentID(), item.getID().toString());
                System.out.println(item.getID() + " yy " + item.getParentID().toString());
            }
        }

        List<PacHivInfoEntity> hivInfos = pacPatientService.findHivInfoByIDs(ids);
        if (hivInfos != null && !hivInfos.isEmpty()) {
            for (PacHivInfoEntity hivInfo : hivInfos) {
                hivCode.put(parentID.getOrDefault(hivInfo.getID(), "x"), hivInfo.getCode());
                System.out.println(hivInfo.getID() + " aa " + hivInfo.getCode());
                System.out.println(parentID.getOrDefault(hivInfo.getID(), "x") + " xx " + hivInfo.getCode());
            }
        }

        PacOpcForm form = new PacOpcForm();
        form.setOptions(options);
        form.setDataPage(dataPage);
        form.setTitle("Danh sách biến động điều trị");
        form.setFileName("DanhSachBDDT_" + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setSiteName(currentUser.getSite().getName());
        form.setStaffName(currentUser.getUser().getName());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setConnected(pacPatientConnected);
        form.setVaac(isVAAC());
        form.setHivCodeOptions(hivCode);

        return exportExcel(new OpcExcel(form, EXTENSION_EXCEL_X));
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
