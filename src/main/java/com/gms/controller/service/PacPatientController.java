package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.InsuranceEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.PacHivInfoEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.PacPatientLogEntity;
import com.gms.entity.db.PacPatientRequestEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.PacNewVaacForm;
import com.gms.entity.form.PacPatientForm;
import com.gms.entity.form.PacReportUpdateForm;
import com.gms.entity.form.PacReviewForm;
import com.gms.service.LocationsService;
import com.gms.service.PacPatientRequestService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.text.ParseException;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author NamAnh_HaUI
 */
@RestController
public class PacPatientController extends BaseController {

    private static final String PHONE_NUMBER = "(^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$)|(02[0-9])+([0-9]{8}$)";

    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private PacPatientRequestService pacPatientRequestService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;

    private HashMap<String, HashMap<String, String>> getOptions() {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.JOB);
        parameterTypes.add(ParameterEntity.RACE);
        parameterTypes.add(ParameterEntity.TEST_OBJECT_GROUP);
        parameterTypes.add(ParameterEntity.MODE_OF_TRANSMISSION);
        parameterTypes.add(ParameterEntity.RISK_BEHAVIOR);
        parameterTypes.add(ParameterEntity.GENDER);
        parameterTypes.add(ParameterEntity.PLACE_TEST);
        parameterTypes.add(ParameterEntity.BLOOD_BASE);
        parameterTypes.add(ParameterEntity.TREATMENT_FACILITY);
        parameterTypes.add(ParameterEntity.STATUS_OF_TREATMENT);
        parameterTypes.add(ParameterEntity.SYSMPTOM);
        parameterTypes.add(ParameterEntity.TREATMENT_REGIMEN);
        parameterTypes.add(ParameterEntity.STATUS_OF_RESIDENT);
        parameterTypes.add(ParameterEntity.SERVICE);
        parameterTypes.add(ParameterEntity.CAUSE_OF_DEATH);
        parameterTypes.add(ParameterEntity.STATUS_OF_CHANGE_TREATMENT);

        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, null);
        return options;
    }

    @RequestMapping(value = {"/pac-new/log.json", "/pac-review/log.json", "/pac-accept/log.json", "/pac-patient/log.json", "/pac-opc/log.json"}, method = RequestMethod.GET)
    public Response<?> actionPatientLog(@RequestParam(name = "oid") Long oID) {
        List<PacPatientLogEntity> logs = pacPatientService.getLogs(oID);
        Set<Long> sIDs = new HashSet<>();
        Map<Long, String> staffs = new HashMap<>();
        sIDs.addAll(CollectionUtils.collect(logs, TransformerUtils.invokerTransformer("getStaffID")));
        for (StaffEntity staff : staffService.findAllByIDs(sIDs)) {
            staffs.put(staff.getID(), staff.getName());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("data", logs);
        data.put("staffs", staffs);
        return new Response<>(true, data);
    }

    @RequestMapping(value = {"/pac-new/log-create.json", "/pac-review/log-create.json", "/pac-accept/log-create.json", "/pac-patient/log-create.json", "/pac-opc/log-create.json"}, method = RequestMethod.POST)
    public Response<?> actionLogCreate(@RequestBody PacPatientLogEntity form, BindingResult bindingResult) {
        pacPatientService.log(form.getPatientID(), form.getContent());
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }

    /**
     * Chuyển gửi
     *
     * @author pdThang
     * @param oid
     * @return
     */
    @RequestMapping(value = "/pac-new/transfer.json", method = RequestMethod.GET)
    public Response<?> actionTransfer(@RequestParam(name = "oid") Long oid) {
        Map<String, Object> data = new HashMap();
        List<PacPatientInfoEntity> listPatient = new ArrayList<>();
        listPatient.add(pacPatientService.findOne(oid));

        HashMap<String, HashMap<String, String>> options = getOptions();
        LinkedHashMap<String, String> optionResident = new LinkedHashMap<>();
        optionResident.put("", "Chọn hiên trạng cư trú");
        for (Map.Entry<String, String> entry : options.get("status-of-resident").entrySet()) {
            optionResident.put(entry.getKey(), entry.getValue());
        }

        options.put(ParameterEntity.STATUS_OF_RESIDENT, optionResident);

        PacPatientInfoEntity entity = pacPatientService.getAddress(listPatient).get(0);
        data.put("entity", entity);
        data.put("options", options);
        return new Response<>(true, "", data);
    }

    @RequestMapping(value = "/pac-new/do-transfer.json", method = RequestMethod.POST)
    public Response<?> actionTranfer(@RequestParam(name = "oid") Long oid, @RequestParam(name = "src", defaultValue = "", required = false) String src,
            @RequestParam(name = "stt") String stt) {
        Date currentDate = new Date();
        Map<String, String> errors = new HashMap();
        if (StringUtils.isEmpty(stt)) {
            errors.put("statusOfResident", "Kết quả xác minh hiện trạng cư trú không được trống");
            return new Response<>(false, errors);
        }
        try {
            PacPatientInfoEntity entity = pacPatientService.findOne(oid);
            entity.setReviewProvinceTime(currentDate);
            entity.setDistrictID(entity.getPermanentDistrictID());
            entity.setWardID(entity.getPermanentWardID());
            entity.setCheckWardTime(null);
            entity.setCheckDistrictTime(null);
            entity.setCheckProvinceTime(null);

            if (entity.getAcceptTime() == null) {
                entity.setAcceptTime(currentDate);
            }

            if (entity.getReviewWardTime() == null) {
                entity.setReviewWardTime(currentDate);
            }

            // Kiểm tra nếu chuyển gửi sang quản lý từ biến động điều trị
            if ("bddt".equals(src)) {
                entity.setParentID(entity.getID());
            }

            entity.setStatusOfResidentID(stt);
            entity.setUpdateAt(currentDate);
            pacPatientService.save(entity);
            pacPatientService.log(entity.getID(), String.format("Chuyển sang danh sách quản lý người nhiễm ngày %s", TextUtils.formatDate(entity.getReviewProvinceTime(), FORMATDATE)));
            return new Response<>(true, "Người nhiễm đã chọn được chuyển sang quản lý thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, String.format("Lỗi cập nhật thông tin. Error: %s", e.getMessage()));
        }
    }

    /**
     * Get data
     *
     * @author DSNAnh
     * @param oids
     * @return
     */
    @RequestMapping(value = "/pac-new/get.json", method = RequestMethod.GET)
    public Response<?> actionGet(@RequestParam(name = "oid") String oids) {
        List<Long> pacIds = new ArrayList<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pacIds.add(Long.parseLong(string));
        }
        Map<String, Object> data = new HashMap();
        List<PacPatientInfoEntity> datas = pacPatientService.findAllByIds(pacIds);
        List<PacPatientInfoEntity> dataTransfer = new ArrayList<>();
        if (datas != null) {
            for (PacPatientInfoEntity model : datas) {
                if (model.getAcceptPermanentTime() == null) {
                    dataTransfer.add(model);
                }
            }
        }
        data.put("dataTransfer", dataTransfer);
        data.put("entities", datas);
        data.put("options", getOptions());
        return new Response<>(true, data);
    }

    /**
     * Yêu cầu rà soát
     *
     * @author DSNAnh
     * @param oids
     * @param tid
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/pac-new/request.json", method = RequestMethod.GET)
    public Response<?> actionRequest(@RequestParam(name = "oid") String oids, @RequestParam(name = "tid", defaultValue = "") String tid) throws Exception {
        List<Long> pacIds = new ArrayList<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pacIds.add(Long.parseLong(string));
        }
        Date currentDate = new Date();
        List<PacPatientInfoEntity> data = pacPatientService.findAllByIds(pacIds);
        List<PacPatientInfoEntity> result = new ArrayList<>();
        for (PacPatientInfoEntity entity : data) {
            entity.setAcceptTime(currentDate);
            entity.setDistrictID(entity.getPermanentDistrictID());
            entity.setWardID(entity.getPermanentWardID());
            result.add(entity);
            pacPatientService.log(entity.getID(), "Chuyển sang danh sách cần rà soát ngày " + TextUtils.formatDate(entity.getAcceptTime(), FORMATDATE));
        }
        pacPatientService.saveAll(result);

        if (StringUtils.isNotEmpty(tid)) {

            long treatmentID;
            try {
                treatmentID = Long.parseLong(tid);
            } catch (NumberFormatException e) {
                return new Response<>(false, "Không tìm thấy bệnh nhân điều trị");
            }

            // Người đang điều trị
            PacPatientInfoEntity target = pacPatientService.findOne(treatmentID);
            if (target == null) {
                return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", treatmentID));
            }

            target.setAcceptTime(currentDate);
            target.setDistrictID(target.getPermanentDistrictID());
            target.setWardID(target.getPermanentWardID());
            PacPatientInfoEntity saveTarget = pacPatientService.save(target);
            pacPatientService.log(saveTarget.getID(), String.format("Chuyển sang danh sách người nhiễm cần rà soát ngày %s", TextUtils.formatDate(saveTarget.getAcceptTime(), FORMATDATE)));
        }
        return new Response<>(true, "Người nhiễm phát hiện đã chọn được chuyển sang chờ rà soát thành công", data);
    }

    /**
     * Chuyển gửi tỉnh khác
     *
     * @author DSNAnh
     * @param oids
     * @return
     */
    @RequestMapping(value = "/pac-new/transfer-province.json", method = RequestMethod.GET)
    public Response<?> actionTransferProvince(@RequestParam(name = "oid") String oids) {
        Date currentDate = new Date();
        Map<String, String> provinceOptions = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinceOptions.put(item.getID(), item.getName());
        }

        List<Long> pacIds = new ArrayList<>();
        String[] split = oids.split(",", -1);
        for (String string : split) {
            if (StringUtils.isEmpty(string)) {
                continue;
            }
            pacIds.add(Long.parseLong(string));
        }

        List<PacPatientInfoEntity> result = new ArrayList<>();
        for (PacPatientInfoEntity entity : pacPatientService.findAllByIds(pacIds)) {
            entity.setAcceptPermanentTime(currentDate);
            entity.setProvinceID(entity.getPermanentProvinceID());
            entity.setDistrictID(entity.getPermanentDistrictID());
            entity.setWardID(entity.getPermanentWardID());
            result.add(entity);
            pacPatientService.log(entity.getID(), String.format("Người nhiễm đã được chuyển sang %s vào ngày %s", provinceOptions.getOrDefault(entity.getPermanentProvinceID(), "không rõ"), TextUtils.formatDate(currentDate, FORMATDATE)));
        }
        pacPatientService.saveAll(result);
        return new Response<>(true, "Người nhiễm đã được chuyển sang tỉnh khác thành công", result);
    }

    /**
     * Lấy thông tin rà soát
     *
     * @author DSNAnh
     * @param oid
     * @return
     */
    @RequestMapping(value = "/pac-review/get.json", method = RequestMethod.GET)
    public Response<?> actionUpdateReview(@RequestParam(name = "oid") String oid) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity model = pacPatientService.findOne(Long.parseLong(oid));
        if (model == null || model.isRemove() || !model.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        data.put("model", model);
        data.put("options", options);
        return new Response<>(true, data);
    }

    /**
     * Cập nhật thông tin rà soát
     *
     * @author DSNAnh
     * @param oid
     * @param tab
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/pac-review/update-review.json", method = RequestMethod.POST)
    public Response<?> actionUpdateResult(@RequestParam(name = "oid") String oid,
            @RequestParam(name = "tab", defaultValue = "") String tab,
            @RequestBody PacReviewForm form,
            BindingResult bindingResult) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();
        PacPatientInfoEntity model = pacPatientService.findOne(Long.parseLong(oid));
        if (model == null || model.isRemove() || !model.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        if (!form.getDeathTime().contains("/") && form.getDeathTime() != null && !"".equals(form.getDeathTime())) {
            form.setDeathTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getDeathTime()));
        }
        if (StringUtils.isNotEmpty(form.getRequestDeathTime()) && !form.getRequestDeathTime().contains("/") && form.getRequestDeathTime() != null && !"".equals(form.getRequestDeathTime())) {
            form.setRequestDeathTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getRequestDeathTime()));
        }
        if (!form.getConfirmTime().contains("/") && form.getConfirmTime() != null && !"".equals(form.getConfirmTime())) {
            form.setConfirmTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getConfirmTime()));
        }
        if (!form.getStartTreatmentTime().contains("/") && form.getStartTreatmentTime() != null && !"".equals(form.getStartTreatmentTime())) {
            form.setStartTreatmentTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getStartTreatmentTime()));
        }
        if (StringUtils.isNotBlank(form.getFullname()) && !TextUtils.removeDiacritical(form.getFullname().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.put("fullname", "Họ và tên chỉ chứa các kí tự chữ cái");
        }
        if (StringUtils.isNotEmpty(form.getYearOfBirth())) {
            if (!form.getYearOfBirth().matches(RegexpEnum.YYYY)) {
                errors.put("yearOfBirth", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }

        if (form.getStatusOfResidentID() == null || "".equals(form.getStatusOfResidentID())) {
            errors.put("statusOfResidentID", "Kết quả xác minh hiện trạng cư trú không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)) {
            errors.put("identityCard", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt");
        }
        if (StringUtils.isNotEmpty(form.getHealthInsuranceNo()) && !TextUtils.removeDiacritical(form.getHealthInsuranceNo().trim()).matches(RegexpEnum.HI_CHAR)) {
            errors.put("healthInsuranceNo", "Số thẻ BHYT không chứa ký tự đặc biệt");
        }
        try {
            Date today = new Date();
            if (StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());
                if (startTreatmentTime.compareTo(today) > 0) {
                    errors.put("startTreatmentTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getDeathTime())) {
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                if (deathTime.compareTo(today) > 0) {
                    errors.put("deathTime", "Ngày tử vong không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getRequestDeathTime())) {
                Date deathTime = sdfrmt.parse(form.getRequestDeathTime());
                if (deathTime.compareTo(today) > 0) {
                    errors.put("requestDeathTime", "Ngày báo tử vong không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(today) > 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được sau ngày hiện tại");
                }
            }
//            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getConfirmTime())) {
//                Date deathTime = sdfrmt.parse(form.getDeathTime());
//                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
//
//                if (deathTime.compareTo(confirmTime) < 0) {
//                    errors.put("deathTime", "Ngày tử vong không được trước ngày XN khẳng định");
//                }
//            }
            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());

                if (deathTime.compareTo(startTreatmentTime) < 0) {
                    errors.put("deathTime", "Ngày tử vong không được trước ngày bắt đầu điều trị");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());

                if (confirmTime.compareTo(startTreatmentTime) > 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được sau ngày bắt đầu điều trị");
                }
            }
        } catch (ParseException ex) {
        }
        form.getCauseOfDeath().remove("");
        form.getCauseOfDeath().remove("null");
        if (StringUtils.isEmpty(form.getDeathTime()) && form.getCauseOfDeath().size() > 0) {
            errors.put("deathTime", "Ngày tử vong không được để trống");
        }
        if (StringUtils.isEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getRequestDeathTime())) {
            errors.put("deathTime", "Ngày tử vong không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isEmpty(form.getRequestDeathTime())) {
            errors.put("requestDeathTime", "Ngày báo tử vong không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getDeathTime()) && form.getCauseOfDeath().isEmpty()) {
            errors.put("causeOfDeath", "Nguyên nhân tử vong không được để trống");
        }

        if (errors.size() > 0) {
            return new Response<>(false, "Cập nhật không thành công, kiểm tra lại thông tin", errors);
        }

        try {
            Object oldModel = model.clone();
            form.setModel(model);
            //Rà soát lại
            Date currentDate = new Date();
            if (tab.equals("review")) {
                model.setCheckWardTime(currentDate);
                if (isTTYT()) {
                    model.setCheckDistrictTime(currentDate);
                }
            }
            if (isTTYT()) {
                model.setReviewDistrictTime(currentDate);
            }
            //Log change
            String log = "Người nhiễm đã được rà soát.";
            String change = model.changeToString(oldModel, true);
            if (change != null && !change.equals("")) {
                log += change;
            }
            model.setHasHealthInsurance(StringUtils.isNotEmpty(model.getHealthInsuranceNo()) ? InsuranceEnum.TRUE.getKey() : null);
            pacPatientService.save(model);
            pacPatientService.log(model.getID(), log);

            //Gửi email thông báo
            SiteEntity ttyt = siteService.findByTTYT(model.getProvinceID(), model.getDistrictID());
            if (ttyt != null) {
                sendNotifyToSite(ttyt.getID(), ServiceEnum.TTYT, "Chờ duyệt ca nhiễm #" + model.getID(), "-", UrlUtils.pacAcceptIndex() + "?fullname=" + model.getFullname());
            }
            return new Response<>(true, "Người nhiễm đã được cập nhật thông tin rà soát thành công.");
        } catch (Exception ex) {
            ex.printStackTrace();
            pacPatientService.log(model.getID(), "Lỗi cập nhật thông tin. " + ex.getMessage());
            return new Response<>(false, "Lỗi cập nhật thông tin. ");
        }
    }

    @RequestMapping(value = {"/pac-new/fillter.json", "/pac-review/fillter.json", "/pac-accept/fillter.json", "/pac-patient/fillter.json"}, method = RequestMethod.GET)
    public Response<?> actionDuplicateFilter(@RequestParam(name = "oid") Long oID, @RequestParam(name = "currenttab", required = false) String currentSrc) {
        PacPatientInfoEntity model = pacPatientService.findOne(oID);
        if (model == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", oID));
        }
        LoggedUser u = getCurrentUser();
        List<PacPatientInfoEntity> filter = new ArrayList<>();

        if (isTTYT()) {
            filter = pacPatientService.duplicateFilterTTYT(model.getID(), model.getProvinceID(), u.getSite().getDistrictID(), model.getFullname(), model.getGenderID(), model.getPermanentProvinceID(), model.getPermanentDistrictID(), model.getPermanentWardID(), model.getIdentityCard(), model.getHealthInsuranceNo());
        } else {
            filter = pacPatientService.duplicateFilter(model.getID(), model.getProvinceID(), model.getFullname(), model.getGenderID(), model.getPermanentProvinceID(), model.getPermanentDistrictID(), model.getPermanentWardID(), model.getIdentityCard(), model.getHealthInsuranceNo());
        }

        List<PacPatientInfoEntity> filteredEntity = new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("model", model);
        result.put("options", getOptions());
        // TH kiểm tra trùng lắp bên BDDT bản thân đã được kết nối không hiên thị
        for (PacPatientInfoEntity item : filter) {

            if (item.getParentID().equals(oID)) {
                continue;
            }

            if (model.getParentID() != null && model.getParentID().equals(item.getID())) {
                result.put("connectedID", item.getID());
                continue;
            } else if (model.getParentID() != null) {
                result.put("connectedID", model.getParentID());
            }

            filteredEntity.add(item);
        }

        //bôi đỏ tên
        List<PacPatientForm> listFilter = new ArrayList<>();
        PacPatientForm form;
        for (PacPatientInfoEntity item : filteredEntity) {
            form = new PacPatientForm();
            form.setID(item.getID());
            form.setFormPac(item);
            if (item.getFullname() != null && model.getFullname() != null && (TextUtils.removeDiacritical(item.getFullname())).toLowerCase().equals(TextUtils.removeDiacritical(model.getFullname()).toLowerCase())) {
                form.setEqualName(true);
            }
            form.setAcceptTime(item.getAcceptTime());
            form.setReviewWardTime(item.getReviewWardTime());
            form.setReviewProvinceTime(item.getReviewProvinceTime());
            form.setPermanentAddressFull(item.getPermanentAddressFull());

            // Kiểm tra có thể kết nối điều trị
            boolean isConnected = (("bddt".equals(currentSrc) && (((ServiceEnum.OPC.getKey().equals(model.getSourceServiceID()) && (model.getParentID() == null || model.getParentID().equals(0l)))
                    || (ServiceEnum.OPC.getKey().equals(model.getSourceServiceID()) && (model.getParentID() != null && !model.getParentID().equals(0l)) && model.getAcceptTime() == null && model.getReviewProvinceTime() == null && model.getReviewWardTime() == null))
                    && ((item.getAcceptTime() == null && !ServiceEnum.OPC.getKey().equals(item.getSourceServiceID())) || (item.getAcceptTime() != null && !ServiceEnum.OPC.getKey().equals(item.getSourceServiceID()) && item.getReviewWardTime() != null
                    && item.getReviewProvinceTime() != null && !ServiceEnum.OPC.getKey().equals(item.getSourceServiceID())))))
                    || (!"bddt".equals(currentSrc) && (((!ServiceEnum.OPC.getKey().equals(model.getSourceServiceID()) && model.getAcceptTime() == null)
                    || (model.getAcceptTime() != null && model.getReviewWardTime() != null && model.getReviewProvinceTime() == null)
                    || (model.getAcceptTime() != null && model.getReviewWardTime() != null && model.getReviewProvinceTime() != null))
                    && ((item.getAcceptTime() == null && ServiceEnum.OPC.getKey().equals(item.getSourceServiceID()) || (item.getAcceptTime() != null && item.getReviewProvinceTime() == null && ServiceEnum.OPC.getKey().equals(item.getSourceServiceID()))) && (item.getParentID() == null || item.getParentID().equals(0l))))));
            form.setIsConnect(isConnected);
            // End

            listFilter.add(form);
        }
        result.put("filter", listFilter);
        return new Response<>(true, result);
    }

    /**
     * Hiển thị thông tin trùng lặp khi kết nối
     *
     * @auth TrangBN
     * @param target_id
     * @param oID
     * @return
     */
    @RequestMapping(value = {"/pac-new/connect.json",}, method = RequestMethod.GET)
    public Response<?> actionDuplicateConnectFilter(@RequestParam(name = "oid") Long oID, @RequestParam(name = "tid") Long target_id) {

        PacPatientInfoEntity model = pacPatientService.findOne(oID);
        PacPatientInfoEntity target = pacPatientService.findOne(target_id);

        if (model == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", oID));
        }

        if (target == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", target_id));
        }

        // Get address full
        List<PacPatientInfoEntity> modelList = new ArrayList<>();
        modelList.add(model);
        modelList = pacPatientService.getAddress(modelList);
        model = modelList.get(0);

        modelList = new ArrayList<>();
        modelList.add(target);
        modelList = pacPatientService.getAddress(modelList);
        target = modelList.get(0);

        Map<String, Object> result = new HashMap<>();
        result.put("model", model);
        result.put("target", target);
        result.put("options", getOptions());
        return new Response<>(true, result);
    }

    /**
     * Cập nhật trạng thái bênh nhân sau khi update
     *
     * @param currentTarget
     * @auth TrangBN
     * @param oID
     * @param tID
     * @return
     */
    @RequestMapping(value = {"/pac-new/update-connected.json"}, method = RequestMethod.GET)
    public Response<?> updatePatientConnect(@RequestParam(name = "oid", required = false) Long oID, @RequestParam(name = "tid", required = false) Long tID, @RequestParam(name = "current", required = false) String currentTarget) {

        PacPatientInfoEntity model;
        PacPatientInfoEntity target;

        if ("bddt".equals(currentTarget)) {
            model = pacPatientService.findOne(tID);
            target = pacPatientService.findOne(oID);
        } else {
            model = pacPatientService.findOne(oID);
            target = pacPatientService.findOne(tID);
        }

        if (model == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", oID));
        }

        if (target == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", tID));
        }

        if (!"bddt".equals(currentTarget) && target.getParentID() != null && target.getParentID() != 0) {
            pacPatientService.log(target.getID(), "Thực hiện kết nối không thành công do đã thực hiện kết nối trước đó.");
            return new Response<>(false, String.format("Không thể thực hiện kết nối vì bệnh nhân %s đã được kết nối điều trị ", tID));
        }

        // Cập nhật thông tin người nhiễm
        model.setStatusOfTreatmentID(target.getStatusOfTreatmentID());
        model.setStartTreatmentTime(target.getStartTreatmentTime());
        model.setSiteTreatmentFacilityID(target.getSiteTreatmentFacilityID());
        model.setTreatmentRegimenID(target.getTreatmentRegimenID());
        model.setSymptomID(target.getSymptomID());
        model.setDeathTime(model.getDeathTime() == null ? target.getDeathTime() : model.getDeathTime());
        model.setCauseOfDeath(target.getCauseOfDeath());
        model.setAidsStatusDate(target.getAidsStatusDate());

        // Cập nhật trạng trạng thái kết nối
        target.setParentID(model.getID());

        Map<String, Object> data = new HashMap();
        try {
            PacPatientInfoEntity saveModel = pacPatientService.save(model);
            pacPatientService.log(saveModel.getID(), "Cập nhật thông tin điều trị sau khi kết nối ca phát hiện với ca \"" + target.getFullname() + " \" thành công.");

            PacPatientInfoEntity saveTarget = pacPatientService.save(target);

            List<PacPatientInfoEntity> patientDecode = pacPatientService.getMetaData(Arrays.asList(saveModel));
            saveModel = patientDecode.get(0);
            pacPatientService.log(saveTarget.getID(), "Cập nhật thông tin sau khi kết nối ca điều trị với ca \"" + saveModel.getFullname() + " \" thành công.");

            data.put("model", saveModel);
            data.put("obj", saveModel);
            return new Response<>(true, "Thực hiện kết nối không thành công", data);

        } catch (Exception e) {
            return new Response<>(false, "Thực hiện kết nối không thành công");
        }
    }

    /**
     * DSNAnh Rà soát lại
     *
     * @param oid
     * @return
     */
    @RequestMapping(value = "/pac-accept/review-check.json", method = RequestMethod.GET)
    public Response<?> actionrRedirect(@RequestParam(name = "oid") String oid, @RequestParam(name = "tab", defaultValue = "") String tab) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity model = pacPatientService.findOne(Long.parseLong(oid));
        if (model == null || model.isRemove() || !model.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        Map<String, Object> data = new HashMap();
        if (tab.equals("review")) {
            //Rà soát lại
            model.setCheckWardTime(null);
            model.setCheckDistrictTime(null);
        } else {
            model.setReviewWardTime(null);
            model.setReviewDistrictTime(null);
        }
        try {
            pacPatientService.save(model);
            pacPatientService.log(model.getID(), "Yêu cầu rà soát lại");
        } catch (Exception ex) {
            return new Response<>(false, "Yêu cầu rà soát lại thất bại", data);
        }
        data.put("entity", model);
        return new Response<>(true, "Yêu cầu rà soát lại thành công", data);
    }

    /**
     * Cập nhật thông tin sau khi kết nối với nguồn dịch vụ là ARV
     *
     * @param currentTarget
     * @auth TrangBN
     * @param tID
     * @param oID
     * @param optionUpdate
     * @return
     */
    @RequestMapping(value = {"/pac-new/update-connected-arv.json"}, method = RequestMethod.GET)
    public Response<?> updatePatientConnectArv(@RequestParam(name = "option_update", required = false) Integer optionUpdate, @RequestParam(name = "oid", required = false) Long oID, @RequestParam(name = "tid", required = false) Long tID, @RequestParam(name = "current", required = false) String currentTarget) {

        PacPatientInfoEntity model;
        PacPatientInfoEntity target;

        if ("bddt".equals(currentTarget)) {
            model = pacPatientService.findOne(tID);
            target = pacPatientService.findOne(oID);
        } else {
            model = pacPatientService.findOne(oID);
            target = pacPatientService.findOne(tID);
        }

        if (model == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", oID));
        }

        if (target == null) {
            return new Response<>(false, String.format("Không tìm thấy ca nhiễm có mã %s", tID));
        }

//        if (!"bddt".equals(currentTarget) && target.getParentID() != null && target.getParentID() != 0) {
//            pacPatientService.log(target.getID(), "Thực hiện kết nối không thành công do đã thực hiện kết nối trước đó.");
//            return new Response<>(false, String.format("Không thể thực hiện kết nối vì bệnh nhân %s đã được kết nối điều trị ", tID));
//        }
        Map<String, Object> data = new HashMap();

        // Chỉ lấy các thông tin về điều trị của người nhiễm đang điều trị
        if (optionUpdate == 1) {
            model.setStatusOfTreatmentID(target.getStatusOfTreatmentID());
            model.setStatusOfChangeTreatmentID(target.getStatusOfChangeTreatmentID());
            model.setStartTreatmentTime(target.getStartTreatmentTime());
            model.setSiteTreatmentFacilityID(target.getSiteTreatmentFacilityID());
            model.setTreatmentRegimenID(target.getTreatmentRegimenID());
            model.setCdFourResult(target.getCdFourResult());
            model.setCdFourResultDate(target.getCdFourResultDate());
            model.setCdFourResultCurrent(target.getCdFourResultCurrent());
            model.setCdFourResultLastestDate(target.getCdFourResultLastestDate());
            model.setVirusLoadArv(target.getVirusLoadArv());
            model.setVirusLoadArvDate(target.getVirusLoadArvDate());
            model.setVirusLoadArvCurrent(target.getVirusLoadArvCurrent());
            model.setVirusLoadArvLastestDate(target.getVirusLoadArvLastestDate());
            model.setClinicalStage(target.getClinicalStage());
            model.setClinicalStageDate(target.getClinicalStageDate());
            model.setAidsStatus(target.getAidsStatus());
            model.setAidsStatusDate(target.getAidsStatusDate());
            model.setSymptomID(target.getSymptomID());
            model.setDeathTime(model.getDeathTime() == null ? target.getDeathTime() : model.getDeathTime());
            model.setRequestDeathTime(model.getRequestDeathTime() == null ? target.getRequestDeathTime() : model.getRequestDeathTime());
            model.setCauseOfDeath(target.getCauseOfDeath());
            model.setChangeTreatmentDate(target.getChangeTreatmentDate());
            //Bổ sung thông tin điều trị
            model.setRegistrationTime(target.getRegistrationTime());
            model.setRegistrationType(target.getRegistrationType());
            model.setFirstTreatmentTime(target.getFirstTreatmentTime());
            model.setFirstTreatmentRegimenId(target.getFirstTreatmentRegimenId());
            model.setEndTime(target.getEndTime());
            model.setEndCase(target.getEndCase());

            target.setParentID(model.getID());

            try {
                PacPatientInfoEntity saveModel = pacPatientService.save(model);
                pacPatientService.log(saveModel.getID(), "Cập nhật thông tin điều trị sau khi kết nối ca phát hiện với ca \"" + target.getFullname() + " \" thành công.");

                PacPatientInfoEntity saveTarget = pacPatientService.save(target);
                List<PacPatientInfoEntity> patientDecode = pacPatientService.getMetaData(Arrays.asList(saveModel));
                saveModel = patientDecode.get(0);
                pacPatientService.log(saveTarget.getID(), "Cập nhật thông tin sau khi kết nối ca điều trị với ca \"" + saveModel.getFullname() + " \" thành công.");

                data.put("model", model);
                return new Response<>(true, "Thực hiện kết nối thành công", data);

            } catch (Exception e) {
                return new Response<>(false, "Thực hiện kết nối không thành công");
            }
        }

        // Lấy toàn bộ thông tin cá nhân và thông tin về điều trị của người nhiễm đang điều trị
        if (optionUpdate == 2) {
            model.setFullname(target.getFullname());
            model.setYearOfBirth(target.getYearOfBirth());
            model.setGenderID(target.getGenderID());
            model.setIdentityCard(target.getIdentityCard());
            model.setHealthInsuranceNo(target.getHealthInsuranceNo());
            model.setPermanentAddressFull(target.getPermanentAddressFull());
            model.setPermanentAddressGroup(target.getPermanentAddressGroup());
            model.setPermanentAddressNo(target.getPermanentAddressNo());
            model.setPermanentAddressStreet(target.getPermanentAddressStreet());
            model.setPermanentDistrictID(target.getPermanentDistrictID());
            model.setPermanentProvinceID(target.getPermanentProvinceID());
            model.setPermanentWardID(target.getPermanentWardID());

            model.setStatusOfTreatmentID(target.getStatusOfTreatmentID());
            model.setStatusOfChangeTreatmentID(target.getStatusOfChangeTreatmentID());
            model.setStartTreatmentTime(target.getStartTreatmentTime());
            model.setSiteTreatmentFacilityID(target.getSiteTreatmentFacilityID());
            model.setTreatmentRegimenID(target.getTreatmentRegimenID());
            model.setCdFourResult(target.getCdFourResult());
            model.setCdFourResultCurrent(target.getCdFourResultCurrent());
            model.setCdFourResultDate(target.getCdFourResultDate());
            model.setCdFourResultLastestDate(target.getCdFourResultLastestDate());
            model.setVirusLoadArv(target.getVirusLoadArv());
            model.setVirusLoadArvCurrent(target.getVirusLoadArvCurrent());
            model.setVirusLoadArvDate(target.getVirusLoadArvDate());
            model.setVirusLoadArvLastestDate(target.getVirusLoadArvLastestDate());
            model.setClinicalStage(target.getClinicalStage());
            model.setClinicalStageDate(target.getClinicalStageDate());
            model.setAidsStatus(target.getAidsStatus());
            model.setAidsStatusDate(target.getAidsStatusDate());
            model.setSymptomID(target.getSymptomID());
            model.setDeathTime(model.getDeathTime() == null ? target.getDeathTime() : model.getDeathTime());
            model.setRequestDeathTime(model.getRequestDeathTime() == null ? target.getRequestDeathTime() : model.getRequestDeathTime());
            model.setCauseOfDeath(target.getCauseOfDeath());
            model.setConfirmCode(target.getConfirmCode());
            model.setChangeTreatmentDate(target.getChangeTreatmentDate());
            //Bổ sung thông tin điều trị
            model.setRegistrationTime(target.getRegistrationTime());
            model.setRegistrationType(target.getRegistrationType());
            model.setFirstTreatmentTime(target.getFirstTreatmentTime());
            model.setFirstTreatmentRegimenId(target.getFirstTreatmentRegimenId());
            model.setEndTime(target.getEndTime());
            model.setEndCase(target.getEndCase());

            target.setParentID(model.getID());
            try {
                PacPatientInfoEntity saveModel = pacPatientService.save(model);
                pacPatientService.log(saveModel.getID(), "Cập nhật thông tin điều trị sau khi kết nối ca phát hiện với ca \"" + target.getFullname() + " \" thành công.");

                PacPatientInfoEntity saveTarget = pacPatientService.save(target);
                List<PacPatientInfoEntity> patientDecode = pacPatientService.getMetaData(Arrays.asList(saveModel));
                saveModel = patientDecode.get(0);
                pacPatientService.log(saveTarget.getID(), "Cập nhật thông tin sau khi kết nối ca điều trị với ca \"" + saveModel.getFullname() + " \" thành công.");

                data.put("model", model);
                return new Response<>(true, "Thực hiện kết nối thành công", data);
            } catch (Exception e) {
                return new Response<>(false, "Thực hiện kết nối không thành công");
            }
        }
        return new Response<>(false, "Thực hiện kết nối không thành công");
    }

    @RequestMapping(value = "/pac-patient/report-update.json", method = RequestMethod.GET)
    public Response<?> actionReportUpdateGet(@RequestParam(name = "oid") String oid) throws CloneNotSupportedException {
        PacPatientInfoEntity patientInfoEntity = pacPatientService.findOne(Long.valueOf(oid));
        if (patientInfoEntity == null || patientInfoEntity.isRemove()) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        data.put("warning", null);
        if (patientInfoEntity.getRequestTime() != null) {
            PacPatientRequestEntity model = pacPatientRequestService.findOne(patientInfoEntity.getID());
            if (model != null) {
                StaffEntity staff = staffService.findOne(model.getCreatedBY());
                String staffName = staff == null ? "Nhân viên đã xóa" : staff.getName();
                data.put("warning", String.format("Yêu cầu bổ sung thông tin ngày %s bởi %s. Ca nhiễm đang chờ tỉnh duyệt!", TextUtils.formatDate(patientInfoEntity.getRequestTime(), FORMATDATE), staffName));
                try {
                    patientInfoEntity.set(model);
                } catch (IllegalAccessException | NoSuchFieldException ex) {
                    getLogger().error(ex.getMessage());
                }
            }
        }
        data.put("options", options);
        data.put("model", patientInfoEntity);
        return new Response<>(true, data);
    }

    //Lấy tên tỉnh, huyện, xã
    public Map<String, String> getLocation(List<String> pIDs) {
        Map<String, String> location = new HashMap<>();
        for (String pID : pIDs) {
            if (pID != null && !"".equals(pID)) {
                location.put(pID, locationsService.findProvince(pID).getName());

                Set<String> districtIDs = new HashSet<>();
                for (DistrictEntity e : locationsService.findDistrictByProvinceID(pID)) {
                    location.put(e.getID(), e.getName());
                    districtIDs.add(e.getID());
                }
                for (WardEntity e : locationsService.findByDistrictID(districtIDs)) {
                    location.put(e.getID(), e.getName());
                }
            }
        }
        return location;
    }

    /**
     * Kiểm tra thông tin trước khi cập nhật
     *
     * @author DSNAnh
     * @param oid
     * @return
     */
    @RequestMapping(value = {"/pac-patient/get.json",}, method = RequestMethod.GET)
    public Response<?> actionPacPatientGet(@RequestParam(name = "oid") Long oid) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity oldData = pacPatientService.findOne(oid);
        PacPatientRequestEntity newData = pacPatientRequestService.findOne(oid);
        PacHivInfoEntity hivInfoEntity = pacPatientService.findOneHivInfo(oid);
        if (oldData == null || oldData.isRemove() || !oldData.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        if (newData == null) {
            return new Response<>(false, "Chưa có yêu cầu cập nhật người nhiễm");
        }
        ArrayList<String> pIDs = new ArrayList<>();
        pIDs.add(oldData.getPermanentProvinceID());
        pIDs.add(newData.getPermanentProvinceID());
        pIDs.add(oldData.getCurrentProvinceID());
        pIDs.add(newData.getCurrentProvinceID());
        Map<String, Object> data = new HashMap();
        data.put("hivInfoCode", hivInfoEntity == null ? "" : hivInfoEntity.getCode() == null ? "" : hivInfoEntity.getCode());
        data.put("oldData", oldData);
        data.put("newData", newData);
        data.put("location", getLocation(pIDs));
        data.put("options", getOptions());
        return new Response<>(true, "", data);
    }

    /**
     * Cập nhật thông tin người nhiễm
     *
     * @author DSNAnh
     * @param oid
     * @return
     */
    @RequestMapping(value = {"/pac-patient/update.json"}, method = RequestMethod.GET)
    public Response<?> actionPacPatientUpdate(@RequestParam(name = "oid") Long oid) {
        PacPatientInfoEntity oldData = pacPatientService.findOne(oid);
        PacPatientRequestEntity newData = pacPatientRequestService.findOne(oid);
        if (newData.getFullname() != null) {
            if (oldData.getFullname() == null) {
                oldData.setFullname(newData.getFullname());
            } else {
                oldData.setFullname(!oldData.getFullname().equals(newData.getFullname()) ? newData.getFullname() : oldData.getFullname());
            }
        } else {
            oldData.setFullname(null);
        }
        if (newData.getYearOfBirth() != null) {
            if (oldData.getYearOfBirth() == null) {
                oldData.setYearOfBirth(newData.getYearOfBirth());
            } else {
                oldData.setYearOfBirth(!oldData.getYearOfBirth().equals(newData.getYearOfBirth()) ? newData.getYearOfBirth() : oldData.getYearOfBirth());
            }
        } else {
            oldData.setYearOfBirth(null);
        }
        if (newData.getGenderID() != null) {
            if (oldData.getGenderID() == null) {
                oldData.setGenderID(newData.getGenderID());
            } else {
                oldData.setGenderID(!oldData.getGenderID().equals(newData.getGenderID()) ? newData.getGenderID() : oldData.getGenderID());
            }
        } else {
            oldData.setGenderID(null);
        }
        if (newData.getRaceID() != null) {
            if (oldData.getRaceID() == null) {
                oldData.setRaceID(newData.getRaceID());
            } else {
                oldData.setRaceID(!oldData.getRaceID().equals(newData.getRaceID()) ? newData.getRaceID() : oldData.getRaceID());
            }
        } else {
            oldData.setRaceID(null);
        }
        if (newData.getIdentityCard() != null) {
            if (oldData.getIdentityCard() == null) {
                oldData.setIdentityCard(newData.getIdentityCard());
            } else {
                oldData.setIdentityCard(!oldData.getIdentityCard().equals(newData.getIdentityCard()) ? newData.getIdentityCard() : oldData.getIdentityCard());
            }
        } else {
            oldData.setIdentityCard(null);
        }
        if (newData.getInsuranceNo() != null) {
            if (oldData.getHealthInsuranceNo() == null) {
                oldData.setHealthInsuranceNo(newData.getInsuranceNo());
                oldData.setHasHealthInsurance(StringUtils.isNotEmpty(oldData.getHealthInsuranceNo()) ? InsuranceEnum.TRUE.getKey() : null);
            } else {
                oldData.setHealthInsuranceNo(!oldData.getHealthInsuranceNo().equals(newData.getInsuranceNo()) ? newData.getInsuranceNo() : oldData.getHealthInsuranceNo());
                oldData.setHasHealthInsurance(StringUtils.isNotEmpty(oldData.getHealthInsuranceNo()) ? InsuranceEnum.TRUE.getKey() : null);
            }
        } else {
            oldData.setHealthInsuranceNo(null);
        }
        if (newData.getPermanentAddressNo() != null) {
            if (oldData.getPermanentAddressNo() == null) {
                oldData.setPermanentAddressNo(newData.getPermanentAddressNo());
            } else {
                oldData.setPermanentAddressNo(!oldData.getPermanentAddressNo().equals(newData.getPermanentAddressNo()) ? newData.getPermanentAddressNo() : oldData.getPermanentAddressNo());
            }
        } else {
            oldData.setPermanentAddressNo(null);
        }
        if (newData.getPermanentAddressGroup() != null) {
            if (oldData.getPermanentAddressGroup() == null) {
                oldData.setPermanentAddressGroup(newData.getPermanentAddressGroup());
            } else {
                oldData.setPermanentAddressGroup(!oldData.getPermanentAddressGroup().equals(newData.getPermanentAddressGroup()) ? newData.getPermanentAddressGroup() : oldData.getPermanentAddressGroup());
            }
        } else {
            oldData.setPermanentAddressGroup(null);
        }
        if (newData.getPermanentAddressStreet() != null) {
            if (oldData.getPermanentAddressStreet() == null) {
                oldData.setPermanentAddressStreet(newData.getPermanentAddressStreet());
            } else {
                oldData.setPermanentAddressStreet(!oldData.getPermanentAddressStreet().equals(newData.getPermanentAddressStreet()) ? newData.getPermanentAddressStreet() : oldData.getPermanentAddressStreet());
            }
        } else {
            oldData.setPermanentAddressStreet(null);
        }
        if (newData.getPermanentProvinceID() != null) {
            if (oldData.getPermanentProvinceID() == null) {
                oldData.setPermanentProvinceID(newData.getPermanentProvinceID());
            } else {
                oldData.setPermanentProvinceID(!oldData.getPermanentProvinceID().equals(newData.getPermanentProvinceID()) ? newData.getPermanentProvinceID() : oldData.getPermanentProvinceID());
            }
        } else {
            oldData.setPermanentProvinceID(null);
        }
        if (newData.getPermanentDistrictID() != null) {
            if (oldData.getPermanentDistrictID() == null) {
                oldData.setPermanentDistrictID(newData.getPermanentDistrictID());
            } else {
                oldData.setPermanentDistrictID(!oldData.getPermanentDistrictID().equals(newData.getPermanentDistrictID()) ? newData.getPermanentDistrictID() : oldData.getPermanentDistrictID());
            }
        } else {
            oldData.setPermanentDistrictID(null);
        }
        if (newData.getPermanentWardID() != null) {
            if (oldData.getPermanentWardID() == null) {
                oldData.setPermanentWardID(newData.getPermanentWardID());
            } else {
                oldData.setPermanentWardID(!oldData.getPermanentWardID().equals(newData.getPermanentWardID()) ? newData.getPermanentWardID() : oldData.getPermanentWardID());
            }
        } else {
            oldData.setPermanentWardID(null);
        }
        if (newData.getCurrentAddressNo() != null) {
            if (oldData.getCurrentAddressNo() == null) {
                oldData.setCurrentAddressNo(newData.getCurrentAddressNo());
            } else {
                oldData.setCurrentAddressNo(!oldData.getCurrentAddressNo().equals(newData.getCurrentAddressNo()) ? newData.getCurrentAddressNo() : oldData.getCurrentAddressNo());
            }
        } else {
            oldData.setCurrentAddressNo(null);
        }
        if (newData.getCurrentAddressGroup() != null) {
            if (oldData.getCurrentAddressGroup() == null) {
                oldData.setCurrentAddressGroup(newData.getCurrentAddressGroup());
            } else {
                oldData.setCurrentAddressGroup(!oldData.getCurrentAddressGroup().equals(newData.getCurrentAddressGroup()) ? newData.getCurrentAddressGroup() : oldData.getCurrentAddressGroup());
            }
        } else {
            oldData.setCurrentAddressGroup(null);
        }
        if (newData.getCurrentAddressStreet() != null) {
            if (oldData.getCurrentAddressStreet() == null) {
                oldData.setCurrentAddressStreet(newData.getCurrentAddressStreet());
            } else {
                oldData.setCurrentAddressStreet(!oldData.getCurrentAddressStreet().equals(newData.getCurrentAddressStreet()) ? newData.getCurrentAddressStreet() : oldData.getCurrentAddressStreet());
            }
        } else {
            oldData.setCurrentAddressStreet(null);
        }
        if (newData.getCurrentProvinceID() != null && oldData.getCurrentProvinceID() != null) {
            if (oldData.getCurrentProvinceID() == null) {
                oldData.setCurrentProvinceID(newData.getCurrentProvinceID());
            } else {
                oldData.setCurrentProvinceID(!oldData.getCurrentProvinceID().equals(newData.getCurrentProvinceID()) ? newData.getCurrentProvinceID() : oldData.getCurrentProvinceID());
            }
        } else {
            oldData.setCurrentProvinceID(null);
        }
        if (newData.getCurrentDistrictID() != null) {
            if (oldData.getCurrentDistrictID() == null) {
                oldData.setCurrentDistrictID(newData.getCurrentDistrictID());
            } else {
                oldData.setCurrentDistrictID(!oldData.getCurrentDistrictID().equals(newData.getCurrentDistrictID()) ? newData.getCurrentDistrictID() : oldData.getCurrentDistrictID());
            }
        } else {
            oldData.setCurrentDistrictID(null);
        }
        if (newData.getCurrentWardID() != null) {
            if (oldData.getCurrentWardID() == null) {
                oldData.setCurrentWardID(newData.getCurrentWardID());
            } else {
                oldData.setCurrentWardID(!oldData.getCurrentWardID().equals(newData.getCurrentWardID()) ? newData.getCurrentWardID() : oldData.getCurrentWardID());
            }
        } else {
            oldData.setCurrentWardID(null);
        }
        if (newData.getStatusOfResidentID() != null) {
            if (oldData.getStatusOfResidentID() == null) {
                oldData.setStatusOfResidentID(newData.getStatusOfResidentID());
            } else {
                oldData.setStatusOfResidentID(!oldData.getStatusOfResidentID().equals(newData.getStatusOfResidentID()) ? newData.getStatusOfResidentID() : oldData.getStatusOfResidentID());
            }
        } else {
            oldData.setStatusOfResidentID(null);
        }
        if (newData.getDeathTime() != null) {
            if (oldData.getDeathTime() == null) {
                oldData.setDeathTime(newData.getDeathTime());
            } else {
                oldData.setDeathTime(!oldData.getDeathTime().equals(newData.getDeathTime()) ? newData.getDeathTime() : oldData.getDeathTime());
            }
        } else {
            oldData.setDeathTime(null);
        }
        if (newData.getCauseOfDeath() != null) {
            if (oldData.getCauseOfDeath() == null) {
                oldData.setCauseOfDeath(newData.getCauseOfDeath());
            } else {
                oldData.setCauseOfDeath(!oldData.getCauseOfDeath().equals(newData.getCauseOfDeath()) ? newData.getCauseOfDeath() : oldData.getCauseOfDeath());
            }
        } else {
            oldData.setCauseOfDeath(null);
        }

        if (newData.getRequestDeathTime() != null) {
            if (oldData.getRequestDeathTime() == null) {
                oldData.setRequestDeathTime(newData.getRequestDeathTime());
            } else {
                oldData.setRequestDeathTime(!oldData.getRequestDeathTime().equals(newData.getRequestDeathTime()) ? newData.getRequestDeathTime() : oldData.getRequestDeathTime());
            }
        } else {
            oldData.setRequestDeathTime(null);
        }

        if (newData.getNote() != null) {
            if (oldData.getNote() == null) {
                oldData.setNote(newData.getNote());
            } else {
                oldData.setNote(!oldData.getNote().equals(newData.getNote()) ? newData.getNote() : oldData.getNote());
            }
        } else {
            oldData.setNote(null);
        }
        oldData.setRequestTime(null);

        oldData.setObjectGroupID(newData.getObjectGroupID());
        oldData.setPatientPhone(newData.getPatientPhone());
        oldData.setModeOfTransmissionID(newData.getModeOfTransmissionID());

        oldData.setStartTreatmentTime(newData.getStartTreatmentTime());
        oldData.setSiteTreatmentFacilityID(newData.getSiteTreatmentFacilityID());
        oldData.setTreatmentRegimenID(newData.getTreatmentRegimenID());
        oldData.setChangeTreatmentDate(newData.getChangeTreatmentDate());
        oldData.setStatusOfChangeTreatmentID(newData.getStatusOfChangeTreatmentID());
        oldData.setStatusOfTreatmentID(newData.getStatusOfTreatmentID());

        try {
//            oldData.setProvinceID(oldData.getPermanentProvinceID());
//            oldData.setDistrictID(oldData.getPermanentDistrictID());
//            oldData.setWardID(oldData.getPermanentWardID());
            pacPatientService.save(oldData);
            PacPatientInfoEntity model = pacPatientService.findOne(oid);

            List<PacPatientInfoEntity> entitys = new ArrayList<>();
            entitys.add(model);
            entitys = pacPatientService.getAddress(entitys);
            entitys = pacPatientService.getMetaData(entitys);
            PacPatientInfoEntity entity = entitys.get(0);

            pacPatientService.log(oid, String.format("Thông tin người nhiễm đã cập nhật theo thông tin từ xã báo lên ngày %s", TextUtils.formatDate(new Date(), FORMATDATE)));
            PacPatientRequestEntity modelRequest = pacPatientRequestService.findOne(oid);
            StaffEntity staff = staffService.findOne(modelRequest.getCreatedBY());
            Map<String, Object> variables = new HashMap<>();
            variables.put("staffName", staff.getName());
            variables.put("model", entity);
            variables.put("options", getOptions());
            sendEmail(staff.getEmail(), String.format("[Thông báo] Đã thay đổi thông tin người nhiễm mã %s", oid), EmailoutboxEntity.TEMPLATE_PAC_PATIENT_UPDATE, variables);
            staffService.sendMessage(staff.getID(), "Đồng ý yêu cầu thay đổi thông tin người nhiễm", String.format("Mã khách hàng: %s", oid), UrlUtils.pacPatientView(oid));
            pacPatientRequestService.remove(newData);
        } catch (Exception ex) {
            return new Response<>(false, "Thực hiện cập nhật thất bại");
        }
        return new Response<>(true, "Thực hiện cập nhật thành công");
    }

    /**
     * Nhập lý do hủy yêu cầu cập nhật thông tin
     *
     * @author DSNAnh
     * @param oid
     * @return
     */
    @RequestMapping(value = {"/pac-patient/update-cancel.json",}, method = RequestMethod.GET)
    public Response<?> actionPacPatientUpdateCancel(@RequestParam(name = "oid") Long oid) {
        PacPatientInfoEntity model = pacPatientService.findOne(oid);
        if (model == null || model.isRemove() || !model.getProvinceID().equals(getCurrentUser().getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        Map<String, Object> data = new HashMap();
        data.put("entity", model);
        data.put("options", getOptions());
        return new Response<>(true, data);
    }

    /**
     * Hủy yêu cầu cập nhật thông tin
     *
     * @author DSNAnh
     * @param oid
     * @param content
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = {"/pac-patient/cancel.json",}, method = RequestMethod.POST)
    public Response<?> actionPacPatientCancel(@RequestParam(name = "oid") Long oid,
            @RequestBody String content) throws Exception {
        content = content.equals("") ? "Xác nhận huỷ" : content;

        PacPatientInfoEntity model = pacPatientService.findOne(oid);
        PacPatientRequestEntity request = pacPatientRequestService.findOne(oid);

        if (model == null || request == null) {
            return new Response<>(false, "Không tìm thấy ca nhiễm yêu cầu");
        }

        StaffEntity staff = staffService.findOne(request.getCreatedBY());
        Map<String, Object> variables = new HashMap<>();
        variables.put("staffName", staff.getName());
        variables.put("content", content.replaceAll("\"", ""));
        variables.put("model", model);
        variables.put("options", getOptions());
        pacPatientService.log(model.getID(), String.format("Từ chối yêu cầu cập nhật, lý do: %s", content));
        sendEmail(staff.getEmail(), String.format("[Thông báo] Từ chối cập nhật người nhiễm mã %s", model.getID()), EmailoutboxEntity.TEMPLATE_PAC_PATIENT_CANCEL, variables);
        //remove
        model.setRequestTime(null);
        pacPatientService.save(model);
        pacPatientRequestService.remove(request);
        return new Response<>(true, "Từ chối yêu cầu cập nhật thành công");
    }

    /**
     * Cập nhật thông tin rà soát
     *
     * @author DSNAnh
     * @param oid
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/pac-patient/report-update-post.json", method = RequestMethod.POST)
    public Response<?> doCctionUpdateReport(@RequestParam(name = "oid") Long oid,
            @RequestBody PacReportUpdateForm form,
            BindingResult bindingResult) {
        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat(FORMATDATE);
            LoggedUser currentUser = getCurrentUser();
            Map<String, String> errors = new HashMap();

            if (StringUtils.isNotEmpty(form.getDeathTime()) && (form.getDeathTime().contains("/") ? !isThisDateValidd(form.getDeathTime()) : !isThisDateValid(form.getDeathTime()))) {
                errors.put("deathTime", "Ngày tử vong không hợp lệ");
            }
            if (StringUtils.isNotEmpty(form.getRequestDeathTime()) && (form.getRequestDeathTime().contains("/") ? !isThisDateValidd(form.getRequestDeathTime()) : !isThisDateValid(form.getRequestDeathTime()))) {
                errors.put("requestDeathTime", "Ngày báo tử vong không hợp lệ");
            }
            if (StringUtils.isNotEmpty(form.getChangeTreatmentDate()) && (form.getChangeTreatmentDate().contains("/") ? !isThisDateValidd(form.getChangeTreatmentDate()) : !isThisDateValid(form.getChangeTreatmentDate()))) {
                errors.put("changeTreatmentDate", "Ngày biến động điều trị không hợp lệ");
            }
            if (StringUtils.isNotEmpty(form.getStartTreatmentTime()) && (form.getStartTreatmentTime().contains("/") ? !isThisDateValidd(form.getStartTreatmentTime()) : !isThisDateValid(form.getStartTreatmentTime()))) {
                errors.put("startTreatmentTime", "Ngày bắt đầu điều trị không hợp lệ");
            }
            //set lại ngày
            if (!form.getDeathTime().contains("/") && form.getDeathTime() != null && !"".equals(form.getDeathTime())) {
                form.setDeathTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getDeathTime()));
            }
            if (form.getRequestDeathTime() != null && !"".equals(form.getRequestDeathTime()) && !form.getRequestDeathTime().contains("/")) {
                form.setRequestDeathTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getRequestDeathTime()));
            }
            if (form.getChangeTreatmentDate() != null && !"".equals(form.getChangeTreatmentDate()) && !form.getChangeTreatmentDate().contains("/")) {
                form.setChangeTreatmentDate(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getChangeTreatmentDate()));
            }
            if (form.getStartTreatmentTime() != null && !"".equals(form.getStartTreatmentTime()) && !form.getStartTreatmentTime().contains("/")) {
                form.setStartTreatmentTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getStartTreatmentTime()));
            }

            if (StringUtils.isNotBlank(form.getFullname()) && !TextUtils.removeDiacritical(form.getFullname().trim()).matches(RegexpEnum.VN_CHAR)) {
                errors.put("fullname", "Họ và tên chỉ chứa các kí tự chữ cái");
            }
            if (StringUtils.isNotEmpty(form.getYearOfBirth())) {
                if (!form.getYearOfBirth().matches(RegexpEnum.YYYY) || Integer.parseInt(TextUtils.formatDate(new Date(), "yyyy")) < Integer.parseInt(form.getYearOfBirth()) || Integer.parseInt(form.getYearOfBirth()) < 1900) {
                    errors.put("yearOfBirth", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
                }
            }

            if (form.getStatusOfResidentID() == null || "".equals(form.getStatusOfResidentID())) {
                errors.put("statusOfResidentID", "Kết quả xác minh hiện trạng cư trú không được để trống");
            }
            if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)) {
                errors.put("identityCard", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt");
            }
            if (StringUtils.isNotEmpty(form.getHealthInsuranceNo()) && !TextUtils.removeDiacritical(form.getHealthInsuranceNo().trim()).matches(RegexpEnum.CMND)) {
                errors.put("healthInsuranceNo", "Số thẻ BHYT không chứa ký tự đặc biệt");
            }
            try {
                Date today = new Date();

                if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isEmpty(errors.get("deathTime"))) {
                    Date deathTime = sdfrmt.parse(form.getDeathTime());
                    if (deathTime.compareTo(today) > 0) {
                        errors.put("deathTime", "Ngày tử vong không được sau ngày hiện tại");
                    }
                }
                if (StringUtils.isNotEmpty(form.getRequestDeathTime()) && StringUtils.isEmpty(errors.get("requestDeathTime"))) {
                    Date deathTime = sdfrmt.parse(form.getRequestDeathTime());
                    if (deathTime.compareTo(today) > 0) {
                        errors.put("requestDeathTime", "Ngày báo tử vong không được sau ngày hiện tại");
                    }
                }
                if (StringUtils.isNotEmpty(form.getStartTreatmentTime()) && StringUtils.isEmpty(errors.get("startTreatmentTime"))) {
                    Date deathTime = sdfrmt.parse(form.getStartTreatmentTime());
                    if (deathTime.compareTo(today) > 0) {
                        errors.put("startTreatmentTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
                    }
                }
                if (StringUtils.isNotEmpty(form.getChangeTreatmentDate()) && StringUtils.isEmpty(errors.get("changeTreatmentDate"))) {
                    Date deathTime = sdfrmt.parse(form.getChangeTreatmentDate());
                    if (deathTime.compareTo(today) > 0) {
                        errors.put("changeTreatmentDate", "Ngày biến động điều trị không được lớn hơn ngày hiện tại");
                    }
                }
                if (StringUtils.isEmpty(form.getChangeTreatmentDate()) && StringUtils.isNotEmpty(form.getStatusOfChangeTreatmentID())) {
                    errors.put("changeTreatmentDate", "Ngày biến động điều trị không được để trống");
                }

            } catch (ParseException ex) {
            }
            form.getCauseOfDeath().remove("");
            form.getCauseOfDeath().remove("null");
            if (StringUtils.isEmpty(form.getDeathTime()) && form.getCauseOfDeath().size() > 0) {
                errors.put("deathTime", "Ngày tử vong không được để trống");
            }
            if (StringUtils.isNotEmpty(form.getDeathTime()) && form.getCauseOfDeath().isEmpty()) {
                errors.put("causeOfDeath", "Nguyên nhân tử vong không được để trống");
            }
            if (StringUtils.isNotEmpty(form.getRequestDeathTime())) {
                if (StringUtils.isEmpty(form.getDeathTime())) {
                    errors.put("deathTime", "Ngày tử vong không được để trống");
                }
                if (form.getCauseOfDeath().isEmpty()) {
                    errors.put("causeOfDeath", "Nguyên nhân tử vong không được để trống");
                }
            }
            if (StringUtils.isEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getRequestDeathTime())) {
                errors.put("deathTime", "Ngày tử vong không được để trống");
            }
            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isEmpty(form.getRequestDeathTime())) {
                errors.put("requestDeathTime", "Ngày báo tử vong không được để trống");
            }

            if (StringUtils.isNotEmpty(form.getHealthInsuranceNo()) && !form.getHealthInsuranceNo().matches(RegexpEnum.HI_CHAR)) {
                errors.put("healthInsuranceNo", "Số thẻ BHYT không chứa ký tự đặc biệt");
            }
            if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)) {
                errors.put("identityCard", "Số CMND không chứa ký tự đặc biệt");
            }

            if (StringUtils.isNotEmpty(form.getPatientPhone())) {
                form.setPatientPhone(form.getPatientPhone().replaceAll("\\s", ""));
                if (!form.getPatientPhone().matches(PHONE_NUMBER)) {
                    errors.put("patientPhone", "Số điện thoại không hợp lệ");
                }
            }

            validateLength("fullname", "Họ và tên", 100, form.getFullname(), errors);
            validateLength("patientPhone", "Số điện thoại", 11, form.getPatientPhone(), errors);
            validateLength("healthInsuranceNo", "Số thẻ BHYT", 50, form.getHealthInsuranceNo(), errors);
            validateLength("identityCard", "Số CMND", 50, form.getIdentityCard(), errors);

            validateLength("permanentAddressNo", "Số nhà", 500, form.getPermanentAddressNo(), errors);
            validateLength("permanentAddressStreet", "Đường phố", 500, form.getPermanentAddressStreet(), errors);
            validateLength("permanentAddressGroup", "Tổ/ấp/Khu phố", 500, form.getPermanentAddressGroup(), errors);
            validateLength("currentAddressNo", "Số nhà", 500, form.getCurrentAddressNo(), errors);
            validateLength("currentAddressStreet", "Đường phố", 500, form.getCurrentAddressStreet(), errors);
            validateLength("currentAddressGroup", "Tổ/ấp/Khu phố", 500, form.getCurrentAddressGroup(), errors);
            validateLength("note", "Ghi chú", 500, form.getNote(), errors);

            validateNull("fullname", "Họ và tên", form.getFullname(), errors);
            validateNull("yearOfBirth", "Năm sinh", form.getYearOfBirth(), errors);
            validateNull("genderID", "Giới tính", form.getGenderID(), errors);
            validateNull("objectGroupID", "Nhóm đối tượng", form.getObjectGroupID(), errors);
            validateNull("permanentProvinceID", "Tỉnh/Thành phố", form.getPermanentProvinceID(), errors);
            validateNull("permanentDistrictID", "Quận/Huyện", form.getPermanentDistrictID(), errors);
            validateNull("permanentWardID", "Phường/xã", form.getPermanentWardID(), errors);
            validateNull("currentProvinceID", "Tỉnh/Thành phố", form.getCurrentProvinceID(), errors);
            validateNull("currentDistrictID", "Quận/Huyện", form.getCurrentDistrictID(), errors);
            validateNull("currentWardID", "Phường/xã", form.getCurrentWardID(), errors);

            if (errors.size() > 0) {
                return new Response<>(false, errors);
            }

            PacPatientInfoEntity patient = pacPatientService.findOne(oid);
            patient.setRequestTime(new Date());
            pacPatientService.save(patient);
            pacPatientService.log(patient.getID(), "Người nhiễm đã được báo cập nhật thông tin");

//        form.setModel(model);
            //thoong tin mac dinh
            PacPatientRequestEntity model = new PacPatientRequestEntity();
            model.setID(patient.getID());
            model.setProvinceID(currentUser.getSite().getProvinceID());
            model.setDistrictID(currentUser.getSite().getDistrictID());
            model.setWardID(currentUser.getSite().getWardID());
            model.setCreateAT(new Date());
            model.setCreatedBY(currentUser.getUser().getID());
            model.setFullname(form.getFullname());
            model.setYearOfBirth(Integer.parseInt(form.getYearOfBirth()));
            model.setGenderID(form.getGenderID());
            model.setRaceID(form.getRaceID());
            model.setIdentityCard(form.getIdentityCard());
            model.setInsuranceNo(form.getHealthInsuranceNo());
            model.setPermanentAddressNo(form.getPermanentAddressNo());
            model.setPermanentAddressGroup(form.getPermanentAddressGroup());
            model.setPermanentProvinceID(form.getPermanentProvinceID());
            model.setPermanentDistrictID(form.getPermanentDistrictID());
            model.setPermanentWardID(form.getPermanentWardID());
            model.setPermanentAddressStreet(form.getPermanentAddressStreet());
            model.setCurrentAddressNo(form.getCurrentAddressNo());
            model.setCurrentAddressStreet(form.getCurrentAddressStreet());
            model.setCurrentAddressGroup(form.getCurrentAddressGroup());
            model.setCurrentProvinceID(form.getCurrentProvinceID());
            model.setCurrentDistrictID(form.getCurrentDistrictID());
            model.setCurrentWardID(form.getCurrentWardID());
            model.setStatusOfResidentID(form.getStatusOfResidentID());
            model.setDeathTime(TextUtils.convertDate(form.getDeathTime(), FORMATDATE));
            model.setCauseOfDeath(form.getCauseOfDeath());
            model.setRequestDeathTime(TextUtils.convertDate(form.getRequestDeathTime(), FORMATDATE));
            model.setNote(form.getNote());

            model.setObjectGroupID(form.getObjectGroupID());
            model.setPatientPhone(form.getPatientPhone());
            model.setModeOfTransmissionID(form.getModeOfTransmissionID());

            model.setStartTreatmentTime(TextUtils.convertDate(form.getStartTreatmentTime(), FORMATDATE));
            model.setSiteTreatmentFacilityID(form.getSiteTreatmentFacilityID());
            model.setTreatmentRegimenID(form.getTreatmentRegimenID());
            model.setChangeTreatmentDate(TextUtils.convertDate(form.getChangeTreatmentDate(), FORMATDATE));
            model.setStatusOfChangeTreatmentID(form.getStatusOfChangeTreatmentID());
            model.setStatusOfTreatmentID(form.getStatusOfTreatmentID());

            pacPatientRequestService.save(model);
            Map<String, Object> variables = new HashMap<>();
            variables.put("ID", patient.getID());
            variables.put("name", patient.getFullname());
            variables.put("sentDate", TextUtils.formatDate(new Date(), "dd/MM/yyyy"));
            variables.put("sentSite", getCurrentUser().getSite().getName());
            for (SiteEntity siteEntity : siteService.findByServiceAndProvince(ServiceEnum.PAC.getKey(), currentUser.getSite().getProvinceID())) {
                sendEmail(siteEntity.getEmail(), "[Thông báo] Yêu cầu cập nhật thông tin người nhiễm người nhiễm mã " + patient.getID(), EmailoutboxEntity.TEMPLATE_PAC_UPDATE_REPORT, variables);
            }
            return new Response<>(true, "Người nhiễm đã được báo cập nhật thông tin thành công.");
        } catch (Exception ex) {
            return new Response<>(false, "Lỗi cập nhật thông tin. " + ex.getMessage());
        }
    }

    private void validateNull(String ID, String name, String text, Map<String, String> errors) {
        if (StringUtils.isBlank(text)) {
            errors.put(ID, name + " không được để trống");
        }

    }

    private void validateLength(String ID, String name, int max, String text, Map<String, String> errors) {
        if (StringUtils.isNotBlank(text) && text.length() > max) {
            errors.put(ID, name + " không được quá " + max + " ký tự");
        }

    }

    /**
     * Lấy thông tin cho danh sách trung ương yêu cầu rà soát
     *
     * @author pdThang
     * @param oid
     * @return
     */
    @RequestMapping(value = "/pac-new/vaac-get.json", method = RequestMethod.GET)
    public Response<?> actionUpdateVAAC(@RequestParam(name = "oid") String oid) {
        LoggedUser currentUser = getCurrentUser();
        PacPatientInfoEntity model = pacPatientService.findOne(Long.parseLong(oid));
        if (model == null || model.isRemove() || !model.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }

        Map<String, Object> data = new HashMap();
        HashMap<String, HashMap<String, String>> options = getOptions();
        data.put("model", model);
        data.put("options", options);
        return new Response<>(true, data);
    }

    /**
     * Cập nhật thông tin cho danh sách trung ương yêu cầu rà soát
     *
     * @author pdThang
     * @param oid
     * @param tab
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/pac-new/update-vaac.json", method = RequestMethod.POST)
    public Response<?> actionUpdateResultVAAC(@RequestParam(name = "oid") String oid,
            @RequestBody PacNewVaacForm form,
            BindingResult bindingResult) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(FORMATDATE);
        LoggedUser currentUser = getCurrentUser();
        Map<String, String> errors = new HashMap();
        PacPatientInfoEntity model = pacPatientService.findOne(Long.parseLong(oid));
        if (model == null || model.isRemove() || !model.getProvinceID().equals(currentUser.getSite().getProvinceID())) {
            return new Response<>(false, "Không có quyền truy cập dữ liệu");
        }
        if (!form.getDeathTime().contains("/") && form.getDeathTime() != null && !"".equals(form.getDeathTime())) {
            form.setDeathTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getDeathTime()));
        }
        if (!form.getConfirmTime().contains("/") && form.getConfirmTime() != null && !"".equals(form.getConfirmTime())) {
            form.setConfirmTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getConfirmTime()));
        }
        if (!form.getStartTreatmentTime().contains("/") && form.getStartTreatmentTime() != null && !"".equals(form.getStartTreatmentTime())) {
            form.setStartTreatmentTime(TextUtils.formatDate("ddMMyyyy", FORMATDATE, form.getStartTreatmentTime()));
        }
        if (StringUtils.isNotBlank(form.getFullname()) && !TextUtils.removeDiacritical(form.getFullname().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.put("fullname", "Họ và tên chỉ chứa các kí tự chữ cái");
        }
        if (StringUtils.isNotEmpty(form.getYearOfBirth())) {
            if (!form.getYearOfBirth().matches(RegexpEnum.YYYY)) {
                errors.put("yearOfBirth", "Năm sinh phải nhập số và đúng định dạng năm (1900-năm hiện tại)");
            }
        }

        if (form.getStatusOfResidentID() == null || "".equals(form.getStatusOfResidentID())) {
            errors.put("statusOfResidentID", "Kết quả xác minh hiện trạng cư trú không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getIdentityCard()) && !TextUtils.removeDiacritical(form.getIdentityCard().trim()).matches(RegexpEnum.CMND)) {
            errors.put("identityCard", "Số CMND/Giấy tờ khác không chứa ký tự đặc biệt");
        }
        if (StringUtils.isNotEmpty(form.getHealthInsuranceNo()) && !TextUtils.removeDiacritical(form.getHealthInsuranceNo().trim()).matches(RegexpEnum.HI_CHAR)) {
            errors.put("healthInsuranceNo", "Số thẻ BHYT không chứa ký tự đặc biệt");
        }
        try {
            Date today = new Date();
            if (StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());
                if (startTreatmentTime.compareTo(today) > 0) {
                    errors.put("startTreatmentTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getDeathTime())) {
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                if (deathTime.compareTo(today) > 0) {
                    errors.put("deathTime", "Ngày tử vong không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getDeathTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                if (confirmTime.compareTo(today) > 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được sau ngày hiện tại");
                }
            }
            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getConfirmTime())) {
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());

                if (deathTime.compareTo(confirmTime) < 0) {
                    errors.put("deathTime", "Ngày tử vong không được trước ngày XN khẳng định");
                }
            }
            if (StringUtils.isNotEmpty(form.getDeathTime()) && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date deathTime = sdfrmt.parse(form.getDeathTime());
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());

                if (deathTime.compareTo(startTreatmentTime) < 0) {
                    errors.put("deathTime", "Ngày tử vong không được trước ngày bắt đầu điều trị");
                }
            }
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getStartTreatmentTime())) {
                Date confirmTime = sdfrmt.parse(form.getConfirmTime());
                Date startTreatmentTime = sdfrmt.parse(form.getStartTreatmentTime());

                if (confirmTime.compareTo(startTreatmentTime) > 0) {
                    errors.put("confirmTime", "Ngày XN khẳng định không được sau ngày bắt đầu điều trị");
                }
            }
        } catch (ParseException ex) {
        }
        form.getCauseOfDeath().remove("");
        form.getCauseOfDeath().remove("null");
        if (StringUtils.isEmpty(form.getDeathTime()) && form.getCauseOfDeath().size() > 0) {
            errors.put("deathTime", "Ngày tử vong không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getDeathTime()) && form.getCauseOfDeath().isEmpty()) {
            errors.put("causeOfDeath", "Nguyên nhân tử vong không được để trống");
        }
        if (StringUtils.isNotEmpty(form.getPermanentAddressNo()) && form.getPermanentAddressNo().length() > 100) {
            errors.put("permanentAddressNo", "Số nhà không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getPermanentAddressStreet()) && form.getPermanentAddressStreet().length() > 100) {
            errors.put("permanentAddressStreet", "Đường phố không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getPermanentAddressGroup()) && form.getPermanentAddressGroup().length() > 100) {
            errors.put("permanentAddressGroup", "Tổ/ấp/Khu phố không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getCurrentAddressNo()) && form.getCurrentAddressNo().length() > 100) {
            errors.put("currentAddressNo", "Số nhà không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getCurrentAddressStreet()) && form.getCurrentAddressStreet().length() > 100) {
            errors.put("currentAddressStreet", "Đường phố không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getCurrentAddressGroup()) && form.getCurrentAddressGroup().length() > 100) {
            errors.put("currentAddressGroup", "Tổ/ấp/Khu phố không quá 100 ký tự");
        }
        if (StringUtils.isNotEmpty(form.getNote()) && form.getNote().length() > 500) {
            errors.put("note", "Ghi chú không quá 500 ký tự");
        }

        if (errors.size() > 0) {
            return new Response<>(false, "Cập nhật không thành công, kiểm tra lại thông tin", errors);
        }

        try {
            Object oldModel = model.clone();
            form.setModel(model);
            //Cập nhật vaac 
            model.setUpdatedPacTime(new Date());

            String log = "Tỉnh cập nhật thông tin rà soát.";
            String change = model.changeToString(oldModel, true);
            if (change != null && !change.equals("")) {
                log += change;
            }
            pacPatientService.save(model);
            pacPatientService.log(model.getID(), log);
            return new Response<>(true, "Người nhiễm đã được cập nhật thông tin rà soát thành công.");
        } catch (Exception ex) {
            String message = "Lỗi cập nhật thông tin. " + ex.getMessage();
            pacPatientService.log(model.getID(), message);
            return new Response<>(false, message);
        }
    }

    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean isThisDateValidd(String dateToValidate) {
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
