package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.ResponseHub;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvStageEntity;
import com.gms.entity.db.PqmArvTestEntity;
import com.gms.entity.db.PqmArvViralLoadEntity;
import com.gms.entity.db.PqmArvVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.hub.PqmArvDelForm;
import com.gms.entity.form.hub.PqmArvDelList;
import com.gms.entity.form.pqm_arv_api.Arv;
import com.gms.entity.form.pqm_arv_api.Main;
import com.gms.entity.form.pqm_arv_api.Stage;
import com.gms.entity.form.pqm_arv_api.Test;
import com.gms.entity.form.pqm_arv_api.Viral;
import com.gms.entity.form.pqm_arv_api.Visit;
import com.gms.service.LocationsService;
import com.gms.service.PqmArvService;
import com.gms.service.PqmArvStageService;
import com.gms.service.PqmArvTestService;
import com.gms.service.PqmArvViralService;
import com.gms.service.PqmArvVisitService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_arvv_api")
public class ArvController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmArvService pqmArvService;
    @Autowired
    private PqmArvStageService stageService;
    @Autowired
    private PqmArvVisitService visitService;
    @Autowired
    private PqmArvViralService viralService;
    @Autowired
    private PqmArvTestService testService;

    private static final String FORMATDATE = "yyyy-MM-dd";

    @RequestMapping(value = "/hub/v1/arv.api", method = RequestMethod.POST)
    public ResponseHub<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            Main form = g.fromJson(content.trim(), Main.class);

            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})", form.getDatas().size());
            }

            int created = 0;
            int updated = 0;
            int errored = 0;

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (Arv item : form.getDatas()) {
                    try {
                        SiteEntity site = siteService.findByArvSiteCode(item.getSiteCode());
                        if (site == null) {
                            HashMap<String, String> errors = new HashMap<>();
                            errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                            masterError.put(item.getCode(), errors);
                            errored++;
                            continue;
                        }
                        item.setProvinceID(form.getProvinceID());
                        HashMap<String, String> errors = validateArv(item, siteService.findOne(site.getID()).getProvinceID());

                        if (StringUtils.isNotEmpty(item.getCode())) {
                            item.setCode(item.getCode().substring(6));
                            item.setCode(item.getCode().trim());
                        }
                        if (!errors.isEmpty()) {
                            masterError.put(StringUtils.isEmpty(item.getCode()) ? "empty-code" : item.getCode(), errors);
                            errored++;
                        } else {
//                        PqmArvEntity model = pqmArvService.findBySiteIDAndCode(site.getID(), item.getCode().toUpperCase());
                            PqmArvEntity model = pqmArvService.findBySiteIDAndCustomer_system_id(item.getCustomer_system_id());

                            if (model != null && !model.getSiteID().equals(site.getID())) {
                                throw new Exception("Customer_system_id " + item.getCustomer_system_id() + " thuộc quản lý của đơn vị khác");
                            }

                            if (model != null) {
                                List<PqmArvStageEntity> stage = stageService.findByArvID(model.getID());
                                List<PqmArvVisitEntity> visit = visitService.findByArvID(model.getID());
                                List<PqmArvViralLoadEntity> viral = viralService.findByArvID(model.getID());
                                List<PqmArvTestEntity> test = testService.findByArvID(model.getID());

                                //Xoá dữ liệu cũ
                                if (stage != null) {
                                    Set<Long> IDs = new HashSet<>();
                                    IDs.addAll(CollectionUtils.collect(stage, TransformerUtils.invokerTransformer("getID")));
                                    stageService.deleteAll(IDs);
                                }
                                if (visit != null) {
                                    Set<Long> IDs = new HashSet<>();
                                    IDs.addAll(CollectionUtils.collect(visit, TransformerUtils.invokerTransformer("getID")));
                                    visitService.deleteAll(IDs);
                                }
                                if (viral != null) {
                                    Set<Long> IDs = new HashSet<>();
                                    IDs.addAll(CollectionUtils.collect(viral, TransformerUtils.invokerTransformer("getID")));
                                    viralService.deleteAll(IDs);
                                }
                                if (test != null) {
                                    Set<Long> IDs = new HashSet<>();
                                    IDs.addAll(CollectionUtils.collect(test, TransformerUtils.invokerTransformer("getID")));
                                    testService.deleteAll(IDs);
                                }
                                pqmArvService.deleteById(model.getID());
                            }

                            PqmArvEntity findBySiteAndCode = pqmArvService.findBySiteIDAndCode(site.getID(), item.getCode());
                            if (findBySiteAndCode != null) {
                                throw new Exception("Mã bệnh án " + item.getCode() + " đã tồn tại ở cơ sở này");
                            }

                            model = new PqmArvEntity();
                            created++;

                            //Thêm trường
                            model.setMonth_report(form.getMonth());
                            model.setYear_report(form.getYear());
                            model.setProvince_id(form.getProvinceID());
                            model.setQuantity(form.getQuantity());
                            model.setCustomer_system_id(item.getCustomer_system_id());
                            model.setPatient_id(item.getPatient_id());

                            model.setSiteID(site.getID());
                            model.setCode(item.getCode().toUpperCase());
                            model.setDob(StringUtils.isEmpty(item.getDob()) ? null : TextUtils.convertDate(item.getDob(), FORMATDATE));
                            model.setFirstTreatmentTime(StringUtils.isEmpty(item.getFirstTreatmentTime()) ? null : TextUtils.convertDate(item.getFirstTreatmentTime(), FORMATDATE));
                            model.setFullName(item.getFullName() == null ? "" : TextUtils.toFullname(item.getFullName()));
                            model.setGenderID(item.getGenderID());
                            model.setIdentityCard(item.getIdentityCard());
                            model.setInsuranceNo(item.getInsuranceNo());
                            model.setPatientPhone(item.getPatientPhone());
                            model.setPermanentAddress(item.getPermanentAddress());
                            model.setCurrentAddress(item.getCurrentAddress());
                            model.setObjectGroupID(item.getObjectGroupID());

                            model.setPermanentProvinceID(item.getPermanentProvinceID());
                            model.setPermanentDistrictID(item.getPermanentDistrictID());
                            model.setPermanentWardID(item.getPermanentWardID());
                            model.setCurrentProvinceID(item.getCurrentProvinceID());
                            model.setCurrentDistrictID(item.getCurrentDistrictID());
                            model.setCurrentWardID(item.getCurrentWardID());
                            model.setSiteConfirm(item.getSiteConfirm());
                            model.setConfirmTime(StringUtils.isEmpty(item.getConfirmTime()) ? null : TextUtils.convertDate(item.getConfirmTime(), FORMATDATE));

                            model = pqmArvService.saveApi(model);

                            if (model.getID() != null) {
                                if (item.getStages() != null && !item.getStages().isEmpty()) {
                                    for (Stage stage : item.getStages()) {
                                        HashMap<String, String> error = validateStage(stage);

                                        if (!error.isEmpty()) {
                                            if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                masterError.put(item.getCode(), new HashMap<String, String>());
                                            }
//                                            masterError.get(model.getCode()).put("Đợt điều trị bệnh án", error.toString());
                                            for (Map.Entry<String, String> entry : error.entrySet()) {
                                                masterError.get(model.getCode()).put("stage[" + item.getStages().indexOf(stage) + "]." + entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            List<PqmArvStageEntity> stages = stageService.findByArvIDAndRegistrationTime(model.getID(), stage.getRegistrationTime());

                                            PqmArvStageEntity im;
                                            if (stages == null) {
                                                im = new PqmArvStageEntity();
                                                im = getStage(stage, im);
                                                im.setArvID(model.getID());
                                                im.setSiteID(model.getSiteID());
                                                //save
                                                stageService.saveApi(im);
                                            } else if (stages.size() == 1) {
                                                im = stages.get(0);
                                                im = getStage(stage, im);
                                                im.setArvID(model.getID());
                                                im.setSiteID(model.getSiteID());
                                                //save
                                                stageService.saveApi(im);
                                            } else {
                                                error.put("registrationTime", "Bệnh án có " + stages.size() + " đợt điều trị có cùng ngày đăng ký trong hệ thống");
                                                if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                    masterError.put(item.getCode(), new HashMap<String, String>());
                                                }
                                                for (Map.Entry<String, String> entry : error.entrySet()) {
                                                    masterError.get(model.getCode()).put("stage[" + item.getStages().indexOf(stage) + "]." + entry.getKey(), entry.getValue());
                                                }
                                            }
                                        }

                                    }
                                }

                                if (item.getVirals() != null && !item.getVirals().isEmpty()) {
                                    for (Viral viral : item.getVirals()) {
                                        HashMap<String, String> error = validateViral(viral);

                                        if (!error.isEmpty()) {
                                            if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                masterError.put(item.getCode(), new HashMap<String, String>());
                                            }
//                                            masterError.get(model.getCode()).put("Lần TLVR", error.toString());
                                            for (Map.Entry<String, String> entry : error.entrySet()) {
                                                masterError.get(model.getCode()).put("tlvr[" + item.getVirals().indexOf(viral) + "]." + entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            List<PqmArvViralLoadEntity> virals = viralService.findByArvAndTestTime(model.getID(), viral.getTestTime());

                                            PqmArvViralLoadEntity im;
                                            if (virals == null) {
                                                im = new PqmArvViralLoadEntity();
                                                im = getViral(im, viral);
                                                im.setArvID(model.getID());
                                                //save
                                                viralService.saveApi(im);
                                            } else if (virals.size() == 1) {
                                                im = virals.get(0);
                                                im = getViral(im, viral);
                                                im.setArvID(model.getID());
                                                //save
                                                viralService.saveApi(im);
                                            } else {
                                                error.put("registrationTime", "Bệnh án có " + virals.size() + " lần TLVR có cùng ngày khám trong hệ thống");
                                                if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                    masterError.put(item.getCode(), new HashMap<String, String>());
                                                }
//                                                masterError.get(model.getCode()).put("Lần TLVR", error.toString());
                                                for (Map.Entry<String, String> entry : error.entrySet()) {
                                                    masterError.get(model.getCode()).put("tlvr[" + item.getVirals().indexOf(viral) + "]." + entry.getKey(), entry.getValue());
                                                }
                                            }
                                        }
                                    }
                                }

                                if (item.getTests() != null && !item.getTests().isEmpty()) {
                                    for (Test test : item.getTests()) {
                                        HashMap<String, String> error = validateTest(test);

                                        if (!error.isEmpty()) {
                                            if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                masterError.put(item.getCode(), new HashMap<String, String>());
                                            }
//                                            masterError.get(model.getCode()).put("Lần khám dự phòng", error.toString());
                                            for (Map.Entry<String, String> entry : error.entrySet()) {
                                                masterError.get(model.getCode()).put("test[" + item.getTests().indexOf(test) + "]." + entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            List<PqmArvTestEntity> tests = testService.findByArvIDAndInhFromTime(model.getID(), test.getInhFromTime());

                                            PqmArvTestEntity im;
                                            if (tests == null) {
                                                im = new PqmArvTestEntity();
                                                im = getTest(im, test);
                                                im.setArvID(model.getID());
                                                //save
                                                testService.saveApi(im);
                                            } else if (tests.size() == 1) {
                                                im = tests.get(0);
                                                im = getTest(im, test);
                                                im.setArvID(model.getID());
                                                //save
                                                testService.saveApi(im);
                                            } else {
                                                error.put("registrationTime", "Bệnh án có " + tests.size() + " lần khám dự phòng có cùng ngày bắt đầu INH trong hệ thống");
                                                if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                    masterError.put(item.getCode(), new HashMap<String, String>());
                                                }
//                                                masterError.get(model.getCode()).put("Lần khám dự phòng", error.toString());
                                                for (Map.Entry<String, String> entry : error.entrySet()) {
                                                    masterError.get(model.getCode()).put("test[" + item.getTests().indexOf(test) + "]." + entry.getKey(), entry.getValue());
                                                }
                                            }
                                        }
                                    }
                                }

                                if (item.getVisits() != null && !item.getVisits().isEmpty()) {
                                    for (Visit visit : item.getVisits()) {
                                        HashMap<String, String> error = validateVisit(visit);

                                        if (!error.isEmpty()) {
                                            if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                masterError.put(item.getCode(), new HashMap<String, String>());
                                            }
//                                            masterError.get(model.getCode()).put("Lần khám", error.toString());
                                            for (Map.Entry<String, String> entry : error.entrySet()) {
                                                masterError.get(model.getCode()).put("visit[" + item.getVisits().indexOf(visit) + "]." + entry.getKey(), entry.getValue());
                                            }
                                        } else {
                                            List<PqmArvVisitEntity> visits = visitService.findByArvIDAndExaminationTime(model.getID(), visit.getExaminationTime());

                                            PqmArvVisitEntity im;
                                            if (visits == null) {
                                                im = new PqmArvVisitEntity();
                                                im = getVisit(im, visit);
                                                im.setArvID(model.getID());
                                                //save
                                                visitService.saveApi(im);
                                            } else if (visits.size() == 1) {
                                                im = visits.get(0);
                                                im = getVisit(im, visit);
                                                im.setArvID(model.getID());
                                                //save
                                                visitService.saveApi(im);
                                            } else {
                                                error.put("registrationTime", "Bệnh án có " + visits.size() + " lần khám có cùng ngày khám trong hệ thống");
                                                if (masterError.getOrDefault(item.getCode(), null) == null) {
                                                    masterError.put(item.getCode(), new HashMap<String, String>());
                                                }
//                                                masterError.get(model.getCode()).put("Lần khám", error.toString());
                                                for (Map.Entry<String, String> entry : error.entrySet()) {
                                                    masterError.get(model.getCode()).put("visit[" + item.getVisits().indexOf(visit) + "]." + entry.getKey(), entry.getValue());
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    } catch (Exception e) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("code", e.getMessage());
                        masterError.put(item.getCode(), errors);
                        errored++;
                    }
                }
            } else {
                return new ResponseHub<>(false, "EXCEPTION", "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu");
            }

            if (!masterError.isEmpty()) {
                return new ResponseHub<>(false, "VALIDATION_ERROR", masterError, form.getDatas().size());
            }

            return new ResponseHub<>(true, "SUCCESS", form.getDatas().size(), updated);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getLocalizedMessage());
        }
    }

    private PqmArvVisitEntity getVisit(PqmArvVisitEntity model, Visit item) {
        model.setExaminationTime(item.getExaminationTime() == null ? null : TextUtils.convertDate(item.getExaminationTime(), FORMATDATE));
        model.setAppointmentTime(item.getAppointmentTime() == null ? null : TextUtils.convertDate(item.getAppointmentTime(), FORMATDATE));
        model.setMutipleMonth(item.getMutipleMonth());
        model.setAdmission_code(item.getAdmission_code());
        try {
            model.setDaysReceived(Integer.valueOf(item.getDaysReceived()));
        } catch (Exception e) {
            model.setDaysReceived(Integer.valueOf("0"));
        }
        return model;

    }

    private PqmArvTestEntity getTest(PqmArvTestEntity model, Test item) {

        model.setInhFromTime(item.getInhFromTime() == null ? null : TextUtils.convertDate(item.getInhFromTime(), FORMATDATE));
        model.setInhToTime(item.getInhToTime() == null ? null : TextUtils.convertDate(item.getInhToTime(), FORMATDATE));
        model.setInhEndCause(item.getInhEndCause());

        return model;

    }

    private PqmArvViralLoadEntity getViral(PqmArvViralLoadEntity model, Viral item) {
        model.setTestTime(item.getTestTime() == null ? null : TextUtils.convertDate(item.getTestTime(), FORMATDATE));
        model.setResultTime(item.getResultTime() == null ? null : TextUtils.convertDate(item.getResultTime(), FORMATDATE));
        model.setResultNumber(item.getResultNumber());
        model.setResult(item.getResult());

        return model;

    }

    private PqmArvStageEntity getStage(Stage stage, PqmArvStageEntity model) {

        model.setRegistrationTime(TextUtils.convertDate(stage.getRegistrationTime(), FORMATDATE));
        model.setRegistrationType(stage.getRegistrationType());
        model.setStatusOfTreatmentID(stage.getStatusOfTreatmentID());
        model.setTreatmentTime(TextUtils.convertDate(stage.getTreatmentTime(), FORMATDATE));
        model.setEndCase(stage.getEndCase());
        model.setEndTime(TextUtils.convertDate(stage.getEndTime(), FORMATDATE));
        model.setTranferFromTime(TextUtils.convertDate(stage.getTranferFromTime(), FORMATDATE));
        model.setRegistrationTime(TextUtils.convertDate(stage.getRegistrationTime(), FORMATDATE));
        model.setRegistrationType(stage.getRegistrationType());
        model.setTreatment_session_id(stage.getTreatment_session_id());
        model.setEndCase(stage.getEndCase());

        return model;
    }

    @RequestMapping(value = "/hub/v1/arv.api", method = RequestMethod.DELETE)
    public ResponseHub<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("customer_system_id") String code
    ) {
        try {
            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByArvSiteCode(siteCode);
            if (site == null) {
                return new ResponseHub<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmArvEntity model = pqmArvService.findBySiteIDAndCustomer_system_id(code);
            if (model == null) {
                return new ResponseHub<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            if (!model.getSiteID().equals(site.getID())) {
                return new ResponseHub<>(false, "EXCEPTION", "Customer ID " + code + " thuộc quản lý của đơn vị khác");
            }

            List<PqmArvStageEntity> stage = stageService.findByArvID(model.getID());
            List<PqmArvVisitEntity> visit = visitService.findByArvID(model.getID());
            List<PqmArvViralLoadEntity> viral = viralService.findByArvID(model.getID());
            List<PqmArvTestEntity> test = testService.findByArvID(model.getID());
            if (stage != null) {
                Set<Long> IDs = new HashSet<>();
                IDs.addAll(CollectionUtils.collect(stage, TransformerUtils.invokerTransformer("getID")));
                stageService.deleteAll(IDs);
            }
            if (stage != null) {
                Set<Long> IDs = new HashSet<>();
                IDs.addAll(CollectionUtils.collect(stage, TransformerUtils.invokerTransformer("getID")));
                stageService.deleteAll(IDs);
            }
            if (visit != null) {
                Set<Long> IDs = new HashSet<>();
                IDs.addAll(CollectionUtils.collect(visit, TransformerUtils.invokerTransformer("getID")));
                visitService.deleteAll(IDs);
            }
            if (viral != null) {
                Set<Long> IDs = new HashSet<>();
                IDs.addAll(CollectionUtils.collect(viral, TransformerUtils.invokerTransformer("getID")));
                viralService.deleteAll(IDs);
            }
            if (test != null) {
                Set<Long> IDs = new HashSet<>();
                IDs.addAll(CollectionUtils.collect(test, TransformerUtils.invokerTransformer("getID")));
                testService.deleteAll(IDs);
            }
            pqmArvService.deleteById(model.getID());
            return new ResponseHub<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateArv(Arv form, String provinceID) {
        HashMap<String, String> errors = new HashMap<>();

        if (StringUtils.isBlank(form.getProvinceID())) {
            errors.put("provinceID", "Tỉnh không được để trống");
        } else {
            if (!provinceID.equals(form.getProvinceID())) {
                errors.put("provinceID", "Tỉnh không đúng với tỉnh của cơ sở nhận API");
            }
        }

        if (StringUtils.isEmpty(form.getCode())) {
            errors.put("code", "Mã bệnh án không được để trống");
        }

        if (StringUtils.isEmpty(form.getCustomer_system_id())) {
            errors.put("customer_system_id", "customer_system_id không được để trống");
        }

        if (StringUtils.isNotEmpty(form.getCode()) && form.getCode().length() < 7) {
            errors.put("code", "Mã bệnh án không đúng định dạng: Ví dụ: 82500.02-02388");
        }

        if (StringUtils.isNotBlank(form.getDob()) && !isThisDateValid(form.getDob())) {
            errors.put("dob", "Ngày sinh không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getDob()) && isThisDateValid(form.getDob())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getDob(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("dob", "Ngày sinh không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && !isThisDateValid(form.getFirstTreatmentTime())) {
            errors.put("firstTreatmentTime", "Ngày bắt đầu điều trị không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getConfirmTime()) && !isThisDateValid(form.getConfirmTime())) {
            errors.put("confirmTime", "Ngày XN khẳng định không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getFirstTreatmentTime()) && isThisDateValid(form.getFirstTreatmentTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getFirstTreatmentTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("firstTreatmentTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getConfirmTime()) && isThisDateValid(form.getConfirmTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getConfirmTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("confirmTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }

        return errors;
    }

    private HashMap<String, String> validateStage(Stage form) {
        HashMap<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(form.getRegistrationTime())) {
            errors.put("registrationTime", "Ngày đăng ký không được để trống");
        }

        String registrationTime = StringUtils.isEmpty(form.getRegistrationTime()) ? null : form.getRegistrationTime();
        String endTime = StringUtils.isEmpty(form.getEndTime()) ? null : form.getEndTime();

        if (registrationTime == null) {
            errors.put("registrationTime", "Ngày đăng ký không được để trống");
        }
        if (StringUtils.isEmpty(form.getRegistrationType())) {
            errors.put("registrationType", "Loại đăng ký không được để trống");
        }
        if (StringUtils.isNotBlank(registrationTime) && !isThisDateValid(registrationTime)) {
            errors.put("registrationTime", "Ngày đăng ký không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(registrationTime) && isThisDateValid(registrationTime)) {
            Date today = new Date();
            Date time = TextUtils.convertDate(registrationTime, FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("registrationTime", "Ngày đăng ký không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.put("endTime", "Ngày kết thúc không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getEndTime()) && isThisDateValid(form.getEndTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getEndTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("endTime", "Ngày kết thúc không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getTreatmentTime()) && !isThisDateValid(form.getTreatmentTime())) {
            errors.put("treatmentTime", "Ngày điều trị không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getTreatmentTime()) && isThisDateValid(form.getTreatmentTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getTreatmentTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("treatmentTime", "Ngày điều trị không được sau ngày hiện tại");
            }
        }

        return errors;
    }

    private HashMap<String, String> validateViral(Viral form) {
        HashMap<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(form.getTestTime())) {
            errors.put("testTime", "Ngày XN không được để trống");
        }

        if (StringUtils.isNotBlank(form.getTestTime()) && !isThisDateValid(form.getTestTime())) {
            errors.put("testTime", "Ngày XN không đúng định dạng yyyy-MM-dd");
        }

        if (StringUtils.isNotBlank(form.getTestTime()) && isThisDateValid(form.getTestTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getTestTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("testTime", "Ngày XN không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getResultTime()) && !isThisDateValid(form.getResultTime())) {
            errors.put("resultTime", "Ngày nhận kết quả không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getResultTime()) && isThisDateValid(form.getResultTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getResultTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("resultTime", "Ngày nhận kết quả không được sau ngày hiện tại");
            }
        }

        return errors;
    }

    private HashMap<String, String> validateTest(Test form) {
        HashMap<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(form.getInhFromTime())) {
            errors.put("inhFromTime", "Ngày bắt đầu không được để trống");
        }
        if (StringUtils.isNotBlank(form.getInhFromTime()) && !isThisDateValid(form.getInhFromTime())) {
            errors.put("inhFromTime", "Ngày bắt đầu không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getInhFromTime()) && isThisDateValid(form.getInhFromTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getInhFromTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("inhFromTime", "Ngày bắt đầu không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getInhToTime()) && !isThisDateValid(form.getInhToTime())) {
            errors.put("inhToTime", "Ngày kết thúc không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getInhToTime()) && isThisDateValid(form.getInhToTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getInhToTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("inhToTime", "Ngày kết thúc không được sau ngày hiện tại");
            }
        }

        return errors;
    }

    private HashMap<String, String> validateVisit(Visit form) {
        HashMap<String, String> errors = new HashMap<>();

        if (StringUtils.isEmpty(form.getExaminationTime())) {
            errors.put("examinationTime", "Ngày khám không được để trống");
        }
        if (StringUtils.isNotBlank(form.getExaminationTime()) && !isThisDateValid(form.getExaminationTime())) {
            errors.put("examinationTime", "Ngày khám không đúng định dạng yyyy-MM-dd");
        }
        if (StringUtils.isNotBlank(form.getExaminationTime()) && isThisDateValid(form.getExaminationTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getExaminationTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("examinationTime", "Ngày khám không được sau ngày hiện tại");
            }
        }
        if (StringUtils.isNotBlank(form.getAppointmentTime()) && !isThisDateValid(form.getAppointmentTime())) {
            errors.put("appointmentTime", "Ngày hẹn khám không đúng định dạng yyyy-MM-dd");
        }
        try {
            if (StringUtils.isNotEmpty(form.getDaysReceived())) {
                int a = Integer.valueOf(form.getDaysReceived());
                if (a < 0) {
                    errors.put("daysReceived", "Số ngày nhận thuốc chỉ được nhập nguyên dương");
                }
            }
        } catch (Exception e) {
            errors.put("daysReceived", "Số ngày nhận thuốc chỉ được nhập nguyên dương");
        }

        return errors;
    }

    private boolean checkNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        try {
            Integer.valueOf(number);
            if (Integer.valueOf(number) < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //check valid date
    private boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/hub/v1/arv-delete.api", method = RequestMethod.POST)
    public ResponseHub<?> actionDelMul(@RequestParam("secret") String secret, @RequestBody String content) {
        try {

            Gson g = new Gson();
            PqmArvDelList form = g.fromJson(content.trim(), PqmArvDelList.class);

            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})", form.getDatas().size());
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            int updated = 0;
            for (PqmArvDelForm item : form.getDatas()) {
                try {
                    SiteEntity site = siteService.findByArvSiteCode(item.getSiteCode());
                    if (site == null) {
                        throw new Exception("Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                    }
                    PqmArvEntity model = pqmArvService.findBySiteIDAndCustomer_system_id(item.getCustomer_system_id());
                    if (model == null) {
                        throw new Exception("Không tìm thấy bản ghi trong hệ thống");
                    }
                    if (!model.getSiteID().equals(site.getID())) {
                        throw new Exception("Customer ID " + item.getCustomer_system_id() + " thuộc quản lý của đơn vị khác");
                    }

                    List<PqmArvStageEntity> stage = stageService.findByArvID(model.getID());
                    List<PqmArvVisitEntity> visit = visitService.findByArvID(model.getID());
                    List<PqmArvViralLoadEntity> viral = viralService.findByArvID(model.getID());
                    List<PqmArvTestEntity> test = testService.findByArvID(model.getID());
                    if (stage != null && !stage.isEmpty()) {
                        Set<Long> IDs = new HashSet<>();
                        IDs.addAll(CollectionUtils.collect(stage, TransformerUtils.invokerTransformer("getID")));
                        stageService.deleteAll(IDs);
                    }
                    if (visit != null && !visit.isEmpty()) {
                        Set<Long> IDs = new HashSet<>();
                        IDs.addAll(CollectionUtils.collect(visit, TransformerUtils.invokerTransformer("getID")));
                        visitService.deleteAll(IDs);
                    }
                    if (viral != null && !viral.isEmpty()) {
                        Set<Long> IDs = new HashSet<>();
                        IDs.addAll(CollectionUtils.collect(viral, TransformerUtils.invokerTransformer("getID")));
                        viralService.deleteAll(IDs);
                    }
                    if (test != null && !test.isEmpty()) {
                        Set<Long> IDs = new HashSet<>();
                        IDs.addAll(CollectionUtils.collect(test, TransformerUtils.invokerTransformer("getID")));
                        testService.deleteAll(IDs);
                    }
                    pqmArvService.deleteById(model.getID());
                    updated++;
                } catch (Exception e) {
                    e.printStackTrace();
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("customer_system_id", e.getMessage());
                    masterError.put(item.getCustomer_system_id(), errors);
                }
            }
            if (!masterError.isEmpty()) {
                return new ResponseHub<>(false, "VALIDATION_ERROR", masterError, form.getDatas().size(), updated);
            }
            return new ResponseHub(true, "SUCCESS", form.getDatas().size(), updated);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }
    }
}
