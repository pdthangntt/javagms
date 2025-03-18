package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PacPatientAcceptSearch;
import com.gms.entity.validate.PacPatientValidate;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import com.gms.service.SiteService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
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
 * Giám sát phát hiện -> Đã rà soát, chuyển sang quản lý
 *
 * @author Văn Thành
 */
@Controller(value = "backend_pac_accept")
public class PacAcceptController extends PacController {

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private PacPatientValidate pacPatientValidate;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PacPatientHistoryService patientHistoryService;

    @RequestMapping(value = {"/pac-accept/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "status_of_resident_id", required = false) String statusOfResidentID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "health_insurance_no", required = false) String healthInsuranceNo,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,@RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientAcceptSearch search = new PacPatientAcceptSearch();
        search.setPageIndex(page);
        search.setPageSize(size);

        search.setRemove(false);
//        search.setProvinceID(currentUser.getSite().getProvinceID());
        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (healthInsuranceNo != null && !"".equals(healthInsuranceNo)) {
            search.setHealthInsuranceNo(StringUtils.normalizeSpace(healthInsuranceNo.trim()));
        }
        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setStatusOfResidentID(statusOfResidentID);

        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setAddressFilter(addressFilter);
        
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        
        if (isPAC()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            if (tab.equals(PacTabEnum.DELETE.getKey())) {
                search.setRemove(true);
            }
        } else if (isTTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());

        } else if (isTYT()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
            search.setDistrictID(currentUser.getSite().getDistrictID());
            search.setWardID(currentUser.getSite().getWardID());
        }

        if (tab.equals("review")) {
            search.setHasReview(1);
        }

        HashMap<String, String> provinceNames = new HashMap<>();
        for (ProvinceEntity object : locationsService.findAllProvince()) {
            provinceNames.put(object.getID(), object.getName());
        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findAccept(search);

        model.addAttribute("title", "Người nhiễm đã rà soát");
        model.addAttribute("smallTitle", "Người nhiễm đã rà soát");
        model.addAttribute("options", options);
        model.addAttribute("isPac", isPAC());
        model.addAttribute("provinceNames", provinceNames);
        model.addAttribute("isVAAC", isVAAC());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("isTYT", isTYT());
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("addressFilter", addressFilter);
        model.addAttribute("search", search);
        model.addAttribute("options", getOptions());
        if (isTYT()) {
            model.addAttribute("warning", "Thông tin rà soát của cơ sở đang chờ Khoa giám sát/TTYT huyện kiểm duyệt!");
        }
        if (isTTYT() && !tab.equals("review")) {
            model.addAttribute("warning", "Thông tin rà soát của cơ sở đang chờ Khoa giám sát kiểm duyệt!");
        }
        return "backend/pac/accept_index";
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
    @RequestMapping(value = {"/pac-accept/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity == null || entity.isRemove() || !isReviewed(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacAcceptIndex());
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
        model.addAttribute("back", UrlUtils.pacAcceptIndex()); //nút quay lại
        model.addAttribute("breadcrumbUrl", UrlUtils.pacAcceptIndex()); // breadcrumbUrl level 2
        model.addAttribute("breadcrumbTitle", "Người nhiễm đã rà soát"); // tên breadcrumbUrl level 2
        model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacAcceptView(pacID)); // tên breadcrumbUrl level 3
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
    @RequestMapping(value = "/pac-accept/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isReviewed(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacAcceptIndex());

        }
        if (entity.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacAcceptIndex());

        }
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        pacPatientService.delete(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa thông tin người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Xóa người nhiễm thành công");
        return redirect(UrlUtils.pacAcceptIndex());
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
    @RequestMapping(value = "/pac-accept/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacAcceptIndex());

        }
        LoggedUser currentUser = getCurrentUser();
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        entity.setRemove(false);
        pacPatientService.save(entity);
        pacPatientService.log(entity.getID(), "Khôi phục người nhiễm đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục người nhiễm đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", entity.getID());
        return redirect(UrlUtils.pacAcceptIndex());
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
    @RequestMapping(value = "/pac-accept/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacAcceptIndex(PacTabEnum.DELETE.getKey()));

        }
        LoggedUser currentUser = getCurrentUser();
        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        pacPatientService.remove(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacAcceptIndex(PacTabEnum.DELETE.getKey()));
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
    @RequestMapping(value = {"/pac-accept/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || entity.isRemove() || !isReviewed(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacAcceptIndex());
        }
        LoggedUser currentUser = getCurrentUser();
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacAcceptIndex());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        // Set dữ liệu hiển thị trang update
        form.setFormPac(entity);
        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm đã rà soát");//breadcrumbTitlelv2
        model.addAttribute("breadcrumbUrl", UrlUtils.pacAcceptIndex());// url nút quay lại, hủy, breadcrumb
        model.addAttribute("formUrl", UrlUtils.pacAcceptUpdate(pacID));//url khi dùng <form action>
        model.addAttribute("options", getOptions());

        // Setting display for update
        form.setID(pacID);

        model.addAttribute("form", form);
        return "backend/pac/form";
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
    @RequestMapping(value = {"/pac-accept/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @ModelAttribute("form") @Valid PacPatientForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isReviewed(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacAcceptIndex());
        }
        LoggedUser currentUser = getCurrentUser();
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacAcceptIndex());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Người nhiễm đã rà soát");
        model.addAttribute("breadcrumbUrl", UrlUtils.pacAcceptIndex());
        model.addAttribute("formUrl", UrlUtils.pacAcceptUpdate(pacID));
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

            return redirect(UrlUtils.pacAcceptIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/form";
        }
    }

    @RequestMapping(value = "/pac-accept/review.html", method = RequestMethod.GET)
    public String actionReview(Model model,
            @RequestParam(name = "id") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            RedirectAttributes redirectAttributes) {
        try {
            LoggedUser currentUser = getCurrentUser();
            PacPatientInfoEntity patient = pacPatientService.findOne(pacID);
            if (patient == null || patient.isRemove()) {
                redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
                return redirect(UrlUtils.pacAcceptIndex() + "?tab=" + tab);
            }

            //kiểm tra quyền sử dụng dữ liệu - chỉ ttyt được sử dụng
//            if (!isTTYT() || !patient.getProvinceID().equals(currentUser.getSite().getProvinceID())
//                    || !patient.getDistrictID().equals(currentUser.getSite().getDistrictID())) {
//                redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc quyền chỉnh sửa ca nhiễm");
//                return redirect(UrlUtils.backendHome());
//            }

            patient.setCheckDistrictTime(new Date());
            patient.setReviewDistrictTime(new Date());
            pacPatientService.save(patient);
            pacPatientService.log(patient.getID(), "TTYT duyệt thông tin rà soát");

            //Gửi email, thông báo về cho huyện và xã
            SiteEntity pac = siteService.findByPAC(patient.getProvinceID());
            SiteEntity tyt = siteService.findByTYT(patient.getProvinceID(), patient.getDistrictID(), patient.getWardID());
            if (pac != null) {
                sendNotifyToSite(pac.getID(), ServiceEnum.PAC, "TTYT " + pac.getName() + " duyệt thông tin rà soát bệnh nhân #" + patient.getID(), "Email thông báo tự động.", UrlUtils.pacAcceptIndex() + "?fullname=" + patient.getFullname());
            }
            if (tyt != null) {
                sendNotifyToSite(tyt.getID(), ServiceEnum.TYT, "TTYT " + tyt.getName() + " duyệt thông tin rà soát bệnh nhân #" + patient.getID(), "Email thông báo tự động.", UrlUtils.pacAcceptIndex() + "?fullname=" + patient.getFullname());
            }
            redirectAttributes.addFlashAttribute("success", "Duyệt thông tin rà soát thành công");
            redirectAttributes.addFlashAttribute("success_id", patient.getID());
            return redirect(UrlUtils.pacAcceptIndex() + "?tab=" + tab);
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duyệt thông tin rà soát không thành công. Vui lòng kiểm tra " + ex.getMessage());
            return redirect(UrlUtils.pacAcceptIndex() + "?tab=" + tab);
        }
    }
}
