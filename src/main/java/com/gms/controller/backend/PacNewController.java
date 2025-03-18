package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.PacTabEnum;
import com.gms.entity.constant.RaceEnum;
import com.gms.entity.db.PacPatientHistoryEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.entity.validate.PacPatientValidate;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Giám sát phát hiện -> Phát hiện ca bệnh
 *
 * @author Văn Thành
 */
@Controller(value = "backend_pac_new")
public class PacNewController extends PacController {

    @Autowired
    private PacPatientValidate pacPatientValidate;
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientHistoryService patientHistoryService;

    /**
     * Danh sách ca nhiễm
     *
     * @author pdThang
     * @param model
     * @param redirectAttributes
     * @param tab
     * @param fullname
     * @param yearOfBirth
     * @param confirmTimeFrom
     * @param confirmTimeTo
     * @param searchProvinceID
     * @param searchDetectProvinceID
     * @param permanentDistrictID
     * @param permanentWardID
     * @param permanentProvinceID
     * @param genderID
     * @param identityCard
     * @param status
     * @param service
     * @param bloodBase
     * @param tabRemove
     * @param siteConfirmID
     * @param siteTreatmentFacilityID
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = {"/pac-new/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "tab", required = false, defaultValue = "new_in_province") String tab,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "search_province_id", required = false) String searchProvinceID,
            @RequestParam(name = "detect_province_id", required = false) String searchDetectProvinceID,//dùng để tìm kiếm tỉnh khác phát hiện
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase,
            @RequestParam(name = "tab_remove", required = false, defaultValue = "0") String tabRemove,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "id", required = false) String ID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        LoggedUser currentUser = getCurrentUser();

        boolean vaac = isVAAC();

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() && !vaac) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC/CDC/PAC)");
            return redirect(UrlUtils.backendHome());
        }

        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        try {
            search.setID(StringUtils.isBlank(ID) ? Long.valueOf("0") : Long.valueOf(ID.trim()));
        } catch (Exception e) {
            search.setID(Long.valueOf("-99"));
        }
        search.setConfirmTimeFrom(confirmTimeFrom);
        search.setConfirmTimeTo(confirmTimeTo);
        search.setRemove(false);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setCurrentPermanentProvinceID(currentUser.getSite().getProvinceID());
        search.setDetectProvinceID(currentUser.getSite().getProvinceID());
        search.setService(service);
        search.setBloodBase(bloodBase);
        search.setIsVAAC(vaac);

        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);

        if (tab.equals(PacTabEnum.NEW_IN_PROVINCE.getKey())) {
            //phát hiện trong tỉnh: 
            search.setPermanentProvince(1);
            search.setDetectProvince(1);
            search.setProvinceID(getCurrentUser().getSite().getProvinceID());
        } else if (tab.equals(PacTabEnum.NEW_OTHER_PROVINCE.getKey())) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
//            search.setSearchDetectProvinceID(searchDetectProvinceID);
            search.setPermanentProvince(0);//1-Cờ này dùng để kiểm tra Tỉnh thường trú của bệnh nhân = Tỉnh của cơ sở đang đăng nhập
            search.setDetectProvince(0);//2-Cờ này dùng để kiểm tra Mã tỉnh/thành phố phát hiệN KHÁC Tỉnh của cơ sở đang đăng nhập
            search.setAcceptPermanentTime("1"); //Cở đánh dấu tỉnh khác phát hiện
        } else if (tab.equals(PacTabEnum.NEW_OUT_PROVINCE.getKey())) {
            //phát hiện ngoại tỉnh
            search.setSearchProvinceID(searchProvinceID);
//            search.setAcceptPermanentTime(status);//Trạng thái
            search.setPermanentProvince(2);//Cờ này dùng để kiểm tra Tỉnh thường trú của bệnh nhân KHÁC Tỉnh của cơ sở đang đăng nhập. 
            search.setDetectProvince(1); //Cờ này dùng để kiểm tra Mã tỉnh/thành phố phát hiệN = Tỉnh của cơ sở đang đăng nhập
        } else if (tab.equals(PacTabEnum.DELETE.getKey())) {
            search.setRemove(true);
            switch (tabRemove) {
                case "1":
                    search.setProvinceID(currentUser.getSite().getProvinceID());
                    search.setPermanentProvince(0);//Cờ này dùng để kiểm tra Tỉnh thường trú của bệnh nhân = Tỉnh của cơ sở đang đăng nhập
                    search.setDetectProvince(0);//Cờ này dùng để kiểm tra Mã tỉnh/thành phố phát hiệN KHÁC Tỉnh của cơ sở đang đăng nhập
                    search.setAcceptPermanentTime("1");
                    break;
                case "2":
                    search.setSearchProvinceID(searchProvinceID);
                    search.setPermanentProvince(2);
                    search.setDetectProvince(1);
                    break;
                default:
                    search.setPermanentProvince(1);
                    search.setDetectProvince(1);
                    search.setProvinceID(getCurrentUser().getSite().getProvinceID());
            }
        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = new DataPage<>();
        if (vaac) {
            dataPage = pacPatientService.findNewVAAC(search);
        } else {
            dataPage = pacPatientService.findNew(search, tab.equals("") ? PacTabEnum.NEW_IN_PROVINCE.getKey() : tab);
        }

        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        model.addAttribute("title", "Người nhiễm chưa rà soát");
        model.addAttribute("smallTitle", "Quản lý ca bệnh");
        model.addAttribute("options", options);
        model.addAttribute("dataPage", dataPage);

        Map<String, String> tabRemoveOption = new HashMap<>();
        tabRemoveOption.put("0", "Phát hiện trong tỉnh");
        tabRemoveOption.put("1", "Tỉnh khác phát hiện");
        tabRemoveOption.put("2", "Phát hiện ngoại tỉnh");
        model.addAttribute("tabRemoveOption", tabRemoveOption);
        model.addAttribute("isVAAC", vaac);
        model.addAttribute("allProvinces", allProvinces);
        model.addAttribute("isPAC", isPAC());

        model.addAttribute("totalInProvince", pacPatientService.countInProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalOtherProvince", pacPatientService.countOtherProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalOutProvince", pacPatientService.countOutProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalVAACNew", pacPatientService.countVAACNew(currentUser.getSite().getProvinceID()));
        return "backend/pac/new_index";
    }

    @RequestMapping(value = {"/pac-new/vaac.html"}, method = RequestMethod.GET)
    public String actionVAACIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "year_of_birth", required = false) Integer yearOfBirth,
            @RequestParam(name = "confirm_time_from", required = false) String confirmTimeFrom,
            @RequestParam(name = "confirm_time_to", required = false) String confirmTimeTo,
            @RequestParam(name = "permanent_district_id", required = false) String permanentDistrictID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_ward_id", required = false) String permanentWardID,//Dùng cho tìm kiếm tab: Phát hiện trong tỉnh và Tỉnh khác phát hiện
            @RequestParam(name = "permanent_province_id", required = false) String permanentProvinceID,// tìm kiếm địa chỉ thường trú tab PHÁT HIỆN NGOẠI TỈNH
            @RequestParam(name = "gender_id", required = false) String genderID,
            @RequestParam(name = "identity_card", required = false) String identityCard,
            @RequestParam(name = "status", required = false, defaultValue = "") String status,
            @RequestParam(name = "service", required = false, defaultValue = "") String service,
            @RequestParam(name = "blood_base", required = false, defaultValue = "") String bloodBase,
            @RequestParam(name = "siteConfirmID", required = false) String siteConfirmID,
            @RequestParam(name = "siteTreatmentFacilityID", required = false) String siteTreatmentFacilityID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) {
        LoggedUser currentUser = getCurrentUser();

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng (Cục VAAC)");
            return redirect(UrlUtils.backendHome());
        }

        HashMap<String, HashMap<String, String>> options = getOptions();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setProvinceID(currentUser.getSite().getProvinceID());
        search.setPageIndex(page);
        search.setPageSize(size);

        //xóa dấu cách 2 đầu và giữa
        if (fullname != null && !"".equals(fullname)) {
            search.setFullname(StringUtils.normalizeSpace(fullname.trim()));
        }
        if (identityCard != null && !"".equals(identityCard)) {
            search.setIdentityCard(StringUtils.normalizeSpace(identityCard.trim()));
        }
        search.setConfirmTimeFrom(isThisDateValid(confirmTimeFrom) ? confirmTimeFrom : null);
        search.setConfirmTimeTo(isThisDateValid(confirmTimeTo) ? confirmTimeTo : null);
        search.setYearOfBirth(yearOfBirth);
        search.setGenderID(genderID);
        search.setPermanentProvinceID(permanentProvinceID);
        search.setPermanentDistrictID(permanentDistrictID);
        search.setPermanentWardID(permanentWardID);
        search.setCurrentPermanentProvinceID(currentUser.getSite().getProvinceID());
        search.setDetectProvinceID(currentUser.getSite().getProvinceID());
        search.setService(service);
        search.setBloodBase(bloodBase);

        search.setSiteConfirmID(siteConfirmID);
        search.setSiteTreatmentFacilityID(siteTreatmentFacilityID);

        HashMap<String, String> allProvinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationsService.findAllProvince()) {
            allProvinces.put(provinceEntity.getID(), provinceEntity.getName());
        }

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findVAACNew(search);

        model.addAttribute("title", "Người nhiễm chưa rà soát");
        model.addAttribute("smallTitle", "Quản lý ca bệnh");
        model.addAttribute("options", options);
        model.addAttribute("isPAC", isPAC());
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("allProvinces", allProvinces);
        model.addAttribute("totalInProvince", pacPatientService.countInProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalOtherProvince", pacPatientService.countOtherProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalOutProvince", pacPatientService.countOutProvince(currentUser.getSite().getProvinceID()));
        model.addAttribute("totalVAACNew", pacPatientService.countVAACNew(currentUser.getSite().getProvinceID()));
        return "backend/pac/new_vaac";
    }

    /**
     * Thêm mới ca nhiễm
     *
     * @author pdThang
     * @param model
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-new/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC()) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng");
            return redirect(UrlUtils.backendHome());
        }

        form.setRaceID(RaceEnum.KINH.getKey());
        model.addAttribute("title", "Thêm mới người nhiễm");
        model.addAttribute("breadcrumbTitle", "Quản lý người nhiễm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pacNew());
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        return "backend/pac/form";
    }

    /**
     * Thêm mới ca nhiễm
     *
     * @author pdThang
     * @param model
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-new/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid PacPatientForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("title", "Thêm mới người nhiễm");
        model.addAttribute("breadcrumbTitle", "Quản lý người nhiễm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pacNew());
        model.addAttribute("form", form);
        model.addAttribute("options", getOptions());
        //validate form
        pacPatientValidate.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", " Thêm thông tin người nhiễm không thành công. Vui lòng kiểm tra lại thông tin");
            return "backend/pac/form";
        }

        try {
            PacPatientInfoEntity entity = form.getPacPatient(null);
            //Mặc định cơ sở phát hiện
            entity.setDetectProvinceID(getCurrentUser().getSite().getProvinceID());
            entity.setDetectDistrictID(getCurrentUser().getSite().getDistrictID());
            //Quản lý
            entity.setProvinceID(getCurrentUser().getSite().getProvinceID());
            entity.setDistrictID(getCurrentUser().getSite().getDistrictID());
            entity.setWardID(getCurrentUser().getSite().getWardID());

            pacPatientService.save(entity);
            pacPatientService.log(entity.getID(), "Thêm mới thông tin người nhiễm");
            redirectAttributes.addFlashAttribute("success", "Người nhiễm được thêm mới thành công");
            redirectAttributes.addFlashAttribute("success_id", entity.getID());
            return redirect(UrlUtils.pacNew());

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "backend/pac/form";
    }

    /**
     * Hiển thị màn hình cập nhật
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param tab
     * @param form
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-new/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {

        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tài khoản không có quyền sửa thông tin"));
            return redirect(UrlUtils.pacNew());
        }

        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isNew(entity, currentUser.getSiteProvince().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.pacNew(tab));
        }

        // Set dữ liệu hiển thị trang update
        form.setFormPac(entity);
        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Quản lý người nhiễm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pacNew());
        model.addAttribute("options", getOptions());

        // Setting display for update
        form.setID(pacID);
        form.setTab(tab);

        model.addAttribute("form", form);
        return "backend/pac/form";
    }

    /**
     * Cập nhật thông tin người nhiễm
     *
     * @author pdThang
     * @param model
     * @param pacID
     * @param tab
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = {"/pac-new/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            @ModelAttribute("form") @Valid PacPatientForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tài khoản không có quyền sửa thông tin"));
            return redirect(UrlUtils.pacNew());
        }

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        model.addAttribute("title", "Cập nhật thông tin người nhiễm");
        model.addAttribute("breadcrumbTitle", "Quản lý người nhiễm");
        model.addAttribute("breadcrumbUrl", UrlUtils.pacNew());
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

            return redirect(UrlUtils.pacNew(form.getTab()));
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/pac/form";
        }
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
    @RequestMapping(value = {"/pac-new/view.html"}, method = RequestMethod.GET)
    public String actionView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity == null || !isNew(entity, currentUser.getSiteProvince().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isVAAC() || (isPAC() && (entity.getProvinceID().equals(currentUser.getSite().getProvinceID()) || (!entity.getProvinceID().equals(currentUser.getSite().getProvinceID()) && entity.getDetectProvinceID().equals(currentUser.getSite().getProvinceID()))))) {
            //DS CON
            List<PacPatientHistoryEntity> childs = patientHistoryService.findByParentID(pacID);
            model.addAttribute("childs", childs);

            // Set dữ liệu hiển thị trang view
            form.setFormPac(entity);
            model.addAttribute("title", "Xem thông tin người nhiễm");
            model.addAttribute("options", getOptions());
            model.addAttribute("back", UrlUtils.pacNew(tab)); //nút quay lại
            model.addAttribute("breadcrumbUrl", UrlUtils.pacNew()); // breadcrumbUrl level 2
            model.addAttribute("breadcrumbTitle", "Người nhiễm phát hiện"); // tên breadcrumbUrl level 2
            model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacPatientView(tab, pacID)); // tên breadcrumbUrl level 3
            form.setID(pacID);

            model.addAttribute("form", form);
            return "backend/pac/patient_view";

        }
        redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
        return redirect(UrlUtils.backendHome());

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
    @RequestMapping(value = {"/pac-new/view-vaac.html"}, method = RequestMethod.GET)
    public String actionVaacView(Model model,
            @RequestParam(name = "id", defaultValue = "") Long pacID,
            PacPatientForm form,
            RedirectAttributes redirectAttributes) {
        LoggedUser currentUser = getCurrentUser();

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);

        if (entity == null || !isPAC()) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        if (entity.isRemove() == true) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy người nhiễm có mã %s", pacID));
            return redirect(UrlUtils.pacNew());
        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (isPAC()) {

            //DS CON
            List<PacPatientHistoryEntity> childs = patientHistoryService.findByParentID(pacID);
            model.addAttribute("childs", childs);

            // Set dữ liệu hiển thị trang view
            form.setFormPac(entity);
            model.addAttribute("title", "Xem thông tin người nhiễm");
            model.addAttribute("options", getOptions());
            model.addAttribute("back", UrlUtils.pacNewVaac()); //nút quay lại
            model.addAttribute("breadcrumbUrl", UrlUtils.pacNew()); // breadcrumbUrl level 2
            model.addAttribute("breadcrumbTitle", "Người nhiễm phát hiện"); // tên breadcrumbUrl level 2
            model.addAttribute("breadcrumbUrlLv3", UrlUtils.pacPatientVaacView(pacID)); // tên breadcrumbUrl level 3
            form.setID(pacID);

            model.addAttribute("form", form);
            return "backend/pac/patient_view";

        }
        redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
        return redirect(UrlUtils.backendHome());

    }

    /**
     * Xóa khách hàng
     *
     * @author PdThang
     * @param model
     * @param pacID
     * @param tab
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/pac-new/remove.html", method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long pacID,
            @RequestParam(name = "tab") String tab,
            RedirectAttributes redirectAttributes) {

        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tài khoản không có quyền xóa"));
            return redirect(UrlUtils.pacNew());
        }

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity.isRemove()) {
            redirectAttributes.addFlashAttribute("error", String.format("Người bệnh không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacNew());

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
        return redirect(UrlUtils.pacNew(tab));
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
    @RequestMapping(value = "/pac-new/restore.html", method = RequestMethod.GET)
    public String actionRestore(Model model,
            @RequestParam(name = "oid") Long pacID,
            RedirectAttributes redirectAttributes) throws Exception {

        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacNew());

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
        return redirect(UrlUtils.pacNew());
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
    @RequestMapping(value = "/pac-new/delete.html", method = RequestMethod.GET)
    public String actionDelete(Model model,
            @RequestParam(name = "id") Long pacID,
            RedirectAttributes redirectAttributes) {

        if (isVAAC()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tài khoản không có quyền xóa"));
            return redirect(UrlUtils.pacNew());
        }

        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity entity = pacPatientService.findOne(pacID);
        if (entity == null || !isNew(entity, currentUser.getSiteProvince().getID())) {
            redirectAttributes.addFlashAttribute("error", String.format("Người nhiễm không tồn tại trong cơ sở"));
            return redirect(UrlUtils.pacNew(PacTabEnum.DELETE.getKey()));

        }

        //kiểm tra quyền sử dụng dữ liệu - chỉ PAC được sử dụng
        if (!isPAC() || !entity.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            redirectAttributes.addFlashAttribute("warning", "Cơ sở không có dịch vụ tương ứng hoặc chỉnh sửa ca nhiễm");
            return redirect(UrlUtils.backendHome());
        }

        pacPatientService.remove(entity.getID());
        pacPatientService.log(entity.getID(), "Xóa vĩnh viễn người nhiễm");
        redirectAttributes.addFlashAttribute("success", "Người nhiễm đã bị xóa vĩnh viễn");
        return redirect(UrlUtils.pacNew(PacTabEnum.DELETE.getKey()));
    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
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
