package com.gms.controller.backend;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.components.hivinfo.CallUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.ParameterForm;
import com.gms.entity.validate.ParameterValidate;
import com.gms.service.LocationsService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Văn Thành
 */
@Controller(value = "backend_parameter")
public class ParameterController extends BaseController {
    
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ParameterValidate parameterValidate;
    @Autowired
    private CallUtils hivInfoAPIUtils;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private SiteService siteService;

    /**
     * Danh sách option theo kiểu
     *
     * @param type
     * @param isOnlyParent
     * @return
     */
    private HashMap<Long, String> getTypeOption(String type, boolean isOnlyParent) {
        HashMap<Long, String> list = new HashMap<>();
        list.put(Long.valueOf("0"), "Cấp cao nhất");
        for (ParameterEntity entity : parameterService.findByType(type)) {
            if (isOnlyParent && entity.getParentID() > 0) {
                continue;
            }
            list.put(entity.getID(), String.format("%s", entity.getValue()));
        }
        return list;
    }
    
    private HashMap<String, String> getSiteOption() {
        HashMap<String, String> items = new HashMap<>();
        items.put("", "Chọn cơ sở");
        for (SiteEntity site : siteService.findAll()) {
            items.put(String.valueOf(site.getID()), site.getName());
        }
        return items;
    }
    
    private HashMap<Integer, String> getActiveLabel() {
        HashMap<Integer, String> list = new HashMap<>();
        list.put(BooleanEnum.FALSE.getKey(), "Tạm khoá");
        list.put(BooleanEnum.TRUE.getKey(), "Hoạt động");
        return list;
    }

    /**
     * Convert dữ liệu sang định dạng map title, des, icon theo parameter type
     *
     * @author vvthanh
     * @param type
     * @return
     */
    private HashMap<String, String> buildTypeParameter(String type) {
        return buildFinalParameter().get(type);
    }

    /**
     * Convert dữ liệu sang định dạng phục vụ page define
     *
     * @author vvthanh
     * @return
     */
    private HashMap<String, HashMap<String, String>> buildFinalParameter() {
        HashMap<String, HashMap<String, String>> dataModels = new LinkedHashMap<>();
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);
        
