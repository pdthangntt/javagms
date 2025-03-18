package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.components.hivinfo.CallUtils;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.DistrictForm;
import com.gms.entity.form.ProvinceForm;
import com.gms.entity.form.WardForm;
import com.gms.entity.json.hivinfo.common.Item;
import com.gms.entity.json.hivinfo.location.District;
import com.gms.entity.json.hivinfo.location.Ward;
import com.gms.entity.validate.DistrictValidate;
import com.gms.entity.validate.ProvinceValidate;
import com.gms.entity.validate.WardValidate;
import com.gms.service.LocationsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author NamAnh_HaUI
 */
@Controller(value = "backend_location")
public class LocationController extends BaseController {

    @Autowired
    LocationsService locationService;
    @Autowired
    ProvinceValidate provinceValidate;
    @Autowired
    DistrictValidate districtValidate;
    @Autowired
    WardValidate wardValidate;
    @Autowired
    private CallUtils hivInfoAPIUtils;

    private HashMap<String, String> getProvinceTypeOption() {
        HashMap<String, String> list = new HashMap<>();
        list.put("Tỉnh", "Tỉnh");
        list.put("Thành phố trung ương", "Thành phố trung ương");
        return list;
    }

    private HashMap<String, String> getDistrictTypeOption() {
        HashMap<String, String> list = new HashMap<>();
        list.put("Huyện", "Huyện");
        list.put("Quận", "Quận");
        list.put("Thị xã", "Thị xã");
        return list;
    }

    private HashMap<String, String> getWardTypeOption() {
        HashMap<String, String> list = new HashMap<>();
        list.put("Xã", "Xã");
        list.put("Phường", "Phường");
        return list;
    }

    //Province
    @RequestMapping(value = "/location/index.html", method = RequestMethod.GET)
    public String actionGetAllProvince(Model model) {
        model.addAttribute("title", "Quản lý Tỉnh Thành");
        List<ProvinceEntity> listProvince = locationService.findAllProvince();
        model.addAttribute("listProvince", listProvince);
        return "backend/location/index";
    }

    @RequestMapping(value = "/location/district.html", method = RequestMethod.GET)
    public String actionGetAllDistrict(Model model,
            @RequestParam(name = "pid") String provinceID,
            RedirectAttributes redirectAttributes) {
        List<DistrictEntity> listDistrict = locationService.findDistrictByProvinceID(provinceID);
        ProvinceEntity provinceEntity = locationService.findProvince(provinceID);
        if (provinceEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tỉnh thành có mã %s", provinceID));
            return redirect(UrlUtils.locationIndex());
        }
        model.addAttribute("title", "Quản lý quận huyện");
        model.addAttribute("provinceName", provinceEntity.getName());
        model.addAttribute("listDistrict", listDistrict);
        model.addAttribute("provinceID", provinceID);
        return "backend/location/district";
    }

    @RequestMapping(value = "/location/province-update.html", method = RequestMethod.GET)
    public String actionProvinceDetail(Model model,
            @RequestParam(name = "pid") String provinceID,
            RedirectAttributes redirectAttributes,
            ProvinceForm form) {
        ProvinceEntity provinceEntity = locationService.findProvince(provinceID);
        if (provinceEntity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tỉnh thành có mã %s", provinceID));
            return redirect(UrlUtils.locationIndex());
        }
        form.setID(provinceEntity.getID());
        form.setName(provinceEntity.getName());
        form.setPosition(provinceEntity.getPosition());
        form.setType(provinceEntity.getType());
        form.setElogCode(provinceEntity.getElogCode());
        form.setHivInfoCode(provinceEntity.getHivInfoCode());
        form.setPqmCode(provinceEntity.getPqmCode());
        model.addAttribute("form", form);
        model.addAttribute("title", "Cập nhật tỉnh thành");
        model.addAttribute("types", getProvinceTypeOption());
        return "backend/location/province-detail";
    }

