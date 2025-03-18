package com.gms.controller.report;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.excel.pac.EarlyHivLocalExcel;
import com.gms.entity.form.pac.PacEarlyHivLocalForm;
import com.gms.entity.form.pac.PacLocalTableForm;
import com.gms.entity.form.pac.TablePacEarlyHivForm;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 *
 * @author TrangBN
 */
@Controller(value = "pac_early_hiv")
public class PacEarlyHivController extends BaseController {

    private static final String TEMPLATE_EARLY_HIV_REPORT = "report/pac/earlyhiv-pdf.html";

    @Autowired
    private LocationsService locationsService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private PacPatientRepositoryImpl patientRepositoryImpl;
    @Autowired
    private PacPatientService pacPatientService;

    /**
     * Load all options to display name
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        return parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
    }

    /**
     * Data Form report
     *
     * @param quarter
     * @param year
     * @param serviceIDs
     * @return
     */
    private PacEarlyHivLocalForm getData(String permanentProvinceID, String permanentDistrictID, String permanentWardID, String genderID, String yearOld, String job, String testObject, String risk, String blood, String statusTreatment, String statusResident, String fromTime, String toTime) throws Exception {
        LoggedUser currentUser = getCurrentUser();

        List<String> pIDs = new ArrayList<>();
        List<String> dIDs = new ArrayList<>();
        List<String> wIDs = new ArrayList<>();
        if (isTTYT()) {
            pIDs.add(currentUser.getSite().getProvinceID());
            dIDs.add(currentUser.getSite().getDistrictID());
        } else if (isTYT()) {
            pIDs.add(currentUser.getSite().getProvinceID());
            dIDs.add(currentUser.getSite().getDistrictID());
            wIDs.add(currentUser.getSite().getWardID());
        }
        if (permanentProvinceID != null && !permanentProvinceID.isEmpty() && !permanentProvinceID.equals("null")) {
            pIDs.addAll(Arrays.asList(permanentProvinceID.split(",")));
        }
        if (permanentDistrictID != null && !permanentDistrictID.isEmpty() && !permanentDistrictID.equals("null")) {
            dIDs.addAll(Arrays.asList(permanentDistrictID.split(",")));
        }
        if (permanentWardID != null && !permanentWardID.isEmpty() && !permanentWardID.equals("null")) {
            wIDs.addAll(Arrays.asList(permanentWardID.split(",")));
        }

        Map<String, String> provinces = new LinkedHashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item.getName());
        }
        Map<String, DistrictEntity> districts = new LinkedHashMap<>();
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        
        Map<String, String> yearOlds = new HashMap<>();
        yearOlds.put("", "Tất cả");
        yearOlds.put("1", "Nhỏ hơn 15 tuổi");
        yearOlds.put("2", "Từ 15 đến 25 tuổi");
        yearOlds.put("3", "Từ 26 đến 49 tuổi");
        yearOlds.put("4", "Lớn hơn 50 tuổi");

        PacEarlyHivLocalForm form = new PacEarlyHivLocalForm();
        form.setSerchWard(false);
        if (!permanentWardID.isEmpty()) {
            form.setSerchWard(true);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        PacPatientInfoEntity entity = pacPatientService.getFirst(getCurrentUser().getSite().getProvinceID());
        Date fromTimeConvert = StringUtils.isEmpty(fromTime) ? (entity == null ? null : entity.getConfirmTime()) : sdf.parse(fromTime);
        Date toTimeConvert = StringUtils.isEmpty(toTime) ? new Date() : sdf.parse(toTime);

        form.setIsTYT(isTYT());
        form.setStartDate(TextUtils.formatDate(fromTimeConvert, FORMATDATE));
        form.setEndDate(TextUtils.formatDate(toTimeConvert, FORMATDATE));
        form.setStaffName(currentUser.getUser().getName());
        form.setProvince(currentUser.getSite().getProvinceID());
        form.setSiteAgency(getSiteAgency(currentUser.getSite()));
        form.setSiteName(currentUser.getSite().getName());
        form.setSiteProvince(getProvinceName(currentUser.getSite()));
        form.setTitle("BÁO CÁO SỐ TRƯỜNG HỢP NHIỄM HIV MỚI PHÁT HIỆN");
        form.setProvinceName(currentUser.getSiteProvince().getName());
        form.setDistrictName(currentUser.getSiteDistrict().getName());
        form.setWardName(currentUser.getSiteWard().getName());
        form.setFileName("BaoCaoMoiPhatHien_" + currentUser.getSite().getCode() + "_" + TextUtils.formatDate(new Date(), "hhmm_ddMMyyyy"));
        form.setTitleLocation(getTitleAddress(pIDs, dIDs, wIDs));
        //param excel
        if (genderID != null && !"".equals(genderID)) {
            form.setSearchGender(getOptions().get(ParameterEntity.GENDER).get(genderID));
        }
        if (yearOld != null && !"".equals(yearOld)) {
            form.setSearchAge(yearOlds.get(yearOld));
        }
        if (job != null && !"".equals(job)) {
            form.setSearchJob(getOptions().get(ParameterEntity.JOB).get(job));
        }
        if (testObject != null && !"".equals(testObject)) {
            form.setSearchObject(getOptions().get(ParameterEntity.TEST_OBJECT_GROUP).get(testObject));
        }
        if (risk != null && !"".equals(risk)) {
            form.setSearchRisk(getOptions().get(ParameterEntity.RISK_BEHAVIOR).get(risk));
        }
        if (blood != null && !"".equals(blood)) {
            form.setSearchBloodBase(getOptions().get(ParameterEntity.BLOOD_BASE).get(blood));
        }
        if (statusResident != null && !"".equals(statusResident)) {
            form.setSearchResident(getOptions().get(ParameterEntity.STATUS_OF_RESIDENT).get(statusResident));
        }
        if (statusTreatment != null && !"".equals(statusTreatment)) {
            form.setSearchTreatment(getOptions().get(ParameterEntity.STATUS_OF_TREATMENT).get(statusTreatment));
        }

        List<TablePacEarlyHivForm> result = patientRepositoryImpl.getTableEarlyHiv(pIDs, dIDs, wIDs, genderID, yearOld, job, testObject, risk, blood, statusTreatment, statusResident, fromTimeConvert, toTimeConvert);

        //set default
        form.setTable(new ArrayList<TablePacEarlyHivForm>());
        TablePacEarlyHivForm item;
        List<TablePacEarlyHivForm> wRow;
        int index = 1;
        for (Map.Entry<String, DistrictEntity> entry : districts.entrySet()) {
            DistrictEntity district = entry.getValue();
            if ((!dIDs.isEmpty() && !dIDs.contains(district.getID()))
                    || !pIDs.contains(district.getProvinceID())) {
                continue;
            }

            wRow = new ArrayList<>();
            for (WardEntity wItem : locationsService.findWardByDistrictID(district.getID())) {
                if (!wIDs.isEmpty() && !wIDs.contains(wItem.getID())) {
                    continue;
                }
                item = new TablePacEarlyHivForm();
                item.setProvinceID(district.getProvinceID());
                item.setProvinceName(provinces.getOrDefault(district.getProvinceID(), "/"));
                item.setDistrictID(district.getID());
                item.setDistrictName(district.getName());
                item.setWardID(wItem.getID());
                item.setWardName(wItem.getName());
                wRow.add(item);
            }

            if (wRow.isEmpty()) {
                continue;
            }
            form.setTotalWard(form.getTotalWard() + wRow.size());
            
            item = new TablePacEarlyHivForm();
            item.setStt(index);
            item.setProvinceID(district.getProvinceID());
            item.setProvinceName(provinces.getOrDefault(district.getProvinceID(), "/"));
            item.setDistrictID(district.getID());
            item.setDistrictName(district.getName());
            form.getTable().add(item);
            form.getTable().addAll(wRow);
            index++;
        }

        //map data
        form.setTotalMale(0);
        form.setTotalFemale(0);
        form.setTotalOther(0);
        for (TablePacEarlyHivForm row : form.getTable()) {
            for (TablePacEarlyHivForm line : result) {
                //Cộng huyện
                if (row.getWardID() == null && row.getDistrictID().equals(line.getDistrictID())) {
                    row.setMale(row.getMale() + line.getMale());
                    row.setFemale(row.getFemale() + line.getFemale());
                    row.setOther(row.getOther() + line.getOther());
                    
                    form.setTotalMale(form.getTotalMale()+ line.getMale());
                    form.setTotalFemale(form.getTotalFemale()+ line.getFemale());
                    form.setTotalOther(form.getTotalOther()+ line.getOther());
                } else if (row.getWardID() != null
                        && row.getDistrictID().equals(line.getDistrictID())
                        && row.getWardID().equals(line.getWardID())) {
                    row.setMale(row.getMale() + line.getMale());
                    row.setFemale(row.getFemale() + line.getFemale());
                    row.setOther(row.getOther()+ line.getOther());
                }
            }
        }
        return form;
    }

    /**
     * Hiển thị danh sách người nhiễm mới HIV
     *
     * @auth TrangBN
     * @param model
     * @param permanentDistrictID
     * @param permanentWardID
     * @param genderID
     * @param yearOld
     * @param job
     * @param testObject
     * @param risk
     * @param blood
     * @param statusTreatment
     * @param statusResident
     * @param fromTime
     * @param toTime
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/pac-early-hiv/index.html"})
    public String actionReportEarlyHivIndex(Model model,
            @RequestParam(name = "permanent_district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "year_old", required = false, defaultValue = "") String yearOld,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "risk", required = false, defaultValue = "") String risk,
            @RequestParam(name = "blood", required = false, defaultValue = "") String blood,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        String provinceID = getCurrentUser().getSite().getProvinceID();
        PacEarlyHivLocalForm form = getData(provinceID, permanentDistrictID, permanentWardID, genderID, yearOld, job, testObject, risk, blood, statusTreatment, statusResident, fromTime, toTime);
        model.addAttribute("parent_title", "Quản lý người nhiễm");
        model.addAttribute("title", "Báo cáo số trường hợp nhiễm HIV mới phát hiện");
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());

        Map<String, String> yearOlds = new HashMap<>();
        yearOlds.put("", "Tất cả");
        yearOlds.put("1", "Nhỏ hơn 15 tuổi");
        yearOlds.put("2", "Từ 15 đến 25 tuổi");
        yearOlds.put("3", "Từ 26 đến 49 tuổi");
        yearOlds.put("4", "Lớn hơn 50 tuổi");
        model.addAttribute("yearOlds", yearOlds);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isTYT", isTYT());
        model.addAttribute("isTTYT", isTTYT());

        return "report/pac/earlyhiv";
    }

    @GetMapping(value = {"/pac-early-hiv/pdf.html"})
    public ResponseEntity<InputStreamResource> actionPdf(
            @RequestParam(name = "permanent_district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "year_old", required = false, defaultValue = "") String yearOld,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "risk", required = false, defaultValue = "") String risk,
            @RequestParam(name = "blood", required = false, defaultValue = "") String blood,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        String provinceID = getCurrentUser().getSite().getProvinceID();
        PacEarlyHivLocalForm form = getData(provinceID, permanentDistrictID, permanentWardID, genderID, yearOld, job, testObject, risk, blood, statusTreatment, statusResident, fromTime, toTime);

        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);
        context.put("isTYT", isTYT());
        return exportPdf(form.getFileName(), TEMPLATE_EARLY_HIV_REPORT, context);
    }

    @GetMapping(value = {"/pac-early-hiv/excel.html"})
    public ResponseEntity<InputStreamResource> actionExcel(@RequestParam(name = "permanent_district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "year_old", required = false, defaultValue = "") String yearOld,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "risk", required = false, defaultValue = "") String risk,
            @RequestParam(name = "blood", required = false, defaultValue = "") String blood,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        String provinceID = getCurrentUser().getSite().getProvinceID();
        return exportExcel(new EarlyHivLocalExcel(getData(provinceID, permanentDistrictID, permanentWardID, genderID, yearOld, job, testObject, risk, blood, statusTreatment, statusResident, fromTime, toTime), EXTENSION_EXCEL_X));
    }

    @GetMapping(value = {"/pac-early-hiv/email.html"})
    public String actionPositiveEmail(RedirectAttributes redirectAttributes,
            @RequestParam(name = "permanent_district_id", required = false, defaultValue = "") String permanentDistrictID,
            @RequestParam(name = "permanent_ward_id", required = false, defaultValue = "") String permanentWardID,
            @RequestParam(name = "gender_id", required = false, defaultValue = "") String genderID,
            @RequestParam(name = "year_old", required = false, defaultValue = "") String yearOld,
            @RequestParam(name = "job", required = false, defaultValue = "") String job,
            @RequestParam(name = "test_object", required = false, defaultValue = "") String testObject,
            @RequestParam(name = "risk", required = false, defaultValue = "") String risk,
            @RequestParam(name = "blood", required = false, defaultValue = "") String blood,
            @RequestParam(name = "status_treatment", required = false, defaultValue = "") String statusTreatment,
            @RequestParam(name = "status_resident", required = false, defaultValue = "") String statusResident,
            @RequestParam(name = "from_time", required = false, defaultValue = "") String fromTime,
            @RequestParam(name = "to_time", required = false, defaultValue = "") String toTime) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        if (StringUtils.isEmpty(currentUser.getUser().getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được cấu hình email!");
            return redirect(UrlUtils.deadReport());
        }
        
        String provinceID = getCurrentUser().getSite().getProvinceID();
        PacEarlyHivLocalForm form = getData(provinceID, permanentDistrictID, permanentWardID, genderID, yearOld, job, testObject, risk, blood, statusTreatment, statusResident, fromTime, toTime);
        HashMap<String, Object> context = new HashMap<>();
        context.put("form", form);

        //Gửi email thông báo
        sendEmailAtachmentExcel(
                currentUser.getUser().getEmail(),
                String.format("Báo cáo nhiễm HIV mới phát hiện ( %s)", TextUtils.formatDate(new Date(), "dd/MM/yyyy")),
                EmailoutboxEntity.TEMPLATE_PAC_EARLY_HIV_LOCAL,
                context,
                new EarlyHivLocalExcel(form, EXTENSION_EXCEL_X));

        redirectAttributes.addFlashAttribute("success", "Thông tin sẽ được gửi đến địa chỉ email của bạn trong vài phút! Bạn vui lòng kiểm tra email.");
        return redirect(UrlUtils.earlyHivReport());
    }

}
