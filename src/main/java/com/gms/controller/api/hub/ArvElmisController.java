package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.pqm_elmis_api.Elmis;
import com.gms.entity.form.pqm_elmis_api.Main;
import com.gms.service.LocationsService;
import com.gms.service.PqmDrugNewService;
import com.gms.service.PqmDrugeLMISEService;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_elmis_arv_api")
public class ArvElmisController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmDrugeLMISEService drugeLMISEService;

    @RequestMapping(value = "/hub/v1/arv-elmis.api", method = RequestMethod.POST)
    public Response<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            Main form = g.fromJson(content.trim(), Main.class);

            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (Elmis item : form.getDatas()) {
                    SiteEntity site = siteService.findByArvSiteCode(item.getSiteCode());
                    if (site == null) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(item.getQuarter()
                                + "-" + item.getYear()
                                + "-" + item.getDrugName()
                                + "-" + item.getDrugCode()
                                + "-" + item.getTtThau(),
                                errors);
                        continue;
                    }

                    HashMap<String, String> errors = validateCustom(item);
                    if (!errors.isEmpty()) {
                        masterError.put(item.getQuarter()
                                + "-" + item.getYear()
                                + "-" + item.getDrugName()
                                + "-" + item.getDrugCode()
                                + "-" + item.getTtThau(),
                                errors);
                    } else {
                        SiteEntity siteItem = siteService.findOne(site.getID());
                        String provinceID = siteService.findOne(site.getID()).getProvinceID();
                        PqmDrugeLMISEEntity model = drugeLMISEService.findByUniqueConstraints(
                                Integer.valueOf(item.getYear()),
                                Integer.valueOf(item.getQuarter()),
                                provinceID,
                                item.getSiteCode(),
                                item.getDrugName(),
                                item.getDrugCode(),
                                item.getTtThau()
                        );

                        if (model == null) {
                            model = new PqmDrugeLMISEEntity();
                        }
                        model.setYear(Integer.valueOf(item.getYear()));
                        model.setQuarter(Integer.valueOf(item.getQuarter()));
                        model.setProvinceID(provinceID);
                        model.setSiteID(siteItem.getID());
                        model.setSiteCode(item.getSiteCode());
                        model.setSiteName(item.getSiteName());
                        model.setDrugCode(item.getDrugCode());
                        model.setDrugName(item.getDrugName());
                        model.setSoDangKy(item.getSoDangKy());
                        model.setTtThau(item.getTtThau());
                        model.setDuTru(StringUtils.isBlank(item.getDuTru()) ? Long.valueOf("0") : Long.valueOf(item.getDuTru()));
                        model.setTongSuDung(StringUtils.isBlank(item.getTongSuDung()) ? Long.valueOf("0") : Long.valueOf(item.getTongSuDung()));
                        model.setTonKho(StringUtils.isBlank(item.getTonKho()) ? Long.valueOf("0") : Long.valueOf(item.getTonKho()));

                        drugeLMISEService.save(model);
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

    @RequestMapping(value = "/hub/v1/arv-elmis.api", method = RequestMethod.DELETE)
    public Response<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("year") String year,
            @RequestParam("quarter") String quarter,
            @RequestParam("siteCode") String siteCode,
            @RequestParam("drugName") String drugName,
            @RequestParam("drugCode") String drugCode,
            @RequestParam("ttThau") String ttThau
    ) {
        try {
            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByArvSiteCode(siteCode);
            if (site == null) {
                return new Response<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmDrugeLMISEEntity model = drugeLMISEService.findByUniqueConstraints(
                    Integer.valueOf(year),
                    Integer.valueOf(quarter),
                    site.getProvinceID(),
                    siteCode,
                    drugName,
                    drugCode,
                    ttThau
            );
            if (model == null) {
                return new Response<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            drugeLMISEService.deleteById(model.getID());

            return new Response<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(Elmis item) {
        HashMap<String, String> errors = new HashMap<>();

        //tháng năm
        if (StringUtils.isBlank(item.getQuarter())) {
            errors.put("quarter", "Quý không được để trống");
        }
        if (StringUtils.isBlank(item.getYear())) {
            errors.put("year", "Năm không được để trống");
        }

        if (!StringUtils.isBlank(item.getYear()) && !StringUtils.isBlank(item.getQuarter())) {
            try {
                int year = Integer.valueOf(item.getYear());
                int quarter = Integer.valueOf(item.getQuarter());
                if (year < 1000 || year > 9999) {
                    errors.put("year", "Năm nằm trong khoảng 1000 - 9999");
                }
                if (quarter < 1 || quarter > 4) {
                    errors.put("quarter", "Quý nằm trong khoảng 1 - 4");
                }

                int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
                int currentQuater = TextUtils.getQuarter(new Date()) + 1;

                int currentTime = Integer.valueOf(currentYear + "" + currentQuater);
                int reportTime = Integer.valueOf(year + "" + quarter);
                if (reportTime > currentTime) {
                    errors.put("quarter-year", "Thời gian báo cáo không được lớn hơn quý hiện tại");
                }

            } catch (Exception e) {
                errors.put("quarter-year", "Quý/Năm không đúng");
            }

        }

        if (StringUtils.isEmpty(item.getDrugCode())) {
            errors.put("drugCode", "Mã thuốc không được để trống");
        }
        if (StringUtils.isEmpty(item.getDrugName())) {
            errors.put("drugName", "Tên thuốc không được để trống");
        }
        if (StringUtils.isEmpty(item.getSoDangKy())) {
            errors.put("soDangKy", "Số đăng ký không được để trống");
        }
        if (StringUtils.isEmpty(item.getTtThau())) {
            errors.put("ttThau", "Quyết định thầu không được để trống");
        }

        if (!checkInteger(item.getDuTru())) {
            errors.put("duTru", "Số lượng dự trù phải là số nguyên dương");
        }
        if (!checkInteger(item.getTongSuDung())) {
            errors.put("tongSuDung", "Số lượng sử dụng phải là số nguyên dương");
        }

        if (!checkLong(item.getTonKho())) {
            errors.put("tonKho", "Số lượng tồn kho phải là số nguyên");
        }
        return errors;
    }

    private boolean checkInteger(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        try {
            int v = Integer.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean checkLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        try {
            double v = Long.valueOf(value);
        } catch (Exception e) {
            return false;
        }
        return true;
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