    @RequestMapping(value = "/location/province-update.html", method = RequestMethod.POST)
    public String actionUpdateProvince(Model model,
            @ModelAttribute("form") @Valid ProvinceForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        ProvinceEntity provinceEntity = new ProvinceEntity();
        provinceValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Cập nhật thông tin tỉnh thành");
            model.addAttribute("types", getProvinceTypeOption());
            return "backend/location/province-detail";
        }
        provinceEntity.setID(form.getID());
        provinceEntity.setName(form.getName());
        provinceEntity.setPosition(form.getPosition());
        provinceEntity.setType(form.getType());
        provinceEntity.setElogCode(form.getElogCode());
        provinceEntity.setHivInfoCode(form.getHivInfoCode());
        provinceEntity.setPqmCode(form.getPqmCode());
        locationService.addProvince(provinceEntity);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        return redirect(UrlUtils.locationIndex());
    }

    @RequestMapping(value = "/location/province-delete.html", method = RequestMethod.GET)
    public String actionDeleteProvince(Model model,
            @RequestParam(name = "pid") String provinceID,
            RedirectAttributes redirectAttributes) {
        ProvinceEntity province = locationService.findProvince(provinceID);
        if (province != null) {
            locationService.deleteProvince(province);
        }
        redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        return redirect(UrlUtils.locationIndex());
    }

    @RequestMapping(value = "/location/province-add.html", method = RequestMethod.GET)
    public String actionAddProvinces(Model model,
            ProvinceForm form) {
        model.addAttribute("form", form);
        model.addAttribute("title", "Thêm tỉnh thành");
        model.addAttribute("types", getProvinceTypeOption());
        return "backend/location/province-add";
    }

    @RequestMapping(value = "/location/province-add.html", method = RequestMethod.POST)
    public String actionAddProvince(Model model,
            @ModelAttribute("form") @Valid ProvinceForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        ProvinceEntity provinceEntity = new ProvinceEntity();
        provinceValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Thêm tỉnh thành");
            model.addAttribute("types", getProvinceTypeOption());
            return "backend/location/province-add";
        }
        provinceEntity.setID(form.getID());
        provinceEntity.setName(form.getName());
        provinceEntity.setPosition(form.getPosition());
        provinceEntity.setType(form.getType());
        provinceEntity.setElogCode(form.getElogCode());
        provinceEntity.setHivInfoCode(form.getHivInfoCode());
        provinceEntity.setPqmCode(form.getPqmCode());
        locationService.addProvince(provinceEntity);
        if (locationService.findProvince(provinceEntity.getID()) != null) {
            redirectAttributes.addFlashAttribute("success", "Thêm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thêm thất bại");
        }
        return redirect(UrlUtils.locationIndex());
    }

    //District
    @RequestMapping(value = "/location/district-update.html", method = RequestMethod.GET)
    public String actionDistrictDetail(Model model,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            RedirectAttributes redirectAttributes,
            DistrictForm form) {
        DistrictEntity districtEntity = locationService.findDistrict(districtID);
        if (districtEntity == null || locationService.findProvince(provinceID) == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quận huyện có mã %s", districtID));
            return redirect(UrlUtils.districtIndex(provinceID));
        }
        if (locationService.findDistrict(provinceID, districtID)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quận huyện theo yêu cầu"));
            return redirect(UrlUtils.districtIndex(provinceID));
        }
        form.setID(districtEntity.getID());
        form.setName(districtEntity.getName());
        form.setPosition(districtEntity.getPosition());
        form.setProvinceID(districtEntity.getProvinceID());
        form.setType(districtEntity.getType());
        form.setElogCode(districtEntity.getElogCode());
        form.setHivInfoCode(districtEntity.getHivInfoCode());
        form.setPqmCode(districtEntity.getPqmCode());
        model.addAttribute("form", form);
        model.addAttribute("provinceID", provinceID);
        model.addAttribute("districtID", districtID);
        model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
        model.addAttribute("types", getDistrictTypeOption());
        model.addAttribute("title", "Cập nhật quận huyện");
        return "backend/location/district-detail";
    }

    @RequestMapping(value = "/location/district-add.html", method = RequestMethod.GET)
    public String actionAddDistricts(Model model,
            DistrictForm form,
            @RequestParam(name = "pid") String provinceID,
            RedirectAttributes redirectAttributes) {
        if (locationService.findProvince(provinceID) == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tỉnh thành có mã %s", provinceID));
            return redirect(UrlUtils.locationIndex());
        }
        form.setProvinceID(provinceID);
        model.addAttribute("form", form);
        model.addAttribute("title", "Thêm quận huyện");
        model.addAttribute("provinceID", provinceID);
        model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
        model.addAttribute("types", getDistrictTypeOption());
        return "backend/location/district-add";
    }

    @RequestMapping(value = "/location/district-update.html", method = RequestMethod.POST)
    public String actionUpdateDistrict(Model model,
            @ModelAttribute("form") @Valid DistrictForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID) {
        DistrictEntity districtEntity = new DistrictEntity();
        districtValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Cập nhật thông tin huyện");
            model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
            model.addAttribute("provinceID", provinceID);
            model.addAttribute("districtID", districtID);
            model.addAttribute("types", getDistrictTypeOption());
            return "backend/location/district-detail";
        }
        districtEntity.setID(form.getID());
        districtEntity.setName(form.getName());
        districtEntity.setPosition(form.getPosition());
        districtEntity.setProvinceID(provinceID);
        districtEntity.setType(form.getType());
        districtEntity.setElogCode(form.getElogCode());
        districtEntity.setHivInfoCode(form.getHivInfoCode());
        districtEntity.setPqmCode(form.getPqmCode());
        locationService.addDistrict(districtEntity);
        model.addAttribute("provinceID", provinceID);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
        return redirect(UrlUtils.districtIndex(provinceID));
    }

    @RequestMapping(value = "/location/ward.html", method = RequestMethod.GET)
    public String actionGetAllWard(Model model,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            RedirectAttributes redirectAttributes) {
        List<WardEntity> listWard = locationService.findWardByDistrictID(districtID);
        DistrictEntity districtEntity = locationService.findDistrict(districtID);
        if (districtEntity == null || locationService.findProvince(provinceID) == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quận huyện có mã %s", districtID));
            return redirect(UrlUtils.districtIndex(provinceID));
        }
        model.addAttribute("title", "Quản lý phường xã");
        model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
        model.addAttribute("districtName", districtEntity.getName());
        model.addAttribute("listWard", listWard);
        model.addAttribute("provinceID", provinceID);
        model.addAttribute("districtID", districtID);
        return "backend/location/ward";
    }

    @RequestMapping(value = "/location/district-delete.html", method = RequestMethod.GET)
    public String actionDeleteDistrict(Model model,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            RedirectAttributes redirectAttributes) {
        locationService.deleteDistrict(districtID);
        if (locationService.findDistrict(districtID) == null) {
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Xóa thất bại");
        }
        return redirect(UrlUtils.districtIndex(provinceID));
    }

    @RequestMapping(value = "/location/district-add.html", method = RequestMethod.POST)
    public String actionAddDistrict(Model model,
            @ModelAttribute("form") @Valid DistrictForm form,
            BindingResult bindingResult,
            @RequestParam(name = "pid") String provinceID,
            RedirectAttributes redirectAttributes) {
        DistrictEntity districtEntity = new DistrictEntity();
        districtValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Thêm quận huyện");
            model.addAttribute("provinceID", provinceID);
            model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
            return "backend/location/district-add";
        }
        districtEntity.setID(form.getID());
        districtEntity.setName(form.getName());
        districtEntity.setPosition(form.getPosition());
        districtEntity.setProvinceID(provinceID);
        districtEntity.setType(form.getType());
        districtEntity.setElogCode(form.getElogCode());
        districtEntity.setHivInfoCode(form.getHivInfoCode());
        locationService.addDistrict(districtEntity);
        if (locationService.findDistrict(districtEntity.getID()) != null) {
            redirectAttributes.addFlashAttribute("success", "Thêm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thêm thất bại");
        }
        return redirect(UrlUtils.districtIndex(provinceID));
    }

    //Ward
    @RequestMapping(value = "/location/ward-add.html", method = RequestMethod.GET)
    public String actionAddWards(Model model,
            WardForm form,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            RedirectAttributes redirectAttributes) {
        if (locationService.findDistrict(provinceID, districtID)) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quận huyện theo yêu cầu"));
            return redirect(UrlUtils.districtIndex(provinceID));
        }
        Map<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        form.setDistrictID(districtID);
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("title", "Thêm phường xã");
        model.addAttribute("provinceID", provinceID);
        model.addAttribute("districtID", districtID);
        model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
        model.addAttribute("districtName", locationService.findDistrict(districtID).getName());
        model.addAttribute("types", getWardTypeOption());
        return "backend/location/ward-add";
    }

    @RequestMapping(value = "/location/ward-add.html", method = RequestMethod.POST)
    public String actionAddWard(Model model,
            @ModelAttribute("form") @Valid WardForm form,
            BindingResult bindingResult,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            RedirectAttributes redirectAttributes) {
        if (locationService.findProvince(provinceID) == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tỉnh thành có mã %s", provinceID));
            return redirect(UrlUtils.wardIndex(provinceID, districtID));
        }
        Map<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        WardEntity wardEntity = new WardEntity();
        wardValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Thêm phường xã");
            model.addAttribute("status", status);
            model.addAttribute("provinceID", provinceID);
            model.addAttribute("districtID", districtID);
            model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
            model.addAttribute("districtName", locationService.findDistrict(districtID).getName());
            model.addAttribute("types", getWardTypeOption());
            return "backend/location/ward-add";
        }
        wardEntity.setID(form.getID());
        wardEntity.setName(form.getName());
        wardEntity.setPosition(form.getPosition());
        wardEntity.setDistrictID(districtID);
        wardEntity.setType(form.getType());
        wardEntity.setElogCode(form.getElogCode());
        wardEntity.setHivInfoCode(form.getHivInfoCode());
        wardEntity.setActive(!form.getActive().equals("1"));
        locationService.addWard(wardEntity);
        if (locationService.findWard(wardEntity.getID()) != null) {
            model.addAttribute("title", "Quản lý phường xã");
            redirectAttributes.addFlashAttribute("success", "Thêm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thêm thất bại");
        }
        return redirect(UrlUtils.wardIndex(provinceID, districtID));
    }

    @RequestMapping(value = "/location/ward-delete.html", method = RequestMethod.GET)
    public String actionDeleteWard(Model model,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            @RequestParam(name = "wid") String wardID,
            RedirectAttributes redirectAttributes) {
        locationService.deleteWard(wardID);
        if (locationService.findWard(wardID) == null) {
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } else {
            redirectAttributes.addFlashAttribute("success", "Xóa thất bại");
        }
        return redirect(UrlUtils.wardIndex(provinceID, districtID));
    }

    @RequestMapping(value = "/location/ward-update.html", method = RequestMethod.GET)
    public String actionWardDetail(Model model,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            @RequestParam(name = "wid") String wardID,
            RedirectAttributes redirectAttributes,
            WardForm form) {
        WardEntity wardEntity = locationService.findWard(wardID);
        if (wardEntity == null || locationService.findDistrict(districtID) == null || locationService.findProvince(provinceID) == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy phường xã có mã %s", wardID));
            return redirect(UrlUtils.wardIndex(provinceID, districtID));
        }
        if (locationService.findWard(provinceID, districtID, wardID)) {
            if (locationService.findDistrict(provinceID, districtID)) {
                redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quận huyện theo yêu cầu"));
                return redirect(UrlUtils.districtIndex(provinceID));
            }
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy phường xã theo yêu cầu"));
            return redirect(UrlUtils.wardIndex(provinceID, districtID));
        }
        Map<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        form.setID(wardEntity.getID());
        form.setName(wardEntity.getName());
        form.setPosition(wardEntity.getPosition());
        form.setDistrictID(wardEntity.getDistrictID());
        form.setType(wardEntity.getType());
        form.setElogCode(wardEntity.getElogCode());
        form.setHivInfoCode(wardEntity.getHivInfoCode());
        form.setActive(wardEntity.isActive() ? "0" : "1");
        model.addAttribute("form", form);
        model.addAttribute("status", status);
        model.addAttribute("provinceID", provinceID);
        model.addAttribute("districtID", districtID);
        model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
        model.addAttribute("districtName", locationService.findDistrict(districtID).getName());
        model.addAttribute("wardID", wardID);
        model.addAttribute("title", "Cập nhật phường xã");
        model.addAttribute("types", getWardTypeOption());
        return "backend/location/ward-detail";
    }

    @RequestMapping(value = "/location/ward-update.html", method = RequestMethod.POST)
    public String actionUpdateWard(Model model,
            @ModelAttribute("form") @Valid WardForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "pid") String provinceID,
            @RequestParam(name = "did") String districtID,
            @RequestParam(name = "wid") String wardID) {
        WardEntity wardEntity = new WardEntity();
        wardValidate.validate(form, bindingResult);
        Map<String, String> status = new HashMap<>();
        status.put("", "Tất cả");
        status.put("0", "Hoạt động");
        status.put("1", "Tạm khóa");

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Cập nhật phường xã");
            model.addAttribute("provinceID", provinceID);
            model.addAttribute("districtID", districtID);
            model.addAttribute("status", status);
            model.addAttribute("provinceName", locationService.findProvince(provinceID).getName());
            model.addAttribute("districtName", locationService.findDistrict(districtID).getName());
            model.addAttribute("wardID", wardID);
            model.addAttribute("types", getWardTypeOption());
            return "backend/location/ward-detail";
        }
        wardEntity.setID(form.getID());
        wardEntity.setName(form.getName());
        wardEntity.setPosition(form.getPosition());
        wardEntity.setDistrictID(districtID);
        wardEntity.setType(form.getType());
        wardEntity.setElogCode(form.getElogCode());
        wardEntity.setHivInfoCode(form.getHivInfoCode());
        wardEntity.setActive(!form.getActive().equals("1"));
        locationService.addWard(wardEntity);
        model.addAttribute("title", "Quản lý phường xã");
        redirectAttributes.addFlashAttribute("success", "Cập nhật phường xã thành công");
        return redirect(UrlUtils.wardIndex(provinceID, districtID));
    }

    @RequestMapping(value = "/location/sync.html", method = RequestMethod.GET)
    public String actionSync(Model model,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "type") String type,
            RedirectAttributes redirectAttributes) {
        try {
            if (code.equals("province") && type.equals("hiv-info")) {
                Map<String, String> items = new HashMap<>();
                for (Item item : hivInfoAPIUtils.getTinhThanh()) {
                    items.put(TextUtils.removemarks(item.getName()), String.valueOf(item.getId()));
                }
                List<ProvinceEntity> entitys = locationService.findAllProvince();
                for (ProvinceEntity entity : entitys) {
                    entity.setHivInfoCode(items.getOrDefault(TextUtils.removemarks(entity.getName().replaceAll(entity.getType(), "")).trim(), null));
                }
                locationService.saveProvince(entitys);
                redirectAttributes.addFlashAttribute("success", "Đồng bộ tỉnh thành với HIV Info thành công!");
                return redirect(UrlUtils.locationIndex());
            }

            if (code.equals("district") && type.equals("hiv-info")) {
                Map<String, String> items = new HashMap<>();
                Map<String, String> mProvinces = new HashMap<>();

                for (ProvinceEntity provinceEntity : locationService.findAllProvince()) {
                    mProvinces.put(provinceEntity.getID(), provinceEntity.getHivInfoCode());
                }
                for (District item : hivInfoAPIUtils.getQuanHuyen()) {
                    items.put(TextUtils.removemarks(item.getName().replaceAll("TX", "").replaceAll("Tx\\.", "").replaceAll("H\\.", "")) + item.getIdtinh(), String.valueOf(item.getId()));
                }
                List<DistrictEntity> entitys = locationService.findAllDistrict();

                for (DistrictEntity entity : entitys) {
                    String key = entity.getName();
                    if (!entity.getName().matches("(.)*(\\d)(.)*")) {
                        key = key.replaceAll(entity.getType(), "").replaceAll("Huyện", "").replaceAll("Thị xã", "");
                    }
                    entity.setHivInfoCode(items.getOrDefault(TextUtils.removemarks(key) + mProvinces.get(entity.getProvinceID()), null));
                }
                locationService.saveDistrict(entitys);
                redirectAttributes.addFlashAttribute("success", "Đồng bộ quận huyện với HIV Info thành công!");
                return redirect(UrlUtils.locationIndex());
            }

            if (code.equals("ward") && type.equals("hiv-info")) {
                Map<String, String> items = new HashMap<>();
                Map<String, DistrictEntity> mDistricts = new HashMap<>();
                Map<String, String> mProvinces = new HashMap<>();

                for (ProvinceEntity item : locationService.findAllProvince()) {
                    mProvinces.put(item.getID(), item.getHivInfoCode());
                }
                for (DistrictEntity item : locationService.findAllDistrict()) {
                    mDistricts.put(item.getID(), item);
                }

                for (Ward item : hivInfoAPIUtils.getPhuongXa()) {
                    String key = TextUtils.removemarks(item.getName().replaceAll("  ", " ")) + "-" + item.getIddistrict();
                    items.put(key, String.valueOf(item.getId()));
                }
                List<WardEntity> entitys = locationService.findAllWard();
                List<WardEntity> models = new ArrayList<>();
                for (WardEntity entity : entitys) {
                    if (entity.getHivInfoCode() != null && !entity.getHivInfoCode().equals("")) {
                        continue;
                    }
                    DistrictEntity dItem = mDistricts.get(entity.getDistrictID());
                    if (!dItem.getProvinceID().equals("72") && !dItem.getProvinceID().equals("82")) {
                        continue;
                    }

//                    String key = TextUtils.removemarks(entity.getName().replaceAll(entity.getType(), "")).trim() + "-" + mDistricts.get(entity.getDistrictID());
                    String name = entity.getName();
                    name = name.replaceAll(entity.getType(), "").trim();
                    if (name.startsWith("Thị trấn")) {
                        name = name.replaceAll("Thị trấn", "tt").trim();
                    }
                    String key = TextUtils.removemarks(name).trim() + "-" + dItem.getHivInfoCode();

                    System.out.println(entity.getName() + ", " + dItem.getName() + " - " + entity.getDistrictID() + " / " + key);

                    entity.setHivInfoCode(items.getOrDefault(key, null));
                    models.add(entity);
                    if (models.size() >= 200) {
                        locationService.saveWard(models);
                        models = new ArrayList<>();
                    }
                }
                locationService.saveWard(models);
                redirectAttributes.addFlashAttribute("success", "Đồng bộ phường xã với HIV Info thành công!");
                return redirect(UrlUtils.locationIndex());
            }

        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error(e.getMessage());
        }
        redirectAttributes.addFlashAttribute("error", "Thông tin cấu hình đồng bộ không chính xác");
        return redirect(UrlUtils.locationIndex());
    }
}
