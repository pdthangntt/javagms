package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ChangeStatusEnum;
import com.gms.entity.constant.ConnectStatusEnum;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author NamAnh_HaUI
 */
@Controller(value = "backend_pac_opc")
public class PacOpcController extends PacController {

    @Autowired
    PacPatientService pacPatientService;
    @Autowired
    LocationsService locationsService;
    @Autowired
    private PacPatientRepositoryImpl pacPatientImpl;
    @Autowired
    private PacPatientHistoryService patientHistoryService;

    @Override
    protected HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = super.getOptions();

        // Trạng thái biến động điều trị
//        String opcStatus = "opcStatus";
//        options.put(opcStatus, new HashMap<String, String>());
//        options.get(opcStatus).put("", "Chọn trạng thái biến động");
//        options.get(opcStatus).put(ChangeStatusEnum.NEW.getKey(), ChangeStatusEnum.NEW.getLabel());
//        options.get(opcStatus).put(ChangeStatusEnum.RETURN.getKey(), ChangeStatusEnum.RETURN.getLabel());
//        options.get(opcStatus).put(ChangeStatusEnum.MOVE_IN.getKey(), ChangeStatusEnum.MOVE_IN.getLabel());
//        options.get(opcStatus).put(ChangeStatusEnum.MOVE_OUT.getKey(), ChangeStatusEnum.MOVE_OUT.getLabel());
//        options.get(opcStatus).put(ChangeStatusEnum.REVOKE.getKey(), ChangeStatusEnum.REVOKE.getLabel());
//        options.get(opcStatus).put(ChangeStatusEnum.DEAD.getKey(), ChangeStatusEnum.DEAD.getLabel());
        HashMap<String, String> provinces = new HashMap<>();
        for (ProvinceEntity entity : locationsService.findAllProvince()) {
            provinces.put(entity.getID(), entity.getName());
        }
        options.put("provinces", provinces);

        // Trạng thái kết nối điều trị
        String connectOption = "connectOption";
        options.put(connectOption, new HashMap<String, String>());
        options.get(connectOption).put("", "Chọn trạng thái kết nối điều trị");
        options.get(connectOption).put(ConnectStatusEnum.NOT_CONNECT.getKey(), ConnectStatusEnum.NOT_CONNECT.getLabel());
        options.get(connectOption).put(ConnectStatusEnum.CONNECT.getKey(), ConnectStatusEnum.CONNECT.getLabel());

        // Bổ sung cơ sở xnkđ khác
        options.get(ParameterEntity.PLACE_TEST).put("-1", "Cơ sở khác");

        return options;
    }

    @RequestMapping(value = {"/pac-opc/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
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
            @RequestParam(name = "status", required = false) String status, // trạng thai dieu tri
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

        model.addAttribute("title", "Danh sách biến động điều trị");
        model.addAttribute("tab", tab);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("isVAAC", vaac);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("options", options);
        model.addAttribute("connected", pacPatientConnected);
        model.addAttribute("search", search);
        return "backend/pac/opc_index";
    }

    /**
     * Hiển thị màn hình view
     *
     * @author DSNAnh
     * @param model
     * @param pacID
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-opc/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity == null || !isOpc(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacOpcIndex());
        }
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacOpcIndex());
        }
        //DS CON
        List<PacPatientHistoryEntity> childs = patientHistoryService.findByParentID(pacID);
        model.addAttribute("childs", childs);

        form.setFormPac(entity);
        model.addAttribute("title", "Xem thông tin người nhiễm");
        model.addAttribute("tab", tab);
        model.addAttribute("options", getOptions());
        model.addAttribute("back", UrlUtils.pacOpcIndex()); //nút quay lại
        model.addAttribute("breadcrumbUrl", UrlUtils.pacOpcIndex()); // breadcrumbUrl level 2
        model.addAttribute("breadcrumbTitle", "Danh sách biến động điều trị"); // tên breadcrumbUrl level 2
        model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacOpcView(tab, pacID)); // tên breadcrumbUrl level 3
        form.setID(pacID);

        model.addAttribute("form", form);
        return "backend/pac/patient_view";
    }

    /**
     * Xóa khách hàng
     *
     * @author DSNAnh
     * @param model
     * @param pacID
     * @param tab
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-opc/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long pacID,
            @RequestParam(name = "tab") String tab,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || entity.isRemove() || !isOpc(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacOpcIndex());

        }
        pacPatientService.delete(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa thông tin người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Xóa người nhiễm thành công");
        return redirect(UrlUtils.pacOpcIndex(tab));
    }

    /**
     * Khôi phục khách hàng
     *
     * @author DSNAnh
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/pac-opc/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacOpcIndex());

        }
        entity.setRemove(false);
        pacPatientService.save(entity);
        pacPatientService.log(entity.getID(), "Khôi phục người nhiễm đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục người nhiễm đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", entity.getID());
        return redirect(UrlUtils.pacOpcIndex());
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author DSNAnh
     * @param model
     * @param pacID
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-opc/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacOpcIndex("remove"));

        }
        pacPatientService.remove(entity);
        pacPatientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacOpcIndex("remove"));
    }
}
