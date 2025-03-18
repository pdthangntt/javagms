package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.bean.ResponseHub;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.db.PqmPrepStageEntity;
import com.gms.entity.db.PqmPrepVisitEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.pqm_prep_api.Prep;
import com.gms.entity.form.pqm_prep_api.PrepMainForm;
import com.gms.entity.form.pqm_prep_api.Stage;
import com.gms.entity.form.pqm_prep_api.Visit;
import com.gms.service.LocationsService;
import com.gms.service.PqmPrepService;
import com.gms.service.PqmPrepStageService;
import com.gms.service.PqmPrepVisitService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_prep_api")
public class PrepController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmPrepService pqmService;
    @Autowired
    private PqmPrepStageService stageService;
    @Autowired
    private PqmPrepVisitService visitService;

    private static final String FORMATDATE = "yyyy-MM-dd";

    @RequestMapping(value = "/hub/v1/prep.api", method = RequestMethod.POST)
    public ResponseHub<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            PrepMainForm form = g.fromJson(content.trim(), PrepMainForm.class);

            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            int created = 0;
            int updated = 0;
            int errored = 0;

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (Prep item : form.getDatas()) {
                    SiteEntity site = siteService.findByPrepSiteCode(item.getSiteCode());
                    if (site == null) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(StringUtils.isBlank(item.getCode()) ? "empty-code" : item.getCode(), errors);
                        errored++;
                        continue;
                    }

                    HashMap<String, String> errors = validateCustom(item);
                    if (!errors.isEmpty()) {
                        masterError.put(StringUtils.isEmpty(item.getCode()) ? "empty-code" : item.getCode(), errors);
                        errored++;
                    } else {
                        PqmPrepEntity model = getPrep(item, site.getID());
                        model.setMonth(form.getMonth());
                        model.setYear(form.getYear());
                        model.setProvince_code(form.getProvince_code());
                        model.setQuantity(form.getQuantity());
                        model.setCustomer_id(item.getCustomer_id());
                        model.setDistrict_code(item.getDistrict_code());
                        model.setClient_address(item.getClient_address());
                        model.setPrep_start_date(StringUtils.isEmpty(item.getPrep_start_date()) ? null : TextUtils.convertDate(item.getPrep_start_date(), FORMATDATE));
                        created++;

                        model = pqmService.save(model);
                        if (model.getID() != null) {
                            if (item.getStages() != null && !item.getStages().isEmpty()) {
                                for (Stage stage : item.getStages()) {
                                    PqmPrepStageEntity stageEntity = getStage(stage, model.getID());
                                    stageEntity = stageService.save(stageEntity);

                                    if (stage.getVisits() != null && !stage.getVisits().isEmpty()) {
                                        for (Visit visit : stage.getVisits()) {
                                            PqmPrepVisitEntity visitEntity = getVisit(visit, model.getID(), stageEntity.getID());
                                            visitService.save(visitEntity);
                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            } else {
                return new ResponseHub<>(false, "EXCEPTION", "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu");
            }

            if (!masterError.isEmpty()) {
                return new ResponseHub<>(false, "VALIDATION_ERROR", masterError, errored);
            }

            return new ResponseHub<>(true, "SUCCESS", created, updated);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }
    }

    private PqmPrepVisitEntity getVisit(Visit visit, Long prepID, Long stageID) {
        List<PqmPrepVisitEntity> items = visitService.findByPrepIDAndExaminationTime(prepID, TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, visit.getExaminationTime()));
        PqmPrepVisitEntity model;
        if (items == null) {
            model = new PqmPrepVisitEntity();
        } else {
            model = items.get(0);
        }

        model.setStageID(stageID);
        model.setPrepID(prepID);
        model.setExaminationTime(StringUtils.isEmpty(visit.getExaminationTime()) ? null : TextUtils.convertDate(visit.getExaminationTime(), FORMATDATE));
        model.setResultsID(visit.getResultsID());
        model.setTreatmentStatus(visit.getTreatmentStatus());
        model.setAppointmentTime(StringUtils.isEmpty(visit.getAppointmentTime()) ? null : TextUtils.convertDate(visit.getAppointmentTime(), FORMATDATE));
        model.setDaysReceived(StringUtils.isEmpty(visit.getDaysReceived()) ? Integer.valueOf("0") : Integer.valueOf(visit.getDaysReceived()));
        model.setTreatmentRegimen(visit.getTreatmentRegimen());

        model.setCreatinin(visit.getCreatinin());
        model.sethBsAg(visit.gethBsAg());
        model.setGanC(visit.getGanC());
        model.setGiangMai(visit.getGiangMai());
        model.setClamydia(visit.getClamydia());
        model.setLau(visit.getLau());
        model.setTuanThuDieuTri(visit.getTuanThuDieuTri());
        model.setTacDungPhu(visit.getTacDungPhu());
        model.setSot(visit.getSot());
        model.setMetMoi(visit.getMetMoi());
        model.setDauCo(visit.getDauCo());
        model.setPhatBan(visit.getPhatBan());

        return model;
    }

    private PqmPrepStageEntity getStage(Stage entity, Long prepID) {
        List<PqmPrepStageEntity> items = stageService.findByPrepIDAndStartTime(prepID, TextUtils.formatDate(FORMATDATE, FORMATDATE_SQL, entity.getStartTime()));
        PqmPrepStageEntity item;
        if (items == null) {
            item = new PqmPrepStageEntity();
        } else {
            item = items.get(0);
        }
        item.setPrepID(prepID);
        item.setStartTime(TextUtils.convertDate(entity.getStartTime(), FORMATDATE));
        item.setEndTime(TextUtils.convertDate(entity.getEndTime(), FORMATDATE));
        item.setType(entity.getType());
        item.setEndCause(entity.getEndCause());

        return item;
    }

    private PqmPrepEntity getPrep(Prep item, Long siteID) {
        PqmPrepEntity model = pqmService.findByCustomer_id(siteID, item.getCustomer_id());
        if (model != null) {
            //Khoá nếu tìm thấy
            pqmService.deleteByID(model.getID());
            Set<Long> stIDs = new HashSet<>();
            Set<Long> vtIDs = new HashSet<>();
            for (PqmPrepStageEntity pqmPrepStageEntity : stageService.findByPrepID(model.getID())) {
                stIDs.add(pqmPrepStageEntity.getID());
            }
            for (PqmPrepVisitEntity e : visitService.findByPrepID(model.getID())) {
                vtIDs.add(e.getID());
            }
            stageService.deleteAll(stIDs);
            visitService.deleteAll(vtIDs);
        }

        model = new PqmPrepEntity();

        model.setCode(item.getCode().toUpperCase());
        model.setType(item.getType());

        //Trường cơ bản prep
        model.setGenderID(item.getGenderID());
        model.setYearOfBirth(item.getYearOfBirth());
        model.setObjectGroupID(item.getObjectGroupID());
        model.setIdentityCard(item.getIdentityCard());
        model.setFullName(TextUtils.toFullname(item.getFullName()));
        model.setPatientPhone(item.getPatientPhone());

        model.setStartTime(StringUtils.isEmpty(item.getStartTime()) ? null : TextUtils.convertDate(item.getStartTime(), FORMATDATE));
        model.setEndTime(StringUtils.isEmpty(item.getEndTime()) ? null : TextUtils.convertDate(item.getEndTime(), FORMATDATE));

        model.setProjectSupport(item.getProjectSupport());
        model.setSource(item.getSource());
        model.setOtherSite(item.getOtherSite());

        model.setSiteID(siteID);
        model.setType(item.getType());
        model.setInsuranceNo(item.getInsuranceNo());

        return model;
    }

    @RequestMapping(value = "/hub/v1/prep.api", method = RequestMethod.DELETE)
    public ResponseHub<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("customer_id") String code
    ) {
        try {
            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByPrepSiteCode(siteCode);
            if (site == null) {
                return new ResponseHub<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmPrepEntity model = pqmService.findByCustomer_id(site.getID(), code);
            if (model == null) {
                return new ResponseHub<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            if (!model.getSiteID().equals(site.getID())) {
                return new ResponseHub<>(false, "EXCEPTION", "Customer ID " + code + " thuộc quản lý của đơn vị khác");
            }

            pqmService.deleteByID(model.getID());
            Set<Long> stIDs = new HashSet<>();
            Set<Long> vtIDs = new HashSet<>();
            for (PqmPrepStageEntity pqmPrepStageEntity : stageService.findByPrepID(model.getID())) {
                stIDs.add(pqmPrepStageEntity.getID());
            }
            for (PqmPrepVisitEntity e : visitService.findByPrepID(model.getID())) {
                vtIDs.add(e.getID());
            }
            stageService.deleteAll(stIDs);
            visitService.deleteAll(vtIDs);

            return new ResponseHub<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(Prep form) {
        HashMap<String, String> errors = new HashMap<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isBlank(form.getStartTime())) {
            errors.put("startTime", "Ngày bắt đầu điều trị không được để trống");
        } else {
            if (!isThisDateValid(form.getStartTime())) {
                errors.put("startTime", "Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
            }
        }

        if (StringUtils.isBlank(form.getCode())) {
            errors.put("code", "Mã khách hàng không được để trống");
        }

        if (StringUtils.isBlank(form.getCustomer_id())) {
            errors.put("customer_id", "Customer_id không được để trống");
        }

        if (StringUtils.isNotBlank(form.getStartTime()) && isThisDateValid(form.getStartTime())) {
            Date today = new Date();
            Date time = TextUtils.convertDate(form.getStartTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("startTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
            }
        }

        if (StringUtils.isNotBlank(form.getEndTime()) && !isThisDateValid(form.getEndTime())) {
            errors.put("endTime", "Ngày kết thúc dùng PrEP không đúng định dạng dd/mm/yyyy");
        }
        if (getCompeDate(form.getStartTime(), form.getEndTime())) {
            errors.put("endTime", "Ngày kết thúc phải lớn hơn ngày bắt đầu điều trị");
        }
        if (StringUtils.isNotBlank(form.getStartTime()) && isThisDateValid(form.getStartTime())
                && StringUtils.isNotBlank(form.getEndTime()) && isThisDateValid(form.getEndTime())) {
            Date today = TextUtils.convertDate(form.getEndTime(), FORMATDATE);
            Date time = TextUtils.convertDate(form.getStartTime(), FORMATDATE);
            if (time.compareTo(today) > 0) {
                errors.put("startTime", "Ngày bắt đầu điều trị không được lớn hơn ngày kết thúc");
            }
        }

        if (form.getStages() != null && !form.getStages().isEmpty()) {

            for (Stage stage : form.getStages()) {
                String index = String.format("(%s-%s)", stage.getStartTime(), StringUtils.isEmpty(stage.getEndTime()) ? "Không rõ" : stage.getEndTime());

//                if (StringUtils.isBlank(stage.getType())) {
//                    errors.put("Giai đoạn " + (index) + " type", "Loại khách hàng không được để trống");
//                }
                if (StringUtils.isBlank(stage.getStartTime())) {
                    errors.put("Giai đoạn " + (index) + " startTime", "Ngày bắt đầu điều trị không được để trống");
                } else {
                    if (!isThisDateValid(stage.getStartTime())) {
                        errors.put("Giai đoạn " + (index) + " startTime", "Ngày bắt đầu điều trị không đúng định dạng dd/mm/yyyy");
                    }
                }
                if (StringUtils.isNotBlank(stage.getStartTime()) && isThisDateValid(stage.getStartTime())
                        && StringUtils.isNotBlank(stage.getEndTime()) && isThisDateValid(stage.getEndTime())) {
                    Date today = TextUtils.convertDate(stage.getEndTime(), FORMATDATE);
                    Date time = TextUtils.convertDate(stage.getStartTime(), FORMATDATE);
                    if (time.compareTo(today) > 0) {
                        errors.put("Giai đoạn " + (index) + " startTime", "Ngày bắt đầu điều trị không được lớn hơn ngày kết thúc");
                    }
                }

                if (StringUtils.isNotBlank(stage.getStartTime()) && isThisDateValid(stage.getStartTime())) {
                    Date today = new Date();
                    Date time = TextUtils.convertDate(stage.getStartTime(), FORMATDATE);
                    if (time.compareTo(today) > 0) {
                        errors.put("Giai đoạn " + (index) + " startTime", "Ngày bắt đầu điều trị không được sau ngày hiện tại");
                    }
                }

                if (StringUtils.isNotBlank(stage.getEndTime()) && !isThisDateValid(stage.getEndTime())) {
                    errors.put("Giai đoạn " + (index) + " endTime", "Ngày kết thúc dùng PrEP không đúng định dạng dd/mm/yyyy");
                }

                //Lượt khám
                if (stage.getVisits() != null && !stage.getVisits().isEmpty()) {

                    for (Visit visit : stage.getVisits()) {
                        String indexv = String.format("(%s)", StringUtils.isEmpty(visit.getExaminationTime()) ? "Không rõ" : visit.getExaminationTime());
                        if (StringUtils.isNotBlank(visit.getExaminationTime()) && !isThisDateValid(visit.getExaminationTime())) {
                            errors.put("Lượt khám " + (indexv) + " examinationTime", "Ngày KH đến khám không đúng định dạng dd/mm/yyyy");
                        }
                        if (StringUtils.isNotBlank(visit.getExaminationTime()) && isThisDateValid(visit.getExaminationTime())) {
                            Date today = new Date();
                            Date time = TextUtils.convertDate(visit.getExaminationTime(), FORMATDATE);
                            if (time.compareTo(today) > 0) {
                                errors.put("Lượt khám " + (indexv) + " examinationTime", "Ngày KH đến khám không được sau ngày hiện tại");
                            }
                        }
                        if (StringUtils.isBlank(visit.getExaminationTime())) {
                            errors.put("Lượt khám " + (indexv) + " examinationTime", "Ngày khám không được để trống");
                        }
                        if (StringUtils.isNotBlank(visit.getAppointmentTime()) && !isThisDateValid(visit.getAppointmentTime())) {
                            errors.put("Lượt khám " + (indexv) + " appointmentTime", "Ngày hẹn khám tiếp theo không đúng định dạng dd/mm/yyyy");
                        }
                        if (getCompeDate(visit.getExaminationTime(), visit.getAppointmentTime())) {
                            errors.put("Lượt khám " + (indexv) + " appointmentTime", "Ngày hẹn khám tiếp theo phải lớn hơn ngày KH đến khám");
                        }
                        if (!StringUtils.isEmpty(visit.getDaysReceived())) {
                            try {
                                if (visit.getDaysReceived().contains(".") || visit.getDaysReceived().contains("-")) {
                                    errors.put("Lượt khám " + (indexv) + " daysReceived", "Số thuốc được phát phải là số nguyên dương");
                                }
                                Integer.valueOf(visit.getDaysReceived());
                            } catch (Exception e) {
                                errors.put("Lượt khám " + (indexv) + " daysReceived", "Số thuốc được phát phải là số nguyên dương");
                            }
                        }
                    }
                }

            }

        }

        return errors;
    }

    private boolean getCompeDate(String date1, String date2) {
        if (!isThisDateValid(date1) || !isThisDateValid(date1)) {
            return false;
        }
        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotEmpty(date1) && StringUtils.isNotEmpty(date1)) {
                Date b = sdfrmt.parse(date1);
                Date a = sdfrmt.parse(date2);
                if (b.compareTo(a) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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

}
