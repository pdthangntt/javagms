package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.pqm_shi_art_api.Art;
import com.gms.entity.form.pqm_shi_art_api.Main;
import com.gms.service.LocationsService;
import com.gms.service.PqmShiArtService;
import com.google.gson.Gson;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "hub_shi_art_api")
public class ShiArtController extends BaseController {

    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmShiArtService pqmService;

    @RequestMapping(value = "/hub/v1/shi-art.api", method = RequestMethod.POST)
    public Response<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            Main form = g.fromJson(content.trim(), Main.class);

            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (Art item : form.getDatas()) {
                    SiteEntity site = siteService.findByArvSiteCode(item.getSiteCode());
                    if (site == null) {
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put("siteCode", "Mã cơ sở " + item.getSiteCode() + " chưa được cấu hình");
                        masterError.put(item.getMonth()
                                + "-" + item.getYear()
                                + "-" + item.getSiteCode(),
                                errors);
                        continue;
                    }

                    HashMap<String, String> errors = validateCustom(item);
                    if (!errors.isEmpty()) {
                        masterError.put(item.getMonth()
                                + "-" + item.getYear()
                                + "-" + item.getSiteCode(),
                                errors);
                    } else {
                        SiteEntity siteItem = siteService.findOne(site.getID());
                        String provinceID = siteService.findOne(site.getID()).getProvinceID();
                        PqmShiArtEntity model = pqmService.findByProvinceIdAndMonthAndYearAndSiteID(
                                site.getID(),
                                site.getProvinceID(),
                                Integer.valueOf(item.getMonth()),
                                Integer.valueOf(item.getYear()));

                        if (model == null) {
                            model = new PqmShiArtEntity();
                        }
                        model.setYear(Integer.valueOf(item.getYear()));
                        model.setMonth(Integer.valueOf(item.getMonth()));
                        model.setProvinceID(provinceID);
                        model.setSiteID(siteItem.getID());
                        model.setSiteCode(item.getSiteCode());
                        model.setSiteName(siteItem.getName());

                        model.setZipCode(item.getZipCode());

                        model.setBnnt(StringUtils.isEmpty(item.getBnnt()) ? 0 : Integer.valueOf(item.getBnnt()));
                        model.setBnm(StringUtils.isEmpty(item.getBnm()) ? 0 : Integer.valueOf(item.getBnm()));
                        model.setBnqldt(StringUtils.isEmpty(item.getBnqldt()) ? 0 : Integer.valueOf(item.getBnqldt()));
                        model.setBndt12t(StringUtils.isEmpty(item.getBndt12t()) ? 0 : Integer.valueOf(item.getBndt12t()));
                        model.setBnnhtk(StringUtils.isEmpty(item.getBnnhtk()) ? 0 : Integer.valueOf(item.getBnnhtk()));
                        model.setTlbnnhtk(StringUtils.isEmpty(item.getTlbnnhtk()) ? Double.valueOf("0") : Double.valueOf(item.getTlbnnhtk()));
                        model.setBnbttk(StringUtils.isEmpty(item.getBnbttk()) ? 0 : Integer.valueOf(item.getBnbttk()));
                        model.setBnbtlk(StringUtils.isEmpty(item.getBnbtlk()) ? 0 : Integer.valueOf(item.getBnbtlk()));

                        pqmService.save(model);
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

    @RequestMapping(value = "/hub/v1/shi-art.api", method = RequestMethod.DELETE)
    public Response<?> actionDel(@RequestParam("secret") String secret,
            @RequestParam("year") String year,
            @RequestParam("month") String month,
            @RequestParam("siteCode") String siteCode
    ) {
        try {
            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            SiteEntity site = siteService.findByArvSiteCode(siteCode);
            if (site == null) {
                return new Response<>(false, "EXCEPTION", "Mã cơ sở " + siteCode + " chưa được cấu hình");
            }

            PqmShiArtEntity model = pqmService.findByProvinceIdAndMonthAndYearAndSiteID(
                    site.getID(),
                    site.getProvinceID(),
                    Integer.valueOf(month),
                    Integer.valueOf(year));
            if (model == null) {
                return new Response<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            pqmService.deleteBySiteIDAndMonthAndYear(site.getID(), Integer.valueOf(month),
                    Integer.valueOf(year));

            return new Response<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(Art item) {
        HashMap<String, String> errors = new HashMap<>();

        //tháng năm
        if (StringUtils.isBlank(item.getMonth())) {
            errors.put("month", "Tháng không được để trống");
        }
        if (StringUtils.isBlank(item.getYear())) {
            errors.put("year", "Năm không được để trống");
        }

        if (!StringUtils.isBlank(item.getYear()) && !StringUtils.isBlank(item.getMonth())) {
            try {
                int year = Integer.valueOf(item.getYear());
                int month = Integer.valueOf(item.getMonth());
                if (year < 1000 || year > 9999) {
                    errors.put("year", "Năm nằm trong khoảng 1000 - 9999");
                }
                if (month < 1 || month > 12) {
                    errors.put("month", "Tháng nằm trong khoảng 1 - 12");
                }

                int currentYear = Integer.valueOf(TextUtils.formatDate(new Date(), "yyyy"));
                int currentQuater = Integer.valueOf(TextUtils.formatDate(new Date(), "MM"));

                int currentTime = Integer.valueOf(currentYear + "" + currentQuater);
                int reportTime = Integer.valueOf(year + "" + month);

                if (reportTime > currentTime) {
                    errors.put("month-year", "Thời gian báo cáo không được lớn hơn tháng hiện tại");
                }

            } catch (Exception e) {
                errors.put("month-year", "Tháng/Năm không đúng");
            }

        }

        if (StringUtils.isEmpty(item.getSiteCode())) {
            errors.put("siteCode", "Mã CSKCB không được để trống");
        }

        if (!checkInteger(item.getBnnt())) {
            errors.put("bnnt", "Số BN hiện nhận thuốc phải là số nguyên dương");
        }
        if (!checkInteger(item.getBnm())) {
            errors.put("bnm", "Số BN mới phải là số nguyên dương");
        }
        if (!checkInteger(item.getBnqldt())) {
            errors.put("bnqldt", "Số BN quay lại điều trị phải là số nguyên dương");
        }
        if (!checkInteger(item.getBndt12t())) {
            errors.put("bndt12t", "Số BN điều trị trên 12 tháng phải là số nguyên dương");
        }
        if (!checkInteger(item.getBnnhtk())) {
            errors.put("bnnhtk", "Số BN nhỡ hẹn tái khám phải là số nguyên dương");
        }
        if (!checkLong(item.getTlbnnhtk())) {
            errors.put("tlbnnhtk", "Tỷ lệ BN nhỡ hẹn tái khám phải là số dương");
        }
        if (!checkInteger(item.getBnbttk())) {
            errors.put("bnbttk", "Số BN bỏ trị trong kỳ phải là số nguyên dương");
        }
        if (!checkInteger(item.getBnbtlk())) {
            errors.put("bnbtlk", "Số BN bỏ trị lũy kế phải là số nguyên dương");
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
            double v = Double.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
