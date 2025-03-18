package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.entity.validate.PacPatientValidate;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
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
@Controller(value = "backend_pac_patient")
public class PacPatientController extends PacController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private PacPatientRepositoryImpl pacPatientImpl;
    @Autowired
    private PacPatientValidate pacPatientValidate;
    @Autowired
    private PacPatientHistoryService patientHistoryService;

    @RequestMapping(value = {"/pac-patient/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "hiv_id", required = false) String hivID,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "request_death_time_from", required = false, defaultValue = "") String requestDeathTimeFrom,
            @RequestParam(name = "request_death_time_to", required = false, defaultValue = "") String requestDeathTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "status_of_resident_id", required = false) String statusOfResidentID,
            @RequestParam(name = "status_of_treatment_id", required = false) String statusOfTreatmentID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        boolean vaac = isVAAC();

        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
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
        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }

        if (healthInsuranceNo != null && !"".equals(healthInsuranceNo)) {
            search.setHealthInsuranceNo(StringUtils.normalizeSpace(healthInsuranceNo.trim()));
        }
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }

        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }

        try {
            search.setHivID(StringUtils.isBlank(hivID) ? Long.valueOf("0") : (pacPatientService.findHivInfoByCode(hivID).getID() == null ? Long.valueOf("0") : pacPatientService.findHivInfoByCode(hivID).getID()));
        } catch (Exception e) {
            search.setHivID(Long.valueOf("-99"));
        }
        search.setAddressFilter(addressFilter);
        search.setIsVAAC(vaac);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        confirmTimeFrom = TextUtils.validDate(confirmTimeFrom) ? confirmTimeFrom : null;
        confirmTimeTo = TextUtils.validDate(confirmTimeTo) ? confirmTimeTo : null;
        requestDeathTimeFrom = TextUtils.validDate(requestDeathTimeFrom) ? requestDeathTimeFrom : null;
        requestDeathTimeTo = TextUtils.validDate(requestDeathTimeTo) ? requestDeathTimeTo : null;
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setRequestDeathTimeFrom(requestDeathTimeFrom);
        search.setRequestDeathTimeTo(requestDeathTimeTo);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        List<String> lsStatusOfRD = new ArrayList<String>();
        if (statusOfResidentID != null && !statusOfResidentID.isEmpty() && !statusOfResidentID.equals("null")) {
            lsStatusOfRD.addAll(Arrays.asList(statusOfResidentID.split(",")));
        }