        for (Map.Entry<String, String> entry : getFinalParameter().entrySet()) {
            String key = entry.getKey();
            if (dataModels.get(key) == null) {
                dataModels.put(key, new HashMap<String, String>());
                dataModels.get(key).put("title", "");
                dataModels.get(key).put("icon", "");
                dataModels.get(key).put("description", "");
                dataModels.get(key).put("hivinfo", "");
                dataModels.get(key).put("elog", "");
                dataModels.get(key).put("sitemap", "");
                dataModels.get(key).put("useparent", "");
                dataModels.get(key).put("pqm", "");
            }
        }
        
        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2) {
                continue;
            }
            if (dataModels.get(splitCode[0]) == null) {
                continue;
            }
            dataModels.get(splitCode[0]).put(splitCode[1], parameter.getValue());
        }
        return dataModels;
    }

    /**
     * Page Tham số hệ thống
     *
     * @author vvthanh
     * @param model
     * @param attribute01
     * @param attribute02
     * @param attribute03
     * @param attribute04
     * @param attribute05
     * @param code
     * @param value
     * @param siteID
     * @param type
     * @param parentID
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = {"/parameter/index.html"})
    public String actionIndex(Model model,
            @RequestParam(name = "attribute01", required = false, defaultValue = "") String attribute01,
            @RequestParam(name = "attribute02", required = false, defaultValue = "") String attribute02,
            @RequestParam(name = "attribute03", required = false, defaultValue = "") String attribute03,
            @RequestParam(name = "attribute04", required = false, defaultValue = "") String attribute04,
            @RequestParam(name = "attribute05", required = false, defaultValue = "") String attribute05,
            @RequestParam(name = "code", required = false, defaultValue = "") String code,
            @RequestParam(name = "value", required = false, defaultValue = "") String value,
            @RequestParam(name = "site_id", required = false, defaultValue = "0") Long siteID,
            @RequestParam(name = "ptype", required = false, defaultValue = ParameterEntity.COUNTRY) String type,
            @RequestParam(name = "pparent", required = false, defaultValue = "0") Long parentID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "100") int size) {
        
        HashMap<String, HashMap<String, String>> finalParameter = buildFinalParameter();
        
        model.addAttribute("title", "Tham số hệ thống");
        model.addAttribute("dataModels", finalParameter);
        model.addAttribute("type", type);
        if (finalParameter.get(type).get("sitemap").equals("1")) {
            model.addAttribute("siteOptions", getSiteOption());
        }
        DataPage<ParameterEntity> dataPage = null;
        if (finalParameter.get(type).get("useparent").equals("1")) {
            model.addAttribute("parentModels", parameterService.findOne(parentID));
            dataPage = parameterService.findByTypeAndParent(code, value, siteID, parentID, type, page, size);
        } else {
            dataPage = parameterService.findByType(code, value, siteID, type, attribute01, attribute02, attribute03, attribute04, attribute05, page, size);
        }
        
        model.addAttribute("dataPage", dataPage);
        return "backend/parameter/index";
    }

    /**
     * Định nghĩa tiêu đề, mô tả ngắn, icon hiển thị ở menu theo parameter type
     *
     * @author vvthanh
     * @param model
     * @return
     */
    @GetMapping(value = {"/parameter/define.html"})
    public String actionDefine(Model model) {
        model.addAttribute("parent_title", "Tham số hệ thống");
        model.addAttribute("title", "Định nghĩa");
        model.addAttribute("parameterTypes", getFinalParameter());
        model.addAttribute("dataModels", buildFinalParameter());
        return "backend/parameter/define";
    }
    
    @GetMapping(value = {"/parameter/new.html"})
    public String actionNew(Model model,
            RedirectAttributes redirectAttributes,
            ParameterForm form,
            @RequestParam(name = "ptype") String type) {
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        if (buildTypeParameter.get("sitemap").equals("1")) {
            form.setProvinceID("");
            form.setSiteID(Long.valueOf(0));
            model.addAttribute("siteOptions", getSiteOption());
        }
        
        form.setType(type);
        form.setStatus(BooleanEnum.TRUE.getKey());
        form.setPosition(1);
        form.setParentID(0);
        
        model.addAttribute("parent_title", String.format("Tham số %s", buildTypeParameter.get("title")));
        model.addAttribute("title", "Thêm mới");
        model.addAttribute("activeLabel", getActiveLabel());
        model.addAttribute("typeParameter", buildTypeParameter);
        model.addAttribute("dataModels", buildFinalParameter());
        model.addAttribute("type", type);
        model.addAttribute("form", form);
        model.addAttribute("parents", getTypeOption(type, true));
        
        return "backend/parameter/new";
    }
    
    @PostMapping(value = {"/parameter/new.html"})
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid ParameterForm form,
            BindingResult bindingResult,
            @RequestParam(name = "ptype") String type,
            RedirectAttributes redirectAttributes) {
        
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        model.addAttribute("parent_title", String.format("Tham số %s", buildTypeParameter.get("title")));
        model.addAttribute("title", "Thêm mới");
        model.addAttribute("activeLabel", getActiveLabel());
        model.addAttribute("typeParameter", buildTypeParameter);
        model.addAttribute("dataModels", buildFinalParameter());
        model.addAttribute("type", type);
        model.addAttribute("form", form);
        model.addAttribute("parents", getTypeOption(type, true));
        
        parameterValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Thêm tham số thất bại!");
            return "backend/parameter/new";
        }
        
        if (buildTypeParameter.get("sitemap").equals("1")) {
            model.addAttribute("siteOptions", getSiteOption());
        }
        
        try {
            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setCode(form.getCode());
            parameterEntity.set(form);
            parameterEntity = parameterService.save(parameterEntity);
            if (parameterEntity == null) {
                throw new Exception(String.format("Thêm dữ liệu tham số %s  thất bại!", buildTypeParameter.get("title")));
            }
            redirectAttributes.addFlashAttribute("success", String.format("Thêm dữ liệu tham số %s thành công", buildTypeParameter.get("title")));
            redirectAttributes.addFlashAttribute("success_id", parameterEntity.getID());
            return redirect(UrlUtils.parameterIndex(type));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "backend/parameter/new";
        }
    }
    
    @GetMapping(value = {"/parameter/remove.html"})
    public String actionRemove(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "ptype") String type,
            @RequestParam(name = "oid") Long paramID) {
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        ParameterEntity entity = parameterService.findOne(paramID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tham số có mã %s", paramID));
            return redirect(UrlUtils.parameterIndex(type));
        }
        if (entity.getType() == null || !entity.getType().equals(type)) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s không thuộc %s", entity.getValue(), buildTypeParameter.get("title")));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        parameterService.remove(entity);
        redirectAttributes.addFlashAttribute("success", String.format("Xoá tham số thành công", paramID));
        return redirect(UrlUtils.parameterIndex(type));
    }
    
    @GetMapping(value = {"/parameter/update.html"})
    public String actionUpdate(Model model,
            RedirectAttributes redirectAttributes,
            ParameterForm form,
            @RequestParam(name = "ptype") String type,
            @RequestParam(name = "oid") Long paramID) {
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        ParameterEntity entity = parameterService.findOne(paramID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tham số có mã %s", paramID));
            return redirect(UrlUtils.parameterIndex(type));
        }
        if (entity.getType() == null || !entity.getType().equals(type)) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s không thuộc %s", entity.getValue(), buildTypeParameter.get("title")));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        form.setID(entity.getID());
        form.setCode(entity.getCode());
        form.setValue(entity.getValue());
        form.setType(entity.getType());
        form.setStatus(entity.getStatus());
        form.setPosition(entity.getPosition());
        form.setParentID(entity.getParentID());
        form.setNote(entity.getNote());
        form.setHivInfoCode(entity.getHivInfoCode());
        form.setElogCode(entity.getElogCode());
        form.setPqmCode(entity.getPqmCode());
        
        if (form.getType().equals(ParameterEntity.TREATMENT_REGIMEN)) {
            form.setAttribute01(entity.getAttribute01());
            form.setAttribute02(entity.getAttribute02());
            form.setAttribute03(entity.getAttribute03());
            form.setAttribute04(entity.getAttribute04());
            form.setAttribute05(entity.getAttribute05());
        }
        
        if (buildTypeParameter.get("sitemap").equals("1")) {
            form.setProvinceID(entity.getProvinceID() == null ? "" : entity.getProvinceID());
            form.setSiteID(entity.getSiteID() == null ? Long.valueOf("0") : entity.getSiteID());
            model.addAttribute("siteOptions", getSiteOption());
        }
        model.addAttribute("parent_title", String.format("Tham số %s", buildTypeParameter.get("title")));
        model.addAttribute("title", "Cập nhật");
        model.addAttribute("activeLabel", getActiveLabel());
        model.addAttribute("typeParameter", buildTypeParameter);
        model.addAttribute("dataModels", buildFinalParameter());
        model.addAttribute("type", type);
        model.addAttribute("form", form);
        model.addAttribute("parameter", entity);
        model.addAttribute("parents", getTypeOption(type, true));
        
        return "backend/parameter/update";
    }
    
    @PostMapping(value = {"/parameter/update.html"})
    public String doActionUpdate(Model model,
            @ModelAttribute("form") @Valid ParameterForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "ptype") String type,
            @RequestParam(name = "oid") Long paramID) {
        
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        ParameterEntity entity = parameterService.findOne(paramID);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy tham số có mã %s", paramID));
            return redirect(UrlUtils.parameterIndex(type));
        }
        if (entity.getType() == null || !entity.getType().equals(type)) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s không thuộc %s", entity.getValue(), buildTypeParameter.get("title")));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        if (buildTypeParameter.get("sitemap").equals("1")) {
            model.addAttribute("siteOptions", getSiteOption());
        }
        
        model.addAttribute("parent_title", String.format("Tham số %s", buildTypeParameter.get("title")));
        model.addAttribute("title", "Cập nhật");
        model.addAttribute("activeLabel", getActiveLabel());
        model.addAttribute("typeParameter", buildTypeParameter);
        model.addAttribute("dataModels", buildFinalParameter());
        model.addAttribute("type", type);
        model.addAttribute("form", form);
        model.addAttribute("parameter", entity);
        model.addAttribute("parents", getTypeOption(type, true));
        
        parameterValidate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Cập nhật tham số thất bại!");
            return "backend/parameter/update";
        }
        
        try {
            entity.set(form);
            entity = parameterService.save(entity);
            if (entity == null) {
                throw new Exception(String.format("Cập nhật dữ liệu tham số %s  thất bại!", buildTypeParameter.get("title")));
            }
            redirectAttributes.addFlashAttribute("success", String.format("Cập nhật dữ liệu tham số %s thành công", buildTypeParameter.get("title")));
            redirectAttributes.addFlashAttribute("success_id", entity.getID());
            return redirect(UrlUtils.parameterIndex(type));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "backend/parameter/update";
        }
    }
    
    @GetMapping(value = {"/parameter/synchronize-hivinfo.html"})
    public String actionSynchronize(Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "ptype") String type) throws Exception {
        
        HashMap<String, String> buildTypeParameter = buildTypeParameter(type);
        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            redirectAttributes.addFlashAttribute("error", String.format("Tham số %s chưa được cấu hình.", type));
            return redirect(UrlUtils.parameterIndex(type));
        }
        
        HashMap<String, String> change = null;
        if (type.equals(ParameterEntity.MODE_OF_TRANSMISSION)) {
            build(hivInfoAPIUtils.getDuongLay(), parameterService.getModesOfTransmision(), change);
        } else if (type.equals(ParameterEntity.JOB)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getCongviec();
            List<ParameterEntity> models = parameterService.getJob();
            change = new HashMap<>();
            change.put("3", "Cán bộ, chiến sỹ thuộc lực lượng vũ trang nhân dân");
            change.put("5", "Công chức, viên chức, người lao động có hợp đồng lao động theo quy định của pháp luật");
            change.put("7", "Học sinh, sinh viên");
            change.put("10", "Nhân viên cơ sở kinh doanh dịch vụ dễ bị lợi dụng để hoạt động mại dâm");
            build(data, models, change);
        } else if (type.equals(ParameterEntity.PLACE_TEST)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getNoixetnghiem(locationsService.findAllProvince());
            List<ParameterEntity> models = parameterService.getPlaceTest();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.SYSMPTOM)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getTrieuTrung();
            List<ParameterEntity> models = parameterService.getSysmptom();
            change = new HashMap<>();
            change.put("9", "Bệnh Lao");
            build(data, models, change);
        } else if (type.equals(ParameterEntity.TREATMENT_REGIMEN)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getPhacDoDieuTri();
            List<ParameterEntity> models = parameterService.getTreatmentRegimen();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.TREATMENT_FACILITY)) {
            List<ProvinceEntity> provinces = locationsService.findAllProvince();
            //Cơ sở điều trị
            List<ParameterEntity> data = hivInfoAPIUtils.getCoSoDieuTri(provinces);
            List<ParameterEntity> models = parameterService.getTreatmentFacility();

            //Map cơ sở hiện tại trên hệ thống
            HashMap<String, ProvinceEntity> provinceOptions = new HashMap<>();
            for (ProvinceEntity province : provinces) {
                provinceOptions.put(province.getHivInfoCode(), province);
            }
            
            for (ParameterEntity item : models) {
                ProvinceEntity province = provinceOptions.get(item.getAttribute01());
                if (province == null) {
                    continue;
                }
                item.setProvinceID(province.getID());
                
                if (province.getName().contains("Quảng Ninh")
                        || province.getName().contains("Tây Ninh")
                        || province.getName().contains("Tiền Giang")
                        || province.getName().contains("Đồng Nai")) {
                    String name = hivInfoAPIUtils.getSiteName(item.getValue(), province);
                }
            }

            //Lưu tham số hệ thống
            build(data, models, change);
        } else if (type.equals(ParameterEntity.RISK_BEHAVIOR)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getNguyCoLayNhiem();
            List<ParameterEntity> models = parameterService.getRiskBehavior();
            change = new HashMap<>();
            change.put("100", "Không rõ");
            change.put("4", "Quan hệ tình dục với nhiều người (không vì tiền hay ma túy)");
            change.put("2", "Quan hệ tình dục với người bán dâm hoặc người mua dâm (vì tiền hay ma túy)");
            build(data, models, change);
        } else if (type.equals(ParameterEntity.CAUSE_OF_DEATH)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getNguyenNhanTuVong();
            List<ParameterEntity> models = parameterService.getCauseOfDeath();
            change = new HashMap<>();
            change.put("6", "Viêm phổi");
            build(data, models, change);
        } else if (type.equals(ParameterEntity.BLOOD_BASE)) {
            List<ParameterEntity> models = parameterService.getBloodBase();
            List<ParameterEntity> data = hivInfoAPIUtils.getNoiLayMau(locationsService.findAllProvince(), models);
            build(data, models, change);
        } else if (type.equals(ParameterEntity.LOCATION_MONITORING)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getDiaDiemGiamSat();
            List<ParameterEntity> models = parameterService.getLocationMonitoring();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.BIOLOGY_PRODUCT_TEST)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getSinhPhamXetNghiem();
            List<ParameterEntity> models = parameterService.getBiologyProductTest();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.COMMUNICABLE_DISEAS)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getBenhLayTruyen();
            List<ParameterEntity> models = parameterService.getCommunicableDiseas();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.LOCATION_OF_BLOOD)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getViTriLayMau();
            List<ParameterEntity> models = parameterService.getLocationOfBlood();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.GENDER)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getGioiTinh();
            List<ParameterEntity> models = parameterService.getGenders();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.STATUS_OF_RESIDENT)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getTinhTrangHienTai();
            List<ParameterEntity> models = parameterService.getStatusOfResident();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.TYPE_OF_PATIENT)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getLoaiBenhNhan();
            List<ParameterEntity> models = parameterService.getTypeOfPatient();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.STATUS_OF_TREATMENT)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getTrangThaiDieuTri();
            List<ParameterEntity> models = parameterService.getStatusOfTreatment();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.RACE)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getDantoc();
            List<ParameterEntity> models = parameterService.getRaces();
            build(data, models, change);
        } else if (type.equals(ParameterEntity.TEST_OBJECT_GROUP)) {
            List<ParameterEntity> data = hivInfoAPIUtils.getDoituong();
            List<ParameterEntity> models = parameterService.getTestObjectGroup();
            change = new HashMap<>();
            change.put("1", "Nghiện chích ma túy (NCMT)");
            change.put("2", "Người hành nghề mại dâm");
            change.put("5", "Người bán máu / hiến máu / cho máu");
            change.put("6", "Bệnh nhân lao");
            change.put("8", "Bệnh nhân nghi ngờ AIDS");
            change.put("11", "Nam quan hệ tình dục với nam (MSM)");
            change.put("15", "Phạm nhân");
            change.put("17", "Vợ/chồng/bạn tình của người nhiễm HIV");
            build(data, models, change);
        }
        
        redirectAttributes.addFlashAttribute("success", String.format("Đồng bộ dữ liệu tham số %s từ hệ thống HIV Info thành công", buildTypeParameter.get("title")));
        return redirect(UrlUtils.parameterIndex(type));
    }

    /**
     * Save tham số đồng bộ
     *
     * @auth vvThành
     * @param data
     * @param models
     */
    private void build(List<ParameterEntity> data, List<ParameterEntity> models, HashMap<String, String> change) {
        if (models == null) {
            models = new ArrayList<>();
        }
        HashMap<String, ParameterEntity> result = new HashMap<>();
        for (ParameterEntity item : data) {
            if (change != null && !change.isEmpty()) {
                for (Map.Entry<String, String> entry : change.entrySet()) {
                    if (item.getHivInfoCode().equals(entry.getKey())) {
                        item.setValue(entry.getValue());
                    }
                }
            }
            
            boolean exist = false;
            for (ParameterEntity model : models) {
                if (model.getHivInfoCode() != null && model.getHivInfoCode().equals(item.getHivInfoCode())) {
                    exist = true;
                    model.setNote(item.getNote());
                    model.setStatus(item.getStatus());
                    result.put(String.valueOf(model.getID()), model);
                } else if (TextUtils.removemarks(item.getValue()).equals(TextUtils.removemarks(model.getValue()))) {
                    exist = true;
                    if (result.get(String.valueOf(model.getID())) == null) {
                        model.setHivInfoCode(item.getHivInfoCode());
                        model.setNote(item.getNote());
                        model.setStatus(item.getStatus());
                        model.setAttribute01(item.getAttribute01());
                        model.setAttribute02(item.getAttribute02());
                        model.setAttribute03(item.getAttribute03());
                        model.setAttribute04(item.getAttribute04());
                        model.setAttribute05(item.getAttribute05());
                        model.setParentID(item.getParentID());
                        result.put(String.valueOf(model.getID()), model);
                        break;
                    }
                }
            }
            
            if (!exist) {
                item.setNote(null);
                item.setCode(String.valueOf(models.size() + result.size() + 1));
//                models.add(item);
                result.put(item.getValue(), item);
            }
        }
        for (ParameterEntity param : result.values()) {
            System.out.println("-----" + param.getCode() + ":" + param.getValue());
            parameterService.save(param);
        }

//        parameterService.save(new ArrayList<>(result.values()));
    }
}
