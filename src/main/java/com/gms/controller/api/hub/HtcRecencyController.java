package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.PqmHtcApiRecencyForm;
import com.gms.entity.form.PqmHtcApiRecencyMainForm;
import com.gms.service.LocationsService;
import com.gms.service.PqmVctRecencyService;
import com.gms.service.PqmVctService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

@RestController(value = "hub_htc_recency_api")
public class HtcRecencyController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmVctRecencyService vctRecencyService;
    @Autowired
    private PqmVctService pqmVctService;

    @RequestMapping(value = "/hub/v1/htc/recency.api", method = RequestMethod.POST)
    public Response<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            PqmHtcApiRecencyMainForm form = g.fromJson(content.trim(), PqmHtcApiRecencyMainForm.class);

            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (PqmHtcApiRecencyForm item : form.getDatas()) {
                    SiteEntity site = siteService.findByElogSiteCode(item.getSiteCode());
                    if (site == null) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(StringUtils.isBlank(item.getCode()) ? "empty-code" : item.getCode(), errors);
                        continue;
                    }

                    HashMap<String, String> errors = validateCustom(item, siteService.findOne(site.getID()).getProvinceID());
                    if (!errors.isEmpty()) {
                        masterError.put(StringUtils.isBlank(item.getCode()) ? "empty-code" : item.getCode(), errors);
                    } else {
                        PqmVctRecencyEntity model = getVisit(item, site.getID());
                        vctRecencyService.save(model);
                    }
                }
            } else {
                return new Response<>(false, "EXCEPTION", "Cấu trúc dữ liệu không đúng hoặc không có dữ liệu");
            }

            if (!masterError.isEmpty()) {
                return new Response<>(false, "VALIDATION_ERROR", masterError);
            }

            return new Response<>(true, "SUCCESS", "Thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

    @RequestMapping(value = "/hub/v1/htc/recency.api", method = RequestMethod.DELETE)
    public Response<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("confirmTestNo") String confirmTestNo,
            @RequestParam("earlyHivDate") String earlyHivDate) {
        try {
            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }
            SiteEntity site = siteService.findByElogSiteCode(siteCode);
            if (site == null) {
                return new Response<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmVctRecencyEntity model = vctRecencyService.findByCodeAndSiteIDAndEarlyHivDate(confirmTestNo, site.getID(), TextUtils.convertDate(earlyHivDate, FORMATDATE));
            if (model == null) {
                return new Response<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }
            vctRecencyService.remove(model.getID());
            return new Response<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(PqmHtcApiRecencyForm item1, String provinceID) {
        HashMap<String, String> errors = new HashMap<>();
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");

        //tỉnh
        List<String> provinces = new ArrayList<>();
        for (ProvinceEntity provinceEntity : locationService.findAllProvince()) {
            provinces.add(provinceEntity.getID());
        }
        if (StringUtils.isBlank(item1.getProvinceID())) {
            errors.put("provinceID", "Tỉnh không được để trống");
        } else {
            if (!provinceID.equals(item1.getProvinceID())) {
                errors.put("provinceID", "Tỉnh không đúng với tỉnh của cơ sở nhận API");
            }
        }
//        if (!provinces.contains(item1.getProvinceID())) {
//            errors.put("provinceID", "Mã tỉnh không tồn tại trong hệ thống ");
//        }

        if (StringUtils.isEmpty(item1.getPatientName())) {
            errors.put("patientName", "Tên bệnh nhân không được để trống");
        }
        if (StringUtils.isEmpty(item1.getEarlyHivDate())) {
            errors.put("earlyHivDate", "Ngày xét nghiệm không được để trống");
        }

        if (StringUtils.isNotEmpty(item1.getEarlyHivDate()) && !isThisDateValid(item1.getEarlyHivDate())) {
            errors.put("earlyHivDate", "Ngày xét nghiệm không đúng định dạng");
        }
        if (StringUtils.isEmpty(item1.getEarlyDiagnose())) {
            errors.put("earlyDiagnose", "Kết quả nhiễm mới không được để trống");
        }
        if (StringUtils.isEmpty(item1.getCode())) {
            errors.put("code", "Mã số nơi gửi không được để trống");
        }
        if ((StringUtils.isNotEmpty(item1.getYearOfBirth())) && !item1.getYearOfBirth().matches("[0-9]{0,4}")) {
            errors.put("yearOfBirth", "Năm sinh năm sinh gồm bốn chữ số");
        } else if ((StringUtils.isNotEmpty(item1.getYearOfBirth())) && (Integer.parseInt(item1.getYearOfBirth()) < 1900
                || Integer.parseInt(item1.getYearOfBirth()) > Calendar.getInstance().get(Calendar.YEAR))) {
            errors.put("yearOfBirth", "Năm sinh năm sinh hợp lệ từ 1900 - năm hiện tại");
        }

        if (StringUtils.isNotEmpty(item1.getEarlyHivDate()) && isThisDateValid(item1.getEarlyHivDate())) {
            Date today = new Date();
            Date exchangeTime = TextUtils.convertDate(item1.getEarlyHivDate(), FORMATDATE);
            if (exchangeTime.compareTo(today) > 0) {
                errors.put("earlyHivDate", "Ngày xét nghiệm không sau ngày hiện tại");
            }
        }
        if (StringUtils.isEmpty(item1.getConfirmTestNo())) {
            errors.put("confirmTestNo", "Mã số BN của PXN không được để trống");

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private PqmVctRecencyEntity getVisit(PqmHtcApiRecencyForm item, Long siteID) {
        Date earlyHivDate = TextUtils.convertDate(item.getEarlyHivDate(), FORMATDATE);
        PqmVctRecencyEntity model = vctRecencyService.findByCodeAndSiteIDAndEarlyHivDate(item.getConfirmTestNo(), siteID, earlyHivDate);
        if (model == null) {
            model = new PqmVctRecencyEntity();
        }
        model.setProvinceID(Long.parseLong(item.getProvinceID()));
        model.setCode(item.getCode());
        model.setPatientName(TextUtils.toFullname(item.getPatientName()));
        model.setGenderID(StringUtils.isBlank(item.getGenderID()) ? null : item.getGenderID());
        model.setYearOfBirth(item.getYearOfBirth());
        model.setIdentityCard(item.getIdentityCard());
        model.setObjectGroupID(item.getObjectGroupID());
        model.setAddress(item.getAddress());
        model.setSiteTesting(item.getSiteTesting());
        model.setConfirmTestNo(item.getConfirmTestNo());
        model.setEarlyHivDate(TextUtils.convertDate(item.getEarlyHivDate(), FORMATDATE));
        model.setEarlyDiagnose(item.getEarlyDiagnose());
        model.setSiteID(siteID);

        //site
        List<SiteEntity> siteHtc = siteService.getSiteHtc(item.getProvinceID().toString());
        Set<Long> siteIDs = new HashSet<>();
        for (SiteEntity site : siteHtc) {
            siteIDs.add(site.getID());
        }
        List<PqmVctEntity> vcts = pqmVctService.findByCodeAndSiteIDs(model.getCode(), siteIDs);
        if (vcts != null && !vcts.isEmpty() && vcts.size() == 1) {
            PqmVctEntity vct = vcts.get(0);
            vct.setEarlyHivDate(model.getEarlyHivDate());
            vct.setEarlyDiagnose(model.getEarlyDiagnose());
            pqmVctService.save(vct);
            model.setPqmVctID(vct.getID());
        }

        return model;
    }

}
