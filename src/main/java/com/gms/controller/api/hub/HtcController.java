package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.bean.ResponseHub;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.RegexpEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmHtcApiElogForm;
import com.gms.entity.form.PqmHtcApiMainForm;
import com.gms.entity.form.hub.PqmHtcDelForm;
import com.gms.entity.form.hub.PqmHtcDelList;
import com.gms.service.LocationsService;
import com.gms.service.PqmVctService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_htc_api")
public class HtcController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmVctService pqmVctService;

    @RequestMapping(value = "/hub/v1/htc/elog.api", method = RequestMethod.POST)
    public ResponseHub<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            PqmHtcApiMainForm form = g.fromJson(content.trim(), PqmHtcApiMainForm.class);

            if (!checksum(secret)) {
                log("/hub/v1/htc/elog.api", "POST", null, secret, content, false, "INCORRECT_CHECKSUM",
                        "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})"
                );
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})", form.getDatas().size());
            }

            int created = 0;
            int updated = 0;
            int errored = 0;
            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (PqmHtcApiElogForm item : form.getDatas()) {
                    String code = String.format("%s-%s", item.getSiteCode(), item.getCode());
                    if (item.getService_type() != null && item.getService_type().toUpperCase().equals("KC")) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("service_type", "KC bỏ qua");
                        masterError.put(code.toUpperCase(), errors);
                        errored++;
                        continue;
                    }
                    SiteEntity site = siteService.findByElogSiteCode(item.getSiteCode());
                    if (site == null) {
                        log("/hub/v1/htc/elog.api", "POST", Long.parseLong("0"), secret, content, false, "EXCEPTION",
                                "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình"
                        );
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(code.toUpperCase(), errors);
                        errored++;
                        continue;
                    }

                    HashMap<String, String> errors = validateCustom(item);
                    if (!errors.isEmpty()) {
                        errored++;
                        masterError.put(code.toUpperCase(), errors);
                    } else {
                        item.setProvince_code(form.getProvince_code());
                        item.setMonth(form.getMonth());
                        item.setYear(form.getYear());
                        item.setQuantity(form.getQuantity());

                        PqmVctEntity model = getVisit(item, site.getID());
                        model.setSiteID(site.getID());
                        if (model.getID() != null && model.getID() > 0) {
                            updated++;
                        } else {
                            created++;
                        }
                        pqmVctService.save(model);
                    }

                }
            } else {
                log("/hub/v1/htc/elog.api", "POST", Long.parseLong("0"), secret, content, false, "EXCEPTION",
                        "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu"
                );
                return new ResponseHub<>(false, "EXCEPTION", "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu");
            }

            if (!masterError.isEmpty()) {
                log("/hub/v1/htc/elog.api", "POST", Long.parseLong("0"), secret, content, false, "VALIDATION_ERROR",
                        masterError.toString()
                );
                return new ResponseHub<>(false, "VALIDATION_ERROR", masterError, form.getDatas().size(), updated);
            }

            log("/hub/v1/htc/elog.api", "POST", Long.parseLong("0"), secret, content, true, "SUCCESS",
                    masterError.toString()
            );
            return new ResponseHub(true, "SUCCESS", form.getDatas().size(), updated);
        } catch (Exception e) {
            e.printStackTrace();
            log("/hub/v1/htc/elog.api", "POST", Long.parseLong("0"), secret, content, false, "EXCEPTION",
                    e.getMessage()
            );
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }
    }

    @RequestMapping(value = "/hub/v1/htc/elog.api", method = RequestMethod.DELETE)
    public ResponseHub<?> actionDel(@RequestParam("secret") String secret,
            //            @RequestParam("code") String code,
            @RequestParam("customer_id") String code,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("patientName") String patientName
    ) {
        try {
            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByElogSiteCode(siteCode);
            if (site == null) {
                log("/hub/v1/htc/elog.api", "DELETE", Long.parseLong("0"), secret, null, false, "EXCEPTION",
                        "Mã cơ sở " + siteCode + " chưa được cấu hình"
                );
                return new ResponseHub<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            String maKhachHang = String.format("%s-%s", siteCode, code);
//            PqmVctEntity model = pqmVctService.findBySiteAndCode(site.getID(), maKhachHang.toUpperCase());
            PqmVctEntity model = pqmVctService.findByCustomerId(code);
            if (model == null) {
                return new ResponseHub<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }
            pqmVctService.remove(model.getID());
            return new ResponseHub<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHub<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(PqmHtcApiElogForm form) {
        HashMap<String, String> errors = new HashMap<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");

        if (StringUtils.isEmpty(form.getConfirmResultsID())) {
            errors.put("confirmResultsID", "Kết quả XNKĐ không được để trống hoặc phải là dương tính");
        }
        if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && !form.getConfirmResultsID().equals("2")) {
            errors.put("confirmResultsID", "Kết quả XNKĐ không được để trống hoặc phải là dương tính");
        }

        // Validate bắt buộc nếu kết quả xét nghiệm có phản ứng
        if ("2".equals(form.getTestResultsID())) {
            // Validate Họ tên
            if (form.getPatientName() == null || "".equals(form.getPatientName())) {
                errors.put("patientName", "Tên khách hàng không được để trống");
            }
        }

        if (StringUtils.isEmpty(form.getSiteCode())) {
            errors.put("siteCode", "Mã cơ sở  không được để trống");
        }
        if (StringUtils.isEmpty(form.getCode())) {
            errors.put("code", "Mã số khách hàng không được để trống");
        }
        // Kiểm tra định ký tự đặc biêt của tên khách hàng
        if (StringUtils.isNotBlank(form.getPatientName()) && !TextUtils.removeDiacritical(form.getPatientName().trim()).matches(RegexpEnum.VN_CHAR)) {
            errors.put("patientName", "Tên khách hàng chỉ chứa các kí tự chữ cái");
        }

        if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && !form.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.put("yearOfBirth", "Năm sinh năm sinh gồm bốn chữ số");
        } else if ((StringUtils.isNotEmpty(form.getYearOfBirth())) && (Integer.parseInt(form.getYearOfBirth()) < 1900
                || Integer.parseInt(form.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.put("yearOfBirth", "Năm sinh năm sinh hợp lệ từ 1900 - năm hiện tại");
        }

        try {
            if (StringUtils.isNotEmpty(form.getResultsTime()) && StringUtils.isNotEmpty(form.getPreTestTime())
                    && isThisDateValid(form.getResultsTime()) && isThisDateValid(form.getPreTestTime())) {
                Date resultsTime = sdfrmt.parse(form.getResultsTime());
                Date advisoryeTime = sdfrmt.parse(form.getPreTestTime());
                if (resultsTime.compareTo(advisoryeTime) < 0) {
                    errors.put("resultsTime", "Ngày khách hàng nhận kết quả xét nghiệm phải sau ngày tư vấn trước XN");
                }
            }
            if (StringUtils.isNotEmpty(form.getResultsTime()) && isThisDateValid(form.getResultsTime())) {
                Date today = new Date();
                Date confirmDate = sdfrmt.parse(form.getResultsTime());
                if (confirmDate.compareTo(today) > 0) {
                    errors.put("resultsTime", "Ngày khách hàng nhận kết quả xét nghiệm không được sau ngày hiện tại");
                }
            }
        } catch (Exception e) {
        }
        if (StringUtils.isNotBlank(form.getResultsTime()) && !isThisDateValid(form.getResultsTime())) {
            errors.put("resultsTime", "Ngày khách hàng nhận kết quả xét nghiệm không đúng định dạng");
        }

        if (StringUtils.isNotEmpty(form.getConfirmResultsID()) && ConfirmTestResultEnum.DUONG_TINH.equals(form.getConfirmResultsID())
                && !TestResultEnum.CO_PHAN_UNG.getKey().equals(form.getTestResultsID())) {
            errors.put("testResultsID", "Kết quả xét nghiệm khẳng định chỉ có khi kết quả XNSL là có phản ứng");
        }

        if (StringUtils.isEmpty(form.getTestResultsID())) {
            errors.put("testResultsID", "Kết quả xét nghiệm sàng lọc không được bỏ trống");
        }
//        if (StringUtils.isEmpty(form.getPreTestTime())) {
//            errors.put("preTestTime", "Ngày lấy máu xét nghiệm sàng lọc không được bỏ trống");
//        }
        if (StringUtils.isEmpty(form.getConfirmTime())) {
            errors.put("confirmTime", "Ngày XN khẳng định không được bỏ trống");
        }

        if (StringUtils.isNotBlank(form.getPreTestTime()) && !isThisDateValid(form.getPreTestTime())) {
            errors.put("preTestTime", "Ngày lấy máu xét nghiệm sàng lọc không đúng định dạng");
        }

        if (StringUtils.isNotBlank(form.getConfirmTime()) && !isThisDateValid(form.getConfirmTime())) {
            errors.put("confirmTime", "Ngày xét nghiệm khẳng định không đúng định dạng");
        }

        try {
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && StringUtils.isNotEmpty(form.getPreTestTime())
                    && isThisDateValid(form.getConfirmTime()) && isThisDateValid(form.getPreTestTime())) {
                Date c5 = sdfrmt.parse(form.getPreTestTime());
                Date c6 = sdfrmt.parse(form.getConfirmTime());
                if (c5.compareTo(c6) > 0) {
                    errors.put("confirmTime", "Ngày xét nghiệm khẳng định không được nhỏ hơn ngày xét nghiệm sàng lọc");
                }
            }

        } catch (ParseException e) {
        }
        // Ngày XN sàng lọc
        try {
            // Ngày xét nghiệm khẳng định
            if (StringUtils.isNotEmpty(form.getConfirmTime()) && isThisDateValid(form.getConfirmTime())) {
                Date today = new Date();
                Date confirmDate = sdfrmt.parse(form.getConfirmTime());
                if (confirmDate.compareTo(today) > 0) {
                    errors.put("confirmTime", "Ngày xét nghiệm khẳng định không được sau ngày hiện tại");
                }
            }

            if (StringUtils.isNotBlank(form.getExchangeTime()) && !isThisDateValid(form.getExchangeTime())) {
                errors.put("exchangeTime", "Ngày chuyển gửi điều trị ARV không đúng định dạng");
            }
            // Ngày chuyển gửi
            if (StringUtils.isNotEmpty(form.getExchangeTime()) && isThisDateValid(form.getExchangeTime())) {
                Date today = new Date();
                Date exchangeTime = sdfrmt.parse(form.getExchangeTime());
                if (exchangeTime.compareTo(today) > 0) {
                    errors.put("exchangeTime", "Ngày chuyển gửi điều trị ARV không được sau ngày hiện tại");
                }
            }

        } catch (Exception e) {
        }
        return errors;
    }

    private boolean checkNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return true;
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

    private PqmVctEntity getVisit(PqmHtcApiElogForm item, Long siteID) {

        String code = String.format("%s-%s", item.getSiteCode(), item.getCode()).toLowerCase();

//        PqmVctEntity model = pqmVctService.findBySiteAndCode(siteID, code.toUpperCase());
        PqmVctEntity model = pqmVctService.findByCustomerId(item.getCustomer_id());
        if (model == null) {
            model = new PqmVctEntity();
        }

        model.setPatientPhone(item.getPatientPhone());

        model.setProvince_code(item.getProvince_code());
        model.setMonth(item.getMonth());
        model.setYear(item.getYear());
        model.setQuantity(item.getQuantity());
        model.setService_type(item.getService_type());

        model.setPermanentAddress(item.getPermanentAddress());
        model.setCurrentAddress(item.getCurrentAddress());

        model.setDistrict1(item.getDistrict1());
        model.setDistrict2(item.getDistrict2());
        model.setProvince1(item.getProvince1());
        model.setProvince2(item.getProvince2());

        Map<String, String> provinces = new HashMap<>();
        for (ProvinceEntity provinceEntity : locationService.findAllProvince()) {
            provinces.put(provinceEntity.getID(), provinceEntity.getName());
        }
        Map<String, String> districts = new HashMap<>();
        for (DistrictEntity districtEntity : locationService.findAllDistrict()) {
            districts.put(districtEntity.getID(), districtEntity.getName());
        }

        if (model.getDistrict1() != null && !model.getDistrict1().isEmpty() && districts.containsKey(model.getDistrict1())) {
            model.setPermanentAddress(model.getPermanentAddress() + "," + districts.get(model.getDistrict1()));
        }

        if (model.getProvince1() != null && !model.getProvince1().isEmpty() && provinces.containsKey(model.getProvince1())) {
            model.setPermanentAddress(model.getPermanentAddress() + "," + provinces.get(model.getProvince1()));
        }

        if (model.getDistrict2() != null && !model.getDistrict2().isEmpty() && districts.containsKey(model.getDistrict2())) {
            model.setCurrentAddress(model.getCurrentAddress() + "," + districts.get(model.getDistrict2()));
        }

        if (model.getProvince2() != null && !model.getProvince2().isEmpty() && provinces.containsKey(model.getProvince2())) {
            model.setCurrentAddress(model.getCurrentAddress() + "," + provinces.get(model.getProvince2()));
        }

        model.setData_sharing_acceptance(item.getData_sharing_acceptance());
        model.setCustomer_id(item.getCustomer_id());

        model.setCode(String.format("%s-%s", item.getSiteCode(), item.getCode()).toLowerCase());
        model.setCode(model.getCode().toUpperCase());

        model.setPatientName(WordUtils.capitalizeFully(item.getPatientName()));
        model.setPatientID(item.getPatientID());
        model.setIdentityCard(item.getPatientID());
        model.setYearOfBirth(item.getYearOfBirth());

//        model.setGenderID(item.getGenderID() == null || "".equals(item.getGenderID()) ? "" : item.getGenderID());
        model.setGenderID("3");
        if (item.getGenderID() != null) {
            if (item.getGenderID().toLowerCase().equals("male")) {
                model.setGenderID("1");
            } else if (item.getGenderID().toLowerCase().equals("female")) {
                model.setGenderID("2");
            } else {
                model.setGenderID("3");
            }
        }

        model.setObjectGroupID(StringUtils.isBlank(item.getObjectGroupID()) ? null : item.getObjectGroupID());

        model.setPreTestTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "yyyy-MM-dd"));
        model.setTestResultsID("2".equals(item.getTestResultsID()) ? "2" : "1".equals(item.getTestResultsID()) ? "1" : "3");
        model.setResultsTime(item.getResultsTime() == null || "".equals(item.getResultsTime()) ? null : TextUtils.convertDate(item.getResultsTime(), "yyyy-MM-dd"));
        model.setConfirmTestNo(item.getConfirmTestNo());
        model.setConfirmResultsID("".equals(item.getConfirmResultsID()) || item.getConfirmResultsID() == null ? "" : item.getConfirmResultsID());
        model.setConfirmTime(item.getConfirmTime() == null || "".equals(item.getConfirmTime()) ? null : TextUtils.convertDate(item.getConfirmTime(), "yyyy-MM-dd"));
        model.setResultsSiteTime(item.getResultsSiteTime() == null || "".equals(item.getResultsSiteTime()) ? null : TextUtils.convertDate(item.getResultsSiteTime(), "yyyy-MM-dd"));

        model.setExchangeTime(item.getExchangeTime() == null || "".equals(item.getExchangeTime()) ? null : TextUtils.convertDate(item.getExchangeTime(), "yyyy-MM-dd"));
        model.setRegisterTherapyTime(item.getRegisterTherapyTime() == null || "".equals(item.getRegisterTherapyTime()) ? null : TextUtils.convertDate(item.getRegisterTherapyTime(), "yyyy-MM-dd"));
//        model.setRegisteredTherapySite(options.get("treatment-facility").get(model.getRegisteredTherapySite()));
        model.setRegisteredTherapySite(model.getRegisteredTherapySite());

        model.setAdvisoryeTime(item.getPreTestTime() == null || "".equals(item.getPreTestTime()) ? null : TextUtils.convertDate(item.getPreTestTime(), "yyyy-MM-dd"));
        model.setTherapyNo(item.getTherapyNo());
        model.setEarlyDiagnose(item.getEarlyDiagnose());
        model.setRegisteredTherapySite(item.getRegisteredTherapySite());
        model.setSource("API ELOG");
        model.setInsuranceNo(item.getInsuranceNo());
        model.setNote(item.getNote());

        return model;
    }

    @RequestMapping(value = "/hub/v1/htc/elog-delete.api", method = RequestMethod.POST)
    public ResponseHub<?> actionDelMul(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            PqmHtcDelList form = g.fromJson(content.trim(), PqmHtcDelList.class);

            if (!checksum(secret)) {
                return new ResponseHub<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})", form.getDatas().size());
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            int updated = 0;
            for (PqmHtcDelForm item : form.getDatas()) {
                try {
                    SiteEntity site = siteService.findByElogSiteCode(item.getSiteCode());
                    if (site == null) {
                        throw new Exception("Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                    }
                    PqmVctEntity model = pqmVctService.findByCustomerId(item.getCustomer_id());
                    if (model == null) {
                        throw new Exception("Không tìm thấy bản ghi trong hệ thống");
                    }
                    if (!model.getSiteID().equals(site.getID())) {
                        throw new Exception("Customer ID " + item.getCustomer_id() + " thuộc quản lý của đơn vị khác");
                    }
                    pqmVctService.remove(model.getID());
                    updated++;
                } catch (Exception e) {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("customer_id", e.getMessage());
                    masterError.put(item.getCustomer_id(), errors);
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