//        search.setStatusOfResidentID(statusOfResidentID);
        search.setStatusOfTreatmentID(statusOfTreatmentID);
        search.setStatusOfResidentIDs(lsStatusOfRD);
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);

        if ("new".equals(tab)) {
            search.setEarlyHiv("8");
        }
        if ("update".equals(tab)) {
            search.setHasRequest(1);
        }
        if ("alive".equals(tab)) {
            search.setDead(1);
        }
        if ("dead".equals(tab)) {
            search.setDead(2);
        }
        if ("review".equals(tab)) {
            search.setHasReview(1);
        }
        if ("other".equals(tab)) {
            search.setOther(1);
        }
        if (isPAC()) {
            if (tab.equals(PacTabEnum.DELETE.getKey())) {
                search.setRemove(true);
            }
        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findPatient(search);
        Map<String, String> pacPatientConnected = pacPatientImpl.getPacPatientConnected();

        Set<Long> ids = new HashSet<>();
        Map<String, String> hivCode = new HashMap<>();
        ids.addAll(CollectionUtils.collect(dataPage.getData(), TransformerUtils.invokerTransformer("getID")));

        List<PacHivInfoEntity> hivInfos = pacPatientService.findHivInfoByIDs(ids);
        if (hivInfos != null && !hivInfos.isEmpty()) {
            for (PacHivInfoEntity hivInfo : hivInfos) {
                hivCode.put(hivInfo.getID().toString(), hivInfo.getCode());
            }
        }

        model.addAttribute("title", "Người nhiễm quản lý");
        model.addAttribute("options", options);
        model.addAttribute("isVAAC", vaac);
        model.addAttribute("hivCode", hivCode);
        model.addAttribute("isPac", isPAC());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("isTYT", isTYT());
        model.addAttribute("search", search);
        model.addAttribute("permanentProvinceID", permanentProvinceID);
        model.addAttribute("connected", pacPatientConnected);
        model.addAttribute("dataPage", dataPage);

        model.addAttribute("options", getOptions());
        return "backend/pac/patient_index";
    }

    /**
     * Hiển thị màn hình view
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-patient/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity == null || !isManaged(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isPAC() && !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (isTTYT() && !entity.getDistrictID().equals(currentUser.getSite().getDistrictID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (isTYT() && !entity.getWardID().equals(currentUser.getSite().getWardID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        //DS CON
        List<PacPatientHistoryEntity> childs = patientHistoryService.findByParentID(pacID);
        model.addAttribute("childs", childs);

        // Set dữ liệu hiển thị trang view
        form.setFormPac(entity);
        model.addAttribute("title", "Xem thông tin người nhiễm");
        model.addAttribute("options", getOptions());
        model.addAttribute("back", UrlUtils.pacPatientIndex()); //nút quay lại
        model.addAttribute("breadcrumbUrl", UrlUtils.pacPatientIndex()); // breadcrumbUrl level 2
        model.addAttribute("breadcrumbTitle", "Người nhiễm quản lý"); // tên breadcrumbUrl level 2
        model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacPatientView(pacID)); // tên breadcrumbUrl level 3
        form.setID(pacID);

        model.addAttribute("form", form);
        return "backend/pac/patient_view";

    }

    /**
     * Xóa khách hàng
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-patient/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || (entity != null && entity.isRemove()) || !isManaged(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacPatientIndex());

        }
        LoggedUser currentUser = getCurrentUser();
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        pacPatientService.delete(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa thông tin người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Xóa người nhiễm thành công");
        return redirect(UrlUtils.pacPatientIndex());
    }

    /**
     * Khôi phục khách hàng
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/pac-patient/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacPatientIndex());

        }
        LoggedUser currentUser = getCurrentUser();
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        entity.setRemove(false);
        pacPatientService.save(entity);
        pacPatientService.log(entity.getID(), "Khôi phục người nhiễm đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục người nhiễm đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", entity.getID());
        return redirect(UrlUtils.pacPatientIndex());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-patient/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacPatientIndex(PacTabEnum.DELETE.getKey()));

        }
        LoggedUser currentUser = getCurrentUser();

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        pacPatientService.remove(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacPatientIndex(PacTabEnum.DELETE.getKey()));
    }

    /**
     * Hiển thị màn hình cập nhật
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-patient/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        Map<String, String> pacPatientConnected = pacPatientImpl.getPacPatientConnected();
        if (entity == null || !isManaged(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        LoggedUser currentUser = getCurrentUser();
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        // Set dữ liệu hiển thị trang update
        form.setFormPac(entity);
        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm quản lý");//breadcrumbTitlelv2
        model.addAttribute("breadcrumbUrl", UrlUtils.pacPatientIndex());// url nút quay lại, hủy, breadcrumb
        model.addAttribute("formUrl", UrlUtils.pacPatientUpdate(pacID));//url khi dùng <form action>
        model.addAttribute("options", getOptions());
        if (pacPatientConnected.getOrDefault(pacID.toString(), null) != null) {
            model.addAttribute("warning", "Người nhiễm đã được kết nối điều trị, không cập nhật được thông tin điều trị!");
        }

        // Setting display for update
        form.setID(pacID);

        model.addAttribute("form", form);
        return "backend/pac/form";
    }

    /**
     * Cập nhật quản lý
     *
     * @param model
     * @param pacID
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-patient/manage-update.html"}, method = RequestMethod.GET)
    public String actionUpdateManage(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        Map<String, String> pacPatientConnected = pacPatientImpl.getPacPatientConnected();

        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }

        // Set dữ liệu hiển thị trang update
        form.setProvinceID(entity.getProvinceID());
        form.setDistrictID(entity.getDistrictID());
        form.setWardID(entity.getWardID());

        form.setPermanentAddressGroup(entity.getPermanentAddressGroup());
        form.setPermanentAddressNo(entity.getPermanentAddressNo());
        form.setPermanentAddressStreet(entity.getPermanentAddressStreet());
        form.setPermanentProvinceID(entity.getPermanentProvinceID());
        form.setPermanentDistrictID(entity.getPermanentDistrictID());
        form.setPermanentWardID(entity.getPermanentWardID());

        form.setCurrentAddressNo(entity.getCurrentAddressNo());
        form.setCurrentAddressStreet(entity.getCurrentAddressStreet());
        form.setCurrentAddressGroup(entity.getCurrentAddressGroup());
        form.setCurrentProvinceID(entity.getCurrentProvinceID());
        form.setCurrentDistrictID(entity.getCurrentDistrictID());
        form.setCurrentWardID(entity.getCurrentWardID());

        form.setReviewProvinceTimeForm(TextUtils.formatDate(entity.getReviewProvinceTime(), FORMATDATE));
        form.setCreateAT(TextUtils.formatDate(entity.getCreateAT(), FORMATDATE));

        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm quản lý");//breadcrumbTitlelv2
        model.addAttribute("breadcrumbUrl", UrlUtils.pacPatientIndex());// url nút quay lại, hủy, breadcrumb
        model.addAttribute("formUrl", UrlUtils.pacPatientUpdate(pacID));//url khi dùng <form action>
        model.addAttribute("options", getOptions());
        if (pacPatientConnected.getOrDefault(pacID.toString(), null) != null) {
            model.addAttribute("warning", "Người nhiễm đã được kết nối điều trị, không cập nhật được thông tin điều trị!");
        }

        // Setting display for update
        form.setID(pacID);

        model.addAttribute("form", form);
        return "backend/pac/form_manage";
    }

    /**
     * Cập nhật thông tin người nhiễm
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-patient/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @ModelAttribute("form") @Valid PacPatientForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isManaged(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }

        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm quản lý");//breadcrumbTitlelv2
        model.addAttribute("breadcrumbUrl", UrlUtils.pacPatientIndex());// url nút quay lại, hủy, breadcrumb
        model.addAttribute("formUrl", UrlUtils.pacPatientUpdate(pacID));//url khi dùng <form action>
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        pacPatientValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", " Cập nhật thông tin người nhiễm không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/pac/form";
        }

        // Gán giá trị cho entity
        try {
            entity = form.getPacPatient(entity);
            entity = pacPatientService.save(entity);
            pacPatientService.log(entity.getID(), "Cập nhật thông tin người nhiễm");
            redirectAttributes.addFlashAttribute("success", "Người nhiễm được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());

            return redirect(UrlUtils.pacPatientIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/form";
        }
    }

    @RequestMapping(value = {"/pac-patient/manage-update.html"}, method = RequestMethod.POST)
    public String doActionManageUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @ModelAttribute("form") PacPatientForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacPatientIndex());
        }

        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm quản lý");//breadcrumbTitlelv2
        model.addAttribute("breadcrumbUrl", UrlUtils.pacPatientIndex());// url nút quay lại, hủy, breadcrumb
        model.addAttribute("formUrl", UrlUtils.pacPatientUpdate(pacID));//url khi dùng <form action>
        model.addAttribute("options", getOptions());
        model.addAttribute("form", form);

        // Gán giá trị cho entity
        try {
            PacPatientInfoEntity old = (PacPatientInfoEntity) entity.clone();
            entity.setProvinceID(form.getProvinceID());
            entity.setDistrictID(form.getDistrictID());
            entity.setWardID(form.getWardID());

            entity.setPermanentAddressGroup(form.getPermanentAddressGroup());
            entity.setPermanentAddressNo(form.getPermanentAddressNo());
            entity.setPermanentAddressStreet(form.getPermanentAddressStreet());
            entity.setPermanentProvinceID(form.getPermanentProvinceID());
            entity.setPermanentDistrictID(form.getPermanentDistrictID());
            entity.setPermanentWardID(form.getPermanentWardID());

            entity.setCurrentAddressNo(form.getCurrentAddressNo());
            entity.setCurrentAddressStreet(form.getCurrentAddressStreet());
            entity.setCurrentAddressGroup(form.getCurrentAddressGroup());
            entity.setCurrentProvinceID(form.getCurrentProvinceID());
            entity.setCurrentDistrictID(form.getCurrentDistrictID());
            entity.setCurrentWardID(form.getCurrentWardID());

            entity.setReviewProvinceTime(TextUtils.convertDate(form.getReviewProvinceTimeForm(), FORMATDATE));
            entity.setCreateAT(TextUtils.convertDate(form.getCreateAT(), FORMATDATE));

            pacPatientService.save(entity);
            pacPatientService.log(entity.getID(), "Cập nhật thông tin quản lý<br/>" + entity.changeToString(old, true));

            redirectAttributes.addFlashAttribute("success", "Thông tin quản lý được cập nhật thành công.");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());

            return redirect(UrlUtils.pacPatientIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/form";
        }
    }

    @RequestMapping(value = "/pac-patient/review.html", method = RequestMethod.GET)
    public String actionReview(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            PacPatientInfoEntity patient = pacPatientService.findOne(pacID);
            if (patient == null || patient.isRemove() || !isManaged(patient)) {
                redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
                return redirect(UrlUtils.pacPatientIndex());

            }

            //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
            if (!isPAC() || !patient.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
                redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc quyền chỉnh sửa ca nhiễm");
                return redirect(UrlUtils.backendHome());
            }

            patient.setCheckProvinceTime(new Date());
            patient.setCheckWardTime(null);
            patient.setCheckDistrictTime(null);
            pacPatientService.save(patient);
            pacPatientService.log(patient.getID(), "Yêu cầu rà soát lại");

            //Gửi email, thông báo về cho huyện và xã
            SiteEntity ttyt = siteService.findByTTYT(patient.getProvinceID(), patient.getDistrictID());
            SiteEntity tyt = siteService.findByTYT(patient.getProvinceID(), patient.getDistrictID(), patient.getWardID());
            if (ttyt != null) {
                sendNotifyToSite(ttyt.getID(), ServiceEnum.PAC, "Cần rà soát lại bệnh nhân #" + patient.getID(), "Email thông báo tự động từ hệ thống dulieuhiv.", UrlUtils.pacReviewIndex() + "?name=" + patient.getFullname());
            }
            if (tyt != null) {
                sendNotifyToSite(tyt.getID(), ServiceEnum.PAC, "Cần rà soát lại bệnh nhân #" + patient.getID(), "Email thông báo tự động từ hệ thống dulieuhiv.", UrlUtils.pacReviewIndex() + "?name=" + patient.getFullname());
            }
            redirectAttributes.addFlashAttribute("success", "Thông tin rà soát đã được gửi về huyện, xã!");
            redirectAttributes.addFlashAttribute("success_id", patient.getID());
            return redirect(UrlUtils.pacPatientIndex());
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Yêu cầu rà soát lại không thành công. Vui lòng kiểm tra " + ex.getMessage());
            return redirect(UrlUtils.pacPatientIndex());
        }
    }
}
