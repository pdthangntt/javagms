package com.gms.controller.api.hub;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.pqm_shi_mmd_api.Main;
import com.gms.entity.form.pqm_shi_mmd_api.Mmd;
import com.gms.service.LocationsService;
import com.gms.service.PqmShiArtService;
import com.gms.service.PqmShiMmdService;
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

@RestController(value = "hub_shi_mmd_api")
public class ShiMmdController extends BaseController {

    ;



    @Autowired
    private LocationsService locationService;
    @Autowired
    private PqmShiMmdService shiMmdService;

    @RequestMapping(value = "/hub/v1/shi-mmd.api", method = RequestMethod.POST)
    public Response<?> actionIndex(@RequestParam("secret") String secret, @RequestBody String content) {
        try {
            Gson g = new Gson();
            Main form = g.fromJson(content.trim(), Main.class);

            if (!checksum(secret)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat})");
            }

            HashMap<String, HashMap<String, String>> masterError = new HashMap<>();
            if (form.getDatas() != null && !form.getDatas().isEmpty()) {
                for (Mmd item : form.getDatas()) {
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

                        PqmShiMmdEntity model = shiMmdService.findByProvinceIdAndMonthAndYear(
                                site.getID(),
                                site.getProvinceID(),
                                Integer.parseInt(item.getMonth()),
                                Integer.parseInt(item.getYear()));

                        if (model == null) {
                            model = new PqmShiMmdEntity();
                        }
                        model.setYear(Integer.valueOf(item.getYear()));
                        model.setMonth(Integer.valueOf(item.getMonth()));
                        model.setProvinceID(provinceID);
                        model.setSiteID(siteItem.getID());
                        model.setSiteCode(item.getSiteCode());
                        model.setSiteName(siteItem.getName());

                        model.setZipCode(item.getZipCode());

                        model.setTotalTurnTk(StringUtils.isNotEmpty(item.getTotalTurnTK()) ? Integer.parseInt(item.getTotalTurnTK()) : 0);
                        model.setTotalTurnLk(StringUtils.isNotEmpty(item.getTotalTurnLK()) ? Integer.parseInt(item.getTotalTurnLK()) : 0);
                        model.setTotalPatientTk(StringUtils.isNotEmpty(item.getTotalPatientTK()) ? Integer.parseInt(item.getTotalPatientTK()) : 0);
                        model.setTotalPatientLk(StringUtils.isNotEmpty(item.getTotalPatientLK()) ? Integer.parseInt(item.getTotalPatientLK()) : 0);
                        model.setTotalTurnLess1MonthTk(StringUtils.isNotEmpty(item.getTotalTurnLess1MonthTK()) ? Integer.parseInt(item.getTotalTurnLess1MonthTK()) : 0);
                        model.setTotalTurnLess1MonthLk(StringUtils.isNotEmpty(item.getTotalTurnLess1MonthLK()) ? Integer.parseInt(item.getTotalTurnLess1MonthLK()) : 0);
                        model.setTotalPatientLess1MonthTk(StringUtils.isNotEmpty(item.getTotalPatientLess1MonthTK()) ? Integer.parseInt(item.getTotalPatientLess1MonthTK()) : 0);
                        model.setTotalPatientLess1MonthLk(StringUtils.isNotEmpty(item.getTotalPatientLess1MonthLK()) ? Integer.parseInt(item.getTotalPatientLess1MonthLK()) : 0);
                        model.setTotalPatient1To2MonthTk(StringUtils.isNotEmpty(item.getTotalPatient1To2MonthTK()) ? Integer.parseInt(item.getTotalPatient1To2MonthTK()) : 0);
                        model.setTotalPatient1To2MonthLk(StringUtils.isNotEmpty(item.getTotalPatient1To2MonthLK()) ? Integer.parseInt(item.getTotalPatient1To2MonthLK()) : 0);
                        model.setTotalTurn1To2MonthTk(StringUtils.isNotEmpty(item.getTotalTurn1To2MonthTK()) ? Integer.parseInt(item.getTotalTurn1To2MonthTK()) : 0);
                        model.setTotalTurn1To2MonthLk(StringUtils.isNotEmpty(item.getTotalTurn1To2MonthLK()) ? Integer.parseInt(item.getTotalTurn1To2MonthLK()) : 0);
                        model.setTotalPatient2To3MonthTk(StringUtils.isNotEmpty(item.getTotalPatient2To3MonthTK()) ? Integer.parseInt(item.getTotalPatient2To3MonthTK()) : 0);
                        model.setTotalPatient2To3MonthLk(StringUtils.isNotEmpty(item.getTotalPatient2To3MonthLK()) ? Integer.parseInt(item.getTotalPatient2To3MonthLK()) : 0);
                        model.setTotalTurn2To3MonthTk(StringUtils.isNotEmpty(item.getTotalTurn2To3MonthTK()) ? Integer.parseInt(item.getTotalTurn2To3MonthTK()) : 0);
                        model.setTotalTurn2To3MonthLk(StringUtils.isNotEmpty(item.getTotalTurn2To3MonthLK()) ? Integer.parseInt(item.getTotalTurn2To3MonthLK()) : 0);
                        model.setTotalPatient3MonthTk(StringUtils.isNotEmpty(item.getTotalPatient3MonthTK()) ? Integer.parseInt(item.getTotalPatient3MonthTK()) : 0);
                        model.setTotalPatient3MonthLk(StringUtils.isNotEmpty(item.getTotalPatient3MonthLK()) ? Integer.parseInt(item.getTotalPatient3MonthLK()) : 0);
                        model.setTotalTurn3MonthTk(StringUtils.isNotEmpty(item.getTotalTurn3MonthTK()) ? Integer.parseInt(item.getTotalTurn3MonthTK()) : 0);
                        model.setTotalTurn3MonthLk(StringUtils.isNotEmpty(item.getTotalTurn3MonthLK()) ? Integer.parseInt(item.getTotalTurn3MonthLK()) : 0);
                        model.setRatio3MonthTk(StringUtils.isNotEmpty(item.getRatioPatient3MonthTK()) ? Float.parseFloat(item.getRatioPatient3MonthTK()) : 0);
                        model.setRatio3MonthLk(StringUtils.isNotEmpty(item.getRatioPatient3MonthLK()) ? Float.parseFloat(item.getRatioPatient3MonthLK()) : 0);

                        shiMmdService.save(model);
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

    @RequestMapping(value = "/hub/v1/shi-mmd.api", method = RequestMethod.DELETE)
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

            PqmShiMmdEntity model = shiMmdService.findByProvinceIdAndMonthAndYear(
                    site.getID(),
                    site.getProvinceID(),
                    Integer.valueOf(month),
                    Integer.valueOf(year));
            if (model == null) {
                return new Response<>(false, "EXCEPTION", "Không tìm thấy bản ghi trong hệ thống");
            }

            shiMmdService.deleteBySiteIDAndMonthAndYear(site.getID(), Integer.valueOf(month),
                    Integer.valueOf(year));

            return new Response<>(true, "SUCCESS", "Xóa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }

    }

    private HashMap<String, String> validateCustom(Mmd item) {
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
        if (!checkInteger(item.getTotalTurnTK())) {
            errors.put("totalTurnTK", "Tổng số lượt trong kỳ phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalTurnLK())) {
            errors.put("totalTurnLK", "Tổng số lượt lũy kế phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalPatientTK())) {
            errors.put("totalPatientTK", "Tổng số BN trong kỳ phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalPatientLK())) {
            errors.put("totalPatientLK", "Tổng số BN lũy kế phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalPatientLess1MonthTK())) {
            errors.put("totalPatientLess1MonthTK", "Số BN trong kỳ dưới 1 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalPatientLess1MonthLK())) {
            errors.put("totalPatientLess1MonthLK", "Số BN lũy kế dưới 1 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurnLess1MonthTK())) {
            errors.put("totalTurnLess1MonthTK", "Số lượt trong kỳ dưới 1 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurnLess1MonthLK())) {
            errors.put("totalTurnLess1MonthLK", "Số lượt lũy kế dưới 1 tháng phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalPatient1To2MonthTK())) {
            errors.put("totalPatient1To2MonthTK", "Số BN trong kỳ từ 1-2 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalPatient1To2MonthLK())) {
            errors.put("totalPatient1To2MonthLK", "Số BN lũy kế từ 1-2 tháng phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalTurn1To2MonthTK())) {
            errors.put("totalTurn1To2MonthTK", "Số lượt trong kỳ từ 1-2 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurn1To2MonthLK())) {
            errors.put("totalTurn1To2MonthLK", "Số lượt lũy kế từ 1-2 tháng phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalPatient2To3MonthTK())) {
            errors.put("totalPatient2To3MonthTK", "Số BN trong kỳ từ 2-3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalPatient2To3MonthLK())) {
            errors.put("totalPatient2To3MonthLK", "Số BN lũy kế từ 2-3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurn2To3MonthTK())) {
            errors.put("totalTurn2To3MonthTK", "Số lượt trong kỳ từ 2-3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurn2To3MonthLK())) {
            errors.put("totalTurn2To3MonthLK", "Số lượt lũy kế từ 2-3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalPatient3MonthTK())) {
            errors.put("totalPatient3MonthTK", "Số BN trong 3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalPatient3MonthLK())) {
            errors.put("totalPatient3MonthLK", "Số BN lũy kế trong 3 tháng phải là số nguyên dương ");
        }
        if (!checkInteger(item.getTotalTurn3MonthTK())) {
            errors.put("totalTurn3MonthTK", "Số lượt trong 3 tháng phải là số nguyên dương");
        }
        if (!checkInteger(item.getTotalTurn3MonthLK())) {
            errors.put("totalTurn3MonthLK", "Số lượt lũy kế trong 3 tháng phải là số nguyên dương ");
        }

        if (!checkFloat(item.getRatioPatient3MonthTK())) {
            errors.put("ratioPatient3MonthTK", "Tỷ lệ BN cấp thuốc trong 3 tháng phải là số dương ");
        }
        if (!checkFloat(item.getRatioPatient3MonthLK())) {
            errors.put("ratioPatient3MonthLK", "Tỷ lệ BN cấp thuốc lũy kế trong 3 tháng phải là số dương");
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

    private boolean checkFloat(String value) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        try {
            float v = Float.valueOf(value);
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
