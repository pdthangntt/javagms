package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PacPatientReviewSearch;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import java.util.HashMap;
import java.util.List;
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
 * Giám sát phát hiện -> Người nhiễm cần rà soát
 *
 * @author Văn Thành
 */
@Controller(value = "backend_pac_review")
public class PacReviewController extends PacController {
    
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientHistoryService patientHistoryService;
    
    @RequestMapping(value = {"/pac-review/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model,
            @RequestParam(name = "address_type", required = false, defaultValue = "hokhau") String addressFilter,
            @RequestParam(name = "tab", required = false, defaultValue = "") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,////Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID, @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        LoggedUser currentUser = getCurrentUser();
        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientReviewSearch search = new PacPatientReviewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        
        search.setRemove(false);

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        
        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);
        
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setAddressFilter(addressFilter);
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
        
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        if (tab.equals("vaac")) {
            dataPage = pacPatientService.findReviewVaac(search);
        } else {
            dataPage = pacPatientService.findReview(search);
        }
        
        HashMap<String, String> provinceNames = new HashMap<>();
        for (ProvinceEntity object : locationsService.findAllProvince()) {
            provinceNames.put(object.getID(), object.getName());
        }
        
        model.addAttribute("title", "Người nhiễm cần rà soát");
        model.addAttribute("smallTitle", "Người nhiễm cần rà soát");
        model.addAttribute("options", options);
        model.addAttribute("isPac", isPAC());
        model.addAttribute("isTTYT", isTTYT());
        model.addAttribute("isVAAC", isVAAC());
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("provinceNames", provinceNames);
        model.addAttribute("addressFilter", addressFilter);
        model.addAttribute("search", search);
        
        model.addAttribute("options", getOptions());
        return "backend/pac/review_index";
    }

    /**
     * Hiển thị màn hình view
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-review/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        
        //|| !isNeedReview(entity)
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacReviewIndex());
        }
        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacReviewIndex());
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
        model.addAttribute("parent_title", "Người nhiễm phát hiện");
        model.addAttribute("options", getOptions());
        model.addAttribute("tab", tab);
        model.addAttribute("back", UrlUtils.pacReviewIndex()); //nút quay lại
        model.addAttribute("breadcrumbUrl", UrlUtils.pacReviewIndex()); // breadcrumbUrl level 2
        model.addAttribute("breadcrumbTitle", "Người nhiễm cần rà soát"); // tên breadcrumbUrl level 2
        model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacReviewView(pacID)); // tên breadcrumbUrl level 3
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
    @RequestMapping(value = "/pac-review/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {
        
        LoggedUser currentUser = getCurrentUser();
        
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isNeedReview(entity)) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacReviewIndex());
            
        }
        if (entity.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacReviewIndex());
            
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
        return redirect(UrlUtils.pacReviewIndex());
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
    @RequestMapping(value = "/pac-review/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacReviewIndex());
            
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
        entity.setRemove(false);
        pacPatientService.save(entity);
        pacPatientService.log(entity.getID(), "Khôi phục người nhiễm đã xóa");
        redirectAttributes.addFlashAttribute("success", "Khôi phục người nhiễm đã xóa thành công");
        redirectAttributes.addFlashAttribute("success_id", entity.getID());
        return redirect(UrlUtils.pacReviewIndex());
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
    @RequestMapping(value = "/pac-review/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacReviewIndex(PacTabEnum.DELETE.getKey()));
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
        
        pacPatientService.remove(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacReviewIndex(PacTabEnum.DELETE.getKey()));
    }
}
